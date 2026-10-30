package quicktime.std.movies;

import java.awt.Dimension;

import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.PlatformManager;
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
 * @see "quicktime.app.display.FullScreenFrame"
 */
public final class FullScreen implements QuickTimeLib {

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
     * @deprecated
     * <I>as of QTJava 4.0 see getSizeOfMainScreen</I>
     * Returns the current size in pixels of the main screen.
     * This method will not alter the display size of the screen.
     * @see "QuickTime::BeginFullScreen()"
     * @return the size of the main screen
     * @throws quicktime.std.StdQTException
     */
    public Dimension getMainScreenSize() throws StdQTException {
        QDDimension dimension = getSizeOfMainScreen();
        return new Dimension(dimension.getWidth(), dimension.getHeight());
    }

    /**
     * Returns the current size in pixels of the main screen.
     * This method will not alter the display size of the screen.
     * @see "QuickTime::BeginFullScreen()"
     * @return the size of the main screen
     * @throws quicktime.std.StdQTException
     */
    public QDDimension getSizeOfMainScreen() throws StdQTException {
        end();
        int[] restoreState = new int[1];
        short[] desiredWidth = new short[1];
        short[] desiredHeight = new short[1];
        desiredWidth[0] = 0;
        desiredHeight[0] = 0;
        StdQTException.checkError(BeginFullScreen(restoreState, 0, desiredWidth, desiredHeight, 0, 0, 8));
        this.restoreState = restoreState[0];
        return new QDDimension(desiredWidth[0], desiredHeight[0]);
    }

    /**
     * @deprecated
     * <I>as of QTJava 4.0 see getSizeOfScreen</I>
     * Returns the current size in pixels of the screen that is associated with the specified GDevice.
     * This method will not alter the display size of the screen.
     * @see "QuickTime::BeginFullScreen()"
     * @return the size of the specified screen
     * @throws quicktime.std.StdQTException
     */
    public Dimension getScreenSize(GDevice whichGD) throws StdQTException {
        QDDimension dimension = getSizeOfScreen(whichGD);
        return new Dimension(dimension.getWidth(), dimension.getHeight());
    }

    /**
     * Returns the current size in pixels of the screen that is associated with the specified GDevice.
     * This method will not alter the display size of the screen.
     * @see "QuickTime::BeginFullScreen()"
     * @return the size of the specified screen
     * @throws quicktime.std.StdQTException
     */
    public QDDimension getSizeOfScreen(GDevice whichGD) throws StdQTException {
        end();
        int[] restoreState = new int[1];
        short[] desiredWidth = new short[1];
        short[] desiredHeight = new short[1];
        desiredWidth[0] = 0;
        desiredHeight[0] = 0;
        StdQTException.checkError(BeginFullScreen(restoreState, QTObject.ID(whichGD), desiredWidth, desiredHeight, 0, 0, 8));
        this.restoreState = restoreState[0];
        return new QDDimension(desiredWidth[0], desiredHeight[0]);
    }

    /**
     * @deprecated
     * <I>as of QTJava 4.0 see sizeOfScreen</I>
     * Returns the closest size to the specified size for the specified screen.
     * This method will not alter the display size of the screen.
     * @see "QuickTime::BeginFullScreen()"
     * @param gd the GDevice for the desired screen or null for the main screen
     * @param width the desired width or 0 to retrieve the current width in pixels of the specified screen
     * @param height the desired height or 0 to retrieve the current height in pixels of the specified screen
     * @return the size of the specified screen
     * @throws quicktime.std.StdQTException
     */
    public Dimension preflightSize(GDevice gd, int width, int height) throws StdQTException {
        QDDimension preflightSize = getPreflightSize(gd, width, height);
        return new Dimension(preflightSize.getWidth(), preflightSize.getHeight());
    }

    /**
     * Returns the closest size to the specified size for the specified screen.
     * This method will not alter the display size of the screen.
     * @see "QuickTime::BeginFullScreen()"
     * @param gd the GDevice for the desired screen or null for the main screen
     * @param width the desired width or 0 to retrieve the current width in pixels of the specified screen
     * @param height the desired height or 0 to retrieve the current height in pixels of the specified screen
     * @return the size of the specified screen
     * @throws quicktime.std.StdQTException
     */
    public QDDimension getPreflightSize(GDevice gd, int width, int height) throws StdQTException {
        end();
        int[] restoreState = new int[1];
        short[] desiredWidth = new short[1];
        short[] desiredHeight = new short[1];
        desiredWidth[0] = ((short) width);
        desiredHeight[0] = ((short) height);
        StdQTException.checkError(BeginFullScreen(restoreState, QTObject.ID(gd), desiredWidth, desiredHeight, 0, 0, 8));
        this.restoreState = restoreState[0];
        return new QDDimension(desiredWidth[0], desiredHeight[0]);
    }

