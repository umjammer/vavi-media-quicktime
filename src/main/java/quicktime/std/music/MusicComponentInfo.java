package quicktime.std.music;

import quicktime.util.QTUtils;

/**
 * Contains information about the Music Component.
 */
public class MusicComponentInfo {

    /**
     * Synthesizer type.
     */
    public int synthType;

    /**
     * Name of the Synthesizer.
     */
    public String name;

    /**
     * A Synthesizer's connections for a MIDI device.
     */
    public SynthesizerConnections connections;

    /**
     * Music Component.
     */
    public MusicComponent mc;

    /**
     * String representation of class.
     */
    public String toString() {
        return getClass().getName() + "[snythType=" + QTUtils.fromOSType(this.synthType) + ",name=" + this.name + ",connections=" + this.connections + ",mc=" + this.mc.toString() + "]";
    }
}
