
package quicktime.std.image;

import quicktime.util.QTUtils;


public final class CompressedFrameInfo {
    int mDataSize;

    int mSimilarity;

    CompressedFrameInfo(int paramInt, byte paramByte) {
        this.mDataSize = paramInt;
        this.mSimilarity = QTUtils.UByte2Int(paramByte);
    }

    public int getDataSize() {
        return this.mDataSize;
    }

    public int getSimilarity() {
        return this.mSimilarity;
    }

    public String toString() {
        return getClass().getName() + "[similarity=" + this.mSimilarity + ",dataSize=" + this.mDataSize + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /CompressedFrameInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
