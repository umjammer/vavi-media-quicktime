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
    public CameraRange(float paramFloat1, float paramFloat2) {
        this();
        setHither(paramFloat1);
        setYon(paramFloat2);
    }

    private CameraRange(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[8];
        CameraRange localCameraRange = new CameraRange();
        paramObjectInputStream.read(localCameraRange.getBytes());
        try {
            EndianOrder.flipBigEndianToNative(localCameraRange, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
        System.arraycopy(localCameraRange.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        CameraRange localCameraRange = (CameraRange) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localCameraRange, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
        paramObjectOutputStream.write(localCameraRange.getBytes());
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
    public void setHither(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    /**
     * sets the camera range's distance away from the yon plane.
     */
    public void setYon(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new CameraRange(getBytes());
    }
}

