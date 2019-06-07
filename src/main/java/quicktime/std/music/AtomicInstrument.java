package quicktime.std.music;

import java.security.AccessController;
import java.security.PrivilegedAction;
import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.AtomContainer;
import quicktime.util.QTUtils;

/**
 * Corresponds to the AtomicInstrument type and its accompanying calls
 */
public final class AtomicInstrument extends AtomContainer implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(AtomicInstrument.class);
                    }
                });
            }
        }.establish();
    }

    int res = 0;

    /**
     * Create a new disposable and empty AtomContainer.
     * Once the application has filled in the container with the appropriate
     * music data the AtomicInstrument can be used. It is the application's responsibility
     * to fill in this data appropriately using the methods of the AtomContainer super class.
     * QuickTime::QTNewAtomContainer
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
     * QuickTime::NAPickEditInstrument
     * @param na an open Note Allocator component.
     * @param prompt Dialog box prompt "New Instrument..."
     * @param flags flags to limit user options, use zero for default.
     * @param {@link quicktime.std.StdQTException}
     */
    public void pickEditInstrument(NoteAllocator paramNoteAllocator, String paramString, int paramInt) throws StdQTException {
        final String str = paramString;
        final NoteAllocator localNoteAllocator = paramNoteAllocator;
        final int i = paramInt;
        final int j = _ID();
        int k = 0;
        if (QTSession.isCurrentOS(4)) {
            System.out.println("New NAPickEditInstrument");
            k = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                public Object invoke() {
                    int l = 0;
                    l = AtomicInstrument.NAPickEditInstrument(QTObject.ID(localNoteAllocator), 0, QTUtils.String2PString(str, 255), 0, 0, j, i);
                    return new Integer(l);
                }
            })).intValue();
            StdQTException.checkError(k);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NAPickEditInstrument(QTObject.ID(paramNoteAllocator), 0, QTUtils.String2PString(paramString, 255), 0, 0, _ID(), paramInt));
            }
        }
    }

    /**
     * Make a new note channel for an atomic instrument.
     * QuickTime::NANewNoteChannelFromAtomicInstrument
     * @param flags specify details of the new note channel
     * @return a new note channel.
     * @return {@link quicktime.QTException}
     */
    public NoteChannel newNoteChannel(int paramInt) throws QTException {
        return newNoteChannel(NoteAllocator.getDefault(), paramInt);
    }

    /**
     * Make a new note channel for an atomic instrument.
     * QuickTime::NANewNoteChannelFromAtomicInstrument
     * @param na an open Note Allocator component.
     * @param flags specify details of the new note channel
     * @return a new note channel.
     * @return {@link quicktime.std.StdQTException}
     */
    public NoteChannel newNoteChannel(NoteAllocator paramNoteAllocator, int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = NANewNoteChannelFromAtomicInstrument(QTObject.ID(paramNoteAllocator), lockAndDeref(0), paramInt, arrayOfInt);
        if (!isLocked())
            unlock();
        StdQTException.checkError(i);
        return new NoteChannel(paramNoteAllocator, arrayOfInt[0], null);
    }

    int getPtr() {
        return lockAndDeref(0);
    }

    private static native int NAPickEditInstrument(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

    private static native int NANewNoteChannelFromAtomicInstrument(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short QTNewAtomContainer(int[] paramArrayOfInt);
}
