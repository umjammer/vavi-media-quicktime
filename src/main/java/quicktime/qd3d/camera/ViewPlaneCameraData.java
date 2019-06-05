
package quicktime.qd3d.camera;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;


public final class ViewPlaneCameraData extends CameraData implements PrimitivesLib {
    private static Object linkage;

    public static final int kNativeSize = 80;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }

        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ViewPlaneCameraData.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public ViewPlaneCameraData() {
        super(80);
    }

    public ViewPlaneCameraData(CameraData paramCameraData,
            float paramFloat1,
            float paramFloat2,
            float paramFloat3,
            float paramFloat4,
            float paramFloat5) {
        this();

        setCameraBytes(paramCameraData);
        setViewPlane(paramFloat1);
        setHalfWidth(paramFloat2);
        setHalfHeight(paramFloat3);
        setCenterX(paramFloat4);
        setCenterY(paramFloat5);
    }

    private ViewPlaneCameraData(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    public float getViewPlane() {
        return getFloatAt(60);
    }

    public float getHalfWidth() {
        return getFloatAt(64);
    }

    public float getHalfHeight() {
        return getFloatAt(68);
    }

    public float getCenterX() {
        return getFloatAt(72);
    }

    public float getCenterY() {
        return getFloatAt(76);
    }

    public void setViewPlane(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    public void setHalfWidth(float paramFloat) {
        setFloatAt(64, paramFloat);
    }

    public void setHalfHeight(float paramFloat) {
        setFloatAt(68, paramFloat);
    }

    public void setCenterX(float paramFloat) {
        setFloatAt(72, paramFloat);
    }

    public void setCenterY(float paramFloat) {
        setFloatAt(76, paramFloat);
    }

    public Object clone() {
        return new ViewPlaneCameraData(this.bytes);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/
 * camera/ViewPlaneCameraData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
