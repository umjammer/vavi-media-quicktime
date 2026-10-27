package quicktime.app.actions;

/**
 * @deprecated
 * The Notifier and NotifyListener interfaces provide a way for objects
 * to deal with some state that is not available immediately but can
 * be acquired. The Notifier represents the class that is waiting for
 * some condition to be set or established. When this condition is met
 * it notifies its listener that the state is established.
 *
 * The kind of classes that interact this way generally have some interdependence
 * and so the NotifyListener will generally expect its Notifier to be a
 * a particular type of object.
 *
 * When a NotifyListener is added to a Notifier if the NotifyListener and Notifier
 * are compatible the addNotifyListener returns true, at that point the Notifier
 * can notify its Listener when the condition it is awaiting is fulfilled.
 *
 * For instance, the QTImageDrawer is a Notifier and the TwoDSprite is a NotifyListener.
 * A TwoDSprite requires valid image data before it can present an image in a SWCompositor.
 * A QTImageDrawer does not have valid image data until it has been added to a java.awt.Component.
 * Thus the QTImageDrawer has a need to notify its TwoDSprite when its image data is complete
 * and valid. The TwoDSprite is a NotifyListener that expects its Notifiers to also implement
 * the ImageSpec interface. When the QTImageDrawer has valid image data it notifies its
 * TwoDSprite listener and the TwoDSprite can retrieve that image data and present it
 * within the context of its Compositor.
 *
 * This type of pairing can be used in other situations where some deferral of resources
 * is an issue. For example a class could be defined that downloads image data through a
 * network connection. When the image data is completely retrieved this type of Notifier
 * could then notify a TwoDSprite (for instance) that its image data is now complete.
 * @see quicktime.app.image.QTImageDrawer,
 * {@link quicktime.app.anim.TwoDSprite}
 */
public interface Notifier {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called at some point when a relationship between a Notifier
     * and a listener is required.
     * @param nl the NotifyListener that is interested in something from this Notifier
     * @return true if the Notifer is an appropriate Notifier for the Listener
     */
    boolean addNotifyListener(NotifyListener nl);
}

