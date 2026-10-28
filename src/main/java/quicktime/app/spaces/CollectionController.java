package quicktime.app.spaces;

/**
 * @deprecated
 * Provides a generic interface for objects that want to control collections
 * of objects and have these objects added to the controller when
 * the objects are added to the space.
 *
 * When a Space has an object added to it (or a CollectionController is added to
 * a Space) and the isWholespace returns true, then all members of the space
 * will be added to the CollectionController. It is up to the CollectionController
 * itself to determine through its protocol if the object is an appropriate
 * object to be controlled. If not the object should not be added to the Controller.
 *
 * If the value of the wholespace setting is false, then it is the responsibility
 * of the application to explicitly add those objects that are to be controlled
 * by this controller.
 *
 * Once the controller has been added to a Space, changing the wholespace state of the
 * controller is NOT guaranteed to either remove or add existing members of
 * the controller - in fact changing this setting will only effect members that are
 * added to the space after this change - the existing members that have been added
 * to the controller will not be removed by the Space.
 *
 * The CollectionController extends Controller, which provides the notification
 * of when the Controller is added to a Space and also to be notified when the Space
 * is addedTo the object that is the ultimate source of interests.
 *
 * The CollectionController extends Collection which is how the Space
 * adds and removes the members from the controller.
 */
public interface CollectionController extends Controller, Collection {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current setting of the wholespace flag.
     * @return true if any member of the Space is a candidate for being controlled
     *  by this controller. If false then objects must be explicitly added to
     *  the space.
     */
    boolean isWholespace();
}
