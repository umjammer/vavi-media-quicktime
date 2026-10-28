package quicktime.std.movies;

public interface NextTaskNeededSooner {

    /**
     * The execute method is called by the idle manager if QuickTime needs tasking sooner than anticipated
     *  @return an error code or zero if no error
     *  @see "quicktime.std.movies#setNextTaskNeededSoonerCallback(quicktime.std.movies.NextTaskNeededSooner)"
     */
    int execute(int paramInt1, int paramInt2);
}
