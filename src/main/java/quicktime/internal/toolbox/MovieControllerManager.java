/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * MovieControllerManager.
 * <p>
 * The standard movie controller ('play'): a controller bar under the movie with a play button and a time slider,
 * drawn with Java 2D into the controller port. Actions go through the action filter before they are done.
 */
public final class MovieControllerManager {

    private MovieControllerManager() {
    }

    /** height of the controller bar */
    static final int BAR = 16;

    static final int play = Bytes.fourCC("play");

    // flags of NewMovieController
    static final int mcTopLeftMovie = 1;
    static final int mcScaleMovieToFit = 2;
    static final int mcWithBadge = 4;
    static final int mcNotVisible = 8;
    static final int mcWithFrame = 16;

    // controller flags
    static final int mcFlagSuppressMovieFrame = 1;
    static final int mcFlagSuppressStepButtons = 2;
    static final int mcFlagSuppressSpeakerButton = 4;

    // info
    static final int mcInfoUndoAvailable = 1;
    static final int mcInfoCutAvailable = 2;
    static final int mcInfoCopyAvailable = 4;
    static final int mcInfoPasteAvailable = 8;
    static final int mcInfoClearAvailable = 16;
    static final int mcInfoHasSound = 32;
    static final int mcInfoIsPlaying = 64;
    static final int mcInfoIsLooping = 128;
    static final int mcInfoIsInPalindrome = 256;
    static final int mcInfoEditingEnabled = 512;

    // actions
    static final short mcActionIdle = 1;
    static final short mcActionDraw = 2;
    static final short mcActionActivate = 3;
    static final short mcActionDeactivate = 4;
    static final short mcActionMouseDown = 5;
    static final short mcActionKey = 6;
    static final short mcActionPlay = 8;
    static final short mcActionGoToTime = 12;
    static final short mcActionSetVolume = 14;
    static final short mcActionGetVolume = 15;
    static final short mcActionStep = 18;
    static final short mcActionSetLooping = 21;
    static final short mcActionGetLooping = 22;
    static final short mcActionSetLoopIsPalindrome = 23;
    static final short mcActionGetLoopIsPalindrome = 24;
    static final short mcActionSetGrowBoxBounds = 25;
    static final short mcActionControllerSizeChanged = 26;
    static final short mcActionSetSelectionBegin = 29;
    static final short mcActionSetSelectionDuration = 30;
    static final short mcActionSetKeysEnabled = 32;
    static final short mcActionGetKeysEnabled = 33;
    static final short mcActionSetPlaySelection = 34;
    static final short mcActionGetPlaySelection = 35;
    static final short mcActionSetUseBadge = 36;
    static final short mcActionGetUseBadge = 37;
    static final short mcActionSetFlags = 38;
    static final short mcActionGetFlags = 39;
    static final short mcActionSetPlayEveryFrame = 40;
    static final short mcActionGetPlayEveryFrame = 41;
    static final short mcActionGetPlayRate = 42;
    static final short mcActionBadgeClick = 44;
    static final short mcActionMovieClick = 45;
    static final short mcActionSuspend = 46;
    static final short mcActionResume = 47;
    static final short mcActionSetControllerKeysEnabled = 48;
    static final short mcActionGetTimeSliderRect = 49;
    static final short mcActionMovieEdited = 50;
    static final short mcActionGetDragEnabled = 51;
    static final short mcActionSetDragEnabled = 52;
    static final short mcActionGetSelectionBegin = 53;
    static final short mcActionGetSelectionDuration = 54;
    static final short mcActionPrerollAndPlay = 55;
    static final short mcActionGetCursorSettingEnabled = 56;
    static final short mcActionSetCursorSettingEnabled = 57;
    static final short mcActionSetColorTable = 58;
    static final short mcActionLinkToURL = 59;
    static final short mcActionForceTimeTableUpdate = 61;
    static final short mcActionMovieChanged = 77;

