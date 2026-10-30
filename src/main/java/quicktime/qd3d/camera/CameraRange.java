package quicktime.qd3d.camera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;

/**
 * CameraRange contains information for the camera's range.
 * Includes the distance to the nearest projection plane, hither plane,
 * and the distance to the farthest projection plane, yon plane.
 *
 * Refer to TQ3CameraRange in QuickDraw3d
 */
public final class CameraRange extends QTByteObject implements Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 8;

    @Serial
    private static final long serialVersionUID = -4968315887114751172L;

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
     * Creates a CameraRange object containing no data.
     */
    public CameraRange() {
        super(8);
    }

    /**
     * Creates a CameraRange object containing given data.
     */
    public CameraRange(float hither, float yon) {
        this();
        setHither(hither);
        setYon(yon);
    }

    private CameraRange(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[8];
        CameraRange cameraRange = new CameraRange();
        in.read(cameraRange.getBytes());
        try {
            EndianOrder.flipBigEndianToNative(cameraRange, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        System.arraycopy(cameraRange.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        CameraRange cameraRange = (CameraRange) clone();
        try {
            EndianOrder.flipNativeToBigEndian(cameraRange, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        out.write(cameraRange.getBytes());
    }

    /**
     * returns the camera range's distance away from the hither plane.
     */
    public float getHither() {
        return getFloatAt(0);
    }

    /**
     * returns the camera range's distance away from the yon plane.
     */
    public float getYon() {
        return getFloatAt(4);
    }

    /**
     * sets the camera range's distance away from the hither plane.
     */
    public void setHither(float hither) {
        setFloatAt(0, hither);
    }

    /**
     * sets the camera range's distance away from the yon plane.
     */
    public void setYon(float yon) {
        setFloatAt(4, yon);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new CameraRange(getBytes());
    }
}
