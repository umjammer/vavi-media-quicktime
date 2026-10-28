package quicktime.app.spaces;

import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
 * A Protocol is a collection of classes that are used to test
 * an object against. The passProtocol (Object o) method
 * uses the Class.instanceOf method on each of the classes
 * in its collection. If the incoming object is not an instance
 * of any of the classes in the Protocol then the passProtocol
 * method returns false. If the incoming object is then it returns
 * true.
 *
 * For DynamicCollections an application can dynamically assign or
 * add classes to a Protocol to tighten the membership requirements
 * of its collection. When an object is added to a dynamic collection
 * the dynamic collection will first test the object against its
 * Protocol's passProtocol method. Only if it passes this test is the
 * object allowed to be added to the Collection. This test is
 * encapsulated in the DynamicCollection's isAppropriate method.
 * @see quicktime.app.spaces.DynamicCollection
 */
public class Protocol implements Collection {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public Protocol(Class[] paramArrayOfClass) {
        if (paramArrayOfClass != null) {
            this.defaultProtocolLimit = paramArrayOfClass.length;
            for (int i = 0; i < this.defaultProtocolLimit; i++) {
                this.protocolsVec.addElement(paramArrayOfClass[i]);
            }
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a Protocol from the supplied class. These default class cannot be
     * removed from this Protocol once created.
     */
    public Protocol(Class paramClass) {
        if (paramClass != null)
            this.protocolsVec.addElement(paramClass);
    }

    private final Vector protocolsVec = new Vector();

    private int defaultProtocolLimit = 0;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns an Enumeration that enables you to examine the default classes for
     * this Protocol.
     * @return an Enumeration
     */
    public Enumeration defaultProtocol() {
        if (this.defaultProtocolLimit > 0) {
            Vector localVector = new Vector();
            for (int i = 0; i < this.defaultProtocolLimit; i++) localVector.addElement(this.protocolsVec.elementAt(i));
            return localVector.elements();
        }
        return null;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns an Enumeration to iterate over all of the members of the Protocol object.
     * @return an Enumeration
     */
    @Override
    public Enumeration members() {
        return this.protocolsVec.elements();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the Protocol has no members, otherwise false
     * @return a boolean
     */
    @Override
    public boolean isEmpty() {
        return this.protocolsVec.isEmpty();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the supplied class object is a member of this Protocol.
     * @param object the object to test for membership
     * @return a boolean
     */
    @Override
    public boolean hasMember(Object object) {
        return this.protocolsVec.contains(object);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the number of class objects that are in this Protocol.
     * @return an int
     */
    @Override
    public int size() {
        return this.protocolsVec.size();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Adds a class object to this Protocol. The application can later remove any class objects
     * that are added to a Protocol in this manner
     * @param member the object that should be added as a member to the collection
     * @return true if the class object was successfully added (or is already a member)
     */
    @Override
    public boolean addMember(Object member) {
        if (!isAppropriate(member))
            return false;
        if (!hasMember(member))
            this.protocolsVec.addElement(member);
        return true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Removes a class object that was previously added to this Protocol. The application cannot
     * remove class objects that are part of the default protocol of this Protocol.
     * @param member the object to remove from the collection
     */
    @Override
    public void removeMember(Object member) {
        int i = this.protocolsVec.indexOf(member);
        if (i >= this.defaultProtocolLimit) {
            this.protocolsVec.removeElement(member);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if this object is an instance of the Class class.
     * @param object the object to test
     * @return a boolean
     * @see quicktime.app.spaces.DynamicCollection,
     * {@link quicktime.app.spaces.Protocol}
     */
    @Override
    public boolean isAppropriate(Object object) {
        return object instanceof Class;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the object is an instance of ALL of the class object members of the Protocol.
     * If the object is not an instance of any one of the member class objects the method returns false.
     * @param object the Object to test
     * @return a boolean
     */
    public boolean passProtocol(Object object) {
        for (Enumeration localEnumeration = members(); localEnumeration.hasMoreElements(); ) {
            Class localClass = (Class) localEnumeration.nextElement();
            if (!localClass.isInstance(object))
                return false;
        }
        return true;
    }
}

