/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import com.sun.jna.Pointer;
import org.rococoa.ObjCClass;
import org.rococoa.ObjCObjectByReference;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSObject;


/**
 * AVAssetImageGenerator.
 *
 * @see "https://developer.apple.com/documentation/avfoundation/avassetimagegenerator"
 */
public abstract class AVAssetImageGenerator extends NSObject {

    static {
        Frameworks.load();
    }

    private static final _Class CLASS = Rococoa.createClass("AVAssetImageGenerator", _Class.class);

    private interface _Class extends ObjCClass {
        AVAssetImageGenerator assetImageGeneratorWithAsset(AVAsset asset);
    }

    public static AVAssetImageGenerator of(AVAsset asset) {
        return CLASS.assetImageGeneratorWithAsset(asset);
    }

    public abstract void setAppliesPreferredTrackTransform(boolean applies);

    public abstract void setRequestedTimeToleranceBefore(CMTime.ByValue tolerance);

    public abstract void setRequestedTimeToleranceAfter(CMTime.ByValue tolerance);

    /** @return a CGImageRef the caller releases, null on error */
    public abstract Pointer copyCGImageAtTime_actualTime_error(CMTime.ByValue requestedTime, Pointer actualTime, ObjCObjectByReference error);
}
