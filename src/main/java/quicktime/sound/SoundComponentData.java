
package quicktime.sound;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;


public final class SoundComponentData extends QTByteObject implements PrimitivesLib {
    private static Object linkage;

    public static final int kNativeSize = 28;

    private QTPointer buff;

    private static final long serialVersionUID = -7514870412490785051L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SoundComponentData.class);
                    }
                });
            }
        }.establish();
    }

    public SoundComponentData() {
        super(28);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[28];

        paramObjectInputStream.read(this.bytes);

        for (int i = 0; i < 8; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 8; i < 12; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        for (int i = 0;; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[28];

        for (int i = 0; i < 8; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        for (int i = 8; i < 12; i += 2) {
            setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        for (int i = 12; i < 28; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }

        setIntInArray(arrayOfByte, 20, 0);

        paramObjectOutputStream.write(arrayOfByte);
    }

    public final int getFlags() {
        return getIntAt(0);
    }

    public final void setFlags(int paramInt) {
        setIntAt(0, paramInt);
    }

    public final int getFormat() {
        return getIntAt(4);
    }

    public final void setFormat(int paramInt) {
        setIntAt(4, paramInt);
    }

    public final int getNumChannels() {
        return getShortAt(8);
    }

    public final void setNumChannels(int paramInt) {
        setShortAt(8, (short) paramInt);
    }

    public final int getSampleSize() {
        return getShortAt(10);
    }

    public final void setSampleSize(int paramInt) {
        setShortAt(10, (short) paramInt);
    }

    public final float getSampleRate() {
        return QTUtils.UFix2X(getIntAt(12));
    }

    public final void setSampleRate(float paramFloat) {
        setIntAt(12, QTUtils.X2UFix(paramFloat));
    }

    public final int getSampleCount() {
        return getIntAt(16);
    }

    public final void setSampleCount(int paramInt) {
        setIntAt(16, paramInt);
    }

    public final QTPointer getBuffer() {
        return QTPointer.fromSCData(this);
    }

    public final void setBuffer(QTPointer paramQTPointer) throws SoundException {
        if (paramQTPointer.getSize() < getSampleSize() / 8 * getSampleCount())
            throw new SoundException(-50);
        setIntAt(20, QTObject.ID(paramQTPointer));
        this.buff = paramQTPointer;
    }

    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",format=" + QTUtils.fromOSType(getFormat()) + ",numChannels="
               + getNumChannels() + ",sampleSize=" + getSampleSize() + ",sampleRate=" + getSampleRate() + ",sampleCount="
               + getSampleCount() + "]";
    }

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}
