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

/**
 * Note channels are used to play notes. The application
 * can either provide the NoteAllocator component for the NoteChanel
 * or can allow the system to create a default one.
 */
public final class NoteChannel extends QTObject implements QuickTimeLib {

    private static Object linkage;

    /**
     * The smallest microtone is a semitone / 128. This constant expresses that value (1/128).
     */
    public static final float kMicrotonalResolution = 0.0078125F;

    /**
     * The smallest step that will effect a change in controller resolution
     */
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

    /**
     * Creates a new NoteChannel object with the request instrument and polyphony.
     * This constructor will use the system created NoteAllocator, which can
     * be retrieved if required, through the NoteChannel.getNoteAllocator() method
     * once the NoteChannel is created.
     * QuickTime::NANewNoteChannel
     */
    public NoteChannel(int paramInt1, int paramInt2) throws QTException {
        this(new NoteRequest(paramInt1, paramInt2));
    }

    /**
     * Creates a new NoteChannel object using the default NoteAllocator
     * QuickTime::NANewNoteChannel
     * @param nr attributes of note request
     */
    public NoteChannel(NoteRequest paramNoteRequest) throws QTException {
        this(NoteAllocator.getDefault(), paramNoteRequest);
    }

    /**
     * Creates a new NoteChannel object.
     * QuickTime::NANewNoteChannel
     * @param na the NoteAllocator component instance to use
     * @param nr attributes of note request
     */
    public NoteChannel(NoteAllocator paramNoteAllocator, NoteRequest paramNoteRequest) throws QTException {
        super(allocate(paramNoteAllocator, paramNoteRequest), QTObject.ID(paramNoteAllocator));
        this.na = paramNoteAllocator;
    }

    /**
     * Creates a new NoteChannel object.
     * QuickTime::NANewNoteChannel
     * QuickTime::NAStuffToneDescription
     * @param na the NoteAllocator component instance to use
     * @param gmNumber the general MIDI number for the desired instrument
     */
    public NoteChannel(NoteAllocator paramNoteAllocator, int paramInt) throws QTException {
        super(allocate(paramNoteAllocator, paramInt), QTObject.ID(paramNoteAllocator));
        this.na = paramNoteAllocator;
    }

    NoteChannel(NoteAllocator paramNoteAllocator, int paramInt, Object paramObject) {
        super(paramInt, paramObject);
        this.na = paramNoteAllocator;
    }

    /**
     * Returns the NoteAllocator that owns this NoteChannel
     */
    public final NoteAllocator getNoteAllocator() {
        return this.na;
    }

