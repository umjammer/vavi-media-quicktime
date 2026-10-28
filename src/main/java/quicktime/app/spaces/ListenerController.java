package quicktime.app.spaces;

/**
 * @deprecated
 * A ListenerController is a controller that is generally invoked
 * or made active by events or notificiations from some source
 * object. For example the MouseController is a ListenerController
 * - it is activated when its source object (a java.awt.Component)
 * broadcasts a mouse event to its listeners. The MouseController
 * once activated will act upon the members of the Space over
 * which it exerts some kind of control, example dragging a visual
 * object around the screen.
 *
 * A ListenerController when added to a Space will be notified when
 * the Space is added to the object that is to be the source of events
 * for that Space. The Space will then ensure that the addedTo and
 * removedFrom methods of the Listener interface are called on
 * controllers of this type.
 */
public interface ListenerController extends Controller, Listener {
}
