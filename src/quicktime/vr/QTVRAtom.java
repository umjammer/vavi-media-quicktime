
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

    public short getMajorVersion() {
        return getShortAt(0);
    }

    public void setMajorVersion(short paramShort) {
        setShortAt(0, paramShort);
    }

    public short getMinorVersion() {
        return getShortAt(2);
    }

    public void setMinorVersion(short paramShort) {
        setShortAt(2, paramShort);
    }

    public String toString() {
        return getClass().getName();
    }

    protected abstract void readObject(ObjectInputStream paramObjectInputStream) throws IOException;

    protected abstract void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException;

    public abstract Object clone();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRAtom.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
