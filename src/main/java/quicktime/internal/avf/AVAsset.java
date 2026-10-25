/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import org.rococoa.ObjCClass;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSArray;
import org.rococoa.cocoa.foundation.NSDictionary;
import org.rococoa.cocoa.foundation.NSObject;
import org.rococoa.cocoa.foundation.NSURL;


/**
 * AVURLAsset.
 *
 * @see "https://developer.apple.com/documentation/avfoundation/avurlasset"
 */
public abstract class AVAsset extends NSObject {

    static {
        Frameworks.load();
    }

    private static final _Class CLASS = Rococoa.createClass("AVURLAsset", _Class.class);

    private interface _Class extends ObjCClass {
        AVAsset URLAssetWithURL_options(NSURL url, NSDictionary options);
    }

    /** @return an asset of the file */
    public static AVAsset fromFile(String path) {
        return CLASS.URLAssetWithURL_options(NSURL.fileURLWithPath(path), null);
    }

    public abstract CMTime.ByValue duration();

    public abstract NSArray tracks();

    public abstract boolean isPlayable();

    public abstract boolean isExportable();

    public abstract boolean isReadable();
}
