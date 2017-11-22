
package quicktime.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ByteEncodedImage extends QTByteObject implements EncodedImage, Cloneable {
    private transient int rowBytes;

    static final long serialVersionUID = -2074813762531832L;

    public static ByteEncodedImage fromByteArray(byte[] paramArrayOfByte) {
        return new ByteEncodedImage(paramArrayOfByte);
    }

    public static ByteEncodedImage fromByteArray(byte[] paramArrayOfByte, int paramInt) {
        return new ByteEncodedImage(paramArrayOfByte, paramInt);
    }

    public ByteEncodedImage(RawEncodedImage paramRawEncodedImage) {
        this(paramRawEncodedImage.getSize());
        paramRawEncodedImage.copyToArray(0, this.bytes, 0, this.bytes.length);
        this.rowBytes = paramRawEncodedImage.getRowBytes();
    }

    public ByteEncodedImage(int paramInt) {
        this(paramInt, -1);
    }

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

    public Object clone() {
        byte[] arrayOfByte = new byte[this.bytes.length];
        System.arraycopy(this.bytes, 0, arrayOfByte, 0, this.bytes.length);
        return new ByteEncodedImage(arrayOfByte, this.rowBytes);
    }

    public byte getByte(int paramInt) {
        if ((paramInt > getSize()) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getByteAt(paramInt);
    }

    public short getShort(int paramInt) {
        if ((paramInt > getSize() - 1) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getShortAt(paramInt);
    }

    public int getInt(int paramInt) {
        if ((paramInt > getSize() - 3) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getIntAt(paramInt);
    }

    public int getRowBytes() {
        return this.rowBytes;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * ByteEncodedImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
