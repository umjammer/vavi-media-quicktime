
package quicktime.std.qtcomponents;

import quicktime.util.QTHandleRef;


public final class SCInfo {
    public QTHandleRef imageData;

    public int dataSize;

    public short notSyncFlag;

    public SCInfo(QTHandleRef paramQTHandleRef, int paramInt, short paramShort) {
        this.imageData = paramQTHandleRef;
        this.dataSize = paramInt;
        this.notSyncFlag = paramShort;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/SCInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
