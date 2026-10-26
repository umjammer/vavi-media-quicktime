/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.rococoa.ID;
import org.rococoa.ObjCClass;
import org.rococoa.ObjCObject;
import org.rococoa.ObjCObjectByReference;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSArray;
import org.rococoa.cocoa.foundation.NSDictionary;
import org.rococoa.cocoa.foundation.NSNumber;
import org.rococoa.cocoa.foundation.NSObject;
import org.rococoa.cocoa.foundation.NSString;
import quicktime.internal.toolbox.VideoSource;


/**
 * A camera of AVFoundation as a video source: an AVCaptureSession with an AVCaptureVideoDataOutput of BGRA frames.
 *
 * @see "https://developer.apple.com/documentation/avfoundation/avcapturesession"
 */
public final class AVFCamera extends VideoSource {

    private static final Logger logger = Logger.getLogger(AVFCamera.class.getName());

    static {
        Frameworks.load();
    }

    /** AVCaptureDevice */
    public abstract static class AVCaptureDevice extends NSObject {
        static final _Class CLASS = Rococoa.createClass("AVCaptureDevice", _Class.class);

        interface _Class extends ObjCClass {
            NSArray devicesWithMediaType(String mediaType);
        }

        public abstract String localizedName();

        public abstract String uniqueID();
    }

    /** AVCaptureDeviceInput */
    public abstract static class AVCaptureDeviceInput extends NSObject {
        static final _Class CLASS = Rococoa.createClass("AVCaptureDeviceInput", _Class.class);

        interface _Class extends ObjCClass {
            AVCaptureDeviceInput deviceInputWithDevice_error(AVCaptureDevice device, ObjCObjectByReference error);
        }
    }

    /** AVCaptureVideoDataOutput */
    public abstract static class AVCaptureVideoDataOutput extends NSObject {
        static final _Class CLASS = Rococoa.createClass("AVCaptureVideoDataOutput", _Class.class);

        interface _Class extends ObjCClass {
            AVCaptureVideoDataOutput alloc();
        }

        public abstract AVCaptureVideoDataOutput init();

        public abstract void setVideoSettings(NSDictionary settings);

        public abstract void setAlwaysDiscardsLateVideoFrames(boolean discard);

        public abstract void setSampleBufferDelegate_queue(ID delegate, ID queue);
    }

    /** AVCaptureSession */
    public abstract static class AVCaptureSession extends NSObject {
        static final _Class CLASS = Rococoa.createClass("AVCaptureSession", _Class.class);

        interface _Class extends ObjCClass {
            AVCaptureSession alloc();
        }

        public abstract AVCaptureSession init();

        public abstract boolean canAddInput(AVCaptureDeviceInput input);

        public abstract void addInput(AVCaptureDeviceInput input);

        public abstract boolean canAddOutput(AVCaptureVideoDataOutput output);

        public abstract void addOutput(AVCaptureVideoDataOutput output);

        public abstract void startRunning();

        public abstract void stopRunning();
    }

    /** CoreMedia, CoreVideo and libdispatch functions */
    interface Media extends Library {
        Media instance = Native.load("CoreMedia", Media.class);

        Pointer CMSampleBufferGetImageBuffer(Pointer sampleBuffer);
    }

    interface CoreVideo extends Library {
        CoreVideo instance = Native.load("CoreVideo", CoreVideo.class);

        int CVPixelBufferLockBaseAddress(Pointer buffer, long flags);

        int CVPixelBufferUnlockBaseAddress(Pointer buffer, long flags);

        Pointer CVPixelBufferGetBaseAddress(Pointer buffer);

        NativeLong CVPixelBufferGetBytesPerRow(Pointer buffer);

        NativeLong CVPixelBufferGetWidth(Pointer buffer);

        NativeLong CVPixelBufferGetHeight(Pointer buffer);
    }

    interface Dispatch extends Library {
        Dispatch instance = Native.load("System", Dispatch.class);

        Pointer dispatch_queue_create(String label, Pointer attr);
    }

    /** kCVPixelFormatType_32BGRA */
    static final int k32BGRA = 0x42475241;

