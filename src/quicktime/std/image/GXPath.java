
package quicktime.std.image;

import quicktime.qd.QDPoint;
import quicktime.util.QTByteObject;


public final class GXPath extends QTByteObject {
    public GXPath(int paramInt) {
        super(4 + 4 * paramInt + 8 * paramInt);
        setIntAt(0, paramInt);
    }

    public int getVectors() {
        return getIntAt(0);
    }

    public boolean getControlBit(int paramInt) {
        return getIntAt(4 + paramInt * 4) != 0;
    }

    public void setControlBit(int paramInt, boolean paramBoolean) {
        setIntAt(4 + paramInt * 4, paramBoolean ? 1 : 0);
    }

    public QDPoint getVector(int paramInt) {
        int i = 4 + getVectors() * 4 + paramInt * 8;
        return QDPoint.fromArray(getBytes(), 8);
    }

    public void setVector(int paramInt, QDPoint paramQDPoint) {
        int i = 4 + getVectors() * 4 + paramInt * 8;
        System.arraycopy(paramQDPoint.getGXPoint(), 0, getBytes(), i, 8);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GXPath.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
