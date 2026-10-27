package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.movies.AtomContainer;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTHandle;

/**
 * This class represents a description of QTVR media.
 *
 * offset size
 * UInt32  descSize;  0  4   /* total size of the QTVRSampleDescription
 * UInt32 descType;  4  4   /* must be 'qtvr'
 *
 * UInt32 reserved1;  8  4   /* must be zero
 * UInt16 reserved2;  12  2   /* must be zero
 * UInt16 dataRefIndex; 14  2   /* must be zero
 *
 * UInt32 data;   16  4   /* Will be extended to hold vrWorld QTAtomContainer
 * => 20
 */
public final class QTVRSampleDescription extends SampleDescription {

    private static EndianDescriptor ed;

    /**
     * This constructor makes a new QTVRSampleDescription object.
     */
    public QTVRSampleDescription(AtomContainer paramAtomContainer) throws QTException {
        super(allocate(paramAtomContainer), true, 1903457906);
        copyFromArray(16, paramAtomContainer.getBytes(), 0, paramAtomContainer.getSize());
    }

    private static int allocate(AtomContainer paramAtomContainer) {
        return 16 + paramAtomContainer.getSize();
    }

    /**
     * This constructor makes a new empty QTVRSampleDescription object.
     */
    public QTVRSampleDescription() throws QTException {
        super(16, true, 1903457906);
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();
        EndianFlipSpec localEndianFlipSpec = new EndianFlipSpec(16, 4, 1);
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