    /** a movie controller */
    static final class ControllerRec {
        int ref;
        MovieRec movie;
        final List<MovieRec> movies = new ArrayList<>();
        /** movie box and controller bar, left top right bottom */
        int left, top, right, bottom;
        boolean attached = true;
        boolean visible = true;
        boolean active = true;
        boolean editing;
        boolean looping;
        boolean palindrome;
        boolean playSelection;
        boolean playEveryFrame;
        boolean keysEnabled = true;
        boolean controllerKeysEnabled = true;
        boolean useBadge;
        boolean dragEnabled;
        boolean cursorSettingEnabled = true;
        int flags;
        int port;
        int clipRgn;
        int movieClipRgn;
        int filterProc;
        int filterRefCon;
        /** the playing rate set by play, 0 when stopped */
        int playRate;
        MovieEdits.MovieEditState undo;
        long duration = -1;

        int movieBottom() {
            return attached && visible ? bottom - BAR : bottom;
        }
    }

    static ControllerRec controller(int mc) {
        ComponentManager.InstanceRec i = ComponentManager.instance(mc);
        if (i == null) {
            throw new IllegalArgumentException("not a movie controller: 0x" + Integer.toHexString(mc));
        }
        if (!(i.impl instanceof ControllerRec)) {
            ControllerRec c = new ControllerRec();
            c.ref = mc;
            i.impl = c;
        }
        return (ControllerRec) i.impl;
    }

    private static int[] box(MovieRec m) {
        double[] b = m.naturalBounds();
        return new int[] {(int) Math.round(b[0]), (int) Math.round(b[1]), (int) Math.round(b[2]), (int) Math.round(b[3])};
    }

    /** places the movie at the top left of the controller */
    private static void layoutMovie(ControllerRec c) {
        if (c.movie == null) {
            return;
        }
        byte[] r = new byte[8];
        Bytes.putShort(r, 0, c.top);
        Bytes.putShort(r, 2, c.left);
        Bytes.putShort(r, 4, Math.max(c.top, c.movieBottom()));
        Bytes.putShort(r, 6, c.right);
        if (c.right > c.left && c.movieBottom() > c.top) {
            MovieToolbox.SetMovieBox(c.movie.ref, r);
        }
    }

    private static void setMovie(ControllerRec c, MovieRec m, int window, int where) {
        c.movie = m;
        if (m != null && !c.movies.contains(m)) {
            c.movies.add(m);
        }
        if (window != 0) {
            c.port = window;
        }
        if (m == null) {
            return;
        }
        int[] b = box(m);
        // a Point is v(16) h(16)
        int x = where == 0 ? b[0] : (short) where;
        int y = where == 0 ? b[1] : (short) (where >> 16);
        c.left = x;
        c.top = y;
        c.right = x + (b[2] - b[0]);
        c.bottom = y + (b[3] - b[1]) + (c.attached && c.visible ? BAR : 0);
        layoutMovie(c);
        if (c.port != 0) {
            MovieToolbox.SetMovieGWorld(m.ref, c.port, 0);
        }
    }

    // ---- the filter

    /** @return true if the action filter handled the action */
    private static boolean filter(ControllerRec c, short action, int params) {
        if (c.filterProc == 0) {
            return false;
        }
        return Closures.callInt(c.filterProc, c.ref, action, params, c.filterRefCon) != 0;
    }

    // ---- actions

