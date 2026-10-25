/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * ClockManager.
 * <p>
 * Time bases, time records, clocks and callbacks of the Movie Toolbox.
 * <ul>
 * <li>A time base runs off its master time base, or off the wall clock when it has none.</li>
 * <li>A TimeRecord is a 16 byte structure: value(8, big endian), scale(4), base(4).</li>
 * <li>Callbacks are one shot like on the real toolbox, they are serviced by a clock thread
 * and by every call that changes a time base.</li>
 * </ul>
 */
public final class ClockManager {

    private static final Logger logger = Logger.getLogger(ClockManager.class.getName());

    private ClockManager() {
    }

    // time base flags
    static final int loopTimeBase = 1;
    static final int palindromeLoopTimeBase = 2;
    static final int maintainTimeBaseZero = 4;

    // time base status
    static final int timeBaseBeforeStartTime = 1;
    static final int timeBaseAfterStopTime = 2;

    // callback types
    static final int callBackAtTime = 1;
    static final int callBackAtRate = 2;
    static final int callBackAtTimeJump = 3;
    static final int callBackAtExtremes = 4;

    // callback flags
    static final int triggerTimeFwd = 1;
    static final int triggerTimeBwd = 2;
    static final int triggerTimeEither = 3;
    static final int triggerRateLT = 4;
    static final int triggerRateGT = 8;
    static final int triggerRateEqual = 16;
    static final int triggerRateNotEqual = 32;
    static final int triggerRateChange = 0;
    static final int triggerAtStart = 1;
    static final int triggerAtStop = 2;

    /** the default time scale of a time base */
    static final int DEFAULT_SCALE = 600;

    /** a time base */
    static final class TimeBaseRec {
        final int ref;
        /** rate as 16.16 fixed */
        int rate;
        /** anchor time, in {@link #scale} */
        long value;
        int scale = DEFAULT_SCALE;
        /** wall clock nanos (or master time in {@link #scale}) when {@link #value} was taken */
        long anchorNanos;
        long anchorMaster;
        long start = Long.MIN_VALUE;
        long stop = Long.MAX_VALUE;
        int flags;
        TimeBaseRec master;
        int masterClock;
        final List<TimeBaseRec> slaves = new CopyOnWriteArrayList<>();
        final List<CallBackRec> callBacks = new CopyOnWriteArrayList<>();
        /** last time seen by the callback service, in {@link #scale} */
        long lastServiced;
        /** owner of the time base (a movie), may be null */
        Object owner;
        /** whether the time base was disposed with its owner, wall clock time bases are shared */
        boolean shared;

        TimeBaseRec() {
            this.ref = Heap.newObject(this, 16);
            this.anchorNanos = System.nanoTime();
        }

        /** @return the master time in this time base's scale */
        private long masterTime() {
            if (master != null) {
                return master.time(scale);
            }
            return Math.floorDiv((System.nanoTime() - anchorNanos) * scale, 1_000_000_000L);
        }

        /** @return the raw time without start/stop handling, in this scale */
        private long rawTime() {
            if (rate == 0) {
                return value;
            }
            long elapsed = master != null ? master.time(scale) - anchorMaster : masterTime();
            return value + Math.floorDiv(elapsed * rate, 0x10000L);
        }

        /** @return the current time in the scale */
        synchronized long time(int toScale) {
            long t = rawTime();
            if (start != Long.MIN_VALUE && stop != Long.MAX_VALUE && stop > start && (flags & (loopTimeBase | palindromeLoopTimeBase)) != 0) {
                long length = stop - start;
                long d = Math.floorMod(t - start, (flags & palindromeLoopTimeBase) != 0 ? length * 2 : length);
                t = start + (d > length ? length * 2 - d : d);
            } else {
                if (t > stop) {
                    t = stop;
                }
                if (t < start) {
                    t = start;
                }
            }
            return convert(t, scale, toScale == 0 ? scale : toScale);
        }

