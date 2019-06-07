package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;

/**
 * This class provides a representation the native QTSSourcerInitParams structure that provides a collection
 * of sourcer initialization parameters
 */
public final class SourcerInitParams extends QTPointer {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 20;

    SourcerInitParams() throws QTException {
        super(20, true);
    }

    SourcerInitParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    /**
     * Constructs a version 1 SourcerInitParams object based on the native QTSourcerInitParams structure
     * @param flags export flags
     * @param dataType the data type of the sourcer object
     * @param data the data to use with the sourcer
     */
    public SourcerInitParams(int paramInt1, int paramInt2, QTPointer paramQTPointer) throws QTException {
        this(1, paramInt1, paramInt2, paramQTPointer);
    }

    /**
     * Constructs a SourcerInitParams object based on the native QTSourcerInitParams structure
     * @param vers version of the params structure
     * @param flags export flags
     * @param dataType the data type of the sourcer object
     * @param data the data to use with the sourcer
     */
    public SourcerInitParams(int paramInt1, int paramInt2, int paramInt3, QTPointer paramQTPointer) throws QTException {
        super(20, true);
        setVersion(paramInt1);
        setFlags(paramInt2);
        setDataType(paramInt3);
        setData(paramQTPointer);
    }

    /**
     * Sets the version of the sourcer init params object
     * @param vers version
     */
    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * Gets the version of the sourcer init params object
     * @return version
     */
    public int getVersion() {
        return getIntAt(0);
    }

    /**
     * Sets the flags associated with the sourcer init params object
     * @param flags flags
     */
    public void setFlags(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * Gets the flags of the sourcer init params object
     * @return flags
     */
    public int getFlags() {
        return getIntAt(4);
    }

    /**
     * Sets the data type of the sourcer object
     * @param type sourcer data type
     */
    public void setDataType(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * Gets the data type
     */
    public int getDataType() {
        return getIntAt(8);
    }

    /**
     * Sets the target container data
     * @param data target container data
     */
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

    /**
     * Gets the target container data
     */
    public QTPointer getData() {
        return this.sourcerData;
    }

    private QTPointer sourcerData = null;

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",dataType=" + QTUtils.fromOSType(getDataType()) + ",data" + getData() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /SourcerInitParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
