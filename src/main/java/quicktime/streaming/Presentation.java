package quicktime.streaming;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MatrixManager;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    private static final Object owner = new Object();

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(Presentation.class));
            }
        }
        new PrivelegedAction().establish();
    }

    Presentation(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    private Presentation(int paramInt) {
        super(paramInt, null);
    }

    /**
     * Creates a new presentation object from the presentation parameters
     * @param params NewPresentationParams object containing information about the specified presentation
     */
    public Presentation(NewPresentationParams params) throws QTException {
        super(allocate(params));
    }

    private static int allocate(NewPresentationParams paramNewPresentationParams) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSNewPresentation(QTObject.ID(paramNewPresentationParams), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Creates a new presentation object from data
     * @param dataType an OSType specifying the type of the data
     * @param data an array of integers containg the actual data for the presentation object
     * @param params the PresParams object containing additional configuration information
     * @throws quicktime.QTException
     */
    public static Presentation fromData(int dataType, QTPointerRef data, PresParams params) throws QTException {
        long[] arrayOfLong = { data.getSize() };
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSNewPresentationFromData(dataType, QTObject.ID(data), arrayOfLong, QTObject.ID(params), arrayOfInt));
        return new Presentation(arrayOfInt[0]);
    }

    /**
     * (Not in carbon yet)
     * @see "QuickTime::QTSNewPresentationFromFile"
     */
    public static Presentation fromFile(QTFile paramQTFile, PresParams paramPresParams) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSNewPresentationFromFile(paramQTFile.getFSSpec(true, 256), QTObject.ID(paramPresParams), arrayOfInt));
        return new Presentation(arrayOfInt[0]);
    }

    /**
     * (Not in carbon yet)
     * @see "QuickTime::QTSNewPresentationFromDataRef"
     */
    public static Presentation fromDataRef(DataRef paramDataRef, PresParams paramPresParams) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSNewPresentationFromDataRef(QTObject.ID(paramDataRef), paramDataRef.getType(), QTObject.ID(paramPresParams), arrayOfInt));
        return new Presentation(arrayOfInt[0]);
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
    public void setFlags(int paramInt1, int paramInt2) throws QTException {
        QTException.checkError(QTSPresSetFlags(_ID(), paramInt1, paramInt2));
    }

    /**
     * This method retrieves the flags associated with the presentation
     * @return presentation flags
     * @see " QuickTime::QTSPresGetFlags"
     * @throws quicktime.QTException
     */
    public int getFlags() throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetFlags(_ID(), arrayOfInt));
        return arrayOfInt[0];
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
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetTimeScale(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    private void setInfo(int paramInt, byte[] paramArrayOfByte) throws QTException {
        QTException.checkError(QTSPresSetInfo(_ID(), 0, paramInt, paramArrayOfByte));
    }

    private void setInfo(Stream paramStream, int paramInt, byte[] paramArrayOfByte) throws QTException {
        QTException.checkError(QTSPresSetInfo(_ID(), QTObject.ID(paramStream), paramInt, paramArrayOfByte));
    }

    private byte[] getInfo(int paramInt) throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSPresGetInfo(_ID(), 0, paramInt, arrayOfByte));
        return arrayOfByte;
    }

    private byte[] getInfo(Stream paramStream, int paramInt) throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSPresGetInfo(_ID(), QTObject.ID(paramStream), paramInt, arrayOfByte));
        return arrayOfByte;
    }

    /**
     * This method retreives the total data rate for all streams
     */
    public int getTotalDataRate() throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetInfo(_ID(), 0, 1685222516, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method returns true if the presentation has a specific characteristic
     * @param characteristic OSType describing the characteristic
     * @return true if the presentation has that characteristic
     * @see " QuickTime::QTSPresGetInfo"
     * @throws quicktime.QTException
     */
    public boolean hasCharacteristic(Stream paramStream, int paramInt) throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSPresHasCharacteristic(_ID(), QTObject.ID(paramStream), paramInt, arrayOfByte));
        return arrayOfByte[0] != 0;
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
        long[] arrayOfLong = { timeValue };
        QTException.checkError(QTSPresPreroll64(_ID(), QTObject.ID(s), arrayOfLong, QTUtils.X2Fix(rate), flags));
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
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetPreferredRate(_ID(), arrayOfInt));
        return QTUtils.Fix2X(arrayOfInt[0]);
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
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSPresGetEnable(_ID(), QTObject.ID(s), arrayOfByte));
        return arrayOfByte[0] != 0;
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
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSPresGetPresenting(_ID(), QTObject.ID(s), arrayOfByte));
        return arrayOfByte[0] != 0;
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
        long[] arrayOfLong1 = { 0L };
        long[] arrayOfLong2 = { 0L };
        QTException.checkError(QTSPresGetActiveSegment(_ID(), QTObject.ID(s), arrayOfLong1, arrayOfLong2));
        return arrayOfLong1[0];
    }

    /**
     * This method gets the duration of the active segment of the presenter
     * @param s stream
     * @return the duration of the active segment
     * @see " QuickTime::QTSPresGetActiveSegment"
     * @throws quicktime.QTException
     */
    public long getActiveSegmentDuration(Stream s) throws QTException {
        long[] arrayOfLong1 = { 0L };
        long[] arrayOfLong2 = { 0L };
        QTException.checkError(QTSPresGetActiveSegment(_ID(), QTObject.ID(s), arrayOfLong1, arrayOfLong2));
        return arrayOfLong2[0];
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
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetPlayHints(_ID(), QTObject.ID(s), arrayOfInt));
        return arrayOfInt[0];
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
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        QTException.checkError(QTSPresSetGWorld(_ID(), 0, QTObject.ID(paramQDGraphics), QTObject.ID(GDevice.getMain())));
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
        Matrix localMatrix = new Matrix();
        QTException.checkError(QTSPresSetMatrix(_ID(), QTObject.ID(s), localMatrix.getBytes()));
        return localMatrix;
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
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        QTException.checkError(QTSPresGetDimensions(_ID(), QTObject.ID(s), arrayOfInt1, arrayOfInt2));
        return new QDDimension(QTUtils.Fix2X(arrayOfInt1[0]), QTUtils.Fix2X(arrayOfInt2[0]));
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
        byte[] arrayOfByte = { 0 };
        short[] arrayOfShort = { 0 };
        QTException.checkError(QTSPresGetGraphicsMode(_ID(), QTObject.ID(s), arrayOfShort, arrayOfByte));
        return new GraphicsMode(arrayOfShort[0], QDColor.fromArray(arrayOfByte, 6));
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
        short[] arrayOfShort1 = { 0 };
        short[] arrayOfShort2 = { 0 };
        QTException.checkError(QTSPresGetVolumes(_ID(), QTObject.ID(s), arrayOfShort1, arrayOfShort2));
        return arrayOfShort2[0];
    }

    /**
     * This method gets the left volume of the presenter
     * @param s stream
     * @return left sound channel volume
     * @see " QuickTime::QTSPresGetVolumes"
     * @throws quicktime.QTException
     */
    public int getLeftVolume(Stream s) throws QTException {
        short[] arrayOfShort1 = { 0 };
        short[] arrayOfShort2 = { 0 };
        QTException.checkError(QTSPresGetVolumes(_ID(), QTObject.ID(s), arrayOfShort1, arrayOfShort2));
        return arrayOfShort1[0];
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
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetIndSourcer(_ID(), QTObject.ID(s), index, arrayOfInt));
        return new Sourcer(arrayOfInt[0]);
    }

    private static short QTSNewPresentation(int paramInt, int[] paramArrayOfInt) {
        return StreamingManager.QTSNewPresentation(paramInt, paramArrayOfInt);
    }

    private static short QTSNewPresentationFromData(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int[] paramArrayOfInt) {
        return StreamingManager.QTSNewPresentationFromData(paramInt1, paramInt2, paramArrayOfLong, paramInt3, paramArrayOfInt);
    }

    private static short QTSNewPresentationFromFile(byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt) {
        return StreamingManager.QTSNewPresentationFromFile(paramArrayOfByte, paramInt, paramArrayOfInt);
    }

    private static short QTSNewPresentationFromDataRef(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return StreamingManager.QTSNewPresentationFromDataRef(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static short QTSPresExport(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return StreamingManager.QTSPresExport(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short QTSPresSetFlags(int paramInt1, int paramInt2, int paramInt3) {
        return StreamingManager.QTSPresSetFlags(paramInt1, paramInt2, paramInt3);
    }

    private static short QTSPresGetFlags(int paramInt, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetFlags(paramInt, paramArrayOfInt);
    }

    private static short QTSPresGetTimeScale(int paramInt, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetTimeScale(paramInt, paramArrayOfInt);
    }

    private static short QTSPresSetInfo(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return StreamingManager.QTSPresSetInfo(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static short QTSPresGetInfo(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return StreamingManager.QTSPresGetInfo(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static short QTSPresGetInfo(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetInfo(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static short QTSPresHasCharacteristic(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return StreamingManager.QTSPresHasCharacteristic(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static short QTSPresSetNotificationProc(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresSetNotificationProc(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short QTSPresGetNotificationProc(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetNotificationProc(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short QTSPresPreview(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4) {
        return StreamingManager.QTSPresPreview(paramInt1, paramInt2, paramArrayOfLong, paramInt3, paramInt4);
    }

    private static short QTSPresPreroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return StreamingManager.QTSPresPreroll(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static short QTSPresPreroll64(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4) {
        return StreamingManager.QTSPresPreroll64(paramInt1, paramInt2, paramArrayOfLong, paramInt3, paramInt4);
    }

    private static short QTSPresStart(int paramInt1, int paramInt2, int paramInt3) {
        return StreamingManager.QTSPresStart(paramInt1, paramInt2, paramInt3);
    }

    private static void QTSPresIdle(int paramInt1, int paramInt2) {
        StreamingManager.QTSPresIdle(paramInt1, paramInt2);
    }

    private static short QTSPresInvalidateRegion(int paramInt1, int paramInt2) {
        return StreamingManager.QTSPresInvalidateRegion(paramInt1, paramInt2);
    }

    private static short QTSPresSkipTo(int paramInt1, int paramInt2) {
        return StreamingManager.QTSPresSkipTo(paramInt1, paramInt2);
    }

    private static short QTSPresSkipTo64(int paramInt, long[] paramArrayOfLong) {
        return StreamingManager.QTSPresSkipTo64(paramInt, paramArrayOfLong);
    }

    private static short QTSPresStop(int paramInt1, int paramInt2, int paramInt3) {
        return StreamingManager.QTSPresStop(paramInt1, paramInt2, paramInt3);
    }

    private static int QTSPresGetNumStreams(int paramInt) {
        return StreamingManager.QTSPresGetNumStreams(paramInt);
    }

    private static int QTSPresGetIndStream(int paramInt1, int paramInt2) {
        return StreamingManager.QTSPresGetIndStream(paramInt1, paramInt2);
    }

    private static short QTSPresSetPreferredRate(int paramInt1, int paramInt2, int paramInt3) {
        return StreamingManager.QTSPresSetPreferredRate(paramInt1, paramInt2, paramInt3);
    }

    private static short QTSPresGetPreferredRate(int paramInt, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetPreferredRate(paramInt, paramArrayOfInt);
    }

    private static short QTSPresSetEnable(int paramInt1, int paramInt2, byte paramByte) {
        return StreamingManager.QTSPresSetEnable(paramInt1, paramInt2, paramByte);
    }

    private static short QTSPresGetEnable(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return StreamingManager.QTSPresGetEnable(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short QTSPresSetPresenting(int paramInt1, int paramInt2, byte paramByte) {
        return StreamingManager.QTSPresSetPresenting(paramInt1, paramInt2, paramByte);
    }

    private static short QTSPresGetPresenting(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return StreamingManager.QTSPresGetPresenting(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short QTSPresSetActiveSegment(int paramInt1, int paramInt2, long[] paramArrayOfLong1, long[] paramArrayOfLong2) {
        return StreamingManager.QTSPresSetActiveSegment(paramInt1, paramInt2, paramArrayOfLong1, paramArrayOfLong2);
    }

    private static short QTSPresGetActiveSegment(int paramInt1, int paramInt2, long[] paramArrayOfLong1, long[] paramArrayOfLong2) {
        return StreamingManager.QTSPresGetActiveSegment(paramInt1, paramInt2, paramArrayOfLong1, paramArrayOfLong2);
    }

    private static short QTSPresSetPlayHints(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return StreamingManager.QTSPresSetPlayHints(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTSPresGetPlayHints(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetPlayHints(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short QTSPresSetGWorld(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return StreamingManager.QTSPresSetGWorld(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTSPresGetGWorld(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return StreamingManager.QTSPresGetGWorld(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short QTSPresSetClip(int paramInt1, int paramInt2, int paramInt3) {
        return StreamingManager.QTSPresSetClip(paramInt1, paramInt2, paramInt3);
    }

    private static short QTSPresGetClip(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetClip(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short QTSPresSetMatrix(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MatrixManager.QTSPresSetMatrix(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short QTSPresGetMatrix(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MatrixManager.QTSPresGetMatrix(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short QTSPresSetDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return StreamingManager.QTSPresSetDimensions(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTSPresGetDimensions(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return StreamingManager.QTSPresGetDimensions(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short QTSPresSetGraphicsMode(int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfByte) {
        return StreamingManager.QTSPresSetGraphicsMode(paramInt1, paramInt2, paramShort, paramArrayOfByte);
    }

    private static short QTSPresGetGraphicsMode(int paramInt1, int paramInt2, short[] paramArrayOfShort, byte[] paramArrayOfByte) {
        return StreamingManager.QTSPresGetGraphicsMode(paramInt1, paramInt2, paramArrayOfShort, paramArrayOfByte);
    }

    private static short QTSPresSetVolumes(int paramInt1, int paramInt2, short paramShort1, short paramShort2) {
        return StreamingManager.QTSPresSetVolumes(paramInt1, paramInt2, paramShort1, paramShort2);
    }

    private static short QTSPresGetVolumes(int paramInt1, int paramInt2, short[] paramArrayOfShort1, short[] paramArrayOfShort2) {
        return StreamingManager.QTSPresGetVolumes(paramInt1, paramInt2, paramArrayOfShort1, paramArrayOfShort2);
    }

    private static short QTSPresAddSourcer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return StreamingManager.QTSPresAddSourcer(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTSPresRemoveSourcer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return StreamingManager.QTSPresRemoveSourcer(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTSPresGetNumSourcers(int paramInt1, int paramInt2) {
        return StreamingManager.QTSPresGetNumSourcers(paramInt1, paramInt2);
    }

    private static short QTSPresGetIndSourcer(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetIndSourcer(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }
}
