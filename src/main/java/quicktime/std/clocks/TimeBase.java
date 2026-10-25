package quicktime.std.clocks;

import java.security.AccessController;
import java.security.PrivilegedAction;
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

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(TimeBase.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Return the TimeBase that is associated with this record. If there is
     * no TimeBase then returns null.
     * @see {@link quicktime.std.clocks.TimeRecord#getTimeBase()}
     */
    public static TimeBase fromTimeRecord(TimeRecord paramTimeRecord) {
        int i = getIntFromPointer(QTObject.ID(paramTimeRecord), 12);
        if (i == 0) {
            return null;
        }
        return new TimeBase(QTObject.ID(paramTimeRecord) + 12, paramTimeRecord);
    }

    /**
     * Retrieve the time base of a callback event.
     * QuickTime::GetCallBackTimeBase()
     * @return the time base of the callback.
     * @see {@link quicktime.std.clocks.QTCallBack#getTimeBase()}
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
     * @see {@link quicktime.std.image.ICMFrameTime#getBase()}
     */
    public static TimeBase fromICMFrameTime(ICMFrameTime paramICMFrameTime) {
        int i = getIntFromArray(paramICMFrameTime.getBytes(), 12);
        return i == 0 ? null : new TimeBase(i, paramICMFrameTime);
    }

    /**
     * This method allows you to get movie's TimeBase.
     * QuickTime::GetMovieTimeBase()
     * @return a TimeBase object for movie's timebase
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.Movie#getTimeBase()}
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
     * QuickTime::TuneGetTimeBase
     * @return the TimeBase
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.music.TunePlayer#getTimeBase()}
     */
    public static TimeBase fromTunePlayer(TunePlayer paramTunePlayer) throws StdQTException {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(TuneGetTimeBase(QTObject.ID(paramTunePlayer), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramTunePlayer);
    }

    /**
     * Returns the Presentation's current time Base.
     * QuickTime::QTSPresGetTimeBase
     * @return the TimeBase
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.streaming.Presentation#getTimeBase()}
     */
    public static TimeBase fromPresentation(Presentation paramPresentation) throws StdQTException {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTSPresGetTimeBase(QTObject.ID(paramPresentation), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramPresentation);
    }

    /**
     * Returns the SequenceGrabber's current time Base.
     * QuickTime::TuneGetTimeBase
     * @return the TimeBase
     * @return {@link quicktime.std.StdQTException}
     * @see quicktime.std.music.SequenceGrabber#getTimeBase
     */
    public static TimeBase fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SGGetTimeBase(QTObject.ID(paramSequenceGrabber), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramSequenceGrabber);
    }

    /**
     * Returns the Time Base of the Wall Clock
     * QuickTime::QTGetWallClockTimeBase
     * @return the TimeBase
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeBase fromWallClock() throws StdQTException {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTGetWallClockTimeBase(arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], this);
    }

    /**
     * This allows your application to obtain a new time base.
     * QuickTime::NewTimeBase
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
     * QuickTime::GetTimeBaseTime
     * @return The time value that contains the current time from the specified time base in specified time scale
     */
    public int getTime() {
        return getTime(0);
    }

    /**
     * This allows your application to obtain the current time value from a time base in the default scale of the time base.
     * QuickTime::GetTimeBaseTime
     * @return the current time specified as a TimeRecord
     * @return {@link quicktime.QTException}
     */
    public TimeRecord getTRTime() throws QTException {
        return getTRTime(0);
    }

    /**
     * This allows your application to obtain the current time value from a time base.
     * QuickTime::GetTimeBaseTime
     * @param scale Specifies the time scale in which to return the current time value
     * @return the current time specified as a TimeRecord
     * @return {@link quicktime.QTException}
     */
    public TimeRecord getTRTime(int paramInt) throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        synchronized (QTNative.globalsLock) {
            GetTimeBaseTime(_ID(), paramInt, QTObject.ID(localTimeRecord));
        }
        return localTimeRecord;
    }

    /**
     * This allows your application to set the current time of a time base.
     * QuickTime::SetTimeBaseTime
     * @param tr The time structure that contains the current time
     */
    public void setTime(TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseTime(_ID(), QTObject.ID(paramTimeRecord));
        }
    }

    /**
     * This allows your application to set the current time of a time base.
     * QuickTime::SetTimeBaseValue
     * @param time Specifies the new time value
     * @param scale Specifies the time scale of the new time value
     */
    public void setValue(int paramInt1, int paramInt2) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseValue(_ID(), paramInt1, paramInt2);
        }
    }

    /**
     * This allows your application to set the rate of a time base.
     * QuickTime::SetTimeBaseRate
     * @param rate Specifies the time rate of the time base
     */
    public void setRate(float paramFloat) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseRate(_ID(), QTUtils.X2Fix(paramFloat));
        }
    }

    /**
     * This allows you to determine the start time of a time base in the preferred scale of the time base
     * QuickTime::GetTimeBaseStartTime
     * @return The time value that contains the start time from the specified time base in the specified time scale
     */
    public int getStartTime() {
        return getStartTime(0);
    }

    /**
     * This allows you to determine the start time of a time base in the default scale of the time base.
     * QuickTime::GetTimeBaseStartTime
     * @return The time value that contains the start time from the specified time base in the specified time scale
     * @return {@link quicktime.QTException}
     */
    public TimeRecord getTRStartTime() throws QTException {
        return getTRStartTime(0);
    }

    /**
     * This allows you to determine the start time of a time base.
     * QuickTime::GetTimeBaseStartTime
     * @param scale Specifies the time scale in which to return the start time
     * @return The time value that contains the start time from the specified time base in the specified time scale
     * @return {@link quicktime.QTException}
     */
    public TimeRecord getTRStartTime(int paramInt) throws QTException {
        TimeRecord localTimeRecord = null;
        synchronized (QTNative.globalsLock) {
            localTimeRecord = new TimeRecord();
            GetTimeBaseStartTime(_ID(), paramInt, QTObject.ID(localTimeRecord));
        }
        return localTimeRecord;
    }

    /**
     * This allows you to set the start time of a time base and defines the time base's the minimum time value.
     * QuickTime::SetTimeBaseStartTime
     * @param tr The time object that contains the start time value
     */
    public void setStartTime(TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseStartTime(_ID(), QTObject.ID(paramTimeRecord));
        }
    }

    /**
     * This allows you to determine the stop time of a time base in the preferred time scale of the TimeBase
     * QuickTime::GetTimeBaseStopTime
     * @return The time value that contains the stop time from the specified time base in the specified time scale
     */
    public int getStopTime() {
        return getStopTime(0);
    }

    /**
     * This allows you to determine the stop time of a time base in the default scale of the time base.
     * QuickTime::GetTimeBaseStopTime
     * @return The time value that contains the stop time from the specified time base in the specified time scale
     * @return {@link quicktime.QTException}
     */
    public TimeRecord getTRStopTime() throws QTException {
        return getTRStopTime(0);
    }

    /**
     * This allows you to determine the stop time of a time base in the default scale of the time base.
     * QuickTime::GetTimeBaseStopTime
     * @return The time value that contains the stop time from the specified time base in the specified time scale
     * @return {@link quicktime.QTException}
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
     * QuickTime::SetTimeBaseStopTime
     * @param tr The time structure that contains the stop time value
     */
    public void setStopTime(TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseStopTime(_ID(), QTObject.ID(paramTimeRecord));
        }
    }

    /**
     * This allows your application to set the contents of the control flags of a time base.
     * QuickTime::SetTimeBaseFlags
     * @param timeBaseFlags Specifies the control flags for the time base.
     */
    public void setFlags(int paramInt) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseFlags(_ID(), paramInt);
        }
    }

    /**
     * This allows your application to change the offset from a time base to either its master time base or its clock component.
     * QuickTime::SetTimeBaseZero
     * @param zero The time structure that corresponds to a 0 time value for the slave time scale
     */
    public void setZero(TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseZero(_ID(), QTObject.ID(paramTimeRecord));
        }
    }

    /**
     * This allows your application to assign a master time base to a time base.
     * QuickTime::SetTimeBaseMasterTimeBase
     * @param master Specifies the master time base to be assigned to this slaved time base
     * @param slaveZero Specifies the time, in the master time base time scale, that corresponds to a 0 time value for this slaved time base
     */
    public void setMasterTimeBase(TimeBase paramTimeBase, TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseMasterTimeBase(_ID(), QTObject.ID(paramTimeBase), QTObject.ID(paramTimeRecord));
        }
    }

    /**
     * This allows your application to determine the master time base that is assigned to a time base.
     * If the TimeBase is slaved to a clock this will return null.
     * QuickTime::GetTimeBaseMasterTimeBase
     * @return The master time base assigned to this time base or null
     * @see getMasterClock
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
     * QuickTime::SetTimeBaseMasterClock
     * @param clockMeister The specified clock.
     * @param slaveZero The specified time record used in offsetting the time value of the time base and specified clock.
     * @return A clock component instance.
     */
    public void setMasterClock(Clock paramClock, TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseMasterClock(_ID(), QTObject.ID(paramClock), QTObject.ID(paramTimeRecord));
        }
    }

    /**
     * Determines the clock component that is assigned to a time base. If this call returns null then the TimeBase
     * is not slaved to a clock, but is slaved to another TimeBase.
     * QuickTime::GetTimeBaseMasterClock
     * @return A clock component instance or null
     * @see getMasterTimeBase
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
     * @return a string representing this TimeBase object
     */
    public String toString() {
        return super.toString() + "[rate=" + getRate() + ",flags=" + getFlags() + "]";
    }

    /**
     * This allows your application to obtain the current time value from a time base.
     * QuickTime::GetTimeBaseTime
     * @param scale Specifies the time scale in which to return the current time value
     * @return The time value that contains the current time from the specified time base in specified time scale
     */
    public int getTime(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseTime(_ID(), paramInt, 0);
        }
    }

    /**
     * This allows your application to retrieve the rate of a time base.
     * QuickTime::GetTimeBaseRate
     * @return The current rate of the time base as a float
     */
    public float getRate() {
        synchronized (QTNative.globalsLock) {
            return QTUtils.Fix2X(GetTimeBaseRate(_ID()));
        }
    }

    /**
     * Useful when needed to make scheduling decisions based on the rate of a time base.
     * QuickTime::GetTimeBaseEffectiveRate
     * @return The effective rate as an float
     */
    public float getEffectiveRate() {
        synchronized (QTNative.globalsLock) {
            return QTUtils.Fix2X(GetTimeBaseEffectiveRate(_ID()));
        }
    }

    /**
     * This allows you to determine the start time of a time base.
     * QuickTime::GetTimeBaseStartTime
     * @param scale Specifies the time scale in which to return the start time
     * @return The time value that contains the start time from the specified time base in the specified time scale
     */
    public int getStartTime(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStartTime(_ID(), paramInt, 0);
        }
    }

    /**
     * This allows you to determine the stop time of a time base in the default scale of the time base.
     * QuickTime::GetTimeBaseStopTime
     * @param scale Specifies the time scale in which to return the stop time
     * @return The time value that contains the stop time from the specified time base in the specified time scale
     */
    public int getStopTime(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStopTime(_ID(), paramInt, 0);
        }
    }

    /**
     * This allows your application to obtain the contents of the control flags of a time base.
     * Flags defined are loopTimeBase or palindromeLoopTimeBase, cf quicktime.std.movies package.
     * QuickTime::GetTimeBaseFlags
     * @return the control flags.
     */
    public int getFlags() {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseFlags(_ID());
        }
    }

    /**
     * This allows your application to retrieve status information from a time base.
     * QuickTime::GetTimeBaseStatus
     * @param unpinnedTime The time structure that will receive the current time of the time base
     * @return The flags that indicate whether the returned time value is outside the range of values specified by the start and stop times of the time base
     */
    public int getStatus(TimeRecord timerecord) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStatus(_ID(), QTObject.ID(timerecord));
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

