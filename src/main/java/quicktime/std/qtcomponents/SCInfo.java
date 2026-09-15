package quicktime.std.qtcomponents;

import quicktime.util.QTHandleRef;

public final class SCInfo {

    /**
     * The compressed image data of a frame
     */
    public QTHandleRef imageData;

    /**
     * The actual number of bytes in the handle that is the compressed data.
     * This may be less than the handle's actual size, in which case only
     * the bytes up to this size are valid compressed data.
     */
    public int dataSize;

    /**
     * Indicates whether the frame is a key frame.
     */
    public short notSyncFlag;

    public SCInfo(QTHandleRef paramQTHandleRef, int paramInt, short paramShort) {
        this.imageData = paramQTHandleRef;
        this.dataSize = paramInt;
        this.notSyncFlag = paramShort;
    }
}

