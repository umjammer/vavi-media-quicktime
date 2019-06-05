
package quicktime.std.music;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public final class NoteChannel extends QTObject implements QuickTimeLib {
    private static Object linkage;

    public static final float kMicrotonalResolution = 0.0078125F;

    public static final float kMicroControllerResolution = 0.0078125F;

    private NoteAllocator na;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(NoteChannel.class);
                    }
                });
            }
        }.establish();
    }

    private static int allocate(NoteAllocator paramNoteAllocator, NoteRequest paramNoteRequest) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NANewNoteChannel(QTObject.ID(paramNoteAllocator), paramNoteRequest.getBytes(), arrayOfInt));

        return arrayOfInt[0];
    }

    private static int allocate(NoteAllocator paramNoteAllocator, int paramInt) throws QTException {
        ToneDescription localToneDescription = new ToneDescription(paramNoteAllocator, paramInt);
        NoteRequest localNoteRequest = new NoteRequest(localToneDescription);
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NANewNoteChannel(QTObject.ID(paramNoteAllocator), localNoteRequest.getBytes(), arrayOfInt));

        return arrayOfInt[0];
    }

    public NoteChannel(int paramInt1, int paramInt2) throws QTException {
        this(new NoteRequest(paramInt1, paramInt2));
    }

    public NoteChannel(NoteRequest paramNoteRequest) throws QTException {
        this(NoteAllocator.getDefault(), paramNoteRequest);
    }

    public NoteChannel(NoteAllocator paramNoteAllocator, NoteRequest paramNoteRequest) throws QTException {
        super(allocate(paramNoteAllocator, paramNoteRequest), QTObject.ID(paramNoteAllocator));
        this.na = paramNoteAllocator;
    }

    public NoteChannel(NoteAllocator paramNoteAllocator, int paramInt) throws QTException {
        super(allocate(paramNoteAllocator, paramInt), QTObject.ID(paramNoteAllocator));
        this.na = paramNoteAllocator;
    }

    NoteChannel(NoteAllocator paramNoteAllocator, int paramInt, Object paramObject) {
        super(paramInt, paramObject);
        this.na = paramNoteAllocator;
    }

    public final NoteAllocator getNoteAllocator() {
        return this.na;
    }

    public final int getIndexInfo() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(NAGetNoteChannelInfo(QTObject.ID(this.na), _ID(), arrayOfInt1, arrayOfInt2));
        return arrayOfInt1[0];
    }

    public final int getPartInfo() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(NAGetNoteChannelInfo(QTObject.ID(this.na), _ID(), arrayOfInt1, arrayOfInt2));
        return arrayOfInt2[0];
    }

    public MusicComponent getMusicComponent() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = NAGetRegisteredMusicDevice(QTObject.ID(this.na), getIndexInfo(), 0, 0, 0, arrayOfInt);
        if (i < 0)
            StdQTException.checkError(i);
        return new MusicComponent(arrayOfInt[0], this);
    }

    public final void preroll() throws StdQTException {
        StdQTException.checkError(NAPrerollNoteChannel(QTObject.ID(this.na), _ID()));
    }

    public final void unroll() throws StdQTException {
        StdQTException.checkError(NAUnrollNoteChannel(QTObject.ID(this.na), _ID()));
    }

    public final void reset() throws StdQTException {
        StdQTException.checkError(NAResetNoteChannel(QTObject.ID(this.na), _ID()));
    }

    public final void setVolume(float paramFloat) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelVolume(QTObject.ID(this.na), _ID(), QTUtils.X2Fix(paramFloat)));
    }

    public final void setBalance(int paramInt) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelBalance(QTObject.ID(this.na), _ID(), paramInt));
    }

    public final void playNoteRaw(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(NAPlayNote(QTObject.ID(this.na), _ID(), paramInt1, paramInt2));
    }

    public final void playNoteCents(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        paramInt1 <<= 8;
        paramInt1 += (int) (1.28F * paramInt2 + 0.5F);
        playNoteRaw(paramInt1, paramInt3);
    }

    public final void playNote(float paramFloat, int paramInt) throws StdQTException {
        StdQTException.checkError(NAPlayNote(QTObject.ID(this.na), _ID(), QTUtils.X2ShortFix(paramFloat), paramInt));
    }

    public final float getController(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(NAGetController(QTObject.ID(this.na), _ID(), paramInt, arrayOfInt));
        return QTUtils.ShortFix2X((short) arrayOfInt[0]);
    }

    public final void setControllerRaw(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(NASetController(QTObject.ID(this.na), _ID(), paramInt1, paramInt2));
    }

    public final void setController(int paramInt, float paramFloat) throws StdQTException {
        StdQTException.checkError(NASetController(QTObject.ID(this.na), _ID(), paramInt, QTUtils.X2ShortFix(paramFloat)));
    }

    public final int getKnob(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAGetKnob(QTObject.ID(this.na), _ID(), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    public final void setKnob(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(NASetKnob(QTObject.ID(this.na), _ID(), paramInt1, paramInt2));
    }

    public final int findTone(ToneDescription paramToneDescription) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException
                .checkError(NAFindNoteChannelTone(QTObject.ID(this.na), _ID(), paramToneDescription.getBytes(), arrayOfInt));

        return arrayOfInt[0];
    }

    public final void setAtomicInstrument(AtomicInstrument paramAtomicInstrument, int paramInt) throws StdQTException {
        QTPointerRef localQTPointerRef = paramAtomicInstrument.toQTPointer();
        int i = NASetAtomicInstrument(QTObject.ID(this.na), _ID(), QTObject.ID(localQTPointerRef), paramInt);
        if (!paramAtomicInstrument.isLocked())
            paramAtomicInstrument.unlock();
        StdQTException.checkError(i);
    }

    public final void setInstrumentNumber(int paramInt) throws StdQTException {
        StdQTException.checkError(NASetInstrumentNumber(QTObject.ID(this.na), _ID(), paramInt));
    }

    public final NoteRequest getNoteRequest() throws QTException {
        NoteRequest localNoteRequest = new NoteRequest();
        StdQTException.checkError(NAGetNoteRequest(QTObject.ID(this.na), _ID(), localNoteRequest.getBytes()));

        return localNoteRequest;
    }

    public final void pickEditInstrument(final String prompt, final int flags) throws StdQTException {
        int k = 0;
        if (QTSession.isCurrentOS(4)) {
            k = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    int i = 0;
                    synchronized (QTNative.globalsLock) {
                        i = NoteChannel.NAPickEditInstrument(QTObject.ID(NoteChannel.this.na),
                                                             0,
                                                             QTUtils.String2PString(prompt, 255),
                                                             0,
                                                             _ID(),
                                                             0,
                                                             flags);
                        return new Integer(i);
                    }
                }
            })).intValue();
            StdQTException.checkError(k);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NAPickEditInstrument(QTObject.ID(this.na),
                                                               0,
                                                               QTUtils.String2PString(prompt, 255),
                                                               0,
                                                               _ID(),
                                                               0,
                                                               flags));
            }
        }
    }

    public final void sendMIDI(MusicMIDIPacket paramMusicMIDIPacket) throws StdQTException {
        int i = NASendMIDI(QTObject.ID(this.na), _ID(), paramMusicMIDIPacket.getBytes());
        StdQTException.checkError(i);
    }

    public final void setSoundLocalization(SoundLocalization paramSoundLocalization) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelSoundLocalization(QTObject.ID(this.na),
                                                                    _ID(),
                                                                    QTObject.ID(paramSoundLocalization)));
    }

    private static native int NANewNoteChannel(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int NAGetNoteChannelInfo(int paramInt1,
                                                   int paramInt2,
                                                   int[] paramArrayOfInt1,
                                                   int[] paramArrayOfInt2);

    private static native int NAPrerollNoteChannel(int paramInt1, int paramInt2);

    private static native int NAUnrollNoteChannel(int paramInt1, int paramInt2);

    private static native int NAResetNoteChannel(int paramInt1, int paramInt2);

    private static native int NASetNoteChannelVolume(int paramInt1, int paramInt2, int paramInt3);

    private static native int NAPlayNote(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NAGetController(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int NASetController(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NAGetKnob(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int NASetKnob(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NAFindNoteChannelTone(int paramInt1,
                                                    int paramInt2,
                                                    byte[] paramArrayOfByte,
                                                    int[] paramArrayOfInt);

    private static native int NASetAtomicInstrument(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NASetInstrumentNumber(int paramInt1, int paramInt2, int paramInt3);

    private static native int NAGetNoteRequest(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int NAPickEditInstrument(int paramInt1,
                                                   int paramInt2,
                                                   byte[] paramArrayOfByte,
                                                   int paramInt3,
                                                   int paramInt4,
                                                   int paramInt5,
                                                   int paramInt6);

    private static native int NASendMIDI(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int NASetNoteChannelSoundLocalization(int paramInt1, int paramInt2, int paramInt3);

    private static native int NASetNoteChannelBalance(int paramInt1, int paramInt2, int paramInt3);

    private static native int NAGetRegisteredMusicDevice(int paramInt1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         int paramInt4,
                                                         int paramInt5,
                                                         int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /NoteChannel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
