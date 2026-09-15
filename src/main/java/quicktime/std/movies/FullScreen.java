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

/**
 * The FullScreen class implements the behavior for converting a screen to full screen mode and back to normal mode.
 * Unlike the C API the QTJava implementation of the FullScreen calls does NOT allow QuickTime to create the
 * window for you. The QTJava API allows you to put the specified screen into full screen mode and then
 * a java window can be used to fill the screen and allow the full capabilities of Java to use the entire screen.
 * The FullScreen can be used with Signed Applets also.
 * @see quicktime.app.display.FullScreenFrame
 */
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

    /**
     * This constructor creates a new FullScreen object.
     * If there is a SecurityManager and its checkTopLevelWindow() call
     * returns false the constructor will throw a SecurityException and the
     * services of the FullScreen class will not be available to the applet.
     */
    public FullScreen() {
        if (!QTSession.canDoFullScreen()) {
            throw new SecurityException("FullScreenWindow not allowed");
        }
    }

    private int restoreState = 0;

    /**
     * Deprecated.
     * <I>as of QTJava 4.0 see getSizeOfMainScreen</I>
     * Returns the current size in pixels of the main screen.
     * This method will not alter the display size of the screen.
     * QuickTime::BeginFullScreen()
     * @return the size of the main screen
     * @return {@link quicktime.std.StdQTException}
     */
    public Dimension getMainScreenSize() throws StdQTException {
        QDDimension localQDDimension = getSizeOfMainScreen();
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * Returns the current size in pixels of the main screen.
     * This method will not alter the display size of the screen.
     * QuickTime::BeginFullScreen()
     * @return the size of the main screen
     * @return {@link quicktime.std.StdQTException}
     */
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
     * Deprecated.
     * <I>as of QTJava 4.0 see getSizeOfScreen</I>
     * Returns the current size in pixels of the screen that is associated with the specified GDevice.
     * This method will not alter the display size of the screen.
     * QuickTime::BeginFullScreen()
     * @return the size of the specified screen
     * @return {@link quicktime.std.StdQTException}
     */
    public Dimension getScreenSize(GDevice paramGDevice) throws StdQTException {
        QDDimension localQDDimension = getSizeOfScreen(paramGDevice);
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * Returns the current size in pixels of the screen that is associated with the specified GDevice.
     * This method will not alter the display size of the screen.
     * QuickTime::BeginFullScreen()
     * @return the size of the specified screen
     * @return {@link quicktime.std.StdQTException}
     */
    public QDDimension getSizeOfScreen(GDevice paramGDevice) throws StdQTException {
        end();
        int[] arrayOfInt = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];
        arrayOfShort1[0] = 0;
        arrayOfShort2[0] = 0;
        StdQTException.checkError(BeginFullScreen(arrayOfInt, QTObject.ID(paramGDevice), arrayOfShort1, arrayOfShort2, 0, 0, 8));
        this.restoreState = arrayOfInt[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    /**
     * Deprecated.
     * <I>as of QTJava 4.0 see sizeOfScreen</I>
     * Returns the closest size to the specified size for the specified screen.
     * This method will not alter the display size of the screen.
     * QuickTime::BeginFullScreen()
     * @param gd the GDevice for the desired screen or null for the main screen
     * @param width the desired width or 0 to retrieve the current width in pixels of the specified screen
     * @param height the desired height or 0 to retrieve the current height in pixels of the specified screen
     * @return the size of the specified screen
     * @return {@link quicktime.std.StdQTException}
     */
    public Dimension preflightSize(GDevice paramGDevice, int paramInt1, int paramInt2) throws StdQTException {
        QDDimension localQDDimension = getPreflightSize(paramGDevice, paramInt1, paramInt2);
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * Returns the closest size to the specified size for the specified screen.
     * This method will not alter the display size of the screen.
     * QuickTime::BeginFullScreen()
     * @param gd the GDevice for the desired screen or null for the main screen
     * @param width the desired width or 0 to retrieve the current width in pixels of the specified screen
     * @param height the desired height or 0 to retrieve the current height in pixels of the specified screen
     * @return the size of the specified screen
     * @return {@link quicktime.std.StdQTException}
     */
    public QDDimension getPreflightSize(GDevice paramGDevice, int paramInt1, int paramInt2) throws StdQTException {
        end();
        int[] arrayOfInt = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];
        arrayOfShort1[0] = ((short) paramInt1);
        arrayOfShort2[0] = ((short) paramInt2);
        StdQTException.checkError(BeginFullScreen(arrayOfInt, QTObject.ID(paramGDevice), arrayOfShort1, arrayOfShort2, 0, 0, 8));
        this.restoreState = arrayOfInt[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    /**
     * Deprecated.
     * <I>as of QTJava 4.0 see beginFullScreen</I>
     * This method converts the main screen to full screen mode by hiding menu bars, control strips, task bars, etc.
     * QuickTime::BeginFullScreen()
     * @param flags controls the operation of this method.
     * @param {@link quicktime.std.StdQTException}
     */
    public Dimension begin(int paramInt) throws StdQTException {
        QDDimension localQDDimension = beginFullScreen(paramInt);
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * This method converts the main screen to full screen mode by hiding menu bars, control strips, task bars, etc.
     * QuickTime::BeginFullScreen()
     * @param flags controls the operation of this method.
     * @param {@link quicktime.std.StdQTException}
     */
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
     * Deprecated.
     * <I>as of QTJava 4.0 see beginFullScreen</I>
     * This method converts the main screen to full screen mode.
     * QuickTime::BeginFullScreen()
     * @param desiredWidth Specifies the resolution width to be displayed for that screen.
     * @param desiredHeight Specifies the resolution height to be displayed for that screen.
     * @param flags Controls the operation of this method.
     * @param {@link quicktime.std.StdQTException}
     */
    public Dimension begin(GDevice paramGDevice, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        QDDimension localQDDimension = beginFullScreen(paramGDevice, paramInt1, paramInt2, paramInt3);
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * This method converts the main screen to full screen mode.
     * QuickTime::BeginFullScreen()
     * @param desiredWidth Specifies the resolution width to be displayed for that screen.
     * @param desiredHeight Specifies the resolution height to be displayed for that screen.
     * @param flags Controls the operation of this method.
     * @param {@link quicktime.std.StdQTException}
     */
    public QDDimension beginFullScreen(GDevice paramGDevice, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        end();
        int[] arrayOfInt = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];
        arrayOfShort1[0] = ((short) paramInt1);
        arrayOfShort2[0] = ((short) paramInt2);
        StdQTException.checkError(BeginFullScreen(arrayOfInt, QTObject.ID(paramGDevice), arrayOfShort1, arrayOfShort2, 0, 0, paramInt3));
        this.restoreState = arrayOfInt[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    /**
     * This method converts the main screen from full screen mode to normal mode.
     * QuickTime::EndFullScreen()
     */
    public void endFullScreen() throws StdQTException {
        end();
    }

    /**
     * This method converts the main screen from full screen mode to normal mode.
     * QuickTime::EndFullScreen()
     */
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

    private static native short BeginFullScreen(int[] paramArrayOfInt, int paramInt1, short[] paramArrayOfShort1, short[] paramArrayOfShort2, int paramInt2, int paramInt3, int paramInt4);

    private static native short EndFullScreen(int paramInt1, int paramInt2);
}

