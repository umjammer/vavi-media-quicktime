package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    static final long serialVersionUID = -4451518596558727761L;

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
    public QTVRPanoSample(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    protected int getNativeSize() {
        return kNativeSize;
    }

    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRPanoSample localQTVRPanoSample = new QTVRPanoSample();
        paramObjectInputStream.read(localQTVRPanoSample.getBytes());
        System.arraycopy(localQTVRPanoSample.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRPanoSample localQTVRPanoSample = (QTVRPanoSample) clone();
        paramObjectOutputStream.write(localQTVRPanoSample.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        try {
            return new QTVRPanoSample(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);
        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 4, 13);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
        EndianFlipSpec localEndianFlipSpec3 = new EndianFlipSpec(56, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec3);
        EndianFlipSpec localEndianFlipSpec4 = new EndianFlipSpec(60, 4, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec4);
        EndianFlipSpec localEndianFlipSpec5 = new EndianFlipSpec(68, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec5);
        EndianFlipSpec localEndianFlipSpec6 = new EndianFlipSpec(72, 4, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec6);
        return localEndianDescriptor;
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
    public void setImageRefTrackIndex(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * @return the track reference index of the full res hot spot track
     */
    public int getHotSpotRefTrackIndex() {
        return getIntAt(8);
    }

    public void setHotSpotRefTrackIndex(int paramInt) {
        setIntAt(8, paramInt);
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
    public void setMinPan(float paramFloat) {
        setFloatAt(12, paramFloat);
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
    public void setMaxPan(float paramFloat) {
        setFloatAt(16, paramFloat);
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
    public void setMinTilt(float paramFloat) {
        setFloatAt(20, paramFloat);
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
    public void setMaxTilt(float paramFloat) {
        setFloatAt(24, paramFloat);
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
    public void setMinFieldOfView(float paramFloat) {
        setFloatAt(28, paramFloat);
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
    public void setMaxFieldOfView(float paramFloat) {
        setFloatAt(32, paramFloat);
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
    public void setDefaultPan(float paramFloat) {
        setFloatAt(36, paramFloat);
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
    public void setDefaultTilt(float paramFloat) {
        setFloatAt(40, paramFloat);
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
    public void setDefaultFieldOfView(float paramFloat) {
        setFloatAt(44, paramFloat);
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
    public void setImageSize(QDPoint paramQDPoint) {
        setIntAt(48, paramQDPoint.getX());
        setIntAt(52, paramQDPoint.getY());
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
    public void setNumFrames(QDPoint paramQDPoint) {
        setShortAt(56, (short) paramQDPoint.getX());
        setShortAt(58, (short) paramQDPoint.getY());
    }

    /**
     * @return the pixel width and height of the hot spot panorama
     */
    public QDPoint getHotSpotSize() {
        return new QDPoint(getIntAt(60), getIntAt(64));
    }

    public void setHotSpotSize(QDPoint paramQDPoint) {
        setIntAt(60, paramQDPoint.getX());
        setIntAt(64, paramQDPoint.getY());
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
    public void setHotSpotNumFrames(QDPoint paramQDPoint) {
        setShortAt(68, (short) paramQDPoint.getX());
        setShortAt(70, (short) paramQDPoint.getY());
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
    public void setFlags(int paramInt) {
        setIntAt(72, paramInt);
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
    public void setPanoType(int paramInt) {
        setIntAt(76, paramInt);
    }

    /**
     * Sets the type of the panorama
     * @param panoType String
     */
    public void setPanoType(String paramString) {
        setIntAt(76, QTUtils.toOSType(paramString));
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
    public void setReserved2(int paramInt) {
        setIntAt(80, paramInt);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[imageRefTrackIndex=" + getImageRefTrackIndex() + ",hotSpotRefTrackIndex=" + getHotSpotRefTrackIndex() + ",minPan=" + getMinPan() + ",maxPan=" + getMaxPan() + ",minTilt=" + getMinTilt() + ",maxTilt=" + getMaxTilt() + ",minFieldOfView=" + getMinFieldOfView() + ",maxFieldOfView=" + getMaxFieldOfView() + ",defaultPan=" + getDefaultPan() + ",defaultTilt=" + getDefaultTilt() + ",defaultFieldOfView=" + getDefaultFieldOfView() + ",imageSize=" + getImageSize() + ",imageNumFrames=" + getNumFrames() + ",hotSpotSize=" + getHotSpotSize() + ",hotSpotNumFrames=" + getHotSpotNumFrames() + ",flags=" + getFlags() + ",panoType=" + getPanoType() + ",reserved2=" + getReserved2() + "]";
    }
}

