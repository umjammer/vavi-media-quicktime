/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;


/**
 * The sequence grabber in Java: video channels capturing from {@link VideoSource}s through video digitizers,
 * sound channels capturing with Java Sound, previewing into a graphics world and recording into movies.
 */
public final class SequenceGrabberManager {

    private static final Logger logger = Logger.getLogger(SequenceGrabberManager.class.getName());

    private SequenceGrabberManager() {
    }

    static final int barg = Bytes.fourCC("barg");
    static final int sgch = Bytes.fourCC("sgch");
    static final int vdig = Bytes.fourCC("vdig");
    static final int vide = Bytes.fourCC("vide");
    static final int soun = Bytes.fourCC("soun");
    static final int text = Bytes.fourCC("text");
    static final int musi = Bytes.fourCC("musi");

    static final int noErr = 0;
    static final int paramErr = -50;
    static final int ioErr = -36;
    static final int badComponentInstance = 0x8001;
    static final int noDeviceForChannel = -9400;
    static final int grabTimeComplete = -9401;
    static final int cantDoThatInCurrentMode = -9402;
    static final int couldntGetRequiredComponent = -9405;
    static final int badSGChannel = -9406;
    static final int seqGrabInfoNotAvailable = -9407;
    static final int deviceCantMeetRequest = -9408;
    static final int digiUnimpErr = -2201;
    static final int qtParamErr = -50;
    /** what SGIdle returns when the maximum record time is reached, as the Java side expects it */
    static final int grabTimeCompleteUnsigned = grabTimeComplete & 0xffff;

    // channel usage
    static final int seqGrabRecord = 1;
    static final int seqGrabPreview = 2;
    static final int seqGrabPlayDuringRecord = 4;
    static final int seqGrabLowLatencyCapture = 8;
    static final int seqGrabAlwaysUseTimeBase = 16;

    // data output flags
    static final int seqGrabToDisk = 1;
    static final int seqGrabToMemory = 2;
    static final int seqGrabDontUseTempMemory = 4;
    static final int seqGrabAppendToFile = 8;
    static final int seqGrabDontAddMovieResource = 16;
    static final int seqGrabDontMakeMovie = 32;
    static final int seqGrabPreExtendFile = 64;

    // device list flags
    static final int sgDeviceListWithIcons = 1;
    static final int sgDeviceListDontCheckAvailability = 2;
    static final int sgDeviceNameFlagDeviceUnavailable = 1;

    // data proc write types
    static final int seqGrabWriteAppend = 0;

    static void register() {
        ComponentManager.register(barg, 0, 0, "Sequence Grabber", "Sequence grabber", c -> new SG(c.ref));
        String[][] channels = {{"vide", "Video Channel"}, {"soun", "Sound Channel"}, {"text", "Text Channel"}, {"musi", "Music Channel"}};
        for (String[] ch : channels) {
            int type = Bytes.fourCC(ch[0]);
            ComponentManager.register(sgch, type, 0, ch[1], "Sequence grabber " + ch[1].toLowerCase(), c -> new Chan(c.ref, type));
        }
        ComponentManager.register(vdig, VideoSource.test, 0, "Test Pattern", "Test pattern video digitizer", c -> new Digitizer(VideoSource.test));
        ComponentManager.register(vdig, VideoSource.scrn, 0, "Screen", "Screen capture video digitizer", c -> new Digitizer(VideoSource.scrn));
        ComponentManager.register(vdig, VideoSource.avfc, 0, "Camera", "AVFoundation camera video digitizer", c -> new Digitizer(VideoSource.avfc));
    }

    // ---- state

    /** a sequence grabber instance */
    static final class SG {
        final int ref;
        int port;
        int gdh;
        final List<Chan> channels = new ArrayList<>();
        boolean previewing;
        boolean recording;
        boolean paused;
        boolean prepared;
        // the data output
        String path;
        int outputFlags = seqGrabToMemory;
        int dataRef;
        int dataRefType;
        final List<Output> outputs = new ArrayList<>();
        int dataProc;
        int dataProcRefCon;
        /** in 60ths of a second, 0 for none */
        int maxRecordTime;
        long started;
        long pausedAt;
        long pausedTotal;
        int timeBase;
        MovieRec movie;

        SG(int ref) {
            this.ref = ref;
            int[] g = QuickDrawManager.currentGWorld();
            port = g[0];
            gdh = g[1];
        }

        /** @return the seconds recorded or previewed */
        double seconds() {
            if (started == 0) {
                return 0;
            }
            long now = paused ? pausedAt : System.nanoTime();
            return (now - started - pausedTotal) / 1e9;
        }
    }

    /** a sequence grabber channel */
    static final class Chan {
        final int ref;
        final int type;
        SG sg;
        int usage = seqGrabRecord | seqGrabPreview;
        int playFlags;
        Rectangle bounds;
        int clip;
        int[] matrix;
        String device;
        String dataSourceName = "";
        int dataSourceScript;
        Output output;
        int refCon;
        // video
        VideoSource source;
        int digitizer;
        int compressorType = Bytes.fourCC("jpeg");
        int compressor;
        int depth;
        int spatialQuality = Codecs.codecNormalQuality;
        int temporalQuality;
        int keyFrameRate;
        /** Fixed frames per second, 0 is as fast as possible */
        int frameRate;
        Rectangle videoRect;
        BufferedImage lastFrame;
        // sound
        short volume = 0x100;
        int sampleRate = (int) (44100L << 16);
        int sampleSize = 16;
        int numChannels = 2;
        int compressionType = Bytes.fourCC("twos");
        int chunkSize = 2;
        String inputDriver;
        int spbDevice;
        TargetDataLine line;
        Thread capture;
        final ByteArrayOutputStream captured = new ByteArrayOutputStream();
        // text and music
        String fontName = "Helvetica";
        int fontSize = 12;
        byte[] foreColor = {0, 0, 0, 0, 0, 0};
        byte[] backColor = {-1, -1, -1, -1, -1, -1};
        int justification;
        int returnToSpace;
        byte[] instrument;
        // recording
        TrackRec track;
        MediaRec media;
        int descIndex;
        double lastFrameTime = -1;
        BufferedImage prevFrame;
        int framesSinceKey;
        long soundFrames;

        Chan(int ref, int type) {
            this.ref = ref;
            this.type = type;
        }

        int timeScale() {
            return type == soun ? (int) ((sampleRate & 0xffff_ffffL) >> 16) : 600;
        }

        boolean visual() {
            return type == vide || type == text;
        }
    }

    /** an output of recorded data */
    static final class Output {
        final int ref;
        final SG sg;
        int dataRef;
        int dataRefType;
        int flags;
        long maxOffset;
        int next;

        Output(SG sg) {
            this.ref = Heap.newObject(this, 4);
            this.sg = sg;
        }

        String path() {
            DataRefRec d = MovieToolbox.dataRef(dataRef, dataRefType);
            return d == null ? null : d.path();
        }
    }

    /** a video digitizer instance */
    static final class Digitizer {
        final int subType;
        VideoSource source;
        int input;
        int clipRgn;
        boolean clipState;
        int standard;

        Digitizer(int subType) {
            this.subType = subType;
            List<VideoSource> l = VideoSource.of(subType);
            this.source = l.isEmpty() ? null : l.getFirst();
        }
    }

