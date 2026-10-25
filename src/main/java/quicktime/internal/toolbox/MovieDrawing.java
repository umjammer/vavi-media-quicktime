/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Draws the frames of movies into their graphics worlds while the movies are tasked.
 */
final class MovieDrawing {

    private static final Logger logger = Logger.getLogger(MovieDrawing.class.getName());

    private MovieDrawing() {
    }

    static final int movieDrawingCallWhenChanged = 0;
    static final int movieDrawingCallAlways = 1;

    static void invalidate(MovieRec m) {
        m.drawnTime = -1;
    }

    /** draws the movie if its time changed since the last drawing */
    static void task(MovieRec m, MediaEngine engine) {
        if (!m.active || m.gworld == 0 || QuickDrawManager.portOrNull(m.gworld) == null) {
            return;
        }
        long t = m.time();
        boolean changed = t != m.drawnTime;
        if (changed) {
            try {
                BufferedImage frame = engine.frame(m, t);
                if (frame != null) {
                    // the frame is the natural bounds, the movie matrix maps it into the gworld
                    double[] n = MovieToolbox.naturalBounds(m);
                    double[][] mm = MatrixManager.toDoubles(m.matrix);
                    AffineTransform at = new AffineTransform(mm[0][0], mm[0][1], mm[1][0], mm[1][1], mm[2][0], mm[2][1]);
                    at.translate(n[0], n[1]);
                    at.scale((n[2] - n[0]) / frame.getWidth(), (n[3] - n[1]) / frame.getHeight());
                    QuickDrawManager.drawImage(m.gworld, frame, 0, 0, 0, 0, at);
                }
            } catch (IOException e) {
                logger.log(Level.FINE, e.getMessage(), e);
            }
            m.drawnTime = t;
        }
        if (m.drawingCompleteProc != 0 && (changed || m.drawingCompleteFlags == movieDrawingCallAlways)) {
            Closures.call(m.drawingCompleteProc, m.ref, m.drawingCompleteRefCon);
        }
    }
}
