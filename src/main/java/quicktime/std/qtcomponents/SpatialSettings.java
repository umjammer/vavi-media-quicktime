package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.image.CodecComponent;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;

public final class SpatialSettings extends QTByteObject implements PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 14;

    @Serial
    private static final long serialVersionUID = -5046382978690696504L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SpatialSettings.class));
            }
        }.establish();
    }

    /**
     * Construct temporal settings to set the dialog display.
     * @param codecType -
     * @param codec -
     * @param depth the pixel depth
     * @param spatialQuality the quality setting
     */
    public SpatialSettings(int paramInt1, CodecComponent paramCodecComponent, int paramInt2, int paramInt3) {
        this();
        setCodecType(paramInt1);
        setCodec(paramCodecComponent);
        setDepth(paramInt2);
        setSpatialQuality(paramInt3);
    }

    SpatialSettings() {
        super(14);
    }

    /**
     * Constructor for building a spacial settings object from a byte array
     * (such as the one you can get from an atom container)
     */
    public SpatialSettings(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[14];
        paramObjectInputStream.read(this.bytes);
        int i = getShortFromArray(this.bytes, 0);
        int j = EndianOrder.flipBigEndianToNative32(i);
        setIntInArray(this.bytes, 0, j);
        short s1 = getShortFromArray(this.bytes, 8);
        short s2 = EndianOrder.flipBigEndianToNative16(s1);
        setShortInArray(this.bytes, 8, s2);
        int k = getShortFromArray(this.bytes, 10);
        int m = EndianOrder.flipBigEndianToNative32(k);
        setIntInArray(this.bytes, 0, m);
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[14];
        int i = getShortFromArray(getBytes(), 0);
        int j = EndianOrder.flipNativeToBigEndian32(i);
        setIntInArray(arrayOfByte, 0, j);
        short s1 = getShortFromArray(getBytes(), 8);
        short s2 = EndianOrder.flipNativeToBigEndian16(s1);
        setShortInArray(arrayOfByte, 8, s2);
        int k = getShortFromArray(getBytes(), 10);
        int m = EndianOrder.flipNativeToBigEndian32(k);
        setIntInArray(arrayOfByte, 0, m);
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Get the default Codec type in the dialog box.
     * @return the Codec type
     */
    public int getCodecType() {
        return getIntAt(0);
    }

    /**
     * Specifies the default Codec type in the dialog box.
     * @param codecType -
     */
    public void setCodecType(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * Get the default Codec in the dialog box.
     * @return the Codec
     */
    public CodecComponent getCodec() {
        return CodecComponent.fromSpatialSettings(this);
    }

    /**
     * Specifies the default Codec in the dialog box.
     * @param codec -
     */
    public void setCodec(CodecComponent paramCodecComponent) {
        setIntAt(4, QTObject.ID(paramCodecComponent));
    }

    /**
     * Get the default depth of the pop-up menu in the dialog box.
     * @return the pixel depth
     */
    public int getDepth() {
        return getShortAt(8);
    }

    /**
     * Specifies the default depth of the pop-up menu in the dialog box.
     * @param depth the pixel depth
     */
    public void setDepth(int paramInt) {
        setShortAt(8, (short) paramInt);
    }

    /**
     * Get the default spatial quality slider in the dialog box.
     * @return the quality setting
     */
    public int getSpatialQuality() {
        return getIntAt(10);
    }

    /**
     * Specifies the spatial quality slider in the dialog box.
     * @param spatialQuality the quality setting
     */
    public void setSpatialQuality(int paramInt) {
        setIntAt(10, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}

