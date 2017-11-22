
package quicktime.std.clocks;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
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

    public static TimeBase fromTimeRecord(TimeRecord paramTimeRecord) {
        int i = getIntFromPointer(QTObject.ID(paramTimeRecord), 12);
        if (i == 0) {
            return null;
        }

        return new TimeBase(QTObject.ID(paramTimeRecord) + 12, paramTimeRecord);
    }

    public static TimeBase fromQTCallBack(QTCallBack paramQTCallBack) {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetCallBackTimeBase(QTObject.ID(paramQTCallBack));
        }
        return new TimeBase(i, paramQTCallBack);
    }

    public static TimeBase fromICMFrameTime(ICMFrameTime paramICMFrameTime) {
        int i = getIntFromArray(paramICMFrameTime.getBytes(), 12);
        return i == 0 ? null : new TimeBase(i, paramICMFrameTime);
    }

    public static TimeBase fromMovie(Movie paramMovie) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetMovieTimeBase(QTObject.ID(paramMovie));
            StdQTException.checkError(GetMoviesError());
        }
        return new TimeBase(i, paramMovie);
    }

    public static TimeBase fromTunePlayer(TunePlayer paramTunePlayer) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(TuneGetTimeBase(QTObject.ID(paramTunePlayer), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramTunePlayer);
    }

    public static TimeBase fromPresentation(Presentation paramPresentation) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTSPresGetTimeBase(QTObject.ID(paramPresentation), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramPresentation);
    }

    public static TimeBase fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SGGetTimeBase(QTObject.ID(paramSequenceGrabber), arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], paramSequenceGrabber);
    }

    public TimeBase fromWallClock() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTGetWallClockTimeBase(arrayOfInt));
        }
        return new TimeBase(arrayOfInt[0], this);
    }

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

    public int getTime() {
        return getTime(0);
    }

    public TimeRecord getTRTime() throws QTException {
        return getTRTime(0);
    }

    public TimeRecord getTRTime(int paramInt) throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        synchronized (QTNative.globalsLock) {
            GetTimeBaseTime(_ID(), paramInt, QTObject.ID(localTimeRecord));
        }
        return localTimeRecord;
    }

    public void setTime(TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseTime(_ID(), QTObject.ID(paramTimeRecord));
        }
    }

    public void setValue(int paramInt1, int paramInt2) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseValue(_ID(), paramInt1, paramInt2);
        }
    }

    public void setRate(float paramFloat) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseRate(_ID(), QTUtils.X2Fix(paramFloat));
        }
    }

    public int getStartTime() {
        return getStartTime(0);
    }

    public TimeRecord getTRStartTime() throws QTException {
        return getTRStartTime(0);
    }

    public TimeRecord getTRStartTime(int paramInt) throws QTException {
        TimeRecord localTimeRecord = null;
        synchronized (QTNative.globalsLock) {
            localTimeRecord = new TimeRecord();
            GetTimeBaseStartTime(_ID(), paramInt, QTObject.ID(localTimeRecord));
        }
        return localTimeRecord;
    }

    public void setStartTime(TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseStartTime(_ID(), QTObject.ID(paramTimeRecord));
        }
    }

    public int getStopTime() {
        return getStopTime(0);
    }

    public TimeRecord getTRStopTime() throws QTException {
        return getTRStopTime(0);
    }

    public TimeRecord getTRStopTime(int paramInt) throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        synchronized (QTNative.globalsLock) {
            GetTimeBaseStopTime(_ID(), paramInt, QTObject.ID(localTimeRecord));
        }
        return localTimeRecord;
    }

    public void setStopTime(TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseStopTime(_ID(), QTObject.ID(paramTimeRecord));
        }
    }

    public void setFlags(int paramInt) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseFlags(_ID(), paramInt);
        }
    }

    public void setZero(TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseZero(_ID(), QTObject.ID(paramTimeRecord));
        }
    }

    public void setMasterTimeBase(TimeBase paramTimeBase, TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseMasterTimeBase(_ID(), QTObject.ID(paramTimeBase), QTObject.ID(paramTimeRecord));
        }
    }

    public TimeBase getMasterTimeBase() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetTimeBaseMasterTimeBase(_ID());
        }
        return i == 0 ? null : new TimeBase(i, this);
    }

    public void setMasterClock(Clock paramClock, TimeRecord paramTimeRecord) {
        synchronized (QTNative.globalsLock) {
            SetTimeBaseMasterClock(_ID(), QTObject.ID(paramClock), QTObject.ID(paramTimeRecord));
        }
    }

    public Clock getMasterClock() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetTimeBaseMasterClock(_ID());
        }
        return i == 0 ? null : new Clock(i, this);
    }

    public String toString() {
        return super.toString() + "[rate=" + getRate() + ",flags=" + getFlags() + "]";
    }

    /* Error */
    public int getTime(int paramInt) {
        synchronized (QTNative.globalsLock) {
        return GetTimeBaseTime(_ID(), paramInt, 0);
        }
    }

    /* Error */
    public float getRate() {
        synchronized (QTNative.globalsLock) {
            return QTUtils.Fix2X(GetTimeBaseRate(_ID()));
        }
    }

    /* Error */
    public float getEffectiveRate() {
        synchronized (QTNative.globalsLock) {
            return QTUtils.Fix2X(GetTimeBaseEffectiveRate(_ID()));
        }
    }

    /* Error */
    public int getStartTime(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStartTime(_ID(), paramInt, 0);
        }
    }

    /* Error */
    public int getStopTime(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStopTime(_ID(), paramInt, 0);
        }
    }

    /* Error */
    public int getFlags() {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseFlags(_ID());
        }
    }

    /* Error */
    public int getStatus(TimeRecord timerecord) {
        synchronized (QTNative.globalsLock) {
            return GetTimeBaseStatus(_ID(), QTObject.ID(timerecord));
        }
    }

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int GetCallBackTimeBase(int paramInt);

    private static native int GetMovieTimeBase(int paramInt);

    private static native int TuneGetTimeBase(int paramInt, int[] paramArrayOfInt);

    private static native short GetMoviesError();

    private static native short MemError();

    private static native int NewTimeBase();

    private static native int GetTimeBaseTime(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetTimeBaseTime(int paramInt1, int paramInt2);

    private static native void SetTimeBaseValue(int paramInt1, int paramInt2, int paramInt3);

    private static native int GetTimeBaseRate(int paramInt);

    private static native void SetTimeBaseRate(int paramInt1, int paramInt2);

    private static native int GetTimeBaseEffectiveRate(int paramInt);

    private static native int GetTimeBaseStartTime(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetTimeBaseStartTime(int paramInt1, int paramInt2);

    private static native int GetTimeBaseStopTime(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetTimeBaseStopTime(int paramInt1, int paramInt2);

    private static native int GetTimeBaseFlags(int paramInt);

    private static native void SetTimeBaseFlags(int paramInt1, int paramInt2);

    private static native int GetTimeBaseStatus(int paramInt1, int paramInt2);

    private static native void SetTimeBaseZero(int paramInt1, int paramInt2);

    private static native void SetTimeBaseMasterTimeBase(int paramInt1, int paramInt2, int paramInt3);

    private static native int GetTimeBaseMasterTimeBase(int paramInt);

    private static native void SetTimeBaseMasterClock(int paramInt1, int paramInt2, int paramInt3);

    private static native int GetTimeBaseMasterClock(int paramInt);

    private static native int SGGetTimeBase(int paramInt, int[] paramArrayOfInt);

    private static native short QTSPresGetTimeBase(int paramInt, int[] paramArrayOfInt);

    private static native short QTGetWallClockTimeBase(int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * clocks/TimeBase.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
