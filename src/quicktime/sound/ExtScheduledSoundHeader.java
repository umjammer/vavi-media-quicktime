
package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;
import quicktime.std.clocks.TimeRecord;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public final class ExtScheduledSoundHeader extends QTPointerRef implements InterfaceLib {
    private static Object linkage;

    public static final int kNativeSize = 106;

    private QTPointerRef ref;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ExtScheduledSoundHeader.class);
                    }
                });
            }
        }.establish();
    }

    public ExtScheduledSoundHeader(QTPointerRef paramQTPointerRef,
            int paramInt1,
            float paramFloat,
            int paramInt2,
            int paramInt3) throws QTException {
        this(paramQTPointerRef,
             paramInt1,
             paramFloat,
             paramQTPointerRef.getSize() / paramInt1 / (paramInt2 / 8),
             paramInt2,
             paramInt3,
             0,
             0);
    }

    public ExtScheduledSoundHeader(QTPointerRef paramQTPointerRef,
            int paramInt1,
            float paramFloat,
            int paramInt2,
            int paramInt3,
            int paramInt4,
            int paramInt5,
            int paramInt6) throws QTException {
        super(106, true);
        common_init();
        setSampleData(paramQTPointerRef);
        setNumChannels(paramInt1);
        setSampleRate(paramFloat);
        setNumFrames(paramInt2);
        setFormat(paramInt4);
        setSampleSize(paramInt3);
        setCompressionID(paramInt5);
        setPacketSize(paramInt6);
    }

    public ExtScheduledSoundHeader(QTPointerRef paramQTPointerRef,
            int paramInt1,
            float paramFloat,
            int paramInt2,
            int paramInt3,
            int paramInt4,
            int paramInt5,
            int paramInt6,
            TimeRecord paramTimeRecord,
            int paramInt7) throws QTException {
        super(106, true);
        common_init();
        setSampleData(paramQTPointerRef);
        setNumChannels(paramInt1);
        setSampleRate(paramFloat);
        setNumFrames(paramInt2);
        setFormat(paramInt4);
        setSampleSize(paramInt3);
        setCompressionID(paramInt5);
        setPacketSize(paramInt6);
        setStartTime(paramTimeRecord);
        setExtendedFlags(paramInt7);
    }

    private void common_init() {
        this.ref = null;
        setByteAt(20, (byte) -2);
        setByteAt(21, (byte) 60);

        setIntAt(66, 4);
        setIntAt(94, 106);
    }

    ExtScheduledSoundHeader(int paramInt, Object paramObject) {
        super(paramInt, 106, paramObject);
    }

    public QTPointerRef getSampleData() {
        return this.ref;
    }

    public void setSampleData(QTPointerRef paramQTPointerRef) {
        this.ref = paramQTPointerRef;
        setIntAt(0, QTObject.ID(paramQTPointerRef));
        setIntAt(102, paramQTPointerRef.getSize());
    }

    public int getNumChannels() {
        return getIntAt(4);
    }

    public void setNumChannels(int paramInt) {
        setIntAt(4, paramInt);
    }

    public float getSampleRate() {
        return QTUtils.UFix2X(getIntAt(8));
    }

    public void setSampleRate(float paramFloat) {
        setIntAt(8, QTUtils.X2UFix(paramFloat));
    }

    public int getNumFrames() {
        return getIntAt(22);
    }

    public void setNumFrames(int paramInt) {
        setIntAt(22, paramInt);
    }

    public int getFormat() {
        return getIntAt(40);
    }

    public void setFormat(int paramInt) {
        setIntAt(40, paramInt);
    }

    public void setSampleSize(int paramInt) {
        setShortAt(62, (short) paramInt);
    }

    public int getSampleSize() {
        return getShortAt(62);
    }

    public int getCompressionID() {
        return getShortAt(56);
    }

    public void setCompressionID(int paramInt) {
        setShortAt(56, (short) paramInt);
    }

    public int getPacketSize() {
        return getShortAt(58);
    }

    public void setPacketSize(int paramInt) {
        setShortAt(58, (short) paramInt);
    }

    public TimeRecord getStartTime() throws QTException {
        return new TimeRecord(getIntAt(86), getLongAt(78));
    }

    public void setStartTime(TimeRecord paramTimeRecord) {
        BlockMove(QTObject.ID(paramTimeRecord), _ID() + 78, 16);
    }

    public int getExtendedFlags() {
        return getIntAt(98);
    }

    public void setExtendedFlags(int paramInt) {
        setIntAt(98, paramInt);
    }

    public String toString() {
        return getClass().getName() + "[" + "data:" + this.ref + ",numChannels:" + getNumChannels() + ",sampleRate:"
               + getSampleRate() + ",sampleSize:" + getSampleSize() + ",sampleFormat:" + QTUtils.fromOSType(getFormat())
               + ",compressionID:" + getCompressionID() + "]";
    }

    private static native void BlockMove(int paramInt1, int paramInt2, int paramInt3);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * ExtScheduledSoundHeader.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
