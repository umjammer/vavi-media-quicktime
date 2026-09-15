package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTException;
import quicktime.util.QTByteObject;

public abstract class QTVRAtom extends QTByteObject implements Cloneable {

    protected QTVRAtom(int paramInt) {
        super(paramInt);
        setShortAt(0, (short) 2);
        setShortAt(2, (short) 0);
    }

    protected QTVRAtom(byte[] paramArrayOfByte, int paramInt) throws QTException {
        super(paramArrayOfByte);
        if (paramArrayOfByte.length != paramInt) {
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
    public void setMajorVersion(short paramShort) {
        setShortAt(0, paramShort);
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
    public void setMinorVersion(short paramShort) {
        setShortAt(2, paramShort);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName();
    }

    protected abstract void readObject(ObjectInputStream paramObjectInputStream) throws IOException;

    protected abstract void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException;

    /**
     * Returns a copy of this class.
     */
    public abstract Object clone();
}

