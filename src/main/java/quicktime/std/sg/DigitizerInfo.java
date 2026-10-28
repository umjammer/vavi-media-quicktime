package quicktime.std.sg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class DigitizerInfo extends QTByteObject implements PrimitivesLib, QuickTimeLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 42;

    @Serial
    private static final long serialVersionUID = -1283193746361395107L;

    DigitizerInfo() {
        super(kNativeSize);
    }

    DigitizerInfo(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        paramObjectInputStream.read(this.bytes);
        setShortInArray(this.bytes, 0, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, 0)));
        for (int i = 4; i < 18; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 28; i < 38; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[kNativeSize];
        setShortInArray(arrayOfByte, 0, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), 0)));
        for (int i = 2; i < 18; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        setIntInArray(arrayOfByte, 20, 0);
        setIntInArray(arrayOfByte, 24, 0);
        for (int i = 28; i < 38; i += 2) {
            setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        setIntInArray(arrayOfByte, 38, 0);
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Indicates the capabilities of the video digitizer component with respect to the input video signal.
     * These flags are discussed in ÒCapability FlagsÓ beginning on page 8-14 of Inside Macintosh: QuickTime Components.
     * @return input video signal features.
     */
    public int getInputCapabilityFlags() {
        return getIntAt(2);
    }

    /**
     * Indicates the capabilities of the video digitizer component with respect to the output digitized video
     * information. These flags are discussed in ÒCapability FlagsÓ beginning on page 8-14 of
     * Inside Macintosh: QuickTime Components.
     * @return output digitized video data features of digitizer component
     */
    public int getOutputCapabilityFlags() {
        return getIntAt(6);
    }

    /**
     * Indicates the smallest height value the digitizer component can accommodate in its destination.
     * @return the minimum Destination width
     */
    public short getMinDestHeight() {
        return getShortAt(28);
    }

    /**
     * Indicates the smallest width value the digitizer component can accommodate in its destination.
     * @return the minimum Destination width
     */
    public short getMinDestWidth() {
        return getShortAt(30);
    }

    /**
     * Indicates the largest height value the digitizer component can accommodate in its destination.
     * @return largest resizable height
     */
    public short getmaxDestHeight() {
        return getShortAt(32);
    }

    /**
     * Indicates the maximum width the digitizer component can accommodate in its destination.
     * @return largest resizable width
     */
    public short getmaxDestWidth() {
        return getShortAt(34);
    }

    /**
     * Indicates the number of blend levels the video digitizer component supports.
     * @return number of blend levels supported (2 if 1-bit mask)
     */
    public short getblendLevels() {
        return getShortAt(36);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new DigitizerInfo(this.bytes);
    }

    /**
     * Print information about this object.
     * @return a string representing this object
     */
    public String toString() {
        return getClass().getName() +
                " MinDestHeight: " + getMinDestHeight() +
                " MinDestWidth: " + getMinDestWidth() +
                " \nmaxDestHeight: " + getmaxDestHeight() +
                " MaxDestWidth: " + getmaxDestWidth() +
                " \nblendLevels: " + getblendLevels() +
                " InputCapabilityFlags: " + getInputCapabilityFlags() +
                " \nOutputCapabilityFlags: " + getOutputCapabilityFlags();
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}