        /** re anchors the time at the current time, before changing rate, master or time */
        synchronized void anchor() {
            long t = time(scale);
            value = t;
            anchorNanos = System.nanoTime();
            anchorMaster = master != null ? master.time(scale) : 0;
        }

        /** slaves follow, their time is derived from this time */
        synchronized void setTime(long v, int s) {
            if (s != 0) {
                scale = s;
            }
            value = v;
            anchorNanos = System.nanoTime();
            anchorMaster = master != null ? master.time(scale) : 0;
            lastServiced = value;
        }

        void setRate(int newRate) {
            int old = effectiveRate();
            anchor();
            synchronized (this) {
                rate = newRate;
            }
            fireRateChanged(old);
        }

        /** @return the effective rate as 16.16 fixed */
        int effectiveRate() {
            long r = rate;
            for (TimeBaseRec m = master; m != null; m = m.master) {
                r = (r * m.rate) >> 16;
            }
            return (int) r;
        }

        int status() {
            long t = rawTime();
            if (t < start) {
                return timeBaseBeforeStartTime;
            }
            if (t > stop) {
                return timeBaseAfterStopTime;
            }
            return 0;
        }

        void fireRateChanged(int old) {
            int now = effectiveRate();
            for (CallBackRec cb : callBacks) {
                if (cb.type == callBackAtRate && cb.armed) {
                    boolean fire = switch (cb.flags) {
                        case triggerRateLT -> now < cb.param;
                        case triggerRateGT -> now > cb.param;
                        case triggerRateEqual -> now == cb.param;
                        case triggerRateNotEqual -> now != cb.param;
                        case triggerRateLT | triggerRateEqual -> now <= cb.param;
                        case triggerRateGT | triggerRateEqual -> now >= cb.param;
                        default -> now != old;
                    };
                    if (fire) {
                        cb.fire();
                    }
                }
            }
            for (TimeBaseRec slave : slaves) {
                slave.fireRateChanged((int) ((long) slave.rate * old >> 16));
            }
        }

        void fireTimeJump() {
            for (CallBackRec cb : callBacks) {
                if (cb.type == callBackAtTimeJump && cb.armed) {
                    cb.fire();
                }
            }
            for (TimeBaseRec slave : slaves) {
                slave.fireTimeJump();
            }
        }

        /** fires time and extremes callbacks passed since the last service */
        void service() {
            if (callBacks.isEmpty()) {
                return;
            }
            long now = time(scale);
            long last = lastServiced;
            lastServiced = now;
            if (now == last && effectiveRate() == 0) {
                return;
            }
            for (CallBackRec cb : callBacks) {
                if (!cb.armed) {
                    continue;
                }
                if (cb.type == callBackAtTime) {
                    long t = convert(cb.param, cb.scale == 0 ? scale : cb.scale, scale);
                    boolean fwd = last < t && t <= now;
                    boolean bwd = now <= t && t < last;
                    if (((cb.flags & triggerTimeFwd) != 0 && fwd) || ((cb.flags & triggerTimeBwd) != 0 && bwd)) {
                        cb.fire();
                    }
                } else if (cb.type == callBackAtExtremes) {
                    if ((cb.flags & triggerAtStop) != 0 && stop != Long.MAX_VALUE && last < stop && now >= stop) {
                        cb.fire();
                    } else if ((cb.flags & triggerAtStart) != 0 && start != Long.MIN_VALUE && last > start && now <= start) {
                        cb.fire();
                    }
                }
            }
        }
    }

    /** a callback */
    static final class CallBackRec {
        final int ref;
        final TimeBaseRec timeBase;
        final int type;
        int proc;
        int refCon;
        int flags;
        int param;
        int scale;
        boolean armed;

        CallBackRec(TimeBaseRec timeBase, int type) {
            this.ref = Heap.newObject(this, 4);
            this.timeBase = timeBase;
            this.type = type;
        }

        void fire() {
            armed = false;
            try {
                Closures.call(proc, ref, refCon);
            } catch (Throwable t) {
                logger.log(Level.WARNING, "callback", t);
            }
        }
    }

