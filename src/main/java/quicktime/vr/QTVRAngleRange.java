package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.QTByteObject;

public final class QTVRAngleRange extends QTByteObject implements Cloneable {

    private static final int kNativeSize = 8;

    private static EndianDescriptor ed;

    static final long serialVersionUID = 2448417950352800908L;

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
    public QTVRAngleRange(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
        if (paramArrayOfByte.length != 8) {
            throw new QTException(-50);
        }
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = new EndianDescriptor(16);
        return ed;
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[8];
        QTVRAngleRange localQTVRAngleRange = new QTVRAngleRange();
        paramObjectInputStream.read(localQTVRAngleRange.getBytes());
        System.arraycopy(localQTVRAngleRange.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRAngleRange localQTVRAngleRange = (QTVRAngleRange) clone();
        paramObjectOutputStream.write(localQTVRAngleRange.getBytes());
    }

    protected Object clone() {
        try {
            return new QTVRAngleRange(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
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
    public void setMinimumAngle(float paramFloat) {
        setFloatAt(0, paramFloat);
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
    public void setMaximumAngle(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[minimumAngle=" + getMinimumAngle() + ",maximumAngle=" + getMaximumAngle() + "]";
    }
}

