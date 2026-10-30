
package quicktime;

import java.awt.Component;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import quicktime.internal.toolbox.PlatformManager;


final class WinNativeHelper {

    private static final Logger logger = System.getLogger(WinNativeHelper.class.getName());

    static int getCompHWnd(Component component) {
        return PlatformManager.getCompHWnd(component);
    }

    static int getDrawingSurfaceHWnd(Component component, int ds) {
        return PlatformManager.getDrawingSurfaceHWnd(component, ds);
    }

    static int getDrawingSurface(Component component) {
        return PlatformManager.getDrawingSurface(component);
    }

    static int lockDrawingSurface(Component component, int ds) {
        return PlatformManager.lockDrawingSurface(component, ds);
    }

    static void unlockDrawingSurface(Component component, int ds) {
        PlatformManager.unlockDrawingSurface(component, ds);
    }

    static void getBounds(Component component, int ds, int[] bounds) {
        PlatformManager.getBounds(component, ds, bounds);
    }

    static void freeDrawingSurface(Component component, int ds) {
        PlatformManager.freeDrawingSurface(component, ds);
    }

    static {
        if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 65540)) {

            try {
                System.loadLibrary("QTJava");
            } catch (Exception e) {
                logger.log(Level.ERROR, "WinNativeHelper :System.loadLibrary failed ", e);
            }
        }
    }
}
