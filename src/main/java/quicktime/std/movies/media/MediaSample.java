
package quicktime.std.movies.media;

import quicktime.util.QTHandle;


public final class MediaSample extends SampleInfo {
    public QTHandle data;

    MediaSample(int paramInt1,
            int paramInt2,
            QTHandle paramQTHandle,
            int paramInt3,
            int paramInt4,
            int paramInt5,
            SampleDescription paramSampleDescription,
            int paramInt6,
            int paramInt7) {
        super(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramSampleDescription, paramInt6, paramInt7);
        this.data = paramQTHandle;
    }

    public String toString() {
        return getClass().getName() + "[time=" + this.time + ",duration=" + this.duration + ",offset=" + this.offset + ",size="
               + this.size + ",numSamples=" + this.numberOfSamples + ",flags=" + this.flags + ",description=" + this.description
               + ",descriptionIndex=" + this.descriptionIndex + ",data=" + this.data + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MediaSample.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
