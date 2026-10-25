/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.rococoa.Foundation;
import org.rococoa.ObjCBlocks.BlockLiteral;
import org.rococoa.ObjCObjectByReference;
import org.rococoa.cocoa.foundation.NSError;
import org.rococoa.cocoa.foundation.NSURL;
import quicktime.internal.toolbox.MediaEngine;
import quicktime.internal.toolbox.MovieAccess;

import static org.rococoa.ObjCBlocks.block;


/**
 * AVFoundation as the media engine.
 * <p>
 * The Java movie model is written as a self contained QuickTime movie that AVFoundation reads
 * ({@link MovieAccess#playable}), frames come from AVAssetImageGenerator, sound and timing from AVPlayer,
 * exports and imports of other formats from AVAssetExportSession.
 */
public class AVFMediaEngine extends MediaEngine {

    private static final Logger logger = Logger.getLogger(AVFMediaEngine.class.getName());

    public AVFMediaEngine() {
        Frameworks.load();
    }

    /** the AVFoundation objects of a movie */
    private static final class State {
        String path;
        AVAsset asset;
        AVAssetImageGenerator generator;
        AVPlayer player;
        boolean playing;
    }

    private final Map<Object, State> states = new HashMap<>();

    /** @return the state with an asset of the current version of the movie */
    private synchronized State state(Object movie) throws IOException {
        State s = states.computeIfAbsent(movie, k -> new State());
        String path = MovieAccess.playable(movie);
        if (!path.equals(s.path)) {
            if (s.player != null) {
                s.player.pause();
            }
            s.path = path;
            s.asset = AVAsset.fromFile(path);
            s.generator = null;
            s.player = null;
            s.playing = false;
        }
        return s;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void task(Object movie) {
        MovieAccess.draw(movie, this);
        State s;
        synchronized (this) {
            s = states.get(movie);
        }
        if (s != null && s.playing && s.player != null) {
            // keep the player on the time base
            double t = MovieAccess.seconds(movie);
            double p = s.player.currentTime().seconds();
            if (!Double.isNaN(p) && Math.abs(p - t) > 0.25) {
                s.player.seekToTime(Frameworks.CoreMedia.instance.CMTimeMake(Math.round(t * 600), 600));
            }
        }
    }

    @Override
    public void sync(Object movie) {
        try {
            float rate = MovieAccess.rate(movie);
            State s;
            synchronized (this) {
                s = states.get(movie);
            }
            if (rate == 0 || !MovieAccess.isActive(movie)) {
                if (s != null && s.player != null && s.playing) {
                    s.player.pause();
                    s.playing = false;
                }
                return;
            }
            if (!MovieAccess.hasSound(movie)) {
                // pictures are drawn by task(), nothing to play
                return;
            }
            s = state(movie);
            if (s.player == null) {
                s.player = AVPlayer.of(s.path);
            }
            s.player.setVolume(MovieAccess.volume(movie));
            double t = MovieAccess.seconds(movie);
            s.player.seekToTime(Frameworks.CoreMedia.instance.CMTimeMake(Math.round(t * 600), 600));
            s.player.setRate(rate);
            s.playing = true;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.WARNING, "playback", e);
        }
    }

    @Override
    public void invalidate(Object movie) {
        MovieAccess.invalidate(movie);
    }

    @Override
    public synchronized void dispose(Object movie) {
        State s = states.remove(movie);
        if (s != null && s.player != null) {
            s.player.pause();
        }
        MovieAccess.forget(movie);
    }

    /** @return a copy of the CGImage as an ARGB image */
    static BufferedImage toImage(Pointer cgImage) {
        Frameworks.CoreGraphics cg = Frameworks.CoreGraphics.instance;
        int w = cg.CGImageGetWidth(cgImage).intValue();
        int h = cg.CGImageGetHeight(cgImage).intValue();
        Memory data = new Memory((long) w * h * 4);
        Pointer space = cg.CGColorSpaceCreateDeviceRGB();
        Pointer context = cg.CGBitmapContextCreate(data, new NativeLong(w), new NativeLong(h), new NativeLong(8), new NativeLong(w * 4L), space,
                Frameworks.CoreGraphics.kCGImageAlphaPremultipliedFirst | Frameworks.CoreGraphics.kCGBitmapByteOrder32Big);
        try {
            cg.CGContextDrawImage(context, new CGRect(0, 0, w, h), cgImage);
            int[] argb = data.getIntArray(0, w * h);
            if (java.nio.ByteOrder.nativeOrder() == java.nio.ByteOrder.LITTLE_ENDIAN) {
                for (int i = 0; i < argb.length; i++) {
                    argb[i] = Integer.reverseBytes(argb[i]);
                }
            }
            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
            image.setRGB(0, 0, w, h, argb, 0, w);
            return image;
        } finally {
            cg.CGContextRelease(context);
            cg.CGColorSpaceRelease(space);
        }
    }

