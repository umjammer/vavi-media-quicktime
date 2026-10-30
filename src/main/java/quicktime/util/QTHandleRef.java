package quicktime.util;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.qtcomponents.CompressionDialog;
import quicktime.std.qtcomponents.SCInfo;
import quicktime.std.qtcomponents.SCSequence;


/**
 * A QTHandleRef is a "smart" handle in that it can be either a Reference to a handle
 * which is not disposable, or it can be a real "independent" handle which is
 * automatically disposed on finalization. This class is used as a generic base class for handle based objects. See QTHandle
 * for a user-level handle object.
 *
 * The default setting is for the Handle to be disposable - subclasses must use the
 * QTHandleRef(int, Object) constructor to create a non-disposable reference
 * For non-disposable handles there are two situations to look out for:
 * (1) Upon finalization the handle should NOT be disposed - it belongs to a parent struct
 * (2) The parent object from which the handle is obtained should not be allowed to
 * go away whilst the reference object is alive.
 * In both situations we must maintain a reference to the parent or owner object.
 * When defining a clone operation subclasses should define it as:
 * public Object clone() { return new QTHandleSubclass (makeAndCopyHandle (), null); }
 */
public class QTHandleRef extends QTObject implements QuickTimeLib, PrimitivesLib, InterfaceLib {

