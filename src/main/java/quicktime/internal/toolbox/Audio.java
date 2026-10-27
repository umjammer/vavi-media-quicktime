/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.IOException;


/**
 * Sound decoders in Java: linear PCM ('raw ', 'twos', 'sowt', 'in24', 'in32', 'fl32', 'fl64', 'lpcm', 'NONE'),
 * µ-law ('ulaw'), A-law ('alaw') and QuickTime IMA 4:1 ('ima4').
 * <p>
 * Samples are decoded to interleaved floats in [-1, 1].
 */
final class Audio {

    private Audio() {
    }

    static final int ulaw = Bytes.fourCC("ulaw");
    static final int alaw = Bytes.fourCC("alaw");
    static final int ima4 = Bytes.fourCC("ima4");

    /** a decoder of the sound data of a sound description */
    abstract static class Decoder {
        final int channels;
        final double rate;
        /** frames in a packet, data is read in whole packets */
        final int framesPerPacket;
        /** bytes of a packet of all channels */
        final int bytesPerPacket;

        Decoder(int channels, double rate, int framesPerPacket, int bytesPerPacket) {
            this.channels = channels;
            this.rate = rate;
            this.framesPerPacket = framesPerPacket;
            this.bytesPerPacket = bytesPerPacket;
        }

        /** @return the interleaved samples of the whole packets in the data */
        abstract float[] decode(byte[] data);
    }

    /** linear PCM */
    static final class Linear extends Decoder {
        final Exporters.Pcm pcm;

        Linear(Exporters.Pcm pcm) {
            super(pcm.channels(), pcm.rate(), 1, pcm.frameBytes());
            this.pcm = pcm;
        }

        @Override
        float[] decode(byte[] data) {
            int bytes = (pcm.bits() + 7) / 8;
            int n = data.length / bytes;
            n -= n % channels;
            float[] out = new float[n];
            for (int i = 0; i < n; i++) {
                out[i] = (float) sample(data, i * bytes, pcm);
            }
            return out;
        }
    }

    /** µ-law and A-law, a byte per sample */
    static final class Law extends Decoder {
        final boolean a;

        Law(int channels, double rate, boolean a) {
            super(channels, rate, 1, channels);
            this.a = a;
        }

        @Override
        float[] decode(byte[] data) {
            int n = data.length - data.length % channels;
            float[] out = new float[n];
            for (int i = 0; i < n; i++) {
                out[i] = (a ? alaw(data[i]) : ulaw(data[i])) / 32768f;
            }
            return out;
        }
    }

    static int ulaw(byte b) {
        int u = ~b & 0xff;
        int t = ((u & 0x0f) << 3) + 0x84;
        t <<= (u & 0x70) >> 4;
        return (u & 0x80) != 0 ? 0x84 - t : t - 0x84;
    }

    static int alaw(byte b) {
        int a = (b & 0xff) ^ 0x55;
        int t = (a & 0x0f) << 4;
        int seg = (a & 0x70) >> 4;
        switch (seg) {
            case 0 -> t += 8;
            case 1 -> t += 0x108;
            default -> {
                t += 0x108;
                t <<= seg - 1;
            }
        }
        return (a & 0x80) != 0 ? t : -t;
    }

    /** @return the µ-law byte of a 16 bit sample */
    static byte toUlaw(int pcm) {
        int sign = pcm < 0 ? 0x80 : 0;
        int v = Math.min(32635, Math.abs(pcm)) + 0x84;
        int exp = 7;
        for (int mask = 0x4000; (v & mask) == 0 && exp > 0; mask >>= 1) {
            exp--;
        }
        int mant = (v >> (exp + 3)) & 0x0f;
        return (byte) ~(sign | exp << 4 | mant);
    }

    static final int[] IMA_STEPS = {
            7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 19, 21, 23, 25, 28, 31, 34, 37, 41, 45, 50, 55, 60, 66, 73, 80, 88, 97, 107, 118, 130, 143,
            157, 173, 190, 209, 230, 253, 279, 307, 337, 371, 408, 449, 494, 544, 598, 658, 724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552,
            1707, 1878, 2066, 2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871, 5358, 5894, 6484, 7132, 7845, 8630, 9493, 10442, 11487,
            12635, 13899, 15289, 16818, 18500, 20350, 22385, 24623, 27086, 29794, 32767,
    };

    static final int[] IMA_INDEX = {-1, -1, -1, -1, 2, 4, 6, 8, -1, -1, -1, -1, 2, 4, 6, 8};

    /** QuickTime IMA 4:1, packets of 64 samples per channel in 34 bytes, the channels' packets interleaved */
    static final class Ima4 extends Decoder {
        Ima4(int channels, double rate) {
            super(channels, rate, 64, 34 * channels);
        }

