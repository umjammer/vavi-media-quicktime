package quicktime.std.sg;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.GDevice;
import quicktime.qd.Pict;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.comp.Component;
import quicktime.std.movies.Movie;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.UserData;


/**
 * Assists in importing sound and video into a movie.
 * The SeqGrabComponent implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 * refer to SeqGrabComponent in QuickTime
 */
public final class SequenceGrabber extends Component implements QuickTimeLib {

    private SGDispatcher upp;

    private boolean hasStarted;

    /**
     * Opens the specified movie export component,'barg', with any subType.
     *
     * @see "QuickTime::OpenADefaultComponent"
     * @see "QuickTime::SGInitialize"
     */
    public SequenceGrabber() throws QTException {
        this(0);
    }

    /**
     * Opens the specified movie export component,'barg', with the given subType.
     *
     * @see "QuickTime::OpenADefaultComponent"
     * @see "QuickTime::SGInitialize"
     * @param subType The movie export component sub type
     */
    public SequenceGrabber(int subType) throws QTException {
        super(1650553447, subType);
        initialize();
    }

    /**
     * Opens the specified Sequence grabber component based on the type and Subtype
     *
     * @see "QuickTime::OpenADefaultComponent"
     * @see "QuickTime::SGInitialize"
     * @param subType The movie export component sub type
     */
    public SequenceGrabber(int type, int subType) throws QTException {
        super(type, subType);
        initialize();
    }

