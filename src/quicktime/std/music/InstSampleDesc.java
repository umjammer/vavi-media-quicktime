
package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class InstSampleDesc extends QTByteObject {
    public static final int kNativeSize = 46;

    static final long serialVersionUID = -7868738374047072834L;

    public InstSampleDesc() {
        super(46);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[46];

        paramObjectInputStream.read(getBytes());
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        paramObjectOutputStream.write(getBytes());
    }

    public int getDataFormat() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    public void setDataFormat(int paramInt) {
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getNumChannels() {
        return EndianOrder.flipBigEndianToNative16(getShortAt(4));
    }

    public void setNumChannels(int paramInt) {
        setShortAt(4, EndianOrder.flipNativeToBigEndian16((short) paramInt));
    }

    public int getSampleSize() {
        return EndianOrder.flipBigEndianToNative16(getShortAt(6));
    }

    public void setSampleSize(int paramInt) {
        setShortAt(6, EndianOrder.flipNativeToBigEndian16((short) paramInt));
    }

    public float getSampleRate() {
        return QTUtils.Fix2X(getSampleRateRaw());
    }

    public void setSampleRate(float paramFloat) {
        setSampleRateRaw(QTUtils.X2Fix(paramFloat));
    }

    public int getSampleRateRaw() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(8));
    }

    public void setSampleRateRaw(int paramInt) {
        setIntAt(8, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getSampleDataID() {
        return EndianOrder.flipBigEndianToNative16(getShortAt(12));
    }

    public void setSampleDataID(int paramInt) {
        setShortAt(12, EndianOrder.flipNativeToBigEndian16((short) paramInt));
    }

    public int getOffset() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(14));
    }

    public void setOffset(int paramInt) {
        setIntAt(14, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getNumSamples() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(18));
    }

    public void setNumSamples(int paramInt) {
        setIntAt(18, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getLoopType() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(22));
    }

    public void setLoopType(int paramInt) {
        setIntAt(22, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getLoopStart() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(26));
    }

    public void setLoopStart(int paramInt) {
        setIntAt(26, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getLoopEnd() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(30));
    }

    public void setLoopEnd(int paramInt) {
        setIntAt(30, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getPitchNormal() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(34));
    }

    public void setPitchNormal(int paramInt) {
        setIntAt(34, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getPitchLow() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(38));
    }

    public void setPitchLow(int paramInt) {
        setIntAt(38, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public int getPitchHigh() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(42));
    }

    public void setPitchHigh(int paramInt) {
        setIntAt(42, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public String toString() {
        return getClass().getName() + "[dataFormat=" + QTUtils.fromOSType(getDataFormat()) + ",numChannels=" + getNumChannels()
               + ",sampleSize=" + getSampleSize() + ",sampleRate=" + getSampleRate() + ",sampleDataID=" + getSampleDataID()
               + ",offset=" + getOffset() + ",numSamples=" + getNumSamples() + ",loopType=" + getLoopType() + ",loopStart="
               + getLoopStart() + ",loopEnd=" + getLoopEnd() + ",pitchNormal=" + getPitchNormal() + ",pitchLow=" + getPitchLow()
               + ",pitchHigh=" + getPitchHigh() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /InstSampleDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
