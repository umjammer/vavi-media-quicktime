package quicktime.std.music;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MusicManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.comp.Component;
import quicktime.std.movies.media.MusicMediaHandler;
import quicktime.util.QTUtils;


/**
 * Corresponds to the TunePlayer component type.
 */
public final class TunePlayer extends Component implements QuickTimeLib {

    Object noteChannels;

    /**
     * Returns the TunePlayer that is responsible for rendering the music data that is contained
     * within a particular music track. If the movie or this media is edited in any way then the
     * returned TunePlayer becomes invalid and must be got again.
     * @see "QuickTime::MusicMediaGetIndexedTunePlayer()"
     * @param index the index within the media.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.MusicMediaHandler#getIndexedTunePlayer(int)
     */
    public static TunePlayer fromMusicMediaHandler(MusicMediaHandler ti, int index) throws StdQTException {
        int[] tp = { 0 };
        StdQTException.checkError(MusicMediaGetIndexedTunePlayer(QTObject.ID(ti), index, tp));
        return new TunePlayer(tp[0], ti);
    }

    private TunePlayer(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Opens the TunePlayer component.
     * @see "QuickTime::OpenADefaultComponent"
     */
    public TunePlayer() throws QTException {
        super(1953853029, 0);
    }

    /**
     * Prepares the tune player to accept subsequent music event sequences
     * by defining one or more parts to be used by sequence Note events.
     * @see "QuickTime::TuneSetHeader"
     * @param header the tune header data
     * @throws quicktime.std.StdQTException
     */
    public void setHeader(MusicData header) throws StdQTException {
        header.lock();
        int i = getIntFromPointer(QTObject.ID(header), 0);
        StdQTException.checkError(TuneSetHeader(_ID(), i));
    }

    /**
     * Returns the TunePlayer's current time Base.
     * @see "QuickTime::TuneGetTimeBase"
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     */
    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromTunePlayer(this);
    }

    /**
     * Sets the time scale used by the tune player.
     * @see "QuickTime::TuneSetTimeScale"
     * @param scale the new scale
     * @throws quicktime.std.StdQTException
     */
    public void setTimeScale(int scale) throws StdQTException {
        StdQTException.checkError(TuneSetTimeScale(_ID(), scale));
    }

    /**
     * Returns the current time scale of the tune player, in units-per-second.
     * @see "QuickTime::TuneGetTimeScale"
     * @return the current scale
     * @throws quicktime.std.StdQTException
     */
    public int getTimeScale() throws StdQTException {
        int[] scale = { 0 };
        StdQTException.checkError(TuneGetTimeScale(_ID(), scale));
        return scale[0];
    }

    /**
     * Returns the number of note channels that the tune player is using.
     * @see "QuickTime::TuneGetIndexedNoteChannel"
     * @return the number of note channels
     * @throws quicktime.std.StdQTException
     */
    public int getNumberOfNoteChannels() throws StdQTException {
        int i = TuneGetIndexedNoteChannel(_ID(), 0, null);
        if (i < 0)
            throw new StdQTException(i);
        return i;
    }

    /**
     * Returns the note channel at the specified index.
     * @see "QuickTime::TuneGetIndexedNoteChannel"
     * @param index the part for which you want the note channel
     * @return the note channel
     * @throws quicktime.std.StdQTException
     */
    public NoteChannel getIndexedNoteChannel(int index) throws StdQTException {
        if (index > 0) {
            int[] nc = { 0 };
            int i = TuneGetIndexedNoteChannel(_ID(), index, nc);
            if (i < 0)
                throw new StdQTException(i);
            return new NoteChannel(getNoteAllocator(), nc[0], this);
        }
        throw new StdQTException(63461);
    }

