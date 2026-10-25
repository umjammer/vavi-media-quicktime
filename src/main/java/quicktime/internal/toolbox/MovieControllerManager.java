/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * MovieControllerManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class MovieControllerManager {

    private MovieControllerManager() {
    }

    /** used by QTObjectManagement */
    public static void DisposeMovieController(int paramInt) {
        throw new UnsupportedToolboxException("DisposeMovieController");
    }

    /** used by MovieController */
    public static int MCActivate(int paramInt1, int paramInt2, byte paramByte) {
        throw new UnsupportedToolboxException("MCActivate");
    }

    /** used by MovieController */
    public static int MCClear(int paramInt) {
        throw new UnsupportedToolboxException("MCClear");
    }

    /** used by MovieController */
    public static int MCClick(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("MCClick");
    }

    /** used by MovieController */
    public static int MCCopy(int paramInt) {
        throw new UnsupportedToolboxException("MCCopy");
    }

    /** used by MovieController */
    public static int MCCut(int paramInt) {
        throw new UnsupportedToolboxException("MCCut");
    }

    /** used by MovieController */
    public static int MCDoAction(int paramInt, short paramShort, byte paramByte) {
        throw new UnsupportedToolboxException("MCDoAction");
    }

    /** used by MovieController */
    public static int MCDoAction(int paramInt, short paramShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MCDoAction");
    }

    /** used by MovieController */
    public static int MCDoAction(int paramInt1, short paramShort, int paramInt2) {
        throw new UnsupportedToolboxException("MCDoAction");
    }

    /** used by MovieController */
    public static int MCDoAction(int paramInt, short paramShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MCDoAction");
    }

    /** used by MovieController */
    public static int MCDoAction(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("MCDoAction");
    }

    /** used by MovieController */
    public static int MCDoAction(int paramInt, short paramShort, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("MCDoAction");
    }

    /** used by MovieController */
    public static int MCDraw(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MCDraw");
    }

    /** used by Region */
    public static int MCDrawBadge(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MCDrawBadge");
    }

    /** used by MovieController */
    public static int MCEnableEditing(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("MCEnableEditing");
    }

    /** used by Region */
    public static int MCGetClip(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("MCGetClip");
    }

    /** used by MovieController */
    public static int MCGetControllerBoundsRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MCGetControllerBoundsRect");
    }

    /** used by Region */
    public static int MCGetControllerBoundsRgn(int paramInt) {
        throw new UnsupportedToolboxException("MCGetControllerBoundsRgn");
    }

    /** used by MovieController */
    public static int MCGetControllerInfo(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MCGetControllerInfo");
    }

    /** used by MovieController, QDGraphics */
    public static int MCGetControllerPort(int paramInt) {
        throw new UnsupportedToolboxException("MCGetControllerPort");
    }

    /** used by MovieController */
    public static int MCGetCurrentTime(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MCGetCurrentTime");
    }

    /** used by MovieController, MultiMovieController */
    public static int MCGetIndMovie(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("MCGetIndMovie");
    }

    /** used by MovieController */
    public static int MCGetVisible(int paramInt) {
        throw new UnsupportedToolboxException("MCGetVisible");
    }

    /** used by Region */
    public static int MCGetWindowRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MCGetWindowRgn");
    }

    /** used by MovieController */
    public static int MCIdle(int paramInt) {
        throw new UnsupportedToolboxException("MCIdle");
    }

    /** used by MovieController */
    public static int MCInvalidate(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MCInvalidate");
    }

    /** used by MovieController */
    public static int MCIsControllerAttached(int paramInt) {
        throw new UnsupportedToolboxException("MCIsControllerAttached");
    }

    /** used by MovieController */
    public static int MCIsEditingEnabled(int paramInt) {
        throw new UnsupportedToolboxException("MCIsEditingEnabled");
    }

    /** used by MovieController */
    public static int MCKey(int paramInt1, byte paramByte, int paramInt2) {
        throw new UnsupportedToolboxException("MCKey");
    }

    /** used by MovieController, MultiMovieController */
    public static int MCMovieChanged(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MCMovieChanged");
    }

    /** used by MovieController */
    public static int MCNewAttachedController(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("MCNewAttachedController");
    }

    /** used by MovieController */
    public static int MCPaste(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MCPaste");
    }

    /** used by MovieController */
    public static int MCPositionController(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2) {
        throw new UnsupportedToolboxException("MCPositionController");
    }

    /** used by MovieController */
    public static int MCPtInController(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MCPtInController");
    }

    /** used by MultiMovieController */
    public static int MCRemoveAMovie(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MCRemoveAMovie");
    }

    /** used by MultiMovieController */
    public static int MCRemoveAllMovies(int paramInt) {
        throw new UnsupportedToolboxException("MCRemoveAllMovies");
    }

    /** used by MovieController, MultiMovieController */
    public static int MCRemoveMovie(int paramInt) {
        throw new UnsupportedToolboxException("MCRemoveMovie");
    }

    /** used by MovieController */
    public static int MCSetActionFilterWithRefCon(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MCSetActionFilterWithRefCon");
    }

    /** used by MovieController */
    public static int MCSetClip(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MCSetClip");
    }

    /** used by MovieController */
    public static int MCSetControllerAttached(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("MCSetControllerAttached");
    }

    /** used by MovieController */
    public static int MCSetControllerBoundsRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MCSetControllerBoundsRect");
    }

    /** used by MovieController */
    public static int MCSetControllerPort(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MCSetControllerPort");
    }

    /** used by MovieController */
    public static int MCSetDuration(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MCSetDuration");
    }

    /** used by MovieController, MultiMovieController */
    public static int MCSetMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("MCSetMovie");
    }

    /** used by MovieController */
    public static int MCSetVisible(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("MCSetVisible");
    }

    /** used by MovieController */
    public static int MCUndo(int paramInt) {
        throw new UnsupportedToolboxException("MCUndo");
    }

    /** used by MovieController */
    public static int NewMovieController(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("NewMovieController");
    }

}
