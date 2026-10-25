/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Decodes, renders and plays the media of movies.
 * <p>
 * The movie model is pure Java, the codecs are the platform's. On macOS this is AVFoundation
 * ({@code quicktime.internal.avf.AVFMediaEngine}), elsewhere media can be edited, read and written but not decoded.
 */
public abstract class MediaEngine {

    private static final Logger logger = Logger.getLogger(MediaEngine.class.getName());

    /** system property to choose the engine class */
    public static final String PROPERTY = "quicktime.mediaEngine";

    private static MediaEngine engine;

    /** @return the media engine of this platform */
    public static synchronized MediaEngine get() {
        if (engine == null) {
            String name = System.getProperty(PROPERTY);
            if (name == null && System.getProperty("os.name", "").startsWith("Mac")) {
                name = "quicktime.internal.avf.AVFMediaEngine";
            }
            if (name != null) {
                try {
                    engine = (MediaEngine) Class.forName(name).getDeclaredConstructor().newInstance();
                } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
                    logger.log(Level.WARNING, "media engine " + name + " is not available", e);
                }
            }
            if (engine == null) {
                engine = new NullEngine();
            }
        }
        return engine;
    }

    /** for tests */
    static synchronized void set(MediaEngine e) {
        engine = e;
    }

    /** @return true if media can be decoded */
    public abstract boolean isAvailable();

    /** called from MoviesTask, plays and draws the movie following its time base */
    public abstract void task(MovieRec movie);

    /** the time, rate or volume of the movie changed */
    public abstract void sync(MovieRec movie);

    /** the movie needs to be redrawn */
    public abstract void invalidate(MovieRec movie);

    /** the movie is disposed */
    public abstract void dispose(MovieRec movie);

    /** @return the image of the movie at the movie time, null if there is no visual media or it cannot be decoded */
    public abstract BufferedImage frame(MovieRec movie, long time) throws IOException;

    /** @return the image of the track at the movie time */
    public abstract BufferedImage frame(TrackRec track, long time) throws IOException;

    /**
     * Exports the movie with the platform's exporters.
     * @param fileType the QuickTime file type ('mpg4', '3gpp', 'M4V ', 'AIFF', 'WAVE', ...)
     * @return an OSErr
     */
    public abstract int export(MovieRec movie, String path, int fileType, MovieFiles.Progress progress) throws IOException;

    /**
     * Imports a file that is not a movie file, by converting it to a QuickTime movie.
     * @return the movie, null if the file cannot be imported
     */
    public abstract MovieRec importFile(String path) throws IOException;

    /** @return a PicHandle of the movie at the time, 0 if not available */
    int pict(MovieRec movie, long time) {
        try {
            BufferedImage image = frame(movie, time);
            return image == null ? 0 : QuickDrawManager.newPict(image);
        } catch (IOException e) {
            logger.log(Level.FINE, e.getMessage(), e);
            return 0;
        }
    }

    /** @return a PicHandle of the track at the time, 0 if not available */
    int pict(TrackRec track, long time) {
        try {
            BufferedImage image = frame(track, time);
            return image == null ? 0 : QuickDrawManager.newPict(image);
        } catch (IOException e) {
            logger.log(Level.FINE, e.getMessage(), e);
            return 0;
        }
    }

    /** media are not decoded, the time base runs without output */
    static class NullEngine extends MediaEngine {

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public void task(MovieRec movie) {
            MovieDrawing.task(movie, this);
        }

        @Override
        public void sync(MovieRec movie) {
        }

        @Override
        public void invalidate(MovieRec movie) {
            MovieDrawing.invalidate(movie);
        }

        @Override
        public void dispose(MovieRec movie) {
        }

        @Override
        public BufferedImage frame(MovieRec movie, long time) {
            return null;
        }

        @Override
        public BufferedImage frame(TrackRec track, long time) {
            return null;
        }

        @Override
        public int export(MovieRec movie, String path, int fileType, MovieFiles.Progress progress) {
            return MovieToolbox.featureUnsupported;
        }

        @Override
        public MovieRec importFile(String path) {
            return null;
        }
    }
}
