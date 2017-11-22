
package quicktime.std.anim;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.Matrix;


public class SpriteWorld extends QTObject implements QuickTimeLib {
    private static Object linkage;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SpriteWorld.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    private static int allocate(QDGraphics paramQDGraphics1,
                                QDGraphics paramQDGraphics2,
                                QDColor paramQDColor,
                                QDGraphics paramQDGraphics3) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            int i = NewSpriteWorld(arrayOfInt,
                                   QTObject.ID(paramQDGraphics1),
                                   QTObject.ID(paramQDGraphics2),
                                   paramQDColor == null ? null : paramQDColor.getRGBColor(),
                                   QTObject.ID(paramQDGraphics3));

            StdQTException.checkError(i);
        }
        return arrayOfInt[0];
    }

    public SpriteWorld(QDGraphics paramQDGraphics1,
            QDGraphics paramQDGraphics2,
            QDColor paramQDColor,
            QDGraphics paramQDGraphics3) throws QTException {
        super(allocate(paramQDGraphics1, paramQDGraphics2, paramQDColor, paramQDGraphics3));
        setWidthHeight(paramQDGraphics2);
        setMatrix(this.mMatrix);
        this.destPort = QTObject.ID(paramQDGraphics1);
    }

    private Matrix mMatrix = new Matrix();

    private int mWidth;

    private int mHeight;

    private int destPort;

    private int[] savedPort = {
        0
    };

    private int[] savedDevice = {
        0
    };

    private void setWidthHeight(QDGraphics paramQDGraphics) {
        synchronized (QTNative.globalsLock) {
            QDRect localQDRect = paramQDGraphics.getPortRect();
            this.mWidth = localQDRect.getWidth();
            this.mHeight = localQDRect.getHeight();
        }
    }

    public final void setBounds(QDRect paramQDRect) throws StdQTException {
        Matrix localMatrix = new Matrix();
        localMatrix.setTx(paramQDRect.getX());
        localMatrix.setTy(paramQDRect.getY());
        localMatrix.setSx(paramQDRect.getWidth() / this.mWidth);
        localMatrix.setSy(paramQDRect.getHeight() / this.mHeight);
        setMatrix(localMatrix);
    }

    public final QDRect getBounds() throws StdQTException {
        this.bounds.setX((int) this.mMatrix.getTx());
        this.bounds.setY((int) this.mMatrix.getTy());
        this.bounds.setWidth((int) (this.mMatrix.getSx() * this.mWidth));
        this.bounds.setHeight((int) (this.mMatrix.getSy() * this.mHeight));
        return this.bounds;
    }

    private QDRect bounds = new QDRect();

    public final void setClip(Region paramRegion) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldClip(_ID(), QTObject.ID(paramRegion)));
        }
    }

    public final void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        paramGraphicsMode = paramGraphicsMode == null ? new GraphicsMode() : paramGraphicsMode;
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldGraphicsMode(_ID(),
                                                                 paramGraphicsMode.getGraphicsMode(),
                                                                 paramGraphicsMode.getColor().getRGBColor()));
        }
    }

    public final Matrix getMatrix() {
        return this.mMatrix;
    }

    public final void setMatrix(Matrix paramMatrix) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            this.mMatrix = (paramMatrix == null ? new Matrix() : paramMatrix);
            StdQTException.checkError(SetSpriteWorldMatrix(_ID(), this.mMatrix.getBytes()));
        }
    }

    public final int idle(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];

        synchronized (QTNative.globalsLock) {
            int i = SpriteWorldIdle(_ID(), paramInt, arrayOfInt);
            StdQTException.checkError(i);
        }
        return arrayOfInt[0];
    }

    public final void invalidate(QDRect paramQDRect) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(InvalidateSpriteWorld(_ID(), paramQDRect == null ? null : paramQDRect.getRect()));
        }
    }

    public final Sprite hitTest(int paramInt, QDPoint paramQDPoint) throws QTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SpriteWorldHitTest(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfInt));
        }

        return arrayOfInt[0] == 0 ? null : new Sprite(arrayOfInt[0], this);
    }

    public final void setFlags(int paramInt1, int paramInt2) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldFlags(_ID(), paramInt1, paramInt2));
        }
    }

    private static native short NewSpriteWorld(int[] paramArrayOfInt,
                                               int paramInt1,
                                               int paramInt2,
                                               byte[] paramArrayOfByte,
                                               int paramInt3);

    private static native short SetSpriteWorldClip(int paramInt1, int paramInt2);

    private static native short SetSpriteWorldGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SetSpriteWorldMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native short SpriteWorldIdle(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short InvalidateSpriteWorld(int paramInt, byte[] paramArrayOfByte);

    private static native short SpriteWorldHitTest(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short SetSpriteWorldFlags(int paramInt1, int paramInt2, int paramInt3);

    private static native void DisposeAllSprites(int paramInt);

    private static native short GetMoviesError();
}
