package quicktime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class QTExceptionTest {

    @Test
    void testErrorCodeToString() {
        assertEquals("memFullErr", QTException.errorCodeToString(-108));
    }

    @Test
    void testIsDrawingError() {
        assertEquals(0, QTException.isDrawingError(-8988));
        assertEquals(0, QTException.isDrawingError(-8976));
        assertEquals(-108, QTException.isDrawingError(-108));
    }

    @Test
    void testCheckError() {
        assertThrows(QTException.class, () -> QTException.checkError(-108));
    }

    @Test
    void testQTSessionJavaVersion() {
        assertTrue(QTSession.getJavaVersion() >= 65540);
        assertFalse(QTSession.isInitialized());
    }
}
