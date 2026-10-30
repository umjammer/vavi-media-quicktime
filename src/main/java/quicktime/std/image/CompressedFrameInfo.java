package quicktime.std.image;

import quicktime.util.QTUtils;

/**
 * Contains information returned by the CSequence.compressFrame() method.
 */
public final class CompressedFrameInfo {

    final int mDataSize;

    final int mSimilarity;

    CompressedFrameInfo(int dataSize, byte similarity) {
        this.mDataSize = dataSize;
        this.mSimilarity = QTUtils.UByte2Int(similarity);
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
