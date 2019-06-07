package quicktime.qd3d.camera;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;

/**
 * A perspective camera specified in terms of an arbitrary view plane.
 *
 * This is most useful when setting the camera to look at a particular
 * object.  The mViewPlane is set to distance from the camera to the object.
 * The halfWidth is set to half the width of the cross section of the object,
 * and the halfHeight equal to the halfWidth divided by the aspect ratio
 * of the viewPort.
 *
 * This is the only perspective camera with specifications for off-axis
 * viewing, which is desirable for scrolling.
 *
 * refer to TQ3ViewPlaneCameraData in QuickDraw3d
 */
public final class ViewPlaneCameraData extends CameraData implements PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
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

    /**
     * Creates a ViewPlaneCameraData object containing no data.
     */
    public ViewPlaneCameraData() {
        super(80);
    }

    /**
     * Sets the ViewPlaneCameraData object's data.
     * @param cameraData The camera data.
     * @param viewPlane -
     * @param halfWidthAtViewPlane -
     * @param halfHeightAtViewPlane -
     * @param centerXOnViewPlane -
     * @param centerYOnViewPlane -
     */
    public ViewPlaneCameraData(CameraData paramCameraData, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
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

    /**
     * returns the viewPlane
     */
    public float getViewPlane() {
        return getFloatAt(60);
    }

    /**
     * returns the  halfWidthAtViewPlane
     */
    public float getHalfWidth() {
        return getFloatAt(64);
    }

    /**
     * returns the  halfHeightAtViewPlane
     */
    public float getHalfHeight() {
        return getFloatAt(68);
    }

    /**
     * returns the  centerXOnViewPlane
     */
    public float getCenterX() {
        return getFloatAt(72);
    }

    /**
     * returns the centerYOnViewPlane
     */
    public float getCenterY() {
        return getFloatAt(76);
    }

    /**
     * sets the viewPlane
     */
    public void setViewPlane(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    /**
     * sets the  halfWidthAtViewPlane
     */
    public void setHalfWidth(float paramFloat) {
        setFloatAt(64, paramFloat);
    }

    /**
     * sets the halfHeightAtViewPlane
     */
    public void setHalfHeight(float paramFloat) {
        setFloatAt(68, paramFloat);
    }

    /**
     * sets the centerXOnViewPlane
     */
    public void setCenterX(float paramFloat) {
        setFloatAt(72, paramFloat);
    }

    /**
     * sets the centerYOnViewPlane
     */
    public void setCenterY(float paramFloat) {
        setFloatAt(76, paramFloat);
    }

    /**
     * Returns a copy of this class.
     */
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
