
package quicktime.app.display;

import java.awt.AWTEventMulticaster;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.QTUnknownOSException;
import quicktime.app.image.ImageDrawer;
import quicktime.app.players.QTPlayer;
import quicktime.qd.NativeGraphics;
import quicktime.qd.NativeGraphicsException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


/**
 * @deprecated
 */
public class QTCanvas extends Canvas {
    private static boolean MRJWorkaroundFlag;

    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
        if (QTSession.isCurrentOS(2)) {
            MRJWorkaroundFlag = false;
        } else if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            String str = System.getProperty("java.version");
            int i = Character.getNumericValue(str.charAt(0));

            int j = 0;
            int k = 0;
            int m = 0;

            for (int n = 1; n < str.length(); n++) {
                char c = str.charAt(n);
                if (Character.isDigit(c)) {
                    if (m == 0) {
                        j = Character.getNumericValue(c);
                        m = 1;
                    } else {
                        k = Character.getNumericValue(c);
                    }
                }
            }

            if ((i == 1) && (j == 1) && (k < 8)) {
                MRJWorkaroundFlag = true;
            } else {
                MRJWorkaroundFlag = false;
            }
        }
    }

    private static int canvasCount = 0;

    /**
     * @deprecated
     */
    public static final int kIntegralResize = 1;

    /**
     * @deprecated
     */
    public static final int kInitialSize = 2;

    /**
     * @deprecated
     */
    public static final int kFreeResize = 3;

    /**
     * @deprecated
     */
    public static final int kAspectResize = 4;

    /**
     * @deprecated
     */
    public static final int kPerformanceResize = 5;

    /**
     * @deprecated
     */
    public static final int kHorizontalResize = 6;

    /**
     * @deprecated
     */
    public static final int kVerticalResize = 7;

    /**
     * @deprecated
     */
    public static final int kInitialSizeNoResize = 8;

    transient int numCanvas;

    private transient Vector<?> mControllers;

    private QTCanvasHelper qtcHelper;

    /**
     * @deprecated
     */
    public QTCanvas() {
        this(3, 0.0F, 0.0F);
    }

    /**
     * @deprecated
     */
    public QTCanvas(int paramInt, float paramFloat1, float paramFloat2) {
        this.resizeFlag = paramInt;
        this.initialWidth = 1;
        this.initialHeight = 1;
        setAlignmentValues(paramFloat1, paramFloat2);
        this.minWidth = 1;
        this.minHeight = 1;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
        this.numCanvas = (canvasCount++);

        this.qtcHelper = new QTCanvasHelper(this);
    }

    private boolean doRedrawForQTPlayer = false;

    private float xAlignment;

    private float yAlignment;

    private int resizeFlag;

    transient int initialWidth;

    transient int initialHeight;

    int currentX;

    int currentY;

    private int minWidth;

    private int minHeight;

    private int maxWidth;

    private int maxHeight;

    private transient int prefWidth;

    private transient int prefHeight;

    int currentWidth;

    int currentHeight;

    transient NativeGraphics ng = null;

    private transient long msecs = 0L;

    transient boolean deferResize = false;

    private transient boolean appHasSetPreferredSize = false;

    boolean isWin = QTSession.isCurrentOS(2);

    /**
     * @deprecated
     */
    public final QDGraphics getPort() throws QTUnknownOSException, NativeGraphicsException {
        QDGraphics localQDGraphics = null;
        if (isVisible()) {
            localQDGraphics = this.ng != null ? this.ng.getGWorld() : null;
        }
        return localQDGraphics == null ? QDGraphics.scratch : localQDGraphics;
    }

    /**
     * @deprecated
     */
    public final Point getNGLocation() throws QTUnknownOSException, NativeGraphicsException {
        if ((this.qtcHelper.mClient instanceof QTDrawable)) {
            return this.ng != null ? this.ng.getLocation() : new Point(0, 0);
        }
        return getLocation();
    }

    /**
     * @deprecated
     */
    public final NativeGraphics getNativeGraphics() {
        return this.ng;
    }

    /**
     * @deprecated
     */
    public final QDPoint translatePoint(int paramInt1, int paramInt2) throws QTUnknownOSException, NativeGraphicsException {
        Point localPoint = getNGLocation();

        return new QDPoint(paramInt1 + localPoint.x, paramInt2 + localPoint.y);
    }

    /**
     * @deprecated
     */
    public final synchronized void removeClient() {
        this.qtcHelper.doRemoveClient();
        repaint();
    }

    /**
     * @deprecated
     */
    public synchronized void setClient(Drawable paramDrawable, boolean paramBoolean) throws QTException {
        this.qtcHelper.doSetClient(paramDrawable,
                                   paramDrawable != null ? paramDrawable.getDisplayBounds() : null,
                                   getMinimumSize(),
                                   getMaximumSize(),
                                   paramBoolean);
    }

    /**
     * @deprecated
     */
    public synchronized void setClient(Drawable paramDrawable, QDRect paramQDRect) throws QTException {
        this.qtcHelper.doSetClient(paramDrawable, paramQDRect, getMinimumSize(), getMaximumSize(), true);
    }

    /**
     * @deprecated
     */
    public final Drawable getClient() {
        return this.qtcHelper.mClient;
    }

    boolean deferRedraw = false;

    private boolean moved;

    void setInitialWidthAndHeight(QDRect paramQDRect, Dimension paramDimension1, Dimension paramDimension2) {
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
        if (!this.appHasSetPreferredSize) {
            this.prefWidth = this.initialWidth;
            this.prefHeight = this.initialHeight;
        }
    }

    final void doParentLayout() {
        Container localContainer = getParent();
        if (localContainer != null) {
            invalidate();
            localContainer.validate();
            return;
        }
        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    /**
     * @deprecated
     */
    public void setAlignment(float paramFloat1, float paramFloat2) {
        Rectangle localRectangle = getBounds();
        setAlignmentValues(paramFloat1, paramFloat2);
        doParentLayout();

        this.doRedrawForQTPlayer = this.qtcHelper.shouldRedrawQTPlayer(localRectangle);
    }

    public final void setAlignmentX(float paramFloat) {
        setAlignment(paramFloat, getAlignmentY());
    }

    public final void setAlignmentY(float paramFloat) {
        setAlignment(getAlignmentX(), paramFloat);
    }

    private final void setAlignmentValues(float paramFloat1, float paramFloat2) {
        if (paramFloat1 > 1.0F) {
            this.xAlignment = 1.0F;
        } else if (paramFloat1 < 0.0F)
            this.xAlignment = 0.0F;
        else {
            this.xAlignment = paramFloat1;
        }
        if (paramFloat2 > 1.0F) {
            this.yAlignment = 1.0F;
        } else if (paramFloat2 < 0.0F)
            this.yAlignment = 0.0F;
        else {
            this.yAlignment = paramFloat2;
        }
    }

    /**
     * @deprecated
     */
    public float getAlignmentX() {
        return this.xAlignment;
    }

    /**
     * @deprecated
     */
    public float getAlignmentY() {
        return this.yAlignment;
    }

    /**
     * @deprecated
     */
    public void setResizeFlag(int paramInt) {
        this.resizeFlag = paramInt;
        Rectangle localRectangle = getBounds();
        doParentLayout();
        this.doRedrawForQTPlayer = this.qtcHelper.shouldRedrawQTPlayer(localRectangle);
    }

    /**
     * @deprecated
     */
    public int getResizeFlag() {
        return this.resizeFlag;
    }

    /**
     * @deprecated
     */
    public void setMinimumSize(Dimension paramDimension) {
        if (paramDimension == null) {
            setMinimumSize(0, 0);
        } else {
            setMinimumSize(paramDimension.width, paramDimension.height);
        }
    }

    /**
     * @deprecated
     */
    public void setMinimumSize(int paramInt1, int paramInt2) {
        if (paramInt1 < 0)
            this.minWidth = 0;
        else {
            this.minWidth = paramInt1;
        }
        if (paramInt2 < 0)
            this.minHeight = 0;
        else {
            this.minHeight = paramInt2;
        }
        if ((this.currentWidth < this.minWidth) || (this.currentHeight < this.minHeight)) {
            doParentLayout();
        }
    }

    /**
     * @deprecated
     */
    public Dimension getMinimumSize() {
        return new Dimension(this.minWidth, this.minHeight);
    }

    /**
     * @deprecated
     */
    public Dimension getBestSize() {
        return new Dimension(this.initialWidth, this.initialHeight);
    }

    /**
     * @deprecated
     */
    public void clientChanged(Dimension paramDimension) {
        clientChanged(paramDimension.width, paramDimension.height);
    }

    /**
     * @deprecated
     */
    public void clientChanged(int paramInt1, int paramInt2) {
        Rectangle localRectangle = new Rectangle(getLocation(), new Dimension(this.initialWidth, this.initialHeight));
        if (paramInt1 < 0)
            this.initialWidth = 0;
        else {
            this.initialWidth = paramInt1;
        }
        if (paramInt2 < 0)
            this.initialHeight = 0;
        else {
            this.initialHeight = paramInt2;
        }
        if (!this.appHasSetPreferredSize) {
            this.prefWidth = this.initialWidth;
            this.prefHeight = this.initialHeight;
        }

        this.doRedrawForQTPlayer = this.qtcHelper.shouldRedrawQTPlayer(localRectangle);
    }

    /**
     * @deprecated
     */
    public Dimension getPreferredSize() {
        return new Dimension(this.prefWidth, this.prefHeight);
    }

    /**
     * @deprecated
     */
    public void setPreferredSize(Dimension paramDimension) {
        if (paramDimension == null) {
            setPreferredSize(this.initialWidth, this.initialHeight);
        } else {
            setPreferredSize(paramDimension.width, paramDimension.height);
        }
    }

    /**
     * @deprecated
     */
    public void setPreferredSize(int paramInt1, int paramInt2) {
        if (paramInt1 < 0) {
            this.prefWidth = this.initialWidth;
            this.appHasSetPreferredSize = false;
        } else {
            this.prefWidth = paramInt1;
            this.appHasSetPreferredSize = true;
        }

        if (paramInt2 < 0) {
            this.prefHeight = this.initialHeight;
            this.appHasSetPreferredSize = false;
        } else {
            this.prefHeight = paramInt2;
            this.appHasSetPreferredSize = true;
        }

        if ((this.currentWidth != this.prefWidth) || (this.currentHeight != this.prefHeight)) {
            doParentLayout();
        }
    }

    /**
     * @deprecated
     */
    public boolean isFocusTraversable() {
        return true;
    }

    /**
     * @deprecated
     */
    public void setMaximumSize(Dimension paramDimension) {
        if (paramDimension == null) {
            Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
            setMaximumSize(localDimension.width, localDimension.height);
        } else {
            setMaximumSize(paramDimension.width, paramDimension.height);
        }
    }

    /**
     * @deprecated
     */
    public void setMaximumSize(int paramInt1, int paramInt2) {
        if (paramInt1 < 0)
            this.maxWidth = 0;
        else {
            this.maxWidth = paramInt1;
        }
        if (paramInt2 < 0)
            this.maxHeight = 0;
        else {
            this.maxHeight = paramInt2;
        }
        if ((this.currentWidth > this.maxWidth) || (this.currentHeight > this.maxHeight)) {
            doParentLayout();
        }
    }

    /**
     * @deprecated
     */
    public Dimension getMaximumSize() {
        return new Dimension(this.maxWidth, this.maxHeight);
    }

    private Dimension getAspectSize(int paramInt1, int paramInt2) {
        if (paramInt1 < this.minWidth) {
            paramInt1 = this.minWidth;
        } else if (paramInt1 > this.maxWidth)
            paramInt1 = this.maxWidth;
        if (paramInt2 < this.minHeight) {
            paramInt2 = this.minHeight;
        } else if (paramInt2 > this.maxHeight) {
            paramInt2 = this.maxHeight;
        }

        float f1 = paramInt1 / (this.initialWidth < 1 ? 1 : this.initialWidth);
        float f2 = paramInt2 / (this.initialHeight < 1 ? 1 : this.initialHeight);

        int i;

        int j;
        if (f1 < f2) {
            i = (int) (this.initialWidth * f1);
            j = (int) (this.initialHeight * f1);
        } else {
            i = (int) (this.initialWidth * f2);
            j = (int) (this.initialHeight * f2);
        }

        return new Dimension(i, j);
    }

    private void setCurrentWidthHeight(int width, int height) {
        switch (getResizeFlag()) {
        case 2:
            this.currentWidth = this.initialWidth;
            this.currentHeight = this.initialHeight;

            if (this.currentWidth < this.minWidth) {
                this.currentWidth = this.minWidth;
            } else if (this.currentWidth > this.maxWidth)
                this.currentWidth = this.maxWidth;
            if (this.currentHeight < this.minHeight) {
                this.currentHeight = this.minHeight;
            } else if (this.currentHeight > this.maxHeight) {
                this.currentHeight = this.maxHeight;
            }

            if ((this.currentWidth > width) || (this.currentHeight > height)) {
                Dimension localDimension = getAspectSize(width, height);
                this.currentWidth = localDimension.width;
                this.currentHeight = localDimension.height;
            }

            break;
        case 4:
            Dimension localDimension = getAspectSize(width, height);
            this.currentWidth = localDimension.width;
            this.currentHeight = localDimension.height;
            break;

        case 6:
            this.currentWidth = width;
            this.currentHeight = this.initialHeight;

            if (this.currentWidth < this.minWidth) {
                this.currentWidth = this.minWidth;
            } else if (this.currentWidth > this.maxWidth)
                this.currentWidth = this.maxWidth;
            if (this.currentHeight < this.minHeight) {
                this.currentHeight = this.minHeight;
            } else if (this.currentHeight > this.maxHeight) {
                this.currentHeight = this.maxHeight;
            }

            if (this.currentHeight > height) {
                Dimension localDimension2 = getAspectSize(width, height);
                this.currentHeight = localDimension2.height;
            }

            break;
        case 7:
            this.currentWidth = this.initialWidth;
            this.currentHeight = height;

            if (this.currentWidth < this.minWidth) {
                this.currentWidth = this.minWidth;
            } else if (this.currentWidth > this.maxWidth)
                this.currentWidth = this.maxWidth;
            if (this.currentHeight < this.minHeight) {
                this.currentHeight = this.minHeight;
            } else if (this.currentHeight > this.maxHeight) {
                this.currentHeight = this.maxHeight;
            }

            if (this.currentWidth > width) {
                Dimension localDimension2 = getAspectSize(width, height);
                this.currentWidth = localDimension2.width;
            }

            break;
        case 5:
            if (width < this.minWidth) {
                width = this.minWidth;
            } else if (width > this.maxWidth)
                width = this.maxWidth;
            if (height < this.minHeight) {
                height = this.minHeight;
            } else if (height > this.maxHeight) {
                height = this.maxHeight;
            }
            boolean flag = false;
            int scale = 2;

            while (!flag) {
                boolean flag2 = false;
                if ((width < this.initialWidth) || (height < this.initialHeight)) {
                    int w = this.initialWidth / scale;
                    int h = this.initialHeight / scale;
                    if ((w <= width) && (h <= height)) {
                        flag = true;

                        flag2 = false;
                        this.currentWidth = w;
                        this.currentHeight = h;

                        while (flag2 != false) {
                            if ((this.currentWidth >= this.minWidth) && (this.currentHeight >= this.minHeight)) {
                                flag2 = true;
                            }
                        }
                    }
                } else {
                    this.currentWidth *= 2;
                    this.currentHeight *= 2;

                    int w = this.initialWidth * scale;
                    int h = this.initialHeight * scale;
                    if ((w >= width) || (h >= height)) {
                        flag = true;

                        if ((w > width) || (h > height)) {
                            w /= 2;
                            h /= 2;
                        }

                        flag2 = false;
                        this.currentWidth = w;
                        this.currentHeight = h;
                        while (flag2 != false) {
                            if ((this.currentWidth <= this.maxWidth) && (this.currentHeight <= this.maxHeight)) {
                                flag2 = true;
                            }
                            this.currentWidth /= 2;
                            this.currentHeight /= 2;
                        }
                    }
                }
                scale *= 2;
            }
            break;
        case 1:
            if (width < this.minWidth) {
                width = this.minWidth;
            } else if (width > this.maxWidth)
                width = this.maxWidth;
            if (height < this.minHeight) {
                height = this.minHeight;
            } else if (height > this.maxHeight) {
                height = this.maxHeight;
            }
            int w = this.initialWidth == 0 ? 0 : width / this.initialWidth;
            int h = this.initialHeight == 0 ? 0 : height / this.initialHeight;

            int l = Math.min(w, h);
            if (l < 1) {
                float w2 = this.initialWidth == 0 ? 0.0F : width / this.initialWidth;
                float h2 = this.initialHeight == 0 ? 0.0F : height / this.initialHeight;

                l = 0;
                if (w2 <= h2) {
                    l = this.initialWidth / width + 1;
                } else
                    l = this.initialHeight / height + 1;
                this.currentWidth = ((int) (this.initialWidth * 1.0F / l));
                this.currentHeight = ((int) (this.initialHeight * 1.0F / l));
            } else {
                this.currentWidth = (this.initialWidth * l);
                this.currentHeight = (this.initialHeight * l);
            }
            break;
        case 8:
            this.currentWidth = this.initialWidth;
            this.currentHeight = this.initialHeight;

            if (this.currentWidth < this.minWidth) {
                this.currentWidth = this.minWidth;
            } else if (this.currentWidth > this.maxWidth)
                this.currentWidth = this.maxWidth;
            if (this.currentHeight < this.minHeight) {
                this.currentHeight = this.minHeight;
            } else if (this.currentHeight > this.maxHeight) {
                this.currentHeight = this.maxHeight;
            }
            break;
        case 3:
        default:
            if (width < this.minWidth) {
                width = this.minWidth;
            } else if (width > this.maxWidth)
                width = this.maxWidth;
            if (height < this.minHeight) {
                height = this.minHeight;
            } else if (height > this.maxHeight) {
                height = this.maxHeight;
            }
            this.currentWidth = width;
            this.currentHeight = height;
            break;
        }
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

        this.currentX = (paramInt1 + (int) (this.xAlignment * i));
        this.currentY = (paramInt2 + (int) (this.yAlignment * j));
    }

    @Deprecated
    public void setSize(Dimension paramDimension) {
        setSize(paramDimension.width, paramDimension.height);
    }

    /**
     * @deprecated
     */
    public void setSize(int paramInt1, int paramInt2) {
        setBounds(this.currentX, this.currentY, paramInt1, paramInt2);
    }

    /**
     * @deprecated
     */
    public final void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    /**
     * @deprecated
     */
    public final synchronized void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        boolean bool = isValid();

        int i = this.currentWidth;
        int j = this.currentHeight;
        setCurrentWidthHeight(paramInt3, paramInt4);
        int k = (i != this.currentWidth) || (j != this.currentHeight) ? 1 : 0;

        int m = this.currentX;
        int n = this.currentY;
        setCurrentXY(paramInt1, paramInt2, paramInt3, paramInt4);

        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);

        if (this.qtcHelper.mClient != null) {
            this.deferResize = ((this.deferResize) || (k != 0));
            if (this.deferResize) {
                this.doRedrawForQTPlayer = ((!this.isWin) && (!bool)
                                            && (((this.qtcHelper.mClient instanceof QTPlayer))
                                                || ((this.qtcHelper.mClient instanceof GroupDrawable))
                                                || ((this.qtcHelper.mClient instanceof ImageDrawer)))
                                            && (isShowing()));
            }

            this.moved = ((m != this.currentX) || (n != this.currentY) || (this.moved));
        }
    }

    private int countRegionErr = 0;

    ComponentListener clientListener;

    /**
     * @deprecated
     */
    public void update(Graphics paramGraphics) {
        if (this.qtcHelper.mClient == null) {
            super.update(paramGraphics);
        } else if (QTSession.isCurrentOS(4)) {
            paint(paramGraphics);
        } else if ((!(this.qtcHelper.mClient instanceof QTDrawable)) || (this.deferRedraw)) {
            paint(paramGraphics);
        }
    }

    /**
     * @deprecated
     */
    public synchronized void setVisible(boolean paramBoolean) {
        super.setVisible(paramBoolean);
        try {
            if (paramBoolean) {
                this.qtcHelper.doSetGWorld(true);
                doParentLayout();
            } else {
                this.qtcHelper.doSetGWorld(false);
                Container localContainer = getParent();
                if (localContainer != null) {
                    localContainer.repaint();
                }
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this.qtcHelper.mClient, "setGWorld");
        }
    }

    /**
     * @deprecated
     */
    public void addQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = AWTEventMulticaster.add(this.clientListener, paramComponentListener);
    }

    /**
     * @deprecated
     */
    public void removeQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = AWTEventMulticaster.remove(this.clientListener, paramComponentListener);
    }

    /**
     * @deprecated
     */
    public synchronized void addNotify() {
        super.addNotify();
        if (!this.addDone) {
            try {
                this.ng = NativeGraphics.getContext(this);
                if (this.qtcHelper.mClient != null) {
                    this.qtcHelper.doSetGWorld(isVisible());
                    this.qtcHelper.mClient.addedTo(this);
                    if (isVisible()) {
                        if (!this.isWin) {
                            this.deferRedraw = true;
                        } else
                            repaint();
                    }
                }
                QTSession.addActiveCanvas(this);
            } catch (QTException e) {
                this.qtcHelper.doRemoveClient();
                throw new QTRuntimeException(e);
            }
            this.addDone = true;
        }
    }

    private boolean addDone = false;

    /**
     * @deprecated
     */
    public synchronized void removeNotify() {
        synchronized (QTSession.terminationLock()) {
            if ((this.addDone) && (QTSession.isInitialized())) {
                try {
                    if (this.qtcHelper.mClient != null) {
                        this.qtcHelper.doSetGWorld(false);
                        this.qtcHelper.mClient.removedFrom(this);
                    }
                    QTSession.removeActiveCanvas(this);

                    this.addDone = false;
                } catch (QTException e) {
                    this.qtcHelper.doRemoveClient();
                    throw new QTRuntimeException(e);
                } finally {
                    if ((QTSession.isCurrentOS(2)) && (this.ng != null)) {
                        RemoveContext(this.ng);
                    }

                    this.ng = null;
                    super.removeNotify();
                }
            } else {
                super.removeNotify();
            }
        }
    }

    /**
     * @deprecated
     */
    public String toString() {
        return getClass().getName() + "_" + this.numCanvas + "_" + "[Location=" + getLocation() + ",Size=" + getSize() + "]";
    }

    protected void finalize() throws Throwable {
        try {
            removeNotify();
        } finally {
            this.qtcHelper.mClient = null;
        }

        super.finalize();
    }

    @Deprecated
    public void paint(Graphics g) {
        if (qtcHelper.mClient != null) {
            if (isValid()) {
                synchronized (this) {
                    QDGraphics qdgraphics;
                    quicktime.qd.Region region;
                    qdgraphics = null;
                    region = null;
                    try {

                        if (QTSession.isCurrentOS(1) || QTSession.isCurrentOS(4)) {
                            qdgraphics = getPort();
                            region = qdgraphics.getClip();
                            qdgraphics.clipRect();
                        }
                        synchronized (qtcHelper.mClient) {
                            if (deferResize)

                                qtcHelper.doClientSetBounds();
                            if (moved) {
                                moved = false;
                                if (clientListener != null)
                                    clientListener.componentMoved(new ComponentEvent(this, 100));
                            }
                            if (deferResize) {
                                deferResize = false;
                                if (clientListener != null)
                                    clientListener.componentResized(new ComponentEvent(this, 101));

                                if (doRedrawForQTPlayer) {
                                    qtcHelper.mClient.redraw(((quicktime.qd.Region) (null)));
                                    doRedrawForQTPlayer = false;
                                }
                                if (!(!MRJWorkaroundFlag || deferRedraw || isWin))
                                    return;
                            }
                        }
                        if (qtcHelper.mClient instanceof QTDrawable) {
                            if (!((QTDrawable) qtcHelper.mClient).getGWorld().equals(QDGraphics.scratch)) {
                                QTUtils.checkFreeMemory();
                                qtcHelper.mClient.redraw(((quicktime.qd.Region) null));
                            }
                        } else if (isShowing()) {
                            QTUtils.checkFreeMemory();
                            qtcHelper.mClient.redraw(((quicktime.qd.Region) null));
                        }
                        deferRedraw = false;
                    } catch (QTException e) {
                        if (e.errorCode() == -500 || e.errorCode() == -108) {
                            if (e.errorCode() == -108)

                                try {
                                    QTUtils.checkFreeMemory();
                                } catch (UtilException f) {
                                    QTRuntimeException.handleOrThrow(new QTRuntimeException(f),
                                                                     qtcHelper.mClient,
                                                                     "redraw");
                                }
                            if (countRegionErr < 3) {
                                if (deferResize)
                                    doRedrawForQTPlayer = true;
                                countRegionErr++;
                                QTUtils.reclaimMemory();
                                repaint();
                                return;
                            } else {
                                countRegionErr = 0;
                                QTRuntimeException.handleOrThrow(new QTRuntimeException(e),
                                                                 qtcHelper.mClient,
                                                                 "setDisplayBounds");
                            }
                        } else {
                            QTRuntimeException.handleOrThrow(new QTRuntimeException(e),
                                                             qtcHelper.mClient,
                                                             "setDisplayBounds");
                        }
                    } finally {
                        if (qdgraphics != null && region != null)
                            qdgraphics.setClip(region);
                    }
                }
                countRegionErr = 0;
            }
            doParentLayout();
            repaint();
        }
    }

    private static native void RemoveContext(Object paramObject);
}
