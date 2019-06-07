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

    /**
     * This method allows you to specify the Matrix property of a sprite.
     * QuickTime::SpriteMediaSetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @param matrix Specifies the new matrix.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMatrix(int paramInt, Matrix paramMatrix) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 1, paramMatrix.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the visibility property of a sprite.
     * QuickTime::SpriteMediaSetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @param visible Specifies the new visibility property.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setVisible(int paramInt, boolean paramBoolean) throws StdQTException {
        short s = (byte) (paramBoolean ? 1 : 0);
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 4, s);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the layer property of a sprite.
     * QuickTime::SpriteMediaSetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @param layer Specifies the new layer property.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setLayer(int paramInt1, int paramInt2) throws StdQTException {
        short s = (short) paramInt2;
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt1, 5, s);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the graphics mode property of a sprite.
     * QuickTime::SpriteMediaSetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @param gMode Specifies the new graphics mode property.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setGraphicsMode(int paramInt, GraphicsMode paramGraphicsMode) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 6, paramGraphicsMode.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the image index property of a sprite.
     * QuickTime::SpriteMediaSetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @param index Specifies the new image index property.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setImageIndex(int paramInt1, int paramInt2) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt1, 100, (short) paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to get the matrix property of a sprite.
     * QuickTime::SpriteMediaGetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @return the matrix property of the specified sprite.
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix(int paramInt) throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 1, localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * This method allows you to get the visibility property of a sprite.
     * QuickTime::SpriteMediaGetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @return the visibility property of the specified sprite.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean getVisible(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 4, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0] != 0;
    }

    /**
     * This method allows you to get the layer property of a sprite.
     * QuickTime::SpriteMediaGetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @return the layer property of the specified sprite.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getLayer(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 5, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method allows you to get the graphics mode property of a sprite.
     * QuickTime::SpriteMediaGetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @return the graphics mode property of the specified sprite.
     * @return {@link quicktime.std.StdQTException}
     */
    public GraphicsMode getGraphicsMode(int paramInt) throws StdQTException {
        GraphicsMode localGraphicsMode = new GraphicsMode();
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 6, localGraphicsMode.getBytes());
        StdQTException.checkError(i);
        return localGraphicsMode;
    }

    /**
     * This method allows you to get the image index property of a sprite.
     * QuickTime::SpriteMediaGetSpriteProperty()
     * @param spriteID Specifies the sprite ID.
     * @return the image index property of the specified sprite.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getImageIndex(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 100, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method allows you to receive the index of the sprite which contains a specified point.
     * QuickTime::SpriteMediaHitTestAllSprites()
     * @param flags Controls the operation of this method.
     * @param loc Specifies the point for hit test.
     * @return the ID of the sprite which contains the point.
     * @return {@link quicktime.std.StdQTException}
     */
    public int hitTestAllSprites(int paramInt, QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaHitTestAllSprites(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Perform hit testing on the sprite specified by loc.
     * QuickTime::SpriteMediaHitTestOneSprite()
     * @param spriteID Specifies the sprite ID.
     * @param flags specifies how to control the hit testing operation
     * @param loc Specifies the point for hit test.
     * @return true or false if the loc matches that of the sprite.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean hitTestOneSprite(int paramInt1, int paramInt2, QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = SpriteMediaHitTestOneSprite(_ID(), paramInt1, paramInt2, paramQDPoint.getPoint(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * This method returns the number of sprites contained in the media.
     * QuickTime::SpriteMediaCountSprites()
     * @return the number of sprites contained in the media.
     * @return {@link quicktime.std.StdQTException}
     */
    public int countSprites() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaCountSprites(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method returns the number of images contained in the image pool of this media.
     * QuickTime::SpriteMediaCountImages()
     * @return the number of images.
     * @return {@link quicktime.std.StdQTException}
     */
    public int countImages() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaCountImages(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method returns the image description for the image specified by the index.
     * QuickTime::SpriteMediaGetIndImageDescription()
     * @param imageIndex Specifies the image index.
     * @return the image description for the image specified by the index.
     * @return {@link quicktime.QTException}
     */
    public ImageDescription getIndImageDescription(int paramInt) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        int i = SpriteMediaGetIndImageDescription(_ID(), (short) paramInt, QTObject.ID(localImageDescription));
        StdQTException.checkError(i);
        return localImageDescription;
    }

    /**
     * This method returns the sample number displayed currently.
     * QuickTime::SpriteMediaGetDisplayedSampleNumber()
     * @return the sample number displayed currently.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getDisplayedSampleNumber() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaGetDisplayedSampleNumber(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns the name of the sprite with the specified ID.
     * QuickTime::SpriteMediaGetSpriteName()
     * @param spriteID Specifies the id of the sprite in question.
     * @return the sprite name for a given sprite ID.
     * @return {@link quicktime.std.StdQTException}
     */
    public String getSpriteName(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[255];
        int i = SpriteMediaGetSpriteName(_ID(), paramInt, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Returns the name of the sprite with the specified ID.
     * QuickTime::SpriteMediaGetImageName()
     * @param imageIndex Specifies the index of the image whose name we wish to receive.
     * @return the name of the image with the specified index from the current key frame sample.
     * @return {@link quicktime.std.StdQTException}
     */
    public String getImageName(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = SpriteMediaGetImageName(_ID(), (short) paramInt, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Return the ID of the sprite specified by spriteIndex.
     * QuickTime::SpriteMediaSpriteIndexToID()
     * @param spriteIndex Specifies the index of the sprite for the operation.
     * @return sprite ID of the sprite Index.
     * @return {@link quicktime.std.StdQTException}
     */
    public int spriteIndexToID(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaSpriteIndexToID(_ID(), (short) paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Return the index of the sprite specified by spriteID.
     * QuickTime::SpriteMediaSpriteIDToIndex()
     * @param spriteID Specifies the ID of the sprite for the operation.
     * @return sprite ID of the sprite Index.
     * @return {@link quicktime.std.StdQTException}
     */
    public short spriteIDtoIndex(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaSpriteIDToIndex(_ID(), paramInt, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Sets the value of the sprite track variable with the ID of the variable to the supplied value.
     * QuickTime::SpriteMediaSetActionVariable()
     * @param variableID Specifies a variable ID of the sprite name.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setActionVariable(int paramInt, float paramFloat) throws StdQTException {
        float[] arrayOfFloat = new float[1];
        arrayOfFloat[0] = paramFloat;
        int i = SpriteMediaSetActionVariable(_ID(), paramInt, arrayOfFloat);
        StdQTException.checkError(i);
    }

    /**
     * Returns the value of the sprite track variable with specified ID.
     * QuickTime::SpriteMediaGetActionVariable()
     * @param variableID Specifies a variable ID of the sprite name.
     * @param {@link quicktime.std.StdQTException}
     */
    public float getActionVariable(int paramInt) throws StdQTException {
        float[] arrayOfFloat = new float[1];
        int i = SpriteMediaGetActionVariable(_ID(), paramInt, arrayOfFloat);
        StdQTException.checkError(i);
        return arrayOfFloat[0];
    }

    /**
     * Sets the value of the sprite track variable with the ID of the variable to the supplied theString.
     * QuickTime::SpriteMediaSetActionVariableToString()
     * @param variableID Specifies a variable ID of the sprite name.
     * @param theString Specifies the string value of the variableID.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setActionVariableToString(int paramInt, String paramString) throws StdQTException {
        int i = SpriteMediaSetActionVariableToString(_ID(), paramInt, QTUtils.String2CString(paramString));
        StdQTException.checkError(i);
    }

    /**
     * Returns the value as String of the sprite track variable with specified ID.
     * QuickTime::SpriteMediaGetActionVariableAsString()
     * @param variableID Specifies a variable ID of the sprite name.
     * @return the string value of the variableID sprite track
     * @return {@link quicktime.std.StdQTException}
     */
    public String getActionVariableAsString(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
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

    /**
     * Returns a propert value for the image specified by imageIndex.
     * QuickTime::SpriteMediaGetIndImageProperty()
     * @param imageIndex Specifies the index of the image.
     * @param {@link quicktime.std.StdQTException}
     */
    public int getSpriteImageGroupID(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaGetIndImageProperty(_ID(), (short) paramInt, 1001, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns a propert value for the image specified by imageIndex.
     * QuickTime::SpriteMediaGetIndImageProperty()
     * @param imageIndex Specifies the index of the image.
     * @param {@link quicktime.std.StdQTException}
     */
    public QDPoint getSpriteImageRegistrationPoint(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        int i = SpriteMediaGetIndImageProperty(_ID(), (short) paramInt, 1000, arrayOfByte);
        StdQTException.checkError(i);
        return QDPoint.fromArray(arrayOfByte, 8);
    }

    /**
     * Creates a new Sprite image. The new image can be used by the target sprite track like any
     * other sprite image.
     * If imageID is 0, then the image is referenced by the next available image index, which is equal to the number of images
     * in the track before this call is made.
     * Requires QuickTime 6
     * QuickTime::SpriteMediaNewImage()
     * @param dataRef Specifies the image to be added to the sprite track.
     * @param imageID a unique ID for the image. Pass in 0 to assign next (incremental) ID, which is usually same as
     *  the next image index unless its previously assigned
     * @param {@link quicktime.std.StdQTException}
     */
    public void newImage(DataRef paramDataRef, int paramInt) throws StdQTException {
        int i = SpriteMediaNewImage(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Free memory allocated for image and removes image from track.
     * Requires QuickTime 6
     * QuickTime::SpriteMediaDisposeImage()
     * @param imageIndex the index of image to be removed
     * @param {@link quicktime.std.StdQTException}
     */
    public void disposeImage(int paramInt) throws StdQTException {
        int i = SpriteMediaDisposeImage(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    private static native int SpriteMediaDisposeImage(int paramInt1, int paramInt2);

    private static native int SpriteMediaNewImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, short paramShort);

    private static native int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, short[] paramArrayOfShort);

    private static native int SpriteMediaHitTestAllSprites(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int SpriteMediaHitTestOneSprite(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte);

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

    private static native int SpriteMediaGetIndImageProperty(int paramInt1, short paramShort, int paramInt2, byte[] paramArrayOfByte);

    private static native int SpriteMediaGetIndImageProperty(int paramInt1, short paramShort, int paramInt2, int[] paramArrayOfInt);

    private static native int SpriteMediaSetActionVariableToString(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SpriteMediaGetActionVariableAsString(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SpriteMediaGetActionVariableAsString(int paramInt1, int paramInt2, int[] paramArrayOfInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SpriteMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
