
package quicktime.std.music;

import quicktime.std.StdQTException;
import quicktime.util.QTHandleRef;


public final class QTMIDIPortList extends QTHandleRef implements Cloneable {
    QTMIDIPortList(int paramInt) {
        super(paramInt, null, false);
    }

    public int getLength() {
        return getShortAt(0);
    }

    public QTMIDIPort getPort(int paramInt) throws StdQTException {
        if (paramInt > getShortAt(0))
            StdQTException.checkError(63461);
        paramInt--;
        QTMIDIPort localQTMIDIPort = new QTMIDIPort();
        byte[] arrayOfByte = localQTMIDIPort.getBytes();

        int i = 2 + paramInt * 96;
        for (int j = 0; j < 96; j++) {
            arrayOfByte[j] = getByteAt(j + i);
        }
        return localQTMIDIPort;
    }

    public Object clone() {
        return new QTMIDIPortList(makeAndCopyHandle());
    }

    public String toString() {
        return getClass().getName() + "[numberOfPorts=" + getLength() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /QTMIDIPortList.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
