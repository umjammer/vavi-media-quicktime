package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.SpriteManager;
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
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SpriteMediaHandler.class));
            }
        }.establish();
    }

    SpriteMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * This method allows you to specify the Matrix property of a sprite.
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @param matrix Specifies the new matrix.
     * @throws quicktime.std.StdQTException
     */
    public void setMatrix(int paramInt, Matrix paramMatrix) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 1, paramMatrix.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the visibility property of a sprite.
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @param visible Specifies the new visibility property.
     * @throws quicktime.std.StdQTException
     */
    public void setVisible(int paramInt, boolean paramBoolean) throws StdQTException {
        short s = (byte) (paramBoolean ? 1 : 0);
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 4, s);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the layer property of a sprite.
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @param layer Specifies the new layer property.
     * @throws quicktime.std.StdQTException
     */
    public void setLayer(int paramInt1, int paramInt2) throws StdQTException {
        short s = (short) paramInt2;
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt1, 5, s);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the graphics mode property of a sprite.
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @param gMode Specifies the new graphics mode property.
     * @throws quicktime.std.StdQTException
     */
    public void setGraphicsMode(int paramInt, GraphicsMode paramGraphicsMode) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt, 6, paramGraphicsMode.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the image index property of a sprite.
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @param index Specifies the new image index property.
     * @throws quicktime.std.StdQTException
     */
    public void setImageIndex(int paramInt1, int paramInt2) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), paramInt1, 100, (short) paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to get the matrix property of a sprite.
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @return the matrix property of the specified sprite.
     * @throws quicktime.std.StdQTException
     */
    public Matrix getMatrix(int paramInt) throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 1, localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * This method allows you to get the visibility property of a sprite.
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @return the visibility property of the specified sprite.
     * @throws quicktime.std.StdQTException
     */
    public boolean getVisible(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 4, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0] != 0;
    }

    /**
     * This method allows you to get the layer property of a sprite.
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @return the layer property of the specified sprite.
     * @throws quicktime.std.StdQTException
     */
    public int getLayer(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 5, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method allows you to get the graphics mode property of a sprite.
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @return the graphics mode property of the specified sprite.
     * @throws quicktime.std.StdQTException
     */
    public GraphicsMode getGraphicsMode(int paramInt) throws StdQTException {
        GraphicsMode localGraphicsMode = new GraphicsMode();
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 6, localGraphicsMode.getBytes());
        StdQTException.checkError(i);
        return localGraphicsMode;
    }

    /**
     * This method allows you to get the image index property of a sprite.
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     * @param spriteID Specifies the sprite ID.
     * @return the image index property of the specified sprite.
     * @throws quicktime.std.StdQTException
     */
    public int getImageIndex(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), paramInt, 100, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method allows you to receive the index of the sprite which contains a specified point.
     * @see "QuickTime::SpriteMediaHitTestAllSprites()"
     * @param flags Controls the operation of this method.
     * @param loc Specifies the point for hit test.
     * @return the ID of the sprite which contains the point.
     * @throws quicktime.std.StdQTException
     */
    public int hitTestAllSprites(int paramInt, QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaHitTestAllSprites(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Perform hit testing on the sprite specified by loc.
     * @see "QuickTime::SpriteMediaHitTestOneSprite()"
     * @param spriteID Specifies the sprite ID.
     * @param flags specifies how to control the hit testing operation
     * @param loc Specifies the point for hit test.
     * @return true or false if the loc matches that of the sprite.
     * @throws quicktime.std.StdQTException
     */
    public boolean hitTestOneSprite(int paramInt1, int paramInt2, QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = SpriteMediaHitTestOneSprite(_ID(), paramInt1, paramInt2, paramQDPoint.getPoint(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * This method returns the number of sprites contained in the media.
     * @see "QuickTime::SpriteMediaCountSprites()"
     * @return the number of sprites contained in the media.
     * @throws quicktime.std.StdQTException
     */
    public int countSprites() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaCountSprites(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method returns the number of images contained in the image pool of this media.
     * @see "QuickTime::SpriteMediaCountImages()"
     * @return the number of images.
     * @throws quicktime.std.StdQTException
     */
    public int countImages() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaCountImages(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method returns the image description for the image specified by the index.
     * @see "QuickTime::SpriteMediaGetIndImageDescription()"
     * @param imageIndex Specifies the image index.
     * @return the image description for the image specified by the index.
     * @throws quicktime.QTException
     */
    public ImageDescription getIndImageDescription(int paramInt) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        int i = SpriteMediaGetIndImageDescription(_ID(), (short) paramInt, QTObject.ID(localImageDescription));
        StdQTException.checkError(i);
        return localImageDescription;
    }

    /**
     * This method returns the sample number displayed currently.
     * @see "QuickTime::SpriteMediaGetDisplayedSampleNumber()"
     * @return the sample number displayed currently.
     * @throws quicktime.std.StdQTException
     */
    public int getDisplayedSampleNumber() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaGetDisplayedSampleNumber(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns the name of the sprite with the specified ID.
     * @see "QuickTime::SpriteMediaGetSpriteName()"
     * @param spriteID Specifies the id of the sprite in question.
     * @return the sprite name for a given sprite ID.
     * @throws quicktime.std.StdQTException
     */
    public String getSpriteName(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[255];
        int i = SpriteMediaGetSpriteName(_ID(), paramInt, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Returns the name of the sprite with the specified ID.
     * @see "QuickTime::SpriteMediaGetImageName()"
     * @param imageIndex Specifies the index of the image whose name we wish to receive.
     * @return the name of the image with the specified index from the current key frame sample.
     * @throws quicktime.std.StdQTException
     */
    public String getImageName(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = SpriteMediaGetImageName(_ID(), (short) paramInt, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Return the ID of the sprite specified by spriteIndex.
     * @see "QuickTime::SpriteMediaSpriteIndexToID()"
     * @param spriteIndex Specifies the index of the sprite for the operation.
     * @return sprite ID of the sprite Index.
     * @throws quicktime.std.StdQTException
     */
    public int spriteIndexToID(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaSpriteIndexToID(_ID(), (short) paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Return the index of the sprite specified by spriteID.
     * @see "QuickTime::SpriteMediaSpriteIDToIndex()"
     * @param spriteID Specifies the ID of the sprite for the operation.
     * @return sprite ID of the sprite Index.
     * @throws quicktime.std.StdQTException
     */
    public short spriteIDtoIndex(int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaSpriteIDToIndex(_ID(), paramInt, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Sets the value of the sprite track variable with the ID of the variable to the supplied value.
     * @see "QuickTime::SpriteMediaSetActionVariable()"
     * @param variableID Specifies a variable ID of the sprite name.
     * @throws quicktime.std.StdQTException
     */
    public void setActionVariable(int paramInt, float paramFloat) throws StdQTException {
        float[] arrayOfFloat = new float[1];
        arrayOfFloat[0] = paramFloat;
        int i = SpriteMediaSetActionVariable(_ID(), paramInt, arrayOfFloat);
        StdQTException.checkError(i);
    }

    /**
     * Returns the value of the sprite track variable with specified ID.
     * @see "QuickTime::SpriteMediaGetActionVariable()"
     * @param variableID Specifies a variable ID of the sprite name.
     * @throws quicktime.std.StdQTException
     */
    public float getActionVariable(int paramInt) throws StdQTException {
        float[] arrayOfFloat = new float[1];
        int i = SpriteMediaGetActionVariable(_ID(), paramInt, arrayOfFloat);
        StdQTException.checkError(i);
        return arrayOfFloat[0];
    }

    /**
     * Sets the value of the sprite track variable with the ID of the variable to the supplied theString.
     * @see "QuickTime::SpriteMediaSetActionVariableToString()"
     * @param variableID Specifies a variable ID of the sprite name.
     * @param theString Specifies the string value of the variableID.
     * @throws quicktime.std.StdQTException
     */
    public void setActionVariableToString(int paramInt, String paramString) throws StdQTException {
        int i = SpriteMediaSetActionVariableToString(_ID(), paramInt, QTUtils.String2CString(paramString));
        StdQTException.checkError(i);
    }

    /**
     * Returns the value as String of the sprite track variable with specified ID.
     * @see "QuickTime::SpriteMediaGetActionVariableAsString()"
     * @param variableID Specifies a variable ID of the sprite name.
     * @return the string value of the variableID sprite track
     * @throws quicktime.std.StdQTException
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

    static class MyStringHandle extends StringHandle {

        protected MyStringHandle(int paramInt1, Object paramObject, int paramInt2) {
            super(paramInt1, paramObject, paramInt2);
        }
    }

    /**
     * Returns a propert value for the image specified by imageIndex.
     * @see "QuickTime::SpriteMediaGetIndImageProperty()"
     * @param imageIndex Specifies the index of the image.
     * @throws quicktime.std.StdQTException
     */
    public int getSpriteImageGroupID(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaGetIndImageProperty(_ID(), (short) paramInt, 1001, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns a propert value for the image specified by imageIndex.
     * @see "QuickTime::SpriteMediaGetIndImageProperty()"
     * @param imageIndex Specifies the index of the image.
     * @throws quicktime.std.StdQTException
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
     * @see "QuickTime::SpriteMediaNewImage()"
     * @param dataRef Specifies the image to be added to the sprite track.
     * @param imageID a unique ID for the image. Pass in 0 to assign next (incremental) ID, which is usually same as
     *  the next image index unless its previously assigned
     * @throws quicktime.std.StdQTException
     */
    public void newImage(DataRef paramDataRef, int paramInt) throws StdQTException {
        int i = SpriteMediaNewImage(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Free memory allocated for image and removes image from track.
     * Requires QuickTime 6
     * @see "QuickTime::SpriteMediaDisposeImage()"
     * @param imageIndex the index of image to be removed
     * @throws quicktime.std.StdQTException
     */
    public void disposeImage(int paramInt) throws StdQTException {
        int i = SpriteMediaDisposeImage(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    private static int SpriteMediaDisposeImage(int paramInt1, int paramInt2) {
        return SpriteManager.SpriteMediaDisposeImage(paramInt1, paramInt2);
    }

    private static int SpriteMediaNewImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return SpriteManager.SpriteMediaNewImage(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return SpriteManager.SpriteMediaSetSpriteProperty(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return SpriteManager.SpriteMediaSetSpriteProperty(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, short paramShort) {
        return SpriteManager.SpriteMediaSetSpriteProperty(paramInt1, paramInt2, paramInt3, paramShort);
    }

    private static int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return SpriteManager.SpriteMediaGetSpriteProperty(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return SpriteManager.SpriteMediaGetSpriteProperty(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, short[] paramArrayOfShort) {
        return SpriteManager.SpriteMediaGetSpriteProperty(paramInt1, paramInt2, paramInt3, paramArrayOfShort);
    }

    private static int SpriteMediaHitTestAllSprites(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return SpriteManager.SpriteMediaHitTestAllSprites(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static int SpriteMediaHitTestOneSprite(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        return SpriteManager.SpriteMediaHitTestOneSprite(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
    }

    private static int SpriteMediaCountSprites(int paramInt, short[] paramArrayOfShort) {
        return SpriteManager.SpriteMediaCountSprites(paramInt, paramArrayOfShort);
    }

    private static int SpriteMediaCountImages(int paramInt, short[] paramArrayOfShort) {
        return SpriteManager.SpriteMediaCountImages(paramInt, paramArrayOfShort);
    }

    private static int SpriteMediaGetIndImageDescription(int paramInt1, short paramShort, int paramInt2) {
        return SpriteManager.SpriteMediaGetIndImageDescription(paramInt1, paramShort, paramInt2);
    }

    private static int SpriteMediaGetDisplayedSampleNumber(int paramInt, int[] paramArrayOfInt) {
        return SpriteManager.SpriteMediaGetDisplayedSampleNumber(paramInt, paramArrayOfInt);
    }

    private static int SpriteMediaGetSpriteName(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return SpriteManager.SpriteMediaGetSpriteName(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SpriteMediaGetImageName(int paramInt, short paramShort, byte[] paramArrayOfByte) {
        return SpriteManager.SpriteMediaGetImageName(paramInt, paramShort, paramArrayOfByte);
    }

    private static int SpriteMediaSpriteIndexToID(int paramInt, short paramShort, int[] paramArrayOfInt) {
        return SpriteManager.SpriteMediaSpriteIndexToID(paramInt, paramShort, paramArrayOfInt);
    }

    private static int SpriteMediaSpriteIDToIndex(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return SpriteManager.SpriteMediaSpriteIDToIndex(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static int SpriteMediaSetActionVariable(int paramInt1, int paramInt2, float[] paramArrayOfFloat) {
        return SpriteManager.SpriteMediaSetActionVariable(paramInt1, paramInt2, paramArrayOfFloat);
    }

    private static int SpriteMediaGetActionVariable(int paramInt1, int paramInt2, float[] paramArrayOfFloat) {
        return SpriteManager.SpriteMediaGetActionVariable(paramInt1, paramInt2, paramArrayOfFloat);
    }

    private static int SpriteMediaGetIndImageProperty(int paramInt1, short paramShort, int paramInt2, byte[] paramArrayOfByte) {
        return SpriteManager.SpriteMediaGetIndImageProperty(paramInt1, paramShort, paramInt2, paramArrayOfByte);
    }

    private static int SpriteMediaGetIndImageProperty(int paramInt1, short paramShort, int paramInt2, int[] paramArrayOfInt) {
        return SpriteManager.SpriteMediaGetIndImageProperty(paramInt1, paramShort, paramInt2, paramArrayOfInt);
    }

    private static int SpriteMediaSetActionVariableToString(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return SpriteManager.SpriteMediaSetActionVariableToString(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SpriteMediaGetActionVariableAsString(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return SpriteManager.SpriteMediaGetActionVariableAsString(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SpriteMediaGetActionVariableAsString(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SpriteManager.SpriteMediaGetActionVariableAsString(paramInt1, paramInt2, paramArrayOfInt);
    }
}

