package quicktime.std.movies;

import quicktime.QTException;
import quicktime.util.QTPointerRef;

/**
 * This class is returned from ActionFilters that deal with WiredSprite actions.
 * @see quicktime.std.movies.actions.ExecuteQTEventAction
 */
public final class ResolvedQTEventSpec extends QTPointerRef {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 16;

    ResolvedQTEventSpec(int paramInt, Object paramObject) {
        super(paramInt, 16, paramObject);
    }

    /**
     * Return the container associated with the event
     */
    public AtomContainer getContainer() {
        return new AtomContainer(getIntAt(0), this, false);
    }

    /**
     * Return the atom associated with the event
     */
    public Atom getAtom() {
        int i = getIntAt(4);
        return i == 0 ? null : new Atom(i);
    }

    /**
     * Return the Track associated with the event
     */
    public Track getTargetTrack() throws QTException {
        return new Track(getIntAt(8), this);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/ResolvedQTEventSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
