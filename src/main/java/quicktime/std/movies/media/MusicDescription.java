package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.std.StdQTException;
import quicktime.std.music.MusicData;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;


/**
 * This class maps the MusicDescription record of the MovieToolbox.
 * MusicDescriptions are used when adding and getting media samples to/from Music tracks.
 */
public final class MusicDescription extends SampleDescription implements InterfaceLib, PrimitivesLib {

    private static final int kNativeSize = 20;

    private static EndianDescriptor ed;

    protected static EndianDescriptor makeED() {
        EndianDescriptor ed = SampleDescription.makeED();
        EndianFlipSpec spec = new EndianFlipSpec(16, 4, 1);
        ed.addFlipSpec(spec);
        return ed;
    }

    /**
     * Returns the EndianDescriptor for this class.
     *
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * This constructor makes a new MusicDescription object.
     */
    public MusicDescription() throws QTException {
        super(kNativeSize, true, 1836413801);
    }

    private MusicDescription(int nativeRep) {
        super(nativeRep, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new MusicDescription(makeAndCopyHandle());
    }

    public void setMusicFlags(int musicFlags) {
        setIntAt(16, musicFlags);
    }

    public int getMusicFlags() {
        return getIntAt(16);
    }

    /**
     * Returns a copy of the TuneHeader MusicHeader data
     */
    public MusicData getTuneHeader() throws QTException {
        if (getSize() == 20) {
            return null;
        }
        int i = getSize() - 20;
        MusicData tuneHeader = new MusicData(i);
        tuneHeader.lock();
        int j = getIntFromPointer(QTObject.ID(tuneHeader), 0);
        BlockMove(lockAndDeref(20), j, i);
        if (!isLocked())
            unlock();
        tuneHeader.unlock();
        return tuneHeader;
    }

    /**
     * Sets the MusicHeader for the TunePlayer or MusicMedia insertion.
     * This will overwrite any preceeding tune header that the description contained.
     */
    public void setTuneHeader(MusicData tuneHeader) throws StdQTException {
        int i = tuneHeader.getSize();
        setSize(20 + i);
        tuneHeader.lock();
        int j = getIntFromPointer(QTObject.ID(tuneHeader), 0);
        BlockMove(j, lockAndDeref(20), i);
        if (!isLocked())
            unlock();
        tuneHeader.unlock();
    }

    private static void BlockMove(int src, int dst, int length) {
        MemoryManager.BlockMove(src, dst, length);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }
}
