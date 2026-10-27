package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.QTByteObject;

public final class QTVRCubicView extends QTByteObject implements Cloneable {

    private static final int kNativeSize = 36;

    private static EndianDescriptor ed;

    @Serial
    private static final long serialVersionUID = -3293127487969383255L;

    /**
     * Creates a QTVRCubicView object containing no data
     */
    public QTVRCubicView() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRCubicView object from a byte array
     * Used in cloning
     */
    public QTVRCubicView(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
        if (paramArrayOfByte.length != kNativeSize) {
            throw new QTException(-50);
        }
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = new EndianDescriptor(16);
        return ed;
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRCubicView localQTVRCubicView = new QTVRCubicView();
        paramObjectInputStream.read(localQTVRCubicView.getBytes());
        System.arraycopy(localQTVRCubicView.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRCubicView localQTVRCubicView = (QTVRCubicView) clone();
        paramObjectOutputStream.write(localQTVRCubicView.getBytes());
    }

    @Override
    protected Object clone() {
        try {
            return new QTVRCubicView(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    /**
     * returns the minPan
     */
    public float getMinPan() {
        return getFloatAt(0);
    }

    /**
     * set the minPan
     */
    public void setMinPan(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    /**
     * returns the maxPan
     */
    public float getMaxPan() {
        return getFloatAt(4);
    }

    /**
     * set the maxPan
     */
    public void setMaxPan(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * returns the minTilt
     */
    public float getMinTilt() {
        return getFloatAt(8);
    }

    /**
     * set the minTilt
     */
    public void setMinTilt(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    /**
     * returns the maxTilt
     */
    public float getMaxTilt() {
        return getFloatAt(12);
    }

    /**
     * set the maxTilt
     */
    public void setMaxTilt(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    /**
     * returns the minFieldOfView
     */
    public float getMinFieldOfView() {
        return getFloatAt(16);
    }

    /**
     * set the minFieldOfView
     */
    public void setMinFieldOfView(float paramFloat) {
        setFloatAt(16, paramFloat);
    }

    /**
     * returns the maxFieldOfView
     */
    public float getMaxFieldOfView() {
        return getFloatAt(20);
    }

    /**
     * set the maxFieldOfView
     */
    public void setMaxFieldOfView(float paramFloat) {
        setFloatAt(20, paramFloat);
    }

    /**
     * returns the defaultPan
     */
    public float getDefaultPan() {
        return getFloatAt(24);
    }

    /**
     * set the defaultPan
     */
    public void setDefaultPan(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    /**
     * returns the defaultTilt
     */
    public float getDefaultTilt() {
        return getFloatAt(28);
    }

    /**
     * set the defaultTilt
     */
    public void setDefaultTilt(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    /**
     * returns the defaultFieldOfView
     */
    public float getDefaultFieldOfView() {
        return getFloatAt(32);
    }

    /**
     * set the defaultFieldOfView
     */
    public void setDefaultFieldOfView(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[minPan=" + getMinPan() + ",maxPan=" + getMaxPan() + ",minTilt=" + getMinTilt() + ",maxTilt" + getMaxTilt() + ",minFieldOfView" + getMinFieldOfView() + ",maxFieldOfView" + getMaxFieldOfView() + ",defaultPan" + getDefaultPan() + ",defaultTilt" + getDefaultTilt() + ",defaultFieldOfView" + getDefaultFieldOfView() + "]";
    }
}

