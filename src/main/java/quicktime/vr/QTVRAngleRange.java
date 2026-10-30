package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.QTByteObject;

public final class QTVRAngleRange extends QTByteObject implements Cloneable {

    private static final int kNativeSize = 8;

    private static EndianDescriptor ed;

    @Serial
    private static final long serialVersionUID = 2448417950352800908L;

    /**
     * Creates a QTVRAngleRange object containing no data
     */
    public QTVRAngleRange() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRAngleRange object from a byte array
     * Used in cloning
     */
    public QTVRAngleRange(byte[] bytes) throws QTException {
        super(bytes);
        if (bytes.length != 8) {
            throw new QTException(-50);
        }
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = new EndianDescriptor(16);
        return ed;
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[8];
        QTVRAngleRange qtvrAngleRange = new QTVRAngleRange();
        in.read(qtvrAngleRange.getBytes());
        System.arraycopy(qtvrAngleRange.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        QTVRAngleRange qtvrAngleRange = (QTVRAngleRange) clone();
        out.write(qtvrAngleRange.getBytes());
    }

    @Override
    protected Object clone() {
        try {
            return new QTVRAngleRange(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    /**
     * returns the minimumAngle
     */
    public float getMinimumAngle() {
        return getFloatAt(0);
    }

    /**
     * set the minimumAngle
     */
    public void setMinimumAngle(float minimumAngle) {
        setFloatAt(0, minimumAngle);
    }

    /**
     * returns the maximumAngle
     */
    public float getMaximumAngle() {
        return getFloatAt(4);
    }

    /**
     * set the maximumAngle
     */
    public void setMaximumAngle(float maximumAngle) {
        setFloatAt(4, maximumAngle);
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[minimumAngle=" + getMinimumAngle() + ",maximumAngle=" + getMaximumAngle() + "]";
    }
}
