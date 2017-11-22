
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;


public final class FlashMediaHandler extends VisualMediaHandler implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(FlashMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    FlashMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public void setPan(short paramShort1, short paramShort2) throws StdQTException {
        int i = FlashMediaSetPan(_ID(), paramShort1, paramShort2);
        StdQTException.checkError(i);
    }

    public void setZoom(short paramShort) throws StdQTException {
        int i = FlashMediaSetZoom(_ID(), paramShort);
        StdQTException.checkError(i);
    }

    public void setZoomRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws StdQTException {
        int i = FlashMediaSetZoomRect(_ID(), paramInt1, paramInt2, paramInt3, paramInt4);
        StdQTException.checkError(i);
    }

    public QDRect getRefConBounds(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int[] arrayOfInt3 = {
            0
        };
        int[] arrayOfInt4 = {
            0
        };
        int i = FlashMediaGetRefConBounds(_ID(), paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4);
        StdQTException.checkError(i);
        return new QDRect(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0] - arrayOfInt1[0], arrayOfInt4[0] - arrayOfInt2[0]);
    }

    public int getRefConID(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = FlashMediaGetRefConID(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public int idToRefCon(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = FlashMediaIDToRefCon(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public int getDisplayedFrameNumber() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = FlashMediaGetDisplayedFrameNumber(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public int frameNumberToMovieTime(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = FlashMediaFrameNumberToMovieTime(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public int frameLabelToMovieTime(QTPointerRef paramQTPointerRef) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = FlashMediaFrameLabelToMovieTime(_ID(), QTObject.ID(paramQTPointerRef), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public String getFlashVariable(String paramString1, String paramString2) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = FlashMediaGetFlashVariable(_ID(),
                                           QTUtils.String2CString(paramString1),
                                           QTUtils.String2CString(paramString2),
                                           arrayOfInt);
        StdQTException.checkError(i);
        myStringHandle localmyStringHandle = new myStringHandle(arrayOfInt[0], this);
        try {
            return localmyStringHandle.toJavaString();
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    public String getSupportedSwfVersion() throws StdQTException {
        byte[] arrayOfByte = {
            0
        };

        int i = FlashMediaGetSupportedSwfVersion(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        if (arrayOfByte.length == 1) {
            return new Byte(arrayOfByte[0]).toString();
        }
        return QTUtils.CString2String(arrayOfByte, 0);
    }

    public void setFlashVariable(String paramString1,
                                 String paramString2,
                                 String paramString3,
                                 boolean paramBoolean) throws StdQTException {
        int i = FlashMediaSetFlashVariable(_ID(),
                                           QTUtils.String2CString(paramString1),
                                           QTUtils.String2CString(paramString2),
                                           QTUtils.String2CString(paramString3),
                                           (byte) (paramBoolean == true ? 1 : 0));

        StdQTException.checkError(i);
    }

    private static native int FlashMediaSetPan(int paramInt, short paramShort1, short paramShort2);

    class myStringHandle extends StringHandle {
        protected myStringHandle(int paramInt, Object paramObject) {
            super(paramInt, paramObject, 1);
        }
    }

    private static native int FlashMediaSetZoom(int paramInt, short paramShort);

    private static native int FlashMediaSetZoomRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native int FlashMediaGetRefConBounds(int paramInt1,
                                                        int paramInt2,
                                                        int[] paramArrayOfInt1,
                                                        int[] paramArrayOfInt2,
                                                        int[] paramArrayOfInt3,
                                                        int[] paramArrayOfInt4);

    private static native int FlashMediaGetRefConID(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int FlashMediaIDToRefCon(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int FlashMediaGetDisplayedFrameNumber(int paramInt, int[] paramArrayOfInt);

    private static native int FlashMediaFrameNumberToMovieTime(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int FlashMediaFrameLabelToMovieTime(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int FlashMediaGetFlashVariable(int paramInt,
                                                         byte[] paramArrayOfByte1,
                                                         byte[] paramArrayOfByte2,
                                                         int[] paramArrayOfInt);

    private static native int FlashMediaSetFlashVariable(int paramInt,
                                                         byte[] paramArrayOfByte1,
                                                         byte[] paramArrayOfByte2,
                                                         byte[] paramArrayOfByte3,
                                                         byte paramByte);

    private static native int FlashMediaGetSupportedSwfVersion(int paramInt, byte[] paramArrayOfByte);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/FlashMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
