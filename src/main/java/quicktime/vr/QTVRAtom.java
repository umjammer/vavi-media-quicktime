package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTException;
import quicktime.util.QTByteObject;

public abstract class QTVRAtom extends QTByteObject implements Cloneable {

    protected QTVRAtom(int size) {
        super(size);
        setShortAt(0, (short) 2);
        setShortAt(2, (short) 0);
    }

    protected QTVRAtom(byte[] bytes, int size) throws QTException {
        super(bytes);
        if (bytes.length != size) {
            throw new QTException(-50);
        }
    }

    protected abstract int getNativeSize();

    /**
     * returns the QTVR majorVersion
     */
    public short getMajorVersion() {
        return getShortAt(0);
    }

    /**
     * sets the QTVR majorVersion
     */
    public void setMajorVersion(short majorVersion) {
        setShortAt(0, majorVersion);
    }

    /**
     * returns the QTVR minorVersion
     */
    public short getMinorVersion() {
        return getShortAt(2);
    }

    /**
     * sets the QTVR minorVersion
     */
    public void setMinorVersion(short minorVersion) {
        setShortAt(2, minorVersion);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName();
    }

    protected abstract void readObject(ObjectInputStream in) throws IOException;

    protected abstract void writeObject(ObjectOutputStream out) throws IOException;

    /**
     * Returns a copy of this class.
     */
    @Override
    public abstract Object clone();
}
