
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;


public final class TextMediaHandler extends VisualMediaHandler implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(TextMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    TextMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public int addTextSample(QTPointerRef paramQTPointerRef,
                             int paramInt1,
                             int paramInt2,
                             int paramInt3,
                             QDColor paramQDColor1,
                             QDColor paramQDColor2,
                             int paramInt4,
                             QDRect paramQDRect,
                             int paramInt5,
                             int paramInt6,
                             int paramInt7,
                             int paramInt8,
                             QDColor paramQDColor3,
                             int paramInt9) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int i = 0;
        int[] arrayOfInt2 = {
            0
        };
        int[] arrayOfInt3 = {
            0
        };

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt2, arrayOfInt3);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = TextMediaAddTextSample(_ID(),
                                       QTObject.ID(paramQTPointerRef),
                                       paramQTPointerRef.getSize(),
                                       (short) paramInt1,
                                       (short) paramInt2,
                                       (short) paramInt3,
                                       paramQDColor1 != null ? paramQDColor1.getRGBColor() : null,
                                       paramQDColor2 != null ? paramQDColor2.getRGBColor() : null,
                                       (short) paramInt4,
                                       paramQDRect.getRect(),
                                       paramInt5,
                                       paramInt6,
                                       (short) paramInt7,
                                       (short) paramInt8,
                                       paramQDColor3 != null ? paramQDColor3.getRGBColor() : null,
                                       paramInt9,
                                       arrayOfInt1);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(arrayOfInt2[0], arrayOfInt3[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    private int addHiliteSample(int paramInt1, int paramInt2, QDColor paramQDColor, int paramInt3) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = TextMediaAddHiliteSample(_ID(),
                                         (short) paramInt1,
                                         (short) paramInt2,
                                         paramQDColor != null ? paramQDColor.getRGBColor() : null,
                                         paramInt3,
                                         arrayOfInt);

        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public FoundTextInfo findNextText(QTPointerRef paramQTPointerRef, int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];

        int i = TextMediaFindNextText(_ID(),
                                      QTObject.ID(paramQTPointerRef),
                                      paramQTPointerRef.getSize(),
                                      (short) paramInt1,
                                      paramInt2,
                                      arrayOfInt1,
                                      arrayOfInt2,
                                      arrayOfInt3);

        StdQTException.checkError(i);
        return new FoundTextInfo(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0]);
    }

    public void hiliteTextSample(int paramInt1, int paramInt2, int paramInt3, QDColor paramQDColor) throws StdQTException {
        int i = TextMediaHiliteTextSample(_ID(),
                                          paramInt1,
                                          (short) paramInt2,
                                          (short) paramInt3,
                                          paramQDColor != null ? paramQDColor.getRGBColor() : null);

        StdQTException.checkError(i);
    }

    public void setDropShadowOffset(QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = new int[1];
        arrayOfInt[0] = paramQDPoint.getPoint();
        StdQTException.checkError(TextMediaSetTextSampleData(_ID(), arrayOfInt, 1685221487));
    }

    public void setDropShadowTranslucency(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        arrayOfInt[0] = paramInt;
        int i = TextMediaSetTextSampleData(_ID(), arrayOfInt, 1685221492);
        StdQTException.checkError(i);
    }

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native int TextMediaAddTextSample(int paramInt1,
                                                     int paramInt2,
                                                     int paramInt3,
                                                     short paramShort1,
                                                     short paramShort2,
                                                     short paramShort3,
                                                     byte[] paramArrayOfByte1,
                                                     byte[] paramArrayOfByte2,
                                                     short paramShort4,
                                                     byte[] paramArrayOfByte3,
                                                     int paramInt4,
                                                     int paramInt5,
                                                     short paramShort5,
                                                     short paramShort6,
                                                     byte[] paramArrayOfByte4,
                                                     int paramInt6,
                                                     int[] paramArrayOfInt);

    private static native int TextMediaAddHiliteSample(int paramInt1,
                                                       short paramShort1,
                                                       short paramShort2,
                                                       byte[] paramArrayOfByte,
                                                       int paramInt2,
                                                       int[] paramArrayOfInt);

    private static native int TextMediaFindNextText(int paramInt1,
                                                    int paramInt2,
                                                    int paramInt3,
                                                    short paramShort,
                                                    int paramInt4,
                                                    int[] paramArrayOfInt1,
                                                    int[] paramArrayOfInt2,
                                                    int[] paramArrayOfInt3);

    private static native int TextMediaHiliteTextSample(int paramInt1,
                                                        int paramInt2,
                                                        short paramShort1,
                                                        short paramShort2,
                                                        byte[] paramArrayOfByte);

    private static native int TextMediaSetTextSampleData(int paramInt1, int[] paramArrayOfInt, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/TextMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
