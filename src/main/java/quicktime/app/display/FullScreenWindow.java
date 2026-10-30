package quicktime.app.display;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.qd.GDevice;
import quicktime.qd.QDRect;
import quicktime.std.movies.FullScreen;

/**
 * @deprecated
 * This class is provided for usage when the screen is put into full screen mode.
 * The class manages a FullScreen object that will hide menu bars, task bars, etc.
 * thus making available the entire screen for an application to use. The FullScreenWindow
 * is the window which will fill that screen. As it is an extension of a java.awt.Window
 * any usage of this window is congruous with the usage of a Window in Java applications.
 */
public class FullScreenWindow extends Window {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static Frame fr = null;

    private final FullScreen fs;

    private static Frame _getFrame() {
        if (fr == null)
            fr = new Frame();
        return fr;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a awt.Window for use in full screen mode. It will create the
     * FullScreen object which enables the screen to be placed into full screen mode
     */
    public FullScreenWindow() throws QTException {
        this(new FullScreen());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a awt.Window for use in full screen mode. The application can pass in the
     * FullScreen object which enables the screen to be placed into full screen mode.
     * @param fs the QuickTime FullScreen object
     */
    public FullScreenWindow(FullScreen fs) throws QTException {
        this(fs, _getFrame());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a awt.Window for use in full screen mode. The application can pass in the
     * FullScreen object which enables the screen to be placed into full screen mode.
     * @param fs the QuickTime FullScreen object
     * @param f the Frame that owns the Window object.
     */
    public FullScreenWindow(FullScreen fs, Frame f) throws QTException {
        super(f);
        this.fs = fs;
    }

    private int w;

    private int h;

    private int f;

    private GDevice gd;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This variable controls whether addNotify will actually call or fail - failing is silent.
     * The variable is set to true during the show method and is reset to false from the hide method
     * which also calls removeNotify to remove the native restrictions imposed by incompatible functionality
     * of native windowing usage in the Java VMs.
     */
    protected boolean doNotify = false;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This returns the FullScreen object that is used by the FullScreenWindow to
     * place the screen into full screen mode when the show method is called,
     * and to return the screen to normal mode when the hide method is called.
     * Whilst the application can use this object for preflighting screen size
     * no state is stored in the FullScreen object, so the application should interact
     * directly with the methods of this, the FullScreenWindow, class.
     * @return a FullScreen object
     */
    public FullScreen getFullScreen() {
        return this.fs;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This returns a previously set value of the requested size of the FullScreenWindow.
     * It may return a width and height of 0, 0 if no previous size has been requested.
     * In this case the current display size of the screen is used when the screen is
     * placed into full screen mode.
     * @return the currently requested size for full screen display
     */
    @Override
    public Dimension getPreferredSize() {
        try {
            if ((this.w == 0) || (this.h == 0)) {
                Dimension dimension = this.fs.preflightSize(this.gd, this.w, this.h);
                this.w = dimension.width;
                this.h = dimension.height;
            }
        } catch (QTException e) {
        }
        return new Dimension(this.w, this.h);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * The application can set the requested size of the full screen when the screen is placed
     * into full screen mode. This method does not establish if the resolution requested is available,
     * it will only request that size when the show method is called. To see if a requested size
     * is available the application should get the FullScreen object from this class (getFullScreen) and
     * call a preflightSize method. The result of the preflightSize method tells you the closest size
     * that the desired screen can be placed in from your requested size.
     * @param width the requested width or 0 to leave the screen at its current resolution when full screen
     *  mode is activated with the show method.
     * @param height the requested height or 0 to leave the screen at its current resolution when full screen
     *  mode is activated with the show method.
     */
    public void setPreferredSize(int width, int height) {
        this.w = width;
        this.h = height;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the flags that will be used when the screen is placed in full screen mode with the show method.
     * @return the full screen flags
     */
    public int getFlags() {
        return this.f;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the flags that will be used when the screen is placed in full screen mode with the show method.
     * @param flags the full screen flags
     */
    public void setFlags(int flags) {
        this.f = flags;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the device that corresponds to the screen that will be placed in full screen mode with the show method.
     * @param gDevice the gDevice of a screen of null to set the main screen
     */
    public void setGDevice(GDevice gDevice) {
        this.gd = gDevice;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the device that corresponds to the screen that will be placed in full screen mode with the show method.
     * @return the gDevice of a screen of null
     */
    public GDevice getGDevice() {
        return this.gd;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is used to place the specified screen, at the specified resolution and flags, into full screen
     * mode. It then resizes and positions the FullScreenWindow object so that the content region of this Window
     * takes up the entire screen. It then shows the FullScreenWindow object. On both MacOS and Win32 there are no
     * insets so the entire drawing surface from 0,0 to the width and height is available for display.
     *
     * Due to the restrictions of the usage of addNotify() with this class, the pack() method is also called from
     * this method (as is toFront). Thus the method does multiple things:
     * Puts the screen into full screen mode
     * Sets the size of the window to the size of the screen
     * call pack() to layout the window.
     * shows the window and brings it to the front
     *
     * It is the Applications responsibility to hide this window when done as the user has no direct way to hide
     * the window.
     */
    @Override
    public void show() {
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            this.doNotify = true;
            pack();
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
        }
        try {
            Dimension dimension = this.fs.begin(this.gd, this.w, this.h, this.f);
            setSize(dimension);
            this.w = dimension.width;
            this.h = dimension.height;
            QDRect bounds;
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                if ((this.gd != null) && (!this.gd.equals(GDevice.getMain()))) {
                    bounds = this.gd.getBounds();
                    if (QTSession.isCurrentOS(4)) {
                        setLocation(bounds.getX(), bounds.getY() + -22);
                    } else {
                        setLocation(bounds.getX() + -5, bounds.getY() + -20);
                    }
                } else if (QTSession.isCurrentOS(4)) {
                    setLocation(0, -44);
                } else {
                    setLocation(-5, -40);
                }
            } else {
                if (this.gd != null) {
                    bounds = this.gd.getBounds();
                    setLocation(bounds.getX(), bounds.getY());
                } else {
                    setLocation(0, 0);
                }
                this.doNotify = true;
                pack();
            }
            super.show();
            if (QTSession.isCurrentOS(4))
                requestFocus();
            toFront();
        } catch (QTException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), this.fs, "begin");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method will return the specified screen to normal mode. It then hides the
     * window.
     */
    @Override
    public void hide() {
        this.doNotify = false;
        try {
            this.fs.end();
        } catch (QTException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), this.fs, "end");
        } finally {
            super.hide();
            if (QTSession.isCurrentOS(1)) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
                QTM.DrawMenuBar();
            }
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * The addNotify method can only be successful in this class during or after the execution of the show method.
     * The native Java VM's have peculiarities in windows positioning which are not compatible with
     * the requirement of the FullScreenWindow's purpose to fill the entire screen. An application should never need
     * to call this method directly - if it is called at an inappropriate time the call will silently fail.
     */
    @Override
    public void addNotify() {
        if (this.doNotify) {
            super.addNotify();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * For a full screen window pack can only be called after the window has been shown.
     * The FullScreenWindow.show call will call pack to ensure that the contained components
     * are laid out correctly.
     */
    @Override
    public void pack() {
        if (this.doNotify) {
            super.pack();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns insets of 0, 0, 0, 0 as a FullScreenWindow has no frame or border.
     * @return the insets of this container.
     */
    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    private static final class QTM implements InterfaceLib {

        private static void DrawMenuBar() {
            PlatformManager.DrawMenuBar();
        }
    }
}
