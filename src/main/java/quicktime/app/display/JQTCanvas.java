package quicktime.app.display;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.JComponent;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.anim.SWCompositor;
import quicktime.app.image.QTImageProducer;
import quicktime.qd.QDRect;
import quicktime.util.RawEncodedImage;
import vavi.awt.image.MacDirectColorModel;


/**
 * This is a Lightweight version of the JQTCanvas class designed for use with Swing
 * Because it is strictly lightweight, and has no native peer, you can only have client classes
 * that do not use a controller such as the Compositor class and MoviePlayer. QuickTime cannot
 * render controllers in an offscreen buffer.
 * @deprecated
 */
@Deprecated
public class JQTCanvas extends JComponent implements DrawingListener {

    private static final Logger logger = System.getLogger(JQTCanvas.class.getName());

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client should maintain the aspect size of its original dimensions
     * but grow bigger by integral increments (2x, 3x, 4x, etc.)
     */
    public static final int kIntegralResize = 1;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client should revert to its initially specified size. The client
     * can be made smaller (using an aspect setting) but can be no larger than its initial size. The JQTCanvas
     * takes this initial size as the best viewing size for its client.
     */
    public static final int kInitialSize = 2;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client can be resized to any size >= the minimum and <=
     * the maximum.
     */
    public static final int kFreeResize = 3;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client can be resized to any size which preserves the
     * initial aspect ratio.
     */
    public static final int kAspectResize = 4;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client can be resized to any size which is a factor of
     * 2 of the initial size.
     */
    public static final int kPerformanceResize = 5;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag to allow the client's horizontal aspect to be any size, but the vertical
     * axis will be the intial size of smaller. This is a good setting for QT VR content.
     */
    public static final int kHorizontalResize = 6;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag to allow the client's vertical aspect to be any size, but the horizontal
     * axis will be the intial size of smaller.
     */
    public static final int kVerticalResize = 7;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag specify that the client cannot be resized
     */
    public static final int kInitialSizeNoResize = 8;

    /**
     * @deprecated
     */
    public static boolean flashScaling = false;

    /**
     * @deprecated
     */
    public static final boolean useMacOSXAcceleration = true;

    transient QTDrawable client;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Default Constructor for JQTCanvas
     */
    public JQTCanvas() {
        this(3, 0.0F, 0.0F);
    }

