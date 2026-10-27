
package quicktime.util;

import java.util.Enumeration;


class QTVectorEnumerator implements Enumeration<QTPointerRef> {
    private final QTVector mVector;

    QTVectorEnumerator(QTVector paramQTVector) {
        this.mVector = paramQTVector;
    }

    private int index = 0;

    @Override
    public boolean hasMoreElements() {
        return this.index < this.mVector.size();
    }

    @Override
    public QTPointerRef nextElement() {
        QTPointerRef localQTPointerRef = this.mVector.makeElement();
        if (localQTPointerRef == null)
            return null;
        try {
            this.mVector.getElement(localQTPointerRef, this.index++);
        } catch (Throwable localThrowable) {
            localQTPointerRef = null;
        }
        return localQTPointerRef;
    }
}
