
package quicktime.std.image;

import quicktime.qd.QDPoint;


public class PathPointInfo {
    public boolean isOnPath;

    public QDPoint point;

    PathPointInfo(QDPoint paramQDPoint, boolean paramBoolean) {
        this.point = paramQDPoint;
        this.isOnPath = paramBoolean;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /PathPointInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
