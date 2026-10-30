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
    public QTVRCubicView(byte[] bytes) throws QTException {
        super(bytes);
        if (bytes.length != kNativeSize) {
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
        this.bytes = new byte[kNativeSize];
        QTVRCubicView qtvrCubicView = new QTVRCubicView();
        in.read(qtvrCubicView.getBytes());
        System.arraycopy(qtvrCubicView.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        QTVRCubicView qtvrCubicView = (QTVRCubicView) clone();
        out.write(qtvrCubicView.getBytes());
    }

    @Override
    protected Object clone() {
        try {
            return new QTVRCubicView(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
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
    public void setMinPan(float minPan) {
        setFloatAt(0, minPan);
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
    public void setMaxPan(float maxPan) {
        setFloatAt(4, maxPan);
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
    public void setMinTilt(float minTilt) {
        setFloatAt(8, minTilt);
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
    public void setMaxTilt(float maxTilt) {
        setFloatAt(12, maxTilt);
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
    public void setMinFieldOfView(float minFieldOfView) {
        setFloatAt(16, minFieldOfView);
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
    public void setMaxFieldOfView(float maxFieldOfView) {
        setFloatAt(20, maxFieldOfView);
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
    public void setDefaultPan(float defaultPan) {
        setFloatAt(24, defaultPan);
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
    public void setDefaultTilt(float defaultTilt) {
        setFloatAt(28, defaultTilt);
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
    public void setDefaultFieldOfView(float defaultFieldOfView) {
        setFloatAt(32, defaultFieldOfView);
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[minPan=" + getMinPan() + ",maxPan=" + getMaxPan() + ",minTilt=" + getMinTilt() + ",maxTilt" + getMaxTilt() + ",minFieldOfView" + getMinFieldOfView() + ",maxFieldOfView" + getMaxFieldOfView() + ",defaultPan" + getDefaultPan() + ",defaultTilt" + getDefaultTilt() + ",defaultFieldOfView" + getDefaultFieldOfView() + "]";
    }
}
