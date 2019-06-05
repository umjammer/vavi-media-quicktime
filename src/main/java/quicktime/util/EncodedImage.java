
package quicktime.util;

public abstract interface EncodedImage {
    public static final int kRowBytesUnknown = -1;

    public abstract int getSize();

    public abstract byte getByte(int paramInt);

    public abstract short getShort(int paramInt);

    public abstract int getInt(int paramInt);

    public abstract int getRowBytes();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * EncodedImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