        @Override
        float[] decode(byte[] data) {
            int packets = data.length / bytesPerPacket;
            float[] out = new float[packets * 64 * channels];
            for (int p = 0; p < packets; p++) {
                for (int c = 0; c < channels; c++) {
                    int o = p * bytesPerPacket + c * 34;
                    int header = Bytes.getShort(data, o) & 0xffff;
                    int predictor = (short) (header & 0xff80);
                    int index = Math.min(88, header & 0x7f);
                    for (int i = 0; i < 64; i++) {
                        int b = data[o + 2 + i / 2] & 0xff;
                        int nibble = (i & 1) == 0 ? b & 0x0f : b >> 4;
                        int step = IMA_STEPS[index];
                        int diff = step >> 3;
                        if ((nibble & 4) != 0) {
                            diff += step;
                        }
                        if ((nibble & 2) != 0) {
                            diff += step >> 1;
                        }
                        if ((nibble & 1) != 0) {
                            diff += step >> 2;
                        }
                        predictor += (nibble & 8) != 0 ? -diff : diff;
                        predictor = Math.clamp(predictor, -32768, 32767);
                        index = Math.clamp(index + IMA_INDEX[nibble], 0, 88);
                        out[(p * 64 + i) * channels + c] = predictor / 32768f;
                    }
                }
            }
            return out;
        }
    }

    /** @return a sample as a double in [-1, 1) */
    static double sample(byte[] b, int o, Exporters.Pcm f) {
        int bytes = (f.bits() + 7) / 8;
        long v = 0;
        for (int i = 0; i < bytes; i++) {
            int x = b[o + (f.bigEndian() ? i : bytes - 1 - i)] & 0xff;
            v = (v << 8) | x;
        }
        if (f.isFloat()) {
            return bytes == 8 ? Double.longBitsToDouble(v) : Float.intBitsToFloat((int) v);
        }
        if (f.signed()) {
            v = (v << (64 - f.bits())) >> (64 - f.bits());
        } else {
            v -= 1L << (f.bits() - 1);
        }
        return v / (double) (1L << (f.bits() - 1));
    }

    /** @return the decoder of a sound description, null if there is none */
    static Decoder decoder(byte[] d) {
        if (d == null || d.length < 36) {
            return null;
        }
        Exporters.Pcm pcm = Exporters.pcm(d);
        if (pcm != null && pcm.channels() > 0 && pcm.bits() > 0) {
            return new Linear(pcm);
        }
        int format = Bytes.getInt(d, 4);
        int version = Bytes.getShort(d, 16);
        int channels = Math.max(1, Bytes.getShort(d, 24) & 0xffff);
        double rate = (Bytes.getInt(d, 32) & 0xffff_ffffL) / 65536.0;
        if (version == 2 && d.length >= 72) {
            rate = Double.longBitsToDouble(Bytes.getLong(d, 40));
            channels = Math.max(1, Bytes.getInt(d, 48));
        }
        if (format == ulaw || format == alaw) {
            return new Law(channels, rate, format == alaw);
        }
        if (format == ima4) {
            return new Ima4(channels, rate);
        }
        return null;
    }

    /**
     * Decodes media frames, packet aligned.
     * @param mediaTime the first frame in the media time scale, which is the sample rate for sound
     * @param frames the number of frames
     * @return interleaved samples of the frames, silence where there is no data, null if the codec is not supported
     */
    static float[] read(MediaRec m, long mediaTime, int frames, int channels) throws IOException {
        float[] out = new float[frames * channels];
        long t = mediaTime;
        int done = 0;
        while (done < frames) {
            long s = m.sampleAt(t);
            if (s == 0) {
                break;
            }
            int r = m.runOfSample(s);
            MediaRec.SampleRun run = m.runs.get(r);
            Decoder dec = decoder(m.descriptions.get(run.descIndex - 1));
            if (dec == null) {
                return done == 0 ? null : out;
            }
            long first = m.firstSampleOf(r);
            long k = s - first;
            // samples of sound are frames of duration 1, or packets of frames for some files
            int framesPerSample = Math.max(1, run.duration);
            long frameInRun = k * framesPerSample + (t - m.timeOfSample(s));
            long packet = frameInRun / dec.framesPerPacket;
            long framesInRun = (long) run.count * framesPerSample;
            long packets = (framesInRun + dec.framesPerPacket - 1) / dec.framesPerPacket;
            int want = frames - done;
            long skip = frameInRun - packet * dec.framesPerPacket;
            long needPackets = Math.min(packets - packet, (want + skip + dec.framesPerPacket - 1) / dec.framesPerPacket);
            needPackets = Math.min(needPackets, 4096);
            if (needPackets <= 0) {
                break;
            }
            long offset = run.offset + run.bytes * packet / packets;
            long end = run.offset + run.bytes * (packet + needPackets) / packets;
            byte[] data = m.dataRef(run.dataRefIndex).read(offset, (int) (end - offset));
            float[] pcm = dec.decode(data);
            int got = pcm.length / dec.channels;
            int n = (int) Math.min(want, got - skip);
            if (n <= 0) {
                break;
            }
            for (int i = 0; i < n; i++) {
                for (int c = 0; c < channels; c++) {
                    int sc = Math.min(c, dec.channels - 1);
                    float v = pcm[(int) ((i + skip) * dec.channels + sc)];
                    if (channels == 1 && dec.channels > 1) {
                        v = 0;
                        for (int x = 0; x < dec.channels; x++) {
                            v += pcm[(int) ((i + skip) * dec.channels + x)];
                        }
                        v /= dec.channels;
                    }
                    out[(done + i) * channels + c] = v;
                }
            }
            done += n;
            t += n;
        }
        return out;
    }
}