    /** @return the frame of the asset at the time in seconds */
    private static BufferedImage frame(State s, double seconds) throws IOException {
        if (s.generator == null) {
            s.generator = AVAssetImageGenerator.of(s.asset);
            s.generator.setAppliesPreferredTrackTransform(true);
            CMTime.ByValue zero = Frameworks.CoreMedia.instance.CMTimeMake(0, 600);
            s.generator.setRequestedTimeToleranceBefore(zero);
            s.generator.setRequestedTimeToleranceAfter(zero);
        }
        ObjCObjectByReference error = new ObjCObjectByReference();
        Pointer image = s.generator.copyCGImageAtTime_actualTime_error(Frameworks.CoreMedia.instance.CMTimeMake(Math.round(seconds * 600), 600), null, error);
        if (image == null) {
            NSError e = error.getValueAs(NSError.class);
            logger.log(Level.FINE, "no frame at " + seconds + ": " + (e != null ? e.description() : ""));
            return null;
        }
        try {
            return toImage(image);
        } finally {
            Frameworks.CoreGraphics.instance.CGImageRelease(image);
        }
    }

    @Override
    public BufferedImage frame(Object movie, long time) throws IOException {
        if (!MovieAccess.hasVisual(movie)) {
            return null;
        }
        State s = state(movie);
        BufferedImage image = frame(s, MovieAccess.seconds(movie, time));
        return image != null ? image : MovieAccess.javaFrame(movie, time);
    }

    /** @return the output file type and the preset of an export file type */
    private static String[] exportType(int fileType) {
        return switch (quicktime.internal.toolbox.Bytes.fourCC(fileType)) {
            case "MooV" -> new String[] {AVAssetExportSession.AVFileTypeQuickTimeMovie, AVAssetExportSession.AVAssetExportPresetHighestQuality};
            case "mpg4" -> new String[] {AVAssetExportSession.AVFileTypeMPEG4, AVAssetExportSession.AVAssetExportPresetHighestQuality};
            case "M4V " -> new String[] {AVAssetExportSession.AVFileTypeAppleM4V, AVAssetExportSession.AVAssetExportPresetHighestQuality};
            case "M4A " -> new String[] {AVAssetExportSession.AVFileTypeAppleM4A, AVAssetExportSession.AVAssetExportPresetAppleM4A};
            case "3gpp" -> new String[] {AVAssetExportSession.AVFileType3GPP, AVAssetExportSession.AVAssetExportPresetLowQuality};
            case "3gp2" -> new String[] {AVAssetExportSession.AVFileType3GPP2, AVAssetExportSession.AVAssetExportPresetLowQuality};
            case "AIFF" -> new String[] {AVAssetExportSession.AVFileTypeAIFF, AVAssetExportSession.AVAssetExportPresetPassthrough};
            case "WAVE" -> new String[] {AVAssetExportSession.AVFileTypeWAVE, AVAssetExportSession.AVAssetExportPresetPassthrough};
            default -> null;
        };
    }

    /** @return true if the export completed */
    private static boolean export(AVAsset asset, String path, String fileType, String preset, Progress progress) throws IOException {
        AVAssetExportSession session = AVAssetExportSession.of(asset, preset);
        if (session == null) {
            return false;
        }
        File out = new File(path);
        if (out.exists() && !out.delete()) {
            throw new IOException("cannot replace " + path);
        }
        session.setOutputURL(NSURL.fileURLWithPath(out.getAbsolutePath()));
        session.setOutputFileType(fileType);
        session.setShouldOptimizeForNetworkUse(true);
        CountDownLatch done = new CountDownLatch(1);
        BlockLiteral handler = block((AVAssetExportSession.CompletionHandler) b -> done.countDown());
        try {
            session.exportAsynchronouslyWithCompletionHandler(handler);
            while (!done.await(100, TimeUnit.MILLISECONDS)) {
                if (progress.progress(session.progress()) != 0) {
                    session.cancelExport();
                }
            }
        } catch (InterruptedException e) {
            session.cancelExport();
            Thread.currentThread().interrupt();
            throw new IOException(e);
        } finally {
            Foundation.getRococoaLibrary().releaseObjCBlock(handler.getPointer());
        }
        if (session.status() != AVAssetExportSession.AVAssetExportSessionStatusCompleted) {
            NSError e = session.error();
            if (session.status() == AVAssetExportSession.AVAssetExportSessionStatusCancelled) {
                throw new IOException("aborted");
            }
            throw new IOException("export failed: " + (e != null ? e.description() : session.status()));
        }
        return true;
    }

    @Override
    public int export(Object movie, String path, int fileType, Progress progress) throws IOException {
        String[] type = exportType(fileType);
        if (type == null) {
            return MovieAccess.featureUnsupported;
        }
        State s = state(movie);
        return export(s.asset, path, type[0], type[1], progress) ? 0 : MovieAccess.featureUnsupported;
    }

    @Override
    public String importFile(String path) throws IOException {
        AVAsset asset = AVAsset.fromFile(path);
        if (!asset.isReadable()) {
            return null;
        }
        File temp = File.createTempFile("qtimport", ".mov");
        temp.deleteOnExit();
        if (!export(asset, temp.getPath(), AVAssetExportSession.AVFileTypeQuickTimeMovie, AVAssetExportSession.AVAssetExportPresetPassthrough, p -> 0)) {
            // compressed formats QuickTime movies cannot carry are converted
            if (!export(asset, temp.getPath(), AVAssetExportSession.AVFileTypeQuickTimeMovie, AVAssetExportSession.AVAssetExportPresetHighestQuality, p -> 0)) {
                return null;
            }
        }
        return temp.getPath();
    }
}
