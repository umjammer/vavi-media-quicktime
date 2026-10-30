package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ClockManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.MusicManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.ICMFrameTime;
import quicktime.std.movies.Movie;
import quicktime.std.music.TunePlayer;
import quicktime.std.sg.SequenceGrabber;
import quicktime.streaming.Presentation;
import quicktime.util.QTUtils;


/**
 * The TimeBase defines the QuickTime time coordinate system of a movie.
 * May be used for general timing purposes.
 * refer to TimeBaseRecord in QuickTime
 */
public final class TimeBase extends QTObject implements QuickTimeLib, QTStreamingLib {

    /**
     * Return the TimeBase that is associated with this record. If there is
     * no TimeBase then returns null.
     *
     * @see quicktime.std.clocks.TimeRecord#getTimeBase()
     */
    public static TimeBase fromTimeRecord(TimeRecord timeRecord) {
        int i = getIntFromPointer(QTObject.ID(timeRecord), 12);
        if (i == 0) {
            return null;
        }
        return new TimeBase(i, timeRecord);
    }

    /**
     * Retrieve the time base of a callback event.
     *
     * @return the time base of the callback.
     * @see "QuickTime::GetCallBackTimeBase()"
     * @see quicktime.std.clocks.QTCallBack#getTimeBase()
     */
    public static TimeBase fromQTCallBack(QTCallBack cb) {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetCallBackTimeBase(QTObject.ID(cb));
        }
        return new TimeBase(i, cb);
    }

    /**
     * @return the current time base - can be null
     * @see quicktime.std.image.ICMFrameTime#getBase()
     */
    public static TimeBase fromICMFrameTime(ICMFrameTime icmFrameTime) {
        int i = getIntFromArray(icmFrameTime.getBytes(), 12);
        return i == 0 ? null : new TimeBase(i, icmFrameTime);
    }

    /**
     * This method allows you to get movie's TimeBase.
     *
     * @return a TimeBase object for movie's timebase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetMovieTimeBase()"
     * @see quicktime.std.movies.Movie#getTimeBase()
     */
    public static TimeBase fromMovie(Movie movie) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetMovieTimeBase(QTObject.ID(movie));
            StdQTException.checkError(GetMoviesError());
        }
        return new TimeBase(i, movie);
    }

    /**
     * Returns the TunePlayer's current time Base.
     *
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TuneGetTimeBase"
     * @see quicktime.std.music.TunePlayer#getTimeBase()
     */
    public static TimeBase fromTunePlayer(TunePlayer tp) throws StdQTException {
        int[] tb = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(TuneGetTimeBase(QTObject.ID(tp), tb));
        }
        return new TimeBase(tb[0], tp);
    }

    /**
     * Returns the Presentation's current time Base.
     *
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::QTSPresGetTimeBase"
     * @see quicktime.streaming.Presentation#getTimeBase()
     */
    public static TimeBase fromPresentation(Presentation presentation) throws StdQTException {
        int[] timeBase = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTSPresGetTimeBase(QTObject.ID(presentation), timeBase));
        }
        return new TimeBase(timeBase[0], presentation);
    }

    /**
     * Returns the SequenceGrabber's current time Base.
     *
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TuneGetTimeBase"
     * @see "quicktime.std.music.SequenceGrabber#getTimeBase"
     */
    public static TimeBase fromSequenceGrabber(SequenceGrabber sg) throws StdQTException {
        int[] timeBase = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SGGetTimeBase(QTObject.ID(sg), timeBase));
        }
        return new TimeBase(timeBase[0], sg);
    }

    /**
     * Returns the Time Base of the Wall Clock
     *
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::QTGetWallClockTimeBase"
     */
    public TimeBase fromWallClock() throws StdQTException {
        int[] tb = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTGetWallClockTimeBase(tb));
        }
        return new TimeBase(tb[0], this);
    }

    /**
     * This allows your application to obtain a new time base.
     *
     * @see "QuickTime::NewTimeBase"
     */
    public TimeBase() throws QTException {
        super(allocate());
    }

    private static int allocate() throws QTException {
        int i = NewTimeBase();
        StdQTException.checkError(MemError());
        return i;
    }

    private TimeBase(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * This allows your application to obtain the current time value from a time base in its preferred time scale.
     *
     * @return The time value that contains the current time from the specified time base in specified time scale
     * @see "QuickTime::GetTimeBaseTime"
     */
    public int getTime() {
        return getTime(0);
    }

    /**
     * This allows your application to obtain the current time value from a time base in the default scale of the time base.
     *
     * @return the current time specified as a TimeRecord
     * @throws quicktime.QTException
     * @see "QuickTime::GetTimeBaseTime"
     */
    public TimeRecord getTRTime() throws QTException {
        return getTRTime(0);
    }

    /**
     * This allows your application to obtain the current time value from a time base.
     *
     * @param scale Specifies the time scale in which to return the current time value
     * @return the current time specified as a TimeRecord
     * @throws quicktime.QTException
     * @see "QuickTime::GetTimeBaseTime"
     */
    public TimeRecord getTRTime(int scale) throws QTException {
        TimeRecord tr = new TimeRecord();
        synchronized (QTNative.globalsLock) {
            GetTimeBaseTime(_ID(), scale, QTObject.ID(tr));
        }
        return tr;
    }

    /**
     * This allows your application to set the current time of a time base.
     *
     * @param tr The time structure that contains the current time
     * @see "QuickTime::SetTimeBaseTime"
     */
    public void setTime(TimeRecord tr) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseTime(_ID(), QTObject.ID(tr));
        }
    }

    /**
     * This allows your application to set the current time of a time base.
     *
     * @param time  Specifies the new time value
     * @param scale Specifies the time scale of the new time value
     * @see "QuickTime::SetTimeBaseValue"
     */
    public void setValue(int time, int scale) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseValue(_ID(), time, scale);
        }
    }

    /**
     * This allows your application to set the rate of a time base.
     *
     * @param rate Specifies the time rate of the time base
     * @see "QuickTime::SetTimeBaseRate"
     */
    public void setRate(float rate) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseRate(_ID(), QTUtils.X2Fix(rate));
        }
    }

    /**
     * This allows you to determine the start time of a time base in the preferred scale of the time base
     *
     * @return The time value that contains the start time from the specified time base in the specified time scale
     * @see "QuickTime::GetTimeBaseStartTime"
     */
    public int getStartTime() {
        return getStartTime(0);
    }

    /**
     * This allows you to determine the start time of a time base in the default scale of the time base.
     *
     * @return The time value that contains the start time from the specified time base in the specified time scale
     * @throws quicktime.QTException
     * @see "QuickTime::GetTimeBaseStartTime"
     */
    public TimeRecord getTRStartTime() throws QTException {
        return getTRStartTime(0);
    }

    /**
     * This allows you to determine the start time of a time base.
     *
     * @param scale Specifies the time scale in which to return the start time
     * @return The time value that contains the start time from the specified time base in the specified time scale
     * @throws quicktime.QTException
     * @see "QuickTime::GetTimeBaseStartTime"
     */
    public TimeRecord getTRStartTime(int scale) throws QTException {
        TimeRecord tr = null;
        synchronized (QTNative.globalsLock) {
            tr = new TimeRecord();
            GetTimeBaseStartTime(_ID(), scale, QTObject.ID(tr));
        }
        return tr;
    }

    /**
     * This allows you to set the start time of a time base and defines the time base's the minimum time value.
     *
     * @param tr The time object that contains the start time value
     * @see "QuickTime::SetTimeBaseStartTime"
     */
    public void setStartTime(TimeRecord tr) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseStartTime(_ID(), QTObject.ID(tr));
        }
    }

    /**
     * This allows you to determine the stop time of a time base in the preferred time scale of the TimeBase
     *
     * @return The time value that contains the stop time from the specified time base in the specified time scale
     * @see "QuickTime::GetTimeBaseStopTime"
     */
    public int getStopTime() {
        return getStopTime(0);
    }

    /**
     * This allows you to determine the stop time of a time base in the default scale of the time base.
     *
     * @return The time value that contains the stop time from the specified time base in the specified time scale
     * @throws quicktime.QTException
     * @see "QuickTime::GetTimeBaseStopTime"
     */
    public TimeRecord getTRStopTime() throws QTException {
        return getTRStopTime(0);
    }

    /**
     * This allows you to determine the stop time of a time base in the default scale of the time base.
     *
     * @return The time value that contains the stop time from the specified time base in the specified time scale
     * @throws quicktime.QTException
     * @see "QuickTime::GetTimeBaseStopTime"
     */
    public TimeRecord getTRStopTime(int scale) throws QTException {
        TimeRecord tr = new TimeRecord();
        synchronized (QTNative.globalsLock) {
            GetTimeBaseStopTime(_ID(), scale, QTObject.ID(tr));
        }
        return tr;
    }

    /**
     * This allows you to set the stop time of a time base and defines the time base's the maximum time value.
     *
     * @param tr The time structure that contains the stop time value
     * @see "QuickTime::SetTimeBaseStopTime"
     */
    public void setStopTime(TimeRecord tr) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseStopTime(_ID(), QTObject.ID(tr));
        }
    }

    /**
     * This allows your application to set the contents of the control flags of a time base.
     *
     * @param timeBaseFlags Specifies the control flags for the time base.
     * @see "QuickTime::SetTimeBaseFlags"
     */
    public void setFlags(int timeBaseFlags) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseFlags(_ID(), timeBaseFlags);
        }
    }

    /**
     * This allows your application to change the offset from a time base to either its master time base or its clock component.
     *
     * @param zero The time structure that corresponds to a 0 time value for the slave time scale
     * @see "QuickTime::SetTimeBaseZero"
     */
    public void setZero(TimeRecord zero) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseZero(_ID(), QTObject.ID(zero));
        }
    }

    /**
     * This allows your application to assign a master time base to a time base.
     *
     * @param master    Specifies the master time base to be assigned to this slaved time base
     * @param slaveZero Specifies the time, in the master time base time scale, that corresponds to a 0 time value for this slaved time base
     * @see "QuickTime::SetTimeBaseMasterTimeBase"
     */
    public void setMasterTimeBase(TimeBase master, TimeRecord slaveZero) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseMasterTimeBase(_ID(), QTObject.ID(master), QTObject.ID(slaveZero));
        }
    }

    /**
     * This allows your application to determine the master time base that is assigned to a time base.
     * If the TimeBase is slaved to a clock this will return null.
     *
     * @return The master time base assigned to this time base or null
     * @see "QuickTime::GetTimeBaseMasterTimeBase"
     * @see #getMasterClock
     */
    public TimeBase getMasterTimeBase() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetTimeBaseMasterTimeBase(_ID());
        }
        return i == 0 ? null : new TimeBase(i, this);
    }

    /**
     * Specifies the clock component that is assigned to a time base.
     *
     * @param clockMeister The specified clock.
     * @param slaveZero    The specified time record used in offsetting the time value of the time base and specified clock.
     * @return A clock component instance.
     * @see "QuickTime::SetTimeBaseMasterClock"
     */
    public void setMasterClock(Clock clockMeister, TimeRecord slaveZero) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseMasterClock(_ID(), QTObject.ID(clockMeister), QTObject.ID(slaveZero));
        }
    }

    /**
     * Determines the clock component that is assigned to a time base. If this call returns null then the TimeBase
     * is not slaved to a clock, but is slaved to another TimeBase.
     *
     * @return A clock component instance or null
     * @see "QuickTime::GetTimeBaseMasterClock"
     * @see #getMasterTimeBase
     */
    public Clock getMasterClock() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetTimeBaseMasterClock(_ID());
        }
        return i == 0 ? null : new Clock(i, this);
    }

    /**
     * Print information about this TimeBase object.
     *
     * @return a string representing this TimeBase object
     */
    public String toString() {
        return super.toString() + "[rate=" + getRate() + ",flags=" + getFlags() + "]";
    }

    /**
     * This allows your application to obtain the current time value from a time base.
     *
     * @param scale Specifies the time scale in which to return the current time value
     * @return The time value that contains the current time from the specified time base in specified time scale
     * @see "QuickTime::GetTimeBaseTime"
     */
    public int getTime(int scale) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseTime(_ID(), scale, 0);
        }
    }

    /**
     * This allows your application to retrieve the rate of a time base.
     *
     * @return The current rate of the time base as a float
     * @see "QuickTime::GetTimeBaseRate"
     */
    public float getRate() {
        synchronized (QTNative.globalsLock) {
            return QTUtils.Fix2X(GetTimeBaseRate(_ID()));
        }
    }

    /**
     * Useful when needed to make scheduling decisions based on the rate of a time base.
     *
     * @return The effective rate as an float
     * @see "QuickTime::GetTimeBaseEffectiveRate"
     */
    public float getEffectiveRate() {
        synchronized (QTNative.globalsLock) {
            return QTUtils.Fix2X(GetTimeBaseEffectiveRate(_ID()));
        }
    }

    /**
     * This allows you to determine the start time of a time base.
     *
     * @param scale Specifies the time scale in which to return the start time
     * @return The time value that contains the start time from the specified time base in the specified time scale
     * @see "QuickTime::GetTimeBaseStartTime"
     */
    public int getStartTime(int scale) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStartTime(_ID(), scale, 0);
        }
    }

    /**
     * This allows you to determine the stop time of a time base in the default scale of the time base.
     *
     * @param scale Specifies the time scale in which to return the stop time
     * @return The time value that contains the stop time from the specified time base in the specified time scale
     * @see "QuickTime::GetTimeBaseStopTime"
     */
    public int getStopTime(int scale) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStopTime(_ID(), scale, 0);
        }
    }

    /**
     * This allows your application to obtain the contents of the control flags of a time base.
     * Flags defined are loopTimeBase or palindromeLoopTimeBase, cf quicktime.std.movies package.
     *
     * @return the control flags.
     * @see "QuickTime::GetTimeBaseFlags"
     */
    public int getFlags() {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseFlags(_ID());
        }
    }

    /**
     * This allows your application to retrieve status information from a time base.
     *
     * @param unpinnedTime The time structure that will receive the current time of the time base
     * @return The flags that indicate whether the returned time value is outside the range of values specified by the start and stop times of the time base
     * @see "QuickTime::GetTimeBaseStatus"
     */
    public int getStatus(TimeRecord unpinnedTime) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStatus(_ID(), QTObject.ID(unpinnedTime));
        }
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static int GetCallBackTimeBase(int cb) {
        return ClockManager.GetCallBackTimeBase(cb);
    }

    private static int GetMovieTimeBase(int movie) {
        return MovieToolbox.GetMovieTimeBase(movie);
    }

    private static int TuneGetTimeBase(int tp, int[] tb) {
        return MusicManager.TuneGetTimeBase(tp, tb);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static int NewTimeBase() {
        return ClockManager.NewTimeBase();
    }

    private static int GetTimeBaseTime(int tb, int scale, int tr) {
        return ClockManager.GetTimeBaseTime(tb, scale, tr);
    }

    private static void SetTimeBaseTime(int tb, int tr) {
        ClockManager.SetTimeBaseTime(tb, tr);
    }

    private static void SetTimeBaseValue(int tb, int value, int scale) {
        ClockManager.SetTimeBaseValue(tb, value, scale);
    }

    private static int GetTimeBaseRate(int tb) {
        return ClockManager.GetTimeBaseRate(tb);
    }

    private static void SetTimeBaseRate(int tb, int rate) {
        ClockManager.SetTimeBaseRate(tb, rate);
    }

    private static int GetTimeBaseEffectiveRate(int tb) {
        return ClockManager.GetTimeBaseEffectiveRate(tb);
    }

    private static int GetTimeBaseStartTime(int tb, int scale, int tr) {
        return ClockManager.GetTimeBaseStartTime(tb, scale, tr);
    }

    private static void SetTimeBaseStartTime(int tb, int tr) {
        ClockManager.SetTimeBaseStartTime(tb, tr);
    }

    private static int GetTimeBaseStopTime(int tb, int scale, int tr) {
        return ClockManager.GetTimeBaseStopTime(tb, scale, tr);
    }

    private static void SetTimeBaseStopTime(int tb, int tr) {
        ClockManager.SetTimeBaseStopTime(tb, tr);
    }

    private static int GetTimeBaseFlags(int tb) {
        return ClockManager.GetTimeBaseFlags(tb);
    }

    private static void SetTimeBaseFlags(int tb, int flags) {
        ClockManager.SetTimeBaseFlags(tb, flags);
    }

    private static int GetTimeBaseStatus(int tb, int unpinnedTime) {
        return ClockManager.GetTimeBaseStatus(tb, unpinnedTime);
    }

    private static void SetTimeBaseZero(int tb, int zero) {
        ClockManager.SetTimeBaseZero(tb, zero);
    }

    private static void SetTimeBaseMasterTimeBase(int tb, int master, int slaveZero) {
        ClockManager.SetTimeBaseMasterTimeBase(tb, master, slaveZero);
    }

    private static int GetTimeBaseMasterTimeBase(int tb) {
        return ClockManager.GetTimeBaseMasterTimeBase(tb);
    }

    private static void SetTimeBaseMasterClock(int tb, int clock, int slaveZero) {
        ClockManager.SetTimeBaseMasterClock(tb, clock, slaveZero);
    }

    private static int GetTimeBaseMasterClock(int tb) {
        return ClockManager.GetTimeBaseMasterClock(tb);
    }

    private static int SGGetTimeBase(int sg, int[] timeBase) {
        return SequenceGrabberManager.SGGetTimeBase(sg, timeBase);
    }

    private static short QTSPresGetTimeBase(int presentation, int[] timeBase) {
        return StreamingManager.QTSPresGetTimeBase(presentation, timeBase);
    }

    private static short QTGetWallClockTimeBase(int[] tb) {
        return ClockManager.QTGetWallClockTimeBase(tb);
    }
}
