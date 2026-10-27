
package quicktime.qd;

import quicktime.internal.awt.DrawingSurfaceInfo;
import quicktime.internal.awt.MacDrawingSurface;


final class MacNativeGraphics extends NativeGraphics {
    MacDrawingSurface nd;

    MacNativeGraphics(DrawingSurfaceInfo paramDrawingSurfaceInfo) {
        super(paramDrawingSurfaceInfo);
        this.nd = ((MacDrawingSurface) paramDrawingSurfaceInfo.getSurface());
    }

    @Override
    public final QDGraphics getGWorld() throws NativeGraphicsException {
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