    private static int act(int mc, short action, int intParam, byte byteParam, short shortParam, byte[] bytes, int[] ints, short[] shorts, int ptr) {
        ControllerRec c = controller(mc);
        if (filter(c, action, ptr)) {
            return 0;
        }
        MovieRec m = c.movie;
        switch (action) {
            case mcActionIdle -> idle(c);
            case mcActionDraw -> draw(c);
            case mcActionActivate -> c.active = true;
            case mcActionDeactivate -> c.active = false;
            case mcActionPlay, mcActionPrerollAndPlay -> {
                if (m != null) {
                    c.playRate = intParam;
                    if (intParam != 0 && c.playSelection && m.selectionDuration > 0) {
                        long t = m.time();
                        if (t < m.selectionTime || t >= m.selectionTime + m.selectionDuration) {
                            m.timeBase.setTime(intParam > 0 ? m.selectionTime : m.selectionTime + m.selectionDuration, m.timeScale);
                        }
                    } else if (intParam > 0 && m.time() >= m.duration()) {
                        m.timeBase.setTime(0, m.timeScale);
                    }
                    MovieToolbox.SetMovieRate(m.ref, intParam);
                }
            }
            case mcActionGoToTime -> {
                if (m != null) {
                    MovieToolbox.SetMovieTime(m.ref, ptr);
                }
            }
            case mcActionSetVolume -> {
                if (m != null) {
                    MovieToolbox.SetMovieVolume(m.ref, shortParam);
                }
            }
            case mcActionGetVolume -> {
                if (shorts != null && m != null) {
                    shorts[0] = m.volume;
                }
            }
            case mcActionStep -> {
                if (m != null) {
                    step(m, shortParam);
                }
            }
            case mcActionSetLooping -> c.looping = byteParam != 0;
            case mcActionGetLooping -> bool(bytes, c.looping);
            case mcActionSetLoopIsPalindrome -> c.palindrome = byteParam != 0;
            case mcActionGetLoopIsPalindrome -> bool(bytes, c.palindrome);
            case mcActionSetSelectionBegin -> {
                if (m != null) {
                    m.selectionTime = (int) ClockManager.recordValueIn(ptr, m.timeScale);
                }
            }
            case mcActionSetSelectionDuration -> {
                if (m != null) {
                    m.selectionDuration = (int) ClockManager.recordValueIn(ptr, m.timeScale);
                }
            }
            case mcActionGetSelectionBegin -> {
                if (m != null) {
                    ClockManager.setRecord(ptr, m.selectionTime, m.timeScale, 0);
                }
            }
            case mcActionGetSelectionDuration -> {
                if (m != null) {
                    ClockManager.setRecord(ptr, m.selectionDuration, m.timeScale, 0);
                }
            }
            case mcActionSetKeysEnabled -> c.keysEnabled = byteParam != 0;
            case mcActionGetKeysEnabled -> bool(bytes, c.keysEnabled);
            case mcActionSetControllerKeysEnabled -> c.controllerKeysEnabled = byteParam != 0;
            case mcActionSetPlaySelection -> c.playSelection = byteParam != 0;
            case mcActionGetPlaySelection -> bool(bytes, c.playSelection);
            case mcActionSetUseBadge -> c.useBadge = byteParam != 0;
            case mcActionGetUseBadge -> bool(bytes, c.useBadge);
            case mcActionSetFlags -> c.flags = intParam;
            case mcActionGetFlags -> {
                if (ints != null) {
                    ints[0] = c.flags;
                }
            }
            case mcActionSetPlayEveryFrame -> c.playEveryFrame = byteParam != 0;
            case mcActionGetPlayEveryFrame -> bool(bytes, c.playEveryFrame);
            case mcActionGetPlayRate -> {
                if (ints != null) {
                    ints[0] = m != null ? m.timeBase.rate : 0;
                }
            }
            case mcActionSetDragEnabled -> c.dragEnabled = byteParam != 0;
            case mcActionGetDragEnabled -> bool(bytes, c.dragEnabled);
            case mcActionSetCursorSettingEnabled -> c.cursorSettingEnabled = byteParam != 0;
            case mcActionGetCursorSettingEnabled -> {
                if (ints != null) {
                    ints[0] = c.cursorSettingEnabled ? 1 : 0;
                }
            }
            case mcActionGetTimeSliderRect -> {
                if (bytes != null) {
                    int[] s = slider(c);
                    Bytes.putShort(bytes, 0, s[1]);
                    Bytes.putShort(bytes, 2, s[0]);
                    Bytes.putShort(bytes, 4, s[3]);
                    Bytes.putShort(bytes, 6, s[2]);
                }
            }
            case mcActionSuspend -> {
                if (m != null) {
                    MovieToolbox.SetMovieRate(m.ref, 0);
                }
            }
            case mcActionResume -> {
                if (m != null && c.playRate != 0) {
                    MovieToolbox.SetMovieRate(m.ref, c.playRate);
                }
            }
            case mcActionMovieEdited, mcActionMovieChanged, mcActionControllerSizeChanged, mcActionForceTimeTableUpdate -> {
                if (m != null) {
                    m.updateTimeBase();
                    MovieDrawing.invalidate(m);
                }
            }
            default -> {
                // badge, color table, URL links, grow box: nothing to do without a window
            }
        }
        return 0;
    }

    private static void bool(byte[] out, boolean v) {
        if (out != null && out.length > 0) {
            out[0] = (byte) (v ? 1 : 0);
        }
    }

