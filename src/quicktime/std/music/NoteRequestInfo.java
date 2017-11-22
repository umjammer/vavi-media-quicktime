
package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public class NoteRequestInfo extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 8;

    private static final long serialVersionUID = -8181311485902882832L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(NoteRequestInfo.class);
                    }
                });
            }
        }.establish();
    }

    public NoteRequestInfo() {
        this(8, null);
    }

    public NoteRequestInfo(int paramInt) throws QTException {
        this(8, null);
        setPolyphony(paramInt);
    }

    NoteRequestInfo(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    NoteRequestInfo(int paramInt, Object paramObject) {
        super(paramInt);

        setPolyphony(1);
        setTypicalPolyphony(1.0F);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        int i = paramObjectInputStream.readInt();
        this.bytes = new byte[i];
        paramObjectInputStream.read(this.bytes);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = getBytes();

        int i = getSize();
        paramObjectOutputStream.writeInt(i);

        paramObjectOutputStream.write(arrayOfByte);
    }

    public final int getFlags() {
        return getByteAt(0);
    }

    public final void setFlags(int paramInt) {
        setByteAt(0, (byte) paramInt);
    }

    public final int getMIDIChannel() {
        int i = getByteAt(1);
        if ((i & 0x80) == 0)
            return 0;
        i &= 0x7F;
        if (i > 16)
            return 0;
        return i;
    }

    public final void setMIDIChannel(int paramInt) {
        paramInt |= 0x80;
        setByteAt(1, (byte) paramInt);
    }

    public final int getPolyphony() {
        return EndianOrder.flipBigEndianToNative16(getShortAt(2));
    }

    public final void setPolyphony(int paramInt) {
        setShortAt(2, EndianOrder.flipNativeToBigEndian16((short) paramInt));
    }

    public final float getTypicalPolyphony() {
        return QTUtils.Fix2X(EndianOrder.flipBigEndianToNative32(getIntAt(4)));
    }

    public final void setTypicalPolyphony(float paramFloat) {
        setIntAt(4, EndianOrder.flipNativeToBigEndian32(QTUtils.X2Fix(paramFloat)));
    }

    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",poly=" + getPolyphony() + ",typPoly=" + getTypicalPolyphony()
               + "]";
    }

    public Object clone() {
        return new NoteRequestInfo(getBytes());
    }

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /NoteRequestInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
