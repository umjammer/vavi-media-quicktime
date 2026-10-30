package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

/**
 * This class maps the ThreeDDescription record of the MovieToolbox.
 * ThreeDDescription records are used when adding and getting media samples to/from ThreeD tracks.
 */
public final class ThreeDDescription extends SampleDescription {

    private static final int kThreeDDescriptionSize = 22;

    private static EndianDescriptor ed;

    protected static EndianDescriptor makeED() {
        EndianDescriptor ed = SampleDescription.makeED();
        EndianFlipSpec spec = new EndianFlipSpec(16, 2, 1);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(18, 4, 1);
        ed.addFlipSpec(endianFlipSpec2);
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
     * This constructor makes an empty SoundDescription object.
     * @param format the format of the ThreeD data that the description describes.
     */
    public ThreeDDescription(int format) throws QTException {
        super(kThreeDDescriptionSize, true, format);
    }

    private ThreeDDescription(int nativeRep, Object ref) {
        super(nativeRep, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new ThreeDDescription(makeAndCopyHandle(), null);
    }

    public void setVersion(int version) {
        setShortAt(16, (short) version);
    }

    public int getVersion() {
        return getShortAt(16);
    }

    public void setRendererType(int rendererType) {
        setIntAt(18, rendererType);
    }

    public int getRendererType() {
        return getIntAt(18);
    }
}