    static SG sg(int ref) {
        ComponentManager.InstanceRec i = ComponentManager.instance(ref);
        return i != null && i.impl instanceof SG s ? s : null;
    }

    static Chan chan(int ref) {
        ComponentManager.InstanceRec i = ComponentManager.instance(ref);
        return i != null && i.impl instanceof Chan c ? c : null;
    }

    static Digitizer digitizer(int ref) {
        ComponentManager.InstanceRec i = ComponentManager.instance(ref);
        return i != null && i.impl instanceof Digitizer d ? d : null;
    }

    static Output output(int ref) {
        return Heap.peerOrNull(ref, Output.class);
    }

    // ---- the sequence grabber

    public static int SGInitialize(int sg) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        s.timeBase = ClockManager.NewTimeBase();
        return noErr;
    }

    public static int SGSetGWorld(int sg, int port, int gdh) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        int[] cur = QuickDrawManager.currentGWorld();
        s.port = port != 0 ? port : cur[0];
        s.gdh = gdh != 0 ? gdh : cur[1];
        return noErr;
    }

    public static int SGGetGWorld(int sg, int[] port, int[] gdh) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        port[0] = s.port;
        if (gdh != null && gdh.length > 0) {
            gdh[0] = s.gdh;
        }
        return noErr;
    }

    /** @return the names of the devices of the channel type */
    static List<String> deviceNames(int type) {
        List<String> l = new ArrayList<>();
        if (type == vide) {
            for (VideoSource v : VideoSource.all()) {
                l.add(v.name());
            }
        } else if (type == soun) {
            l.addAll(SoundManager.inputDevices());
        }
        return l;
    }

    public static int SGNewChannel(int sg, int channelType, int[] ref) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        ComponentManager.ComponentRec c = ComponentManager.find(sgch, channelType);
        if (c == null) {
            return couldntGetRequiredComponent;
        }
        if (channelType == soun && SoundManager.inputDevices().isEmpty()) {
            return noDeviceForChannel;
        }
        int r = ComponentManager.open(c);
        Chan ch = chan(r);
        ch.sg = s;
        if (channelType == vide) {
            List<VideoSource> l = VideoSource.all();
            ch.source = l.getFirst();
            ch.device = ch.source.name();
            Dimension d = ch.source.size();
            ch.bounds = new Rectangle(0, 0, d.width, d.height);
        } else if (channelType == soun) {
            ch.device = SoundManager.inputDevices().getFirst();
            ch.inputDriver = ch.device;
        } else if (channelType == text) {
            ch.bounds = new Rectangle(0, 0, 320, 60);
        }
        s.channels.add(ch);
        ref[0] = r;
        return noErr;
    }

    /** a channel component opened on its own is given to its sequence grabber */
    public static int SGInitChannel(int ref, int owner) {
        Chan c = chan(ref);
        SG s = sg(owner);
        if (c == null || s == null) {
            return c == null ? badSGChannel : badComponentInstance;
        }
        c.sg = s;
        if (!s.channels.contains(c)) {
            s.channels.add(c);
        }
        if (c.type == vide && c.source == null) {
            c.source = VideoSource.all().getFirst();
            c.device = c.source.name();
            c.bounds = new Rectangle(c.source.size());
        }
        return noErr;
    }

    public static int SGDisposeChannel(int sg, int ref) {
        SG s = sg(sg);
        Chan c = chan(ref);
        if (s == null || c == null) {
            return s == null ? badComponentInstance : badSGChannel;
        }
        stopChannel(c);
        s.channels.remove(c);
        return ComponentManager.CloseComponent(ref);
    }

    /** the channel at the 1 based index, reference and type */
    private static int indChannel(int sg, short index, int[] ref, int[] type) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (index < 1 || index > s.channels.size()) {
            return paramErr;
        }
        Chan c = s.channels.get(index - 1);
        if (ref != null && ref.length > 0) {
            ref[0] = c.ref;
        }
        if (type != null && type.length > 0) {
            type[0] = c.type;
        }
        return noErr;
    }

    /** the reference is not wanted */
    public static int SGGetIndChannel(int sg, short index, int ref, int[] type) {
        return indChannel(sg, index, null, type);
    }

    public static int SGGetIndChannel(int sg, short index, int[] ref, int[] type) {
        return indChannel(sg, index, ref, type);
    }

    // ---- data output

    public static int SGSetDataOutput(int sg, byte[] fsspec, int flags) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (s.recording) {
            return cantDoThatInCurrentMode;
        }
        s.path = fsspec != null ? FileManager.specToPath(fsspec) : null;
        s.outputFlags = flags;
        s.dataRef = 0;
        return noErr;
    }

    public static int SGGetDataOutput(int sg, byte[] fsspec, int[] flags) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (fsspec != null && s.path != null) {
            byte[] b = FileManager.pathToSpec(s.path);
            System.arraycopy(b, 0, fsspec, 0, Math.min(b.length, fsspec.length));
        }
        flags[0] = s.outputFlags;
        return noErr;
    }

    public static int SGSetDataRef(int sg, int dataRef, int dataRefType, int flags) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (s.recording) {
            return cantDoThatInCurrentMode;
        }
        s.outputFlags = flags;
        if (dataRef == 0) {
            s.dataRef = 0;
            s.path = null;
            return noErr;
        }
        s.dataRef = Heap.newHandle(Heap.handleBytes(dataRef));
        s.dataRefType = dataRefType;
        DataRefRec d = MovieToolbox.dataRef(dataRef, dataRefType);
        s.path = d.path();
        return noErr;
    }

    public static int SGGetDataRef(int sg, int[] dataRef, int[] dataRefType, int[] flags) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (s.dataRef != 0) {
            dataRef[0] = Heap.newHandle(Heap.handleBytes(s.dataRef));
            dataRefType[0] = s.dataRefType;
        } else if (s.path != null) {
            dataRef[0] = Heap.newHandle(FileManager.newAliasRecord(s.path));
            dataRefType[0] = DataRefRec.alis;
        } else {
            dataRef[0] = 0;
            dataRefType[0] = 0;
        }
        flags[0] = s.outputFlags;
        return noErr;
    }

    public static int SGSetDataProc(int sg, int proc, int refCon) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        s.dataProc = proc;
        s.dataProcRefCon = refCon;
        return noErr;
    }

    public static int SGSetMaximumRecordTime(int sg, int ticks) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        s.maxRecordTime = ticks;
        return noErr;
    }

    public static int SGGetMaximumRecordTime(int sg, int[] ticks) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        ticks[0] = s.maxRecordTime;
        return noErr;
    }

    private static long space(String path) {
        File f = new File(path != null ? path : System.getProperty("java.io.tmpdir"));
        File dir = f.isDirectory() ? f : f.getAbsoluteFile().getParentFile();
        return dir != null ? dir.getUsableSpace() : 0;
    }

    /** in KB, saturated */
    public static int SGGetStorageSpaceRemaining(int sg, int[] bytes) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        bytes[0] = (int) Math.min(Integer.MAX_VALUE, space(s.path));
        return noErr;
    }

    public static int SGGetDataOutputStorageSpaceRemaining(int sg, int output, int[] space) {
        SG s = sg(sg);
        Output o = output(output);
        if (s == null || o == null) {
            return s == null ? badComponentInstance : paramErr;
        }
        space[0] = (int) Math.min(0xffff_ffffL, space(o.path()));
        return noErr;
    }

    /** in 60ths of a second, from the maximum record time or the space left at the current data rate */
    public static int SGGetTimeRemaining(int sg, int[] ticks) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        double elapsed = s.seconds();
        if (s.maxRecordTime > 0) {
            ticks[0] = (int) Math.max(0, s.maxRecordTime - elapsed * 60);
            return noErr;
        }
        long written = 0;
        if (s.movie != null) {
            for (TrackRec t : s.movie.tracks) {
                if (t.media != null) {
                    for (MediaRec.SampleRun r : t.media.runs) {
                        written += r.bytes;
                    }
                }
            }
        }
        double rate = elapsed > 0 && written > 0 ? written / elapsed : 0;
        ticks[0] = rate > 0 ? (int) Math.min(Integer.MAX_VALUE, space(s.path) / rate * 60) : Integer.MAX_VALUE;
        return noErr;
    }

    public static int SGGetMode(int sg, byte[] previewMode, byte[] recordMode) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        previewMode[0] = (byte) (s.previewing ? 1 : 0);
        recordMode[0] = (byte) (s.recording ? 1 : 0);
        return noErr;
    }

    public static int SGGetTimeBase(int sg, int[] timeBase) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (s.timeBase == 0) {
            s.timeBase = ClockManager.NewTimeBase();
        }
        timeBase[0] = s.timeBase;
        return noErr;
    }

    // ---- outputs

    public static int SGNewOutput(int sg, int dataRef, int dataRefType, int flags, int[] output) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        Output o = new Output(s);
        o.dataRef = Heap.newHandle(Heap.handleBytes(dataRef));
        o.dataRefType = dataRefType;
        o.flags = flags;
        s.outputs.add(o);
        output[0] = o.ref;
        return noErr;
    }

    public static int SGDisposeOutput(int sg, int output) {
        SG s = sg(sg);
        Output o = output(output);
        if (s == null || o == null) {
            return s == null ? badComponentInstance : paramErr;
        }
        s.outputs.remove(o);
        for (Chan c : s.channels) {
            if (c.output == o) {
                c.output = null;
            }
        }
        Heap.free(output);
        return noErr;
    }

    public static int SGSetOutputFlags(int sg, int output, int flags) {
        Output o = output(output);
        if (o == null) {
            return paramErr;
        }
        o.flags = flags;
        return noErr;
    }

    public static int SGSetChannelOutput(int sg, int ref, int output) {
        Chan c = chan(ref);
        Output o = output(output);
        if (c == null) {
            return badSGChannel;
        }
        c.output = o;
        return noErr;
    }

    public static int SGGetOutputDataReference(int sg, int output, int[] dataRef, int[] dataRefType) {
        Output o = output(output);
        if (o == null) {
            return paramErr;
        }
        dataRef[0] = o.dataRef;
        dataRefType[0] = o.dataRefType;
        return noErr;
    }

    /** a wide */
    public static int SGSetOutputMaximumOffset(int sg, int output, byte[] maxOffset) {
        Output o = output(output);
        if (o == null) {
            return paramErr;
        }
        o.maxOffset = Bytes.getLong(maxOffset, 0);
        return noErr;
    }

    public static int SGGetOutputMaximumOffset(int sg, int output, byte[] maxOffset) {
        Output o = output(output);
        if (o == null) {
            return paramErr;
        }
        Bytes.putLong(maxOffset, 0, o.maxOffset);
        return noErr;
    }

    public static int SGSetOutputNextOutput(int sg, int output, int next) {
        Output o = output(output);
        if (o == null) {
            return paramErr;
        }
        o.next = next;
        return noErr;
    }

    public static int SGGetOutputNextOutput(int sg, int output, int[] next) {
        Output o = output(output);
        if (o == null) {
            return paramErr;
        }
        next[0] = o.next;
        return noErr;
    }

    // ---- preview and record

    /** starts the devices of the channels used */
    private static int prepare(SG s, boolean record) {
        for (Chan c : s.channels) {
            if (c.usage == 0) {
                continue;
            }
            try {
                if (c.type == vide && c.source != null) {
                    c.source.start();
                } else if (c.type == soun && record && (c.usage & seqGrabRecord) != 0) {
                    startSound(c);
                }
            } catch (IOException | LineUnavailableException e) {
                logger.log(Level.FINE, "device", e);
                return noDeviceForChannel;
            }
        }
        s.prepared = true;
        return noErr;
    }

    private static AudioFormat format(Chan c) {
        double rate = (c.sampleRate & 0xffff_ffffL) / 65536.0;
        return new AudioFormat((float) rate, c.sampleSize, c.numChannels, c.sampleSize > 8, true);
    }

    /** captures the sound input on a thread of its own */
    private static void startSound(Chan c) throws LineUnavailableException {
        if (c.line != null) {
            return;
        }
        AudioFormat f = format(c);
        TargetDataLine line = SoundManager.sources.apply(f);
        if (line == null) {
            throw new LineUnavailableException("no sound input");
        }
        if (!line.isOpen()) {
            line.open(f);
        }
        line.start();
        c.line = line;
        synchronized (c.captured) {
            c.captured.reset();
        }
        Thread t = new Thread(() -> {
            byte[] b = new byte[4096 - 4096 % Math.max(1, f.getFrameSize())];
            while (c.line == line && !Thread.currentThread().isInterrupted()) {
                int n = line.read(b, 0, b.length);
                if (n <= 0) {
                    break;
                }
                if (c.sg != null && c.sg.paused) {
                    continue;
                }
                synchronized (c.captured) {
                    c.captured.write(b, 0, n);
                }
                // as fast as the real time
                try {
                    Thread.sleep(Math.max(1, (long) (n / (double) f.getFrameSize() / f.getSampleRate() * 1000) - 1));
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "Sequence grabber sound");
        t.setDaemon(true);
        c.capture = t;
        t.start();
    }

    private static void stopChannel(Chan c) {
        if (c.line != null) {
            TargetDataLine l = c.line;
            c.line = null;
            if (c.capture != null) {
                c.capture.interrupt();
                try {
                    c.capture.join(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                c.capture = null;
            }
            l.stop();
            l.close();
        }
        if (c.source != null) {
            c.source.stop();
        }
    }

    public static int SGPrepare(int sg, byte prepareForPreview, byte prepareForRecord) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        return prepare(s, prepareForRecord != 0);
    }

    public static int SGRelease(int sg) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (s.previewing || s.recording) {
            return cantDoThatInCurrentMode;
        }
        for (Chan c : s.channels) {
            stopChannel(c);
        }
        s.prepared = false;
        return noErr;
    }

    public static int SGStartPreview(int sg) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (s.recording) {
            return cantDoThatInCurrentMode;
        }
        int e = prepare(s, false);
        if (e != noErr) {
            return e;
        }
        s.previewing = true;
        s.started = System.nanoTime();
        s.pausedTotal = 0;
        return noErr;
    }

    /** @return the data reference recorded into, a file or memory */
    private static DataRefRec target(SG s, Chan c) throws IOException {
        if (c.output != null) {
            String p = c.output.path();
            if (p != null) {
                return DataRefRec.forFile(p);
            }
            return MovieToolbox.dataRef(c.output.dataRef, c.output.dataRefType);
        }
        if (s.path != null && (s.outputFlags & seqGrabToMemory) == 0) {
            return DataRefRec.forFile(s.path);
        }
        if (s.dataRef != 0) {
            DataRefRec d = MovieToolbox.dataRef(s.dataRef, s.dataRefType);
            if (d.type == DataRefRec.hndl || d.path() != null) {
                return d.path() != null ? DataRefRec.forFile(d.path()) : d;
            }
        }
        return DataRefRec.forHandle(Heap.newHandle(0));
    }

    public static int SGStartRecord(int sg) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (s.recording) {
            return cantDoThatInCurrentMode;
        }
        s.previewing = false;
        try {
            String path = s.path != null && (s.outputFlags & seqGrabToMemory) == 0 ? s.path : null;
            if (path != null && (s.outputFlags & seqGrabAppendToFile) == 0) {
                DataRefRec.closeReader(path);
                MdatWriter.close(path);
                new File(path).delete();
            }
            int e = prepare(s, true);
            if (e != noErr) {
                return e;
            }
            boolean makeMovie = (s.outputFlags & seqGrabDontMakeMovie) == 0;
            s.movie = makeMovie ? new MovieRec() : null;
            for (Chan c : s.channels) {
                c.track = null;
                c.media = null;
                c.lastFrameTime = -1;
                c.prevFrame = null;
                c.framesSinceKey = 0;
                c.soundFrames = 0;
                if ((c.usage & seqGrabRecord) == 0 || !makeMovie || (c.type != vide && c.type != soun)) {
                    continue;
                }
                TrackRec t = s.movie.newTrack();
                MediaRec m = new MediaRec(t, c.type, c.timeScale());
                m.dataRefs.add(target(s, c));
                m.handlerName = c.type == vide ? "Apple Video Media Handler" : "Apple Sound Media Handler";
                t.media = m;
                if (c.type == vide) {
                    Rectangle b = c.bounds != null ? c.bounds : new Rectangle(c.source.size());
                    t.width = b.width << 16;
                    t.height = b.height << 16;
                } else {
                    t.volume = c.volume;
                }
                c.track = t;
                c.media = m;
            }
            s.recording = true;
            s.paused = false;
            s.started = System.nanoTime();
            s.pausedTotal = 0;
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static int SGPause(int sg, byte pause) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        boolean p = pause != 0;
        if (p && !s.paused) {
            s.pausedAt = System.nanoTime();
        } else if (!p && s.paused) {
            s.pausedTotal += System.nanoTime() - s.pausedAt;
        }
        s.paused = p;
        return noErr;
    }

    public static int SGGetPause(int sg, byte[] paused) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        paused[0] = (byte) (s.paused ? 1 : 0);
        return noErr;
    }

    /** @return the frame of the video channel, cropped to the video rect */
    private static BufferedImage frame(Chan c) {
        if (c.source == null) {
            return null;
        }
        BufferedImage img = c.source.grab();
        if (img != null && c.videoRect != null) {
            Rectangle r = c.videoRect.intersection(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
            if (!r.isEmpty()) {
                img = img.getSubimage(r.x, r.y, r.width, r.height);
            }
        }
        return img;
    }

    /** @return the frame drawn at the size of the bounds */
    private static BufferedImage scaled(Chan c, BufferedImage img) {
        Rectangle b = c.bounds;
        if (b == null || b.width <= 0 || b.height <= 0 || (b.width == img.getWidth() && b.height == img.getHeight())) {
            return img;
        }
        BufferedImage t = Codecs.argb(b.width, b.height);
        Graphics2D g = t.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, b.width, b.height, null);
        g.dispose();
        return t;
    }

    /** draws the frame into the grabber's port at the channel's bounds through its matrix */
    private static void draw(SG s, Chan c, BufferedImage img) {
        Rectangle b = c.bounds != null ? c.bounds : new Rectangle(0, 0, img.getWidth(), img.getHeight());
        AffineTransform at = c.matrix != null ? Imaging.transform(c.matrix) : new AffineTransform();
        at.concatenate(Imaging.mapRect(new Rectangle(0, 0, img.getWidth(), img.getHeight()), b));
        Imaging.draw(s.port, img, at, Imaging.ditherCopy, null, c.clip);
    }

    /** @return the codec of the video channel */
    private static Codecs.Codec codec(Chan c) {
        Codecs.Codec k = ImageCompressionManager.codec(c.compressorType, c.compressor);
        return k != null && k.canCompress() ? k : Codecs.get(Bytes.fourCC("jpeg"));
    }

    /** writes data for a channel: to the movie's media, and to the data proc */
    private static int write(SG s, Chan c, byte[] data, long[] offset, double seconds) throws IOException {
        long at = -1;
        if (c.media != null) {
            at = c.media.dataRefs.getFirst().append(data, 0, data.length);
        }
        if (s.dataProc != 0) {
            int p = Heap.newPtr(data.length);
            int off = Heap.newPtr(4);
            try {
                Heap.write(p, data, 0, data.length);
                Heap.putInt(off, (int) Math.max(0, at));
                int time = (int) Math.round(seconds * c.timeScale());
                Object r = Closures.call(s.dataProc, c.ref, p, data.length, off, c.refCon, time, (short) seqGrabWriteAppend, s.dataProcRefCon);
                int e = r instanceof Number n ? n.shortValue() : 0;
                if (e != 0) {
                    return e;
                }
            } finally {
                Heap.free(p);
                Heap.free(off);
            }
        }
        offset[0] = at;
        return noErr;
    }

    private static int recordVideo(SG s, Chan c, double seconds) throws IOException {
        double interval = c.frameRate > 0 ? 65536.0 / c.frameRate : 0;
        if (c.lastFrameTime >= 0 && seconds - c.lastFrameTime < interval) {
            return noErr;
        }
        BufferedImage img = frame(c);
        if (img == null) {
            return noErr;
        }
        img = scaled(c, img);
        Codecs.Codec k = codec(c);
        boolean key = c.prevFrame == null || !k.isTemporal() || c.temporalQuality == 0
                || (c.keyFrameRate > 0 && c.framesSinceKey >= c.keyFrameRate);
        int depth = c.depth != 0 ? c.depth : 24;
        byte[] data = k.encode(img, key ? null : c.prevFrame, k.depth(depth), c.spatialQuality);
        long[] at = new long[1];
        int e = write(s, c, data, at, seconds);
        if (e != noErr) {
            return e;
        }
        if (c.media != null) {
            // the previous frame lasts until this one
            closeVideoSample(c, seconds);
            if (c.descIndex == 0 || Bytes.getInt(c.media.descriptions.get(c.descIndex - 1), 4) != k.type) {
                c.descIndex = c.media.descriptionIndex(Codecs.description(k.type, img.getWidth(), img.getHeight(), k.depth(depth), k.name, 0, c.spatialQuality));
            }
            // the duration is fixed when the next frame comes
            c.media.addSamples(1, at[0], data.length, data.length, 1, 1, c.descIndex, key, 0);
        }
        c.lastFrameTime = seconds;
        c.prevFrame = img;
        c.framesSinceKey = key ? 1 : c.framesSinceKey + 1;
        c.lastFrame = img;
        return noErr;
    }

    /** sets the duration of the last video sample from its time to the time */
    private static void closeVideoSample(Chan c, double seconds) {
        if (c.media == null || c.media.runs.isEmpty() || c.lastFrameTime < 0) {
            return;
        }
        MediaRec.SampleRun last = c.media.runs.getLast();
        int duration = (int) Math.max(1, Math.round((seconds - c.lastFrameTime) * c.timeScale()));
        if (last.count == 1) {
            last.duration = duration;
        } else {
            // split the last sample off its run
            last.count--;
            long size = last.bytes / (last.count + 1);
            last.bytes -= size;
            MediaRec.SampleRun r = new MediaRec.SampleRun(last.dataRefIndex, last.offset + last.bytes, (int) size, size, duration, 1, last.descIndex, last.sync, 0);
            c.media.runs.add(r);
        }
        c.media.changed();
    }

    private static int recordSound(SG s, Chan c, double seconds) throws IOException {
        byte[] data;
        synchronized (c.captured) {
            AudioFormat f = format(c);
            int frame = Math.max(1, f.getFrameSize());
            int n = c.captured.size() - c.captured.size() % frame;
            if (n == 0) {
                return noErr;
            }
            byte[] all = c.captured.toByteArray();
            data = java.util.Arrays.copyOf(all, n);
            c.captured.reset();
            c.captured.write(all, n, all.length - n);
        }
        int frameBytes = c.numChannels * ((c.sampleSize + 7) / 8);
        long frames = data.length / frameBytes;
        long[] at = new long[1];
        int e = write(s, c, data, at, seconds);
        if (e != noErr) {
            return e;
        }
        if (c.media != null) {
            if (c.descIndex == 0) {
                double rate = (c.sampleRate & 0xffff_ffffL) / 65536.0;
                c.descIndex = c.media.descriptionIndex(Importers.soundDescription(Bytes.fourCC("twos"), c.numChannels, c.sampleSize, rate, true, false, true));
            }
            c.media.addSamples(1, at[0], 1, data.length, 1, (int) frames, c.descIndex, true, 0);
        }
        c.soundFrames += frames;
        return noErr;
    }

    public static int SGIdle(int sg) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (!s.previewing && !s.recording || s.paused) {
            return noErr;
        }
        double seconds = s.seconds();
        try {
            for (Chan c : new ArrayList<>(s.channels)) {
                if (s.recording && (c.usage & seqGrabRecord) != 0) {
                    int e = c.type == vide ? recordVideo(s, c, seconds) : c.type == soun ? recordSound(s, c, seconds) : noErr;
                    if (e != noErr) {
                        SGStop(sg);
                        return e;
                    }
                }
                boolean show = s.previewing ? (c.usage & seqGrabPreview) != 0 : (c.usage & seqGrabPlayDuringRecord) != 0;
                if (show && c.type == vide) {
                    BufferedImage img = s.recording && c.lastFrame != null ? c.lastFrame : frame(c);
                    if (img != null) {
                        c.lastFrame = img;
                        draw(s, c, img);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.FINE, "idle", e);
            SGStop(sg);
            return ioErr;
        } catch (RuntimeException e) {
            logger.log(Level.FINE, "idle", e);
            return paramErr;
        }
        if (s.recording && s.maxRecordTime > 0 && seconds * 60 >= s.maxRecordTime) {
            SGStop(sg);
            return grabTimeCompleteUnsigned;
        }
        return noErr;
    }

    public static int SGUpdate(int sg, int updateRgn) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        for (Chan c : s.channels) {
            if (c.type == vide && c.lastFrame != null) {
                draw(s, c, c.lastFrame);
            }
        }
        return noErr;
    }

    public static int SGStop(int sg) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        double seconds = s.seconds();
        boolean wasRecording = s.recording;
        if (wasRecording) {
            try {
                for (Chan c : s.channels) {
                    if (c.type == soun && c.line != null) {
                        recordSound(s, c, seconds);
                    }
                    if (c.type == vide) {
                        closeVideoSample(c, Math.max(seconds, c.lastFrameTime + 1.0 / 30));
                    }
                }
            } catch (IOException e) {
                logger.log(Level.FINE, "stop", e);
            }
        }
        for (Chan c : s.channels) {
            stopChannel(c);
        }
        s.previewing = false;
        s.recording = false;
        s.paused = false;
        s.prepared = false;
        if (wasRecording && s.movie != null) {
            MovieRec m = s.movie;
            for (TrackRec t : m.tracks) {
                if (t.media == null) {
                    continue;
                }
                t.media.changed();
                long d = t.media.duration();
                if (d > 0) {
                    t.edits.add(new TrackRec.Edit(ClockManager.convert(d, t.media.timeScale, m.timeScale), 0, 0x10000));
                }
            }
            m.updateTimeBase();
            try {
                for (TrackRec t : m.tracks) {
                    if (t.media != null) {
                        String p = t.media.dataRefs.getFirst().path();
                        if (p != null) {
                            MdatWriter.flush(p);
                        }
                    }
                }
                if (s.path != null && (s.outputFlags & (seqGrabToMemory | seqGrabDontAddMovieResource)) == 0) {
                    MovieFiles.writeMovieResource(m, s.path);
                }
            } catch (IOException e) {
                logger.log(Level.FINE, "movie", e);
                return ioErr;
            }
        }
        return noErr;
    }

    /** the recorded movie */
    public static int SGGetMovie(int sg) {
        SG s = sg(sg);
        return s == null || s.movie == null ? 0 : s.movie.ref;
    }

    /** grabs a picture of the first video channel, into the bounds or at its own size */
    public static int SGGrabPict(int sg, int[] pict, byte[] bounds, short offscreenDepth, int grabPictFlags) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        for (Chan c : s.channels) {
            if (c.type != vide || c.source == null) {
                continue;
            }
            try {
                boolean started = s.prepared || s.previewing || s.recording;
                if (!started) {
                    c.source.start();
                }
                BufferedImage img = frame(c);
                if (!started) {
                    c.source.stop();
                }
                if (img == null) {
                    return seqGrabInfoNotAvailable;
                }
                Rectangle r = bounds != null ? Imaging.rect(bounds) : new Rectangle(0, 0, img.getWidth(), img.getHeight());
                if (r.width != img.getWidth() || r.height != img.getHeight()) {
                    BufferedImage t = Codecs.argb(r.width, r.height);
                    Graphics2D g = t.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(img, 0, 0, r.width, r.height, null);
                    g.dispose();
                    img = t;
                }
                pict[0] = Heap.newHandle(PictCodec.encode(img, r.x, r.y));
                return noErr;
            } catch (IOException e) {
                return noDeviceForChannel;
            }
        }
        return noDeviceForChannel;
    }

    /** writes data of a channel into the output, the offset written at is returned in the wide */
    public static int SGWriteExtendedMovieData(int sg, int ref, int p, int len, byte[] offset, int output) {
        SG s = sg(sg);
        Chan c = chan(ref);
        if (s == null || c == null) {
            return s == null ? badComponentInstance : badSGChannel;
        }
        Output o = output(output);
        String path = o != null ? o.path() : s.path;
        if (path == null) {
            return paramErr;
        }
        try {
            long at = MdatWriter.append(path, Heap.read(p, len), 0, len);
            Bytes.putLong(offset, 0, at);
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    // ---- settings

    /** @return the settings of a channel as properties text */
    static byte[] settings(Chan c) {
        Properties p = new Properties();
        p.setProperty("type", Bytes.fourCC(c.type));
        p.setProperty("usage", String.valueOf(c.usage));
        if (c.device != null) {
            p.setProperty("device", c.device);
        }
        if (c.bounds != null) {
            p.setProperty("bounds", c.bounds.x + "," + c.bounds.y + "," + c.bounds.width + "," + c.bounds.height);
        }
        p.setProperty("compressorType", String.valueOf(c.compressorType));
        p.setProperty("depth", String.valueOf(c.depth));
        p.setProperty("spatialQuality", String.valueOf(c.spatialQuality));
        p.setProperty("temporalQuality", String.valueOf(c.temporalQuality));
        p.setProperty("keyFrameRate", String.valueOf(c.keyFrameRate));
        p.setProperty("frameRate", String.valueOf(c.frameRate));
        p.setProperty("volume", String.valueOf(c.volume));
        p.setProperty("sampleRate", String.valueOf(c.sampleRate));
        p.setProperty("sampleSize", String.valueOf(c.sampleSize));
        p.setProperty("numChannels", String.valueOf(c.numChannels));
        p.setProperty("compressionType", String.valueOf(c.compressionType));
        try {
            java.io.StringWriter w = new java.io.StringWriter();
            p.store(w, null);
            return w.toString().getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static void apply(Chan c, byte[] b) {
        Properties p = new Properties();
        try {
            p.load(new java.io.StringReader(new String(b, StandardCharsets.UTF_8)));
        } catch (IOException e) {
            return;
        }
        c.usage = Integer.parseInt(p.getProperty("usage", String.valueOf(c.usage)));
        String device = p.getProperty("device");
        if (device != null) {
            setDevice(c, device);
        }
        String bounds = p.getProperty("bounds");
        if (bounds != null) {
            String[] v = bounds.split(",");
            c.bounds = new Rectangle(Integer.parseInt(v[0]), Integer.parseInt(v[1]), Integer.parseInt(v[2]), Integer.parseInt(v[3]));
        }
        c.compressorType = Integer.parseInt(p.getProperty("compressorType", String.valueOf(c.compressorType)));
        c.depth = Integer.parseInt(p.getProperty("depth", String.valueOf(c.depth)));
        c.spatialQuality = Integer.parseInt(p.getProperty("spatialQuality", String.valueOf(c.spatialQuality)));
        c.temporalQuality = Integer.parseInt(p.getProperty("temporalQuality", String.valueOf(c.temporalQuality)));
        c.keyFrameRate = Integer.parseInt(p.getProperty("keyFrameRate", String.valueOf(c.keyFrameRate)));
        c.frameRate = Integer.parseInt(p.getProperty("frameRate", String.valueOf(c.frameRate)));
        c.volume = Short.parseShort(p.getProperty("volume", String.valueOf(c.volume)));
        c.sampleRate = Integer.parseInt(p.getProperty("sampleRate", String.valueOf(c.sampleRate)));
        c.sampleSize = Integer.parseInt(p.getProperty("sampleSize", String.valueOf(c.sampleSize)));
        c.numChannels = Integer.parseInt(p.getProperty("numChannels", String.valueOf(c.numChannels)));
        c.compressionType = Integer.parseInt(p.getProperty("compressionType", String.valueOf(c.compressionType)));
    }

    static final int sgChannelSettingsType = Bytes.fourCC("sgch");

    public static int SGGetChannelSettings(int sg, int ref, int[] userData, int flags) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        UserDataRec u = new UserDataRec();
        u.add(sgChannelSettingsType, settings(c));
        userData[0] = u.ref;
        return noErr;
    }

    public static int SGSetChannelSettings(int sg, int ref, int userData, int flags) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        UserDataRec u = MovieToolbox.userData(userData);
        UserDataRec.Item i = u.item(sgChannelSettingsType, 1);
        if (i != null) {
            apply(c, i.data);
        }
        return noErr;
    }

    /** an item of each channel's settings */
    public static int SGGetSettings(int sg, int[] userData, int flags) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        UserDataRec u = new UserDataRec();
        for (Chan c : s.channels) {
            u.add(sgChannelSettingsType, settings(c));
        }
        userData[0] = u.ref;
        return noErr;
    }

    /** the channels are made again from the settings */
    public static int SGSetSettings(int sg, int userData, int flags) {
        SG s = sg(sg);
        if (s == null) {
            return badComponentInstance;
        }
        if (s.previewing || s.recording) {
            return cantDoThatInCurrentMode;
        }
        UserDataRec u = MovieToolbox.userData(userData);
        for (Chan c : new ArrayList<>(s.channels)) {
            SGDisposeChannel(sg, c.ref);
        }
        int n = u.count(sgChannelSettingsType);
        for (int k = 1; k <= n; k++) {
            byte[] b = u.item(sgChannelSettingsType, k).data;
            Properties p = new Properties();
            try {
                p.load(new java.io.StringReader(new String(b, StandardCharsets.UTF_8)));
            } catch (IOException e) {
                continue;
            }
            int[] ref = new int[1];
            if (SGNewChannel(sg, Bytes.fourCC(p.getProperty("type", "vide")), ref) == noErr) {
                apply(chan(ref[0]), b);
            }
        }
        return noErr;
    }

    /** a dialog of the device, the compressor and the frame rate, or the device and the rate for sound */
    public static int SGSettingsDialog(int sg, int ref, short numPanels, int[] panelList, int flags, int proc, int procRefNum) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            return noErr;
        }
        return SGDialog.show(c) ? noErr : -128;
    }

    // ---- channels

    public static int SGSetChannelUsage(int ref, int usage) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.usage = usage;
        return noErr;
    }

    public static int SGGetChannelUsage(int ref, int[] usage) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        usage[0] = c.usage;
        return noErr;
    }

    public static int SGSetChannelPlayFlags(int ref, int flags) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.playFlags = flags;
        return noErr;
    }

    public static int SGGetChannelPlayFlags(int ref, int[] flags) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        flags[0] = c.playFlags;
        return noErr;
    }

    public static int SGSetChannelBounds(int ref, byte[] bounds) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.bounds = Imaging.rect(bounds);
        return noErr;
    }

    public static int SGGetChannelBounds(int ref, byte[] bounds) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        Imaging.putRect(bounds, 0, c.bounds != null ? c.bounds : new Rectangle());
        return noErr;
    }

    public static int SGSetChannelClip(int ref, int rgn) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        if (c.clip != 0) {
            Heap.disposeHandle(c.clip);
        }
        c.clip = rgn != 0 ? QuickDrawManager.copyRegion(rgn) : 0;
        return noErr;
    }

    public static int SGGetChannelClip(int ref, int[] rgn) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        rgn[0] = c.clip != 0 ? QuickDrawManager.copyRegion(c.clip) : 0;
        return noErr;
    }

    public static int SGSetChannelMatrix(int ref, byte[] matrix) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.matrix = matrix != null ? MatrixManager.read(matrix) : null;
        return noErr;
    }

    public static int SGGetChannelMatrix(int ref, byte[] matrix) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        int[] m = c.matrix != null ? c.matrix : MatrixManager.identity();
        for (int i = 0; i < 9; i++) {
            Bytes.putInt(matrix, i * 4, m[i]);
        }
        return noErr;
    }

    public static int SGGetChannelTimeScale(int ref, int[] scale) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        scale[0] = c.timeScale();
        return noErr;
    }

    public static int SGSetChannelVolume(int ref, short volume) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.volume = volume;
        return noErr;
    }

    public static int SGGetChannelVolume(int ref, short[] volume) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        volume[0] = c.volume;
        return noErr;
    }

    public static int SGChannelSetDataSourceName(int ref, byte[] name, int scriptTag) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.dataSourceName = name != null ? Bytes.getPString(name, 0) : "";
        c.dataSourceScript = scriptTag;
        return noErr;
    }

    public static int SGChannelGetDataSourceName(int ref, byte[] name, int[] scriptTag) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        Bytes.putPString(name, 0, c.dataSourceName, Math.min(256, name.length));
        scriptTag[0] = c.dataSourceScript;
        return noErr;
    }

    /** the description of what the channel records */
    public static int SGGetChannelSampleDescription(int ref, int desc) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        byte[] d;
        if (c.type == vide) {
            Codecs.Codec k = codec(c);
            Dimension size = c.bounds != null ? c.bounds.getSize() : c.source != null ? c.source.size() : new Dimension();
            d = Codecs.description(k.type, size.width, size.height, k.depth(c.depth != 0 ? c.depth : 24), k.name, 0, c.spatialQuality);
        } else if (c.type == soun) {
            d = Importers.soundDescription(Bytes.fourCC("twos"), c.numChannels, c.sampleSize, (c.sampleRate & 0xffff_ffffL) / 65536.0, true, false, true);
        } else {
            return seqGrabInfoNotAvailable;
        }
        Heap.setHandleBytes(desc, d);
        return noErr;
    }

    /** SGDeviceList: count(2) selectedIndex(2) reserved(4) {name Str63(64) inputs(4) flags(4) refCon(4) reserved(4)}... */
    public static int SGGetChannelDeviceList(int ref, int flags, int[] list) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        List<String> names = deviceNames(c.type);
        byte[] b = new byte[8 + names.size() * 80];
        Bytes.putShort(b, 0, names.size());
        Bytes.putShort(b, 2, Math.max(0, names.indexOf(c.device)));
        for (int i = 0; i < names.size(); i++) {
            Bytes.putPString(b, 8 + i * 80, names.get(i), 64);
        }
        list[0] = Heap.newHandle(b);
        return noErr;
    }

    public static int SGDisposeDeviceList(int sg, int list) {
        if (list != 0) {
            Heap.disposeHandle(list);
        }
        return noErr;
    }

    /** @return true if the device was found */
    static boolean setDevice(Chan c, String name) {
        if (c.type == vide) {
            for (VideoSource v : VideoSource.all()) {
                if (v.name().equals(name)) {
                    if (c.source != null) {
                        c.source.stop();
                    }
                    c.source = v;
                    c.device = name;
                    if (c.digitizer != 0) {
                        Digitizer d = digitizer(c.digitizer);
                        if (d != null) {
                            d.source = v;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (c.type == soun) {
            if (!SoundManager.inputDevices().contains(name)) {
                return false;
            }
            c.device = name;
            c.inputDriver = name;
            return true;
        }
        c.device = name;
        return true;
    }

    public static int SGSetChannelDevice(int ref, byte[] name) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        return setDevice(c, Bytes.getPString(name, 0)) ? noErr : noDeviceForChannel;
    }

    // ---- video channels

    public static int SGSetVideoCompressorType(int ref, int type) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.compressorType = type;
        c.compressor = 0;
        return noErr;
    }

    public static int SGGetVideoCompressorType(int ref, int[] type) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        type[0] = c.compressorType;
        return noErr;
    }

    public static int SGSetVideoCompressor(int ref, short depth, int compressor, int spatialQuality, int temporalQuality, int keyFrameRate) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.depth = depth;
        c.compressor = compressor;
        ComponentManager.ComponentRec r = compressor != 0 ? ComponentManager.component(compressor) : null;
        if (r != null && r.type == ImageCompressionManager.imco) {
            c.compressorType = r.subType;
        }
        c.spatialQuality = spatialQuality;
        c.temporalQuality = temporalQuality;
        c.keyFrameRate = keyFrameRate;
        return noErr;
    }

    public static int SGGetVideoCompressor(int ref, short[] depth, int[] compressor, int[] spatialQuality, int[] temporalQuality, int[] keyFrameRate) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        depth[0] = (short) c.depth;
        if (c.compressor != 0) {
            compressor[0] = c.compressor;
        } else {
            ComponentManager.ComponentRec r = ComponentManager.find(ImageCompressionManager.imco, codec(c).type);
            compressor[0] = r != null ? r.ref : 0;
        }
        spatialQuality[0] = c.spatialQuality;
        temporalQuality[0] = c.temporalQuality;
        keyFrameRate[0] = c.keyFrameRate;
        return noErr;
    }

    public static int SGSetFrameRate(int ref, int frameRate) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.frameRate = frameRate;
        return noErr;
    }

    public static int SGGetFrameRate(int ref, int[] frameRate) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        frameRate[0] = c.frameRate;
        return noErr;
    }

    public static int SGSetVideoRect(int ref, byte[] rect) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.videoRect = Imaging.rect(rect);
        return noErr;
    }

    public static int SGGetVideoRect(int ref, byte[] rect) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        Imaging.putRect(rect, 0, c.videoRect != null ? c.videoRect : c.source != null ? new Rectangle(c.source.size()) : new Rectangle());
        return noErr;
    }

    public static int SGGetSrcVideoBounds(int ref, byte[] rect) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        if (c.source == null) {
            return noDeviceForChannel;
        }
        Imaging.putRect(rect, 0, new Rectangle(c.source.size()));
        return noErr;
    }

    /** the digitizer instance of the channel's source */
    public static int SGGetVideoDigitizerComponent(int ref) {
        Chan c = chan(ref);
        if (c == null || c.source == null) {
            return 0;
        }
        if (c.digitizer == 0 || digitizer(c.digitizer) == null) {
            ComponentManager.ComponentRec r = ComponentManager.find(vdig, c.source.subType());
            if (r == null) {
                return 0;
            }
            c.digitizer = ComponentManager.open(r);
        }
        digitizer(c.digitizer).source = c.source;
        return c.digitizer;
    }

    public static int SGSetVideoDigitizerComponent(int ref, int vdigRef) {
        Chan c = chan(ref);
        Digitizer d = digitizer(vdigRef);
        if (c == null) {
            return badSGChannel;
        }
        if (d == null || d.source == null) {
            return noDeviceForChannel;
        }
        if (c.source != null && c.source != d.source) {
            c.source.stop();
        }
        c.digitizer = vdigRef;
        c.source = d.source;
        c.device = d.source.name();
        return noErr;
    }

    public static int SGVideoDigitizerChanged(int ref) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        if (c.digitizer != 0) {
            Digitizer d = digitizer(c.digitizer);
            if (d != null && d.source != null) {
                c.source = d.source;
                c.device = d.source.name();
            }
        }
        return noErr;
    }

    // ---- sound channels

    public static int SGSetSoundInputParameters(int ref, short sampleSize, short numChannels, int compressionType) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        if (sampleSize != 8 && sampleSize != 16 && sampleSize != 24 || numChannels < 1 || numChannels > 2) {
            return deviceCantMeetRequest;
        }
        c.sampleSize = sampleSize;
        c.numChannels = numChannels;
        c.compressionType = compressionType;
        return noErr;
    }

    public static int SGGetSoundInputParameters(int ref, short[] sampleSize, short[] numChannels, int[] compressionType) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        sampleSize[0] = (short) c.sampleSize;
        numChannels[0] = (short) c.numChannels;
        compressionType[0] = c.compressionType;
        return noErr;
    }

    public static int SGSetSoundInputRate(int ref, int rate) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.sampleRate = rate;
        return noErr;
    }

    public static int SGGetSoundInputRate(int ref) {
        Chan c = chan(ref);
        return c == null ? 0 : c.sampleRate;
    }

    public static int SGSetSoundRecordChunkSize(int ref, int seconds) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.chunkSize = seconds;
        return noErr;
    }

    public static int SGGetSoundRecordChunkSize(int ref) {
        Chan c = chan(ref);
        return c == null ? 0 : c.chunkSize;
    }

    public static int SGSetSoundInputDriver(int ref, byte[] name) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        return setDevice(c, Bytes.getPString(name, 0)) ? noErr : noDeviceForChannel;
    }

    /** the sound input device of the channel, opened for the channel */
    public static int SGGetSoundInputDriver(int ref) {
        Chan c = chan(ref);
        if (c == null || c.inputDriver == null) {
            return 0;
        }
        if (c.spbDevice == 0 || SoundManager.device(c.spbDevice) == null) {
            int[] r = new int[1];
            byte[] name = new byte[256];
            Bytes.putPString(name, 0, c.inputDriver, 256);
            if (SoundManager.SPBOpenDevice(name, (short) 1, r) != 0) {
                return 0;
            }
            c.spbDevice = r[0];
        }
        return c.spbDevice;
    }

    public static int SGSoundInputDriverChanged(int ref) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.spbDevice = 0;
        return noErr;
    }

    // ---- text and music channels

    public static int SGSetFontName(int ref, byte[] name) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.fontName = Bytes.getPString(name, 0);
        return noErr;
    }

    public static int SGSetFontSize(int ref, short size) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.fontSize = size;
        return noErr;
    }

    public static int SGSetTextForeColor(int ref, byte[] color) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.foreColor = color.clone();
        return noErr;
    }

    public static int SGSetTextBackColor(int ref, byte[] color) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.backColor = color.clone();
        return noErr;
    }

    public static int SGSetJustification(int ref, short justification) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.justification = justification;
        return noErr;
    }

    public static int SGSetTextReturnToSpaceValue(int ref, short value) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.returnToSpace = value;
        return noErr;
    }

    public static int SGGetTextReturnToSpaceValue(int ref, short[] value) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        value[0] = (short) c.returnToSpace;
        return noErr;
    }

    public static int SGSetInstrument(int ref, byte[] tone) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        c.instrument = tone.clone();
        return noErr;
    }

    public static int SGGetInstrument(int ref, byte[] tone) {
        Chan c = chan(ref);
        if (c == null) {
            return badSGChannel;
        }
        if (c.instrument == null) {
            return seqGrabInfoNotAvailable;
        }
        System.arraycopy(c.instrument, 0, tone, 0, Math.min(tone.length, c.instrument.length));
        return noErr;
    }

    // ---- video digitizers

    public static int VDGetNumberOfInputs(int vd, short[] inputs) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        // the number of the last input
        inputs[0] = (short) Math.max(0, (d.source != null ? d.source.inputs().size() : 1) - 1);
        return noErr;
    }

    public static int VDSetInput(int vd, short input) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        int n = d.source != null ? d.source.inputs().size() : 1;
        if (input < 0 || input >= n) {
            return qtParamErr;
        }
        d.input = input;
        return noErr;
    }

    public static int VDGetInput(int vd, short[] input) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        input[0] = (short) d.input;
        return noErr;
    }

    /** all inputs are component video (rgbComponentIn) */
    public static int VDGetInputFormat(int vd, short input, short[] format) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        format[0] = 2;
        return noErr;
    }

    public static int VDSetInputStandard(int vd, short standard) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        d.standard = standard;
        return noErr;
    }

    public static int VDSetClipRgn(int vd, int rgn) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        Area a = d.clipRgn != 0 ? Regions.read(d.clipRgn) : new Area();
        a.add(Regions.read(rgn));
        if (d.clipRgn == 0) {
            d.clipRgn = QuickDrawManager.copyRegion(rgn);
        }
        Regions.write(d.clipRgn, a);
        return noErr;
    }

    public static int VDClearClipRgn(int vd, int rgn) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        if (d.clipRgn != 0) {
            Area a = Regions.read(d.clipRgn);
            a.subtract(Regions.read(rgn));
            Regions.write(d.clipRgn, a);
        }
        return noErr;
    }

    public static int VDSetClipState(int vd, short state) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        d.clipState = state != 0;
        return noErr;
    }

    public static int VDGetClipState(int vd, short[] state) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        state[0] = (short) (d.clipState ? 1 : 0);
        return noErr;
    }

    /**
     * DigitizerInfo: vdigType(2) inputCapabilityFlags(4) outputCapabilityFlags(4) inputCurrentFlags(4) outputCurrentFlags(4) slot(2) gdh(4)
     * maskgdh(4) minDestHeight(2) minDestWidth(2) maxDestHeight(2) maxDestWidth(2) blendLevels(2) reserved(4)
     */
    public static int VDGetDigitizerInfo(int vd, byte[] info) {
        Digitizer d = digitizer(vd);
        if (d == null) {
            return badComponentInstance;
        }
        java.util.Arrays.fill(info, (byte) 0);
        Dimension size = d.source != null ? d.source.size() : new Dimension();
        Bytes.putShort(info, 0, 2); // vdTypeBasic
        int inputFlags = 1 << 6 | 1 << 8; // digiInDoesColor, digiInDoesComponent
        int outputFlags = 1 << 3 | 1 << 4 | 1 << 5 | 1 << 17 | 1 << 18; // digiOutDoes16, 32, Dither, Stretch, Shrink
        Bytes.putInt(info, 2, inputFlags);
        Bytes.putInt(info, 6, outputFlags);
        Bytes.putInt(info, 10, inputFlags);
        Bytes.putInt(info, 14, outputFlags);
        Bytes.putShort(info, 28, 1);
        Bytes.putShort(info, 30, 1);
        Bytes.putShort(info, 32, size.height);
        Bytes.putShort(info, 34, size.width);
        Bytes.putShort(info, 36, 0);
        return noErr;
    }
}
