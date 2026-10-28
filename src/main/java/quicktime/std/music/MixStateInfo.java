package quicktime.std.music;

/**
 * Contains information about the mix state of a specified part.
 */
public class MixStateInfo {

    /**
     * The part number the information pertains too.
     */
    public final int partNumber;

    /**
     * The volume of that part.
     */
    public final float volume;

    /**
     * The balance of that part.
     */
    public final float balance;

    /**
     * The mix flags of that part.
     */
    public final int flags;

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
