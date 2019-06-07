package quicktime.std.sg;

import quicktime.std.image.Compressor;

/**
 * The SGChannelInfo class contains information about the reference information of a media sample.
 */
public class SGVideoCompressorInfo {

    public int mDepth;

    /**
     * Deprecated.
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
    public SGVideoCompressorInfo(int paramInt1, Compressor paramCompressor, int paramInt2, int paramInt3, int paramInt4) {
        this.mDepth = paramInt1;
        this.mCompressor = 0;
        this.mSpatialQuality = paramInt2;
        this.mTemporalQuality = paramInt3;
        this.mKeyFrameRate = paramInt4;
        this.theCompressor = paramCompressor;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SGVideoCompressorInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
