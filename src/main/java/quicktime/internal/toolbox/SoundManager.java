/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;


/**
 * The Sound Manager in Java: sound channels playing commands and 'snd ' resources with Java Sound,
 * sound and AIFF headers, sound description extensions and sound input (SPB) recording.
 */
public final class SoundManager {

    private static final Logger logger = Logger.getLogger(SoundManager.class.getName());

    private SoundManager() {
    }

    static final short noErr = 0;
    static final short paramErr = -50;
    static final short ioErr = -36;
    static final short eofErr = -39;
    static final short rfNumErr = -51;
    static final short badChannel = -205;
    static final short badFormat = -206;
    static final short queueFull = -203;
    static final short resProblem = -204;
    static final short notEnoughHardwareErr = -201;
    static final short siNoSoundInHardware = -220;
    static final short siBadSoundInDevice = -221;
    static final short siNoBufferSpecified = -222;
    static final short siInvalidCompression = -223;
    static final short siInvalidSampleRate = -225;
    static final short siInvalidSampleSize = -226;
    static final short siDeviceBusyErr = -227;
    static final short siBadRefNum = -229;
    static final short siUnknownInfoType = -231;
    static final short codecExtensionNotFoundErr = -8971;

    // commands
    static final int nullCmd = 0;
    static final int quietCmd = 3;
    static final int flushCmd = 4;
    static final int reInitCmd = 5;
    static final int waitCmd = 10;
    static final int pauseCmd = 11;
    static final int resumeCmd = 12;
    static final int callBackCmd = 13;
    static final int syncCmd = 14;
    static final int availableCmd = 24;
    static final int versionCmd = 25;
    static final int freqDurationCmd = 40;
    static final int restCmd = 41;
    static final int freqCmd = 42;
    static final int ampCmd = 43;
    static final int getAmpCmd = 45;
    static final int volumeCmd = 46;
    static final int getVolumeCmd = 47;
    static final int clockComponentCmd = 50;
    static final int getClockComponentCmd = 51;
    static final int scheduledSoundCmd = 52;
    static final int soundCmd = 80;
    static final int bufferCmd = 81;
    static final int rateCmd = 82;
    static final int getRateCmd = 85;
    static final int rateMultiplierCmd = 86;
    static final int getRateMultiplierCmd = 87;
    static final int dataOffsetFlag = 0x8000;

    // sound header encodings
    static final int stdSH = 0x00;
    static final int extSH = 0xff;
    static final int cmpSH = 0xfe;

    static final int sampledSynth = 5;
    static final int initStereo = 0xc0;

    static final int kSoundNotCompressed = Bytes.fourCC("NONE");
    static final int k8BitOffsetBinaryFormat = Bytes.fourCC("raw ");
    static final int k16BitBigEndianFormat = Bytes.fourCC("twos");

    // info selectors
    static final int siSampleRate = Bytes.fourCC("srat");
    static final int siSampleSize = Bytes.fourCC("ssiz");
    static final int siNumberChannels = Bytes.fourCC("chan");
    static final int siCompressionType = Bytes.fourCC("comp");
    static final int siContinuous = Bytes.fourCC("cont");
    static final int siLevelMeterOnOff = Bytes.fourCC("lmet");
    static final int siActiveLevels = Bytes.fourCC("lmac");
    static final int siInputGain = Bytes.fourCC("gain");
    static final int siDeviceName = Bytes.fourCC("name");
    static final int siDeviceIcon = Bytes.fourCC("icon");
    static final int siAsync = Bytes.fourCC("asyn");
    static final int siActiveChannels = Bytes.fourCC("chac");
    static final int siSampleRateAvailable = Bytes.fourCC("srav");
    static final int siSampleSizeAvailable = Bytes.fourCC("ssav");
    static final int siChannelAvailable = Bytes.fourCC("chav");
    static final int siCompressionAvailable = Bytes.fourCC("cmav");
    static final int siTwosComplementOnOff = Bytes.fourCC("twos");
    static final int siRecordingQuality = Bytes.fourCC("qual");
    static final int siPlayThruOnOff = Bytes.fourCC("plth");
    static final int siSpeakerMute = Bytes.fourCC("smut");
    static final int siVolume = Bytes.fourCC("volu");
    static final int siClock = Bytes.fourCC("sclk");
    static final int siEQSpectrumBands = Bytes.fourCC("eqsb");
    static final int siEQSpectrumLevels = Bytes.fourCC("eqlv");
    static final int siEQToneControlOnOff = Bytes.fourCC("eqtc");
    static final int siEQToneControlGain = Bytes.fourCC("eqtg");

    // ---- sound data

    /** decoded sound: interleaved samples */
    record Sound(float[] samples, int channels, double rate, int baseNote) {
        int frames() {
            return samples.length / channels;
        }
    }

    /** @return a version 0 sound description for the decoders */
    static byte[] description(int format, int channels, int bits, double rate) {
        byte[] d = new byte[36];
        Bytes.putInt(d, 0, 36);
        Bytes.putInt(d, 4, format == kSoundNotCompressed ? (bits == 8 ? k8BitOffsetBinaryFormat : k16BitBigEndianFormat) : format);
        Bytes.putShort(d, 24, channels);
        Bytes.putShort(d, 26, bits);
        Bytes.putInt(d, 32, (int) Math.round(rate * 65536.0));
        return d;
    }

    /** @return the size of a sound header of the encoding */
    static int headerSize(int encode) {
        return encode == stdSH ? 22 : 64;
    }

    /** @return the sound of a sound header at the address */
    static Sound soundHeader(int p) throws IOException {
        int samplePtr = Heap.getInt(p);
        int encode = Heap.getByte(p + 20) & 0xff;
        int baseNote = Heap.getByte(p + 21) & 0xff;
        double rate = (Heap.getInt(p + 8) & 0xffff_ffffL) / 65536.0;
        int data = samplePtr != 0 ? samplePtr : p + headerSize(encode);
        int channels, bits, format, bytes;
        long frames;
        switch (encode) {
            case stdSH -> {
                channels = 1;
                bits = 8;
                format = k8BitOffsetBinaryFormat;
                frames = Heap.getInt(p + 4) & 0xffff_ffffL;
                bytes = (int) frames;
            }
            case extSH -> {
                channels = Math.max(1, Heap.getInt(p + 4));
                frames = Heap.getInt(p + 22) & 0xffff_ffffL;
                bits = Math.max(8, Heap.getShort(p + 48));
                format = bits == 8 ? k8BitOffsetBinaryFormat : k16BitBigEndianFormat;
                bytes = (int) (frames * channels * ((bits + 7) / 8));
            }
            case cmpSH -> {
                channels = Math.max(1, Heap.getInt(p + 4));
                frames = Heap.getInt(p + 22) & 0xffff_ffffL;
                format = Heap.getInt(p + 40);
                bits = Math.max(8, Heap.getShort(p + 62));
                if (format == 0 || format == kSoundNotCompressed) {
                    format = bits == 8 ? k8BitOffsetBinaryFormat : k16BitBigEndianFormat;
                }
                long[] fb = framesAndBytes(format, channels, bits);
                bytes = fb == null ? 0 : (int) ((frames + fb[0] - 1) / fb[0] * fb[1]);
            }
            default -> throw new IOException("bad sound header encoding: " + encode);
        }
        Audio.Decoder dec = Audio.decoder(description(format, channels, bits, rate));
        if (dec == null) {
            throw new IOException("unsupported sound format: " + Bytes.fourCC(format));
        }
        if (frames == 0 && samplePtr == 0) {
            // no length given, the data up to the end of the handle
            bytes = Heap.remaining(data) / dec.bytesPerPacket * dec.bytesPerPacket;
            frames = (long) bytes / dec.bytesPerPacket * dec.framesPerPacket;
        }
        bytes = Math.min(bytes, Heap.remaining(data));
        float[] s = dec.decode(Heap.read(data, bytes));
        if (s.length > frames * channels) {
            s = Arrays.copyOf(s, (int) (frames * channels));
        }
        return new Sound(s, channels, rate, baseNote == 0 ? 60 : baseNote);
    }

