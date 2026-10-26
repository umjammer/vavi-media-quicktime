/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;


/**
 * The C functions of the frameworks used, AVFoundation is loaded so that its classes can be created.
 */
final class Frameworks {

    private Frameworks() {
    }

    /** AVFoundation, nothing but its classes is used */
    interface AVFoundation extends Library {
        AVFoundation instance = Native.load("AVFoundation", AVFoundation.class);
    }

    /** CoreMedia */
    interface CoreMedia extends Library {
        CoreMedia instance = Native.load("CoreMedia", CoreMedia.class);

        CMTime.ByValue CMTimeMake(long value, int timescale);

        double CMTimeGetSeconds(CMTime.ByValue time);
    }

    /** CoreGraphics */
    interface CoreGraphics extends Library {
        CoreGraphics instance = Native.load("CoreGraphics", CoreGraphics.class);

        /** kCGImageAlphaPremultipliedFirst */
        int kCGImageAlphaPremultipliedFirst = 2;
        /** kCGBitmapByteOrder32Big */
        int kCGBitmapByteOrder32Big = 4 << 12;

        Pointer CGColorSpaceCreateDeviceRGB();

        void CGColorSpaceRelease(Pointer space);

        Pointer CGBitmapContextCreate(Pointer data, NativeLong width, NativeLong height, NativeLong bitsPerComponent, NativeLong bytesPerRow, Pointer space, int bitmapInfo);

        void CGContextDrawImage(Pointer context, CGRect rect, Pointer image);

        void CGContextRelease(Pointer context);

        NativeLong CGImageGetWidth(Pointer image);

        NativeLong CGImageGetHeight(Pointer image);

        void CGImageRelease(Pointer image);
    }

    static void load() {
        AVFoundation a = AVFoundation.instance;
        CoreMedia m = CoreMedia.instance;
        CoreGraphics g = CoreGraphics.instance;
    }
}
