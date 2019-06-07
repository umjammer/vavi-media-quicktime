package quicktime.app.image;

import java.awt.Dimension;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.QDDimension;
import quicktime.std.movies.Atom;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.AtomData;
import quicktime.util.EndianOrder;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;

/**
 * Deprecated.
 * QTTransistions are visual effects that are applied to a two images, transitioning from
 * the source image to the destination image. Upon completion of the transition the source and
 * destination images are swapped, so you can also transition back to the original source image
 * or set a new desintation image.
 *
 * The QTTransition can run in two modes - in time mode the doTransition method will run the effect in
 * as close as time as possible to the time specified. In frames mode the effect will be run
 * for the number of frames that are specified.
 *
 * The doTransition method is provided as a means of running the transition according to the
 * time or frames setting. The transition will run as effectively as possible from start to
 * finish - it is a call that blocks the thread of execution that made this call whilst the
 * transition is running.
 *
 * If a transition should be run with more application control then the application can control
 * the setting of the current frame of the transition and call redraw after setting each frame.
 * The redraw method will redraw the current frame of the transition.
 */
public class QTTransition extends QTFilter {

    QTFilter.ImageContainer drawDestListener;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a QTTransition with default settings of a time-specified duration of 2000 msecs,
     * an effect fps of 20, a redraw setting as kMultiFrame and an initialSize of 0, 0.
     */
    public QTTransition() throws QTException {
        this(new QDDimension(0, 0));
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * Creates a QTTransition with default settings of a time-specified duration of 2000 msecs,
     * an effect fps of 20, a redraw setting as kMultiFrame and the specified initialSize.
     */
    public QTTransition(Dimension paramDimension) throws QTException {
        this(new QDDimension(paramDimension.width, paramDimension.height));
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a QTTransition with default settings of a time-specified duration of 2000 msecs,
     * an effect fps of 20, a redraw setting as kMultiFrame and the specified initialSize.
     */
    public QTTransition(QDDimension paramQDDimension) throws QTException {
        super(2, paramQDDimension);
        setFramesPerSecond(20);
        this.currentFrame = 0;
    }

    private int srcB = QTUtils.toOSType("srcB");

    private int mEffectDuration = 2000;

    private boolean mDoTime = true;

    private int fps;

    private boolean doProfile = false;

    private long profileTime = 0L;

    private int framesRendered;

    QTEffectPresenter thePresenter;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will take the incoming parameters and use this as the control parameters for the
     * effect. The container will be locked for the duration of the effect object's life or until
     * another effect is set.
     * @param effectParams the parameters that determine the effect that is run
     * @param {@link quicktime.QTException}
     */
    public void setEffect(AtomContainer paramAtomContainer) throws QTException {
        if (paramAtomContainer == null) {
            this.effectSamplePtr = null;
            if (this.effectSample != null)
                this.effectSample.unlock();
            this.effectSample = null;
            return;
        }
        this.effectSample = paramAtomContainer;
        Atom localAtom1 = Atom.kParentIsContainer;
        int i = this.effectSample.countChildrenOfType(localAtom1, 1936876320);
        for (int j = 0; j < i; j++) {
            Object localObject = this.effectSample.findChildByIndex_Atom(localAtom1, 1936876320, 1);
            this.effectSample.removeAtom((Atom) localObject);
        }
        this.effectSample.insertChild(localAtom1, 1936876320, 1, 0, EndianOrder.flipNativeToBigEndian32(this.srcA));
        this.effectSample.insertChild(localAtom1, 1936876320, 2, 0, EndianOrder.flipNativeToBigEndian32(this.srcB));
        Atom localAtom2 = this.effectSample.findChildByID_Atom(localAtom1, 2003329396, 1);
        this.effectSample.lock();
        Object localObject = this.effectSample.getAtomData(localAtom2);
        this.effectDesc.setCType(EndianOrder.flipBigEndianToNative32(((AtomData) localObject).getInt(0)));
        this.effectSamplePtr = RawEncodedImage.fromQTPointer(this.effectSample.toQTPointer());
        setUpSequence();
    }

    /**
     * Deprecated.
     * Sets the destination image of the Transition - the image that the transition will transition to. * @deprecated since QTJava 6.1
     */
    public void setDestinationImage(ImageSpec paramImageSpec) throws QTException {
        if (paramImageSpec != null) {
            if (this.drawDestListener != null)
                this.drawDestListener.dispose();
            this.drawDestListener = new QTFilter.ImageContainer(paramImageSpec);
        } else {
            this.drawDestListener.dispose();
            this.drawDestListener = null;
        }
        setUpSequence();
    }

    /**
     * Deprecated.
     * Returns the current destination image * @deprecated since QTJava 6.1
     */
    public ImageSpec getDestinationImage() {
        return this.drawDestListener.image;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by a number of other methods to create
     * the DSequence that is used to render the QTEffect.
     * When called the method will create a new DSequence if the required
     * state has been established to enable the creation of the DSequence.
     * @return true if a new DSequence was created, otherwise false.
     * @return {@link quicktime.QTException}
     */
    protected synchronized boolean setUpSequence() throws QTException {
        if ((super.setUpSequence()) && (this.drawDestListener != null)) {
            this.drawDestListener.prepareSequence(this.srcB);
            return true;
        }
        return false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will run the currently selected effect on the visual rectangle that is defined by the display
     * bounds of this object.
     * After the effect is run the destination image of the transition is now the visible image in the effect's display space
     */
    public synchronized void doTransition() throws QTException {
        if (this.effectSequence == null) {
            return;
        }
        _doEffect();
        swapImages();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This swaps the source and destination images.
     */
    public void swapImages() throws QTException {
        QTFilter.ImageContainer localImageContainer = this.drawSourceListener;
        this.drawSourceListener = this.drawDestListener;
        this.drawDestListener = localImageContainer;
        this.drawSourceListener.prepareSequence(this.srcA);
        this.drawDestListener.prepareSequence(this.srcB);
        redraw(null);
    }

    /**
     * Deprecated.
     * Sets the maximum number of frames that an effect can run for.* @deprecated since QTJava 6.1
     */
    public synchronized void setFrames(int paramInt) throws QTException {
        this.mNumberOfFrames = (paramInt < 1 ? 1 : paramInt);
        this.mEffectDuration = ((int) (this.mNumberOfFrames / getFramesPerSecond() * 1000.0F + 0.5D));
        if (this.mEffectDuration < 1)
            this.mEffectDuration = 1;
        this.ft.setScale(this.mNumberOfFrames);
        this.ft.setDuration(this.mNumberOfFrames);
        this.ft.setVirtualDuration(this.mNumberOfFrames);
        if (this.currentFrame > this.mNumberOfFrames)
            this.currentFrame = this.mNumberOfFrames;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the maximum frames per second that the effect will attempt to render
     */
    public void setFramesPerSecond(int paramInt) throws QTException {
        this.mNumberOfFrames = ((int) (this.mEffectDuration / 1000.0F * paramInt + 0.5F));
        this.ft.setScale(this.mNumberOfFrames);
        this.ft.setDuration(this.mNumberOfFrames);
        this.ft.setVirtualDuration(this.mNumberOfFrames);
        if (this.currentFrame > this.mNumberOfFrames) {
            this.currentFrame = this.mNumberOfFrames;
        }
        this.ft.setFrameNumber(this.currentFrame);
        this.fps = paramInt;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current frames per second that the effect draws in
     * if the effect is rendered using the time constraint
     * @return the fps of the effect
     */
    public int getFramesPerSecond() {
        return this.fps;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This sets the current frame of the effect. A value of zero means to draw the source image of
     * the transition.
     * @param frame the frame to be rendered
     */
    public void setCurrentFrame(int paramInt) {
        this.currentFrame = paramInt;
        if (this.currentFrame > this.mNumberOfFrames)
            this.currentFrame = this.mNumberOfFrames;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the duration of the effect when it is run if the effect is run in time mode
     * @param time the time in milliseconds for the effect to run for in time mode
     * @param {@link quicktime.QTException}
     */
    public synchronized void setTime(int paramInt) throws QTException {
        this.mEffectDuration = (paramInt < 1 ? 1 : paramInt);
        this.mNumberOfFrames = ((int) (this.mEffectDuration / 1000.0D * getFramesPerSecond() + 0.5D));
        if (this.mNumberOfFrames < 1)
            this.mNumberOfFrames = 1;
        this.ft.setScale(this.mNumberOfFrames);
        this.ft.setDuration(this.mNumberOfFrames);
        this.ft.setVirtualDuration(this.mNumberOfFrames);
        if (this.currentFrame > this.mNumberOfFrames)
            this.currentFrame = this.mNumberOfFrames;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the time that an effect will run for in milliseconds
     * @return milliseconds
     */
    public int getTime() {
        return this.mEffectDuration;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If true then the effect will run in the time alloted.
     * If false then the effect will run for the specified frames.
     * @param flag a boolean
     */
    public synchronized void doTime(boolean paramBoolean) {
        this.mDoTime = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the effect is running for a specified duration or
     * false if the effect is running for a specified number of frames
     * @return true if the doEffect method runs the effect in time mode, false if in frame mode.
     */
    public boolean isTime() {
        return this.mDoTime;
    }

    /**
     * @deprecated
     */
    private final void _doEffect() throws QTException {
        if (this.effectSequence == null) {
            return;
        }
        int i = this.currentFrame;
        this.currentFrame = 1;
        Thread localThread = Thread.currentThread();
        int j = localThread.getPriority();
        localThread.setPriority(10);
        long l1 = System.currentTimeMillis();
        if (this.doProfile) {
            this.framesRendered = 0;
        }
        try {
            this.inEffect = true;
            if (isTime()) {
                float f1 = this.mEffectDuration / 1000.0F * getFramesPerSecond();
                float f2 = 0.0F;
                float f3 = 0.0F;
                drawAdvanceCurrentFrame();
                if (this.mNumberOfFrames > 1) {
                    for (; ; ) {
                        long l2 = System.currentTimeMillis();
                        int k = this.currentFrame;
                        drawAdvanceCurrentFrame();
                        if (this.currentFrame >= this.mNumberOfFrames) {
                            if (k == this.mNumberOfFrames)
                                break;
                            drawAdvanceCurrentFrame();
                            break;
                        }
                        long l3 = System.currentTimeMillis();
                        f2 = l3 - l2;
                        long l4 = this.mEffectDuration - (l3 - l1);
                        if (l4 <= 0L) {
                            this.currentFrame = this.mNumberOfFrames;
                            drawAdvanceCurrentFrame();
                            break;
                        }
                        f3 = l4 / (this.mNumberOfFrames - this.currentFrame);
                        float f4 = f3 - f2;
                        if (f4 < 0.0F) {
                            this.currentFrame += (int) (f2 / f3 + 0.5F);
                            if (this.currentFrame >= this.mNumberOfFrames) {
                                this.currentFrame = this.mNumberOfFrames;
                                drawAdvanceCurrentFrame();
                                break;
                            }
                        } else {
                            try {
                                Thread.sleep((int) f4);
                            } catch (InterruptedException localInterruptedException) {
                            }
                        }
                    }
                }
                if (this.doProfile) {
                    this.profileTime = (System.currentTimeMillis() - l1);
                }
            } else {
                this.currentFrame = 1;
                for (; ; ) {
                    drawAdvanceCurrentFrame();
                    if (this.currentFrame == 1)
                        break;
                    Thread.yield();
                }
            }
            this.inEffect = false;
        } finally {
            localThread.setPriority(j);
            this.currentFrame = i;
            this.inEffect = false;
        }
        if (this.doProfile) {
            this.profileTime = (System.currentTimeMillis() - l1);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set this to true if you wish to profile the actual running of the effect, or false to not collect.
     * Once profiling your application can determine the amount of time it took to render a transition
     * and how many frames were actually rendered. These values are generated and reset from each call
     * of the doTransition() method.
     * @param flag if true will profile
     */
    public void setProfiled(boolean paramBoolean) {
        this.doProfile = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current profiling status.
     * @return a boolean indicating the profiling status of the transition
     */
    public boolean isProfiled() {
        return this.doProfile;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If profiling this returns the number of frames that were rendered in the last time
     * that the doTransition() method was called. If profiling is disabled
     * this method returns 0.
     * @return an int
     */
    public int profileFramesRendered() {
        if (!this.doProfile)
            return 0;
        return this.framesRendered;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If profiling this returns the total time it took to run the doTransition method.
     * If profiling is disabled this method returns 0.
     * @return an int
     */
    public int profileDuration() {
        if (!this.doProfile)
            return 0;
        return (int) this.profileTime;
    }

    private void drawAdvanceCurrentFrame() throws QTException {
        this.tb.setValue(this.currentFrame, this.mNumberOfFrames);
        this.ft.setValue(this.currentFrame);
        this.effectSequence.decompressFrameWhen(this.effectSamplePtr, 0, this.ft);
        this.currentFrame += 1;
        this.framesRendered += 1;
        if (this.currentFrame > this.mNumberOfFrames)
            this.currentFrame = 1;
        if (this.thePresenter != null) {
            this.thePresenter.sprite.invalidate();
        }
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /QTTransition.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
