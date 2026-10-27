package quicktime.std.image;

import quicktime.util.QTUtils;

/**
 * Contains information returned by the CSequence.compressFrame() method.
 */
public final class CompressedFrameInfo {

    int mDataSize;

    int mSimilarity;

    CompressedFrameInfo(int paramInt, byte paramByte) {
        this.mDataSize = paramInt;
        this.mSimilarity = QTUtils.UByte2Int(paramByte);
    }

    /**
     * Returns the size, in bytes, of the compressed image.
     */
    public int getDataSize() {
        return this.mDataSize;
    }

    /**
     * Returns the similarity value of the image just compressed.
     */
    public int getSimilarity() {
        return this.mSimilarity;
    }

    /**
     * Return a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[similarity=" + this.mSimilarity + ",dataSize=" + this.mDataSize + "]";
    }
}
