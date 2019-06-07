package quicktime.std.movies.media;

import quicktime.std.movies.TimeInfo;

/**
 * The SampleTimeInfo class encapsulates a media sample's timing information.
 */
public final class SampleTimeInfo extends TimeInfo {

    /**
     * This method returns the sample number in media.
     */
    public int sampleNum;

    /**
     * This constructor creates a SampleTimeInfo object.
     * @param time Specifies that sample time
     * @param duration Specifies the sample duration.
     */
    public SampleTimeInfo(int paramInt1, int paramInt2, int paramInt3) {
        super(paramInt1, paramInt2);
        this.sampleNum = paramInt3;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SampleTimeInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
