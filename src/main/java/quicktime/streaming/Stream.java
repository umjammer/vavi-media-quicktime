package quicktime.streaming;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.util.QTPointerRef;

/**
 * The Stream class represents a broadcast stream used by a Presentation object
 */
public final class Stream extends QTObject implements QTStreamingLib {

    private static Object linkage;

    private static final Object owner = new Object();

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(Stream.class));
            }
        }
        new PrivelegedAction().establish();
    }

    Stream(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Create a new stream object
     * @see "QuickTime::QTSPresNewStream"
     * @param pres supervising Presentation object
     * @param dataType OSType specifying type of stream data
     * @param data data
     */
    public Stream(Presentation paramPresentation, int paramInt, QTPointerRef paramQTPointerRef) throws QTException {
        super(allocate(paramPresentation, paramInt, paramQTPointerRef, 0));
    }

    /**
     * Create a new stream object
     * @see "QuickTime::QTSPresNewStream"
     * @param pres supervising Presentation object
     * @param dataType OSType specifying type of stream data
     * @param data data
     * @param flags stream flags
     */
    public Stream(Presentation paramPresentation, int paramInt1, QTPointerRef paramQTPointerRef, int paramInt2) throws QTException {
        super(allocate(paramPresentation, paramInt1, paramQTPointerRef, paramInt2));
    }

    private static int allocate(Presentation paramPresentation, int paramInt1, QTPointerRef paramQTPointerRef, int paramInt2) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresNewStream(QTObject.ID(paramPresentation), paramInt1, QTObject.ID(paramQTPointerRef), paramQTPointerRef.getSize(), paramInt2, arrayOfInt[0]));
        return arrayOfInt[0];
    }

    /**
     * Retrieves the presentation object this stream is associated with
     * @return Presentation the presentation object
     * @see " QuickTime::QTSGetStreamPresentation"
     * @throws quicktime.QTException
     */
    public Presentation getPresentation() throws QTException {
        return new Presentation(QTSGetStreamPresentation(_ID()), this);
    }

    private static short QTSPresNewStream(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        return StreamingManager.QTSPresNewStream(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    }

    private static int QTSGetStreamPresentation(int paramInt) {
        return StreamingManager.QTSGetStreamPresentation(paramInt);
    }
}
