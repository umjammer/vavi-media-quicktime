
package quicktime.app.display;

import java.awt.event.MouseEvent;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.MouseController;
import quicktime.app.actions.MouseResponder;
import quicktime.app.spaces.Space;
import quicktime.qd.Region;


/**
 * @deprecated
 */
public class GroupController extends MouseController {
    private GroupDrawable group;

    private QTDrawable current;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public GroupController(MouseResponder paramMouseResponder, boolean paramBoolean) {
        super(paramMouseResponder, paramBoolean);
    }

    private boolean isActive = false;

    /**
     * @deprecated
     */
    public Space getSpace() {
        return this.group;
    }

    /**
     * @deprecated
     */
    protected void setTargetSpace(Space paramSpace) {
        this.group = ((GroupDrawable) paramSpace);
    }

    /**
     * @deprecated
     */
    protected Object getSelected(MouseEvent paramMouseEvent) throws QTException {
        this.current = ((QTDrawable) this.group.hitTest(paramMouseEvent.getX(), paramMouseEvent.getY()));
        if ((isWholespace()) || (hasMember(this.current)))
            return this.current;
        return null;
    }

    /**
     * @deprecated
     */
    protected void deactivateResponder() {
        this.current = null;
        this.isActive = false;
        super.deactivateResponder();
    }

    /**
     * @deprecated
     */
    protected void responderActivated() {
        this.isActive = true;
    }

    /**
     * @deprecated
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
        if (!this.isActive)
            return;
        try {
            synchronized (this.group) {
                MouseResponder localMouseResponder = getResponder();
                if ((localMouseResponder.isInterested(4)) && (localMouseResponder.isActive())) {
                    QTDrawable localQTDrawable = this.current;
                    doDragTarget(paramMouseEvent);
                    this.current = localQTDrawable;

                    Region localRegion1 = new Region(this.current.getDisplayBounds());
                    localMouseResponder.mouseDragged(paramMouseEvent);

                    Region localRegion2 = new Region(this.current.getDisplayBounds());
                    Region localRegion3 = localRegion1.union(localRegion2).diff(localRegion2);
                    if (!localRegion3.empty()) {
                        this.group.redrawMember(this.current, localRegion3);
                    }
                    if (!this.group.hasMember(this.current))
                        deactivateResponder();
                }
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mouseDragged");
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/GroupController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
