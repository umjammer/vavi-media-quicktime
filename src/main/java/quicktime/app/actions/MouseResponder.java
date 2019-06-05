
package quicktime.app.actions;

import java.awt.event.MouseEvent;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public abstract class MouseResponder {
    public static final int kNoModifiersMask = 0;

    public static final int kAnyModifiersMask = 0;

    public static final int kNoModifiers = -1;

    public static final int kAnyModifiers = -2;

    public static final int kAnyOneOfModifiers = -3;

    public static final int kModifiersDisregarded = -4;

    public static final int kModifiersExactMatch = -5;

    public static final int kSpecifiedModifiersOrNone = -6;

    public static final int kClickEvents = 1;

    public static final int kPressReleaseEvents = 2;

    public static final int kDragEvents = 4;

    public static final int kMoveEvents = 8;

    public static final int kEnterEvents = 16;

    public static final int kExitEvents = 32;

    public static final int kTargetMoveEvents = 64;

    public static final int kMouseEvents = 51;

    public static final int kMouseMotionEvents = 12;

    public static final int kQTMouseMotionEvents = 76;

    public static final int kRolloverEvents = 100;

    public static final int kMouseOrMouseMotionEvents = 63;

    public static final int kAnyMouseEvent = 127;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected MouseResponder(int paramInt1, int paramInt2, int paramInt3) {
        this.modifierKeyMask = paramInt1;
        this.modifierTestConditions = paramInt2;
        this.eventInterests = paramInt3;
    }

    private int activationEvent = 0;

    /**
     * @deprecated
     */
    public int modifierKeyMask;

    /**
     * @deprecated
     */
    public int modifierTestConditions;

    private int eventInterests = 0;

    MouseController controller;

    /**
     * @deprecated
     */
    public MouseController getMouseController() {
        return this.controller;
    }

    /**
     * @deprecated
     */
    public boolean isInterested(int paramInt) {
        return (this.eventInterests & paramInt) != 0;
    }

    /**
     * @deprecated
     */
    public boolean isInterested(int paramInt1, int paramInt2) {
        return ((this.eventInterests & paramInt1) != 0) && (doModifiersTest(paramInt2));
    }

    /**
     * @deprecated
     */
    private boolean doModifiersTest(int paramInt) {
        paramInt &= 0xF;
        switch (this.modifierTestConditions) {
        case -6:
            return (paramInt == 0) || (paramInt == this.modifierKeyMask);
        case -3:
            return (paramInt & this.modifierKeyMask) != 0;
        case -4:
            return true;
        case -2:
            return paramInt != 0;
        case -1:
            return paramInt == 0;
        case -5:
            return paramInt == this.modifierKeyMask;
        }
        return false;
    }

    void activate(int paramInt) {
        this.activationEvent = paramInt;
    }

    /**
     * @deprecated
     */
    public int activationEventType() {
        return this.activationEvent;
    }

    /**
     * @deprecated
     */
    public boolean isActive() {
        return this.activationEvent != 0;
    }

    /**
     * @deprecated
     */
    protected void deactivate() {
        this.activationEvent = 0;
        removeTarget();
    }

    /**
     * @deprecated
     */
    protected abstract void setTargetSpace(Object paramObject);

    /**
     * @deprecated
     */
    protected abstract void setTarget(Object paramObject);

    /**
     * @deprecated
     */
    protected abstract void removeTarget();

    /**
     * @deprecated
     */
    public abstract boolean isAppropriate(Object paramObject);

    /**
     * @deprecated
     */
    public void mouseClicked(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mouseEntered(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mouseExited(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mouseEnteredTarget(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mouseExitedTarget(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mouseMoved(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mousePressed(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mouseReleased(MouseEvent paramMouseEvent) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/MouseResponder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
