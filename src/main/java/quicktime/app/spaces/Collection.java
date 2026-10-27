package quicktime.app.spaces;

import java.util.Enumeration;

import quicktime.QTException;


/**
 * This interface provide a uniform means of dealing with a collection
 * of Objects in QTJava. Particular implementations of this interface provide
 * both their own storage mechanism and semantics about the default position
 * and characteristics of the addMember method.
 * <p>
 * The isAppropriate method returns true if the object argument conforms to the
 * type of object that the Collection is a collector of. The addMember
 * implementation will call isAppropriate and returns true if the object is an
 * appropriate member (and is therefore added to the collection).
 * <p>
 * The individual collection classes explain the customizations and restrictions
 * on membership they enforce.
 * <p>
 * The type of object that a collection contains is statically determined by
 * the isAppropriate () method. The DynamicCollection extension of Collection
 * provides a mechanism for dynamically customizing the membership requirements
 * for individual collections.
 *
 * @see quicktime.app.spaces.DynamicCollection
 * @deprecated
 */
public interface Collection {

    /**
     * Adds an object to the collection. Returns true if the object is
     * appropriate member of the collection (and thus added) and false if not.
     *
     * @param member the object that should be added as a member to the collection
     * @return a boolean.
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    boolean addMember(Object member) throws QTException;

    /**
     * Returns true if the provided object is a member of the collection.
     *
     * @param member the object to test for membership
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    boolean hasMember(Object member);

    /**
     * Returns true if the provided object is an appropriate candidate for
     * membership in a collection. The provided QTJava collections use this method
     * to ensure a minimal requirement of Class or Interface instanceof tests to
     * ensure the type of object that can be added to a collection. Some collections
     * can have a protocol that allows an application to further refine and tighten
     * the type of object that is allowed within an individual collection.
     *
     * @param object the object to test
     * @return true if the supplied object is appropriate for this collection, or false if not
     * @see quicktime.app.spaces.DynamicCollection
     * @see quicktime.app.spaces.Protocol
     * @since QTJava 6.1
     * @deprecated
     */
    boolean isAppropriate(Object object);

    /**
     * Returns true if the collection contains no members, or false if
     * it has one or more members.
     *
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    boolean isEmpty();

    /**
     * Returns an enumeration to iterate over the current members of a collection.
     *
     * @return an enumeration
     * @since QTJava 6.1
     * @deprecated
     */
    Enumeration members();

    /**
     * If the object is a member of the collection it is removed.
     *
     * @param member the object to remove from the collection
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    void removeMember(Object member) throws QTException;

    /**
     * Returns the number of members in the collection, ie. the collection's size.
     *
     * @return the size of the collection.
     * @since QTJava 6.1
     * @deprecated
     */
    int size();
}