    /**
     * Places a sequence of music events into a queue to be played.
     * @see "QuickTime::TuneQueue"
     * @param tune the events to be played
     * @param tuneRate the playback rate at which to play the events
     * @param tuneStartPosition sequence starting time
     * @param tuneStopPosition sequence ending time
     * @throws quicktime.std.StdQTException
     */
    public void queue(MusicData tune, float tuneRate, int tuneStartPosition, int tuneStopPosition, int queueFlags) throws StdQTException {
        tune.lock();
        int pointer = getIntFromPointer(QTObject.ID(tune), 0);
        StdQTException.checkError(TuneQueue(_ID(), pointer, QTUtils.X2Fix(tuneRate), tuneStartPosition, tuneStopPosition, queueFlags, 0, 0));
    }

    /**
     * Play the sequence of music events at a specified position.
     * @see "QuickTime::TuneInstant"
     * @param tune the tune data is contained within the TuneStatus object
     * @param tunePosition the position within tune sequence data in time units
     * @throws quicktime.std.StdQTException
     */
    public void instant(TuneStatus tune, int tunePosition) throws StdQTException {
        StdQTException.checkError(TuneInstant(_ID(), tune.getTuneDataPtr(), tunePosition));
    }

    /**
     * Returns an initialised structure describing the state of the tune player.
     * @see "QuickTime::TuneGetStatus"
     * @return a tune status object
     * @throws quicktime.std.StdQTException
     */
    public TuneStatus getStatus() throws StdQTException {
        TuneStatus status = new TuneStatus();
        StdQTException.checkError(TuneGetStatus(_ID(), status.getBytes()));
        return status;
    }

    /**
     * Stops a currently playing sequence.
     * @see "QuickTime::TuneStop"
     */
    public void stop() throws StdQTException {
        StdQTException.checkError(TuneStop(_ID(), 0));
    }

    /**
     * Sets the volume for the entire sequence.
     * @see "QuickTime::TuneSetVolume"
     * @param volume the new volume
     * @throws quicktime.std.StdQTException
     */
    public void setVolume(float volume) throws StdQTException {
        StdQTException.checkError(TuneSetVolume(_ID(), QTUtils.X2Fix(volume)));
    }

    /**
     * Returns the volume of the sequence.
     * @see "QuickTime::TuneGetVolume"
     * @return the current volume
     * @throws quicktime.std.StdQTException
     */
    public float getVolume() throws StdQTException {
        int i = TuneGetVolume(_ID());
        if (i < 0)
            throw new StdQTException(i);
        return QTUtils.Fix2X(i);
    }

    /**
     * Prepares the tune player by attempting to reserve note channels for each part in the sequence.
     * @see "QuickTime::TunePreroll"
     */
    public void preroll() throws StdQTException {
        StdQTException.checkError(TunePreroll(_ID()));
    }

    /**
     * Releases any note channels that may have been allocated by previous calls to preroll.
     * @see "QuickTime::TuneUnroll"
     */
    public void unroll() throws StdQTException {
        StdQTException.checkError(TuneUnroll(_ID()));
    }

    /**
     * Prepares the tune player to accept music event sequences by allocating specified note channels for them.
     * @see "QuickTime::TuneSetNoteChannels"
     * @param noteChannelList the note channels to be set for the tune player
     * @throws quicktime.std.StdQTException
     */
    public void setNoteChannels(NoteChannel[] noteChannelList) throws StdQTException {
        int len = noteChannelList.length;
        int[] arrayOfInt = new int[len];
        for (int i = 0; i < len; i++) arrayOfInt[i] = QTObject.ID(noteChannelList[i]);
        StdQTException.checkError(TuneSetNoteChannels(_ID(), len, arrayOfInt, 0, 0));
        this.noteChannels = noteChannelList;
    }

    /**
     * Modifies the pitch and velocity of every note of the specified part of a tune.
     * @see "QuickTime::TuneSetPartTranspose"
     * @param part the part number for which you want the pitch and velocity changed
     * @param transpose the value by which to modify the pitch of the note - can specify fractional tuning
     * @param velocityShift a value to add to the velocity of each note played
     * @throws quicktime.std.StdQTException
     */
    public void setPartTranspose(int part, float transpose, int velocityShift) throws StdQTException {
        StdQTException.checkError(TuneSetPartTranspose(_ID(), part, QTUtils.X2Fix(transpose), velocityShift));
    }

