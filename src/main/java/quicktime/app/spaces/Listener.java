package quicktime.app.spaces;

/**
 * @deprecated
 * Objects that implement this interface are declaring an interest that
 * is derived or sourced from the object that is supplied as the argument
 * to the addedTo interest. For instance java.awt.Components are a source
 * for events that may be used to control the playback of a movie. One such
 * application of this interface is for objects that are set as the client
 * of a QTCanvas to be notified when they have been added to the canvas.
 *
 * However this interface is more general and can be used to specify a variety
 * of situations and applications depending on the container that the implementor
 * of this interest is added to.
 *
 * The Listener object can cast the incoming source object to an expected class
 * - if the source object is not of that type a runtime exception is generated.
 * Generally the container and member relationship will enforce the appropriateness
 * of a member for its container.
 */
public interface Listener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by some kind of container object when the Listener
     * is added to the object that is the source of the interest.
     * @param source the object that is to be the source of interest for the
     *  the Listener object.
     */
    void addedTo(Object source);

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the specified object when the Listener object
     * is removed from the object that is the source of the interest.
     * @param source the object that is to be the source of interest for the
     *  the Listener object.
     */
    void removedFrom(Object source);
}

