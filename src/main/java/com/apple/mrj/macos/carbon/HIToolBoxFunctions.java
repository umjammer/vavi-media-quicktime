
package com.apple.mrj.macos.carbon;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.SwingUtilities;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;


class HIToolBoxFunctions implements InterfaceLib {
    private static Object linkage;

    static int SetEventLoopTimerNextFireTime(int paramInt, double paramDouble) {
        return PlatformManager.SetEventLoopTimerNextFireTime(paramInt, paramDouble);
    }

    static int RemoveEventLoopTimer(int paramInt) {
        return PlatformManager.RemoveEventLoopTimer(paramInt);
    }

    static void _UseMainRunLoopForCurrentThread() {
        PlatformManager._UseMainRunLoopForCurrentThread();
    }

    static int GetMainEventLoop() {
        return PlatformManager.GetMainEventLoop();
    }

    static int InstallEventLoopTimer(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return PlatformManager.InstallEventLoopTimer(paramInt1, paramDouble1, paramDouble2, paramInt2, paramInt3, paramArrayOfInt);
    }

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(HIToolBoxFunctions.class);
                    }
                });
            }
        }

        new PrivelegedAction().establish();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.load("/System/Library/Frameworks/Carbon.framework/Versions/A/Carbon");
            }
        });
    }
}
