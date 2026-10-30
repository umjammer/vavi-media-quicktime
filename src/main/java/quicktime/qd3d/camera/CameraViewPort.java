package quicktime.qd3d.camera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.QDPoint;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;

/**
 * CameraViewPort contains information for the camera's viewport.
 * Viewport specification.  Origin is (-1, 1), and corresponds to the
 * upper left-hand corner; width and height maximum is (2.0, 2.0),
 * corresponding to the lower left-hand corner of the window.  The
 * Viewport specifies a part of the viewPlane that gets displayed
 * on the window that is to be drawn.
 * Normally, it is set with an origin of (-1.0, 1.0), and a width and
 * height of both 2.0, specifying that the entire window is to be
 * drawn.  If, for example, an exposure event of the window exposed
 * the right half of the window, you would set the origin to (0, 1),
 * and the width and height to (1.0) and (2.0), respectively.
 *
 * Refer to TQ3CameraPlacement in QuickDraw3d
 */
public final class CameraViewPort extends QTByteObject implements Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 16;

    @Serial
    private static final long serialVersionUID = -480864256741555003L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates a CameraViewPort object containing no data.
     */
    public CameraViewPort() {
        super(16);
    }

    /**
     * Creates a CameraViewPort object containing given data.
     * @param origin The origin.
     * @param width The width.
     * @param height The height.
     */
    public CameraViewPort(QDPoint origin, float width, float height) {
        this();
        setOrigin(origin);
        setWidth(width);
        setHeight(height);
    }

    private CameraViewPort(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[16];
        CameraViewPort cameraViewPort = new CameraViewPort();
        in.read(cameraViewPort.getBytes());
        try {
            EndianOrder.flipBigEndianToNative(cameraViewPort, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        System.arraycopy(cameraViewPort.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        CameraViewPort cameraViewPort = (CameraViewPort) clone();
        try {
            EndianOrder.flipNativeToBigEndian(cameraViewPort, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        out.write(cameraViewPort.getBytes());
    }

    /**
     * returns the origin of camera viewport
     */
    public QDPoint getOrigin() {
        return QDPoint.fromArray(getBytes(), -8);
    }

    /**
     * returns the camera viewport's width
     */
    public float getWidth() {
        return getFloatAt(8);
    }

    /**
     * returns the camera viewport's height
     */
    public float getHeight() {
        return getFloatAt(12);
    }

    /**
     * sets the camera viewport's origin
     */
    public void setOrigin(QDPoint origin) {
        setFloatAt(0, origin.getXF());
        setFloatAt(4, origin.getYF());
    }

    /**
     * sets the camera viewport's width
     */
    public void setWidth(float width) {
        setFloatAt(8, width);
    }

    /**
     * sets the camera viewport's height
     */
    public void setHeight(float height) {
        setFloatAt(12, height);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new CameraViewPort(this.bytes);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName();
    }
}
