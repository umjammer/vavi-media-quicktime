
package quicktime.app.display;

import java.awt.Dimension;
import java.util.Enumeration;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Protocol;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDColor;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.image.Matrix;
import quicktime.util.QTHandle;
import quicktime.util.QTUtils;


/**
 * @deprecated
 */
public class DirectGroup extends GroupDrawable {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static Protocol p = makeP();

    private boolean doMemCleanup;

    private static Protocol makeP() {
        try {
            return new Protocol(Class.forName("quicktime.app.display.QTDrawable"));
        } catch (Exception localException) {
            throw new QTRuntimeException(localException.getMessage());
        }
    }

    /**
     * @deprecated
     */
    public DirectGroup(Dimension paramDimension, QDColor paramQDColor) throws QTException {
        this(new QDDimension(paramDimension.width, paramDimension.height), paramQDColor, 10, 1, p);
    }

    /**
     * @deprecated
     */
    public DirectGroup(QDDimension paramQDDimension, QDColor paramQDColor) throws QTException {
        this(paramQDDimension, paramQDColor, 10, 1, p);
    }

    /**
     * @deprecated
     */
    protected DirectGroup(QDDimension paramQDDimension,
            QDColor paramQDColor,
            int paramInt1,
            int paramInt2,
            Protocol paramProtocol) throws QTException {
        super(paramQDDimension, paramQDColor, paramInt1, paramInt2, paramProtocol);
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return (getProtocol().passProtocol(paramObject)) && (!(paramObject instanceof GroupDrawable));
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject, int paramInt, float paramFloat1, float paramFloat2) throws QTException {
        if (!isAppropriate(paramObject))
            return false;
        setAlignedMemberLocation((QTDrawable) paramObject, paramFloat1, paramFloat2);
        _addQTDrawable(new GroupMember(this, (QTDrawable) paramObject, paramInt));
        return true;
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject, int paramInt) throws QTException {
        if (!isAppropriate(paramObject))
            return false;
        QTDrawable localQTDrawable = (QTDrawable) paramObject;
        _addQTDrawable(new GroupMember(this, localQTDrawable, paramInt));
        return true;
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject) throws QTException {
        int i = 1;
        if ((paramObject instanceof Layerable))
            i = ((Layerable) paramObject).getLayer();
        return addMember(paramObject, i);
    }

    /**
     * @deprecated
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        super.setGWorld(paramQDGraphics);
        Enumeration<?> localEnumeration = this.vec.elements();
        while (localEnumeration.hasMoreElements()) {
            GroupMember localGroupMember = (GroupMember) localEnumeration.nextElement();
            localGroupMember.drawer.setGWorld(this.port);
        }
        if (this.visible) {
            setClip(null);
        }
    }

    /**
     * @deprecated
     */
    public void setDisplayBounds(QDRect paramQDRect) throws QTException {
        super.setDisplayBounds(paramQDRect);
        setClip(null);
    }

    /**
     * @deprecated
     */
    public synchronized void redrawMember(QTDrawable paramQTDrawable, Region paramRegion) throws QTException {
        if (paramQTDrawable != null) {
            GroupMember localGroupMember1 = null;
            try {
                localGroupMember1 = memberChanged(paramQTDrawable);
            } catch (QTException localQTException) {
                if ((localQTException.errorCode() == 65036) || (localQTException.errorCode() == -108)) {
                    QTUtils.reclaimMemory();
                }

                throw localQTException;
            }
            if (localGroupMember1 == null) {
                redraw(null);
                return;
            }

            synchronized (QTNative.globalsLock) {

                int i = 0;
                int j = 0;
                for (int k = 0; k < this.vec.size(); k++) {
                    GroupMember localGroupMember2 = (GroupMember) this.vec.elementAt(k);
                    if (i != 0) {
                        Region localRegion2 = localGroupMember2.drawer.getClip().sect(paramRegion);
                        if (!localRegion2.empty()) {
                            localGroupMember2.drawer.redraw(localRegion2);
                        }
                    }
                    if ((i == 0) && (localGroupMember2.drawer.equals(paramQTDrawable))) {
                        localGroupMember2.drawer.redraw(null);
                        i = 1;
                        if (paramRegion != null) {
                            paramRegion = paramRegion.union(localGroupMember1.clipRegion);
                        } else
                            paramRegion = localGroupMember1.clipRegion;
                    }
                }
                Region localRegion1 = paramRegion.sect(this.eraseRegion);
                if (!localRegion1.empty()) {
                    this.port.setBackColor(this.bgColor);
                    this.port.eraseRgn(localRegion1);
                }
            }
        }
        redraw(null);
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws QTException {
        this.clip = (paramRegion == null ? new Region(getDisplayBounds()) : paramRegion.sect(getDisplayBounds()));
        Region localRegion = this.clip;
        Object localObject = null;
        this.doMemCleanup = true;
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember localGroupMember = (GroupMember) this.vec.elementAt(i);
            if (localObject != null)
                localRegion = localRegion.diff(drawersRegion((GroupMember) localObject));
            localGroupMember.drawer.setClip(localRegion);

            localObject = localGroupMember;
        }
        if (localObject != null) {
            this.eraseRegion = localRegion.diff(drawersRegion((GroupMember) localObject));
        } else {
            this.eraseRegion = localRegion;
        }

        if (!this.doMemCleanup)
            QTUtils.reclaimMemory();
    }

