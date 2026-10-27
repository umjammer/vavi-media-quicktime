/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.qd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quicktime.QTSession;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * QDGraphicsTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-10-28 nsano initial version <br>
 */
class QDGraphicsTest {

    @BeforeEach
    void setUp() throws Exception {
        QTSession.open();
    }

    @AfterEach
    void tearDown() {
        QTSession.close();
    }

    @Test
    void test1() throws Exception {
        QDGraphics qdg = new QDGraphics(new QDRect());
        assertTrue(qdg.isOffscreen());
    }
}
