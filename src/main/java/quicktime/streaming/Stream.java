
package quicktime.streaming;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.util.QTPointerRef;


public final class Stream extends QTObject implements QTStreamingLib {
    private static Object linkage;

    private static Object owner = new Object();

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }

        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Stream.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    Stream(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public Stream(Presentation paramPresentation, int paramInt, QTPointerRef paramQTPointerRef) throws QTException {
        super(allocate(paramPresentation, paramInt, paramQTPointerRef, 0));
    }

    public Stream(Presentation paramPresentation,
            int paramInt1,
            QTPointerRef paramQTPointerRef,
            int paramInt2) throws QTException {
        super(allocate(paramPresentation, paramInt1, paramQTPointerRef, paramInt2));
    }

    private static int allocate(Presentation paramPresentation,
                                int paramInt1,
                                QTPointerRef paramQTPointerRef,
                                int paramInt2) throws QTException {
        int[] arrayOfInt = {
            0
        };
        QTException.checkError(QTSPresNewStream(QTObject.ID(paramPresentation),
                                                paramInt1,
                                                QTObject.ID(paramQTPointerRef),
                                                paramQTPointerRef.getSize(),
                                                paramInt2,
                                                arrayOfInt[0]));
        return arrayOfInt[0];
    }

    public Presentation getPresentation() throws QTException {
        return new Presentation(QTSGetStreamPresentation(_ID()), this);
    }

    private static native short QTSPresNewStream(int paramInt1,
                                                 int paramInt2,
                                                 int paramInt3,
                                                 int paramInt4,
                                                 int paramInt5,
                                                 int paramInt6);

    private static native int QTSGetStreamPresentation(int paramInt);
}
