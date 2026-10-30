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

    SourcerInitParams(byte[] byteArray) throws QTException {
        super(byteArray);
    }

    /**
     * Constructs a version 1 SourcerInitParams object based on the native QTSourcerInitParams structure
     * @param flags export flags
     * @param dataType the data type of the sourcer object
     * @param data the data to use with the sourcer
     */
    public SourcerInitParams(int flags, int dataType, QTPointer data) throws QTException {
        this(1, flags, dataType, data);
    }

    /**
     * Constructs a SourcerInitParams object based on the native QTSourcerInitParams structure
     * @param vers version of the params structure
     * @param flags export flags
     * @param dataType the data type of the sourcer object
     * @param data the data to use with the sourcer
     */
    public SourcerInitParams(int vers, int flags, int dataType, QTPointer data) throws QTException {
        super(20, true);
        setVersion(vers);
        setFlags(flags);
        setDataType(dataType);
        setData(data);
    }

    /**
     * Sets the version of the sourcer init params object
     * @param vers version
     */
    public void setVersion(int vers) {
        setIntAt(0, vers);
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
    public void setFlags(int flags) {
        setIntAt(4, flags);
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
    public void setDataType(int type) {
        setIntAt(8, type);
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
    public void setData(QTPointer data) {
        if (data != null) {
            setIntAt(12, QTObject.ID(data));
            setIntAt(16, data.getSize());
        } else {
            setIntAt(12, 0);
            setIntAt(16, 0);
        }
        this.sourcerData = data;
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
