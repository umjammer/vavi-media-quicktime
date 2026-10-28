package quicktime.streaming;

import quicktime.QTObject;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;

/**
 * This class provides a representation the native ExportParams structure that provides a collection
 * of export parameters
 */
public final class ExportParams extends QTByteObject {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 40;

    ExportParams() {
        super(kNativeSize);
    }

    /**
     * Constructs an ExportParams object based on the native ExportParams structure
     * @param vers version of the params structure
     * @param expType export type of the data
     * @param extraData export extra data
     * @param targetType type of the target container
     * @param targData target container data
     * @param flagsIn input flags
     * @param flagsOut output flags
     */
    public ExportParams(int vers, int expType, QTPointer paramQTPointer1, int targetType, QTPointer targData, QTPointer extraData, int flagsIn, int flagsOut) {
        super(kNativeSize);
        setVersion(vers);
        setExportType(expType);
        setDestinationContainerType(targetType);
        setDestinationContainerData(targData);
        setDestinationContainerExtras(extraData);
        setFlagsIn(flagsIn);
        setFlagsOut(flagsOut);
    }

    /**
     * Sets the version of the export params object
     * @param vers version
     */
    public void setVersion(int vers) {
        setIntAt(0, vers);
    }

    /**
     * Gets the version of the export params object
     * @return version
     */
    public int getVersion() {
        return getIntAt(0);
    }

    /**
     * Sets the type of the export object
     * @param type export type
     */
    public void setExportType(int type) {
        setIntAt(4, type);
    }

    /**
     * Gets the export type
     */
    public int getExportType() {
        return getIntAt(4);
    }

    /**
     * Sets the type of the target container
     * @param type target container type
     */
    public void setDestinationContainerType(int type) {
        setIntAt(12, type);
    }

    /**
     * Gets the type of the target container
     */
    public int getDestinationContainerType() {
        return getIntAt(12);
    }

    /**
     * Sets the target container data
     * @param data target container data
     */
    public void setDestinationContainerData(QTPointer data) {
        if (data != null) {
            setIntAt(16, QTObject.ID(data));
        } else
            setIntAt(16, 0);
        this.destData = data;
    }

    /**
     * Gets the target container data
     */
    public QTPointer getDestinationContainerData() {
        return this.destData;
    }

    /**
     * Sets the destination container extras
     * @param data container extras data
     */
    public void setDestinationContainerExtras(QTPointer data) {
        if (data != null) {
            setIntAt(20, QTObject.ID(data));
        } else
            setIntAt(20, 0);
        this.destExtras = data;
    }

    /**
     * Gets the target container data
     */
    public QTPointer getDestinationContainerExtras() {
        return this.destExtras;
    }

    /**
     * Sets the input flags
     * @param flags the input flags
     */
    public void setFlagsIn(int flags) {
        setIntAt(24, flags);
    }

    /**
     * Gets the input flags
     */
    public int getFlagsIn() {
        return getIntAt(24);
    }

    /**
     * Sets the output flags
     * @param flags output flags
     */
    public void setFlagsOut(int flags) {
        setIntAt(28, flags);
    }

    /**
     * Gets output flags
     */
    public int getFlagsOut() {
        return getIntAt(28);
    }

    private QTPointer destData = null;

    private QTPointer destExtras = null;

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() +
                "[version=" + getVersion() +
                ",exportType=" + QTUtils.fromOSType(getExportType()) +
                ",destinationContainerType" + QTUtils.fromOSType(getDestinationContainerType()) +
                ",destinationContainerData" + getDestinationContainerData() +
                ",desintationContainerExtras" + getDestinationContainerExtras() +
                ",flagsIn" + getFlagsIn() +
                ",flagsOut" + getFlagsOut() +
                "]";
    }
}
