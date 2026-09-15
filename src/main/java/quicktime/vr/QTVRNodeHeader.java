package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;

public final class QTVRNodeHeader extends QTVRAtom {

    private static final int kNativeSize = 28;

    static final long serialVersionUID = 5440311233545515486L;

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
    public QTVRNodeHeader(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    protected int getNativeSize() {
        return kNativeSize;
    }

    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRNodeHeader localQTVRNodeHeader = new QTVRNodeHeader();
        paramObjectInputStream.read(localQTVRNodeHeader.getBytes());
        System.arraycopy(localQTVRNodeHeader.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRNodeHeader localQTVRNodeHeader = (QTVRNodeHeader) clone();
        paramObjectOutputStream.write(localQTVRNodeHeader.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        try {
            return new QTVRNodeHeader(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);
        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 4, 6);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
        return localEndianDescriptor;
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
    public void setNodeType(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * set the nodeType from a string
     */
    public void setNodeType(String paramString) {
        setIntAt(4, QTUtils.toOSType(paramString));
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
    public void setNodeID(int paramInt) {
        setIntAt(8, paramInt);
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
    public void setNameAtomID(int paramInt) {
        setIntAt(12, paramInt);
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
    public void setCommentAtomID(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int paramInt) {
        setIntAt(20, paramInt);
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
    public void setReserved2(int paramInt) {
        setIntAt(24, paramInt);
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

