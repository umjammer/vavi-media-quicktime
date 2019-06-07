package quicktime.std.movies;

public abstract interface NextTaskNeededSooner {

    /**
     * The execute method is called by the idle manager if QuickTime needs tasking sooner than anticipated
     *  @return an error code or zero if no error
     *  @see quicktime.std.movies#setNextTaskNeededSoonerCallback (quicktime.std.movies.NextTaskNeededSooner)
     */
    public abstract int execute(int paramInt1, int paramInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/NextTaskNeededSooner.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
