/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * MatrixManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class MatrixManager {

    private MatrixManager() {
    }

    /** used by Matrix */
    public static void ConcatMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("ConcatMatrix");
    }

    /** used by Matrix */
    public static void CopyMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("CopyMatrix");
    }

    /** used by Matrix */
    public static byte EqualMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("EqualMatrix");
    }

    /** used by DSequence */
    public static short GetDSequenceMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetDSequenceMatrix");
    }

    /** used by Matrix */
    public static short GetMatrixType(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetMatrixType");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetDefaultMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetDefaultMatrix");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetMatrix");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportSetMatrix");
    }

    /** used by Matrix */
    public static byte InverseMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("InverseMatrix");
    }

    /** used by Matrix */
    public static void MapMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("MapMatrix");
    }

    /** used by Quaternion */
    public static int Q3Quaternion_SetMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("Q3Quaternion_SetMatrix");
    }

    /** used by Presentation */
    public static short QTSPresGetMatrix(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresGetMatrix");
    }

    /** used by Presentation */
    public static short QTSPresSetMatrix(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresSetMatrix");
    }

    /** used by Matrix */
    public static short QuadToQuadMatrix(int[] paramArrayOfInt1, int[] paramArrayOfInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QuadToQuadMatrix");
    }

    /** used by Matrix */
    public static void RectMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("RectMatrix");
    }

    /** used by Matrix */
    public static void RotateMatrix(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("RotateMatrix");
    }

    /** used by VisualChannel */
    public static int SGGetChannelMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGGetChannelMatrix");
    }

    /** used by VisualChannel */
    public static int SGSetChannelMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetChannelMatrix");
    }

    /** used by Matrix */
    public static void ScaleMatrix(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("ScaleMatrix");
    }

    /** used by DSequence */
    public static short SetDSequenceMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetDSequenceMatrix");
    }

    /** used by Matrix */
    public static void SetIdentityMatrix(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetIdentityMatrix");
    }

    /** used by SpriteWorld */
    public static short SetSpriteWorldMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetSpriteWorldMatrix");
    }

    /** used by Matrix */
    public static void SkewMatrix(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("SkewMatrix");
    }

    /** used by Matrix */
    public static short TransformFixedPoints(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt) {
        throw new UnsupportedToolboxException("TransformFixedPoints");
    }

    /** used by Matrix */
    public static byte TransformFixedRect(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt) {
        throw new UnsupportedToolboxException("TransformFixedRect");
    }

    /** used by Matrix */
    public static byte TransformFixedRect(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TransformFixedRect");
    }

    /** used by Matrix */
    public static short TransformPoints(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt) {
        throw new UnsupportedToolboxException("TransformPoints");
    }

    /** used by Matrix */
    public static byte TransformRect(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt) {
        throw new UnsupportedToolboxException("TransformRect");
    }

    /** used by Matrix */
    public static byte TransformRect(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TransformRect");
    }

    /** used by Matrix */
    public static short TransformRgn(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("TransformRgn");
    }

    /** used by Matrix */
    public static void TranslateMatrix(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TranslateMatrix");
    }

}
