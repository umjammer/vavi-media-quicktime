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
    public SourcerLoopParams(int loopFlags, int flagsMask, int numLoops) throws QTException {
        this(1, 0, loopFlags, flagsMask, numLoops);
    }

    /**
     * Constructs a SourcerLoopParams object based on the native QTSLoopParams structure
     * @param vers version of the params structure
     * @param flags sourcer flags
     * @param loopFlags the loop flags of the sourcer object
     * @param flagsMask the mask of the flags to use for the sourcer
     * @param numLoops the number of loops for the sourcer
     */
    public SourcerLoopParams(int vers, int flags, int loopFlags, int flagsMask, int numLoops) throws QTException {
        super(20, true);
        setVersion(vers);
        setFlags(flags);
        setLoopFlags(loopFlags);
        setFlagsMask(flagsMask);
        setNumLoops(numLoops);
    }

    /**
     * Sets the version of the sourcer loop params object
     * @param vers version
     */
    public void setVersion(int vers) {
        setIntAt(0, vers);
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
    public void setFlags(int flags) {
        setIntAt(4, flags);
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
    public void setLoopFlags(int loopFlags) {
        setIntAt(8, loopFlags);
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
    public void setFlagsMask(int flagsMask) {
        setIntAt(12, flagsMask);
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
    public void setNumLoops(int numLoops) {
        setIntAt(16, numLoops);
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
