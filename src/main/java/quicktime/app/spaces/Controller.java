package quicktime.app.spaces;

/**
 * @deprecated
 * A Controller is an object that exerts some control over members of
 * a Space. The Controller interface defines a minimal requirement for
 * all Controllers - that is they are notified whenever they are added or
 * removed from a Space. No particular functionality or modus operandi
 * of a controller is assumed.
 *
 * There are a number of interfaces that extend this interface that provide
 * a particular behaviour of the Controller - particularly in relationship
 * of the controller to its space. Those interfaces functionality
 * in terms of the requirements they have when they are added to a Space are
 * all supported in the SimpleSpace implementation. For example, TicklishControllers
 * provide a list of Ticklish objects that should be tickled by the Space's
 * Timer. When a TicklishController is added to the SimpleSpace the SimpleSpace
 * will tickle this controller.
 *
 * The extension interfaces provide their own documentation for the actions
 * they require. The provided controller interfaces should provide a comprehensive
 * set of requirements for describing the interaction between controllers and
 * spaces.
 * @see quicktime.app.spaces.Space
 */
public interface Controller {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the Space when a Controller is being addedTo the Space s.
     * If the Controller is not an appropriate controller for that kind of Space this
     * method can throw an exception and the controller won't be added to the Space.
     * @param s the Space being added to
     */
    void addedToSpace(Space paramSpace);

    /**
     * @deprecated
     * @since QTJava 6.1
     * Called by the Space when a Controller is being removed
     * from a Space. This is only called by the Space on controllers that have
     * previously been added, thus the space they are being removed from
     * is the space they were added to.
     */
    void removedFromSpace();
}

