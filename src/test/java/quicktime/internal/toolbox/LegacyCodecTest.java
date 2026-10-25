/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.movies.Movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/** frames of the test movies through the Java decoders of {@link Codecs} */
class LegacyCodecTest {

    static final String MEDIA = "src/test/resources/media/";

    @BeforeEach
    void setUp() throws Exception {
        QTSession.open();
    }

    @AfterEach
    void tearDown() {
        QTSession.close();
    }

    static MediaRec media(String file, int track) throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + file)));
        return Heap.peer(QTObject.ID(movie.getIndTrack(track).getMedia()), MediaRec.class);
    }

    static int colors(BufferedImage img) {
        Set<Integer> s = new HashSet<>();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                s.add(img.getRGB(x, y));
            }
        }
        return s.size();
    }

    @Test
    void testCinepak() throws Exception {
        MediaRec m = media("qtvr.mov", 1);
        assertEquals("cvid", Bytes.fourCC(Bytes.getInt(m.descriptions.get(0), 4)));
        // the middle needs the frames from the previous key frame
        BufferedImage middle = JavaFrames.decode(m, m.duration() / 2);
        assertNotNull(middle);
        assertEquals(288, middle.getWidth());
        assertTrue(colors(middle) > 100, "a photo has many colors: " + colors(middle));
        // seeking back decodes from a key frame again
        BufferedImage first = JavaFrames.decode(m, 0);
        assertTrue(colors(first) > 100);
    }

    @Test
    void testGraphics() throws Exception {
        MediaRec m = media("qtvr.mov", 2);
        assertEquals("smc ", Bytes.fourCC(Bytes.getInt(m.descriptions.get(0), 4)));
        BufferedImage img = JavaFrames.decode(m, m.duration() * 2 / 3);
        assertNotNull(img);
        assertTrue(colors(img) >= 2, "the hot spots are drawn");
    }

    @Test
    void testPicture() throws Exception {
        MediaRec m = media("Ships.mov", 1);
        assertEquals("qdrw", Bytes.fourCC(Bytes.getInt(m.descriptions.get(0), 4)));
        BufferedImage img = JavaFrames.decode(m, 0);
        assertNotNull(img);
        assertTrue(colors(img) > 10);
    }
}
