/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import quicktime.QTSession;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.Pict;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTConstants;
import quicktime.std.image.Matrix;
import quicktime.std.movies.Movie;
import quicktime.util.RawEncodedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class RenderingTest implements StdQTConstants {

    static final String MEDIA = "src/test/resources/media/";

    @TempDir
    Path temp;

    @BeforeEach
    void setUp() throws Exception {
        QTSession.open();
    }

    @AfterEach
    void tearDown() {
        QTSession.close();
    }

    @Test
    void testImageMovie() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "qt3logo.jpg")));
        QDRect box = movie.getBox();
        assertTrue(box.getWidth() > 0);
        Pict pict = movie.getPict(0);
        assertNotNull(pict);
        BufferedImage image = PictCodec.decode(pict.getBytes());
        assertNotNull(image);
        assertEquals((int) box.getWidth(), image.getWidth());
        BufferedImage original = javax.imageio.ImageIO.read(new File(MEDIA + "qt3logo.jpg"));
        int x = original.getWidth() / 2, y = original.getHeight() / 2;
        assertEquals(original.getRGB(x, y) & 0xffffff, image.getRGB(x, y) & 0xffffff);
    }

    @Test
    void testDrawIntoGWorld() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "qt3logo.jpg")));
        QDRect box = movie.getBox();
        QDGraphics g = new QDGraphics(box);
        movie.setGWorld(g, null);
        movie.task(0);
        RawEncodedImage pixels = g.getPixMap().getPixelData();
        BufferedImage original = javax.imageio.ImageIO.read(new File(MEDIA + "qt3logo.jpg"));
        int x = original.getWidth() / 2, y = original.getHeight() / 2;
        int argb = pixels.getInt(y * pixels.getRowBytes() + x * 4);
        assertEquals(original.getRGB(x, y) & 0xffffff, argb & 0xffffff);
    }

    @Test
    void testExportMP4() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "qt3logo.jpg")));
        File out = temp.resolve("out.mp4").toFile();
        movie.convertToFile(new QTFile(out), Bytes.fourCC("mpg4"), kMoviePlayer, -1);
        byte[] b = Files.readAllBytes(out.toPath());
        assertEquals("ftyp", new String(b, 4, 4));
        assertEquals("mp42", new String(b, 8, 4));
        Movie reopened = Movie.fromFile(OpenMovieFile.asRead(new QTFile(out)));
        assertEquals(1, reopened.getTrackCount());
        assertNotNull(reopened.getPict(0));
    }

    @Test
    void testRegion() throws Exception {
        Region a = new Region(new QDRect(0, 0, 100, 100));
        Region b = new Region(new QDRect(50, 50, 100, 100));
        Region u = a.union(b);
        assertEquals(150, u.getBounds().getWidth(), 0);
        assertTrue(u.getSize() > 10);
        assertTrue(u.pointIn(new quicktime.qd.QDPoint(120, 120)));
        assertFalse(u.pointIn(new quicktime.qd.QDPoint(120, 20)));
        Region s = u.sect(new QDRect(0, 0, 60, 60));
        assertEquals(60, s.getBounds().getWidth(), 0);
    }

    @Test
    void testMatrix() throws Exception {
        Matrix m = new Matrix();
        m.scale(2, 3, 0, 0);
        m.translate(10, 20);
        quicktime.qd.QDPoint p = new quicktime.qd.QDPoint(1, 1);
        m.transformPoints(p);
        assertEquals(12, p.getX());
        assertEquals(23, p.getY());
        Matrix i = new Matrix();
        assertTrue(m.inverse(i));
    }
}
