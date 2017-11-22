
package quicktime.std.movies;

public final class Atom {
    public static final Atom kParentIsContainer = new Atom(0);

    int atom;

    public Atom(int paramInt) {
        this.atom = paramInt;
    }

    public int getAtom() {
        return this.atom;
    }

    public String toString() {
        return getClass().getName() + "[atom=" + this.atom + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/Atom.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