    /**
     * Returns the index of the NoteAllocator of the MusicComponent that is used by this NoteChannel.
     * QuickTime::NAGetNoteChannelInfo
     * @return index
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getIndexInfo() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(NAGetNoteChannelInfo(QTObject.ID(this.na), _ID(), arrayOfInt1, arrayOfInt2));
        return arrayOfInt1[0];
    }

    /**
     * Returns the part number within the NoteChannel's MusicComponent that corresponds to this channel.
     * QuickTime::NAGetNoteChannelInfo
     * @return part number
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getPartInfo() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(NAGetNoteChannelInfo(QTObject.ID(this.na), _ID(), arrayOfInt1, arrayOfInt2));
        return arrayOfInt2[0];
    }

    /**
     * Returns the Music component in use by the note channel.
     * QuickTime::NAGetNoteChannelInfo
     * QuickTime::NAGetRegisteredMusicDevice
     * @return index
     * @return {@link quicktime.std.StdQTException}
     */
    public MusicComponent getMusicComponent() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = NAGetRegisteredMusicDevice(QTObject.ID(this.na), getIndexInfo(), 0, 0, 0, arrayOfInt);
        if (i < 0)
            StdQTException.checkError(i);
        return new MusicComponent(arrayOfInt[0], this);
    }

    /**
     * Will attempt to re-allocate the note channel if it was invalid previously.
     * QuickTime::NAPrerollNoteChannel
     */
    public final void preroll() throws StdQTException {
        StdQTException.checkError(NAPrerollNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Marks a note channel as available to be stolen.
     * QuickTime::NAUnrollNoteChannel
     */
    public final void unroll() throws StdQTException {
        StdQTException.checkError(NAUnrollNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Turns off all currently sounding notes, and resets all controllers to their
     * default values.
     * QuickTime::NAResetNoteChannel
     */
    public final void reset() throws StdQTException {
        StdQTException.checkError(NAResetNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Sets the volume on the note channel.
     * QuickTime::NASetNoteChannelVolume
     * @param volume volume value
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setVolume(float paramFloat) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelVolume(QTObject.ID(this.na), _ID(), QTUtils.X2Fix(paramFloat)));
    }

    /**
     * Modifies the pan controller setting for the note channel.
     * QuickTime::NASetNoteChannelBalance
     * @param balance pan value (-128 left, to 128 right)
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setBalance(int paramInt) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelBalance(QTObject.ID(this.na), _ID(), paramInt));
    }

    /**
     * Plays a musical note with given pitch and velocity.
     * QuickTime::NAPlayNote
     * @param pitch MIDI pitch values (0 to 127, 60 is middle C)
     * @param velocity velocity of note, where 0 = silence - note off.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void playNoteRaw(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(NAPlayNote(QTObject.ID(this.na), _ID(), paramInt1, paramInt2));
    }

    /**
     * Plays a musical note with given pitch and velocity.
     * The microtone resolution is 128 steps (kMicrotonalResolution) per semitone.
     * QuickTime::NAPlayNote
     * @param noteNumber 0 -&gt; 127 correspond to MIDI note numbers (60 is middle C).
     * @param cents 0 -&gt; 100 correspond to the division of a semitone into 100 logarithmically equal segments.
     * @param velocity velocity of note, where 0 = silence - note off.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void playNoteCents(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        paramInt1 <<= 8;
        paramInt1 += (int) (1.28F * paramInt2 + 0.5F);
        playNoteRaw(paramInt1, paramInt3);
    }

    /**
     * Plays a musical note with given pitch and velocity.
     * The microtone resolution is 128 steps (kMicrotonalResolution) per semitone.
     * QuickTime::NAPlayNote
     * @param pitch 1.000 -&gt; 127.999 correspond to MIDI note numbers (60 is middle C).
     * @param velocity velocity of note, where 0 = silence - note off.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void playNote(float paramFloat, int paramInt) throws StdQTException {
        StdQTException.checkError(NAPlayNote(QTObject.ID(this.na), _ID(), QTUtils.X2ShortFix(paramFloat), paramInt));
    }

    /**
     * Retrieves the current value for the specified controller
     * The micro controller resolution is 128 steps (kMicroControllerResolution) per semitone.
     * QuickTime::NAGetController
     * @param controllerNumber which controller
     * @param {@link quicktime.std.StdQTException}
     */
    public final float getController(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(NAGetController(QTObject.ID(this.na), _ID(), paramInt, arrayOfInt));
        return QTUtils.ShortFix2X((short) arrayOfInt[0]);
    }

    /**
     * Changes the specified controller to the specified value.
     * QuickTime::NASetController
     * @param controllerNumber which controller
     * @param controllerValue the new controller value,
     * 			dealing with raw controller values, which have the full range of a short (-32768 to 32767)
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setControllerRaw(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(NASetController(QTObject.ID(this.na), _ID(), paramInt1, paramInt2));
    }

    /**
     * Changes the specified controller to the specified value.
     * The micro controller resolution is 128 steps (kMicroControllerResolution) per semitone.
     * QuickTime::NASetController
     * @param controllerNumber which controller
     * @param controllerValue the new controller value, range -128.000 to 127.999
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setController(int paramInt, float paramFloat) throws StdQTException {
        StdQTException.checkError(NASetController(QTObject.ID(this.na), _ID(), paramInt, QTUtils.X2ShortFix(paramFloat)));
    }

    /**
     * Returns the value of a knob for the note channel.
     * QuickTime::NAGetKnob
     * @param knobNumber index or ID of the knob
     * @return the value of the knob
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getKnob(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAGetKnob(QTObject.ID(this.na), _ID(), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Sets the value of a knob for the note channel.
     * QuickTime::NASetKnob
     * @param knobNumber index or ID of the knob
     * @param knobValue the value of the knob
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setKnob(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(NASetKnob(QTObject.ID(this.na), _ID(), paramInt1, paramInt2));
    }

    /**
     * Locate the best fitting instrument number for the given ToneDescription.
     * QuickTime::NAFindNoteChannelTone
     * @param td description for instrument fit
     * @return the instrument index of fit
     * @return {@link quicktime.std.StdQTException}
     */
    public final int findTone(ToneDescription paramToneDescription) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAFindNoteChannelTone(QTObject.ID(this.na), _ID(), paramToneDescription.getBytes(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Initialises a note channel with an atomic instrument.
     * QuickTime::NASetAtomicInstrument
     * @param instrument the AtomicInstrument to set the channel to.
     * @param flags use the set flags from Atomic instrument.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setAtomicInstrument(AtomicInstrument paramAtomicInstrument, int paramInt) throws StdQTException {
        QTPointerRef localQTPointerRef = paramAtomicInstrument.toQTPointer();
        int i = NASetAtomicInstrument(QTObject.ID(this.na), _ID(), QTObject.ID(localQTPointerRef), paramInt);
        if (!paramAtomicInstrument.isLocked())
            paramAtomicInstrument.unlock();
        StdQTException.checkError(i);
    }

    /**
     * Change the instrument setting to the specified instrument number.
     * QuickTime::NASetInstrumentNumber
     * @param instrumentNumber instrument number to apply
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setInstrumentNumber(int paramInt) throws StdQTException {
        StdQTException.checkError(NASetInstrumentNumber(QTObject.ID(this.na), _ID(), paramInt));
    }

    /**
     * Get the NoteRequest that corresponds to this NoteChannel.
     * QuickTime::NAGetNoteRequest
     * @return a new NoteRequest class with the parameters of this NoteChannel
     * @return {@link quicktime.QTException}
     */
    public final NoteRequest getNoteRequest() throws QTException {
        NoteRequest localNoteRequest = new NoteRequest();
        StdQTException.checkError(NAGetNoteRequest(QTObject.ID(this.na), _ID(), localNoteRequest.getBytes()));
        return localNoteRequest;
    }

    /**
     * Presents a user interface for changing the instrument used by the note channel.
     * QuickTime::NAPickEditInstrument
     * @param prompt Dialog box prompt "New Instrument..."
     * @param flags flags to limit user options
     * @param {@link quicktime.std.StdQTException}
     */
    public final void pickEditInstrument(final String prompt, final int flags) throws StdQTException {
        int k = 0;
        if (QTSession.isCurrentOS(4)) {
            k = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                public Object invoke() {
                    int i = 0;
                    synchronized (QTNative.globalsLock) {
                        i = NoteChannel.NAPickEditInstrument(QTObject.ID(NoteChannel.this.na), 0, QTUtils.String2PString(prompt, 255), 0, _ID(), 0, flags);
                        return new Integer(i);
                    }
                }
            })).intValue();
            StdQTException.checkError(k);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NAPickEditInstrument(QTObject.ID(this.na), 0, QTUtils.String2PString(prompt, 255), 0, _ID(), 0, flags));
            }
        }
    }

    /**
     * Get the NoteRequest that corresponds to this NoteChannel.
     * QuickTime::NASendMIDI
     */
    public final void sendMIDI(MusicMIDIPacket paramMusicMIDIPacket) throws StdQTException {
        int i = NASendMIDI(QTObject.ID(this.na), _ID(), paramMusicMIDIPacket.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * Passes sound localization data to a note channel.
     * QuickTime::NASetNoteChannelSoundLocalization
     * @param slData sound localization data
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setSoundLocalization(SoundLocalization paramSoundLocalization) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelSoundLocalization(QTObject.ID(this.na), _ID(), QTObject.ID(paramSoundLocalization)));
    }

    private static native int NANewNoteChannel(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int NAGetNoteChannelInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int NAPrerollNoteChannel(int paramInt1, int paramInt2);

    private static native int NAUnrollNoteChannel(int paramInt1, int paramInt2);

    private static native int NAResetNoteChannel(int paramInt1, int paramInt2);

    private static native int NASetNoteChannelVolume(int paramInt1, int paramInt2, int paramInt3);

    private static native int NAPlayNote(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NAGetController(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int NASetController(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NAGetKnob(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int NASetKnob(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NAFindNoteChannelTone(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int NASetAtomicInstrument(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NASetInstrumentNumber(int paramInt1, int paramInt2, int paramInt3);

    private static native int NAGetNoteRequest(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int NAPickEditInstrument(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

    private static native int NASendMIDI(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int NASetNoteChannelSoundLocalization(int paramInt1, int paramInt2, int paramInt3);

    private static native int NASetNoteChannelBalance(int paramInt1, int paramInt2, int paramInt3);

    private static native int NAGetRegisteredMusicDevice(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /NoteChannel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
