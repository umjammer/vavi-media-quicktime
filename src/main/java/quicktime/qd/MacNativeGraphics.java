
package quicktime.qd;

import sun.awt.DrawingSurfaceInfo;
import sun.awt.MacDrawingSurface;


final class MacNativeGraphics extends NativeGraphics {
    MacDrawingSurface nd;

    MacNativeGraphics(DrawingSurfaceInfo paramDrawingSurfaceInfo) {
        super(paramDrawingSurfaceInfo);
        this.nd = ((MacDrawingSurface) paramDrawingSurfaceInfo.getSurface());
    }

    public final QDGraphics getGWorld() throws NativeGraphicsException {
        return QDGraphics.fromNativeGraphics(this);
    }

    int _getWindow() throws NativeGraphicsException {
        throw new NativeGraphicsException("Method NOT IMPLEMENTED");
    }

    public String toString() {
        return getClass().getName();
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/
 * MacNativeGraphics.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
