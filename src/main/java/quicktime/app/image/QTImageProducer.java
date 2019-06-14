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
 * Deprecated.
 * The QTImageProducer implements the java.awt.image.ImageProducer interface for a
 * single source QuickTime object. The QuickTime source can be a single frame (ie. still
 * image) or a mutliple frame source such as a movie, sprite world animation, effect, etc.
 * The QTImageProducer can produce images for multiple ImageConsumers.
 *
 * The isRedrawing() method returns true if the source QT object is an object that
 * requires redrawing. In this case the ImageObserver
 * of this Producer should explicitly call the redraw() method and repaint the
 * resultant Image to see any changes in the source of the pixel data. For instance
 * a typical movie will be a sequence of images over time, and for consequent frames
 * to be seen the producer must be redraw. The act of redrawing the producer will also
 * result in the registered consumers being notified and given the changed pixels.
 * Redrawing can also be optimised to only redraw those pixels within a specified region.
 *
 * When the source is originally added to the Producer it is drawn. If the source is not
 * or does not require redrawing then consequent calls
 * to redraw will have no effect as the source pixels have not changed. If
 * the redrawing characteristics of the QTDrawable source change then
 * you must call setRedrawing(true) to tell the producer that it should redraw the source's
 * pixel data before the redraw method is called. By default an QTImageProduce is seen as
 * potentially presenting dynamic or changing image data.
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
        return ((paramQTDrawable instanceof Space)) || ((paramQTDrawable instanceof Timeable)) || (((paramQTDrawable instanceof Redrawable)) && (!((Redrawable) paramQTDrawable).isSingleFrame()));
    }

    private Dimension size;

    private int rowInts;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates an ImageProducer from the specified qtSource. The initialSize of the
     * qtSource should be specified to get the best pixel resolution of the source
     * material.
     * @param qtSource the source of pixel data for the ImageProducer.
     * @param initSize the intial size of the source data
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

    /**
     * Deprecated.
     */
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the Redrawable only has a single frame to render and thus
     * will never need to re-acquire or redraw its image. Certain Redrawable objects
     * are able to optimise their resources knowing this constraint.
     * @return a boolean
     */
    public boolean isSingleFrame() {
        return this.singleFrame;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is used to register an ImageConsumer with the
     * ImageProducer for access to the image data during a later
     * reconstruction of the Image.  The ImageProducer may, at its
     * discretion, start delivering the image data to the consumer
     * using the ImageConsumer interface immediately, or when the
     * next available image reconstruction is triggered by a call
     * to the startProduction method.
     * @see {@link quicktime.app.image.QTImageProducer#startProduction(java.awt.image.ImageConsumer)}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method determines if a given ImageConsumer object
     * is currently registered with this ImageProducer as one
     * of its consumers.
     */
    public synchronized boolean isConsumer(ImageConsumer paramImageConsumer) {
        return this.consumers.contains(paramImageConsumer);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method removes the given ImageConsumer object
     * from the list of consumers currently registered to
     * receive image data.  It is not considered an error
     * to remove a consumer that is not currently registered.
     * The ImageProducer should stop sending data to this
     * consumer as soon as is feasible.
     */
    public synchronized void removeConsumer(ImageConsumer paramImageConsumer) {
        this.consumers.removeElement(paramImageConsumer);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method both registers the given ImageConsumer object
     * as a consumer and starts an immediate reconstruction of
     * the image data which will then be delivered to this
     * consumer and any other consumer which may have already
     * been registered with the producer.  This method differs
     * from the addConsumer method in that a reproduction of
     * the image data should be triggered as soon as possible.
     * @see {@link quicktime.app.image.QTImageProducer#addConsumer(java.awt.image.ImageConsumer)}
     */
    public void startProduction(ImageConsumer paramImageConsumer) {
        synchronized (QTNative.globalsLock) {
            synchronized (this) {
                try {
                    addConsumer(paramImageConsumer);
                    if (isConsumer(paramImageConsumer)) {
                        paramImageConsumer.setPixels(0, 0, this.size.width, this.size.height, this.cm, this.pixels, 0, this.rowInts);
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called to redraw the QuickTime image source and
     * update any of the current ImageConsumers with the new pixels that have been generated.
     * This update will only be done if the redrawing flag is true.
     * @param invalidRgn if null the whole image is redrawn and all of the pixels
     *  are grabbed and sent to the consumers. If not null then the qtSource
     *  invalidates the region specified, which can result in a faster draw. Also only those
     *  pixels that are within the bounds of the region are sent to the consumers.
     * @param {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method can be called by an application to have the QTImageProducer to update the pixel
     * data for each of the registered ImageConsumers when the source QTDrawable object has drawn.
     * Thus an application can either explicitly draw the QTDrawable source itself then call this method
     * or can receive notification by the QTDrawable source when it has drawn, and use this method to
     * update the registered ImageConsumers.
     * @param invalidRgn enter null to copy all pixels, or specify a region that specifies the changed pixel data,
     *  which can econimize the copying of the pixel data to only those areas that have changed.
     * @param {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If true then the redraw method of the object will fetch new image data
     * from its image source.
     * @param redrawFlag boolean determining whether the current image is redrawn (false)
     *  of fresh or updated image data is first retrieved (true).
     */
    public synchronized void setRedrawing(boolean paramBoolean) {
        this.doesRedraw = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the image data may be altered or different upon different calls
     * of the redraw method of the QTDrawable interface
     */
    public boolean isRedrawing() {
        return this.doesRedraw;
    }

    /**
     * Deprecated.
     * This returns the rendered size of the qtSource pixel data as specified in the constructor. * @deprecated since QTJava 6.1
     */
    public Dimension getSize() {
        return new Dimension(this.size);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is used by an ImageConsumer to request that
     * the ImageProducer attempt to resend the image data one
     * more time in TOPDOWNLEFTRIGHT order so that higher
     * quality conversion algorithms which depend on receiving
     * pixels in order can be used to produce a better output
     * version of the image.  The ImageProducer is free to
     * ignore this call if it cannot resend the data in that
     * order.  If the data can be resent, then the ImageProducer
     * should respond by executing the following minimum set of
     * ImageConsumer method calls:
     *
     * ic.setHints(TOPDOWNLEFTRIGHT | < otherhints >);
     * ic.setPixels(...);	// As many times as needed
     * ic.imageComplete();
     *
     * @see ImageConsumer.setHints(int)
     */
    public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /QTImageProducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
