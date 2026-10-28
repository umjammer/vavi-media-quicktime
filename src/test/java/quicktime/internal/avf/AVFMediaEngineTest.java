/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;
import quicktime.QTSession;
import quicktime.internal.toolbox.MediaEngine;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.Pict;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.media.Media;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * AVFoundation is only on macOS.
 */
@EnabledOnOs(OS.MAC)
class AVFMediaEngineTest implements StdQTConstants {

    static final String MEDIA = "src/test/resources/media/";

    /** volume of the played sound, 0.0 ~ 1.0 */
    static final float VOLUME = Float.parseFloat(System.getProperty("vavi.test.volume", "0.2"));

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
    void testEngine() {
        assertInstanceOf(AVFMediaEngine.class, MediaEngine.get());
    }

    @Test
    void testFrame() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "qt3logo.jpg")));
        Pict pict = movie.getPict(0);
        assertNotNull(pict);
        assertTrue(pict.getSize() > 512);
    }

    @Test
    void testExport3gpp() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "sin440.aif")));
        File out = temp.resolve("out.3gp").toFile();
        movie.convertToFile(new QTFile(out), quicktime.util.QTUtils.toOSType("3gpp"), kMoviePlayer, -1);
        byte[] b = Files.readAllBytes(out.toPath());
        assertEquals("ftyp", new String(b, 4, 4));
        Movie reopened = Movie.fromFile(OpenMovieFile.asRead(new QTFile(out)));
        Media media = reopened.getIndTrack(1).getMedia();
        String format = quicktime.util.QTUtils.fromOSType(media.getSampleDescription(1).getDataFormat());
        System.err.println("3gpp: " + format + ", " + out.length() + " bytes");
        assertTrue(format.equals("mp4a") || format.equals("samr"), format);
    }

    @Test
    void testPlay() throws Exception {
        Movie movie = Movie.fromFile(OpenMovieFile.asRead(new QTFile(MEDIA + "sin440.aif")));
        movie.setVolume(VOLUME);
        movie.start();
        for (int i = 0; i < 20; i++) {
            Thread.sleep(25);
            movie.task(0);
        }
        assertTrue(movie.getTime() > 0);
        movie.stop();
    }
}
