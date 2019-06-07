package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
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
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QTHandleRef.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Compresses a frame that is sourced by a PixMap from the supplied QDGraphics, returning
     * the compressed data.
     * QuickTime::SCCompressSequenceFrame()
     * @param src the PixMap's source QDGraphics.
     * @param srcRect you can specify to compress just a part of the pix map by specifying
     *  a rectangle - or pass in null to compress the entire pix map.
     * @return an image data object.
     * @return {@link quicktime.QTException}
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
     * QuickTime::SCRequestImageSettings()
     * @see {@link quicktime.std.qtcomponents.CompressionDialog#getInfoState()}
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
     * QuickTime::GetHandleSize
     * @return the size of the handle
     */
    public int getSize() {
        return GetHandleSize(_ID());
    }

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

    public final void copyToArray(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 1, paramInt2, paramArrayOfByte.length);
        getBytesAt(paramInt1, paramInt3, paramArrayOfByte, paramInt2);
    }

    public final void copyToArray(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 2, paramInt2, paramArrayOfShort.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfShort, paramInt2 * 2, paramInt3 * 2);
        if (!this.locked) {
            unlock();
        }
    }

    public final void copyToArray(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 2, paramInt2, paramArrayOfChar.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfChar, paramInt2 * 2, paramInt3 * 2);
        if (!this.locked) {
            unlock();
        }
    }

    public final void copyToArray(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 4, paramInt2, paramArrayOfInt.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfInt, paramInt2 * 4, paramInt3 * 4);
        if (!this.locked) {
            unlock();
        }
    }

    public final void copyToArray(int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 4, paramInt2, paramArrayOfFloat.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfFloat, paramInt2 * 4, paramInt3 * 4);
        if (!this.locked) {
            unlock();
        }
    }

    public final void copyToArray(int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 8, paramInt2, paramArrayOfLong.length);
        copyPointerToArray(lockAndDeref(0), paramInt1, paramArrayOfLong, paramInt2 * 8, paramInt3 * 8);
        if (!this.locked) {
            unlock();
        }
    }

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

    public final void copyFromArray(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 1, paramInt2, paramArrayOfByte.length);
        setBytesAt(paramInt1, paramInt3, paramArrayOfByte, paramInt2);
    }

    public final void copyFromArray(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 2, paramInt2, paramArrayOfShort.length);
        copyArrayToPointer(paramArrayOfShort, paramInt2 * 2, lockAndDeref(0), paramInt1, paramInt3 * 2);
        if (!this.locked) {
            unlock();
        }
    }

    public final void copyFromArray(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 2, paramInt2, paramArrayOfChar.length);
        copyArrayToPointer(paramArrayOfChar, paramInt2 * 2, lockAndDeref(0), paramInt1, paramInt3 * 2);
        if (!this.locked) {
            unlock();
        }
    }

    public final void copyFromArray(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 4, paramInt2, paramArrayOfInt.length);
        copyArrayToPointer(paramArrayOfInt, paramInt2 * 4, lockAndDeref(0), paramInt1, paramInt3 * 4);
        if (!this.locked) {
            unlock();
        }
    }

    public final void copyFromArray(int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 4, paramInt2, paramArrayOfFloat.length);
        copyArrayToPointer(paramArrayOfFloat, paramInt2 * 4, lockAndDeref(0), paramInt1, paramInt3 * 4);
        if (!this.locked) {
            unlock();
        }
    }

    public final void copyFromArray(int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3) {
        QTUtils.doBoundsChecks(paramInt1, getSize(), paramInt3, 8, paramInt2, paramArrayOfLong.length);
        copyArrayToPointer(paramArrayOfLong, paramInt2 * 8, lockAndDeref(0), paramInt1, paramInt3 * 8);
        if (!this.locked) {
            unlock();
        }
    }

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
     * QuickTime::HLockHi
     */
    public void lockHigh() {
        HLockHi(_ID());
        this.locked = true;
    }

    /**
     * Move the handle to high memory. Typically you call this before locking a small
     * handle that is going to be locked for a short period of time.
     * QuickTime::MoveHHi
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

    private static native int SCCompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2, short[] paramArrayOfShort);

    private static native int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int GetHandleSize(int paramInt);

    private static native void HLock(int paramInt);

    private static native void HLockHi(int paramInt);

    private static native void MoveHHi(int paramInt);

    private static native void HUnlock(int paramInt);

    private static native short MemError();

    private static native int NewHandleClear(int paramInt);

    private static native int NewHandle(int paramInt);

    private static native void BlockMove(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4);

    private static native void copyPointerToArray(int paramInt1, int paramInt2, short[] paramArrayOfShort, int paramInt3, int paramInt4);

    private static native void copyPointerToArray(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4);

    private static native void copyPointerToArray(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4);

    private static native void copyPointerToArray(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4);

    private static native void copyPointerToArray(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4);

    private static native void copyPointerToArray(int paramInt1, int paramInt2, double[] paramArrayOfDouble, int paramInt3, int paramInt4);

    private static native void copyArrayToPointer(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native void copyArrayToPointer(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native void copyArrayToPointer(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native void copyArrayToPointer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native void copyArrayToPointer(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native void copyArrayToPointer(long[] paramArrayOfLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native void copyArrayToPointer(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native byte getByteFromHandle(int paramInt1, int paramInt2);

    private static native short getShortFromHandle(int paramInt1, int paramInt2);

    private static native int getIntFromHandle(int paramInt1, int paramInt2);

    private static native long getLongFromHandle(int paramInt1, int paramInt2);

    private static native float getFloatFromHandle(int paramInt1, int paramInt2);

    private static native double getDoubleFromHandle(int paramInt1, int paramInt2);

    private static native void setByteInHandle(int paramInt1, int paramInt2, byte paramByte);

    private static native void setShortInHandle(int paramInt1, int paramInt2, short paramShort);

    private static native void setIntInHandle(int paramInt1, int paramInt2, int paramInt3);

    private static native void setLongInHandle(int paramInt1, int paramInt2, long paramLong);

    private static native void setFloatInHandle(int paramInt1, int paramInt2, float paramFloat);

    private static native void setDoubleInHandle(int paramInt1, int paramInt2, double paramDouble);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * QTHandleRef.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
