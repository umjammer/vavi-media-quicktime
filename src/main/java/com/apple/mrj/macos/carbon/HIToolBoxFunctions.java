
package com.apple.mrj.macos.carbon;

import javax.swing.SwingUtilities;

import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.InterfaceLib;


class HIToolBoxFunctions implements InterfaceLib {

    static int SetEventLoopTimerNextFireTime(int ref, double delay) {
        return PlatformManager.SetEventLoopTimerNextFireTime(ref, delay);
    }

    static int RemoveEventLoopTimer(int ref) {
        return PlatformManager.RemoveEventLoopTimer(ref);
    }

    static void _UseMainRunLoopForCurrentThread() {
        PlatformManager._UseMainRunLoopForCurrentThread();
    }

    static int GetMainEventLoop() {
        return PlatformManager.GetMainEventLoop();
    }

    static int InstallEventLoopTimer(int inEventLoop, double delay, double interval, int proc, int userData, int[] outTimer) {
        return PlatformManager.InstallEventLoopTimer(inEventLoop, delay, interval, proc, userData, outTimer);
    }

    static {
        SwingUtilities.invokeLater(() -> System.load("/System/Library/Frameworks/Carbon.framework/Versions/A/Carbon"));
    }
}
