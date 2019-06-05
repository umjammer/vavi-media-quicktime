
package quicktime.vr;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.QTByteObject;
import quicktime.util.QTHandle;


public class CursorRecord extends QTByteObject implements PrimitivesLib {
    private static final int kNativeSize = 8;

    public CursorRecord() {
        super(8);
    }

    public CursorRecord(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
        if (paramArrayOfByte.length != 8) {
            throw new QTException(-50);
        }
    }

    public short getType() {
        return getShortAt(0);
    }

    public void setType(short paramShort) {
        setShortAt(0, paramShort);
    }

    public short getCursorID() {
        return getShortAt(2);
    }

    public void setCursorID(short paramShort) {
        setShortAt(2, paramShort);
    }

    public QTHandle getData() throws QTException {
        int i = getIntAt(4);
        int j = getIntFromHandle(i, 0);
        QTHandle localQTHandle = new QTHandle();

        setIntInHandle(QTObject.ID(localQTHandle), 0, j);
        return localQTHandle;
    }

    public void setData(QTHandle paramQTHandle) {
        int i = 0;
        if (paramQTHandle != null) {
            i = getIntFromHandle(QTObject.ID(paramQTHandle), 0);
        } else
            i = 0;
        setIntAt(4, i);
    }

    private static native void setIntInHandle(int paramInt1, int paramInt2, int paramInt3);

    private static native int getIntFromHandle(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * CursorRecord.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
