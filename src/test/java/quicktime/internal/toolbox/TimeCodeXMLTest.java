/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.TimeCodeMedia;
import quicktime.std.qtcomponents.TimeCodeDef;
import quicktime.std.qtcomponents.TimeCodeDescription;
import quicktime.std.qtcomponents.TimeCodeInfo;
import quicktime.std.qtcomponents.TimeCodeTime;
import quicktime.std.qtcomponents.TimeCoder;
import quicktime.std.qtcomponents.XMLAttribute;
import quicktime.std.qtcomponents.XMLContent;
import quicktime.std.qtcomponents.XMLDoc;
import quicktime.std.qtcomponents.XMLElement;
import quicktime.std.qtcomponents.XMLParser;
import quicktime.util.QTHandle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TimeCodeXMLTest implements StdQTConstants {

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

    static TimeCodeDef def(int fps, boolean drop) {
        TimeCodeDef d = new TimeCodeDef();
        d.setFlags(drop ? 1 : 0);
        d.setTimeScale(drop ? 2997 : fps * 100);
        d.setFrameDuration(100);
        d.setFramesPerSecond(fps);
        return d;
    }

    @Test
    void testTimeCode() throws Exception {
        Movie movie = new Movie();
        Track track = movie.newTrack(0, 0, 0);
        TimeCodeMedia media = new TimeCodeMedia(track, 2500);
        TimeCoder tc = media.getTimeCodeHandler();

        TimeCodeDef d = def(25, false);
        TimeCodeTime t = tc.toTimeCode(25 * 3661 + 7, d);
        assertEquals(1, t.getHours());
        assertEquals(1, t.getMinutes());
        assertEquals(1, t.getSeconds());
        assertEquals(7, t.getFrames());
        assertEquals(25 * 3661 + 7, tc.toFrameNumber(t, d));
        assertEquals("01:01:01:07", tc.timeCodeToString(d, t));

        // drop frame: 00:01:00;02 follows 00:00:59;29
        TimeCodeDef nd = def(30, true);
        assertEquals("00:00:59;29", tc.timeCodeToString(nd, tc.toTimeCode(1799, nd)));
        assertEquals("00:01:00;02", tc.timeCodeToString(nd, tc.toTimeCode(1800, nd)));
        assertEquals("00:10:00;00", tc.timeCodeToString(nd, tc.toTimeCode(17982, nd)));
        assertEquals(17982, tc.toFrameNumber(new TimeCodeTime(0, 10, 0, 0), nd));

        // a sample starting at 10:00:00:00
        TimeCodeDescription desc = new TimeCodeDescription();
        desc.setTimeCodeDef(d);
        byte[] frame = new byte[4];
        Bytes.putInt(frame, 0, 25 * 36000);
        QTHandle sample = new QTHandle(frame);
        media.beginEdits();
        media.addSample(sample, 0, 4, 2500 * 60, desc, 1, 0);
        media.endEdits();
        track.insertMedia(0, 0, media.getDuration(), 1);
        TimeCodeInfo info = tc.getAtTime(2500 * 2 + 400);
        assertEquals(25 * 36002 + 4, info.frameNumber);
        assertEquals("10:00:02:04", tc.timeCodeToString(info.definition, info.time));
        assertEquals(25, info.definition.getFramesPerSecond());

        tc.setFlags(1, 1);
        assertEquals(1, tc.getFlags());
    }

    static final String DOC = """
            <?xml version="1.0"?>
            <!-- a playlist -->
            <smil xmlns="http://www.w3.org/2001/SMIL20/Language">
              <head><meta name="title" content="Test &amp; more"/></head>
              <body>
                <video src="a.mov" dur="10" begin="50%" loop="true" fill="freeze" color="#ff8000"/>
                <text>Hello <![CDATA[<world>]]></text>
              </body>
            </smil>
            """;

    @Test
    void testXMLParse() throws Exception {
        File f = temp.resolve("doc.smil").toFile();
        Files.writeString(f.toPath(), DOC);
        XMLParser p = new XMLParser();
        int smil = p.addElement("smil", 0, 0, 0);
        int video = p.addElement("video", 0, 0, 0);
        int dur = p.addAttributeAndValue(video, 0, "dur", 0, 0);
        int begin = p.addAttribute(video, 0, "begin", 0);
        p.addAttributeValueKind(video, begin, 50f);
        int loop = p.addAttributeAndValue(video, 0, "loop", 0, true);
        int fill = p.addAttributeAndValue(video, 0, "fill", 0, new String[] {"remove", "freeze"}, false);

        XMLDoc doc = p.parse(new QTFile(f), false, false);
        XMLElement root = doc.getRootElement();
        assertEquals(smil, root.getIdentifier());
        assertEquals(1, root.countAttributes());
        assertEquals("xmlns", root.getAttributes()[0].getName());
        XMLContent[] contents = root.getContents();
        assertEquals(2, contents.length);
        XMLElement head = contents[0].getElement();
        assertTrue(head.identifierUnrecognized());
        assertEquals("head", head.getName());
        XMLElement meta = head.getContents()[0].getElement();
        assertEquals("Test & more", meta.getAttributes()[1].getValueString());

        XMLContent[] body = contents[1].getElement().getContents();
        XMLElement v = body[0].getElement();
        assertEquals(video, v.getIdentifier());
        XMLAttribute[] a = v.getAttributes();
        assertEquals(6, a.length);
        assertEquals("a.mov", a[0].getValueString());
        assertEquals(dur, a[1].getIdentifier());
        assertEquals(10, a[1].getInteger());
        assertEquals(50, a[2].getPercent());
        assertEquals(loop, a[3].getIdentifier());
        assertTrue(a[3].getBoolean());
        assertEquals(fill, a[4].getIdentifier());
        assertTrue(a[5].identifierUnrecognized());
        XMLElement text = body[1].getElement();
        assertEquals("Hello <world>", text.getContents()[0].getString());
        doc.dispose();

        // a broken document
        File bad = temp.resolve("bad.xml").toFile();
        Files.writeString(bad.toPath(), "<a>\n<b>\n</a>");
        assertThrows(StdQTException.class, () -> p.parse(new QTFile(bad), false, false));
        assertEquals(3, p.getParseErrorLine());
        assertTrue(p.getParseErrorDescription().contains("mismatched"));

        // uppercase names need the flag
        File upper = temp.resolve("upper.xml").toFile();
        Files.writeString(upper.toPath(), "<A B=c/>", StandardCharsets.UTF_8);
        assertThrows(StdQTException.class, () -> p.parse(new QTFile(upper), false, false));
        XMLDoc d = p.parse(new QTFile(upper), true, true);
        assertEquals("a", d.getRootElement().getName());
        assertEquals("c", d.getRootElement().getAttributes()[0].getValueString());
    }

    @Test
    void testXMLEvents() throws Exception {
        File f = temp.resolve("doc.smil").toFile();
        Files.writeString(f.toPath(), DOC);
        XMLParser p = new XMLParser();
        List<String> events = new ArrayList<>();
        p.setStartDocumentHandler(() -> events.add("start"));
        p.setEndDocumentHandler(() -> events.add("end"));
        p.setStartElementHandler((name, attributes) -> events.add("<" + name + (attributes != null ? " " + attributes.length : "")));
        p.setEndElementHandler(name -> events.add("</" + name));
        p.setCharDataHandler(text -> events.add("'" + text.trim()));
        p.setCommentHandler(comment -> events.add("!" + comment.trim()));
        p.setPreprocessorHandler((name, attributes) -> events.add("?" + name));
        p.eventParse(new QTFile(f), false, false);
        assertEquals("start", events.getFirst());
        assertEquals("end", events.getLast());
        assertTrue(events.contains("?xml"), events.toString());
        assertTrue(events.contains("!a playlist"), events.toString());
        assertTrue(events.contains("<video 12"), events.toString());
        assertTrue(events.contains("'Hello <world>"), events.toString());
        assertEquals(events.indexOf("<smil 2") + 1, events.indexOf("<head"));
    }
}
