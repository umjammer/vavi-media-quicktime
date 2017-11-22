
package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.std.movies.Track;
import quicktime.util.QTPointer;


public final class SourcerTrackParams extends QTPointer {
    public static final int kNativeSize = 32;

    SourcerTrackParams() throws QTException {
        super(32, true);
    }

    SourcerTrackParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    public SourcerTrackParams(int paramInt, Track paramTrack) throws QTException {
        this(1, paramInt, paramTrack, 0L, 0L, null);
    }

    public SourcerTrackParams(int paramInt,
            Track paramTrack,
            long paramLong1,
            long paramLong2,
            SourcerLoopParams paramSourcerLoopParams) throws QTException {
        this(1, paramInt, paramTrack, paramLong1, paramLong2, paramSourcerLoopParams);
    }

    public SourcerTrackParams(int paramInt1,
            int paramInt2,
            Track paramTrack,
            long paramLong1,
            long paramLong2,
            SourcerLoopParams paramSourcerLoopParams) throws QTException {
        super(32, true);
        setVersion(paramInt1);
        setFlags(paramInt2);
        setTrack(paramTrack);
        setTrackStartOffset(paramLong1);
        setDuration(paramLong2);
        setLoopParams(paramSourcerLoopParams);
    }

    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getVersion() {
        return getIntAt(0);
    }

    public void setFlags(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getFlags() {
        return getIntAt(4);
    }

    public void setTrack(Track paramTrack) throws QTRuntimeException {
        if (paramTrack != null) {
            setIntAt(8, QTObject.ID(paramTrack));
            this.itsTrack = paramTrack;
        } else {
            throw new QTRuntimeException("Track must not be null");
        }
    }

    public Track getTrack() {
        return this.itsTrack;
    }

    public void setTrackStartOffset(long paramLong) {
        setLongAt(12, paramLong);
    }

    public long getTrackStartOffset() {
        return getLongAt(12);
    }

    public void setDuration(long paramLong) {
        setLongAt(20, paramLong);
    }

    public long getDuration() {
        return getLongAt(20);
    }

    public void setLoopParams(SourcerLoopParams paramSourcerLoopParams) {
        if (paramSourcerLoopParams != null) {
            setIntAt(28, QTObject.ID(paramSourcerLoopParams));
            this.itsParams = paramSourcerLoopParams;
        } else {
            setIntAt(28, 0);
            this.itsParams = null;
        }
    }

    public SourcerLoopParams getLoopParams() {
        return this.itsParams;
    }

    private Track itsTrack = null;

    private SourcerLoopParams itsParams = null;

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",track=" + getTrack()
               + ",trackStartOffset" + getTrackStartOffset() + ",duration" + getDuration() + ",loopParams" + getLoopParams()
               + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /SourcerTrackParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
