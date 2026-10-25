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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * Frames of movies whose video samples a Java codec of {@link Codecs} can decode.
 * The tracks are composed by layer with their matrices into the natural bounds of the movie.
 */
final class JavaFrames {

    private JavaFrames() {
    }

    /** the last decoded frame of a media, temporal codecs decode on from it */
    private static final class Last {
        long sample;
        int descIndex;
        Codecs.Desc desc;
        BufferedImage image;
    }

    private static final Map<MediaRec, Last> last = Collections.synchronizedMap(new WeakHashMap<>());

    /** @return the bytes of the sample */
    private static byte[] sample(MediaRec m, long s) throws IOException {
        int r = m.runOfSample(s);
        MediaRec.SampleRun run = m.runs.get(r);
        long k = s - m.firstSampleOf(r);
        DataRefRec d = m.dataRef(run.dataRefIndex);
        if (d == null) {
            throw new IOException("no data reference");
        }
        return d.read(run.offsetOf(k), (int) run.bytesOf(k, 1));
    }

    /** @return the image of the sample at the media time, null if the codec is not supported */
    static BufferedImage decode(MediaRec m, long mediaTime) throws IOException {
        long s = m.sampleAt(mediaTime);
        if (s == 0) {
            return null;
        }
        int descIndex = m.runs.get(m.runOfSample(s)).descIndex;
        byte[] descBytes = m.descriptions.get(descIndex - 1);
        Codecs.Codec c = Codecs.get(Bytes.getInt(descBytes, 4));
        if (c == null) {
            return null;
        }
        if (!c.isTemporal()) {
            return c.decode(sample(m, s), new Codecs.Desc(descBytes), null);
        }
        Last l = last.get(m);
        if (l != null && l.sample == s && l.descIndex == descIndex) {
            return l.image;
        }
        long sync = m.syncSampleAtOrBefore(s);
        long from;
        if (l != null && l.descIndex == descIndex && l.sample < s && l.sample >= sync) {
            from = l.sample + 1;
        } else {
            l = new Last();
            l.descIndex = descIndex;
            l.desc = new Codecs.Desc(descBytes);
            from = Math.max(1, sync);
        }
        BufferedImage img = l.image;
        for (long i = from; i <= s; i++) {
            if (m.runs.get(m.runOfSample(i)).descIndex != descIndex) {
                continue;
            }
            img = c.decode(sample(m, i), l.desc, img);
        }
        l.sample = s;
        l.image = img;
        last.put(m, l);
        return img;
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