    /**
     * @deprecated
     * <I>as of QTJava 4.0 see beginFullScreen</I>
     * This method converts the main screen to full screen mode by hiding menu bars, control strips, task bars, etc.
     * @see "QuickTime::BeginFullScreen()"
     * @param flags controls the operation of this method.
     * @throws quicktime.std.StdQTException
     */
    public Dimension begin(int flags) throws StdQTException {
        QDDimension dimension = beginFullScreen(flags);
        return new Dimension(dimension.getWidth(), dimension.getHeight());
    }

    /**
     * This method converts the main screen to full screen mode by hiding menu bars, control strips, task bars, etc.
     * @see "QuickTime::BeginFullScreen()"
     * @param flags controls the operation of this method.
     * @throws quicktime.std.StdQTException
     */
    public QDDimension beginFullScreen(int flags) throws StdQTException {
        end();
        int[] restoreState = new int[1];
        short[] desiredWidth = new short[1];
        short[] desiredHeight = new short[1];
        desiredWidth[0] = 0;
        desiredHeight[0] = 0;
        StdQTException.checkError(BeginFullScreen(restoreState, 0, desiredWidth, desiredHeight, 0, 0, flags));
        this.restoreState = restoreState[0];
        return new QDDimension(desiredWidth[0], desiredHeight[0]);
    }

    /**
     * @deprecated
     * <I>as of QTJava 4.0 see beginFullScreen</I>
     * This method converts the main screen to full screen mode.
     * @see "QuickTime::BeginFullScreen()"
     * @param desiredWidth Specifies the resolution width to be displayed for that screen.
     * @param desiredHeight Specifies the resolution height to be displayed for that screen.
     * @param flags Controls the operation of this method.
     * @throws quicktime.std.StdQTException
     */
    public Dimension begin(GDevice whichGD, int desiredWidth, int desiredHeight, int flags) throws StdQTException {
        QDDimension dimension = beginFullScreen(whichGD, desiredWidth, desiredHeight, flags);
        return new Dimension(dimension.getWidth(), dimension.getHeight());
    }

    /**
     * This method converts the main screen to full screen mode.
     * @see "QuickTime::BeginFullScreen()"
     * @param desiredWidth Specifies the resolution width to be displayed for that screen.
     * @param desiredHeight Specifies the resolution height to be displayed for that screen.
     * @param flags Controls the operation of this method.
     * @throws quicktime.std.StdQTException
     */
    public QDDimension beginFullScreen(GDevice whichGD, int desiredWidth, int desiredHeight, int flags) throws StdQTException {
        end();
        int[] restoreState = new int[1];
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];
        arrayOfShort1[0] = ((short) desiredWidth);
        arrayOfShort2[0] = ((short) desiredHeight);
        StdQTException.checkError(BeginFullScreen(restoreState, QTObject.ID(whichGD), arrayOfShort1, arrayOfShort2, 0, 0, flags));
        this.restoreState = restoreState[0];
        return new QDDimension(arrayOfShort1[0], arrayOfShort2[0]);
    }

    /**
     * This method converts the main screen from full screen mode to normal mode.
     * @see "QuickTime::EndFullScreen()"
     */
    public void endFullScreen() throws StdQTException {
        end();
    }

    /**
     * This method converts the main screen from full screen mode to normal mode.
     * @see "QuickTime::EndFullScreen()"
     */
    public void end() throws StdQTException {
        if (this.restoreState != 0) {
            int i = EndFullScreen(this.restoreState, 0);
            StdQTException.checkError(i);
        }
        this.restoreState = 0;
    }

    private void dispose() throws Throwable {
        end();
    }

    private static short BeginFullScreen(int[] restoreState, int whichGD, short[] desiredWidth, short[] desiredHeight, int newWindow, int eraseColor, int flags) {
        return PlatformManager.BeginFullScreen(restoreState, whichGD, desiredWidth, desiredHeight, newWindow, eraseColor, flags);
    }

    private static short EndFullScreen(int fullState, int flags) {
        return PlatformManager.EndFullScreen(fullState, flags);
    }
}
