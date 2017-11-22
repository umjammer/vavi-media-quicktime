
package quicktime.app.sg;

import java.awt.Dimension;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.app.time.TaskThread;
import quicktime.app.time.Tasking;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.Matrix;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;


/**
 * @deprecated
 */
public class SGDrawer extends Tasking implements QTDrawable {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static TaskThread sgTasker = new TaskThread("Default SG Tasker", 20);

    private SGVideoChannel chan;

    /**
     * @deprecated
     */
    public SGDrawer(SGVideoChannel paramSGVideoChannel) throws QTException {
        this.chan = paramSGVideoChannel;
        setDefaultTasker(sgTasker);
    }

    /**
     * @deprecated
     */
    public SGVideoChannel getSGChannel() {
        return this.chan;
    }

    /**
     * @deprecated
     */
    public SequenceGrabber getSequenceGrabber() {
        return this.chan.getSequenceGrabber();
    }

    /**
     * @deprecated
     */
    public QDGraphics getGWorld() throws StdQTException {
        return this.chan.getSequenceGrabber().getGWorld();
    }

    /**
     * @deprecated
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.chan.getSequenceGrabber().setGWorld(paramQDGraphics, null);
        if (!QDGraphics.scratch.equals(paramQDGraphics)) {
            this.deferTasking = true;
        } else {
            stopTasking();
            this.chan.getSequenceGrabber().stop();
        }
    }

    private boolean deferTasking = false;

    private SGCaptureCallback stopTimeCallback;

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        QDRect localQDRect = this.chan.getBounds();
        Matrix localMatrix = this.chan.getMatrix();
        return new Dimension((int) (localQDRect.getWidth() / localMatrix.getSx()),
                             (int) (localQDRect.getHeight() / localMatrix.getSy()));
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.chan.getBounds();
        Matrix localMatrix = this.chan.getMatrix();
        return new QDDimension(localQDRect.getWidth() / localMatrix.getSx(), localQDRect.getHeight() / localMatrix.getSy());
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws StdQTException {
        return this.chan.getMatrix();
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.chan.setMatrix(paramMatrix);
    }

    /**
     * @deprecated
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.chan.setBounds(paramQDRect);
        if (this.deferTasking) {
            this.deferTasking = false;
            startTasking();
        }
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
    public QDRect getDisplayBounds() throws StdQTException {
        return this.chan.getBounds();
    }

    /**
     * @deprecated
     */
    public final synchronized void redraw(Region paramRegion) throws StdQTException {
        this.chan.getSequenceGrabber().update(paramRegion);
    }

    /**
     * @deprecated
     */
    public Region getClip() throws QTException {
        return this.chan.getClip();
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws StdQTException {
        this.chan.setClip(paramRegion);
    }

    /**
     * @deprecated
     */
    public final void task() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            if (!this.chan.getSequenceGrabber().idleMore()) {
                stopTasking();
                this.chan.getSequenceGrabber().stop();
                if (this.stopTimeCallback != null) {
                    new Thread(new Runnable() {
                        public void run() {
                            SGDrawer.this.stopTimeCallback.finish(SGDrawer.this);
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * @deprecated
     */
    public void setSGCaptureCallback(SGCaptureCallback paramSGCaptureCallback) {
        this.stopTimeCallback = paramSGCaptureCallback;
    }

    /**
     * @deprecated
     */
    public SGCaptureCallback getSGCaptureCallback() {
        return this.stopTimeCallback;
    }

    public String toString() {
        return getClass().getName() + "[" + this.chan + "]";
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
 * SGDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
