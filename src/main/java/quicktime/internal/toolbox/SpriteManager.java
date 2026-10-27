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
    public static void DisposeAllSprites(int paramInt) {
        throw new UnsupportedToolboxException("DisposeAllSprites");
    }

    /** used by Sprite */
    public static void DisposeSprite(int paramInt) {
        throw new UnsupportedToolboxException("DisposeSprite");
    }

    /** used by QTObjectManagement */
    public static void DisposeSpriteWorld(int paramInt) {
        throw new UnsupportedToolboxException("DisposeSpriteWorld");
    }

    /** used by Sprite */
    public static short GetSpriteProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetSpriteProperty");
    }

    /** used by ImageDescription, RawEncodedImage, Sprite */
    public static short GetSpriteProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetSpriteProperty");
    }

    /** used by Sprite */
    public static short GetSpriteProperty(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GetSpriteProperty");
    }

    /** used by Sprite */
    public static void InvalidateSprite(int paramInt) {
        throw new UnsupportedToolboxException("InvalidateSprite");
    }

    /** used by SpriteWorld */
    public static short InvalidateSpriteWorld(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("InvalidateSpriteWorld");
    }

    /** used by Sprite */
    public static short NewSprite(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte paramByte, short paramShort) {
        throw new UnsupportedToolboxException("NewSprite");
    }

    /** used by Sprite */
    public static short NewSprite(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, byte paramByte, short paramShort) {
        throw new UnsupportedToolboxException("NewSprite");
    }

    /** used by Sprite */
    public static short NewSprite(int[] paramArrayOfInt1, int paramInt1, int paramInt2, int[] paramArrayOfInt2, byte[] paramArrayOfByte, byte paramByte, short paramShort) {
        throw new UnsupportedToolboxException("NewSprite");
    }

    /** used by SpriteWorld */
    public static short NewSpriteWorld(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) {
        throw new UnsupportedToolboxException("NewSpriteWorld");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int paramInt1, int paramInt2, short paramShort) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by Sprite */
    public static short SetSpriteProperty(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SetSpriteProperty");
    }

    /** used by SpriteWorld */
    public static short SetSpriteWorldClip(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetSpriteWorldClip");
    }

    /** used by SpriteWorld */
    public static short SetSpriteWorldFlags(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetSpriteWorldFlags");
    }

    /** used by SpriteWorld */
    public static short SetSpriteWorldGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetSpriteWorldGraphicsMode");
    }

    /** used by Sprite */
    public static short SpriteHitTest(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteHitTest");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaCountImages(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SpriteMediaCountImages");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaCountSprites(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SpriteMediaCountSprites");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaDisposeImage(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SpriteMediaDisposeImage");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetActionVariable(int paramInt1, int paramInt2, float[] paramArrayOfFloat) {
        throw new UnsupportedToolboxException("SpriteMediaGetActionVariable");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetActionVariableAsString(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteMediaGetActionVariableAsString");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetActionVariableAsString(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SpriteMediaGetActionVariableAsString");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetDisplayedSampleNumber(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SpriteMediaGetDisplayedSampleNumber");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetImageName(int paramInt, short paramShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteMediaGetImageName");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetIndImageDescription(int paramInt1, short paramShort, int paramInt2) {
        throw new UnsupportedToolboxException("SpriteMediaGetIndImageDescription");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetIndImageProperty(int paramInt1, short paramShort, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteMediaGetIndImageProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetIndImageProperty(int paramInt1, short paramShort, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SpriteMediaGetIndImageProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetSpriteName(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteMediaGetSpriteName");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteMediaGetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SpriteMediaGetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaGetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SpriteMediaGetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaHitTestAllSprites(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SpriteMediaHitTestAllSprites");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaHitTestOneSprite(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteMediaHitTestOneSprite");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaNewImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("SpriteMediaNewImage");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetActionVariable(int paramInt1, int paramInt2, float[] paramArrayOfFloat) {
        throw new UnsupportedToolboxException("SpriteMediaSetActionVariable");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetActionVariableToString(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteMediaSetActionVariableToString");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SpriteMediaSetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("SpriteMediaSetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSetSpriteProperty(int paramInt1, int paramInt2, int paramInt3, short paramShort) {
        throw new UnsupportedToolboxException("SpriteMediaSetSpriteProperty");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSpriteIDToIndex(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SpriteMediaSpriteIDToIndex");
    }

    /** used by SpriteMediaHandler */
    public static int SpriteMediaSpriteIndexToID(int paramInt, short paramShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SpriteMediaSpriteIndexToID");
    }

    /** used by SpriteWorld */
    public static short SpriteWorldHitTest(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SpriteWorldHitTest");
    }

    /** used by SpriteWorld */
    public static short SpriteWorldIdle(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SpriteWorldIdle");
    }

}
