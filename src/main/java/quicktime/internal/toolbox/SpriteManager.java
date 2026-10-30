/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


/**
 * SpriteManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class SpriteManager {

    private SpriteManager() {
    }

    /** used by SpriteWorld */
    public static void DisposeAllSprites(int spriteWorld) {
        throw new UnsupportedToolboxException("DisposeAllSprites");
    }

    /** used by Sprite */
    public static void DisposeSprite(int sprite) {
        throw new UnsupportedToolboxException("DisposeSprite");
    }

    /** used by QTObjectManagement */
    public static void DisposeSpriteWorld(int spriteWorld) {
        throw new UnsupportedToolboxException("DisposeSpriteWorld");
    }

    /** used by Sprite */
    public static short GetSpriteProperty(int sprite, int propertyType, byte[] propertyValue) {
        throw new UnsupportedToolboxException("GetSpriteProperty");
    }

    /** used by ImageDescription, RawEncodedImage, Sprite */
    public static short GetSpriteProperty(int sprite, int propertyType, int[] propertyValue) {
        throw new UnsupportedToolboxException("GetSpriteProperty");
    }

    /** used by Sprite */
    public static short GetSpriteProperty(int sprite, int propertyType, short[] propertyValue) {
        throw new UnsupportedToolboxException("GetSpriteProperty");
    }

    /** used by Sprite */
    public static void InvalidateSprite(int sprite) {
        throw new UnsupportedToolboxException("InvalidateSprite");
    }

    /** used by SpriteWorld */
    public static short InvalidateSpriteWorld(int spriteWorld, byte[] invalidArea) {
        throw new UnsupportedToolboxException("InvalidateSpriteWorld");
    }

    /** used by Sprite */
    public static short NewSprite(int[] newSprite, int spriteWorld, int idh, byte[] imageDataPtr, byte[] matrix, byte visible, short layer) {
        throw new UnsupportedToolboxException("NewSprite");
    }

    /** used by Sprite */
    public static short NewSprite(int[] newSprite, int spriteWorld, int idh, int imageDataPtr, byte[] matrix, byte visible, short layer) {
        throw new UnsupportedToolboxException("NewSprite");
    }

    /** used by Sprite */
    public static short NewSprite(int[] newSprite, int spriteWorld, int idh, int[] imageDataPtr, byte[] matrix, byte visible, short layer) {
        throw new UnsupportedToolboxException("NewSprite");
    }

    /** used by SpriteWorld */
    public static short NewSpriteWorld(int[] newSpriteWorld, int destination, int spriteLayer, byte[] backgroundColor, int background) {
        throw new UnsupportedToolboxException("NewSpriteWorld");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int sprite, int propertyType, byte[] propertyValue) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int sprite, int propertyType, int propertyValue) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int sprite, int propertyType, int[] propertyValue) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int sprite, int propertyType, short propertyValue) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int sprite, int propertyType, short[] propertyValue) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by SpriteWorld */
    public static short SetSpriteWorldClip(int spriteWorld, int clipRgn) {
        throw new UnsupportedToolboxException("SetSpriteWorldClip");
    }

    /** used by SpriteWorld */
    public static short SetSpriteWorldFlags(int spriteWorld, int flags, int flagsMask) {
        throw new UnsupportedToolboxException("SetSpriteWorldFlags");
    }

    /** used by SpriteWorld */
    public static short SetSpriteWorldGraphicsMode(int spriteWorld, int mode, byte[] opColor) {
        throw new UnsupportedToolboxException("SetSpriteWorldGraphicsMode");
    }

    /** used by Sprite */
    public static short SpriteHitTest(int sprite, int flags, int loc, byte[] wasHit) {
        throw new UnsupportedToolboxException("SpriteHitTest");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaCountImages(int mh, short[] numImages) {
        throw new UnsupportedToolboxException("SpriteMediaCountImages");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaCountSprites(int mh, short[] numSprites) {
        throw new UnsupportedToolboxException("SpriteMediaCountSprites");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaDisposeImage(int mh, int imageIndex) {
        throw new UnsupportedToolboxException("SpriteMediaDisposeImage");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetActionVariable(int mh, int variableID, float[] value) {
        throw new UnsupportedToolboxException("SpriteMediaGetActionVariable");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetActionVariableAsString(int mh, int variableID, byte[] theCString) {
        throw new UnsupportedToolboxException("SpriteMediaGetActionVariableAsString");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetActionVariableAsString(int mh, int variableID, int[] theCString) {
        throw new UnsupportedToolboxException("SpriteMediaGetActionVariableAsString");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetDisplayedSampleNumber(int mh, int[] sampleNum) {
        throw new UnsupportedToolboxException("SpriteMediaGetDisplayedSampleNumber");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetImageName(int mh, short imageIndex, byte[] imageName) {
        throw new UnsupportedToolboxException("SpriteMediaGetImageName");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetIndImageDescription(int mh, short imageIndex, int imageDescription) {
        throw new UnsupportedToolboxException("SpriteMediaGetIndImageDescription");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetIndImageProperty(int mh, short imageIndex, int imagePropertyType, byte[] imagePropertyValue) {
        throw new UnsupportedToolboxException("SpriteMediaGetIndImageProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetIndImageProperty(int mh, short imageIndex, int imagePropertyType, int[] imagePropertyValue) {
        throw new UnsupportedToolboxException("SpriteMediaGetIndImageProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetSpriteName(int mh, int spriteID, byte[] spriteName) {
        throw new UnsupportedToolboxException("SpriteMediaGetSpriteName");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetSpriteProperty(int mh, int spriteIndex, int propertyType, byte[] propertyValue) {
        throw new UnsupportedToolboxException("SpriteMediaGetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetSpriteProperty(int mh, int spriteIndex, int propertyType, int[] propertyValue) {
        throw new UnsupportedToolboxException("SpriteMediaGetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetSpriteProperty(int mh, int spriteIndex, int propertyType, short[] propertyValue) {
        throw new UnsupportedToolboxException("SpriteMediaGetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaHitTestAllSprites(int mh, int flags, int loc, int[] spriteHitIndex) {
        throw new UnsupportedToolboxException("SpriteMediaHitTestAllSprites");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaHitTestOneSprite(int mh, int spriteIndex, int flags, int loc, byte[] wasHit) {
        throw new UnsupportedToolboxException("SpriteMediaHitTestOneSprite");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaNewImage(int mh, int dataRef, int dataRefType, int desiredID) {
        throw new UnsupportedToolboxException("SpriteMediaNewImage");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetActionVariable(int mh, int variableID, float[] value) {
        throw new UnsupportedToolboxException("SpriteMediaSetActionVariable");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetActionVariableToString(int mh, int variableID, byte[] theCString) {
        throw new UnsupportedToolboxException("SpriteMediaSetActionVariableToString");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetSpriteProperty(int mh, int spriteIndex, int propertyType, byte[] propertyValue) {
        throw new UnsupportedToolboxException("SpriteMediaSetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetSpriteProperty(int mh, int spriteIndex, int propertyType, int propertyValue) {
        throw new UnsupportedToolboxException("SpriteMediaSetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetSpriteProperty(int mh, int spriteIndex, int propertyType, short propertyValue) {
        throw new UnsupportedToolboxException("SpriteMediaSetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSpriteIDToIndex(int mh, int spriteID, short[] spriteIndex) {
        throw new UnsupportedToolboxException("SpriteMediaSpriteIDToIndex");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSpriteIndexToID(int mh, short spriteIndex, int[] spriteID) {
        throw new UnsupportedToolboxException("SpriteMediaSpriteIndexToID");
    }

    /** used by SpriteWorld */
    public static short SpriteWorldHitTest(int spriteWorld, int flags, int loc, int[] spriteHit) {
        throw new UnsupportedToolboxException("SpriteWorldHitTest");
    }

    /** used by SpriteWorld */
    public static short SpriteWorldIdle(int spriteWorld, int flagsIn, int[] flagsOut) {
        throw new UnsupportedToolboxException("SpriteWorldIdle");
    }

}
