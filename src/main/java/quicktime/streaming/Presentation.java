package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MatrixManager;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.io.QTFile;
import quicktime.jdirect.QTStreamingLib;
import quicktime.qd.GDevice;
import quicktime.qd.Pict;
import quicktime.qd.QDColor;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.Region;
import quicktime.std.clocks.TimeBase;
import quicktime.std.comp.Component;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.Matrix;
import quicktime.std.movies.media.DataRef;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


/**
 * The presentation class is the basis of a broadcast. It can manage one or more streams of content.
 * Most presentation calls can refer to a specific stream or all streams if a stream is not specified.
 */
public final class Presentation extends QTObject implements QTStreamingLib {

    private static final Object owner = new Object();

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
    }

    Presentation(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    private Presentation(int nativeRep) {
        super(nativeRep, null);
    }

    /**
     * Creates a new presentation object from the presentation parameters
     * @param params NewPresentationParams object containing information about the specified presentation
     */
    public Presentation(NewPresentationParams params) throws QTException {
        super(allocate(params));
    }

    private static int allocate(NewPresentationParams newPresentationParams) throws QTException {
        int[] outPresentation = { 0 };
        QTException.checkError(QTSNewPresentation(QTObject.ID(newPresentationParams), outPresentation));
        return outPresentation[0];
    }

    /**
     * Creates a new presentation object from data
     * @param dataType an OSType specifying the type of the data
     * @param data an array of integers containg the actual data for the presentation object
     * @param params the PresParams object containing additional configuration information
     * @throws quicktime.QTException
     */
    public static Presentation fromData(int dataType, QTPointerRef data, PresParams params) throws QTException {
        long[] dataLength = { data.getSize() };
        int[] outPresentation = { 0 };
        QTException.checkError(QTSNewPresentationFromData(dataType, QTObject.ID(data), dataLength, QTObject.ID(params), outPresentation));
        return new Presentation(outPresentation[0]);
    }

    /**
     * (Not in carbon yet)
     * @see "QuickTime::QTSNewPresentationFromFile"
     */
    public static Presentation fromFile(QTFile qtFile, PresParams presParams) throws QTException {
        int[] outPresentation = { 0 };
        QTException.checkError(QTSNewPresentationFromFile(qtFile.getFSSpec(true, 256), QTObject.ID(presParams), outPresentation));
        return new Presentation(outPresentation[0]);
    }

    /**
     * (Not in carbon yet)
     * @see "QuickTime::QTSNewPresentationFromDataRef"
     */
    public static Presentation fromDataRef(DataRef dataRef, PresParams presParams) throws QTException {
        int[] outPresentation = { 0 };
        QTException.checkError(QTSNewPresentationFromDataRef(QTObject.ID(dataRef), dataRef.getType(), QTObject.ID(presParams), outPresentation));
        return new Presentation(outPresentation[0]);
    }

    /**
     * This method exports the stream settings for all streams
     * @param params ExportParams object
     * @throws quicktime.QTException
     */
    public void export(ExportParams params) throws QTException {
        QTException.checkError(QTSPresExport(_ID(), 0, params.getBytes()));
    }

    /**
     * This method exports the stream settings
     * @param params ExportParams object
     * @param s stream to export
     * @see " QuickTime::QTSPresExport"
     * @throws quicktime.QTException
     */
    public void export(ExportParams params, Stream s) throws QTException {
        QTException.checkError(QTSPresExport(_ID(), QTObject.ID(s), params.getBytes()));
    }

    /**
     * This method sets the flags associated with the presentation
     */
    public void setFlags(int flags, int flagsMask) throws QTException {
        QTException.checkError(QTSPresSetFlags(_ID(), flags, flagsMask));
    }

    /**
     * This method retrieves the flags associated with the presentation
     * @return presentation flags
     * @see " QuickTime::QTSPresGetFlags"
     * @throws quicktime.QTException
     */
    public int getFlags() throws QTException {
        int[] flags = { 0 };
        QTException.checkError(QTSPresGetFlags(_ID(), flags));
        return flags[0];
    }

    /**
     * This method retrieves the time base associated with the presentation
     * @return TimeBase
     * @throws quicktime.QTException
     */
    public TimeBase getTimeBase() throws QTException {
        return TimeBase.fromPresentation(this);
    }

    /**
     * This method retrieves the time scale associated with the presentation
     * @return time scale
     * @see " QuickTime::QTSPresGetTimeScale"
     * @throws quicktime.QTException
     */
    public int getTimeScale() throws QTException {
        int[] timeScale = { 0 };
        QTException.checkError(QTSPresGetTimeScale(_ID(), timeScale));
        return timeScale[0];
    }

    private void setInfo(int selector, byte[] param) throws QTException {
        QTException.checkError(QTSPresSetInfo(_ID(), 0, selector, param));
    }

    private void setInfo(Stream stream, int selector, byte[] param) throws QTException {
        QTException.checkError(QTSPresSetInfo(_ID(), QTObject.ID(stream), selector, param));
    }

    private byte[] getInfo(int selector) throws QTException {
        byte[] param = { 0 };
        QTException.checkError(QTSPresGetInfo(_ID(), 0, selector, param));
        return param;
    }

    private byte[] getInfo(Stream stream, int selector) throws QTException {
        byte[] param = { 0 };
        QTException.checkError(QTSPresGetInfo(_ID(), QTObject.ID(stream), selector, param));
        return param;
    }

    /**
     * This method retreives the total data rate for all streams
     */
    public int getTotalDataRate() throws QTException {
        int[] param = { 0 };
        QTException.checkError(QTSPresGetInfo(_ID(), 0, 1685222516, param));
        return param[0];
    }

    /**
     * This method returns true if the presentation has a specific characteristic
     * @param characteristic OSType describing the characteristic
     * @return true if the presentation has that characteristic
     * @see " QuickTime::QTSPresGetInfo"
     * @throws quicktime.QTException
     */
    public boolean hasCharacteristic(Stream stream, int characteristic) throws QTException {
        byte[] hasIt = { 0 };
        QTException.checkError(QTSPresHasCharacteristic(_ID(), QTObject.ID(stream), characteristic, hasIt));
        return hasIt[0] != 0;
    }

    /**
     * This method previews the presentation with all streams
     * @see "QuickTime::QTSPresPreview"
     */
    public void preview() throws QTException {
        preview(0L, 65536.0F, 0);
    }

    /**
     * This method previews the presentation with all streams
     * @param timeValue -
     * @param rate -
     * @see "@param flags QuickTime::QTSPresPreview"
     * @throws quicktime.QTException
     */
    public void preview(long timeValue, float rate, int flags) throws QTException {
        long[] arrayOfLong = { timeValue };
        QTException.checkError(QTSPresPreview(_ID(), 0, arrayOfLong, QTUtils.X2Fix(rate), flags));
    }

    /**
     * This method previews the presentation
     * @param s stream
     * @param timeValue -
     * @param rate -
     * @see "@param flags QuickTime::QTSPresPreview"
     * @throws quicktime.QTException
     */
    public void preview(Stream s, long timeValue, float rate, int flags) throws QTException {
        long[] arrayOfLong = { timeValue };
        QTException.checkError(QTSPresPreview(_ID(), QTObject.ID(s), arrayOfLong, QTUtils.X2Fix(rate), flags));
    }

    /**
     * This method previews all streams in the presentation with default parameters
     * @see "QuickTime::QTSPresPreroll"
     */
    public void preroll() throws QTException {
        preroll(0, 65536.0F, 0);
    }

    /**
     * This method previews all streams in the presentation
     * @param timeValue -
     * @param rate -
     * @see "@param flags QuickTime::QTSPresPreroll"
     * @throws quicktime.QTException
     */
    public void preroll(int timeValue, float rate, int flags) throws QTException {
        QTException.checkError(QTSPresPreroll(_ID(), 0, timeValue, QTUtils.X2Fix(rate), flags));
    }

    /**
     * This method previews the presentation with a specific stream
     * @param s stream
     * @param timeValue -
     * @param rate -
     * @see "@param flags QuickTime::QTSPresPreroll"
     * @throws quicktime.QTException
     */
    public void preroll(Stream s, int timeValue, float rate, int flags) throws QTException {
        QTException.checkError(QTSPresPreroll(_ID(), QTObject.ID(s), timeValue, QTUtils.X2Fix(rate), flags));
    }

    /**
     * This method previews the presentation with a specific stream
     * @param s stream
     * @param timeValue (64 bit)
     * @param rate -
     * @see "@param flags QuickTime::QTSPresPreroll64"
     * @throws quicktime.QTException
     */
    public void preroll(Stream s, long timeValue, float rate, int flags) throws QTException {
        long[] preferredTime = { timeValue };
        QTException.checkError(QTSPresPreroll64(_ID(), QTObject.ID(s), preferredTime, QTUtils.X2Fix(rate), flags));
    }

    /**
     * This method starts all streams associated with the presentation using no flags
     * @see "QuickTime::QTSPresStart"
     */
    public void start() throws QTException {
        start(0);
    }

    /**
     * This method starts all streams associated with the presentation
     * @param flags the presentation flags
     * @see " QuickTime::QTSPresStart"
     * @throws quicktime.QTException
     */
    public void start(int flags) throws QTException {
        QTSPresStart(_ID(), 0, flags);
    }

    /**
     * This method starts the specific stream associated with the presentation
     * @param s stream
     * @param flags the presentation flags
     * @see " QuickTime::QTSPresStart"
     * @throws quicktime.QTException
     */
    public void start(Stream s, int flags) throws QTException {
        QTException.checkError(QTSPresStart(_ID(), QTObject.ID(s), flags));
    }

    /**
     * Provides processing time to the presentation component
     * After starting a presentation, call this function as often as possible, until you
     * stop the presentation by calling stop.
     * @param params PresIdleParams
     * @see " QuickTime::QTSPresIdle"
     */
    public void idle(PresIdleParams params) {
        QTSPresIdle(_ID(), QTObject.ID(params));
    }

    /**
     * The invalidate function allows you to tell the presentation that it must refresh its display
     * @see "QuickTime::QTSPresInvalidateRegion"
     * @param updateRgn Indicates the part of the QDGraphics that has been changed
     *  If you set this parameter to null, the presentation uses the current visible region
     * @throws quicktime.QTException
     */
    public void invalidateRegion(Region updateRgn) throws QTException {
        QTException.checkError(QTSPresInvalidateRegion(_ID(), QTObject.ID(updateRgn)));
    }

    /**
     * This method skips to a specific point in the presentation
     * @param timeValue the timeValue to skip to
     * @see " QuickTime::QTSPresSkipTo"
     * @throws quicktime.QTException
     */
    public void skipTo(int timeValue) throws QTException {
        QTException.checkError(QTSPresSkipTo(_ID(), timeValue));
    }

    /**
     * This method skips to a specific point in the presentation (64 bit version)
     * @param timeValue the time value to skip to
     * @see " QuickTime::QTSPresSkipTo64"
     * @throws quicktime.QTException
     */
    public void skipTo(long timeValue) throws QTException {
        long[] arrayOfLong = { timeValue };
        QTException.checkError(QTSPresSkipTo64(_ID(), arrayOfLong));
    }

    /**
     * This method stops the presentation of all streams
     * @see "QuickTime::QTSPresStop"
     */
    public void stop() throws QTException {
        QTException.checkError(QTSPresStop(_ID(), 0, 0));
    }

    /**
     * This method stops the presentation of a specific stream
     * @param s stream
     * @param flags the presentation flags
     * @see " QuickTime::QTSPresStop"
     * @throws quicktime.QTException
     */
    public void stop(Stream s, int flags) throws QTException {
        QTException.checkError(QTSPresStop(_ID(), QTObject.ID(s), flags));
    }

    /**
     * This method gets the number of streams associated with the presentation
     * @return number of streams
     * @see " QuickTime::QTSPresGetNumStreams"
     */
    public int getNumStreams() {
        return QTSPresGetNumStreams(_ID());
    }

    /**
     * This method gets a stream owned by the presentation object from an index
     * @param index index of the stream to retrieve
     * @return stream object
     * @see " QuickTime::QTSPresGetIndStream"
     * @throws quicktime.QTException
     */
    public Stream getIndStream(int index) throws QTException {
        int i = QTSPresGetIndStream(_ID(), index);
        return new Stream(i, this);
    }

    /**
     * This method sets the preferred rate of the presentation
     * @param rate the rate of the presentation
     * @param flags additional flags
     * @see " QuickTime::QTSPresSetPreferredRate"
     * @throws quicktime.QTException
     */
    public void setPreferredRate(float rate, int flags) throws QTException {
        QTException.checkError(QTSPresSetPreferredRate(_ID(), QTUtils.X2Fix(rate), flags));
    }

    /**
     * This method gets the preferred rate of the presentation
     * @return rate the rate of the presentation
     * @see " QuickTime::QTSPresGetPreferredRate"
     * @throws quicktime.QTException
     */
    public float getPreferredRate() throws QTException {
        int[] preferredRate = { 0 };
        QTException.checkError(QTSPresGetPreferredRate(_ID(), preferredRate));
        return QTUtils.Fix2X(preferredRate[0]);
    }

    /**
     * This method specifies whether the presentation is enabled
     * @param s stream
     * @param enableMode true if the presentation should be enabled
     * @see " QuickTime::QTSPresSetEnable"
     * @throws quicktime.QTException
     */
    public void setEnable(Stream s, boolean enableMode) throws QTException {
        QTException.checkError(QTSPresSetEnable(_ID(), QTObject.ID(s), (byte) (enableMode == true ? 1 : 0)));
    }

    /**
     * This method returns true if the presentation is enabled
     * @param s stream
     * @return enabled state of the presentation
     * @see " QuickTime::QTSPresGetEnable"
     * @throws quicktime.QTException
     */
    public boolean getEnable(Stream s) throws QTException {
        byte[] enableMode = { 0 };
        QTException.checkError(QTSPresGetEnable(_ID(), QTObject.ID(s), enableMode));
        return enableMode[0] != 0;
    }

    /**
     * This method toggles the presenter's presentation mode
     * @param s stream
     * @param inPresentingMode sets the presentation mode to be presenting if true
     * @see " QuickTime::QTSPresSetPresenting"
     * @throws quicktime.QTException
     */
    public void setPresenting(Stream s, boolean inPresentingMode) throws QTException {
        QTException.checkError(QTSPresSetPresenting(_ID(), QTObject.ID(s), (byte) (inPresentingMode == true ? 1 : 0)));
    }

    /**
     * This method returns the presenter's presentation mode
     * @param s stream
     * @return true if the presenter is presenting, false otherwise
     * @see " QuickTime::QTSPresGetPresenting"
     * @throws quicktime.QTException
     */
    public boolean getPresenting(Stream s) throws QTException {
        byte[] presenting = { 0 };
        QTException.checkError(QTSPresGetPresenting(_ID(), QTObject.ID(s), presenting));
        return presenting[0] != 0;
    }

    /**
     * This method sets the active segment of the presenter
     * @param s stream
     * @param startTime the start time of the active segment
     * @param duration the duration of the active segment
     * @see " QuickTime::QTSPresSetActiveSegment"
     * @throws quicktime.QTException
     */
    public void setActiveSegment(Stream s, long startTime, long duration) throws QTException {
        long[] arrayOfLong1 = { startTime };
        long[] arrayOfLong2 = { duration };
        QTException.checkError(QTSPresSetActiveSegment(_ID(), QTObject.ID(s), arrayOfLong1, arrayOfLong2));
    }

    /**
     * This method gets the start time of the active segment of the presenter
     * @param s stream
     * @return the start time of the active segment
     * @see " QuickTime::QTSPresGetActiveSegment"
     * @throws quicktime.QTException
     */
    public long getActiveSegmentStart(Stream s) throws QTException {
        long[] startTime = { 0L };
        long[] duration = { 0L };
        QTException.checkError(QTSPresGetActiveSegment(_ID(), QTObject.ID(s), startTime, duration));
        return startTime[0];
    }

    /**
     * This method gets the duration of the active segment of the presenter
     * @param s stream
     * @return the duration of the active segment
     * @see " QuickTime::QTSPresGetActiveSegment"
     * @throws quicktime.QTException
     */
    public long getActiveSegmentDuration(Stream s) throws QTException {
        long[] startTime = { 0L };
        long[] duration = { 0L };
        QTException.checkError(QTSPresGetActiveSegment(_ID(), QTObject.ID(s), startTime, duration));
        return duration[0];
    }

    /**
     * This method sets the playing hints of the presenter
     * @param s stream
     * @param flags the flags of the presenter
     * @param mask the masks of the presenter
     * @see " QuickTime::QTSPresSetPlayHints"
     * @throws quicktime.QTException
     */
    public void setPlayHints(Stream s, int flags, int mask) throws QTException {
        QTException.checkError(QTSPresSetPlayHints(_ID(), QTObject.ID(s), flags, mask));
    }

    /**
     * This method gets the playing hints of the presenter
     * @param s stream
     * @return play hints
     * @see " QuickTime::QTSPresGetPlayHints"
     * @throws quicktime.QTException
     */
    public int getPlayHints(Stream s) throws QTException {
        int[] playHints = { 0 };
        QTException.checkError(QTSPresGetPlayHints(_ID(), QTObject.ID(s), playHints));
        return playHints[0];
    }

    /**
     * This method sets the GWorld of the presenter
     * @param s stream
     * @param gWorld qdGraphics object representing the gWorld
     * @param gd GDevice
     * @see " QuickTime::QTSPresSetGWorld"
     * @throws quicktime.QTException
     */
    public void setGWorld(Stream s, QDGraphics gWorld, GDevice gd) throws QTException {
        QTException.checkError(QTSPresSetGWorld(_ID(), QTObject.ID(s), QTObject.ID(gWorld), QTObject.ID(gd)));
    }

    /**
     * This method sets the GWorld of the presenter (uses the main device as the default GDevice)
     * @param s stream
     * @param gWorld qdGraphics object representing the gWorld
     * @see " QuickTime::QTSPresSetGWorld"
     * @throws quicktime.QTException
     */
    public void setGWorld(Stream s, QDGraphics gWorld) throws QTException {
        setGWorld(s, gWorld, GDevice.getMain());
    }

    /**
     * This method sets the GWorld of the presenter (uses the main device as the default GDevice)
     * @see "QuickTime::setGWorld"
     */
    public void setGWorld(QDGraphics gWorld) throws QTException {
        QTException.checkError(QTSPresSetGWorld(_ID(), 0, QTObject.ID(gWorld), QTObject.ID(GDevice.getMain())));
    }

    /**
     * This method retrieves the GWorld of the presenter
     * @param s stream
     * @return qdGraphics object representing the gWorld
     * @throws quicktime.QTException
     */
    public QDGraphics getGWorld(Stream s) throws QTException {
        return QDGraphics.fromPresentation(this, s);
    }

    /**
     * This method retrieves the GWorld of the presenter
     * @return qdGraphics object representing the gWorld
     * @see " QuickTime::getGWorld"
     * @throws quicktime.QTException
     */
    public QDGraphics getGWorld() throws QTException {
        return QDGraphics.fromPresentation(this, null);
    }

    /**
     * This method sets the clipping region of the presenter
     * @param s stream
     * @param rgn the clipping region
     * @see " QuickTime::QTSPresSetClip"
     * @throws quicktime.QTException
     */
    public void setClip(Stream s, Region rgn) throws QTException {
        QTException.checkError(QTSPresSetClip(_ID(), QTObject.ID(s), QTObject.ID(rgn)));
    }

    /**
     * This method retrieves the clipping region of the presenter
     * @param s stream
     * @return the clipping region
     * @throws quicktime.QTException
     */
    public Region getClip(Stream s) throws QTException {
        return Region.fromPresentationClip(this, s);
    }

    /**
     * This method sets the matrix of the presenter
     * @param s stream
     * @param matrix the matrix
     * @see " QuickTime::QTSPresSetMatrix"
     * @throws quicktime.QTException
     */
    public void setMatrix(Stream s, Matrix matrix) throws QTException {
        QTException.checkError(QTSPresSetMatrix(_ID(), QTObject.ID(s), matrix.getBytes()));
    }

    /**
     * This method gets the matrix of the presenter
     * @param s stream
     * @return matrix the matrix
     * @see " QuickTime::QTSPresSetMatrix"
     * @throws quicktime.QTException
     */
    public Matrix getMatrix(Stream s) throws QTException {
        Matrix matrix = new Matrix();
        QTException.checkError(QTSPresSetMatrix(_ID(), QTObject.ID(s), matrix.getBytes()));
        return matrix;
    }

    /**
     * This method sets the dimensions of the presenter
     * @param s stream
     * @param width the width of the presenter
     * @param height the height of the presenter
     * @see " QuickTime::QTSPresSetDimensions"
     * @throws quicktime.QTException
     */
    public void setDimensions(Stream s, float width, float height) throws QTException {
        QTException.checkError(QTSPresSetDimensions(_ID(), QTObject.ID(s), QTUtils.X2Fix(width), QTUtils.X2Fix(height)));
    }

    /**
     * This method sets the dimensions of the presenter
     * @param s stream
     * @param dim dimensions of the presenter
     * @see " QuickTime::QTSPresSetDimensions"
     * @throws quicktime.QTException
     */
    public void setDimensions(Stream s, QDDimension dim) throws QTException {
        setDimensions(s, dim.getWidthF(), dim.getHeightF());
    }

    /**
     * This method gets the dimensions of the presenter
     * @param s stream
     * @return dimensions of the presenter
     * @see " QuickTime::QTSPresGetDimensions"
     * @throws quicktime.QTException
     */
    public QDDimension getDimensions(Stream s) throws QTException {
        int[] width = { 0 };
        int[] height = { 0 };
        QTException.checkError(QTSPresGetDimensions(_ID(), QTObject.ID(s), width, height));
        return new QDDimension(QTUtils.Fix2X(width[0]), QTUtils.Fix2X(height[0]));
    }

    /**
     * This method sets the graphics mode of the presenter
     * @param s stream
     * @param mode the graphics mode of the presenter
     * @param opColor the QDColor associated with the graphics mode (operand color)
     * @see " QuickTime::QTSPresSetGraphicsMode"
     * @throws quicktime.QTException
     */
    public void setGraphicsMode(Stream s, int mode, QDColor opColor) throws QTException {
        QTException.checkError(QTSPresSetGraphicsMode(_ID(), QTObject.ID(s), (short) mode, opColor.getRGBColor()));
    }

    /**
     * This method gets the graphics mode of the presenter
     * @param s stream
     * @return the graphics mode of the presenter
     * @see " QuickTime::QTSPresGetGraphicsMode"
     * @throws quicktime.QTException
     */
    public GraphicsMode getGraphicsMode(Stream s) throws QTException {
        byte[] opColor = { 0 };
        short[] mode = { 0 };
        QTException.checkError(QTSPresGetGraphicsMode(_ID(), QTObject.ID(s), mode, opColor));
        return new GraphicsMode(mode[0], QDColor.fromArray(opColor, 6));
    }

    /**
     * This method gets the picture associated with the presenter
     * @param s stream
     * @return the picture of the presenter
     * @throws quicktime.QTException
     */
    public Pict getPicture(Stream s) throws QTException {
        return Pict.fromPresentation(this, s);
    }

    /**
     * This method sets the right and left volumes of the presenter
     * @param s stream
     * @param leftVol left sound channel volume
     * @param rightVol right sound channel volume
     * @see " QuickTime::QTSPresSetVolumes"
     * @throws quicktime.QTException
     */
    public void setVolumes(Stream s, int leftVol, int rightVol) throws QTException {
        QTException.checkError(QTSPresSetVolumes(_ID(), QTObject.ID(s), (short) leftVol, (short) rightVol));
    }

    /**
     * This method gets the right volume of the presenter
     * @param s stream
     * @return right sound channel volume
     * @see " QuickTime::QTSPresGetVolumes"
     * @throws quicktime.QTException
     */
    public int getRightVolume(Stream s) throws QTException {
        short[] leftVolume = { 0 };
        short[] rightVolume = { 0 };
        QTException.checkError(QTSPresGetVolumes(_ID(), QTObject.ID(s), leftVolume, rightVolume));
        return rightVolume[0];
    }

    /**
     * This method gets the left volume of the presenter
     * @param s stream
     * @return left sound channel volume
     * @see " QuickTime::QTSPresGetVolumes"
     * @throws quicktime.QTException
     */
    public int getLeftVolume(Stream s) throws QTException {
        short[] leftVolume = { 0 };
        short[] rightVolume = { 0 };
        QTException.checkError(QTSPresGetVolumes(_ID(), QTObject.ID(s), leftVolume, rightVolume));
        return leftVolume[0];
    }

    /**
     * @deprecated
     * <I>use addSourcer()</I>
     * This method adds a source component to the presenter
     * @param s stream
     * @param sourcer sourcer component to be added
     * @param flags additional flags
     * @see " QuickTime::QTSPresAddSourcer"
     * @throws quicktime.QTException
     */
    public void addSource(Stream s, Component sourcer, int flags) throws QTException {
        addSourcer(s, (Sourcer) sourcer, flags);
    }

    /**
     * @deprecated
     * <I>use removeSourcer()</I>
     * This method removes a source component from the presenter
     * @param s stream
     * @param sourcer sourcer component to be removed
     * @param flags additional flags
     * @see " QuickTime::QTSPresRemoveSourcer"
     * @throws quicktime.QTException
     */
    public void removeSource(Stream s, Component sourcer, int flags) throws QTException {
        removeSourcer(s, (Sourcer) sourcer, flags);
    }

    /**
     * This method adds a source component to the presenter
     * @param s stream
     * @param sourcer sourcer component to be added
     * @param flags additional flags
     * @see " QuickTime::QTSPresAddSourcer"
     * @throws quicktime.QTException
     */
    public void addSourcer(Stream s, Sourcer sourcer, int flags) throws QTException {
        QTException.checkError(QTSPresAddSourcer(_ID(), QTObject.ID(s), QTObject.ID(sourcer), flags));
    }

    /**
     * @deprecated
     * <I/>
     * This method removes a source component from the presenter
     * @param s stream
     * @param sourcer sourcer component to be removed
     * @param flags additional flags
     * @see " QuickTime::QTSPresRemoveSourcer"
     * @throws quicktime.QTException
     */
    public void removeSourcer(Stream s, Sourcer sourcer, int flags) throws QTException {
        QTException.checkError(QTSPresRemoveSourcer(_ID(), QTObject.ID(s), QTObject.ID(sourcer), flags));
    }

    /**
     * @deprecated
     * <I/>
     * This method returns the number of source components for the presenter
     * @param s stream
     * @return number of sources
     * @see " QuickTime::QTSPresGetNumSourcers"
     */
    public int getNumSources(Stream s) {
        return getNumSourcers(s);
    }

    /**
     * This method returns the number of source components for the presenter
     * @param s stream
     * @return number of sources
     * @see " QuickTime::QTSPresGetNumSourcers"
     */
    public int getNumSourcers(Stream s) {
        return QTSPresGetNumSourcers(_ID(), QTObject.ID(s));
    }

    /**
     * This method returns a component at a given index associated with a specific stream
     * @param s stream
     * @param index index
     * @return sourcer
     * @see " QuickTime::QTSPresGetIndSourcer"
     * @throws quicktime.QTException
     */
    public Sourcer getIndSourcer(Stream s, int index) throws QTException {
        int[] sourcer = { 0 };
        QTException.checkError(QTSPresGetIndSourcer(_ID(), QTObject.ID(s), index, sourcer));
        return new Sourcer(sourcer[0]);
    }

    private static short QTSNewPresentation(int newPresentationParams, int[] outPresentation) {
        return StreamingManager.QTSNewPresentation(newPresentationParams, outPresentation);
    }

    private static short QTSNewPresentationFromData(int dataType, int data, long[] dataLength, int presParams, int[] outPresentation) {
        return StreamingManager.QTSNewPresentationFromData(dataType, data, dataLength, presParams, outPresentation);
    }

    private static short QTSNewPresentationFromFile(byte[] fileSpec, int presParams, int[] outPresentation) {
        return StreamingManager.QTSNewPresentationFromFile(fileSpec, presParams, outPresentation);
    }

    private static short QTSNewPresentationFromDataRef(int dataRef, int dataRefType, int presParams, int[] outPresentation) {
        return StreamingManager.QTSNewPresentationFromDataRef(dataRef, dataRefType, presParams, outPresentation);
    }

    private static short QTSPresExport(int presentation, int stream, byte[] exportParams) {
        return StreamingManager.QTSPresExport(presentation, stream, exportParams);
    }

    private static short QTSPresSetFlags(int presentation, int flags, int flagsMask) {
        return StreamingManager.QTSPresSetFlags(presentation, flags, flagsMask);
    }

    private static short QTSPresGetFlags(int presentation, int[] flags) {
        return StreamingManager.QTSPresGetFlags(presentation, flags);
    }

    private static short QTSPresGetTimeScale(int presentation, int[] timeScale) {
        return StreamingManager.QTSPresGetTimeScale(presentation, timeScale);
    }

    private static short QTSPresSetInfo(int presentation, int stream, int selector, byte[] param) {
        return StreamingManager.QTSPresSetInfo(presentation, stream, selector, param);
    }

    private static short QTSPresGetInfo(int presentation, int stream, int selector, byte[] param) {
        return StreamingManager.QTSPresGetInfo(presentation, stream, selector, param);
    }

    private static short QTSPresGetInfo(int presentation, int stream, int selector, int[] param) {
        return StreamingManager.QTSPresGetInfo(presentation, stream, selector, param);
    }

    private static short QTSPresHasCharacteristic(int presentation, int stream, int characteristic, byte[] hasIt) {
        return StreamingManager.QTSPresHasCharacteristic(presentation, stream, characteristic, hasIt);
    }

    private static short QTSPresSetNotificationProc(int presentation, int notificationProc, int[] refCon) {
        return StreamingManager.QTSPresSetNotificationProc(presentation, notificationProc, refCon);
    }

    private static short QTSPresGetNotificationProc(int presentation, int notificationProc, int[] refCon) {
        return StreamingManager.QTSPresGetNotificationProc(presentation, notificationProc, refCon);
    }

    private static short QTSPresPreview(int presentation, int stream, long[] timeValue, int rate, int flags) {
        return StreamingManager.QTSPresPreview(presentation, stream, timeValue, rate, flags);
    }

    private static short QTSPresPreroll(int presentation, int stream, int timeValue, int rate, int flags) {
        return StreamingManager.QTSPresPreroll(presentation, stream, timeValue, rate, flags);
    }

    private static short QTSPresPreroll64(int presentation, int stream, long[] preferredTime, int rate, int flags) {
        return StreamingManager.QTSPresPreroll64(presentation, stream, preferredTime, rate, flags);
    }

    private static short QTSPresStart(int presentation, int stream, int flags) {
        return StreamingManager.QTSPresStart(presentation, stream, flags);
    }

    private static void QTSPresIdle(int presentation, int returnedEvent) {
        StreamingManager.QTSPresIdle(presentation, returnedEvent);
    }

    private static short QTSPresInvalidateRegion(int presentation, int region) {
        return StreamingManager.QTSPresInvalidateRegion(presentation, region);
    }

    private static short QTSPresSkipTo(int presentation, int timeValue) {
        return StreamingManager.QTSPresSkipTo(presentation, timeValue);
    }

    private static short QTSPresSkipTo64(int presentation, long[] timeValue) {
        return StreamingManager.QTSPresSkipTo64(presentation, timeValue);
    }

    private static short QTSPresStop(int presentation, int stream, int flags) {
        return StreamingManager.QTSPresStop(presentation, stream, flags);
    }

    private static int QTSPresGetNumStreams(int presentation) {
        return StreamingManager.QTSPresGetNumStreams(presentation);
    }

    private static int QTSPresGetIndStream(int presentation, int index) {
        return StreamingManager.QTSPresGetIndStream(presentation, index);
    }

    private static short QTSPresSetPreferredRate(int presentation, int rate, int flags) {
        return StreamingManager.QTSPresSetPreferredRate(presentation, rate, flags);
    }

    private static short QTSPresGetPreferredRate(int presentation, int[] preferredRate) {
        return StreamingManager.QTSPresGetPreferredRate(presentation, preferredRate);
    }

    private static short QTSPresSetEnable(int presentation, int stream, byte enableMode) {
        return StreamingManager.QTSPresSetEnable(presentation, stream, enableMode);
    }

    private static short QTSPresGetEnable(int presentation, int stream, byte[] enableMode) {
        return StreamingManager.QTSPresGetEnable(presentation, stream, enableMode);
    }

    private static short QTSPresSetPresenting(int presentation, int stream, byte presenting) {
        return StreamingManager.QTSPresSetPresenting(presentation, stream, presenting);
    }

    private static short QTSPresGetPresenting(int presentation, int stream, byte[] presenting) {
        return StreamingManager.QTSPresGetPresenting(presentation, stream, presenting);
    }

    private static short QTSPresSetActiveSegment(int presentation, int stream, long[] startTime, long[] duration) {
        return StreamingManager.QTSPresSetActiveSegment(presentation, stream, startTime, duration);
    }

    private static short QTSPresGetActiveSegment(int presentation, int stream, long[] startTime, long[] duration) {
        return StreamingManager.QTSPresGetActiveSegment(presentation, stream, startTime, duration);
    }

    private static short QTSPresSetPlayHints(int presentation, int stream, int playHints, int playHintsMask) {
        return StreamingManager.QTSPresSetPlayHints(presentation, stream, playHints, playHintsMask);
    }

    private static short QTSPresGetPlayHints(int presentation, int stream, int[] playHints) {
        return StreamingManager.QTSPresGetPlayHints(presentation, stream, playHints);
    }

    private static short QTSPresSetGWorld(int presentation, int stream, int gWorld, int gdHandle) {
        return StreamingManager.QTSPresSetGWorld(presentation, stream, gWorld, gdHandle);
    }

    private static short QTSPresGetGWorld(int presentation, int stream, int[] gWorld, int[] gdHandle) {
        return StreamingManager.QTSPresGetGWorld(presentation, stream, gWorld, gdHandle);
    }

    private static short QTSPresSetClip(int presentation, int stream, int clip) {
        return StreamingManager.QTSPresSetClip(presentation, stream, clip);
    }

    private static short QTSPresGetClip(int presentation, int stream, int[] clip) {
        return StreamingManager.QTSPresGetClip(presentation, stream, clip);
    }

    private static short QTSPresSetMatrix(int presentation, int stream, byte[] matrix) {
        return MatrixManager.QTSPresSetMatrix(presentation, stream, matrix);
    }

    private static short QTSPresGetMatrix(int presentation, int stream, byte[] matrix) {
        return MatrixManager.QTSPresGetMatrix(presentation, stream, matrix);
    }

    private static short QTSPresSetDimensions(int presentation, int stream, int width, int height) {
        return StreamingManager.QTSPresSetDimensions(presentation, stream, width, height);
    }

    private static short QTSPresGetDimensions(int presentation, int stream, int[] width, int[] height) {
        return StreamingManager.QTSPresGetDimensions(presentation, stream, width, height);
    }

    private static short QTSPresSetGraphicsMode(int presentation, int stream, short mode, byte[] opColor) {
        return StreamingManager.QTSPresSetGraphicsMode(presentation, stream, mode, opColor);
    }

    private static short QTSPresGetGraphicsMode(int presentation, int stream, short[] mode, byte[] opColor) {
        return StreamingManager.QTSPresGetGraphicsMode(presentation, stream, mode, opColor);
    }

    private static short QTSPresSetVolumes(int presentation, int stream, short leftVolume, short rightVolume) {
        return StreamingManager.QTSPresSetVolumes(presentation, stream, leftVolume, rightVolume);
    }

    private static short QTSPresGetVolumes(int presentation, int stream, short[] leftVolume, short[] rightVolume) {
        return StreamingManager.QTSPresGetVolumes(presentation, stream, leftVolume, rightVolume);
    }

    private static short QTSPresAddSourcer(int presentation, int stream, int sourcer, int flags) {
        return StreamingManager.QTSPresAddSourcer(presentation, stream, sourcer, flags);
    }

    private static short QTSPresRemoveSourcer(int presentation, int stream, int sourcer, int flags) {
        return StreamingManager.QTSPresRemoveSourcer(presentation, stream, sourcer, flags);
    }

    private static short QTSPresGetNumSourcers(int presentation, int stream) {
        return StreamingManager.QTSPresGetNumSourcers(presentation, stream);
    }

    private static short QTSPresGetIndSourcer(int presentation, int stream, int index, int[] sourcer) {
        return StreamingManager.QTSPresGetIndSourcer(presentation, stream, index, sourcer);
    }
}
