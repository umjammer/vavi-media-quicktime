
package quicktime.qd;

import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.WinJNIDrawingSurface;
import quicktime.internal.awt.DrawingSurfaceInfo;
import quicktime.internal.awt.Win32DrawingSurface;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.QuickTimeLib;


final class WinNativeGraphics extends NativeGraphics implements QuickTimeLib {

    private static final Vector<Integer> hWndVector = new Vector<>();

    private static void _destroyPorts() {
        Enumeration<Integer> e = hWndVector.elements();
        while (e.hasMoreElements()) {
            Integer nativeWindow = e.nextElement();

            DestroyPortAssociation(GetNativeWindowPort(nativeWindow));
        }
        hWndVector.removeAllElements();
    }

    WinNativeGraphics(int hWnd) {
        super((Object) null);
        this.hWnd = hWnd;
        createAssociation();
    }

    WinNativeGraphics(WinJNIDrawingSurface jnidrawingsurfaceinfo) {
        super(jnidrawingsurfaceinfo);
        if (QTSession.getJavaVersion() >= 65540) {
            this.jnidrawingsurfaceinfo.lock();

            this.hWnd = this.jnidrawingsurfaceinfo.getHWnd();
            this.jnidrawingsurfaceinfo.unlock();
        }
        createAssociation();
    }

    WinNativeGraphics(DrawingSurfaceInfo dsi) {
        super(dsi);

        Win32DrawingSurface surface = (Win32DrawingSurface) this.dsi.getSurface();
        this.dsi.lock();
        this.hWnd = surface.getHWnd();
        this.dsi.unlock();
        createAssociation();
    }

    private void createAssociation() {
        int i = 0;
        for (int j = 0; j < hWndVector.size(); j++) {
            if (hWndVector.elementAt(j) == this.hWnd) {
                i = 1;
                break;
            }
        }

        if (i == 0) {

            int j = CreatePortAssociation(this.hWnd, 0, 2);
            if (j == 0)
                throw new QTRuntimeException("No Port for HWnd");
            hWndVector.addElement(this.hWnd);
        }
    }

    int hWnd = 0;

    @Override
    public QDGraphics getGWorld() throws NativeGraphicsException {
        return QDGraphics.fromNativeGraphics(this);
    }

    private void removeContext() {
        if (this.hWnd != 0) {
            if (hWndVector.isEmpty()) {
                this.hWnd = 0;
                return;
            }

            for (int i = 0; i < hWndVector.size(); i++) {
                if (hWndVector.elementAt(i) == this.hWnd) {
                    hWndVector.removeElementAt(i);
                    DestroyPortAssociation(GetNativeWindowPort(this.hWnd));
                    break;
                }
            }
            this.hWnd = 0;
        }
    }

    @Override
    int _getWindow() throws NativeGraphicsException {
        return 0;
    }

    @Override
    protected void dispose() throws Throwable {
        removeContext();
    }

    public String toString() {
        return getClass().getName() + "[vector=" + hWndVector + ",hWnd=" + this.hWnd + "]";
    }

    private static void DestroyPortAssociation(int cgp) {
        PlatformManager.DestroyPortAssociation(cgp);
    }

    private static int GetNativeWindowPort(int nativeWindow) {
        return PlatformManager.GetNativeWindowPort(nativeWindow);
    }

    private static int CreatePortAssociation(int theWnd, int storage, int flags) {
        return PlatformManager.CreatePortAssociation(theWnd, storage, flags);
    }
}
