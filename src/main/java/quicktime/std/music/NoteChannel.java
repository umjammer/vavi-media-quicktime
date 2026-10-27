package quicktime.std.music;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MusicManager;
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

    /**
     * The smallest microtone is a semitone / 128. This constant expresses that value (1/128).
     */
    public static final float kMicrotonalResolution = 0.0078125F;

    /**
     * The smallest step that will effect a change in controller resolution
     */
    public static final float kMicroControllerResolution = 0.0078125F;

    private final NoteAllocator na;

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
     * @see "QuickTime::NANewNoteChannel"
     */
    public NoteChannel(int paramInt1, int paramInt2) throws QTException {
        this(new NoteRequest(paramInt1, paramInt2));
    }

    /**
     * Creates a new NoteChannel object using the default NoteAllocator
     * @see "QuickTime::NANewNoteChannel"
     * @param nr attributes of note request
     */
    public NoteChannel(NoteRequest nr) throws QTException {
        this(NoteAllocator.getDefault(), nr);
    }

    /**
     * Creates a new NoteChannel object.
     * @see "QuickTime::NANewNoteChannel"
     * @param na the NoteAllocator component instance to use
     * @param nr attributes of note request
     */
    public NoteChannel(NoteAllocator na, NoteRequest nr) throws QTException {
        super(allocate(na, nr), QTObject.ID(na));
        this.na = na;
    }

    /**
     * Creates a new NoteChannel object.
     * @see "QuickTime::NANewNoteChannel"
     * @see "QuickTime::NAStuffToneDescription"
     * @param na the NoteAllocator component instance to use
     * @param gmNumber the general MIDI number for the desired instrument
     */
    public NoteChannel(NoteAllocator na, int gmNumber) throws QTException {
        super(allocate(na, gmNumber), QTObject.ID(na));
        this.na = na;
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
     * @see "QuickTime::NAGetNoteChannelInfo"
     * @return index
     * @throws quicktime.std.StdQTException
     */
    public final int getIndexInfo() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(NAGetNoteChannelInfo(QTObject.ID(this.na), _ID(), arrayOfInt1, arrayOfInt2));
        return arrayOfInt1[0];
    }

    /**
     * Returns the part number within the NoteChannel's MusicComponent that corresponds to this channel.
     * @see "QuickTime::NAGetNoteChannelInfo"
     * @return part number
     * @throws quicktime.std.StdQTException
     */
    public final int getPartInfo() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(NAGetNoteChannelInfo(QTObject.ID(this.na), _ID(), arrayOfInt1, arrayOfInt2));
        return arrayOfInt2[0];
    }

    /**
     * Returns the Music component in use by the note channel.
     * @see "QuickTime::NAGetNoteChannelInfo"
     * @see "QuickTime::NAGetRegisteredMusicDevice"
     * @return index
     * @throws quicktime.std.StdQTException
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
     * @see "QuickTime::NAPrerollNoteChannel"
     */
    public final void preroll() throws StdQTException {
        StdQTException.checkError(NAPrerollNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Marks a note channel as available to be stolen.
     * @see "QuickTime::NAUnrollNoteChannel"
     */
    public final void unroll() throws StdQTException {
        StdQTException.checkError(NAUnrollNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Turns off all currently sounding notes, and resets all controllers to their
     * default values.
     * @see "QuickTime::NAResetNoteChannel"
     */
    public final void reset() throws StdQTException {
        StdQTException.checkError(NAResetNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Sets the volume on the note channel.
     * @see "QuickTime::NASetNoteChannelVolume"
     * @param volume volume value
     * @throws quicktime.std.StdQTException
     */
    public final void setVolume(float volume) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelVolume(QTObject.ID(this.na), _ID(), QTUtils.X2Fix(volume)));
    }

    /**
     * Modifies the pan controller setting for the note channel.
     * @see "QuickTime::NASetNoteChannelBalance"
     * @param balance pan value (-128 left, to 128 right)
     * @throws quicktime.std.StdQTException
     */
    public final void setBalance(int balance) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelBalance(QTObject.ID(this.na), _ID(), balance));
    }

    /**
     * Plays a musical note with given pitch and velocity.
     * @see "QuickTime::NAPlayNote"
     * @param pitch MIDI pitch values (0 to 127, 60 is middle C)
     * @param velocity velocity of note, where 0 = silence - note off.
     * @throws quicktime.std.StdQTException
     */
    public final void playNoteRaw(int pitch, int velocity) throws StdQTException {
        StdQTException.checkError(NAPlayNote(QTObject.ID(this.na), _ID(), pitch, velocity));
    }

    /**
     * Plays a musical note with given pitch and velocity.
     * The microtone resolution is 128 steps (kMicrotonalResolution) per semitone.
     * @see "QuickTime::NAPlayNote"
     * @param noteNumber 0 -&gt; 127 correspond to MIDI note numbers (60 is middle C).
     * @param cents 0 -&gt; 100 correspond to the division of a semitone into 100 logarithmically equal segments.
     * @param velocity velocity of note, where 0 = silence - note off.
     * @throws quicktime.std.StdQTException
     */
    public final void playNoteCents(int noteNumber, int cents, int velocity) throws StdQTException {
        noteNumber <<= 8;
        noteNumber += (int) (1.28F * cents + 0.5F);
        playNoteRaw(noteNumber, velocity);
    }

    /**
     * Plays a musical note with given pitch and velocity.
     * The microtone resolution is 128 steps (kMicrotonalResolution) per semitone.
     * @see "QuickTime::NAPlayNote"
     * @param pitch 1.000 -&gt; 127.999 correspond to MIDI note numbers (60 is middle C).
     * @param velocity velocity of note, where 0 = silence - note off.
     * @throws quicktime.std.StdQTException
     */
    public final void playNote(float pitch, int velocity) throws StdQTException {
        StdQTException.checkError(NAPlayNote(QTObject.ID(this.na), _ID(), QTUtils.X2ShortFix(pitch), velocity));
    }

    /**
     * Retrieves the current value for the specified controller
     * The micro controller resolution is 128 steps (kMicroControllerResolution) per semitone.
     * @see "QuickTime::NAGetController"
     * @param controllerNumber which controller
     * @throws quicktime.std.StdQTException
     */
    public final float getController(int controllerNumber) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(NAGetController(QTObject.ID(this.na), _ID(), controllerNumber, arrayOfInt));
        return QTUtils.ShortFix2X((short) arrayOfInt[0]);
    }

    /**
     * Changes the specified controller to the specified value.
     * @see "QuickTime::NASetController"
     * @param controllerNumber which controller
     * @param controllerValue the new controller value,
     *    dealing with raw controller values, which have the full range of a short (-32768 to 32767)
     * @throws quicktime.std.StdQTException
     */
    public final void setControllerRaw(int controllerNumber, int controllerValue) throws StdQTException {
        StdQTException.checkError(NASetController(QTObject.ID(this.na), _ID(), controllerNumber, controllerValue));
    }

    /**
     * Changes the specified controller to the specified value.
     * The micro controller resolution is 128 steps (kMicroControllerResolution) per semitone.
     * @see "QuickTime::NASetController"
     * @param controllerNumber which controller
     * @param controllerValue the new controller value, range -128.000 to 127.999
     * @throws quicktime.std.StdQTException
     */
    public final void setController(int controllerNumber, float controllerValue) throws StdQTException {
        StdQTException.checkError(NASetController(QTObject.ID(this.na), _ID(), controllerNumber, QTUtils.X2ShortFix(controllerValue)));
    }

    /**
     * Returns the value of a knob for the note channel.
     * @see "QuickTime::NAGetKnob"
     * @param knobNumber index or ID of the knob
     * @return the value of the knob
     * @throws quicktime.std.StdQTException
     */
    public final int getKnob(int knobNumber) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAGetKnob(QTObject.ID(this.na), _ID(), knobNumber, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Sets the value of a knob for the note channel.
     * @see "QuickTime::NASetKnob"
     * @param knobNumber index or ID of the knob
     * @param knobValue the value of the knob
     * @throws quicktime.std.StdQTException
     */
    public final void setKnob(int knobNumber, int knobValue) throws StdQTException {
        StdQTException.checkError(NASetKnob(QTObject.ID(this.na), _ID(), knobNumber, knobValue));
    }

    /**
     * Locate the best fitting instrument number for the given ToneDescription.
     * @see "QuickTime::NAFindNoteChannelTone"
     * @param td description for instrument fit
     * @return the instrument index of fit
     * @throws quicktime.std.StdQTException
     */
    public final int findTone(ToneDescription td) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAFindNoteChannelTone(QTObject.ID(this.na), _ID(), td.getBytes(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Initializes a note channel with an atomic instrument.
     * @see "QuickTime::NASetAtomicInstrument"
     * @param instrument the AtomicInstrument to set the channel to.
     * @param flags use the set flags from Atomic instrument.
     * @throws quicktime.std.StdQTException
     */
    public final void setAtomicInstrument(AtomicInstrument instrument, int flags) throws StdQTException {
        QTPointerRef localQTPointerRef = instrument.toQTPointer();
        int i = NASetAtomicInstrument(QTObject.ID(this.na), _ID(), QTObject.ID(localQTPointerRef), flags);
        if (!instrument.isLocked())
            instrument.unlock();
        StdQTException.checkError(i);
    }

    /**
     * Change the instrument setting to the specified instrument number.
     * @see "QuickTime::NASetInstrumentNumber"
     * @param instrumentNumber instrument number to apply
     * @throws quicktime.std.StdQTException
     */
    public final void setInstrumentNumber(int instrumentNumber) throws StdQTException {
        StdQTException.checkError(NASetInstrumentNumber(QTObject.ID(this.na), _ID(), instrumentNumber));
    }

    /**
     * Get the NoteRequest that corresponds to this NoteChannel.
     * @see "QuickTime::NAGetNoteRequest"
     * @return a new NoteRequest class with the parameters of this NoteChannel
     * @throws quicktime.QTException
     */
    public final NoteRequest getNoteRequest() throws QTException {
        NoteRequest localNoteRequest = new NoteRequest();
        StdQTException.checkError(NAGetNoteRequest(QTObject.ID(this.na), _ID(), localNoteRequest.getBytes()));
        return localNoteRequest;
    }

    /**
     * Presents a user interface for changing the instrument used by the note channel.
     * @see "QuickTime::NAPickEditInstrument"
     * @param prompt Dialog box prompt "New Instrument..."
     * @param flags flags to limit user options
     * @throws quicktime.std.StdQTException
     */
    public final void pickEditInstrument(String prompt, int flags) throws StdQTException {
        int k = 0;
        if (QTSession.isCurrentOS(4)) {
            k = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int i = 0;
                synchronized (QTNative.globalsLock) {
                    i = NoteChannel.NAPickEditInstrument(QTObject.ID(NoteChannel.this.na), 0, QTUtils.String2PString(prompt, 255), 0, _ID(), 0, flags);
                    return i;
                }
            });
            StdQTException.checkError(k);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NAPickEditInstrument(QTObject.ID(this.na), 0, QTUtils.String2PString(prompt, 255), 0, _ID(), 0, flags));
            }
        }
    }

    /**
     * Get the NoteRequest that corresponds to this NoteChannel.
     * @see "QuickTime::NASendMIDI"
     */
    public final void sendMIDI(MusicMIDIPacket paramMusicMIDIPacket) throws StdQTException {
        int i = NASendMIDI(QTObject.ID(this.na), _ID(), paramMusicMIDIPacket.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * Passes sound localization data to a note channel.
     * @see "QuickTime::NASetNoteChannelSoundLocalization"
     * @param slData sound localization data
     * @throws quicktime.std.StdQTException
     */
    public final void setSoundLocalization(SoundLocalization slData) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelSoundLocalization(QTObject.ID(this.na), _ID(), QTObject.ID(slData)));
    }

    private static int NANewNoteChannel(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return MusicManager.NANewNoteChannel(paramInt, paramArrayOfByte, paramArrayOfInt);
    }

    private static int NAGetNoteChannelInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return MusicManager.NAGetNoteChannelInfo(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int NAPrerollNoteChannel(int paramInt1, int paramInt2) {
        return MusicManager.NAPrerollNoteChannel(paramInt1, paramInt2);
    }

    private static int NAUnrollNoteChannel(int paramInt1, int paramInt2) {
        return MusicManager.NAUnrollNoteChannel(paramInt1, paramInt2);
    }

    private static int NAResetNoteChannel(int paramInt1, int paramInt2) {
        return MusicManager.NAResetNoteChannel(paramInt1, paramInt2);
    }

    private static int NASetNoteChannelVolume(int paramInt1, int paramInt2, int paramInt3) {
        return MusicManager.NASetNoteChannelVolume(paramInt1, paramInt2, paramInt3);
    }

    private static int NAPlayNote(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MusicManager.NAPlayNote(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int NAGetController(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return MusicManager.NAGetController(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static int NASetController(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MusicManager.NASetController(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int NAGetKnob(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return MusicManager.NAGetKnob(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static int NASetKnob(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MusicManager.NASetKnob(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int NAFindNoteChannelTone(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return MusicManager.NAFindNoteChannelTone(paramInt1, paramInt2, paramArrayOfByte, paramArrayOfInt);
    }

    private static int NASetAtomicInstrument(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MusicManager.NASetAtomicInstrument(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int NASetInstrumentNumber(int paramInt1, int paramInt2, int paramInt3) {
        return MusicManager.NASetInstrumentNumber(paramInt1, paramInt2, paramInt3);
    }

    private static int NAGetNoteRequest(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MusicManager.NAGetNoteRequest(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int NAPickEditInstrument(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        return MusicManager.NAPickEditInstrument(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4, paramInt5, paramInt6);
    }

    private static int NASendMIDI(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MusicManager.NASendMIDI(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int NASetNoteChannelSoundLocalization(int paramInt1, int paramInt2, int paramInt3) {
        return MusicManager.NASetNoteChannelSoundLocalization(paramInt1, paramInt2, paramInt3);
    }

    private static int NASetNoteChannelBalance(int paramInt1, int paramInt2, int paramInt3) {
        return MusicManager.NASetNoteChannelBalance(paramInt1, paramInt2, paramInt3);
    }

    private static int NAGetRegisteredMusicDevice(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt) {
        return MusicManager.NAGetRegisteredMusicDevice(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfInt);
    }
}
