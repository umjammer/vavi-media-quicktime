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
 * @deprecated
 * The SWController deals with SpriteWorldHitTest calls on the SpriteWorld that
 * is contained by the SWCompositor's sub classes.
 * By default it will do hit testing on the actual sprite image itself. However, an application can
 * set the hit-test flags to support any mode of hit testing appropriate.
 * @see quicktime.app.event.QTMouseController
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
     * @deprecated
     * @since QTJava 6.1
     * Construct a SWController object.
     * @param mr the Responder that will respond to mouse events when a sprite is hit.
     * @param wholeSpace if true all object that are in the space will be dragged. If false
     *  then you must explicitly add the object to the controller.
     */
    public SWController(MouseResponder mr, boolean wholeSpace) {
        this(mr, wholeSpace, 10);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Construct a SWController object.
     * @param mr the Responder that will respond to mouse events when a sprite is hit.
     * @param wholeSpace if true all object that are in the space will be dragged. If false
     *  then you must explicitly add the object to the controller.
     * @param flags that will control the hit test parameters of the SpriteWorld
     */
    public SWController(MouseResponder mr, boolean wholeSpace, int flags) {
        super(mr, wholeSpace);
        this.flags = flags;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current Space that the SWController is attached to.
     * This space will be an instance of the SWCompositor class.
     * @return the Space the controller is attached to.
     */
    @Override
    public Space getSpace() {
        return this.sd;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the target Space of the MouseController. This target must be
     * a kind of SWCompositor class.
     * @param target the potential target space for the SWController.
     */
    @Override
    protected void setTargetSpace(Space target) {
        this.sd = ((SWCompositor) target);
    }

    /**
     * @deprecated
     * Returns the flags that control the hit test parameters on the SpriteWorld. * @deprecated since QTJava 6.1
     */
    public int getFlags() {
        return this.flags;
    }

    /**
     * @deprecated
     * Set the flags for controlling the hit test behaviour of the SpriteWorld.* @deprecated since QTJava 6.1
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * A method that is called whenever the MouseController deactivates its responder.
     * A subclass should use this method to deactivate a responder (either explicitly or calling super.deactivateResponder).
     * For example a user may have
     * dragged a member out of a group, so the drag action's target is no longer valid and
     * the Responder has no target to act upon.
     */
    @Override
    protected void deactivateResponder() {
        this.isActive = false;
        super.deactivateResponder();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * A notification method that is called whenever the MouseController activates its responder.
     */
    @Override
    protected void responderActivated() {
        this.isActive = true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is used to setup translation values for x and y of an event to
     * translate the event into the local coordinate space of a member's display Space.
     * In the case of a DirectGroup the translation is (0, 0), in the case of a
     * Compositor member (which can be embedded n-deep) the translation is the
     * sum of the locations of the space's parents.
     *
     * The method sets the value of the protected instance variables xLocationOffset and yLocation offset.
     */
    @Override
    protected void setTranslation() {
        SWCompositor parent = this.sd;
        SWCompositor swCompositor2 = parent;
        this.xLocationOffset = 0;
        this.yLocationOffset = 0;
        try {
            while (parent != null) {
                swCompositor2 = parent;
                QDRect displayBounds = swCompositor2.getDisplayBounds();
                this.xLocationOffset += displayBounds.getX();
                this.yLocationOffset += displayBounds.getY();
                parent = swCompositor2.getParent();
            }
        } catch (QTException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), swCompositor2, "getDisplayBounds");
        }
    }

    private final QDPoint p = new QDPoint(0, 0);

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called when a MouseController has received a mouse
     * event in its space. This method returns a TwoDSprite object if the SpriteWorld.hitTest
     * method returns a Sprite at the event's location.
     * @param event a mouse down event
     * @return a TwoDSprite object that is located at the event coordinates or null if no Sprite
     *  at the location of the event.
     * @throws quicktime.QTException
     */
    @Override
    protected Object getSelected(MouseEvent event) throws QTException {
        QDRect displayBounds = this.sd.getDisplayBounds();
        this.p.setX(event.getX() + displayBounds.getX());
        this.p.setY(event.getY() + displayBounds.getY());
        if (!displayBounds.inside(this.p.getX(), this.p.getY())) {
            return null;
        }
        if (isWholespace()) {
            this.sd.hitTestFlag = this.flags;
            return this.sd.hitTest(this.p.getX(), this.p.getY());
        }
        TwoDSprite selected = null;
        Enumeration enumeration = members();
        while (enumeration.hasMoreElements()) {
            this.p.setX(event.getX());
            this.p.setY(event.getY());
            TwoDSprite twoDSprite = (TwoDSprite) enumeration.nextElement();
            if (twoDSprite.hitTest(this.flags, this.p)) {
                if (selected != null) {
                    if (twoDSprite.getLayer() < selected.getLayer())
                        selected = twoDSprite;
                } else {
                    selected = twoDSprite;
                }
            }
        }
        return selected;
    }
}
