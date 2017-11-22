
package quicktime.std.anim;

import java.awt.Dimension;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDDimension;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.RawEncodedImage;


public final class Sprite extends QTObject implements QuickTimeLib, quicktime.jdirect.PrimitivesLib {
    private static Object linkage;

    private SpriteWorld sw;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Sprite.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    Sprite(int paramInt, SpriteWorld paramSpriteWorld) throws QTException {
        super(paramInt);
        this.sw = paramSpriteWorld;
    }

    public Sprite(SpriteWorld paramSpriteWorld,
            ImageDescription paramImageDescription,
            EncodedImage paramEncodedImage,
            Matrix paramMatrix,
            boolean paramBoolean,
            int paramInt) throws QTException {
        super(allocate(paramSpriteWorld, paramImageDescription, paramEncodedImage, paramMatrix, paramBoolean, paramInt));
        this.sw = paramSpriteWorld;
    }

    private static int allocate(SpriteWorld paramSpriteWorld,
                                ImageDescription paramImageDescription,
                                EncodedImage paramEncodedImage,
                                Matrix paramMatrix,
                                boolean paramBoolean,
                                int paramInt) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        int[] arrayOfInt = new int[1];
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = NewSprite(arrayOfInt,
                              QTObject.ID(paramSpriteWorld),
                              QTObject.ID(paramImageDescription),
                              ((ByteEncodedImage) paramEncodedImage).getBytes(),
                              paramMatrix.getBytes(),
                              b,
                              (short) paramInt);

            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = NewSprite(arrayOfInt,
                              QTObject.ID(paramSpriteWorld),
                              QTObject.ID(paramImageDescription),
                              ((IntEncodedImage) paramEncodedImage).getInts(),
                              paramMatrix.getBytes(),
                              b,
                              (short) paramInt);

            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = NewSprite(arrayOfInt,
                              QTObject.ID(paramSpriteWorld),
                              QTObject.ID(paramImageDescription),
                              QTObject.ID((RawEncodedImage) paramEncodedImage),
                              paramMatrix.getBytes(),
                              b,
                              (short) paramInt);
            }

            StdQTException.checkError(i);
        }
        return arrayOfInt[0];
    }

    public SpriteWorld getSpriteWorld() {
        return this.sw;
    }

    public void invalidate() {
        synchronized (QTNative.globalsLock) {
            InvalidateSprite(_ID());
        }
    }

    public boolean hitTest(int paramInt, QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = {
            0
        };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SpriteHitTest(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfByte));
        }

        return arrayOfByte[0] != 0;
    }

    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 1, localMatrix.getBytes());

            StdQTException.checkError(i);
        }
        return localMatrix;
    }

    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 1, paramMatrix.getBytes());

            StdQTException.checkError(i);
        }
    }

    public ImageDescription getImageDescription() throws StdQTException {
        return ImageDescription.fromSprite(this);
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, arrayOfInt);
            StdQTException.checkError(i);
        }
        return new Dimension(getShortFromHandle(arrayOfInt[0], 32), getShortFromHandle(arrayOfInt[0], 34));
    }

    public QDDimension getOriginalSize() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, arrayOfInt);
            StdQTException.checkError(i);
        }
        return new QDDimension(getShortFromHandle(arrayOfInt[0], 32), getShortFromHandle(arrayOfInt[0], 34));
    }

    public void setImageDescription(ImageDescription paramImageDescription) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, arrayOfInt);
            StdQTException.checkError(i);

            if (arrayOfInt[0] == QTObject.ID(paramImageDescription))
                return;
            i = SetSpriteProperty(_ID(), 2, QTObject.ID(paramImageDescription));

            StdQTException.checkError(i);
        }
    }

    public RawEncodedImage getImageData() throws StdQTException {
        return RawEncodedImage.fromSprite(this);
    }

    public void setImageData(EncodedImage paramEncodedImage) throws StdQTException {
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((ByteEncodedImage) paramEncodedImage).getBytes());

            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((IntEncodedImage) paramEncodedImage).getInts());

            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, QTObject.ID((RawEncodedImage) paramEncodedImage));
            }

            StdQTException.checkError(i);
        }
        setImageDataSize(paramEncodedImage.getSize());
    }

    public boolean setImageDataIfDifferent(EncodedImage paramEncodedImage) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetSpriteProperty(_ID(), 3, arrayOfInt);
            StdQTException.checkError(i);
        }
        i = -50;
        synchronized (QTNative.globalsLock) {
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((ByteEncodedImage) paramEncodedImage).getBytes());

            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((IntEncodedImage) paramEncodedImage).getInts());

            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, QTObject.ID((RawEncodedImage) paramEncodedImage));
            }

            StdQTException.checkError(i);
        }
        int ai1[] = new int[] {
            0
        };
        GetSpriteProperty(_ID(), 3, ai1);
        if (ai1[0] != arrayOfInt[0]) {
            setImageDataSize(paramEncodedImage.getSize());
            return true;
        }
        return false;
    }

    public boolean getVisible() throws StdQTException {
        byte[] arrayOfByte = {
            0
        };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 4, arrayOfByte);
            StdQTException.checkError(i);
        }
        return arrayOfByte[0] != 0;
    }

    public void setVisible(boolean paramBoolean) throws StdQTException {
        int i = (byte) (paramBoolean ? 1 : 0);
        synchronized (QTNative.globalsLock) {
            int j = SetSpriteProperty(_ID(), 4, (short) i);
            StdQTException.checkError(j);
        }
    }

    public int getLayer() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 5, arrayOfShort);
            StdQTException.checkError(i);
        }
        return arrayOfShort[0];
    }

    public void setLayer(int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 5, (short) paramInt);
            StdQTException.checkError(i);
        }
    }

    public GraphicsMode getGraphicsMode() throws StdQTException {
        GraphicsMode localGraphicsMode = new GraphicsMode();
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 6, localGraphicsMode.getBytes());
            StdQTException.checkError(i);
        }
        return localGraphicsMode;
    }

    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            if (paramGraphicsMode != null) {
                int i = SetSpriteProperty(_ID(), 6, paramGraphicsMode.getBytes());
                StdQTException.checkError(i);
            }
        }
    }

    public int getImageDataSize() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 7, arrayOfInt);
            StdQTException.checkError(i);
        }
        return arrayOfInt[0];
    }

    public void setImageDataSize(int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 7, paramInt);
            StdQTException.checkError(i);
        }
    }

    public void remove() {
        synchronized (QTNative.globalsLock) {
            DisposeSprite(_ID());
        }
        this.sw = null;
        try {
            finalize();
        } catch (Throwable localThrowable) {
        }
    }

    private static native short NewSprite(int[] paramArrayOfInt,
                                          int paramInt1,
                                          int paramInt2,
                                          byte[] paramArrayOfByte1,
                                          byte[] paramArrayOfByte2,
                                          byte paramByte,
                                          short paramShort);

    private static native short NewSprite(int[] paramArrayOfInt1,
                                          int paramInt1,
                                          int paramInt2,
                                          int[] paramArrayOfInt2,
                                          byte[] paramArrayOfByte,
                                          byte paramByte,
                                          short paramShort);

    private static native short NewSprite(int[] paramArrayOfInt,
                                          int paramInt1,
                                          int paramInt2,
                                          int paramInt3,
                                          byte[] paramArrayOfByte,
                                          byte paramByte,
                                          short paramShort);

    private static native void InvalidateSprite(int paramInt);

    private static native short SpriteHitTest(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native short GetSpriteProperty(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native short GetSpriteProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short GetSpriteProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, short paramShort);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, int paramInt3);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native void DisposeSprite(int paramInt);

    private static native short getShortFromHandle(int paramInt1, int paramInt2);
}
