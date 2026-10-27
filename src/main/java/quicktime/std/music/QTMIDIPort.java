package quicktime.std.music;

import quicktime.util.UtilException;

/**
 * Provides information about the port used by the QuickTime synthesizer.
 */
public final class QTMIDIPort extends SynthesizerConnections {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 96;

    QTMIDIPort() {
        super(kNativeSize);
    }

    private QTMIDIPort(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    /**
     * Return the name of the output port.
     */
    public String getPortName() throws UtilException {
        return getPStringAt(32);
    }

    /**
     * Set the name of the output port.
     */
    public void setPortName(String paramString) throws UtilException {
        setPStringAt(32, 63, paramString);
    }

    /**
     * Return a copy of this object.
     */
    @Override
    public Object clone() {
        return new QTMIDIPort(this.bytes);
    }
}
