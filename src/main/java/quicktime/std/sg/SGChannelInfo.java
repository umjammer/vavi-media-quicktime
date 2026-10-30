package quicktime.std.sg;

/**
 * The SGChannelInfo class contains information about the reference information of a media sample.
 */
public class SGChannelInfo {

    /**
     * The  sample's size, eg returns 8 for 8 bit, 16 for 16 bit.
     */
    public final int mSampleSize;

    /**
     * Number of Channels 1-for Mono, 2 for Stereo.
     */
    public final int mNumChannels;

    /**
     * Format of Channel data, e.g  'raw', 'MAC3', 'MAC6'.
     */
    public final int mCompressionType;

    SGChannelInfo(int sampleSize, int numChannels, int compressionType) {
        this.mSampleSize = sampleSize;
        this.mNumChannels = numChannels;
        this.mCompressionType = compressionType;
    }
}
