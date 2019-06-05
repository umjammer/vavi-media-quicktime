
package quicktime.std.sg;

import quicktime.std.image.Compressor;


public class SGVideoCompressorInfo {
    public int mDepth;

    /**
     * @deprecated
     */
    public int mCompressor;

    public Compressor theCompressor;

    public int mSpatialQuality;

    public int mTemporalQuality;

    public int mKeyFrameRate;

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
