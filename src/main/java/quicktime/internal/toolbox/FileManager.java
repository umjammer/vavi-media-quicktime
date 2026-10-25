/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * FileManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class FileManager {

    private FileManager() {
    }

    /** used by QTFile */
    public static int FSGetCatalogInfo(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, int paramInt4) {
        throw new UnsupportedToolboxException("FSGetCatalogInfo");
    }

    /** used by QTFile */
    public static short FSMakeFSRefUnicode(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("FSMakeFSRefUnicode");
    }

    /** used by QTFile */
    public static short FSMakeFSSpec(short paramShort, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("FSMakeFSSpec");
    }

    /** used by QTFile */
    public static short FSPathMakeRef(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("FSPathMakeRef");
    }

    /** used by GraphicsImporter, QTHandle */
    public static int GraphicsImportGetAliasedDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsImportGetAliasedDataReference");
    }

    /** used by AliasHandle */
    public static short NewAlias(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NewAlias");
    }

    /** used by QTFile */
    public static short PBGetCatInfoSync(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("PBGetCatInfoSync");
    }

    /** used by AliasHandle */
    public static short QTNewAlias(byte[] paramArrayOfByte, int[] paramArrayOfInt, byte paramByte) {
        throw new UnsupportedToolboxException("QTNewAlias");
    }

    /** used by QTFile */
    public static short QTNewDataReferenceFromFullPathUnicode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("QTNewDataReferenceFromFullPathUnicode");
    }

    /** used by QTFile */
    public static short ResolveAlias(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("ResolveAlias");
    }

    /** used by QTFile */
    public static short ResolveAliasFile(byte[] paramArrayOfByte1, byte paramByte, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("ResolveAliasFile");
    }

    /** used by QTFile */
    public static void StandardGetFilePreview(int paramInt, short paramShort, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("StandardGetFilePreview");
    }

    public static short FSpOpenDF(byte[] fsspec, byte permission, short[] refNum) {
        throw new UnsupportedToolboxException("FSpOpenDF");
    }

    public static short FSClose(short refNum) {
        throw new UnsupportedToolboxException("FSClose");
    }
}
