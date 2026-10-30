package quicktime.qd3d.camera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.qd3d.math.Point3D;
import quicktime.qd3d.math.Vector3D;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


/**
 * CameraPlacement contains information for the camera's range.
 * Includes the location point of the camera, the camera' point
 * of interest, and the camera's up vector positioning.
 *
 * refer to TQ3CameraPlacement in QuickDraw3D
 */
public final class CameraPlacement extends QTByteObject implements PrimitivesLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 36;

    @Serial
    private static final long serialVersionUID = -2481353228250322010L;

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates a CameraRange object containing no data.
     */
    public CameraPlacement() {
        super(36);
    }

    /**
     * Creates a CameraRange object containing given data.
     * @param cameraLocation The camera placement's camera location.
     * @param pointOfInterest The camera placement's point of interest.
     * @param upVector The camera placement's upVector.
     */
    public CameraPlacement(Point3D cameraLocation, Point3D pointOfInterest, Vector3D upVector) {
        this();
        setCameraLocation(cameraLocation);
        setPointOfInterest(pointOfInterest);
        setUpVector(upVector);
    }

    private CameraPlacement(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[36];
        byte[] arrayOfByte = new byte[36];
        in.read(arrayOfByte);
        for (int i = 0; i < 36; i += 4) {
            int j = getIntFromArray(arrayOfByte, i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(getBytes(), i, k);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = getBytes();
        for (int i = 0; i < 36; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(movie, i, k);
        }
        out.write(movie);
    }

    /**
     * returns the camera placement's cameraLocation
     */
    public Point3D getCameraLocation() {
        Point3D cameraLocation = new Point3D();
        System.arraycopy(getBytes(), 0, cameraLocation.getBytes(), 0, cameraLocation.getBytes().length);
        return cameraLocation;
    }

    /**
     * returns the camera placement's pointOfInterest
     */
    public Point3D getPointOfInterest() {
        Point3D point3D = new Point3D();
        System.arraycopy(getBytes(), 12, point3D.getBytes(), 0, point3D.getBytes().length);
        return point3D;
    }

    /**
     * returns the camera placement's upVector
     */
    public Vector3D getUpVector() {
        return new Vector3D(getFloatAt(24), getFloatAt(28), getFloatAt(32));
    }

    /**
     * sets the camera placement's cameraLocation
     */
    public void setCameraLocation(Point3D cameraLocation) {
        setFloatAt(0, cameraLocation.getX());
        setFloatAt(4, cameraLocation.getY());
        setFloatAt(8, cameraLocation.getZ());
    }

    /**
     * sets the camera placement's pointOfInterest
     */
    public void setPointOfInterest(Point3D pointOfInterest) {
        setFloatAt(12, pointOfInterest.getX());
        setFloatAt(16, pointOfInterest.getY());
        setFloatAt(20, pointOfInterest.getZ());
    }

    /**
     * sets the camera placement's upVector
     */
    public void setUpVector(Vector3D upVector) {
        setFloatAt(24, upVector.getX());
        setFloatAt(28, upVector.getY());
        setFloatAt(32, upVector.getZ());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new CameraPlacement(this.bytes);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return super.toString() + "[location=" + getCameraLocation() + ",interest=" + getPointOfInterest() + ",upVector=" + getUpVector() + "]";
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
