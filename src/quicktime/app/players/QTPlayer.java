
package quicktime.app.players;

import java.awt.Dimension;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.audio.AudioSpec;
import quicktime.app.display.QTDrawable;
import quicktime.app.time.TaskThread;
import quicktime.app.time.Tasking;
import quicktime.qd.QDDimension;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.image.Matrix;
import quicktime.std.movies.MovieController;


/**
 * @deprecated
 */
public class QTPlayer extends Tasking implements QTDrawable, Playable, AudioSpec {
    MovieController controller;

    private TaskThread tt;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public QTPlayer(MovieController paramMovieController) throws StdQTException {
        this.controller = paramMovieController;
    }

    private boolean setControllerVisibleFlag = false;

    private boolean reserveSize = true;

    private QTPlayerEventHandler eventHandler;

    /**
     * @deprecated
     */
    public void addedTo(Object paramObject) {
        if (this.eventHandler == null)
            this.eventHandler = QTPlayerEventHandler.make(this, paramObject);
        if (this.eventHandler != null) {
            this.eventHandler.addedTo();
        }
    }

    /**
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
        if (this.eventHandler != null)
            this.eventHandler.removedFrom();
        this.eventHandler = null;
    }

    public MovieController getMovieController() {
        return this.controller;
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        QDDimension localQDDimension = getOriginalSize();
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        QDDimension localQDDimension = this.controller.getRequiredSize();
        QDRect localQDRect = this.controller.getMovie().getNaturalBoundsRect();
        return new QDDimension(localQDRect.getWidth() + localQDDimension.getWidth(),
                               localQDRect.getHeight() + localQDDimension.getHeight());
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws StdQTException {
        return this.controller.getMovie().getMatrix();
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.controller.getMovie().setMatrix(paramMatrix);
        this.controller.movieChanged();
        this.controller.idle();
    }

    /**
     * @deprecated
     */
    public QDGraphics getGWorld() throws StdQTException {
        return this.controller.getPort();
    }

    /**
     * @deprecated
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QDException, StdQTException {
        if (getGWorld().equals(paramQDGraphics))
            return;
        this.controller.setPort(paramQDGraphics);
        if (!QDGraphics.scratch.equals(paramQDGraphics)) {
            startTasking();
            this.redrawRequired = true;
            this.controller.getMovie().setActive(true);
        } else {
            setRate(0.0F);
            stopTasking();
            this.controller.getMovie().setActive(false);
        }
    }

    private boolean redrawRequired = false;

    /**
     * @deprecated
     */
    public final void redraw(Region paramRegion) throws QTException {
        if (paramRegion != null) {
            try {
                this.controller.invalidate(getGWorld(), paramRegion);
                this.controller.idle();
            } catch (StdQTException localStdQTException) {
                this.controller.draw();
            }
        } else {
            this.controller.draw();
        }
    }

    /**
     * @deprecated
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws QTException {
        QDDimension localQDDimension = this.controller.getRequiredSize();
        if ((paramQDRect.getHeight() < localQDDimension.getHeight())
            || (paramQDRect.getWidth() < localQDDimension.getWidth())) {
            if (this.controller.getVisible()) {
                this.controller.setVisible(false);
                this.setControllerVisibleFlag = true;
            }
            paramQDRect.setHeight(paramQDRect.getHeight() + localQDDimension.getHeight());
            paramQDRect.setWidth(paramQDRect.getWidth() + localQDDimension.getWidth());
        } else {
            if (this.setControllerVisibleFlag) {
                this.controller.setVisible(true);
                this.setControllerVisibleFlag = false;
            }
            if ((!this.reserveSize) && (!this.controller.getVisible())) {
                paramQDRect.setHeight(paramQDRect.getHeight() + localQDDimension.getHeight());
                paramQDRect.setWidth(paramQDRect.getWidth() + localQDDimension.getWidth());
            }
        }

        this.controller.setBounds(paramQDRect);
        if (this.redrawRequired) {
            this.redrawRequired = false;
            if (this.controller.getVisible()) {
                redraw(null);
            }
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
        return this.reserveSize ? this.controller.getBounds()
                                : this.controller.getVisible() ? this.controller.getBounds()
                                                               : this.controller.getMovie().getBounds();
    }

    /**
     * @deprecated
     */
    public final void task() throws QTException {
        this.controller.idle();
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws QTException {
        this.controller.setClip(paramRegion);
    }

    /**
     * @deprecated
     */
    public Region getClip() throws QTException {
        return this.controller.getClip();
    }

    /**
     * @deprecated
     */
    public void setTime(int paramInt) throws StdQTException {
        this.controller.getMovie().setTimeValue(paramInt);
        this.controller.movieChanged();
    }

    /**
     * @deprecated
     */
    public int getTime() throws StdQTException {
        return this.controller.getMovie().getTime();
    }

    /**
     * @deprecated
     */
    public int getDuration() throws StdQTException {
        return this.controller.getMovie().getDuration();
    }

    public TimeBase getTimeBase() throws QTException {
        return this.controller.getMovie().getTimeBase();
    }

    /**
     * @deprecated
     */
    public void setRate(float paramFloat) throws StdQTException {
        if (paramFloat != 0.0F) {
            this.controller.prerollAndPlay(paramFloat);
        } else {
            this.controller.play(paramFloat);
        }
    }

    /**
     * @deprecated
     */
    public float getRate() throws StdQTException {
        return this.controller.getMovie().getRate();
    }

    /**
     * @deprecated
     */
    public int getScale() throws QTException {
        return this.controller.getMovie().getTimeScale();
    }

    public float getVolume() throws QTException {
        float f = this.controller.getVolume();
        return Math.abs(f);
    }

    public void setVolume(float paramFloat) throws QTException {
        float f = Math.abs(paramFloat);
        if (isMuted()) {
            this.controller.setVolume(f * -1.0F);
        } else {
            this.controller.setVolume(f);
        }
    }

    public boolean isMuted() throws QTException {
        return this.controller.getVolume() < 0.0F;
    }

    /**
     * @deprecated
     */
    public void setMuted(boolean paramBoolean) throws QTException {
        float f = Math.abs(this.controller.getVolume());
        if (paramBoolean) {
            this.controller.setVolume(f * -1.0F);
        } else {
            this.controller.setVolume(f);
        }
    }

    public void setControllerVisible(boolean paramBoolean) throws QTException {
        this.controller.setVisible(paramBoolean);
        if ((this.setControllerVisibleFlag) && (!paramBoolean)) {
            this.setControllerVisibleFlag = false;
        }
    }

    public boolean isControllerVisible() throws QTException {
        return this.controller.getVisible();
    }

    /**
     * @deprecated
     */
    public void setControllerSizeReserved(boolean paramBoolean) throws QTException {
        this.reserveSize = paramBoolean;
        setDisplayBounds(getDisplayBounds());
    }

    public boolean isControllerSizeReserved() {
        return this.reserveSize;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * players/QTPlayer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