    private Region drawersRegion(GroupMember paramGroupMember) throws QTException {
        QTDrawable localQTDrawable = paramGroupMember.drawer;
        int i = localQTDrawable.getMatrix().getType();
        int j = 0;
        switch (i) {
        case 4:
        case 5:
        case 6:
            j = 1;
            break;
        case 2:
        case 3:
            Matrix localMatrix = localQTDrawable.getMatrix();
            if ((localMatrix.getSx() < 0.0F) || (localMatrix.getSy() < 0.0F)) {
                j = 1;
            }

            break;
        }

        if ((paramGroupMember.clipRegion == null) || (this.redoCachedMembersClip))
            paramGroupMember.clipRegion = (j != 0 ? createRotatedRegion(localQTDrawable)
                                                  : new Region(localQTDrawable.getDisplayBounds()));
        return paramGroupMember.clipRegion;
    }

    private Region createRotatedRegion(QTDrawable paramQTDrawable) throws QTException {
        if (this.doMemCleanup) {
            QTUtils.reclaimMemory();
            QTUtils.checkFreeMemory();
            this.doMemCleanup = false;
        }

        Matrix localMatrix = paramQTDrawable.getMatrix().copy();
        QDRect localQDRect = new QDRect(paramQTDrawable.getOriginalSize());
        localMatrix.transformRect(localQDRect);

        QDGraphics localQDGraphics = new QDGraphics(1, localQDRect);
        localQDGraphics.setBackColor(QDColor.white);
        localQDGraphics.eraseRect(localQDRect);
        paramQTDrawable.setGWorld(localQDGraphics);
        paramQTDrawable.redraw(null);
        paramQTDrawable.setGWorld(getGWorld());
        try {
            return new Region(localQDGraphics.getPixMap());
        } catch (QTException localQTException1) {
            if ((localQTException1.errorCode() == -108) || (localQTException1.errorCode() == 65036)) {

                QTUtils.reclaimMemory();
                try {
                    return new Region(localQDGraphics.getPixMap());
                } catch (QTException localQTException2) {
                    if (localQTException2.errorCode() == 65036) {
                        new QTHandle(512000, false);
                        return new Region(localQDGraphics.getPixMap());
                    }
                }
            }
            throw localQTException1;
        }
    }

    /**
     * @deprecated
     */
    public GroupMember memberChanged(QTDrawable paramQTDrawable) throws QTException {
        GroupMember localGroupMember = getGroupMember(paramQTDrawable);
        if (localGroupMember != null) {
            setClips(paramQTDrawable);
        }
        return localGroupMember;
    }

    /**
     * @deprecated
     */
    public boolean setMemberAlignment(QTDrawable paramQTDrawable, float paramFloat1, float paramFloat2) throws QTException {
        boolean bool = super.setMemberAlignment(paramQTDrawable, paramFloat1, paramFloat2);
        if (bool) {
            setClips(paramQTDrawable);
            redraw(null);
        }
        return bool;
    }

    /**
     * @deprecated
     */
    public boolean setMemberLayer(QTDrawable paramQTDrawable, int paramInt) throws QTException {
        boolean bool = super.setMemberLayer(paramQTDrawable, paramInt);
        if (bool) {
            this.redoCachedMembersClip = true;
            setClip(null);
            this.redoCachedMembersClip = false;
            redraw(null);
        }
        return bool;
    }

    private boolean redoCachedMembersClip = false;

    private boolean setClips(QTDrawable paramQTDrawable) throws QTException {
        boolean bool = false;
        Region localRegion = this.clip;
        Object localObject = null;
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember localGroupMember = (GroupMember) this.vec.elementAt(i);
            if (localObject != null)
                localRegion = localRegion.diff(((GroupMember) localObject).clipRegion);
            localObject = localGroupMember;
            if (((GroupMember) localObject).drawer.equals(paramQTDrawable)) {
                bool = true;

                this.doMemCleanup = true;
                ((GroupMember) localObject).clipRegion = null;
                this.redoCachedMembersClip = true;
                for (i++; i < this.vec.size(); i++) {
                    localGroupMember = (GroupMember) this.vec.elementAt(i);
                    localRegion = localRegion.diff(drawersRegion((GroupMember) localObject));
                    localGroupMember.drawer.setClip(localRegion);
                    localObject = localGroupMember;
                }
            }
        }

        if (localObject != null) {
            this.eraseRegion = localRegion.diff(drawersRegion((GroupMember) localObject));
        }
        this.redoCachedMembersClip = false;

        if (!this.doMemCleanup) {
            QTUtils.reclaimMemory();
        }
        return bool;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/DirectGroup.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
