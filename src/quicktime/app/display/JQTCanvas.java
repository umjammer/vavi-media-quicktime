
package quicktime.app.display;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.anim.SWCompositor;
import quicktime.app.image.QTImageProducer;
import quicktime.qd.QDRect;
import quicktime.util.RawEncodedImage;


/**
 * @deprecated
 */
public class JQTCanvas extends JComponent implements DrawingListener {
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

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

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

    public static boolean flashScaling = false;

    public static boolean useMacOSXAcceleration = true;

    transient QTDrawable client;

    /**
     * @deprecated
     */
    public JQTCanvas() {
        this(3, 0.0F, 0.0F);
    }

    public JQTCanvas(int paramInt, float paramFloat1, float paramFloat2) {
        this.resizeFlag = paramInt;
        setAlignmentX(paramFloat1);
        setAlignmentY(paramFloat2);
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

    private boolean debug = false;

    private boolean paintlock = false;

    java.awt.image.DirectColorModel colorModel = new java.awt.image.DirectColorModel(32, 16711680, 65280, 255, 0);

    public QTImageProducer ip;

    java.awt.Image image;

    quicktime.qd.QDGraphics gw = null;

    QDRect gwBounds;

    int[] pixels;

    private ComponentListener clientListener;

    /**
     * @deprecated
     */
    public void paint(java.awt.Graphics paramGraphics) {
        if ((!this.inited) && (this.client != null)) {
            this.client.addedTo(this);
            this.inited = true;
            super.paint(paramGraphics);
            return;
        }

        if (this.image != null) {
            Dimension localDimension = getSize();
            paramGraphics.drawImage(this.image, 0, 0, localDimension.width, localDimension.height, this);
        }
    }

    public void setFlashScaling(boolean paramBoolean) {
        flashScaling = paramBoolean;
    }

    public boolean isFlashScaling() {
        return flashScaling;
    }

    java.awt.Image imageFromEncodedImage(RawEncodedImage paramRawEncodedImage, QDRect paramQDRect) {
        paramRawEncodedImage.copyToArray(0, this.pixels, 0, this.pixels.length);
        java.awt.image.MemoryImageSource localMemoryImageSource = new java.awt.image.MemoryImageSource(paramQDRect
                .getWidth(), paramQDRect.getHeight(), this.colorModel, this.pixels, 0, paramRawEncodedImage.getRowBytes() / 4);

        return getToolkit().createImage(localMemoryImageSource);
    }

    /**
     * @deprecated
     */
    public void setClient(QTDrawable paramQTDrawable, boolean paramBoolean) throws QTException {
        doSetClient(paramQTDrawable,
                    paramQTDrawable != null ? paramQTDrawable.getDisplayBounds() : null,
                    getMinimumSize(),
                    getMaximumSize(),
                    paramBoolean);
    }

    /**
     * @deprecated
     */
    public synchronized void setClient(QTDrawable paramQTDrawable, QDRect paramQDRect) throws QTException {
        doSetClient(paramQTDrawable, paramQDRect, getMinimumSize(), getMaximumSize(), true);
    }

    /**
     * @deprecated
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

    private void doSetClient(QTDrawable paramQTDrawable,
                             QDRect paramQDRect,
                             Dimension paramDimension1,
                             Dimension paramDimension2,
                             boolean paramBoolean) throws QTException {
        if (paramQTDrawable == this.client) {
            return;
        }
        if (this.client != null) {
            removeClient();
        }
        if (paramQTDrawable != null) {
            this.client = paramQTDrawable;

            if (paramBoolean) {
                setMinimumSize(paramDimension1);
                setMaximumSize(paramDimension2);
                setPreferredSize(new Dimension(paramQDRect.getWidth(), paramQDRect.getHeight()));
                this.initialWidth = paramQDRect.getWidth();
                this.initialHeight = paramQDRect.getHeight();
                this.currentX = paramQDRect.getX();
                this.currentY = paramQDRect.getY();
                this.currentWidth = this.initialWidth;
                this.currentHeight = this.initialHeight;

                doParentLayout();
            }

            if (((paramQTDrawable instanceof SWCompositor)) || ((paramQTDrawable instanceof quicktime.app.anim.Compositor))) {
                SWCompositor localSWCompositor = (SWCompositor) paramQTDrawable;

                this.gwBounds = localSWCompositor.getDisplayBounds();
                this.gw = localSWCompositor.getGWorld();
                if (this.gw == null) {
                    this.gw = new quicktime.qd.QDGraphics(this.gwBounds);
                    localSWCompositor.setGWorld(this.gw);
                }

                if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
                    boolean bool = false;
                    this.image = java.awt.image.MacDirectColorModel
                            .GetWrappedGWorldImage(quicktime.QTObject.ID(this.gw),
                                                   32,
                                                   this.gwBounds.getWidth(),
                                                   this.gwBounds.getHeight(),
                                                   bool);
                } else {
                    this.pixels = new int[((RawEncodedImage) localSWCompositor.getImage()).getRowBytes() / 4
                                          * paramQDRect.getHeight()];
                    this.image = imageFromEncodedImage((RawEncodedImage) localSWCompositor.getImage(),
                                                       localSWCompositor.getDescription().getBounds());
                }
                prepareImage(this.image, this);
                localSWCompositor.addDrawingListener(this);
            } else {
                paramQTDrawable.redraw(null);
                this.ip = new QTImageProducer(this.client, new Dimension(paramQDRect.getWidth(), paramQDRect.getHeight()));
                this.image = createImage(this.ip);
                prepareImage(this.image, this);
            }
            if ((paramQTDrawable instanceof DrawingNotifier))
                ((DrawingNotifier) paramQTDrawable).addDrawingListener(this);
        }
        repaint();
    }

    public java.awt.Image createImage(java.awt.image.ImageProducer paramImageProducer) {
        if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
            boolean bool = false;

            QTImageProducer localQTImageProducer = (QTImageProducer) paramImageProducer;

            Dimension localDimension = localQTImageProducer.getSize();
            quicktime.qd.QDGraphics localQDGraphics = localQTImageProducer.getGWorld();
            this.gw = localQDGraphics;

            java.awt.image.BufferedImage localBufferedImage = (BufferedImage) java.awt.image.MacDirectColorModel
                    .GetWrappedGWorldImage(quicktime.QTObject.ID(localQDGraphics),
                                           32,
                                           localDimension.width,
                                           localDimension.height,
                                           bool);

            return localBufferedImage;
        }

        return super.createImage(paramImageProducer);
    }

    /**
     * @deprecated
     */
    public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        if (this.debug) {
            System.out.println("setBounds( " + paramInt1 + ", " + paramInt2 + ", " + paramInt3 + ", " + paramInt4 + ")");
        }
        Dimension localDimension = setCurrentWidthHeight(paramInt3, paramInt4);
        if (flashScaling) {
            setPreferredSize(localDimension);
            this.initialWidth = paramInt3;
            this.initialHeight = paramInt4;
            try {
                if (((this.client instanceof SWCompositor)) || ((this.client instanceof quicktime.app.anim.Compositor))) {
                    SWCompositor localSWCompositor = (SWCompositor) this.client;

                    this.gwBounds = new QDRect(getX(), getY(), paramInt3, paramInt4);
                    this.gw = new quicktime.qd.QDGraphics(this.gwBounds);
                    localSWCompositor.setGWorld(this.gw);

                    if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
                        boolean bool = false;
                        this.image = java.awt.image.MacDirectColorModel
                                .GetWrappedGWorldImage(quicktime.QTObject.ID(this.gw),
                                                       32,
                                                       this.gwBounds.getWidth(),
                                                       this.gwBounds.getHeight(),
                                                       bool);
                    } else {
                        this.pixels = new int[((RawEncodedImage) localSWCompositor.getImage()).getRowBytes() / 4
                                              * this.gwBounds.getHeight()];
                        this.image = imageFromEncodedImage((RawEncodedImage) localSWCompositor.getImage(), this.gwBounds);
                    }

                    prepareImage(this.image, this);
                } else {
                    this.client.redraw(null);
                    this.ip = new QTImageProducer(this.client, localDimension);
                    this.image = createImage(this.ip);
                    prepareImage(this.image, this);
                }
            } catch (QTException localQTException) {
                localQTException.printStackTrace();
            }
            repaint();
        }

