package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.SpriteManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;


public final class SpriteMediaHandler extends VisualMediaHandler implements QuickTimeLib {

    SpriteMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * This method allows you to specify the Matrix property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @param matrix   Specifies the new matrix.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     */
    public void setMatrix(int spriteID, Matrix matrix) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), spriteID, 1, matrix.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the visibility property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @param visible  Specifies the new visibility property.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     */
    public void setVisible(int spriteID, boolean visible) throws StdQTException {
        short s = (byte) (visible ? 1 : 0);
        int i = SpriteMediaSetSpriteProperty(_ID(), spriteID, 4, s);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the layer property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @param layer    Specifies the new layer property.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     */
    public void setLayer(int spriteID, int layer) throws StdQTException {
        short s = (short) layer;
        int i = SpriteMediaSetSpriteProperty(_ID(), spriteID, 5, s);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the graphics mode property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @param gMode    Specifies the new graphics mode property.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     */
    public void setGraphicsMode(int spriteID, GraphicsMode gMode) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), spriteID, 6, gMode.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the image index property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @param index    Specifies the new image index property.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSetSpriteProperty()"
     */
    public void setImageIndex(int spriteID, int index) throws StdQTException {
        int i = SpriteMediaSetSpriteProperty(_ID(), spriteID, 100, (short) index);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to get the matrix property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @return the matrix property of the specified sprite.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     */
    public Matrix getMatrix(int spriteID) throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 1, localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * This method allows you to get the visibility property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @return the visibility property of the specified sprite.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     */
    public boolean getVisible(int spriteID) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 4, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0] != 0;
    }

    /**
     * This method allows you to get the layer property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @return the layer property of the specified sprite.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     */
    public int getLayer(int spriteID) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 5, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method allows you to get the graphics mode property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @return the graphics mode property of the specified sprite.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     */
    public GraphicsMode getGraphicsMode(int spriteID) throws StdQTException {
        GraphicsMode localGraphicsMode = new GraphicsMode();
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 6, localGraphicsMode.getBytes());
        StdQTException.checkError(i);
        return localGraphicsMode;
    }

    /**
     * This method allows you to get the image index property of a sprite.
     *
     * @param spriteID Specifies the sprite ID.
     * @return the image index property of the specified sprite.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetSpriteProperty()"
     */
    public int getImageIndex(int spriteID) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 100, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method allows you to receive the index of the sprite which contains a specified point.
     *
     * @param flags Controls the operation of this method.
     * @param loc   Specifies the point for hit test.
     * @return the ID of the sprite which contains the point.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaHitTestAllSprites()"
     */
    public int hitTestAllSprites(int flags, QDPoint loc) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaHitTestAllSprites(_ID(), flags, loc.getPoint(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Perform hit testing on the sprite specified by loc.
     *
     * @param spriteID Specifies the sprite ID.
     * @param flags    specifies how to control the hit testing operation
     * @param loc      Specifies the point for hit test.
     * @return true or false if the loc matches that of the sprite.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaHitTestOneSprite()"
     */
    public boolean hitTestOneSprite(int spriteID, int flags, QDPoint loc) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = SpriteMediaHitTestOneSprite(_ID(), spriteID, flags, loc.getPoint(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * This method returns the number of sprites contained in the media.
     *
     * @return the number of sprites contained in the media.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaCountSprites()"
     */
    public int countSprites() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaCountSprites(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method returns the number of images contained in the image pool of this media.
     *
     * @return the number of images.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaCountImages()"
     */
    public int countImages() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaCountImages(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method returns the image description for the image specified by the index.
     *
     * @param imageIndex Specifies the image index.
     * @return the image description for the image specified by the index.
     * @throws quicktime.QTException
     * @see "QuickTime::SpriteMediaGetIndImageDescription()"
     */
    public ImageDescription getIndImageDescription(int imageIndex) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        int i = SpriteMediaGetIndImageDescription(_ID(), (short) imageIndex, QTObject.ID(localImageDescription));
        StdQTException.checkError(i);
        return localImageDescription;
    }

    /**
     * This method returns the sample number displayed currently.
     *
     * @return the sample number displayed currently.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetDisplayedSampleNumber()"
     */
    public int getDisplayedSampleNumber() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaGetDisplayedSampleNumber(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns the name of the sprite with the specified ID.
     *
     * @param spriteID Specifies the id of the sprite in question.
     * @return the sprite name for a given sprite ID.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetSpriteName()"
     */
    public String getSpriteName(int spriteID) throws StdQTException {
        byte[] arrayOfByte = new byte[255];
        int i = SpriteMediaGetSpriteName(_ID(), spriteID, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Returns the name of the sprite with the specified ID.
     *
     * @param imageIndex Specifies the index of the image whose name we wish to receive.
     * @return the name of the image with the specified index from the current key frame sample.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetImageName()"
     */
    public String getImageName(int imageIndex) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = SpriteMediaGetImageName(_ID(), (short) imageIndex, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Return the ID of the sprite specified by spriteIndex.
     *
     * @param spriteIndex Specifies the index of the sprite for the operation.
     * @return sprite ID of the sprite Index.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSpriteIndexToID()"
     */
    public int spriteIndexToID(int spriteIndex) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaSpriteIndexToID(_ID(), (short) spriteIndex, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Return the index of the sprite specified by spriteID.
     *
     * @param spriteID Specifies the ID of the sprite for the operation.
     * @return sprite ID of the sprite Index.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSpriteIDToIndex()"
     */
    public short spriteIDtoIndex(int spriteID) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SpriteMediaSpriteIDToIndex(_ID(), spriteID, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Sets the value of the sprite track variable with the ID of the variable to the supplied value.
     *
     * @param variableID Specifies a variable ID of the sprite name.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSetActionVariable()"
     */
    public void setActionVariable(int variableID, float value) throws StdQTException {
        float[] arrayOfFloat = new float[1];
        arrayOfFloat[0] = value;
        int i = SpriteMediaSetActionVariable(_ID(), variableID, arrayOfFloat);
        StdQTException.checkError(i);
    }

    /**
     * Returns the value of the sprite track variable with specified ID.
     *
     * @param variableID Specifies a variable ID of the sprite name.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetActionVariable()"
     */
    public float getActionVariable(int variableID) throws StdQTException {
        float[] arrayOfFloat = new float[1];
        int i = SpriteMediaGetActionVariable(_ID(), variableID, arrayOfFloat);
        StdQTException.checkError(i);
        return arrayOfFloat[0];
    }

    /**
     * Sets the value of the sprite track variable with the ID of the variable to the supplied theString.
     *
     * @param variableID Specifies a variable ID of the sprite name.
     * @param theString  Specifies the string value of the variableID.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaSetActionVariableToString()"
     */
    public void setActionVariableToString(int variableID, String theString) throws StdQTException {
        int i = SpriteMediaSetActionVariableToString(_ID(), variableID, QTUtils.String2CString(theString));
        StdQTException.checkError(i);
    }

    /**
     * Returns the value as String of the sprite track variable with specified ID.
     *
     * @param variableID Specifies a variable ID of the sprite name.
     * @return the string value of the variableID sprite track
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetActionVariableAsString()"
     */
    public String getActionVariableAsString(int variableID) throws StdQTException {
        int[] arrayOfInt = {0};
        int i = SpriteMediaGetActionVariableAsString(_ID(), variableID, arrayOfInt);
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
     *
     * @param imageIndex Specifies the index of the image.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetIndImageProperty()"
     */
    public int getSpriteImageGroupID(int imageIndex) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SpriteMediaGetIndImageProperty(_ID(), (short) imageIndex, 1001, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns a propert value for the image specified by imageIndex.
     *
     * @param imageIndex Specifies the index of the image.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetIndImageProperty()"
     */
    public QDPoint getSpriteImageRegistrationPoint(int imageIndex) throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        int i = SpriteMediaGetIndImageProperty(_ID(), (short) imageIndex, 1000, arrayOfByte);
        StdQTException.checkError(i);
        return QDPoint.fromArray(arrayOfByte, 8);
    }

    /**
     * Creates a new Sprite image. The new image can be used by the target sprite track like any
     * other sprite image.
     * If imageID is 0, then the image is referenced by the next available image index, which is equal to the number of images
     * in the track before this call is made.
     * Requires QuickTime 6
     *
     * @param dataRef Specifies the image to be added to the sprite track.
     * @param imageID a unique ID for the image. Pass in 0 to assign next (incremental) ID, which is usually same as
     *                the next image index unless its previously assigned
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaNewImage()"
     */
    public void newImage(DataRef dataRef, int imageID) throws StdQTException {
        int i = SpriteMediaNewImage(_ID(), QTObject.ID(dataRef), dataRef.getType(), imageID);
        StdQTException.checkError(i);
    }

    /**
     * Free memory allocated for image and removes image from track.
     * Requires QuickTime 6
     *
     * @param imageIndex the index of image to be removed
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaDisposeImage()"
     */
    public void disposeImage(int imageIndex) throws StdQTException {
        int i = SpriteMediaDisposeImage(_ID(), (short) imageIndex);
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
