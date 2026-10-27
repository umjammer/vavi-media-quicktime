/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.std.movies;

import java.awt.Component;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.util.RawEncodedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MovieControllerTest {

    static final String MEDIA = "src/test/resources/media/";

    @BeforeEach
    void setUp() throws Exception {
        QTSession.open();
    }

    @AfterEach
    void tearDown() {
        QTSession.close();
    }

    @Test
    void testController() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "jumps.mov")));
        QDRect box = movie.getBox();
        MovieController mc = new MovieController(movie);
        assertEquals(movie, mc.getMovie());
        QDRect bounds = mc.getBounds();
        assertEquals(box.getHeight() + 16, bounds.getHeight(), 0);
        QDGraphics port = new QDGraphics(new QDRect(0, 0, bounds.getWidth(), bounds.getHeight()));
        mc.setPort(port);
        mc.setBounds(new QDRect(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight()));

        // click the play button
        mc.click(new QDPoint(8, bounds.getHeight() - 8), 0);
        assertTrue(mc.getPlayRate() != 0 || movie.getRate() != 0);
        mc.idle();
        mc.key(' ', 0);
        assertEquals(0, movie.getRate(), 0);

        // the bar is drawn
        mc.idle();
        RawEncodedImage pixels = port.getPixMap().getPixelData();
        int y = bounds.getHeight() - 2;
        assertNotEquals(0, pixels.getInt(y * pixels.getRowBytes() + 4 * 4) & 0xffffff);

        mc.setLooping(true);
        assertTrue(mc.getLooping());
        mc.setLoopIsPalindrome(true);
        assertTrue(mc.getLoopIsPalindrome());
    }

    @Test
    void testComponent() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "jumps.mov")));
        QTComponent c = QTFactory.makeQTComponent(new MovieController(movie));
        Component component = c.asComponent();
        assertEquals(160, component.getPreferredSize().width);
        assertEquals(120 + 16, component.getPreferredSize().height);
    }
}
