package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;

/**
 * This class represents a Vector of uniform sized objects into a handle.
 */
public abstract class QTVector extends QTPointerRef implements QuickTimeLib {

    private static Object linkage;

    private int mElSize;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QTVector.class);
                    }
                });
            }
        }.establish();
    }

    protected QTVector(int paramInt1, int paramInt2) throws QTException {
        super(paramInt1 * paramInt2, true);
        this.mInitialSize = getSize();
        this.mElSize = paramInt1;
    }

    protected int mNumElements = 0;

    private int mInitialSize;

    private final boolean doSizeCheck() throws UtilException {
        if (this.mNumElements * this.mElSize >= getSize())
            return true;
        return false;
    }

    protected void insertElement(QTPointerRef paramQTPointerRef, int paramInt) throws UtilException, ArrayIndexOutOfBoundsException {
        if (paramInt >= this.mNumElements)
            throw new ArrayIndexOutOfBoundsException("QTVector:insert past end");
        if (doSizeCheck()) {
            throw new ArrayIndexOutOfBoundsException("QTVector is full");
        }
        int i = _ID() + this.mElSize * paramInt;
        if (paramInt != this.mNumElements - 1) {
            BlockMove(i + 0, i + this.mElSize, this.mElSize * (this.mNumElements - paramInt));
        }
        int j = QTObject.ID(paramQTPointerRef);
        BlockMove(j + 0, i + 0, this.mElSize);
        this.mNumElements += 1;
    }

    /**
     * remove element at specified position
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public void remove(int paramInt) throws ArrayIndexOutOfBoundsException, UtilException {
        if ((paramInt >= this.mNumElements) || (this.mNumElements == 0)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = _ID() + this.mElSize * paramInt;
        int j = this.mElSize * (this.mNumElements - paramInt - 1);
        if (j > 0) {
            BlockMove(i + this.mElSize, i + 0, j);
        }
        this.mNumElements -= 1;
    }

    protected void getElement(QTPointerRef paramQTPointerRef, int paramInt) throws ArrayIndexOutOfBoundsException, UtilException {
        if ((paramInt >= this.mNumElements) || (this.mNumElements == 0)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = QTObject.ID(paramQTPointerRef);
        int j = _ID() + this.mElSize * paramInt;
        BlockMove(j, i, this.mElSize);
    }

    public boolean isEmpty() {
        return this.mNumElements == 0;
    }

    public void removeAll() throws UtilException {
        this.mNumElements = 0;
    }

    /**
     * Returns the size of the QTPionter
     */
    public int getSize() {
        return this.mNumElements * this.mElSize;
    }

    public int size() {
        return this.mNumElements;
    }

    public Enumeration<QTPointerRef> elements() {
        return new QTVectorEnumerator(this);
    }

    protected abstract QTPointerRef makeElement();

    private static native void BlockMove(int paramInt1, int paramInt2, int paramInt3);
}
