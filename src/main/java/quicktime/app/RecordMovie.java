package quicktime.app;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.image.CSequence;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.CompressedFrameInfo;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.QTImage;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.VideoMedia;
import quicktime.util.QTHandle;
import quicktime.util.RawEncodedImage;

/**
 * This class is used to record a movie from a source QDGraphics.
 * It provides a profiling mode so that the an estimate of the compression
 * time for each frame captured. The use of this class can enable a movie
 * to be recorded where the record rate is unrelated to the capture rate.
 * That is, a recorded rate of 10 frames a second can be specified and each
 * frame that is captured will have a duration of 1/10 of a second regardless
 * of how long the compression process takes or how often the frameReady method
 * is called.
 *
 * Typically this class is used as a utility service by some other class that
 * wants to provide recording capabilities. When a RecordMovie object is
 * set in the target class, the target class notifies the RecordMovie object
 * that it now has a source QDGraphics. Whenever appropriate, the target class then
 * notifies the RecordMovie object that a frame is ready to be captured from the
 * QDGraphics object and the RecordMovie object either captures that frame or
 * profiles the compression based on its current mode of operation. When the
 * frameReady method has been called the specified number of frames when recording
 * it returns false to indicate that it is no longer interested and the target class
 * can discard the RecordMovie object and ceases notification of frame readiness. In its turn
 * the RecordMovie disavows any knowledge of the QDGraphics object it was compressing
 * until the next time it receives notification that it has been added to a source.
 *
 * Once a movie has completed recording the RecordMovie object will spawn a thread and
 * call the RecordMovieCallback's finish method with the movie that has just been recorded.
 * The recorded movie will have a video track added with all of the data captured and the application
 * can at this point do whatever is appropriate with the movie - save it, play it back, etc.
 * Each record operation will add a new video track with the specified compression and visual
 * media characteristics.
 * <p>
 * An example usage of the RecordMovie class:
 * <pre>
 * RecordMovie rm = new RecordMovie (myMovie, myRecordCallback);
 * rm.setCompressionSettings (...);
 * myRecordThis.setRecordMovie (rm);
 * </pre>
 *
 * An example definition of a Class that uses the RecordMovie object to capture its QDGraphics:
 * <pre>
 * class RecordThis {
 *   QDGraphics g;
 *   RecordMovie rm;
 *
 *   public void setRecordMovie (RecordMovie rm) throws QTException {
 *     rm.addedToSource (g);
 *     this.rm = rm;
 *   }
 *   // some call in this object that is responsible for drawing
 *   void blitImage () {
 *     //do blit to g
 *     //....
 *     if (rm != null && rm.frameReady() == false) // capture current state of g
 *       rm = null;                                // no longer interested drop reference to rm
 *     }
 *   }
 * </pre>
 * @see "quicktime.app.display.Compositor"
 * @deprecated
 */
public class RecordMovie {

    private QTHandle imageHandle;

    private RawEncodedImage compressedImage;

    private VideoMedia vidMedia;

    private int recordFrames;

    private int currentRecordFrame;

    private CSequence seq;

    private ImageDescription recDesc;

    private Track vidTrack;

    private RecordMovieCallback cb;

    private boolean doAsync;

    private Movie mov;

