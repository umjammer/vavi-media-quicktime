
package quicktime.std.movies.media;

import quicktime.std.movies.TimeInfo;


public class SampleInfo extends TimeInfo {
    public int offset;

    public int size;

    public int numberOfSamples;

    public SampleDescription description;

    public int flags;

    public int descriptionIndex;

    SampleInfo(int paramInt1,
            int paramInt2,
            int paramInt3,
            int paramInt4,
            int paramInt5,
            SampleDescription paramSampleDescription,
            int paramInt6,
            int paramInt7) {
        super(paramInt1, paramInt2);
        this.offset = paramInt3;
        this.size = paramInt4;
        this.numberOfSamples = paramInt5;
        this.description = paramSampleDescription;
        this.flags = paramInt7;
        this.descriptionIndex = paramInt6;
    }

    public String toString() {
        return getClass().getName() + "[time=" + this.time + ",duration=" + this.duration + ",offset=" + this.offset + ",size="
               + this.size + ",numSamples=" + this.numberOfSamples + ",flags=" + this.flags + ",description=" + this.description
               + ",descriptionIndex=" + this.descriptionIndex + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SampleInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
