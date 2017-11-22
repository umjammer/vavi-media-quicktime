
package quicktime.std.music;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


public final class ToneDescription extends QTByteObject implements QuickTimeLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 76;

    static final long serialVersionUID = 2107309866399025424L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ToneDescription.class);
                    }
                });
            }
        }.establish();
    }

    public ToneDescription() {
        super(kNativeSize);
    }

    public ToneDescription(int paramInt) throws QTException {
        this(NoteAllocator.getDefault(), paramInt);
    }

    public ToneDescription(NoteAllocator paramNoteAllocator, int paramInt) throws StdQTException {
        this();
        stuff(paramNoteAllocator, paramInt);
    }

    private ToneDescription(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[76];

        paramObjectInputStream.read(getBytes());
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        paramObjectOutputStream.write(getBytes());
    }

    public final int getSynthesizerType() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    public final void setSynthesizerType(int paramInt) {
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public final String getSynthesizerName() throws UtilException {
        return getPStringAt(4);
    }

    public final void setSynthesizerName(String paramString) throws UtilException {
        setPStringAt(4, 31, paramString);
    }

    public final String getInstrumentName() throws UtilException {
        return getPStringAt(36);
    }

    public final void setInstrumentName(String paramString) throws UtilException {
        setPStringAt(36, 31, paramString);
    }

    public final int getInstrumentNumber() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(68));
    }

    public final void setInstrumentNumber(int paramInt) {
        setIntAt(68, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public final int getGMNumber() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(72));
    }

    public final void setGMNumber(int paramInt) {
        setIntAt(72, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public final void stuff(NoteAllocator paramNoteAllocator, int paramInt) throws StdQTException {
        StdQTException.checkError(NAStuffToneDescription(QTObject.ID(paramNoteAllocator), paramInt, getBytes()));
    }

    public final void pickInstrument(final NoteAllocator na, final String prompt, final int flags) throws StdQTException {
        int i = 0;

        if (QTSession.isCurrentOS(4)) {
            i = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    int i = 0;
                    synchronized (QTNative.globalsLock) {
                        i = ToneDescription.NAPickInstrument(QTObject.ID(na),
                                                             0,
                                                             QTUtils.String2PString(prompt, 255),
                                                             ToneDescription.this.getBytes(),
                                                             flags,
                                                             0,
                                                             0,
                                                             0);
                    }

                    return i;
                }
            })).intValue();
            StdQTException.checkError(i);
        } else {
            synchronized (QTNative.globalsLock) {
                i = NAPickInstrument(QTObject.ID(na), 0, QTUtils.String2PString(prompt, 255), getBytes(), flags, 0, 0, 0);
            }

            StdQTException.checkError(i);
        }
    }

    int res = 0;

    public String toString() {
        String str = "";
        try {
            str = getInstrumentName();
        } catch (UtilException localUtilException) {
        }
        return getClass().getName() + "[synthType=" + QTUtils.fromOSType(getSynthesizerType()) + ",instNum="
               + Integer.toString(getInstrumentNumber()) + ",gmNum=" + Integer.toString(getGMNumber()) + ",instName=" + str
               + "]";
    }

    public Object clone() {
        return new ToneDescription(getBytes());
    }

    private static native int NAStuffToneDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int NAPickInstrument(int paramInt1,
                                               int paramInt2,
                                               byte[] paramArrayOfByte1,
                                               byte[] paramArrayOfByte2,
                                               int paramInt3,
                                               int paramInt4,
                                               int paramInt5,
                                               int paramInt6);

    private static native int GetMainEventLoop();
}
