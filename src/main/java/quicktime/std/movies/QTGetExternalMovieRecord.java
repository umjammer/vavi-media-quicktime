package quicktime.std.movies;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.util.QTPointerRef;


public final class QTGetExternalMovieRecord extends QTPointerRef implements quicktime.jdirect.PrimitivesLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 20;

    QTGetExternalMovieRecord(int nativeRep, Object ref) {
        super(nativeRep, 20, ref);
    }

    /**
     * Return the target Type associated with the event
     */
    public int getTargetType() {
        return getIntAt(0);
    }

    /**
     * Return the movie name associated with the event
     */
    public String getMovieName() {
        int i = getIntAt(4);
        int j = getByteFromPointer(i, 0);
        byte[] arrayOfByte = new byte[j];
        copyPointerToArray(i, 1, arrayOfByte, 0, j);
        return new String(arrayOfByte);
    }

    /**
     * Return the movie ID
     */
    public int getMovieID() {
        return getIntAt(8);
    }

    /**
     * Return the movie associated with the event
     */
    public Movie getMovie() {
        return new Movie(getIntAt(12), this);
    }

    /**
     * Return the movie controller
     */
    public MovieController getMovieController() {
        return new MovieController(getIntAt(16), this);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static byte getByteFromPointer(int ptr, int offset) {
        return MemoryManager.getByteFromPointer(ptr, offset);
    }
}