    /** @return frames per packet and bytes per packet of all channels, null if unknown */
    static long[] framesAndBytes(int format, int channels, int bits) {
        if (format == Audio.ima4) {
            return new long[] {64, 34L * channels};
        }
        if (format == Audio.ulaw || format == Audio.alaw) {
            return new long[] {1, channels};
        }
        String f = Bytes.fourCC(format);
        return switch (f) {
            case "raw ", "twos", "sowt", "NONE", "lpcm" -> new long[] {1, (long) channels * ((bits + 7) / 8)};
            case "in24" -> new long[] {1, 3L * channels};
            case "in32", "fl32" -> new long[] {1, 4L * channels};
            case "fl64" -> new long[] {1, 8L * channels};
            default -> null;
        };
    }

    /** @return the offset of the first sound header of a 'snd ' resource */
    static int soundHeaderOffset(byte[] r) throws IOException {
        if (r.length < 4) {
            throw new IOException("short snd resource");
        }
        int format = Bytes.getShort(r, 0);
        int p;
        if (format == 1) {
            int modifiers = Bytes.getShort(r, 2);
            p = 4 + modifiers * 6;
        } else if (format == 2) {
            p = 4;
        } else {
            throw new IOException("bad snd resource format: " + format);
        }
        int commands = Bytes.getShort(r, p);
        p += 2;
        for (int i = 0; i < commands; i++) {
            int cmd = Bytes.getShort(r, p + i * 8) & 0xffff;
            if ((cmd & 0x7fff) == bufferCmd || (cmd & 0x7fff) == soundCmd) {
                return Bytes.getInt(r, p + i * 8 + 4);
            }
        }
        return p + commands * 8;
    }

    /** @return the commands of a 'snd ' resource */
    static List<byte[]> resourceCommands(byte[] r) {
        List<byte[]> l = new ArrayList<>();
        int format = Bytes.getShort(r, 0);
        int p = format == 1 ? 4 + Bytes.getShort(r, 2) * 6 : 4;
        int commands = Bytes.getShort(r, p);
        p += 2;
        for (int i = 0; i < commands && p + i * 8 + 8 <= r.length; i++) {
            l.add(Arrays.copyOfRange(r, p + i * 8, p + i * 8 + 8));
        }
        return l;
    }

    // ---- channels

    /** a sound channel, playing its queue on a thread */
    static final class Channel implements Runnable {
        final int ref;
        final int synth;
        final int init;
        final int userRoutine;
        final LinkedBlockingDeque<byte[]> queue = new LinkedBlockingDeque<>(128);
        final Thread thread;
        volatile boolean disposed;
        volatile boolean paused;
        volatile boolean busy;
        /** commands queued or being executed */
        final java.util.concurrent.atomic.AtomicInteger pending = new java.util.concurrent.atomic.AtomicInteger();
        volatile boolean quiet;
        /** 0x0100 is full, left and right */
        volatile int volume = 0x0100_0100;
        volatile int amplitude = 255;
        volatile double rateMultiplier = 1;
        volatile double rate = 1;
        volatile boolean levelMeter;
        volatile int[] levels = {0, 0};
        int[] eqBands = new int[0];
        boolean toneControl;
        int toneGain;
        int clock;
        boolean clockEnabled;
        /** the installed sound of the sampled synthesizer */
        Sound installed;
        // status, in seconds
        volatile double start;
        volatile double end;
        volatile double current;
        SoundPlayer.Sink sink;
        /** the resource being played, kept for offset commands */
        byte[] resource;
        int resourceHandle;