    private Thread cbThread;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a default RecordMovie object. No movie or callback object are specified
     * so in this state the object can only be used for profiling. If a recording is desired
     * the application must specify the movie with the setMovie method. In order for profiling
     * to be enabled the application must also set the compression settings, describing the
     * type of compression to be applied.
     */
    public RecordMovie() throws QTException {
        this(null, null, null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Record the output of each composited image to a frame. When the movie is finished
     * recording the RecordMovieCallback's finish method is called with the resulting movie.
     * In this case the compositor will continue to draw to the destination graphics and any
     * changes as a result of user interaction will be recorded. By default the entire
     * bounds of the target QDGraphics will be captured.
     * @param theMovie the movie to which a video track will be added
     * @param cBack when recording is finished the cBack.finish method is called with the resultant movie. If this
     *  value is null then no notification is given to the application when recording is completed.
     */
    public RecordMovie(Movie theMovie, RecordMovieCallback cBack) throws QTException {
        this(theMovie, cBack, null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Record the output of each composited image to a frame. When the movie is finished
     * recording the RecordMovieCallback's finish method is called with the resulting movie.
     * In this case the compositor will continue to draw to the destination graphics and any
     * changes as a result of user interaction will be recorded.
     * @param theMovie the movie to which a video track will be added
     * @param cBack when recording is finished the cBack.finish method is called with the resultant movie. If this
     *  value is null then no notification is given to the application when recording is completed.
     * @param rectBounds if only some part of the QDGraphics is to be captured you can specify the rectangle
     *  here. A value of null means that the entire QDGraphics bounds will be captured
     */
    public RecordMovie(Movie theMovie, RecordMovieCallback cBack, QDRect rectBounds) throws QTException {
        this.gwRect = rectBounds;
        setMovie(theMovie, cBack);
    }

    private boolean inPreflightMode = false;

    private boolean recording = false;

    private QDGraphics gw;

    private QDRect gwRect;

    private int scale;

    private int spatialQuality;

    private int temporalQuality;

    private int keyFrameRate;

    private int codecType;

    private CodecComponent codec;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the movie that a video track will be added to in any consequent record operation
     * and the callback object that will be called when the movie is finished recording.
     * If no callback is specified the application receives no notification when recording
     * is completed.
     * @param theMovie to record
     * @param cBack the call back object
     * @throws quicktime.QTException
     */
    public void setMovie(Movie theMovie, RecordMovieCallback cBack) throws QTException {
        if (this.recording)
            throw new QTAppException("Can't reset movie whilst recording");
        this.mov = theMovie;
        this.cb = cBack;
        if (this.cb != null) {
            this.cbThread = new Thread(() -> {
                if (RecordMovie.this.cb != null) {
                    RecordMovie.this.cb.finish(RecordMovie.this.mov);
                }
            });
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Establishes the type of compression that will be applied to the source QDGraphics
     * when the recording or preflighting is done.
     * @param scale the time scale of the video track that is added
     * @param spatialQuality the spatial compression quality that is to be applied when compressing each frame
     * @param temporalQuality the temporal compression quality that is to be applied when compressing each frame
     * @param keyFrameRate the key frame rate that is used if temporal compression is used.
     *  A value of zero lets QuickTime decide on the keyFrameRate. This is disregarded if no temporal compression is specified
     * @param codecType the codec identifier that is used to specify which compressor is used to compress the images
     * @param codec qualifies which type of codec of codecType is used, ex. bestSpeed, bestQuality, etc.
     * @throws quicktime.QTException
     */
    public void setCompressionSettings(int scale, int spatialQuality, int temporalQuality, int keyFrameRate, int codecType, CodecComponent codec) throws QTException {
        this.scale = scale;
        this.spatialQuality = spatialQuality;
        this.temporalQuality = temporalQuality;
        this.keyFrameRate = keyFrameRate;
        this.codecType = codecType;
        this.codec = codec;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the target object that control the drawing and capturing of the image
     * drawn in the QDGraphics. When a RecordMovie object is set to a target, the target calls this method
     * with the QDGraphics object which is the source image. If an application is setting a record movie
     * object to a target it is the target's responsibility to notify the RecordMovie object that it has a
     * target graphics. Thus in general the application does not need to call this method specifically.
     * If you are using this object to record drawing in a QDGraphics object then this method should be
     * called and it then becomes the responsibility of the caller to notify the RecordMovie object
     * when a frame is ready for capturing by calling the frameReady method.
     * The applicatioin should ensure that the RecordMovie object has been set up with compression
     * parameters (if in preflight mode) and also a target movie if in record mode before being
     * set to a target ie. the target object can assume this requirement when it calls the addedToSource
     * method. If in an inappropriate state this method will throw a QTAppException detailing the exceptional state.
     * @param g the graphics object which is the source of the compression and capture operations of the RecordMovie class
     * @throws quicktime.QTException
     * @see quicktime.app.RecordMovie#frameReady()
     */
    public void addedToSource(QDGraphics g) throws QTException {
        if (this.recording) {
            throw new QTAppException("Can't reset parameters once recording has begun");
        }
        if ((!this.inPreflightMode) && ((this.mov == null) || (this.cb == null))) {
            throw new QTAppException("Must specify a movie and the callback class before recording");
        }
        if (this.codec == null) {
            throw new QTAppException("Must set compression settings before record or preflight can be done");
        }
        if (this.gwRect == null)
            this.gwRect = g.getPortRect();
        this.gw = g;
        if ((this.vidTrack == null) && (!this.inPreflightMode)) {
            addNewTrack();
        }
        this.seq = new CSequence(this.gw, this.gwRect, this.gw.getPixMap().getPixelSize(), this.codecType, this.codec, this.spatialQuality, this.temporalQuality, this.keyFrameRate, null, 0);
        this.recDesc = this.seq.getDescription();
        int i = QTImage.getMaxCompressionSize(this.gw, this.gwRect, this.gw.getPixMap().getPixelSize(), this.spatialQuality, this.codecType, this.codec);
        this.imageHandle = new QTHandle(i, true);
        this.imageHandle.lock();
        this.compressedImage = RawEncodedImage.fromQTHandle(this.imageHandle);
        if (!this.inPreflightMode) {
            this.vidMedia.beginEdits();
        }
        this.recording = (!this.inPreflightMode);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This call will do a preflight of the record and you will be able to asses the impact of compressing a
     * single pass of the QDGraphics target. Preflighting will be stopped when recording is started.
     * @param flag if true preflighting is enabled, if false preflighting is disabled.
     * @throws quicktime.QTException
     */
    public synchronized void setPreflighting(boolean flag) throws QTException {
        if (this.recording)
            throw new QTAppException("Can't set preflighting whilst recording");
        this.inPreflightMode = flag;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the RecordMovie object is preflighting a compression operation.
     * @return boolean
     */
    public boolean isPreflighting() {
        return this.inPreflightMode;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Places a RecordMovie object in record mode. A movie must already have been specified
     * for this method to succeed.
     * @param numRecordFrames the number of frames the record activity should record
     * @throws quicktime.QTException
     */
    public synchronized void recordMode(int numRecordFrames) throws QTException {
        if (this.mov == null)
            throw new QTAppException("Can't go to record mode without first specifying a movie");
        this.currentRecordFrame = 1;
        this.inPreflightMode = false;
        this.recordFrames = numRecordFrames;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Aborts recording before it has completed. Any captured images will be lost.
     */
    public void abortRecording() throws QTException {
        if (this.recording) {
            this.vidMedia.endEdits();
            cleanup();
            this.recording = false;
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * The source of the record operation calls this method whether the RecordMovie object
     * is preflighting or recording whenever it is appropriate for the RecordMovie object
     * to capture the current state of the QDGraphics.
     * @return true if interested in another frame, false will end the record or preflight operation
     * @throws quicktime.QTException
     */
    public boolean frameReady() throws QTException {
        synchronized (QTNative.globalsLock) {
            synchronized (this) {
                CompressedFrameInfo localCompressedFrameInfo;
                if (this.inPreflightMode) {
                    localCompressedFrameInfo = this.seq.compressFrame(this.gw, this.gwRect, 4, this.compressedImage);
                    return true;
                }
                if ((this.recording) && (this.currentRecordFrame++ <= this.recordFrames)) {
                    localCompressedFrameInfo = this.seq.compressFrame(this.gw, this.gwRect, 4, this.compressedImage);
                    int i = localCompressedFrameInfo.getSimilarity() == 0 ? 1 : 0;
                    this.vidMedia.addSample(this.imageHandle, 0, localCompressedFrameInfo.getDataSize(), 600 / this.scale, this.recDesc, 1, i != 0 ? 0 : 1);
                    if (this.currentRecordFrame > this.recordFrames) {
                        cleanupMovie();
                        this.recording = false;
                        this.cbThread.start();
                        return false;
                    }
                    return true;
                }
            }
        }
        cleanup();
        return false;
    }

    private void cleanupMovie() throws QTException {
        this.vidMedia.endEdits();
        this.vidTrack.insertMedia(0, 0, this.vidMedia.getDuration(), 1.0F);
        cleanup();
    }

    private void cleanup() {
        this.vidTrack = null;
        this.vidMedia = null;
        this.compressedImage = null;
        this.imageHandle = null;
        this.gw = null;
    }

    private void addNewTrack() throws QTException {
        if (this.mov == null)
            throw new QTAppException("Can't add a Track without movie being specified");
        if (this.vidTrack != null)
            throw new QTAppException("Can't alter movie while current movie is being recorded");
        this.vidTrack = this.mov.addTrack(this.gwRect.getWidth(), this.gwRect.getHeight(), 0.0F);
        this.vidMedia = new VideoMedia(this.vidTrack, 600);
    }
}
