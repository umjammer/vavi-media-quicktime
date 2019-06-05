
package quicktime.app.display;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.Transformable;
import quicktime.app.spaces.Protocol;
import quicktime.app.spaces.SimpleSpace;
import quicktime.qd.QDColor;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.Matrix;


/**
 * @deprecated
 */
public abstract class GroupDrawable extends SimpleSpace implements QTDisplaySpace {
    static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected GroupDrawable(Dimension paramDimension,
            QDColor paramQDColor,
            int paramInt1,
            int paramInt2,
            Protocol paramProtocol) throws QTException {
        this(new QDDimension(paramDimension.width, paramDimension.height), paramQDColor, paramInt1, paramInt2, paramProtocol);
    }

    /**
     * @deprecated
     */
    protected GroupDrawable(QDDimension paramQDDimension,
            QDColor paramQDColor,
            int paramInt1,
            int paramInt2,
            Protocol paramProtocol) throws QTException {
        super(paramProtocol, paramInt1, paramInt2);
        this.displayBounds = new QDRect(paramQDDimension);
        this.initSize = paramQDDimension;
        this.eraseRegion = new Region(this.displayBounds);
        this.clip = this.eraseRegion;
        setBackgroundColor(paramQDColor);
    }

    QDGraphics port = QDGraphics.scratch;

    QDRect displayBounds;

    boolean visible = false;

    QDDimension initSize;

    Region eraseRegion;

    QDColor bgColor;

    protected Region clip;

    /**
     * @deprecated
     */
    protected Vector<Object> vec = new Vector<>(8);

    /**
     * @deprecated
     */
    public QDGraphics getGWorld() throws StdQTException {
        return this.port;
    }

    /**
     * @deprecated
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.port = paramQDGraphics;
        boolean bool = this.visible;
        this.visible = (!QDGraphics.scratch.equals(paramQDGraphics));
    }

    /**
     * @deprecated
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        this.displayBounds.setX(paramInt1);
        this.displayBounds.setY(paramInt2);
        setDisplayBounds(this.displayBounds);
    }

    /**
     * @deprecated
     */
    public void setDisplayBounds(QDRect paramQDRect) throws QTException {
        this.displayBounds = paramQDRect;
    }

    /**
     * @deprecated
     */
    public QDRect getDisplayBounds() throws StdQTException {
        return this.displayBounds;
    }

    /**
     * @deprecated
     */
    public QDColor getBackgroundColor() {
        return this.bgColor;
    }

    /**
     * @deprecated
     */
    public void setBackgroundColor(QDColor paramQDColor) throws QTException {
        if (paramQDColor == null) {
            this.bgColor = this.port.getBackColor();
        } else
            this.bgColor = paramQDColor;
        if (this.visible) {
            redraw(null);
        }
    }

