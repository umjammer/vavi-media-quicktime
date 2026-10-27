package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

public final class TemporalSettings extends QTByteObject implements PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 12;

    @Serial
    private static final long serialVersionUID = -5866822543021494879L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(TemporalSettings.class));
            }
        }.establish();
    }

    /**
     * Construct temporal settings to set the dialog display.
     * @param temporalQuality the quality setting
     * @param frameRate the frame rate
     * @param keyFrameRate the key frame rate
     */
    public TemporalSettings(int paramInt1, float paramFloat, int paramInt2) {
        this();
        setTemporalQuality(paramInt1);
        setFrameRate(paramFloat);
        setKeyFrameRate(paramInt2);
    }

    TemporalSettings() {
        super(12);
    }

    /**
     * Constructor for building a temporal settings object from a byte array
     * (such as the one you can get from an atom container)
     */
    public TemporalSettings(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[12];
        paramObjectInputStream.read(this.bytes);
        for (int i = 0; i < 12; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[12];
        for (int i = 0; i < 12; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Get the default temporal quality slider in the dialog box.
     * @return the quality setting
     */
    public int getTemporalQuality() {
        return getIntAt(0);
    }

    /**
     * Specifies the temporal quality slider in the dialog box.
     * @param temporalQuality the quality setting
     */
    public void setTemporalQuality(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * Get the default frame rate in the dialog box.
     * @return the frame rate
     */
    public float getFrameRate() {
        return QTUtils.Fix2X(getIntAt(4));
    }

    /**
     * Specifies the frame rate in the dialog box.
     * @param frameRate the frame rate
     */
    public void setFrameRate(float paramFloat) {
        setIntAt(4, QTUtils.X2Fix(paramFloat));
    }

    /**
     * Get the default key frame rate in the dialog box.
     * @return the key frame rate
     */
    public int getKeyFrameRate() {
        return getIntAt(8);
    }

    /**
     * Specifies the key frame rate in the dialog box.
     * @param keyFrameRate the key frame rate
     */
    public void setKeyFrameRate(int paramInt) {
        setIntAt(8, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}

