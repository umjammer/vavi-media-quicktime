
package quicktime;

import java.awt.Component;
import java.awt.Rectangle;


public class WinJNIDrawingSurface {
    private Component comp;

    private int drawingSurface = 0;

    public WinJNIDrawingSurface(Component paramComponent) {
        this.comp = paramComponent;
        this.drawingSurface = WinNativeHelper.getDrawingSurface(paramComponent);
    }

    public WinJNIDrawingSurface() {
    }

    public int lock() {
        if (this.drawingSurface == 0) {
            return 0;
        }
        return WinNativeHelper.lockDrawingSurface(this.comp, this.drawingSurface);
    }

    public void unlock() {
        if (this.drawingSurface == 0) {
            return;
        }
        WinNativeHelper.unlockDrawingSurface(this.comp, this.drawingSurface);
    }

    public Rectangle getBounds() {
        int[] arrayOfInt = new int[4];

        WinNativeHelper.getBounds(this.comp, this.drawingSurface, arrayOfInt);

        Rectangle localRectangle = new Rectangle(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);

        return localRectangle;
    }

    public int getHWnd() {
        if (this.drawingSurface == 0) {
            return 0;
        }
        return WinNativeHelper.getDrawingSurfaceHWnd(this.comp, this.drawingSurface);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/
 * WinJNIDrawingSurface.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
