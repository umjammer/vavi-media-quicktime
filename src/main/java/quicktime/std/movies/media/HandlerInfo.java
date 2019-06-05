
package quicktime.std.movies.media;

import quicktime.util.QTUtils;


public class HandlerInfo {
    public int subType;

    public String name;

    public int manufacturerID;

    public HandlerInfo(int paramInt1, String paramString, int paramInt2) {
        this.subType = paramInt1;
        this.name = paramString;
        this.manufacturerID = paramInt2;
    }

    public String toString() {
        return getClass().getName() + "[mediaType=" + QTUtils.fromOSType(this.subType) + ",manufacturerName=" + this.name + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/HandlerInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
