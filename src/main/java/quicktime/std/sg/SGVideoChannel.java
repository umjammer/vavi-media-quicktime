package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SGVideoChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGVideoChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    /**
     * Creates a sequence grabber channel and assigns a channel component to the channel.
     * The channel component is responsible for providing digitized data to the
     * sequence grabber component.
     * QuickTime::SGNewChannel
     * @param owner specifies the sequence grabber component the channel is attached to.
     */
    public SGVideoChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramSequenceGrabber, 1986618469);
    }

    final SampleDescription makeDescription() throws QTException {
        return new ImageDescription(0);
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     * QuickTime::SGGetChannelSampleDescription()
     * @return a ImageDescription object.
     * @return {@link quicktime.QTException}
     */
    public final ImageDescription getImageDescription() throws QTException {
        return (ImageDescription) getSampleDescription();
    }

    /**
     * Set the type of Video Compressor
     * QuickTime::SGSetVideoCompressorType
     * @param compressorType sets the type of compressor to use on the channel.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setCompressorType(int paramInt) throws StdQTException {
        int i = SGSetVideoCompressorType(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Get the Video Compressor Type
     * QuickTime::SGGetVideoCompressorType
     */
    public int getCompressorType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SGGetVideoCompressorType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Deprecated.
     * <I>see setCompressor which takes a Compressor parameter</I>
     * Set a channelÕs current image-compression parameters.
     * QuickTime::SGSetVideoCompressor
     * @param depth Specifies the depth at which the image is likely to be viewed.
     * @param compressor Specifies the image compressor identifier.
     * @param spatialQuality Specifies the desired compressed image quality.
     * @param temporalQuality This parameter governs the level of compression you desire with respect to information between successive frames in the sequence.
     * @param keyFrameRate Specifies the maximum number of frames allowed between key frames.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setCompressor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws StdQTException {
        int i = SGSetVideoCompressor(_ID(), (short) paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
        StdQTException.checkError(i);
    }

    /**
     * Set a channelÕs current image-compression parameters.
     * QuickTime::SGSetVideoCompressor
     * @param depth Specifies the depth at which the image is likely to be viewed.
     * @param compressor Specifies the image compressor identifier.
     * @param spatialQuality Specifies the desired compressed image quality.
     * @param temporalQuality This parameter governs the level of compression you desire with respect to information between successive frames in the sequence.
     * @param keyFrameRate Specifies the maximum number of frames allowed between key frames.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setCompressor(int paramInt1, Compressor paramCompressor, int paramInt2, int paramInt3, int paramInt4) throws StdQTException {
        int i = SGSetVideoCompressor(_ID(), (short) paramInt1, QTObject.ID(paramCompressor), paramInt2, paramInt3, paramInt4);
        StdQTException.checkError(i);
    }

    /**
     * Get a channelÕs current image-compression parameters.
     * QuickTime::SGGetVideoCompressor
     */
    public SGVideoCompressorInfo getCompressor() throws StdQTException {
        return Compressor.fromSGVideoChannel(this);
    }

    /**
     * Allows you to determine the video digitizer component that is providing source
     * video to a video channel component.
     * QuickTime::SGGetVideoDigitizerComponent
     * @return the Video Digitizer component or null if there is no video digitizer
     */
    public VideoDigitizer getDigitizerComponent() {
        int i = SGGetVideoDigitizerComponent(_ID());
        return VideoDigitizer.makeVideoDigitizer(i, this);
    }

    /**
     * Allows you to assign a video digitizer component to a video channel.
     * QuickTime::SGSetVideoDigitizerComponent
     * @param vdig the digitizer component that identifies a connection to a video digitizer component.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDigitizerComponent(VideoDigitizer paramVideoDigitizer) throws StdQTException {
        StdQTException.checkError(SGSetVideoDigitizerComponent(_ID(), QTObject.ID(paramVideoDigitizer)));
    }

    /**
     * Allows you to notify the sequence grabber component whenever you change the
     * configuration of a video channel's video digitizer.
     * QuickTime::SGVideoDigitizerChanged
     */
    public void digitizerChanged() throws StdQTException {
        StdQTException.checkError(SGVideoDigitizerChanged(_ID()));
    }

    /**
     * Allows you to specify the portion of the source video image that is to be captured.
     * QuickTime::SGSetVideoRect
     * @param r the rectangle that defines the potion of the source video image to be captured.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setVideoRect(QDRect paramQDRect) throws StdQTException {
        int i = SGSetVideoRect(_ID(), paramQDRect.getRect());
        StdQTException.checkError(i);
    }

    /**
     * Allows you to determine the portion of the source video image that is to be captured.
     * QuickTime::SGGetVideoRect
     * @return the rectangle that defines the portion of the source video being captured.
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getVideoRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = SGGetVideoRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * Allows you to determine the size of the source video boundary rectangle.
     * QuickTime::SGGetSrcVideoBounds
     * @return the rectangle that defines the size of the source video being captured.
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getSrcVideoBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = SGGetSrcVideoBounds(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * The SGSetFrameRate function allows you to specify a video channelÕs frame rate for recording.
     * QuickTime::SGSetFrameRate
     * @param frameRate Specifies the desired frame rate. Set this parameter to 0 to select the channelÕs default frame rate. Typically, this corresponds to the fastest rate that the channel can support.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setFrameRate(float paramFloat) throws StdQTException {
        int i = SGSetFrameRate(_ID(), QTUtils.X2Fix(paramFloat));
        StdQTException.checkError(i);
    }

    /**
     * The SGGetFrameRate function allows you to retrieve a video channelÕs frame rate for recording.
     * QuickTime::SGGetFrameRate
     * @return the current frame rate
     * @return {@link quicktime.std.StdQTException}
     */
    public float getFrameRate() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = SGGetFrameRate(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    private static native int SGSetVideoCompressorType(int paramInt1, int paramInt2);

    private static native int SGGetVideoCompressorType(int paramInt, int[] paramArrayOfInt);

    private static native int SGSetVideoCompressor(int paramInt1, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native int SGGetVideoDigitizerComponent(int paramInt);

    private static native int SGSetVideoDigitizerComponent(int paramInt1, int paramInt2);

    private static native int SGVideoDigitizerChanged(int paramInt);

    private static native int SGSetVideoRect(int paramInt, byte[] paramArrayOfByte);

    private static native int SGGetVideoRect(int paramInt, byte[] paramArrayOfByte);

    private static native int SGGetSrcVideoBounds(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSetFrameRate(int paramInt1, int paramInt2);

    private static native int SGGetFrameRate(int paramInt, int[] paramArrayOfInt);
}

