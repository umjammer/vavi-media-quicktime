
package quicktime.app.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.time.TaskThread;
import quicktime.app.time.Tasking;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.MoviePrePreroll;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTUtils;


class AWTCanvasHelper extends Tasking implements java.awt.event.MouseListener, KeyListener {
    private static final boolean debug = false;

    private TaskThread tt;

    private boolean deferTasking = false;

    private static final TaskThread sgTasker = new TaskThread("Default SG Tasker", 20);

    private final QTJavaAWTCanvas canvas;

    AWTCanvasHelper(QTJavaAWTCanvas paramQTJavaAWTCanvas) {
        this.canvas = paramQTJavaAWTCanvas;
    }

    private Window parentWindow;

    private WindowAdapter wa;

    @Override
    public final void task() throws QTException {
        if (this.canvas.movieController != null) {
            this.canvas.movieController.idle();
        } else if (this.canvas.movie != null) {
            this.canvas.movie.task(0);
        } else if ((this.canvas.grabber != null) && (!this.canvas.grabber.idleMore())) {

            stopTasking();
            this.canvas.grabber.stop();
        }
    }

    private float lastRate = 0.0F;

    void setRate(float paramFloat) throws StdQTException {
        if (this.canvas.movieController != null) {
            if (paramFloat != 0.0F) {
                this.canvas.movieController.prerollAndPlay(paramFloat);
            } else
                this.canvas.movieController.play(paramFloat);
        } else if (this.canvas.movie != null) {
            synchronized (this) {
                this.lastRate = paramFloat;
            }
            if (paramFloat == 0.0F) {
                this.canvas.movie.stop();
            } else
                this.canvas.movie.prePreroll(this.canvas.movie.getTime(), paramFloat, new PrerollCallback(paramFloat));
        }
    }

    class PrerollCallback implements MoviePrePreroll {
        private final float r;

        PrerollCallback(float paramFloat) {
            this.r = paramFloat;
        }

