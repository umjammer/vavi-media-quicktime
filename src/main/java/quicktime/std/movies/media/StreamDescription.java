package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

/**
 * This class maps the StreamDescription record of the MovieToolbox.
 */
public final class StreamDescription extends SampleDescription {

    private static final int kNativeSize = 28;

    private static EndianDescriptor ed;

    protected static EndianDescriptor makeED() {
        EndianDescriptor ed = SampleDescription.makeED();
        EndianFlipSpec spec = new EndianFlipSpec(16, 4, 3);
        ed.addFlipSpec(spec);
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
     * This constructor makes a new StreamDescription object. This sets the default type of the
     * StreamDescription to 'rtsp' -> StdQTConstants4.kQTSRTSPDataType
     */
    public StreamDescription() throws QTException {
        super(28, true, 1920234352);
    }

    /**
     * This constructor makes a new StreamDescription object. This sets the default type of the
     * @param mediaType this is the mediaType of the StreamDescription
     *
     *   StdQTConstants4.kQTSRTSPDataType
     *   StdQTConstants4.kQTSSDPDataType
     */
    public StreamDescription(int mediaType) throws QTException {
        super(28, true, mediaType);
    }

    private StreamDescription(int nativeRep, Object ref) {
        super(nativeRep, ref, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new StreamDescription(makeAndCopyHandle(), null);
    }

    public void setFlags(int flags) {
        setIntAt(24, flags);
    }

    public int getFlags() {
        return getIntAt(24);
    }

    public void setVersion(int version) {
        setIntAt(16, version);
    }

    public int getVersion() {
        return getIntAt(16);
    }
}
