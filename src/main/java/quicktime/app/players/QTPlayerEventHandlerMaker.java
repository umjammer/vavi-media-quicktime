package quicktime.app.players;

/**
 * Deprecated.
 * Objects that implement this interface return a QTPlayerEventHandler
 * when the make method is called based on their particular
 * ability to receive events from the addedToInterest source
 * and handle events for the particular player.
 */
public abstract interface QTPlayerEventHandlerMaker {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return an event handler for the specified player where the
     * addedToInterest object is the object that will be the source
     * of events for that player. If either the player or interest
     * is not appropriate then this method should return null.
     * @param p the QTPlayer
     * @param addedToInterest the object that the QTPlayer has been
     *  added to
     * @return a QTPlayerEventHandler or null if can't handle events
     *  from either the specified source or the specified player.
     */
    public abstract QTPlayerEventHandler make(QTPlayer paramQTPlayer, Object paramObject);
}

