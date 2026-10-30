package quicktime.std.music;

import com.apple.mrj.macos.carbon.CarbonAccess;
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

    private static int allocate(NoteAllocator na, NoteRequest noteRequest) throws QTException {
        int[] outChannel = new int[1];
        StdQTException.checkError(NANewNoteChannel(QTObject.ID(na), noteRequest.getBytes(), outChannel));
        return outChannel[0];
    }

    private static int allocate(NoteAllocator na, int gmNumber) throws QTException {
        ToneDescription td = new ToneDescription(na, gmNumber);
        NoteRequest noteRequest = new NoteRequest(td);
        int[] outChannel = new int[1];
        StdQTException.checkError(NANewNoteChannel(QTObject.ID(na), noteRequest.getBytes(), outChannel));
        return outChannel[0];
    }

    /**
     * Creates a new NoteChannel object with the request instrument and polyphony.
     * This constructor will use the system created NoteAllocator, which can
     * be retrieved if required, through the NoteChannel.getNoteAllocator() method
     * once the NoteChannel is created.
     * @see "QuickTime::NANewNoteChannel"
     */
    public NoteChannel(int gmNumber, int poly) throws QTException {
        this(new NoteRequest(gmNumber, poly));
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

    NoteChannel(NoteAllocator na, int nativeRep, Object ref) {
        super(nativeRep, ref);
        this.na = na;
    }

    /**
     * Returns the NoteAllocator that owns this NoteChannel
     */
    public NoteAllocator getNoteAllocator() {
        return this.na;
    }

    /**
     * Returns the index of the NoteAllocator of the MusicComponent that is used by this NoteChannel.
     * @see "QuickTime::NAGetNoteChannelInfo"
     * @return index
     * @throws quicktime.std.StdQTException
     */
    public int getIndexInfo() throws StdQTException {
        int[] index = new int[1];
        int[] part = new int[1];
        StdQTException.checkError(NAGetNoteChannelInfo(QTObject.ID(this.na), _ID(), index, part));
        return index[0];
    }

    /**
     * Returns the part number within the NoteChannel's MusicComponent that corresponds to this channel.
     * @see "QuickTime::NAGetNoteChannelInfo"
     * @return part number
     * @throws quicktime.std.StdQTException
     */
    public int getPartInfo() throws StdQTException {
        int[] index = new int[1];
        int[] part = new int[1];
        StdQTException.checkError(NAGetNoteChannelInfo(QTObject.ID(this.na), _ID(), index, part));
        return part[0];
    }

    /**
     * Returns the Music component in use by the note channel.
     * @see "QuickTime::NAGetNoteChannelInfo"
     * @see "QuickTime::NAGetRegisteredMusicDevice"
     * @return index
     * @throws quicktime.std.StdQTException
     */
    public MusicComponent getMusicComponent() throws StdQTException {
        int[] mc = new int[1];
        int i = NAGetRegisteredMusicDevice(QTObject.ID(this.na), getIndexInfo(), 0, 0, 0, mc);
        if (i < 0)
            StdQTException.checkError(i);
        return new MusicComponent(mc[0], this);
    }

    /**
     * Will attempt to re-allocate the note channel if it was invalid previously.
     * @see "QuickTime::NAPrerollNoteChannel"
     */
    public void preroll() throws StdQTException {
        StdQTException.checkError(NAPrerollNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Marks a note channel as available to be stolen.
     * @see "QuickTime::NAUnrollNoteChannel"
     */
    public void unroll() throws StdQTException {
        StdQTException.checkError(NAUnrollNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Turns off all currently sounding notes, and resets all controllers to their
     * default values.
     * @see "QuickTime::NAResetNoteChannel"
     */
    public void reset() throws StdQTException {
        StdQTException.checkError(NAResetNoteChannel(QTObject.ID(this.na), _ID()));
    }

    /**
     * Sets the volume on the note channel.
     * @see "QuickTime::NASetNoteChannelVolume"
     * @param volume volume value
     * @throws quicktime.std.StdQTException
     */
    public void setVolume(float volume) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelVolume(QTObject.ID(this.na), _ID(), QTUtils.X2Fix(volume)));
    }

    /**
     * Modifies the pan controller setting for the note channel.
     * @see "QuickTime::NASetNoteChannelBalance"
     * @param balance pan value (-128 left, to 128 right)
     * @throws quicktime.std.StdQTException
     */
    public void setBalance(int balance) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelBalance(QTObject.ID(this.na), _ID(), balance));
    }

    /**
     * Plays a musical note with given pitch and velocity.
     * @see "QuickTime::NAPlayNote"
     * @param pitch MIDI pitch values (0 to 127, 60 is middle C)
     * @param velocity velocity of note, where 0 = silence - note off.
     * @throws quicktime.std.StdQTException
     */
    public void playNoteRaw(int pitch, int velocity) throws StdQTException {
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
    public void playNoteCents(int noteNumber, int cents, int velocity) throws StdQTException {
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
    public void playNote(float pitch, int velocity) throws StdQTException {
        StdQTException.checkError(NAPlayNote(QTObject.ID(this.na), _ID(), QTUtils.X2ShortFix(pitch), velocity));
    }

    /**
     * Retrieves the current value for the specified controller
     * The micro controller resolution is 128 steps (kMicroControllerResolution) per semitone.
     * @see "QuickTime::NAGetController"
     * @param controllerNumber which controller
     * @throws quicktime.std.StdQTException
     */
    public float getController(int controllerNumber) throws StdQTException {
        int[] controllerValue = { 0 };
        StdQTException.checkError(NAGetController(QTObject.ID(this.na), _ID(), controllerNumber, controllerValue));
        return QTUtils.ShortFix2X((short) controllerValue[0]);
    }

    /**
     * Changes the specified controller to the specified value.
     * @see "QuickTime::NASetController"
     * @param controllerNumber which controller
     * @param controllerValue the new controller value,
     *    dealing with raw controller values, which have the full range of a short (-32768 to 32767)
     * @throws quicktime.std.StdQTException
     */
    public void setControllerRaw(int controllerNumber, int controllerValue) throws StdQTException {
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
    public void setController(int controllerNumber, float controllerValue) throws StdQTException {
        StdQTException.checkError(NASetController(QTObject.ID(this.na), _ID(), controllerNumber, QTUtils.X2ShortFix(controllerValue)));
    }

    /**
     * Returns the value of a knob for the note channel.
     * @see "QuickTime::NAGetKnob"
     * @param knobNumber index or ID of the knob
     * @return the value of the knob
     * @throws quicktime.std.StdQTException
     */
    public int getKnob(int knobNumber) throws StdQTException {
        int[] knobValue = new int[1];
        StdQTException.checkError(NAGetKnob(QTObject.ID(this.na), _ID(), knobNumber, knobValue));
        return knobValue[0];
    }

    /**
     * Sets the value of a knob for the note channel.
     * @see "QuickTime::NASetKnob"
     * @param knobNumber index or ID of the knob
     * @param knobValue the value of the knob
     * @throws quicktime.std.StdQTException
     */
    public void setKnob(int knobNumber, int knobValue) throws StdQTException {
        StdQTException.checkError(NASetKnob(QTObject.ID(this.na), _ID(), knobNumber, knobValue));
    }

    /**
     * Locate the best fitting instrument number for the given ToneDescription.
     * @see "QuickTime::NAFindNoteChannelTone"
     * @param td description for instrument fit
     * @return the instrument index of fit
     * @throws quicktime.std.StdQTException
     */
    public int findTone(ToneDescription td) throws StdQTException {
        int[] instrumentNumber = new int[1];
        StdQTException.checkError(NAFindNoteChannelTone(QTObject.ID(this.na), _ID(), td.getBytes(), instrumentNumber));
        return instrumentNumber[0];
    }

    /**
     * Initializes a note channel with an atomic instrument.
     * @see "QuickTime::NASetAtomicInstrument"
     * @param instrument the AtomicInstrument to set the channel to.
     * @param flags use the set flags from Atomic instrument.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomicInstrument(AtomicInstrument instrument, int flags) throws StdQTException {
        QTPointerRef qtPointerRef = instrument.toQTPointer();
        int i = NASetAtomicInstrument(QTObject.ID(this.na), _ID(), QTObject.ID(qtPointerRef), flags);
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
    public void setInstrumentNumber(int instrumentNumber) throws StdQTException {
        StdQTException.checkError(NASetInstrumentNumber(QTObject.ID(this.na), _ID(), instrumentNumber));
    }

    /**
     * Get the NoteRequest that corresponds to this NoteChannel.
     * @see "QuickTime::NAGetNoteRequest"
     * @return a new NoteRequest class with the parameters of this NoteChannel
     * @throws quicktime.QTException
     */
    public NoteRequest getNoteRequest() throws QTException {
        NoteRequest noteRequest = new NoteRequest();
        StdQTException.checkError(NAGetNoteRequest(QTObject.ID(this.na), _ID(), noteRequest.getBytes()));
        return noteRequest;
    }

    /**
     * Presents a user interface for changing the instrument used by the note channel.
     * @see "QuickTime::NAPickEditInstrument"
     * @param prompt Dialog box prompt "New Instrument..."
     * @param flags flags to limit user options
     * @throws quicktime.std.StdQTException
     */
    public void pickEditInstrument(String prompt, int flags) throws StdQTException {
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
    public void sendMIDI(MusicMIDIPacket musicMIDIPacket) throws StdQTException {
        int i = NASendMIDI(QTObject.ID(this.na), _ID(), musicMIDIPacket.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * Passes sound localization data to a note channel.
     * @see "QuickTime::NASetNoteChannelSoundLocalization"
     * @param slData sound localization data
     * @throws quicktime.std.StdQTException
     */
    public void setSoundLocalization(SoundLocalization slData) throws StdQTException {
        StdQTException.checkError(NASetNoteChannelSoundLocalization(QTObject.ID(this.na), _ID(), QTObject.ID(slData)));
    }

    private static int NANewNoteChannel(int na, byte[] noteRequest, int[] outChannel) {
        return MusicManager.NANewNoteChannel(na, noteRequest, outChannel);
    }

    private static int NAGetNoteChannelInfo(int na, int noteChannel, int[] index, int[] part) {
        return MusicManager.NAGetNoteChannelInfo(na, noteChannel, index, part);
    }

    private static int NAPrerollNoteChannel(int na, int noteChannel) {
        return MusicManager.NAPrerollNoteChannel(na, noteChannel);
    }

    private static int NAUnrollNoteChannel(int na, int noteChannel) {
        return MusicManager.NAUnrollNoteChannel(na, noteChannel);
    }

    private static int NAResetNoteChannel(int na, int noteChannel) {
        return MusicManager.NAResetNoteChannel(na, noteChannel);
    }

    private static int NASetNoteChannelVolume(int na, int noteChannel, int volume) {
        return MusicManager.NASetNoteChannelVolume(na, noteChannel, volume);
    }

    private static int NAPlayNote(int na, int noteChannel, int pitch, int velocity) {
        return MusicManager.NAPlayNote(na, noteChannel, pitch, velocity);
    }

    private static int NAGetController(int na, int noteChannel, int controllerNumber, int[] controllerValue) {
        return MusicManager.NAGetController(na, noteChannel, controllerNumber, controllerValue);
    }

    private static int NASetController(int na, int noteChannel, int controllerNumber, int controllerValue) {
        return MusicManager.NASetController(na, noteChannel, controllerNumber, controllerValue);
    }

    private static int NAGetKnob(int na, int noteChannel, int knobNumber, int[] knobValue) {
        return MusicManager.NAGetKnob(na, noteChannel, knobNumber, knobValue);
    }

    private static int NASetKnob(int na, int noteChannel, int knobNumber, int knobValue) {
        return MusicManager.NASetKnob(na, noteChannel, knobNumber, knobValue);
    }

    private static int NAFindNoteChannelTone(int na, int noteChannel, byte[] td, int[] instrumentNumber) {
        return MusicManager.NAFindNoteChannelTone(na, noteChannel, td, instrumentNumber);
    }

    private static int NASetAtomicInstrument(int na, int noteChannel, int instrument, int flags) {
        return MusicManager.NASetAtomicInstrument(na, noteChannel, instrument, flags);
    }

    private static int NASetInstrumentNumber(int na, int noteChannel, int instrumentNumber) {
        return MusicManager.NASetInstrumentNumber(na, noteChannel, instrumentNumber);
    }

    private static int NAGetNoteRequest(int na, int noteChannel, byte[] nrOut) {
        return MusicManager.NAGetNoteRequest(na, noteChannel, nrOut);
    }

    private static int NAPickEditInstrument(int na, int filterProc, byte[] prompt, int refCon, int nc, int instrument, int modifiers) {
        return MusicManager.NAPickEditInstrument(na, filterProc, prompt, refCon, nc, instrument, modifiers);
    }

    private static int NASendMIDI(int na, int noteChannel, byte[] mp) {
        return MusicManager.NASendMIDI(na, noteChannel, mp);
    }

    private static int NASetNoteChannelSoundLocalization(int na, int noteChannel, int data) {
        return MusicManager.NASetNoteChannelSoundLocalization(na, noteChannel, data);
    }

    private static int NASetNoteChannelBalance(int na, int noteChannel, int balance) {
        return MusicManager.NASetNoteChannelBalance(na, noteChannel, balance);
    }

    private static int NAGetRegisteredMusicDevice(int na, int index, int synthType, int name, int connections, int[] mc) {
        return MusicManager.NAGetRegisteredMusicDevice(na, index, synthType, name, connections, mc);
    }
}