    /** @return the video capture devices */
    public static List<VideoSource> devices() {
        List<VideoSource> l = new ArrayList<>();
        NSArray a = AVCaptureDevice.CLASS.devicesWithMediaType("vide");
        for (int i = 0; a != null && i < a.count(); i++) {
            AVCaptureDevice d = Rococoa.cast(a.objectAtIndex(i), AVCaptureDevice.class);
            l.add(new AVFCamera(d));
        }
        return l;
    }

    private final AVCaptureDevice device;
    private final String name;
    private AVCaptureSession session;
    private ObjCObject delegate;
    private volatile BufferedImage latest;
    private volatile Dimension size = new Dimension(640, 480);

    private AVFCamera(AVCaptureDevice device) {
        this.device = device;
        this.name = device.localizedName();
    }

    @Override
    public int subType() {
        return 0x61766663; // 'avfc'
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Dimension size() {
        return size;
    }

    /** the sample buffer delegate, called on the capture queue */
    public final class Delegate {
        public void captureOutput_didOutputSampleBuffer_fromConnection(ID output, ID sampleBuffer, ID connection) {
            try {
                Pointer image = Media.instance.CMSampleBufferGetImageBuffer(new Pointer(sampleBuffer.longValue()));
                if (image == null) {
                    return;
                }
                CoreVideo cv = CoreVideo.instance;
                cv.CVPixelBufferLockBaseAddress(image, 1);
                try {
                    int w = cv.CVPixelBufferGetWidth(image).intValue();
                    int h = cv.CVPixelBufferGetHeight(image).intValue();
                    int rowBytes = cv.CVPixelBufferGetBytesPerRow(image).intValue();
                    Pointer base = cv.CVPixelBufferGetBaseAddress(image);
                    int[] px = new int[w * h];
                    for (int y = 0; y < h; y++) {
                        // BGRA bytes read as little endian ints are ARGB
                        base.read((long) y * rowBytes, px, y * w, w);
                    }
                    if (java.nio.ByteOrder.nativeOrder() != java.nio.ByteOrder.LITTLE_ENDIAN) {
                        for (int i = 0; i < px.length; i++) {
                            px[i] = Integer.reverseBytes(px[i]);
                        }
                    }
                    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    img.setRGB(0, 0, w, h, px, 0, w);
                    size = new Dimension(w, h);
                    latest = img;
                } finally {
                    cv.CVPixelBufferUnlockBaseAddress(image, 1);
                }
            } catch (RuntimeException e) {
                logger.log(Level.FINE, "frame", e);
            }
        }
    }

    @Override
    public synchronized void start() throws IOException {
        if (session != null) {
            return;
        }
        ObjCObjectByReference error = new ObjCObjectByReference();
        AVCaptureDeviceInput input = AVCaptureDeviceInput.CLASS.deviceInputWithDevice_error(device, error);
        if (input == null) {
            throw new IOException("cannot open " + name);
        }
        AVCaptureSession s = AVCaptureSession.CLASS.alloc().init();
        if (!s.canAddInput(input)) {
            throw new IOException("cannot use " + name);
        }
        s.addInput(input);
        AVCaptureVideoDataOutput out = AVCaptureVideoDataOutput.CLASS.alloc().init();
        NSDictionary settings = NSDictionary.dictionaryWithObjectsAndKeys(NSNumber.of(k32BGRA), NSString.stringWithString("PixelFormatType"));
        out.setVideoSettings(settings);
        out.setAlwaysDiscardsLateVideoFrames(true);
        delegate = Rococoa.proxy(new Delegate());
        Pointer queue = Dispatch.instance.dispatch_queue_create("quicktime.capture", null);
        out.setSampleBufferDelegate_queue(delegate.id(), ID.fromLong(Pointer.nativeValue(queue)));
        if (!s.canAddOutput(out)) {
            throw new IOException("cannot capture from " + name);
        }
        s.addOutput(out);
        s.startRunning();
        session = s;
    }

    @Override
    public BufferedImage grab() {
        return latest;
    }

    @Override
    public synchronized void stop() {
        if (session != null) {
            session.stopRunning();
            session = null;
            delegate = null;
        }
    }
}
