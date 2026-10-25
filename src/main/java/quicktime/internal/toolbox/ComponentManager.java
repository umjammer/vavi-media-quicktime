/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * ComponentManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class ComponentManager {

    private ComponentManager() {
    }

    /** used by QTObjectManagement */
    public static short CloseComponent(int paramInt) {
        throw new UnsupportedToolboxException("CloseComponent");
    }

    /** used by ComponentDescription */
    public static int CountComponents(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("CountComponents");
    }

    /** used by ComponentIdentifier */
    public static int FindNextComponent(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("FindNextComponent");
    }

    /** used by ComponentIdentifier, SGDispatcher */
    public static short GetComponentInfo(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("GetComponentInfo");
    }

    /** used by DataHandler, DataRef */
    public static int GetDataHandler(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetDataHandler");
    }

    /** used by MovieExporter */
    public static int MovieExportDoUserDialog(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MovieExportDoUserDialog");
    }

    /** used by MovieExporter */
    public static int MovieExportGetAuxiliaryData(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieExportGetAuxiliaryData");
    }

    /** used by MovieExporter */
    public static int MovieExportGetCreatorType(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieExportGetCreatorType");
    }

    /** used by MovieExporter */
    public static int MovieExportGetFileNameExtension(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieExportGetFileNameExtension");
    }

    /** used by AtomContainer */
    public static int MovieExportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieExportGetSettingsAsAtomContainer");
    }

    /** used by MovieExporter */
    public static int MovieExportGetShortFileTypeString(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MovieExportGetShortFileTypeString");
    }

    /** used by MovieExporter */
    public static int MovieExportGetSourceMediaType(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieExportGetSourceMediaType");
    }

    /** used by MovieExporter */
    public static int MovieExportSetProgressProc(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieExportSetProgressProc");
    }

    /** used by MovieExporter */
    public static int MovieExportSetSampleDescription(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieExportSetSampleDescription");
    }

    /** used by MovieExporter */
    public static int MovieExportSetSettingsFromAtomContainer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MovieExportSetSettingsFromAtomContainer");
    }

    /** used by MovieExporter */
    public static int MovieExportToDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
        throw new UnsupportedToolboxException("MovieExportToDataRef");
    }

    /** used by MovieExporter */
    public static int MovieExportToFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("MovieExportToFile");
    }

    /** used by MovieExporter */
    public static int MovieExportToHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("MovieExportToHandle");
    }

    /** used by MovieExporter */
    public static int MovieExportValidate(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MovieExportValidate");
    }

    /** used by Track */
    public static int MovieImportDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt1, int paramInt6, int[] paramArrayOfInt2, int paramInt7, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("MovieImportDataRef");
    }

    /** used by MovieImporter */
    public static int MovieImportDoUserDialog(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("MovieImportDoUserDialog");
    }

    /** used by Track */
    public static int MovieImportFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int paramInt4, int[] paramArrayOfInt2, int paramInt5, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("MovieImportFile");
    }

    /** used by MovieImporter */
    public static int MovieImportGetAuxiliaryDataType(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieImportGetAuxiliaryDataType");
    }

    /** used by MovieImporter */
    public static int MovieImportGetFileType(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieImportGetFileType");
    }

    /** used by AtomContainer */
    public static int MovieImportGetMIMETypeList(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieImportGetMIMETypeList");
    }

    /** used by MovieImporter, SampleDescription */
    public static int MovieImportGetSampleDescription(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("MovieImportGetSampleDescription");
    }

    /** used by AtomContainer */
    public static int MovieImportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieImportGetSettingsAsAtomContainer");
    }

    /** used by Track */
    public static int MovieImportHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt1, int paramInt5, int[] paramArrayOfInt2, int paramInt6, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("MovieImportHandle");
    }

    /** used by MovieImporter */
    public static int MovieImportSetAuxiliaryData(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieImportSetAuxiliaryData");
    }

    /** used by MovieImporter */
    public static int MovieImportSetChunkSize(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MovieImportSetChunkSize");
    }

    /** used by MovieImporter */
    public static int MovieImportSetDimensions(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieImportSetDimensions");
    }

    /** used by MovieImporter */
    public static int MovieImportSetDuration(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MovieImportSetDuration");
    }

    /** used by MovieImporter */
    public static int MovieImportSetFromScrap(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("MovieImportSetFromScrap");
    }

    /** used by MovieImporter */
    public static int MovieImportSetMediaFile(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MovieImportSetMediaFile");
    }

    /** used by MovieImporter */
    public static int MovieImportSetNewMovieFlags(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MovieImportSetNewMovieFlags");
    }

    /** used by MovieImporter */
    public static int MovieImportSetOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieImportSetOffsetAndLimit");
    }

    /** used by MovieImporter */
    public static int MovieImportSetProgressProc(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieImportSetProgressProc");
    }

    /** used by MovieImporter */
    public static int MovieImportSetSampleDescription(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieImportSetSampleDescription");
    }

    /** used by MovieImporter */
    public static int MovieImportSetSampleDuration(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieImportSetSampleDuration");
    }

    /** used by MovieImporter */
    public static int MovieImportSetSettingsFromAtomContainer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MovieImportSetSettingsFromAtomContainer");
    }

    /** used by MovieImporter */
    public static int MovieImportValidate(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("MovieImportValidate");
    }

    /** used by DataHandler */
    public static short OpenAComponent(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("OpenAComponent");
    }

    /** used by TimeCoder */
    public static int TCFrameNumberToTimeCode(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("TCFrameNumberToTimeCode");
    }

    /** used by QTHandle */
    public static int TCGetCurrentTimeCode(int paramInt, int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("TCGetCurrentTimeCode");
    }

    /** used by TimeCoder */
    public static int TCGetDisplayOptions(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("TCGetDisplayOptions");
    }

    /** used by QTHandle, UserData */
    public static int TCGetSourceRef(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TCGetSourceRef");
    }

    /** used by QTHandle */
    public static int TCGetTimeCodeAtTime(int paramInt1, int paramInt2, int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("TCGetTimeCodeAtTime");
    }

    /** used by TimeCoder */
    public static int TCGetTimeCodeFlags(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TCGetTimeCodeFlags");
    }

    /** used by TimeCoder */
    public static int TCSetDisplayOptions(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("TCSetDisplayOptions");
    }

    /** used by TimeCoder */
    public static int TCSetSourceRef(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("TCSetSourceRef");
    }

    /** used by TimeCoder */
    public static int TCSetTimeCodeFlags(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("TCSetTimeCodeFlags");
    }

    /** used by TimeCoder */
    public static int TCTimeCodeToFrameNumber(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TCTimeCodeToFrameNumber");
    }

    /** used by TimeCoder */
    public static int TCTimeCodeToString(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("TCTimeCodeToString");
    }

    /** used by TimeCoder */
    public static int TCTimeCodeToString(int paramInt, byte[] paramArrayOfByte1, int[] paramArrayOfInt, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("TCTimeCodeToString");
    }

    /** used by XMLParser */
    public static short XMLParseAddAttribute(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("XMLParseAddAttribute");
    }

    /** used by XMLParser */
    public static short XMLParseAddAttributeAndValue(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, int[] paramArrayOfInt, int paramInt4, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("XMLParseAddAttributeAndValue");
    }

    /** used by XMLParser */
    public static short XMLParseAddAttributeValueKind(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("XMLParseAddAttributeValueKind");
    }

    /** used by XMLParser */
    public static short XMLParseAddElement(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        throw new UnsupportedToolboxException("XMLParseAddElement");
    }

    /** used by XMLParser */
    public static short XMLParseAddNameSpace(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("XMLParseAddNameSpace");
    }

    /** used by XMLParser */
    public static short XMLParseDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("XMLParseDataRef");
    }

    /** used by XMLDoc */
    public static short XMLParseDisposeXMLDoc(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseDisposeXMLDoc");
    }

    /** used by XMLParser */
    public static short XMLParseFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("XMLParseFile");
    }

    /** used by XMLParser */
    public static short XMLParseGetDetailedParseError(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("XMLParseGetDetailedParseError");
    }

    /** used by XMLParser */
    public static short XMLParseSetCharDataHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetCharDataHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetCommentHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetCommentHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetEndDocumentHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetEndDocumentHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetEndElementHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetEndElementHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("XMLParseSetOffsetAndLimit");
    }

    /** used by XMLParser */
    public static short XMLParseSetPreprocessInstructionHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetPreprocessInstructionHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetStartDocumentHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetStartDocumentHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetStartElementHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetStartElementHandler");
    }

    public static short OpenADefaultComponent(int componentType, int componentSubType, int[] ci) {
        throw new UnsupportedToolboxException("OpenADefaultComponent");
    }

    public static int CountComponentInstances(int component) {
        throw new UnsupportedToolboxException("CountComponentInstances");
    }
}
