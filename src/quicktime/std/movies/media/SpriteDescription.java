
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;


public final class SpriteDescription extends SampleDescription {
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

    public SpriteDescription() throws QTException {
        super(28, true, 1936749172);
    }

    private SpriteDescription(int paramInt) {
        super(paramInt, null, false);
    }

    public Object clone() {
        return new SpriteDescription(makeAndCopyHandle());
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

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SpriteDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