        @Override
        public void execute(Movie paramMovie, int paramInt) {
            synchronized (this) {
                if (AWTCanvasHelper.this.lastRate == 0.0F) {
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

    protected void addedTo() {
        if (this.canvas.movieController != null) {
            this.canvas.addMouseListener(this);
            this.canvas.addKeyListener(this);
        }
        Object localObject1 = this.canvas.getParent();
        Object localObject2 = localObject1;
        while (localObject2 != null) {
            localObject1 = localObject2;
            if ((localObject1 instanceof Window))
                break;
            localObject2 = ((Container) localObject1).getParent();
        }
        if ((localObject1 instanceof Window)) {
            this.parentWindow = ((Window) localObject1);
            this.wa = new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent paramAnonymousWindowEvent) {
                    if (QTSession.isInitialized())
                        try {
                            if (AWTCanvasHelper.this.canvas.movieController != null) {

                                AWTCanvasHelper.this.canvas.movieController.activate(AWTCanvasHelper.this.getGWorld(), true);
                                AWTCanvasHelper.this.canvas.movieController.activate();
                            } else if (AWTCanvasHelper.this.canvas.movie != null) {

                                AWTCanvasHelper.this.canvas.movie.setActive(true);
                            }
                            AWTCanvasHelper.this.redraw(null);
                        } catch (QTException localQTException) {
                        }
                }

                @Override
                public void windowDeactivated(WindowEvent paramAnonymousWindowEvent) {
                    if (QTSession.isInitialized()) {
                        try {
                            if (AWTCanvasHelper.this.canvas.movieController != null) {
                                AWTCanvasHelper.this.canvas.movieController.deactivate();
                                AWTCanvasHelper.this.canvas.movieController.activate(AWTCanvasHelper.this.getGWorld(), false);
                            }
                            AWTCanvasHelper.this.redraw(null);

                        } catch (QTException localQTException) {
                        }
                    } else {
                        AWTCanvasHelper.this.removedFrom();
                    }
                }
            };
            this.parentWindow.addWindowListener(this.wa);
        }
    }

    protected void removedFrom() {
        if (this.canvas.movieController != null) {
            this.canvas.removeMouseListener(this);
            this.canvas.removeKeyListener(this);
        }

        if (this.parentWindow != null) {
            this.parentWindow.removeWindowListener(this.wa);
            this.parentWindow = null;
            this.wa = null;
        }
    }

    @Override
    public void keyPressed(KeyEvent paramKeyEvent) {
        try {
            if (this.canvas.movieController != null) {

                this.canvas.movieController.key(paramKeyEvent.getKeyCode(), paramKeyEvent.getModifiers());
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "keyPressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent paramKeyEvent) {
    }

    @Override
    public void keyTyped(KeyEvent paramKeyEvent) {
    }

    @Override
    public void mousePressed(MouseEvent paramMouseEvent) {
        try {
            if (this.canvas.movieController != null) {

                this.canvas.movieController.click(new quicktime.qd.QDPoint(paramMouseEvent.getX(), paramMouseEvent.getY()),
                                                  paramMouseEvent.getModifiers());
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mousePressed");
        }
    }

    @Override
    public void mouseClicked(MouseEvent paramMouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent paramMouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent paramMouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent paramMouseEvent) {
    }

    void doRemoveClient() {
        try {
            if ((this.canvas.movieController != null) || (this.canvas.movie != null)
                || (this.canvas.graphicsImporter != null)) {
                if (QTSession.isInitialized())
                    doSetGWorld(false);
                removedFrom();
            }
        } catch (QTException localQTException) {
        } finally {
            this.canvas.movieController = null;
            this.canvas.graphicsImporter = null;
            this.canvas.movie = null;
        }
    }

    void doSetSequenceGrabber(SequenceGrabber paramSequenceGrabber,
                              QDRect paramQDRect,
                              Dimension paramDimension1,
                              Dimension paramDimension2,
                              boolean paramBoolean) throws QTException {
        setDefaultTasker(sgTasker);
        this.canvas.grabber.setGWorld(QDGraphics.scratch, null);
        this.canvas.setInitialWidthAndHeight(paramQDRect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
        int i = this.canvas.currentWidth;
        int j = this.canvas.currentHeight;
        doSetBounds();

        if (paramSequenceGrabber != null) {
            try {
                doSetGWorld(this.canvas.isVisible());
                if (this.canvas.isDisplayable()) {
                    addedTo();
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!paramBoolean)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException localQTException) {
                removedFrom();
                throw localQTException;
            }
        } else {
            this.canvas.repaint();
        }
    }

    void doSetGraphicsImporter(GraphicsImporter paramGraphicsImporter,
                               QDRect paramQDRect,
                               Dimension paramDimension1,
                               Dimension paramDimension2,
                               boolean paramBoolean) throws QTException {
        doRemoveClient();

        if (paramGraphicsImporter != null) {
            int i = this.canvas.currentWidth;
            int j = this.canvas.currentHeight;
            this.canvas.graphicsImporter = paramGraphicsImporter;

            this.canvas.setInitialWidthAndHeight(paramQDRect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
            doSetBounds();
            try {
                doSetGWorld(this.canvas.isVisible());
                if (this.canvas.isDisplayable()) {
                    addedTo();
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!paramBoolean)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException localQTException) {
                removedFrom();
                throw localQTException;
            }
        } else {
            this.canvas.repaint();
        }
    }

    void doSetMovieController(MovieController paramMovieController,
                              QDRect paramQDRect,
                              Dimension paramDimension1,
                              Dimension paramDimension2,
                              boolean paramBoolean) throws QTException {
        if (paramMovieController == this.canvas.movieController) {
            return;
        }

        doRemoveClient();

        if (paramMovieController != null) {
            int i = this.canvas.currentWidth;
            int j = this.canvas.currentHeight;
            this.canvas.movieController = paramMovieController;
            this.canvas.movie = paramMovieController.getMovie();

            this.canvas.setInitialWidthAndHeight(paramQDRect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
            doSetBounds();
            try {
                doSetGWorld(this.canvas.isVisible());
                this.canvas.movieController.setKeysEnabled(true);
                if (this.canvas.isDisplayable()) {
                    addedTo();
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!paramBoolean)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException localQTException) {
                removedFrom();
                throw localQTException;
            }
        } else {
            this.canvas.repaint();
        }
    }

    void doSetMovie(Movie paramMovie,
                    QDRect paramQDRect,
                    Dimension paramDimension1,
                    Dimension paramDimension2,
                    boolean paramBoolean) throws QTException {
        if (paramMovie == this.canvas.movie) {
            return;
        }

        doRemoveClient();

        if (paramMovie != null) {
            this.canvas.movie = paramMovie;
            int i = this.canvas.currentWidth;
            int j = this.canvas.currentHeight;

            this.canvas.setInitialWidthAndHeight(paramQDRect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
            doSetBounds();
            try {
                doSetGWorld(this.canvas.isVisible());
                if (this.canvas.isDisplayable()) {
                    addedTo();
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!paramBoolean)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException localQTException) {
                removedFrom();
                throw localQTException;
            }
        } else {
            this.canvas.repaint();
        }
    }

    synchronized void setControllerGWorld(MovieController paramMovieController, QDGraphics paramQDGraphics) throws QDException,
                                                                                                            StdQTException {
        this.canvas.movieController.getMovie().setGWorld(QDGraphics.scratch, null);
        if (this.canvas.movieController.getVisible()) {
            this.canvas.movieController.setPort(paramQDGraphics);
        } else {
            this.canvas.movieController.getMovie().setGWorld(paramQDGraphics, null);
        }

        if (!QDGraphics.scratch.equals(paramQDGraphics)) {
            startTasking();

            paramMovieController.getMovie().setActive(true);
        } else {
            setRate(0.0F);
            stopTasking();
            paramMovieController.getMovie().setActive(false);
        }
    }

    synchronized void setMovieGWorld(QDGraphics paramQDGraphics) throws QDException, StdQTException {
        this.canvas.movie.setGWorld(paramQDGraphics, null);
        if (QDGraphics.scratch.equals(paramQDGraphics)) {
            stopTasking();
            setRate(0.0F);
            this.canvas.movie.setActive(false);
        } else {
            startTasking();
            this.canvas.movie.setActive(true);
        }
    }

    void doSetGWorld(boolean paramBoolean) throws QTException {
        QDGraphics localQDGraphics1;
        if ((paramBoolean) && (this.canvas.isDisplayable())) {
            localQDGraphics1 = this.canvas.getPort();
        } else {
            localQDGraphics1 = QDGraphics.scratch;
        }
        if (this.canvas.movieController != null) {
            MovieController localMovieController = this.canvas.movieController;
            QDGraphics localQDGraphics2 = localMovieController.getPort();
            if (!localQDGraphics1.equals(localQDGraphics2)) {
                setControllerGWorld(localMovieController, localQDGraphics1);
            }
        } else if (this.canvas.movie != null) {
            setMovieGWorld(localQDGraphics1);
        } else if (this.canvas.graphicsImporter != null) {
            this.canvas.graphicsImporter.setGWorld(localQDGraphics1, null);
        } else if (this.canvas.grabber != null) {
            this.canvas.grabber.setGWorld(localQDGraphics1, null);
        }

        if (paramBoolean) {
            QTUtils.checkFreeMemory();
            if (this.canvas.clientListener != null) {
                this.canvas.clientListener.componentShown(new ComponentEvent(this.canvas, 102));
            }
        } else if (this.canvas.clientListener != null) {
            this.canvas.clientListener.componentHidden(new ComponentEvent(this.canvas, 103));
        }
    }

    boolean shouldRedrawQTPlayer(Rectangle paramRectangle) {
        if ((!this.canvas.isWin) && (this.canvas.isShowing()) && (!paramRectangle.equals(this.canvas.getBounds()))) {
            return true;
        }

        return false;
    }

    final void redraw(Region paramRegion) throws QTException {
        if (this.canvas.movieController != null) {
            if (paramRegion != null) {
                try {
                    this.canvas.movieController.invalidate(getGWorld(), paramRegion);
                } catch (StdQTException localStdQTException) {
                    this.canvas.movieController.draw();
                }
            } else {
                this.canvas.movieController.draw();
                this.canvas.movieController.idle();
            }
        } else if (this.canvas.movie != null) {
            if (paramRegion != null) {
                this.canvas.movie.invalidateRegion(paramRegion);
            } else
                this.canvas.movie.invalidateRegion(new Region(this.canvas.movie.getBounds()));
            this.canvas.movie.update();
            this.canvas.movie.task(0);
        } else {
            Region localRegion;
            if (this.canvas.graphicsImporter != null) {
                if (paramRegion != null) {
                    localRegion = this.canvas.graphicsImporter.getClip();
                    this.canvas.graphicsImporter.setClip(paramRegion);
                    this.canvas.graphicsImporter.draw();
                    this.canvas.graphicsImporter.setClip(localRegion);
                } else {
                    this.canvas.graphicsImporter.draw();
                }
            } else if (this.canvas.grabber != null) {
                if (paramRegion != null) {
                    if ((this.canvas.channel instanceof SGVideoChannel)) {
                        localRegion = ((SGVideoChannel) this.canvas.channel).getClip();
                        ((SGVideoChannel) this.canvas.channel).setClip(paramRegion);
                        this.canvas.grabber.update(paramRegion);
                        ((SGVideoChannel) this.canvas.channel).setClip(localRegion);
                    }
                } else {
                    this.canvas.grabber.update(null);
                }
            }
        }
    }

    public QDGraphics getGWorld() throws StdQTException {
        if (this.canvas.movieController != null)
            return this.canvas.movieController.getPort();
        if (this.canvas.movie != null)
            return this.canvas.movie.getGWorld();
        if (this.canvas.graphicsImporter != null)
            return this.canvas.graphicsImporter.getGWorld();
        if (this.canvas.grabber != null) {
            return this.canvas.grabber.getGWorld();
        }

        return QDGraphics.scratch;
    }

    void doSetBounds() throws QTException {
        Point localPoint = this.canvas.getNGLocation();

        if (this.canvas.movie != null) {
            if (this.canvas.movieController != null) {
                if ((this.canvas.movieController.getVisible()) && (this.canvas.isVisible()))
                    this.canvas.movieController
                            .setBounds(new QDRect(localPoint.x,
                                                  localPoint.y,
                                                  this.canvas.currentWidth,
                                                  this.canvas.currentHeight));
            } else
                this.canvas.movie
                        .setBounds(new QDRect(localPoint.x, localPoint.y, this.canvas.currentWidth, this.canvas.currentHeight));
        } else if (this.canvas.graphicsImporter != null) {
            this.canvas.graphicsImporter
                    .setBoundsRect(new QDRect(localPoint.x, localPoint.y, this.canvas.currentWidth, this.canvas.currentHeight));
        } else if (this.canvas.channel != null) {
            ((SGVideoChannel) this.canvas.channel)
                    .setBounds(new QDRect(localPoint.x, localPoint.y, this.canvas.currentWidth, this.canvas.currentHeight));
            if (!QDGraphics.scratch.equals(getGWorld())) {
                this.deferTasking = false;
                startTasking();
            }
        }
    }
}


