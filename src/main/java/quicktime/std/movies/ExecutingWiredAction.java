package quicktime.std.movies;

/**
 * Classes that implement this interface can be installed into a given movie and
 * receive notification when their wired actions are executed
 */
public abstract interface ExecutingWiredAction {

    /**
     * The exceute method is called by the Movie toolbox when a wired action is executed
     * @param mov the Movie that contains the wired actions
     * @param flags the flags of the movie specifying execution settings
     * @param actionContainer the container encapuslating the wired actions
     * @see {@link quicktime.std.movies.Movie#addExecuteWiredActionsProc(quicktime.std.movies.ExecutingWiredAction)}
     */
    public abstract short execute(Movie paramMovie, int paramInt, AtomContainer paramAtomContainer);
}

