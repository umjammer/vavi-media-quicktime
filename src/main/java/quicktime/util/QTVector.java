package quicktime.util;

import java.util.Enumeration;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;


/**
 * This class represents a Vector of uniform sized objects into a handle.
 */
public abstract class QTVector extends QTPointerRef implements QuickTimeLib {

    private final int mElSize;

    protected QTVector(int elSize, int numElements) throws QTException {
        super(elSize * numElements, true);
        this.mInitialSize = getSize();
        this.mElSize = elSize;
    }

    protected int mNumElements = 0;

    private final int mInitialSize;

    private boolean doSizeCheck() throws UtilException {
        if (this.mNumElements * this.mElSize >= getSize())
            return true;
        return false;
    }

    protected void insertElement(QTPointerRef qtPointerRef, int index) throws UtilException, ArrayIndexOutOfBoundsException {
        if (index >= this.mNumElements)
            throw new ArrayIndexOutOfBoundsException("QTVector:insert past end");
        if (doSizeCheck()) {
            throw new ArrayIndexOutOfBoundsException("QTVector is full");
        }
        int i = _ID() + this.mElSize * index;
        if (index != this.mNumElements - 1) {
            BlockMove(i + 0, i + this.mElSize, this.mElSize * (this.mNumElements - index));
        }
        int j = QTObject.ID(qtPointerRef);
        BlockMove(j + 0, i + 0, this.mElSize);
        this.mNumElements += 1;
    }

    /**
     * remove element at specified position
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public void remove(int index) throws ArrayIndexOutOfBoundsException, UtilException {
        if ((index >= this.mNumElements) || (this.mNumElements == 0)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = _ID() + this.mElSize * index;
        int j = this.mElSize * (this.mNumElements - index - 1);
        if (j > 0) {
            BlockMove(i + this.mElSize, i + 0, j);
        }
        this.mNumElements -= 1;
    }

    protected void getElement(QTPointerRef qtPointerRef, int index) throws ArrayIndexOutOfBoundsException, UtilException {
        if ((index >= this.mNumElements) || (this.mNumElements == 0)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = QTObject.ID(qtPointerRef);
        int j = _ID() + this.mElSize * index;
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
    @Override
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

    private static void BlockMove(int src, int dst, int size) {
        MemoryManager.BlockMove(src, dst, size);
    }
}