    private void initialize() throws QTException {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to capture media with security settings when class is signed");
        }
        int i = 0;
        int[] port = { 0 };
        int[] gdh = { 0 };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(port, gdh);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            setGWorld(QDGraphics.scratch, null);
            i = SGInitialize(_ID());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(port[0], gdh[0]);
            this.hasStarted = false;
        }
        if (i < 0) {
            try {
                _dispose();
            } catch (Throwable _) {
            }
            StdQTException.checkError(i);
        }
    }

    /**
     * Establishes the graphics port and device for a sequence grabber.
     * The sequence grabber component displays the recorded or previewed data in this graphics world.
     *
     * @see "QuickTime::SGSetGWorld"
     * @param port Specifies the destination graphics port.
     *     The specified graphics port must be a color graphics port.
     * @param gdh Specifies the destination graphics device.
     *     Set this parameter to null to use the current device.
     *     If the gp parameter specifies a graphics world, set this parameter
     *     to null to use that graphics world's graphics device.
     * @throws quicktime.std.StdQTException
     */
    public void setGWorld(QDGraphics port, GDevice gdh) throws StdQTException {
        if (port == null)
            throw new StdQTException(-50);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGSetGWorld(_ID(), QTObject.ID(port), QTObject.ID(gdh));
        }
        StdQTException.checkError(i);
    }

    /**
     * Returns the QDGraphics of the sequence grabber.
     * @see "QuickTime::SGGetGWorld"
     * @return The current graf port of the sequence grabber.
     * @throws quicktime.std.StdQTException
     */
    public QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromSequenceGrabber(this);
    }

    /**
     * Returns the current device of the sequence grabber.
     * @see "QuickTime::SGGetGWorld"
     * @return The current device of the sequence grabber.
     * @throws quicktime.std.StdQTException
     */
    public GDevice getGWorldDevice() throws StdQTException {
        return GDevice.fromSequenceGrabber(this);
    }

    /**
     * Removes a channel from a sequence grabber component.
     * @see "QuickTime::SGDisposeChannel"
     * @param c the channel you want to dispose
     * @throws quicktime.std.StdQTException
     */
    public void disposeChannel(SGChannel c) throws StdQTException {
        c.disposeChannel(this);
    }

    /**
     * The SGNewOutput function creates a new sequence grabber output. You specify
     * the outputÕs destination container using a data reference. Once you have
     * created the sequence grabber output, you can use the SGSetChannelOutput
     * function to assign the output to a sequence grabber channel.
     *
     * @see "QuickTime::SGNewOutput"
     * @param dataRef Specifies the type of channel.
     * @param whereFlags Specifies the type of channel.
     * @throws quicktime.std.StdQTException
     */
    public SGOutput newOutput(DataRef dataRef, int whereFlags) throws StdQTException {
        return new SGOutput(this, QTObject.ID(dataRef), dataRef.getType(), whereFlags);
    }

    /**
     * Disposes of an output associated with a sequence grabber.
     * @see "QuickTime::SGDisposeOutput"
     */
    public void disposeOutput(SGOutput sgOutput) throws QTException {
        sgOutput.disposeQTObject();
    }

    boolean isValid() {
        return stillValid();
    }

    /**
     * Allows your channel component to add data to a movie.
     * @see "QuickTime::SGWriteExtendedMovieData("
     * @param p -
     * @param len -
     * @param offset offset to the new data in the movie. If the movie is in memory, the
     *  returned offset reflects the location the data will have in the movie on a permanent storage device.
     * @param sgOut output to which the data was written.
     *  associated with the sequence grabber output.
     * @throws quicktime.std.StdQTException
     */
    public void writeExtendedMovieData(SGChannel ref, int p, int len, long offset, SGOutput sgOut) throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        setLongInArray(arrayOfByte, 0, offset);
        int i = SGWriteExtendedMovieData(_ID(), QTObject.ID(ref), p, len, arrayOfByte, QTObject.ID(sgOut));
        StdQTException.checkError(i);
    }

    /**
     * Instructs the sequence grabber to begin processing data from its channels.
     * @see "QuickTime::SGStartPreview"
     */
    public void startPreview() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGStartPreview(_ID());
            this.hasStarted = true;
        }
        StdQTException.checkError(i);
    }

    /**
     * Instructs the sequence grabber component to begin collecting data from its channels.
     * @see "QuickTime::SGStartRecord"
     */
    public void startRecord() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SGStartRecord(_ID());
            StdQTException.checkError(i);
            this.hasStarted = true;
        }
    }

    /**
     * Provides processing time to the sequence grabber component and its channel components.
     * After starting a preview or record operation, you should call this function as often
     * as possible, until you stop the operation by calling stop.
     * @see "QuickTime::SGIdle"
     */
    public void idle() throws StdQTException {
        int i = 0;
        int[] port = { 0 };
        int[] gdh = { 0 };
        if (this.hasStarted) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(port, gdh);
                i = SGIdle(_ID());
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    SetGWorld(port[0], gdh[0]);
                }
            }
        }
        StdQTException.checkError(i);
    }

    /**
     * Provides processing time to the sequence grabber component and its channel components.
     * After starting a preview or record operation, you should call this function as often
     * as possible, until you stop the operation by calling stop. If the sequence grabber
     * has a stop time set, then this method will return false when the sequence grabber
     * has captured media for the specified time. At that point the application should
     * stop the sequence grabber and not call idle until restarted.
     * It will return true when further idling is required.
     * @see "QuickTime::SGIdle"
     * @return true if requires more idling, false if comleted.
     * @throws quicktime.std.StdQTException
     */
    public boolean idleMore() throws StdQTException {
        int i = 0;
        int[] port = { 0 };
        int[] gdh = { 0 };
        if (this.hasStarted) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(port, gdh);
                i = SGIdle(_ID());
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    SetGWorld(port[0], gdh[0]);
                }
            }
        }
        if (i == 56135)
            return false;
        StdQTException.checkError(i);
        return true;
    }

    /**
     * The SGUpdate function allows you to tell the sequence grabber that it must refresh its display.
     * @see "QuickTime::SGUpdate"
     * @param updateRgn Indicates the part of the QDGraphics that has been changed.
     *  If you set this parameter to null, the sequence grabber uses the windowÕs current visible region
     * @throws quicktime.std.StdQTException
     */
    public void update(Region updateRgn) throws StdQTException {
        int i = 0;
        int[] port = { 0 };
        int[] gdh = { 0 };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(port, gdh);
            i = SGUpdate(_ID(), QTObject.ID(updateRgn));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(port[0], gdh[0]);
            }
        }
        StdQTException.checkError(i);
    }

    /**
     * Stops a preview or record operation.
     * @see "QuickTime::SGStop"
     */
    public void stop() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        QDGraphics gWorld = getGWorld();
        this.hasStarted = false;
        synchronized (QTNative.globalsLock) {
            i = SGStop(_ID());
        }
        StdQTException.checkError(i);
    }

    /**
     * You can suspend or restart a record or preview operation by calling the pause function.
     * You supply a byte value that instructs the sequence grabber whether to pause or restart
     * the current operation.
     * @see "QuickTime::SGPause"
     * @param pause Instructs whether to suspend or restart the current operation.
     * @throws quicktime.std.StdQTException
     */
    public void pause(int pause) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGPause(_ID(), (byte) pause);
        }
        StdQTException.checkError(i);
    }

    /**
     * Specifies control information about the current operation to the sequence grabber component.
     * @see "QuickTime::SGGetPause"
     */
    public int getPause() throws StdQTException {
        byte[] paused = new byte[1];
        int i = SGGetPause(_ID(), paused);
        StdQTException.checkError(i);
        return paused[0];
    }

    /**
     * Instructs the sequence grabber to get ready to begin a preview or record operation.
     * May commence both operations. You specify the operations.
     * @see "QuickTime::SGPrepare"
     * @param prepareForPreview The preview operation.
     * @param prepareForRecord The record operation.
     * @throws quicktime.std.StdQTException
     */
    public void prepare(boolean prepareForPreview, boolean prepareForRecord) throws StdQTException {
        int i = 0;
        byte b1 = (byte) (prepareForPreview ? 1 : 0);
        byte b2 = (byte) (prepareForRecord ? 1 : 0);
        synchronized (QTNative.globalsLock) {
            i = SGPrepare(_ID(), b1, b2);
        }
        StdQTException.checkError(i);
    }

    /**
     * Instructs the sequence grabber to release any system resources it allocated previously.
     * You should call release whenever you call SGPrepare without subsequently starting a
     * record or preview operation.
     * @see "QuickTime::SGRelease"
     */
    public void release() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGRelease(_ID());
        }
        StdQTException.checkError(i);
    }

    /**
     * Determines a reference to the movie that contains the data collected during a record operation.
     * You can use this movie identifier with Movie Toolbox functions.
     * @see "QuickTime::SGGetMovie"
     * @return The Movie or null if there is no movie.
     */
    public Movie getMovie() {
        return Movie.fromSequenceGrabber(this);
    }

    /**
     * Configures a sequence grabber.
     * You provide this configuration information in a Movie Toolbox user data list.
     * Typically, you obtain this configuration data from the getSettings method.
     * Note that the sequence grabber disposes of any of its current channels before applying
     * this configuration information. It then opens connections to new channels as appropriate.
     * You can restore saved settings by using the Movie Toolbox's NewUserDataFromHandle function.
     *
     * @see "QuickTime::SGSetSettings"
     * @param ud Specifies a user data list with configuration information for the sequence grabber.
     * @throws quicktime.std.StdQTException
     */
    public void setSettings(UserData ud) throws StdQTException {
        int i = SGSetSettings(_ID(), QTObject.ID(ud), 0);
        StdQTException.checkError(i);
    }

    /**
     * Retrieves the current settings of all channels used by the sequence grabber.
     * The sequence grabber places all of this configuration information into a
     * Movie Toolbox user data list.
     * @see "QuickTime::SGGetSettings"
     * @return A Movie Toolbox user data list that contains the configuration information.
     * @throws quicktime.std.StdQTException
     */
    public UserData getSettings() throws StdQTException {
        return UserData.fromSequenceGrabber(this);
    }

    /**
     * Provides a simple interface to obtain a QuickDraw picture from a sequence grabber component.
     * The sequence grabber can display the picture directly, or it can write the picture to an
     * offscreen buffer. This function is limited in scope, however, and does not allow you to
     * control all of the parameters that govern the operation. When you call this function,
     * the sequence grabber component obtains and configures appropriate sequence grabber
     * channel components (if necessary), grabs the data, and then releases any components
     * it obtained.
     * @see "QuickTime::SGGrabPict"
     * @param bounds The bounding rectangle.
     * @param offscreenDepth The bit depth of the Pict.
     * @param grabPictFlags The flags for the operation.
     * @return The Pict.
     * @throws quicktime.QTException
     */
    public Pict grabPict(QDRect bounds, int offscreenDepth, int grabPictFlags) throws QTException {
        return Pict.fromSequenceGrabber(this, bounds, offscreenDepth, grabPictFlags);
    }

    /**
     * Specifies the movie file for a record operation and other options that govern the operation.
     * The sequence grabber component stores the data that is obtained during the record operation
     * as a QuickTime movie in this file. This function also allows you to control some aspects
     * of the record operation, which are related to output, by specifying control flags.
     * @see "QuickTime::SGSetDataOutput"
     * @param movieFile Specifies which movie file to access.
     * @param whereFlags -
     * @throws quicktime.QTException
     */
    public void setDataOutput(QTFile movieFile, int whereFlags) throws QTException {
        int i = 0;
        if ((whereFlags == 32) && (movieFile == null)) {
            synchronized (QTNative.globalsLock) {
                i = SGSetDataOutput(_ID(), null, whereFlags);
            }
        } else {
            synchronized (QTNative.globalsLock) {
                i = SGSetDataOutput(_ID(), movieFile.getFSSpec(false, 256), whereFlags);
            }
        }
        StdQTException.checkError(i);
    }

    /**
     * Allows you to determine the movie file that is currently assigned to a sequence grabber component.
     * @see "QuickTime::SGGetDataOutput"
     * @return The QTFile movie file.
     * @throws quicktime.QTException
     */
    public QTFile getDataOutputFile() throws QTException {
        return QTFile.fromSequenceGrabber(this);
    }

    /**
     * Allows you to determine the record control flags that is currently assigned to a sequence grabber component.
     * @see "QuickTime::SGGetDataOutput"
     * @return The record control flags.
     * @throws quicktime.QTException
     */
    public int getDataOutputFlags() throws QTException {
        byte[] fsspec = new byte[(QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 'F' : 256];
        int[] flags = { 0 };
        int i = SGGetDataOutput(_ID(), fsspec, flags);
        StdQTException.checkError(i);
        return flags[0];
    }

    /**
     * Determines control information flags for the current operation.
     * @see "QuickTime::SGSetDataRef"
     * @param dataRef information that identifies the destination container
     * @throws quicktime.std.StdQTException
     */
    public void setDataRef(DataRef dataRef, int flags) throws StdQTException {
        int i = 0;
        if (dataRef != null) {
            synchronized (QTNative.globalsLock) {
                i = SGSetDataRef(_ID(), QTObject.ID(dataRef), dataRef.getType(), flags);
            }
        }
        synchronized (QTNative.globalsLock) {
            i = SGSetDataRef(_ID(), 0, 0, flags);
        }
        StdQTException.checkError(i);
    }

    /**
     * Determines the data reference that is currently assigned to a sequence grabber component.
     * @see "QuickTime::SGGetDataRef"
     * @return the dataRef
     * @throws quicktime.std.StdQTException
     */
    public DataRef getDataRef() throws StdQTException {
        return DataRef.fromSequenceGrabber(this);
    }

    /**
     * Determines the data reference flags that control the record operation.
     * @see "QuickTime::SGGetDataRef"
     * @return the record flags
     * @throws quicktime.std.StdQTException
     */
    public int getDataRefFlags() throws StdQTException {
        int[] dataRef = new int[1];
        int[] dataRefType = new int[1];
        int[] flags = new int[1];
        int i = SGGetDataRef(_ID(), dataRef, dataRefType, flags);
        StdQTException.checkError(i);
        return flags[0];
    }

    /**
     * Allows you to set the time limit for recording.
     * @see "QuickTime::SGSetMaximumRecordTime"
     * @return the ticks of recording time. If 0 then no limit has been set
     * @throws quicktime.std.StdQTException
     */
    public void setMaximumRecordTime(int maximumRecordTime) throws StdQTException {
        StdQTException.checkError(SGSetMaximumRecordTime(_ID(), maximumRecordTime));
    }

    /**
     * Allows you to retrieve the time limit you have set for recording.
     * @see "QuickTime::SGGetMaximumRecordTime"
     * @return the ticks of recording time. If 0 then no limit has been set
     * @throws quicktime.std.StdQTException
     */
    public int getMaximumRecordTime() throws StdQTException {
        int[] ticks = { 0 };
        StdQTException.checkError(SGGetMaximumRecordTime(_ID(), ticks));
        return ticks[0];
    }

    /**
     * Allows you to monitor the amount of space remaining for use during a record operation.
     * @see "QuickTime::SGGetStorageSpaceRemaining"
     * @return the number of bytes left for the current record operation.
     * @throws quicktime.std.StdQTException
     */
    public int getStorageSpaceRemaining() throws StdQTException {
        int[] bytes = { 0 };
        StdQTException.checkError(SGGetStorageSpaceRemaining(_ID(), bytes));
        return bytes[0];
    }

    /**
     * Allows you to obtain an estimate of the amount of recording time that remains for the
     * current recording operation.
     * @see "QuickTime::SGGetTimeRemaining"
     * @return the ticks of recording time remaining
     * @throws quicktime.std.StdQTException
     */
    public int getTimeRemaining() throws StdQTException {
        int[] ticks = { 0 };
        StdQTException.checkError(SGGetTimeRemaining(_ID(), ticks));
        return ticks[0];
    }

    /**
     * Determines whether the particular
     * sequence grabber is in preview mode.
     * @see "QuickTime::SGGetMode"
     */
    public boolean isPreviewMode() throws StdQTException {
        byte[] previewMode = new byte[1];
        byte[] recordMode = new byte[1];
        int i = SGGetMode(_ID(), previewMode, recordMode);
        StdQTException.checkError(i);
        return previewMode[0] != 0;
    }

    /**
     * Determines whether the particular
     * sequence grabber is in Record mode.
     * @see "QuickTime::SGGetMode"
     */
    public boolean isRecordMode() throws StdQTException {
        byte[] previewMode = new byte[1];
        byte[] recordMode = new byte[1];
        int i = SGGetMode(_ID(), previewMode, recordMode);
        StdQTException.checkError(i);
        return recordMode[0] != 0;
    }

    /**
     * Returns the SequenceGrabbers's current time Base.
     * @see "QuickTime::SGGetTimeBase"
     * @return the TimeBase
     * @throws quicktime.std.StdQTException
     */
    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromSequenceGrabber(this);
    }

    /**
     * Allows you to obtain info on the channel type
     * currently in use by a sequence grabber component.
     * @see "QuickTime::SGGetIndChannel"
     * @param index identifies the channel to be queried
     * @return the channel type (e.g VideoMediaType, SoundMediaType etc..)
     * @throws quicktime.std.StdQTException
     */
    public int getIndChannelType(int index) throws StdQTException {
        int[] type = { 0 };
        StdQTException.checkError(SGGetIndChannel(_ID(), (short) index, 0, type));
        return type[0];
    }

    /**
     * Allows you to obtain the channel type
     * currently in use by a sequence grabber component.
     * @see "QuickTime::SGGetIndChannel"
     * @param index identifies the channel to be queried
     * @return the connection to the channel.
     * @throws quicktime.QTException
     */
    public SGChannel getIndChannel(int index) throws QTException {
        int[] ref = { 0 };
        int[] type = { 0 };
        StdQTException.checkError(SGGetIndChannel(_ID(), (short) index, ref, type));
        return switch (type[0]) {
            case 1936684398 -> new SGSoundChannel(ref[0], this);
            case 1952807028 -> new SGTextChannel(ref[0], this);
            case 1986618469 -> new SGVideoChannel(ref[0], this);
            case 1836413801 -> new SGMusicChannel(ref[0], this);
            default -> throw new QTException("Unknown SGChannel - please report bug");
        };
    }

    /**
     * Specify a data function for use by the sequence grabber.
     * Whenever any channel assigned to the sequence grabber writes data,
     * your data function is called as well.
     * Your data function may then write the data to another destination.
     * @see "QuickTime::SGSetDataProc"
     * @param proc the callback that will be called by the SequenceGrabber
     * @throws quicktime.std.StdQTException
     */
    public void setDataProc(SGDataProc proc) throws StdQTException {
        int i = 0;
        if (proc == null) {
            removeDataProc();
            return;
        }
        if (this.upp != null) {
            removeDataProc();
        }
        this.upp = new SGDispatcher(this, proc);
        synchronized (QTNative.globalsLock) {
            i = SGSetDataProc(_ID(), this.upp.ID(), 0);
        }
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered data proc.
     * @see "QuickTime::SGSetDataProc"
     */
    public void removeDataProc() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGSetDataProc(_ID(), 0, 0);
        }
        StdQTException.checkError(i);
        if (this.upp != null) {
            this.upp.cleanup();
            this.upp = null;
        }
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }

    private static void SetGWorld(int port, int gdh) {
        QuickDrawManager.SetGWorld(port, gdh);
    }

    private static int SGInitialize(int sg) {
        return SequenceGrabberManager.SGInitialize(sg);
    }

    private static int SGSetGWorld(int sg, int port, int gdh) {
        return SequenceGrabberManager.SGSetGWorld(sg, port, gdh);
    }

    private static int SGWriteExtendedMovieData(int sg, int ref, int p, int len, byte[] offset, int output) {
        return SequenceGrabberManager.SGWriteExtendedMovieData(sg, ref, p, len, offset, output);
    }

    private static int SGStartPreview(int sg) {
        return SequenceGrabberManager.SGStartPreview(sg);
    }

    private static int SGStartRecord(int sg) {
        return SequenceGrabberManager.SGStartRecord(sg);
    }

    private static int SGIdle(int sg) {
        return SequenceGrabberManager.SGIdle(sg);
    }

    private static int SGUpdate(int sg, int updateRgn) {
        return SequenceGrabberManager.SGUpdate(sg, updateRgn);
    }

    private static int SGStop(int sg) {
        return SequenceGrabberManager.SGStop(sg);
    }

    private static int SGPause(int sg, byte pause) {
        return SequenceGrabberManager.SGPause(sg, pause);
    }

    private static int SGGetPause(int sg, byte[] paused) {
        return SequenceGrabberManager.SGGetPause(sg, paused);
    }

    private static int SGPrepare(int sg, byte prepareForPreview, byte prepareForRecord) {
        return SequenceGrabberManager.SGPrepare(sg, prepareForPreview, prepareForRecord);
    }

    private static int SGRelease(int sg) {
        return SequenceGrabberManager.SGRelease(sg);
    }

    private static int SGSetSettings(int sg, int userData, int flags) {
        return SequenceGrabberManager.SGSetSettings(sg, userData, flags);
    }

    private static int SGSetDataOutput(int sg, byte[] fsspec, int flags) {
        return SequenceGrabberManager.SGSetDataOutput(sg, fsspec, flags);
    }

    private static int SGGetDataOutput(int sg, byte[] fsspec, int[] flags) {
        return SequenceGrabberManager.SGGetDataOutput(sg, fsspec, flags);
    }

    private static int SGSetDataRef(int sg, int dataRef, int dataRefType, int flags) {
        return SequenceGrabberManager.SGSetDataRef(sg, dataRef, dataRefType, flags);
    }

    private static int SGGetDataRef(int sg, int[] dataRef, int[] dataRefType, int[] flags) {
        return SequenceGrabberManager.SGGetDataRef(sg, dataRef, dataRefType, flags);
    }

    private static int SGSetMaximumRecordTime(int sg, int ticks) {
        return SequenceGrabberManager.SGSetMaximumRecordTime(sg, ticks);
    }

    private static int SGGetMaximumRecordTime(int sg, int[] ticks) {
        return SequenceGrabberManager.SGGetMaximumRecordTime(sg, ticks);
    }

    private static int SGGetStorageSpaceRemaining(int sg, int[] bytes) {
        return SequenceGrabberManager.SGGetStorageSpaceRemaining(sg, bytes);
    }

    private static int SGGetTimeRemaining(int sg, int[] ticks) {
        return SequenceGrabberManager.SGGetTimeRemaining(sg, ticks);
    }

    private static int SGGetMode(int sg, byte[] previewMode, byte[] recordMode) {
        return SequenceGrabberManager.SGGetMode(sg, previewMode, recordMode);
    }

    private static int SGGetGWorld(int sg, int[] port, int[] gdh) {
        return SequenceGrabberManager.SGGetGWorld(sg, port, gdh);
    }

    private static int SGGrabPict(int sg, int[] pict, byte[] bounds, short offscreenDepth, int grabPictFlags) {
        return SequenceGrabberManager.SGGrabPict(sg, pict, bounds, offscreenDepth, grabPictFlags);
    }

    private static int SGGetIndChannel(int sg, short index, int ref, int[] type) {
        return SequenceGrabberManager.SGGetIndChannel(sg, index, ref, type);
    }

    private static int SGGetIndChannel(int sg, short index, int[] ref, int[] type) {
        return SequenceGrabberManager.SGGetIndChannel(sg, index, ref, type);
    }

    private static int SGSetDataProc(int sg, int proc, int refCon) {
        return SequenceGrabberManager.SGSetDataProc(sg, proc, refCon);
    }

    private static void setLongInArray(byte[] array, int offset, long value) {
        MemoryManager.setLongInArray(array, offset, value);
    }
}
