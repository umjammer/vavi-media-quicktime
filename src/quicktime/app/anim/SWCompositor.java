
package quicktime.app.anim;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.RecordMovie;
import quicktime.app.actions.Invalidator;
import quicktime.app.actions.Notifier;
import quicktime.app.actions.TickleList;
import quicktime.app.display.DrawingListener;
import quicktime.app.display.DrawingNotifier;
import quicktime.app.display.Layerable;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.image.Compositable;
import quicktime.app.image.DynamicImage;
import quicktime.app.image.Transformable;
import quicktime.app.spaces.Protocol;
import quicktime.app.spaces.SimpleSpace;
import quicktime.app.time.Timer;
import quicktime.qd.QDColor;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.anim.Sprite;
import quicktime.std.anim.SpriteWorld;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.EncodedImage;
import quicktime.util.RawEncodedImage;


/**
 * @deprecated
 */
public abstract class SWCompositor extends SimpleSpace implements QTDisplaySpace, DynamicImage, DrawingNotifier {
    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected SWCompositor(QDGraphics paramQDGraphics1,
            QDColor paramQDColor,
            QDGraphics paramQDGraphics2,
            int paramInt1,
            int paramInt2,
            Protocol paramProtocol) throws QTException {
        super(paramProtocol, paramInt1, paramInt2);
        this.mSpriteGWorld = paramQDGraphics1;
        this.mBGColor = paramQDColor;
        this.mBGPict = paramQDGraphics2;
        this.bnds = paramQDGraphics1.getPortRect();
    }

    public int hitTestFlag = 10;

    private QDPoint hitTestPoint = new QDPoint(0, 0);

    private QDGraphics mBGPict;

    private QDGraphics mSpriteGWorld;

    private Region mClip = null;

    private QDColor mBGColor;

    private QDGraphics mCurrentGrafPort = QDGraphics.scratch;

    private QDRect bnds;

    private GraphicsMode gMode;

    private Matrix savedMatrix = new Matrix();

    private ASpriteWorld mSpriteWorld = null;

    /**
     * @deprecated
     */
    public int taskFlag = 0;

    private int lastDrawRequiresDrawing = 0;

    private TickleList invalList;

    private RecordMovie rm;

    private boolean recordMovie = false;

    TwoDSprite spritePresenter;

    private long startTime = 0L;

    private int frameCount = 0;

    private SWCompositor parent;

    private boolean autoRedraw = true;

    private Vector listenerList;

    final class ASpriteWorld extends SpriteWorld {
        SWCompositor theCompositor;

        ASpriteWorld(QDGraphics paramQDGraphics1,
                QDGraphics paramQDGraphics2,
                QDColor paramQDColor,
                QDGraphics paramQDGraphics3,
                SWCompositor paramSWCompositor) throws QTException {
            super(paramQDGraphics1, paramQDGraphics2, paramQDColor, paramQDGraphics3);
            this.theCompositor = paramSWCompositor;
        }

        void _idle(int paramInt) throws QTException {
            SWCompositor.this.lastDrawRequiresDrawing = idle(paramInt);

            if (SWCompositor.this.recordMovie) {
                SWCompositor.this.recordMovie = SWCompositor.this.rm.frameReady();
                if (!SWCompositor.this.recordMovie)
                    SWCompositor.this.rm = null;
            }
            if (SWCompositor.this.listenerList != null) {
                synchronized (SWCompositor.this) {
                    for (int i = 0; i < SWCompositor.this.listenerList.size(); i++)
                        ((DrawingListener) SWCompositor.this.listenerList.elementAt(i)).drawingComplete(this.theCompositor);
                }
            }
        }

