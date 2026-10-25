/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;


/**
 * Frames of movies whose video samples are still images Java can decode:
 * 'jpeg', 'png ', 'gif ', 'tiff', 'BMPf' and uncompressed 'raw ' (24 and 32 bit).
 * The tracks are composed by layer with their matrices into the natural bounds of the movie.
 */
final class JavaFrames {

    private JavaFrames() {
    }

    /** @return the image of the sample at the media time, null if the codec is not supported */
    static BufferedImage decode(MediaRec m, long mediaTime) throws IOException {
        long s = m.sampleAt(mediaTime);
        if (s == 0) {
            return null;
        }
        int r = m.runOfSample(s);
        MediaRec.SampleRun run = m.runs.get(r);
        byte[] desc = m.descriptions.get(run.descIndex - 1);
        int codec = Bytes.getInt(desc, 4);
        long k = s - m.firstSampleOf(r);
        DataRefRec d = m.dataRef(run.dataRefIndex);
        if (d == null) {
            return null;
        }
        String c = Bytes.fourCC(codec);
        switch (c) {
            case "jpeg", "png ", "gif ", "tiff", "BMPf", "WRLE" -> {
                byte[] data = d.read(run.offsetOf(k), (int) run.bytesOf(k, 1));
                return ImageIO.read(new ByteArrayInputStream(data));
            }
            case "raw " -> {
                int width = Bytes.getShort(desc, 32);
                int height = Bytes.getShort(desc, 34);
                int depth = Bytes.getShort(desc, 82);
                if (depth != 24 && depth != 32) {
                    return null;
                }
                byte[] data = d.read(run.offsetOf(k), (int) run.bytesOf(k, 1));
                int bpp = depth / 8;
                int rowBytes = height == 0 ? 0 : data.length / height;
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int o = y * rowBytes + x * bpp;
                        if (o + bpp > data.length) {
                            break;
                        }
                        int rgb = depth == 32 ? ((data[o + 1] & 0xff) << 16) | ((data[o + 2] & 0xff) << 8) | (data[o + 3] & 0xff)
                                : ((data[o] & 0xff) << 16) | ((data[o + 1] & 0xff) << 8) | (data[o + 2] & 0xff);
                        img.setRGB(x, y, 0xff00_0000 | rgb);
                    }
                }
                return img;
            }
            default -> {
                return null;
            }
        }
    }

    /** @return the composed frame at the movie time, null if nothing could be decoded */
    static BufferedImage frame(MovieRec m, long time) throws IOException {
        double[] n = MovieToolbox.naturalBounds(m);
        int w = (int) Math.round(n[2] - n[0]);
        int h = (int) Math.round(n[3] - n[1]);
        if (w <= 0 || h <= 0) {
            return null;
        }
        List<TrackRec> tracks = new ArrayList<>();
        for (TrackRec t : m.tracks) {
            if (t.enabled && t.media != null && t.media.isVisual() && (t.usage & TrackRec.trackUsageInMovie) != 0) {
                tracks.add(t);
            }
        }
        // higher layers are drawn first and covered by lower ones
        tracks.sort(Comparator.comparingInt((TrackRec t) -> -t.layer));
        BufferedImage frame = null;
        Graphics2D g = null;
        try {
            for (TrackRec t : tracks) {
                long mt = t.trackTimeToMediaTime(time);
                if (mt < 0) {
                    continue;
                }
                BufferedImage img = decode(t.media, mt);
                if (img == null) {
                    continue;
                }
                if (frame == null) {
                    frame = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    g = frame.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.setComposite(AlphaComposite.SrcOver);
                }
                double[][] tm = MatrixManager.toDoubles(t.matrix);
                AffineTransform at = new AffineTransform();
                at.translate(-n[0], -n[1]);
                at.concatenate(new AffineTransform(tm[0][0], tm[0][1], tm[1][0], tm[1][1], tm[2][0], tm[2][1]));
                double tw = t.width / 65536.0;
                double th = t.height / 65536.0;
                at.scale(tw == 0 ? 1 : tw / img.getWidth(), th == 0 ? 1 : th / img.getHeight());
                g.drawImage(img, at, null);
            }
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
        return frame;
    }
}
