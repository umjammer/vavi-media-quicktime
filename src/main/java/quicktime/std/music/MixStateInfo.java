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

    MixStateInfo(int partNumber, float volume, float balance, int flags) {
        this.partNumber = partNumber;
        this.volume = volume;
        this.balance = balance;
        this.flags = flags;
    }

    /**
     * String representation of class.
     */
    public String toString() {
        return getClass().getName() + "[partNumber=" + this.partNumber + ",volume=" + this.volume + ",balance=" + this.balance + ",flags=" + this.flags + "]";
    }
}
