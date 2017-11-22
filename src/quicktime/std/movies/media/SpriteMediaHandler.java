
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;


public final class SpriteMediaHandler extends VisualMediaHandler implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SpriteMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    SpriteMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public void setMatrix(int paramInt, Matrix paramMatrix) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 1, paramMatrix.getBytes());

        StdQTException.checkError(i);
    }

    public void setVisible(int paramInt, boolean paramBoolean) throws StdQTException {
        short s = (byte) (paramBoolean ? 1 : 0);
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 4, s);

        StdQTException.checkError(i);
    }

    public void setLayer(int paramInt1, int paramInt2) throws StdQTException {
        short s = (short) paramInt2;
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt1, 5, s);

        StdQTException.checkError(i);
    }

    public void setGraphicsMode(int paramInt, GraphicsMode paramGraphicsMode) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 6, paramGraphicsMode.getBytes());

        StdQTException.checkError(i);
    }

    public void setImageIndex(int paramInt1, int paramInt2) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt1, 100, (short) paramInt2);

        StdQTException.checkError(i);
    }

    public Matrix getMatrix(int paramInt) throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 1, localMatrix.getBytes());

        StdQTException.checkError(i);
        return localMatrix;
    }

    public boolean getVisible(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 4, arrayOfShort);

        StdQTException.checkError(i);

        return arrayOfShort[0] != 0;
    }

    public int getLayer(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 5, arrayOfShort);

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public GraphicsMode getGraphicsMode(int paramInt) throws StdQTException {
        GraphicsMode localGraphicsMode = new GraphicsMode();
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 6, localGraphicsMode.getBytes());

        StdQTException.checkError(i);

        return localGraphicsMode;
    }

    public int getImageIndex(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 100, arrayOfShort);

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int hitTestAllSprites(int paramInt, QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaHitTestAllSprites(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfInt);

        StdQTException.checkError(i);

        return arrayOfInt[0];
    }

    public boolean hitTestOneSprite(int paramInt1, int paramInt2, QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = SpriteMediaHitTestOneSprite(_ID(), paramInt1, paramInt2, paramQDPoint.getPoint(), arrayOfByte);

        StdQTException.checkError(i);

        return arrayOfByte[0] != 0;
    }

    public int countSprites() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaCountSprites(_ID(), arrayOfShort);
        StdQTException.checkError(i);

        return arrayOfShort[0];
    }

    public int countImages() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaCountImages(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public ImageDescription getIndImageDescription(int paramInt) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        int i = SpriteMediaGetIndImageDescription(_ID(), (short) paramInt, QTObject.ID(localImageDescription));

        StdQTException.checkError(i);

        return localImageDescription;
    }

    public int getDisplayedSampleNumber() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaGetDisplayedSampleNumber(_ID(), arrayOfInt);
        StdQTException.checkError(i);

        return arrayOfInt[0];
    }

    public String getSpriteName(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[255];
        int i = SpriteMediaGetSpriteName(_ID(), paramInt, arrayOfByte);
        StdQTException.checkError(i);

        return QTUtils.PString2String(arrayOfByte, 0);
    }

    public String getImageName(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = SpriteMediaGetImageName(_ID(), (short) paramInt, arrayOfByte);
        StdQTException.checkError(i);

        return QTUtils.PString2String(arrayOfByte, 0);
    }

    public int spriteIndexToID(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaSpriteIndexToID(_ID(), (short) paramInt, arrayOfInt);
        StdQTException.checkError(i);

        return arrayOfInt[0];
    }

    public short spriteIDtoIndex(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];

        int i = SpriteMediaSpriteIDToIndex(_ID(), paramInt, arrayOfShort);
        StdQTException.checkError(i);

        return arrayOfShort[0];
    }

    public void setActionVariable(int paramInt, float paramFloat) throws StdQTException {
        float[] arrayOfFloat = new float[1];
        arrayOfFloat[0] = paramFloat;
        int i = SpriteMediaSetActionVariable(_ID(), paramInt, arrayOfFloat);
        StdQTException.checkError(i);
    }

    public float getActionVariable(int paramInt) throws StdQTException {
        float[] arrayOfFloat = new float[1];
        int i = SpriteMediaGetActionVariable(_ID(), paramInt, arrayOfFloat);
        StdQTException.checkError(i);

        return arrayOfFloat[0];
    }

    public void setActionVariableToString(int paramInt, String paramString) throws StdQTException {
        int i = SpriteMediaSetActionVariableToString(_ID(), paramInt, QTUtils.String2CString(paramString));
        StdQTException.checkError(i);
    }

    public String getActionVariableAsString(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = SpriteMediaGetActionVariableAsString(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        try {
            MyStringHandle localMyStringHandle = new MyStringHandle(arrayOfInt[0], this, 1);
            return localMyStringHandle.toJavaString();
        } catch (QTException localQTException) {
            localQTException.printStackTrace();
        }
        return null;
    }

    class MyStringHandle extends StringHandle {
        protected MyStringHandle(int paramInt1, Object paramObject, int paramInt2) {
            super(paramInt1, paramObject, paramInt2);
        }
    }

    public int getSpriteImageGroupID(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];

        int i = SpriteMediaGetIndImageProperty(_ID(), (short) paramInt, 1001, arrayOfInt);
        StdQTException.checkError(i);

        return arrayOfInt[0];
    }

    public QDPoint getSpriteImageRegistrationPoint(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[8];

        int i = SpriteMediaGetIndImageProperty(_ID(), (short) paramInt, 1000, arrayOfByte);
        StdQTException.checkError(i);

        return QDPoint.fromArray(arrayOfByte, 8);
    }

    public void newImage(DataRef paramDataRef, int paramInt) throws StdQTException {
        int i = SpriteMediaNewImage(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
        StdQTException.checkError(i);
    }

    public void disposeImage(int paramInt) throws StdQTException {
        int i = SpriteMediaDisposeImage(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    private static native int SpriteMediaDisposeImage(int paramInt1, int paramInt2);

    private static native int SpriteMediaNewImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int SpriteMediaSetSpriteProperty(int paramInt1,
                                                           int paramInt2,
                                                           int paramInt3,
                                                           byte[] paramArrayOfByte);

    private static native int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, short paramShort);

    private static native int SpriteMediaGetSpriteProperty(int paramInt1,
                                                           int paramInt2,
                                                           int paramInt3,
                                                           byte[] paramArrayOfByte);

    private static native int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int SpriteMediaGetSpriteProperty(int paramInt1,
                                                           int paramInt2,
                                                           int paramInt3,
                                                           short[] paramArrayOfShort);

    private static native int SpriteMediaHitTestAllSprites(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int SpriteMediaHitTestOneSprite(int paramInt1,
                                                          int paramInt2,
                                                          int paramInt3,
                                                          int paramInt4,
                                                          byte[] paramArrayOfByte);

    private static native int SpriteMediaCountSprites(int paramInt, short[] paramArrayOfShort);

    private static native int SpriteMediaCountImages(int paramInt, short[] paramArrayOfShort);

    private static native int SpriteMediaGetIndImageDescription(int paramInt1, short paramShort, int paramInt2);

    private static native int SpriteMediaGetDisplayedSampleNumber(int paramInt, int[] paramArrayOfInt);

    private static native int SpriteMediaGetSpriteName(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SpriteMediaGetImageName(int paramInt, short paramShort, byte[] paramArrayOfByte);

    private static native int SpriteMediaSpriteIndexToID(int paramInt, short paramShort, int[] paramArrayOfInt);

    private static native int SpriteMediaSpriteIDToIndex(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native int SpriteMediaSetActionVariable(int paramInt1, int paramInt2, float[] paramArrayOfFloat);

    private static native int SpriteMediaGetActionVariable(int paramInt1, int paramInt2, float[] paramArrayOfFloat);

    private static native int SpriteMediaGetIndImageProperty(int paramInt1,
                                                             short paramShort,
                                                             int paramInt2,
                                                             byte[] paramArrayOfByte);

    private static native int SpriteMediaGetIndImageProperty(int paramInt1,
                                                             short paramShort,
                                                             int paramInt2,
                                                             int[] paramArrayOfInt);

    private static native int SpriteMediaSetActionVariableToString(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SpriteMediaGetActionVariableAsString(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SpriteMediaGetActionVariableAsString(int paramInt1, int paramInt2, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SpriteMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
