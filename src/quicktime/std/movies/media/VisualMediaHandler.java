
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.util.QTPointer;


public class VisualMediaHandler extends MediaHandler implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(VisualMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    protected VisualMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public final void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        StdQTException.checkError(MediaSetGraphicsMode(_ID(),
                                                       paramGraphicsMode.getGraphicsMode(),
                                                       paramGraphicsMode.getColor().getRGBColor()));
    }

    public final GraphicsMode getGraphicsMode() throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[6];
        StdQTException.checkError(MediaGetGraphicsMode(_ID(), arrayOfInt, arrayOfByte));

        return new GraphicsMode(arrayOfInt[0], QDColor.fromArray(arrayOfByte, 6));
    }

    public final boolean getTrackOpaque() throws StdQTException {
        byte[] arrayOfByte = {
            0
        };
        StdQTException.checkError(MediaGetTrackOpaque(_ID(), arrayOfByte));

        return arrayOfByte[0] != 0;
    }

    public final int hitTestForTargetRefCon(int paramInt, QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(MediaHitTestForTargetRefCon(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfInt));

        return arrayOfInt[0];
    }

    public final boolean hitTestTargetRefCon(int paramInt1, int paramInt2, QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = {
            0
        };
        StdQTException.checkError(MediaHitTestTargetRefCon(_ID(), paramInt1, paramInt2, paramQDPoint.getPoint(), arrayOfByte));

        return arrayOfByte[0] != 0;
    }

    public void setPublicInfo(int paramInt, QTPointer paramQTPointer) throws StdQTException {
        StdQTException.checkError(MediaSetPublicInfo(_ID(), paramInt, QTObject.ID(paramQTPointer), paramQTPointer.getSize()));
    }

    public QTPointer getPublicInfo(int paramInt) throws QTException {
        byte[] arrayOfByte = {
            0
        };
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(MediaGetPublicInfo(_ID(), paramInt, arrayOfByte, arrayOfInt));

        return new QTPointer(arrayOfByte);
    }

    private static native int MediaHitTestForTargetRefCon(int paramInt1, long paramLong, int paramInt2, int[] paramArrayOfInt);

    private static native int MediaHitTestTargetRefCon(int paramInt1,
                                                       int paramInt2,
                                                       int paramInt3,
                                                       int paramInt4,
                                                       byte[] paramArrayOfByte);

    private static native int MediaSetGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MediaGetGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte);

    private static native int MediaGetTrackOpaque(int paramInt, byte[] paramArrayOfByte);

    private static native int MediaSetPublicInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int MediaGetPublicInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/VisualMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
