
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDGraphics;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;


public class CDSequence extends QTObject implements QuickTimeLib {
    private static Object linkage;

    ImageDescription mImageDescription;

    protected QDGraphics cdPort;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(CDSequence.class);
                    }
                });
            }
        }.establish();
    }

    CDSequence(int paramInt, QDGraphics paramQDGraphics) throws QTException {
        super(paramInt);
        this.cdPort = paramQDGraphics;
    }

    public final ImageDescription getDescription() {
        return this.mImageDescription;
    }

    public final void setTimeBase(TimeBase paramTimeBase) throws StdQTException {
        StdQTException.checkError(CDSequenceSetTimeBase(_ID(), QTObject.ID(paramTimeBase)));
    }

    public boolean busy() throws StdQTException {
        int i = CDSequenceBusy(_ID());
        if (i < 0)
            StdQTException.checkError(i);
        return i != 0;
    }

    public final boolean equivalentImageDescription(ImageDescription paramImageDescription) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        StdQTException.checkError(CDSequenceEquivalentImageDescription(_ID(), QTObject.ID(paramImageDescription), arrayOfByte));

        return arrayOfByte[0] != 0;
    }

    private static native short CDSequenceSetTimeBase(int paramInt1, int paramInt2);

    private static native short CDSequenceBusy(int paramInt);

    private static native short CDSequenceEquivalentImageDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
}
