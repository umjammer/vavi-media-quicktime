
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
 * @deprecated
 */
public class QTTransition extends QTFilter {
    QTFilter.ImageContainer drawDestListener;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public QTTransition() throws QTException {
        this(new QDDimension(0, 0));
    }

    /**
     * @deprecated
     */
    public QTTransition(Dimension paramDimension) throws QTException {
        this(new QDDimension(paramDimension.width, paramDimension.height));
    }

    /**
     * @deprecated
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
     * @deprecated
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

    public ImageSpec getDestinationImage() {
        return this.drawDestListener.image;
    }

    /**
     * @deprecated
     */
    protected synchronized boolean setUpSequence() throws QTException {
        if ((super.setUpSequence()) && (this.drawDestListener != null)) {
            this.drawDestListener.prepareSequence(this.srcB);
            return true;
        }

        return false;
    }

    /**
     * @deprecated
     */
    public synchronized void doTransition() throws QTException {
        if (this.effectSequence == null) {
            return;
        }
        _doEffect();
        swapImages();
    }

    /**
     * @deprecated
     */
    public void swapImages() throws QTException {
        QTFilter.ImageContainer localImageContainer = this.drawSourceListener;
        this.drawSourceListener = this.drawDestListener;
        this.drawDestListener = localImageContainer;

        this.drawSourceListener.prepareSequence(this.srcA);
        this.drawDestListener.prepareSequence(this.srcB);

        redraw(null);
    }

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
     * @deprecated
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
     * @deprecated
     */
    public int getFramesPerSecond() {
        return this.fps;
    }

    /**
     * @deprecated
     */
    public void setCurrentFrame(int paramInt) {
        this.currentFrame = paramInt;
        if (this.currentFrame > this.mNumberOfFrames)
            this.currentFrame = this.mNumberOfFrames;
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public int getTime() {
        return this.mEffectDuration;
    }

    /**
     * @deprecated
     */
    public synchronized void doTime(boolean paramBoolean) {
        this.mDoTime = paramBoolean;
    }

    /**
     * @deprecated
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
                    for (;;) {
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
                for (;;) {
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
     * @deprecated
     */
    public void setProfiled(boolean paramBoolean) {
        this.doProfile = paramBoolean;
    }

    /**
     * @deprecated
     */
    public boolean isProfiled() {
        return this.doProfile;
    }

    /**
     * @deprecated
     */
    public int profileFramesRendered() {
        if (!this.doProfile)
            return 0;
        return this.framesRendered;
    }

    /**
     * @deprecated
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
