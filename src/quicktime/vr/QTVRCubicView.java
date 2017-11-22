
package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.QTByteObject;


public final class QTVRCubicView extends QTByteObject implements Cloneable {
    private static final int kNativeSize = 36;

    private static EndianDescriptor ed;

    static final long serialVersionUID = -3293127487969383255L;

    public QTVRCubicView() {
        super(kNativeSize);
    }

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

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRCubicView localQTVRCubicView = new QTVRCubicView();
        paramObjectInputStream.read(localQTVRCubicView.getBytes());
        System.arraycopy(localQTVRCubicView.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRCubicView localQTVRCubicView = (QTVRCubicView) clone();
        paramObjectOutputStream.write(localQTVRCubicView.getBytes());
    }

    protected Object clone() {
        try {
            return new QTVRCubicView(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    public float getMinPan() {
        return getFloatAt(0);
    }

    public void setMinPan(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    public float getMaxPan() {
        return getFloatAt(4);
    }

    public void setMaxPan(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    public float getMinTilt() {
        return getFloatAt(8);
    }

    public void setMinTilt(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    public float getMaxTilt() {
        return getFloatAt(12);
    }

    public void setMaxTilt(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    public float getMinFieldOfView() {
        return getFloatAt(16);
    }

    public void setMinFieldOfView(float paramFloat) {
        setFloatAt(16, paramFloat);
    }

    public float getMaxFieldOfView() {
        return getFloatAt(20);
    }

    public void setMaxFieldOfView(float paramFloat) {
        setFloatAt(20, paramFloat);
    }

    public float getDefaultPan() {
        return getFloatAt(24);
    }

    public void setDefaultPan(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    public float getDefaultTilt() {
        return getFloatAt(28);
    }

    public void setDefaultTilt(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    public float getDefaultFieldOfView() {
        return getFloatAt(32);
    }

    public void setDefaultFieldOfView(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    public String toString() {
        return getClass().getName() + "[minPan=" + getMinPan() + ",maxPan=" + getMaxPan() + ",minTilt=" + getMinTilt()
               + ",maxTilt" + getMaxTilt() + ",minFieldOfView" + getMinFieldOfView() + ",maxFieldOfView" + getMaxFieldOfView()
               + ",defaultPan" + getDefaultPan() + ",defaultTilt" + getDefaultTilt() + ",defaultFieldOfView"
               + getDefaultFieldOfView() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRCubicView.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
