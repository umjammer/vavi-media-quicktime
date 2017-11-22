
package quicktime.app.sg;

import java.awt.Dimension;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.app.time.TaskThread;
import quicktime.app.time.Tasking;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.image.Matrix;
import quicktime.streaming.Presentation;


public class PresentationDrawer extends Tasking implements QTDrawable {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static TaskThread idleTasker = new TaskThread("Default Pres Tasker", 20);

    private Presentation pres;

    /**
     * @deprecated
     */
    public PresentationDrawer(Presentation paramPresentation) throws QTException {
        this.pres = paramPresentation;
        setDefaultTasker(idleTasker);
    }

    /**
     * @deprecated
     */
    public Presentation getPresentation() {
        return this.pres;
    }

    /**
     * @deprecated
     */
    public QDGraphics getGWorld() throws QTException {
        return this.pres.getGWorld();
    }

    /**
     * @deprecated
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.pres.setGWorld(paramQDGraphics);
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        QDDimension localQDDimension = this.pres.getDimensions(null);
        Matrix localMatrix = this.pres.getMatrix(null);
        return new Dimension((int) (localQDDimension.getWidth() / localMatrix.getSx()),
                             (int) (localQDDimension.getHeight() / localMatrix.getSy()));
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        QDDimension localQDDimension = this.pres.getDimensions(null);
        Matrix localMatrix = this.pres.getMatrix(null);
        return new QDDimension(localQDDimension.getWidth() / localMatrix.getSx(),
                               localQDDimension.getHeight() / localMatrix.getSy());
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws QTException {
        return this.pres.getMatrix(null);
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.pres.setMatrix(null, paramMatrix);
    }

    /**
     * @deprecated
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws QTException {
        QDRect localQDRect = getDisplayBounds();

        Matrix localMatrix = new Matrix();

        localMatrix.setTx(paramQDRect.getX());
        localMatrix.setTy(paramQDRect.getY());
        localMatrix.setSx(paramQDRect.getWidth() / localQDRect.getWidth());
        localMatrix.setSy(paramQDRect.getHeight() / localQDRect.getHeight());

        setMatrix(localMatrix);

        this.pres.setDimensions(null, paramQDRect.getWidth(), paramQDRect.getHeight());
    }

    /**
     * @deprecated
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * @deprecated
     */
    public QDRect getDisplayBounds() throws QTException {
        return new QDRect(this.pres.getDimensions(null));
    }

    /**
     * @deprecated
     */
    public final synchronized void redraw(Region paramRegion) throws QTException {
        this.pres.invalidateRegion(paramRegion);
    }

    /**
     * @deprecated
     */
    public Region getClip() throws QTException {
        return this.pres.getClip(null);
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws QTException {
        this.pres.setClip(null, paramRegion);
    }

    /**
     * @deprecated
     */
    public final synchronized void task() throws QTException {
        this.pres.idle(null);
    }

    public String toString() {
        return getClass().getName() + "[" + this.pres + "]";
    }

    /**
     * @deprecated
     */
    public void addedTo(Object paramObject) {
    }

    /**
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/sg/
 * PresentationDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
