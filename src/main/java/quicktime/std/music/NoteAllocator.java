package quicktime.std.music;

import com.apple.mrj.macos.carbon.CarbonAccess;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MusicManager;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.Pict;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.Track;
import quicktime.util.QTUtils;


/**
 * Corresponds to the NoteAllocator component type.
 */
public final class NoteAllocator extends Component implements QuickTimeLib {

    static NoteAllocator defaultNA = null;

    int res;

    /**
     * Returns a system created NoteAllocator instance that can be used
     * by applications.
     *
     * @return a NoteAllocator
     * @throws quicktime.QTException
     */
    public static NoteAllocator getDefault() throws QTException {
        if (defaultNA == null) {
            defaultNA = new NoteAllocator();
        } else if (!defaultNA.stillValid()) {
            defaultNA = new NoteAllocator();
        }
        return defaultNA;
    }

    /**
     * Opens the NoteAllocator component.
     *
     * @see "QuickTime::OpenADefaultComponent"
     */
    public NoteAllocator() throws QTException {
        this(0);
    }

    /**
     * Opens the NoteAllocator component with a specified subType.
     *
     * @see "QuickTime::OpenADefaultComponent"
     */
    public NoteAllocator(int subType) throws QTException {
        super(1852798049, subType);
    }

    NoteAllocator(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Returns the number of NoteChannels handled by the specific NoteAllocator instance.
     *
     * @return number of NoteChannels
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NAGetIndNoteChannel"
     */
    public int numNoteChannels() throws StdQTException {
        int i = NAGetIndNoteChannel(_ID(), 0, null, null);
        if (i < 0)
            StdQTException.checkError(i);
        return i;
    }

    /**
     * Returns a new NoteChannel as indexed into the NoteAllocator instance.
     *
     * @param index the index of the NoteChannel
     * @return a new NoteChannel instance
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NAGetIndNoteChannel"
     */
    public NoteChannel getIndNoteChannel(int index) throws StdQTException {
        int[] noteChannel = new int[1];
        int[] count = new int[1];
        int i = NAGetIndNoteChannel(_ID(), index, noteChannel, count);
        if (i < 0)
            StdQTException.checkError(i);
        return new NoteChannel(this, noteChannel[0], this);
    }

    /**
     * Call periodically to allow the note allocator to perform tasks.
     *
     * @see "QuickTime::NATask"
     */
    public void task() throws StdQTException {
        StdQTException.checkError(NATask(_ID()));
    }

    /**
     * Displays a dialog to allow instrument selection.
     *
     * @param prompt   dialog box prompt.
     * @param track    a movie track
     * @param songName the name of the song to display
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NAPickArrangement"
     */
    public void pickArrangement(String prompt, Track track, String songName) throws StdQTException {
        String str1 = prompt;
        Track t = track;
        String str2 = songName;
        int i = _ID();
        if (QTSession.isCurrentOS(4)) {
            this.res = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                NoteAllocator.this.res = NoteAllocator.NAPickArrangement(i, 0, QTUtils.String2PString(str1, 255), 0, 0, QTObject.ID(t), QTUtils.String2PString(str2, 255));
                return NoteAllocator.this.res;
            });
            StdQTException.checkError(this.res);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NAPickArrangement(_ID(), 0, QTUtils.String2PString(prompt, 255), 0, 0, QTObject.ID(track), QTUtils.String2PString(songName, 255)));
            }
        }
    }

    /**
     * Displays a copyright dialog with information specific to a music device.
     *
     * @param p         picture for dialog
     * @param author    author information
     * @param copyright copyright information
     * @param other     and additional information
     * @param title     title information
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NACopyrightDialog"
     */
    public void copyrightDialog(Pict p, String author, String copyright, String other, String title) throws StdQTException {
        String str1 = author;
        String str2 = copyright;
        String str3 = other;
        Pict pict = p;
        String str4 = title;
        int i = _ID();
        if (QTSession.isCurrentOS(4)) {
            this.res = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                NoteAllocator.this.res = NoteAllocator.NACopyrightDialog(i, QTObject.ID(pict), QTUtils.String2PString(str1, 255), QTUtils.String2PString(str2, 255), QTUtils.String2PString(str3, 255), QTUtils.String2PString(str4, 255), 0, 0);
                return NoteAllocator.this.res;
            });
            StdQTException.checkError(this.res);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NACopyrightDialog(_ID(), QTObject.ID(p), QTUtils.String2PString(author, 255), QTUtils.String2PString(copyright, 255), QTUtils.String2PString(other, 255), QTUtils.String2PString(title, 255), 0, 0));
            }
        }
    }

    /**
     * Return the number of MusicComponents that are registered with the note allocator.
     *
     * @return the number of music components.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NAGetRegisteredMusicDevice"
     */
    public int numMusicComponents() throws StdQTException {
        int i = NAGetRegisteredMusicDevice(_ID(), 0, null, null, null, null);
        if (i < 0)
            StdQTException.checkError(i);
        return i;
    }

    /**
     * Registers a music component with the NoteAllocator, where the MusicComponent will
     * use the supplied SoundBank for its sample data. The call returns the Name of
     * the sample bank, which is used in the ToneDescription/NoteRequest as the synthesizerName
     * field to create NoteChannels that use this component (the synthesizerType will be
     * StdQTConstants.kSoftSynthComponentSubType).
     *
     * @param soundBankFile a DLS or SF2 file of sample data
     * @return the name of the synthesizer to use when creating NoteChannels that use this bank
     * @throws quicktime.QTException
     * @see "QuickTime::NARegisterMusicDevice"
     */
    public String registerMusicDevice_SoundBank(QTFile soundBankFile) throws QTException {
        byte[] name = new byte[32];
        StdQTException.checkError(NARegisterMusicDevice(_ID(), 0, name, new SynthesizerConnections(soundBankFile).getBytes()));
        return QTUtils.PString2String(name, 0);
    }

    /**
     * Registers a music component with the NoteAllocator.
     *
     * @param synthType   the subType of the MusicComponent of the synth
     * @param name        the synthesizer name
     * @param connections a synthesizer connections for a MIDI device.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NARegisterMusicDevice"
     */
    public void registerMusicDevice(int synthType, String name, SynthesizerConnections connections) throws StdQTException {
        StdQTException.checkError(NARegisterMusicDevice(_ID(), synthType, QTUtils.String2PString(name, 31), connections.getBytes()));
    }

    /**
     * Removes a previously registered music component from the NoteAllocator.
     *
     * @param index the index of the synthesizer to unregister.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NAUnregisterMusicDevice"
     */
    public void unregisterMusicDevice(int index) throws StdQTException {
        StdQTException.checkError(NAUnregisterMusicDevice(_ID(), index));
    }

    /**
     * Return information about music components that are registered with the note allocator.
     *
     * @param index the index of the component you are interested in.
     * @return music component information
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NAGetRegisteredMusicDevice"
     */
    public MusicComponentInfo getRegisteredMusicDevice(int index) throws StdQTException {
        byte[] name = new byte[256];
        int[] synthType = new int[1];
        SynthesizerConnections synthesizerConnections = new SynthesizerConnections();
        int[] mc = new int[1];
        int i = NAGetRegisteredMusicDevice(_ID(), index, synthType, name, synthesizerConnections.getBytes(), mc);
        if (i < 0)
            StdQTException.checkError(i);
        MusicComponentInfo registeredMusicDevice = new MusicComponentInfo();
        registeredMusicDevice.synthType = synthType[0];
        registeredMusicDevice.name = new String(name, 1, name[0]);
        registeredMusicDevice.mc = new MusicComponent(mc[0], null);
        registeredMusicDevice.connections = synthesizerConnections;
        return registeredMusicDevice;
    }

    /**
     * Obtain external MIDI connection information about the MIDI device that is
     * attached to the system and selected as the default MIDI input device.
     *
     * @see "QuickTime::NAGetDefaultMIDIInput()"
     */
    public SynthesizerConnections getDefaultMIDIInput() throws StdQTException {
        SynthesizerConnections defaultMIDIInput = new SynthesizerConnections();
        StdQTException.checkError(NAGetDefaultMIDIInput(_ID(), defaultMIDIInput.getBytes()));
        return defaultMIDIInput;
    }

    /**
     * Initializes an external MIDI device that is used to receive external MIDI information.
     *
     * @param sc connections for the external MIDI input device
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::NASetDefaultMIDIInput()"
     */
    public void setDefaultMIDIInput(SynthesizerConnections sc) throws StdQTException {
        StdQTException.checkError(NASetDefaultMIDIInput(_ID(), sc.getBytes()));
    }

    /**
     * Saves the current list of registered devices to the QuickTime default file.
     *
     * @see "QuickTime::NASaveMusicConfiguration()"
     */
    public void saveMusicConfiguration() throws StdQTException {
        StdQTException.checkError(NASaveMusicConfiguration(_ID()));
    }

    /**
     * Get the MIDI input ports that are available to the NoteAllocator.
     *
     * @return the MIDI input ports list.
     * @throws quicktime.QTException
     * @see "QuickTime::NAGetMIDIPorts()"
     */
    public QTMIDIPortList getMIDIInPorts() throws QTException {
        int[] inputPorts = new int[1];
        StdQTException.checkError(NAGetMIDIPorts(_ID(), inputPorts, null));
        return new QTMIDIPortList(inputPorts[0]);
    }

    /**
     * Get the MIDI output ports that are available to the NoteAllocator.
     *
     * @return the MIDI output ports list.
     * @throws quicktime.QTException
     * @see "QuickTime::NAGetMIDIPorts()"
     */
    public QTMIDIPortList getMIDIOutPorts() throws QTException {
        int[] outputPorts = new int[1];
        StdQTException.checkError(NAGetMIDIPorts(_ID(), null, outputPorts));
        return new QTMIDIPortList(outputPorts[0]);
    }

    /**
     * Removes the external default MIDI service procedure call, if previously defined by
     * the useDefaultMIDIInput call.
     *
     * @see "QuickTime::NALoseDefaultMIDIInput()"
     */
    public void loseDefaultMIDIInput() throws StdQTException {
        StdQTException.checkError(NALoseDefaultMIDIInput(_ID()));
    }

    private static int NAGetIndNoteChannel(int na, int index, int[] noteChannel, int[] count) {
        return MusicManager.NAGetIndNoteChannel(na, index, noteChannel, count);
    }

    private static int NATask(int na) {
        return MusicManager.NATask(na);
    }

    private static int NAPickArrangement(int na, int filterProc, byte[] prompt, int zero1, int zero2, int t, byte[] songName) {
        return MusicManager.NAPickArrangement(na, filterProc, prompt, zero1, zero2, t, songName);
    }

    private static int NACopyrightDialog(int na, int p, byte[] author, byte[] copyright, byte[] other, byte[] title, int filterProc, int refCon) {
        return MusicManager.NACopyrightDialog(na, p, author, copyright, other, title, filterProc, refCon);
    }

    private static int NAGetRegisteredMusicDevice(int na, int index, int[] synthType, byte[] name, byte[] connections, int[] mc) {
        return MusicManager.NAGetRegisteredMusicDevice(na, index, synthType, name, connections, mc);
    }

    private static int NARegisterMusicDevice(int na, int synthType, byte[] name, byte[] connections) {
        return MusicManager.NARegisterMusicDevice(na, synthType, name, connections);
    }

    private static int NAUnregisterMusicDevice(int na, int index) {
        return MusicManager.NAUnregisterMusicDevice(na, index);
    }

    private static int NAGetDefaultMIDIInput(int na, byte[] midiIn) {
        return MusicManager.NAGetDefaultMIDIInput(na, midiIn);
    }

    private static int NASetDefaultMIDIInput(int na, byte[] midiIn) {
        return MusicManager.NASetDefaultMIDIInput(na, midiIn);
    }

    private static int NASaveMusicConfiguration(int na) {
        return MusicManager.NASaveMusicConfiguration(na);
    }

    private static int NAGetMIDIPorts(int na, int[] inputPorts, int[] outputPorts) {
        return MusicManager.NAGetMIDIPorts(na, inputPorts, outputPorts);
    }

    private static int NALoseDefaultMIDIInput(int na) {
        return MusicManager.NALoseDefaultMIDIInput(na);
    }
}
