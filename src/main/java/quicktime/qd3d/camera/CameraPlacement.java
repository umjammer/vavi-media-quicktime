package quicktime.qd3d.camera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 36;

    private static final long serialVersionUID = -2481353228250322010L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(CameraPlacement.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static final EndianDescriptor getEndianDescriptor() {
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
    public CameraPlacement(Point3D paramPoint3D1, Point3D paramPoint3D2, Vector3D paramVector3D) {
        this();
        setCameraLocation(paramPoint3D1);
        setPointOfInterest(paramPoint3D2);
        setUpVector(paramVector3D);
    }

    private CameraPlacement(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[36];
        byte[] arrayOfByte = new byte[36];
        paramObjectInputStream.read(arrayOfByte);
        for (int i = 0; i < 36; i += 4) {
            int j = getIntFromArray(arrayOfByte, i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(getBytes(), i, k);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = getBytes();
        for (int i = 0; i < 36; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(arrayOfByte, i, k);
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * returns the camera placement's cameraLocation
     */
    public Point3D getCameraLocation() {
        Point3D localPoint3D = new Point3D();
        System.arraycopy(getBytes(), 0, localPoint3D.getBytes(), 0, localPoint3D.getBytes().length);
        return localPoint3D;
    }

    /**
     * returns the camera placement's pointOfInterest
     */
    public Point3D getPointOfInterest() {
        Point3D localPoint3D = new Point3D();
        System.arraycopy(getBytes(), 12, localPoint3D.getBytes(), 0, localPoint3D.getBytes().length);
        return localPoint3D;
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
    public void setCameraLocation(Point3D paramPoint3D) {
        setFloatAt(0, paramPoint3D.getX());
        setFloatAt(4, paramPoint3D.getY());
        setFloatAt(8, paramPoint3D.getZ());
    }

    /**
     * sets the camera placement's pointOfInterest
     */
    public void setPointOfInterest(Point3D paramPoint3D) {
        setFloatAt(12, paramPoint3D.getX());
        setFloatAt(16, paramPoint3D.getY());
        setFloatAt(20, paramPoint3D.getZ());
    }

    /**
     * sets the camera placement's upVector
     */
    public void setUpVector(Vector3D paramVector3D) {
        setFloatAt(24, paramVector3D.getX());
        setFloatAt(28, paramVector3D.getY());
        setFloatAt(32, paramVector3D.getZ());
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        return new CameraPlacement(this.bytes);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return super.toString() + "[location=" + getCameraLocation() + ",interest=" + getPointOfInterest() + ",upVector=" + getUpVector() + "]";
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}

