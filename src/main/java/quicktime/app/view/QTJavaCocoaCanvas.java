
package quicktime.app.view;

import com.apple.eawt.CocoaComponent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.sg.SGVideoChannel;


class QTJavaCocoaCanvas extends CocoaComponent implements QTComponent, KeyListener, MouseListener {

    private static final Logger logger = System.getLogger(QTJavaCocoaCanvas.class.getName());

    private static final int kSetMovie = 0;
    private static final int kSetImage = 1;
    private static final int kSetMovieController = 2;
    private static final int kSetSeqGrabber = 3;

    static {
        if (!QTSession.isCurrentOS(4))
            throw new QTRuntimeException("OS not supported");
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() < 65540)) {
            throw new QTRuntimeException("Java version not supported");
        }

        String str = "/System/Library/QuickTimeJava/QuickTimeJava.bundle/Contents/Resources/libCocoaInterface.jnilib";
        System.load(str);

        initGlobalLock(QTNative.globalsLock);
    }

    private int qtjavaView = 0;

    private boolean addDone = false;

    private final boolean viewShown = false;

    private Movie movie;

    private MovieController controller;

    private GraphicsImporter image;

    private SGVideoChannel sgChannel;

    private int currentX;

    private int currentY;

    private int currentWidth;

    private int currentHeight;

    private int minWidth;

    private int minHeight;

    private int maxWidth;

    private int maxHeight;

    private final QDRect oldbounds = null;

    transient int initialWidth;

    transient int initialHeight;

    private transient int prefWidth;

    private transient int prefHeight;

    private boolean performanceResize = false;

    private final boolean QTMOVIEVIEW = false;

    private WindowAdapter wa;

    private Window parentWindow;

    public int createNSView() {
        QDRect localQDRect = new QDRect(0, 0, 1, 1);
        Object localObject = null;
        if (this.movie != null) {
            try {
                if (this.controller != null) {
                    try {
                        localQDRect = this.controller.getBounds();
                        logger.log(Level.DEBUG, "createNSView controller " + this.controller + "bounds " + localQDRect);
                    } catch (QTException _) {
                    }
                } else {
                    localQDRect = this.movie.getBounds();
                }
                setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
            } catch (StdQTException _) {
            }
            return this.qtjavaView = createNSViewNative(localQDRect.getWidthF(),
                                                        localQDRect.getHeightF(),
                                                        QTObject.ID(this.movie),
                                                        this.controller != null ? QTObject.ID(this.controller)
                                                                                : null);
        }

        if (this.image != null) {
            try {
                localQDRect = this.image.getBoundsRect();
                setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
            } catch (StdQTException _) {
            }
            return this.qtjavaView = createImageNSViewNative(localQDRect.getWidth(),
                                                             localQDRect.getHeight(),
                    QTObject.ID(this.image));
        }
        if (this.sgChannel != null) {
            try {
                if ((this.sgChannel instanceof SGVideoChannel))
                    localQDRect = this.sgChannel.getBounds();
                setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
                logger.log(Level.DEBUG, "***createNSView sgChannel " + this.sgChannel + ", " + localQDRect);
            } catch (StdQTException _) {
            }
            return this.qtjavaView = createSequenceGrabberNSView(localQDRect.getWidth(),
                                                                 localQDRect.getHeight(),
                    QTObject.ID(this.sgChannel.getSequenceGrabber()));
        }
        return 0;
    }

    QTJavaCocoaCanvas(MovieController paramMovieController) throws QTException {
        this();
        this.movie = paramMovieController.getMovie();
        this.controller = paramMovieController;
        logger.log(Level.DEBUG, "QTJavaCocoaCanvas " + this.movie + ", " + this.controller);
    }

    QTJavaCocoaCanvas(Movie paramMovie) {
        this();
        this.movie = paramMovie;
        this.controller = null;
        this.performanceResize = false;
        logger.log(Level.DEBUG, "QTJavaCocoaCanvas " + this.movie);
    }

    QTJavaCocoaCanvas(GraphicsImporter paramGraphicsImporter) {
        this();
        this.image = paramGraphicsImporter;
        logger.log(Level.DEBUG, "QTJavaCocoaCanvas " + paramGraphicsImporter);
    }

    QTJavaCocoaCanvas(SGVideoChannel paramSGVideoChannel) {
        this();
        this.sgChannel = paramSGVideoChannel;
        logger.log(Level.DEBUG, "QTJavaCocoaCanvas " + this.sgChannel);
    }

    private QTJavaCocoaCanvas() {
        this.initialWidth = 1;
        this.initialHeight = 1;
        this.minWidth = 1;
        this.minHeight = 1;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!this.addDone) {
            logger.log(Level.DEBUG, "addNotify qtjavaView " + this.qtjavaView);
            addedTo();
            QTSession.addActiveCanvas(this);
            this.addDone = true;
        }
        if (isVisible()) {
            repaint();
        }
    }

    @Override
    public synchronized void removeNotify() {
        logger.log(Level.DEBUG, "removeNotify:");
        if ((this.addDone) && (QTSession.isInitialized())) {
            try {
                if (this.movie != null)
                    doRemoveDrawable();
                QTSession.removeActiveCanvas(this);
                logger.log(Level.DEBUG, "removeNotify:");
                this.addDone = false;
            } finally {
                super.removeNotify();
            }
        } else
            super.removeNotify();
    }

    private void doParentLayout() {
        logger.log(Level.DEBUG, "doParentLayout:" + getParent());
        Container localContainer = getParent();
        if (localContainer != null) {
            invalidate();
            localContainer.validate();
        }

        super.setBounds(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    @Override
    public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        logger.log(Level.DEBUG, "setBounds: " + paramInt3 + "," + paramInt4);
        super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private void setCurrentXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = paramInt3 - this.currentWidth;
        if (i < 0) {
            i = 0;
        }

        int j = paramInt4 - this.currentHeight;
        if (j < 0) {
            j = 0;
        }
        if (this.performanceResize) {
            this.currentX = (paramInt1 + (int) (0.5F * i));
            this.currentY = (paramInt2 + (int) (0.5F * j));
        } else {
            this.currentX = paramInt1;
            this.currentY = paramInt2;
        }
        logger.log(Level.DEBUG, "setCurrentXY " + this.currentX + " " + this.currentY);
    }

    @Override
    public float getAlignmentX() {
        return this.performanceResize ? 0.5F : 0.0F;
    }

    @Override
    public float getAlignmentY() {
        return this.performanceResize ? 0.5F : 0.0F;
    }

    private void setCurrentWidthHeight(int paramInt1, int paramInt2) {
        if (paramInt1 < this.minWidth) {
            paramInt1 = this.minWidth;
        } else if (paramInt1 > this.maxWidth)
            paramInt1 = this.maxWidth;
        if (paramInt2 < this.minHeight) {
            paramInt2 = this.minHeight;
        } else if (paramInt2 > this.maxHeight) {
            paramInt2 = this.maxHeight;
        }
        if (this.performanceResize) {
            setPerformanceResize(paramInt1, paramInt2);
        } else {
            this.currentWidth = paramInt1;
            this.currentHeight = paramInt2;
        }

        logger.log(Level.DEBUG, "setCurrentWidthHeight( " + this.currentWidth + ", " + this.currentHeight + ", " + this.minWidth
                               + ", " + this.minHeight);
    }

    private void setInitialWidthAndHeight(QDRect paramQDRect, Dimension paramDimension1, Dimension paramDimension2) {
        this.initialWidth = paramQDRect.getWidth();
        this.initialHeight = paramQDRect.getHeight();

        this.minWidth = Math.max(paramDimension1.width, 0);
        this.minHeight = Math.max(paramDimension1.height, 0);
        this.maxWidth = Math.max(paramDimension2.width, this.minWidth);
        this.maxHeight = Math.max(paramDimension2.height, this.minHeight);
        if (this.initialWidth < this.minWidth) {
            this.initialWidth = this.minWidth;
        } else if (this.initialWidth > this.maxWidth) {
            this.initialWidth = this.maxWidth;
        }
        if (this.initialHeight < this.minHeight) {
            this.initialHeight = this.minHeight;
        } else if (this.initialHeight > this.maxHeight) {
            this.initialHeight = this.maxHeight;
        }
        this.prefWidth = this.initialWidth;
        this.prefHeight = this.initialHeight;
        logger.log(Level.DEBUG, "setInitialWidthAndHeight " + this.initialWidth + " " + this.initialHeight + " " + this.prefWidth
                               + " " + this.prefHeight);
    }

    private void setPerformanceResize(int i, int j) {
        boolean flag = false;
label0:
        for (int k1 = 2; !flag; k1 *= 2) {
            if (i < initialWidth || j < initialHeight) {
                int k = initialWidth / k1;
                int i1 = initialHeight / k1;
                if (k > i || i1 > j)
                    continue;
                flag = true;
                boolean flag1 = false;
                currentWidth = k;
                currentHeight = i1;
                do {
                    if (!flag1)
                        continue label0;
                    if (currentWidth >= minWidth && currentHeight >= minHeight)
                        flag1 = true;
                    currentWidth *= 2;
                    currentHeight *= 2;
                } while (true);
            }
            int l = initialWidth * k1;
            int j1 = initialHeight * k1;
            if (l < i && j1 < j)
                continue;
            flag = true;
            if (l > i || j1 > j) {
                l /= 2;
                j1 /= 2;
            }
            boolean flag2 = false;
            currentWidth = l;
            currentHeight = j1;
            do {
                if (!flag2)
                    continue label0;
                if (currentWidth <= maxWidth && currentHeight <= maxHeight)
                    flag2 = true;
                currentWidth /= 2;
                currentHeight /= 2;
            } while (true);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(this.prefWidth, this.prefHeight);
        logger.log(Level.DEBUG, "getPreferredSize " + localDimension);
        return localDimension;
    }

    public Dimension getBestSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(this.initialWidth, this.initialHeight);
        logger.log(Level.DEBUG, "getBestSize " + localDimension);
        return localDimension;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(this.minWidth, this.minHeight);
        logger.log(Level.DEBUG, "getMinimumSize " + localDimension);
        return localDimension;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(32767, 32767);
        logger.log(Level.DEBUG, "getMaximumSize " + localDimension);
        return localDimension;
    }

    private void setFullScreen() {
        setFullScreen((byte) 1);
    }

    public void removeMovie() throws QTException {
        if (this.movie != null) {
            this.movie.setRate(0.0F);
            this.movie.stop();
        }
    }

    @Override
    public Movie getMovie() {
        return this.movie;
    }

    @Override
    public MovieController getMovieController() {
        return this.controller;
    }

    @Override
    public void setMovieController(MovieController paramMovieController) throws QTException {
        QDRect localQDRect = null;
        if (paramMovieController == this.controller) {
            return;
        }
        doRemoveDrawable();

        this.controller = paramMovieController;
        this.movie = paramMovieController.getMovie();

        if (paramMovieController != null) {
            try {
                localQDRect = paramMovieController.getBounds();
                if ((this.qtjavaView != 0) && (isValid())) {
                    sendMessage(2, QTObject.ID(paramMovieController));
                    setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
                }
            } catch (QTException _) {
            }
            if ((this.qtjavaView != 0) && (isValid())) {
                addedTo();
            }
        }
    }

    @Override
    public void setMovie(Movie paramMovie) throws QTException {
        QDRect localQDRect = null;
        if (paramMovie == this.movie) {
            return;
        }
        logger.log(Level.DEBUG, "setMovie " + paramMovie);
        doRemoveDrawable();

        this.movie = paramMovie;
        if (paramMovie != null) {
            localQDRect = this.movie.getBounds();
            if ((this.qtjavaView != 0) && (isValid()))
                sendMessage(0, QTObject.ID(this.movie));
            setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
        }
    }

    public void setSGChannel(SGVideoChannel paramSGVideoChannel) throws QTException {
        QDRect localQDRect = new QDRect(0, 0, 1, 1);
        if (paramSGVideoChannel == this.sgChannel) {
            return;
        }
        logger.log(Level.DEBUG, "setSGChannel " + this.sgChannel);
        if (this.sgChannel != null) {
            doRemoveDrawable();
        }
        if (paramSGVideoChannel != null) {
            this.sgChannel = paramSGVideoChannel;
            if ((this.sgChannel instanceof SGVideoChannel))
                localQDRect = this.sgChannel.getBounds();
            if ((this.qtjavaView != 0) && (isValid()))
                sendMessage(3, QTObject.ID(this.sgChannel.getSequenceGrabber()));
            setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
        }
    }

    @Override
    public Component asComponent() {
        return this;
    }

    void doRemoveDrawable() {
        QDGraphics localQDGraphics = null;
        logger.log(Level.DEBUG, "doRemoveDrawable:");
        try {
            localQDGraphics = QDGraphics.scratch;
            if (this.movie != null && QTSession.isInitialized()) {
                this.movie.setRate(0.0F);
                this.movie.stop();
            }

            if ((this.qtjavaView != 0) && (isValid())) {
                if (this.movie != null) {
                    if (this.controller != null)
                        sendMessage(2, null);
                    if (this.movie != null)
                        sendMessage(0, null);
                }
                if (this.image != null) {
                    sendMessage(1, null);
                }
            }
        } catch (QTException localQTException) {
            localQTException.printStackTrace();
        } finally {
            if (this.controller != null)
                removedFrom();
            logger.log(Level.DEBUG, "finally:");
            this.movie = null;
            this.controller = null;
            this.image = null;
        }
    }

    @Override
    public GraphicsImporter getImage() {
        return this.image;
    }

    @Override
    public void setImage(GraphicsImporter paramGraphicsImporter) {
        QDRect localQDRect = null;
        if (this.image == paramGraphicsImporter) {
            return;
        }
        doRemoveDrawable();

        this.image = paramGraphicsImporter;
        if (paramGraphicsImporter != null) {
            try {
                localQDRect = this.image.getBoundsRect();
                setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
            } catch (StdQTException localStdQTException) {
            }
            if ((this.qtjavaView != 0) && (isValid()))
                sendMessage(1, QTObject.ID(this.image));
            doParentLayout();
        }
        repaint();
    }

    protected void addedTo() {
        try {
            logger.log(Level.DEBUG, "addedTo " + this.controller.getPort());
        } catch (StdQTException _) {
        }
        if (this.controller != null) {
            addKeyListener(this);
        }
        Object localObject1 = getParent();
        Object localObject2 = localObject1;
        while (localObject2 != null) {
            localObject1 = localObject2;
            if ((localObject1 instanceof Window))
                break;
            localObject2 = ((Container) localObject1).getParent();
        }
        if (localObject1 != null)
            ((Container) localObject1).addKeyListener(this);
        try {
            if (this.controller != null) {
                this.controller.setKeysEnabled(true);
            }
        } catch (StdQTException _) {
        }
        if ((localObject1 instanceof Window)) {
            this.parentWindow = ((Window) localObject1);
            this.wa = new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent paramAnonymousWindowEvent) {
                    try {
                        if (QTJavaCocoaCanvas.this.controller != null) {
                            QTJavaCocoaCanvas.this.controller.activate(QTJavaCocoaCanvas.this.controller.getPort(), true);
                            QTJavaCocoaCanvas.this.controller.activate();
                        }
                        QTJavaCocoaCanvas.this.redraw(null);
                    } catch (QTException _) {
                    }
                }

                @Override
                public void windowDeactivated(WindowEvent paramAnonymousWindowEvent) {
                    synchronized (QTSession.terminationLock()) {
                        if (QTSession.isInitialized()) {
                            try {
                                if (QTJavaCocoaCanvas.this.controller != null) {
                                    QTJavaCocoaCanvas.this.controller.deactivate();
                                    QTJavaCocoaCanvas.this.controller.activate(QTJavaCocoaCanvas.this.controller.getPort(),
                                                                               false);
                                }
                                QTJavaCocoaCanvas.this.redraw(null);
                            } catch (QTException _) {
                            }
                        } else {
                            QTJavaCocoaCanvas.this.removedFrom();
                        }
                    }
                }
            };
            this.parentWindow.addWindowListener(this.wa);
        }
    }

    protected void removedFrom() {
        removeKeyListener(this);
        if (getParent() != null)
            getParent().removeKeyListener(this);
        if (this.parentWindow != null) {
            this.parentWindow.removeWindowListener(this.wa);
            this.parentWindow = null;
            this.wa = null;
        }
    }

    @Override
    public void keyPressed(KeyEvent paramKeyEvent) {
        try {
            logger.log(Level.DEBUG, "Key pressed " + paramKeyEvent);
            if (this.controller != null) {
                boolean bool = this.controller.key(paramKeyEvent.getKeyCode(), paramKeyEvent.getModifiers());
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "keyPressed");
        }
    }

    @Override
    public void mousePressed(MouseEvent paramMouseEvent) {
        logger.log(Level.DEBUG, "mousePressed " + paramMouseEvent);
        try {
            Object localObject1 = getParent();
            Object localObject2 = localObject1;
            while (localObject2 != null) {
                logger.log(Level.DEBUG, localObject2 + " getLocation " + ((Container) localObject2).getLocation() + " getBounds "
                                       + ((Container) localObject2).getBounds());
                if (((localObject2 instanceof Window)) || ((localObject2 instanceof Frame)))
                    break;
                localObject1 = localObject2;
                localObject2 = ((Container) localObject1).getParent();
            }
            logger.log(Level.DEBUG, localObject1 + " getLocation " + ((Container) localObject1).getLocation() + " getBounds "
                                   + ((Container) localObject1).getBounds());
            logger.log(Level.DEBUG, paramMouseEvent.getX() + ((Container) localObject1).getLocation().x + getLocation().x
                               + ", "
                               + (paramMouseEvent.getY()
                                  + (((Container) localObject1).getLocation().y == 22 ? ((Container) localObject1)
                                          .getLocation().y : ((Container) localObject1).getLocation().y
                                                             - 22)
                                  + getLocation().y));

            if (this.controller != null)
                this.controller.click(
                                      new QDPoint(paramMouseEvent.getX() + ((Container) localObject1).getLocation().x
                                                  + getLocation().x,
                                                  ((Container) localObject1).getLocation().y != 22
                                                                                                   ? paramMouseEvent.getY()
                                                                                                     + (((Container) localObject1)
                                                                                                             .getLocation().y
                                                                                                        - 22)
                                                                                                     + getLocation().y
                                                                                                   : paramMouseEvent
                                                                                                           .getY()),
                                      paramMouseEvent.getModifiers());
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mousePressed");
        }
    }

    private void redraw(Region paramRegion) throws QTException {
        logger.log(Level.DEBUG, "RD:");
        if (paramRegion != null) {
            try {
                if (this.controller != null) {
                    this.controller.invalidate(this.controller.getPort(), paramRegion);
                    this.controller.idle();
                }
            } catch (StdQTException localStdQTException) {
                if (this.controller != null) {
                    this.controller.draw();
                }
            }
        } else if (this.controller != null) {
            this.controller.draw();
        }
    }

    private static void initGlobalLock(Object paramObject) {
        PlatformManager.initGlobalLock(paramObject);
    }

    private static void initGlobalOffScreen(Object paramObject) {
        PlatformManager.initGlobalOffScreen(paramObject);
    }

    private void setFullScreen(byte paramByte) {
        PlatformManager.setFullScreen(paramByte);
    }

    private void setMovie(int paramInt, Object paramObject) {
        PlatformManager.setMovie(paramInt, paramObject);
    }

    private void setMovieController(int paramInt, Object paramObject) {
        PlatformManager.setMovieController(paramInt, paramObject);
    }

    private int createNSViewNative(float paramFloat1, float paramFloat2, Object paramObject1, Object paramObject2) {
        return PlatformManager.createNSViewNative(paramFloat1, paramFloat2, paramObject1, paramObject2);
    }

    private int createImageNSViewNative(int paramInt1, int paramInt2, Object paramObject) {
        return PlatformManager.createImageNSViewNative(paramInt1, paramInt2, paramObject);
    }

    private int createSequenceGrabberNSView(int paramInt1, int paramInt2, Object paramObject) {
        return PlatformManager.createSequenceGrabberNSView(paramInt1, paramInt2, paramObject);
    }

    private Object getMovieController(int paramInt) {
        return PlatformManager.getMovieController(paramInt);
    }

    private void removeMovie(int paramInt) {
        PlatformManager.removeMovie(paramInt);
    }

    @Override
    public void keyReleased(KeyEvent paramKeyEvent) {
    }

    @Override
    public void keyTyped(KeyEvent paramKeyEvent) {
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
}
