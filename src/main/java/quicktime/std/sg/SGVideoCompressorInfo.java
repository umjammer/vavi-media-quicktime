package quicktime.std.sg;

import quicktime.std.image.Compressor;

/**
 * The SGChannelInfo class contains information about the reference information of a media sample.
 */
public class SGVideoCompressorInfo {

    public int mDepth;

    /**
     * @deprecated
     * <I>use theCompressor field</I>
     * This field is no longer used and is ALWAYS set to zero
     */
    public int mCompressor;

    public Compressor theCompressor;

    public int mSpatialQuality;

    public int mTemporalQuality;

    public int mKeyFrameRate;

    /**
     * This constructor creates a SGVideoCompressorInfo object.
     * @param depth Specifies the depth at which the image is likely to be viewed.
     * @param compressor Specifies the image compressor identifier.
     * @param spatialQuality Specifies the desired compressed image quality.
     * @param temporalQuality format of the sound Data.
     * @param keyFrameRate format of the sound Data.
     */
    public SGVideoCompressorInfo(int depth, Compressor compressor, int spatialQuality, int temporalQuality, int keyFrameRate) {
        this.mDepth = depth;
        this.mCompressor = 0;
        this.mSpatialQuality = spatialQuality;
        this.mTemporalQuality = temporalQuality;
        this.mKeyFrameRate = keyFrameRate;
        this.theCompressor = compressor;
    }
}

