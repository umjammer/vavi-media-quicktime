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
import quicktime.util.QTUtils;

public final class QTVRPanoSample extends QTVRAtom {

    private static final int kNativeSize = 84;

    static final int kQTVRPanoFlagHorizontal = 1;

    static final int kQTVRPanoFlagLast = Integer.MIN_VALUE;

    @Serial
    private static final long serialVersionUID = -4451518596558727761L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRPanoSample object containing no data
     */
    public QTVRPanoSample() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRPanoSample object from a byte array
     * Used in cloning
     */
    public QTVRPanoSample(byte[] bytes) throws QTException {
        super(bytes, kNativeSize);
    }

    @Override
    protected int getNativeSize() {
        return kNativeSize;
    }

    @Override
    protected void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRPanoSample qtvrPanoSample = new QTVRPanoSample();
        in.read(qtvrPanoSample.getBytes());
        System.arraycopy(qtvrPanoSample.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Override
    protected void writeObject(ObjectOutputStream out) throws IOException {
        QTVRPanoSample qtvrPanoSample = (QTVRPanoSample) clone();
        out.write(qtvrPanoSample.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        try {
            return new QTVRPanoSample(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    private static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 2, 2);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(4, 4, 13);
        ed.addFlipSpec(endianFlipSpec2);
        EndianFlipSpec endianFlipSpec3 = new EndianFlipSpec(56, 2, 2);
        ed.addFlipSpec(endianFlipSpec3);
        EndianFlipSpec endianFlipSpec4 = new EndianFlipSpec(60, 4, 2);
        ed.addFlipSpec(endianFlipSpec4);
        EndianFlipSpec endianFlipSpec5 = new EndianFlipSpec(68, 2, 2);
        ed.addFlipSpec(endianFlipSpec5);
        EndianFlipSpec endianFlipSpec6 = new EndianFlipSpec(72, 4, 3);
        ed.addFlipSpec(endianFlipSpec6);
        return ed;
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * @return the track reference index of the full res image track
     */
    public int getImageRefTrackIndex() {
        return getIntAt(4);
    }

    /**
     * @param imageRefTrackIndex the track reference index of the full res image track
     */
    public void setImageRefTrackIndex(int imageRefTrackIndex) {
        setIntAt(4, imageRefTrackIndex);
    }

    /**
     * @return the track reference index of the full res hot spot track
     */
    public int getHotSpotRefTrackIndex() {
        return getIntAt(8);
    }

    public void setHotSpotRefTrackIndex(int hotSpotRefTrackIndex) {
        setIntAt(8, hotSpotRefTrackIndex);
    }

    /**
     * @return the minPan
     */
    public float getMinPan() {
        return getFloatAt(12);
    }

    /**
     * @param minPan the minPan
     */
    public void setMinPan(float minPan) {
        setFloatAt(12, minPan);
    }

    /**
     * @return the maxPan
     */
    public float getMaxPan() {
        return getFloatAt(16);
    }

    /**
     * @param maxPan the maxPan
     */
    public void setMaxPan(float maxPan) {
        setFloatAt(16, maxPan);
    }

    /**
     * @return the minTilt
     */
    public float getMinTilt() {
        return getFloatAt(20);
    }

    /**
     * @param minTilt the minTilt
     */
    public void setMinTilt(float minTilt) {
        setFloatAt(20, minTilt);
    }

    /**
     * @return the maxTilt
     */
    public float getMaxTilt() {
        return getFloatAt(24);
    }

    /**
     * @param maxTilt the maxTilt
     */
    public void setMaxTilt(float maxTilt) {
        setFloatAt(24, maxTilt);
    }

    /**
     * @return the minFieldOfView
     */
    public float getMinFieldOfView() {
        return getFloatAt(28);
    }

    /**
     * @param minFieldOfView the minFieldOfView
     */
    public void setMinFieldOfView(float minFieldOfView) {
        setFloatAt(28, minFieldOfView);
    }

    /**
     * @return the maxFieldOfView
     */
    public float getMaxFieldOfView() {
        return getFloatAt(32);
    }

    /**
     * @param maxFieldOfView the maxFieldOfView
     */
    public void setMaxFieldOfView(float maxFieldOfView) {
        setFloatAt(32, maxFieldOfView);
    }

    /**
     * @return the defaultPan
     */
    public float getDefaultPan() {
        return getFloatAt(36);
    }

    /**
     * @param defaultPan the defaultPan
     */
    public void setDefaultPan(float defaultPan) {
        setFloatAt(36, defaultPan);
    }

    /**
     * @return the defaultTilt
     */
    public float getDefaultTilt() {
        return getFloatAt(40);
    }

    /**
     * @param defaultTilt the defaultTilt
     */
    public void setDefaultTilt(float defaultTilt) {
        setFloatAt(40, defaultTilt);
    }

    /**
     * @return the defaultFieldOfView
     */
    public float getDefaultFieldOfView() {
        return getFloatAt(44);
    }

    /**
     * @param defaultFieldOfView the defaultFieldOfView
     */
    public void setDefaultFieldOfView(float defaultFieldOfView) {
        setFloatAt(44, defaultFieldOfView);
    }

    /**
     * @return the pixel width and height of the panorama
     */
    public QDPoint getImageSize() {
        return new QDPoint(getIntAt(48), getIntAt(52));
    }

    /**
     * @param imageSize the pixel width and height of the panorama
     */
    public void setImageSize(QDPoint imageSize) {
        setIntAt(48, imageSize.getX());
        setIntAt(52, imageSize.getY());
    }

    /**
     * @return the number of diced image frames wide and high
     */
    public QDPoint getNumFrames() {
        return new QDPoint(getShortAt(56), getShortAt(58));
    }

    /**
     * @param imageNumFrames the number of diced image frames wide and high
     */
    public void setNumFrames(QDPoint imageNumFrames) {
        setShortAt(56, (short) imageNumFrames.getX());
        setShortAt(58, (short) imageNumFrames.getY());
    }

    /**
     * @return the pixel width and height of the hot spot panorama
     */
    public QDPoint getHotSpotSize() {
        return new QDPoint(getIntAt(60), getIntAt(64));
    }

    public void setHotSpotSize(QDPoint hotSpotSize) {
        setIntAt(60, hotSpotSize.getX());
        setIntAt(64, hotSpotSize.getY());
    }

    /**
     * @return the number of diced hotspot frames wide and high
     */
    public QDPoint getHotSpotNumFrames() {
        return new QDPoint(getShortAt(68), getShortAt(70));
    }

    /**
     * @param hotSpotNumFrames the number of diced hotspot frames wide and high
     */
    public void setHotSpotNumFrames(QDPoint hotSpotNumFrames) {
        setShortAt(68, (short) hotSpotNumFrames.getX());
        setShortAt(70, (short) hotSpotNumFrames.getY());
    }

    /**
     * @return the flags
     */
    public int getFlags() {
        return getIntAt(72);
    }

    /**
     * @param flags -
     */
    public void setFlags(int flags) {
        setIntAt(72, flags);
    }

    /**
     * @return the panoType, (reserved1)
     */
    public int getPanoType() {
        return getIntAt(76);
    }

    /**
     * @param panoType (reserved1)
     */
    public void setPanoType(int panoType) {
        setIntAt(76, panoType);
    }

    /**
     * Sets the type of the panorama
     * @param panoType String
     */
    public void setPanoType(String panoType) {
        setIntAt(76, QTUtils.toOSType(panoType));
    }

    /**
     * @return the reserved2
     */
    public int getReserved2() {
        return getIntAt(80);
    }

    /**
     * @param reserved2 -
     */
    public void setReserved2(int reserved2) {
        setIntAt(80, reserved2);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[imageRefTrackIndex=" + getImageRefTrackIndex() + ",hotSpotRefTrackIndex=" + getHotSpotRefTrackIndex() + ",minPan=" + getMinPan() + ",maxPan=" + getMaxPan() + ",minTilt=" + getMinTilt() + ",maxTilt=" + getMaxTilt() + ",minFieldOfView=" + getMinFieldOfView() + ",maxFieldOfView=" + getMaxFieldOfView() + ",defaultPan=" + getDefaultPan() + ",defaultTilt=" + getDefaultTilt() + ",defaultFieldOfView=" + getDefaultFieldOfView() + ",imageSize=" + getImageSize() + ",imageNumFrames=" + getNumFrames() + ",hotSpotSize=" + getHotSpotSize() + ",hotSpotNumFrames=" + getHotSpotNumFrames() + ",flags=" + getFlags() + ",panoType=" + getPanoType() + ",reserved2=" + getReserved2() + "]";
    }
}
