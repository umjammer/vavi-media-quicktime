/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * What a {@link MediaEngine} sees of a movie, the movie objects are opaque.
 */
public final class MovieAccess {

    private MovieAccess() {
    }

    public static final int featureUnsupported = MovieToolbox.featureUnsupported;

    /** a file holding the current version of a movie */
    private record Playable(int key, String path, boolean temporary) {
    }

    private static final Map<MovieRec, Playable> playables = new HashMap<>();

    private static MovieRec movie(Object movie) {
        return (MovieRec) movie;
    }

    /**
     * @return a self contained QuickTime movie file of the current version of the movie,
     *         the file it was read from when it is unchanged
     */
    public static synchronized String playable(Object movie) throws IOException {
        MovieRec m = movie(movie);
        int key = Arrays.hashCode(MovieAtoms.write(m));
        Playable p = playables.get(m);
        if (p != null && p.key == key && new File(p.path).exists()) {
            return p.path;
        }
        forget(m);
        String path;
        boolean temporary = false;
        if (!m.changed && m.path != null && new File(m.path).exists() && !MdatWriter.isOpen(m.path)) {
            path = m.path;
        } else {
            File temp = File.createTempFile("qtplay", ".mov");
            temp.deleteOnExit();
            MovieFiles.flatten(m, temp.getPath(), MovieFiles.flattenForceMovieResourceBeforeMovieData, null, MovieFiles.NO_PROGRESS).dispose();
            path = temp.getPath();
            temporary = true;
        }
        playables.put(m, new Playable(key, path, temporary));
        return path;
    }

    /** removes the playable file of the movie */
    public static synchronized void forget(Object movie) {
        Playable p = playables.remove(movie(movie));
        if (p != null && p.temporary) {
            new File(p.path).delete();
        }
    }

    /** @return the frame decoded in Java, for still image codecs, null if not decodable */
    public static java.awt.image.BufferedImage javaFrame(Object movie, long time) throws IOException {
        return JavaFrames.frame(movie(movie), time);
    }

    /** draws the movie into its graphics world when its time changed */
    public static void draw(Object movie, MediaEngine engine) {
        MovieDrawing.task(movie(movie), engine);
    }

    public static void invalidate(Object movie) {
        MovieDrawing.invalidate(movie(movie));
    }

    /** @return the current movie time in seconds */
    public static double seconds(Object movie) {
        MovieRec m = movie(movie);
        return m.time() / (double) m.timeScale;
    }

    /** @return the movie time in seconds */
    public static double seconds(Object movie, long time) {
        return time / (double) movie(movie).timeScale;
    }

    /** @return the effective rate */
    public static float rate(Object movie) {
        return movie(movie).timeBase.effectiveRate() / 65536f;
    }

    public static boolean isActive(Object movie) {
        return movie(movie).active;
    }

    /** @return the volume, 1 is full */
    public static float volume(Object movie) {
        return Math.max(0, movie(movie).volume) / 256f;
    }

    public static boolean hasSound(Object movie) {
        for (TrackRec t : movie(movie).tracks) {
            if (t.enabled && t.media != null && t.media.isSound()) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasVisual(Object movie) {
        for (TrackRec t : movie(movie).tracks) {
            if (t.enabled && t.media != null && t.media.isVisual()) {
                return true;
            }
        }
        return false;
    }

    /** the session ends: sound stops */
    public static void terminate() {
        SoundPlayer.stopAll();
        SoundManager.disposeAll();
    }
}
