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

public final class QTVRNodeHeader extends QTVRAtom {

    private static final int kNativeSize = 28;

    @Serial
    private static final long serialVersionUID = 5440311233545515486L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRNodeHeader object containing no data.
     */
    public QTVRNodeHeader() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRNodeHeader object from a byte array
     * Used in cloning
     */
    public QTVRNodeHeader(byte[] bytes) throws QTException {
        super(bytes, kNativeSize);
    }

    @Override
    protected int getNativeSize() {
        return kNativeSize;
    }

    @Override
    protected void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRNodeHeader qtvrNodeHeader = new QTVRNodeHeader();
        in.read(qtvrNodeHeader.getBytes());
        System.arraycopy(qtvrNodeHeader.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Override
    protected void writeObject(ObjectOutputStream out) throws IOException {
        QTVRNodeHeader qtvrNodeHeader = (QTVRNodeHeader) clone();
        out.write(qtvrNodeHeader.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        try {
            return new QTVRNodeHeader(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    private static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 2, 2);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(4, 4, 6);
        ed.addFlipSpec(endianFlipSpec2);
        return ed;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
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
     * returns the nodeID
     */
    public int getNodeID() {
        return getIntAt(8);
    }

    /**
     * set the nodeID
     */
    public void setNodeID(int nodeID) {
        setIntAt(8, nodeID);
    }

    /**
     * returns the nameAtomID
     */
    public int getNameAtomID() {
        return getIntAt(12);
    }

    /**
     * set the nameAtomID
     */
    public void setNameAtomID(int nameAtomID) {
        setIntAt(12, nameAtomID);
    }

    /**
     * returns the commentAtomID
     */
    public int getCommentAtomID() {
        return getIntAt(16);
    }

    /**
     * set the commentAtomID
     */
    public void setCommentAtomID(int commentAtomID) {
        setIntAt(16, commentAtomID);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int reserved1) {
        setIntAt(20, reserved1);
    }

    /**
     * returns the reserved1
     */
    public int getReserved1() {
        return getIntAt(20);
    }

    /**
     * set the reserved2
     */
    public void setReserved2(int reserved2) {
        setIntAt(24, reserved2);
    }

    /**
     * returns the reserved2
     */
    public int getReserved2() {
        return getIntAt(24);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[nodeType=" + QTUtils.fromOSType(getNodeType()) + ",nodeID=" + getNodeID() + ",nameAtomID=" + getNameAtomID() + ",commentAtomID" + getCommentAtomID() + "]";
    }
}
