
package quicktime.std.clocks;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.jdirect.SoundLib;
import quicktime.sound.SndChannel;
import quicktime.sound.SoundException;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;


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

    public Clock(int paramInt) throws QTException {
        super(1668050795, paramInt);
    }

    Clock(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public TimeRecord getTime() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        int i = ClockGetTime(_ID(), QTObject.ID(localTimeRecord));
        StdQTException.checkError(i);
        return localTimeRecord;
    }

    public static Clock fromSndChannel(SndChannel paramSndChannel) throws SoundException {
        int[] arrayOfInt = {
            0
        };
        SoundException.checkError(SndGetInfo(QTObject.ID(paramSndChannel), 1935895659, arrayOfInt));

        return new Clock(arrayOfInt[0], paramSndChannel);
    }

    private static native short SndGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int ClockGetTime(int paramInt1, int paramInt2);
}
