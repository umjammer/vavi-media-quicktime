package quicktime.std.qtcomponents;

import quicktime.util.QTHandleRef;

public final class SCInfo {

    /**
     * The compressed image data of a frame
     */
    public final QTHandleRef imageData;

    /**
     * The actual number of bytes in the handle that is the compressed data.
     * This may be less than the handle's actual size, in which case only
     * the bytes up to this size are valid compressed data.
     */
    public final int dataSize;

    /**
     * Indicates whether the frame is a key frame.
     */
    public final short notSyncFlag;

    public SCInfo(QTHandleRef imageData, int dataSize, short notSyncFlag) {
        this.imageData = imageData;
        this.dataSize = dataSize;
        this.notSyncFlag = notSyncFlag;
    }
}
