
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
     */
    public RecordMovie() throws QTException {
        this(null, null, null);
    }

    /**
     * @deprecated
     */
    public RecordMovie(Movie paramMovie, RecordMovieCallback paramRecordMovieCallback) throws QTException {
        this(paramMovie, paramRecordMovieCallback, null);
    }

    /**
     * @deprecated
     */
    public RecordMovie(Movie paramMovie, RecordMovieCallback paramRecordMovieCallback, QDRect paramQDRect) throws QTException {
        this.gwRect = paramQDRect;
        setMovie(paramMovie, paramRecordMovieCallback);
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
     */
    public void setMovie(Movie paramMovie, RecordMovieCallback paramRecordMovieCallback) throws QTException {
        if (this.recording)
            throw new QTAppException("Can't reset movie whilst recording");
        this.mov = paramMovie;
        this.cb = paramRecordMovieCallback;
        if (this.cb != null) {
            this.cbThread = new Thread(new Runnable() {
                public void run() {
                    if (RecordMovie.this.cb != null) {
                        RecordMovie.this.cb.finish(RecordMovie.this.mov);
                    }
                }
            });
        }
    }

    /**
     * @deprecated
     */
    public void setCompressionSettings(int paramInt1,
                                       int paramInt2,
                                       int paramInt3,
                                       int paramInt4,
                                       int paramInt5,
                                       CodecComponent paramCodecComponent) throws QTException {
        this.scale = paramInt1;
        this.spatialQuality = paramInt2;
        this.temporalQuality = paramInt3;
        this.keyFrameRate = paramInt4;
        this.codecType = paramInt5;
        this.codec = paramCodecComponent;
    }

    /**
     * @deprecated
     */
    public void addedToSource(QDGraphics paramQDGraphics) throws QTException {
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
            this.gwRect = paramQDGraphics.getPortRect();
        this.gw = paramQDGraphics;

        if ((this.vidTrack == null) && (!this.inPreflightMode)) {
            addNewTrack();
        }
        this.seq = new CSequence(this.gw,
                                 this.gwRect,
                                 this.gw.getPixMap().getPixelSize(),
                                 this.codecType,
                                 this.codec,
                                 this.spatialQuality,
                                 this.temporalQuality,
                                 this.keyFrameRate,
                                 null,
                                 0);

        this.recDesc = this.seq.getDescription();
        int i = QTImage.getMaxCompressionSize(this.gw,
                                              this.gwRect,
                                              this.gw.getPixMap().getPixelSize(),
                                              this.spatialQuality,
                                              this.codecType,
                                              this.codec);

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
     */
    public synchronized void setPreflighting(boolean paramBoolean) throws QTException {
        if (this.recording)
            throw new QTAppException("Can't set preflighting whilst recording");
        this.inPreflightMode = paramBoolean;
    }

    /**
     * @deprecated
     */
    public boolean isPreflighting() {
        return this.inPreflightMode;
    }

    /**
     * @deprecated
     */
    public synchronized void recordMode(int paramInt) throws QTException {
        if (this.mov == null)
            throw new QTAppException("Can't go to record mode without first specifying a movie");
        this.currentRecordFrame = 1;
        this.inPreflightMode = false;
        this.recordFrames = paramInt;
    }

    /**
     * @deprecated
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
                    this.vidMedia.addSample(this.imageHandle,
                                            0,
                                            localCompressedFrameInfo.getDataSize(),
                                            600 / this.scale,
                                            this.recDesc,
                                            1,
                                            i != 0 ? 0 : 1);

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

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * RecordMovie.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
