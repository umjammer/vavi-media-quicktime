package quicktime.app.spaces;

import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * A Protocol is a collection of classes that are used to test
 * an object against. The passProtocol (Object o) method
 * uses the Class.instanceOf method on each of the classes
 * in its collection. If the incoming object is not an instance
 * of any of the classes in the Protocol then the passProtocol
 * method returns false. If the incoming object is then it returns
 * true.
 * <p>
 * For DynamicCollections an application can dynamically assign or
 * add classes to a Protocol to tighten the membership requirements
 * of its collection. When an object is added to a dynamic collection
 * the dynamic collection will first test the object against its
 * Protocol's passProtocol method. Only if it passes this test is the
 * object allowed to be added to the Collection. This test is
 * encapsulated in the DynamicCollection's isAppropriate method.
 *
 * @see quicktime.app.spaces.DynamicCollection
 * @deprecated
 */
public class Protocol implements Collection<Class<?>> {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public Protocol(Class[] classes) {
        if (classes != null) {
            this.defaultProtocolLimit = classes.length;
            for (int i = 0; i < this.defaultProtocolLimit; i++) {
                this.protocolsVec.addElement(classes[i]);
            }
        }
    }

    /**
     * Creates a Protocol from the supplied class. These default class cannot be
     * removed from this Protocol once created.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public Protocol(Class<?> clazz) {
        if (clazz != null)
            this.protocolsVec.addElement(clazz);
    }

    private final Vector<Class<?>> protocolsVec = new Vector<>();

    private int defaultProtocolLimit = 0;

    /**
     * Returns an Enumeration that enables you to examine the default classes for
     * this Protocol.
     *
     * @return an Enumeration
     * @since QTJava 6.1
     * @deprecated
     */
    public Enumeration<Class<?>> defaultProtocol() {
        if (this.defaultProtocolLimit > 0) {
            Vector<Class<?>> vector = new Vector<>();
            for (int i = 0; i < this.defaultProtocolLimit; i++) vector.addElement(this.protocolsVec.elementAt(i));
            return vector.elements();
        }
        return null;
    }

    /**
     * Returns an Enumeration to iterate over all of the members of the Protocol object.
     *
     * @return an Enumeration
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public Enumeration<Class<?>> members() {
        return this.protocolsVec.elements();
    }

    /**
     * Returns true if the Protocol has no members, otherwise false
     *
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isEmpty() {
        return this.protocolsVec.isEmpty();
    }

    /**
     * Returns true if the supplied class object is a member of this Protocol.
     *
     * @param object the object to test for membership
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean hasMember(Class<?> object) {
        return this.protocolsVec.contains(object);
    }

    /**
     * Returns the number of class objects that are in this Protocol.
     *
     * @return an int
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public int size() {
        return this.protocolsVec.size();
    }

    /**
     * Adds a class object to this Protocol. The application can later remove any class objects
     * that are added to a Protocol in this manner
     *
     * @param member the object that should be added as a member to the collection
     * @return true if the class object was successfully added (or is already a member)
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean addMember(Class<?> member) {
        if (!isAppropriate(member))
            return false;
        if (!hasMember(member))
            this.protocolsVec.addElement(member);
        return true;
    }

    /**
     * Removes a class object that was previously added to this Protocol. The application cannot
     * remove class objects that are part of the default protocol of this Protocol.
     *
     * @param member the object to remove from the collection
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void removeMember(Class<?> member) {
        int i = this.protocolsVec.indexOf(member);
        if (i >= this.defaultProtocolLimit) {
            this.protocolsVec.removeElement(member);
        }
    }

    /**
     * Returns true if this object is an instance of the Class class.
     *
     * @param object the object to test
     * @return a boolean
     * @see quicktime.app.spaces.DynamicCollection,
     * @see quicktime.app.spaces.Protocol
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isAppropriate(Object object) {
        return object instanceof Class;
    }

    /**
     * Returns true if the object is an instance of ALL of the class object members of the Protocol.
     * If the object is not an instance of any one of the member class objects the method returns false.
     *
     * @param object the Object to test
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    public boolean passProtocol(Object object) {
        for (Enumeration<Class<?>> enumeration = members(); enumeration.hasMoreElements(); ) {
            Class<?> clazz = enumeration.nextElement();
            if (!clazz.isInstance(object))
                return false;
        }
        return true;
    }
}
