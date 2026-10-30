package quicktime.util;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SpriteManager;
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

    private int rowBytes;

    /**
     * This will return a RawEncodedImage object from a pointer. It will not copy
     * the pointer but will reference the same data.
     */
    public static RawEncodedImage fromQTPointer(QTPointerRef nativeRep) {
        return new RawEncodedImage(QTObject.ID(nativeRep), nativeRep.getSize(), nativeRep, -1);
    }

    /**
     * This will return a RawEncodedImage object from a handle. It will not copy
     * the handle but will reference the same data.
     */
    public static RawEncodedImage fromQTHandle(QTHandleRef qtHandleRef) {
        qtHandleRef.lock();
        return fromQTPointer(qtHandleRef.toQTPointer());
    }

    /**
     * Returns the actual Pixel data itself. This can then be passed around in ICM calls
     * that deal with pixel data.
     *
     * @param pm the PixMap
     * @return the pixels
     * @see "QuickTime::GetPixBaseAddr()"
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
     *
     * @return the image data.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     * @see quicktime.std.anim.Sprite#getImageData()
     */
    public static RawEncodedImage fromSprite(Sprite sprite) throws StdQTException {
        int[] propertyValue = {0};
        int i = GetSpriteProperty(QTObject.ID(sprite), 3, propertyValue);
        StdQTException.checkError(i);
        return new RawEncodedImage(propertyValue[0], sprite.getImageDataSize(), sprite, -1);
    }

    /**
     * This will return a RawEncodedImage object from a byte array. It will copy
     * the bytes.
     */
    public RawEncodedImage(byte[] byteArray) throws QTException {
        super(byteArray);
        this.rowBytes = -1;
    }

    /**
     * This will return a RawEncodedImage object from an int array. It will copy
     * the ints.
     *
     * @param ints the int array containing the image data
     */
    public RawEncodedImage(int[] ints) throws QTException {
        this(ints.length * 4, false);
        copyFromArray(0, ints, 0, ints.length);
    }

    /**
     * This will return a RawEncodedImage object from an int array. It will copy
     * the ints.
     *
     * @param ints     the int array containing the image data
     * @param rowBytes the row bytes of the image data
     */
    public RawEncodedImage(int[] ints, int rowBytes) throws QTException {
        this(ints.length * 4, false, rowBytes);
        copyFromArray(0, ints, 0, ints.length);
    }

    /**
     * Allocate the specified amount of bytes for containing encoded image data.
     *
     * @param size  the size of the pointer to create.
     * @param clear clear the pointer upon creation.
     */
    public RawEncodedImage(int size, boolean clear) throws QTException {
        super(size, clear);
        this.rowBytes = -1;
    }

    /**
     * Allocate the specified amount of bytes for containing encoded image data.
     *
     * @param size     the size of the pointer to create.
     * @param clear    clear the pointer upon creation.
     * @param rowBytes the rowbytes of the encoded image.
     */
    public RawEncodedImage(int size, boolean clear, int rowBytes) throws QTException {
        super(size, clear);
        this.rowBytes = rowBytes;
    }

    private RawEncodedImage(int nativeRep, int size, Object ref, int rowBytes) {
        super(nativeRep, size, ref);
        this.rowBytes = rowBytes;
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
     *
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
     *
     * @param offset The offset is specified in bytes into the encoded image object.
     * @param value  the new value.
     */
    public void setByte(int offset, byte value) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        setByteAt(offset, value);
    }

    /**
     * Returns the short at the specified offset
     *
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
     *
     * @param offset The offset is specified in bytes into the encoded image object.
     * @param value  the new value.
     */
    public void setShort(int offset, short value) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        setShortAt(offset, value);
    }

    /**
     * Returns the int at the specified offset
     *
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
     *
     * @param offset The offset is specified in bytes into the encoded image object.
     * @param value  the new value.
     */
    public void setInt(int offset, int value) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        setIntAt(offset, value);
    }

    /**
     * Sets an array of ints at the specified offset
     *
     * @param offset The offset is specified in bytes into the encoded image object.
     * @param value  an array of ints
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
     *
     * @return an int
     */
    @Override
    public int getRowBytes() {
        return this.rowBytes;
    }

    /**
     * Sets the row bytes of the encoded image data
     *
     * @param rowBytes the rowBytes of the encoded image data
     */
    public void setRowBytes(int rowBytes) {
        this.rowBytes = rowBytes;
    }

    private static int GetPixBaseAddr(int pixMap) {
        return QuickDrawManager.GetPixBaseAddr(pixMap);
    }

    private static short GetSpriteProperty(int sprite, int propertyType, int[] propertyValue) {
        return SpriteManager.GetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static short getShortFromHandle(int h, int offset) {
        return MemoryManager.getShortFromHandle(h, offset);
    }

    private static int getIntFromHandle(int h, int offset) {
        return MemoryManager.getIntFromHandle(h, offset);
    }

    private static void copyArrayToPointer(int[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }
}
