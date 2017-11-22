
package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.WinJNIDrawingSurface;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import sun.awt.DrawingSurfaceInfo;
import sun.awt.Win32DrawingSurface;


final class WinNativeGraphics extends NativeGraphics implements QuickTimeLib {
    private static Object linkage;

  static {
      class PrivelegedAction {
          void establish()
          {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    return QTNative
                            .linkNativeMethods(WinNativeGraphics.class);
                }
            });
          }
        }
      new PrivelegedAction().establish();
  }

    private static Vector<Integer> hWndVector = new Vector<>();

    private static final boolean debug = false;

    private static final void _destroyPorts() {
        Enumeration<Integer> e = hWndVector.elements();
        while (e.hasMoreElements()) {
            Integer localObject = e.nextElement();

            DestroyPortAssociation(GetNativeWindowPort((localObject).intValue()));
        }
        hWndVector.removeAllElements();
    }

    WinNativeGraphics(int paramInt) {
        super((Object) null);
        this.hWnd = paramInt;
        createAssociation();
    }

    WinNativeGraphics(WinJNIDrawingSurface paramWinJNIDrawingSurface) {
        super(paramWinJNIDrawingSurface);
        if (QTSession.getJavaVersion() >= 65540) {
            this.jnidrawingsurfaceinfo.lock();

            this.hWnd = this.jnidrawingsurfaceinfo.getHWnd();
            this.jnidrawingsurfaceinfo.unlock();
        }
        createAssociation();
    }

    WinNativeGraphics(DrawingSurfaceInfo paramDrawingSurfaceInfo) {
        super(paramDrawingSurfaceInfo);

        Win32DrawingSurface localWin32DrawingSurface = (Win32DrawingSurface) this.dsi.getSurface();
        this.dsi.lock();
        this.hWnd = localWin32DrawingSurface.getHWnd();
        this.dsi.unlock();
        createAssociation();
    }

    private void createAssociation() {
        int i = 0;
        for (int j = 0; j < hWndVector.size(); j++) {
            if (hWndVector.elementAt(j).intValue() == this.hWnd) {
                i = 1;
                break;
            }
        }

        if (i == 0) {

            int j = CreatePortAssociation(this.hWnd, 0, 2);
            if (j == 0)
                throw new QTRuntimeException("No Port for HWnd");
            hWndVector.addElement(new Integer(this.hWnd));
        }
    }

    int hWnd = 0;

    public final QDGraphics getGWorld() throws NativeGraphicsException {
        return QDGraphics.fromNativeGraphics(this);
    }

    private void removeContext() {
        if (this.hWnd != 0) {
            if (hWndVector.isEmpty()) {
                this.hWnd = 0;
                return;
            }

            for (int i = 0; i < hWndVector.size(); i++) {
                if (hWndVector.elementAt(i).intValue() == this.hWnd) {
                    hWndVector.removeElementAt(i);
                    DestroyPortAssociation(GetNativeWindowPort(this.hWnd));
                    break;
                }
            }
            this.hWnd = 0;
        }
    }

    int _getWindow() throws NativeGraphicsException {
        return 0;
    }

    protected void finalize() throws Throwable {
        removeContext();
        super.finalize();
    }

    public String toString() {
        return getClass().getName() + "[vector=" + hWndVector + ",hWnd=" + this.hWnd + "]";
    }

    private static native void DestroyPortAssociation(int paramInt);

    private static native int GetNativeWindowPort(int paramInt);

    private static native int CreatePortAssociation(int paramInt1, int paramInt2, int paramInt3);
}