    /**
     * @deprecated
     */
    public JQTCanvas(int resizeFlag, float alignmentX, float alignmentY) {
        this.resizeFlag = resizeFlag;
        setAlignmentX(alignmentX);
        setAlignmentY(alignmentY);
        this.initialWidth = 1;
        this.initialHeight = 1;
        setMinimumSize(new Dimension(1, 1));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    int initialWidth = 0;

    int initialHeight = 0;

    int currentWidth;

    int currentHeight;

    private int currentX;

    private int currentY;

    private int resizeFlag;

    private transient boolean inited = false;

    private final boolean paintlock = false;

    final java.awt.image.DirectColorModel colorModel = new java.awt.image.DirectColorModel(32, 16711680, 65280, 255, 0);

    /**
     * @deprecated
     */
    public QTImageProducer ip;

    java.awt.Image image;

    quicktime.qd.QDGraphics gw = null;

    QDRect gwBounds;

    int[] pixels;

    private ComponentListener clientListener;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is invoked by Swing to draw this component
     */
    @Override
    public void paint(java.awt.Graphics graphics) {
        if ((!this.inited) && (this.client != null)) {
            this.client.addedTo(this);
            this.inited = true;
            super.paint(graphics);
            return;
        }
        if (this.image != null) {
            Dimension size = getSize();
            graphics.drawImage(this.image, 0, 0, size.width, size.height, this);
        }
    }

    /**
     * @deprecated
     */
    public void setFlashScaling(boolean flashScaling) {
        flashScaling = flashScaling;
    }

    /**
     * @deprecated
     */
    public boolean isFlashScaling() {
        return flashScaling;
    }

    java.awt.Image imageFromEncodedImage(RawEncodedImage rawEncodedImage, QDRect rect) {
        rawEncodedImage.copyToArray(0, this.pixels, 0, this.pixels.length);
        java.awt.image.MemoryImageSource memoryImageSource = new java.awt.image.MemoryImageSource(rect.getWidth(), rect.getHeight(), this.colorModel, this.pixels, 0, rawEncodedImage.getRowBytes() / 4);
        return getToolkit().createImage(memoryImageSource);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Associates a new client, a Drawable object, with this JQTCanvas.
     * The flag determines if awt will perform a layout and how the client will be integrated
     * with the canvas. If the flag is false then the new client will take on the current size and
     * position of the canvas. If the flag is true, then awt will layout the canvas again, using
     * the initial size of the client and the resize flags to resize the canvas and its client.
     *
     * The size of the client when this method is called is used to ascertain the best size
     * for viewing this client's media.
     *
     * This method can throw a number of exceptions:
     * QTUnknownOSException the application is trying to run on a platform that is not supported by QuickTime
     * NativeGraphicsException the native graphics environment is not initialised
     * QTException or subclass - there was a problem within QuickTime itself in dealing with this client
     * @param layoutFlag if true java.awt will re-layout the canvas, if false the client will fit
     *  into the existing state (display size, etc) of the canvas, disregarding any resize that might
     *  have occured to honour the layout flag setting with respect to the different size of the new client.
     *  Keep in mind that only controllerless clients are valid
     * @throws quicktime.QTException
     */
    public void setClient(QTDrawable client, boolean layoutFlag) throws QTException {
        doSetClient(client, client != null ? client.getDisplayBounds() : null, getMinimumSize(), getMaximumSize(), layoutFlag);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Associates a new client, a Drawable object, with this JQTCanvas. It will use existing
     * alignment and resize restrictions.
     *
     * This method can throw a number of exceptions:
     * QTUnknownOSException the application is trying to run on a platform that is not supported by QuickTime
     * NativeGraphicsException the native graphics environment is not initialised
     * QTException or subclass - there was a problem within QuickTime itself in dealing with this client
     * @param initialBounds the best size of the client
     *  Keep in mind that only controllerless clients are valid
     * @throws quicktime.QTException
     */
    public synchronized void setClient(QTDrawable client, QDRect initialBounds) throws QTException {
        doSetClient(client, initialBounds, getMinimumSize(), getMaximumSize(), true);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Removes the current client, a drawable object, from the canvas.
     */
    public final void removeClient() {
        if ((this.client instanceof DrawingNotifier)) {
            ((DrawingNotifier) this.client).removeDrawingListener(this);
        }
        this.client.removedFrom(this);
        this.client = null;
        if (QTSession.isCurrentOS(4))
            this.gw = null;
        this.image = null;
        repaint();
    }

    private void doSetClient(QTDrawable client, QDRect rect, Dimension minSize, Dimension maxSize, boolean doLayout) throws QTException {
        if (client == this.client) {
            return;
        }
        if (this.client != null) {
            removeClient();
        }
        if (client != null) {
            this.client = client;
            if (doLayout) {
                setMinimumSize(minSize);
                setMaximumSize(maxSize);
                setPreferredSize(new Dimension(rect.getWidth(), rect.getHeight()));
                this.initialWidth = rect.getWidth();
                this.initialHeight = rect.getHeight();
                this.currentX = rect.getX();
                this.currentY = rect.getY();
                this.currentWidth = this.initialWidth;
                this.currentHeight = this.initialHeight;
                doParentLayout();
            }
            if (((client instanceof SWCompositor)) || ((client instanceof quicktime.app.anim.Compositor))) {
                SWCompositor swCompositor = (SWCompositor) client;
                this.gwBounds = swCompositor.getDisplayBounds();
                this.gw = swCompositor.getGWorld();
                if (this.gw == null) {
                    this.gw = new quicktime.qd.QDGraphics(this.gwBounds);
                    swCompositor.setGWorld(this.gw);
                }
                if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
                    boolean bool = false;
                    this.image = MacDirectColorModel.GetWrappedGWorldImage(quicktime.QTObject.ID(this.gw), 32, this.gwBounds.getWidth(), this.gwBounds.getHeight(), bool);
                } else {
                    this.pixels = new int[swCompositor.getImage().getRowBytes() / 4 * rect.getHeight()];
                    this.image = imageFromEncodedImage((RawEncodedImage) swCompositor.getImage(), swCompositor.getDescription().getBounds());
                }
                prepareImage(this.image, this);
                swCompositor.addDrawingListener(this);
            } else {
                client.redraw(null);
                this.ip = new QTImageProducer(this.client, new Dimension(rect.getWidth(), rect.getHeight()));
                this.image = createImage(this.ip);
                prepareImage(this.image, this);
            }
            if ((client instanceof DrawingNotifier))
                ((DrawingNotifier) client).addDrawingListener(this);
        }
        repaint();
    }

    /**
     * @deprecated
     */
    @Override
    public java.awt.Image createImage(java.awt.image.ImageProducer imageProducer) {
        if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
            boolean bool = false;
            QTImageProducer qtImageProducer = (QTImageProducer) imageProducer;
            Dimension size = qtImageProducer.getSize();
            quicktime.qd.QDGraphics gw = qtImageProducer.getGWorld();
            this.gw = gw;
            java.awt.image.BufferedImage image = (BufferedImage) MacDirectColorModel.GetWrappedGWorldImage(quicktime.QTObject.ID(gw), 32, size.width, size.height, bool);
            return image;
        }
        return super.createImage(imageProducer);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Moves and resizes this component. The new location of the top-left corner is specified by x and y,
     * and the new size is specified by width and height.
     * @param x the new x-coordinate of this component
     * @param y the new y-coordinate of this component
     * @param width the new width of this component
     * @param height the new height of this component
     * @see quicktime.app.display.JQTCanvas#setBounds(java.awt.Rectangle)
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        logger.log(Level.DEBUG, "setBounds( " + x + ", " + y + ", " + width + ", " + height + ")");
        Dimension dimension = setCurrentWidthHeight(width, height);
        if (flashScaling) {
            setPreferredSize(dimension);
            this.initialWidth = width;
            this.initialHeight = height;
            try {
                if (((this.client instanceof SWCompositor)) || ((this.client instanceof quicktime.app.anim.Compositor))) {
                    SWCompositor swCompositor = (SWCompositor) this.client;
                    this.gwBounds = new QDRect(getX(), getY(), width, height);
                    this.gw = new quicktime.qd.QDGraphics(this.gwBounds);
                    swCompositor.setGWorld(this.gw);
                    if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
                        boolean bool = false;
                        this.image = MacDirectColorModel.GetWrappedGWorldImage(quicktime.QTObject.ID(this.gw), 32, this.gwBounds.getWidth(), this.gwBounds.getHeight(), bool);
                    } else {
                        this.pixels = new int[swCompositor.getImage().getRowBytes() / 4 * this.gwBounds.getHeight()];
                        this.image = imageFromEncodedImage((RawEncodedImage) swCompositor.getImage(), this.gwBounds);
                    }
                    prepareImage(this.image, this);
                } else {
                    this.client.redraw(null);
                    this.ip = new QTImageProducer(this.client, dimension);
                    this.image = createImage(this.ip);
                    prepareImage(this.image, this);
                }
            } catch (QTException e) {
                e.printStackTrace();
            }
            repaint();
        }
        int i = width - this.currentWidth;
        if (i < 0) {
            i = 0;
        }
        int j = height - this.currentHeight;
        if (j < 0)
            j = 0;
        this.currentX = (x + (int) (getAlignmentX() * i));
        this.currentY = (y + (int) (getAlignmentY() * j));
        logger.log(Level.DEBUG, "setBounds(" + x + ", " + y + ", " + width + ", " + height + ")");
        logger.log(Level.DEBUG, "Alignment x: " + getAlignmentX() + ", y: " + getAlignmentY());
        logger.log(Level.DEBUG, "widthDiff: " + i);
        logger.log(Level.DEBUG, "heightDiff: " + j);
        logger.log(Level.DEBUG, "currentX:" + this.currentX);
        logger.log(Level.DEBUG, "currentY:" + this.currentY);
        super.reshape(this.currentX, this.currentY, dimension.width, dimension.height);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Moves and resizes this component to conform to the new bounding rectangle r.
     * This component's new position is specified by r.x and r.y, and its new size
     * is specified by r.width and r.height
     */
    @Override
    public void setBounds(Rectangle bounds) {
        logger.log(Level.DEBUG, "setBounds( " + bounds + ")");
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Resizes the JQTCanvas object to the specified parameters.
     * @param dim a Dimension object with the new width and height
     */
    @Override
    public void setSize(Dimension dim) {
        logger.log(Level.DEBUG, "setSize( " + dim + ")");
        setSize(dim.width, dim.height);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Resizes the JQTCanvas object, as well as the associated Drawable object.
     * @param width the width, in pixels, to make this component
     * @param height the height, in pixels, to make this component
     */
    @Override
    public void setSize(int width, int height) {
        logger.log(Level.DEBUG, "setSize( " + width + ", " + height + ")");
        setBounds(this.currentX, this.currentY, width, height);
    }

    /**
     * @deprecated
     * <I>As of JDK version 1.1,
     * replaced by setBounds(int, int, int, int).</I>
     */
    @Override
    public final void reshape(int x, int y, int width, int height) {
        logger.log(Level.DEBUG, "reshape( " + x + ", " + y + ", " + width + ", " + height + ")");
        setBounds(x, y, width, height);
    }

    private void doParentLayout() {
        logger.log(Level.DEBUG, "doParentLayout()");
        java.awt.Container parent = getParent();
        if (parent != null) {
            invalidate();
            parent.validate();
            return;
        }
        super.reshape(this.currentX, this.currentY, getWidth(), getHeight());
    }

    private Dimension setCurrentWidthHeight(int width, int height) {
        logger.log(Level.DEBUG, "setCurrentWidthHeight( " + width + ", " + height + "), flag " + getResizeFlag());
        Dimension min = getMinimumSize();
        Dimension max = getMaximumSize();
        int w1 = min.width;
        int h1 = min.height;
        int w2 = max.width;
        int h2 = max.height;
        switch(getResizeFlag()) {
            case 2:
                this.currentWidth = this.initialWidth;
                this.currentHeight = this.initialHeight;
                if (this.currentWidth < w1) {
                    this.currentWidth = w1;
                } else if (this.currentWidth > w2)
                    this.currentWidth = w2;
                if (this.currentHeight < h1) {
                    this.currentHeight = h1;
                } else if (this.currentHeight > h2) {
                    this.currentHeight = h2;
                }
                if ((this.currentWidth > width) || (this.currentHeight > height)) {
                    Dimension dim = getAspectSize(width, height);
                    this.currentWidth = dim.width;
                    this.currentHeight = dim.height;
                }
                break;
            case 4:
                Dimension dim2 = getAspectSize(width, height);
                this.currentWidth = dim2.width;
                this.currentHeight = dim2.height;
                break;
            case 6:
                this.currentWidth = width;
                this.currentHeight = this.initialHeight;
                if (this.currentWidth < w1) {
                    this.currentWidth = w1;
                } else if (this.currentWidth > w2)
                    this.currentWidth = w2;
                if (this.currentHeight < h1) {
                    this.currentHeight = h1;
                } else if (this.currentHeight > h2) {
                    this.currentHeight = h2;
                }
                if (this.currentHeight > height) {
                    Dimension dim = getAspectSize(width, height);
                    this.currentHeight = dim.height;
                }
                break;
            case 7:
                this.currentWidth = this.initialWidth;
                this.currentHeight = height;
                if (this.currentWidth < w1) {
                    this.currentWidth = w1;
                } else if (this.currentWidth > w2)
                    this.currentWidth = w2;
                if (this.currentHeight < h1) {
                    this.currentHeight = h1;
                } else if (this.currentHeight > h2) {
                    this.currentHeight = h2;
                }
                if (this.currentWidth > width) {
                    Dimension dim = getAspectSize(width, height);
                    this.currentWidth = dim.width;
                }
                break;
            case 5:
                if (width < w1) {
                    width = w1;
                } else if (width > w2)
                    width = w2;
                if (height < h1) {
                    height = h1;
                } else if (height > h2) {
                    height = h2;
                }
                boolean flag = false;
                int scale = 2;
                while (flag == false) {
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
                                if ((this.currentWidth >= w1) && (this.currentHeight >= h1)) {
                                    flag2 = true;
                                }
                                this.currentWidth *= 2;
                                this.currentHeight *= 2;
                            }
                        }
                    } else {
                        int i1 = this.initialWidth * scale;
                        int i2 = this.initialHeight * scale;
                        if ((i1 >= width) || (i2 >= height)) {
                            flag = true;
                            if ((i1 > width) || (i2 > height)) {
                                i1 /= 2;
                                i2 /= 2;
                            }
                            flag2 = false;
                            this.currentWidth = i1;
                            this.currentHeight = i2;
                            while (flag2 != false) {
                                if ((this.currentWidth <= w2) && (this.currentHeight <= h2)) {
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
                if (width < w1) {
                    width = w1;
                } else if (width > w2)
                    width = w2;
                if (height < h1) {
                    height = h1;
                } else if (height > h2) {
                    height = h2;
                }
                int w = this.initialWidth == 0 ? 0 : width / this.initialWidth;
                int h = this.initialHeight == 0 ? 0 : height / this.initialHeight;
                int l = Math.min(w, h);
                if (l < 1) {
                    float f1 = this.initialWidth == 0 ? 0.0F : width / this.initialWidth;
                    float f2 = this.initialHeight == 0 ? 0.0F : height / this.initialHeight;
                    l = 0;
                    if (f1 <= f2) {
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
                if (this.currentWidth < w1) {
                    this.currentWidth = w1;
                } else if (this.currentWidth > w2)
                    this.currentWidth = w2;
                if (this.currentHeight < h1) {
                    this.currentHeight = h1;
                } else if (this.currentHeight > h2) {
                    this.currentHeight = h2;
                }
                break;
            case 3:
            default:
                if (width < w1) {
                    width = w1;
                } else if (width > w2)
                    width = w2;
                if (height < h1) {
                    height = h1;
                } else if (height > h2) {
                    height = h2;
                }
                this.currentWidth = width;
                this.currentHeight = height;
                break;
        }
        return new Dimension(this.currentWidth, this.currentHeight);
    }

    private Dimension getAspectSize(int width, int height) {
        Dimension minimumSize = getMinimumSize();
        Dimension maximumSize = getMaximumSize();
        int i = minimumSize.width;
        int j = minimumSize.height;
        int k = maximumSize.width;
        int m = maximumSize.height;
        if (width < i) {
            width = i;
        } else if (width > k)
            width = k;
        if (height < j) {
            height = j;
        } else if (height > m) {
            height = m;
        }
        float f1 = width / (Math.max(this.initialWidth, 1));
        float f2 = height / (Math.max(this.initialHeight, 1));
        int n;
        int i1;
        if (f1 < f2) {
            n = (int) (this.initialWidth * f1);
            i1 = (int) (this.initialHeight * f1);
        } else {
            n = (int) (this.initialWidth * f2);
            i1 = (int) (this.initialHeight * f2);
        }
        return new Dimension(n, i1);
    }

    /**
     * @deprecated
     */
    public void setAlignment(float alignmentX, float alignmentY) {
        setAlignmentX(alignmentX);
        setAlignmentY(alignmentY);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Reset the resize flag and re-layout the component.
     * @param rFlag the new value for the resizeFlag
     */
    public void setResizeFlag(int rFlag) {
        this.resizeFlag = rFlag;
        Rectangle bounds = getBounds();
        doParentLayout();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current setting of the resize flag being used.
     * @return the resize flag value
     */
    public int getResizeFlag() {
        return this.resizeFlag;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Called when a client is finished drawing and the canvas needs to be updated
     * @param drawable the drawable which drawing has just been completed upon
     * @return an error code or zero if no error
     * @see <A href="../../../quicktime/app/display/package-summary.html">quicktime.app.display</A>
     */
    @Override
    public void drawingComplete(QTDrawable drawable) {
        if (((drawable instanceof SWCompositor)) || ((drawable instanceof quicktime.app.anim.Compositor))) {
            try {
                if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
                    boolean bool = false;
                    this.image = MacDirectColorModel.GetWrappedGWorldImage(quicktime.QTObject.ID(this.gw), 32, this.gwBounds.getWidth(), this.gwBounds.getHeight(), bool);
                } else {
                    this.image = imageFromEncodedImage((RawEncodedImage) ((SWCompositor) drawable).getImage(), ((SWCompositor) drawable).getDescription().getBounds());
                }
            } catch (QTException e) {
                e.printStackTrace();
            }
            repaint();
        } else {
            if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
                repaint();
                return;
            }
            try {
                this.ip.updateConsumers(null);
            } catch (QTException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * A ComponentListener and ComponentEvent are used to listen to these types
     * of events that occur to the QTCanvas' client - ie. when the QTCanvas' client
     * is resized, moved, shown or hidden. These events generally occur in conjunction
     * with the Component events they mirror - with some exceptions:
     * ComponentShown and ComponentHidden are generated when the QTCanvas is shown
     * or hidden and when the QTDrawable client is set or removed.
     * ComonentMoved and ComponentResized are generated when the QTCanvas is
     * moved or resized but the event is actually generated when the QTCanvas is repainted
     * as this is when the QTDrawable client is itself resized or moved.
     * @param e the ComponentListener
     * <HR/>
     * <A/>
     * <H3>
     * removeQTClientListener</H3>
     *
     * public void removeQTClientListener(java.awt.event.ComponentListener
     * e)
     * @deprecated
     * @since QTJava 6.1
     * Remove a previously registered ComponentListener that is listening
     * to events that occur with the QTCanvas' QTDrawable client.
     * @param e the ComponentListener
     * @see #addQTClientListener
     * <HR/>
     * <A/>
     * <TABLE>
     *
     * <TR>
     * <A/>
     * <TABLE>
     *
     * <TR>    <A href="../../../overview-summary.html">Overview</A> 
     * <TD>    <A href="package-summary.html">Package</A> </TD>
     * <TD>  Class </TD>
     * <TD>    <A href="package-tree.html">Tree</A> </TD>
     * <TD>    <A href="../../../deprecated-list.html">Deprecated</A> </TD>
     * <TD>    <A href="../../../index-all.html">Index</A> </TD>
     * <TD>    <A href="../../../help-doc.html">Help</A> </TD>
     * </TR>
     * </TABLE>
     *
     * <TD>
     *
     * </TD>
     * </TR>
     *
     * <TR>
     * <TD>
     *  <A href="../../../quicktime/app/display/GroupMember.html">PREV CLASS</A> 
     *  <A href="../../../quicktime/app/display/QTCanvas.html">NEXT CLASS</A></TD>
     * <TD>
     * <A href="../../../index.html" target="_top">FRAMES</A>   
     *  <A href="JQTCanvas.html" target="_top">NO FRAMES</A>   
     *  
     *
     * <A href="../../../allclasses-noframe.html" target="">All Classes</A>
     *
     * </TD>
     * </TR>
     * <TR>
     * <TD>
     * SUMMARY: NESTED | <A href="#field_summary">FIELD</A> | <A href="#constructor_summary">CONSTR</A> | <A href="#method_summary">METHOD</A></TD>
     * <TD>
     * DETAIL: <A href="#field_detail">FIELD</A> | <A href="#constructor_detail">CONSTR</A> | <A href="#method_detail">METHOD</A></TD>
     * </TR>
     * </TABLE>
     * <HR/>
     */
    public void addQTClientListener(ComponentListener e) {
        this.clientListener = java.awt.AWTEventMulticaster.add(this.clientListener, e);
    }

    /**
     * @deprecated
     */
    public void removeQTClientListener(ComponentListener componentListener) {
        this.clientListener = java.awt.AWTEventMulticaster.remove(this.clientListener, componentListener);
    }
}
