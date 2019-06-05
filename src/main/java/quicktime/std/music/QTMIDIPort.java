
package quicktime.std.music;

import quicktime.util.UtilException;


public final class QTMIDIPort extends SynthesizerConnections {
    public static final int kNativeSize = 96;

    QTMIDIPort() {
        super(kNativeSize);
    }

    private QTMIDIPort(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    public String getPortName() throws UtilException {
        return getPStringAt(32);
    }

    public void setPortName(String paramString) throws UtilException {
        setPStringAt(32, 63, paramString);
    }

    public Object clone() {
        return new QTMIDIPort(this.bytes);
    }
}