    /** steps by frames of the first visual track, or by a tenth of a second */
    private static void step(MovieRec m, int steps) {
        m.timeBase.setRate(0);
        long t = m.time();
        for (int i = 0; i < Math.abs(steps); i++) {
            long[] next = {-1, 0};
            for (TrackRec tr : m.tracks) {
                if (tr.enabled && tr.media != null && tr.media.isVisual()) {
                    long[] r = MovieToolbox.trackNextInterestingTime(tr, MovieToolbox.nextTimeMediaSample, t, steps > 0 ? 0x10000 : -0x10000);
                    if (r[0] >= 0 && (next[0] < 0 || (steps > 0 ? r[0] < next[0] : r[0] > next[0]))) {
                        next = r;
                    }
                }
            }
            t = next[0] >= 0 ? next[0] : Math.max(0, Math.min(m.duration(), t + (steps > 0 ? 1 : -1) * Math.max(1, m.timeScale / 10)));
        }
        m.timeBase.setTime(t, m.timeScale);
        m.timeBase.fireTimeJump();
        MovieDrawing.invalidate(m);
    }

    private static void idle(ControllerRec c) {
        MovieRec m = c.movie;
        if (m == null) {
            return;
        }
        int rate = m.timeBase.rate;
        if (rate != 0) {
            long t = m.time();
            long start = c.playSelection && m.selectionDuration > 0 ? m.selectionTime : 0;
            long end = c.playSelection && m.selectionDuration > 0 ? m.selectionTime + m.selectionDuration : m.duration();
            boolean atEnd = rate > 0 ? t >= end : t <= start;
            if (atEnd) {
                if (c.looping && c.palindrome) {
                    m.timeBase.setRate(-rate);
                } else if (c.looping) {
                    m.timeBase.setTime(rate > 0 ? start : end, m.timeScale);
                    m.timeBase.fireTimeJump();
                } else {
                    m.timeBase.setRate(0);
                    c.playRate = 0;
                }
                MediaEngine.get().sync(m);
            }
        }
        MovieToolbox.MoviesTask(m.ref, 0);
        if (c.attached && c.visible && c.port != 0) {
            draw(c);
        }
    }

    /** @return the slider rect, left top right bottom */
    private static int[] slider(ControllerRec c) {
        int y0 = c.bottom - BAR;
        return new int[] {c.left + BAR + 4, y0 + 4, c.right - 6, c.bottom - 4};
    }

