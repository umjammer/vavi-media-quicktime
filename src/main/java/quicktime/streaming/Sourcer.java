
package quicktime.streaming;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.std.comp.Component;
import quicktime.std.comp.ComponentDescription;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.util.QTPointer;


public class Sourcer extends Component implements QTStreamingLib {
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
                        return QTNative.linkNativeMethods(Sourcer.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    Sourcer(int paramInt) throws QTException {
        super(paramInt);
        initialize(null);
    }

    public Sourcer(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier);
        initialize(null);
    }

    public Sourcer(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        super(allocate(paramInt1, paramInt2, paramInt3));
        initialize(null);
    }

    public Sourcer(int paramInt1, int paramInt2, int paramInt3, SourcerInitParams paramSourcerInitParams) throws QTException {
        super(allocate(paramInt1, paramInt2, paramInt3));
        initialize(paramSourcerInitParams);
    }

    private static int allocate(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        ComponentDescription localComponentDescription = new ComponentDescription(paramInt1);
        localComponentDescription.setSubType(paramInt2);
        localComponentDescription.setManufacturer(paramInt3);
        ComponentIdentifier localComponentIdentifier = ComponentIdentifier.find(localComponentDescription);
        return QTObject.ID(localComponentIdentifier);
    }

    protected void initialize(SourcerInitParams paramSourcerInitParams) throws QTException {
        if (paramSourcerInitParams == null) {
            QTException.checkError(QTSSourcerInitialize(_ID(), 0));
        } else {
            QTException.checkError(QTSSourcerInitialize(_ID(), QTObject.ID(paramSourcerInitParams)));
        }
    }

    public void setInfo(int paramInt, QTPointer paramQTPointer) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), paramInt, QTObject.ID(paramQTPointer)));
    }

    public QTPointer getInfo(int paramInt) throws QTException {
        byte[] arrayOfByte = {
            0
        };
        QTException.checkError(QTSSourcerGetInfo(_ID(), paramInt, arrayOfByte));

        return new QTPointer(arrayOfByte);
    }

    public void setCallbackParams(SourcerCallbackParams paramSourcerCallbackParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1935893104, QTObject.ID(paramSourcerCallbackParams)));
    }

    public SourcerCallbackParams getCallbackParams() throws QTException {
        byte[] arrayOfByte = {
            0
        };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1935893104, arrayOfByte));

        return new SourcerCallbackParams(arrayOfByte);
    }

    public void setLoopParams(SourcerLoopParams paramSourcerLoopParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1819242352, QTObject.ID(paramSourcerLoopParams)));
    }

    public SourcerLoopParams getLoopParams() throws QTException {
        byte[] arrayOfByte = {
            0
        };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1819242352, arrayOfByte));

        return new SourcerLoopParams(arrayOfByte);
    }

    public void setSourcerTrackParams(SourcerTrackParams paramSourcerTrackParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1953653099, QTObject.ID(paramSourcerTrackParams)));
    }

    public SourcerTrackParams getSourcerTrackParams() throws QTException {
        byte[] arrayOfByte = {
            0
        };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1953653099, arrayOfByte));

        return new SourcerTrackParams(arrayOfByte);
    }

    public int idle(long paramLong, int paramInt) throws QTException {
        int[] arrayOfInt = {
            0
        };
        QTException.checkError(QTSSourcerIdle(_ID(), paramLong, paramInt, arrayOfInt));

        return arrayOfInt[0];
    }

    public void setEnable(boolean paramBoolean) throws QTException {
        setEnable(paramBoolean, 0);
    }

    public void setEnable(boolean paramBoolean, int paramInt) throws QTException {
        QTException.checkError(QTSSourcerSetEnable(_ID(), (byte) (paramBoolean == true ? 1 : 0), paramInt));
    }

    public boolean isEnable() throws QTException {
        return isEnable(0);
    }

    public boolean isEnable(int paramInt) throws QTException {
        byte[] arrayOfByte = {
            0
        };
        QTException.checkError(QTSSourcerGetEnable(_ID(), arrayOfByte, paramInt));

        return arrayOfByte[0] != 0;
    }

    public void setTimeScale(int paramInt) throws QTException {
        QTException.checkError(QTSSourcerSetTimeScale(_ID(), paramInt));
    }

    public int getTimeScale() throws QTException {
        int[] arrayOfInt = {
            0
        };
        QTException.checkError(QTSSourcerGetTimeScale(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    public static native int QTSSourcerInitialize(int paramInt1, int paramInt2);

    public static native int QTSSourcerSetInfo(int paramInt1, int paramInt2, int paramInt3);

    public static native int QTSSourcerGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    public static native int QTSSourcerIdle(int paramInt1, long paramLong, int paramInt2, int[] paramArrayOfInt);

    public static native int QTSSourcerSetEnable(int paramInt1, byte paramByte, int paramInt2);

    public static native int QTSSourcerGetEnable(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    public static native int QTSSourcerSetTimeScale(int paramInt1, int paramInt2);

    public static native int QTSSourcerGetTimeScale(int paramInt, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /Sourcer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
