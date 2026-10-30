package quicktime.std.sg;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.image.Compressor;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.media.SampleDescription;
import quicktime.util.QTUtils;


/**
 * Assists in importing video into a movie.
 * The SGChannel implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 * Refer to SGChannel in QuickTime Components
 */
public final class SGVideoChannel extends VisualChannel implements QuickTimeLib {

    SGVideoChannel(int type, SequenceGrabber owner) throws StdQTException {
        super(type, owner);
    }

    /**
     * Creates a sequence grabber channel and assigns a channel component to the channel.
     * The channel component is responsible for providing digitized data to the
     * sequence grabber component.
     *
     * @param owner specifies the sequence grabber component the channel is attached to.
     * @see "QuickTime::SGNewChannel"
     */
    public SGVideoChannel(SequenceGrabber owner) throws StdQTException {
        super(owner, 1986618469);
    }

    @Override
    SampleDescription makeDescription() throws QTException {
        return new ImageDescription(0);
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     *
     * @return a ImageDescription object.
     * @throws quicktime.QTException
     * @see "QuickTime::SGGetChannelSampleDescription()"
     */
    public ImageDescription getImageDescription() throws QTException {
        return (ImageDescription) getSampleDescription();
    }

    /**
     * Set the type of Video Compressor
     *
     * @param compressorType sets the type of compressor to use on the channel.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetVideoCompressorType"
     */
    public void setCompressorType(int compressorType) throws StdQTException {
        int i = SGSetVideoCompressorType(_ID(), compressorType);
        StdQTException.checkError(i);
    }

    /**
     * Get the Video Compressor Type
     *
     * @see "QuickTime::SGGetVideoCompressorType"
     */
    public int getCompressorType() throws StdQTException {
        int[] type = new int[1];
        int i = SGGetVideoCompressorType(_ID(), type);
        StdQTException.checkError(i);
        return type[0];
    }

    /**
     * @param depth           Specifies the depth at which the image is likely to be viewed.
     * @param compressor      Specifies the image compressor identifier.
     * @param spatialQuality  Specifies the desired compressed image quality.
     * @param temporalQuality This parameter governs the level of compression you desire with respect to information between successive frames in the sequence.
     * @param keyFrameRate    Specifies the maximum number of frames allowed between key frames.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetVideoCompressor"
     * @deprecated <I>see setCompressor which takes a Compressor parameter</I>
     * Set a channelÕs current image-compression parameters.
     */
    public void setCompressor(int depth, int compressor, int spatialQuality, int temporalQuality, int keyFrameRate) throws StdQTException {
        int i = SGSetVideoCompressor(_ID(), (short) depth, compressor, spatialQuality, temporalQuality, keyFrameRate);
        StdQTException.checkError(i);
    }

    /**
     * Set a channelÕs current image-compression parameters.
     *
     * @param depth           Specifies the depth at which the image is likely to be viewed.
     * @param compressor      Specifies the image compressor identifier.
     * @param spatialQuality  Specifies the desired compressed image quality.
     * @param temporalQuality This parameter governs the level of compression you desire with respect to information between successive frames in the sequence.
     * @param keyFrameRate    Specifies the maximum number of frames allowed between key frames.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetVideoCompressor"
     */
    public void setCompressor(int depth, Compressor compressor, int spatialQuality, int temporalQuality, int keyFrameRate) throws StdQTException {
        int i = SGSetVideoCompressor(_ID(), (short) depth, QTObject.ID(compressor), spatialQuality, temporalQuality, keyFrameRate);
        StdQTException.checkError(i);
    }

    /**
     * Get a channelÕs current image-compression parameters.
     *
     * @see "QuickTime::SGGetVideoCompressor"
     */
    public SGVideoCompressorInfo getCompressor() throws StdQTException {
        return Compressor.fromSGVideoChannel(this);
    }

    /**
     * Allows you to determine the video digitizer component that is providing source
     * video to a video channel component.
     *
     * @return the Video Digitizer component or null if there is no video digitizer
     * @see "QuickTime::SGGetVideoDigitizerComponent"
     */
    public VideoDigitizer getDigitizerComponent() {
        int i = SGGetVideoDigitizerComponent(_ID());
        return VideoDigitizer.makeVideoDigitizer(i, this);
    }

    /**
     * Allows you to assign a video digitizer component to a video channel.
     *
     * @param vdig the digitizer component that identifies a connection to a video digitizer component.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetVideoDigitizerComponent"
     */
    public void setDigitizerComponent(VideoDigitizer vdig) throws StdQTException {
        StdQTException.checkError(SGSetVideoDigitizerComponent(_ID(), QTObject.ID(vdig)));
    }

    /**
     * Allows you to notify the sequence grabber component whenever you change the
     * configuration of a video channel's video digitizer.
     *
     * @see "QuickTime::SGVideoDigitizerChanged"
     */
    public void digitizerChanged() throws StdQTException {
        StdQTException.checkError(SGVideoDigitizerChanged(_ID()));
    }

    /**
     * Allows you to specify the portion of the source video image that is to be captured.
     *
     * @param r the rectangle that defines the potion of the source video image to be captured.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetVideoRect"
     */
    public void setVideoRect(QDRect r) throws StdQTException {
        int i = SGSetVideoRect(_ID(), r.getRect());
        StdQTException.checkError(i);
    }

    /**
     * Allows you to determine the portion of the source video image that is to be captured.
     *
     * @return the rectangle that defines the portion of the source video being captured.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetVideoRect"
     */
    public QDRect getVideoRect() throws StdQTException {
        QDRect videoRect = new QDRect();
        int i = SGGetVideoRect(_ID(), videoRect.getRect());
        StdQTException.checkError(i);
        return videoRect;
    }

    /**
     * Allows you to determine the size of the source video boundary rectangle.
     *
     * @return the rectangle that defines the size of the source video being captured.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetSrcVideoBounds"
     */
    public QDRect getSrcVideoBounds() throws StdQTException {
        QDRect srcVideoBounds = new QDRect();
        int i = SGGetSrcVideoBounds(_ID(), srcVideoBounds.getRect());
        StdQTException.checkError(i);
        return srcVideoBounds;
    }

    /**
     * The SGSetFrameRate function allows you to specify a video channelÕs frame rate for recording.
     *
     * @param frameRate Specifies the desired frame rate. Set this parameter to 0 to select the channelÕs default frame rate. Typically, this corresponds to the fastest rate that the channel can support.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetFrameRate"
     */
    public void setFrameRate(float frameRate) throws StdQTException {
        int i = SGSetFrameRate(_ID(), QTUtils.X2Fix(frameRate));
        StdQTException.checkError(i);
    }

    /**
     * The SGGetFrameRate function allows you to retrieve a video channelÕs frame rate for recording.
     *
     * @return the current frame rate
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetFrameRate"
     */
    public float getFrameRate() throws StdQTException {
        int[] frameRate = {0};
        int i = SGGetFrameRate(_ID(), frameRate);
        StdQTException.checkError(i);
        return QTUtils.Fix2X(frameRate[0]);
    }

    private static int SGSetVideoCompressorType(int ref, int type) {
        return SequenceGrabberManager.SGSetVideoCompressorType(ref, type);
    }

    private static int SGGetVideoCompressorType(int ref, int[] type) {
        return SequenceGrabberManager.SGGetVideoCompressorType(ref, type);
    }

    private static int SGSetVideoCompressor(int ref, short depth, int compressor, int spatialQuality, int temporalQuality, int keyFrameRate) {
        return SequenceGrabberManager.SGSetVideoCompressor(ref, depth, compressor, spatialQuality, temporalQuality, keyFrameRate);
    }

    private static int SGGetVideoDigitizerComponent(int ref) {
        return SequenceGrabberManager.SGGetVideoDigitizerComponent(ref);
    }

    private static int SGSetVideoDigitizerComponent(int ref, int vdigRef) {
        return SequenceGrabberManager.SGSetVideoDigitizerComponent(ref, vdigRef);
    }

    private static int SGVideoDigitizerChanged(int ref) {
        return SequenceGrabberManager.SGVideoDigitizerChanged(ref);
    }

    private static int SGSetVideoRect(int ref, byte[] rect) {
        return SequenceGrabberManager.SGSetVideoRect(ref, rect);
    }

    private static int SGGetVideoRect(int ref, byte[] rect) {
        return SequenceGrabberManager.SGGetVideoRect(ref, rect);
    }

    private static int SGGetSrcVideoBounds(int ref, byte[] rect) {
        return SequenceGrabberManager.SGGetSrcVideoBounds(ref, rect);
    }

    private static int SGSetFrameRate(int ref, int frameRate) {
        return SequenceGrabberManager.SGSetFrameRate(ref, frameRate);
    }

    private static int SGGetFrameRate(int ref, int[] frameRate) {
        return SequenceGrabberManager.SGGetFrameRate(ref, frameRate);
    }
}
