package quicktime.qd3d.camera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


/**
 * CameraData contains information for the camera angle and position.
 * Includes the camera's placement location, the camera's range, and
 * the camera's viewport plane.
 *
 * refer to TQ3CameraData in QuickDraw3D
 */
public class CameraData extends QTByteObject implements PrimitivesLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 60;

    @Serial
    private static final long serialVersionUID = 8266295306447856273L;

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates a CameraData object containing no data.
     */
    public CameraData() {
        this(60);
    }

    /**
     * Creates a CameraData object from the data.
     */
    public CameraData(CameraPlacement paramCameraPlacement, CameraRange paramCameraRange, CameraViewPort paramCameraViewPort) {
        this();
        setPlacement(paramCameraPlacement);
        setRange(paramCameraRange);
        setViewPort(paramCameraViewPort);
    }

    CameraData(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    CameraData(int paramInt) {
        super(paramInt);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        int i = paramObjectInputStream.readInt();
        this.bytes = new byte[i];
        paramObjectInputStream.read(this.bytes);
        for (int j = 0; j < 60; j += 4) {
            int k = getIntFromArray(this.bytes, j);
            int m = EndianOrder.flipBigEndianToNative32(k);
            setIntInArray(getBytes(), j, m);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = getBytes();
        int i = getSize();
        paramObjectOutputStream.writeInt(i);
        for (int j = 0; j < i; j += 4) {
            int k = getIntFromArray(getBytes(), j);
            int m = EndianOrder.flipNativeToBigEndian32(k);
            setIntInArray(arrayOfByte, j, m);
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * returns the camera placement
     */
    public final CameraPlacement getPlacement() {
        CameraPlacement localCameraPlacement = new CameraPlacement();
        System.arraycopy(getBytes(), 0, localCameraPlacement.getBytes(), 0, 36);
        return localCameraPlacement;
    }

    /**
     * sets the camera placement
     */
    public final void setPlacement(CameraPlacement paramCameraPlacement) {
        System.arraycopy(paramCameraPlacement.getBytes(), 0, getBytes(), 0, 36);
    }

    /**
     * returns the camera range
     */
    public final CameraRange getRange() {
        CameraRange localCameraRange = new CameraRange();
        System.arraycopy(getBytes(), 36, localCameraRange.getBytes(), 0, 8);
        return localCameraRange;
    }

    /**
     * sets the camera range
     */
    public final void setRange(CameraRange paramCameraRange) {
        System.arraycopy(paramCameraRange.getBytes(), 0, getBytes(), 36, 8);
    }

    /**
     * returns the camera range
     */
    public final CameraViewPort getViewPort() {
        CameraViewPort localCameraViewPort = new CameraViewPort();
        System.arraycopy(getBytes(), 44, localCameraViewPort.getBytes(), 0, 16);
        return localCameraViewPort;
    }

    /**
     * sets the camera range
     */
    public final void setViewPort(CameraViewPort paramCameraViewPort) {
        System.arraycopy(paramCameraViewPort.getBytes(), 0, getBytes(), 44, 16);
    }

    final void setCameraBytes(CameraData paramCameraData) {
        System.arraycopy(paramCameraData.getBytes(), 0, getBytes(), 0, 60);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new CameraData(this.bytes);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName();
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}
