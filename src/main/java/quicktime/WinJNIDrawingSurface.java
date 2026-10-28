package quicktime;

import java.awt.Component;
import java.awt.Rectangle;

/**
 * This class is the equivalent of quicktime.internal.awt.DrawingSurface in 1.3. From 1.4 onwards the DrawingSurface
 * of awt component cannot be accessed via java awt classes. This class makes calls to the native
 * WinNativeHelper library using JNI , that returns the DrawingSurface for the component .
 * This implementation is just for Windows operating system.
 */
public class WinJNIDrawingSurface {

    private Component comp;

    private int drawingSurface = 0;

    /**
     * Returns the native drawing surface for the component supplied
     */
    public WinJNIDrawingSurface(Component paramComponent) {
        this.comp = paramComponent;
        this.drawingSurface = WinNativeHelper.getDrawingSurface(paramComponent);
    }

    /**
     * Returns the native drawing surface for the component supplied
     */
    public WinJNIDrawingSurface() {
    }

    /**
     * Locks the drawing surface and returns the lock.
     * @return lock the lock
     */
    public int lock() {
        if (this.drawingSurface == 0) {
            return 0;
        }
        return WinNativeHelper.lockDrawingSurface(this.comp, this.drawingSurface);
    }

    /**
     * Unlocks the drawing surface
     */
    public void unlock() {
        if (this.drawingSurface == 0) {
            return;
        }
        WinNativeHelper.unlockDrawingSurface(this.comp, this.drawingSurface);
    }

    /**
     * Gets the bounding rectangle of the current drawing surface.
     * @return Rectangle the bounding rectangle
     */
    public Rectangle getBounds() {
        int[] arrayOfInt = new int[4];
        WinNativeHelper.getBounds(this.comp, this.drawingSurface, arrayOfInt);
        Rectangle localRectangle = new Rectangle(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);
        return localRectangle;
    }

    /**
     * This is the equivalent of quicktime.internal.awt.DrawingSurface.getHWnd()
     * @return hwnd the drawing surface hwnd
     */
    public int getHWnd() {
        if (this.drawingSurface == 0) {
            return 0;
        }
        return WinNativeHelper.getDrawingSurfaceHWnd(this.comp, this.drawingSurface);
    }
}
