package quicktime.std.movies.media;

import quicktime.std.movies.TimeInfo;


/**
 * The SampleTimeInfo class encapsulates a media sample's timing information.
 */
public final class SampleTimeInfo extends TimeInfo {

    /**
     * This method returns the sample number in media.
     */
    public final int sampleNum;

    /**
     * This constructor creates a SampleTimeInfo object.
     *
     * @param time     Specifies that sample time
     * @param duration Specifies the sample duration.
     */
    public SampleTimeInfo(int time, int duration, int sampleNum) {
        super(time, duration);
        this.sampleNum = sampleNum;
    }
}