    /**
     * Returns the NoteAllocator that the tune player is using.
     * @see "QuickTime::TuneGetNoteAllocator"
     * @return the note allocator
     */
    public NoteAllocator getNoteAllocator() {
        return new NoteAllocator(TuneGetNoteAllocator(_ID()), this);
    }

    /**
     * Adjusts the volume to play at a generally softer value that was the normal value
     * for QT 2_1, otherwise sets the volume to a normal level.
     * @see "QuickTime::TuneSetSofter"
     * @param softer if true it sets the overall volume to a softer level, if false sets it to normal
     * @throws quicktime.std.StdQTException
     */
    public void setSofter(boolean softer) throws StdQTException {
        StdQTException.checkError(TuneSetSofter(_ID(), softer ? 1 : 0));
    }

    /**
     * Call this method periodically to allow the tune player to perform tasks it must perform
     * at foreground task time.
     * @see "QuickTime::TuneTask"
     */
    public void task() throws StdQTException {
        StdQTException.checkError(TuneTask(_ID()));
    }

    /**
     * Modifies the pan controller setting for a tune player.
     * @see "QuickTime::TuneSetBalance"
     * @param balance the new balance setting: range is -128 (left) to 127 (right)
     * @throws quicktime.std.StdQTException
     */
    public void setBalance(int balance) throws StdQTException {
        StdQTException.checkError(TuneSetBalance(_ID(), balance));
    }

    /**
     * Passes sound localisation data to the tune player.
     * @see "QuickTime::TuneSetSoundLocalization"
     * @param data the sound localisation data
     * @throws quicktime.std.StdQTException
     */
    public void setSoundLocalization(SoundLocalization data) throws StdQTException {
        StdQTException.checkError(TuneSetSoundLocalization(_ID(), QTObject.ID(data)));
    }

    /**
     * Prepares the tune player to accept subsequence music events by defining one
     * or more parts to be used. You specify the end of the header with the size parameter
     * so that the parsing will not overflow if there is no end marker.
     * @see "QuickTime::TuneSetHeaderWithSize"
     * @param header a list of general events
     * @param size the size of the header in bytes
     * @throws quicktime.std.StdQTException
     */
    public void setHeaderWithSize(MusicData header, int size) throws StdQTException {
        header.lock();
        int i = getIntFromPointer(QTObject.ID(header), 0);
        StdQTException.checkError(TuneSetHeaderWithSize(_ID(), i, size));
    }

    /**
     * Alters the volume and the balance of the specified part, with the flags specifying special conditions.
     * @see "QuickTime::TuneSetPartMix"
     * @param partNumber the part to effect
     * @param volume the new volume of the specified part
     * @param balance the new balance of the specified part
     * @param mixFlags the mix flags that will apply to that part - mute or solo part.
     * @throws quicktime.std.StdQTException
     */
    public void setPartMix(int partNumber, float volume, float balance, int mixFlags) throws StdQTException {
        StdQTException.checkError(TuneSetPartMix(_ID(), partNumber, QTUtils.X2Fix(volume), QTUtils.X2Fix(balance), mixFlags));
    }

