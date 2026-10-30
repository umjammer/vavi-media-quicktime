package quicktime.qd3d.camera;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;


/**
 * OrthographicCameraData contains information for the camera angle and position.
 * <p>
 * The lens characteristics are set with the dimensions of a
 * rectangular viewPort in the frame of the camera.
 * <p>
 * Refer to TQ3OrthographicCameraData in QuickDraw3d
 */
public final class OrthographicCameraData extends CameraData implements PrimitivesLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 76;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    /**
     * Creates a OrthographicCameraData object containing no data.
     */
    public OrthographicCameraData() {
        super(kNativeSize);
    }

    /**
     * Creates a OrthographicCameraData object containing given data.
     *
     * @param cameraData The camera data.
     * @param left       The left position.
     * @param top        The top position.
     * @param right      The right position.
     * @param bottom     The bottom position.
     */
    public OrthographicCameraData(CameraData cameraData, float left, float top, float right, float bottom) {
        this();
        setCameraBytes(cameraData);
        setLeft(left);
        setTop(top);
        setRight(right);
        setBottom(bottom);
    }

    private OrthographicCameraData(byte[] bytes) {
        super(bytes);
    }

    /**
     * returns the left position
     */
    public float getLeft() {
        return getFloatAt(60);
    }

    /**
     * returns the top position
     */
    public float getTop() {
        return getFloatAt(64);
    }

    /**
     * returns the right position
     */
    public float getRight() {
        return getFloatAt(68);
    }

    /**
     * returns the bottom position
     */
    public float getBottom() {
        return getFloatAt(72);
    }

    /**
     * sets the left position
     */
    public void setLeft(float left) {
        setFloatAt(60, left);
    }

    /**
     * sets the top position
     */
    public void setTop(float top) {
        setFloatAt(64, top);
    }

    /**
     * sets the right position
     */
    public void setRight(float right) {
        setFloatAt(68, right);
    }

    /**
     * sets the bottom position
     */
    public void setBottom(float bottom) {
        setFloatAt(72, bottom);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new OrthographicCameraData(this.bytes);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
