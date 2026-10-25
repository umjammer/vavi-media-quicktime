/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import java.util.List;

import com.sun.jna.Structure;


/**
 * CoreMedia CMTime: value(8) timescale(4) flags(4) epoch(8).
 *
 * @see "https://developer.apple.com/documentation/coremedia/cmtime"
 */
public class CMTime extends Structure {

    public static final int kCMTimeFlags_Valid = 1;

    public long value;
    public int timescale;
    public int flags;
    public long epoch;

    public CMTime() {
    }

    public CMTime(long value, int timescale) {
        this.value = value;
        this.timescale = timescale;
        this.flags = kCMTimeFlags_Valid;
    }

    @Override
    protected List<String> getFieldOrder() {
        return List.of("value", "timescale", "flags", "epoch");
    }

    /** @return the time in seconds, NaN if invalid */
    public double seconds() {
        return (flags & kCMTimeFlags_Valid) != 0 && timescale != 0 ? value / (double) timescale : Double.NaN;
    }

    @Override
    public String toString() {
        return "CMTime[" + value + "/" + timescale + ", flags=" + flags + "]";
    }

    /** passed by value */
    public static class ByValue extends CMTime implements Structure.ByValue {

        public ByValue() {
        }

        public ByValue(long value, int timescale) {
            super(value, timescale);
        }
    }

    /** passed by reference */
    public static class ByReference extends CMTime implements Structure.ByReference {
    }
}