        Channel(int synth, int init, int userRoutine) {
            this.ref = Heap.newObject(this, 4);
            this.synth = synth;
            this.init = init;
            this.userRoutine = userRoutine;
            this.thread = new Thread(this, "Sound channel");
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void run() {
            try {
                while (!disposed) {
                    byte[] cmd = queue.poll(50, TimeUnit.MILLISECONDS);
                    if (cmd == null) {
                        busy = false;
                        continue;
                    }
                    busy = true;
                    quiet = false;
                    try {
                        execute(this, cmd, false);
                    } finally {
                        pending.decrementAndGet();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                busy = false;
                if (sink != null) {
                    sink.close();
                    sink = null;
                }
            }
        }

        /** plays the sound, pacing as the output does, @return false if quieted */
        boolean play(Sound s, double pitch, double seconds) throws InterruptedException {
            if (sink == null) {
                sink = SoundPlayer.sinks.get();
                if (sink == null) {
                    sink = new PacedSink();
                }
            }
            double step = s.rate() * pitch * rate * rateMultiplier / SoundPlayer.RATE;
            int frames = s.frames();
            long total = seconds > 0 ? (long) (seconds * SoundPlayer.RATE) : (long) (frames / step);
            start = current;
            end = start + total / SoundPlayer.RATE;
            byte[] out = new byte[SoundPlayer.CHUNK * 4];
            double p = 0;
            for (long done = 0; done < total; ) {
                while (paused && !quiet && !disposed) {
                    Thread.sleep(10);
                }
                if (quiet || disposed) {
                    sink.flush();
                    return false;
                }
                int n = (int) Math.min(SoundPlayer.CHUNK, total - done);
                float gl = (volume & 0xffff) / 256f * amplitude / 255f, gr = (volume >>> 16) / 256f * amplitude / 255f;
                int peakL = 0, peakR = 0;
                for (int i = 0; i < n; i++, p += step) {
                    int k = (int) p;
                    if (seconds > 0 && k >= frames) {
                        // a note longer than the sound repeats it
                        p -= frames;
                        k = (int) p;
                    }
                    float l = 0, r = 0;
                    if (k < frames) {
                        int c = s.channels();
                        l = s.samples()[k * c];
                        r = s.samples()[k * c + (c > 1 ? 1 : 0)];
                    }
                    int sl = clamp(l * gl), sr = clamp(r * gr);
                    peakL = Math.max(peakL, Math.abs(sl));
                    peakR = Math.max(peakR, Math.abs(sr));
                    out[i * 4] = (byte) sl;
                    out[i * 4 + 1] = (byte) (sl >> 8);
                    out[i * 4 + 2] = (byte) sr;
                    out[i * 4 + 3] = (byte) (sr >> 8);
                }
                if (levelMeter) {
                    levels = new int[] {peakL >> 7, peakR >> 7};
                }
                sink.write(out, n * 4);
                done += n;
                current = start + done / SoundPlayer.RATE;
            }
            return true;
        }

        void rest(double seconds) throws InterruptedException {
            long until = System.nanoTime() + (long) (seconds * 1e9);
            while (System.nanoTime() < until && !quiet && !disposed) {
                Thread.sleep(Math.max(1, Math.min(10, (until - System.nanoTime()) / 1_000_000)));
            }
            current += seconds;
        }
    }

    private static int clamp(float v) {
        return Math.max(-32768, Math.min(32767, Math.round(v * 32767)));
    }

    /** an output without a device, taking the time the sound would take */
    static final class PacedSink implements SoundPlayer.Sink {
        long playedUntil = System.nanoTime();

        @Override
        public void write(byte[] b, int length) {
            long now = System.nanoTime();
            playedUntil = Math.max(playedUntil, now) + (long) (length / 4 / SoundPlayer.RATE * 1e9);
            long ahead = playedUntil - now - 50_000_000L;
            if (ahead > 0) {
                try {
                    Thread.sleep(ahead / 1_000_000, (int) (ahead % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        @Override
        public double latency() {
            return Math.max(0, (playedUntil - System.nanoTime()) / 1e9);
        }

        @Override
        public void flush() {
            playedUntil = System.nanoTime();
        }

        @Override
        public void close() {
        }
    }

    private static final List<Channel> channels = new ArrayList<>();

    static Channel channel(int ref) {
        return Heap.peerOrNull(ref, Channel.class);
    }

    /** the session ends */
    static void disposeAll() {
        List<Channel> all;
        synchronized (channels) {
            all = new ArrayList<>(channels);
            channels.clear();
        }
        for (Channel c : all) {
            dispose(c, true);
        }
        List<Device> ds;
        synchronized (devices) {
            ds = new ArrayList<>(devices.values());
            devices.clear();
        }
        for (Device d : ds) {
            d.stop = true;
        }
    }

    private static void dispose(Channel c, boolean quietNow) {
        if (quietNow) {
            c.quiet = true;
            clear(c);
        } else {
            await(c);
        }
        c.disposed = true;
        try {
            c.thread.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (c.resourceHandle != 0) {
            c.resourceHandle = 0;
        }
    }

    private static boolean enqueue(Channel c, byte[] cmd, boolean first) {
        c.pending.incrementAndGet();
        boolean ok = first ? c.queue.offerFirst(cmd) : c.queue.offerLast(cmd);
        if (!ok) {
            c.pending.decrementAndGet();
        }
        return ok;
    }

    private static void clear(Channel c) {
        List<byte[]> dropped = new ArrayList<>();
        c.queue.drainTo(dropped);
        c.pending.addAndGet(-dropped.size());
    }

    /** waits until the channel did all its commands */
    private static void await(Channel c) {
        while (c.pending.get() > 0 && !c.disposed) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /** @return the frequency of a note, MIDI numbers, 60 is middle C */
    static double frequency(int note) {
        return 440 * Math.pow(2, (note - 69) / 12.0);
    }

    /** @return a sound of a square wave */
    static Sound tone(int note) {
        double f = frequency(note);
        int period = Math.max(2, (int) Math.round(SoundPlayer.RATE / f));
        float[] s = new float[period];
        for (int i = 0; i < period; i++) {
            s[i] = i < period / 2 ? 0.25f : -0.25f;
        }
        return new Sound(s, 1, SoundPlayer.RATE, note);
    }

    /** @return the address of a command's data, offsets are into the resource being played */
    private static int address(Channel c, int cmd, int param2) {
        if ((cmd & dataOffsetFlag) != 0 && c.resourceHandle != 0) {
            return Heap.deref(c.resourceHandle) + param2;
        }
        return param2;
    }

    /** executes a command, getters write their results into the command */
    static short execute(Channel c, byte[] cmd, boolean immediate) throws InterruptedException {
        int op = Bytes.getShort(cmd, 0) & 0xffff;
        int param1 = Bytes.getShort(cmd, 2);
        int param2 = Bytes.getInt(cmd, 4);
        try {
            switch (op & 0x7fff) {
                case nullCmd, syncCmd, availableCmd, reInitCmd -> {
                }
                case quietCmd -> {
                    c.quiet = true;
                    if (c.sink != null) {
                        c.sink.flush();
                    }
                }
                case flushCmd -> clear(c);
                case waitCmd -> c.rest(param1 / 2000.0);
                case restCmd -> c.rest(param1 / 2000.0);
                case pauseCmd -> c.paused = true;
                case resumeCmd -> c.paused = false;
                case callBackCmd -> {
                    if (c.userRoutine != 0) {
                        int p = Heap.newPtr(8);
                        Heap.write(p, cmd, 0, 8);
                        try {
                            Closures.call(c.userRoutine, c.ref, p);
                        } finally {
                            Heap.free(p);
                        }
                    }
                }
                case versionCmd -> Bytes.putInt(cmd, 4, 0x0003_0000);
                case volumeCmd -> c.volume = param2;
                case getVolumeCmd -> Heap.putInt(param2, c.volume);
                case ampCmd -> c.amplitude = Math.max(0, Math.min(255, param1));
                case getAmpCmd -> Heap.putShort(param2, (short) c.amplitude);
                case rateMultiplierCmd -> c.rateMultiplier = (param2 & 0xffff_ffffL) / 65536.0;
                case getRateMultiplierCmd -> Heap.putInt(param2, (int) Math.round(c.rateMultiplier * 65536));
                case rateCmd -> c.rate = (param2 & 0xffff_ffffL) / 65536.0;
                case getRateCmd -> Heap.putInt(param2, (int) Math.round(c.rate * 65536));
                case clockComponentCmd -> c.clock = param2;
                case getClockComponentCmd -> Heap.putInt(param2, clock(c));
                case soundCmd -> c.installed = soundHeader(address(c, op, param2));
                case bufferCmd, scheduledSoundCmd -> c.play(soundHeader(address(c, op, param2)), 1, 0);
                case freqDurationCmd, freqCmd -> {
                    int note = param2 & 0xff;
                    double seconds = op == freqCmd ? 1 : param1 / 2000.0;
                    if (c.installed != null) {
                        c.play(c.installed, frequency(note) / frequency(c.installed.baseNote()), seconds);
                    } else {
                        c.play(tone(note), 1, seconds);
                    }
                }
                default -> {
                    return badFormat;
                }
            }
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "sound command " + op, e);
            return badFormat;
        }
    }

    /** @return a clock component instance of the channel */
    private static int clock(Channel c) {
        if (c.clock == 0) {
            int[] ci = new int[1];
            if (ComponentManager.OpenADefaultComponent(ComponentManager.clok, Bytes.fourCC("micr"), ci) == noErr) {
                c.clock = ci[0];
            }
        }
        return c.clock;
    }

    public static short SndNewChannel(int[] chan, short synth, int init, int userRoutine) {
        Channel c = new Channel(synth, init, userRoutine);
        synchronized (channels) {
            channels.add(c);
        }
        chan[0] = c.ref;
        return noErr;
    }

    public static short SndDisposeChannel(int chan, byte quietNow) {
        Channel c = channel(chan);
        if (c == null) {
            return badChannel;
        }
        synchronized (channels) {
            channels.remove(c);
        }
        dispose(c, quietNow != 0);
        Heap.free(chan);
        return noErr;
    }

    /** the command is a SndCommand: cmd(2) param1(2) param2(4) */
    public static short SndDoCommand(int chan, int cmd, byte noWait) {
        Channel c = channel(chan);
        if (c == null) {
            return badChannel;
        }
        byte[] b = Heap.read(cmd, 8);
        if (noWait != 0) {
            return enqueue(c, b, false) ? noErr : queueFull;
        }
        try {
            c.pending.incrementAndGet();
            c.queue.putLast(b);
            return noErr;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return queueFull;
        }
    }

    /** runs the command at once, playing commands are queued in front */
    public static short SndDoImmediate(int chan, int cmd) {
        Channel c = channel(chan);
        if (c == null) {
            return badChannel;
        }
        byte[] b = Heap.read(cmd, 8);
        int op = Bytes.getShort(b, 0) & 0x7fff;
        if (op == bufferCmd || op == freqDurationCmd || op == freqCmd || op == scheduledSoundCmd || op == waitCmd || op == restCmd) {
            c.quiet = true;
            return enqueue(c, b, true) ? noErr : queueFull;
        }
        try {
            short e = execute(c, b, true);
            Heap.write(cmd, b, 0, 8);
            return e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return badChannel;
        }
    }

    /** plays a 'snd ' resource handle, on a channel of its own if chan is 0 */
    public static short SndPlay(int chan, int sndHandle, byte async) {
        byte[] r = Heap.handleBytes(sndHandle);
        List<byte[]> commands;
        try {
            soundHeaderOffset(r);
            commands = resourceCommands(r);
        } catch (IOException | RuntimeException e) {
            return badFormat;
        }
        boolean own = chan == 0;
        Channel c;
        if (own) {
            int[] ch = new int[1];
            SndNewChannel(ch, (short) sampledSynth, 0, 0);
            c = channel(ch[0]);
        } else {
            c = channel(chan);
            if (c == null) {
                return badChannel;
            }
        }
        c.resourceHandle = sndHandle;
        for (byte[] cmd : commands) {
            int op = Bytes.getShort(cmd, 0) & 0xffff;
            if ((op & 0x7fff) == soundCmd) {
                // the resource's sound is played
                Bytes.putShort(cmd, 0, (op & dataOffsetFlag) | bufferCmd);
            }
            try {
                c.pending.incrementAndGet();
                c.queue.putLast(cmd);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return queueFull;
            }
        }
        if (own || async == 0) {
            await(c);
        }
        if (own) {
            SndDisposeChannel(c.ref, (byte) 0);
        }
        return noErr;
    }

    /** SCStatus: startTime(4) endTime(4) currentTime(4) busy(1) disposed(1) paused(1) unused(1) attributes(4) cpuLoad(4), times are Fixed seconds */
    public static short SndChannelStatus(int chan, short length, byte[] status) {
        Channel c = channel(chan);
        if (c == null) {
            return badChannel;
        }
        byte[] s = new byte[24];
        Bytes.putInt(s, 0, Bytes.doubleToFixed(c.start));
        Bytes.putInt(s, 4, Bytes.doubleToFixed(c.end));
        Bytes.putInt(s, 8, Bytes.doubleToFixed(c.current));
        s[12] = (byte) (c.pending.get() > 0 ? 1 : 0);
        s[13] = (byte) (c.disposed ? 1 : 0);
        s[14] = (byte) (c.paused ? 1 : 0);
        Bytes.putInt(s, 16, c.init);
        System.arraycopy(s, 0, status, 0, Math.min(Math.min(length, 24), status.length));
        return noErr;
    }

    private static short getInfo(int chan, int selector, Object out) {
        Channel c = channel(chan);
        if (c == null) {
            return badChannel;
        }
        byte[] v;
        if (selector == siLevelMeterOnOff || selector == siEQToneControlOnOff) {
            v = new byte[] {(byte) ((selector == siLevelMeterOnOff ? c.levelMeter : c.toneControl) ? 1 : 0)};
        } else if (selector == siActiveLevels) {
            int[] l = c.levels;
            v = new byte[4];
            Bytes.putShort(v, 0, 2);
            v[2] = (byte) Math.min(255, l[0]);
            v[3] = (byte) Math.min(255, l[1]);
        } else if (selector == siClock) {
            v = new byte[4];
            Bytes.putInt(v, 0, clock(c));
        } else if (selector == siVolume) {
            v = new byte[4];
            Bytes.putInt(v, 0, c.volume);
        } else if (selector == siEQToneControlGain) {
            v = new byte[4];
            Bytes.putInt(v, 0, c.toneGain);
        } else if (selector == siEQSpectrumBands) {
            // count(2) ptr(4): the frequencies are written to the pointer
            if (out instanceof byte[] b && b.length >= 6) {
                int count = Bytes.getShort(b, 0);
                int ptr = Bytes.getInt(b, 2);
                for (int i = 0; i < count && i < c.eqBands.length && ptr != 0; i++) {
                    Heap.putInt(ptr + i * 4, c.eqBands[i] << 16);
                }
            }
            return noErr;
        } else if (selector == siEQSpectrumLevels) {
            int level = Math.max(c.levels[0], c.levels[1]);
            if (out instanceof byte[] b) {
                Arrays.fill(b, (byte) Math.min(255, level));
            }
            return noErr;
        } else {
            return siUnknownInfoType;
        }
        if (out instanceof Integer p) {
            Heap.write(p, v, 0, v.length);
        } else {
            Bytes.toArray(v, 0, out, 0, Math.min(v.length, Imaging.capacity(out)));
        }
        return noErr;
    }

    public static short SndGetInfo(int chan, int selector, byte[] info) {
        return getInfo(chan, selector, info);
    }

    public static short SndGetInfo(int chan, int selector, int[] info) {
        return getInfo(chan, selector, info);
    }

    private static short setInfo(int chan, int selector, int value, byte[] data) {
        Channel c = channel(chan);
        if (c == null) {
            return badChannel;
        }
        if (selector == siLevelMeterOnOff) {
            c.levelMeter = value != 0;
        } else if (selector == siEQToneControlOnOff) {
            c.toneControl = value != 0;
        } else if (selector == siClock) {
            c.clockEnabled = value != 0;
        } else if (selector == siVolume) {
            c.volume = value;
        } else if (selector == siEQToneControlGain) {
            c.toneGain = value;
        } else if (selector == siEQSpectrumBands && data != null && data.length >= 6) {
            int count = Bytes.getShort(data, 0);
            int ptr = Bytes.getInt(data, 2);
            int[] bands = new int[Math.max(0, count)];
            for (int i = 0; i < bands.length && ptr != 0; i++) {
                bands[i] = Heap.getInt(ptr + i * 4) >>> 16;
            }
            c.eqBands = bands;
        } else {
            return siUnknownInfoType;
        }
        return noErr;
    }

    public static short SndSetInfo(int chan, int selector, byte value) {
        return setInfo(chan, selector, value, null);
    }

    public static short SndSetInfo(int chan, int selector, byte[] info) {
        return setInfo(chan, selector, info.length >= 4 ? Bytes.getInt(info, 0) : info.length > 0 ? info[0] : 0, info);
    }

    public static short SndSetInfo(int chan, int selector, int value) {
        return setInfo(chan, selector, value, null);
    }

    // ---- sound headers

    /**
     * Makes a format 1 'snd ' resource with a sampled synthesizer and a buffer command of the header:
     * a standard header for 8 bit mono, an extended one for other linear samples, else a compressed one.
     */
    public static short SetupSndHeader(int sndHandle, short numChannels, int sampleRate, short sampleSize, int compressionType, short baseNote, int numBytes,
                                       short[] headerLen) {
        int channels = Math.max(1, numChannels);
        boolean linear = compressionType == kSoundNotCompressed || compressionType == 0 || compressionType == k8BitOffsetBinaryFormat
                || compressionType == k16BitBigEndianFormat;
        int encode = linear ? (channels == 1 && sampleSize == 8 ? stdSH : extSH) : cmpSH;
        long[] fb = framesAndBytes(linear ? (sampleSize == 8 ? k8BitOffsetBinaryFormat : k16BitBigEndianFormat) : compressionType, channels, sampleSize);
        if (fb == null) {
            return siInvalidCompression;
        }
        long frames = numBytes / fb[1] * fb[0];
        byte[] r = new byte[20 + headerSize(encode)];
        Bytes.putShort(r, 0, 1); // format
        Bytes.putShort(r, 2, 1); // modifiers
        Bytes.putShort(r, 4, sampledSynth);
        Bytes.putInt(r, 6, channels == 2 ? initStereo : 0x80);
        Bytes.putShort(r, 10, 1); // commands
        Bytes.putShort(r, 12, dataOffsetFlag | bufferCmd);
        Bytes.putShort(r, 14, 0);
        Bytes.putInt(r, 16, 20);
        int h = 20;
        Bytes.putInt(r, h + 8, sampleRate);
        r[h + 20] = (byte) encode;
        r[h + 21] = (byte) (baseNote == 0 ? 60 : baseNote);
        if (encode == stdSH) {
            Bytes.putInt(r, h + 4, numBytes);
        } else {
            Bytes.putInt(r, h + 4, channels);
            Bytes.putInt(r, h + 22, (int) frames);
            System.arraycopy(Exporters.extended((sampleRate & 0xffff_ffffL) / 65536.0), 0, r, h + 26, 10);
            if (encode == extSH) {
                Bytes.putShort(r, h + 48, sampleSize);
            } else {
                Bytes.putInt(r, h + 40, compressionType);
                Bytes.putShort(r, h + 56, -1); // variable compression
                Bytes.putShort(r, h + 62, sampleSize);
            }
        }
        int size = Heap.handleSize(sndHandle);
        if (size < r.length) {
            Heap.setHandleSize(sndHandle, r.length);
        }
        Heap.write(Heap.deref(sndHandle), r, 0, r.length);
        if (headerLen != null && headerLen.length > 0) {
            headerLen[0] = (short) r.length;
        }
        return noErr;
    }

    public static short GetSoundHeaderOffset(int sndHandle, int[] offset) {
        try {
            offset[0] = soundHeaderOffset(Heap.handleBytes(sndHandle));
            return noErr;
        } catch (IOException | RuntimeException e) {
            return badFormat;
        }
    }

    /** SoundComponentData: flags(4) format(4) numChannels(2) sampleSize(2) sampleRate(4) sampleCount(4) buffer(4) reserved(4) */
    static void componentData(byte[] scd, int format, int channels, int bits, int rate, int count, int buffer) {
        Arrays.fill(scd, 0, Math.min(28, scd.length), (byte) 0);
        Bytes.putInt(scd, 4, format);
        Bytes.putShort(scd, 8, channels);
        Bytes.putShort(scd, 10, bits);
        Bytes.putInt(scd, 12, rate);
        Bytes.putInt(scd, 16, count);
        Bytes.putInt(scd, 20, buffer);
    }

    public static short ParseSndHeader(int sndHandle, byte[] scd, int[] numFrames, int[] dataOffset) {
        try {
            byte[] r = Heap.handleBytes(sndHandle);
            int h = soundHeaderOffset(r);
            int encode = r[h + 20] & 0xff;
            int rate = Bytes.getInt(r, h + 8);
            int channels, bits, format, frames;
            switch (encode) {
                case stdSH -> {
                    channels = 1;
                    bits = 8;
                    format = k8BitOffsetBinaryFormat;
                    frames = Bytes.getInt(r, h + 4);
                }
                case extSH -> {
                    channels = Bytes.getInt(r, h + 4);
                    bits = Bytes.getShort(r, h + 48);
                    format = bits == 8 ? k8BitOffsetBinaryFormat : k16BitBigEndianFormat;
                    frames = Bytes.getInt(r, h + 22);
                }
                case cmpSH -> {
                    channels = Bytes.getInt(r, h + 4);
                    bits = Bytes.getShort(r, h + 62);
                    format = Bytes.getInt(r, h + 40);
                    frames = Bytes.getInt(r, h + 22);
                }
                default -> {
                    return badFormat;
                }
            }
            int offset = h + headerSize(encode);
            int samplePtr = Bytes.getInt(r, h);
            componentData(scd, format, channels, bits, rate, frames, samplePtr != 0 ? samplePtr : Heap.deref(sndHandle) + offset);
            numFrames[0] = frames;
            dataOffset[0] = offset;
            return noErr;
        } catch (IOException | RuntimeException e) {
            return badFormat;
        }
    }

    // ---- AIFF headers

    /** writes an AIFF (linear samples) or AIFC header at the current position of the open file */
    public static short SetupAIFFHeader(short fRefNum, short numChannels, int sampleRate, short sampleSize, int compressionType, int numBytes, int numFrames) {
        FileManager.OpenedFile f = FileManager.opened(fRefNum);
        if (f == null) {
            return rfNumErr;
        }
        boolean aifc = compressionType != kSoundNotCompressed && compressionType != 0 && compressionType != k16BitBigEndianFormat
                && compressionType != k8BitOffsetBinaryFormat;
        MovieAtoms.Out o = new MovieAtoms.Out();
        int commSize = aifc ? 18 + 4 + 2 : 18;
        int form = 4 + (aifc ? 12 : 0) + 8 + commSize + 16 + numBytes;
        o.i32(Bytes.fourCC("FORM")).i32(form).i32(Bytes.fourCC(aifc ? "AIFC" : "AIFF"));
        if (aifc) {
            o.i32(Bytes.fourCC("FVER")).i32(4).i32(0xa280_5140);
        }
        o.i32(Bytes.fourCC("COMM")).i32(commSize).i16(numChannels).i32(numFrames).i16(sampleSize).bytes(Exporters.extended((sampleRate & 0xffff_ffffL) / 65536.0));
        if (aifc) {
            o.i32(compressionType).i16(0);
        }
        o.i32(Bytes.fourCC("SSND")).i32(8 + numBytes).i32(0).i32(0);
        try {
            synchronized (f) {
                RandomAccessFile r = f.file();
                r.write(o.toByteArray());
            }
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    /** reads the COMM and SSND chunks of an AIFF or AIFC file from its start */
    public static short ParseAIFFHeader(short fRefNum, byte[] scd, int[] numFrames, int[] dataOffset) {
        FileManager.OpenedFile f = FileManager.opened(fRefNum);
        if (f == null) {
            return rfNumErr;
        }
        try {
            synchronized (f) {
                RandomAccessFile r = f.file();
                byte[] head = new byte[12];
                r.seek(0);
                r.readFully(head);
                int formType = Bytes.getInt(head, 8);
                if (Bytes.getInt(head, 0) != Bytes.fourCC("FORM") || (formType != Bytes.fourCC("AIFF") && formType != Bytes.fourCC("AIFC"))) {
                    return badFormat;
                }
                long p = 12;
                int channels = 0, bits = 0, frames = 0, format = k16BitBigEndianFormat;
                double rate = 0;
                long data = -1;
                byte[] ch = new byte[8];
                while (p + 8 <= r.length()) {
                    r.seek(p);
                    r.readFully(ch);
                    int type = Bytes.getInt(ch, 0);
                    long size = Bytes.getInt(ch, 4) & 0xffff_ffffL;
                    if (type == Bytes.fourCC("COMM")) {
                        byte[] c = new byte[(int) Math.min(size, 64)];
                        r.readFully(c);
                        channels = Bytes.getShort(c, 0);
                        frames = Bytes.getInt(c, 2);
                        bits = Bytes.getShort(c, 6);
                        rate = Importers.extended(c, 8);
                        if (formType == Bytes.fourCC("AIFC") && c.length >= 22) {
                            format = Bytes.getInt(c, 18);
                        } else {
                            format = bits <= 8 ? k8BitOffsetBinaryFormat : k16BitBigEndianFormat;
                        }
                        if (format == kSoundNotCompressed) {
                            format = bits <= 8 ? k8BitOffsetBinaryFormat : k16BitBigEndianFormat;
                        }
                    } else if (type == Bytes.fourCC("SSND")) {
                        byte[] s = new byte[8];
                        r.readFully(s);
                        data = p + 16 + (Bytes.getInt(s, 0) & 0xffff_ffffL);
                    }
                    p += 8 + size + (size & 1);
                }
                if (channels == 0 || data < 0) {
                    return badFormat;
                }
                componentData(scd, format, channels, bits, (int) Math.round(rate * 65536.0), frames, 0);
                numFrames[0] = frames;
                dataOffset[0] = (int) data;
            }
            return noErr;
        } catch (IOException e) {
            return eofErr;
        }
    }

    // ---- sound description extensions

    /** @return the size of the sound description without its extensions */
    static int baseSize(byte[] d) {
        int version = d.length >= 18 ? Bytes.getShort(d, 16) : 0;
        return Math.min(d.length, version == 2 ? 72 : version == 1 ? 52 : 36);
    }

    /** extensions are atoms after the base, the 'wave' atom's children are searched too */
    public static short GetSoundDescriptionExtension(int desc, int[] extension, int idType) {
        byte[] d = Heap.handleBytes(desc);
        int end = Math.min(d.length, Math.max(baseSize(d), Bytes.getInt(d, 0)));
        for (MovieAtoms.Atom a : MovieAtoms.atoms(d, baseSize(d), end)) {
            if (a.type() == idType) {
                extension[0] = Heap.newHandle(Arrays.copyOfRange(d, a.data(), a.end()));
                return noErr;
            }
            if (a.type() == Bytes.fourCC("wave")) {
                for (MovieAtoms.Atom w : MovieAtoms.atoms(d, a.data(), a.end())) {
                    if (w.type() == idType) {
                        extension[0] = Heap.newHandle(Arrays.copyOfRange(d, w.data(), w.end()));
                        return noErr;
                    }
                }
            }
        }
        extension[0] = 0;
        return codecExtensionNotFoundErr;
    }

    public static short AddSoundDescriptionExtension(int desc, int extension, int idType) {
        byte[] d = Heap.handleBytes(desc);
        byte[] e = Heap.handleBytes(extension);
        int size = Math.max(baseSize(d), Math.min(d.length, Bytes.getInt(d, 0)));
        byte[] n = Arrays.copyOf(d, size + 8 + e.length);
        Bytes.putInt(n, size, 8 + e.length);
        Bytes.putInt(n, size + 4, idType);
        System.arraycopy(e, 0, n, size + 8, e.length);
        Bytes.putInt(n, 0, n.length);
        Heap.setHandleBytes(desc, n);
        return noErr;
    }

    public static short RemoveSoundDescriptionExtension(int desc, int idType) {
        byte[] d = Heap.handleBytes(desc);
        int base = baseSize(d);
        int end = Math.min(d.length, Math.max(base, Bytes.getInt(d, 0)));
        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        os.write(d, 0, base);
        boolean removed = false;
        for (MovieAtoms.Atom a : MovieAtoms.atoms(d, base, end)) {
            if (a.type() == idType) {
                removed = true;
                continue;
            }
            os.write(d, a.offset(), a.end() - a.offset());
        }
        if (!removed) {
            return codecExtensionNotFoundErr;
        }
        byte[] b = os.toByteArray();
        Bytes.putInt(b, 0, b.length);
        Heap.setHandleBytes(desc, b);
        return noErr;
    }

    // ---- sound input

    /** makes the input line of a device, for tests to feed it */
    static Function<AudioFormat, TargetDataLine> sources = format -> {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try {
            return (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException | IllegalArgumentException | SecurityException e) {
            return null;
        }
    };

    /** @return the names of the input devices */
    static List<String> inputDevices() {
        List<String> names = new ArrayList<>();
        try {
            for (Mixer.Info m : AudioSystem.getMixerInfo()) {
                Mixer mixer = AudioSystem.getMixer(m);
                if (mixer.getTargetLineInfo(new DataLine.Info(TargetDataLine.class, null)).length > 0) {
                    names.add(m.getName());
                }
            }
        } catch (RuntimeException e) {
            logger.log(Level.FINE, "mixers", e);
        }
        if (names.isEmpty() && sources.apply(new AudioFormat(44100, 16, 2, true, true)) != null) {
            names.add("Built-in Input");
        }
        return names;
    }

    /** an open input device */
    static final class Device {
        final int ref;
        final String name;
        double rate = 44100;
        int sampleSize = 16;
        int channels = 2;
        int compression = kSoundNotCompressed;
        boolean continuous;
        boolean levelMeter;
        boolean async = true;
        boolean twos = true;
        boolean playThrough;
        int gain = 0x10000;
        int quality = Bytes.fourCC("best");
        // recording status
        volatile boolean recording;
        volatile boolean paused;
        volatile boolean stop;
        volatile long totalBytes;
        volatile long recordedBytes;
        volatile int level;
        Thread thread;

        Device(String name) {
            this.ref = Heap.newObject(this, 4);
            this.name = name;
        }

        int frameBytes() {
            return channels * ((sampleSize + 7) / 8);
        }

        AudioFormat format() {
            return new AudioFormat((float) rate, sampleSize, channels, sampleSize > 8 || twos, true);
        }
    }

    private static final Map<Integer, Device> devices = new HashMap<>();

    static Device device(int ref) {
        return Heap.peerOrNull(ref, Device.class);
    }

    public static short SPBGetIndexedDevice(short count, byte[] deviceName, int[] deviceIconHandle) {
        List<String> names = inputDevices();
        if (count < 1 || count > names.size()) {
            return siBadSoundInDevice;
        }
        Bytes.putPString(deviceName, 0, names.get(count - 1), Math.min(256, deviceName.length));
        if (deviceIconHandle != null && deviceIconHandle.length > 0) {
            deviceIconHandle[0] = 0;
        }
        return noErr;
    }

    public static short SPBOpenDevice(byte[] deviceName, short permission, int[] inRefNum) {
        List<String> names = inputDevices();
        if (names.isEmpty()) {
            return siNoSoundInHardware;
        }
        String name = deviceName == null || deviceName.length == 0 || deviceName[0] == 0 ? names.getFirst() : Bytes.getPString(deviceName, 0);
        if (!names.contains(name)) {
            return siBadSoundInDevice;
        }
        Device d = new Device(name);
        synchronized (devices) {
            devices.put(d.ref, d);
        }
        inRefNum[0] = d.ref;
        return noErr;
    }

    public static short SPBCloseDevice(int inRefNum) {
        Device d;
        synchronized (devices) {
            d = devices.remove(inRefNum);
        }
        if (d == null) {
            return siBadRefNum;
        }
        d.stop = true;
        Heap.free(inRefNum);
        return noErr;
    }

    /** @return the value of a device setting */
    private static byte[] deviceInfo(Device d, int selector) {
        byte[] v;
        if (selector == siSampleRate) {
            v = SoundManager.int4((int) Math.round(d.rate * 65536.0));
        } else if (selector == siSampleSize) {
            v = short2(d.sampleSize);
        } else if (selector == siNumberChannels) {
            v = short2(d.channels);
        } else if (selector == siCompressionType) {
            v = int4(d.compression);
        } else if (selector == siContinuous) {
            v = short2(d.continuous ? 1 : 0);
        } else if (selector == siLevelMeterOnOff) {
            v = short2(d.levelMeter ? 1 : 0);
        } else if (selector == siActiveLevels) {
            v = short2(d.level);
        } else if (selector == siAsync) {
            v = short2(d.async ? 1 : 0);
        } else if (selector == siTwosComplementOnOff) {
            v = short2(d.twos ? 1 : 0);
        } else if (selector == siPlayThruOnOff) {
            v = short2(d.playThrough ? 1 : 0);
        } else if (selector == siInputGain) {
            v = int4(d.gain);
        } else if (selector == siRecordingQuality) {
            v = int4(d.quality);
        } else if (selector == siActiveChannels) {
            v = int4((1 << d.channels) - 1);
        } else if (selector == siDeviceName) {
            v = new byte[256];
            Bytes.putPString(v, 0, d.name, 256);
        } else if (selector == siDeviceIcon) {
            v = int4(0);
        } else if (selector == siSampleRateAvailable) {
            // count(2) handle(4) of UnsignedFixed rates
            int[] rates = {8000, 11025, 16000, 22050, 32000, 44100, 48000};
            byte[] h = new byte[rates.length * 4];
            for (int i = 0; i < rates.length; i++) {
                Bytes.putInt(h, i * 4, rates[i] << 16);
            }
            v = available(rates.length, h);
        } else if (selector == siSampleSizeAvailable) {
            v = available(2, new byte[] {0, 8, 0, 16});
        } else if (selector == siChannelAvailable) {
            v = short2(2);
        } else if (selector == siCompressionAvailable) {
            byte[] h = new byte[8];
            Bytes.putInt(h, 0, kSoundNotCompressed);
            Bytes.putInt(h, 4, Audio.ulaw);
            v = available(2, h);
        } else {
            return null;
        }
        return v;
    }

    private static byte[] available(int count, byte[] list) {
        byte[] v = new byte[6];
        Bytes.putShort(v, 0, count);
        Bytes.putInt(v, 2, Heap.newHandle(list));
        return v;
    }

    static byte[] int4(int v) {
        byte[] b = new byte[4];
        Bytes.putInt(b, 0, v);
        return b;
    }

    static byte[] short2(int v) {
        byte[] b = new byte[2];
        Bytes.putShort(b, 0, v);
        return b;
    }

    private static short getDeviceInfo(int inRefNum, int selector, Object out) {
        Device d = device(inRefNum);
        if (d == null) {
            return siBadRefNum;
        }
        byte[] v = deviceInfo(d, selector);
        if (v == null) {
            return siUnknownInfoType;
        }
        if (out instanceof Integer p) {
            Heap.write(p, v, 0, v.length);
        } else {
            Bytes.toArray(v, 0, out, 0, Math.min(v.length, Imaging.capacity(out)));
        }
        return noErr;
    }

    public static short SPBGetDeviceInfo(int inRefNum, int selector, byte[] info) {
        return getDeviceInfo(inRefNum, selector, info);
    }

    public static short SPBGetDeviceInfo(int inRefNum, int selector, int info) {
        return getDeviceInfo(inRefNum, selector, info);
    }

    public static short SPBGetDeviceInfo(int inRefNum, int selector, int[] info) {
        return getDeviceInfo(inRefNum, selector, info);
    }

    public static short SPBGetDeviceInfo(int inRefNum, int selector, short[] info) {
        return getDeviceInfo(inRefNum, selector, info);
    }

    private static short setDeviceInfo(int inRefNum, int selector, int value) {
        Device d = device(inRefNum);
        if (d == null) {
            return siBadRefNum;
        }
        if (d.recording && (selector == siSampleRate || selector == siSampleSize || selector == siNumberChannels || selector == siCompressionType)) {
            return siDeviceBusyErr;
        }
        if (selector == siSampleRate) {
            double r = (value & 0xffff_ffffL) / 65536.0;
            if (r < 1000 || r > 192000) {
                return siInvalidSampleRate;
            }
            d.rate = r;
        } else if (selector == siSampleSize) {
            if (value != 8 && value != 16 && value != 24) {
                return siInvalidSampleSize;
            }
            d.sampleSize = value;
        } else if (selector == siNumberChannels) {
            if (value < 1 || value > 2) {
                return paramErr;
            }
            d.channels = value;
        } else if (selector == siCompressionType) {
            if (value != kSoundNotCompressed && value != 0 && value != Audio.ulaw) {
                return siInvalidCompression;
            }
            d.compression = value == 0 ? kSoundNotCompressed : value;
        } else if (selector == siContinuous) {
            d.continuous = value != 0;
        } else if (selector == siLevelMeterOnOff) {
            d.levelMeter = value != 0;
        } else if (selector == siAsync) {
            d.async = value != 0;
        } else if (selector == siTwosComplementOnOff) {
            d.twos = value != 0;
        } else if (selector == siPlayThruOnOff) {
            d.playThrough = value != 0;
        } else if (selector == siInputGain) {
            d.gain = value;
        } else if (selector == siRecordingQuality) {
            d.quality = value;
        } else {
            return siUnknownInfoType;
        }
        return noErr;
    }

    public static short SPBSetDeviceInfo(int inRefNum, int selector, int[] info) {
        return setDeviceInfo(inRefNum, selector, info[0]);
    }

    public static short SPBSetDeviceInfo(int inRefNum, int selector, short[] info) {
        return setDeviceInfo(inRefNum, selector, info[0]);
    }

    /** bytes per millisecond of recorded data, µ-law is a byte per sample */
    private static double bytesPerMs(Device d) {
        int frame = d.compression == Audio.ulaw ? d.channels : d.frameBytes();
        return d.rate * frame / 1000.0;
    }

    public static short SPBMillisecondsToBytes(int inRefNum, int[] value) {
        Device d = device(inRefNum);
        if (d == null) {
            return siBadRefNum;
        }
        value[0] = (int) Math.round(value[0] * bytesPerMs(d));
        return noErr;
    }

    public static short SPBBytesToMilliseconds(int inRefNum, int[] value) {
        Device d = device(inRefNum);
        if (d == null) {
            return siBadRefNum;
        }
        value[0] = (int) Math.round(value[0] / bytesPerMs(d));
        return noErr;
    }

    /**
     * SPB: inRefNum(4) count(4) milliseconds(4) bufferLength(4) bufferPtr(4) completionRoutine(4) interruptRoutine(4) userLong(4) error(2) unused(4).
     * count (or milliseconds if count is 0) bytes are recorded into the buffer, count is set to the bytes recorded.
     */
    public static short SPBRecord(int spb, byte async) {
        Device d = device(Heap.getInt(spb));
        if (d == null) {
            return siBadRefNum;
        }
        if (d.recording) {
            return siDeviceBusyErr;
        }
        int buffer = Heap.getInt(spb + 16);
        int bufferLength = Heap.getInt(spb + 12);
        if (buffer == 0 || bufferLength <= 0) {
            return siNoBufferSpecified;
        }
        long want = Heap.getInt(spb + 4);
        if (want <= 0) {
            want = Math.round(Heap.getInt(spb + 8) * bytesPerMs(d));
        }
        want = Math.min(want, bufferLength);
        TargetDataLine line = sources.apply(d.format());
        if (line == null) {
            return siNoSoundInHardware;
        }
        d.recording = true;
        d.paused = false;
        d.stop = false;
        d.totalBytes = want;
        d.recordedBytes = 0;
        long total = want;
        Runnable r = () -> {
            short err = noErr;
            try {
                if (!line.isOpen()) {
                    line.open(d.format());
                }
                line.start();
                boolean ulawOut = d.compression == Audio.ulaw;
                int frame = d.frameBytes();
                byte[] in = new byte[4096 - 4096 % frame];
                long done = 0;
                while (done < total && !d.stop) {
                    if (d.paused) {
                        line.stop();
                        while (d.paused && !d.stop) {
                            Thread.sleep(10);
                        }
                        line.start();
                        continue;
                    }
                    long need = ulawOut ? (total - done) * (frame / d.channels) : total - done;
                    int n = line.read(in, 0, (int) Math.min(in.length, Math.max(frame, need - need % frame)));
                    if (n <= 0) {
                        break;
                    }
                    byte[] out = ulawOut ? toUlaw(in, n, d.sampleSize) : Arrays.copyOf(in, n);
                    int m = (int) Math.min(out.length, total - done);
                    Heap.write(buffer + (int) done, out, 0, m);
                    done += m;
                    d.recordedBytes = done;
                    if (d.levelMeter) {
                        d.level = level(in, n, d.sampleSize);
                    }
                }
                Heap.putInt(spb + 4, (int) done);
            } catch (LineUnavailableException e) {
                err = siNoSoundInHardware;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                err = -128;
            } catch (RuntimeException e) {
                logger.log(Level.FINE, "record", e);
                err = siBadSoundInDevice;
            } finally {
                line.stop();
                line.close();
                d.recording = false;
                Heap.putShort(spb + 32, err);
            }
            int completion = Heap.getInt(spb + 20);
            if (completion != 0) {
                Closures.call(completion, spb);
            }
        };
        Heap.putShort(spb + 32, (short) 1);
        if (async != 0) {
            d.thread = new Thread(r, "Sound input");
            d.thread.setDaemon(true);
            d.thread.start();
            return noErr;
        }
        r.run();
        return Heap.getShort(spb + 32);
    }

    /** big endian signed samples to µ-law */
    private static byte[] toUlaw(byte[] in, int n, int bits) {
        int bytes = (bits + 7) / 8;
        byte[] out = new byte[n / bytes];
        for (int i = 0; i < out.length; i++) {
            int v = bytes == 1 ? (in[i] << 8) : (in[i * bytes] << 8) | (in[i * bytes + 1] & 0xff);
            out[i] = Audio.toUlaw(v);
        }
        return out;
    }

    private static int level(byte[] in, int n, int bits) {
        int bytes = (bits + 7) / 8;
        int peak = 0;
        for (int i = 0; i + bytes <= n; i += bytes) {
            int v = bytes == 1 ? in[i] << 8 : (in[i] << 8) | (in[i + 1] & 0xff);
            peak = Math.max(peak, Math.abs(v));
        }
        return peak >> 7;
    }

    public static short SPBPauseRecording(int inRefNum) {
        Device d = device(inRefNum);
        if (d == null) {
            return siBadRefNum;
        }
        d.paused = true;
        return noErr;
    }

    public static short SPBResumeRecording(int inRefNum) {
        Device d = device(inRefNum);
        if (d == null) {
            return siBadRefNum;
        }
        d.paused = false;
        return noErr;
    }

    public static short SPBStopRecording(int inRefNum) {
        Device d = device(inRefNum);
        if (d == null) {
            return siBadRefNum;
        }
        d.stop = true;
        Thread t = d.thread;
        if (t != null) {
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return noErr;
    }

    public static short SPBGetRecordingStatus(int inRefNum, short[] recordingStatus, short[] meterLevel, int[] totalSamplesToRecord, int[] numberOfSamplesRecorded,
                                              int[] totalMsecsToRecord, int[] numberOfMsecsRecorded) {
        Device d = device(inRefNum);
        if (d == null) {
            return siBadRefNum;
        }
        double perMs = bytesPerMs(d);
        int sampleBytes = d.compression == Audio.ulaw ? 1 : (d.sampleSize + 7) / 8;
        recordingStatus[0] = (short) (d.recording ? (d.paused ? 2 : 1) : 0);
        meterLevel[0] = (short) d.level;
        totalSamplesToRecord[0] = (int) (d.totalBytes / sampleBytes);
        numberOfSamplesRecorded[0] = (int) (d.recordedBytes / sampleBytes);
        totalMsecsToRecord[0] = (int) Math.round(d.totalBytes / perMs);
        numberOfMsecsRecorded[0] = (int) Math.round(d.recordedBytes / perMs);
        return noErr;
    }
}
