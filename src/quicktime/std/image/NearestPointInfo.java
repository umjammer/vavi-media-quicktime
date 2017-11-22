
package quicktime.std.image;

public class NearestPointInfo {
    public int contourIndex;

    public int pointIndex;

    public float deltaDistance;

    NearestPointInfo(int paramInt1, int paramInt2, float paramFloat) {
        this.contourIndex = paramInt1;
        this.pointIndex = paramInt2;
        this.deltaDistance = paramFloat;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /NearestPointInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
