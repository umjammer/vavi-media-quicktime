
package quicktime.qd3d.camera;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.PrimitivesLib;


public final class ViewAngleAspectCameraData extends CameraData implements PrimitivesLib {
    public static final int kNativeSize = 68;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    public ViewAngleAspectCameraData() {
        super(68);
    }

    public ViewAngleAspectCameraData(CameraData paramCameraData, float paramFloat1, float paramFloat2) {
        this();

        setCameraBytes(paramCameraData);
        setFov(paramFloat1);
        setAspectRatio(paramFloat2);
    }

    private ViewAngleAspectCameraData(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    public float getFov() {
        return getFloatAt(60);
    }

    public float setAspectRatio() {
        return getFloatAt(64);
    }

    public void setFov(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    public void setAspectRatio(float paramFloat) {
        setFloatAt(64, paramFloat);
    }

    public Object clone() {
        return new ViewAngleAspectCameraData(this.bytes);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/
 * camera/ViewAngleAspectCameraData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
