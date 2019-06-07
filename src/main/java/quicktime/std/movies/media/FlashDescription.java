package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

/**
 * This class maps the FlashDescription record of the MovieToolbox.
 * FlashDescription are used when adding and getting media samples to/from Flash tracks.
 */
public final class FlashDescription extends SampleDescription {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 28;

    private static EndianDescriptor ed;

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();
        EndianFlipSpec localEndianFlipSpec = new EndianFlipSpec(16, 4, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec);
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
     * This constructor makes a new SpriteDescription object.
     */
    public FlashDescription() throws QTException {
        super(kNativeSize, true, 1718383464);
    }

    private FlashDescription(int paramInt) {
        super(paramInt, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    public Object clone() {
        return new FlashDescription(makeAndCopyHandle());
    }

    public void setVersion(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getVersion() {
        return getIntAt(16);
    }

    public void setDecompressorType(int paramInt) {
        setIntAt(20, paramInt);
    }

    public int getDecompressorType() {
        return getIntAt(20);
    }

    public void setSampleFlags(int paramInt) {
        setIntAt(24, paramInt);
    }

    public int getSampleFlags() {
        return getIntAt(24);
    }
}
