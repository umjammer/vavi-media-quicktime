package quicktime.streaming;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
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

    private static Object owner = new Object();

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Presentation.class);
                    }
                });
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
    public Presentation(NewPresentationParams paramNewPresentationParams) throws QTException {
        super(allocate(paramNewPresentationParams));
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
     * @param {@link quicktime.QTException}
     */
    public static Presentation fromData(int paramInt, QTPointerRef paramQTPointerRef, PresParams paramPresParams) throws QTException {
        long[] arrayOfLong = { paramQTPointerRef.getSize() };
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSNewPresentationFromData(paramInt, QTObject.ID(paramQTPointerRef), arrayOfLong, QTObject.ID(paramPresParams), arrayOfInt));
        return new Presentation(arrayOfInt[0]);
    }

    /**
     * (Not in carbon yet)
     * QuickTime::QTSNewPresentationFromFile
     */
    public static Presentation fromFile(QTFile paramQTFile, PresParams paramPresParams) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSNewPresentationFromFile(paramQTFile.getFSSpec(true, 256), QTObject.ID(paramPresParams), arrayOfInt));
        return new Presentation(arrayOfInt[0]);
    }

    /**
     * (Not in carbon yet)
     * QuickTime::QTSNewPresentationFromDataRef
     */
    public static Presentation fromDataRef(DataRef paramDataRef, PresParams paramPresParams) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSNewPresentationFromDataRef(QTObject.ID(paramDataRef), paramDataRef.getType(), QTObject.ID(paramPresParams), arrayOfInt));
        return new Presentation(arrayOfInt[0]);
    }

    /**
     * This method exports the stream settings for all streams
     * @param params ExportParams object
     * @param {@link quicktime.QTException}
     */
    public void export(ExportParams paramExportParams) throws QTException {
        QTException.checkError(QTSPresExport(_ID(), 0, paramExportParams.getBytes()));
    }

    /**
     * This method exports the stream settings
     * @param params ExportParams object
     * @param s stream to export
     *  QuickTime::QTSPresExport
     * @param {@link quicktime.QTException}
     */
    public void export(ExportParams paramExportParams, Stream paramStream) throws QTException {
        QTException.checkError(QTSPresExport(_ID(), QTObject.ID(paramStream), paramExportParams.getBytes()));
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
     *  QuickTime::QTSPresGetFlags
     * @return {@link quicktime.QTException}
     */
    public int getFlags() throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetFlags(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method retrieves the time base associated with the presentation
     * @return TimeBase
     * @return {@link quicktime.QTException}
     */
    public TimeBase getTimeBase() throws QTException {
        return TimeBase.fromPresentation(this);
    }

    /**
     * This method retrieves the time scale associated with the presentation
     * @return time scale
     *  QuickTime::QTSPresGetTimeScale
     * @return {@link quicktime.QTException}
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
     *  QuickTime::QTSPresGetInfo
     * @return {@link quicktime.QTException}
     */
    public boolean hasCharacteristic(Stream paramStream, int paramInt) throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSPresHasCharacteristic(_ID(), QTObject.ID(paramStream), paramInt, arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * This method previews the presentation with all streams
     * QuickTime::QTSPresPreview
     */
    public void preview() throws QTException {
        preview(0L, 65536.0F, 0);
    }

    /**
     * This method previews the presentation with all streams
     * @param timeValue -
     * @param rate -
     * @param flags QuickTime::QTSPresPreview
     * @param {@link quicktime.QTException}
     */
    public void preview(long paramLong, float paramFloat, int paramInt) throws QTException {
        long[] arrayOfLong = { paramLong };
        QTException.checkError(QTSPresPreview(_ID(), 0, arrayOfLong, QTUtils.X2Fix(paramFloat), paramInt));
    }

    /**
     * This method previews the presentation
     * @param s stream
     * @param timeValue -
     * @param rate -
     * @param flags QuickTime::QTSPresPreview
     * @param {@link quicktime.QTException}
     */
    public void preview(Stream paramStream, long paramLong, float paramFloat, int paramInt) throws QTException {
        long[] arrayOfLong = { paramLong };
        QTException.checkError(QTSPresPreview(_ID(), QTObject.ID(paramStream), arrayOfLong, QTUtils.X2Fix(paramFloat), paramInt));
    }

    /**
     * This method previews all streams in the presentation with default parameters
     * QuickTime::QTSPresPreroll
     */
    public void preroll() throws QTException {
        preroll(0, 65536.0F, 0);
    }

    /**
     * This method previews all streams in the presentation
     * @param timeValue -
     * @param rate -
     * @param flags QuickTime::QTSPresPreroll
     * @param {@link quicktime.QTException}
     */
    public void preroll(int paramInt1, float paramFloat, int paramInt2) throws QTException {
        QTException.checkError(QTSPresPreroll(_ID(), 0, paramInt1, QTUtils.X2Fix(paramFloat), paramInt2));
    }

    /**
     * This method previews the presentation with a specific stream
     * @param s stream
     * @param timeValue -
     * @param rate -
     * @param flags QuickTime::QTSPresPreroll
     * @param {@link quicktime.QTException}
     */
    public void preroll(Stream paramStream, int paramInt1, float paramFloat, int paramInt2) throws QTException {
        QTException.checkError(QTSPresPreroll(_ID(), QTObject.ID(paramStream), paramInt1, QTUtils.X2Fix(paramFloat), paramInt2));
    }

    /**
     * This method previews the presentation with a specific stream
     * @param s stream
     * @param timeValue (64 bit)
     * @param rate -
     * @param flags QuickTime::QTSPresPreroll64
     * @param {@link quicktime.QTException}
     */
    public void preroll(Stream paramStream, long paramLong, float paramFloat, int paramInt) throws QTException {
        long[] arrayOfLong = { paramLong };
        QTException.checkError(QTSPresPreroll64(_ID(), QTObject.ID(paramStream), arrayOfLong, QTUtils.X2Fix(paramFloat), paramInt));
    }

    /**
     * This method starts all streams associated with the presentation using no flags
     * QuickTime::QTSPresStart
     */
    public void start() throws QTException {
        start(0);
    }

    /**
     * This method starts all streams associated with the presentation
     * @param flags the presentation flags
     *  QuickTime::QTSPresStart
     * @param {@link quicktime.QTException}
     */
    public void start(int paramInt) throws QTException {
        QTSPresStart(_ID(), 0, paramInt);
    }

    /**
     * This method starts the specific stream associated with the presentation
     * @param s stream
     * @param flags the presentation flags
     *  QuickTime::QTSPresStart
     * @param {@link quicktime.QTException}
     */
    public void start(Stream paramStream, int paramInt) throws QTException {
        QTException.checkError(QTSPresStart(_ID(), QTObject.ID(paramStream), paramInt));
    }

    /**
     * Provides processing time to the presentation component
     * After starting a presentation, call this function as often as possible, until you
     * stop the presentation by calling stop.
     * @param params PresIdleParams
     *  QuickTime::QTSPresIdle
     */
    public void idle(PresIdleParams paramPresIdleParams) {
        QTSPresIdle(_ID(), QTObject.ID(paramPresIdleParams));
    }

    /**
     * The invalidate function allows you to tell the presentation that it must refresh its display
     * QuickTime::QTSPresInvalidateRegion
     * @param updateRgn Indicates the part of the QDGraphics that has been changed
     *  If you set this parameter to null, the presentation uses the current visible region
     * @param {@link quicktime.QTException}
     */
    public void invalidateRegion(Region paramRegion) throws QTException {
        QTException.checkError(QTSPresInvalidateRegion(_ID(), QTObject.ID(paramRegion)));
    }

    /**
     * This method skips to a specific point in the presentation
     * @param timeValue the timeValue to skip to
     *  QuickTime::QTSPresSkipTo
     * @param {@link quicktime.QTException}
     */
    public void skipTo(int paramInt) throws QTException {
        QTException.checkError(QTSPresSkipTo(_ID(), paramInt));
    }

    /**
     * This method skips to a specific point in the presentation (64 bit version)
     * @param timeValue the time value to skip to
     *  QuickTime::QTSPresSkipTo64
     * @param {@link quicktime.QTException}
     */
    public void skipTo(long paramLong) throws QTException {
        long[] arrayOfLong = { paramLong };
        QTException.checkError(QTSPresSkipTo64(_ID(), arrayOfLong));
    }

    /**
     * This method stops the presentation of all streams
     * QuickTime::QTSPresStop
     */
    public void stop() throws QTException {
        QTException.checkError(QTSPresStop(_ID(), 0, 0));
    }

    /**
     * This method stops the presentation of a specific stream
     * @param s stream
     * @param flags the presentation flags
     *  QuickTime::QTSPresStop
     * @param {@link quicktime.QTException}
     */
    public void stop(Stream paramStream, int paramInt) throws QTException {
        QTException.checkError(QTSPresStop(_ID(), QTObject.ID(paramStream), paramInt));
    }

    /**
     * This method gets the number of streams associated with the presentation
     * @return number of streams
     *  QuickTime::QTSPresGetNumStreams
     */
    public int getNumStreams() {
        return QTSPresGetNumStreams(_ID());
    }

    /**
     * This method gets a stream owned by the presentation object from an index
     * @param index index of the stream to retrieve
     * @return stream object
     *  QuickTime::QTSPresGetIndStream
     * @return {@link quicktime.QTException}
     */
    public Stream getIndStream(int paramInt) throws QTException {
        int i = QTSPresGetIndStream(_ID(), paramInt);
        return new Stream(i, this);
    }

    /**
     * This method sets the preferred rate of the presentation
     * @param rate the rate of the presentation
     * @param flags additional flags
     *  QuickTime::QTSPresSetPreferredRate
     * @param {@link quicktime.QTException}
     */
    public void setPreferredRate(float paramFloat, int paramInt) throws QTException {
        QTException.checkError(QTSPresSetPreferredRate(_ID(), QTUtils.X2Fix(paramFloat), paramInt));
    }

    /**
     * This method gets the preferred rate of the presentation
     * @return rate the rate of the presentation
     *  QuickTime::QTSPresGetPreferredRate
     * @return {@link quicktime.QTException}
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
     *  QuickTime::QTSPresSetEnable
     * @param {@link quicktime.QTException}
     */
    public void setEnable(Stream paramStream, boolean paramBoolean) throws QTException {
        QTException.checkError(QTSPresSetEnable(_ID(), QTObject.ID(paramStream), (byte) (paramBoolean == true ? 1 : 0)));
    }

    /**
     * This method returns true if the presentation is enabled
     * @param s stream
     * @return enabled state of the presentation
     *  QuickTime::QTSPresGetEnable
     * @return {@link quicktime.QTException}
     */
    public boolean getEnable(Stream paramStream) throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSPresGetEnable(_ID(), QTObject.ID(paramStream), arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * This method toggles the presenter's presentation mode
     * @param s stream
     * @param inPresentingMode sets the presentation mode to be presenting if true
     *  QuickTime::QTSPresSetPresenting
     * @param {@link quicktime.QTException}
     */
    public void setPresenting(Stream paramStream, boolean paramBoolean) throws QTException {
        QTException.checkError(QTSPresSetPresenting(_ID(), QTObject.ID(paramStream), (byte) (paramBoolean == true ? 1 : 0)));
    }

    /**
     * This method returns the presenter's presentation mode
     * @param s stream
     * @return true if the presenter is presenting, false otherwise
     *  QuickTime::QTSPresGetPresenting
     * @return {@link quicktime.QTException}
     */
    public boolean getPresenting(Stream paramStream) throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSPresGetPresenting(_ID(), QTObject.ID(paramStream), arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * This method sets the active segment of the presenter
     * @param s stream
     * @param startTime the start time of the active segment
     * @param duration the duration of the active segment
     *  QuickTime::QTSPresSetActiveSegment
     * @param {@link quicktime.QTException}
     */
    public void setActiveSegment(Stream paramStream, long paramLong1, long paramLong2) throws QTException {
        long[] arrayOfLong1 = { paramLong1 };
        long[] arrayOfLong2 = { paramLong2 };
        QTException.checkError(QTSPresSetActiveSegment(_ID(), QTObject.ID(paramStream), arrayOfLong1, arrayOfLong2));
    }

    /**
     * This method gets the start time of the active segment of the presenter
     * @param s stream
     * @return the start time of the active segment
     *  QuickTime::QTSPresGetActiveSegment
     * @return {@link quicktime.QTException}
     */
    public long getActiveSegmentStart(Stream paramStream) throws QTException {
        long[] arrayOfLong1 = { 0L };
        long[] arrayOfLong2 = { 0L };
        QTException.checkError(QTSPresGetActiveSegment(_ID(), QTObject.ID(paramStream), arrayOfLong1, arrayOfLong2));
        return arrayOfLong1[0];
    }

    /**
     * This method gets the duration of the active segment of the presenter
     * @param s stream
     * @return the duration of the active segment
     *  QuickTime::QTSPresGetActiveSegment
     * @return {@link quicktime.QTException}
     */
    public long getActiveSegmentDuration(Stream paramStream) throws QTException {
        long[] arrayOfLong1 = { 0L };
        long[] arrayOfLong2 = { 0L };
        QTException.checkError(QTSPresGetActiveSegment(_ID(), QTObject.ID(paramStream), arrayOfLong1, arrayOfLong2));
        return arrayOfLong2[0];
    }

    /**
     * This method sets the playing hints of the presenter
     * @param s stream
     * @param flags the flags of the presenter
     * @param mask the masks of the presenter
     *  QuickTime::QTSPresSetPlayHints
     * @param {@link quicktime.QTException}
     */
    public void setPlayHints(Stream paramStream, int paramInt1, int paramInt2) throws QTException {
        QTException.checkError(QTSPresSetPlayHints(_ID(), QTObject.ID(paramStream), paramInt1, paramInt2));
    }

    /**
     * This method gets the playing hints of the presenter
     * @param s stream
     * @return play hints
     *  QuickTime::QTSPresGetPlayHints
     * @return {@link quicktime.QTException}
     */
    public int getPlayHints(Stream paramStream) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetPlayHints(_ID(), QTObject.ID(paramStream), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method sets the GWorld of the presenter
     * @param s stream
     * @param gWorld qdGraphics object representing the gWorld
     * @param gd GDevice
     *  QuickTime::QTSPresSetGWorld
     * @param {@link quicktime.QTException}
     */
    public void setGWorld(Stream paramStream, QDGraphics paramQDGraphics, GDevice paramGDevice) throws QTException {
        QTException.checkError(QTSPresSetGWorld(_ID(), QTObject.ID(paramStream), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice)));
    }

    /**
     * This method sets the GWorld of the presenter (uses the main device as the default GDevice)
     * @param s stream
     * @param gWorld qdGraphics object representing the gWorld
     *  QuickTime::QTSPresSetGWorld
     * @param {@link quicktime.QTException}
     */
    public void setGWorld(Stream paramStream, QDGraphics paramQDGraphics) throws QTException {
        setGWorld(paramStream, paramQDGraphics, GDevice.getMain());
    }

    /**
     * This method sets the GWorld of the presenter (uses the main device as the default GDevice)
     * QuickTime::setGWorld
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        QTException.checkError(QTSPresSetGWorld(_ID(), 0, QTObject.ID(paramQDGraphics), QTObject.ID(GDevice.getMain())));
    }

    /**
     * This method retrieves the GWorld of the presenter
     * @param s stream
     * @return qdGraphics object representing the gWorld
     * @return {@link quicktime.QTException}
     */
    public QDGraphics getGWorld(Stream paramStream) throws QTException {
        return QDGraphics.fromPresentation(this, paramStream);
    }

    /**
     * This method retrieves the GWorld of the presenter
     * @return qdGraphics object representing the gWorld
     *  QuickTime::getGWorld
     * @return {@link quicktime.QTException}
     */
    public QDGraphics getGWorld() throws QTException {
        return QDGraphics.fromPresentation(this, null);
    }

    /**
     * This method sets the clipping region of the presenter
     * @param s stream
     * @param rgn the clipping region
     *  QuickTime::QTSPresSetClip
     * @param {@link quicktime.QTException}
     */
    public void setClip(Stream paramStream, Region paramRegion) throws QTException {
        QTException.checkError(QTSPresSetClip(_ID(), QTObject.ID(paramStream), QTObject.ID(paramRegion)));
    }

    /**
     * This method retrieves the clipping region of the presenter
     * @param s stream
     * @return the clipping region
     * @return {@link quicktime.QTException}
     */
    public Region getClip(Stream paramStream) throws QTException {
        return Region.fromPresentationClip(this, paramStream);
    }

    /**
     * This method sets the matrix of the presenter
     * @param s stream
     * @param matrix the matrix
     *  QuickTime::QTSPresSetMatrix
     * @param {@link quicktime.QTException}
     */
    public void setMatrix(Stream paramStream, Matrix paramMatrix) throws QTException {
        QTException.checkError(QTSPresSetMatrix(_ID(), QTObject.ID(paramStream), paramMatrix.getBytes()));
    }

    /**
     * This method gets the matrix of the presenter
     * @param s stream
     * @return matrix the matrix
     *  QuickTime::QTSPresSetMatrix
     * @return {@link quicktime.QTException}
     */
    public Matrix getMatrix(Stream paramStream) throws QTException {
        Matrix localMatrix = new Matrix();
        QTException.checkError(QTSPresSetMatrix(_ID(), QTObject.ID(paramStream), localMatrix.getBytes()));
        return localMatrix;
    }

    /**
     * This method sets the dimensions of the presenter
     * @param s stream
     * @param width the width of the presenter
     * @param height the height of the presenter
     *  QuickTime::QTSPresSetDimensions
     * @param {@link quicktime.QTException}
     */
    public void setDimensions(Stream paramStream, float paramFloat1, float paramFloat2) throws QTException {
        QTException.checkError(QTSPresSetDimensions(_ID(), QTObject.ID(paramStream), QTUtils.X2Fix(paramFloat1), QTUtils.X2Fix(paramFloat2)));
    }

    /**
     * This method sets the dimensions of the presenter
     * @param s stream
     * @param dim dimensions of the presenter
     *  QuickTime::QTSPresSetDimensions
     * @param {@link quicktime.QTException}
     */
    public void setDimensions(Stream paramStream, QDDimension paramQDDimension) throws QTException {
        setDimensions(paramStream, paramQDDimension.getWidthF(), paramQDDimension.getHeightF());
    }

    /**
     * This method gets the dimensions of the presenter
     * @param s stream
     * @return dimensions of the presenter
     *  QuickTime::QTSPresGetDimensions
     * @return {@link quicktime.QTException}
     */
    public QDDimension getDimensions(Stream paramStream) throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        QTException.checkError(QTSPresGetDimensions(_ID(), QTObject.ID(paramStream), arrayOfInt1, arrayOfInt2));
        return new QDDimension(QTUtils.Fix2X(arrayOfInt1[0]), QTUtils.Fix2X(arrayOfInt2[0]));
    }

    /**
     * This method sets the graphics mode of the presenter
     * @param s stream
     * @param mode the graphics mode of the presenter
     * @param opColor the QDColor associated with the graphics mode (operand color)
     *  QuickTime::QTSPresSetGraphicsMode
     * @param {@link quicktime.QTException}
     */
    public void setGraphicsMode(Stream paramStream, int paramInt, QDColor paramQDColor) throws QTException {
        QTException.checkError(QTSPresSetGraphicsMode(_ID(), QTObject.ID(paramStream), (short) paramInt, paramQDColor.getRGBColor()));
    }

    /**
     * This method gets the graphics mode of the presenter
     * @param s stream
     * @return the graphics mode of the presenter
     *  QuickTime::QTSPresGetGraphicsMode
     * @return {@link quicktime.QTException}
     */
    public GraphicsMode getGraphicsMode(Stream paramStream) throws QTException {
        byte[] arrayOfByte = { 0 };
        short[] arrayOfShort = { 0 };
        QTException.checkError(QTSPresGetGraphicsMode(_ID(), QTObject.ID(paramStream), arrayOfShort, arrayOfByte));
        return new GraphicsMode(arrayOfShort[0], QDColor.fromArray(arrayOfByte, 6));
    }

    /**
     * This method gets the picture associated with the presenter
     * @param s stream
     * @return the picture of the presenter
     * @return {@link quicktime.QTException}
     */
    public Pict getPicture(Stream paramStream) throws QTException {
        return Pict.fromPresentation(this, paramStream);
    }

    /**
     * This method sets the right and left volumes of the presenter
     * @param s stream
     * @param leftVol left sound channel volume
     * @param rightVol right sound channel volume
     *  QuickTime::QTSPresSetVolumes
     * @param {@link quicktime.QTException}
     */
    public void setVolumes(Stream paramStream, int paramInt1, int paramInt2) throws QTException {
        QTException.checkError(QTSPresSetVolumes(_ID(), QTObject.ID(paramStream), (short) paramInt1, (short) paramInt2));
    }

    /**
     * This method gets the right volume of the presenter
     * @param s stream
     * @return right sound channel volume
     *  QuickTime::QTSPresGetVolumes
     * @return {@link quicktime.QTException}
     */
    public int getRightVolume(Stream paramStream) throws QTException {
        short[] arrayOfShort1 = { 0 };
        short[] arrayOfShort2 = { 0 };
        QTException.checkError(QTSPresGetVolumes(_ID(), QTObject.ID(paramStream), arrayOfShort1, arrayOfShort2));
        return arrayOfShort2[0];
    }

    /**
     * This method gets the left volume of the presenter
     * @param s stream
     * @return left sound channel volume
     *  QuickTime::QTSPresGetVolumes
     * @return {@link quicktime.QTException}
     */
    public int getLeftVolume(Stream paramStream) throws QTException {
        short[] arrayOfShort1 = { 0 };
        short[] arrayOfShort2 = { 0 };
        QTException.checkError(QTSPresGetVolumes(_ID(), QTObject.ID(paramStream), arrayOfShort1, arrayOfShort2));
        return arrayOfShort1[0];
    }

    /**
     * @deprecated
     */
    public void addSource(Stream paramStream, Component paramComponent, int paramInt) throws QTException {
        addSourcer(paramStream, (Sourcer) paramComponent, paramInt);
    }

    /**
     * @deprecated
     */
    public void removeSource(Stream paramStream, Component paramComponent, int paramInt) throws QTException {
        removeSourcer(paramStream, (Sourcer) paramComponent, paramInt);
    }

    /**
     * This method adds a source component to the presenter
     * @param s stream
     * @param sourcer sourcer component to be added
     * @param flags additional flags
     *  QuickTime::QTSPresAddSourcer
     * @param {@link quicktime.QTException}
     */
    public void addSourcer(Stream paramStream, Sourcer paramSourcer, int paramInt) throws QTException {
        QTException.checkError(QTSPresAddSourcer(_ID(), QTObject.ID(paramStream), QTObject.ID(paramSourcer), paramInt));
    }

    /**
     * Deprecated.
     * <I/>
     * This method removes a source component from the presenter
     * @param s stream
     * @param sourcer sourcer component to be removed
     * @param flags additional flags
     *  QuickTime::QTSPresRemoveSourcer
     * @param {@link quicktime.QTException}
     */
    public void removeSourcer(Stream paramStream, Sourcer paramSourcer, int paramInt) throws QTException {
        QTException.checkError(QTSPresRemoveSourcer(_ID(), QTObject.ID(paramStream), QTObject.ID(paramSourcer), paramInt));
    }

    /**
     * Deprecated.
     * <I/>
     * This method returns the number of source components for the presenter
     * @param s stream
     * @return number of sources
     *  QuickTime::QTSPresGetNumSourcers
     */
    public int getNumSources(Stream paramStream) {
        return getNumSourcers(paramStream);
    }

    /**
     * This method returns the number of source components for the presenter
     * @param s stream
     * @return number of sources
     *  QuickTime::QTSPresGetNumSourcers
     */
    public int getNumSourcers(Stream paramStream) {
        return QTSPresGetNumSourcers(_ID(), QTObject.ID(paramStream));
    }

    /**
     * This method returns a component at a given index associated with a specific stream
     * @param s stream
     * @param index index
     * @return sourcer
     *  QuickTime::QTSPresGetIndSourcer
     * @return {@link quicktime.QTException}
     */
    public Sourcer getIndSourcer(Stream paramStream, int paramInt) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSPresGetIndSourcer(_ID(), QTObject.ID(paramStream), paramInt, arrayOfInt));
        return new Sourcer(arrayOfInt[0]);
    }

    private static native short QTSNewPresentation(int paramInt, int[] paramArrayOfInt);

    private static native short QTSNewPresentationFromData(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int[] paramArrayOfInt);

    private static native short QTSNewPresentationFromFile(byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt);

    private static native short QTSNewPresentationFromDataRef(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short QTSPresExport(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short QTSPresSetFlags(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTSPresGetFlags(int paramInt, int[] paramArrayOfInt);

    private static native short QTSPresGetTimeScale(int paramInt, int[] paramArrayOfInt);

    private static native short QTSPresSetInfo(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native short QTSPresGetInfo(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native short QTSPresGetInfo(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short QTSPresHasCharacteristic(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native short QTSPresSetNotificationProc(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTSPresGetNotificationProc(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTSPresPreview(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4);

    private static native short QTSPresPreroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short QTSPresPreroll64(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4);

    private static native short QTSPresStart(int paramInt1, int paramInt2, int paramInt3);

    private static native void QTSPresIdle(int paramInt1, int paramInt2);

    private static native short QTSPresInvalidateRegion(int paramInt1, int paramInt2);

    private static native short QTSPresSkipTo(int paramInt1, int paramInt2);

    private static native short QTSPresSkipTo64(int paramInt, long[] paramArrayOfLong);

    private static native short QTSPresStop(int paramInt1, int paramInt2, int paramInt3);

    private static native int QTSPresGetNumStreams(int paramInt);

    private static native int QTSPresGetIndStream(int paramInt1, int paramInt2);

    private static native short QTSPresSetPreferredRate(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTSPresGetPreferredRate(int paramInt, int[] paramArrayOfInt);

    private static native short QTSPresSetEnable(int paramInt1, int paramInt2, byte paramByte);

    private static native short QTSPresGetEnable(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short QTSPresSetPresenting(int paramInt1, int paramInt2, byte paramByte);

    private static native short QTSPresGetPresenting(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short QTSPresSetActiveSegment(int paramInt1, int paramInt2, long[] paramArrayOfLong1, long[] paramArrayOfLong2);

    private static native short QTSPresGetActiveSegment(int paramInt1, int paramInt2, long[] paramArrayOfLong1, long[] paramArrayOfLong2);

    private static native short QTSPresSetPlayHints(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSPresGetPlayHints(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTSPresSetGWorld(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSPresGetGWorld(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short QTSPresSetClip(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTSPresGetClip(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTSPresSetMatrix(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short QTSPresGetMatrix(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short QTSPresSetDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSPresGetDimensions(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short QTSPresSetGraphicsMode(int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfByte);

    private static native short QTSPresGetGraphicsMode(int paramInt1, int paramInt2, short[] paramArrayOfShort, byte[] paramArrayOfByte);

    private static native short QTSPresSetVolumes(int paramInt1, int paramInt2, short paramShort1, short paramShort2);

    private static native short QTSPresGetVolumes(int paramInt1, int paramInt2, short[] paramArrayOfShort1, short[] paramArrayOfShort2);

    private static native short QTSPresAddSourcer(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSPresRemoveSourcer(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSPresGetNumSourcers(int paramInt1, int paramInt2);

    private static native short QTSPresGetIndSourcer(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /Presentation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
