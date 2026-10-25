/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * ImageCompressionManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class ImageCompressionManager {

    private ImageCompressionManager() {
    }

    /** used by ImageDescription */
    public static short AddImageDescriptionExtension(int i, int j, int k) {
        throw new UnsupportedToolboxException("AddImageDescriptionExtension");
    }

    /** used by CDSequence */
    public static short CDSequenceBusy(int paramInt) {
        throw new UnsupportedToolboxException("CDSequenceBusy");
    }

    /** used by ImageSequenceDataSource */
    public static short CDSequenceChangedSourceData(int paramInt) {
        throw new UnsupportedToolboxException("CDSequenceChangedSourceData");
    }

    /** used by ImageSequenceDataSource */
    public static short CDSequenceDisposeDataSource(int paramInt) {
        throw new UnsupportedToolboxException("CDSequenceDisposeDataSource");
    }

    /** used by QTObjectManagement */
    public static short CDSequenceEnd(int paramInt) {
        throw new UnsupportedToolboxException("CDSequenceEnd");
    }

    /** used by CDSequence */
    public static short CDSequenceEquivalentImageDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("CDSequenceEquivalentImageDescription");
    }

    /** used by DSequence */
    public static short CDSequenceFlush(int paramInt) {
        throw new UnsupportedToolboxException("CDSequenceFlush");
    }

    /** used by ImageSequenceDataSource */
    public static short CDSequenceGetDataSource(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("CDSequenceGetDataSource");
    }

    /** used by DSequence */
    public static short CDSequenceInvalidate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("CDSequenceInvalidate");
    }

    /** used by ImageSequenceDataSource */
    public static short CDSequenceNewDataSource(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("CDSequenceNewDataSource");
    }

    /** used by ImageSequenceDataSource */
    public static short CDSequenceSetSourceData(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("CDSequenceSetSourceData");
    }

    /** used by ImageSequenceDataSource */
    public static short CDSequenceSetSourceData(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("CDSequenceSetSourceData");
    }

    /** used by ImageSequenceDataSource */
    public static short CDSequenceSetSourceData(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        throw new UnsupportedToolboxException("CDSequenceSetSourceData");
    }

    /** used by CodecInfo */
    public static short CodecManagerVersion(int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("CodecManagerVersion");
    }

    /** used by QTImage */
    public static short CompressImage(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("CompressImage");
    }

    /** used by QTImage */
    public static short CompressImage(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("CompressImage");
    }

    /** used by QTImage */
    public static short CompressImage(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("CompressImage");
    }

    /** used by Pict */
    public static short CompressPicture(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("CompressPicture");
    }

    /** used by CSequence */
    public static short CompressSequenceBegin(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort1, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, short paramShort2, int paramInt9) {
        throw new UnsupportedToolboxException("CompressSequenceBegin");
    }

    /** used by CSequence */
    public static short CompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, short paramShort, byte[] paramArrayOfByte2, int[] paramArrayOfInt, byte[] paramArrayOfByte3, int paramInt3) {
        throw new UnsupportedToolboxException("CompressSequenceFrame");
    }

    /** used by CSequence */
    public static short CompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, short paramShort, int paramInt3, int[] paramArrayOfInt, byte[] paramArrayOfByte2, int paramInt4) {
        throw new UnsupportedToolboxException("CompressSequenceFrame");
    }

    /** used by CSequence */
    public static short CompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, short paramShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2, byte[] paramArrayOfByte2, int paramInt3) {
        throw new UnsupportedToolboxException("CompressSequenceFrame");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, byte[] paramArrayOfByte1, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, int paramInt2, short paramShort, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, int paramInt2, short paramShort, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, int paramInt2, short paramShort, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, int[] paramArrayOfInt, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, int[] paramArrayOfInt, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by QTImage */
    public static short ConvertImage(int paramInt1, int[] paramArrayOfInt1, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("ConvertImage");
    }

    /** used by ImageDescription */
    public static short CountImageDescriptionExtensionType(int i, int j, int[] ai) {
        throw new UnsupportedToolboxException("CountImageDescriptionExtensionType");
    }

    /** used by VectorStream */
    public static int CurveAddAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4) {
        throw new UnsupportedToolboxException("CurveAddAtomToVectorStream");
    }

    /** used by VectorStream */
    public static int CurveAddAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("CurveAddAtomToVectorStream");
    }

    /** used by VectorStream */
    public static int CurveAddAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4) {
        throw new UnsupportedToolboxException("CurveAddAtomToVectorStream");
    }

    /** used by VectorStream */
    public static int CurveAddPathAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("CurveAddPathAtomToVectorStream");
    }

    /** used by VectorStream */
    public static int CurveAddZeroAtomToVectorStream(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("CurveAddZeroAtomToVectorStream");
    }

    /** used by GXPaths */
    public static int CurveCountPointsInPath(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("CurveCountPointsInPath");
    }

    /** used by VectorStream */
    public static int CurveCreateVectorStream(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("CurveCreateVectorStream");
    }

    /** used by AtomData */
    public static int CurveGetAtomDataFromVectorStream(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("CurveGetAtomDataFromVectorStream");
    }

    /** used by GXPaths */
    public static int CurveGetLength(int paramInt1, byte[] paramArrayOfByte, int paramInt2, long[] paramArrayOfLong) {
        throw new UnsupportedToolboxException("CurveGetLength");
    }

    /** used by GXPaths */
    public static int CurveGetNearestPathPoint(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("CurveGetNearestPathPoint");
    }

    /** used by GXPaths */
    public static int CurveGetPathPoint(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("CurveGetPathPoint");
    }

    /** used by CurvePath */
    public static int CurveInsertPointIntoPath(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, byte paramByte) {
        throw new UnsupportedToolboxException("CurveInsertPointIntoPath");
    }

    /** used by GXPaths */
    public static int CurveLengthToPoint(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("CurveLengthToPoint");
    }

    /** used by CurvePath */
    public static int CurveNewPath(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("CurveNewPath");
    }

    /** used by GXPaths */
    public static int CurvePathPointToLength(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("CurvePathPointToLength");
    }

    /** used by GXPaths */
    public static int CurveSetPathPoint(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, byte paramByte) {
        throw new UnsupportedToolboxException("CurveSetPathPoint");
    }

    /** used by DataCodecCompressor */
    public static int DataCodecCompress(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("DataCodecCompress");
    }

    /** used by DataCodecDecompressor */
    public static int DataCodecDecompress(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("DataCodecDecompress");
    }

    /** used by DataCodecCompressor */
    public static int DataCodecGetCompressBufferSize(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("DataCodecGetCompressBufferSize");
    }

    /** used by QTImage */
    public static short DecompressImage(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, short paramShort, int paramInt3) {
        throw new UnsupportedToolboxException("DecompressImage");
    }

    /** used by QTImage */
    public static short DecompressImage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort, int paramInt4) {
        throw new UnsupportedToolboxException("DecompressImage");
    }

    /** used by QTImage */
    public static short DecompressImage(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort, int paramInt3) {
        throw new UnsupportedToolboxException("DecompressImage");
    }

    /** used by DSequence */
    public static short DecompressSequenceBegin(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort1, int paramInt4, short paramShort2, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("DecompressSequenceBegin");
    }

    /** used by DSequence */
    public static short DecompressSequenceBeginS(int[] paramArrayOfInt, int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, short paramShort1, int paramInt5, short paramShort2, int paramInt6, int paramInt7) {
        throw new UnsupportedToolboxException("DecompressSequenceBeginS");
    }

    /** used by DSequence */
    public static short DecompressSequenceBeginS(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort1, int paramInt6, short paramShort2, int paramInt7, int paramInt8) {
        throw new UnsupportedToolboxException("DecompressSequenceBeginS");
    }

    /** used by DSequence */
    public static short DecompressSequenceBeginS(int[] paramArrayOfInt1, int paramInt1, int[] paramArrayOfInt2, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort1, int paramInt5, short paramShort2, int paramInt6, int paramInt7) {
        throw new UnsupportedToolboxException("DecompressSequenceBeginS");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrame(int paramInt1, byte[] paramArrayOfByte, short paramShort, short[] paramArrayOfShort, int paramInt2) {
        throw new UnsupportedToolboxException("DecompressSequenceFrame");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrame(int paramInt1, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3) {
        throw new UnsupportedToolboxException("DecompressSequenceFrame");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrame(int paramInt1, int[] paramArrayOfInt, short paramShort, short[] paramArrayOfShort, int paramInt2) {
        throw new UnsupportedToolboxException("DecompressSequenceFrame");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrameS(int paramInt1, byte[] paramArrayOfByte, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3) {
        throw new UnsupportedToolboxException("DecompressSequenceFrameS");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrameS(int paramInt1, int paramInt2, int paramInt3, short paramShort, short[] paramArrayOfShort, int paramInt4) {
        throw new UnsupportedToolboxException("DecompressSequenceFrameS");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrameS(int paramInt1, int[] paramArrayOfInt, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3) {
        throw new UnsupportedToolboxException("DecompressSequenceFrameS");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrameWhen(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("DecompressSequenceFrameWhen");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrameWhen(int paramInt1, int paramInt2, int paramInt3, short paramShort, short[] paramArrayOfShort, int paramInt4, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("DecompressSequenceFrameWhen");
    }

    /** used by DSequence */
    public static short DecompressSequenceFrameWhen(int paramInt1, int[] paramArrayOfInt, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("DecompressSequenceFrameWhen");
    }

    /** used by QTObjectManagement */
    public static short DisposeCodecNameList(int paramInt) {
        throw new UnsupportedToolboxException("DisposeCodecNameList");
    }

    /** used by QTImage */
    public static short FCompressImage(int paramInt1, byte[] paramArrayOfByte1, short paramShort1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short paramShort2, int paramInt6, int paramInt7, int paramInt8, int paramInt9, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("FCompressImage");
    }

    /** used by QTImage */
    public static short FCompressImage(int paramInt1, byte[] paramArrayOfByte, short paramShort1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short paramShort2, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10) {
        throw new UnsupportedToolboxException("FCompressImage");
    }

    /** used by QTImage */
    public static short FCompressImage(int paramInt1, byte[] paramArrayOfByte, short paramShort1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short paramShort2, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("FCompressImage");
    }

    /** used by Pict */
    public static short FCompressPicture(int paramInt1, int paramInt2, short paramShort1, int paramInt3, int paramInt4, short paramShort2, short paramShort3, int paramInt5, int paramInt6, int paramInt7) {
        throw new UnsupportedToolboxException("FCompressPicture");
    }

    /** used by QTImage */
    public static short FDecompressImage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort, int paramInt4, int paramInt5, byte[] paramArrayOfByte3, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10) {
        throw new UnsupportedToolboxException("FDecompressImage");
    }

    /** used by Decompressor */
    public static short FindCodec(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("FindCodec");
    }

    /** used by Compressor */
    public static short FindCodec(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        throw new UnsupportedToolboxException("FindCodec");
    }

    /** used by CSequence */
    public static short GetCSequenceDataRateParams(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetCSequenceDataRateParams");
    }

    /** used by CSequence */
    public static short GetCSequenceFrameNumber(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetCSequenceFrameNumber");
    }

    /** used by CSequence */
    public static short GetCSequenceKeyFrameRate(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetCSequenceKeyFrameRate");
    }

    /** used by CSequence */
    public static short GetCSequenceMaxCompressionSize(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetCSequenceMaxCompressionSize");
    }

    /** used by QDGraphics */
    public static short GetCSequencePrevBuffer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetCSequencePrevBuffer");
    }

    /** used by CodecInfo */
    public static short GetCodecInfo(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetCodecInfo");
    }

    /** used by CodecNameList */
    public static short GetCodecNameList(int[] paramArrayOfInt, short paramShort) {
        throw new UnsupportedToolboxException("GetCodecNameList");
    }

    /** used by QTImage */
    public static short GetCompressedImageSize(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetCompressedImageSize");
    }

    /** used by QTImage */
    public static short GetCompressedImageSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetCompressedImageSize");
    }

    /** used by QTImage */
    public static short GetCompressedImageSize(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetCompressedImageSize");
    }

    /** used by CompressionInfo */
    public static short GetCompressionInfo(short paramShort1, int paramInt, short paramShort2, short paramShort3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetCompressionInfo");
    }

    /** used by QTImage */
    public static short GetCompressionTime(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetCompressionTime");
    }

    /** used by QDGraphics */
    public static short GetDSequenceImageBuffer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetDSequenceImageBuffer");
    }

    /** used by QDGraphics */
    public static short GetDSequenceScreenBuffer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetDSequenceScreenBuffer");
    }

    /** used by GraphicsImporter */
    public static short GetGraphicsImporterForDataRef(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetGraphicsImporterForDataRef");
    }

    /** used by GraphicsImporter */
    public static short GetGraphicsImporterForDataRefWithFlags(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        throw new UnsupportedToolboxException("GetGraphicsImporterForDataRefWithFlags");
    }

    /** used by GraphicsImporter */
    public static short GetGraphicsImporterForFile(byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetGraphicsImporterForFile");
    }

    /** used by GraphicsImporter */
    public static short GetGraphicsImporterForFileWithFlags(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt) {
        throw new UnsupportedToolboxException("GetGraphicsImporterForFileWithFlags");
    }

    /** used by ColorTable */
    public static short GetImageDescriptionCTable(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetImageDescriptionCTable");
    }

    /** used by ImageDescription, QTHandle */
    public static short GetImageDescriptionExtension(int i, int[] ai, int j, int k) {
        throw new UnsupportedToolboxException("GetImageDescriptionExtension");
    }

    /** used by QTImage */
    public static short GetMaxCompressionSize(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMaxCompressionSize");
    }

    /** used by ImageDescription */
    public static short GetNextImageDescriptionExtensionType(int i, int[] ai) {
        throw new UnsupportedToolboxException("GetNextImageDescriptionExtensionType");
    }

    /** used by QTImage */
    public static short GetSimilarity(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetSimilarity");
    }

    /** used by QTImage */
    public static short GetSimilarity(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetSimilarity");
    }

    /** used by QTImage */
    public static short GetSimilarity(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetSimilarity");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportCanTranscode(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportCanTranscode");
    }

    /** used by AtomContainer, GraphicsExporter */
    public static int GraphicsExportCanUseCompressor(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportCanUseCompressor");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportDoExport(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportDoExport");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportDoStandaloneExport(int paramInt) {
        throw new UnsupportedToolboxException("GraphicsExportDoStandaloneExport");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportDoTranscode(int paramInt) {
        throw new UnsupportedToolboxException("GraphicsExportDoTranscode");
    }

    /** used by ImageDescription */
    public static int GraphicsExportDoUseCompressor(int i, int j, int[] ai) {
        throw new UnsupportedToolboxException("GraphicsExportDoUseCompressor");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportDrawInputImage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("GraphicsExportDrawInputImage");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetCompressionMethod(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetCompressionMethod");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetCompressionQuality(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetCompressionQuality");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetDefaultFileNameExtension(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetDefaultFileNameExtension");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetDefaultFileTypeAndCreator(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsExportGetDefaultFileTypeAndCreator");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetDepth(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetDepth");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetDontRecompress(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportGetDontRecompress");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetExifEnabled(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportGetExifEnabled");
    }

    /** used by DataRef */
    public static int GraphicsExportGetInputDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputDataReference");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetInputDataSize(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputDataSize");
    }

    /** used by QTFile */
    public static int GraphicsExportGetInputFile(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputFile");
    }

    /** used by QDGraphics */
    public static int GraphicsExportGetInputGWorld(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputGWorld");
    }

    /** used by GraphicsImporter */
    public static int GraphicsExportGetInputGraphicsImporter(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputGraphicsImporter");
    }

    /** used by QTHandle */
    public static int GraphicsExportGetInputHandle(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputHandle");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetInputImageDepth(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputImageDepth");
    }

    /** used by ImageDescription */
    public static int GraphicsExportGetInputImageDescription(int i, int[] ai) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputImageDescription");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetInputImageDimensions(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputImageDimensions");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetInputOffsetAndLimit(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputOffsetAndLimit");
    }

    /** used by Pict */
    public static int GraphicsExportGetInputPicture(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputPicture");
    }

    /** used by PixMap */
    public static int GraphicsExportGetInputPixmap(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputPixmap");
    }

    /** used by QTPointer */
    public static int GraphicsExportGetInputPtr(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsExportGetInputPtr");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetInterlaceStyle(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetInterlaceStyle");
    }

    /** used by AtomContainer, GraphicsExporter */
    public static int GraphicsExportGetMIMETypeList(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetMIMETypeList");
    }

    /** used by GraphicsExporter, UserData */
    public static int GraphicsExportGetMetaData(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetMetaData");
    }

    /** used by DataRef */
    public static int GraphicsExportGetOutputDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsExportGetOutputDataReference");
    }

    /** used by QTFile */
    public static int GraphicsExportGetOutputFile(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportGetOutputFile");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetOutputFileTypeAndCreator(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsExportGetOutputFileTypeAndCreator");
    }

    /** used by QTHandle */
    public static int GraphicsExportGetOutputHandle(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetOutputHandle");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetOutputOffsetAndMaxSize(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportGetOutputOffsetAndMaxSize");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetResolution(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsExportGetResolution");
    }

    /** used by AtomContainer */
    public static int GraphicsExportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetSettingsAsAtomContainer");
    }

    /** used by QTHandle */
    public static int GraphicsExportGetSettingsAsText(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetSettingsAsText");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetTargetDataSize(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsExportGetTargetDataSize");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportGetThumbnailEnabled(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsExportGetThumbnailEnabled");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportMayExporterReadInputData(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportMayExporterReadInputData");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportReadInputData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportReadInputData");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportReadOutputData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportReadOutputData");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportRequestSettings(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportRequestSettings");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetCompressionMethod(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetCompressionMethod");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetCompressionQuality(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetCompressionQuality");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetDepth(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetDepth");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetDontRecompress(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("GraphicsExportSetDontRecompress");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetExifEnabled(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("GraphicsExportSetExifEnabled");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputDataReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputDataReference");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputFile");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputGWorld(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputGWorld");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputGraphicsImporter(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputGraphicsImporter");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputHandle(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputHandle");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputOffsetAndLimit");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputPicture(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputPicture");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputPixmap(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputPixmap");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInputPtr(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("GraphicsExportSetInputPtr");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetInterlaceStyle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetInterlaceStyle");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetMetaData(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetMetaData");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetOutputDataReference(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportSetOutputDataReference");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetOutputFile(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportSetOutputFile");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetOutputFileTypeAndCreator(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportSetOutputFileTypeAndCreator");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetOutputHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetOutputHandle");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetOutputOffsetAndMaxSize(int paramInt1, int paramInt2, int paramInt3, byte paramByte) {
        throw new UnsupportedToolboxException("GraphicsExportSetOutputOffsetAndMaxSize");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetResolution(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportSetResolution");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetSettingsFromAtomContainer(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsExportSetSettingsFromAtomContainer");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetTargetDataSize(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportSetTargetDataSize");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportSetThumbnailEnabled(int paramInt1, byte paramByte, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsExportSetThumbnailEnabled");
    }

    /** used by GraphicsExporter */
    public static int GraphicsExportWriteOutputData(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsExportWriteOutputData");
    }

    /** used by QTFile */
    public static int GraphicsImportDoExportImageFileDialog(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2, int[] paramArrayOfInt, byte[] paramArrayOfByte3, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GraphicsImportDoExportImageFileDialog");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportDoesDrawAllPixels(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GraphicsImportDoesDrawAllPixels");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportDraw(int paramInt) {
        throw new UnsupportedToolboxException("GraphicsImportDraw");
    }

    /** used by GraphicsImporter */
    public static short GraphicsImportExportImageFile(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, short paramShort) {
        throw new UnsupportedToolboxException("GraphicsImportExportImageFile");
    }

    /** used by Pict */
    public static int GraphicsImportGetAsPicture(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetAsPicture");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetBoundsRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetBoundsRect");
    }

    /** used by Region */
    public static int GraphicsImportGetClip(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetClip");
    }

    /** used by QTFile */
    public static int GraphicsImportGetDataFile(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetDataFile");
    }

    /** used by QTHandle */
    public static int GraphicsImportGetDataHandle(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetDataHandle");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetDataOffsetAndSize(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsImportGetDataOffsetAndSize");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsImportGetDataReference");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetDataReferenceOffsetAndLimit(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsImportGetDataReferenceOffsetAndLimit");
    }

    /** used by Region */
    public static int GraphicsImportGetDefaultClip(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetDefaultClip");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetDefaultGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetDefaultGraphicsMode");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetDefaultSourceRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetDefaultSourceRect");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetDestRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetDestRect");
    }

    /** used by AtomContainer */
    public static int GraphicsImportGetExportImageTypeList(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetExportImageTypeList");
    }

    /** used by AtomContainer */
    public static int GraphicsImportGetExportSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetExportSettingsAsAtomContainer");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetFlags(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetFlags");
    }

    /** used by QDGraphics */
    public static int GraphicsImportGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsImportGetGWorld");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetGraphicsMode");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetImageCount(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetImageCount");
    }

    /** used by ImageDescription */
    public static int GraphicsImportGetImageDescription(int i, int[] ai) {
        throw new UnsupportedToolboxException("GraphicsImportGetImageDescription");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetImageIndex(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetImageIndex");
    }

    /** used by AtomContainer */
    public static int GraphicsImportGetMIMETypeList(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetMIMETypeList");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetMetaData(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsImportGetMetaData");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetNaturalBounds(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetNaturalBounds");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetQuality(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportGetQuality");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportGetSourceRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportGetSourceRect");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportReadData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsImportReadData");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportReadData(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("GraphicsImportReadData");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportReadData(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsImportReadData");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSaveAsPicture(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsImportSaveAsPicture");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSaveAsQuickTimeImageFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsImportSaveAsQuickTimeImageFile");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetBoundsRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportSetBoundsRect");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetClip(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsImportSetClip");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetDataFile(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportSetDataFile");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetDataHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsImportSetDataHandle");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetDataReference(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsImportSetDataReference");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetDataReferenceOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsImportSetDataReferenceOffsetAndLimit");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetDestRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportSetDestRect");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetExportSettingsFromAtomContainer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportSetExportSettingsFromAtomContainer");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetFlags(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsImportSetFlags");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetGWorld(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GraphicsImportSetGWorld");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportSetGraphicsMode");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetImageIndex(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsImportSetImageIndex");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetQuality(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GraphicsImportSetQuality");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportSetSourceRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GraphicsImportSetSourceRect");
    }

    /** used by GraphicsImporter */
    public static int GraphicsImportValidate(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GraphicsImportValidate");
    }

    /** used by DSequence */
    public static short HitTestDSequenceData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4) {
        throw new UnsupportedToolboxException("HitTestDSequenceData");
    }

    /** used by DSequence */
    public static short HitTestDSequenceData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5) {
        throw new UnsupportedToolboxException("HitTestDSequenceData");
    }

    /** used by DSequence */
    public static short HitTestDSequenceData(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2, int paramInt4) {
        throw new UnsupportedToolboxException("HitTestDSequenceData");
    }

    /** used by ICMPixelFormatInfo */
    public static short ICMGetPixelFormatInfo(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("ICMGetPixelFormatInfo");
    }

    /** used by ICMPixelFormatInfo */
    public static short ICMSetPixelFormatInfo(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("ICMSetPixelFormatInfo");
    }

    /** used by DSequence */
    public static short ICMShieldSequenceCursor(int paramInt) {
        throw new UnsupportedToolboxException("ICMShieldSequenceCursor");
    }

    /** used by ImageDescription */
    public static short MakeImageDescriptionForEffect(int i, int[] ai) {
        throw new UnsupportedToolboxException("MakeImageDescriptionForEffect");
    }

    /** used by ImageDescription */
    public static short MakeImageDescriptionForPixMap(int i, int[] ai) {
        throw new UnsupportedToolboxException("MakeImageDescriptionForPixMap");
    }

    /** used by Pict */
    public static short MakeThumbnailFromPicture(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("MakeThumbnailFromPicture");
    }

    /** used by Pict */
    public static short MakeThumbnailFromPixMap(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MakeThumbnailFromPixMap");
    }

    /** used by DSequence */
    public static short PtInDSequenceData(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("PtInDSequenceData");
    }

    /** used by DSequence */
    public static short PtInDSequenceData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("PtInDSequenceData");
    }

    /** used by DSequence */
    public static short PtInDSequenceData(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("PtInDSequenceData");
    }

    /** used by QDGraphics */
    public static short QTGetPixelSize(int paramInt) {
        throw new UnsupportedToolboxException("QTGetPixelSize");
    }

    /** used by ImageDescription */
    public static short RemoveImageDescriptionExtension(int i, int j, int k) {
        throw new UnsupportedToolboxException("RemoveImageDescriptionExtension");
    }

    /** used by ImageDescription */
    public static int SCCompressImage(int i, int j, byte[] abyte0, int[] ai, int[] ai1) {
        throw new UnsupportedToolboxException("SCCompressImage");
    }

    /** used by Pict */
    public static int SCCompressPicture(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SCCompressPicture");
    }

    /** used by ImageCompressionDialog */
    public static int SCCompressPictureFile(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("SCCompressPictureFile");
    }

    /** used by SCSequence */
    public static int SCCompressSequenceBegin(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SCCompressSequenceBegin");
    }

    /** used by SCSequence */
    public static int SCCompressSequenceEnd(int paramInt) {
        throw new UnsupportedToolboxException("SCCompressSequenceEnd");
    }

    /** used by QTHandleRef */
    public static int SCCompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SCCompressSequenceFrame");
    }

    /** used by ImageCompressionDialog */
    public static int SCDefaultPictFileSettings(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("SCDefaultPictFileSettings");
    }

    /** used by ImageCompressionDialog */
    public static int SCDefaultPictHandleSettings(int paramInt1, int paramInt2, short paramShort) {
        throw new UnsupportedToolboxException("SCDefaultPictHandleSettings");
    }

    /** used by ImageCompressionDialog */
    public static int SCDefaultPixMapSettings(int paramInt1, int paramInt2, short paramShort) {
        throw new UnsupportedToolboxException("SCDefaultPixMapSettings");
    }

    /** used by ImageCompressionDialog */
    public static int SCGetCompressFlags(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SCGetCompressFlags");
    }

    /** used by CompressionDialog, ImageCompressionDialog, SoundCompressionDialog */
    public static int SCGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SCGetInfo");
    }

    /** used by ColorTable, CompressionDialog, ImageCompressionDialog, QTHandle, QTHandleRef, SoundCompressionDialog */
    public static int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SCGetInfo");
    }

    /** used by CompressionDialog, ImageCompressionDialog, SoundCompressionDialog */
    public static int SCGetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SCGetInfo");
    }

    /** used by AtomContainer */
    public static int SCGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SCGetSettingsAsAtomContainer");
    }

    /** used by QDGraphics */
    public static int SCNewGWorld(int paramInt1, int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("SCNewGWorld");
    }

    /** used by CompressionDialog */
    public static int SCRequestImageSettings(int paramInt) {
        throw new UnsupportedToolboxException("SCRequestImageSettings");
    }

    /** used by ImageCompressionDialog */
    public static int SCRequestSequenceSettings(int paramInt) {
        throw new UnsupportedToolboxException("SCRequestSequenceSettings");
    }

    /** used by ImageCompressionDialog */
    public static int SCSetCompressFlags(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SCSetCompressFlags");
    }

    /** used by CompressionDialog, ImageCompressionDialog, SoundCompressionDialog */
    public static int SCSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SCSetInfo");
    }

    /** used by CompressionDialog, ImageCompressionDialog, SoundCompressionDialog */
    public static int SCSetInfo(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SCSetInfo");
    }

    /** used by CompressionDialog, ImageCompressionDialog, SoundCompressionDialog */
    public static int SCSetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SCSetInfo");
    }

    /** used by CompressionDialog, ImageCompressionDialog, SoundCompressionDialog */
    public static int SCSetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SCSetInfo");
    }

    /** used by CompressionDialog */
    public static int SCSetSettingsFromAtomContainer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SCSetSettingsFromAtomContainer");
    }

    /** used by ImageCompressionDialog */
    public static int SCSetTestImagePictFile(int paramInt, short paramShort1, byte[] paramArrayOfByte, short paramShort2) {
        throw new UnsupportedToolboxException("SCSetTestImagePictFile");
    }

    /** used by ImageCompressionDialog */
    public static int SCSetTestImagePictHandle(int paramInt1, int paramInt2, byte[] paramArrayOfByte, short paramShort) {
        throw new UnsupportedToolboxException("SCSetTestImagePictHandle");
    }

    /** used by ImageCompressionDialog */
    public static int SCSetTestImagePixMap(int paramInt1, int paramInt2, byte[] paramArrayOfByte, short paramShort) {
        throw new UnsupportedToolboxException("SCSetTestImagePixMap");
    }

    /** used by CSequence */
    public static short SetCSequenceDataRateParams(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetCSequenceDataRateParams");
    }

    /** used by CSequence */
    public static short SetCSequenceFrameNumber(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetCSequenceFrameNumber");
    }

    /** used by CSequence */
    public static short SetCSequenceKeyFrameRate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetCSequenceKeyFrameRate");
    }

    /** used by CSequence */
    public static short SetCSequencePreferredPacketSize(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetCSequencePreferredPacketSize");
    }

    /** used by CSequence */
    public static short SetCSequencePrev(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetCSequencePrev");
    }

    /** used by CSequence */
    public static short SetCSequenceQuality(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetCSequenceQuality");
    }

    /** used by DSequence */
    public static short SetDSequenceAccuracy(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetDSequenceAccuracy");
    }

    /** used by DSequence */
    public static short SetDSequenceFlags(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetDSequenceFlags");
    }

    /** used by DSequence */
    public static short SetDSequenceMask(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetDSequenceMask");
    }

    /** used by DSequence */
    public static short SetDSequenceMatte(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetDSequenceMatte");
    }

    /** used by DSequence */
    public static short SetDSequenceSrcRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetDSequenceSrcRect");
    }

    /** used by DSequence */
    public static short SetDSequenceTimeCode(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("SetDSequenceTimeCode");
    }

    /** used by DSequence */
    public static short SetDSequenceTransferMode(int paramInt, short paramShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetDSequenceTransferMode");
    }

    /** used by ImageDescription */
    public static short SetImageDescriptionCTable(int i, int j) {
        throw new UnsupportedToolboxException("SetImageDescriptionCTable");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, int paramInt4, int paramInt5, byte[] paramArrayOfByte3, int paramInt6) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte2, int paramInt7) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4, int paramInt5, byte[] paramArrayOfByte2, int paramInt6) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, int paramInt5, int paramInt6, byte[] paramArrayOfByte2, int paramInt7) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int paramInt8) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6, byte[] paramArrayOfByte, int paramInt7) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, int paramInt4, int paramInt5, byte[] paramArrayOfByte2, int paramInt6) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte, int paramInt7) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by QTImage */
    public static short TrimImage(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6) {
        throw new UnsupportedToolboxException("TrimImage");
    }

    /** used by GraphicsImporter, QTHandle */
    public static int GraphicsImportGetAliasedDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GraphicsImportGetAliasedDataReference");
    }

}
