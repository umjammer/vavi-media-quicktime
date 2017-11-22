
package quicktime.app.spaces;

import java.util.Enumeration;

import quicktime.QTException;


/**
 * @deprecated
 */
public abstract interface Collection {
    /**
     * @deprecated
     */
    public abstract boolean addMember(Object paramObject) throws QTException;

    /**
     * @deprecated
     */
    public abstract boolean hasMember(Object paramObject);

    /**
     * @deprecated
     */
    public abstract boolean isAppropriate(Object paramObject);

    /**
     * @deprecated
     */
    public abstract boolean isEmpty();

    /**
     * @deprecated
     */
    public abstract Enumeration members();

    /**
     * @deprecated
     */
    public abstract void removeMember(Object paramObject) throws QTException;

    /**
     * @deprecated
     */
    public abstract int size();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * spaces/Collection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
