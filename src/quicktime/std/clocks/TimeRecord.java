
package quicktime.std.clocks;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.QTPointerRef;


public class TimeRecord extends QTPointerRef implements QuickTimeLib {
    private static Object linkage;

    private static final int kNativeSize = 16;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(TimeRecord.class);
                    }
                });
            }
        }.establish();
    }

    public TimeRecord() throws QTException {
        super(kNativeSize, true);
    }

    public TimeRecord(int paramInt, long paramLong) throws QTException {
        super(16, true);
        setValue(paramLong);
        setScale(paramInt);
    }

    protected TimeRecord(int paramInt, Object paramObject) {
        super(paramInt, 16, paramObject);
    }

    public final long getValue() {
        return getLongAt(0);
    }

    public final int getScale() {
        return getIntAt(8);
    }

    public final void setValue(long paramLong) {
        setLongAt(0, paramLong);
    }

    public final void setScale(int paramInt) {
        setIntAt(8, paramInt);
    }

    public final void convertTime(TimeBase paramTimeBase) {
        ConvertTime(_ID(), QTObject.ID(paramTimeBase));
    }

    public final void convertTimeScale(int paramInt) {
        ConvertTimeScale(_ID(), paramInt);
    }

    public final void addTime(TimeRecord paramTimeRecord) {
        AddTime(_ID(), QTObject.ID(paramTimeRecord));
    }

    public final void subtractTime(TimeRecord paramTimeRecord) {
        SubtractTime(_ID(), QTObject.ID(paramTimeRecord));
    }

    public final TimeBase getTimeBase() {
        return TimeBase.fromTimeRecord(this);
    }

    public String toString() {
        return super.toString() + "[value=" + getValue() + ",scale=" + getScale() + "]";
    }

    private static native void ConvertTime(int paramInt1, int paramInt2);

    private static native void ConvertTimeScale(int paramInt1, int paramInt2);

    private static native void AddTime(int paramInt1, int paramInt2);

    private static native void SubtractTime(int paramInt1, int paramInt2);
}
