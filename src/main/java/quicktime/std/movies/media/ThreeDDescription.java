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
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();
        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(16, 2, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(18, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
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
     * This constructor makes an empty SoundDescription object.
     * @param format the format of the ThreeD data that the description describes.
     */
    public ThreeDDescription(int format) throws QTException {
        super(kThreeDDescriptionSize, true, format);
    }

    private ThreeDDescription(int paramInt, Object paramObject) {
        super(paramInt, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new ThreeDDescription(makeAndCopyHandle(), null);
    }

    public void setVersion(int paramInt) {
        setShortAt(16, (short) paramInt);
    }

    public int getVersion() {
        return getShortAt(16);
    }

    public void setRendererType(int paramInt) {
        setIntAt(18, paramInt);
    }

    public int getRendererType() {
        return getIntAt(18);
    }
}

