
package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.io.QTFile;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;


public class SynthesizerConnections extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 32;

    private QTPointer spec;

    private static final long serialVersionUID = -2499879404792104283L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SynthesizerConnections.class);
                    }
                });
            }
        }.establish();
    }

    public SynthesizerConnections() {
        this(32);
    }

    public SynthesizerConnections(QTFile paramQTFile) throws QTException {
        this();
        setSoundBankFile(paramQTFile);
    }

    protected SynthesizerConnections(int paramInt) {
        super(paramInt);
    }

    protected SynthesizerConnections(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        int i = paramObjectInputStream.readInt();
        this.bytes = new byte[i];
        paramObjectInputStream.read(this.bytes);

        for (int j = 0; j < 32; j += 4) {
            setIntInArray(this.bytes, j, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, j)));
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = getBytes();

        int i = getSize();
        paramObjectOutputStream.writeInt(i);

        for (int j = 0; j < 32; j += 2) {
            setIntInArray(arrayOfByte, j, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), j)));
        }

        paramObjectOutputStream.write(arrayOfByte);
    }

    public void setSoundBankFile(QTFile paramQTFile) throws QTException {
        byte[] arrayOfByte = paramQTFile.getFSSpec(true, 256);
        this.spec = new QTPointer(arrayOfByte);
        setIntAt(24, QTObject.ID(this.spec));
    }

    public QTFile getSoundBankFile() throws QTException {
        return QTFile.fromSynthesizerConnections(this);
    }

    public final int getClientID() {
        return getIntAt(0);
    }

    public final void setClientID(int paramInt) {
        setIntAt(0, paramInt);
    }

    public final int getInputID() {
        return getIntAt(8);
    }

    public final void setInputID(int paramInt) {
        setIntAt(8, paramInt);
    }

    public final int getOutputID() {
        return getIntAt(4);
    }

    public final void setOutputID(int paramInt) {
        setIntAt(4, paramInt);
    }

    public final int getMIDIChannel() {
        return getIntAt(12);
    }

    public final void setMIDIChannel(int paramInt) {
        setIntAt(12, paramInt);
    }

    public final int getFlags() {
        return getIntAt(16);
    }

    public final void setFlags(int paramInt) {
        setIntAt(16, paramInt);
    }

    public final int getUniqueID() {
        return getIntAt(20);
    }

    public final void setUniqueID(int paramInt) {
        setIntAt(20, paramInt);
    }

    public String toString() {
        return getClass().getName() + "[flags=" + getFlags()
               + (getFlags() > 0 ? ",clientID=" + QTUtils.fromOSType(getClientID()) + ",inputID="
                                   + QTUtils.fromOSType(getInputID()) + ",outputID=" + QTUtils.fromOSType(getOutputID())
                                   + ",uniqueID=" + QTUtils.fromOSType(getUniqueID()) + "]"
                                 : "]")
               + "";
    }

    public Object clone() {
        return new SynthesizerConnections(this.bytes);
    }

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /SynthesizerConnections.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
