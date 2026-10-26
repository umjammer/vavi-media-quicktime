package quicktime.qd3d.camera;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;

/**
 * OrthographicCameraData contains information for the camera angle and position.
 *
 * The lens characteristics are set with the dimensions of a
 * rectangular viewPort in the frame of the camera.
 *
 * Refer to TQ3OrthographicCameraData in QuickDraw3d
 */
public final class OrthographicCameraData extends CameraData implements PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 76;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(OrthographicCameraData.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * Creates a OrthographicCameraData object containing no data.
     */
    public OrthographicCameraData() {
        super(76);
    }

    /**
     * Creates a OrthographicCameraData object containing given data.
     * @param cameraData The camera data.
     * @param left The left position.
     * @param top The top position.
     * @param right The right position.
     * @param bottom The bottom position.
     */
    public OrthographicCameraData(CameraData paramCameraData, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this();
        setCameraBytes(paramCameraData);
        setLeft(paramFloat1);
        setTop(paramFloat2);
        setRight(paramFloat3);
        setBottom(paramFloat4);
    }

    private OrthographicCameraData(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
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
    public void setLeft(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    /**
     * sets the top position
     */
    public void setTop(float paramFloat) {
        setFloatAt(64, paramFloat);
    }

    /**
     * sets the right position
     */
    public void setRight(float paramFloat) {
        setFloatAt(68, paramFloat);
    }

    /**
     * sets the bottom position
     */
    public void setBottom(float paramFloat) {
        setFloatAt(72, paramFloat);
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        return new OrthographicCameraData(this.bytes);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}

