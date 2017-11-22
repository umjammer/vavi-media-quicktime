
package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;


public final class SourcerInitParams extends QTPointer {
    public static final int kNativeSize = 20;

    SourcerInitParams() throws QTException {
        super(20, true);
    }

    SourcerInitParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    public SourcerInitParams(int paramInt1, int paramInt2, QTPointer paramQTPointer) throws QTException {
        this(1, paramInt1, paramInt2, paramQTPointer);
    }

    public SourcerInitParams(int paramInt1, int paramInt2, int paramInt3, QTPointer paramQTPointer) throws QTException {
        super(20, true);
        setVersion(paramInt1);
        setFlags(paramInt2);
        setDataType(paramInt3);
        setData(paramQTPointer);
    }

    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getVersion() {
        return getIntAt(0);
    }

    public void setFlags(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getFlags() {
        return getIntAt(4);
    }

    public void setDataType(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getDataType() {
        return getIntAt(8);
    }

    public void setData(QTPointer paramQTPointer) {
        if (paramQTPointer != null) {
            setIntAt(12, QTObject.ID(paramQTPointer));
            setIntAt(16, paramQTPointer.getSize());
        } else {
            setIntAt(12, 0);
            setIntAt(16, 0);
        }
        this.sourcerData = paramQTPointer;
    }

    public QTPointer getData() {
        return this.sourcerData;
    }

    private QTPointer sourcerData = null;

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",dataType="
               + QTUtils.fromOSType(getDataType()) + ",data" + getData() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /SourcerInitParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
