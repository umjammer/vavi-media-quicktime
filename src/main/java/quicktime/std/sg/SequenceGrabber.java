package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
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

    private static Object linkage;

    private SGDispatcher upp;

    private boolean hasStarted;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SequenceGrabber.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Opens the specified movie export component,'barg', with any subType.
     *
     * QuickTime::OpenADefaultComponent
     * QuickTime::SGInitialize
     */
    public SequenceGrabber() throws QTException {
        this(0);
    }

    /**
     * Opens the specified movie export component,'barg', with the given subType.
     *
     * QuickTime::OpenADefaultComponent
     * QuickTime::SGInitialize
     * @param subType The movie export component sub type
     */
    public SequenceGrabber(int paramInt) throws QTException {
        super(1650553447, paramInt);
        initialize();
    }

    /**
     * Opens the specified Sequence grabber component based on the type and Subtype
     *
     * QuickTime::OpenADefaultComponent
     * QuickTime::SGInitialize
     * @param subType The movie export component sub type
     */
    public SequenceGrabber(int paramInt1, int paramInt2) throws QTException {
        super(paramInt1, paramInt2);
        initialize();
    }

    private void initialize() throws QTException {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to capture media with security settings when class is signed");
        }
        int i = 0;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt1, arrayOfInt2);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            setGWorld(QDGraphics.scratch, null);
            i = SGInitialize(_ID());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
            this.hasStarted = false;
        }
        if (i < 0) {
            try {
                finalize();
            } catch (Throwable localThrowable) {
            }
            StdQTException.checkError(i);
        }
    }

    /**
     * Establishes the graphics port and device for a sequence grabber.
     * The sequence grabber component displays the recorded or previewed data in this graphics world.
     *
     * QuickTime::SGSetGWorld
     * @param port Specifies the destination graphics port.
     *     The specified graphics port must be a color graphics port.
     * @param gdh Specifies the destination graphics device.
     *     Set this parameter to null to use the current device.
     *     If the gp parameter specifies a graphics world, set this parameter
     *     to null to use that graphics world's graphics device.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws StdQTException {
        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGSetGWorld(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice));
        }
        StdQTException.checkError(i);
    }

    /**
     * Returns the QDGraphics of the sequence grabber.
     * QuickTime::SGGetGWorld
     * @return The current graf port of the sequence grabber.
     * @return {@link quicktime.std.StdQTException}
     */
    public QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromSequenceGrabber(this);
    }

    /**
     * Returns the current device of the sequence grabber.
     * QuickTime::SGGetGWorld
     * @return The current device of the sequence grabber.
     * @return {@link quicktime.std.StdQTException}
     */
    public GDevice getGWorldDevice() throws StdQTException {
        return GDevice.fromSequenceGrabber(this);
    }

    /**
     * Removes a channel from a sequence grabber component.
     * QuickTime::SGDisposeChannel
     * @param c the channel you want to dispose
     * @param {@link quicktime.std.StdQTException}
     */
    public void disposeChannel(SGChannel paramSGChannel) throws StdQTException {
        paramSGChannel.disposeChannel(this);
    }

    /**
     * The SGNewOutput function creates a new sequence grabber output. You specify
     * the outputÕs destination container using a data reference. Once you have
     * created the sequence grabber output, you can use the SGSetChannelOutput
     * function to assign the output to a sequence grabber channel.
     *
     * QuickTime::SGNewOutput
     * @param dataRef Specifies the type of channel.
     * @param whereFlags Specifies the type of channel.
     * @param {@link quicktime.std.StdQTException}
     */
    public SGOutput newOutput(DataRef paramDataRef, int paramInt) throws StdQTException {
        return new SGOutput(this, QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
    }

    /**
     * Disposes of an output associated with a sequence grabber.
     * QuickTime::SGDisposeOutput
     */
    public void disposeOutput(SGOutput paramSGOutput) throws QTException {
        paramSGOutput.disposeQTObject();
    }

    boolean isValid() {
        return stillValid();
    }

    /**
     * Allows your channel component to add data to a movie.
     * QuickTime::SGWriteExtendedMovieData(
     * @param p -
     * @param len -
     * @param offset offset to the new data in the movie. If the movie is in memory, the
     *  returned offset reflects the location the data will have in the movie on a permanent storage device.
     * @param sgOut output to which the data was written.
     *  associated with the sequence grabber output.
     * @param {@link quicktime.std.StdQTException}
     */
    public void writeExtendedMovieData(SGChannel paramSGChannel, int paramInt1, int paramInt2, long paramLong, SGOutput paramSGOutput) throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        setLongInArray(arrayOfByte, 0, paramLong);
        int i = SGWriteExtendedMovieData(_ID(), QTObject.ID(paramSGChannel), paramInt1, paramInt2, arrayOfByte, QTObject.ID(paramSGOutput));
        StdQTException.checkError(i);
    }

    /**
     * Instructs the sequence grabber to begin processing data from its channels.
     * QuickTime::SGStartPreview
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
     * QuickTime::SGStartRecord
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
     * QuickTime::SGIdle
     */
    public void idle() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        if (this.hasStarted) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(arrayOfInt1, arrayOfInt2);
                i = SGIdle(_ID());
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
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
     * QuickTime::SGIdle
     * @return true if requires more idling, false if comleted.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean idleMore() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        if (this.hasStarted) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(arrayOfInt1, arrayOfInt2);
                i = SGIdle(_ID());
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
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
     * QuickTime::SGUpdate
     * @param updateRgn Indicates the part of the QDGraphics that has been changed.
     *  If you set this parameter to null, the sequence grabber uses the windowÕs current visible region
     * @param {@link quicktime.std.StdQTException}
     */
    public void update(Region paramRegion) throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt1, arrayOfInt2);
            i = SGUpdate(_ID(), QTObject.ID(paramRegion));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
            }
        }
        StdQTException.checkError(i);
    }

    /**
     * Stops a preview or record operation.
     * QuickTime::SGStop
     */
    public void stop() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        QDGraphics localQDGraphics = getGWorld();
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
     * QuickTime::SGPause
     * @param pause Instructs whether to suspend or restart the current operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void pause(int paramInt) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGPause(_ID(), (byte) paramInt);
        }
        StdQTException.checkError(i);
    }

    /**
     * Specifies control information about the current operation to the sequence grabber component.
     * QuickTime::SGGetPause
     */
    public int getPause() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = SGGetPause(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0];
    }

    /**
     * Instructs the sequence grabber to get ready to begin a preview or record operation.
     * May commence both operations. You specify the operations.
     * QuickTime::SGPrepare
     * @param prepareForPreview The preview operation.
     * @param prepareForRecord The record operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void prepare(boolean paramBoolean1, boolean paramBoolean2) throws StdQTException {
        int i = 0;
        byte b1 = (byte) (paramBoolean1 ? 1 : 0);
        byte b2 = (byte) (paramBoolean2 ? 1 : 0);
        synchronized (QTNative.globalsLock) {
            i = SGPrepare(_ID(), b1, b2);
        }
        StdQTException.checkError(i);
    }

    /**
     * Instructs the sequence grabber to release any system resources it allocated previously.
     * You should call release whenever you call SGPrepare without subsequently starting a
     * record or preview operation.
     * QuickTime::SGRelease
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
     * QuickTime::SGGetMovie
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
     * QuickTime::SGSetSettings
     * @param ud Specifies a user data list with configuration information for the sequence grabber.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSettings(UserData paramUserData) throws StdQTException {
        int i = SGSetSettings(_ID(), QTObject.ID(paramUserData), 0);
        StdQTException.checkError(i);
    }

    /**
     * Retrieves the current settings of all channels used by the sequence grabber.
     * The sequence grabber places all of this configuration information into a
     * Movie Toolbox user data list.
     * QuickTime::SGGetSettings
     * @return A Movie Toolbox user data list that contains the configuration information.
     * @return {@link quicktime.std.StdQTException}
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
     * QuickTime::SGGrabPict
     * @param bounds The bounding rectangle.
     * @param offscreenDepth The bit depth of the Pict.
     * @param grabPictFlags The flags for the operation.
     * @return The Pict.
     * @return {@link quicktime.QTException}
     */
    public Pict grabPict(QDRect paramQDRect, int paramInt1, int paramInt2) throws QTException {
        return Pict.fromSequenceGrabber(this, paramQDRect, paramInt1, paramInt2);
    }

    /**
     * Specifies the movie file for a record operation and other options that govern the operation.
     * The sequence grabber component stores the data that is obtained during the record operation
     * as a QuickTime movie in this file. This function also allows you to control some aspects
     * of the record operation, which are related to output, by specifying control flags.
     * QuickTime::SGSetDataOutput
     * @param movieFile Specifies which movie file to access.
     * @param whereFlags -
     * @param {@link quicktime.QTException}
     */
    public void setDataOutput(QTFile paramQTFile, int paramInt) throws QTException {
        int i = 0;
        if ((paramInt == 32) && (paramQTFile == null)) {
            synchronized (QTNative.globalsLock) {
                i = SGSetDataOutput(_ID(), null, paramInt);
            }
        } else {
            synchronized (QTNative.globalsLock) {
                i = SGSetDataOutput(_ID(), paramQTFile.getFSSpec(false, 256), paramInt);
            }
        }
        StdQTException.checkError(i);
    }

    /**
     * Allows you to determine the movie file that is currently assigned to a sequence grabber component.
     * QuickTime::SGGetDataOutput
     * @return The QTFile movie file.
     * @return {@link quicktime.QTException}
     */
    public QTFile getDataOutputFile() throws QTException {
        return QTFile.fromSequenceGrabber(this);
    }

    /**
     * Allows you to determine the record control flags that is currently assigned to a sequence grabber component.
     * QuickTime::SGGetDataOutput
     * @return The record control flags.
     * @return {@link quicktime.QTException}
     */
    public int getDataOutputFlags() throws QTException {
        byte[] arrayOfByte = new byte[(QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 'F' : 256];
        int[] arrayOfInt = { 0 };
        int i = SGGetDataOutput(_ID(), arrayOfByte, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines control information flags for the current operation.
     * QuickTime::SGSetDataRef
     * @param dataRef information that identifies the destination container
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDataRef(DataRef paramDataRef, int paramInt) throws StdQTException {
        int i = 0;
        if (paramDataRef != null) {
            synchronized (QTNative.globalsLock) {
                i = SGSetDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
            }
        }
        synchronized (QTNative.globalsLock) {
            i = SGSetDataRef(_ID(), 0, 0, paramInt);
        }
        StdQTException.checkError(i);
    }

    /**
     * Determines the data reference that is currently assigned to a sequence grabber component.
     * QuickTime::SGGetDataRef
     * @return the dataRef
     * @return {@link quicktime.std.StdQTException}
     */
    public DataRef getDataRef() throws StdQTException {
        return DataRef.fromSequenceGrabber(this);
    }

    /**
     * Determines the data reference flags that control the record operation.
     * QuickTime::SGGetDataRef
     * @return the record flags
     * @return {@link quicktime.std.StdQTException}
     */
    public int getDataRefFlags() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = SGGetDataRef(_ID(), arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return arrayOfInt3[0];
    }

    /**
     * Allows you to set the time limit for recording.
     * QuickTime::SGSetMaximumRecordTime
     * @return the ticks of recording time. If 0 then no limit has been set
     * @return {@link quicktime.std.StdQTException}
     */
    public void setMaximumRecordTime(int paramInt) throws StdQTException {
        StdQTException.checkError(SGSetMaximumRecordTime(_ID(), paramInt));
    }

    /**
     * Allows you to retrieve the time limit you have set for recording.
     * QuickTime::SGGetMaximumRecordTime
     * @return the ticks of recording time. If 0 then no limit has been set
     * @return {@link quicktime.std.StdQTException}
     */
    public int getMaximumRecordTime() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SGGetMaximumRecordTime(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Allows you to monitor the amount of space remaining for use during a record operation.
     * QuickTime::SGGetStorageSpaceRemaining
     * @return the number of bytes left for the current record operation.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getStorageSpaceRemaining() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SGGetStorageSpaceRemaining(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Allows you to obtain an estimate of the amount of recording time that remains for the
     * current recording operation.
     * QuickTime::SGGetTimeRemaining
     * @return the ticks of recording time remaining
     * @return {@link quicktime.std.StdQTException}
     */
    public int getTimeRemaining() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SGGetTimeRemaining(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Determines whether the particular
     * sequence grabber is in preview mode.
     * QuickTime::SGGetMode
     */
    public boolean isPreviewMode() throws StdQTException {
        byte[] arrayOfByte1 = new byte[1];
        byte[] arrayOfByte2 = new byte[1];
        int i = SGGetMode(_ID(), arrayOfByte1, arrayOfByte2);
        StdQTException.checkError(i);
        return arrayOfByte1[0] != 0;
    }

    /**
     * Determines whether the particular
     * sequence grabber is in Record mode.
     * QuickTime::SGGetMode
     */
    public boolean isRecordMode() throws StdQTException {
        byte[] arrayOfByte1 = new byte[1];
        byte[] arrayOfByte2 = new byte[1];
        int i = SGGetMode(_ID(), arrayOfByte1, arrayOfByte2);
        StdQTException.checkError(i);
        return arrayOfByte2[0] != 0;
    }

    /**
     * Returns the SequenceGrabbers's current time Base.
     * QuickTime::SGGetTimeBase
     * @return the TimeBase
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromSequenceGrabber(this);
    }

    /**
     * Allows you to obtain info on the channel type
     * currently in use by a sequence grabber component.
     * QuickTime::SGGetIndChannel
     * @param index identifies the channel to be queried
     * @return the channel type (e.g VideoMediaType, SoundMediaType etc..)
     * @return {@link quicktime.std.StdQTException}
     */
    public int getIndChannelType(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SGGetIndChannel(_ID(), (short) paramInt, 0, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Allows you to obtain the channel type
     * currently in use by a sequence grabber component.
     * QuickTime::SGGetIndChannel
     * @param index identifies the channel to be queried
     * @return the connection to the channel.
     * @return {@link quicktime.QTException}
     */
    public SGChannel getIndChannel(int paramInt) throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        StdQTException.checkError(SGGetIndChannel(_ID(), (short) paramInt, arrayOfInt1, arrayOfInt2));
        switch(arrayOfInt2[0]) {
            case 1936684398:
                return new SGSoundChannel(arrayOfInt1[0], this);
            case 1952807028:
                return new SGTextChannel(arrayOfInt1[0], this);
            case 1986618469:
                return new SGVideoChannel(arrayOfInt1[0], this);
            case 1836413801:
                return new SGMusicChannel(arrayOfInt1[0], this);
        }
        throw new QTException("Unknown SGChannel - please report bug");
    }

    /**
     * Specify a data function for use by the sequence grabber.
     * Whenever any channel assigned to the sequence grabber writes data,
     * your data function is called as well.
     * Your data function may then write the data to another destination.
     * QuickTime::SGSetDataProc
     * @param proc the callback that will be called by the SequenceGrabber
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDataProc(SGDataProc paramSGDataProc) throws StdQTException {
        int i = 0;
        if (paramSGDataProc == null) {
            removeDataProc();
            return;
        }
        if (this.upp != null) {
            removeDataProc();
        }
        this.upp = new SGDispatcher(this, paramSGDataProc);
        synchronized (QTNative.globalsLock) {
            i = SGSetDataProc(_ID(), this.upp.ID(), 0);
        }
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered data proc.
     * QuickTime::SGSetDataProc
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

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static int SGInitialize(int paramInt) {
        return SequenceGrabberManager.SGInitialize(paramInt);
    }

    private static int SGSetGWorld(int paramInt1, int paramInt2, int paramInt3) {
        return SequenceGrabberManager.SGSetGWorld(paramInt1, paramInt2, paramInt3);
    }

    private static int SGWriteExtendedMovieData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5) {
        return SequenceGrabberManager.SGWriteExtendedMovieData(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5);
    }

    private static int SGStartPreview(int paramInt) {
        return SequenceGrabberManager.SGStartPreview(paramInt);
    }

    private static int SGStartRecord(int paramInt) {
        return SequenceGrabberManager.SGStartRecord(paramInt);
    }

    private static int SGIdle(int paramInt) {
        return SequenceGrabberManager.SGIdle(paramInt);
    }

    private static int SGUpdate(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGUpdate(paramInt1, paramInt2);
    }

    private static int SGStop(int paramInt) {
        return SequenceGrabberManager.SGStop(paramInt);
    }

    private static int SGPause(int paramInt, byte paramByte) {
        return SequenceGrabberManager.SGPause(paramInt, paramByte);
    }

    private static int SGGetPause(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGGetPause(paramInt, paramArrayOfByte);
    }

    private static int SGPrepare(int paramInt, byte paramByte1, byte paramByte2) {
        return SequenceGrabberManager.SGPrepare(paramInt, paramByte1, paramByte2);
    }

    private static int SGRelease(int paramInt) {
        return SequenceGrabberManager.SGRelease(paramInt);
    }

    private static int SGSetSettings(int paramInt1, int paramInt2, int paramInt3) {
        return SequenceGrabberManager.SGSetSettings(paramInt1, paramInt2, paramInt3);
    }

    private static int SGSetDataOutput(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        return SequenceGrabberManager.SGSetDataOutput(paramInt1, paramArrayOfByte, paramInt2);
    }

    private static int SGGetDataOutput(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetDataOutput(paramInt, paramArrayOfByte, paramArrayOfInt);
    }

    private static int SGSetDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return SequenceGrabberManager.SGSetDataRef(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int SGGetDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        return SequenceGrabberManager.SGGetDataRef(paramInt, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }

    private static int SGSetMaximumRecordTime(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGSetMaximumRecordTime(paramInt1, paramInt2);
    }

    private static int SGGetMaximumRecordTime(int paramInt, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetMaximumRecordTime(paramInt, paramArrayOfInt);
    }

    private static int SGGetStorageSpaceRemaining(int paramInt, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetStorageSpaceRemaining(paramInt, paramArrayOfInt);
    }

    private static int SGGetTimeRemaining(int paramInt, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetTimeRemaining(paramInt, paramArrayOfInt);
    }

    private static int SGGetMode(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return SequenceGrabberManager.SGGetMode(paramInt, paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int SGGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return SequenceGrabberManager.SGGetGWorld(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int SGGrabPict(int paramInt1, int[] paramArrayOfInt, byte[] paramArrayOfByte, short paramShort, int paramInt2) {
        return SequenceGrabberManager.SGGrabPict(paramInt1, paramArrayOfInt, paramArrayOfByte, paramShort, paramInt2);
    }

    private static int SGGetIndChannel(int paramInt1, short paramShort, int paramInt2, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetIndChannel(paramInt1, paramShort, paramInt2, paramArrayOfInt);
    }

    private static int SGGetIndChannel(int paramInt, short paramShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return SequenceGrabberManager.SGGetIndChannel(paramInt, paramShort, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int SGSetDataProc(int paramInt1, int paramInt2, int paramInt3) {
        return SequenceGrabberManager.SGSetDataProc(paramInt1, paramInt2, paramInt3);
    }

    private static void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong) {
        MemoryManager.setLongInArray(paramArrayOfByte, paramInt, paramLong);
    }
}

