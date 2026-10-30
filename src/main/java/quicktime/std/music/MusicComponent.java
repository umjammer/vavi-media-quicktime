package quicktime.std.music;

import quicktime.QTException;
import quicktime.internal.toolbox.MusicManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.util.QTUtils;


/**
 * Corresponds to an instance of the MusicComponent component type.
 * Typically an applications uses either the NoteChannel or a movie to playback
 * QTMA data. A NoteChannel corresponds to a part in the MusicComponent that it is
 * using (as dictated by the NoteRequest used to create the NoteChannel).
 * <p>
 * The NoteAllocator keeps an indexed list of each unique MusicComponent that is in use by the system.
 * The Application can call the NoteChannel.getIndexInfo() call to get the index within
 * the NoteAllocator that can then be used to retrieve the MusicComponent that the NoteChannel
 * is using. (There is also a call NoteChannel.getMusicComponent() that will return the actual
 * MusicComponent instance).
 * <p>
 * There are three calls that require a part number to be known (ie. this is the part number
 * within a MusicComponent that a particular note channel is using), and in those cases the
 * application can use the NoteChannel.getPartInfo() to return that part number. This can
 * then be used in the MusicComponent calls that require a part number to be supplied.
 */
public final class MusicComponent extends Component implements QuickTimeLib {

    /**
     * Opens the MusicComponent component.
     *
     * @see "QuickTime::OpenADefaultComponent"
     * @deprecated <I>should use NoteAllocator calls to get a MusicComponent Instance</I>
     */
    public MusicComponent() throws QTException {
        this(0);
    }

    /**
     * Opens the MusicComponent component with a specified subType.
     *
     * @see "QuickTime::OpenADefaultComponent"
     * @deprecated <I>should use NoteAllocator calls to get a MusicComponent Instance</I>
     */
    public MusicComponent(int subType) throws QTException {
        super(1836413801, subType);
    }

