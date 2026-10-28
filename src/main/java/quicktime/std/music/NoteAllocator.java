package quicktime.std.music;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;
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

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(NoteAllocator.class));
            }
        }.establish();
    }

    static NoteAllocator defaultNA = null;

    int res;

    /**
     * Returns a system created NoteAllocator instance that can be used
     * by applications.
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
     * @see "QuickTime::OpenADefaultComponent"
     */
    public NoteAllocator() throws QTException {
        this(0);
    }

    /**
     * Opens the NoteAllocator component with a specified subType.
     * @see "QuickTime::OpenADefaultComponent"
     */
    public NoteAllocator(int paramInt) throws QTException {
        super(1852798049, paramInt);
    }

    NoteAllocator(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Returns the number of NoteChannels handled by the specific NoteAllocator instance.
     * @see "QuickTime::NAGetIndNoteChannel"
     * @return number of NoteChannels
     * @throws quicktime.std.StdQTException
     */
    public int numNoteChannels() throws StdQTException {
        int i = NAGetIndNoteChannel(_ID(), 0, null, null);
        if (i < 0)
            StdQTException.checkError(i);
        return i;
    }

    /**
     * Returns a new NoteChannel as indexed into the NoteAllocator instance.
     * @see "QuickTime::NAGetIndNoteChannel"
     * @param index the index of the NoteChannel
     * @return a new NoteChannel instance
     * @throws quicktime.std.StdQTException
     */
    public NoteChannel getIndNoteChannel(int index) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = NAGetIndNoteChannel(_ID(), index, arrayOfInt1, arrayOfInt2);
        if (i < 0)
            StdQTException.checkError(i);
        return new NoteChannel(this, arrayOfInt1[0], this);
    }

    /**
     * Call periodically to allow the note allocator to perform tasks.
     * @see "QuickTime::NATask"
     */
    public void task() throws StdQTException {
        StdQTException.checkError(NATask(_ID()));
    }

    /**
     * Displays a dialog to allow instrument selection.
     * @see "QuickTime::NAPickArrangement"
     * @param prompt dialog box prompt.
     * @param track a movie track
     * @param songName the name of the song to display
     * @throws quicktime.std.StdQTException
     */
    public void pickArrangement(String prompt, Track track, String songName) throws StdQTException {
        String str1 = prompt;
        Track localTrack = track;
        String str2 = songName;
        int i = _ID();
        if (QTSession.isCurrentOS(4)) {
            this.res = (Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                @Override
                public Object invoke() {
                    NoteAllocator.this.res = NoteAllocator.NAPickArrangement(i, 0, QTUtils.String2PString(str1, 255), 0, 0, QTObject.ID(localTrack), QTUtils.String2PString(str2, 255));
                    return NoteAllocator.this.res;
                }
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
     * @see "QuickTime::NACopyrightDialog"
     * @param p picture for dialog
     * @param author author information
     * @param copyright copyright information
     * @param other and additional information
     * @param title title information
     * @throws quicktime.std.StdQTException
     */
    public void copyrightDialog(Pict p, String author, String copyright, String other, String title) throws StdQTException {
        String str1 = author;
        String str2 = copyright;
        String str3 = other;
        Pict localPict = p;
        String str4 = title;
        int i = _ID();
        if (QTSession.isCurrentOS(4)) {
            this.res = (Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                @Override
                public Object invoke() {
                    NoteAllocator.this.res = NoteAllocator.NACopyrightDialog(i, QTObject.ID(localPict), QTUtils.String2PString(str1, 255), QTUtils.String2PString(str2, 255), QTUtils.String2PString(str3, 255), QTUtils.String2PString(str4, 255), 0, 0);
                    return NoteAllocator.this.res;
                }
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
     * @see "QuickTime::NAGetRegisteredMusicDevice"
     * @return the number of music components.
     * @throws quicktime.std.StdQTException
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
     * @see "QuickTime::NARegisterMusicDevice"
     * @param soundBankFile a DLS or SF2 file of sample data
     * @return the name of the synthesizer to use when creating NoteChannels that use this bank
     * @throws quicktime.QTException
     */
    public String registerMusicDevice_SoundBank(QTFile soundBankFile) throws QTException {
        byte[] arrayOfByte = new byte[32];
        StdQTException.checkError(NARegisterMusicDevice(_ID(), 0, arrayOfByte, new SynthesizerConnections(soundBankFile).getBytes()));
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Registers a music component with the NoteAllocator.
     * @see "QuickTime::NARegisterMusicDevice"
     * @param synthType the subType of the MusicComponent of the synth
     * @param name the synthesizer name
     * @param connections a synthesizer connections for a MIDI device.
     * @throws quicktime.std.StdQTException
     */
    public void registerMusicDevice(int synthType, String name, SynthesizerConnections connections) throws StdQTException {
        StdQTException.checkError(NARegisterMusicDevice(_ID(), synthType, QTUtils.String2PString(name, 31), connections.getBytes()));
    }

    /**
     * Removes a previously registered music component from the NoteAllocator.
     * @see "QuickTime::NAUnregisterMusicDevice"
     * @param index the index of the synthesizer to unregister.
     * @throws quicktime.std.StdQTException
     */
    public void unregisterMusicDevice(int index) throws StdQTException {
        StdQTException.checkError(NAUnregisterMusicDevice(_ID(), index));
    }

    /**
     * Return information about music components that are registered with the note allocator.
     * @see "QuickTime::NAGetRegisteredMusicDevice"
     * @param index the index of the component you are interested in.
     * @return music component information
     * @throws quicktime.std.StdQTException
     */
    public MusicComponentInfo getRegisteredMusicDevice(int index) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int[] arrayOfInt1 = new int[1];
        SynthesizerConnections localSynthesizerConnections = new SynthesizerConnections();
        int[] arrayOfInt2 = new int[1];
        int i = NAGetRegisteredMusicDevice(_ID(), index, arrayOfInt1, arrayOfByte, localSynthesizerConnections.getBytes(), arrayOfInt2);
        if (i < 0)
            StdQTException.checkError(i);
        MusicComponentInfo localMusicComponentInfo = new MusicComponentInfo();
        localMusicComponentInfo.synthType = arrayOfInt1[0];
        localMusicComponentInfo.name = new String(arrayOfByte, 1, arrayOfByte[0]);
        localMusicComponentInfo.mc = new MusicComponent(arrayOfInt2[0], null);
        localMusicComponentInfo.connections = localSynthesizerConnections;
        return localMusicComponentInfo;
    }

    /**
     * Obtain external MIDI connection information about the MIDI device that is
     * attached to the system and selected as the default MIDI input device.
     * @see "QuickTime::NAGetDefaultMIDIInput()"
     */
    public SynthesizerConnections getDefaultMIDIInput() throws StdQTException {
        SynthesizerConnections localSynthesizerConnections = new SynthesizerConnections();
        StdQTException.checkError(NAGetDefaultMIDIInput(_ID(), localSynthesizerConnections.getBytes()));
        return localSynthesizerConnections;
    }

    /**
     * Initializes an external MIDI device that is used to receive external MIDI information.
     * @see "QuickTime::NASetDefaultMIDIInput()"
     * @param sc connections for the external MIDI input device
     * @throws quicktime.std.StdQTException
     */
    public void setDefaultMIDIInput(SynthesizerConnections sc) throws StdQTException {
        StdQTException.checkError(NASetDefaultMIDIInput(_ID(), sc.getBytes()));
    }

    /**
     * Saves the current list of registered devices to the QuickTime default file.
     * @see "QuickTime::NASaveMusicConfiguration()"
     */
    public void saveMusicConfiguration() throws StdQTException {
        StdQTException.checkError(NASaveMusicConfiguration(_ID()));
    }

    /**
     * Get the MIDI input ports that are available to the NoteAllocator.
     * @see "QuickTime::NAGetMIDIPorts()"
     * @return the MIDI input ports list.
     * @throws quicktime.QTException
     */
    public QTMIDIPortList getMIDIInPorts() throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAGetMIDIPorts(_ID(), arrayOfInt, null));
        return new QTMIDIPortList(arrayOfInt[0]);
    }

    /**
     * Get the MIDI output ports that are available to the NoteAllocator.
     * @see "QuickTime::NAGetMIDIPorts()"
     * @return the MIDI output ports list.
     * @throws quicktime.QTException
     */
    public QTMIDIPortList getMIDIOutPorts() throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAGetMIDIPorts(_ID(), null, arrayOfInt));
        return new QTMIDIPortList(arrayOfInt[0]);
    }

    /**
     * Removes the external default MIDI service procedure call, if previously defined by
     * the useDefaultMIDIInput call.
     * @see "QuickTime::NALoseDefaultMIDIInput()"
     */
    public void loseDefaultMIDIInput() throws StdQTException {
        StdQTException.checkError(NALoseDefaultMIDIInput(_ID()));
    }

    private static int NAGetIndNoteChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return MusicManager.NAGetIndNoteChannel(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int NATask(int paramInt) {
        return MusicManager.NATask(paramInt);
    }

    private static int NAPickArrangement(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte2) {
        return MusicManager.NAPickArrangement(paramInt1, paramInt2, paramArrayOfByte1, paramInt3, paramInt4, paramInt5, paramArrayOfByte2);
    }

    private static int NACopyrightDialog(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt3, int paramInt4) {
        return MusicManager.NACopyrightDialog(paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4, paramInt3, paramInt4);
    }

    private static int NAGetRegisteredMusicDevice(int paramInt1, int paramInt2, int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) {
        return MusicManager.NAGetRegisteredMusicDevice(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfInt2);
    }

    private static int NARegisterMusicDevice(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return MusicManager.NARegisterMusicDevice(paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int NAUnregisterMusicDevice(int paramInt1, int paramInt2) {
        return MusicManager.NAUnregisterMusicDevice(paramInt1, paramInt2);
    }

    private static int NAGetDefaultMIDIInput(int paramInt, byte[] paramArrayOfByte) {
        return MusicManager.NAGetDefaultMIDIInput(paramInt, paramArrayOfByte);
    }

    private static int NASetDefaultMIDIInput(int paramInt, byte[] paramArrayOfByte) {
        return MusicManager.NASetDefaultMIDIInput(paramInt, paramArrayOfByte);
    }

    private static int NASaveMusicConfiguration(int paramInt) {
        return MusicManager.NASaveMusicConfiguration(paramInt);
    }

    private static int NAGetMIDIPorts(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return MusicManager.NAGetMIDIPorts(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int NALoseDefaultMIDIInput(int paramInt) {
        return MusicManager.NALoseDefaultMIDIInput(paramInt);
    }
}
