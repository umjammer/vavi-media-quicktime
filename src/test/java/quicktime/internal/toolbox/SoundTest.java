/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.OpenFile;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.sound.SPB;
import quicktime.sound.SPBDevice;
import quicktime.sound.SndChannel;
import quicktime.sound.SndHandle;
import quicktime.sound.SndInfo;
import quicktime.sound.SoundException;
import quicktime.std.movies.Movie;
import quicktime.std.movies.media.SoundDescription;
import quicktime.util.QTHandle;
import quicktime.util.QTPointer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SoundTest {

    static final String MEDIA = "src/test/resources/media/";

    @TempDir
    Path temp;

    /** captures what is played */
    static final class Capture implements SoundPlayer.Sink {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        @Override
        public synchronized void write(byte[] b, int length) {
            bytes.write(b, 0, length);
            try {
                // as fast as twice the real time
                Thread.sleep((long) (length / 4 / SoundPlayer.RATE * 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public double latency() {
            return 0;
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        synchronized short[] left() {
            byte[] b = bytes.toByteArray();
            short[] s = new short[b.length / 4];
            for (int i = 0; i < s.length; i++) {
                s[i] = (short) ((b[i * 4] & 0xff) | (b[i * 4 + 1] << 8));
            }
            return s;
        }
    }

    Supplier<SoundPlayer.Sink> savedSinks;
    Function<AudioFormat, TargetDataLine> savedSources;
    Capture capture;

    @BeforeEach
    void setUp() throws Exception {
        QTSession.open();
        savedSinks = SoundPlayer.sinks;
        savedSources = SoundManager.sources;
        capture = new Capture();
        SoundPlayer.sinks = () -> capture;
    }

    @AfterEach
    void tearDown() {
        QTSession.close();
        SoundPlayer.sinks = savedSinks;
        SoundManager.sources = savedSources;
    }

    /** @return zero crossings per second of the samples at 44.1 kHz, of the non silent part */
    static double crossings(short[] s) {
        int first = 0, last = s.length - 1;
        while (first < s.length && Math.abs(s[first]) < 100) {
            first++;
        }
        while (last > first && Math.abs(s[last]) < 100) {
            last--;
        }
        int n = 0;
        for (int i = first + 1; i <= last; i++) {
            if ((s[i - 1] < 0) != (s[i] < 0)) {
                n++;
            }
        }
        return n / ((last - first) / (double) SoundPlayer.RATE);
    }

    @Test
    void testCodecs() {
        assertEquals(0, Audio.ulaw((byte) 0xff));
        assertEquals(-32124, Audio.ulaw((byte) 0x00));
        assertEquals(8, Audio.alaw((byte) 0xd5));
        for (int v : new int[] {0, 1000, -1000, 30000, -30000}) {
            int back = Audio.ulaw(Audio.toUlaw(v));
            assertTrue(Math.abs(back - v) <= Math.max(8, Math.abs(v) / 16), v + " -> " + back);
        }
        // an IMA4 packet of zero nibbles decays from the predictor
        byte[] packet = new byte[34];
        Audio.Ima4 ima = new Audio.Ima4(1, 44100);
        assertEquals(64, ima.decode(packet).length);
    }

    @Test
    void testMovieSound() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "sin440.aif")));
        MovieRec m = Heap.peer(QTObject.ID(movie), MovieRec.class);
        // what is mixed is the sine
        byte[] out = new byte[SoundPlayer.CHUNK * 4];
        SoundPlayer.mix(m, 0.25, 1, out);
        short[] s = new short[SoundPlayer.CHUNK];
        int peak = 0;
        for (int i = 0; i < s.length; i++) {
            s[i] = (short) ((out[i * 4] & 0xff) | (out[i * 4 + 1] << 8));
            peak = Math.max(peak, Math.abs(s[i]));
        }
        assertTrue(peak > 10000, "peak " + peak);
        assertEquals(880, crossings(s), 120);

        movie.start();
        Thread.sleep(400);
        assertTrue(SoundPlayer.isPlaying(m));
        movie.stop();
        Thread.sleep(100);
        assertTrue(!SoundPlayer.isPlaying(m));
        short[] played = capture.left();
        assertTrue(played.length > SoundPlayer.RATE / 10, "played " + played.length);
        assertEquals(880, crossings(played), 120);
    }

    @Test
    void testExportSound() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "sin440.aif")));
        File wav = temp.resolve("sin.wav").toFile();
        movie.convertToFile(new QTFile(wav), quicktime.std.StdQTConstants.kQTFileTypeWave, quicktime.std.StdQTConstants.kMoviePlayer, 0, 0);
        assertTrue(wav.length() > 22050);
    }

    /** @return a sound handle of a 16 bit mono sine */
    static SndHandle sine(double frequency, int frames) throws Exception {
        SndHandle h = new SndHandle(1, 22050f, 16, quicktime.sound.SoundConstants.k16BitBigEndianFormat);
        byte[] data = new byte[frames * 2];
        for (int i = 0; i < frames; i++) {
            int v = (int) (Math.sin(2 * Math.PI * frequency * i / 22050) * 20000);
            data[i * 2] = (byte) (v >> 8);
            data[i * 2 + 1] = (byte) v;
        }
        h.appendSoundData(new QTPointer(data));
        return h;
    }

    @Test
    void testChannel() throws Exception {
        SndHandle h = sine(1000, 22050 / 4);
        SndInfo info = h.parseSndHeader();
        assertEquals(16, info.sndData.getSampleSize());
        SndChannel c = new SndChannel();
        c.play(h);
        long until = System.currentTimeMillis() + 3000;
        while (!c.isFinished() && System.currentTimeMillis() < until) {
            Thread.sleep(20);
        }
        assertTrue(c.isFinished());
        short[] s = capture.left();
        assertEquals(SoundPlayer.RATE / 4, s.length, SoundPlayer.CHUNK * 2);
        assertEquals(2000, crossings(s), 150);
    }

    @Test
    void testAIFFHeader() throws Exception {
        File f = temp.resolve("header.aif").toFile();
        QTFile file = new QTFile(f);
        file.createMovieFile(0, 0);
        OpenFile o = OpenFile.asWrite(file);
        SndInfo.setupAIFFHeader(o, 2, 44100f, 16, quicktime.sound.SoundConstants.kSoundNotCompressed, 4000, 1000);
        o.close();
        OpenFile in = OpenFile.asRead(file);
        SndInfo info = SndInfo.parseAIFFHeader(in);
        in.close();
        assertEquals(1000, info.numFrames);
        assertEquals(2, info.sndData.getNumChannels());
        assertEquals(44100f, info.sndData.getSampleRate(), 0.01);
        assertEquals(54, info.dataOffset);
    }

    @Test
    void testDescriptionExtension() throws Exception {
        SoundDescription d = new SoundDescription(quicktime.sound.SoundConstants.k16BitBigEndianFormat);
        d.addExtension(new QTHandle(new byte[] {9, 8, 7}), 0x74657374);
        assertArrayEquals(new byte[] {9, 8, 7}, d.getExtension(0x74657374).getBytes());
        d.removeExtension(0x74657374);
    }

    @Test
    void testRecord() throws Exception {
        // a sine for an input device
        SoundManager.sources = format -> (TargetDataLine) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] {TargetDataLine.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "read" -> {
                        byte[] b = (byte[]) args[0];
                        int off = (Integer) args[1], len = (Integer) args[2];
                        for (int i = 0; i + 1 < len; i += 2) {
                            b[off + i] = 0x40;
                            b[off + i + 1] = 0;
                        }
                        yield len - len % 2;
                    }
                    case "isOpen", "isActive", "isRunning" -> false;
                    case "getFormat" -> format;
                    case "hashCode" -> 0;
                    case "equals" -> proxy == args[0];
                    default -> null;
                });
        SPBDevice device = new SPBDevice(null, 1);
        assertNotNull(device.getName());
        device.setSampleRate(22050f);
        device.setNumberChannels(1);
        assertEquals(22050f, device.getSampleRate(), 0.01);
        QTPointer buffer = new QTPointer(4410, true);
        SPB spb = new SPB(device, 0, 100, buffer);
        spb.record(false);
        assertEquals(4410, spb.getCount());
        assertEquals(0x40, buffer.getBytes()[0]);
    }
}
