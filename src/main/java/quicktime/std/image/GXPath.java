package quicktime.std.image;

import quicktime.qd.QDPoint;
import quicktime.util.QTByteObject;

/**
 * You use the GXPath to specify a single contour composed of
 * straight lines and curves.
 */
public final class GXPath extends QTByteObject {

    /**
     * Creates a GXPath object with 1 contour and the number of points specified
     * @param numberOfVectors the number of geometric points in the contour.
     */
    public GXPath(int paramInt) {
        super(4 + 4 * paramInt + 8 * paramInt);
        setIntAt(0, paramInt);
    }

    /**
     * Returns the no of points in the contour
     * @return the no of points in the contour
     */
    public int getVectors() {
        return getIntAt(0);
    }

    /**
     * Returns the control bit of the corresponding point in the contour
     * @param whichPoint the index of point in the contour
     */
    public boolean getControlBit(int paramInt) {
        return getIntAt(4 + paramInt * 4) != 0;
    }

    /**
     * Sets the ControlBit of point in the contour ( 1 == onCurve )
     * @param whichPoint the  index of point in the contour
     * @param onOrOffCurve the controlBit value ( Consecutive onCurve points
     *  are joined with a straight line and offCurve points are joined with
     *  a curve).
     */
    public void setControlBit(int paramInt, boolean paramBoolean) {
        setIntAt(4 + paramInt * 4, paramBoolean ? 1 : 0);
    }

    /**
     * Returns the point in the contour
     * @param whichPoint the index of points in the contour
     * @return the point in the contour at the specified specified
     */
    public QDPoint getVector(int paramInt) {
        int i = 4 + getVectors() * 4 + paramInt * 8;
        return QDPoint.fromArray(getBytes(), 8);
    }

    /**
     * Sets the point in the contour at the specified index
     * @param whichPoint the index of the point in the contour
     * @param pt the point value to be set
     */
    public void setVector(int paramInt, QDPoint paramQDPoint) {
        int i = 4 + getVectors() * 4 + paramInt * 8;
        System.arraycopy(paramQDPoint.getGXPoint(), 0, getBytes(), i, 8);
    }
}
