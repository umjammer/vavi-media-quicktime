/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * ClockManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class ClockManager {

    private ClockManager() {
    }

    /** used by TimeRecord */
    public static void AddTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("AddTime");
    }

    /** used by CDSequence */
    public static short CDSequenceSetTimeBase(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("CDSequenceSetTimeBase");
    }

    /** used by QTCallBack */
    public static short CallMeWhen(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("CallMeWhen");
    }

    /** used by QTCallBack */
    public static void CancelCallBack(int paramInt) {
        throw new UnsupportedToolboxException("CancelCallBack");
    }

    /** used by Clock */
    public static int ClockGetTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("ClockGetTime");
    }

    /** used by TimeRecord */
    public static void ConvertTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("ConvertTime");
    }

    /** used by TimeRecord */
    public static void ConvertTimeScale(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("ConvertTimeScale");
    }

    /** used by QTObjectManagement */
    public static void DisposeTimeBase(int paramInt) {
        throw new UnsupportedToolboxException("DisposeTimeBase");
    }

    /** used by CBRunner, TimeBase */
    public static int GetCallBackTimeBase(int paramInt) {
        throw new UnsupportedToolboxException("GetCallBackTimeBase");
    }

    /** used by QTCallBack */
    public static short GetCallBackType(int paramInt) {
        throw new UnsupportedToolboxException("GetCallBackType");
    }

    /** used by CBRunner, TimeBase */
    public static int GetTimeBaseEffectiveRate(int paramInt) {
        throw new UnsupportedToolboxException("GetTimeBaseEffectiveRate");
    }

    /** used by TimeBase */
    public static int GetTimeBaseFlags(int paramInt) {
        throw new UnsupportedToolboxException("GetTimeBaseFlags");
    }

    /** used by TimeBase */
    public static int GetTimeBaseMasterClock(int paramInt) {
        throw new UnsupportedToolboxException("GetTimeBaseMasterClock");
    }

    /** used by TimeBase */
    public static int GetTimeBaseMasterTimeBase(int paramInt) {
        throw new UnsupportedToolboxException("GetTimeBaseMasterTimeBase");
    }

    /** used by TimeBase */
    public static int GetTimeBaseRate(int paramInt) {
        throw new UnsupportedToolboxException("GetTimeBaseRate");
    }

    /** used by TimeBase */
    public static int GetTimeBaseStartTime(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetTimeBaseStartTime");
    }

    /** used by TimeBase */
    public static int GetTimeBaseStatus(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetTimeBaseStatus");
    }

    /** used by TimeBase */
    public static int GetTimeBaseStopTime(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetTimeBaseStopTime");
    }

    /** used by CBRunner, TimeBase */
    public static int GetTimeBaseTime(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetTimeBaseTime");
    }

    /** used by QTCallBack */
    public static int NewCallBack(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("NewCallBack");
    }

    /** used by TimeBase */
    public static int NewTimeBase() {
        throw new UnsupportedToolboxException("NewTimeBase");
    }

    /** used by TimeBase */
    public static short QTGetWallClockTimeBase(int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTGetWallClockTimeBase");
    }

    /** used by TimeBase */
    public static void SetTimeBaseFlags(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTimeBaseFlags");
    }

    /** used by TimeBase */
    public static void SetTimeBaseMasterClock(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetTimeBaseMasterClock");
    }

    /** used by TimeBase */
    public static void SetTimeBaseMasterTimeBase(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetTimeBaseMasterTimeBase");
    }

    /** used by TimeBase */
    public static void SetTimeBaseRate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTimeBaseRate");
    }

    /** used by TimeBase */
    public static void SetTimeBaseStartTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTimeBaseStartTime");
    }

    /** used by TimeBase */
    public static void SetTimeBaseStopTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTimeBaseStopTime");
    }

    /** used by TimeBase */
    public static void SetTimeBaseTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTimeBaseTime");
    }

    /** used by TimeBase */
    public static void SetTimeBaseValue(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetTimeBaseValue");
    }

    /** used by TimeBase */
    public static void SetTimeBaseZero(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTimeBaseZero");
    }

    /** used by TimeRecord */
    public static void SubtractTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SubtractTime");
    }

    /** used by MovieController */
    public static int TickCount() {
        throw new UnsupportedToolboxException("TickCount");
    }

}
