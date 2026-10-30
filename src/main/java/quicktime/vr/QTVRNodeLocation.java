package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;

public final class QTVRNodeLocation extends QTVRAtom {

    private static final int kNativeSize = 24;

    private static EndianDescriptor ed;

    @Serial
    private static final long serialVersionUID = -2055560448381571608L;

    /**
     * Creates a QTVRNodeLocation object containing no data
     */
    public QTVRNodeLocation() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRNodeLocation object from a byte array
     */
    public QTVRNodeLocation(byte[] bytes) throws QTException {
        super(bytes, kNativeSize);
    }

    @Override
    protected int getNativeSize() {
        return kNativeSize;
    }

    private static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 2, 2);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(4, 4, 5);
        ed.addFlipSpec(endianFlipSpec2);
        return ed;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    @Override
    protected void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRNodeLocation qtvrNodeLocation = new QTVRNodeLocation();
        in.read(qtvrNodeLocation.getBytes());
        System.arraycopy(qtvrNodeLocation.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Override
    protected void writeObject(ObjectOutputStream out) throws IOException {
        QTVRNodeLocation qtvrNodeLocation = (QTVRNodeLocation) clone();
        out.write(qtvrNodeLocation.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        try {
            return new QTVRNodeLocation(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    /**
     * returns the nodeType
     */
    public int getNodeType() {
        return getIntAt(4);
    }

    /**
     * set the nodeType
     */
    public void setNodeType(int nodeType) {
        setIntAt(4, nodeType);
    }

    /**
     * set the nodeType from a string
     */
    public void setNodeType(String nodeType) {
        setIntAt(4, QTUtils.toOSType(nodeType));
    }

    /**
     * returns the locationFlags
     */
    public int getLocationFlags() {
        return getIntAt(8);
    }

    /**
     * set the locationFlags
     */
    public void setLocationFlags(int locationFlags) {
        setIntAt(8, locationFlags);
    }

    /**
     * returns the locationData
     */
    public int getLocationData() {
        return getIntAt(12);
    }

    /**
     * set the locationData
     */
    public void setLocationData(int locationData) {
        setIntAt(12, locationData);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int reserved1) {
        setIntAt(16, reserved1);
    }

    /**
     * returns the reserved1
     */
    public int getReserved1() {
        return getIntAt(16);
    }

    /**
     * set the reserved2
     */
    public void setReserved2(int reserved2) {
        setIntAt(20, reserved2);
    }

    /**
     * returns the reserved2
     */
    public int getReserved2() {
        return getIntAt(20);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[nodeType=" + QTUtils.fromOSType(getNodeType()) + ",locationFlags=" + getLocationFlags() + ",locationData=" + getLocationData() + "]";
    }
}
