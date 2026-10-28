/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A source of video frames for the video digitizers of the sequence grabber.
 * <p>
 * There are a test pattern, the screen (with {@link Robot}) and on macOS the cameras of AVFoundation
 * ({@code quicktime.internal.avf.AVFCamera}).
 */
public abstract class VideoSource {

    private static final Logger logger = Logger.getLogger(VideoSource.class.getName());

    /** the digitizer subtype of the source */
    public abstract int subType();

    /** the device name */
    public abstract String name();

    /** @return the names of the inputs */
    public List<String> inputs() {
        return List.of(name());
    }

    /** @return the size of the frames */
    public abstract Dimension size();

    public abstract void start() throws IOException;

    /** @return the latest frame, null if there is none yet */
    public abstract BufferedImage grab();

    public abstract void stop();

    /** @return true if the device can be used */
    public boolean isAvailable() {
        return true;
    }

    static final int test = Bytes.fourCC("test");
    static final int scrn = Bytes.fourCC("scrn");
    static final int avfc = Bytes.fourCC("avfc");

    /** the platform's cameras, for tests to replace */
    static final Supplier<List<VideoSource>> cameras = () -> {
        if (!System.getProperty("os.name", "").startsWith("Mac")) {
            return List.of();
        }
        try {
            @SuppressWarnings("unchecked")
            List<VideoSource> l = (List<VideoSource>) Class.forName("quicktime.internal.avf.AVFCamera").getMethod("devices").invoke(null);
            return l;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            logger.log(Level.FINE, "no cameras", e);
            return List.of();
        }
    };

    /** @return all sources: cameras, the screen and the test pattern */
    static List<VideoSource> all() {
        List<VideoSource> l = new ArrayList<>(cameras.get());
        if (!GraphicsEnvironment.isHeadless()) {
            l.add(new Screen());
        }
        l.add(new TestPattern());
        return l;
    }

    /** @return the sources of a digitizer subtype */
    static List<VideoSource> of(int subType) {
        List<VideoSource> l = new ArrayList<>();
        for (VideoSource s : all()) {
            if (s.subType() == subType) {
                l.add(s);
            }
        }
        return l;
    }

    /** color bars with a moving box and the frame number */
    static final class TestPattern extends VideoSource {
        final int width = 320;
        final int height = 240;
        long frame;
        long started;

        @Override
        public int subType() {
            return test;
        }

        @Override
        public String name() {
            return "Test Pattern";
        }

        @Override
        public Dimension size() {
            return new Dimension(width, height);
        }

        @Override
        public void start() {
            started = System.nanoTime();
        }

        @Override
        public BufferedImage grab() {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            Color[] bars = {Color.white, Color.yellow, Color.cyan, Color.green, Color.magenta, Color.red, Color.blue, Color.black};
            for (int i = 0; i < bars.length; i++) {
                g.setColor(bars[i]);
                g.fillRect(i * width / bars.length, 0, (i + 1) * width / bars.length - i * width / bars.length, height * 3 / 4);
            }
            g.setColor(Color.darkGray);
            g.fillRect(0, height * 3 / 4, width, height - height * 3 / 4);
            double seconds = (System.nanoTime() - started) / 1e9;
            int x = (int) ((seconds * 80) % Math.max(1, width - 20));
            g.setColor(Color.orange);
            g.fillRect(x, height * 3 / 4 + 4, 20, Math.max(4, height / 4 - 8));
            g.setColor(Color.white);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Math.max(10, height / 16)));
            g.drawString(String.valueOf(frame++), 4, height - 6);
            g.dispose();
            return img;
        }

        @Override
        public void stop() {
        }
    }

    /** the primary screen */
    static final class Screen extends VideoSource {
        Robot robot;

        @Override
        public int subType() {
            return scrn;
        }

        @Override
        public String name() {
            return "Screen";
        }

        @Override
        public Dimension size() {
            return Toolkit.getDefaultToolkit().getScreenSize();
        }

        @Override
        public boolean isAvailable() {
            return !GraphicsEnvironment.isHeadless();
        }

        @Override
        public void start() throws IOException {
            try {
                robot = new Robot();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        @Override
        public BufferedImage grab() {
            return robot == null ? null : robot.createScreenCapture(new Rectangle(size()));
        }

        @Override
        public void stop() {
            robot = null;
        }
    }
}
