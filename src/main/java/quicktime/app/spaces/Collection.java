package quicktime.app.spaces;

import java.util.Enumeration;
import quicktime.QTException;

/**
 * Deprecated.
 * This interface provide an uniform means of dealing with a collection
 * of Objects in QTJava. Particular implementations of this interface provide
 * both their own storage mechanism and semantics about the default position
 * and characteristics of the addMember method.
 *
 * The isAppropriate method returns true if the object argument conforms to the
 * type of object that the Collection is a collector of. The addMember
 * implementation will call isAppropriate and returns true if the object is an
 * appropriate member (and is therefore added to the collection).
 *
 * The individual collection classes explain the customisations and restrictions
 * on membership they enforce.
 *
 * The type of object that a collection contains is statically determined by
 * the isAppropriate () method. The DynamicCollection extension of Collection
 * provides a mechanism for dynamically customising the membership requirments
 * for individual collections.
 * @see {@link quicktime.app.spaces.DynamicCollection}
 */
public abstract interface Collection {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds an object to the collection. Returns true if the object is
     * appropriate member of the collection (and thus added) and false if not.
     * @param member the object that should be added as a member to the collection
     * @return a boolean.
     * @return {@link quicktime.QTException}
     */
    public abstract boolean addMember(Object paramObject) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the provided object is a member of the collection.
     * @param member the object to test for membership
     * @return a boolean
     */
    public abstract boolean hasMember(Object paramObject);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the provided object is an appropriate candidate for
     * membership in a collection. The provided QTJava collections use this method
     * to ensure a minimal requirement of Class or Interface instanceof tests to
     * ensure the type of object that can be added to a collection. Some collections
     * can have a protocol that allows an application to further refine and tighten
     * the type of object that is allowed within an individual collection.
     * @param object the object to test
     * @return true if the supplied object is appropriate for this collection, or false if not
     * @see {@link quicktime.app.spaces.DynamicCollection},
     * {@link quicktime.app.spaces.Protocol}
     */
    public abstract boolean isAppropriate(Object paramObject);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the collection contains no members, or false if
     * it has one or more members.
     * @return a boolean
     */
    public abstract boolean isEmpty();

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns an enumeration to iterate over the current members of a collection.
     * @return an enumeration
     */
    public abstract Enumeration members();

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the object is a member of the collection it is removed.
     * @param member the object to remove from the collection
     * @param {@link quicktime.QTException}
     */
    public abstract void removeMember(Object paramObject) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the number of members in the collection, ie. the collection's size.
     * @return the size of the collection.
     */
    public abstract int size();
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * spaces/Collection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
