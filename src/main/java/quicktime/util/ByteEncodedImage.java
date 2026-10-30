package quicktime.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;


/**
 * ByteEncodedImages keep the encoded image data as an array of bytes.
 */
public class ByteEncodedImage extends QTByteObject implements EncodedImage, Cloneable {

    private transient int rowBytes;

    @Serial
    private static final long serialVersionUID = -2074813762531832L;

    /**
     * This will make a ByteEncodedImage from an array of bytes. It will not copy
     * the byte array but creates a proxy for the same bytes.
     */
    public static ByteEncodedImage fromByteArray(byte[] bytes) {
        return new ByteEncodedImage(bytes);
    }

    /**
     * This will make a ByteEncodedImage from an array of bytes. It will not copy
     * the byte array but creates a proxy for the same bytes.
     */
    public static ByteEncodedImage fromByteArray(byte[] bytes, int rowBytes) {
        return new ByteEncodedImage(bytes, rowBytes);
    }

    /**
     * Creates a ByteEncodedImage from raw data. It will copy the data
     */
    public ByteEncodedImage(RawEncodedImage rawEncodedImage) {
        this(rawEncodedImage.getSize());
        rawEncodedImage.copyToArray(0, this.bytes, 0, this.bytes.length);
        this.rowBytes = rawEncodedImage.getRowBytes();
    }

    /**
     * Creates a new ByteEncodedImage of specified size.
     */
    public ByteEncodedImage(int size) {
        this(size, -1);
    }

    /**
     * Creates a new ByteEncodedImage of specified size with the specified rowBytes
     */
    public ByteEncodedImage(int size, int rowBytes) {
        super(size);
        this.rowBytes = rowBytes;
    }

    private ByteEncodedImage(byte[] bytes) {
        this(bytes, -1);
    }

    private ByteEncodedImage(byte[] bytes, int rowBytes) {
        super(bytes);
        this.rowBytes = rowBytes;
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        int i = in.readInt();
        this.bytes = new byte[i];
        in.read(getBytes());
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        int i = getSize();
        out.writeInt(i);
        byte[] movie = getBytes();
        out.write(movie);
    }

    /**
     * Returns a copy of the current object
     */
    @Override
    public Object clone() {
        byte[] bytes = new byte[this.bytes.length];
        System.arraycopy(this.bytes, 0, bytes, 0, this.bytes.length);
        return new ByteEncodedImage(bytes, this.rowBytes);
    }

    /**
     * Returns the byte at the specified offset
     * The offset is specified in bytes into the encoded image object.
     */
    @Override
    public byte getByte(int offset) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getByteAt(offset);
    }

    /**
     * Returns the short at the specified offset
     * The offset is specified in bytes into the encoded image object.
     */
    @Override
    public short getShort(int offset) {
        if ((offset > getSize() - 1) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getShortAt(offset);
    }

    /**
     * Returns the int at the specified offset
     * The offset is specified in bytes into the encoded image object.
     */
    @Override
    public int getInt(int offset) {
        if ((offset > getSize() - 3) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getIntAt(offset);
    }

    /**
     * Returns either kRowBytesUnknown or the number of bytes per row
     * that the encoded image data is comprised of. This number will
     * at least be as big as width*pixelSize, and maybe larger.
     */
    @Override
    public int getRowBytes() {
        return this.rowBytes;
    }
}
