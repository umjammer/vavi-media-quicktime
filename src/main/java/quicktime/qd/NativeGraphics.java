package quicktime.qd;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Point;
import java.awt.peer.ComponentPeer;
import quicktime.QTSession;
import quicktime.QTUnknownOSException;
import quicktime.WinJNIDrawingSurface;
import quicktime.internal.awt.DrawingSurface;
import quicktime.internal.awt.DrawingSurfaceInfo;

/**
 * You use this class to obtain a QDGraphics object from a Java object known to host
 * a native graphics context. Currently known java objects are java.awt.Components
 */
public abstract class NativeGraphics {

    DrawingSurfaceInfo dsi;

    WinJNIDrawingSurface jnidrawingsurfaceinfo;

    /**
     * This method is called by the Canvas to get the platform specific object that
     * will be able to return the QDGraphics that will be dependent on the particular
     * platform that we are currently running on.
     * @return the platform specific implementation of the NativeGrapics class
     * @throws quicktime.qd.NativeGraphicsException
     * @throws quicktime.QTUnknownOSException
     */
    public static NativeGraphics getContext(Canvas paramCanvas) throws NativeGraphicsException, QTUnknownOSException {
        return getContext((Component) paramCanvas);
    }

    /**
     * This method is called by the Canvas to get the platform specific object that
     * will be able to return the QDGraphics that will be dependent on the particular
     * platform that we are currently running on.
     * @return the platform specific implementation of the NativeGrapics class
     * @throws quicktime.qd.NativeGraphicsException
     * @throws quicktime.QTUnknownOSException
     */
    public static NativeGraphics getContext(Component paramComponent) throws NativeGraphicsException, QTUnknownOSException {
        ComponentPeer localComponentPeer = sun.awt.AWTAccessor.getComponentAccessor().getPeer(paramComponent);
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

    /**
     * This method is called by a Java container object to get the platform specific object that
     * will be able to return the QDGraphics that will be dependent on the particular
     * platform and Java environment that we are currently running on.
     * @return the platform specific implementation of the NativeGrapics class
     * @throws quicktime.qd.NativeGraphicsException
     * @throws quicktime.QTUnknownOSException
     */
    public static NativeGraphics getContext(Object paramObject) throws NativeGraphicsException, QTUnknownOSException {
        Class<?> localClass = paramObject.getClass();
        String str = localClass.getName();
        while (!localClass.getName().equals("java.lang.Object")) {
            str = localClass.getName();
            localClass = localClass.getSuperclass();
        }
        if (str.equals("java.awt.Component")) {
            ComponentPeer localComponentPeer = sun.awt.AWTAccessor.getComponentAccessor().getPeer((Component) paramObject);
            if (localComponentPeer == null)
                throw new NativeGraphicsException("AWT drawing environment is not initialised");
            Object localObject;
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                localObject = ((DrawingSurface) localComponentPeer).getDrawingSurfaceInfo();
                return new MacNativeGraphics((DrawingSurfaceInfo) localObject);
            }
            if (QTSession.isCurrentOS(2)) {
                if (QTSession.getJavaVersion() >= 0x10004) {
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

    /**
     * This returns the QDGraphics that represents the native drawing surface
     * that you get from java.
     */
    public abstract QDGraphics getGWorld() throws NativeGraphicsException;

    abstract int _getWindow() throws NativeGraphicsException;

    public final Point getLocation() {
        if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 0x10004)) {
            return this.jnidrawingsurfaceinfo.getBounds().getLocation();
        }
        return this.dsi.getBounds().getLocation();
    }

    /**
     * This will lock the native drawing surface.
     */
    public void lock() {
        if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 0x10004)) {
            this.jnidrawingsurfaceinfo.lock();
        } else {
            this.dsi.lock();
        }
    }

    /**
     * This will unlock the native drawing surface.
     */
    public void unlock() {
        if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 0x10004)) {
            this.jnidrawingsurfaceinfo.unlock();
        } else
            this.dsi.lock();
    }

    protected void dispose() throws Throwable {
        this.dsi = null;
        this.jnidrawingsurfaceinfo = null;
    }
}
