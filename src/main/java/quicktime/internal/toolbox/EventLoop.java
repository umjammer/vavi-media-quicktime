/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Carbon event loop timers, run on daemon threads.
 */
final class EventLoop {

    private static final Logger logger = Logger.getLogger(EventLoop.class.getName());

    private EventLoop() {
    }

    /** the main event loop reference */
    static final int MAIN = Heap.newObject("MainEventLoop", 4);

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "EventLoopTimer");
        t.setDaemon(true);
        return t;
    });

    private record Timer(int proc, int userData, double interval, ScheduledFuture<?>[] future) {
    }

    private static final Map<Integer, Timer> timers = new ConcurrentHashMap<>();

    private static void schedule(int ref, Timer timer, double delay) {
        Runnable fire = () -> {
            try {
                Closures.call(timer.proc, ref, timer.userData);
            } catch (Throwable t) {
                logger.log(Level.WARNING, "event loop timer", t);
            }
        };
        long d = (long) (Math.max(0, delay) * 1_000_000);
        if (timer.interval > 0) {
            timer.future[0] = scheduler.scheduleAtFixedRate(fire, d, (long) (timer.interval * 1_000_000), TimeUnit.MICROSECONDS);
        } else {
            timer.future[0] = scheduler.schedule(fire, d, TimeUnit.MICROSECONDS);
        }
    }

    static int install(double delay, double interval, int proc, int userData, int[] outTimer) {
        int ref = Heap.newObject("EventLoopTimer", 4);
        Timer timer = new Timer(proc, userData, interval, new ScheduledFuture<?>[1]);
        timers.put(ref, timer);
        schedule(ref, timer, delay);
        outTimer[0] = ref;
        return 0;
    }

    static int remove(int ref) {
        Timer timer = timers.remove(ref);
        if (timer == null) {
            return -50;
        }
        timer.future[0].cancel(false);
        Heap.free(ref);
        return 0;
    }

    static int setNextFireTime(int ref, double delay) {
        Timer timer = timers.get(ref);
        if (timer == null) {
            return -50;
        }
        timer.future[0].cancel(false);
        schedule(ref, timer, delay);
        return 0;
    }
}