    private static final List<TimeBaseRec> timeBases = new CopyOnWriteArrayList<>();

    private static Thread service;

    private static synchronized void startService() {
        if (service == null) {
            service = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(5);
                        serviceAll();
                    } catch (InterruptedException e) {
                        break;
                    } catch (Throwable t) {
                        logger.log(Level.WARNING, "clock service", t);
                    }
                }
            }, "QuickTimeClock");
            service.setDaemon(true);
            service.start();
        }
    }

    /** services the callbacks of all time bases */
    static void serviceAll() {
        for (TimeBaseRec tb : timeBases) {
            tb.service();
        }
    }

    /** @return a new time base */
    static TimeBaseRec newTimeBase(Object owner) {
        TimeBaseRec tb = new TimeBaseRec();
        tb.owner = owner;
        timeBases.add(tb);
        return tb;
    }

    static void disposeTimeBase(TimeBaseRec tb) {
        if (tb == null) {
            return;
        }
        timeBases.remove(tb);
        if (tb.master != null) {
            tb.master.slaves.remove(tb);
        }
        for (TimeBaseRec slave : tb.slaves) {
            slave.anchor();
            slave.master = null;
        }
        for (CallBackRec cb : tb.callBacks) {
            Heap.free(cb.ref);
        }
        Heap.free(tb.ref);
    }

    static TimeBaseRec timeBase(int ref) {
        return Heap.peer(ref, TimeBaseRec.class);
    }

    /** converts a time value between scales, rounding down */
    static long convert(long value, int fromScale, int toScale) {
        if (fromScale == toScale || fromScale == 0 || toScale == 0) {
            return value;
        }
        if (value == Long.MAX_VALUE || value == Long.MIN_VALUE) {
            return value;
        }
        return Math.floorDiv(value * toScale, fromScale);
    }

    // ---- time records

    static long recordValue(int tr) {
        return Heap.getLong(tr);
    }

    static int recordScale(int tr) {
        return Heap.getInt(tr + 8);
    }

    static int recordBase(int tr) {
        return Heap.getInt(tr + 12);
    }

    static void setRecord(int tr, long value, int scale, int base) {
        if (tr != 0) {
            Heap.putLong(tr, value);
            Heap.putInt(tr + 8, scale);
            Heap.putInt(tr + 12, base);
        }
    }

    /** @return the time value of the record in the scale, converting through its time base if needed */
    static long recordValueIn(int tr, int scale) {
        return convert(recordValue(tr), recordScale(tr), scale);
    }

    public static void AddTime(int dst, int src) {
        int scale = recordScale(dst);
        setRecord(dst, recordValue(dst) + recordValueIn(src, scale), scale, recordBase(dst));
    }

    public static void SubtractTime(int dst, int src) {
        int scale = recordScale(dst);
        setRecord(dst, recordValue(dst) - recordValueIn(src, scale), scale, recordBase(dst));
    }

    public static void ConvertTimeScale(int tr, int newScale) {
        setRecord(tr, recordValueIn(tr, newScale), newScale, recordBase(tr));
    }

    /** converts the record to the time of another time base, both must share a master or the wall clock */
    public static void ConvertTime(int tr, int newBase) {
        TimeBaseRec from = Heap.peerOrNull(recordBase(tr), TimeBaseRec.class);
        TimeBaseRec to = Heap.peerOrNull(newBase, TimeBaseRec.class);
        int scale = recordScale(tr);
        long value = recordValue(tr);
        if (from != null && to != null && from != to) {
            // time relative to "now" is carried over at the effective rates of both time bases
            long nowFrom = from.time(scale);
            long nowTo = to.time(scale);
            int rf = from.effectiveRate();
            int rt = to.effectiveRate();
            long delta = value - nowFrom;
            value = nowTo + (rf == 0 ? 0 : delta * rt / rf);
        }
        setRecord(tr, value, scale, newBase);
    }

    // ---- time bases

    public static int NewTimeBase() {
        startService();
        Heap.setMemError(0);
        return newTimeBase(null).ref;
    }

    public static void DisposeTimeBase(int tb) {
        TimeBaseRec t = Heap.peerOrNull(tb, TimeBaseRec.class);
        if (t != null && !t.shared) {
            disposeTimeBase(t);
        }
    }

    private static TimeBaseRec wallClock;

    public static short QTGetWallClockTimeBase(int[] tb) {
        synchronized (ClockManager.class) {
            if (wallClock == null) {
                wallClock = newTimeBase(null);
                wallClock.shared = true;
                wallClock.scale = 1000;
                wallClock.setTime(System.currentTimeMillis(), 1000);
                wallClock.rate = 0x10000;
            }
        }
        tb[0] = wallClock.ref;
        return 0;
    }

    public static int GetTimeBaseTime(int tb, int scale, int tr) {
        TimeBaseRec t = timeBase(tb);
        int s = scale == 0 ? t.scale : scale;
        long v = t.time(s);
        setRecord(tr, v, s, tb);
        return (int) v;
    }

    public static void SetTimeBaseTime(int tb, int tr) {
        TimeBaseRec t = timeBase(tb);
        t.setTime(recordValue(tr), recordScale(tr));
        t.fireTimeJump();
    }

    public static void SetTimeBaseValue(int tb, int value, int scale) {
        TimeBaseRec t = timeBase(tb);
        t.setTime(value, scale);
        t.fireTimeJump();
    }

    public static int GetTimeBaseRate(int tb) {
        return timeBase(tb).rate;
    }

    public static void SetTimeBaseRate(int tb, int rate) {
        timeBase(tb).setRate(rate);
    }

    public static int GetTimeBaseEffectiveRate(int tb) {
        return timeBase(tb).effectiveRate();
    }

    public static int GetTimeBaseStartTime(int tb, int scale, int tr) {
        TimeBaseRec t = timeBase(tb);
        int s = scale == 0 ? t.scale : scale;
        long v = t.start == Long.MIN_VALUE ? 0 : convert(t.start, t.scale, s);
        setRecord(tr, v, s, tb);
        return (int) v;
    }

    public static void SetTimeBaseStartTime(int tb, int tr) {
        TimeBaseRec t = timeBase(tb);
        t.anchor();
        synchronized (t) {
            t.start = recordValueIn(tr, t.scale);
        }
    }

    public static int GetTimeBaseStopTime(int tb, int scale, int tr) {
        TimeBaseRec t = timeBase(tb);
        int s = scale == 0 ? t.scale : scale;
        long v = t.stop == Long.MAX_VALUE ? 0 : convert(t.stop, t.scale, s);
        setRecord(tr, v, s, tb);
        return (int) v;
    }

    public static void SetTimeBaseStopTime(int tb, int tr) {
        TimeBaseRec t = timeBase(tb);
        t.anchor();
        synchronized (t) {
            t.stop = recordValueIn(tr, t.scale);
        }
    }

    public static int GetTimeBaseFlags(int tb) {
        return timeBase(tb).flags;
    }

    public static void SetTimeBaseFlags(int tb, int flags) {
        TimeBaseRec t = timeBase(tb);
        t.anchor();
        t.flags = flags;
    }

    public static int GetTimeBaseStatus(int tb, int unpinnedTime) {
        TimeBaseRec t = timeBase(tb);
        if (unpinnedTime != 0) {
            setRecord(unpinnedTime, t.rawTime(), t.scale, tb);
        }
        return t.status();
    }

    public static void SetTimeBaseZero(int tb, int zero) {
        TimeBaseRec t = timeBase(tb);
        if (t.master != null && zero != 0) {
            // the master time that corresponds to time 0 of this time base
            t.setTime(-recordValueIn(zero, t.scale) + t.master.time(t.scale), t.scale);
        }
    }

    static void setMaster(TimeBaseRec t, TimeBaseRec master, int slaveZero) {
        t.anchor();
        if (t.master != null) {
            t.master.slaves.remove(t);
        }
        for (TimeBaseRec m = master; m != null; m = m.master) {
            if (m == t) {
                throw new IllegalArgumentException("time base loop");
            }
        }
        t.master = master;
        t.masterClock = 0;
        if (master != null) {
            master.slaves.add(t);
        }
        t.anchorNanos = System.nanoTime();
        t.anchorMaster = master != null ? master.time(t.scale) : 0;
        if (slaveZero != 0 && master != null) {
            SetTimeBaseZero(t.ref, slaveZero);
        }
    }

    public static void SetTimeBaseMasterTimeBase(int tb, int master, int slaveZero) {
        setMaster(timeBase(tb), Heap.peerOrNull(master, TimeBaseRec.class), slaveZero);
    }

    public static int GetTimeBaseMasterTimeBase(int tb) {
        TimeBaseRec m = timeBase(tb).master;
        return m == null ? 0 : m.ref;
    }

    public static void SetTimeBaseMasterClock(int tb, int clock, int slaveZero) {
        TimeBaseRec t = timeBase(tb);
        setMaster(t, null, 0);
        t.masterClock = clock;
    }

    public static int GetTimeBaseMasterClock(int tb) {
        TimeBaseRec t = timeBase(tb);
        if (t.master != null) {
            return 0;
        }
        if (t.masterClock == 0) {
            t.masterClock = ComponentManager.defaultClock();
        }
        return t.masterClock;
    }

    // ---- clocks

    private static final long startNanos = System.nanoTime();

    /** clock components keep time in microseconds */
    public static int ClockGetTime(int clock, int tr) {
        long micros = (System.nanoTime() - startNanos) / 1000;
        setRecord(tr, micros, 1_000_000, 0);
        return 0;
    }

    /** @return ticks (1/60 second) since startup */
    public static int TickCount() {
        return (int) ((System.nanoTime() - startNanos) * 60 / 1_000_000_000L);
    }

    // ---- callbacks

    public static int NewCallBack(int tb, short type) {
        TimeBaseRec t = timeBase(tb);
        CallBackRec cb = new CallBackRec(t, type & 0x00ff);
        t.callBacks.add(cb);
        startService();
        return cb.ref;
    }

    static void disposeCallBack(int ref) {
        CallBackRec cb = Heap.peerOrNull(ref, CallBackRec.class);
        if (cb != null) {
            cb.timeBase.callBacks.remove(cb);
            Heap.free(ref);
        }
    }

    public static short CallMeWhen(int cb, int proc, int refCon, int param1, int param2, int param3) {
        CallBackRec c = Heap.peerOrNull(cb, CallBackRec.class);
        if (c == null) {
            return -50;
        }
        c.proc = proc;
        c.refCon = refCon;
        switch (c.type) {
            case callBackAtTime -> {
                c.flags = param1;
                c.param = param2;
                c.scale = param3;
                c.timeBase.lastServiced = c.timeBase.time(c.timeBase.scale);
            }
            case callBackAtRate -> {
                c.param = param1;
                c.flags = param2;
            }
            case callBackAtExtremes -> c.flags = param1;
            default -> {
            }
        }
        c.armed = true;
        return 0;
    }

    public static void CancelCallBack(int cb) {
        CallBackRec c = Heap.peerOrNull(cb, CallBackRec.class);
        if (c != null) {
            c.armed = false;
        }
    }

    public static short GetCallBackType(int cb) {
        return (short) Heap.peer(cb, CallBackRec.class).type;
    }

    public static int GetCallBackTimeBase(int cb) {
        return Heap.peer(cb, CallBackRec.class).timeBase.ref;
    }

    /** used by CDSequence */
    public static short CDSequenceSetTimeBase(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("CDSequenceSetTimeBase");
    }

    /** @return the time bases, for tests */
    static List<TimeBaseRec> timeBases() {
        return new ArrayList<>(timeBases);
    }
}
