package quicktime.std.clocks;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ClockManager;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.jdirect.SoundLib;
import quicktime.sound.SndChannel;
import quicktime.sound.SoundException;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;

/**
 * The Clock implements methods for a corresponding QuickTime component.
 * Clock components offer two basic services. They generate time information
 * and schedule time-based callback events. Its methods offer access to a
 * component's structure and its fields.
 * refer to Clock Component in QuickTime
 */
public final class Clock extends Component implements QuickTimeLib, SoundLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Clock.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Opens the specified component,'clok', with the given subType.
     * QuickTime::OpenADefaultComponent
     */
    public Clock(int paramInt) throws QTException {
        super(1668050795, paramInt);
    }

    Clock(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Allows your application to obtain the current time according to the specified clock.
     * QuickTime::ClockGetTime
     * @return The time value that contains the current time from the specified time base in specified time scale
     * @return {@link quicktime.QTException}
     */
    public TimeRecord getTime() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        int i = ClockGetTime(_ID(), QTObject.ID(localTimeRecord));
        StdQTException.checkError(i);
        return localTimeRecord;
    }

    /**
     * Returns the clock of the SndChannel.
     * QuickTime::SndSetInfo
     * @return the clock
     * @return {@link quicktime.sound.SoundException}
     */
    public static Clock fromSndChannel(SndChannel paramSndChannel) throws SoundException {
        int[] arrayOfInt = { 0 };
        SoundException.checkError(SndGetInfo(QTObject.ID(paramSndChannel), 1935895659, arrayOfInt));
        return new Clock(arrayOfInt[0], paramSndChannel);
    }

    private static short SndGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SoundManager.SndGetInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int ClockGetTime(int paramInt1, int paramInt2) {
        return ClockManager.ClockGetTime(paramInt1, paramInt2);
    }
}
