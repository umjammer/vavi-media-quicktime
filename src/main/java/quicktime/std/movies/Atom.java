package quicktime.std.movies;

/**
 * Class that represents the QTAtom object
 */
public final class Atom {

    public static final Atom kParentIsContainer = new Atom(0);

    int atom;

    /**
     * Construct an Atom out of the constant value that defines this data type.
     */
    public Atom(int paramInt) {
        this.atom = paramInt;
    }

    /**
     * @return the current value of the atom
     */
    public int getAtom() {
        return this.atom;
    }

    /**
     * @return a String reprentation of this atom
     */
    public String toString() {
        return getClass().getName() + "[atom=" + this.atom + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/Atom.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
