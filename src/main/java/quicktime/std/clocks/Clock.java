package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ClockManager;
import quicktime.internal.toolbox.SoundManager;
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

    /**
     * Opens the specified component,'clok', with the given subType.
     * @see "QuickTime::OpenADefaultComponent"
     */
    public Clock(int subType) throws QTException {
        super(1668050795, subType);
    }

    Clock(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Allows your application to obtain the current time according to the specified clock.
     * @see "QuickTime::ClockGetTime"
     * @return The time value that contains the current time from the specified time base in specified time scale
     * @throws quicktime.QTException
     */
    public TimeRecord getTime() throws QTException {
        TimeRecord tr = new TimeRecord();
        int i = ClockGetTime(_ID(), QTObject.ID(tr));
        StdQTException.checkError(i);
        return tr;
    }

    /**
     * Returns the clock of the SndChannel.
     * @see "QuickTime::SndSetInfo"
     * @return the clock
     * @throws quicktime.sound.SoundException
     */
    public static Clock fromSndChannel(SndChannel chan) throws SoundException {
        int[] info = { 0 };
        SoundException.checkError(SndGetInfo(QTObject.ID(chan), 1935895659, info));
        return new Clock(info[0], chan);
    }

    private static short SndGetInfo(int chan, int selector, int[] info) {
        return SoundManager.SndGetInfo(chan, selector, info);
    }

    private static int ClockGetTime(int clock, int tr) {
        return ClockManager.ClockGetTime(clock, tr);
    }
}
