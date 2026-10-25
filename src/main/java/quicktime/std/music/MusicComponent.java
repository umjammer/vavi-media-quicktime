package quicktime.std.music;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.internal.toolbox.MusicManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.util.QTUtils;

/**
 * Corresponds to an instance of the MusicComponent component type.
 * Typically an applications uses either the NoteChannel or a movie to playback
 * QTMA data. A NoteChannel corresponds to a part in the MusicComponent that it is
 * using (as dictated by the NoteRequest used to create the NoteChannel).
 *
 * The NoteAllocator keeps an indexed list of each unique MusicComponent that is in use by the system.
 * The Application can call the NoteChannel.getIndexInfo() call to get the index within
 * the NoteAllocator that can then be used to retrieve the MusicComponent that the NoteChannel
 * is using. (There is also a call NoteChannel.getMusicComponent() that will return the actual
 * MusicComponent instance).
 *
 * There are three calls that require a part number to be known (ie. this is the part number
 * within a MusicComponent that a particular note channel is using), and in those cases the
 * application can use the NoteChannel.getPartInfo() to return that part number. This can
 * then be used in the MusicComponent calls that require a part number to be supplied.
 */
public final class MusicComponent extends Component implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(MusicComponent.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Deprecated.
     * <I>should use NoteAllocator calls to get a MusicComponent Instance</I>
     * Opens the MusicComponent component.
     * QuickTime::OpenADefaultComponent
     */
    public MusicComponent() throws QTException {
        this(0);
    }

    /**
     * Deprecated.
     * <I>should use NoteAllocator calls to get a MusicComponent Instance</I>
     * Opens the MusicComponent component with a specified subType.
     * QuickTime::OpenADefaultComponent
     */
    public MusicComponent(int paramInt) throws QTException {
        super(1836413801, paramInt);
    }

    MusicComponent(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Returns a structure describing the synthesizer controlled by the music component device.
     * QuickTime::MusicGetDescription
     * @return a SynthesizerDescription
     * @return {@link quicktime.std.StdQTException}
     */
    public SynthesizerDescription getDescription() throws StdQTException {
        SynthesizerDescription localSynthesizerDescription = new SynthesizerDescription();
        StdQTException.checkError(MusicGetDescription(_ID(), localSynthesizerDescription.getBytes()));
        return localSynthesizerDescription;
    }

    /**
     * Returns the AtomicInstrument that is set for that part. If not set, this method
     * can return null.
     * QuickTime::MusicGetPartAtomicInstrument
     * @param part the part
     * @param flags Specify what pieces of information about an atomic instrument the caller is interested in.
     * @return the AtomicInstrument
     * @return {@link quicktime.std.StdQTException}
     */
    public AtomicInstrument getPartAtomicInstrument(int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MusicGetPartAtomicInstrument(_ID(), paramInt1, arrayOfInt, paramInt2));
        if (arrayOfInt[0] == 0)
            return null;
        return new AtomicInstrument(arrayOfInt[0], this);
    }

    /**
     * Returns the value of the specified global synthesizer
     * knob. A global knob controls an aspect of the entire synthesizer. It is not specific
     * to a part within the synthesizer.
     * QuickTime::MusicGetKnob
     * @param knobID the knob id
     * @return the knob value
     * @return {@link quicktime.std.StdQTException}
     */
    public int getKnob(int paramInt) throws StdQTException {
        int i = MusicGetKnob(_ID(), paramInt);
        if ((i >>> 16 == 32768) && ((i | 0xFFFF) != 0))
            throw new StdQTException(i | 0xFFFF);
        return i;
    }

    /**
     * Sets the value of a global knob for a synthesiser.
     * QuickTime::MusicSetKnob
     * @param knobID the knob id
     * @param knobValue the knob value
     * @param {@link quicktime.std.StdQTException}
     */
    public void setKnob(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(MusicSetKnob(_ID(), paramInt1, paramInt2));
    }

    /**
     * Returns a part's name.
     * QuickTime::MusicGetPartName
     * @param part the part
     * @return the name
     * @return {@link quicktime.std.StdQTException}
     */
    public String getPartName(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[32];
        StdQTException.checkError(MusicGetPartName(_ID(), paramInt, arrayOfByte));
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Sets a part's name.
     * QuickTime::MusicSetPartName
     * @param part the part
     * @param name the name
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPartName(int paramInt, String paramString) throws StdQTException {
        StdQTException.checkError(MusicSetPartName(_ID(), paramInt, QTUtils.String2PString(paramString, 31)));
    }

    /**
     * Sets a value in semitones,
     * which is the synthesizerÕs master tuning.
     * QuickTime::MusicSetMasterTune
     * @param masterTune the new tuning
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMasterTune(float paramFloat) throws StdQTException {
        StdQTException.checkError(MusicSetMasterTune(_ID(), QTUtils.X2Fix(paramFloat)));
    }

    /**
     * Returns a value in semitones,
     * which is the synthesizerÕs master tuning.
     * QuickTime::MusicGetMasterTune
     * @return tuning
     * @return {@link quicktime.std.StdQTException}
     */
    public float getMasterTune() throws StdQTException {
        int i = MusicGetMasterTune(_ID());
        if (i < 0)
            throw new StdQTException(i);
        return QTUtils.Fix2X(i);
    }

    /**
     * Returns a Instrument knob description for the specified knob index or knob ID for the MusicComponent.
     * QuickTime::MusicGetInstrumentKnobDescription
     * @param knobIndex the knob index to get
     * @return a KnobDescription
     * @return {@link quicktime.std.StdQTException}
     */
    public KnobDescription getInstrumentKnobDescrption(int paramInt) throws StdQTException {
        KnobDescription localKnobDescription = new KnobDescription();
        StdQTException.checkError(MusicGetInstrumentKnobDescription(_ID(), paramInt, localKnobDescription.getBytes()));
        return localKnobDescription;
    }

    /**
     * Returns a Drum knob description for the specified knob index or knob ID for the MusicComponent.
     * QuickTime::MusicGetDrumKnobDescription
     * @param knobIndex the knob index to get
     * @return a KnobDescription
     * @return {@link quicktime.std.StdQTException}
     */
    public KnobDescription getDrumKnobDescrption(int paramInt) throws StdQTException {
        KnobDescription localKnobDescription = new KnobDescription();
        StdQTException.checkError(MusicGetDrumKnobDescription(_ID(), paramInt, localKnobDescription.getBytes()));
        return localKnobDescription;
    }

    /**
     * Returns a knob description for the specified global knob index or knob ID for the MusicComponent.
     * QuickTime::MusicGetKnobDescription
     * @param knobIndex the knob index to get
     * @return a KnobDescription
     * @return {@link quicktime.std.StdQTException}
     */
    public KnobDescription getKnobDescrption(int paramInt) throws StdQTException {
        KnobDescription localKnobDescription = new KnobDescription();
        StdQTException.checkError(MusicGetKnobDescription(_ID(), paramInt, localKnobDescription.getBytes()));
        return localKnobDescription;
    }

    private static int MusicGetDescription(int paramInt, byte[] paramArrayOfByte) {
        return MusicManager.MusicGetDescription(paramInt, paramArrayOfByte);
    }

    private static int MusicGetPartAtomicInstrument(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        return MusicManager.MusicGetPartAtomicInstrument(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    }

    private static int MusicGetPartName(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MusicManager.MusicGetPartName(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int MusicSetPartName(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MusicManager.MusicSetPartName(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int MusicGetKnob(int paramInt1, int paramInt2) {
        return MusicManager.MusicGetKnob(paramInt1, paramInt2);
    }

    private static int MusicSetKnob(int paramInt1, int paramInt2, int paramInt3) {
        return MusicManager.MusicSetKnob(paramInt1, paramInt2, paramInt3);
    }

    private static int MusicGetMasterTune(int paramInt) {
        return MusicManager.MusicGetMasterTune(paramInt);
    }

    private static int MusicSetMasterTune(int paramInt1, int paramInt2) {
        return MusicManager.MusicSetMasterTune(paramInt1, paramInt2);
    }

    private static int MusicGetInstrumentKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MusicManager.MusicGetInstrumentKnobDescription(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int MusicGetDrumKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MusicManager.MusicGetDrumKnobDescription(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int MusicGetKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MusicManager.MusicGetKnobDescription(paramInt1, paramInt2, paramArrayOfByte);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /MusicComponent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