    /**
     * Returns the current state of a part in a mix, its volume, balance, and mute/solo state.
     * @see "QuickTime::TuneGetPartMix"
     * @param partNumber the part number you require information about
     * @return the mix state of a part
     * @throws quicktime.std.StdQTException
     */
    public MixStateInfo getPartMix(int partNumber) throws StdQTException {
        int[] volumeOut = { 0 };
        int[] balanceOut = { 0 };
        int[] mixFlagsOut = { 0 };
        StdQTException.checkError(TuneGetPartMix(_ID(), partNumber, volumeOut, balanceOut, mixFlagsOut));
        return new MixStateInfo(partNumber, QTUtils.Fix2X(volumeOut[0]), QTUtils.Fix2X(balanceOut[0]), mixFlagsOut[0]);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static int MusicMediaGetIndexedTunePlayer(int ti, int sampleDescIndex, int[] tp) {
        return MusicManager.MusicMediaGetIndexedTunePlayer(ti, sampleDescIndex, tp);
    }

    private static int TuneSetHeader(int tp, int header) {
        return MusicManager.TuneSetHeader(tp, header);
    }

    private static int TuneSetTimeScale(int tp, int scale) {
        return MusicManager.TuneSetTimeScale(tp, scale);
    }

    private static int TuneGetTimeScale(int tp, int[] scale) {
        return MusicManager.TuneGetTimeScale(tp, scale);
    }

    private static int TuneGetIndexedNoteChannel(int tp, int i, int[] nc) {
        return MusicManager.TuneGetIndexedNoteChannel(tp, i, nc);
    }

    private static int TuneQueue(int tp, int tune, int tuneRate, int tuneStartPosition, int tuneStopPosition, int queueFlags, int callBackProc, int refCon) {
        return MusicManager.TuneQueue(tp, tune, tuneRate, tuneStartPosition, tuneStopPosition, queueFlags, callBackProc, refCon);
    }

    private static int TuneInstant(int tp, int tune, int tunePosition) {
        return MusicManager.TuneInstant(tp, tune, tunePosition);
    }

    private static int TuneGetStatus(int tp, byte[] status) {
        return MusicManager.TuneGetStatus(tp, status);
    }

    private static int TuneStop(int tp, int stopFlags) {
        return MusicManager.TuneStop(tp, stopFlags);
    }

    private static int TuneSetVolume(int tp, int volume) {
        return MusicManager.TuneSetVolume(tp, volume);
    }

    private static int TuneGetVolume(int tp) {
        return MusicManager.TuneGetVolume(tp);
    }

    private static int TunePreroll(int tp) {
        return MusicManager.TunePreroll(tp);
    }

    private static int TuneUnroll(int tp) {
        return MusicManager.TuneUnroll(tp);
    }

    private static int TuneSetNoteChannels(int tp, int count, int[] noteChannelList, int playCallBackProc, int refCon) {
        return MusicManager.TuneSetNoteChannels(tp, count, noteChannelList, playCallBackProc, refCon);
    }

    private static int TuneSetPartTranspose(int tp, int part, int transpose, int velocityShift) {
        return MusicManager.TuneSetPartTranspose(tp, part, transpose, velocityShift);
    }

    private static int TuneGetNoteAllocator(int tp) {
        return MusicManager.TuneGetNoteAllocator(tp);
    }

    private static int TuneSetSofter(int tp, int softer) {
        return MusicManager.TuneSetSofter(tp, softer);
    }

    private static int TuneTask(int tp) {
        return MusicManager.TuneTask(tp);
    }

    private static int TuneSetBalance(int tp, int balance) {
        return MusicManager.TuneSetBalance(tp, balance);
    }

    private static int TuneSetSoundLocalization(int tp, int data) {
        return MusicManager.TuneSetSoundLocalization(tp, data);
    }

    private static int TuneSetHeaderWithSize(int tp, int header, int size) {
        return MusicManager.TuneSetHeaderWithSize(tp, header, size);
    }

    private static int TuneSetPartMix(int tp, int partNumber, int volume, int balance, int mixFlags) {
        return MusicManager.TuneSetPartMix(tp, partNumber, volume, balance, mixFlags);
    }

    private static int TuneGetPartMix(int tp, int partNumber, int[] volumeOut, int[] balanceOut, int[] mixFlagsOut) {
        return MusicManager.TuneGetPartMix(tp, partNumber, volumeOut, balanceOut, mixFlagsOut);
    }
}
