
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.movies.AtomContainer;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTHandle;


public final class QTVRSampleDescription extends SampleDescription {
    private static EndianDescriptor ed;

    public QTVRSampleDescription(AtomContainer paramAtomContainer) throws QTException {
        super(allocate(paramAtomContainer), true, 1903457906);
        copyFromArray(16, paramAtomContainer.getBytes(), 0, paramAtomContainer.getSize());
    }

    private static int allocate(AtomContainer paramAtomContainer) {
        return 16 + paramAtomContainer.getSize();
    }

    public QTVRSampleDescription() throws QTException {
        super(16, true, 1903457906);
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();

        EndianFlipSpec localEndianFlipSpec = new EndianFlipSpec(16, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public void setVRWorld(AtomContainer paramAtomContainer) throws QTException {
        int i = 16 + paramAtomContainer.getSize();
        setSize(i);

        copyFromArray(16, paramAtomContainer.getBytes(), 0, i);

        setIntAt(0, i);
    }

    public AtomContainer getVRWorld() throws QTException {
        int i = getSize() - 16;
        QTHandle localQTHandle = new QTHandle(i, false);

        localQTHandle.copyFromArray(0, getBytes(), 16, i);

        return AtomContainer.fromQTHandle(localQTHandle);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/QTVRSampleDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
