/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.Pict;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.media.UserData;
import quicktime.std.sg.SGDeviceList;
import quicktime.std.sg.SGSoundChannel;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;
import quicktime.std.sg.VideoDigitizer;
import quicktime.util.RawEncodedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SequenceGrabberTest implements StdQTConstants {

    @TempDir
    Path temp;

    Function<AudioFormat, TargetDataLine> savedSources;

    /** a sound input of a constant level */
    static TargetDataLine line(AudioFormat format) {
        return (TargetDataLine) Proxy.newProxyInstance(SequenceGrabberTest.class.getClassLoader(), new Class<?>[] {TargetDataLine.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "read" -> {
                        byte[] b = (byte[]) args[0];
                        int off = (Integer) args[1], len = (Integer) args[2];
                        for (int i = 0; i + 1 < len; i += 2) {
                            b[off + i] = 0x20;
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
    }

    @BeforeEach
    void setUp() throws Exception {
        QTSession.open();
        savedSources = SoundManager.sources;
        SoundManager.sources = SequenceGrabberTest::line;
    }

    @AfterEach
    void tearDown() {
        QTSession.close();
        SoundManager.sources = savedSources;
    }

    static int pixel(QDGraphics g, int x, int y) throws Exception {
        RawEncodedImage pixels = g.getPixMap().getPixelData();
        return pixels.getInt(y * pixels.getRowBytes() + x * 4) | 0xff000000;
    }

    static void idle(SequenceGrabber sg, long millis) throws Exception {
        long until = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < until) {
            sg.idle();
            Thread.sleep(15);
        }
    }

    @Test
    void testPreview() throws Exception {
        SequenceGrabber sg = new SequenceGrabber();
        SGVideoChannel video = new SGVideoChannel(sg);
        SGDeviceList devices = video.getDeviceList(0);
        assertTrue(devices.getCount() >= 1);
        assertEquals("Test Pattern", devices.getDeviceName(devices.getCount() - 1).getName());
        video.setDevice("Test Pattern");
        QDRect bounds = new QDRect(160, 120);
        video.setBounds(bounds);
        video.setUsage(seqGrabPreview);
        QDGraphics g = new QDGraphics(bounds);
        sg.setGWorld(g, null);
        sg.startPreview();
        assertTrue(sg.isPreviewMode());
        sg.idle();
        // white and black bars at the ends
        assertEquals(0xffffffff, pixel(g, 2, 2));
        assertEquals(0xff000000, pixel(g, 157, 2));
        sg.stop();

        VideoDigitizer vd = video.getDigitizerComponent();
        assertNotNull(vd);
        assertEquals(0, vd.getNumberOfInputs());
        assertEquals(240, vd.getDigitizerInfo().getmaxDestHeight());

        Pict pict = sg.grabPict(new QDRect(80, 60), 0, 0);
        assertEquals(80, pict.getPictFrame().getWidth());
    }

    @Test
    void testRecord() throws Exception {
        File file = temp.resolve("grab.mov").toFile();
        SequenceGrabber sg = new SequenceGrabber();
        SGVideoChannel video = new SGVideoChannel(sg);
        video.setDevice("Test Pattern");
        video.setBounds(new QDRect(160, 120));
        video.setUsage(seqGrabRecord);
        video.setCompressorType(kAnimationCodecType);
        video.setFrameRate(15);
        SGSoundChannel sound = new SGSoundChannel(sg);
        sound.setUsage(seqGrabRecord);
        sound.setSoundInputRate(22050f);
        sound.setSoundInputParameters(16, 1, quicktime.sound.SoundConstants.kSoundNotCompressed);
        QDGraphics g = new QDGraphics(new QDRect(160, 120));
        sg.setGWorld(g, null);
        sg.setDataOutput(new QTFile(file), seqGrabToDisk);
        sg.startRecord();
        assertTrue(sg.isRecordMode());
        idle(sg, 700);
        sg.stop();

        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(file)));
        assertEquals(2, movie.getTrackCount());
        MovieRec m = Heap.peer(QTObject.ID(movie), MovieRec.class);
        MediaRec v = m.tracks.getFirst().media;
        assertTrue(v.sampleCount() >= 5, "frames: " + v.sampleCount());
        assertEquals("rle ", Bytes.fourCC(Bytes.getInt(v.descriptions.getFirst(), 4)));
        BufferedImage frame = JavaFrames.decode(v, v.duration() / 2);
        assertNotNull(frame);
        assertEquals(0xffffffff, frame.getRGB(2, 2));
        MediaRec s = m.tracks.get(1).media;
        assertTrue(s.isSound());
        assertTrue(s.sampleCount() > 22050 / 4, "sound frames: " + s.sampleCount());
        float[] pcm = Audio.read(s, 100, 10, 1);
        assertEquals(0x2000 / 32768f, pcm[0], 0.001);
        // the movie lasts as long as it was recorded
        double seconds = movie.getDuration() / (double) movie.getTimeScale();
        assertEquals(0.7, seconds, 0.3);
    }

    @Test
    void testDataProc() throws Exception {
        SequenceGrabber sg = new SequenceGrabber();
        SGVideoChannel video = new SGVideoChannel(sg);
        video.setDevice("Test Pattern");
        video.setBounds(new QDRect(64, 48));
        video.setUsage(seqGrabRecord);
        QDGraphics g = new QDGraphics(new QDRect(64, 48));
        sg.setGWorld(g, null);
        AtomicInteger calls = new AtomicInteger();
        AtomicInteger bytes = new AtomicInteger();
        sg.setDataProc((channel, data, offset, chRefCon, time, writeType) -> {
            calls.incrementAndGet();
            bytes.addAndGet(data.getSize());
            assertInstanceOf(SGVideoChannel.class, channel);
            return 0;
        });
        sg.setDataOutput(null, seqGrabDontMakeMovie);
        sg.startRecord();
        // idle by count, not by time: the first frame warms up the codec, which may take the whole time on a busy machine
        for (int i = 0; i < 5; i++) {
            sg.idle();
            Thread.sleep(15);
        }
        sg.stop();
        assertTrue(calls.get() >= 3, "calls " + calls.get());
        assertTrue(bytes.get() > 0);
    }

    @Test
    void testSettings() throws Exception {
        SequenceGrabber sg = new SequenceGrabber();
        SGVideoChannel video = new SGVideoChannel(sg);
        video.setCompressorType(kPNGCodecType);
        video.setFrameRate(12);
        UserData settings = sg.getSettings();
        SequenceGrabber other = new SequenceGrabber();
        other.setSettings(settings);
        SGVideoChannel restored = (SGVideoChannel) other.getIndChannel(1);
        assertEquals(kPNGCodecType, restored.getCompressorType());
        assertEquals(12f, restored.getFrameRate(), 0.01);
    }

    static final int kPNGCodecType = 0x706e6720;
}
