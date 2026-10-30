package quicktime.std.music;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import com.apple.mrj.macos.carbon.CarbonAccess;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.AtomManager;
import quicktime.internal.toolbox.MusicManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.AtomContainer;
import quicktime.util.QTUtils;


/**
 * Corresponds to the AtomicInstrument type and its accompanying calls
 */
public final class AtomicInstrument extends AtomContainer implements QuickTimeLib {

    private static final Logger logger = System.getLogger(AtomicInstrument.class.getName());

    int res = 0;

    /**
     * Create a new disposable and empty AtomContainer.
     * Once the application has filled in the container with the appropriate
     * music data the AtomicInstrument can be used. It is the application's responsibility
     * to fill in this data appropriately using the methods of the AtomContainer super class.
     * @see "QuickTime::QTNewAtomContainer"
     */
    public AtomicInstrument() throws QTException {
        super(allocate(), null, false);
    }

    AtomicInstrument(int nativeRep, Object ref) {
        super(nativeRep, ref, false);
    }

    private static int allocate() throws StdQTException {
        int[] container = { 0 };
        int i = QTNewAtomContainer(container);
        if (i != 0)
            throw new StdQTException(i);
        return container[0];
    }

    /**
     * Presents a user interface for modifying the AtomicInstrument.
     * @see "QuickTime::NAPickEditInstrument"
     * @param na an open Note Allocator component.
     * @param prompt Dialog box prompt "New Instrument..."
     * @param flags flags to limit user options, use zero for default.
     * @throws quicktime.std.StdQTException
     */
    public void pickEditInstrument(NoteAllocator na, String prompt, int flags) throws StdQTException {
        String str = prompt;
        NoteAllocator noteAllocator = na;
        int i = flags;
        int j = _ID();
        int k = 0;
        if (QTSession.isCurrentOS(4)) {
            logger.log(Level.DEBUG, "New NAPickEditInstrument");
            k = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int l = 0;
                l = AtomicInstrument.NAPickEditInstrument(QTObject.ID(noteAllocator), 0, QTUtils.String2PString(str, 255), 0, 0, j, i);
                return l;
            });
            StdQTException.checkError(k);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NAPickEditInstrument(QTObject.ID(na), 0, QTUtils.String2PString(prompt, 255), 0, 0, _ID(), flags));
            }
        }
    }

    /**
     * Make a new note channel for an atomic instrument.
     * @see "QuickTime::NANewNoteChannelFromAtomicInstrument"
     * @param flags specify details of the new note channel
     * @return a new note channel.
     * @throws quicktime.QTException
     */
    public NoteChannel newNoteChannel(int flags) throws QTException {
        return newNoteChannel(NoteAllocator.getDefault(), flags);
    }

    /**
     * Make a new note channel for an atomic instrument.
     * @see "QuickTime::NANewNoteChannelFromAtomicInstrument"
     * @param na an open Note Allocator component.
     * @param flags specify details of the new note channel
     * @return a new note channel.
     * @throws quicktime.std.StdQTException
     */
    public NoteChannel newNoteChannel(NoteAllocator na, int flags) throws StdQTException {
        int[] outChannel = new int[1];
        int i = NANewNoteChannelFromAtomicInstrument(QTObject.ID(na), lockAndDeref(0), flags, outChannel);
        if (!isLocked())
            unlock();
        StdQTException.checkError(i);
        return new NoteChannel(na, outChannel[0], null);
    }

    int getPtr() {
        return lockAndDeref(0);
    }

    private static int NAPickEditInstrument(int na, int filterProc, byte[] prompt, int refCon, int nc, int instrument, int modifiers) {
        return MusicManager.NAPickEditInstrument(na, filterProc, prompt, refCon, nc, instrument, modifiers);
    }

    private static int NANewNoteChannelFromAtomicInstrument(int na, int instrument, int flags, int[] outChannel) {
        return MusicManager.NANewNoteChannelFromAtomicInstrument(na, instrument, flags, outChannel);
    }

    private static short QTNewAtomContainer(int[] container) {
        return AtomManager.QTNewAtomContainer(container);
    }
}
