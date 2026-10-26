/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


/**
 * Plays the sound tracks of movies with Java Sound, following their time bases.
 * <p>
 * The enabled sound tracks a Java decoder of {@link Audio} can read are mixed to 16 bit stereo at 44.1 kHz with
 * the movie and track volumes and the media balance. The time base is the master, the sound is moved to it when they drift apart.
 */
final class SoundPlayer {

    private static final Logger logger = Logger.getLogger(SoundPlayer.class.getName());

    private SoundPlayer() {
    }

    static final float RATE = 44100f;
    static final int CHANNELS = 2;
    /** frames rendered at once */
    static final int CHUNK = 1024;
    /** seconds the sound may drift from the time base */
    static final double TOLERANCE = 0.2;

    /** where the mixed sound goes */
    interface Sink {
        /** writes 16 bit signed little endian stereo frames, blocking while the output is full */
        void write(byte[] b, int length);

        /** @return seconds written but not played yet */
        double latency();

        void flush();

        void close();
    }

    /** Java Sound output */
    static final class LineSink implements Sink {
        final SourceDataLine line;

        LineSink() throws LineUnavailableException {
            AudioFormat format = new AudioFormat(RATE, 16, CHANNELS, true, false);
            line = AudioSystem.getSourceDataLine(format);
            line.open(format, (int) (RATE * 4 * 0.25));
            line.start();
        }

        @Override
        public void write(byte[] b, int length) {
            line.write(b, 0, length);
        }

        @Override
        public double latency() {
            return (line.getBufferSize() - line.available()) / (RATE * 4);
        }

        @Override
        public void flush() {
            line.flush();
        }

        @Override
        public void close() {
            line.drain();
            line.close();
        }
    }

    private static boolean warned;

    /** makes the output, for tests to capture it */
    static Supplier<Sink> sinks = () -> {
        try {
            return new LineSink();
        } catch (LineUnavailableException | IllegalArgumentException | SecurityException e) {
            if (!warned) {
                warned = true;
                logger.log(Level.INFO, "no sound output: " + e);
            }
            return null;
        }
    };

    private static final Map<MovieRec, Player> players = new HashMap<>();

    /** starts, stops or moves the sound of the movie after its rate, time or volume changed */
    static synchronized void sync(MovieRec m) {
        Player p = players.get(m);
        boolean play = m.active && m.timeBase.effectiveRate() > 0 && m.volume > 0 && playable(m);
        if (play && p == null) {
            p = new Player(m);
            players.put(m, p);
            p.start();
        } else if (!play && p != null) {
            players.remove(m);
            p.stop();
        } else if (p != null) {
            p.resync = true;
        }
    }

    static synchronized void dispose(MovieRec m) {
        Player p = players.remove(m);
        if (p != null) {
            p.stop();
        }
    }

    static synchronized void stopAll() {
        for (Player p : players.values()) {
            p.stop();
        }
        players.clear();
    }

    static synchronized boolean isPlaying(MovieRec m) {
        return players.containsKey(m);
    }

    /** @return true if a sound track can be decoded */
    static boolean playable(MovieRec m) {
        for (TrackRec t : m.tracks) {
            if (t.enabled && t.media != null && t.media.isSound()) {
                for (byte[] d : t.media.descriptions) {
                    if (Audio.decoder(d) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** the sound of a playing movie */
    static final class Player implements Runnable {
        final MovieRec m;
        volatile boolean running = true;
        volatile boolean resync;
        Thread thread;

        Player(MovieRec m) {
            this.m = m;
        }

        void start() {
            thread = new Thread(this, "QuickTime sound");
            thread.setDaemon(true);
            thread.start();
        }

        void stop() {
            running = false;
            if (thread != null) {
                thread.interrupt();
            }
        }

        @Override
        public void run() {
            Sink sink = sinks.get();
            if (sink == null) {
                return;
            }
            try {
                double pos = seconds();
                byte[] out = new byte[CHUNK * CHANNELS * 2];
                while (running && !Thread.currentThread().isInterrupted()) {
                    double rate = m.timeBase.effectiveRate() / 65536.0;
                    if (rate <= 0) {
                        break;
                    }
                    double now = seconds();
                    double heard = pos - sink.latency() * rate;
                    if (resync || Math.abs(heard - now) > TOLERANCE) {
                        resync = false;
                        sink.flush();
                        pos = now;
                    }
                    double duration = m.duration() / (double) m.timeScale;
                    if (pos >= duration) {
                        Thread.sleep(20);
                        continue;
                    }
                    mix(m, pos, rate, out);
                    sink.write(out, out.length);
                    pos += CHUNK / RATE * rate;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException | RuntimeException e) {
                logger.log(Level.FINE, "sound", e);
            } finally {
                sink.close();
            }
        }

        double seconds() {
            return m.time() / (double) m.timeScale;
        }
    }

    /** mixes CHUNK frames of the movie from the time in seconds at the rate into 16 bit stereo */
    static void mix(MovieRec m, double seconds, double rate, byte[] out) throws IOException {
        float[] left = new float[CHUNK];
        float[] right = new float[CHUNK];
        float movieVolume = Math.max(0, m.volume) / 256f;
        for (TrackRec t : m.tracks) {
            if (!t.enabled || t.media == null || !t.media.isSound() || t.volume <= 0) {
                continue;
            }
            float[] s = render(t, seconds, rate);
            if (s == null) {
                continue;
            }
            float v = movieVolume * t.volume / 256f;
            float balance = Math.max(-1, Math.min(1, t.media.balance / 256f));
            float gl = v * Math.min(1, 1 - balance), gr = v * Math.min(1, 1 + balance);
            for (int i = 0; i < CHUNK; i++) {
                left[i] += s[i * 2] * gl;
                right[i] += s[i * 2 + 1] * gr;
            }
        }
        for (int i = 0; i < CHUNK; i++) {
            put(out, i * 4, left[i]);
            put(out, i * 4 + 2, right[i]);
        }
    }

    private static void put(byte[] b, int o, float v) {
        int s = Math.max(-32768, Math.min(32767, Math.round(v * 32767)));
        b[o] = (byte) s;
        b[o + 1] = (byte) (s >> 8);
    }

    /** @return CHUNK stereo frames of the track, resampled to the output rate, null if silent */
    static float[] render(TrackRec t, double seconds, double rate) throws IOException {
        long[] start = new long[1];
        long movieTime = (long) Math.floor(seconds * t.movie.timeScale);
        TrackRec.Edit e = t.editAt(movieTime, start);
        if (e == null || e.isEmpty() || e.rate <= 0) {
            return null;
        }
        MediaRec md = t.media;
        double editRate = e.rate / 65536.0;
        double mediaPos = e.mediaTime + (seconds - start[0] / (double) t.movie.timeScale) * md.timeScale * editRate;
        // media frames per output frame
        double step = md.timeScale * editRate * rate / RATE;
        long first = (long) Math.floor(mediaPos);
        int frames = (int) Math.ceil(step * CHUNK) + 2;
        float[] in = Audio.read(md, first, frames, CHANNELS);
        if (in == null) {
            return null;
        }
        float[] out = new float[CHUNK * CHANNELS];
        double p = mediaPos - first;
        for (int i = 0; i < CHUNK; i++, p += step) {
            int k = (int) p;
            float f = (float) (p - k);
            if (k + 1 >= frames) {
                break;
            }
            for (int c = 0; c < CHANNELS; c++) {
                out[i * CHANNELS + c] = in[k * CHANNELS + c] * (1 - f) + in[(k + 1) * CHANNELS + c] * f;
            }
        }
        return out;
    }
}