    /** draws the controller bar into the controller port */
    private static void draw(ControllerRec c) {
        if (!c.attached || !c.visible || c.port == 0 || QuickDrawManager.portOrNull(c.port) == null) {
            return;
        }
        QuickDrawManager.Port p = QuickDrawManager.port(c.port);
        Graphics2D g = p.graphics();
        if (g == null) {
            return;
        }
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int y0 = c.bottom - BAR;
            g.setColor(new Color(0xe0e0e0));
            g.fillRect(c.left, y0, c.right - c.left, BAR);
            g.setColor(new Color(0x808080));
            g.drawLine(c.left, y0, c.right, y0);
            boolean playing = c.movie != null && c.movie.timeBase.rate != 0;
            g.setColor(Color.darkGray);
            if (playing) {
                g.fillRect(c.left + 5, y0 + 4, 2, BAR - 8);
                g.fillRect(c.left + 9, y0 + 4, 2, BAR - 8);
            } else {
                g.fillPolygon(new Polygon(new int[] {c.left + 5, c.left + 5, c.left + 12}, new int[] {y0 + 3, y0 + BAR - 3, y0 + BAR / 2}, 3));
            }
            int[] s = slider(c);
            g.setColor(Color.white);
            g.fillRect(s[0], s[1], s[2] - s[0], s[3] - s[1]);
            g.setColor(Color.gray);
            g.drawRect(s[0], s[1], s[2] - s[0], s[3] - s[1]);
            if (c.movie != null) {
                long d = c.duration >= 0 ? c.duration : c.movie.duration();
                if (c.movie.selectionDuration > 0 && d > 0) {
                    int a = s[0] + (int) ((s[2] - s[0]) * c.movie.selectionTime / d);
                    int b = s[0] + (int) ((s[2] - s[0]) * (c.movie.selectionTime + c.movie.selectionDuration) / d);
                    g.setColor(new Color(0x9090ff));
                    g.fillRect(a, s[1] + 1, Math.max(1, b - a), s[3] - s[1] - 1);
                }
                int x = d <= 0 ? s[0] : s[0] + (int) Math.min(s[2] - s[0], (s[2] - s[0]) * c.movie.time() / d);
                g.setColor(Color.darkGray);
                g.fillRect(x - 2, s[1] - 2, 5, s[3] - s[1] + 4);
            }
        } finally {
            g.dispose();
        }
    }

    // ---- Movie Toolbox calls

    public static int NewMovieController(int movie, byte[] rect, int flags) {
        int ci = ComponentManager.open(controllerComponent());
        ControllerRec c = controller(ci);
        c.visible = (flags & mcNotVisible) == 0;
        MovieRec m = MovieToolbox.movie(movie);
        int top = Bytes.getShort(rect, 0), left = Bytes.getShort(rect, 2), bottom = Bytes.getShort(rect, 4), right = Bytes.getShort(rect, 6);
        int[] cur = QuickDrawManager.currentGWorld();
        c.port = m.gworld != 0 ? m.gworld : cur[0];
        c.movie = m;
        c.movies.add(m);
        if ((flags & mcScaleMovieToFit) != 0) {
            c.left = left;
            c.top = top;
            c.right = right;
            c.bottom = bottom;
            layoutMovie(c);
        } else {
            int[] b = box(m);
            c.left = left;
            c.top = top;
            c.right = left + (b[2] - b[0]);
            c.bottom = top + (b[3] - b[1]) + (c.visible ? BAR : 0);
            layoutMovie(c);
        }
        MoviesError.ok();
        return ci;
    }

    private static ComponentManager.ComponentRec controllerComponent;

    private static synchronized ComponentManager.ComponentRec controllerComponent() {
        if (controllerComponent == null) {
            byte[] d = new byte[20];
            Bytes.putInt(d, 0, play);
            controllerComponent = ComponentManager.component(ComponentManager.FindNextComponent(0, d));
        }
        return controllerComponent;
    }

    public static void DisposeMovieController(int mc) {
        ComponentManager.CloseComponent(mc);
    }

    public static int MCNewAttachedController(int mc, int movie, int window, int where) {
        ControllerRec c = controller(mc);
        c.attached = true;
        setMovie(c, MovieToolbox.movie(movie), window != 0 ? window : QuickDrawManager.currentGWorld()[0], where);
        return 0;
    }

    public static int MCSetMovie(int mc, int movie, int window, int where) {
        ControllerRec c = controller(mc);
        setMovie(c, movie == 0 ? null : MovieToolbox.movie(movie), window, where);
        return 0;
    }

    /** index 0 is the current movie */
    public static int MCGetIndMovie(int mc, short index) {
        ControllerRec c = controller(mc);
        if (index == 0) {
            return c.movie != null ? c.movie.ref : 0;
        }
        return index >= 1 && index <= c.movies.size() ? c.movies.get(index - 1).ref : 0;
    }

    public static int MCRemoveAllMovies(int mc) {
        ControllerRec c = controller(mc);
        c.movies.clear();
        c.movie = null;
        return 0;
    }

    public static int MCRemoveAMovie(int mc, int movie) {
        ControllerRec c = controller(mc);
        MovieRec m = MovieToolbox.movie(movie);
        c.movies.remove(m);
        if (c.movie == m) {
            c.movie = c.movies.isEmpty() ? null : c.movies.getFirst();
        }
        return 0;
    }

    public static int MCRemoveMovie(int mc) {
        ControllerRec c = controller(mc);
        if (c.movie != null) {
            c.movies.remove(c.movie);
        }
        c.movie = c.movies.isEmpty() ? null : c.movies.getFirst();
        return 0;
    }

    public static int MCIdle(int mc) {
        return act(mc, mcActionIdle, 0, (byte) 0, (short) 0, null, null, null, 0);
    }

    public static int MCDraw(int mc, int window) {
        ControllerRec c = controller(mc);
        if (window != 0) {
            c.port = window;
        }
        return act(mc, mcActionDraw, window, (byte) 0, (short) 0, null, null, null, window);
    }

    public static int MCDrawBadge(int mc, int movieRgn, int[] badgeRgn) {
        if (badgeRgn != null && badgeRgn.length > 0) {
            badgeRgn[0] = 0;
        }
        return 0;
    }

    public static int MCActivate(int mc, int window, byte activate) {
        return act(mc, activate != 0 ? mcActionActivate : mcActionDeactivate, 0, (byte) 0, (short) 0, null, null, null, 0);
    }

    /** a click on the bar plays or pauses, on the slider goes to the time */
    public static int MCClick(int mc, int window, int where, int when, int modifiers) {
        ControllerRec c = controller(mc);
        if (filter(c, mcActionMouseDown, 0) || c.movie == null) {
            return 0;
        }
        int v = (short) (where >> 16);
        int h = (short) where;
        if (!c.attached || !c.visible || v < c.bottom - BAR || v >= c.bottom || h < c.left || h >= c.right) {
            filter(c, mcActionMovieClick, 0);
            return 0;
        }
        int[] s = slider(c);
        if (h < s[0]) {
            boolean playing = c.movie.timeBase.rate != 0;
            act(mc, mcActionPlay, playing ? 0 : c.movie.preferredRate, (byte) 0, (short) 0, null, null, null, 0);
        } else {
            long d = c.movie.duration();
            long t = Math.max(0, Math.min(d, (h - s[0]) * d / Math.max(1, s[2] - s[0])));
            c.movie.timeBase.setTime(t, c.movie.timeScale);
            c.movie.timeBase.fireTimeJump();
            MediaEngine.get().sync(c.movie);
            MovieDrawing.invalidate(c.movie);
        }
        draw(c);
        return 0;
    }

    /** space plays or pauses, the arrows step, return and enter play */
    public static int MCKey(int mc, byte key, int modifiers) {
        ControllerRec c = controller(mc);
        if (filter(c, mcActionKey, key) || c.movie == null || !c.keysEnabled) {
            return 0;
        }
        switch (key) {
            case ' ', 13, 3 -> act(mc, mcActionPlay, c.movie.timeBase.rate != 0 ? 0 : c.movie.preferredRate, (byte) 0, (short) 0, null, null, null, 0);
            case 28 -> act(mc, mcActionStep, 0, (byte) 0, (short) -1, null, null, null, 0);
            case 29 -> act(mc, mcActionStep, 0, (byte) 0, (short) 1, null, null, null, 0);
            case 30 -> act(mc, mcActionSetVolume, 0, (byte) 0, (short) Math.min(0x100, c.movie.volume + 0x20), null, null, null, 0);
            case 31 -> act(mc, mcActionSetVolume, 0, (byte) 0, (short) Math.max(0, c.movie.volume - 0x20), null, null, null, 0);
            default -> {
                return 0;
            }
        }
        return 1;
    }

    public static int MCDoAction(int mc, short action, int param) {
        return act(mc, action, param, (byte) param, (short) param, null, null, null, param);
    }

    public static int MCDoAction(int mc, short action, byte param) {
        return act(mc, action, param, param, param, null, null, null, param);
    }

    public static int MCDoAction(int mc, short action, short param) {
        return act(mc, action, param, (byte) param, param, null, null, null, param);
    }

    public static int MCDoAction(int mc, short action, byte[] param) {
        return act(mc, action, 0, param != null && param.length > 0 ? param[0] : 0, (short) 0, param, null, null, 0);
    }

    public static int MCDoAction(int mc, short action, int[] param) {
        return act(mc, action, param != null && param.length > 0 ? param[0] : 0, (byte) 0, (short) 0, null, param, null, 0);
    }

    public static int MCDoAction(int mc, short action, short[] param) {
        return act(mc, action, 0, (byte) 0, param != null && param.length > 0 ? param[0] : 0, null, null, param, 0);
    }

    public static int MCSetActionFilterWithRefCon(int mc, int proc, int refCon) {
        ControllerRec c = controller(mc);
        c.filterProc = proc;
        c.filterRefCon = refCon;
        return 0;
    }

    public static int MCPositionController(int mc, byte[] movieRect, byte[] controllerRect, int flags) {
        ControllerRec c = controller(mc);
        int top = Bytes.getShort(movieRect, 0), left = Bytes.getShort(movieRect, 2), bottom = Bytes.getShort(movieRect, 4), right = Bytes.getShort(movieRect, 6);
        c.left = left;
        c.top = top;
        c.right = right;
        c.bottom = bottom + (c.attached && c.visible ? BAR : 0);
        layoutMovie(c);
        return 0;
    }

    public static int MCGetControllerBoundsRect(int mc, byte[] rect) {
        ControllerRec c = controller(mc);
        Bytes.putShort(rect, 0, c.attached ? c.top : c.bottom - BAR);
        Bytes.putShort(rect, 2, c.left);
        Bytes.putShort(rect, 4, c.bottom);
        Bytes.putShort(rect, 6, c.right);
        return 0;
    }

    public static int MCSetControllerBoundsRect(int mc, byte[] rect) {
        ControllerRec c = controller(mc);
        c.top = Bytes.getShort(rect, 0);
        c.left = Bytes.getShort(rect, 2);
        c.bottom = Bytes.getShort(rect, 4);
        c.right = Bytes.getShort(rect, 6);
        layoutMovie(c);
        return 0;
    }

    public static int MCGetControllerBoundsRgn(int mc) {
        ControllerRec c = controller(mc);
        return QuickDrawManager.newRectRegion(c.left, c.attached ? c.top : c.bottom - BAR, c.right, c.bottom);
    }

    public static int MCGetWindowRgn(int mc, int window) {
        ControllerRec c = controller(mc);
        return QuickDrawManager.newRectRegion(c.left, c.top, c.right, c.bottom);
    }

    public static int MCSetControllerPort(int mc, int port) {
        ControllerRec c = controller(mc);
        c.port = port;
        if (c.movie != null && port != 0) {
            MovieToolbox.SetMovieGWorld(c.movie.ref, port, 0);
        }
        return 0;
    }

    public static int MCGetControllerPort(int mc) {
        ControllerRec c = controller(mc);
        return c.port != 0 ? c.port : QuickDrawManager.currentGWorld()[0];
    }

    public static int MCSetControllerAttached(int mc, byte attach) {
        ControllerRec c = controller(mc);
        boolean was = c.attached;
        c.attached = attach != 0;
        if (was != c.attached && c.visible) {
            c.bottom += c.attached ? BAR : -BAR;
        }
        layoutMovie(c);
        return 0;
    }

    public static int MCIsControllerAttached(int mc) {
        return controller(mc).attached ? 1 : 0;
    }

    public static int MCSetVisible(int mc, byte visible) {
        ControllerRec c = controller(mc);
        boolean was = c.visible;
        c.visible = visible != 0;
        if (was != c.visible && c.attached) {
            c.bottom += c.visible ? BAR : -BAR;
        }
        layoutMovie(c);
        return 0;
    }

    public static int MCGetVisible(int mc) {
        return controller(mc).visible ? 1 : 0;
    }

    public static int MCEnableEditing(int mc, byte enabled) {
        controller(mc).editing = enabled != 0;
        return 0;
    }

    public static int MCIsEditingEnabled(int mc) {
        return controller(mc).editing ? 1 : 0;
    }

    private static void saveUndo(ControllerRec c) {
        if (c.undo != null) {
            c.undo.dispose();
        }
        c.undo = new MovieEdits.MovieEditState(c.movie);
    }

    public static int MCCut(int mc) {
        ControllerRec c = controller(mc);
        if (!c.editing || c.movie == null) {
            return 0;
        }
        saveUndo(c);
        MovieRec cut = MovieEdits.cutSelection(c.movie);
        MovieEdits.putOnScrap(cut);
        return cut.ref;
    }

    public static int MCCopy(int mc) {
        ControllerRec c = controller(mc);
        if (!c.editing || c.movie == null) {
            return 0;
        }
        MovieRec copy = MovieEdits.copySelection(c.movie);
        MovieEdits.putOnScrap(copy);
        return copy.ref;
    }

    public static int MCPaste(int mc, int movie) {
        ControllerRec c = controller(mc);
        if (!c.editing || c.movie == null) {
            return 0;
        }
        MovieRec src = movie != 0 ? MovieToolbox.movie(movie) : MovieEdits.fromScrap();
        if (src == null) {
            return 0;
        }
        saveUndo(c);
        MovieEdits.pasteSelection(c.movie, src);
        return 0;
    }

    public static int MCClear(int mc) {
        ControllerRec c = controller(mc);
        if (!c.editing || c.movie == null) {
            return 0;
        }
        saveUndo(c);
        MovieEdits.clearSelection(c.movie);
        return 0;
    }

    public static int MCUndo(int mc) {
        ControllerRec c = controller(mc);
        if (c.undo == null || c.movie == null) {
            return 0;
        }
        MovieEdits.MovieEditState redo = new MovieEdits.MovieEditState(c.movie);
        MovieEdits.useMovieEditState(c.movie, c.undo);
        c.undo.dispose();
        c.undo = redo;
        return 0;
    }

    public static int MCMovieChanged(int mc, int movie) {
        ControllerRec c = controller(mc);
        if (c.movie != null) {
            c.movie.updateTimeBase();
            MovieDrawing.invalidate(c.movie);
        }
        return 0;
    }

    public static int MCSetDuration(int mc, int duration) {
        controller(mc).duration = duration;
        return 0;
    }

    public static int MCGetCurrentTime(int mc, int[] scale) {
        ControllerRec c = controller(mc);
        if (c.movie == null) {
            return 0;
        }
        if (scale != null && scale.length > 0) {
            scale[0] = c.movie.timeScale;
        }
        return (int) c.movie.time();
    }

    public static int MCPtInController(int mc, int pt, byte[] inController) {
        ControllerRec c = controller(mc);
        int v = (short) (pt >> 16);
        int h = (short) pt;
        boolean in = c.attached && c.visible && h >= c.left && h < c.right && v >= c.bottom - BAR && v < c.bottom;
        if (inController != null && inController.length > 0) {
            inController[0] = (byte) (in ? 1 : 0);
        }
        return 0;
    }

    public static int MCSetClip(int mc, int theClip, int movieClip) {
        ControllerRec c = controller(mc);
        c.clipRgn = theClip == 0 ? 0 : QuickDrawManager.copyRegion(theClip);
        c.movieClipRgn = movieClip == 0 ? 0 : QuickDrawManager.copyRegion(movieClip);
        if (c.movie != null) {
            c.movie.clipRgn = c.movieClipRgn;
        }
        return 0;
    }

    public static int MCGetClip(int mc, int[] theClip, int[] movieClip) {
        ControllerRec c = controller(mc);
        if (theClip != null && theClip.length > 0) {
            theClip[0] = c.clipRgn == 0 ? 0 : QuickDrawManager.copyRegion(c.clipRgn);
        }
        if (movieClip != null && movieClip.length > 0) {
            movieClip[0] = c.movieClipRgn == 0 ? 0 : QuickDrawManager.copyRegion(c.movieClipRgn);
        }
        return 0;
    }

    public static int MCInvalidate(int mc, int window, int rgn) {
        ControllerRec c = controller(mc);
        if (c.movie != null) {
            MovieDrawing.invalidate(c.movie);
        }
        return 0;
    }

    public static int MCGetControllerInfo(int mc, int[] info) {
        ControllerRec c = controller(mc);
        int f = 0;
        if (c.movie != null) {
            if (c.movie.timeBase.rate != 0) {
                f |= mcInfoIsPlaying;
            }
            if (MovieAccess.hasSound(c.movie)) {
                f |= mcInfoHasSound;
            }
            if (c.editing) {
                f |= mcInfoEditingEnabled | mcInfoPasteAvailable;
                if (c.movie.selectionDuration > 0) {
                    f |= mcInfoCutAvailable | mcInfoCopyAvailable | mcInfoClearAvailable;
                }
                if (c.undo != null) {
                    f |= mcInfoUndoAvailable;
                }
            }
        }
        if (c.looping) {
            f |= mcInfoIsLooping;
        }
        if (c.palindrome) {
            f |= mcInfoIsInPalindrome;
        }
        info[0] = f;
        return 0;
    }

    /** @return a snapshot of the controller port, for components drawing it */
    static BufferedImage snapshot(int mc) {
        ControllerRec c = controller(mc);
        return c.port == 0 ? null : QuickDrawManager.snapshot(c.port);
    }
}
