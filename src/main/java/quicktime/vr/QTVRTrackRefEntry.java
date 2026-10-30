package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTByteObject;

public final class QTVRTrackRefEntry extends QTByteObject implements Cloneable {

    private static final int kNativeSize = 10;

    @Serial
    private static final long serialVersionUID = 7513389675945192418L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRTrackRefEntry object containing no data
     */
    public QTVRTrackRefEntry() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRTrackRefEntry object from a byte array
     * Used in cloning
     */
    public QTVRTrackRefEntry(byte[] bytes) throws QTException {
        super(bytes);
        if (bytes.length != kNativeSize) {
            throw new QTException(-50);
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRTrackRefEntry qtvrTrackRefEntry = new QTVRTrackRefEntry();
        in.read(qtvrTrackRefEntry.getBytes());
        System.arraycopy(qtvrTrackRefEntry.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        QTVRTrackRefEntry qtvrTrackRefEntry = (QTVRTrackRefEntry) clone();
        out.write(qtvrTrackRefEntry.getBytes());
    }

    @Override
    protected Object clone() {
        try {
            return new QTVRTrackRefEntry(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    private static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 4, 1);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(4, 2, 1);
        ed.addFlipSpec(endianFlipSpec2);
        EndianFlipSpec endianFlipSpec3 = new EndianFlipSpec(6, 4, 1);
        ed.addFlipSpec(endianFlipSpec3);
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
     * Track reference types
     * kQTVRImageTrackRefType
     * kQTVRHotSpotTrackRefType
     */
    public int getTrackRefType() {
        return getIntAt(0);
    }

    /**
     * Track reference types
     * kQTVRImageTrackRefType
     * kQTVRHotSpotTrackRefType
     */
    public void setTrackRefType(int trackRefType) {
        setIntAt(0, trackRefType);
    }

    /**
     * kQTVRFullTrackRes             = kQTVRFullRes,
     * kQTVRHalfTrackRes             = kQTVRHalfRes,
     * kQTVRQuarterTrackRes          = kQTVRQuarterRes,
     * kQTVRPreviewTrackRes          = 0x8000
     */
    public short getTrackResolution() {
        return getShortAt(4);
    }

    /**
     * kQTVRFullTrackRes             = kQTVRFullRes,
     * kQTVRHalfTrackRes             = kQTVRHalfRes,
     * kQTVRQuarterTrackRes          = kQTVRQuarterRes,
     * kQTVRPreviewTrackRes          = 0x8000
     */
    public void setTrackResolution(short trackResolution) {
        setShortAt(4, trackResolution);
    }

    /**
     * returns the trackRefIndex
     */
    public int getTrackRefIndex() {
        return getIntAt(6);
    }

    /**
     * set the trackRefIndex
     */
    public void setTrackRefIndex(int trackRefIndex) {
        setIntAt(6, trackRefIndex);
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[trackRefType=" + getTrackRefType() + ",trackResolution=" + getTrackResolution() + ",trackRefIndex=" + getTrackRefIndex() + "]";
    }
}
