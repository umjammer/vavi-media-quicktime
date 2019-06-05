
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

    public static SCInfo fromSCSequence(SCSequence paramSCSequence,
                                        QDGraphics paramQDGraphics,
                                        QDRect paramQDRect) throws QTException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        short[] arrayOfShort = {
            0
        };
        int[] arrayOfInt3 = {
            0
        };
        int[] arrayOfInt4 = {
            0
        };

        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt3, arrayOfInt4);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = SCCompressSequenceFrame(QTObject.ID(paramSCSequence.getDialog()),
                                        QTObject.ID(paramQDGraphics.getPixMap()),
                                        paramQDRect != null ? paramQDRect.getRect() : null,
                                        arrayOfInt1,
                                        arrayOfInt2,
                                        arrayOfShort);

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

    public static QTHandleRef fromCompressionDialogState(CompressionDialog paramCompressionDialog) throws QTException {
        int[] arrayOfInt = {
            0
        };
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

    public boolean inMemory() {
        return getIntFromPointer(_ID(), 0) != 0;
    }

    public final boolean isLocked() {
        return this.locked;
    }

    public final void lock() {
        this.locked = true;
        doLock();
    }

    protected void doLock() {
        HLock(_ID());
    }

    public final void unlock() {
        this.locked = false;
        doUnlock();
    }

    public void lockHigh() {
        HLockHi(_ID());
        this.locked = true;
    }

    public final void moveHigh() {
        MoveHHi(_ID());
    }

    protected void doUnlock() {
        HUnlock(_ID());
    }

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

    public QTPointerRef toQTPointer() {
        lock();
        return new QTPointerRef(lockAndDeref(0), getSize(), this);
    }

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
        while (getByteAt(paramInt++) != 0)
            i++;
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

    private static native int SCCompressSequenceFrame(int paramInt1,
                                                      int paramInt2,
                                                      byte[] paramArrayOfByte,
                                                      int[] paramArrayOfInt1,
                                                      int[] paramArrayOfInt2,
                                                      short[] paramArrayOfShort);

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

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  short[] paramArrayOfShort,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  char[] paramArrayOfChar,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  int[] paramArrayOfInt,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  float[] paramArrayOfFloat,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  long[] paramArrayOfLong,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  double[] paramArrayOfDouble,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyArrayToPointer(byte[] paramArrayOfByte,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyArrayToPointer(short[] paramArrayOfShort,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyArrayToPointer(char[] paramArrayOfChar,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyArrayToPointer(int[] paramArrayOfInt,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyArrayToPointer(float[] paramArrayOfFloat,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyArrayToPointer(long[] paramArrayOfLong,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyArrayToPointer(double[] paramArrayOfDouble,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

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
