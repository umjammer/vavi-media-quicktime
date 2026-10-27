package quicktime.app.image;

import java.awt.Dimension;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.DrawingListener;
import quicktime.app.display.DrawingNotifier;
import quicktime.app.display.QTDrawable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.ImageSequenceDataSource;
import quicktime.std.movies.Atom;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.AtomData;
import quicktime.util.EncodedImage;
import quicktime.util.EndianOrder;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;

/**
 * @deprecated
 * QTFilters are visual effects that are applied to a single source image.
 * They inherit much of the characteristics of the QTEffect class.
 */
public class QTFilter extends QTEffect {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a QTFilter which is a singleFrame filter and an initialSize of 0, 0.
     */
    public QTFilter() throws QTException {
        this(1, new QDDimension(0, 0));
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0</I>
     * Creates a QTFilter which is a singleFrame filter and specified initialSize.
     * @param d the initSize of the effect.
     */
    public QTFilter(Dimension paramDimension) throws QTException {
        this(1, paramDimension);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a QTFilter which is a singleFrame filter and specified initialSize.
     * @param d the initSize of the effect.
     */
    public QTFilter(QDDimension paramQDDimension) throws QTException {
        this(1, paramQDDimension);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a QTFilter of specified frame setting and an initialSize of 0, 0.
     */
    public QTFilter(int paramInt) throws QTException {
        this(paramInt, new QDDimension(0, 0));
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0</I>
     * Creates a QTFilter of specified frame setting and initSize.
     * @param d the initSize of the effect.
     */
    public QTFilter(int paramInt, Dimension paramDimension) throws QTException {
        this(paramInt, new QDDimension(paramDimension.width, paramDimension.height));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a QTFilter of specified frame setting and initSize.
     * @param d the initSize of the effect.
     */
    public QTFilter(int paramInt, QDDimension paramQDDimension) throws QTException {
        super(paramInt, paramQDDimension);
        if (paramInt == 2) {
        }
    }

    int srcA = QTUtils.toOSType("srcA");

    boolean inEffect = false;

    ImageContainer drawSourceListener;

    private static int nThreads;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method will take the incoming parameters and use this as the control parameters for the
     * effect. The container will be locked for the duration of the effect object's life or until
     * another effect is set.
     * @param effectParams the parameters that determine the effect that is run
     * @throws quicktime.QTException
     */
    @Override
    public void setEffect(AtomContainer effectParams) throws QTException {
        if (effectParams == null) {
            this.effectSamplePtr = null;
            if (this.effectSample != null)
                this.effectSample.unlock();
            this.effectSample = null;
            return;
        }
        this.effectSample = effectParams;
        Atom localAtom1 = Atom.kParentIsContainer;
        int i = this.effectSample.countChildrenOfType(localAtom1, 1936876320);
        for (int j = 0; j < i; j++) {
            Atom localObject = this.effectSample.findChildByIndex_Atom(localAtom1, 1936876320, 1);
            this.effectSample.removeAtom(localObject);
        }
        this.effectSample.insertChild(localAtom1, 1936876320, 1, 0, EndianOrder.flipNativeToBigEndian32(this.srcA));
        Atom localAtom2 = this.effectSample.findChildByID_Atom(localAtom1, 2003329396, 1);
        this.effectSample.lock();
        AtomData localObject = this.effectSample.getAtomData(localAtom2);
        this.effectDesc.setCType(EndianOrder.flipBigEndianToNative32(localObject.getInt(0)));
        this.effectSamplePtr = RawEncodedImage.fromQTPointer(this.effectSample.toQTPointer());
        setUpSequence();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * If the original size was not specified in the Constructor then the
     * original size of the current sourceImage is returned. If the original size
     * was specified then it is returned.
     * @return a QDDimension
     * @throws quicktime.QTException
     */
    @Override
    public QDDimension getOriginalSize() throws QTException {
        return super.getOriginalSize();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This sets the QT source of the QTFilter, which is the image that the current filter will be
     * applied to when doEffect is called.
     * @param image the image to use as the source of the filter
     * @throws quicktime.QTException
     */
    public void setSourceImage(ImageSpec paramImageSpec) throws QTException {
        if (paramImageSpec != null) {
            if (this.drawSourceListener != null)
                this.drawSourceListener.dispose();
            this.drawSourceListener = new ImageContainer(paramImageSpec);
            this.initSize = new QDDimension(this.drawSourceListener.imgDesc.getWidth(), this.drawSourceListener.imgDesc.getHeight());
        } else {
            this.drawSourceListener.dispose();
            this.drawSourceListener = null;
        }
        setUpSequence();
    }

    /**
     * @deprecated
     * Returns the currently set source image of the QTFilter * @deprecated since QTJava 6.1
     */
    public ImageSpec getSourceImage() {
        return this.drawSourceListener != null ? this.drawSourceListener.image : null;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by a number of other methods to create
     * the DSequence that is used to render the QTEffect.
     * When called the method will create a new DSequence if the required
     * state has been established to enable the creation of the DSequence.
     * @return true if a new DSequence was created, otherwise false.
     * @throws quicktime.QTException
     */
    @Override
    protected synchronized boolean setUpSequence() throws QTException {
        if (this.drawSourceListener == null) {
            return false;
        }
        if (super.setUpSequence()) {
            this.drawSourceListener.prepareSequence(this.srcA);
            return true;
        }
        return false;
    }

    /**
     * @deprecated
     */
    class ImageContainer extends Thread implements DrawingListener {

        ImageSpec image;

        EncodedImage encImage;

        ImageDescription imgDesc;

        ImageSequenceDataSource imgDataSeq;

        ImageContainer(ImageSpec paramImageSpec) throws QTException {
            super();
            this.image = paramImageSpec;
            this.imgDesc = this.image.getDescription();
            this.encImage = this.image.getImage();
            if ((this.image instanceof DrawingNotifier)) {
                ((DrawingNotifier) this.image).addDrawingListener(this);
                if ((this.image instanceof QTDrawable)) {
                    ((QTDrawable) this.image).setGWorld(QDGraphics.validScratch);
                }
                start();
            }
        }

        void prepareSequence(int paramInt) throws QTException {
            if (this.imgDataSeq != null) {
                this.imgDataSeq.dispose();
            }
            this.imgDataSeq = new ImageSequenceDataSource(QTFilter.this.effectSequence, paramInt, 1, this.imgDesc);
            this.imgDataSeq.setSourceData(this.encImage);
        }

        private boolean needRedraw = false;
        private boolean running = true;

        synchronized void requestRedraw() {
            needRedraw = true;
            notifyAll();
        }

        void dispose() throws QTException {
            running = false;
            synchronized (this) {
                notifyAll();
            }
            if (this.imgDataSeq != null) {
                this.imgDataSeq.dispose();
            }
            if ((this.image instanceof DrawingNotifier)) {
                ((DrawingNotifier) this.image).removeDrawingListener(this);
                if ((this.image instanceof QTDrawable)) {
                    ((QTDrawable) this.image).setGWorld(QDGraphics.scratch);
                }
            }
        }

        /**
         * @deprecated
         */
        @Override
        public void drawingComplete(QTDrawable drawable) {
            if (this.imgDataSeq != null) {
                try {
                    this.imgDataSeq.setSourceData(this.encImage);
                    this.imgDataSeq.changedSourceData();
                } catch (QTException localQTException) {
                    localQTException.printStackTrace();
                }
                if ((!QTFilter.this.inEffect) && (this == QTFilter.this.drawSourceListener)) {
                    requestRedraw();
                }
            }
        }

        /**
         * @deprecated
         */
        @Override
        public void run() {
            while (running) {
                synchronized (this) {
                    while (!needRedraw && running) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            if (!running) return;
                        }
                    }
                    needRedraw = false;
                }
                if (!running) break;
                try {
                    QTFilter.this.redraw(null);
                } catch (QTException localQTException) {
                    localQTException.printStackTrace();
                }
            }
        }
    }
}
