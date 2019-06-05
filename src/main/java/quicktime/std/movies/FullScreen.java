
package quicktime.std.movies;

import java.awt.Dimension;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.GDevice;
import quicktime.qd.QDDimension;
import quicktime.std.StdQTException;


public final class FullScreen implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(FullScreen.class);
                    }
                });
            }
        }.establish();
    }

    public FullScreen() {
        if (!QTSession.canDoFullScreen()) {
            throw new SecurityException("FullScreenWindow not allowed");
        }
    }

    private int restoreState = 0;

    /**
     * @deprecated
     */
    public Dimension getMainScreenSize() throws StdQTException {
        QDDimension localQDDimension = getSizeOfMainScreen();
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    public QDDimension getSizeOfMainScreen() throws StdQTException {
        end();

        int[] arrayOfInt = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];

        arrayOfShort1[0] = 0;
        arrayOfShort2[0] = 0;

        StdQTException.checkError(BeginFullScreen(arrayOfInt, 0, arrayOfShort1, arrayOfShort2, 0, 0, 8));

        this.restoreState = arrayOfInt[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    /**
     * @deprecated
     */
    public Dimension getScreenSize(GDevice paramGDevice) throws StdQTException {
        QDDimension localQDDimension = getSizeOfScreen(paramGDevice);
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    public QDDimension getSizeOfScreen(GDevice paramGDevice) throws StdQTException {
        end();

        int[] arrayOfInt = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];

        arrayOfShort1[0] = 0;
        arrayOfShort2[0] = 0;

        StdQTException
                .checkError(BeginFullScreen(arrayOfInt, QTObject.ID(paramGDevice), arrayOfShort1, arrayOfShort2, 0, 0, 8));

        this.restoreState = arrayOfInt[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    /**
     * @deprecated
     */
    public Dimension preflightSize(GDevice paramGDevice, int paramInt1, int paramInt2) throws StdQTException {
        QDDimension localQDDimension = getPreflightSize(paramGDevice, paramInt1, paramInt2);
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    public QDDimension getPreflightSize(GDevice paramGDevice, int paramInt1, int paramInt2) throws StdQTException {
        end();

        int[] arrayOfInt = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];

        arrayOfShort1[0] = ((short) paramInt1);
        arrayOfShort2[0] = ((short) paramInt2);

        StdQTException
                .checkError(BeginFullScreen(arrayOfInt, QTObject.ID(paramGDevice), arrayOfShort1, arrayOfShort2, 0, 0, 8));

        this.restoreState = arrayOfInt[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    /**
     * @deprecated
     */
    public Dimension begin(int paramInt) throws StdQTException {
        QDDimension localQDDimension = beginFullScreen(paramInt);
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    public QDDimension beginFullScreen(int paramInt) throws StdQTException {
        end();

        int[] arrayOfInt = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];

        arrayOfShort1[0] = 0;
        arrayOfShort2[0] = 0;

        StdQTException.checkError(BeginFullScreen(arrayOfInt, 0, arrayOfShort1, arrayOfShort2, 0, 0, paramInt));

        this.restoreState = arrayOfInt[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    /**
     * @deprecated
     */
    public Dimension begin(GDevice paramGDevice, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        QDDimension localQDDimension = beginFullScreen(paramGDevice, paramInt1, paramInt2, paramInt3);
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    public QDDimension beginFullScreen(GDevice paramGDevice,
                                       int paramInt1,
                                       int paramInt2,
                                       int paramInt3) throws StdQTException {
        end();

        int[] arrayOfInt = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];

        arrayOfShort1[0] = ((short) paramInt1);
        arrayOfShort2[0] = ((short) paramInt2);

        StdQTException.checkError(BeginFullScreen(arrayOfInt,
                                                  QTObject.ID(paramGDevice),
                                                  arrayOfShort1,
                                                  arrayOfShort2,
                                                  0,
                                                  0,
                                                  paramInt3));

        this.restoreState = arrayOfInt[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    public void endFullScreen() throws StdQTException {
        end();
    }

    public void end() throws StdQTException {
        if (this.restoreState != 0) {
            int i = EndFullScreen(this.restoreState, 0);
            StdQTException.checkError(i);
        }
        this.restoreState = 0;
    }

    protected void finalize() throws Throwable {
        end();
        super.finalize();
    }

    private static native short BeginFullScreen(int[] paramArrayOfInt,
                                                int paramInt1,
                                                short[] paramArrayOfShort1,
                                                short[] paramArrayOfShort2,
                                                int paramInt2,
                                                int paramInt3,
                                                int paramInt4);

    private static native short EndFullScreen(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/FullScreen.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
