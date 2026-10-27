package quicktime.std.movies;

import quicktime.QTException;
import quicktime.qd.QDColor;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.std.image.Matrix;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * TweenData is a variable data structure that is used to set tween atom values
 * that are stored in an atom of an AtomContainer with insertChild(... tweenData)
 */
public final class TweenData extends QTByteObject {

    public static final int kQDPointNativeSize = 4;

    public static final int kQDRectNativeSize = 8;

    public static final int kRGBNativeSize = 6;

    public static final int kModifierTrackGraphicsModeNativeSize = 10;

    public static final int kMatrixNativeSize = 36;

    public static final int kFixedPointNativeSize = 8;

    public static final int kQTFloatDoubleNativeSize = 8;

    private final int type;

    /**
     * Allocates a TweenData object of type kTweenTypeShort from short start and end values
     * @param start The interpolation start value
     * @param end The interpolation end value
     */
    public TweenData(short paramShort1, short paramShort2) throws QTException {
        super(allocSize(1));
        this.type = 1;
        setShortAt(0, paramShort1);
        setShortAt(2, paramShort2);
    }

    /**
     * Allocates a TweenData object of type kTweenTypeLong from int start and end values
     * @param start The interpolation start value
     * @param end The interpolation end value
     */
    public TweenData(int paramInt1, int paramInt2) throws QTException {
        super(allocSize(2));
        this.type = 2;
        setIntAt(0, paramInt1);
        setIntAt(4, paramInt2);
    }

    /**
     * Allocates a TweenData object of type kTweenTypeFixed or kTweenTypeQTFloatSingle from float start and end values
     * @param tweenType kTweenTypeFixed or kTweenTypeQTFloatSingle
     * @param start The interpolation start value
     * @param end The interpolation end value
     */
    public TweenData(int paramInt, float paramFloat1, float paramFloat2) throws QTException {
        super(allocSize(paramInt));
        this.type = paramInt;
        switch(this.type) {
            case 3:
                setIntAt(0, QTUtils.X2Fix(paramFloat1));
                setIntAt(4, QTUtils.X2Fix(paramFloat2));
                break;
            case 10:
                setFloatAt(0, paramFloat1);
                setFloatAt(4, paramFloat2);
        }
    }

    /**
     * Allocates a TweenData object of type kTweenTypeQTFloatDouble from double start and end values
     * @param start The interpolation start value
     * @param end The interpolation end value
     */
    public TweenData(double paramDouble1, double paramDouble2) throws QTException {
        super(allocSize(11));
        this.type = 11;
        setDoubleAt(0, paramDouble1);
        setDoubleAt(8, paramDouble2);
    }

    /**
     * Allocates a TweenData object of type kTweenTypePoint from QDPoint start and end values
     * @param start The interpolation start Point
     * @param end The interpolation end Point
     */
    public TweenData(QDPoint paramQDPoint1, QDPoint paramQDPoint2) throws QTException {
        super(allocSize(4));
        this.type = 4;
        setIntAt(0, paramQDPoint1.getPoint());
        setIntAt(4, paramQDPoint2.getPoint());
    }

    /**
     * Allocates a TweenData object of type kTweenTypeQDRect from QDRect start and end values
     * @param start The interpolation start Rect
     * @param end The interpolation end Rect
     */
    public TweenData(QDRect paramQDRect1, QDRect paramQDRect2) throws QTException {
        super(allocSize(5));
        this.type = 5;
        setBytesAt(0, 8, paramQDRect1.getBytes(), 0);
        setBytesAt(8, 8, paramQDRect2.getBytes(), 0);
    }

    /**
     * Allocates a TweenData object of type kTweenTypeMatrix from Matrix start and end values
     * @param start The interpolation start matrix
     * @param end The interpolation end matrix
     */
    public TweenData(Matrix paramMatrix1, Matrix paramMatrix2) throws QTException {
        super(allocSize(7));
        this.type = 7;
        setBytesAt(0, 36, paramMatrix1.getBytes(), 0);
        setBytesAt(36, 36, paramMatrix2.getBytes(), 0);
    }

    /**
     * Allocates a TweenData object of type kTweenTypeRGBColor from QDColor start and end values
     * @param start The interpolation start color
     * @param end The interpolation end color
     */
    public TweenData(QDColor paramQDColor1, QDColor paramQDColor2) throws QTException {
        super(allocSize(8));
        this.type = 8;
        setBytesAt(0, 6, paramQDColor1.getBytes(), 0);
        setBytesAt(6, 6, paramQDColor2.getBytes(), 0);
    }

    /**
     * Allocates a TweenData object of type kTweenTypeGraphicsModeWithRGBColor from QDColor start and end values and a graphics mode
     * Only the QDColor fields are interpolated.
     * @param start The interpolation start matrix
     * @param end The interpolation end matrix
     * @param graphicsMode The graphic mode
     */
    public TweenData(QDColor paramQDColor1, QDColor paramQDColor2, int paramInt) throws QTException {
        super(allocSize(9));
        this.type = 9;
        setIntAt(0, paramInt);
        setBytesAt(4, 6, paramQDColor1.getBytes(), 0);
        setIntAt(10, paramInt);
        setBytesAt(14, 6, paramQDColor2.getBytes(), 0);
    }

    /**
     * Allocates a TweenData object of type kTweenTypeFixedPoint from float start and end x and y values
     * @param startX The interpolation start x value
     * @param startY The interpolation start y value
     * @param endX The interpolation end x value
     * @param endY The interpolation end y value
     */
    public TweenData(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) throws QTException {
        super(allocSize(12));
        this.type = 12;
        setFloatAt(0, paramFloat1);
        setFloatAt(4, paramFloat2);
        setFloatAt(8, paramFloat3);
        setFloatAt(12, paramFloat4);
    }

    /**
     * The kTweenType type
     */
    public int getType() {
        return this.type;
    }

    private static int allocSize(int paramInt) throws QTException {
        int i = switch (paramInt) {
            case 1 -> 2;
            case 2, 3, 10 -> 4;
            case 4 -> 4;
            case 5 -> 8;
            case 7 -> 36;
            case 8 -> 6;
            case 9 -> 10;
            case 11 -> 8;
            case 12 -> 8;
            default -> 0;
        };
        if (i == 0) {
            throw new QTException("unsupported tween type");
        }
        return i + i;
    }
}

