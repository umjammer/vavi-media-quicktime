
package quicktime.app.players;

import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.audio.AudioSpec;
import quicktime.app.display.DrawingListener;
import quicktime.app.display.DrawingNotifier;
import quicktime.app.image.DynamicImage;
import quicktime.app.image.ImagePresenter;
import quicktime.app.time.TaskAllMovies;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieDrawingComplete;
import quicktime.std.movies.MoviePrePreroll;


/**
 * @deprecated
 */
public class MoviePresenter extends ImagePresenter
                            implements AudioSpec, Playable, DynamicImage, MovieDrawingComplete, DrawingNotifier {
    private QDGraphics g;

    private Movie m;

    private boolean gModeOpaque;

    protected TwoDSprite spritePresenter;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    class MPInvalidator extends Invalidator {
        MPInvalidator(TwoDSprite paramTwoDSprite) {
            super(paramTwoDSprite);
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            MoviePresenter.this.m.task(0);
            if (MoviePresenter.this.mChanged) {
                MoviePresenter.this.spritePresenter.invalidate();
                MoviePresenter.this.mChanged = false;
            }
            return true;
        }
    }

    private static QDRect moveToZero(QDRect paramQDRect) {
        paramQDRect.move(0, 0);
        return paramQDRect;
    }

    /**
     * @deprecated
     */
    public MoviePresenter(Movie paramMovie) throws QTException {
        this(paramMovie, new QDGraphics(32, paramMovie.getBounds(), 4096));
    }

    /**
     * @deprecated
     */
    public MoviePresenter(Movie paramMovie, QDGraphics paramQDGraphics) throws QTException {
        super(moveToZero(paramMovie.getBounds()));
        this.m = paramMovie;
        this.g = paramQDGraphics;
        paramMovie.setGWorld(paramQDGraphics, null);
        PixMap localPixMap = paramQDGraphics.getPixMap();
        setImageData(localPixMap.getPixelData(), new ImageDescription(localPixMap));
        this.gModeOpaque = true;
        paramMovie.task(0);
    }

    private boolean mChanged = false;

    private boolean isPres = false;

    /**
     * @deprecated
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        if (getGWorld().equals(paramQDGraphics))
            return;
        super.setGWorld(paramQDGraphics);
        if (QDGraphics.scratch.equals(paramQDGraphics)) {
            TaskAllMovies.removeMovie();
            this.isPres = false;
            this.m.removeDrawingCompleteProc();
            this.m.setActive(false);
        } else {
            if (QDGraphics.validScratch.equals(paramQDGraphics)) {
                this.isPres = false;
            } else
                this.isPres = true;
            this.m.setDrawingCompleteProc(0, this);
            TaskAllMovies.addMovieAndStart();
            this.m.setActive(true);
        }
    }

    /**
     * @deprecated
     */
    public boolean isPresenting() {
        return this.isPres;
    }

    /**
     * @deprecated
     */
    public int execute(Movie paramMovie) {
        try {
            if (this.isPres)
                doDraw();
            this.mChanged = true;
            if (this.listenerList != null) {
                synchronized (this) {
                    for (int i = 0; i < this.listenerList.size(); i++)
                        ((DrawingListener) this.listenerList.elementAt(i)).drawingComplete(this);
                }
            }
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
        return 0;
    }

    /**
     * @deprecated
     */
    public Movie getMovie() {
        return this.m;
    }

    /**
     * @deprecated
     */
    public QDGraphics getOffscreenBuffer() {
        return this.g;
    }

    /**
     * @deprecated
     */
    public void redraw(Region paramRegion) throws QTException {
        if (paramRegion != null) {
            this.m.invalidateRegion(paramRegion);
        } else
            this.m.invalidateRegion(new Region(this.m.getBounds()));
        this.m.task(0);
        if ((this.spritePresenter != null) && (this.spritePresenter.isValid())) {
            this.spritePresenter.invalidate();
        }
        super.redraw(paramRegion);
    }

    /**
     * @deprecated
     */
    public TimeBase getTimeBase() throws QTException {
        return this.m.getTimeBase();
    }

    /**
     * @deprecated
     */
    public void setRate(float paramFloat) throws QTException {
        synchronized (this) {
            this.lastRate = paramFloat;
        }
        if (paramFloat == 0.0F) {
            this.m.stop();
        } else
            this.m.prePreroll(this.m.getTime(), paramFloat, new PrerollCallback(paramFloat));
    }

    private float lastRate = 0.0F;

    private Vector listenerList;

    class PrerollCallback implements MoviePrePreroll {
        PrerollCallback(float paramFloat) {
            this.r = paramFloat;
        }

        private float r;

        public void execute(Movie paramMovie, int paramInt) {
            synchronized (MoviePresenter.this) {
                if (MoviePresenter.this.lastRate == 0.0F) {
                    return;
                }
                if (paramInt == 0) {
                    try {
                        paramMovie.preroll(paramMovie.getTime(), this.r);
                        paramMovie.setRate(this.r);
                    } catch (QTException localQTException) {
                        QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), paramMovie, "preroll");
                    }
                } else {
                    QTRuntimeException.handleOrThrow(new QTRuntimeException(paramInt), paramMovie, "prePreroll");
                }
            }
        }
    }

    /**
     * @deprecated
     */
    public float getRate() throws QTException {
        return this.m.getRate();
    }

    /**
     * @deprecated
     */
    public void setTime(int paramInt) throws QTException {
        this.m.setTimeValue(paramInt);
    }

    /**
     * @deprecated
     */
    public int getTime() throws QTException {
        return this.m.getTime();
    }

    /**
     * @deprecated
     */
    public int getDuration() throws QTException {
        return this.m.getDuration();
    }

    /**
     * @deprecated
     */
    public int getScale() throws QTException {
        return this.m.getTimeScale();
    }

    /**
     * @deprecated
     */
    public float getVolume() throws QTException {
        float f = this.m.getVolume();
        return Math.abs(f);
    }

    /**
     * @deprecated
     */
    public void setVolume(float paramFloat) throws QTException {
        float f = Math.abs(paramFloat);
        if (isMuted()) {
            this.m.setVolume(f * -1.0F);
        } else {
            this.m.setVolume(f);
        }
    }

    /**
     * @deprecated
     */
    public boolean isMuted() throws QTException {
        return this.m.getVolume() < 0.0F;
    }

    /**
     * @deprecated
     */
    public void setMuted(boolean paramBoolean) throws QTException {
        float f = Math.abs(this.m.getVolume());
        if (paramBoolean) {
            this.m.setVolume(f * -1.0F);
        } else {
            this.m.setVolume(f);
        }
    }

    /**
     * @deprecated
     */
    public void movieChanged() {
        this.mChanged = true;
    }

    /**
     * @deprecated
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) throws QTException {
        this.spritePresenter = paramTwoDSprite;
        this.m.setActive(true);
        if (!this.drawCompleteListening) {
            this.m.setDrawingCompleteProc(0, this);
            this.drawCompleteListening = true;
        }
        return new MPInvalidator(this.spritePresenter);
    }

    /**
     * @deprecated
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) throws QTException {
        if (this.listenerList == null) {
            this.m.removeDrawingCompleteProc();
            this.drawCompleteListening = false;
        }
        this.spritePresenter = null;
        if (QDGraphics.scratch.equals(getGWorld())) {
            this.m.setActive(false);
        }
    }

    private boolean drawCompleteListening = false;

    /**
     * @deprecated
     */
    public synchronized void addDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList == null) {
            this.listenerList = new Vector();
        }
        if (this.listenerList.contains(paramDrawingListener)) {
            return;
        }
        this.listenerList.addElement(paramDrawingListener);

        if (!this.drawCompleteListening) {
            try {
                this.m.setDrawingCompleteProc(0, this);
                this.drawCompleteListening = true;
            } catch (StdQTException localStdQTException) {
            }
        }
    }

    /**
     * @deprecated
     */
    public synchronized void removeDrawingListener(DrawingListener paramDrawingListener) {
        this.listenerList.removeElement(paramDrawingListener);

        if (this.listenerList.isEmpty()) {
            if (this.spritePresenter == null) {
                try {
                    this.m.removeDrawingCompleteProc();
                    this.drawCompleteListening = false;
                } catch (StdQTException localStdQTException) {
                }
            }
            this.listenerList = null;
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * players/MoviePresenter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
