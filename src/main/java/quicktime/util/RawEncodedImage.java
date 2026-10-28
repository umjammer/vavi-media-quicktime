package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SpriteManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.PixMap;
import quicktime.std.StdQTException;
import quicktime.std.anim.Sprite;

/**
 * RawEncodedImages keep the encoded image data as a pointer to some block of memory.
 * This is a totally opaque representation of the encoded image - it does not provide
 * any means of directly manipulating the encoded data.
 */
public final class RawEncodedImage extends QTPointer implements QuickTimeLib, EncodedImage {

    private static Object linkage;

    private int rowBytes;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(RawEncodedImage.class));
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * This will return a RawEncodedImage object from a pointer. It will not copy
     * the pointer but will reference the same data.
     */
    public static RawEncodedImage fromQTPointer(QTPointerRef paramQTPointerRef) {
        return new RawEncodedImage(QTObject.ID(paramQTPointerRef), paramQTPointerRef.getSize(), paramQTPointerRef, -1);
    }

    /**
     * This will return a RawEncodedImage object from a handle. It will not copy
     * the handle but will reference the same data.
     */
    public static RawEncodedImage fromQTHandle(QTHandleRef paramQTHandleRef) {
        paramQTHandleRef.lock();
        return fromQTPointer(paramQTHandleRef.toQTPointer());
    }

    /**
     * Returns the actual Pixel data itself. This can then be passed around in ICM calls
     * that deal with pixel data.
     * @see "QuickTime::GetPixBaseAddr()"
     * @param pm the PixMap
     * @return the pixels
     * @see quicktime.qd.PixMap#getPixelData()
     */
    public static RawEncodedImage fromPixMap(PixMap pm) {
        int i = getShortFromHandle(QTObject.ID(pm), 10) - getShortFromHandle(QTObject.ID(pm), 6);
        int j = getShortFromHandle(QTObject.ID(pm), 4) & 0x3FFF;
        int k = i * j;
        int m = pm.isOffscreen() ? GetPixBaseAddr(QTObject.ID(pm)) : getIntFromHandle(QTObject.ID(pm), 0);
        return new RawEncodedImage(m, k, pm, j);
    }

    /**
     * This method gets the image data property of this sprite.
     * @see "QuickTime::GetSpriteProperty ()"
     * @return the image data.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.anim.Sprite#getImageData()
     */
    public static RawEncodedImage fromSprite(Sprite paramSprite) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GetSpriteProperty(QTObject.ID(paramSprite), 3, arrayOfInt);
        StdQTException.checkError(i);
        return new RawEncodedImage(arrayOfInt[0], paramSprite.getImageDataSize(), paramSprite, -1);
    }

    /**
     * This will return a RawEncodedImage object from a byte array. It will copy
     * the bytes.
     */
    public RawEncodedImage(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
        this.rowBytes = -1;
    }

    /**
     * This will return a RawEncodedImage object from an int array. It will copy
     * the ints.
     * @param ints the int array containing the image data
     */
    public RawEncodedImage(int[] ints) throws QTException {
        this(ints.length * 4, false);
        copyFromArray(0, ints, 0, ints.length);
    }

    /**
     * This will return a RawEncodedImage object from an int array. It will copy
     * the ints.
     * @param ints the int array containing the image data
     * @param rowBytes the row bytes of the image data
     */
    public RawEncodedImage(int[] ints, int rowBytes) throws QTException {
        this(ints.length * 4, false, rowBytes);
        copyFromArray(0, ints, 0, ints.length);
    }

    /**
     * Allocate the specified amount of bytes for containing encoded image data.
     * @param size the size of the pointer to create.
     * @param clear clear the pointer upon creation.
     */
    public RawEncodedImage(int size, boolean clear) throws QTException {
        super(size, clear);
        this.rowBytes = -1;
    }

    /**
     * Allocate the specified amount of bytes for containing encoded image data.
     * @param size the size of the pointer to create.
     * @param clear clear the pointer upon creation.
     * @param rowBytes the rowbytes of the encoded image.
     */
    public RawEncodedImage(int size, boolean clear, int rowBytes) throws QTException {
        super(size, clear);
        this.rowBytes = rowBytes;
    }

    private RawEncodedImage(int paramInt1, int paramInt2, Object paramObject, int paramInt3) {
        super(paramInt1, paramInt2, paramObject);
        this.rowBytes = paramInt3;
    }

    /**
     * Returns a copy of this object
     */
    @Override
    public Object clone() {
        return new RawEncodedImage(makeAndCopyPtr(0, getSize()), getSize(), null, this.rowBytes);
    }

    /**
     * Returns the byte at the specified offset
     * @param offset The offset is specified in bytes into the encoded image object.
     * @return a byte
     */
    @Override
    public byte getByte(int offset) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getByteAt(offset);
    }

    /**
     * Sets the byte at the specified offset
     * @param offset The offset is specified in bytes into the encoded image object.
     * @param value the new value.
     */
    public void setByte(int offset, byte value) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        setByteAt(offset, value);
    }

    /**
     * Returns the short at the specified offset
     * @param offset The offset is specified in bytes into the encoded image object.
     * @return a short
     */
    @Override
    public short getShort(int offset) {
        if ((offset > getSize() - 1) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getShortAt(offset);
    }

    /**
     * Sets the short at the specified offset
     * @param offset The offset is specified in bytes into the encoded image object.
     * @param value the new value.
     */
    public void setShort(int offset, short value) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        setShortAt(offset, value);
    }

    /**
     * Returns the int at the specified offset
     * @param offset The offset is specified in bytes into the encoded image object.
     * @return an int
     */
    @Override
    public int getInt(int offset) {
        if ((offset > getSize() - 3) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getIntAt(offset);
    }

    /**
     * Sets the int at the specified offset
     * @param offset The offset is specified in bytes into the encoded image object.
     * @param value the new value.
     */
    public void setInt(int offset, int value) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        setIntAt(offset, value);
    }

    /**
     * Sets an array of ints at the specified offset
     * @param offset The offset is specified in bytes into the encoded image object.
     * @param value an array of ints
     */
    public void setInts(int offset, int[] value) {
        if ((offset + value.length * 4 > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        copyArrayToPointer(value, 0, _ID(), offset, value.length * 4);
    }

    /**
     * Returns either kRowBytesUnknown or the number of bytes per row
     * that the encoded image data is comprised of. This number will
     * at least be as big as width*pixelSize, and maybe larger.
     * @return an int
     */
    @Override
    public int getRowBytes() {
        return this.rowBytes;
    }

    /**
     * Sets the row bytes of the encoded image data
     * @param rowBytes the rowBytes of the encoded image data
     */
    public void setRowBytes(int rowBytes) {
        this.rowBytes = rowBytes;
    }

    private static int GetPixBaseAddr(int paramInt) {
        return QuickDrawManager.GetPixBaseAddr(paramInt);
    }

    private static short GetSpriteProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SpriteManager.GetSpriteProperty(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short getShortFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getShortFromHandle(paramInt1, paramInt2);
    }

    private static int getIntFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromHandle(paramInt1, paramInt2);
    }

    private static void copyArrayToPointer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4);
    }
}

