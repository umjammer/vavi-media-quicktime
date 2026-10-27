package quicktime.qd3d.camera;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;

/**
 * A view angle aspect camera is a perspective camera specified in
 * terms of the minimum view angle and the aspect ratio of X to Y.
 *
 * refer to TQ3ViewAngleAspectCameraData in QuickDraw3d
 */
public final class ViewAngleAspectCameraData extends CameraData implements PrimitivesLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 68;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    /**
     * Creates a ViewAngleAspectCameraData object containing no data.
     */
    public ViewAngleAspectCameraData() {
        super(68);
    }

    /**
     * Creates a ViewAngleAspectCameraData object containing given data.
     * @param fov The field of view
     * @param aspectRatio The aspect ratio of x to y
     */
    public ViewAngleAspectCameraData(CameraData paramCameraData, float paramFloat1, float paramFloat2) {
        this();
        setCameraBytes(paramCameraData);
        setFov(paramFloat1);
        setAspectRatio(paramFloat2);
    }

    private ViewAngleAspectCameraData(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    /**
     * returns the field of view
     */
    public float getFov() {
        return getFloatAt(60);
    }

    /**
     * returns the aspect ratio
     */
    public float setAspectRatio() {
        return getFloatAt(64);
    }

    /**
     * sets the field of view
     */
    public void setFov(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    /**
     * sets the aspect ratio
     */
    public void setAspectRatio(float paramFloat) {
        setFloatAt(64, paramFloat);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new ViewAngleAspectCameraData(this.bytes);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}