    /**
     * @deprecated
     */
    public synchronized void removeMember(Object paramObject) throws QTException {
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember localGroupMember = (GroupMember) this.vec.elementAt(i);
            QTDrawable localQTDrawable = localGroupMember.drawer;
            if (localQTDrawable.equals(paramObject)) {
                localQTDrawable.setGWorld(QDGraphics.scratch);
                this.vec.removeElementAt(i);
                memberRemoved(paramObject);

                if (!this.visible) {
                    break;
                }

                setClip(null);
                redraw(null);
                break;
            }
        }
    }

    /**
     * @deprecated
     */
    public synchronized Enumeration members() {
        Vector<Object> localVector = new Vector<>();
        Enumeration<?> localEnumeration = this.vec.elements();
        while (localEnumeration.hasMoreElements()) {
            GroupMember localGroupMember = (GroupMember) localEnumeration.nextElement();
            localVector.addElement(localGroupMember.drawer);
        }
        return localVector.elements();
    }

    /**
     * @deprecated
     */
    public boolean isEmpty() {
        return this.vec.isEmpty();
    }

    /**
     * @deprecated
     */
    public GroupMember getGroupMember(Object paramObject) {
        Enumeration<?> localEnumeration = this.vec.elements();
        while (localEnumeration.hasMoreElements()) {
            GroupMember localGroupMember = (GroupMember) localEnumeration.nextElement();
            if (localGroupMember.drawer.equals(paramObject))
                return localGroupMember;
        }
        return null;
    }

    /**
     * @deprecated
     */
    public int size() {
        return this.vec.size();
    }

    /**
     * @deprecated
     */
    public boolean setMemberAlignment(QTDrawable paramQTDrawable, float paramFloat1, float paramFloat2) throws QTException {
        GroupMember localGroupMember = getGroupMember(paramQTDrawable);
        if (localGroupMember != null) {
            setAlignedMemberLocation(localGroupMember.drawer, paramFloat1, paramFloat2);
        }
        return localGroupMember != null;
    }

    /**
     * @deprecated
     */
    public boolean setMemberLayer(QTDrawable paramQTDrawable, int paramInt) throws QTException {
        if (!isAppropriate(paramQTDrawable))
            return false;
        if (!hasMember(paramQTDrawable))
            return false;
        GroupMember localGroupMember1 = null;
        for (int i = 0; i < this.vec.size(); i++) {
            localGroupMember1 = (GroupMember) this.vec.elementAt(i);
            if (localGroupMember1.drawer.equals(paramQTDrawable)) {
                this.vec.removeElementAt(i);
                break;
            }
            localGroupMember1 = null;
        }
        if (localGroupMember1 != null) {
            int i = 0;
            localGroupMember1.layer = paramInt;
            for (int j = 0; j < this.vec.size(); j++) {
                GroupMember localGroupMember2 = (GroupMember) this.vec.elementAt(j);
                if (paramInt <= localGroupMember2.layer) {
                    i = 1;
                    this.vec.insertElementAt(localGroupMember1, j);
                    break;
                }
            }
            if (i == 0) {
                this.vec.addElement(localGroupMember1);
            }
        }
        return localGroupMember1 != null;
    }

    /**
     * @deprecated
     */
    public int getBackLayer() {
        if (this.vec.size() == 0)
            return -1;
        GroupMember localGroupMember = (GroupMember) this.vec.elementAt(this.vec.size() - 1);
        return localGroupMember.getLayer();
    }

    /**
     * @deprecated
     */
    public int getFrontLayer() {
        if (this.vec.size() == 0)
            return -1;
        GroupMember localGroupMember = (GroupMember) this.vec.elementAt(0);
        return localGroupMember.getLayer();
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.initSize.getWidth(), this.initSize.getHeight());
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        return this.initSize.copy();
    }

    /**
     * @deprecated
     */
    public synchronized void redraw(Region paramRegion) throws QTException {
        if (!this.eraseRegion.empty()) {
            this.port.setBackColor(this.bgColor);
            this.port.eraseRgn(this.eraseRegion);
        }
        for (int i = this.vec.size() - 1; i >= 0; i--) {
            ((GroupMember) this.vec.elementAt(i)).drawer.redraw(paramRegion);
        }
    }

    /**
     * @deprecated
     */
    public Layerable getLayerable(Object paramObject) {
        return getGroupMember(paramObject);
    }

    /**
     * @deprecated
     */
    public Transformable getTransformable(Object paramObject) {
        if (hasMember(paramObject) == true)
            return (Transformable) paramObject;
        return null;
    }

    /**
     * @deprecated
     */
    public Object hitTest(int paramInt1, int paramInt2) throws QTException {
        if (this.vec != null) {
            if (!this.displayBounds.inside(paramInt1, paramInt2))
                return null;
            for (int i = 0; i < this.vec.size(); i++) {
                GroupMember localGroupMember = (GroupMember) this.vec.elementAt(i);
                if (localGroupMember.drawer.getDisplayBounds().inside(paramInt1, paramInt2))
                    return localGroupMember.drawer;
            }
        }
        return null;
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        localMatrix.setTx(this.displayBounds.getX());
        localMatrix.setTy(this.displayBounds.getY());

        return localMatrix;
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        int i = (int) (paramMatrix.getTx() + 0.5F);
        int j = (int) (paramMatrix.getTy() + 0.5F);
        setDisplayBounds(new QDRect(i, j, this.displayBounds.getWidth(), this.displayBounds.getHeight()));
    }

    /**
     * @deprecated
     */
    public Region getClip() throws QTException {
        return this.clip;
    }

    /**
     * @deprecated
     */
    public Region getBackgroundClip() {
        return this.eraseRegion;
    }

    final synchronized void _addQTDrawable(GroupMember paramGroupMember) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        int i = 0;

        synchronized (this.vec) {
            for (int j = 0; j < this.vec.size(); j++) {
                GroupMember localGroupMember = (GroupMember) this.vec.elementAt(j);
                if (paramGroupMember.drawer.equals(localGroupMember.drawer))
                    return;
                if (paramGroupMember.layer <= localGroupMember.layer) {
                    this.vec.insertElementAt(paramGroupMember, j);
                    i = 1;
                    break;
                }
            }

            if (i == 0) {
                this.vec.addElement(paramGroupMember);
            }
        }

        memberAdded(paramGroupMember.drawer);
        if (this.visible)
            setClip(null);
        paramGroupMember.drawer.setGWorld(getGWorld());
        if (this.visible) {
            redraw(null);
        }
    }

    /**
     * @deprecated
     */
    protected void setAlignedMemberLocation(QTDrawable paramQTDrawable,
                                            float paramFloat1,
                                            float paramFloat2) throws QTException {
        if (paramFloat1 < 0.0F) {
            paramFloat1 = 1.0F;
        } else if (paramFloat1 > 1.0F)
            paramFloat1 = 1.0F;
        if (paramFloat2 < 0.0F) {
            paramFloat2 = 1.0F;
        } else if (paramFloat2 > 1.0F) {
            paramFloat2 = 1.0F;
        }
        QDRect localQDRect1 = getDisplayBounds();

        int i = localQDRect1.getX();
        int j = localQDRect1.getY();
        int k = localQDRect1.getWidth();
        int m = localQDRect1.getHeight();

        QDRect localQDRect2 = paramQTDrawable.getDisplayBounds();

        int n = k - localQDRect2.getWidth();
        int i1 = m - localQDRect2.getHeight();
        if (n < 0)
            n = 0;
        if (i1 < 0) {
            i1 = 0;
        }

        int i2 = (int) (n * paramFloat1);

        int i3 = (int) (i1 * paramFloat2);

        paramQTDrawable.setLocation(i + i2, j + i3);
    }

    /**
     * @deprecated
     */
    public abstract GroupMember memberChanged(QTDrawable paramQTDrawable) throws QTException;

    /**
     * @deprecated
     */
    public abstract void redrawMember(QTDrawable paramQTDrawable, Region paramRegion) throws QTException;

    /**
     * @deprecated
     */
    public abstract void setClip(Region paramRegion) throws QTException;
}
