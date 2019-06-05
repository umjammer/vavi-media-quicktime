
package com.apple.mrj.macos.carbon;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.SwingUtilities;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;


class HIToolBoxFunctions implements InterfaceLib {
    private static Object linkage;

    static native int SetEventLoopTimerNextFireTime(int paramInt, double paramDouble);

    static native int RemoveEventLoopTimer(int paramInt);

    static native void _UseMainRunLoopForCurrentThread();

    static native int GetMainEventLoop();

    static native int InstallEventLoopTimer(int paramInt1,
                                            double paramDouble1,
                                            double paramDouble2,
                                            int paramInt2,
                                            int paramInt3,
                                            int[] paramArrayOfInt);

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