        void _cleanup() {
            try {
                finalize();
            } catch (Throwable localThrowable) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(localThrowable.getMessage()), this, "finalize");
            }
        }
    }

    SpriteWorld getSpriteWorld() {
        return this.mSpriteWorld;
    }

    /**
     * @deprecated
     */
    public void resetStatistics() {
        this.startTime = System.currentTimeMillis();
        this.frameCount = 0;
    }

    /**
     * @deprecated
     */
    public float getStatistics() {
        if ((getTimer().getRate() == 0.0F) || (this.mSpriteWorld == null))
            return 0.0F;
        if (this.frameCount == 0)
            return 0.0F;
        return 1000.0F / ((System.currentTimeMillis() - this.startTime) / this.frameCount);
    }

    /**
     * @deprecated
     */
    public Enumeration getInvalidatorList() {
        return this.invalList == null ? null : this.invalList.members();
    }

    /**
     * @deprecated
     */
    public synchronized Invalidator getMemberInvalidator(Object paramObject) {
        if (!hasMember(paramObject))
            return null;
        if (this.invalList == null)
            return null;
        Enumeration localEnumeration = this.invalList.members();
        while (localEnumeration.hasMoreElements()) {
            Invalidator localInvalidator = (Invalidator) localEnumeration.nextElement();
            if (localInvalidator.getTarget().equals(paramObject))
                return localInvalidator;
        }
        return null;
    }

    /**
     * @deprecated
     */
    public void setRecordMovie(RecordMovie paramRecordMovie) throws QTException {
        this.rm = paramRecordMovie;
        paramRecordMovie.addedToSource(this.mSpriteGWorld);
        this.recordMovie = true;
    }

    /**
     * @deprecated
     */
    public boolean hasRecordMovie() {
        return this.recordMovie;
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() throws QTException {
        return new ImageDescription(this.mSpriteGWorld.getPixMap());
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage() throws QTException, IndexOutOfBoundsException {
        return RawEncodedImage.fromPixMap(this.mSpriteGWorld.getPixMap());
    }

    /**
     * @deprecated
     */
    public SWCompositor getParent() {
        return this.parent;
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws StdQTException {
        return this.mSpriteWorld != null ? this.mSpriteWorld.getMatrix() : this.savedMatrix;
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        if (this.mSpriteWorld != null) {
            this.mSpriteWorld.setMatrix(paramMatrix);
        }
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        QDRect localQDRect = this.mSpriteGWorld.getPortRect();
        return new Dimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.mSpriteGWorld.getPortRect();
        return new QDDimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * @deprecated
     */
    public QDGraphics getGWorld() {
        return this.mCurrentGrafPort;
    }

    /**
     * @deprecated
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        if (paramQDGraphics == null)
            paramQDGraphics = QDGraphics.scratch;
        if (paramQDGraphics.equals(this.mCurrentGrafPort)) {
            return;
        }
        getTimer().setActive(false);

        if ((this.mSpriteWorld == null) && (!QDGraphics.scratch.equals(paramQDGraphics))) {
            this.mSpriteWorld = new ASpriteWorld(paramQDGraphics, this.mSpriteGWorld, this.mBGColor, this.mBGPict, this);
            if (this.gMode != null)
                this.mSpriteWorld.setGraphicsMode(this.gMode);
            setMatrix(this.savedMatrix);
            createMembers();

        } else if (QDGraphics.scratch.equals(paramQDGraphics)) {
            saveMembers();
            this.savedMatrix = this.mSpriteWorld.getMatrix();
            this.mSpriteWorld._cleanup();
            this.mSpriteWorld = null;
        } else {
            saveMembers();
            this.savedMatrix = this.mSpriteWorld.getMatrix();
            this.mSpriteWorld._cleanup();
            this.mSpriteWorld = null;
            this.mSpriteWorld = new ASpriteWorld(paramQDGraphics, this.mSpriteGWorld, this.mBGColor, this.mBGPict, this);
            if (this.gMode != null)
                this.mSpriteWorld.setGraphicsMode(this.gMode);
            setMatrix(this.savedMatrix);
            createMembers();
        }

        this.mCurrentGrafPort = paramQDGraphics;
        getTimer().setActive(this.mSpriteWorld != null);
    }

    /**
     * @deprecated
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        if (this.mSpriteWorld != null) {
            this.mSpriteWorld.setBounds(paramQDRect);
        } else {
            this.savedMatrix.setTx(paramQDRect.getX());
            this.savedMatrix.setTy(paramQDRect.getY());
            QDRect localQDRect = this.mSpriteGWorld.getPortRect();
            this.savedMatrix.setSx(paramQDRect.getWidth() / localQDRect.getWidth());
            this.savedMatrix.setSy(paramQDRect.getHeight() / localQDRect.getHeight());
        }
        this.bnds = paramQDRect;
    }

    /**
     * @deprecated
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * @deprecated
     */
    public QDRect getDisplayBounds() throws QTException {
        if (this.mSpriteWorld != null)
            return this.mSpriteWorld.getBounds();
        return this.bnds;
    }

    /**
     * @deprecated
     */
    public final void redraw(Region paramRegion) throws QTException {
        if (this.mSpriteWorld != null) {
            QDRect localQDRect1 = null;
            if (paramRegion != null) {
                QDRect localQDRect2 = paramRegion.getRgnBBox();
                localQDRect1 = localQDRect2.union(getDisplayBounds());
                if (localQDRect2.equals(localQDRect1)) {
                    localQDRect1 = null;
                } else
                    localQDRect1 = localQDRect2.intersection(getDisplayBounds());
            }
            this.mSpriteWorld.invalidate(localQDRect1);
            idle();
        }
    }

    /**
     * @deprecated
     */
    public boolean isAutoRedrawing() {
        return this.autoRedraw;
    }

    /**
     * @deprecated
     */
    public void setAutoRedrawing(boolean paramBoolean) {
        this.autoRedraw = paramBoolean;
    }

    /**
     * @deprecated
     */
    protected void idle() throws QTException {
        this.mSpriteWorld._idle(this.taskFlag);
        if ((this.spritePresenter != null) && (this.lastDrawRequiresDrawing != 0)) {
            this.lastDrawRequiresDrawing = 0;
            this.spritePresenter.invalidate();
        }
    }

    /**
     * @deprecated
     */
    public Object hitTest(int paramInt1, int paramInt2) throws QTException {
        if (this.mSpriteWorld != null) {
            this.hitTestPoint.setX(paramInt1);
            this.hitTestPoint.setY(paramInt2);

            if ((getClip() != null) && (!getClip().pointIn(this.hitTestPoint))) {
                return null;
            }

            Sprite localSprite = this.mSpriteWorld.hitTest(this.hitTestFlag, this.hitTestPoint);
            if (localSprite == null)
                return null;
            Enumeration localEnumeration = sprites();
            while (localEnumeration.hasMoreElements()) {
                TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
                if (localSprite.equals(localTwoDSprite.getSprite())) {
                    return localTwoDSprite;
                }
            }
        }
        return null;
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws QTException {
        if (this.mSpriteWorld != null) {
            this.mSpriteWorld.setClip(paramRegion);
        }
        this.mClip = paramRegion;
    }

    /**
     * @deprecated
     */
    public Region getClip() {
        return this.mClip;
    }

    public boolean tickle(float paramFloat, int paramInt) throws QTException {
        Timer localTimer = getTimer();
        if ((localTimer.isActive()) && (this.mSpriteWorld != null)) {
            super.tickle(paramFloat, paramInt);
            if (this.invalList != null)
                this.invalList.tickle(paramFloat, paramInt);
            idle();
            this.frameCount += 1;
            return this.mSpriteWorld != null;
        }
        return false;
    }

    /**
     * @deprecated
     */
    public synchronized void removedFromCompositor(SWCompositor paramSWCompositor) throws QTException {
        this.spritePresenter.spriteIsCompositor = null;
        this.spritePresenter = null;
        this.parent = null;
        setGWorld(QDGraphics.scratch);
        this.taskFlag = 0;
    }

    /**
     * @deprecated
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) throws QTException {
        if (this.spritePresenter != null)
            removedFromCompositor(null);
        setGWorld(QDGraphics.validScratch);
        this.taskFlag = 1;
        this.spritePresenter = paramTwoDSprite;
        this.spritePresenter.spriteIsCompositor = this;
        this.parent = paramSWCompositor;
        return null;
    }

    /**
     * @deprecated
     */
    public GraphicsMode getGraphicsMode() {
        return this.gMode;
    }

    /**
     * @deprecated
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        if (this.mSpriteWorld != null)
            this.mSpriteWorld.setGraphicsMode(paramGraphicsMode);
        this.gMode = paramGraphicsMode;
    }

    /**
     * @deprecated
     */
    public Compositable getCompositable(Object paramObject) {
        return getMemberSprite(paramObject);
    }

    /**
     * @deprecated
     */
    public Layerable getLayerable(Object paramObject) {
        return getMemberSprite(paramObject);
    }

    /**
     * @deprecated
     */
    public Transformable getTransformable(Object paramObject) {
        return getMemberSprite(paramObject);
    }

    /**
     * @deprecated
     */
    protected synchronized void postProcessAdd(Object paramObject, TwoDSprite paramTwoDSprite) throws QTException {
        doDynamicImageAdd(paramObject, paramTwoDSprite);
        paramTwoDSprite.setParent(this);
        if ((paramObject instanceof Notifier)) {
            Notifier localNotifier = (Notifier) paramObject;
            localNotifier.addNotifyListener(paramTwoDSprite);
        }
        memberAdded(paramObject);
        if (this.mSpriteWorld != null) {
            paramTwoDSprite.createSprite(this.mSpriteWorld);
            if (this.autoRedraw) {
                idle();
            }
        }
    }

    /**
     * @deprecated
     */
    protected synchronized void postProcessRemove(Object paramObject, TwoDSprite paramTwoDSprite) throws QTException {
        doDynamicImageRemove(paramObject, paramTwoDSprite);
        memberRemoved(paramObject);
        paramTwoDSprite.remove();
        paramTwoDSprite.setParent(null);
        if ((this.autoRedraw) && (this.mSpriteWorld != null)) {
            idle();
        }
    }

    /**
     * @deprecated
     */
    protected void saveMembers() throws QTException {
        boolean bool = this.autoRedraw;
        try {
            this.autoRedraw = false;
            for (Enumeration localEnumeration = sprites(); localEnumeration.hasMoreElements();) {
                TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
                doDynamicImageRemove(getSpriteMember(localTwoDSprite), localTwoDSprite);
                localTwoDSprite.removeNotify();
            }
        } finally {
            Enumeration localEnumeration;
            this.autoRedraw = bool;
        }
    }

    /**
     * @deprecated
     */
    protected void createMembers() throws QTException {
        boolean bool = this.autoRedraw;
        try {
            this.autoRedraw = false;
            for (Enumeration localEnumeration = sprites(); localEnumeration.hasMoreElements();) {
                TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
                localTwoDSprite.createSprite(this.mSpriteWorld);
                doDynamicImageAdd(getSpriteMember(localTwoDSprite), localTwoDSprite);
            }
        } finally {
            Enumeration localEnumeration;
            this.autoRedraw = bool;
        }
    }

    /**
     * @deprecated
     */
    public String toString() {
        try {
            return getClass().getName() + "[sw=" + this.mSpriteWorld + ",timer=" + getTimer() + ",grafPort=" + getGWorld()
                   + ",bounds=" + getDisplayBounds() + "]";
        } catch (QTException localQTException) {
        }
        return getClass().getName();
    }

    private void doDynamicImageAdd(Object paramObject, TwoDSprite paramTwoDSprite) throws QTException {
        if ((this.mSpriteWorld != null) && ((paramObject instanceof DynamicImage))) {
            Invalidator localInvalidator = ((DynamicImage) paramObject).addedToCompositor(this, paramTwoDSprite);
            if (localInvalidator != null) {
                if (this.invalList == null)
                    this.invalList = new TickleList();
                this.invalList.addMember(localInvalidator);
            }
        }
    }

    private void doDynamicImageRemove(Object paramObject, TwoDSprite paramTwoDSprite) throws QTException {
        if ((this.mSpriteWorld != null) && ((paramObject instanceof DynamicImage))) {
            ((DynamicImage) paramObject).removedFromCompositor(this);
            if (this.invalList != null) {
                Enumeration localEnumeration = this.invalList.members();
                while (localEnumeration.hasMoreElements()) {
                    Invalidator localInvalidator = (Invalidator) localEnumeration.nextElement();
                    if (localInvalidator.getTarget().equals(paramTwoDSprite)) {
                        this.invalList.removeMember(localInvalidator);
                    }
                }

                if (this.invalList.isEmpty()) {
                    this.invalList = null;
                }
            }
        }
    }

    /**
     * @deprecated
     */
    public synchronized void addDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList == null) {
            this.listenerList = new Vector();
        }
        if (this.listenerList.contains(paramDrawingListener)) {
            return;
        }
        this.listenerList.addElement(paramDrawingListener);
    }

    /**
     * @deprecated
     */
    public synchronized void removeDrawingListener(DrawingListener paramDrawingListener) {
        this.listenerList.removeElement(paramDrawingListener);

        if (this.listenerList.isEmpty()) {
            this.listenerList = null;
        }
    }

    /**
     * @deprecated
     */
    public abstract TwoDSprite getMemberSprite(Object paramObject);

    /**
     * @deprecated
     */
    public abstract Enumeration sprites();

    /**
     * @deprecated
     */
    public abstract Object getSpriteMember(TwoDSprite paramTwoDSprite);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/anim/
 * SWCompositor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
