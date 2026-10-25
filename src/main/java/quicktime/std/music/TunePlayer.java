package quicktime.std.music;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MusicManager;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    Object noteChannels;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(TunePlayer.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Returns the TunePlayer that is responsible for rendering the music data that is contained
     * within a particular music track. If the movie or this media is edited in any way then the
     * returned TunePlayer becomes invalid and must be got again.
     * QuickTime::MusicMediaGetIndexedTunePlayer()
     * @param index the index within the media.
     * @param {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.media.MusicMediaHandler#getIndexedTunePlayer(int)}
     */
    public static TunePlayer fromMusicMediaHandler(MusicMediaHandler paramMusicMediaHandler, int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MusicMediaGetIndexedTunePlayer(QTObject.ID(paramMusicMediaHandler), paramInt, arrayOfInt));
        return new TunePlayer(arrayOfInt[0], paramMusicMediaHandler);
    }

    private TunePlayer(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Opens the TunePlayer component.
     * QuickTime::OpenADefaultComponent
     */
    public TunePlayer() throws QTException {
        super(1953853029, 0);
    }

    /**
     * Prepares the tune player to accept subsequent music event sequences
     * by defining one or more parts to be used by sequence Note events.
     * QuickTime::TuneSetHeader
     * @param header the tune header data
     * @param {@link quicktime.std.StdQTException}
     */
    public void setHeader(MusicData paramMusicData) throws StdQTException {
        paramMusicData.lock();
        int i = getIntFromPointer(QTObject.ID(paramMusicData), 0);
        StdQTException.checkError(TuneSetHeader(_ID(), i));
    }

    /**
     * Returns the TunePlayer's current time Base.
     * QuickTime::TuneGetTimeBase
     * @return the TimeBase
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromTunePlayer(this);
    }

    /**
     * Sets the time scale used by the tune player.
     * QuickTime::TuneSetTimeScale
     * @param scale the new scale
     * @param {@link quicktime.std.StdQTException}
     */
    public void setTimeScale(int paramInt) throws StdQTException {
        StdQTException.checkError(TuneSetTimeScale(_ID(), paramInt));
    }

    /**
     * Returns the current time scale of the tune player, in units-per-second.
     * QuickTime::TuneGetTimeScale
     * @return the current scale
     * @return {@link quicktime.std.StdQTException}
     */
    public int getTimeScale() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(TuneGetTimeScale(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Returns the number of note channels that the tune player is using.
     * QuickTime::TuneGetIndexedNoteChannel
     * @return the number of note channels
     * @return {@link quicktime.std.StdQTException}
     */
    public int getNumberOfNoteChannels() throws StdQTException {
        int i = TuneGetIndexedNoteChannel(_ID(), 0, null);
        if (i < 0)
            throw new StdQTException(i);
        return i;
    }

    /**
     * Returns the note channel at the specified index.
     * QuickTime::TuneGetIndexedNoteChannel
     * @param index the part for which you want the note channel
     * @return the note channel
     * @return {@link quicktime.std.StdQTException}
     */
    public NoteChannel getIndexedNoteChannel(int paramInt) throws StdQTException {
        if (paramInt > 0) {
            int[] arrayOfInt = { 0 };
            int i = TuneGetIndexedNoteChannel(_ID(), paramInt, arrayOfInt);
            if (i < 0)
                throw new StdQTException(i);
            return new NoteChannel(getNoteAllocator(), arrayOfInt[0], this);
        }
        throw new StdQTException(63461);
    }

    /**
     * Places a sequence of music events into a queue to be played.
     * QuickTime::TuneQueue
     * @param tune the events to be played
     * @param tuneRate the playback rate at which to play the events
     * @param tuneStartPosition sequence starting time
     * @param tuneStopPosition sequence ending time
     * @param {@link quicktime.std.StdQTException}
     */
    public void queue(MusicData paramMusicData, float paramFloat, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        paramMusicData.lock();
        int i = getIntFromPointer(QTObject.ID(paramMusicData), 0);
        StdQTException.checkError(TuneQueue(_ID(), i, QTUtils.X2Fix(paramFloat), paramInt1, paramInt2, paramInt3, 0, 0));
    }

    /**
     * Play the sequence of music events at a specified position.
     * QuickTime::TuneInstant
     * @param tune the tune data is contained within the TuneStatus object
     * @param tunePosition the position within tune sequence data in time units
     * @param {@link quicktime.std.StdQTException}
     */
    public void instant(TuneStatus paramTuneStatus, int paramInt) throws StdQTException {
        StdQTException.checkError(TuneInstant(_ID(), paramTuneStatus.getTuneDataPtr(), paramInt));
    }

    /**
     * Returns an initialised structure describing the state of the tune player.
     * QuickTime::TuneGetStatus
     * @return a tune status object
     * @return {@link quicktime.std.StdQTException}
     */
    public TuneStatus getStatus() throws StdQTException {
        TuneStatus localTuneStatus = new TuneStatus();
        StdQTException.checkError(TuneGetStatus(_ID(), localTuneStatus.getBytes()));
        return localTuneStatus;
    }

    /**
     * Stops a currently playing sequence.
     * QuickTime::TuneStop
     */
    public void stop() throws StdQTException {
        StdQTException.checkError(TuneStop(_ID(), 0));
    }

    /**
     * Sets the volume for the entire sequence.
     * QuickTime::TuneSetVolume
     * @param volume the new volume
     * @param {@link quicktime.std.StdQTException}
     */
    public void setVolume(float paramFloat) throws StdQTException {
        StdQTException.checkError(TuneSetVolume(_ID(), QTUtils.X2Fix(paramFloat)));
    }

    /**
     * Returns the volume of the sequence.
     * QuickTime::TuneGetVolume
     * @return the current volume
     * @return {@link quicktime.std.StdQTException}
     */
    public float getVolume() throws StdQTException {
        int i = TuneGetVolume(_ID());
        if (i < 0)
            throw new StdQTException(i);
        return QTUtils.Fix2X(i);
    }

    /**
     * Prepares the tune player by attempting to reserve note channels for each part in the sequence.
     * QuickTime::TunePreroll
     */
    public void preroll() throws StdQTException {
        StdQTException.checkError(TunePreroll(_ID()));
    }

    /**
     * Releases any note channels that may have been allocated by previous calls to preroll.
     * QuickTime::TuneUnroll
     */
    public void unroll() throws StdQTException {
        StdQTException.checkError(TuneUnroll(_ID()));
    }

    /**
     * Prepares the tune player to accept music event sequences by allocating specified note channels for them.
     * QuickTime::TuneSetNoteChannels
     * @param noteChannelList the note channels to be set for the tune player
     * @param {@link quicktime.std.StdQTException}
     */
    public void setNoteChannels(NoteChannel[] paramArrayOfNoteChannel) throws StdQTException {
        int i = paramArrayOfNoteChannel.length;
        int[] arrayOfInt = new int[i];
        for (int j = 0; j < i; j++) arrayOfInt[j] = QTObject.ID(paramArrayOfNoteChannel[j]);
        StdQTException.checkError(TuneSetNoteChannels(_ID(), i, arrayOfInt, 0, 0));
        this.noteChannels = paramArrayOfNoteChannel;
    }

    /**
     * Modifies the pitch and velocity of every note of the specified part of a tune.
     * QuickTime::TuneSetPartTranspose
     * @param part the part number for which you want the pitch and velocity changed
     * @param transpose the value by which to modify the pitch of the note - can specify fractional tuning
     * @param velocityShift a value to add to the velocity of each note played
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPartTranspose(int paramInt1, float paramFloat, int paramInt2) throws StdQTException {
        StdQTException.checkError(TuneSetPartTranspose(_ID(), paramInt1, QTUtils.X2Fix(paramFloat), paramInt2));
    }

    /**
     * Returns the NoteAllocator that the tune player is using.
     * QuickTime::TuneGetNoteAllocator
     * @return the note allocator
     */
    public NoteAllocator getNoteAllocator() {
        return new NoteAllocator(TuneGetNoteAllocator(_ID()), this);
    }

    /**
     * Adjusts the volume to play at a generally softer value that was the normal value
     * for QT 2_1, otherwise sets the volume to a normal level.
     * QuickTime::TuneSetSofter
     * @param softer if true it sets the overall volume to a softer level, if false sets it to normal
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSofter(boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(TuneSetSofter(_ID(), paramBoolean ? 1 : 0));
    }

    /**
     * Call this method periodically to allow the tune player to perform tasks it must perform
     * at foreground task time.
     * QuickTime::TuneTask
     */
    public void task() throws StdQTException {
        StdQTException.checkError(TuneTask(_ID()));
    }

    /**
     * Modifies the pan controller setting for a tune player.
     * QuickTime::TuneSetBalance
     * @param balance the new balance setting: range is -128 (left) to 127 (right)
     * @param {@link quicktime.std.StdQTException}
     */
    public void setBalance(int paramInt) throws StdQTException {
        StdQTException.checkError(TuneSetBalance(_ID(), paramInt));
    }

    /**
     * Passes sound localisation data to the tune player.
     * QuickTime::TuneSetSoundLocalization
     * @param data the sound localisation data
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSoundLocalization(SoundLocalization paramSoundLocalization) throws StdQTException {
        StdQTException.checkError(TuneSetSoundLocalization(_ID(), QTObject.ID(paramSoundLocalization)));
    }

    /**
     * Prepares the tune player to accept subsequence music events by defining one
     * or more parts to be used. You specify the end of the header with the size parameter
     * so that the parsing will not overflow if there is no end marker.
     * QuickTime::TuneSetHeaderWithSize
     * @param header a list of general events
     * @param size the size of the header in bytes
     * @param {@link quicktime.std.StdQTException}
     */
    public void setHeaderWithSize(MusicData paramMusicData, int paramInt) throws StdQTException {
        paramMusicData.lock();
        int i = getIntFromPointer(QTObject.ID(paramMusicData), 0);
        StdQTException.checkError(TuneSetHeaderWithSize(_ID(), i, paramInt));
    }

    /**
     * Alters the volume and the balance of the specified part, with the flags specifying special conditions.
     * QuickTime::TuneSetPartMix
     * @param partNumber the part to effect
     * @param volume the new volume of the specified part
     * @param balance the new balance of the specified part
     * @param mixFlags the mix flags that will apply to that part - mute or solo part.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPartMix(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2) throws StdQTException {
        StdQTException.checkError(TuneSetPartMix(_ID(), paramInt1, QTUtils.X2Fix(paramFloat1), QTUtils.X2Fix(paramFloat2), paramInt2));
    }

    /**
     * Returns the current state of a part in a mix, its volume, balance, and mute/solo state.
     * QuickTime::TuneGetPartMix
     * @param partNumber the part number you require information about
     * @return the mix state of a part
     * @return {@link quicktime.std.StdQTException}
     */
    public MixStateInfo getPartMix(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        StdQTException.checkError(TuneGetPartMix(_ID(), paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3));
        return new MixStateInfo(paramInt, QTUtils.Fix2X(arrayOfInt1[0]), QTUtils.Fix2X(arrayOfInt2[0]), arrayOfInt3[0]);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static int MusicMediaGetIndexedTunePlayer(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MusicManager.MusicMediaGetIndexedTunePlayer(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int TuneSetHeader(int paramInt1, int paramInt2) {
        return MusicManager.TuneSetHeader(paramInt1, paramInt2);
    }

    private static int TuneSetTimeScale(int paramInt1, int paramInt2) {
        return MusicManager.TuneSetTimeScale(paramInt1, paramInt2);
    }

    private static int TuneGetTimeScale(int paramInt, int[] paramArrayOfInt) {
        return MusicManager.TuneGetTimeScale(paramInt, paramArrayOfInt);
    }

    private static int TuneGetIndexedNoteChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MusicManager.TuneGetIndexedNoteChannel(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int TuneQueue(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        return MusicManager.TuneQueue(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    }

    private static int TuneInstant(int paramInt1, int paramInt2, int paramInt3) {
        return MusicManager.TuneInstant(paramInt1, paramInt2, paramInt3);
    }

    private static int TuneGetStatus(int paramInt, byte[] paramArrayOfByte) {
        return MusicManager.TuneGetStatus(paramInt, paramArrayOfByte);
    }

    private static int TuneStop(int paramInt1, int paramInt2) {
        return MusicManager.TuneStop(paramInt1, paramInt2);
    }

    private static int TuneSetVolume(int paramInt1, int paramInt2) {
        return MusicManager.TuneSetVolume(paramInt1, paramInt2);
    }

    private static int TuneGetVolume(int paramInt) {
        return MusicManager.TuneGetVolume(paramInt);
    }

    private static int TunePreroll(int paramInt) {
        return MusicManager.TunePreroll(paramInt);
    }

    private static int TuneUnroll(int paramInt) {
        return MusicManager.TuneUnroll(paramInt);
    }

    private static int TuneSetNoteChannels(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4) {
        return MusicManager.TuneSetNoteChannels(paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4);
    }

    private static int TuneSetPartTranspose(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MusicManager.TuneSetPartTranspose(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int TuneGetNoteAllocator(int paramInt) {
        return MusicManager.TuneGetNoteAllocator(paramInt);
    }

    private static int TuneSetSofter(int paramInt1, int paramInt2) {
        return MusicManager.TuneSetSofter(paramInt1, paramInt2);
    }

    private static int TuneTask(int paramInt) {
        return MusicManager.TuneTask(paramInt);
    }

    private static int TuneSetBalance(int paramInt1, int paramInt2) {
        return MusicManager.TuneSetBalance(paramInt1, paramInt2);
    }

    private static int TuneSetSoundLocalization(int paramInt1, int paramInt2) {
        return MusicManager.TuneSetSoundLocalization(paramInt1, paramInt2);
    }

    private static int TuneSetHeaderWithSize(int paramInt1, int paramInt2, int paramInt3) {
        return MusicManager.TuneSetHeaderWithSize(paramInt1, paramInt2, paramInt3);
    }

    private static int TuneSetPartMix(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MusicManager.TuneSetPartMix(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static int TuneGetPartMix(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        return MusicManager.TuneGetPartMix(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /TunePlayer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
