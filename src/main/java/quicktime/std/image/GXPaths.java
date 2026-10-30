package quicktime.std.image;

import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


/**
 * The GXPaths class allows you to group multiple path contours together.
 * You use this class when specifying the geometry of a path shape.
 */
public final class GXPaths extends QTByteObject implements QuickTimeLib {

    private final Curve comp;

    private static int howManyBytes(GXPath[] gxPaths) {
        int i = 0;
        for (GXPath gxPath : gxPaths) i += gxPath.getSize();
        return i;
    }

    /**
     * Creates a GXPaths object which corresponds to gxPaths object of QuickDrawGX
     * It sets the total number of contours in the path equal to the total number of paths
     * ie every path has one contour.
     *
     * @param curveComponent the curve Component
     * @param ar             array of GXPath objects containing the geometries for the paths
     */
    public GXPaths(Curve curveComponent, GXPath[] ar) {
        super(howManyBytes(ar) + 4);
        setIntAt(0, ar.length);
        int i = 4;
        for (GXPath gxPath : ar) {
            setBytesAt(i, gxPath.getSize(), gxPath.getBytes(), 0);
            i += gxPath.getSize();
        }
        this.comp = curveComponent;
    }

    /**
     * Returns the no. of contours in the Paths object
     *
     * @return the total number of coutours
     */
    public int getContours() {
        return getIntAt(0);
    }

    /**
     * This method is used to count the points along either one of a path's contours
     * or all of its contours.
     *
     * @param contourIndex the index of the contour to be counted.
     * @return the number of points in the contour or path.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::CurveCountPointsInPath"
     */
    public int countPointsInPath(int contourIndex) throws StdQTException {
        int[] count = {0};
        int i = CurveCountPointsInPath(QTObject.ID(this.comp), getBytes(), contourIndex, count);
        StdQTException.checkError(i);
        return count[0];
    }

    /**
     * This method is best for adding a single point to a path rather than large numbers of points to a path.
     *
     * @param contourIndex Specifies the index of the path contour to which to add the point.
     * @param pointIndex   Specifies the index of the point to add.
     * @param ptIsOnPath   If TRUE , specifies that the new point is to be on the path.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::CurveSetPathPoint"
     */
    public void setPathPoint(int contourIndex, int pointIndex, QDPoint point, boolean ptIsOnPath) throws StdQTException {
        int i = CurveSetPathPoint(QTObject.ID(this.comp), getBytes(), contourIndex, pointIndex, point.getGXPoint(), (byte) (ptIsOnPath ? 1 : 0));
        StdQTException.checkError(i);
    }

    /**
     * This method is used to get a point from a path and to find out if the point is on the curve.
     *
     * @param contourIndex Specifies the index of the contour from which to get the point.
     * @param pointIndex   Specifies the index of the point to get.
     * @return thePoint field that is to receive the point.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::CurveGetPathPoint"
     */
    public PathPointInfo getPathPoint(int contourIndex, int pointIndex) throws StdQTException {
        QDPoint point = new QDPoint(0.0F, 0.0F);
        byte[] isOnPath = new byte[1];
        int i = CurveGetPathPoint(QTObject.ID(this.comp), getBytes(), contourIndex, pointIndex, point.getGXPoint(), isOnPath);
        StdQTException.checkError(i);
        return new PathPointInfo(point, isOnPath[0] != 0);
    }

    /**
     * This method is used to get the point at a specified distance along a curve.
     *
     * @param index  Specifies the index of the path contour from which to get the point.
     * @param length Specifies the distance along the curve at which to find the point.
     * @return QDPoint[] array of two points containing the location (first element) and the tangent point (second element)
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::CurveLengthToPoint"
     */
    public QDPoint[] lengthToPoint(int index, int length) throws StdQTException {
        QDPoint point1 = new QDPoint(0.0F, 0.0F);
        QDPoint point2 = new QDPoint(0.0F, 0.0F);
        int i = CurveLengthToPoint(QTObject.ID(this.comp), getBytes(), index, length, point1.getGXPoint(), point2.getGXPoint());
        StdQTException.checkError(i);
        QDPoint[] points = new QDPoint[2];
        points[0] = point1;
        points[1] = point2;
        return points;
    }

