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

    protected QTHandleRef(int paramInt1, Object paramObject, boolean paramBoolean, int paramInt2) {
        super(paramInt1, paramObject, paramInt2);
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

    protected final void getBytesAt(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) {
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfByte, paramInt3, paramInt2);
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
    public final void copyToArray(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 1, paramInt2, paramArrayOfByte.length);
        getBytesAt(paramInt1, paramInt3, paramArrayOfByte, paramInt2);
    }

    /**
     * An efficient short[] copy that copies length shorts from the handle to the
     * short array.
     * @param handleOffset the byte offset from the handle to copy the shorts from
     * @param destArray the destination short array
     * @param destOffset the offset within the short array to start copying to
     * @param length the number of shorts to copy
     */
    public final void copyToArray(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 2, paramInt2, paramArrayOfShort.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfShort, paramInt2 * 2, paramInt3 * 2);
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
    public final void copyToArray(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 2, paramInt2, paramArrayOfChar.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfChar, paramInt2 * 2, paramInt3 * 2);
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
    public final void copyToArray(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 4, paramInt2, paramArrayOfInt.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfInt, paramInt2 * 4, paramInt3 * 4);
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
    public final void copyToArray(int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 4, paramInt2, paramArrayOfFloat.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfFloat, paramInt2 * 4, paramInt3 * 4);
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
    public final void copyToArray(int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 8, paramInt2, paramArrayOfLong.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfLong, paramInt2 * 8, paramInt3 * 8);
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
    public final void copyToArray(int paramInt1, double[] paramArrayOfDouble, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 8, paramInt2, paramArrayOfDouble.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfDouble, paramInt2 * 8, paramInt3 * 8);
        if (!this.locked)
            unlock();
    }

    protected final void setBytesAt(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) {
        copyArrayToPointer(paramArrayOfByte, paramInt3, lockAndDeref(0), paramInt1, paramInt2);
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
    public final void copyFromArray(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 1, paramInt2, paramArrayOfByte.length);
        setBytesAt(paramInt1, paramInt3, paramArrayOfByte, paramInt2);
    }

    /**
     * An efficient short[] copy that copies length shorts from the byte array to
     * the handle.
     * @param handleOffset how many bytes offset from the base handle to copy to.
     * @param srcArray the short[] to copy
     * @param srcOffset the nth index within the short array to start copying from
     * @param length how many elements of the short array to copy
     */
    public final void copyFromArray(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 2, paramInt2, paramArrayOfShort.length);
        copyArrayToPointer(paramArrayOfShort, paramInt2 * 2, lockAndDeref(0), paramInt1, paramInt3 * 2);
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
    public final void copyFromArray(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 2, paramInt2, paramArrayOfChar.length);
        copyArrayToPointer(paramArrayOfChar, paramInt2 * 2, lockAndDeref(0), paramInt1, paramInt3 * 2);
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
    public final void copyFromArray(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 4, paramInt2, paramArrayOfInt.length);
        copyArrayToPointer(paramArrayOfInt, paramInt2 * 4, lockAndDeref(0), paramInt1, paramInt3 * 4);
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
    public final void copyFromArray(int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 4, paramInt2, paramArrayOfFloat.length);
        copyArrayToPointer(paramArrayOfFloat, paramInt2 * 4, lockAndDeref(0), paramInt1, paramInt3 * 4);
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
    public final void copyFromArray(int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 8, paramInt2, paramArrayOfLong.length);
        copyArrayToPointer(paramArrayOfLong, paramInt2 * 8, lockAndDeref(0), paramInt1, paramInt3 * 8);
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
    public final void copyFromArray(int paramInt1, double[] paramArrayOfDouble, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 8, paramInt2, paramArrayOfDouble.length);
        copyArrayToPointer(paramArrayOfDouble, paramInt2 * 8, lockAndDeref(0), paramInt1, paramInt3 * 8);
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

    protected int makeAndCopyHandle(int paramInt1, int paramInt2) {
        int i = 0;
        try {
            i = allocate(paramInt2, false);
        } catch (UtilException localUtilException) {
            throw new QTRuntimeException(localUtilException);
        }
        HLock(i);
        BlockMove(lockAndDeref(0) + paramInt1, getIntFromPointer(i, 0) + 0, paramInt2);
        if (!this.locked)
            unlock();
        HUnlock(i);
        return i;
    }

    protected void copyFromHandle(int paramInt1, QTHandleRef paramQTHandleRef, int paramInt2, int paramInt3) {
        BlockMove(paramQTHandleRef.lockAndDeref(0) + paramInt2, getIntFromPointer(_ID(), 0) + paramInt1, paramInt3);
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
    public QTPointerRef toQTPointer(int paramInt1, int paramInt2) {
        paramInt2 = paramInt2 + paramInt1 > getSize() ? getSize() - paramInt1 : paramInt2;
        lock();
        return new QTPointerRef(lockAndDeref(paramInt1), paramInt2, this);
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

    protected void setIntAt(int paramInt1, int paramInt2) {
        setIntInHandle(_ID(), paramInt1, paramInt2);
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

    protected void setCStringAt(int paramInt1, int paramInt2, String paramString) throws UtilException {
        paramInt2 = Math.min(paramInt2, paramString.length());
        if (paramInt2 + paramInt1 > getSize()) {
            throw new UtilException(-50);
        }
        byte[] arrayOfByte = paramString.getBytes();
        setBytesAt(paramInt1, paramInt2, arrayOfByte, 0);
        setByteAt(paramInt1 + paramInt2, (byte) 0);
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

    protected void setPStringAt(int paramInt1, int paramInt2, String paramString) throws UtilException {
        paramInt2 = Math.min(Math.min(paramInt2, paramString.length()), 255);
        if (paramInt2 + paramInt1 > getSize()) {
            throw new UtilException(-50);
        }
        setByteAt(paramInt1++, (byte) paramInt2);
        byte[] arrayOfByte = paramString.getBytes();
        setBytesAt(paramInt1, paramInt2, arrayOfByte, 0);
    }

    private static int SCCompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2, short[] paramArrayOfShort) {
        return ImageCompressionManager.SCCompressSequenceFrame(paramInt1, paramInt2, paramArrayOfByte, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfShort);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfInt);
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

    private static void BlockMove(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.BlockMove(paramInt1, paramInt2, paramInt3);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, short[] paramArrayOfShort, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfShort, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfChar, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfFloat, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfLong, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, double[] paramArrayOfDouble, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfDouble, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfShort, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfFloat, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(long[] paramArrayOfLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfLong, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyArrayToPointer(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfDouble, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static byte getByteFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getByteFromHandle(paramInt1, paramInt2);
    }

    private static short getShortFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getShortFromHandle(paramInt1, paramInt2);
    }

    private static int getIntFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromHandle(paramInt1, paramInt2);
    }

    private static long getLongFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getLongFromHandle(paramInt1, paramInt2);
    }

    private static float getFloatFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getFloatFromHandle(paramInt1, paramInt2);
    }

    private static double getDoubleFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getDoubleFromHandle(paramInt1, paramInt2);
    }

    private static void setByteInHandle(int paramInt1, int paramInt2, byte paramByte) {
        MemoryManager.setByteInHandle(paramInt1, paramInt2, paramByte);
    }

    private static void setShortInHandle(int paramInt1, int paramInt2, short paramShort) {
        MemoryManager.setShortInHandle(paramInt1, paramInt2, paramShort);
    }

    private static void setIntInHandle(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.setIntInHandle(paramInt1, paramInt2, paramInt3);
    }

    private static void setLongInHandle(int paramInt1, int paramInt2, long paramLong) {
        MemoryManager.setLongInHandle(paramInt1, paramInt2, paramLong);
    }

    private static void setFloatInHandle(int paramInt1, int paramInt2, float paramFloat) {
        MemoryManager.setFloatInHandle(paramInt1, paramInt2, paramFloat);
    }

    private static void setDoubleInHandle(int paramInt1, int paramInt2, double paramDouble) {
        MemoryManager.setDoubleInHandle(paramInt1, paramInt2, paramDouble);
    }
}

