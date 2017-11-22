
package quicktime.app.event;

import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.display.QTDrawable;
import quicktime.app.spaces.Collection;
import quicktime.app.spaces.Space;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;


/**
 * @deprecated
 */
public class QTMouseTargetController extends QTMouseController implements Collection {
    private Object mousePressTarget;

    private boolean inPressDraggingTarget;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public QTMouseTargetController() {
        this(true);
    }

    /**
     * @deprecated
     */
    public QTMouseTargetController(boolean paramBoolean) {
        this.wholespace = paramBoolean;
        this.memberVec = new Vector();
    }

    private QDPoint cachePoint = new QDPoint(0, 0);

    MouseTargetListener mouseTargetListener;

    private Object currentMouseMovedTarget = null;

    private boolean wholespace;

    private Vector memberVec;

    /**
     * @deprecated
     */
    public synchronized void addedToSpace(Space paramSpace) {
        super.addedToSpace(paramSpace);
        for (int i = 0; i < this.memberVec.size(); i++) {
            Object localObject = this.memberVec.elementAt(i);
            if (this.compSpace != null) {
                if (!(localObject instanceof TwoDSprite)) {
                    this.memberVec.removeElementAt(i);
                    TwoDSprite localTwoDSprite = this.compSpace.getMemberSprite(localObject);
                    if (localTwoDSprite != null) {
                        this.memberVec.insertElementAt(localTwoDSprite, i);
                    } else {
                        i--;
                    }
                } else if (!this.space.hasMember(localObject)) {
                    this.memberVec.removeElementAt(i);
                    i--;
                }

            } else if (!this.space.hasMember(localObject)) {
                this.memberVec.removeElementAt(i);
                i--;
            }
        }
    }

    /**
     * @deprecated
     */
    protected void removeActiveHandler() {
        if ((this.mouseTargetListener != null) && (this.currentMouseMovedTarget != null)) {
            QTMouseEvent localQTMouseEvent = new QTMouseEvent(this.space);
            localQTMouseEvent.initialiseEventProfile(508, -1, -1, 0L, 0, 0);
            localQTMouseEvent.setTarget(this.currentMouseMovedTarget);
            doMouseTargetExit(localQTMouseEvent);
            this.currentMouseMovedTarget = null;
        }
        super.removeActiveHandler();
    }

    /**
     * @deprecated
     */
    protected void addHandler() {
        super.addHandler();
        if (hasMouseTargetListener())
            this.handler.addMouseTargetListener();
    }

    /**
     * @deprecated
     */
    public boolean isWholespace() {
        return this.wholespace;
    }

    /**
     * @deprecated
     */
    public void setWholespace(boolean paramBoolean) {
        this.wholespace = paramBoolean;
    }

    /**
     * @deprecated
     */
    public synchronized boolean addMember(Object paramObject) {
        if (!isAppropriate(paramObject))
            return false;
        if (!hasMember(paramObject)) {
            if (this.compSpace != null) {
                if (!(paramObject instanceof TwoDSprite)) {
                    TwoDSprite localTwoDSprite = this.compSpace.getMemberSprite(paramObject);
                    if (localTwoDSprite == null) {
                        return false;
                    }
                    this.memberVec.addElement(localTwoDSprite);
                    return true;
                }

                if (!this.compSpace.hasMember(paramObject)) {
                    return false;
                }
            } else if ((this.space != null) && (!this.space.hasMember(paramObject))) {
                return false;
            }
            this.memberVec.addElement(paramObject);
        }
        return true;
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        if (this.space != null) {
            return this.space.isAppropriate(paramObject);
        }
        return true;
    }

    /**
     * @deprecated
     */
    public void removeMember(Object paramObject) {
        this.memberVec.removeElement(paramObject);
    }

    /**
     * @deprecated
     */
    public Enumeration members() {
        return this.memberVec.elements();
    }

    /**
     * @deprecated
     */
    public boolean isEmpty() {
        return this.memberVec.isEmpty();
    }

    /**
     * @deprecated
     */
    public int size() {
        return this.memberVec.size();
    }

