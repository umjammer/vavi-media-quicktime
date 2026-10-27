package quicktime.qd3d.camera;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;


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

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 80;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
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
    public ViewPlaneCameraData(CameraData cameraData, float viewPlane, float halfWidthAtViewPlane, float halfHeightAtViewPlane, float centerXOnViewPlane, float centerYOnViewPlane) {
        this();
        setCameraBytes(cameraData);
        setViewPlane(viewPlane);
        setHalfWidth(halfWidthAtViewPlane);
        setHalfHeight(halfHeightAtViewPlane);
        setCenterX(centerXOnViewPlane);
        setCenterY(centerYOnViewPlane);
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
    @Override
    public Object clone() {
        return new ViewPlaneCameraData(this.bytes);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}

