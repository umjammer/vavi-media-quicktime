
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;


public final class StreamDescription extends SampleDescription {
    private static final int kNativeSize = 28;

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

    public StreamDescription() throws QTException {
        super(28, true, 1920234352);
    }

    public StreamDescription(int paramInt) throws QTException {
        super(28, true, paramInt);
    }

    private StreamDescription(int paramInt, Object paramObject) {
        super(paramInt, paramObject, false);
    }

    public Object clone() {
        return new StreamDescription(makeAndCopyHandle(), null);
    }

    public void setFlags(int paramInt) {
        setIntAt(24, paramInt);
    }

    public int getFlags() {
        return getIntAt(24);
    }

    public void setVersion(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getVersion() {
        return getIntAt(16);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/StreamDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