    /**
     * @deprecated
     */
    public boolean hasMember(Object paramObject) {
        return paramObject == null ? false : this.memberVec.contains(paramObject);
    }

    /**
     * @deprecated
     */
    public void addQTMouseListener(QTMouseListener paramQTMouseListener) {
        if ((paramQTMouseListener instanceof MouseTargetListener))
            addMouseTargetListener((MouseTargetListener) paramQTMouseListener);
        super.addQTMouseListener(paramQTMouseListener);
    }

    /**
     * @deprecated
     */
    public void removeQTMouseListener(QTMouseListener paramQTMouseListener) {
        if ((paramQTMouseListener instanceof MouseTargetListener))
            removeMouseTargetListener((MouseTargetListener) paramQTMouseListener);
        super.removeQTMouseListener(paramQTMouseListener);
    }

    void addMouseTargetListener(MouseTargetListener paramMouseTargetListener) {
        boolean bool = hasMouseTargetListener();
        this.mouseTargetListener = QTEventCaster.add(this.mouseTargetListener, paramMouseTargetListener);
        updateMouseTargetListener(bool);
    }

    void removeMouseTargetListener(MouseTargetListener paramMouseTargetListener) {
        boolean bool = hasMouseTargetListener();
        this.mouseTargetListener = QTEventCaster.remove(this.mouseTargetListener, paramMouseTargetListener);
        updateMouseTargetListener(bool);
    }

    void updateMouseTargetListener(boolean paramBoolean) {
        if (this.handler != null) {
            if ((paramBoolean) && (!hasMouseTargetListener())) {
                this.handler.removeMouseTargetListener();
            } else if ((hasMouseTargetListener()) && (!paramBoolean))
                this.handler.addMouseTargetListener();
        }
    }

    boolean hasMouseTargetListener() {
        return this.mouseTargetListener != null;
    }

