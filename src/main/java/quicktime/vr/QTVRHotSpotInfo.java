package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;

public final class QTVRHotSpotInfo extends QTVRAtom {

    private static final int kNativeSize = 68;

    @Serial
    private static final long serialVersionUID = -7062349173192562621L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRHotSpotInfo object containing no data.
     */
    public QTVRHotSpotInfo() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRHotSpotInfo object from a byte array
     * Used in cloning
     */
    public QTVRHotSpotInfo(byte[] bytes) throws QTException {
        super(bytes, kNativeSize);
    }

    @Override
    protected int getNativeSize() {
        return 68;
    }

    @Override
    protected void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRHotSpotInfo qtvrHotSpotInfo = new QTVRHotSpotInfo();
        in.read(qtvrHotSpotInfo.getBytes());
        System.arraycopy(qtvrHotSpotInfo.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Override
    protected void writeObject(ObjectOutputStream out) throws IOException {
        QTVRHotSpotInfo qtvrHotSpotInfo = (QTVRHotSpotInfo) clone();
        out.write(qtvrHotSpotInfo.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        try {
            return new QTVRHotSpotInfo(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    private static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 2, 2);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(4, 4, 16);
        ed.addFlipSpec(endianFlipSpec2);
        return ed;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * returns the hotSpotType
     */
    public int getHotSpotType() {
        return getIntAt(4);
    }

    /**
     * set the hotSpotType
     * 'link' 'undf' 'url '
     */
    public void setHotSpotType(int hotSpotType) {
        setIntAt(4, hotSpotType);
    }

    /**
     * sets the hotSpotType using a string
     * "link" "undf" "url "
     */
    public void setHotSpotType(String hotSpotType) {
        setIntAt(4, QTUtils.toOSType(hotSpotType));
    }

    /**
     * returns the nameAtomID
     */
    public int getNameAtomID() {
        return getIntAt(8);
    }

    /**
     * set the nameAtomID
     * ID of the QTVRString atom
     * zero means no name atom
     */
    public void setNameAtomID(int nameAtomID) {
        setIntAt(8, nameAtomID);
    }

    /**
     * returns the commentAtomID
     */
    public int getCommentAtomID() {
        return getIntAt(12);
    }

    /**
     * set the commentAtomID
     * ID of the QTVRString atom
     * zero means no comment atom
     */
    public void setCommentAtomID(int commentAtomID) {
        setIntAt(12, commentAtomID);
    }

    /**
     * returns the mouseOver cursorID
     */
    public int getMouseOverCursorID() {
        return getIntAt(16);
    }

    /**
     * set the mouseOver cursorID
     */
    public void setMouseOverCursorID(int mouseOverCursorID) {
        setIntAt(16, mouseOverCursorID);
    }

    /**
     * returns the mouseDown cursorID
     */
    public int getMouseDownCursorID() {
        return getIntAt(20);
    }

    /**
     * set the mouseDown cursorID
     */
    public void setMouseDownCursorID(int mouseDownCursorID) {
        setIntAt(20, mouseDownCursorID);
    }

    /**
     * returns the mouseUp cursorID
     */
    public int getMouseUpCursorID() {
        return getIntAt(24);
    }

    /**
     * set the mouseUp cursorID
     */
    public void setMouseUpCursorID(int mouseUpCursorID) {
        setIntAt(24, mouseUpCursorID);
    }

    /**
     * returns the bestPan
     */
    public float getBestPan() {
        return getFloatAt(28);
    }

    /**
     * set the bestPan
     */
    public void setBestPan(float bestPan) {
        setFloatAt(28, bestPan);
    }

    /**
     * returns the bestTilt
     */
    public float getBestTilt() {
        return getFloatAt(32);
    }

    /**
     * set the bestTilt
     */
    public void setBestTilt(float bestTilt) {
        setFloatAt(32, bestTilt);
    }

    /**
     * returns the bestFOV
     */
    public float getBestFOV() {
        return getFloatAt(36);
    }

    /**
     * set the bestFOV
     */
    public void setBestFOV(float bestFOV) {
        setFloatAt(36, bestFOV);
    }

    /**
     * returns the bestViewCenter
     */
    public QDPoint getBestViewCenter() {
        return new QDPoint(getFloatAt(40), getFloatAt(44));
    }

    /**
     * set the bestViewCenter
     */
    public void setBestViewCenter(QDPoint bestViewCenter) {
        setFloatAt(40, bestViewCenter.getXF());
        setFloatAt(44, bestViewCenter.getYF());
    }

    /**
     * returns the hotSpotRect
     */
    public QDRect getHotSpotRect() {
        return new QDRect(getIntAt(50), getIntAt(48), getIntAt(54) - getIntAt(50), getIntAt(52) - getIntAt(48));
    }

    /**
     * set the hotSpotRect
     */
    public void setHotSpotRect(QDRect hotSpotRect) {
        setIntAt(48, hotSpotRect.getY());
        setIntAt(50, hotSpotRect.getX());
        setIntAt(52, hotSpotRect.getY() + hotSpotRect.getHeight());
        setIntAt(54, hotSpotRect.getX() + hotSpotRect.getWidth());
    }

    /**
     * returns the flags
     */
    public int getFlags() {
        return getIntAt(56);
    }

    /**
     * set the flags
     */
    public void setFlags(int flags) {
        setIntAt(56, flags);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int reserved1) {
        setIntAt(60, reserved1);
    }

    /**
     * returns the reserved1
     */
    public int getReserved1() {
        return getIntAt(60);
    }

    /**
     * set the reserved2
     */
    public void setReserved2(int reserved2) {
        setIntAt(64, reserved2);
    }

    /**
     * returns the reserved2
     */
    public int getReserved2() {
        return getIntAt(64);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[hotSpotType=" + QTUtils.fromOSType(getHotSpotType()) + ",nameAtomID=" + getNameAtomID() + ",commentAtomID=" + getCommentAtomID() + ",bestPan" + getBestPan() + ",bestTilt" + getBestTilt() + ",bestFOV" + getBestFOV() + ",bestViewCenter" + getBestViewCenter() + ",hotSpotRect" + getHotSpotRect() + ",flags" + getFlags() + "]";
    }
}