        int i = paramInt3 - this.currentWidth;
        if (i < 0) {
            i = 0;
        }
        int j = paramInt4 - this.currentHeight;
        if (j < 0)
            j = 0;
        this.currentX = (paramInt1 + (int) (getAlignmentX() * i));
        this.currentY = (paramInt2 + (int) (getAlignmentY() * j));

        if (this.debug) {
            System.out.println("setBounds(" + paramInt1 + ", " + paramInt2 + ", " + paramInt3 + ", " + paramInt4 + ")");
            System.out.println("Alignment x: " + getAlignmentX() + ", y: " + getAlignmentY());
            System.out.println("widthDiff: " + i);
            System.out.println("heightDiff: " + j);
            System.out.println("currentX:" + this.currentX);
            System.out.println("currentY:" + this.currentY);
        }
        super.reshape(this.currentX, this.currentY, localDimension.width, localDimension.height);
    }

    /**
     * @deprecated
     */
    public void setBounds(Rectangle paramRectangle) {
        if (this.debug)
            System.out.println("setBounds( " + paramRectangle + ")");
        setBounds(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
    }

    /**
     * @deprecated
     */
    public void setSize(Dimension paramDimension) {
        if (this.debug) {
            System.out.println("setSize( " + paramDimension + ")");
        }
        setSize(paramDimension.width, paramDimension.height);
    }

    /**
     * @deprecated
     */
    public void setSize(int paramInt1, int paramInt2) {
        if (this.debug)
            System.out.println("setSize( " + paramInt1 + ", " + paramInt2 + ")");
        setBounds(this.currentX, this.currentY, paramInt1, paramInt2);
    }

    /**
     * @deprecated
     */
    public final void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        if (this.debug)
            System.out.println("reshape( " + paramInt1 + ", " + paramInt2 + ", " + paramInt3 + ", " + paramInt4 + ")");
        setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private final void doParentLayout() {
        if (this.debug)
            System.out.println("doParentLayout()");
        java.awt.Container localContainer = getParent();
        if (localContainer != null) {
            invalidate();
            localContainer.validate();
            return;
        }
        super.reshape(this.currentX, this.currentY, getWidth(), getHeight());
    }

    private Dimension setCurrentWidthHeight(int width, int height) {
        if (this.debug)
            System.out.println("setCurrentWidthHeight( " + width + ", " + height + "), flag " + getResizeFlag());
        Dimension min = getMinimumSize();
        Dimension max = getMaximumSize();
        int w1 = min.width;
        int h1 = min.height;
        int w2 = max.width;
        int h2 = max.height;

        switch (getResizeFlag()) {
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

    private Dimension getAspectSize(int paramInt1, int paramInt2) {
        Dimension localDimension1 = getMinimumSize();
        Dimension localDimension2 = getMaximumSize();
        int i = localDimension1.width;
        int j = localDimension1.height;
        int k = localDimension2.width;
        int m = localDimension2.height;

        if (paramInt1 < i) {
            paramInt1 = i;
        } else if (paramInt1 > k)
            paramInt1 = k;
        if (paramInt2 < j) {
            paramInt2 = j;
        } else if (paramInt2 > m) {
            paramInt2 = m;
        }

        float f1 = paramInt1 / (this.initialWidth < 1 ? 1 : this.initialWidth);
        float f2 = paramInt2 / (this.initialHeight < 1 ? 1 : this.initialHeight);

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

    public void setAlignment(float paramFloat1, float paramFloat2) {
        setAlignmentX(paramFloat1);
        setAlignmentY(paramFloat2);
    }

    /**
     * @deprecated
     */
    public void setResizeFlag(int paramInt) {
        this.resizeFlag = paramInt;
        Rectangle localRectangle = getBounds();
        doParentLayout();
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
    public void drawingComplete(QTDrawable paramQTDrawable) {
        if (((paramQTDrawable instanceof SWCompositor)) || ((paramQTDrawable instanceof quicktime.app.anim.Compositor))) {
            try {
                if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
                    boolean bool = false;
                    this.image = java.awt.image.MacDirectColorModel
                            .GetWrappedGWorldImage(quicktime.QTObject.ID(this.gw),
                                                   32,
                                                   this.gwBounds.getWidth(),
                                                   this.gwBounds.getHeight(),
                                                   bool);
                } else {
                    this.image = imageFromEncodedImage((RawEncodedImage) ((SWCompositor) paramQTDrawable).getImage(),
                                                       ((SWCompositor) paramQTDrawable).getDescription().getBounds());
                }
            } catch (QTException localQTException1) {
                localQTException1.printStackTrace();
            }
            repaint();
        } else {
            if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
                repaint();
                return;
            }
            try {
                this.ip.updateConsumers(null);
            } catch (QTException localQTException2) {
                localQTException2.printStackTrace();
            }
        }
    }

    /**
     * @deprecated
     */
    public void addQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = java.awt.AWTEventMulticaster.add(this.clientListener, paramComponentListener);
    }

    /**
     * @deprecated
     */
    public void removeQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = java.awt.AWTEventMulticaster.remove(this.clientListener, paramComponentListener);
    }
}
