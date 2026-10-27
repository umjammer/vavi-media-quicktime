package quicktime.app.spaces;

import java.util.Enumeration;
import quicktime.QTException;
import quicktime.app.time.Ticklish;
import quicktime.app.time.Timer;

/**
 * @deprecated
 * A Space is a dynamic collection of members. A Space also has
 * a Timer and a collection of Controllers. The Timer
 * provides the parent TimeBase for all members and controllers
 * within the Space that are themselves Spaces or objects that
 * have a TimeBase. The getSource method returns the source
 * object which the Space has been addedTo (ie. the Space is also
 * a Listener).
 *
 * The tickleList is the list of TicklishControllers that have been
 * added to ths Space and will be tickled by the Space when the Space
 * itself is tickled by its Timer.
 *
 * A default implementation of the Space interface is provided in
 * the SimpleSpace class - refer to the documentation for details
 * of that implementation.
 * @see quicktime.app.spaces.SimpleSpace
 */
public interface Space extends Listener, DynamicCollection, Ticklish {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is used by the application to add a Controller to
     * the Space. A Controller will control members of a Space.
     * @param c the Controller to add to the Space.
     * @throws quicktime.QTException
     */
    void addController(Controller paramController) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Removes a controller that was previously added to the Space.
     * @param c the Controller to remove from the Space.
     * @throws quicktime.QTException
     */
    void removeController(Controller paramController) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns an Enumeration of all Controllers that are currently
     * attached to the Space.
     * @return an Enumeration
     */
    Enumeration controllers();

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns an Enumeration of all of the Controllers that implement the Ticklish interface
     * (TicklishControllers) that have been added to the Space. These controllers are tickled
     * each time the Space itself is tickled by its Timer.
     * @return an Enumeration of Ticklish objects
     */
    Enumeration tickleList();

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the Timer of the Space. This Timer provids a TimeBase for the Space and
     * if active will tickle and send timeChanged calls to the TicklishControllers that
     * are attached to the Space.
     * @return a Timer
     */
    Timer getTimer();

    /**
     * @deprecated
     * @since QTJava 6.1
     * This object is the source for all of the members and controllers of the Space that
     * implement the Listener interface. All such members and controllers will have the same
     * source as the parent space when it is addedTo a Listener source. If the parent Space
     * has not been added to a Listener source this returns null.
     * @return the Listener source object
     */
    Object getSource();
}

