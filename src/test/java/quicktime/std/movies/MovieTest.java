/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.std.movies;

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
import quicktime.std.StdQTConstants;
import quicktime.std.movies.media.Media;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.movies.media.UserData;
import quicktime.util.QTHandle;
import quicktime.util.QTPointer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MovieTest implements StdQTConstants, quicktime.io.IOConstants {

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

    static Movie open(String path) throws Exception {
        return Movie.fromFile(OpenMovieFile.asRead(new QTFile(path)));
    }

    @Test
    void testOpenCompressedMovie() throws Exception {
        Movie movie = open(MEDIA + "Sample.mov");
        assertTrue(movie.getTrackCount() > 0);
        assertTrue(movie.getDuration() > 0);
        Track track = movie.getIndTrack(1);
        Media media = track.getMedia();
        assertTrue(media.getSampleCount() > 0);
        System.err.println("Sample.mov: tracks=" + movie.getTrackCount() + " duration=" + movie.getDuration() + "/" + movie.getTimeScale()
                + " box=" + movie.getBox() + " samples=" + media.getSampleCount());
    }

    @Test
    void testOpenMovie() throws Exception {
        Movie movie = open(MEDIA + "jumps.mov");
        assertEquals(1, movie.getTrackCount());
        Track track = movie.getIndTrackType(1, videoMediaType, movieTrackMediaType);
        assertNotNull(track);
        Media media = track.getMedia();
        assertTrue(media.getSampleCount() > 1);
        assertEquals(movie.getDuration(), track.getDuration());
        assertTrue(movie.getBox().getWidth() > 0);
        SampleDescription desc = media.getSampleDescription(1);
        assertTrue(desc.getSize() > 0);
        System.err.println("jumps.mov: duration=" + movie.getDuration() + "/" + movie.getTimeScale() + " box=" + movie.getBox()
                + " samples=" + media.getSampleCount() + " sync=" + media.getSyncSampleCount() + " desc=" + desc);
    }

    @Test
    void testFlatten() throws Exception {
        Movie movie = open(MEDIA + "jumps.mov");
        File out = temp.resolve("flat.mov").toFile();
        Movie flat = movie.flattenData(flattenAddMovieToDataFork, new QTFile(out), kMoviePlayer, smSystemScript, createMovieFileDeleteCurFile);
        assertNotNull(flat);
        assertTrue(out.length() > 0);
        Movie reopened = open(out.getPath());
        assertEquals(movie.getDuration(), reopened.getDuration());
        Media a = movie.getIndTrack(1).getMedia();
        Media b = reopened.getIndTrack(1).getMedia();
        assertEquals(a.getSampleCount(), b.getSampleCount());
        // same sample data
        assertArrayEquals(a.getSample(0, 0, 1).data.getBytes(), b.getSample(0, 0, 1).data.getBytes());
    }

    @Test
    void testEdit() throws Exception {
        Movie movie = open(MEDIA + "jumps.mov");
        int d = movie.getDuration();
        movie.insertSegment(movie, 0, d / 2, d);
        assertEquals(d + d / 2, movie.getDuration());
        movie.deleteSegment(0, d / 2);
        assertEquals(d, movie.getDuration());
        movie.setSelection(0, d / 4);
        Movie cut = movie.cutSelection();
        assertEquals(d / 4, cut.getDuration());
        assertEquals(d - d / 4, movie.getDuration());
        movie.setSelection(0, 0);
        movie.pasteSelection(cut);
        assertEquals(d, movie.getDuration());
    }

    @Test
    void testText() throws Exception {
        Movie movie = open(MEDIA + "TextOnly.mov");
        Track track = movie.getIndTrackType(1, textMediaType, movieTrackMediaType);
        assertNotNull(track);
        QTPointer text = new QTPointer("Text".getBytes());
        SearchResult r = movie.searchText(text, 0, 0, 0);
        System.err.println("search: " + r);
        assertNotNull(r);
    }

    @Test
    void testCreate() throws Exception {
        File out = temp.resolve("new.mov").toFile();
        QTFile file = new QTFile(out);
        Movie movie = Movie.createMovieFile(file, kMoviePlayer, createMovieFileDeleteCurFile | createMovieFileDontCreateResFile);
        Track track = movie.addTrack(160, 120, 0);
        quicktime.std.movies.media.VideoMedia media = new quicktime.std.movies.media.VideoMedia(track, 600);
        media.beginEdits();
        Media src = open(MEDIA + "jumps.mov").getIndTrack(1).getMedia();
        QTHandle h = new QTHandle();
        quicktime.std.image.ImageDescription desc = new quicktime.std.image.ImageDescription(QDConstantsHolder.k32ARGB);
        for (int i = 0; i < 3; i++) {
            byte[] data = new byte[] {(byte) i, 1, 2, 3};
            h.setSize(data.length);
            h.copyFromArray(0, data, 0, data.length);
            media.addSample(h, 0, data.length, 100, desc, 1, 0);
        }
        media.endEdits();
        track.insertMedia(0, 0, media.getDuration(), 1);
        OpenMovieFile omf = OpenMovieFile.asWrite(file);
        movie.addResource(omf, movieInDataForkResID, file.getName());
        omf.close();
        assertTrue(src.getSampleCount() > 0);

        Movie reopened = open(out.getPath());
        assertEquals(300, reopened.getDuration());
        Media m = reopened.getIndTrack(1).getMedia();
        assertEquals(3, m.getSampleCount());
        assertArrayEquals(new byte[] {2, 1, 2, 3}, m.getSample(0, 200, 1).data.getBytes());
    }

    @Test
    void testUserData() throws Exception {
        Movie movie = open(MEDIA + "TextOnly.mov");
        UserData ud = movie.getUserData();
        int type = quicktime.util.QTUtils.toOSType("Ⓒnam".replace('Ⓒ', (char) 0xa9));
        ud.addText(new QTHandle("hello".getBytes()), type, 1, 0);
        assertEquals("hello", ud.getTextAsString(type, 1, 0));
    }

    @Test
    void testImportAiff() throws Exception {
        Movie movie = open(MEDIA + "sin440.aif");
        Track track = movie.getIndTrackType(1, soundMediaType, movieTrackMediaType);
        assertNotNull(track);
        assertTrue(movie.getDuration() > 0);
        File wav = temp.resolve("out.wav").toFile();
        movie.convertToFile(new QTFile(wav), kQTFileTypeWave, kMoviePlayer, smSystemScript);
        byte[] b = Files.readAllBytes(wav.toPath());
        assertEquals("RIFF", new String(b, 0, 4));
        System.err.println("sin440.aif: duration=" + movie.getDuration() + "/" + movie.getTimeScale() + " wav=" + b.length);
    }

    /** k32ARGBPixelFormat */
    static final class QDConstantsHolder {
        static final int k32ARGB = 32;
    }
}
