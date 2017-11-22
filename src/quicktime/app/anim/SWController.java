
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
     */
    public SWController(MouseResponder paramMouseResponder, boolean paramBoolean) {
        this(paramMouseResponder, paramBoolean, 10);
    }

    /**
     * @deprecated
     */
    public SWController(MouseResponder paramMouseResponder, boolean paramBoolean, int paramInt) {
        super(paramMouseResponder, paramBoolean);
        this.flags = paramInt;
    }

    /**
     * @deprecated
     */
    public Space getSpace() {
        return this.sd;
    }

    /**
     * @deprecated
     */
    protected void setTargetSpace(Space paramSpace) {
        this.sd = ((SWCompositor) paramSpace);
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int paramInt) {
        this.flags = paramInt;
    }

    /**
     * @deprecated
     */
    protected void deactivateResponder() {
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
