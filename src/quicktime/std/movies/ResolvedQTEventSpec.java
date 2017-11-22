
package quicktime.std.movies;

import quicktime.QTException;
import quicktime.util.QTPointerRef;


public final class ResolvedQTEventSpec extends QTPointerRef {
    public static final int kNativeSize = 16;

    ResolvedQTEventSpec(int paramInt, Object paramObject) {
        super(paramInt, 16, paramObject);
    }

    public AtomContainer getContainer() {
        return new AtomContainer(getIntAt(0), this, false);
    }

    public Atom getAtom() {
        int i = getIntAt(4);
        return i == 0 ? null : new Atom(i);
    }

    public Track getTargetTrack() throws QTException {
        return new Track(getIntAt(8), this);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/ResolvedQTEventSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
