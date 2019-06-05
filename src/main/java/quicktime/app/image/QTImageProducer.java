
package quicktime.app.image;

import java.awt.Dimension;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.app.players.MoviePlayer;
import quicktime.app.players.QTPlayer;
import quicktime.app.spaces.Space;
import quicktime.app.time.Timeable;
import quicktime.jdirect.QTNative;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.util.RawEncodedImage;


/**
 * @deprecated
 */
public class QTImageProducer implements ImageProducer, Redrawable {
    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static long msecs = 0L;

    private static final boolean profile = false;

    private static boolean requiresRedrawing(QTDrawable paramQTDrawable) {
        return ((paramQTDrawable instanceof Space)) || ((paramQTDrawable instanceof Timeable))
               || (((paramQTDrawable instanceof Redrawable)) && (!((Redrawable) paramQTDrawable).isSingleFrame()));
    }

    private Dimension size;

    private int rowInts;

    /**
     * @deprecated
     */
    public QTImageProducer(QTDrawable paramQTDrawable, Dimension paramDimension) throws QTException {
        if ((paramQTDrawable instanceof QTPlayer))
            paramQTDrawable = new MoviePlayer(((QTPlayer) paramQTDrawable).getMovieController().getMovie());
        this.qtSource = paramQTDrawable;
        setRedrawing(requiresRedrawing(paramQTDrawable));
        this.singleFrame = (!isRedrawing());
        this.size = paramDimension;
        this.gw = new QDGraphics(new QDRect(this.size));
        paramQTDrawable.setDisplayBounds(new QDRect(this.size));
        paramQTDrawable.setGWorld(this.gw);
        this.pm = this.gw.getPixMap();
        this.rowInts = (this.pm.getRowBytes() / 4);
        this.pixels = new int[this.rowInts * this.size.height];
        paramQTDrawable.redraw(null);
        copyPixels();
        if (isSingleFrame()) {
            this.gw.disposeQTObject();
            this.gw = null;
            this.pm = null;
            this.qtSource = null;
        }
    }

    public QDGraphics getGWorld() {
        return this.gw;
    }

    private QDGraphics gw;

    private PixMap pm;

    private int[] pixels;

    private QTDrawable qtSource;

    private ColorModel cm = new DirectColorModel(32, 16711680, 65280, 255, 0);

    private Hashtable props = new Hashtable();

    private Vector consumers = new Vector();

    private boolean doesRedraw;

    private boolean singleFrame;

    /**
     * @deprecated
     */
    public boolean isSingleFrame() {
        return this.singleFrame;
    }

    /**
     * @deprecated
     */
    public synchronized void addConsumer(ImageConsumer paramImageConsumer) {
        if (isConsumer(paramImageConsumer)) {
            return;
        }

        try {
            this.consumers.addElement(paramImageConsumer);
            paramImageConsumer.setDimensions(this.size.width, this.size.height);

            if (isConsumer(paramImageConsumer)) {
                paramImageConsumer.setProperties(this.props);
            } else {
                return;
            }
            if (isConsumer(paramImageConsumer)) {
                paramImageConsumer.setColorModel(this.cm);
            } else {
                return;
            }
            if (isConsumer(paramImageConsumer)) {
                int i = 14;
                if (isSingleFrame())
                    i |= 0x10;
                paramImageConsumer.setHints(i);
            }
        } catch (Exception localException) {
            if (isConsumer(paramImageConsumer)) {
                paramImageConsumer.imageComplete(1);
            }
        }
    }

    /**
     * @deprecated
     */
    public synchronized boolean isConsumer(ImageConsumer paramImageConsumer) {
        return this.consumers.contains(paramImageConsumer);
    }

    /**
     * @deprecated
     */
    public synchronized void removeConsumer(ImageConsumer paramImageConsumer) {
        this.consumers.removeElement(paramImageConsumer);
    }

    /**
     * @deprecated
     */
    public void startProduction(ImageConsumer paramImageConsumer) {
        synchronized (QTNative.globalsLock) {
            synchronized (this) {
                try {
                    addConsumer(paramImageConsumer);
                    if (isConsumer(paramImageConsumer)) {
                        paramImageConsumer
                                .setPixels(0, 0, this.size.width, this.size.height, this.cm, this.pixels, 0, this.rowInts);
                    } else {
                        return;
                    }
                    if (isConsumer(paramImageConsumer)) {
                        if (isSingleFrame()) {
                            paramImageConsumer.imageComplete(3);
                            if (isConsumer(paramImageConsumer)) {
                                paramImageConsumer.imageComplete(1);
                                removeConsumer(paramImageConsumer);
                            }
                        } else {
                            paramImageConsumer.imageComplete(2);
                        }
                    }
                } catch (Exception localException) {
                    if (isConsumer(paramImageConsumer)) {
                        paramImageConsumer.imageComplete(1);
                    }
                }
            }
        }
    }

    private synchronized void copyPixels() throws QTException {
        RawEncodedImage localRawEncodedImage = this.pm.getPixelData();
        localRawEncodedImage.copyToArray(0, this.pixels, 0, this.pixels.length);
    }

    /**
     * @deprecated
     */
    public synchronized void redraw(Region paramRegion) throws QTException {
        if ((!this.consumers.isEmpty()) || (!isSingleFrame())) {

            this.qtSource.redraw(null);
        } else {
            return;
        }
        updateConsumers(paramRegion);
    }

    /**
     * @deprecated
     */
    public synchronized void updateConsumers(Region paramRegion) throws QTException {
        if (!isSingleFrame())
            copyPixels();
        int m = 0;
        int i;
        int j;
        int k;
        if (paramRegion != null) {
            Object localObject = paramRegion.getRgnBBox();
            i = ((QDRect) localObject).getX();
            j = ((QDRect) localObject).getY();
            k = ((QDRect) localObject).getWidth();
            m = ((QDRect) localObject).getHeight();
        } else {
            i = 0;
            j = 0;
            k = this.size.width;
            m = this.size.height;
        }

        Object localObject = this.consumers.elements();
        while (((Enumeration) localObject).hasMoreElements()) {
            ImageConsumer localImageConsumer = (ImageConsumer) ((Enumeration) localObject).nextElement();
            localImageConsumer.setPixels(i, j, k, m, this.cm, this.pixels, 0, this.rowInts);

            if (isConsumer(localImageConsumer)) {
                localImageConsumer.imageComplete(2);
            }
        }
    }

    /**
     * @deprecated
     */
    public synchronized void setRedrawing(boolean paramBoolean) {
        this.doesRedraw = paramBoolean;
    }

    /**
     * @deprecated
     */
    public boolean isRedrawing() {
        return this.doesRedraw;
    }

    public Dimension getSize() {
        return new Dimension(this.size);
    }

    /**
     * @deprecated
     */
    public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /QTImageProducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
