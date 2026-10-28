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
    public static TimeBase fromTimeRecord(TimeRecord paramTimeRecord) {
        int i = getIntFromPointer(QTObject.ID(paramTimeRecord), 12);
        if (i == 0) {
            return null;
        }
        return new TimeBase(i, paramTimeRecord);
    }

    /**
     * Retrieve the time base of a callback event.
     *
     * @return the time base of the callback.
     * @see "QuickTime::GetCallBackTimeBase()"
     * @see quicktime.std.clocks.QTCallBack#getTimeBase()
     */
    public static TimeBase fromQTCallBack(QTCallBack paramQTCallBack) {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetCallBackTimeBase(QTObject.ID(paramQTCallBack));
        }
        return new TimeBase(i, paramQTCallBack);
    }

    /**
     * @return the current time base - can be null
     * @see quicktime.std.image.ICMFrameTime#getBase()
     */
    public static TimeBase fromICMFrameTime(ICMFrameTime paramICMFrameTime) {
        int i = getIntFromArray(paramICMFrameTime.getBytes(), 12);
        return i == 0 ? null : new TimeBase(i, paramICMFrameTime);
    }

    /**
     * This method allows you to get movie's TimeBase.
     *
     * @return a TimeBase object for movie's timebase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetMovieTimeBase()"
     * @see quicktime.std.movies.Movie#getTimeBase()
     */
    public static TimeBase fromMovie(Movie paramMovie) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetMovieTimeBase(QTObject.ID(paramMovie));
            StdQTException.checkError(GetMoviesError());
        }
        return new TimeBase(i, paramMovie);
    }

    /**
     * Returns the TunePlayer's current time Base.
     *
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TuneGetTimeBase"
     * @see quicktime.std.music.TunePlayer#getTimeBase()
     */
    public static TimeBase fromTunePlayer(TunePlayer paramTunePlayer) throws StdQTException {
        int[] arrayOfInt = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(TuneGetTimeBase(QTObject.ID(paramTunePlayer), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramTunePlayer);
    }

    /**
     * Returns the Presentation's current time Base.
     *
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::QTSPresGetTimeBase"
     * @see quicktime.streaming.Presentation#getTimeBase()
     */
    public static TimeBase fromPresentation(Presentation paramPresentation) throws StdQTException {
        int[] arrayOfInt = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTSPresGetTimeBase(QTObject.ID(paramPresentation), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramPresentation);
    }

    /**
     * Returns the SequenceGrabber's current time Base.
     *
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TuneGetTimeBase"
     * @see "quicktime.std.music.SequenceGrabber#getTimeBase"
     */
    public static TimeBase fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SGGetTimeBase(QTObject.ID(paramSequenceGrabber), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramSequenceGrabber);
    }

    /**
     * Returns the Time Base of the Wall Clock
     *
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::QTGetWallClockTimeBase"
     */
    public TimeBase fromWallClock() throws StdQTException {
        int[] arrayOfInt = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTGetWallClockTimeBase(arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], this);
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

    private TimeBase(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
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
        TimeRecord localTimeRecord = new TimeRecord();
        synchronized (QTNative.globalsLock) {
            GetTimeBaseTime(_ID(), scale, QTObject.ID(localTimeRecord));
        }
        return localTimeRecord;
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
        TimeRecord localTimeRecord = null;
        synchronized (QTNative.globalsLock) {
            localTimeRecord = new TimeRecord();
            GetTimeBaseStartTime(_ID(), scale, QTObject.ID(localTimeRecord));
        }
        return localTimeRecord;
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
    public TimeRecord getTRStopTime(int paramInt) throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        synchronized (QTNative.globalsLock) {
            GetTimeBaseStopTime(_ID(), paramInt, QTObject.ID(localTimeRecord));
        }
        return localTimeRecord;
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

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static int GetCallBackTimeBase(int paramInt) {
        return ClockManager.GetCallBackTimeBase(paramInt);
    }

    private static int GetMovieTimeBase(int paramInt) {
        return MovieToolbox.GetMovieTimeBase(paramInt);
    }

    private static int TuneGetTimeBase(int paramInt, int[] paramArrayOfInt) {
        return MusicManager.TuneGetTimeBase(paramInt, paramArrayOfInt);
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

    private static int GetTimeBaseTime(int paramInt1, int paramInt2, int paramInt3) {
        return ClockManager.GetTimeBaseTime(paramInt1, paramInt2, paramInt3);
    }

    private static void SetTimeBaseTime(int paramInt1, int paramInt2) {
        ClockManager.SetTimeBaseTime(paramInt1, paramInt2);
    }

    private static void SetTimeBaseValue(int paramInt1, int paramInt2, int paramInt3) {
        ClockManager.SetTimeBaseValue(paramInt1, paramInt2, paramInt3);
    }

    private static int GetTimeBaseRate(int paramInt) {
        return ClockManager.GetTimeBaseRate(paramInt);
    }

    private static void SetTimeBaseRate(int paramInt1, int paramInt2) {
        ClockManager.SetTimeBaseRate(paramInt1, paramInt2);
    }

    private static int GetTimeBaseEffectiveRate(int paramInt) {
        return ClockManager.GetTimeBaseEffectiveRate(paramInt);
    }

    private static int GetTimeBaseStartTime(int paramInt1, int paramInt2, int paramInt3) {
        return ClockManager.GetTimeBaseStartTime(paramInt1, paramInt2, paramInt3);
    }

    private static void SetTimeBaseStartTime(int paramInt1, int paramInt2) {
        ClockManager.SetTimeBaseStartTime(paramInt1, paramInt2);
    }

    private static int GetTimeBaseStopTime(int paramInt1, int paramInt2, int paramInt3) {
        return ClockManager.GetTimeBaseStopTime(paramInt1, paramInt2, paramInt3);
    }

    private static void SetTimeBaseStopTime(int paramInt1, int paramInt2) {
        ClockManager.SetTimeBaseStopTime(paramInt1, paramInt2);
    }

    private static int GetTimeBaseFlags(int paramInt) {
        return ClockManager.GetTimeBaseFlags(paramInt);
    }

    private static void SetTimeBaseFlags(int paramInt1, int paramInt2) {
        ClockManager.SetTimeBaseFlags(paramInt1, paramInt2);
    }

    private static int GetTimeBaseStatus(int paramInt1, int paramInt2) {
        return ClockManager.GetTimeBaseStatus(paramInt1, paramInt2);
    }

    private static void SetTimeBaseZero(int paramInt1, int paramInt2) {
        ClockManager.SetTimeBaseZero(paramInt1, paramInt2);
    }

    private static void SetTimeBaseMasterTimeBase(int paramInt1, int paramInt2, int paramInt3) {
        ClockManager.SetTimeBaseMasterTimeBase(paramInt1, paramInt2, paramInt3);
    }

    private static int GetTimeBaseMasterTimeBase(int paramInt) {
        return ClockManager.GetTimeBaseMasterTimeBase(paramInt);
    }

    private static void SetTimeBaseMasterClock(int paramInt1, int paramInt2, int paramInt3) {
        ClockManager.SetTimeBaseMasterClock(paramInt1, paramInt2, paramInt3);
    }

    private static int GetTimeBaseMasterClock(int paramInt) {
        return ClockManager.GetTimeBaseMasterClock(paramInt);
    }

    private static int SGGetTimeBase(int paramInt, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetTimeBase(paramInt, paramArrayOfInt);
    }

    private static short QTSPresGetTimeBase(int paramInt, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetTimeBase(paramInt, paramArrayOfInt);
    }

    private static short QTGetWallClockTimeBase(int[] paramArrayOfInt) {
        return ClockManager.QTGetWallClockTimeBase(paramArrayOfInt);
    }
}
