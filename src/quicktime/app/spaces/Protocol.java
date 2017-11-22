
package quicktime.app.spaces;

import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
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
     */
    public Protocol(Class paramClass) {
        if (paramClass != null)
            this.protocolsVec.addElement(paramClass);
    }

    private Vector protocolsVec = new Vector();

    private int defaultProtocolLimit = 0;

    /**
     * @deprecated
     */
    public Enumeration defaultProtocol() {
        if (this.defaultProtocolLimit > 0) {
            Vector localVector = new Vector();
            for (int i = 0; i < this.defaultProtocolLimit; i++)
                localVector.addElement(this.protocolsVec.elementAt(i));
            return localVector.elements();
        }
        return null;
    }

    /**
     * @deprecated
     */
    public Enumeration members() {
        return this.protocolsVec.elements();
    }

    /**
     * @deprecated
     */
    public boolean isEmpty() {
        return this.protocolsVec.isEmpty();
    }

    /**
     * @deprecated
     */
    public boolean hasMember(Object paramObject) {
        return this.protocolsVec.contains(paramObject);
    }

    /**
     * @deprecated
     */
    public int size() {
        return this.protocolsVec.size();
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject) {
        if (!isAppropriate(paramObject))
            return false;
        if (!hasMember(paramObject))
            this.protocolsVec.addElement(paramObject);
        return true;
    }

    /**
     * @deprecated
     */
    public void removeMember(Object paramObject) {
        int i = this.protocolsVec.indexOf(paramObject);
        if (i >= this.defaultProtocolLimit) {
            this.protocolsVec.removeElement(paramObject);
        }
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Class;
    }

    /**
     * @deprecated
     */
    public boolean passProtocol(Object paramObject) {
        for (Enumeration localEnumeration = members(); localEnumeration.hasMoreElements();) {
            Class localClass = (Class) localEnumeration.nextElement();
            if (!localClass.isInstance(paramObject))
                return false;
        }
        return true;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * spaces/Protocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
