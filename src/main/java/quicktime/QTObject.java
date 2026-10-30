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

    public static int ID(QTObject qtObject) {
        return qtObject == null ? 0 : qtObject.nativeRep;
    }

    protected QTObject(int nativeRep) {
        this(nativeRep, 0);
    }

    protected QTObject(int nativeRep, Object ref) {
        this(nativeRep, ref, 0);
    }

    protected QTObject(int nativeRep, Object ref, int ownerID) {
        _doSC();
        this.nativeRep = nativeRep;
        this.ref = ref;
        if (ref == null) {
            if (nativeRep == 0)
                throw new QTNullPointerException("The QT native object represented by this QTJava object is not valid:" + getClass().getName());
            QTObjectManagement.add(this, this.nativeRep, ownerID);
        }
    }

    protected QTObject(int nativeRep, int ownerID) {
        if (nativeRep == 0)
            throw new QTNullPointerException("The QT native object represented by this QTJava object is not valid:" + getClass().getName());
        _doSC();
        this.nativeRep = nativeRep;
        QTObjectManagement.add(this, this.nativeRep, ownerID);
    }

    private transient int nativeRep = 0;

    private transient Object ref = null;

    protected final transient int sessionAllocated = QTSession.sessionID;

    protected final int _ID() {
        if (qtIsNOTInitialized || this.nativeRep == 0 || sessionAllocated != QTSession.sessionID) {
            String name = getClass().getName();
            if (qtIsNOTInitialized)
                throw new QTNullPointerException("QuickTime MUST be initialized:" + name);
            if (this.nativeRep == 0)
                throw new QTNullPointerException("The QT native object represented by this QTJava object is no longer valid:" + name);
            throw new QTNullPointerException("This QTJava object was allocated in a previous QTSession and is no longer valid:" + name);
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
        } catch (Throwable e) {
            throw new QTException(e.toString());
        }
    }

    protected void _dispose() throws QTException {
    }

    /**
     * Indicates whether some other object is equal to this one.
     * If true, indicates that the two objects refer to the same memory.
     */
    public boolean equals(Object qtObject) {
        if (this == qtObject)
            return true;
        if ((qtObject != null) && ((qtObject instanceof QTObject))) {
            return this.nativeRep == ID((QTObject) qtObject);
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

    protected final void setNR(int nr) {
        if (this.ref == null) {
            throw new QTRuntimeException("Cannot reset a disposable reference " + getClass().getName());
        }
        if (this.nativeRep == nr)
            return;
        this.nativeRep = nr;
    }
}