    /**
     * This method is used to calculate the length of one of a path's contours or the
     * sum of the lengths of all of its contours.
     *
     * @param index Contains the index of the contour whose length is to be calculated or, if the value is 0 ,
     *              specifies to calculate the lengths of all of the path's contours and return the sum of the lengths.
     * @return wideLength Contains the length.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::CurveGetLength"
     */
    public long getLength(int index) throws StdQTException {
        long[] length = {0L};
        int i = CurveGetLength(QTObject.ID(this.comp), getBytes(), index, length);
        StdQTException.checkError(i);
        return length[0];
    }

    /**
     * This method is used to to get the length of a path between specified starting
     * and ending distances that is nearest a point.
     *
     * @param startDist Specifies the distance along the path at which to start measuring the path's length.
     * @param endDist   Specifies the distance along the path at which to stop measuring the path's length.
     * @param thePoint  Contains a pointer to a point; the function measures the path closest to this point.
     * @return the length of the specified part of the path.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::CurvePathPointToLength"
     */
    public float getPathPointToLength(int startDist, int endDist, QDPoint thePoint) throws StdQTException {
        int[] length = {0};
        int i = CurvePathPointToLength(QTObject.ID(this.comp), getBytes(), startDist, endDist, thePoint.getGXPoint(), length);
        StdQTException.checkError(i);
        return QTUtils.Fix2X(length[0]);
    }

    /**
     * This method is used to to find the closest point on a path to a specified point.
     * In programs where users directly manipulate curves, you can use this function
     * to determine the closest control point to a given point.
     *
     * @param thePoint Contains a point for which to find the closest point on the path.
     * @return information about the specified point
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::CurveGetNearestPathPoint"
     */
    public NearestPointInfo getNearestPathPoint(QDPoint thePoint) throws StdQTException {
        int[] contourIndex = {0};
        int[] pointIndex = {0};
        int[] delta = {0};
        int i = CurveGetNearestPathPoint(QTObject.ID(this.comp), getBytes(), thePoint.getGXPoint(), contourIndex, pointIndex, delta);
        StdQTException.checkError(i);
        return new NearestPointInfo(contourIndex[0], pointIndex[0], QTUtils.Fix2X(delta[0]));
    }

    private static int CurveCountPointsInPath(int effect, byte[] paths, int contourIndex, int[] count) {
        return ImageCompressionManager.CurveCountPointsInPath(effect, paths, contourIndex, count);
    }

    private static int CurveSetPathPoint(int effect, byte[] paths, int counterIndex, int pointIndex, byte[] point, byte isOnPath) {
        return ImageCompressionManager.CurveSetPathPoint(effect, paths, counterIndex, pointIndex, point, isOnPath);
    }

    private static int CurveGetLength(int effect, byte[] paths, int contourIndex, long[] length) {
        return ImageCompressionManager.CurveGetLength(effect, paths, contourIndex, length);
    }

    private static int CurveGetPathPoint(int effect, byte[] paths, int contourIndex, int pointIndex, byte[] point, byte[] isOnPath) {
        return ImageCompressionManager.CurveGetPathPoint(effect, paths, contourIndex, pointIndex, point, isOnPath);
    }

    private static int CurveLengthToPoint(int effect, byte[] paths, int contourIndex, int length, byte[] point, byte[] tangent) {
        return ImageCompressionManager.CurveLengthToPoint(effect, paths, contourIndex, length, point, tangent);
    }

    private static int CurveGetNearestPathPoint(int effect, byte[] paths, byte[] point, int[] contourIndex, int[] pointIndex, int[] delta) {
        return ImageCompressionManager.CurveGetNearestPathPoint(effect, paths, point, contourIndex, pointIndex, delta);
    }

    private static int CurvePathPointToLength(int effect, byte[] paths, int startDist, int endDist, byte[] point, int[] length) {
        return ImageCompressionManager.CurvePathPointToLength(effect, paths, startDist, endDist, point, length);
    }
}
