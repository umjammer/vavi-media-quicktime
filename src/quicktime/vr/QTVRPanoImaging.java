
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

    public QTVRPanoImaging() {
        super(kNativeSize);
    }

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

    public int getImagingMode() {
        return getIntAt(4);
    }

    public void setImagingMode(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getImagingValidFlags() {
        return getIntAt(8);
    }

    public void setImagingValidFlags(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getCorrection() {
        return getIntAt(12);
    }

    public void setCorrection(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getQuality() {
        return getIntAt(16);
    }

    public void setQuality(int paramInt) {
        setIntAt(16, paramInt);
    }

    public boolean isDirectDraw() {
        return getIntAt(20) == 1;
    }

    public void setDirectDraw(boolean paramBoolean) {
        setIntAt(20, paramBoolean ? 1 : 0);
    }

    public int getImagingProperty(int paramInt) throws QTException {
        if ((paramInt < 0) || (paramInt > 5))
            throw new QTException(-50);
        int i = 24 + paramInt * 4;
        return getIntAt(i);
    }

    public void setImagingProperty(int paramInt1, int paramInt2) throws QTException {
        if ((paramInt2 < 0) || (paramInt2 > 5))
            throw new QTException(-50);
        int i = 24 + paramInt2 * 4;
        setIntAt(i, paramInt1);
    }

    public void setReserved1(int paramInt) {
        setIntAt(48, paramInt);
    }

    public int getReserved1() {
        return getIntAt(48);
    }

    public void setReserved2(int paramInt) {
        setIntAt(52, paramInt);
    }

    public int getReserved2() {
        return getIntAt(52);
    }

    public String toString() {
        String str = getClass().getName() + "[imagingMode=" + getImagingMode() + ",imagingValidFlags=" + getImagingValidFlags()
                     + ",correction=" + getCorrection() + ",quality=" + getQuality() + ",directDraw=" + isDirectDraw();

        try {
            str = str + ",imagingProperties=[" + getImagingProperty(0) + "][" + getImagingProperty(1) + "]["
                  + getImagingProperty(2) + "][" + getImagingProperty(3) + "][" + getImagingProperty(4) + "]["
                  + getImagingProperty(5) + "]";
        } catch (QTException localQTException) {
        }

        return str + " ]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRPanoImaging.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
