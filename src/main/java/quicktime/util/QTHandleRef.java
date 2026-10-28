package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
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

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QTHandleRef.class));
            }
        }.establish();
    }

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
    public static SCInfo fromSCSequence(SCSequence paramSCSequence, QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        short[] arrayOfShort = { 0 };
        int[] arrayOfInt3 = { 0 };
        int[] arrayOfInt4 = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt3, arrayOfInt4);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = SCCompressSequenceFrame(QTObject.ID(paramSCSequence.getDialog()), QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect != null ? paramQDRect.getRect() : null, arrayOfInt1, arrayOfInt2, arrayOfShort);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(arrayOfInt3[0], arrayOfInt4[0]);
            }
        }
        if (i == 1) {
            throw new StdQTException(-128);
        }
        StdQTException.checkError(i);
        return new SCInfo(new QTHandleRef(arrayOfInt1[0], paramSCSequence, false), arrayOfInt2[0], arrayOfShort[0]);
    }

    /**
     * Retrieves all of the settings for a dialog.
     * @see "QuickTime::SCRequestImageSettings()"
     * @see quicktime.std.qtcomponents.CompressionDialog#getInfoState()
     */
    public static QTHandleRef fromCompressionDialogState(CompressionDialog paramCompressionDialog) throws QTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(QTObject.ID(paramCompressionDialog), 1936946273, arrayOfInt));
        return new QTHandleRef(arrayOfInt[0], null, false);
    }

    protected QTHandleRef(int paramInt, boolean paramBoolean) throws QTException {
        super(allocate(paramInt, paramBoolean));
        UtilException.checkError(MemError());
        this.locked = false;
    }

    protected QTHandleRef(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject);
        this.locked = paramBoolean;
    }

    protected QTHandleRef(int handleOffset, Object paramObject, boolean paramBoolean, int destOffset) {
        super(handleOffset, paramObject, destOffset);
        this.locked = paramBoolean;
    }

    private static int allocate(int paramInt, boolean paramBoolean) throws UtilException {
        int i = paramBoolean ? NewHandleClear(paramInt) : NewHandle(paramInt);
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
        byte[] arrayOfByte = new byte[i];
        getBytesAt(0, i, arrayOfByte, 0);
        return arrayOfByte;
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

    protected final int lockAndDeref(int paramInt) {
        doLock();
        int i = getIntFromPointer(_ID(), 0);
        return i + paramInt;
    }

    protected final int makeAndCopyHandle() {
        return makeAndCopyHandle(0, GetHandleSize(_ID()));
    }

    protected int makeAndCopyHandle(int handleOffset, int destOffset) {
        int i = 0;
        try {
            i = allocate(destOffset, false);
        } catch (UtilException localUtilException) {
            throw new QTRuntimeException(localUtilException);
        }
        HLock(i);
        BlockMove(lockAndDeref(0) + handleOffset, getIntFromPointer(i, 0) + 0, destOffset);
        if (!this.locked)
            unlock();
        HUnlock(i);
        return i;
    }

    protected void copyFromHandle(int handleOffset, QTHandleRef paramQTHandleRef, int destOffset, int length) {
        BlockMove(paramQTHandleRef.lockAndDeref(0) + destOffset, getIntFromPointer(_ID(), 0) + handleOffset, length);
        if (!paramQTHandleRef.isLocked()) {
            paramQTHandleRef.unlock();
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

    protected byte getByteAt(int paramInt) {
        return getByteFromHandle(_ID(), paramInt);
    }

    protected short getShortAt(int paramInt) {
        return getShortFromHandle(_ID(), paramInt);
    }

    protected int getIntAt(int paramInt) {
        return getIntFromHandle(_ID(), paramInt);
    }

    protected long getLongAt(int paramInt) {
        return getLongFromHandle(_ID(), paramInt);
    }

    protected float getFloatAt(int paramInt) {
        return getFloatFromHandle(_ID(), paramInt);
    }

    protected double getDoubleAt(int paramInt) {
        return getDoubleFromHandle(_ID(), paramInt);
    }

    protected void setByteAt(int paramInt, byte paramByte) {
        setByteInHandle(_ID(), paramInt, paramByte);
    }

    protected void setShortAt(int paramInt, short paramShort) {
        setShortInHandle(_ID(), paramInt, paramShort);
    }

    protected void setIntAt(int handleOffset, int destOffset) {
        setIntInHandle(_ID(), handleOffset, destOffset);
    }

    protected void setLongAt(int paramInt, long paramLong) {
        setLongInHandle(_ID(), paramInt, paramLong);
    }

    protected void setFloatAt(int paramInt, float paramFloat) {
        setFloatInHandle(_ID(), paramInt, paramFloat);
    }

    protected void setDoubleAt(int paramInt, double paramDouble) {
        setDoubleInHandle(_ID(), paramInt, paramDouble);
    }

    protected int strlen(int paramInt) {
        int i = 0;
        while (getByteAt(paramInt++) != 0) i++;
        return i;
    }

    protected String getCStringAt(int paramInt) throws UtilException {
        int i = strlen(paramInt);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        byte[] arrayOfByte = new byte[i];
        getBytesAt(paramInt, i, arrayOfByte, 0);
        return new String(arrayOfByte);
    }

    protected void setCStringAt(int handleOffset, int destOffset, String paramString) throws UtilException {
        destOffset = Math.min(destOffset, paramString.length());
        if (destOffset + handleOffset > getSize()) {
            throw new UtilException(-50);
        }
        byte[] arrayOfByte = paramString.getBytes();
        setBytesAt(handleOffset, destOffset, arrayOfByte, 0);
        setByteAt(handleOffset + destOffset, (byte) 0);
    }

    protected String getPStringAt(int paramInt) throws UtilException {
        int i = getByteAt(paramInt++);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        if (i > 0) {
            byte[] arrayOfByte = new byte[i];
            getBytesAt(paramInt, i, arrayOfByte, 0);
            return new String(arrayOfByte);
        }
        return "";
    }

    protected void setPStringAt(int handleOffset, int destOffset, String paramString) throws UtilException {
        destOffset = Math.min(Math.min(destOffset, paramString.length()), 255);
        if (destOffset + handleOffset > getSize()) {
            throw new UtilException(-50);
        }
        setByteAt(handleOffset++, (byte) destOffset);
        byte[] arrayOfByte = paramString.getBytes();
        setBytesAt(handleOffset, destOffset, arrayOfByte, 0);
    }

    private static int SCCompressSequenceFrame(int handleOffset, int destOffset, byte[] srcArray, int[] destArray1, int[] destArray2, short[] destArray) {
        return ImageCompressionManager.SCCompressSequenceFrame(handleOffset, destOffset, srcArray, destArray1, destArray2, destArray);
    }

    private static int SCGetInfo(int handleOffset, int destOffset, int[] destArray) {
        return ImageCompressionManager.SCGetInfo(handleOffset, destOffset, destArray);
    }

    private static int GetHandleSize(int paramInt) {
        return MemoryManager.GetHandleSize(paramInt);
    }

    private static void HLock(int paramInt) {
        MemoryManager.HLock(paramInt);
    }

    private static void HLockHi(int paramInt) {
        MemoryManager.HLockHi(paramInt);
    }

    private static void MoveHHi(int paramInt) {
        MemoryManager.MoveHHi(paramInt);
    }

    private static void HUnlock(int paramInt) {
        MemoryManager.HUnlock(paramInt);
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static int NewHandleClear(int paramInt) {
        return MemoryManager.NewHandleClear(paramInt);
    }

    private static int NewHandle(int paramInt) {
        return MemoryManager.NewHandle(paramInt);
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

    private static void copyPointerToArray(int handleOffset, int destOffset, byte[] destArray, int length, int paramInt4) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, paramInt4);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, short[] destArray, int length, int paramInt4) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, paramInt4);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, char[] destArray, int length, int paramInt4) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, paramInt4);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, int[] destArray, int length, int paramInt4) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, paramInt4);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, float[] destArray, int length, int paramInt4) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, paramInt4);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, long[] destArray, int length, int paramInt4) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, paramInt4);
    }

    private static void copyPointerToArray(int handleOffset, int destOffset, double[] destArray, int length, int paramInt4) {
        MemoryManager.copyPointerToArray(handleOffset, destOffset, destArray, length, paramInt4);
    }

    private static void copyArrayToPointer(byte[] destArray, int handleOffset, int destOffset, int length, int paramInt4) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, paramInt4);
    }

    private static void copyArrayToPointer(short[] destArray, int handleOffset, int destOffset, int length, int paramInt4) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, paramInt4);
    }

    private static void copyArrayToPointer(char[] destArray, int handleOffset, int destOffset, int length, int paramInt4) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, paramInt4);
    }

    private static void copyArrayToPointer(int[] destArray, int handleOffset, int destOffset, int length, int paramInt4) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, paramInt4);
    }

    private static void copyArrayToPointer(float[] destArray, int handleOffset, int destOffset, int length, int paramInt4) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, paramInt4);
    }

    private static void copyArrayToPointer(long[] destArray, int handleOffset, int destOffset, int length, int paramInt4) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, paramInt4);
    }

    private static void copyArrayToPointer(double[] destArray, int handleOffset, int destOffset, int length, int paramInt4) {
        MemoryManager.copyArrayToPointer(destArray, handleOffset, destOffset, length, paramInt4);
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

    private static void setByteInHandle(int handleOffset, int destOffset, byte paramByte) {
        MemoryManager.setByteInHandle(handleOffset, destOffset, paramByte);
    }

    private static void setShortInHandle(int handleOffset, int destOffset, short paramShort) {
        MemoryManager.setShortInHandle(handleOffset, destOffset, paramShort);
    }

    private static void setIntInHandle(int handleOffset, int destOffset, int length) {
        MemoryManager.setIntInHandle(handleOffset, destOffset, length);
    }

    private static void setLongInHandle(int handleOffset, int destOffset, long paramLong) {
        MemoryManager.setLongInHandle(handleOffset, destOffset, paramLong);
    }

    private static void setFloatInHandle(int handleOffset, int destOffset, float paramFloat) {
        MemoryManager.setFloatInHandle(handleOffset, destOffset, paramFloat);
    }

    private static void setDoubleInHandle(int handleOffset, int destOffset, double paramDouble) {
        MemoryManager.setDoubleInHandle(handleOffset, destOffset, paramDouble);
    }
}

