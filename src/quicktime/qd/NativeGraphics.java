
package quicktime.qd;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Point;
import java.awt.peer.ComponentPeer;

import quicktime.QTSession;
import quicktime.QTUnknownOSException;
import quicktime.WinJNIDrawingSurface;
import sun.awt.DrawingSurface;
import sun.awt.DrawingSurfaceInfo;


public abstract class NativeGraphics {
    DrawingSurfaceInfo dsi;

    WinJNIDrawingSurface jnidrawingsurfaceinfo;

    public static final NativeGraphics getContext(Canvas paramCanvas) throws NativeGraphicsException, QTUnknownOSException {
        return getContext(paramCanvas);
    }

    public static final NativeGraphics getContext(Component paramComponent) throws NativeGraphicsException,
                                                                            QTUnknownOSException {
        ComponentPeer localComponentPeer = paramComponent.getPeer();

        if (localComponentPeer == null)
            throw new NativeGraphicsException("AWT drawing environment is not initialised");
        Object localObject;
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            localObject = ((DrawingSurface) localComponentPeer).getDrawingSurfaceInfo();
            return new MacNativeGraphics((DrawingSurfaceInfo) localObject);
        }
        if (QTSession.isCurrentOS(2)) {
            if (QTSession.getJavaVersion() >= 65540) {
                localObject = new WinJNIDrawingSurface(paramComponent);
                return new WinNativeGraphics((WinJNIDrawingSurface) localObject);
            }
            localObject = ((DrawingSurface) localComponentPeer).getDrawingSurfaceInfo();
            return new WinNativeGraphics((DrawingSurfaceInfo) localObject);
        }

        throw new QTUnknownOSException();
    }

    public static final NativeGraphics getContext(Object paramObject) throws NativeGraphicsException, QTUnknownOSException {
        Class<?> localClass = paramObject.getClass();
        String str = localClass.getName();
        while (!localClass.getName().equals("java.lang.Object")) {
            str = localClass.getName();
            localClass = localClass.getSuperclass();
        }

        if (str.equals("java.awt.Component")) {
            ComponentPeer localComponentPeer = ((Component) paramObject).getPeer();

            if (localComponentPeer == null)
                throw new NativeGraphicsException("AWT drawing environment is not initialised");
            Object localObject;
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                localObject = ((DrawingSurface) localComponentPeer).getDrawingSurfaceInfo();
                return new MacNativeGraphics((DrawingSurfaceInfo) localObject);
            }
            if (QTSession.isCurrentOS(2)) {
                if (QTSession.getJavaVersion() >= 65540) {
                    localObject = new WinJNIDrawingSurface((Component) paramObject);
                    return new WinNativeGraphics((WinJNIDrawingSurface) localObject);
                }
                localObject = ((DrawingSurface) localComponentPeer).getDrawingSurfaceInfo();
                return new WinNativeGraphics((DrawingSurfaceInfo) localObject);
            }

            throw new QTUnknownOSException();
        }

        throw new NativeGraphicsException("Unknown Native Context");
    }

    NativeGraphics(Object paramObject) {
        this.dsi = null;
        this.jnidrawingsurfaceinfo = null;
    }

    NativeGraphics(DrawingSurfaceInfo paramDrawingSurfaceInfo) {
        this.dsi = paramDrawingSurfaceInfo;
    }

    NativeGraphics(WinJNIDrawingSurface paramWinJNIDrawingSurface) {
        this.jnidrawingsurfaceinfo = paramWinJNIDrawingSurface;
    }

    public abstract QDGraphics getGWorld() throws NativeGraphicsException;

    abstract int _getWindow() throws NativeGraphicsException;

    public final Point getLocation() {
        if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 65540)) {
            return this.jnidrawingsurfaceinfo.getBounds().getLocation();
        }
        return this.dsi.getBounds().getLocation();
    }

    public void lock() {
        if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 65540)) {
            this.jnidrawingsurfaceinfo.lock();
        } else {
            this.dsi.lock();
        }
    }

    public void unlock() {
        if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 65540)) {
            this.jnidrawingsurfaceinfo.unlock();
        } else
            this.dsi.lock();
    }

    protected void finalize() throws Throwable {
        this.dsi = null;
        this.jnidrawingsurfaceinfo = null;
        super.finalize();
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/
 * NativeGraphics.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
