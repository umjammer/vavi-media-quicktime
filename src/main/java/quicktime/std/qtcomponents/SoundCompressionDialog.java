
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;


public final class SoundCompressionDialog extends CompressionDialog implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SoundCompressionDialog.class);
                    }
                });
            }
        }.establish();
    }

    public SoundCompressionDialog() throws QTException {
        super(1936684398);
    }

    public float getInfoSampleRate() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SCGetInfo(_ID(), 1936945780, arrayOfInt));

        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    public void setInfoSampleRate(float paramFloat) throws StdQTException {
        int[] arrayOfInt = {
            QTUtils.X2Fix(paramFloat)
        };
        StdQTException.checkError(SCSetInfo(_ID(), 1936945780, arrayOfInt));
    }

    public int getInfoSampleSize() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        StdQTException.checkError(SCGetInfo(_ID(), 1936946035, arrayOfShort));

        return arrayOfShort[0];
    }

    public void setInfoSampleSize(int paramInt) throws StdQTException {
        short[] arrayOfShort = {
            (short) paramInt
        };
        StdQTException.checkError(SCSetInfo(_ID(), 1936946035, arrayOfShort));
    }

    public int getInfoChannelCount() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        StdQTException.checkError(SCGetInfo(_ID(), 1936941923, arrayOfShort));

        return arrayOfShort[0];
    }

    public void setInfoChannelCount(int paramInt) throws StdQTException {
        short[] arrayOfShort = {
            (short) paramInt
        };
        StdQTException.checkError(SCSetInfo(_ID(), 1936941923, arrayOfShort));
    }

    public void setInfoCompression(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            paramInt
        };
        StdQTException.checkError(SCSetInfo(_ID(), 1936941940, arrayOfInt));
    }

    public int getInfoCompression() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SCGetInfo(_ID(), 1936941940, arrayOfInt));

        return arrayOfInt[0];
    }

    public void setInfoCompressionList(int[] paramArrayOfInt) throws StdQTException {
        if (paramArrayOfInt == null) {
            StdQTException.checkError(SCSetInfo(_ID(), 1668577644, (int[]) null));
        } else {
            int i = NewHandle(paramArrayOfInt.length * 4);
            copyArrayToPointer(paramArrayOfInt, 0, getIntFromPointer(i, 0), 0, paramArrayOfInt.length * 4);
            int j = SCSetInfo(_ID(), 1668577644, i);
            DisposeHandle(i);
            StdQTException.checkError(j);
        }
    }

    public int[] getInfoCompressionList() throws StdQTException {
        int i = NewHandle(4);
        int[] arrayOfInt1 = {
            i
        };
        int j = SCGetInfo(_ID(), 1668577644, arrayOfInt1);
        int k = GetHandleSize(i);
        int[] arrayOfInt2 = null;
        if (k > 3) {
            arrayOfInt2 = new int[k / 4];
            copyPointerToArray(getIntFromPointer(i, 0), 0, arrayOfInt2, 0, k);
        }
        DisposeHandle(i);
        StdQTException.checkError(j);
        return arrayOfInt2;
    }

    private static native int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SCSetInfo(int paramInt1, int paramInt2, int paramInt3);

    private static native int SCSetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SCGetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native int SCSetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native int SCGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SCSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native void copyArrayToPointer(int[] paramArrayOfInt,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void DisposeHandle(int paramInt);

    private static native int NewHandle(int paramInt);

    private static native int GetHandleSize(int paramInt);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  int[] paramArrayOfInt,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/SoundCompressionDialog.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