    /**
     * Compresses a frame that is sourced by a PixMap from the supplied QDGraphics, returning
     * the compressed data.
     * @see "QuickTime::SCCompressSequenceFrame()"
     * @param src the PixMap's source QDGraphics.
     * @param srcRect you can specify to compress just a part of the pix map by specifying
     *  a rectangle - or pass in null to compress the entire pix map.
     * @return an image data object.
     * @throws quicktime.QTException
     */
    public static SCInfo fromSCSequence(SCSequence scSequence, QDGraphics src, QDRect srcRect) throws QTException {
        if (src == null) {
            throw new StdQTException(-50);
        }
        int[] destArray1 = { 0 };
        int[] destArray2 = { 0 };
        short[] destArray = { 0 };
        int[] ai = { 0 };
        int[] ai1 = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(ai, ai1);
            SetGWorld(QTObject.ID(src), 0);
            i = SCCompressSequenceFrame(QTObject.ID(scSequence.getDialog()), QTObject.ID(src.getPixMap()), srcRect != null ? srcRect.getRect() : null, destArray1, destArray2, destArray);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(ai[0], ai1[0]);
            }
        }
        if (i == 1) {
            throw new StdQTException(-128);
        }
        StdQTException.checkError(i);
        return new SCInfo(new QTHandleRef(destArray1[0], scSequence, false), destArray2[0], destArray[0]);
    }

    /**
     * Retrieves all of the settings for a dialog.
     * @see "QuickTime::SCRequestImageSettings()"
     * @see quicktime.std.qtcomponents.CompressionDialog#getInfoState()
     */
    public static QTHandleRef fromCompressionDialogState(CompressionDialog ci) throws QTException {
        int[] destArray = { 0 };
        StdQTException.checkError(SCGetInfo(QTObject.ID(ci), 1936946273, destArray));
        return new QTHandleRef(destArray[0], null, false);
    }

    protected QTHandleRef(int size, boolean clear) throws QTException {
        super(allocate(size, clear));
        UtilException.checkError(MemError());
        this.locked = false;
    }

    protected QTHandleRef(int nativeRep, Object ref, boolean locked) {
        super(nativeRep, ref);
        this.locked = locked;
    }

    protected QTHandleRef(int handleOffset, Object ref, boolean locked, int destOffset) {
        super(handleOffset, ref, destOffset);
        this.locked = locked;
    }

    private static int allocate(int size, boolean clear) throws UtilException {
        int i = clear ? NewHandleClear(size) : NewHandle(size);
        if (i == 0)
            throw new UtilException(-108);
        return i;
    }

    private transient boolean locked = false;

    /**
     * This method returns the size (number of bytes) of the handle
     * @see "QuickTime::GetHandleSize"
     * @return the size of the handle
     */
    public int getSize() {
        return GetHandleSize(_ID());
    }

    /**
     * Returns a copy of the data of the handle as a byte array
     * @return a byte array - copy of handle data
     */
    public byte[] getBytes() {
        int i = getSize();
        byte[] destArray = new byte[i];
        getBytesAt(0, i, destArray, 0);
        return destArray;
    }

    protected final void getBytesAt(int handleOffset, int destOffset, byte[] destArray, int length) {
        copyPointerToArray(lockAndDeref(0), handleOffset, destArray, length, destOffset);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient byte[] copy that copies length bytes from the handle to the
     * byte array.
     * @param handleOffset the byte offset from the handle to copy the bytes from
     * @param destArray the destination byte array
     * @param destOffset the offset within the byte array to start copying to
     * @param length the number of bytes to copy
     */
    public final void copyToArray(int handleOffset, byte[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 1, destOffset, destArray.length);
        getBytesAt(handleOffset, length, destArray, destOffset);
    }

    /**
     * An efficient short[] copy that copies length shorts from the handle to the
     * short array.
     * @param handleOffset the byte offset from the handle to copy the shorts from
     * @param destArray the destination short array
     * @param destOffset the offset within the short array to start copying to
     * @param length the number of shorts to copy
     */
    public final void copyToArray(int handleOffset, short[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 2, destOffset, destArray.length);
        copyPointerToArray(lockAndDeref(0), handleOffset, destArray, destOffset * 2, length * 2);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient char[] copy that copies length chars from the handle to the
     * char array.
     * @param handleOffset the byte offset from the handle to copy the chars from
     * @param destArray the destination char array
     * @param destOffset the offset within the char array to start copying to
     * @param length the number of chars to copy
     */
    public final void copyToArray(int handleOffset, char[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 2, destOffset, destArray.length);
        copyPointerToArray(lockAndDeref(0), handleOffset, destArray, destOffset * 2, length * 2);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient int[] copy that copies length ints from the handle to the
     * int array.
     * @param handleOffset the byte offset from the handle to copy the ints from
     * @param destArray the destination int array
     * @param destOffset the offset within the int array to start copying to
     * @param length the number of ints to copy
     */
    public final void copyToArray(int handleOffset, int[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 4, destOffset, destArray.length);
        copyPointerToArray(lockAndDeref(0), handleOffset, destArray, destOffset * 4, length * 4);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient float[] copy that copies length floats from the handle to the
     * float array.
     * @param handleOffset the byte offset from the handle to copy the floats from
     * @param destArray the destination float array
     * @param destOffset the offset within the float array to start copying to
     * @param length the number of floats to copy
     */
    public final void copyToArray(int handleOffset, float[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 4, destOffset, destArray.length);
        copyPointerToArray(lockAndDeref(0), handleOffset, destArray, destOffset * 4, length * 4);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient long[] copy that copies length longs from the handle to the
     * long array.
     * @param handleOffset the byte offset from the handle to copy the longs from
     * @param destArray the destination long array
     * @param destOffset the offset within the long array to start copying to
     * @param length the number of longs to copy
     */
    public final void copyToArray(int handleOffset, long[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 8, destOffset, destArray.length);
        copyPointerToArray(lockAndDeref(0), handleOffset, destArray, destOffset * 8, length * 8);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient double[] copy that copies length doubles from the handle to the
     * double array.
     * @param handleOffset the byte offset from the handle to copy the doubles from
     * @param destArray the destination double array
     * @param destOffset the offset within the double array to start copying to
     * @param length the number of doubles to copy
     */
    public final void copyToArray(int handleOffset, double[] destArray, int destOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 8, destOffset, destArray.length);
        copyPointerToArray(lockAndDeref(0), handleOffset, destArray, destOffset * 8, length * 8);
        if (!this.locked)
            unlock();
    }

    protected final void setBytesAt(int handleOffset, int destOffset, byte[] destArray, int length) {
        copyArrayToPointer(destArray, length, lockAndDeref(0), handleOffset, destOffset);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient byte[] copy that copies length bytes from the byte array to
     * the handle.
     * @param handleOffset how many bytes offset from the base handle to copy to.
     * @param srcArray the byte[] to copy
     * @param srcOffset the nth index within the byte [] to start copying from
     * @param length how many elements of the byte array to copy
     */
    public final void copyFromArray(int handleOffset, byte[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 1, srcOffset, srcArray.length);
        setBytesAt(handleOffset, length, srcArray, srcOffset);
    }

    /**
     * An efficient short[] copy that copies length shorts from the byte array to
     * the handle.
     * @param handleOffset how many bytes offset from the base handle to copy to.
     * @param srcArray the short[] to copy
     * @param srcOffset the nth index within the short array to start copying from
     * @param length how many elements of the short array to copy
     */
    public final void copyFromArray(int handleOffset, short[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 2, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 2, lockAndDeref(0), handleOffset, length * 2);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient char[] copy that copies length chars from the byte array to
     * the handle.
     * @param handleOffset how many bytes offset from the base handle to copy to.
     * @param srcArray the char[] to copy
     * @param srcOffset the nth index within the char array to start copying from
     * @param length how many elements of the char array to copy
     */
    public final void copyFromArray(int handleOffset, char[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 2, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 2, lockAndDeref(0), handleOffset, length * 2);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient int[] copy that copies length ints from the byte array to
     * the handle.
     * @param handleOffset how many bytes offset from the base handle to copy to.
     * @param srcArray the int[] to copy
     * @param srcOffset the nth index within the int array to start copying from
     * @param length how many elements of the int array to copy
     */
    public final void copyFromArray(int handleOffset, int[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 4, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 4, lockAndDeref(0), handleOffset, length * 4);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient float[] copy that copies length floats from the byte array to
     * the handle.
     * @param handleOffset how many bytes offset from the base handle to copy to.
     * @param srcArray the float[] to copy
     * @param srcOffset the nth index within the float array to start copying from
     * @param length how many elements of the float array to copy
     */
    public final void copyFromArray(int handleOffset, float[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 4, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 4, lockAndDeref(0), handleOffset, length * 4);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient long[] copy that copies length longs from the byte array to
     * the handle.
     * @param handleOffset how many bytes offset from the base handle to copy to.
     * @param srcArray the long[] to copy
     * @param srcOffset the nth index within the long array to start copying from
     * @param length how many elements of the long array to copy
     */
    public final void copyFromArray(int handleOffset, long[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 8, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 8, lockAndDeref(0), handleOffset, length * 8);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * An efficient double[] copy that copies length doubles from the byte array to
     * the handle.
     * @param handleOffset how many bytes offset from the base handle to copy to.
     * @param srcArray the double[] to copy
     * @param srcOffset the nth index within the double array to start copying from
     * @param length how many elements of the double array to copy
     */
    public final void copyFromArray(int handleOffset, double[] srcArray, int srcOffset, int length) {
        QTUtils.doBoundsChecks(handleOffset, getSize(), length, 8, srcOffset, srcArray.length);
        copyArrayToPointer(srcArray, srcOffset * 8, lockAndDeref(0), handleOffset, length * 8);
        if (!this.locked) {
            unlock();
        }
    }

    /**
     * checks this handles purgedness
     */
    public boolean inMemory() {
        return getIntFromPointer(_ID(), 0) != 0;
    }

    /**
     * Returns true if the handle is locked otherwise false.
     */
    public final boolean isLocked() {
        return this.locked;
    }

    /**
     * Locks the relocatable block.
     */
    public final void lock() {
        this.locked = true;
        doLock();
    }

    protected void doLock() {
        HLock(_ID());
    }

    /**
     * Unlocks the relocatable block.
     */
    public final void unlock() {
        this.locked = false;
        doUnlock();
    }

    /**
     * Moves the relocatable block to the top of the heap and locks it.
     * @see "QuickTime::HLockHi"
     */
    public void lockHigh() {
        HLockHi(_ID());
        this.locked = true;
    }

    /**
     * Move the handle to high memory. Typically you call this before locking a small
     * handle that is going to be locked for a short period of time.
     * @see "QuickTime::MoveHHi"
     */
    public final void moveHigh() {
        MoveHHi(_ID());
    }

    protected void doUnlock() {
        HUnlock(_ID());
    }

    /**
     * A String representation of the class.
     */
    public String toString() {
        return getClass().getName() + "[size=" + getSize() + "]";
    }

    protected final int lockAndDeref(int offset) {
        doLock();
        int i = getIntFromPointer(_ID(), 0);
        return i + offset;
    }

    protected final int makeAndCopyHandle() {
        return makeAndCopyHandle(0, GetHandleSize(_ID()));
    }

    protected int makeAndCopyHandle(int handleOffset, int destOffset) {
        int i = 0;
        try {
            i = allocate(destOffset, false);
        } catch (UtilException e) {
            throw new QTRuntimeException(e);
        }
        HLock(i);
        BlockMove(lockAndDeref(0) + handleOffset, getIntFromPointer(i, 0) + 0, destOffset);
        if (!this.locked)
            unlock();
        HUnlock(i);
        return i;
    }

    protected void copyFromHandle(int handleOffset, QTHandleRef qtHandleRef, int destOffset, int length) {
        BlockMove(qtHandleRef.lockAndDeref(0) + destOffset, getIntFromPointer(_ID(), 0) + handleOffset, length);
        if (!qtHandleRef.isLocked()) {
            qtHandleRef.unlock();
        }
    }

    /**
     * Locks the handle and returns a pointer object. You must keep the handle locked for
     * the lifetime of the returned pointer.
     * @return an object representing the pointer.
     */
    public QTPointerRef toQTPointer() {
        lock();
        return new QTPointerRef(lockAndDeref(0), getSize(), this);
    }

    /**
     * Locks the handle and returns a pointer object. The handle should remain locked
     * for the lifetime of the returned pointer. It will return a pointer that is
     * offset within the handle that has a valid access to size bytes from the offset
     * @param offset the number of bytes to offset within the handle
     * @param size the number of bytes the pointer has valid access to.
     * @return an object representing the pointer.
     */
    public QTPointerRef toQTPointer(int offset, int size) {
        offset = offset + size > getSize() ? getSize() - size : offset;
        lock();
        return new QTPointerRef(lockAndDeref(offset), offset, this);
    }

    protected byte getByteAt(int offset) {
        return getByteFromHandle(_ID(), offset);
    }

    protected short getShortAt(int offset) {
        return getShortFromHandle(_ID(), offset);
    }

    protected int getIntAt(int offset) {
        return getIntFromHandle(_ID(), offset);
    }

    protected long getLongAt(int offset) {
        return getLongFromHandle(_ID(), offset);
    }

    protected float getFloatAt(int offset) {
        return getFloatFromHandle(_ID(), offset);
    }

    protected double getDoubleAt(int offset) {
        return getDoubleFromHandle(_ID(), offset);
    }

    protected void setByteAt(int offset, byte value) {
        setByteInHandle(_ID(), offset, value);
    }

    protected void setShortAt(int offset, short value) {
        setShortInHandle(_ID(), offset, value);
    }

    protected void setIntAt(int handleOffset, int destOffset) {
        setIntInHandle(_ID(), handleOffset, destOffset);
    }

    protected void setLongAt(int offset, long value) {
        setLongInHandle(_ID(), offset, value);
    }

    protected void setFloatAt(int offset, float value) {
        setFloatInHandle(_ID(), offset, value);
    }

    protected void setDoubleAt(int offset, double value) {
        setDoubleInHandle(_ID(), offset, value);
    }

    protected int strlen(int offset) {
        int i = 0;
        while (getByteAt(offset++) != 0) i++;
        return i;
    }

    protected String getCStringAt(int offset) throws UtilException {
        int i = strlen(offset);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        byte[] destArray = new byte[i];
        getBytesAt(offset, i, destArray, 0);
        return new String(destArray);
    }

    protected void setCStringAt(int offset, int length, String str) throws UtilException {
        length = Math.min(length, str.length());
        if (length + offset > getSize()) {
            throw new UtilException(-50);
        }
        byte[] destArray = str.getBytes();
        setBytesAt(offset, length, destArray, 0);
        setByteAt(offset + length, (byte) 0);
    }

    protected String getPStringAt(int offset) throws UtilException {
        int i = getByteAt(offset++);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        if (i > 0) {
            byte[] destArray = new byte[i];
            getBytesAt(offset, i, destArray, 0);
            return new String(destArray);
        }
        return "";
    }

    protected void setPStringAt(int offset, int length, String str) throws UtilException {
        length = Math.min(Math.min(length, str.length()), 255);
        if (length + offset > getSize()) {
            throw new UtilException(-50);
        }
        setByteAt(offset++, (byte) length);
        byte[] destArray = str.getBytes();
        setBytesAt(offset, length, destArray, 0);
    }

    private static int SCCompressSequenceFrame(int handleOffset, int destOffset, byte[] srcArray, int[] destArray1, int[] destArray2, short[] destArray) {
        return ImageCompressionManager.SCCompressSequenceFrame(handleOffset, destOffset, srcArray, destArray1, destArray2, destArray);
    }

    private static int SCGetInfo(int handleOffset, int destOffset, int[] destArray) {
        return ImageCompressionManager.SCGetInfo(handleOffset, destOffset, destArray);
    }

    private static int GetHandleSize(int h) {
        return MemoryManager.GetHandleSize(h);
    }

    private static void HLock(int h) {
        MemoryManager.HLock(h);
    }

    private static void HLockHi(int h) {
        MemoryManager.HLockHi(h);
    }

    private static void MoveHHi(int h) {
        MemoryManager.MoveHHi(h);
    }

    private static void HUnlock(int h) {
        MemoryManager.HUnlock(h);
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static int NewHandleClear(int size) {
        return MemoryManager.NewHandleClear(size);
    }

    private static int NewHandle(int size) {
        return MemoryManager.NewHandle(size);
    }

    private static void BlockMove(int handleOffset, int destOffset, int length) {
        MemoryManager.BlockMove(handleOffset, destOffset, length);
    }

    private static void SetGWorld(int handleOffset, int destOffset) {
        QuickDrawManager.SetGWorld(handleOffset, destOffset);
    }

    private static void GetGWorld(int[] destArray1, int[] destArray2) {
        QuickDrawManager.GetGWorld(destArray1, destArray2);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, byte[] destArray, int length, int size) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, size);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, short[] destArray, int length, int size) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, size);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, char[] destArray, int length, int size) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, size);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, int[] destArray, int length, int size) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, size);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, float[] destArray, int length, int size) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, size);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, long[] destArray, int length, int size) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, size);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, double[] destArray, int length, int size) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, size);
    }

    private static void copyArrayToPointer(byte[] destArray, int handleOffset, int destOffset, int length, int size) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, size);
    }

    private static void copyArrayToPointer(short[] destArray, int handleOffset, int destOffset, int length, int size) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, size);
    }

    private static void copyArrayToPointer(char[] destArray, int handleOffset, int destOffset, int length, int size) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, size);
    }

    private static void copyArrayToPointer(int[] destArray, int handleOffset, int destOffset, int length, int size) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, size);
    }

    private static void copyArrayToPointer(float[] destArray, int handleOffset, int destOffset, int length, int size) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, size);
    }

    private static void copyArrayToPointer(long[] destArray, int handleOffset, int destOffset, int length, int size) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, size);
    }

    private static void copyArrayToPointer(double[] destArray, int handleOffset, int destOffset, int length, int size) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, size);
    }

    private static int getIntFromPointer(int handleOffset, int destOffset) {
        return MemoryManager.getIntFromPointer(handleOffset, destOffset);
    }

    private static byte getByteFromHandle(int handleOffset, int destOffset) {
        return MemoryManager.getByteFromHandle(handleOffset, destOffset);
    }

    private static short getShortFromHandle(int handleOffset, int destOffset) {
        return MemoryManager.getShortFromHandle(handleOffset, destOffset);
    }

    private static int getIntFromHandle(int handleOffset, int destOffset) {
        return MemoryManager.getIntFromHandle(handleOffset, destOffset);
    }

    private static long getLongFromHandle(int handleOffset, int destOffset) {
        return MemoryManager.getLongFromHandle(handleOffset, destOffset);
    }

    private static float getFloatFromHandle(int handleOffset, int destOffset) {
        return MemoryManager.getFloatFromHandle(handleOffset, destOffset);
    }

    private static double getDoubleFromHandle(int handleOffset, int destOffset) {
        return MemoryManager.getDoubleFromHandle(handleOffset, destOffset);
    }

    private static void setByteInHandle(int handleOffset, int destOffset, byte value) {
        MemoryManager.setByteInHandle(handleOffset, destOffset, value);
    }

    private static void setShortInHandle(int handleOffset, int destOffset, short value) {
        MemoryManager.setShortInHandle(handleOffset, destOffset, value);
    }

    private static void setIntInHandle(int handleOffset, int destOffset, int length) {
        MemoryManager.setIntInHandle(handleOffset, destOffset, length);
    }

    private static void setLongInHandle(int handleOffset, int destOffset, long value) {
        MemoryManager.setLongInHandle(handleOffset, destOffset, value);
    }

    private static void setFloatInHandle(int handleOffset, int destOffset, float value) {
        MemoryManager.setFloatInHandle(handleOffset, destOffset, value);
    }

    private static void setDoubleInHandle(int handleOffset, int destOffset, double value) {
        MemoryManager.setDoubleInHandle(handleOffset, destOffset, value);
    }
}
