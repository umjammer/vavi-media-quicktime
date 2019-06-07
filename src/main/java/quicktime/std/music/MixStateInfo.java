package quicktime.std.music;

/**
 * Contains information about the mix state of a specified part.
 */
public class MixStateInfo {

    /**
     * The part number the information pertains too.
     */
    public int partNumber;

    /**
     * The volume of that part.
     */
    public float volume;

    /**
     * The balance of that part.
     */
    public float balance;

    /**
     * The mix flags of that part.
     */
    public int flags;

    MixStateInfo(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2) {
        this.partNumber = paramInt1;
        this.volume = paramFloat1;
        this.balance = paramFloat2;
        this.flags = paramInt2;
    }

    /**
     * String representation of class.
     */
    public String toString() {
        return getClass().getName() + "[partNumber=" + this.partNumber + ",volume=" + this.volume + ",balance=" + this.balance + ",flags=" + this.flags + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /MixStateInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
