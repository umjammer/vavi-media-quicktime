package quicktime.app.spaces;

/**
 * Deprecated.
 * The DynamicCollection uses the Protocol class to enable
 * the application to dynamically type members on a collection
 * by collection basis. Dynamic collections use the protocol to
 * test to see if an object is appropriate before it is accepted
 * as a candidate for membership of the collection.
 *
 * This enables an application to tighten the restrictions on membership
 * on a case by case basis. For example, the DirectGroup is a DynamicCollection,
 * of which the fundamental requirement is that the member implement the
 * QTDrawable interface. However if a particular application wanted to ensure that
 * for a particular group only movies were in the collection, the application could
 * get the Protocol object for that particular DirectGroup and add the MoviePlayer
 * class to the collection of class objects that make up the Protocol. Consequent
 * uses of that direct group would ensure that the object being added was both a
 * QTDrawable and MoviePlayer before the object was added. The application (having
 * specified this protocol) can then make certain assumptions about the type of object
 * that is a member of the collection.
 *
 * Changing a protocol after a member has been added to the collection will not mean that
 * existing members are removed if they do not match the new protocol, thus protocols should
 * only be specified when the collection is empty.
 * @see {@link quicktime.app.spaces.Protocol}
 */
public abstract interface DynamicCollection extends Collection {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the Protocol class that is used to when testing
     * an object's appropriateness for membership of a collection.
     * @return a Protocol object.
     */
    public abstract Protocol getProtocol();
}

