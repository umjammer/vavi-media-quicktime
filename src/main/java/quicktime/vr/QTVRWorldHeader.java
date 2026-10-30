package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

public final class QTVRWorldHeader extends QTVRAtom {

    private static final int kNativeSize = 24;

    @Serial
    private static final long serialVersionUID = -8701506970872651584L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRWorldHeader object containing no data
     */
    public QTVRWorldHeader() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRWorldHeader object from a byte array
     * Used in cloning
     */
    public QTVRWorldHeader(byte[] bytes) throws QTException {
        super(bytes, kNativeSize);
    }

    @Override
    protected int getNativeSize() {
        return kNativeSize;
    }

    @Override
    protected void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRWorldHeader qtvrWorldHeader = new QTVRWorldHeader();
        in.read(qtvrWorldHeader.getBytes());
        System.arraycopy(qtvrWorldHeader.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Override
    protected void writeObject(ObjectOutputStream out) throws IOException {
        QTVRWorldHeader qtvrWorldHeader = (QTVRWorldHeader) clone();
        out.write(qtvrWorldHeader.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        try {
            return new QTVRWorldHeader(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    private static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 2, 2);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(4, 4, 5);
        ed.addFlipSpec(endianFlipSpec2);
        return ed;
    }

    /**
     * returns the name AtomID
     */
    public int getNameAtomID() {
        return getIntAt(4);
    }

    /**
     * set the name AtomID
     */
    public void setNameAtomID(int nameAtomID) {
        setIntAt(4, nameAtomID);
    }

    /**
     * returns the default NodeID
     */
    public int getDefaultNodeID() {
        return getIntAt(8);
    }

    /**
     * set the default NodeID
     */
    public void setDefaultNodeID(int defaultNodeID) {
        setIntAt(8, defaultNodeID);
    }

    /**
     * returns the vrWorldFlags
     */
    public int getVRWorldFlags() {
        return getIntAt(12);
    }

    /**
     * set the vrWorldFlags
     */
    public void setVRWorldFlags(int vrWorldFlags) {
        setIntAt(12, vrWorldFlags);
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
        return getClass().getName() + "[nameAtomID=" + getNameAtomID() + ",defaultNodeID=" + getDefaultNodeID() + ",vrWorldFlags=" + getVRWorldFlags() + "]";
    }
}
