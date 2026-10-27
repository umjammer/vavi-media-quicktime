package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointer;

/**
 * This class provides a representation the native QTSLoopParams structure that
 * specifies looping behavior of the sourcer object
 */
public final class SourcerLoopParams extends QTPointer {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 20;

    SourcerLoopParams() throws QTException {
        super(20, true);
    }

    SourcerLoopParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    /**
     * Constructs a SourcerLoopParams object based on the native QTSLoopParams structure
     * @param loopFlags the loop flags of the sourcer object
     * @param flagsMask the mask of the flags to use for the sourcer
     * @param numLoops the number of loops for the sourcer
     */
    public SourcerLoopParams(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        this(1, 0, paramInt1, paramInt2, paramInt3);
    }

    /**
     * Constructs a SourcerLoopParams object based on the native QTSLoopParams structure
     * @param vers version of the params structure
     * @param flags sourcer flags
     * @param loopFlags the loop flags of the sourcer object
     * @param flagsMask the mask of the flags to use for the sourcer
     * @param numLoops the number of loops for the sourcer
     */
    public SourcerLoopParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws QTException {
        super(20, true);
        setVersion(paramInt1);
        setFlags(paramInt2);
        setLoopFlags(paramInt3);
        setFlagsMask(paramInt4);
        setNumLoops(paramInt5);
    }

    /**
     * Sets the version of the sourcer loop params object
     * @param vers version
     */
    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * Gets the version of the sourcer loop params object
     * @return version
     */
    public int getVersion() {
        return getIntAt(0);
    }

    /**
     * Sets the flags associated with the sourcer loop params object
     * @param flags flags
     */
    public void setFlags(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * Gets the flags of the sourcer loop params object
     * @return flags
     */
    public int getFlags() {
        return getIntAt(4);
    }

    /**
     * Sets the loop flags of the sourcer loop params object
     * @param loopFlags loop flags
     */
    public void setLoopFlags(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * Gets the loop flags
     * @return loop flags
     */
    public int getLoopFlags() {
        return getIntAt(8);
    }

    /**
     * Sets the flags mask of the sourcer loop params object
     * @param flagsMask mask for the sourcer loop flags
     */
    public void setFlagsMask(int paramInt) {
        setIntAt(12, paramInt);
    }

    /**
     * Gets the flags mask
     */
    public int getFlagsMask() {
        return getIntAt(12);
    }

    /**
     * Sets the number of loops of the sourcer loop params object
     * @param numLoops number of loops
     */
    public void setNumLoops(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * Gets the number of loops
     */
    public int getNumLoops() {
        return getIntAt(16);
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",loopFlags=" + getLoopFlags() + ",flagsMask" + getFlagsMask() + ",numLoops" + getFlagsMask() + "]";
    }
}
