
package quicktime.streaming;

import quicktime.QTObject;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;


public final class ExportParams extends QTByteObject {
    public static final int kNativeSize = 40;

    ExportParams() {
        super(40);
    }

    public ExportParams(int paramInt1,
            int paramInt2,
            QTPointer paramQTPointer1,
            int paramInt3,
            QTPointer paramQTPointer2,
            QTPointer paramQTPointer3,
            int paramInt4,
            int paramInt5) {
        super(40);
        setVersion(paramInt1);
        setExportType(paramInt2);
        setDestinationContainerType(paramInt3);
        setDestinationContainerData(paramQTPointer2);
        setDestinationContainerExtras(paramQTPointer3);
        setFlagsIn(paramInt4);
        setFlagsOut(paramInt5);
    }

    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getVersion() {
        return getIntAt(0);
    }

    public void setExportType(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getExportType() {
        return getIntAt(4);
    }

    public void setDestinationContainerType(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getDestinationContainerType() {
        return getIntAt(12);
    }

    public void setDestinationContainerData(QTPointer paramQTPointer) {
        if (paramQTPointer != null) {
            setIntAt(16, QTObject.ID(paramQTPointer));
        } else
            setIntAt(16, 0);
        this.destData = paramQTPointer;
    }

    public QTPointer getDestinationContainerData() {
        return this.destData;
    }

    public void setDestinationContainerExtras(QTPointer paramQTPointer) {
        if (paramQTPointer != null) {
            setIntAt(20, QTObject.ID(paramQTPointer));
        } else
            setIntAt(20, 0);
        this.destExtras = paramQTPointer;
    }

    public QTPointer getDestinationContainerExtras() {
        return this.destExtras;
    }

    public void setFlagsIn(int paramInt) {
        setIntAt(24, paramInt);
    }

    public int getFlagsIn() {
        return getIntAt(24);
    }

    public void setFlagsOut(int paramInt) {
        setIntAt(28, paramInt);
    }

    public int getFlagsOut() {
        return getIntAt(28);
    }

    private QTPointer destData = null;

    private QTPointer destExtras = null;

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",exportType=" + QTUtils.fromOSType(getExportType())
               + ",destinationContainerType" + QTUtils.fromOSType(getDestinationContainerType()) + ",destinationContainerData"
               + getDestinationContainerData() + ",desintationContainerExtras" + getDestinationContainerExtras() + ",flagsIn"
               + getFlagsIn() + ",flagsOut" + getFlagsOut() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /ExportParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
