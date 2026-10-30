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
    public ViewAngleAspectCameraData(CameraData cameraBytes, float fov, float aspectRatio) {
        this();
        setCameraBytes(cameraBytes);
        setFov(fov);
        setAspectRatio(aspectRatio);
    }

    private ViewAngleAspectCameraData(byte[] bytes) {
        super(bytes);
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
    public void setFov(float fov) {
        setFloatAt(60, fov);
    }

    /**
     * sets the aspect ratio
     */
    public void setAspectRatio(float aspectRatio) {
        setFloatAt(64, aspectRatio);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new ViewAngleAspectCameraData(this.bytes);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
