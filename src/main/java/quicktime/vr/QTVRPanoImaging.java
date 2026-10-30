package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

public final class QTVRPanoImaging extends QTVRAtom {

    private static final int kNativeSize = 56;

    @Serial
    private static final long serialVersionUID = 8351228396821852032L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRPanoImaging object containing no data
     */
    public QTVRPanoImaging() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRPanoImaging object from a byte array
     * Used in cloning
     */
    public QTVRPanoImaging(byte[] bytes) throws QTException {
        super(bytes, kNativeSize);
    }

    @Override
    protected int getNativeSize() {
        return kNativeSize;
    }

    @Override
    protected void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRPanoImaging qtvrPanoImaging = new QTVRPanoImaging();
        in.read(qtvrPanoImaging.getBytes());
        System.arraycopy(qtvrPanoImaging.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Override
    protected void writeObject(ObjectOutputStream out) throws IOException {
        QTVRPanoImaging qtvrPanoImaging = (QTVRPanoImaging) clone();
        out.write(qtvrPanoImaging.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        try {
            return new QTVRPanoImaging(this.bytes);
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
        return ed;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * returns the imaging Mode
     */
    public int getImagingMode() {
        return getIntAt(4);
    }

    /**
     * set the imaging Mode
     */
    public void setImagingMode(int imagingMode) {
        setIntAt(4, imagingMode);
    }

    /**
     * Valid bits are
     * kQTVRValidCorrection       = 1L << 0,
     * kQTVRValidQuality             = 1L << 1,
     * kQTVRValidDirectDraw          = 1L << 2,
     * kQTVRValidFirstExtraProperty  = 1L << 3
     */
    public int getImagingValidFlags() {
        return getIntAt(8);
    }

    /**
     * Valid bits are
     * kQTVRValidCorrection       = 1L << 0,
     * kQTVRValidQuality             = 1L << 1,
     * kQTVRValidDirectDraw          = 1L << 2,
     * kQTVRValidFirstExtraProperty  = 1L << 3
     */
    public void setImagingValidFlags(int imagingValidFlags) {
        setIntAt(8, imagingValidFlags);
    }

    /**
     * returns the panorama correction
     */
    public int getCorrection() {
        return getIntAt(12);
    }

    /**
     * set the panorama correction
     */
    public void setCorrection(int correction) {
        setIntAt(12, correction);
    }

    /**
     * returns the panorama quality
     */
    public int getQuality() {
        return getIntAt(16);
    }

    /**
     * set the panorama quality
     */
    public void setQuality(int quality) {
        setIntAt(16, quality);
    }

    /**
     * returns the direct draw mode
     */
    public boolean isDirectDraw() {
        return getIntAt(20) == 1;
    }

    /**
     * set the direct draw mode
     */
    public void setDirectDraw(boolean directDraw) {
        setIntAt(20, directDraw ? 1 : 0);
    }

    /**
     * returns the imagingProperty
     */
    public int getImagingProperty(int index) throws QTException {
        if ((index < 0) || (index > 5))
            throw new QTException(-50);
        int i = 24 + index * 4;
        return getIntAt(i);
    }

    /**
     * set the imagingProperty
     */
    public void setImagingProperty(int value, int index) throws QTException {
        if ((index < 0) || (index > 5))
            throw new QTException(-50);
        int i = 24 + index * 4;
        setIntAt(i, value);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int reserved1) {
        setIntAt(48, reserved1);
    }

    /**
     * returns the reserved1
     */
    public int getReserved1() {
        return getIntAt(48);
    }

    /**
     * set the reserved2
     */
    public void setReserved2(int reserved2) {
        setIntAt(52, reserved2);
    }

    /**
     * returns the reserved2
     */
    public int getReserved2() {
        return getIntAt(52);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        String str = getClass().getName() + "[imagingMode=" + getImagingMode() + ",imagingValidFlags=" + getImagingValidFlags() + ",correction=" + getCorrection() + ",quality=" + getQuality() + ",directDraw=" + isDirectDraw();
        try {
            str = str + ",imagingProperties=[" + getImagingProperty(0) + "][" + getImagingProperty(1) + "][" + getImagingProperty(2) + "][" + getImagingProperty(3) + "][" + getImagingProperty(4) + "][" + getImagingProperty(5) + "]";
        } catch (QTException e) {
        }
        return str + " ]";
    }
}
