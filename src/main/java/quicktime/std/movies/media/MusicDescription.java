package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.StdQTException;
import quicktime.std.music.MusicData;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

/**
 * This class maps the MusicDescription record of the MovieToolbox.
 * MusicDescriptions are used when adding and getting media samples to/from Music tracks.
 */
public final class MusicDescription extends SampleDescription implements InterfaceLib, PrimitivesLib {

    private static Object linkage;

    private static final int kNativeSize = 20;

    private static EndianDescriptor ed;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(MusicDescription.class));
            }
        }.establish();
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

    /**
     * This constructor makes a new MusicDescription object.
     */
    public MusicDescription() throws QTException {
        super(kNativeSize, true, 1836413801);
    }

    private MusicDescription(int paramInt) {
        super(paramInt, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new MusicDescription(makeAndCopyHandle());
    }

    public void setMusicFlags(int paramInt) {
        setIntAt(16, paramInt);
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
        MusicData localMusicData = new MusicData(i);
        localMusicData.lock();
        int j = getIntFromPointer(QTObject.ID(localMusicData), 0);
        BlockMove(lockAndDeref(20), j, i);
        if (!isLocked())
            unlock();
        localMusicData.unlock();
        return localMusicData;
    }

    /**
     * Sets the MusicHeader for the TunePlayer or MusicMedia insertion.
     * This will overwrite any preceeding tune header that the description contained.
     */
    public void setTuneHeader(MusicData paramMusicData) throws StdQTException {
        int i = paramMusicData.getSize();
        setSize(20 + i);
        paramMusicData.lock();
        int j = getIntFromPointer(QTObject.ID(paramMusicData), 0);
        BlockMove(j, lockAndDeref(20), i);
        if (!isLocked())
            unlock();
        paramMusicData.unlock();
    }

    private static void BlockMove(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.BlockMove(paramInt1, paramInt2, paramInt3);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }
}

