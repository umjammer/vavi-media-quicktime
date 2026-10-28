
package com.apple.mrj.macos.carbon;

import javax.swing.SwingUtilities;

import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.InterfaceLib;


class HIToolBoxFunctions implements InterfaceLib {

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
        SwingUtilities.invokeLater(() -> System.load("/System/Library/Frameworks/Carbon.framework/Versions/A/Carbon"));
    }
}
