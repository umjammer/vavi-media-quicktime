/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import org.rococoa.ObjCBlock;
import org.rococoa.ObjCBlocks.BlockLiteral;
import org.rococoa.ObjCClass;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSArray;
import org.rococoa.cocoa.foundation.NSError;
import org.rococoa.cocoa.foundation.NSObject;
import org.rococoa.cocoa.foundation.NSURL;


/**
 * AVAssetExportSession.
 *
 * @see "https://developer.apple.com/documentation/avfoundation/avassetexportsession"
 */
public abstract class AVAssetExportSession extends NSObject {

    static {
        Frameworks.load();
    }

    public static final String AVAssetExportPresetPassthrough = "AVAssetExportPresetPassthrough";
    public static final String AVAssetExportPresetHighestQuality = "AVAssetExportPresetHighestQuality";
    public static final String AVAssetExportPresetMediumQuality = "AVAssetExportPresetMediumQuality";
    public static final String AVAssetExportPresetLowQuality = "AVAssetExportPresetLowQuality";
    public static final String AVAssetExportPresetAppleM4A = "AVAssetExportPresetAppleM4A";

    public static final String AVFileTypeQuickTimeMovie = "com.apple.quicktime-movie";
    public static final String AVFileTypeMPEG4 = "public.mpeg-4";
    public static final String AVFileTypeAppleM4V = "com.apple.m4v-video";
    public static final String AVFileTypeAppleM4A = "com.apple.m4a-audio";
    public static final String AVFileType3GPP = "public.3gpp";
    public static final String AVFileType3GPP2 = "public.3gpp2";
    public static final String AVFileTypeAIFF = "public.aiff-audio";
    public static final String AVFileTypeWAVE = "com.microsoft.waveform-audio";

    // AVAssetExportSessionStatus
    public static final int AVAssetExportSessionStatusUnknown = 0;
    public static final int AVAssetExportSessionStatusWaiting = 1;
    public static final int AVAssetExportSessionStatusExporting = 2;
    public static final int AVAssetExportSessionStatusCompleted = 3;
    public static final int AVAssetExportSessionStatusFailed = 4;
    public static final int AVAssetExportSessionStatusCancelled = 5;

    private static final _Class CLASS = Rococoa.createClass("AVAssetExportSession", _Class.class);

    private interface _Class extends ObjCClass {
        AVAssetExportSession exportSessionWithAsset_presetName(AVAsset asset, String presetName);
    }

    /** @return a session, null if the preset is not compatible with the asset */
    public static AVAssetExportSession of(AVAsset asset, String preset) {
        return CLASS.exportSessionWithAsset_presetName(asset, preset);
    }

    public abstract void setOutputURL(NSURL url);

    /** the output file types of the preset and the asset */
    public abstract NSArray supportedFileTypes();

    public abstract void setOutputFileType(String fileType);

    public abstract void setShouldOptimizeForNetworkUse(boolean optimize);

    /** the block is a {@link CompletionHandler} */
    public abstract void exportAsynchronouslyWithCompletionHandler(BlockLiteral handler);

    public abstract void cancelExport();

    public abstract long status();

    public abstract float progress();

    public abstract NSError error();

    /** export completion */
    public interface CompletionHandler extends ObjCBlock {
        void complete(BlockLiteral block);
    }
}
