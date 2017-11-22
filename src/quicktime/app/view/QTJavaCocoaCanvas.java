
package quicktime.app.view;

import com.apple.eawt.CocoaComponent;
import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
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
    private static final int kSetMovie = 0;

    private static final int kSetImage = 1;

    private static final int kSetMovieController = 2;

    private static final int kSetSeqGrabber = 3;

    private static boolean debug = false;

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

    private boolean viewShown = false;

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

    private QDRect oldbounds = null;

    transient int initialWidth;

    transient int initialHeight;

    private transient int prefWidth;

    private transient int prefHeight;

    private boolean performanceResize = false;

    private boolean QTMOVIEVIEW = false;

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
                        if (debug)
                            System.err.println("createNSView controller " + this.controller + "bounds " + localQDRect);
                    } catch (QTException localQTException) {
                    }
                } else {
                    localQDRect = this.movie.getBounds();
                }
                setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
            } catch (StdQTException localStdQTException1) {
            }
            return this.qtjavaView = createNSViewNative(localQDRect.getWidthF(),
                                                        localQDRect.getHeightF(),
                                                        new Integer(QTObject.ID(this.movie)),
                                                        this.controller != null ? new Integer(QTObject.ID(this.controller))
                                                                                : null);
        }

        if (this.image != null) {
            try {
                localQDRect = this.image.getBoundsRect();
                setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
            } catch (StdQTException localStdQTException2) {
            }
            return this.qtjavaView = createImageNSViewNative(localQDRect.getWidth(),
                                                             localQDRect.getHeight(),
                                                             new Integer(QTObject.ID(this.image)));
        }
        if (this.sgChannel != null) {
            try {
                if ((this.sgChannel instanceof SGVideoChannel))
                    localQDRect = this.sgChannel.getBounds();
                setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
                if (debug)
                    System.err.println("***createNSView sgChannel " + this.sgChannel + ", " + localQDRect);
            } catch (StdQTException localStdQTException3) {
            }
            return this.qtjavaView = createSequenceGrabberNSView(localQDRect.getWidth(),
                                                                 localQDRect.getHeight(),
                                                                 new Integer(QTObject.ID(this.sgChannel.getSequenceGrabber())));
        }
        return 0;
    }

    QTJavaCocoaCanvas(MovieController paramMovieController) throws QTException {
        this();
        this.movie = paramMovieController.getMovie();
        this.controller = paramMovieController;
        if (debug) {
            System.err.println("QTJavaCocoaCanvas " + this.movie + ", " + this.controller);
        }
    }

    QTJavaCocoaCanvas(Movie paramMovie) {
        this();
        this.movie = paramMovie;
        this.controller = null;
        this.performanceResize = false;
        if (debug) {
            System.err.println("QTJavaCocoaCanvas " + this.movie);
        }
    }

    QTJavaCocoaCanvas(GraphicsImporter paramGraphicsImporter) {
        this();
        this.image = paramGraphicsImporter;
        if (debug)
            System.err.println("QTJavaCocoaCanvas " + paramGraphicsImporter);
    }

    QTJavaCocoaCanvas(SGVideoChannel paramSGVideoChannel) {
        this();
        this.sgChannel = paramSGVideoChannel;
        if (debug)
            System.err.println("QTJavaCocoaCanvas " + this.sgChannel);
    }

    private QTJavaCocoaCanvas() {
        this.initialWidth = 1;
        this.initialHeight = 1;
        this.minWidth = 1;
        this.minHeight = 1;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
    }

    public void addNotify() {
        super.addNotify();
        if (!this.addDone) {
            if (debug)
                System.err.println("addNotify qtjavaView " + this.qtjavaView);
            addedTo();
            QTSession.addActiveCanvas(this);
            this.addDone = true;
        }
        if (isVisible()) {
            repaint();
        }
    }

    public synchronized void removeNotify() {
        if (debug)
            System.err.println("removeNotify:");
        if ((this.addDone) && (QTSession.isInitialized())) {
            try {
                if (this.movie != null)
                    doRemoveDrawable();
                QTSession.removeActiveCanvas(this);
                if (debug)
                    System.err.println("removeNotify:");
                this.addDone = false;
            } finally {
                super.removeNotify();
            }
        } else
            super.removeNotify();
    }

    private final void doParentLayout() {
        if (debug)
            System.err.println("doParentLayout:" + getParent());
        Container localContainer = getParent();
        if (localContainer != null) {
            invalidate();
            localContainer.validate();
        }

        super.setBounds(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        if (debug)
            System.err.println("setBounds: " + paramInt3 + "," + paramInt4);
        super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private final void setCurrentXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
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
        if (debug) {
            System.err.println("setCurrentXY " + this.currentX + " " + this.currentY);
        }
    }

    public float getAlignmentX() {
        return this.performanceResize ? 0.5F : 0.0F;
    }

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

        if (debug)
            System.out.println("setCurrentWidthHeight( " + this.currentWidth + ", " + this.currentHeight + ", " + this.minWidth
                               + ", " + this.minHeight);
    }

    private void setInitialWidthAndHeight(QDRect paramQDRect, Dimension paramDimension1, Dimension paramDimension2) {
        this.initialWidth = paramQDRect.getWidth();
        this.initialHeight = paramQDRect.getHeight();

        if (paramDimension1.width < 0)
            this.minWidth = 0;
        else {
            this.minWidth = paramDimension1.width;
        }
        if (paramDimension1.height < 0)
            this.minHeight = 0;
        else {
            this.minHeight = paramDimension1.height;
        }
        if (paramDimension2.width < this.minWidth)
            this.maxWidth = this.minWidth;
        else {
            this.maxWidth = paramDimension2.width;
        }
        if (paramDimension2.height < this.minHeight)
            this.maxHeight = this.minHeight;
        else {
            this.maxHeight = paramDimension2.height;
        }
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
        if (debug)
            System.err.println("setInitialWidthAndHeight " + this.initialWidth + " " + this.initialHeight + " " + this.prefWidth
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

    public Dimension getPreferredSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(this.prefWidth, this.prefHeight);
        if (debug)
            System.err.println("getPreferredSize " + localDimension);
        return localDimension;
    }

    public Dimension getBestSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(this.initialWidth, this.initialHeight);
        if (debug)
            System.err.println("getBestSize " + localDimension);
        return localDimension;
    }

    public Dimension getMinimumSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(this.minWidth, this.minHeight);
        if (debug)
            System.err.println("getMinimumSize " + localDimension);
        return localDimension;
    }

    public Dimension getMaximumSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(32767, 32767);
        if (debug)
            System.err.println("getMaximumSize " + localDimension);
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

    public Movie getMovie() {
        return this.movie;
    }

    public MovieController getMovieController() {
        return this.controller;
    }

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
                    sendMessage(2, new Integer(QTObject.ID(paramMovieController)));
                    setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
                }
            } catch (QTException localQTException) {
            }
            if ((this.qtjavaView != 0) && (isValid())) {
                addedTo();
            }
        }
    }

    public void setMovie(Movie paramMovie) throws QTException {
        QDRect localQDRect = null;
        if (paramMovie == this.movie) {
            return;
        }
        if (debug)
            System.err.println("setMovie " + paramMovie);
        doRemoveDrawable();

        this.movie = paramMovie;
        if (paramMovie != null) {
            localQDRect = this.movie.getBounds();
            if ((this.qtjavaView != 0) && (isValid()))
                sendMessage(0, new Integer(QTObject.ID(this.movie)));
            setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
        }
    }

    public void setSGChannel(SGVideoChannel paramSGVideoChannel) throws QTException {
        QDRect localQDRect = new QDRect(0, 0, 1, 1);
        if (paramSGVideoChannel == this.sgChannel) {
            return;
        }
        if (debug)
            System.err.println("setSGChannel " + this.sgChannel);
        if (this.sgChannel != null) {
            doRemoveDrawable();
        }
        if (paramSGVideoChannel != null) {
            this.sgChannel = paramSGVideoChannel;
            if ((this.sgChannel instanceof SGVideoChannel))
                localQDRect = this.sgChannel.getBounds();
            if ((this.qtjavaView != 0) && (isValid()))
                sendMessage(3, new Integer(QTObject.ID(this.sgChannel.getSequenceGrabber())));
            setInitialWidthAndHeight(localQDRect, getMinimumSize(), getMaximumSize());
        }
    }

    public Component asComponent() {
        return this;
    }

    void doRemoveDrawable() {
        QDGraphics localQDGraphics = null;
        if (debug)
            System.out.println("doRemoveDrawable:");
        try {
            localQDGraphics = QDGraphics.scratch;
            if ((this.movie != null) && (QTSession.isInitialized()) && (this.movie != null)) {
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
            if (debug)
                localQTException.printStackTrace();
        } finally {
            if (this.controller != null)
                removedFrom();
            if (debug)
                System.out.println("finally:");
            this.movie = null;
            this.controller = null;
            this.image = null;
        }
    }

    public GraphicsImporter getImage() {
        return this.image;
    }

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
                sendMessage(1, new Integer(QTObject.ID(this.image)));
            doParentLayout();
        }
        repaint();
    }

    protected void addedTo() {
        if (debug) {
            try {
                if (debug)
                    System.err.println("addedTo " + this.controller.getPort());
            } catch (StdQTException localStdQTException1) {
            }
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
        } catch (StdQTException localStdQTException2) {
        }
        if ((localObject1 instanceof Window)) {
            this.parentWindow = ((Window) localObject1);
            this.wa = new WindowAdapter() {
                public void windowActivated(WindowEvent paramAnonymousWindowEvent) {
                    try {
                        if (QTJavaCocoaCanvas.this.controller != null) {
                            QTJavaCocoaCanvas.this.controller.activate(QTJavaCocoaCanvas.this.controller.getPort(), true);
                            QTJavaCocoaCanvas.this.controller.activate();
                        }
                        QTJavaCocoaCanvas.this.redraw(null);
                    } catch (QTException localQTException) {
                    }
                }

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
                            } catch (QTException localQTException) {
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

    public void keyPressed(KeyEvent paramKeyEvent) {
        try {
            if (debug)
                System.err.println("Key pressed " + paramKeyEvent);
            if (this.controller != null) {
                boolean bool = this.controller.key(paramKeyEvent.getKeyCode(), paramKeyEvent.getModifiers());
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "keyPressed");
        }
    }

    public void mousePressed(MouseEvent paramMouseEvent) {
        if (debug)
            System.err.println("mousePressed " + paramMouseEvent);
        try {
            Object localObject1 = getParent();
            Object localObject2 = localObject1;
            while (localObject2 != null) {
                if (debug)
                    System.err.println(localObject2 + " getLocation " + ((Container) localObject2).getLocation() + " getBounds "
                                       + ((Container) localObject2).getBounds());
                if (((localObject2 instanceof Window)) || ((localObject2 instanceof Frame))
                    || ((localObject2 instanceof Applet)))
                    break;
                localObject1 = localObject2;
                localObject2 = ((Container) localObject1).getParent();
            }
            if (debug)
                System.err.println(localObject1 + " getLocation " + ((Container) localObject1).getLocation() + " getBounds "
                                   + ((Container) localObject1).getBounds());
            if ((localObject1 instanceof Applet)) {
                if (this.controller != null) {
                    this.controller.click(new QDPoint(paramMouseEvent.getX(),
                                                      paramMouseEvent.getY() + (((Container) localObject1).getLocation().y - 22)
                                                                              + getLocation().y),
                                          paramMouseEvent.getModifiers());
                }
            } else {
                if (debug) {
                    System.err.println(paramMouseEvent.getX() + ((Container) localObject1).getLocation().x + getLocation().x
                                       + ", "
                                       + (paramMouseEvent.getY()
                                          + (((Container) localObject1).getLocation().y == 22 ? ((Container) localObject1)
                                                  .getLocation().y : ((Container) localObject1).getLocation().y
                                                                     - 22)
                                          + getLocation().y));
                }

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
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mousePressed");
        }
    }

    private final void redraw(Region paramRegion) throws QTException {
        if (debug)
            System.out.println("RD:");
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

    private static native void initGlobalLock(Object paramObject);

    private static native void initGlobalOffScreen(Object paramObject);

    private native void setFullScreen(byte paramByte);

    private native void setMovie(int paramInt, Object paramObject);

    private native void setMovieController(int paramInt, Object paramObject);

    private native int createNSViewNative(float paramFloat1, float paramFloat2, Object paramObject1, Object paramObject2);

    private native int createImageNSViewNative(int paramInt1, int paramInt2, Object paramObject);

    private native int createSequenceGrabberNSView(int paramInt1, int paramInt2, Object paramObject);

    private native Object getMovieController(int paramInt);

    private native void removeMovie(int paramInt);

    public void keyReleased(KeyEvent paramKeyEvent) {
    }

    public void keyTyped(KeyEvent paramKeyEvent) {
    }

    public void mouseClicked(MouseEvent paramMouseEvent) {
    }

    public void mouseEntered(MouseEvent paramMouseEvent) {
    }

    public void mouseExited(MouseEvent paramMouseEvent) {
    }

    public void mouseReleased(MouseEvent paramMouseEvent) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/view/
 * QTJavaCocoaCanvas.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
