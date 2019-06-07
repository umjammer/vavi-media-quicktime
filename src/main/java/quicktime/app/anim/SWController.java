package quicktime.app.anim;

import java.awt.event.MouseEvent;
import java.util.Enumeration;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.MouseController;
import quicktime.app.actions.MouseResponder;
import quicktime.app.spaces.Space;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;

/**
 * Deprecated.
 * The SWController deals with SpriteWorldHitTest calls on the SpriteWorld that
 * is contained by the SWCompositor's sub classes.
 * By default it will do hit testing on the actual sprite image itself. However, an application can
 * set the hit-test flags to support any mode of hit testing appropriate.
 * @see {@link quicktime.app.event.QTMouseController}
 */
public class SWController extends MouseController {

    private SWCompositor sd;

    private boolean isActive;

    private int flags;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Construct a SWController object.
     * @param mr the Responder that will respond to mouse events when a sprite is hit.
     * @param wholeSpace if true all object that are in the space will be dragged. If false
     *  then you must explicitly add the object to the controller.
     */
    public SWController(MouseResponder paramMouseResponder, boolean paramBoolean) {
        this(paramMouseResponder, paramBoolean, 10);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Construct a SWController object.
     * @param mr the Responder that will respond to mouse events when a sprite is hit.
     * @param wholeSpace if true all object that are in the space will be dragged. If false
     *  then you must explicitly add the object to the controller.
     * @param flags that will control the hit test parameters of the SpriteWorld
     */
    public SWController(MouseResponder paramMouseResponder, boolean paramBoolean, int paramInt) {
        super(paramMouseResponder, paramBoolean);
        this.flags = paramInt;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current Space that the SWController is attached to.
     * This space will be an instance of the SWCompositor class.
     * @return the Space the controller is attached to.
     */
    public Space getSpace() {
        return this.sd;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the target Space of the MouseController. This target must be
     * a kind of SWCompositor class.
     * @param target the potential target space for the SWController.
     */
    protected void setTargetSpace(Space paramSpace) {
        this.sd = ((SWCompositor) paramSpace);
    }

    /**
     * Deprecated.
     * Returns the flags that control the hit test parameters on the SpriteWorld. * @deprecated since QTJava 6.1
     */
    public int getFlags() {
        return this.flags;
    }

    /**
     * Deprecated.
     * Set the flags for controlling the hit test behaviour of the SpriteWorld.* @deprecated since QTJava 6.1
     */
    public void setFlags(int paramInt) {
        this.flags = paramInt;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A method that is called whenever the MouseController deactivates its responder.
     * A subclass should use this method to deactivate a responder (either explicitly or calling super.deactivateResponder).
     * For example a user may have
     * dragged a member out of a group, so the drag action's target is no longer valid and
     * the Responder has no target to act upon.
     */
    protected void deactivateResponder() {
        this.isActive = false;
        super.deactivateResponder();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A notification method that is called whenever the MouseController activates its responder.
     */
    protected void responderActivated() {
        this.isActive = true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is used to setup translation values for x and y of an event to
     * translate the event into the local coordinate space of a member's display Space.
     * In the case of a DirectGroup the translation is (0, 0), in the case of a
     * Compositor member (which can be embedded n-deep) the translation is the
     * sum of the locations of the space's parents.
     *
     * The method sets the value of the protected instance variables xLocationOffset and yLocation offset.
     */
    protected void setTranslation() {
        SWCompositor localSWCompositor1 = this.sd;
        SWCompositor localSWCompositor2 = localSWCompositor1;
        this.xLocationOffset = 0;
        this.yLocationOffset = 0;
        try {
            while (localSWCompositor1 != null) {
                localSWCompositor2 = localSWCompositor1;
                QDRect localQDRect = localSWCompositor2.getDisplayBounds();
                this.xLocationOffset += localQDRect.getX();
                this.yLocationOffset += localQDRect.getY();
                localSWCompositor1 = localSWCompositor2.getParent();
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), localSWCompositor2, "getDisplayBounds");
        }
    }

    private QDPoint p = new QDPoint(0, 0);

    /**
     * @deprecated
     */
    protected Object getSelected(MouseEvent paramMouseEvent) throws QTException {
        QDRect localQDRect = this.sd.getDisplayBounds();
        this.p.setX(paramMouseEvent.getX() + localQDRect.getX());
        this.p.setY(paramMouseEvent.getY() + localQDRect.getY());
        if (!localQDRect.inside(this.p.getX(), this.p.getY())) {
            return null;
        }
        if (isWholespace()) {
            this.sd.hitTestFlag = this.flags;
            return this.sd.hitTest(this.p.getX(), this.p.getY());
        }
        Object localObject = null;
        Enumeration localEnumeration = members();
        while (localEnumeration.hasMoreElements()) {
            this.p.setX(paramMouseEvent.getX());
            this.p.setY(paramMouseEvent.getY());
            TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
            if (localTwoDSprite.hitTest(this.flags, this.p)) {
                if (localObject != null) {
                    if (localTwoDSprite.getLayer() < ((TwoDSprite) localObject).getLayer())
                        localObject = localTwoDSprite;
                } else {
                    localObject = localTwoDSprite;
                }
            }
        }
        return localObject;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/anim/
 * SWController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
