
package quicktime.util;

import java.util.Enumeration;


class QTVectorEnumerator implements Enumeration<QTPointerRef> {
    private final QTVector mVector;

    QTVectorEnumerator(QTVector vector) {
        this.mVector = vector;
    }

    private int index = 0;

    @Override
    public boolean hasMoreElements() {
        return this.index < this.mVector.size();
    }

    @Override
    public QTPointerRef nextElement() {
        QTPointerRef qtPointerRef = this.mVector.makeElement();
        if (qtPointerRef == null)
            return null;
        try {
            this.mVector.getElement(qtPointerRef, this.index++);
        } catch (Throwable e) {
            qtPointerRef = null;
        }
        return qtPointerRef;
    }
}
