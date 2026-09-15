package quicktime.std.sg;

/**
 * The SGChannelInfo class contains information about the reference information of a media sample.
 */
public class SGChannelInfo {

    /**
     * The  sample's size, eg returns 8 for 8 bit, 16 for 16 bit.
     */
    public int mSampleSize;

    /**
     * Number of Channels 1-for Mono, 2 for Stereo.
     */
    public int mNumChannels;

    /**
     * Format of Channel data, e.g  'raw', 'MAC3', 'MAC6'.
     */
    public int mCompressionType;

    SGChannelInfo(int paramInt1, int paramInt2, int paramInt3) {
        this.mSampleSize = paramInt1;
        this.mNumChannels = paramInt2;
        this.mCompressionType = paramInt3;
    }
}

