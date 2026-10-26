package quicktime.std.clocks;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ClockManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.QTPointerRef;

/**
 * The TimeRecord allows a full description of a QuickTime time specification structure.
 * Contains a time value, which is scaled to the time base coordinate system.
 * refer to TimeRecord in QuickTime
 */
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

    /**
     * This record is an empty record that will need to be filled out by passing
     * it into calls. It has no value for the TimeBase field.
     */
    public TimeRecord() throws QTException {
        super(kNativeSize, true);
    }

    /**
     * Create a TimeRecord with the supplied values for scale and time value.
     * @param scale the scale that is used to interpret the meaning of the supplied value
     * @param value the value as expressed in scale untis.
     */
    public TimeRecord(int paramInt, long paramLong) throws QTException {
        super(16, true);
        setValue(paramLong);
        setScale(paramInt);
    }

    protected TimeRecord(int paramInt, Object paramObject) {
        super(paramInt, 16, paramObject);
    }

    /**
     * Determines the time value of the time record.
     * @return The value of the time value.
     */
    public final long getValue() {
        return getLongAt(0);
    }

    /**
     * Determines the time scale of the time record.
     * @return The value of the time scale.
     */
    public final int getScale() {
        return getIntAt(8);
    }

    /**
     * Sets the time value of the time record.
     * @param value the data to store in the field.
     */
    public final void setValue(long paramLong) {
        setLongAt(0, paramLong);
    }

    /**
     * Sets the time scale of the time record.
     * @param scale the data to store in the field.
     */
    public final void setScale(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * Convert a time from one time base into a time that is relative to another time base.
     * QuickTime::ConvertTime
     * @param newBase Specifies the time base for this operation
     */
    public final void convertTime(TimeBase paramTimeBase) {
        ConvertTime(_ID(), QTObject.ID(paramTimeBase));
    }

    /**
     * Convert a time from one time scale into a time that is relative to another time base.
     * QuickTime::ConvertTimeScale
     * @param newScale The time scale
     */
    public final void convertTimeScale(int paramInt) {
        ConvertTimeScale(_ID(), paramInt);
    }

    /**
     * This adds two times specified in time structures.
     * QuickTime::AddTime
     * @param source The source operand time base with a time value to add
     */
    public final void addTime(TimeRecord paramTimeRecord) {
        AddTime(_ID(), QTObject.ID(paramTimeRecord));
    }

    /**
     * This subtracts two times specified in time structures.
     * QuickTime::SubtractTime
     * @param source The source operand time base with a time value to subtract
     */
    public final void subtractTime(TimeRecord paramTimeRecord) {
        SubtractTime(_ID(), QTObject.ID(paramTimeRecord));
    }

    /**
     * Return the TimeBase that is associated with this record. If there is
     * no TimeBase then returns null.
     */
    public final TimeBase getTimeBase() {
        return TimeBase.fromTimeRecord(this);
    }

    /**
     * Print information about this TimeRecord object.
     * @return a string representing this TimeRecord object
     */
    public String toString() {
        return super.toString() + "[value=" + getValue() + ",scale=" + getScale() + "]";
    }

    private static void ConvertTime(int paramInt1, int paramInt2) {
        ClockManager.ConvertTime(paramInt1, paramInt2);
    }

    private static void ConvertTimeScale(int paramInt1, int paramInt2) {
        ClockManager.ConvertTimeScale(paramInt1, paramInt2);
    }

    private static void AddTime(int paramInt1, int paramInt2) {
        ClockManager.AddTime(paramInt1, paramInt2);
    }

    private static void SubtractTime(int paramInt1, int paramInt2) {
        ClockManager.SubtractTime(paramInt1, paramInt2);
    }
}
