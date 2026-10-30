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

    SpriteMediaHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
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
        Matrix matrix = new Matrix();
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 1, matrix.getBytes());
        StdQTException.checkError(i);
        return matrix;
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
        short[] propertyValue = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 4, propertyValue);
        StdQTException.checkError(i);
        return propertyValue[0] != 0;
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
        short[] propertyValue = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 5, propertyValue);
        StdQTException.checkError(i);
        return propertyValue[0];
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
        GraphicsMode graphicsMode = new GraphicsMode();
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 6, graphicsMode.getBytes());
        StdQTException.checkError(i);
        return graphicsMode;
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
        short[] propertyValue = new short[1];
        int i = SpriteMediaGetSpriteProperty(_ID(), spriteID, 100, propertyValue);
        StdQTException.checkError(i);
        return propertyValue[0];
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
        int[] spriteHitIndex = new int[1];
        int i = SpriteMediaHitTestAllSprites(_ID(), flags, loc.getPoint(), spriteHitIndex);
        StdQTException.checkError(i);
        return spriteHitIndex[0];
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
        byte[] wasHit = new byte[1];
        int i = SpriteMediaHitTestOneSprite(_ID(), spriteID, flags, loc.getPoint(), wasHit);
        StdQTException.checkError(i);
        return wasHit[0] != 0;
    }

    /**
     * This method returns the number of sprites contained in the media.
     *
     * @return the number of sprites contained in the media.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaCountSprites()"
     */
    public int countSprites() throws StdQTException {
        short[] numSprites = new short[1];
        int i = SpriteMediaCountSprites(_ID(), numSprites);
        StdQTException.checkError(i);
        return numSprites[0];
    }

    /**
     * This method returns the number of images contained in the image pool of this media.
     *
     * @return the number of images.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaCountImages()"
     */
    public int countImages() throws StdQTException {
        short[] numImages = new short[1];
        int i = SpriteMediaCountImages(_ID(), numImages);
        StdQTException.checkError(i);
        return numImages[0];
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
        ImageDescription imageDescription = new ImageDescription(0);
        int i = SpriteMediaGetIndImageDescription(_ID(), (short) imageIndex, QTObject.ID(imageDescription));
        StdQTException.checkError(i);
        return imageDescription;
    }

    /**
     * This method returns the sample number displayed currently.
     *
     * @return the sample number displayed currently.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetDisplayedSampleNumber()"
     */
    public int getDisplayedSampleNumber() throws StdQTException {
        int[] sampleNum = new int[1];
        int i = SpriteMediaGetDisplayedSampleNumber(_ID(), sampleNum);
        StdQTException.checkError(i);
        return sampleNum[0];
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
        byte[] spriteName = new byte[255];
        int i = SpriteMediaGetSpriteName(_ID(), spriteID, spriteName);
        StdQTException.checkError(i);
        return QTUtils.PString2String(spriteName, 0);
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
        byte[] imageName = new byte[256];
        int i = SpriteMediaGetImageName(_ID(), (short) imageIndex, imageName);
        StdQTException.checkError(i);
        return QTUtils.PString2String(imageName, 0);
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
        int[] spriteID = new int[1];
        int i = SpriteMediaSpriteIndexToID(_ID(), (short) spriteIndex, spriteID);
        StdQTException.checkError(i);
        return spriteID[0];
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
        short[] spriteIndex = new short[1];
        int i = SpriteMediaSpriteIDToIndex(_ID(), spriteID, spriteIndex);
        StdQTException.checkError(i);
        return spriteIndex[0];
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
        float[] value = new float[1];
        int i = SpriteMediaGetActionVariable(_ID(), variableID, value);
        StdQTException.checkError(i);
        return value[0];
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
        int[] theCString = {0};
        int i = SpriteMediaGetActionVariableAsString(_ID(), variableID, theCString);
        StdQTException.checkError(i);
        try {
            MyStringHandle myStringHandle = new MyStringHandle(theCString[0], this, 1);
            return myStringHandle.toJavaString();
        } catch (QTException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class MyStringHandle extends StringHandle {

        protected MyStringHandle(int nativeRep, Object ref, int strFormat) {
            super(nativeRep, ref, strFormat);
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
        int[] imagePropertyValue = new int[1];
        int i = SpriteMediaGetIndImageProperty(_ID(), (short) imageIndex, 1001, imagePropertyValue);
        StdQTException.checkError(i);
        return imagePropertyValue[0];
    }

    /**
     * Returns a propert value for the image specified by imageIndex.
     *
     * @param imageIndex Specifies the index of the image.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteMediaGetIndImageProperty()"
     */
    public QDPoint getSpriteImageRegistrationPoint(int imageIndex) throws StdQTException {
        byte[] imagePropertyValue = new byte[8];
        int i = SpriteMediaGetIndImageProperty(_ID(), (short) imageIndex, 1000, imagePropertyValue);
        StdQTException.checkError(i);
        return QDPoint.fromArray(imagePropertyValue, 8);
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

    private static int SpriteMediaDisposeImage(int mh, int imageIndex) {
        return SpriteManager.SpriteMediaDisposeImage(mh, imageIndex);
    }

    private static int SpriteMediaNewImage(int mh, int dataRef, int dataRefType, int desiredID) {
        return SpriteManager.SpriteMediaNewImage(mh, dataRef, dataRefType, desiredID);
    }

    private static int SpriteMediaSetSpriteProperty(int mh, int spriteIndex, int propertyType, byte[] propertyValue) {
        return SpriteManager.SpriteMediaSetSpriteProperty(mh, spriteIndex, propertyType, propertyValue);
    }

    private static int SpriteMediaSetSpriteProperty(int mh, int spriteIndex, int propertyType, int propertyValue) {
        return SpriteManager.SpriteMediaSetSpriteProperty(mh, spriteIndex, propertyType, propertyValue);
    }

    private static int SpriteMediaSetSpriteProperty(int mh, int spriteIndex, int propertyType, short propertyValue) {
        return SpriteManager.SpriteMediaSetSpriteProperty(mh, spriteIndex, propertyType, propertyValue);
    }

    private static int SpriteMediaGetSpriteProperty(int mh, int spriteIndex, int propertyType, byte[] propertyValue) {
        return SpriteManager.SpriteMediaGetSpriteProperty(mh, spriteIndex, propertyType, propertyValue);
    }

    private static int SpriteMediaGetSpriteProperty(int mh, int spriteIndex, int propertyType, int[] propertyValue) {
        return SpriteManager.SpriteMediaGetSpriteProperty(mh, spriteIndex, propertyType, propertyValue);
    }

    private static int SpriteMediaGetSpriteProperty(int mh, int spriteIndex, int propertyType, short[] propertyValue) {
        return SpriteManager.SpriteMediaGetSpriteProperty(mh, spriteIndex, propertyType, propertyValue);
    }

    private static int SpriteMediaHitTestAllSprites(int mh, int flags, int loc, int[] spriteHitIndex) {
        return SpriteManager.SpriteMediaHitTestAllSprites(mh, flags, loc, spriteHitIndex);
    }

    private static int SpriteMediaHitTestOneSprite(int mh, int spriteIndex, int flags, int loc, byte[] wasHit) {
        return SpriteManager.SpriteMediaHitTestOneSprite(mh, spriteIndex, flags, loc, wasHit);
    }

    private static int SpriteMediaCountSprites(int mh, short[] numSprites) {
        return SpriteManager.SpriteMediaCountSprites(mh, numSprites);
    }

    private static int SpriteMediaCountImages(int mh, short[] numImages) {
        return SpriteManager.SpriteMediaCountImages(mh, numImages);
    }

    private static int SpriteMediaGetIndImageDescription(int mh, short imageIndex, int imageDescription) {
        return SpriteManager.SpriteMediaGetIndImageDescription(mh, imageIndex, imageDescription);
    }

    private static int SpriteMediaGetDisplayedSampleNumber(int mh, int[] sampleNum) {
        return SpriteManager.SpriteMediaGetDisplayedSampleNumber(mh, sampleNum);
    }

    private static int SpriteMediaGetSpriteName(int mh, int spriteID, byte[] spriteName) {
        return SpriteManager.SpriteMediaGetSpriteName(mh, spriteID, spriteName);
    }

    private static int SpriteMediaGetImageName(int mh, short imageIndex, byte[] imageName) {
        return SpriteManager.SpriteMediaGetImageName(mh, imageIndex, imageName);
    }

    private static int SpriteMediaSpriteIndexToID(int mh, short spriteIndex, int[] spriteID) {
        return SpriteManager.SpriteMediaSpriteIndexToID(mh, spriteIndex, spriteID);
    }

    private static int SpriteMediaSpriteIDToIndex(int mh, int spriteID, short[] spriteIndex) {
        return SpriteManager.SpriteMediaSpriteIDToIndex(mh, spriteID, spriteIndex);
    }

    private static int SpriteMediaSetActionVariable(int mh, int variableID, float[] value) {
        return SpriteManager.SpriteMediaSetActionVariable(mh, variableID, value);
    }

    private static int SpriteMediaGetActionVariable(int mh, int variableID, float[] value) {
        return SpriteManager.SpriteMediaGetActionVariable(mh, variableID, value);
    }

    private static int SpriteMediaGetIndImageProperty(int mh, short imageIndex, int imagePropertyType, byte[] imagePropertyValue) {
        return SpriteManager.SpriteMediaGetIndImageProperty(mh, imageIndex, imagePropertyType, imagePropertyValue);
    }

    private static int SpriteMediaGetIndImageProperty(int mh, short imageIndex, int imagePropertyType, int[] imagePropertyValue) {
        return SpriteManager.SpriteMediaGetIndImageProperty(mh, imageIndex, imagePropertyType, imagePropertyValue);
    }

    private static int SpriteMediaSetActionVariableToString(int mh, int variableID, byte[] theCString) {
        return SpriteManager.SpriteMediaSetActionVariableToString(mh, variableID, theCString);
    }

    private static int SpriteMediaGetActionVariableAsString(int mh, int variableID, byte[] theCString) {
        return SpriteManager.SpriteMediaGetActionVariableAsString(mh, variableID, theCString);
    }

    private static int SpriteMediaGetActionVariableAsString(int mh, int variableID, int[] theCString) {
        return SpriteManager.SpriteMediaGetActionVariableAsString(mh, variableID, theCString);
    }
}
