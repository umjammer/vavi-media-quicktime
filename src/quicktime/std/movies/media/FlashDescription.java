
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;


public final class FlashDescription extends SampleDescription {
    public static final int kNativeSize = 28;

    private static EndianDescriptor ed;

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();

        EndianFlipSpec localEndianFlipSpec = new EndianFlipSpec(16, 4, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public FlashDescription() throws QTException {
        super(kNativeSize, true, 1718383464);
    }

    private FlashDescription(int paramInt) {
        super(paramInt, null, false);
    }

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
