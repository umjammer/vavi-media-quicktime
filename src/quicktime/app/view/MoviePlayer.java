
package quicktime.app.view;

import java.awt.Dimension;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.app.time.TaskAllMovies;
import quicktime.app.time.Timeable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.image.Matrix;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieDrawingComplete;
import quicktime.std.movies.MoviePrePreroll;


public class MoviePlayer implements Presentable, Timeable, DrawingNotifier {
    private Movie mMovie;

    private Vector listenerList;

    private MovieComplete mComplete;

    public MoviePlayer(Movie paramMovie) throws StdQTException {
        this.mMovie = paramMovie;
        paramMovie.setPlayHints(256, 256);
    }

    public Movie getMovie() {
        return this.mMovie;
    }

    public void addedTo(Object paramObject) {
    }

    public void removedFrom(Object paramObject) {
    }

    public QDGraphics getGWorld() throws StdQTException {
        return this.mMovie.getGWorld();
    }

    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws StdQTException {
        if (getGWorld().equals(paramQDGraphics))
            return;
        this.mMovie.setGWorld(paramQDGraphics, null);
        if (QDGraphics.scratch.equals(paramQDGraphics)) {
            TaskAllMovies.removeMovie();
            setRate(0.0F);
            this.mMovie.setActive(false);
        } else {
            TaskAllMovies.addMovieAndStart();
            this.mMovie.setActive(true);
        }
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        QDRect localQDRect = this.mMovie.getNaturalBoundsRect();
        return new Dimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.mMovie.getNaturalBoundsRect();
        return new QDDimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    public Matrix getMatrix() throws StdQTException {
        return this.mMovie.getMatrix();
    }

    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.mMovie.setMatrix(paramMatrix);
        this.mMovie.task(0);
    }

    public final void redraw(Region paramRegion) throws QTException {
        if (paramRegion != null) {
            this.mMovie.invalidateRegion(paramRegion);
        } else
            this.mMovie.invalidateRegion(new Region(this.mMovie.getBounds()));
        this.mMovie.task(0);
    }

    public synchronized void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.mMovie.setBounds(paramQDRect);
    }

    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    public QDRect getDisplayBounds() throws StdQTException {
        return this.mMovie.getBounds();
    }

    public void setClip(Region paramRegion) throws QTException {
        this.mMovie.setDisplayClipRgn(paramRegion);
    }

    public Region getClip() throws QTException {
        return this.mMovie.getDisplayClipRgn();
    }

    public void setTime(int paramInt) throws StdQTException {
        this.mMovie.setTimeValue(paramInt);
    }

    public int getTime() throws StdQTException {
        return this.mMovie.getTime();
    }

    public int getDuration() throws StdQTException {
        return this.mMovie.getDuration();
    }

    public TimeBase getTimeBase() throws QTException {
        return this.mMovie.getTimeBase();
    }

    public void setRate(float paramFloat) throws StdQTException {
        synchronized (this) {
            this.lastRate = paramFloat;
        }
        if (paramFloat == 0.0F) {
            this.mMovie.stop();
        } else
            this.mMovie.prePreroll(this.mMovie.getTime(), paramFloat, new PrerollCallback(paramFloat));
    }

    private float lastRate = 0.0F;

    class PrerollCallback implements MoviePrePreroll {
        private float r;

        PrerollCallback(float paramFloat) {
            this.r = paramFloat;
        }

        public void execute(Movie paramMovie, int paramInt) {
            synchronized (MoviePlayer.this) {
                if (MoviePlayer.this.lastRate == 0.0F) {
                    return;
                }
                if (paramInt == 0) {
                    try {
                        paramMovie.preroll(paramMovie.getTime(), this.r);
                        paramMovie.setRate(this.r);
                    } catch (QTException localQTException) {
                        QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), paramMovie, "preroll");
                    }
                } else
                    QTRuntimeException.handleOrThrow(new QTRuntimeException(paramInt), paramMovie, "prePreroll");
            }
        }
    }

    class MovieComplete implements MovieDrawingComplete {

        public int execute(Movie paramMovie) {
            synchronized (MoviePlayer.this) {
                for (int i = 0; i < MoviePlayer.this.listenerList.size(); i++)
                    ((DrawingListener) MoviePlayer.this.listenerList.elementAt(i)).drawingComplete(MoviePlayer.this);
            }
            return 0;
        }
    }

    public synchronized void addDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList == null) {
            this.listenerList = new Vector();
        }
        if (this.listenerList.contains(paramDrawingListener)) {
            return;
        }
        this.listenerList.addElement(paramDrawingListener);

        if (this.mComplete == null) {
            try {
                this.mComplete = new MovieComplete();
                this.mMovie.setDrawingCompleteProc(0, this.mComplete);
            } catch (StdQTException localStdQTException) {
                this.mComplete = null;
            }
        }
    }

    public synchronized void removeDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList != null) {
            this.listenerList.removeElement(paramDrawingListener);

            if (this.listenerList.isEmpty()) {
                try {
                    this.mMovie.removeDrawingCompleteProc();
                } catch (StdQTException localStdQTException) {
                }
                this.mComplete = null;
                this.listenerList = null;
            }
        }
    }

    public float getRate() throws StdQTException {
        return this.mMovie.getRate();
    }

    public int getScale() throws QTException {
        return this.mMovie.getTimeScale();
    }

    public float getVolume() throws QTException {
        float f = this.mMovie.getVolume();
        return Math.abs(f);
    }

    public void setVolume(float paramFloat) throws QTException {
        float f = Math.abs(paramFloat);
        if (isMuted()) {
            this.mMovie.setVolume(f * -1.0F);
        } else {
            this.mMovie.setVolume(f);
        }
    }

    public boolean isMuted() throws QTException {
        return this.mMovie.getVolume() < 0.0F;
    }

    public boolean isSingleFrame() {
        return false;
    }

    public void setMuted(boolean paramBoolean) throws QTException {
        float f = Math.abs(this.mMovie.getVolume());
        if (paramBoolean) {
            this.mMovie.setVolume(f * -1.0F);
        } else {
            this.mMovie.setVolume(f);
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/view/
 * MoviePlayer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
