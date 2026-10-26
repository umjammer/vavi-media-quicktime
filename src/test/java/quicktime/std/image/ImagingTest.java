/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.std.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.qd.Pict;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTConstants4;
import quicktime.std.qtcomponents.DataCodecCompressor;
import quicktime.std.qtcomponents.DataCodecDecompressor;
import quicktime.std.qtcomponents.DataCodecInfo;
import quicktime.std.qtcomponents.ImageCompressionDialog;
import quicktime.std.qtcomponents.SpatialSettings;
import quicktime.util.QTHandle;
import quicktime.util.QTPointer;
import quicktime.util.RawEncodedImage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ImagingTest implements StdQTConstants, StdQTConstants4 {

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

    /** @return the argb pixel of the gworld */
    static int pixel(QDGraphics g, int x, int y) throws Exception {
        RawEncodedImage pixels = g.getPixMap().getPixelData();
        return pixels.getInt(y * pixels.getRowBytes() + x * 4);
    }

    /** a gworld with a test pattern: a red left half, a blue right half with a green square */
    static QDGraphics pattern(int w, int h) throws Exception {
        QDGraphics g = new QDGraphics(new QDRect(w, h));
        RawEncodedImage pixels = g.getPixMap().getPixelData();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = x < w / 2 ? 0xffff0000 : 0xff0000ff;
                if (x >= w / 4 && x < w / 4 + 8 && y >= 4 && y < 12) {
                    c = 0xff00ff00;
                }
                pixels.setInt(y * pixels.getRowBytes() + x * 4, c);
            }
        }
        return g;
    }

    static void assertClose(int expected, int actual, int tolerance) {
        for (int shift = 0; shift < 24; shift += 8) {
            int e = (expected >> shift) & 0xff, a = (actual >> shift) & 0xff;
            assertTrue(Math.abs(e - a) <= tolerance, Integer.toHexString(expected) + " != " + Integer.toHexString(actual));
        }
    }

    @Test
    void testImporterDraw() throws Exception {
        GraphicsImporter gi = new GraphicsImporter(new QTFile(MEDIA + "qt3logo.jpg"));
        QDRect bounds = gi.getNaturalBounds();
        BufferedImage original = ImageIO.read(new File(MEDIA + "qt3logo.jpg"));
        assertEquals(original.getWidth(), bounds.getWidth());
        assertEquals(original.getHeight(), bounds.getHeight());
        ImageDescription desc = gi.getImageDescription();
        assertEquals(kJPEGCodecType, desc.getCType());

        QDGraphics g = new QDGraphics(bounds);
        gi.setGWorld(g, null);
        gi.draw();
        int x = original.getWidth() / 2, y = original.getHeight() / 2;
        assertClose(original.getRGB(x, y), pixel(g, x, y), 2);

        // scaled to half into a bounds rect
        QDGraphics half = new QDGraphics(new QDRect(bounds.getWidth() / 2, bounds.getHeight() / 2));
        gi.setGWorld(half, null);
        gi.setBoundsRect(new QDRect(bounds.getWidth() / 2, bounds.getHeight() / 2));
        gi.draw();
        assertClose(original.getRGB(x, y), pixel(half, x / 2, y / 2), 40);
    }

    @Test
    void testImportPict() throws Exception {
        GraphicsImporter gi = new GraphicsImporter(new QTFile(MEDIA + "images/Ship01.pct"));
        QDRect bounds = gi.getNaturalBounds();
        assertTrue(bounds.getWidth() > 0);
        Pict pict = gi.getAsPicture();
        assertNotNull(pict);
        QDGraphics g = new QDGraphics(bounds);
        gi.setGWorld(g, null);
        gi.draw();
    }

    @Test
    void testExport() throws Exception {
        GraphicsImporter gi = new GraphicsImporter(new QTFile(MEDIA + "qt3logo.jpg"));
        File png = temp.resolve("logo.png").toFile();
        GraphicsExporter ge = new GraphicsExporter(kQTFileTypePNG);
        ge.setInputGraphicsImporter(gi);
        ge.setOutputFile(new QTFile(png));
        int size = ge.doExport();
        assertEquals(png.length(), size);
        BufferedImage original = ImageIO.read(new File(MEDIA + "qt3logo.jpg"));
        BufferedImage exported = ImageIO.read(png);
        assertEquals(original.getWidth(), exported.getWidth());
        assertClose(original.getRGB(10, 10), exported.getRGB(10, 10), 2);

        // JPEG to JPEG is copied
        File jpg = temp.resolve("logo.jpg").toFile();
        GraphicsExporter jpeg = new GraphicsExporter(kQTFileTypeJPEG);
        jpeg.setInputGraphicsImporter(gi);
        jpeg.setOutputFile(new QTFile(jpg));
        jpeg.setDontRecompress(true);
        jpeg.doExport();
        assertArrayEquals(Files.readAllBytes(new File(MEDIA + "qt3logo.jpg").toPath()), Files.readAllBytes(jpg.toPath()));

        // a gworld to a QuickTime image file and back
        QDGraphics g = pattern(32, 16);
        File qtif = temp.resolve("pattern.qtif").toFile();
        GraphicsExporter q = new GraphicsExporter(kQTFileTypeQuickTimeImage);
        q.setInputGWorld(g);
        q.setOutputFile(new QTFile(qtif));
        q.doExport();
        GraphicsImporter back = new GraphicsImporter(new QTFile(qtif));
        QDGraphics g2 = new QDGraphics(back.getNaturalBounds());
        back.setGWorld(g2, null);
        back.draw();
        assertEquals(pixel(g, 1, 1) | 0xff000000, pixel(g2, 1, 1) | 0xff000000);
        assertEquals(pixel(g, 30, 1) | 0xff000000, pixel(g2, 30, 1) | 0xff000000);
    }

    @Test
    void testFormats() throws Exception {
        QDGraphics g = pattern(32, 16);
        int[][] formats = {{kQTFileTypePNG, 0}, {kQTFileTypeBMP, 0}, {kQTFileTypeGIF, 8}, {kQTFileTypeTIFF, 0}, {kQTFileTypePicture, 0},
                {kQTFileTypePhotoShop, 0}, {kQTFileTypeJPEG, 40}, {kQTFileTypeQuickTimeImage, 0}};
        for (int[] f : formats) {
            File file = temp.resolve("pattern." + Integer.toHexString(f[0])).toFile();
            GraphicsExporter ge = new GraphicsExporter(f[0]);
            ge.setInputGWorld(g);
            ge.setOutputFile(new QTFile(file));
            ge.doExport();
            GraphicsImporter gi = new GraphicsImporter(new QTFile(file));
            assertEquals(32, gi.getNaturalBounds().getWidth(), Integer.toHexString(f[0]));
            QDGraphics out = new QDGraphics(gi.getNaturalBounds());
            gi.setGWorld(out, null);
            gi.draw();
            assertClose(pixel(g, 2, 2), pixel(out, 2, 2), f[1]);
            assertClose(pixel(g, 30, 14), pixel(out, 30, 14), f[1]);
        }
        // MacPaint is 1 bit 576 x 720
        File paint = temp.resolve("pattern.pntg").toFile();
        GraphicsExporter ge = new GraphicsExporter(kQTFileTypeMacPaint);
        ge.setInputGWorld(g);
        ge.setOutputFile(new QTFile(paint));
        ge.doExport();
        GraphicsImporter gi = new GraphicsImporter(new QTFile(paint));
        assertEquals(576, gi.getNaturalBounds().getWidth());
    }

    /** compresses the pattern and draws it back */
    void roundTrip(int codec, int tolerance) throws Exception {
        QDGraphics g = pattern(32, 16);
        QDRect r = new QDRect(32, 16);
        int max = QTImage.getMaxCompressionSize(g, r, 0, codecHighQuality, codec, CodecComponent.anyCodec);
        RawEncodedImage data = new RawEncodedImage(max, true);
        ImageDescription desc = QTImage.compress(g, r, codecHighQuality, codec, data);
        assertEquals(codec, desc.getCType());
        assertTrue(desc.getDataSize() > 0 && desc.getDataSize() <= max);
        QDGraphics out = new QDGraphics(r);
        QTImage.decompress(data, desc, out, r, 0);
        for (int[] p : new int[][] {{1, 1}, {30, 14}, {10, 8}}) {
            assertClose(pixel(g, p[0], p[1]), pixel(out, p[0], p[1]), tolerance);
        }
    }

    @Test
    void testCodecs() throws Exception {
        roundTrip(kRawCodecType, 0);
        roundTrip(kAnimationCodecType, 0);
        roundTrip(kPNGCodecType, 0);
        roundTrip(kJPEGCodecType, 40);
        roundTrip(kComponentVideoCodecType, 40);
        roundTrip(kPlanarRGBCodecType, 0);
        roundTrip(kTIFFCodecType, 0);

        CodecNameList list = new CodecNameList(1);
        boolean animation = false;
        for (int i = 0; i < list.getCount(); i++) {
            animation |= list.getNth(i).getCType() == kAnimationCodecType;
        }
        assertTrue(animation);
        CodecInfo info = new CodecInfo(kAnimationCodecType, CodecComponent.anyCodec);
        assertEquals("Animation", info.getTypeName());
    }

    @Test
    void testSequence() throws Exception {
        QDRect r = new QDRect(32, 16);
        QDGraphics src = pattern(32, 16);
        CSequence cs = new CSequence(src, r, kAnimationCodecType, CodecComponent.anyCodec, codecLosslessQuality, codecLosslessQuality, 10, 0);
        ImageDescription desc = cs.getDescription();
        RawEncodedImage data = new RawEncodedImage(cs.getMaxCompressionSize(src), true);
        QDGraphics dst = new QDGraphics(r);
        DSequence ds = new DSequence(desc, dst, r, null, null, 0, codecNormalQuality, CodecComponent.anyCodec);

        CompressedFrameInfo key = cs.compressFrame(src, r, codecFlagUpdatePrevious, data);
        assertEquals(0, key.getSimilarity());
        ds.decompressFrameS(data, 0);
        assertEquals(pixel(src, 20, 8) | 0xff000000, pixel(dst, 20, 8) | 0xff000000);

        // change a pixel, the difference frame is smaller
        RawEncodedImage pixels = src.getPixMap().getPixelData();
        pixels.setInt(8 * pixels.getRowBytes() + 20 * 4, 0xffffff00);
        CompressedFrameInfo diff = cs.compressFrame(src, r, codecFlagUpdatePrevious, data);
        assertTrue(diff.getSimilarity() != 0);
        assertTrue(diff.getDataSize() < key.getDataSize(), diff.getDataSize() + " < " + key.getDataSize());
        ds.decompressFrameS(data, 0);
        assertEquals(0xffffff00, pixel(dst, 20, 8) | 0xff000000);
        assertEquals(pixel(src, 2, 2) | 0xff000000, pixel(dst, 2, 2) | 0xff000000);
    }

    @Test
    void testDescriptionExtensions() throws Exception {
        ImageDescription desc = new ImageDescription(kRawCodecType);
        QTHandle ext = new QTHandle(new byte[] {1, 2, 3, 4});
        desc.addExtension(0x74657374, ext);
        assertEquals(1, desc.countExtensionType(0x74657374));
        assertArrayEquals(new byte[] {1, 2, 3, 4}, desc.getExtension(0x74657374, 1).getBytes());
        desc.removeExtension(0x74657374, 1);
        assertEquals(0, desc.countExtensionType(0x74657374));
    }

    @Test
    void testCompressionDialog() throws Exception {
        ImageCompressionDialog d = new ImageCompressionDialog();
        SpatialSettings s = new SpatialSettings(kPNGCodecType, CodecComponent.anyCodec, 24, codecHighQuality);
        d.setInfoSpatialSettings(s);
        assertEquals(kPNGCodecType, d.getInfoSpatialSettings().getCodecType());
        QDGraphics g = pattern(32, 16);
        QTImage image = d.compressImage(g, new QDRect(32, 16));
        assertEquals(kPNGCodecType, image.getDescription().getCType());
        QDGraphics out = new QDGraphics(new QDRect(32, 16));
        QTImage.decompress(image.getImage(), image.getDescription(), out, new QDRect(32, 16), 0);
        assertEquals(pixel(g, 30, 2) | 0xff000000, pixel(out, 30, 2) | 0xff000000);
    }

    @Test
    void testPictures() throws Exception {
        Pict pict = Pict.fromGraphicsImporter(new GraphicsImporter(new QTFile(MEDIA + "qt3logo.jpg")));
        QDRect frame = pict.getPictFrame();
        Pict compressed = pict.compress(codecNormalQuality, kJPEGCodecType);
        assertNotNull(compressed);
        QDGraphics out = new QDGraphics(frame);
        compressed.draw(out, frame);
        BufferedImage original = ImageIO.read(new File(MEDIA + "qt3logo.jpg"));
        assertClose(original.getRGB(20, 20), pixel(out, 20, 20), 40);
        Pict thumbnail = pict.makeThumbnail(0);
        assertTrue(thumbnail.getPictFrame().getWidth() <= 80);
    }

    @Test
    void testDataCodec() throws Exception {
        byte[] text = "QuickTime QuickTime QuickTime QuickTime QuickTime QuickTime".getBytes();
        DataCodecCompressor c = new DataCodecCompressor(0x7a6c6962);
        QTPointer src = new QTPointer(text);
        QTPointer dst = new QTPointer(c.getCompressBufferSize(text.length), true);
        DataCodecInfo info = c.compress(src, dst);
        assertTrue(info.actualDstSize < text.length);
        DataCodecDecompressor d = new DataCodecDecompressor(0x7a6c6962);
        QTPointer packed = new QTPointer(info.actualDstSize, true);
        packed.copyFromArray(0, dst.getBytes(), 0, info.actualDstSize);
        QTPointer unpacked = new QTPointer(text.length, true);
        d.decompress(packed, unpacked);
        assertArrayEquals(text, unpacked.getBytes());
    }
}