    boolean isInterestedInMouseMoveEvents(QTMouseEvent paramQTMouseEvent) {
        return ((this.mouseTargetListener != null)
                && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers())))
               || (super.isInterestedInMouseMoveEvents(paramQTMouseEvent));
    }

    /**
     * @deprecated
     */
    protected void doMousePressed(QTMouseEvent paramQTMouseEvent) {
        this.mousePressTarget = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
        if (this.mousePressTarget != null) {
            this.inPressDraggingTarget = true;
            paramQTMouseEvent.setTarget(this.mousePressTarget);
            this.mouseButtonListener.mousePressed(paramQTMouseEvent);
            this.currentMouseMovedTarget = null;
        }
    }

    /**
     * @deprecated
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseButtonListener != null)
            && (this.mouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            if (translateAndIsWithin(paramQTMouseEvent)) {
                doMouseReleased(paramQTMouseEvent, true);
            } else if (this.mousePressTarget != null) {
                doMouseReleased(paramQTMouseEvent, false);
            }
        }
    }

    /**
     * @deprecated
     */
    protected void doMouseReleased(QTMouseEvent paramQTMouseEvent, boolean paramBoolean) {
        paramQTMouseEvent.setTarget(this.mousePressTarget);
        this.mouseButtonListener.mouseReleased(paramQTMouseEvent);
        if (this.inPressDraggingTarget) {
            this.currentMouseMovedTarget = this.mousePressTarget;
        } else {
            this.currentMouseMovedTarget = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
            if ((this.mouseTargetListener != null)
                && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers())))
                if (paramBoolean) {
                    Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
                    if (localObject != null) {
                        paramQTMouseEvent.setEventType(507);
                        paramQTMouseEvent.setTarget(localObject);
                        doMouseTargetEnter(paramQTMouseEvent);
                        paramQTMouseEvent.setEventType(502);
                    }
                    this.currentMouseMovedTarget = localObject;
                } else {
                    this.currentMouseMovedTarget = null;
                }
        }
        this.mousePressTarget = null;
        paramQTMouseEvent.setTarget(null);
    }

    /**
     * @deprecated
     */
    protected void doMouseClicked(QTMouseEvent paramQTMouseEvent) {
        Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
        if (localObject != null) {
            paramQTMouseEvent.setTarget(localObject);

            this.mouseButtonListener.mouseClicked(paramQTMouseEvent);
            paramQTMouseEvent.setTarget(null);
        }
    }

    /**
     * @deprecated
     */
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
        if (this.mousePressTarget == null) {
            return;
        }
        super.mouseDragged(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    protected void doMouseDragged(QTMouseEvent paramQTMouseEvent) {
        paramQTMouseEvent.setTarget(this.mousePressTarget);
        if ((this.mouseMoveListener != null)
            && (this.mouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            if (this.drawSpace != null) {
                try {
                    QTDrawable localQTDrawable = (QTDrawable) this.mousePressTarget;
                    Region localRegion1 = null;
                    localRegion1 = new Region(localQTDrawable.getDisplayBounds());

                    this.mouseMoveListener.mouseDragged(paramQTMouseEvent);

                    Region localRegion2 = new Region(localQTDrawable.getDisplayBounds());
                    Region localRegion3 = localRegion1.union(localRegion2).diff(localRegion2);
                    if (!localRegion3.empty())
                        this.drawSpace.redrawMember(localQTDrawable, localRegion3);
                } catch (QTException localQTException) {
                    QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mouseDragged");
                }
            } else {
                this.mouseMoveListener.mouseDragged(paramQTMouseEvent);
            }
        }
        if ((this.mouseTargetListener != null)
            && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
            if ((localObject == null) && (this.inPressDraggingTarget)) {
                paramQTMouseEvent.setEventType(508);
                doMouseTargetExit(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(506);
                this.inPressDraggingTarget = false;
            } else if (localObject != null) {
                if ((this.inPressDraggingTarget) && (localObject != this.mousePressTarget)) {
                    paramQTMouseEvent.setEventType(508);
                    doMouseTargetExit(paramQTMouseEvent);
                    paramQTMouseEvent.setEventType(506);
                    this.inPressDraggingTarget = false;
                } else if ((!this.inPressDraggingTarget) && (localObject == this.mousePressTarget)) {
                    paramQTMouseEvent.setEventType(507);
                    doMouseTargetEnter(paramQTMouseEvent);
                    paramQTMouseEvent.setEventType(506);
                    this.inPressDraggingTarget = true;
                }
            }
        }
    }

    /**
     * @deprecated
     */
    protected void doMouseTargetExit(QTMouseEvent paramQTMouseEvent) {
        this.mouseTargetListener.mouseTargetExited(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    protected void doMouseTargetEnter(QTMouseEvent paramQTMouseEvent) {
        this.mouseTargetListener.mouseTargetEntered(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    protected void doMouseMoved(QTMouseEvent paramQTMouseEvent, boolean paramBoolean) {
        if ((this.mouseTargetListener != null)
            && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            if ((!paramBoolean) && (this.currentMouseMovedTarget != null)) {
                paramQTMouseEvent.setEventType(508);
                paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                doMouseTargetExit(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(503);

                this.currentMouseMovedTarget = null;
                return;
            }

            Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());

            if (localObject == null) {
                if (this.currentMouseMovedTarget != null) {
                    paramQTMouseEvent.setEventType(508);
                    paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                    doMouseTargetExit(paramQTMouseEvent);
                    paramQTMouseEvent.setEventType(503);

                    this.currentMouseMovedTarget = null;
                }
            } else if (this.currentMouseMovedTarget == null) {
                paramQTMouseEvent.setEventType(507);
                paramQTMouseEvent.setTarget(localObject);
                doMouseTargetEnter(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(503);
                this.currentMouseMovedTarget = localObject;
            } else if (this.currentMouseMovedTarget != localObject) {
                paramQTMouseEvent.setEventType(508);
                paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                doMouseTargetExit(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(507);
                paramQTMouseEvent.setTarget(localObject);
                doMouseTargetEnter(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(503);
                this.currentMouseMovedTarget = localObject;
            } else if ((this.mouseMoveListener != null)
                       && (this.mouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
                paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                this.mouseMoveListener.mouseMoved(paramQTMouseEvent);
            }
        }
    }

    /**
     * @deprecated
     */
    public void mouseEntered(QTMouseEvent paramQTMouseEvent) {
        super.mouseEntered(paramQTMouseEvent);
        if (((this.mouseEnterExitListener != null)
             && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) ? 1 : 0) == 0) {
            doMouseEnter_Target(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    protected void doMouseEntered(QTMouseEvent paramQTMouseEvent) {
        super.doMouseEntered(paramQTMouseEvent);
        doMouseEnter_Target(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    public void mouseExited(QTMouseEvent paramQTMouseEvent) {
        if (((this.mouseEnterExitListener != null)
             && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) ? 1 : 0) == 0) {
            doMouseExit_Target(paramQTMouseEvent);
        }
        super.mouseExited(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    protected void doMouseExited(QTMouseEvent paramQTMouseEvent) {
        doMouseExit_Target(paramQTMouseEvent);
        super.doMouseExited(paramQTMouseEvent);
    }

    private void doMouseEnter_Target(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseTargetListener != null)
            && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            Object localObject;
            if (this.mousePressTarget != null) {
                localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
                if ((localObject != null) && (!this.inPressDraggingTarget) && (localObject == this.mousePressTarget)) {
                    paramQTMouseEvent.setEventType(507);
                    paramQTMouseEvent.setTarget(this.mousePressTarget);
                    doMouseTargetEnter(paramQTMouseEvent);
                    this.inPressDraggingTarget = true;
                }
            } else {
                localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
                if (localObject != null) {
                    paramQTMouseEvent.setEventType(507);
                    paramQTMouseEvent.setTarget(localObject);
                    doMouseTargetEnter(paramQTMouseEvent);
                    this.currentMouseMovedTarget = localObject;
                }
            }
            paramQTMouseEvent.setTarget(null);
            paramQTMouseEvent.setEventType(504);
        }
    }

    private void doMouseExit_Target(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseTargetListener != null)
            && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            if (this.mousePressTarget != null) {
                Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
                if ((localObject == null) && (this.inPressDraggingTarget)) {
                    paramQTMouseEvent.setEventType(508);
                    paramQTMouseEvent.setTarget(this.mousePressTarget);
                    doMouseTargetExit(paramQTMouseEvent);
                    this.inPressDraggingTarget = false;
                }
            } else if (this.currentMouseMovedTarget != null) {
                paramQTMouseEvent.setEventType(508);
                paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                doMouseTargetExit(paramQTMouseEvent);
                this.currentMouseMovedTarget = null;
            }
            paramQTMouseEvent.setTarget(null);
            paramQTMouseEvent.setEventType(505);
        }
    }

    /**
     * @deprecated
     */
    protected Object getHitTarget(int paramInt1, int paramInt2) {
        if (this.displaySpace != null) {
            try {
                QDRect localQDRect = this.displaySpace.getDisplayBounds();
                int i = paramInt1 + localQDRect.getX();
                int j = paramInt2 + localQDRect.getY();

                if (!localQDRect.inside(i, j)) {
                    return null;
                }

                if (isWholespace()) {
                    return this.displaySpace.hitTest(i, j);
                }

                if (this.drawSpace != null) {
                    Object localObject = this.displaySpace.hitTest(i, j);
                    if (hasMember(localObject)) {
                        return localObject;
                    }
                    return null;
                }
                Object localObject = null;

                Enumeration localEnumeration = members();

                this.cachePoint.setX(i);
                this.cachePoint.setY(j);
                while (localEnumeration.hasMoreElements()) {
                    TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
                    if (localTwoDSprite.hitTest(this.compSpace.hitTestFlag | 0x10, this.cachePoint)) {
                        if (localObject != null) {
                            if (localTwoDSprite.getLayer() < ((TwoDSprite) localObject).getLayer())
                                localObject = localTwoDSprite;
                        } else {
                            localObject = localTwoDSprite;
                        }
                    }
                }
                return localObject;
            } catch (QTException localQTException) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException),
                                                 this.displaySpace,
                                                 "getDisplayBounds");
            }
        }
        return null;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTMouseTargetController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
