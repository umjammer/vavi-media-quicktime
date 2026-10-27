package quicktime.std.image;

import quicktime.qd.QDPoint;

/**
 * This class contains information about a Point that is in GXPath collection and
 * whether the requested point is on or off the path.
 */
public class PathPointInfo {

    /**
     * The QDPoint is on the queried path.
     */
    public boolean isOnPath;

    /**
     * The QDPoint that was found at the specified index of the queried path.
     */
    public QDPoint point;

    PathPointInfo(QDPoint paramQDPoint, boolean paramBoolean) {
        this.point = paramQDPoint;
        this.isOnPath = paramBoolean;
    }
}
