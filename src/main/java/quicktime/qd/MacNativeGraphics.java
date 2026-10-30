
package quicktime.qd;

import quicktime.internal.awt.DrawingSurfaceInfo;
import quicktime.internal.awt.MacDrawingSurface;


final class MacNativeGraphics extends NativeGraphics {
    final MacDrawingSurface nd;

    MacNativeGraphics(DrawingSurfaceInfo dsi) {
        super(dsi);
        this.nd = ((MacDrawingSurface) dsi.getSurface());
    }

    @Override
    public QDGraphics getGWorld() throws NativeGraphicsException {
        return QDGraphics.fromNativeGraphics(this);
    }

    @Override
    int _getWindow() throws NativeGraphicsException {
        throw new NativeGraphicsException("Method NOT IMPLEMENTED");
    }

    public String toString() {
        return getClass().getName();
    }
}
