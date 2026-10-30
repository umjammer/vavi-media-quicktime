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
    public CameraData(CameraPlacement placement, CameraRange range, CameraViewPort viewPort) {
        this();
        setPlacement(placement);
        setRange(range);
        setViewPort(viewPort);
    }

    CameraData(byte[] bytes) {
        super(bytes);
    }

    CameraData(int size) {
        super(size);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        int i = in.readInt();
        this.bytes = new byte[i];
        in.read(this.bytes);
        for (int j = 0; j < 60; j += 4) {
            int k = getIntFromArray(this.bytes, j);
            int m = EndianOrder.flipBigEndianToNative32(k);
            setIntInArray(getBytes(), j, m);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = getBytes();
        int i = getSize();
        out.writeInt(i);
        for (int j = 0; j < i; j += 4) {
            int k = getIntFromArray(getBytes(), j);
            int m = EndianOrder.flipNativeToBigEndian32(k);
            setIntInArray(movie, j, m);
        }
        out.write(movie);
    }

    /**
     * returns the camera placement
     */
    public final CameraPlacement getPlacement() {
        CameraPlacement placement = new CameraPlacement();
        System.arraycopy(getBytes(), 0, placement.getBytes(), 0, 36);
        return placement;
    }

    /**
     * sets the camera placement
     */
    public final void setPlacement(CameraPlacement placement) {
        System.arraycopy(placement.getBytes(), 0, getBytes(), 0, 36);
    }

    /**
     * returns the camera range
     */
    public final CameraRange getRange() {
        CameraRange range = new CameraRange();
        System.arraycopy(getBytes(), 36, range.getBytes(), 0, 8);
        return range;
    }

    /**
     * sets the camera range
     */
    public final void setRange(CameraRange range) {
        System.arraycopy(range.getBytes(), 0, getBytes(), 36, 8);
    }

    /**
     * returns the camera range
     */
    public final CameraViewPort getViewPort() {
        CameraViewPort viewPort = new CameraViewPort();
        System.arraycopy(getBytes(), 44, viewPort.getBytes(), 0, 16);
        return viewPort;
    }

    /**
     * sets the camera range
     */
    public final void setViewPort(CameraViewPort viewPort) {
        System.arraycopy(viewPort.getBytes(), 0, getBytes(), 44, 16);
    }

    final void setCameraBytes(CameraData cameraBytes) {
        System.arraycopy(cameraBytes.getBytes(), 0, getBytes(), 0, 60);
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

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
