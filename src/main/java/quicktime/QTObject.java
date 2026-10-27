package quicktime;

import quicktime.jdirect.QTNative;
import quicktime.std.clocks.QTCallBack;
import quicktime.std.movies.media.Media;

/**
 * This is a class that defines an unique identifier for QT objects.
 */
public abstract class QTObject {

    private static final boolean doSecurityCheck = false;

    static boolean qtIsNOTInitialized = true;

    public static int ID(QTObject paramQTObject) {
        return paramQTObject == null ? 0 : paramQTObject.nativeRep;
    }

    protected QTObject(int paramInt) {
        this(paramInt, 0);
    }

    protected QTObject(int paramInt, Object paramObject) {
        this(paramInt, paramObject, 0);
    }

    protected QTObject(int paramInt1, Object paramObject, int paramInt2) {
        _doSC();
        this.nativeRep = paramInt1;
        this.ref = paramObject;
        if (paramObject == null) {
            if (paramInt1 == 0)
                throw new QTNullPointerException("The QT native object represented by this QTJava object is not valid:" + getClass().getName());
            QTObjectManagement.add(this, this.nativeRep, paramInt2);
        }
    }

    protected QTObject(int paramInt1, int paramInt2) {
        if (paramInt1 == 0)
            throw new QTNullPointerException("The QT native object represented by this QTJava object is not valid:" + getClass().getName());
        _doSC();
        this.nativeRep = paramInt1;
        QTObjectManagement.add(this, this.nativeRep, paramInt2);
    }

    private transient int nativeRep = 0;

    private transient Object ref = null;

    protected final transient int sessionAllocated = QTSession.sessionID;

    protected final int _ID() {
        if (qtIsNOTInitialized || this.nativeRep == 0 || sessionAllocated != QTSession.sessionID) {
            String str = getClass().getName();
            if (qtIsNOTInitialized)
                throw new QTNullPointerException("QuickTime MUST be initialized:" + str);
            if (this.nativeRep == 0)
                throw new QTNullPointerException("The QT native object represented by this QTJava object is no longer valid:" + str);
            throw new QTNullPointerException("This QTJava object was allocated in a previous QTSession and is no longer valid:" + str);
        }
        return this.nativeRep;
    }

    protected boolean stillValid() {
        return (QTObjectManagement.isMember(this.nativeRep)) && (sessionAllocated == QTSession.sessionID);
    }

    private void disposeNative() throws Throwable {
        try {
            synchronized (this) {
                if (QTSession.isInitialized() && this.nativeRep != 0 && this.ref == null && (sessionAllocated == QTSession.sessionID)) {
                    if (QTObjectManagement.isMember(this.nativeRep)) {
                        _dispose();
                        synchronized (QTNative.globalsLock) {
                            QTObjectManagement.remove(this.nativeRep, this);
                        }
                    }
                    _dispose();
                }
            }
        } finally {
            this.nativeRep = 0;
        }
    }

    /**
     * This call will dispose the underlying QuickTime object that is associated with the QTJava object.
     * After this call the QTJava object is no longer usable and will throw QTRuntimeExceptions to indicate
     * that the QTJava object's native object has been disposed.
     */
    public void disposeQTObject() throws QTException {
        try {
            disposeNative();
        } catch (Throwable localThrowable) {
            throw new QTException(localThrowable.toString());
        }
    }

    protected void _dispose() throws QTException {
    }

    /**
     * Indicates whether some other object is equal to this one.
     * If true, indicates that the two objects refer to the same memory.
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject != null) && ((paramObject instanceof QTObject))) {
            return this.nativeRep == ID((QTObject) paramObject);
        }
        return false;
    }

    /**
     * A String representation of the class.
     */
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(this.nativeRep);
    }

    private void _doSC() {
        if ((doSecurityCheck) && (!(this instanceof QTCallBack)) && (!(this instanceof Media))) {
            if ((getClass().getClassLoader() != null) && (!getClass().getName().startsWith("quicktime."))) {
                throw new SecurityException(getClass().getName() + " cannot be constructed with current Security settings");
            }
        }
    }

    protected final void setNR(int paramInt) {
        if (this.ref == null) {
            throw new QTRuntimeException("Cannot reset a disposable reference " + getClass().getName());
        }
        if (this.nativeRep == paramInt)
            return;
        this.nativeRep = paramInt;
    }
}
