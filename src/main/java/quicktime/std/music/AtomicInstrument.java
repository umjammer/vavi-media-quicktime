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

    AtomicInstrument(int paramInt, Object paramObject) {
        super(paramInt, paramObject, false);
    }

    private static int allocate() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = QTNewAtomContainer(arrayOfInt);
        if (i != 0)
            throw new StdQTException(i);
        return arrayOfInt[0];
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
        NoteAllocator localNoteAllocator = na;
        int i = flags;
        int j = _ID();
        int k = 0;
        if (QTSession.isCurrentOS(4)) {
            logger.log(Level.DEBUG, "New NAPickEditInstrument");
            k = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int l = 0;
                l = AtomicInstrument.NAPickEditInstrument(QTObject.ID(localNoteAllocator), 0, QTUtils.String2PString(str, 255), 0, 0, j, i);
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
        int[] arrayOfInt = new int[1];
        int i = NANewNoteChannelFromAtomicInstrument(QTObject.ID(na), lockAndDeref(0), flags, arrayOfInt);
        if (!isLocked())
            unlock();
        StdQTException.checkError(i);
        return new NoteChannel(na, arrayOfInt[0], null);
    }

    int getPtr() {
        return lockAndDeref(0);
    }

    private static int NAPickEditInstrument(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        return MusicManager.NAPickEditInstrument(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4, paramInt5, paramInt6);
    }

    private static int NANewNoteChannelFromAtomicInstrument(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return MusicManager.NANewNoteChannelFromAtomicInstrument(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static short QTNewAtomContainer(int[] paramArrayOfInt) {
        return AtomManager.QTNewAtomContainer(paramArrayOfInt);
    }
}
