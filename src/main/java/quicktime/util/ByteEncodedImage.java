package quicktime.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * ByteEncodedImages keep the encoded image data as an array of bytes.
 */
public class ByteEncodedImage extends QTByteObject implements EncodedImage, Cloneable {

    private transient int rowBytes;

    static final long serialVersionUID = -2074813762531832L;

    public static ByteEncodedImage fromByteArray(byte[] paramArrayOfByte) {
        return new ByteEncodedImage(paramArrayOfByte);
    }

    public static ByteEncodedImage fromByteArray(byte[] paramArrayOfByte, int paramInt) {
        return new ByteEncodedImage(paramArrayOfByte, paramInt);
    }

    /**
     * Creates a ByteEncodedImage from raw data. It will copy the data
     */
    public ByteEncodedImage(RawEncodedImage paramRawEncodedImage) {
        this(paramRawEncodedImage.getSize());
        paramRawEncodedImage.copyToArray(0, this.bytes, 0, this.bytes.length);
        this.rowBytes = paramRawEncodedImage.getRowBytes();
    }

    /**
     * Creates a new ByteEncodedImage of specified size.
     */
    public ByteEncodedImage(int paramInt) {
        this(paramInt, -1);
    }

    /**
     * Creates a new ByteEncodedImage of specified size with the specified rowBytes
     */
    public ByteEncodedImage(int paramInt1, int paramInt2) {
        super(paramInt1);
        this.rowBytes = paramInt2;
    }

    private ByteEncodedImage(byte[] paramArrayOfByte) {
        this(paramArrayOfByte, -1);
    }

    private ByteEncodedImage(byte[] paramArrayOfByte, int paramInt) {
        super(paramArrayOfByte);
        this.rowBytes = paramInt;
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        int i = paramObjectInputStream.readInt();
        this.bytes = new byte[i];
        paramObjectInputStream.read(getBytes());
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        int i = getSize();
        paramObjectOutputStream.writeInt(i);
        byte[] arrayOfByte = getBytes();
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Returns a copy of the current object
     */
    public Object clone() {
        byte[] arrayOfByte = new byte[this.bytes.length];
        System.arraycopy(this.bytes, 0, arrayOfByte, 0, this.bytes.length);
        return new ByteEncodedImage(arrayOfByte, this.rowBytes);
    }

    /**
     * Returns the byte at the specified offset
     * The offset is specified in bytes into the encoded image object.
     */
    public byte getByte(int paramInt) {
        if ((paramInt > getSize()) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getByteAt(paramInt);
    }

    /**
     * Returns the short at the specified offset
     * The offset is specified in bytes into the encoded image object.
     */
    public short getShort(int paramInt) {
        if ((paramInt > getSize() - 1) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getShortAt(paramInt);
    }

    /**
     * Returns the int at the specified offset
     * The offset is specified in bytes into the encoded image object.
     */
    public int getInt(int paramInt) {
        if ((paramInt > getSize() - 3) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getIntAt(paramInt);
    }

    /**
     * Returns either kRowBytesUnknown or the number of bytes per row
     * that the encoded image data is comprised of. This number will
     * at least be as big as width*pixelSize, and maybe larger.
     */
    public int getRowBytes() {
        return this.rowBytes;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * ByteEncodedImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
