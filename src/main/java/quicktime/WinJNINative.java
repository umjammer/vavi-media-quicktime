
package quicktime;

import quicktime.internal.toolbox.PlatformManager;


final class WinJNINative {

    static int DoSecurityInit() {
        return PlatformManager.DoSecurityInit();
    }

    static void DestroyPortAssociations() {
        PlatformManager.DestroyPortAssociations();
    }

    static void DestroyHWnd(int hWnd) {
        PlatformManager.DestroyHWnd(hWnd);
    }

    static int GetHWND() {
        return PlatformManager.GetHWND();
    }

    static {
        if (QTSession.isCurrentOS(2)) {
            System.loadLibrary("QTJava");
        }
    }
}
