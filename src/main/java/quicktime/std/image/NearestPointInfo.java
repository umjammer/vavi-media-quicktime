package quicktime.std.image;

/**
 * This class contains information about the index and distance values of
 * a specified point contained in a GXPaths collection of GXPath collection of points.
 */
public class NearestPointInfo {

    /**
     * The contour index value of the point
     */
    public final int contourIndex;

    /**
     * The point index value of the point
     */
    public final int pointIndex;

    /**
     * The distance between the specified point and the closest point in the contour to it.
     */
    public final float deltaDistance;

    NearestPointInfo(int contourIndex, int pointIndex, float deltaDistance) {
        this.contourIndex = contourIndex;
        this.pointIndex = pointIndex;
        this.deltaDistance = deltaDistance;
    }
}