    MusicComponent(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Returns a structure describing the synthesizer controlled by the music component device.
     *
     * @return a SynthesizerDescription
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicGetDescription"
     */
    public SynthesizerDescription getDescription() throws StdQTException {
        SynthesizerDescription description = new SynthesizerDescription();
        StdQTException.checkError(MusicGetDescription(_ID(), description.getBytes()));
        return description;
    }

    /**
     * Returns the AtomicInstrument that is set for that part. If not set, this method
     * can return null.
     *
     * @param part  the part
     * @param flags Specify what pieces of information about an atomic instrument the caller is interested in.
     * @return the AtomicInstrument
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicGetPartAtomicInstrument"
     */
    public AtomicInstrument getPartAtomicInstrument(int part, int flags) throws StdQTException {
        int[] ai = {0};
        StdQTException.checkError(MusicGetPartAtomicInstrument(_ID(), part, ai, flags));
        if (ai[0] == 0)
            return null;
        return new AtomicInstrument(ai[0], this);
    }

    /**
     * Returns the value of the specified global synthesizer
     * knob. A global knob controls an aspect of the entire synthesizer. It is not specific
     * to a part within the synthesizer.
     *
     * @param knobID the knob id
     * @return the knob value
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicGetKnob"
     */
    public int getKnob(int knobID) throws StdQTException {
        int i = MusicGetKnob(_ID(), knobID);
        if ((i >>> 16 == 32768) && ((i | 0xFFFF) != 0))
            throw new StdQTException(i | 0xFFFF);
        return i;
    }

    /**
     * Sets the value of a global knob for a synthesiser.
     *
     * @param knobID    the knob id
     * @param knobValue the knob value
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicSetKnob"
     */
    public void setKnob(int knobID, int knobValue) throws StdQTException {
        StdQTException.checkError(MusicSetKnob(_ID(), knobID, knobValue));
    }

    /**
     * Returns a part's name.
     *
     * @param part the part
     * @return the name
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicGetPartName"
     */
    public String getPartName(int part) throws StdQTException {
        byte[] name = new byte[32];
        StdQTException.checkError(MusicGetPartName(_ID(), part, name));
        return QTUtils.PString2String(name, 0);
    }

    /**
     * Sets a part's name.
     *
     * @param part the part
     * @param name the name
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicSetPartName"
     */
    public void setPartName(int part, String name) throws StdQTException {
        StdQTException.checkError(MusicSetPartName(_ID(), part, QTUtils.String2PString(name, 31)));
    }

    /**
     * Sets a value in semitones,
     * which is the synthesizerÕs master tuning.
     *
     * @param masterTune the new tuning
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicSetMasterTune"
     */
    public void setMasterTune(float masterTune) throws StdQTException {
        StdQTException.checkError(MusicSetMasterTune(_ID(), QTUtils.X2Fix(masterTune)));
    }

    /**
     * Returns a value in semitones,
     * which is the synthesizerÕs master tuning.
     *
     * @return tuning
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicGetMasterTune"
     */
    public float getMasterTune() throws StdQTException {
        int i = MusicGetMasterTune(_ID());
        if (i < 0)
            throw new StdQTException(i);
        return QTUtils.Fix2X(i);
    }

    /**
     * Returns a Instrument knob description for the specified knob index or knob ID for the MusicComponent.
     *
     * @param knobIndex the knob index to get
     * @return a KnobDescription
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicGetInstrumentKnobDescription"
     */
    public KnobDescription getInstrumentKnobDescrption(int knobIndex) throws StdQTException {
        KnobDescription instrumentKnobDescrption = new KnobDescription();
        StdQTException.checkError(MusicGetInstrumentKnobDescription(_ID(), knobIndex, instrumentKnobDescrption.getBytes()));
        return instrumentKnobDescrption;
    }

    /**
     * Returns a Drum knob description for the specified knob index or knob ID for the MusicComponent.
     *
     * @param knobIndex the knob index to get
     * @return a KnobDescription
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicGetDrumKnobDescription"
     */
    public KnobDescription getDrumKnobDescrption(int knobIndex) throws StdQTException {
        KnobDescription drumKnobDescrption = new KnobDescription();
        StdQTException.checkError(MusicGetDrumKnobDescription(_ID(), knobIndex, drumKnobDescrption.getBytes()));
        return drumKnobDescrption;
    }

    /**
     * Returns a knob description for the specified global knob index or knob ID for the MusicComponent.
     *
     * @param knobIndex the knob index to get
     * @return a KnobDescription
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MusicGetKnobDescription"
     */
    public KnobDescription getKnobDescrption(int knobIndex) throws StdQTException {
        KnobDescription knobDescrption = new KnobDescription();
        StdQTException.checkError(MusicGetKnobDescription(_ID(), knobIndex, knobDescrption.getBytes()));
        return knobDescrption;
    }

    private static int MusicGetDescription(int mc, byte[] pd) {
        return MusicManager.MusicGetDescription(mc, pd);
    }

    private static int MusicGetPartAtomicInstrument(int mc, int part, int[] ai, int flags) {
        return MusicManager.MusicGetPartAtomicInstrument(mc, part, ai, flags);
    }

    private static int MusicGetPartName(int mc, int part, byte[] name) {
        return MusicManager.MusicGetPartName(mc, part, name);
    }

    private static int MusicSetPartName(int mc, int part, byte[] name) {
        return MusicManager.MusicSetPartName(mc, part, name);
    }

    private static int MusicGetKnob(int mc, int knobID) {
        return MusicManager.MusicGetKnob(mc, knobID);
    }

    private static int MusicSetKnob(int mc, int knobID, int knobValue) {
        return MusicManager.MusicSetKnob(mc, knobID, knobValue);
    }

    private static int MusicGetMasterTune(int mc) {
        return MusicManager.MusicGetMasterTune(mc);
    }

    private static int MusicSetMasterTune(int mc, int masterTune) {
        return MusicManager.MusicSetMasterTune(mc, masterTune);
    }

    private static int MusicGetInstrumentKnobDescription(int mc, int knobIndex, byte[] mkd) {
        return MusicManager.MusicGetInstrumentKnobDescription(mc, knobIndex, mkd);
    }

    private static int MusicGetDrumKnobDescription(int mc, int knobIndex, byte[] mkd) {
        return MusicManager.MusicGetDrumKnobDescription(mc, knobIndex, mkd);
    }

    private static int MusicGetKnobDescription(int mc, int knobIndex, byte[] mkd) {
        return MusicManager.MusicGetKnobDescription(mc, knobIndex, mkd);
    }
}
