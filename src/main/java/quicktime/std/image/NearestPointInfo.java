package quicktime.std.image;

/**
 * This class contains information about the index and distance values of
 * a specified point contained in a GXPaths collection of GXPath collection of points.
 */
public class NearestPointInfo {

    /**
     * The contour index value of the point
     */
    public int contourIndex;

    /**
     * The point index value of the point
     */
    public int pointIndex;

    /**
     * The distance between the specified point and the closest point in the contour to it.
     */
    public float deltaDistance;

    NearestPointInfo(int paramInt1, int paramInt2, float paramFloat) {
        this.contourIndex = paramInt1;
        this.pointIndex = paramInt2;
        this.deltaDistance = paramFloat;
    }
}
