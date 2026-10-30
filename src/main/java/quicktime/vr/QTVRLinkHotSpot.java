package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.qd.QDPoint;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

public final class QTVRLinkHotSpot extends QTVRAtom {

    private static final int kNativeSize = 72;

    @Serial
    private static final long serialVersionUID = 2642915607141217566L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRLinkHotSpot object containing no data
     */
    public QTVRLinkHotSpot() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRLinkHotSpot object from a byte array
     * Used in cloning
     */
    public QTVRLinkHotSpot(byte[] bytes) throws QTException {
        super(bytes, kNativeSize);
    }

    @Override
    protected int getNativeSize() {
        return 72;
    }

    @Override
    protected void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRLinkHotSpot qtvrLinkHotSpot = new QTVRLinkHotSpot();
        in.read(qtvrLinkHotSpot.getBytes());
        System.arraycopy(qtvrLinkHotSpot.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Override
    protected void writeObject(ObjectOutputStream out) throws IOException {
        QTVRLinkHotSpot qtvrLinkHotSpot = (QTVRLinkHotSpot) clone();
        out.write(qtvrLinkHotSpot.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        try {
            return new QTVRLinkHotSpot(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    private static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 2, 2);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(4, 4, 17);
        ed.addFlipSpec(endianFlipSpec2);
        return ed;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * returns the toNodeID
     */
    public int getToNodeID() {
        return getIntAt(4);
    }

    /**
     * set the toNodeID
     */
    public void setToNodeID(int toNodeID) {
        setIntAt(4, toNodeID);
    }

    /**
     * returns the fromValidFlags
     */
    public int getFromValidFlags() {
        return getIntAt(8);
    }

    /**
     * set the fromValidFlags
     */
    public void setFromValidFlags(int fromValidFlags) {
        setIntAt(8, fromValidFlags);
    }

    /**
     * returns the fromPan
     */
    public float getFromPan() {
        return getFloatAt(12);
    }

    /**
     * set the fromPan
     */
    public void setFromPan(float fromPan) {
        setFloatAt(12, fromPan);
    }

    /**
     * returns the fromTilt
     */
    public float getFromTilt() {
        return getFloatAt(16);
    }

    /**
     * set the fromTilt
     */
    public void setFromTilt(float fromTilt) {
        setFloatAt(16, fromTilt);
    }

    /**
     * returns the fromFOV
     */
    public float getFromFOV() {
        return getFloatAt(20);
    }

    /**
     * set the fromFOV
     */
    public void setFromFOV(float fromFOV) {
        setFloatAt(20, fromFOV);
    }

    /**
     * returns the fromViewCenter
     */
    public QDPoint getFromViewCenter() {
        return new QDPoint(getFloatAt(24), getFloatAt(28));
    }

    /**
     * set the fromViewCenter
     */
    public void setFromViewCenter(QDPoint fromViewCenter) {
        setFloatAt(24, fromViewCenter.getXF());
        setFloatAt(28, fromViewCenter.getYF());
    }

    /**
     * returns the toValidFlags
     */
    public int getToValidFlags() {
        return getIntAt(32);
    }

    /**
     * set the toValidFlags
     */
    public void setToValidFlags(int toValidFlags) {
        setIntAt(32, toValidFlags);
    }

    /**
     * returns the toPan
     */
    public float getToPan() {
        return getFloatAt(36);
    }

    /**
     * set the toPan
     */
    public void setToPan(float toPan) {
        setFloatAt(36, toPan);
    }

    /**
     * returns the toTilt
     */
    public float getToTilt() {
        return getFloatAt(40);
    }

    /**
     * set the toTilt
     */
    public void setToTilt(float toTilt) {
        setFloatAt(40, toTilt);
    }

    /**
     * returns the toFOV
     */
    public float getToFOV() {
        return getFloatAt(44);
    }

    /**
     * set the toFOV
     */
    public void setToFOV(float toFOV) {
        setFloatAt(44, toFOV);
    }

    /**
     * returns the toViewCenter
     */
    public QDPoint getToViewCenter() {
        return new QDPoint(getFloatAt(48), getFloatAt(52));
    }

    /**
     * set the toViewCenter
     */
    public void setToViewCenter(QDPoint toViewCenter) {
        setFloatAt(48, toViewCenter.getXF());
        setFloatAt(52, toViewCenter.getYF());
    }

    /**
     * returns the distance
     */
    public float getDistance() {
        return getFloatAt(56);
    }

    /**
     * set the distance
     */
    public void setDistance(float distance) {
        setFloatAt(56, distance);
    }

    /**
     * returns the flags
     */
    public int getFlags() {
        return getIntAt(60);
    }

    /**
     * set the flags
     */
    public void setFlags(int flags) {
        setIntAt(60, flags);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int reserved1) {
        setIntAt(64, reserved1);
    }

    /**
     * returns the reserved1
     */
    public int getReserved1() {
        return getIntAt(64);
    }

    /**
     * set the reserved2
     */
    public void setReserved2(int reserved2) {
        setIntAt(68, reserved2);
    }

    /**
     * returns the reserved2
     */
    public int getReserved2() {
        return getIntAt(68);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[toNodeID=" + getToNodeID() + ",fromValidFlags=" + getFromValidFlags() + ",fromPan=" + getFromPan() + ",fromTilt" + getFromTilt() + ",fromFOV" + getFromFOV() + ",fromViewCenter" + getFromViewCenter() + ",toValidFlags" + getToValidFlags() + ",toPan" + getToPan() + ",toTilt" + getToTilt() + ",toFOV" + getToFOV() + ",toViewCenter" + getToViewCenter() + ",distance" + getDistance() + ",flags" + getFlags() + "]";
    }
}
