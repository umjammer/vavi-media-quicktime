package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

public final class QTVRPanoImaging extends QTVRAtom {

    private static final int kNativeSize = 56;

    static final long serialVersionUID = 8351228396821852032L;

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
    public QTVRPanoImaging(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    protected int getNativeSize() {
        return kNativeSize;
    }

    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRPanoImaging localQTVRPanoImaging = new QTVRPanoImaging();
        paramObjectInputStream.read(localQTVRPanoImaging.getBytes());
        System.arraycopy(localQTVRPanoImaging.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRPanoImaging localQTVRPanoImaging = (QTVRPanoImaging) clone();
        paramObjectOutputStream.write(localQTVRPanoImaging.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        try {
            return new QTVRPanoImaging(this.bytes);
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
        return localEndianDescriptor;
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
    public void setImagingMode(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * Valid bits are
     * kQTVRValidCorrection   			 = 1L << 0,
     * kQTVRValidQuality             = 1L << 1,
     * kQTVRValidDirectDraw          = 1L << 2,
     * kQTVRValidFirstExtraProperty  = 1L << 3
     */
    public int getImagingValidFlags() {
        return getIntAt(8);
    }

    /**
     * Valid bits are
     * kQTVRValidCorrection   			 = 1L << 0,
     * kQTVRValidQuality             = 1L << 1,
     * kQTVRValidDirectDraw          = 1L << 2,
     * kQTVRValidFirstExtraProperty  = 1L << 3
     */
    public void setImagingValidFlags(int paramInt) {
        setIntAt(8, paramInt);
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
    public void setCorrection(int paramInt) {
        setIntAt(12, paramInt);
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
    public void setQuality(int paramInt) {
        setIntAt(16, paramInt);
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
    public void setDirectDraw(boolean paramBoolean) {
        setIntAt(20, paramBoolean ? 1 : 0);
    }

    /**
     * returns the imagingProperty
     */
    public int getImagingProperty(int paramInt) throws QTException {
        if ((paramInt < 0) || (paramInt > 5))
            throw new QTException(-50);
        int i = 24 + paramInt * 4;
        return getIntAt(i);
    }

    /**
     * set the imagingProperty
     */
    public void setImagingProperty(int paramInt1, int paramInt2) throws QTException {
        if ((paramInt2 < 0) || (paramInt2 > 5))
            throw new QTException(-50);
        int i = 24 + paramInt2 * 4;
        setIntAt(i, paramInt1);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int paramInt) {
        setIntAt(48, paramInt);
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
    public void setReserved2(int paramInt) {
        setIntAt(52, paramInt);
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
        } catch (QTException localQTException) {
        }
        return str + " ]";
    }
}

