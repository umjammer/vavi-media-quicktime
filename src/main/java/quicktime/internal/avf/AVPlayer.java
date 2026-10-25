/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import org.rococoa.ObjCClass;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSObject;
import org.rococoa.cocoa.foundation.NSURL;


/**
 * AVPlayer.
 *
 * @see "https://developer.apple.com/documentation/avfoundation/avplayer"
 */
public abstract class AVPlayer extends NSObject {

    static {
        Frameworks.load();
    }

    private static final _Class CLASS = Rococoa.createClass("AVPlayer", _Class.class);

    private interface _Class extends ObjCClass {
        AVPlayer playerWithURL(NSURL url);
    }

    public static AVPlayer of(String path) {
        return CLASS.playerWithURL(NSURL.fileURLWithPath(path));
    }

    public abstract void play();

    public abstract void pause();

    public abstract float rate();

    public abstract void setRate(float rate);

    public abstract void setVolume(float volume);

    public abstract void seekToTime(CMTime.ByValue time);

    public abstract void seekToTime_toleranceBefore_toleranceAfter(CMTime.ByValue time, CMTime.ByValue before, CMTime.ByValue after);

    public abstract CMTime.ByValue currentTime();
}
