package quicktime.std.music;

import quicktime.std.StdQTException;
import quicktime.util.QTHandleRef;

/**
 * Provides a list of ports used by the QuickTime synthesizer.
 */
public final class QTMIDIPortList extends QTHandleRef implements Cloneable {

    QTMIDIPortList(int paramInt) {
        super(paramInt, null, false);
    }

    /**
     * Return the name of the output port.
     */
    public int getLength() {
        return getShortAt(0);
    }

    /**
     * Return a port at the specified (one based) index.
     * @param index the index which contains the port to return.
     * @return a QTMIDIPort found at the specified index.
     * @throws quicktime.std.StdQTException
     */
    public QTMIDIPort getPort(int index) throws StdQTException {
        if (index > getShortAt(0))
            StdQTException.checkError(63461);
        index--;
        QTMIDIPort localQTMIDIPort = new QTMIDIPort();
        byte[] arrayOfByte = localQTMIDIPort.getBytes();
        int i = 2 + index * 96;
        for (int j = 0; j < 96; j++) {
            arrayOfByte[j] = getByteAt(j + i);
        }
        return localQTMIDIPort;
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new QTMIDIPortList(makeAndCopyHandle());
    }

    /**
     * String representation.
     */
    public String toString() {
        return getClass().getName() + "[numberOfPorts=" + getLength() + "]";
    }
}
