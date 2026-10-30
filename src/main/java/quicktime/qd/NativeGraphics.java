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
    public static NativeGraphics getContext(Canvas canvas) throws NativeGraphicsException, QTUnknownOSException {
        return getContext((Component) canvas);
    }

    /**
     * This method is called by the Canvas to get the platform specific object that
     * will be able to return the QDGraphics that will be dependent on the particular
     * platform that we are currently running on.
     * @return the platform specific implementation of the NativeGrapics class
     * @throws quicktime.qd.NativeGraphicsException
     * @throws quicktime.QTUnknownOSException
     */
    public static NativeGraphics getContext(Component comp) throws NativeGraphicsException, QTUnknownOSException {
        ComponentPeer peer = sun.awt.AWTAccessor.getComponentAccessor().getPeer(comp);
        if (peer == null)
            throw new NativeGraphicsException("AWT drawing environment is not initialised");
        Object dsi;
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            dsi = ((DrawingSurface) peer).getDrawingSurfaceInfo();
            return new MacNativeGraphics((DrawingSurfaceInfo) dsi);
        }
        if (QTSession.isCurrentOS(2)) {
            if (QTSession.getJavaVersion() >= 65540) {
                dsi = new WinJNIDrawingSurface(comp);
                return new WinNativeGraphics((WinJNIDrawingSurface) dsi);
            }
            dsi = ((DrawingSurface) peer).getDrawingSurfaceInfo();
            return new WinNativeGraphics((DrawingSurfaceInfo) dsi);
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
    public static NativeGraphics getContext(Object comp) throws NativeGraphicsException, QTUnknownOSException {
        Class<?> superclass = comp.getClass();
        String name = superclass.getName();
        while (!superclass.getName().equals("java.lang.Object")) {
            name = superclass.getName();
            superclass = superclass.getSuperclass();
        }
        if (name.equals("java.awt.Component")) {
            ComponentPeer peer = sun.awt.AWTAccessor.getComponentAccessor().getPeer((Component) comp);
            if (peer == null)
                throw new NativeGraphicsException("AWT drawing environment is not initialised");
            Object dsi;
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                dsi = ((DrawingSurface) peer).getDrawingSurfaceInfo();
                return new MacNativeGraphics((DrawingSurfaceInfo) dsi);
            }
            if (QTSession.isCurrentOS(2)) {
                if (QTSession.getJavaVersion() >= 0x10004) {
                    dsi = new WinJNIDrawingSurface((Component) comp);
                    return new WinNativeGraphics((WinJNIDrawingSurface) dsi);
                }
                dsi = ((DrawingSurface) peer).getDrawingSurfaceInfo();
                return new WinNativeGraphics((DrawingSurfaceInfo) dsi);
            }
            throw new QTUnknownOSException();
        }
        throw new NativeGraphicsException("Unknown Native Context");
    }

    NativeGraphics(Object component) {
        this.dsi = null;
        this.jnidrawingsurfaceinfo = null;
    }

    NativeGraphics(DrawingSurfaceInfo dsi) {
        this.dsi = dsi;
    }

    NativeGraphics(WinJNIDrawingSurface jnidrawingsurfaceinfo) {
        this.jnidrawingsurfaceinfo = jnidrawingsurfaceinfo;
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
