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
 * A DirectGroup contains a set of QTDrawable objects that present their
 * visual contents directly to the screen.
 *
 * Items that overlap screen space will draw over each other and the front
 * object will clip the rear object so that the rear object does not draw
 * in the area occupied by the front object.
 *
 * When the group is resized the group's items will not be resized or moved
 * but clipped to the new viewing area of the DirectGroup. Doing a setMatrix
 * on a DirectGroup will inherit the default behaviour of the GroupDrawable super
 * class which merely repositions the group - it will not scale, rotate or skew
 * the group or its members. As such the DirectGroup's display area reperesents a viewing area for its
 * members and when this viewing area is changed the user will see more or
 * less of the members that are in the DirectGroup.
 *
 * This behaves different from the SWCompositor group - where a resize of that group
 * will resize and scale the members of that group.
 *
 * This behaviour of the DirectGroup allows an application to use the DirectGroup as
 * a clipping area for its members, where the members of the group are not resized. This
 * class can be used to implement a scrolling pane for example. For example an application
 * may have a very large image that it wants to presents only portions of it. If the application
 * used the GraphicsImporterDrawer (GID) as the client of the QTCanvas the image presented by
 * the GID would be scaled to the viewing area of the QTCanvas. However if the client of the
 * canvas was a DirectGroup and the group contained the GID then the DirectGroup would represent
 * the viewing area of the GID. The application could define a MouseController and responder for this DirectGroup
 * that allowed the user to click on the GID member and when dragging the mouse around change the part
 * of the image that the user sees. Thus the GID image is not resized, and the DirectGroup acts as a clipping
 * viewing area to only see a part of the GID member.
 *
 * A DirectGroup can only be a top-level container object. Currently you cannot embed DirectGroups within other
 * (parent) DirectGroups. The isAppropriate() method returns false for a potential DirectGroup member object.
 * In this case the member won't be added, and the addMember methods will return false. If an application
 * wanted to support this capability they should overide the isAppropriate method to allow these kinds of members.
 */
public class DirectGroup extends GroupDrawable {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static final Protocol p = makeP();

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
     * <I>since QTJava 4.0, replaced by DirectGroup</I>
     * Create a new grouped drawable where the members draw directly to the screen.
     * @param initialSize the size of the display group
     * @param bc the background color of the group
     * {@link quicktime.app.display.DirectGroup#DirectGroup(quicktime.qd.QDDimension, quicktime.qd.QDColor)}
     */
    public DirectGroup(Dimension initialSize, QDColor bc) throws QTException {
        this(new QDDimension(initialSize.width, initialSize.height), bc, 10, 1, p);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a new grouped drawable where the members draw directly to the screen.
     * @param initialSize the size of the display group
     * @param bc the background color of the group
     */
    public DirectGroup(QDDimension initialSize, QDColor bc) throws QTException {
        this(initialSize, bc, 10, 1, p);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a new grouped drawable where the members draw directly to the screen.
     * @param initialSize the size of the display group
     * @param bc the background color of the group
     * @param scale the scale of the space's Timer
     * @param period the period of the space's Timer
     * @param p the default Protocol of members
     */
    protected DirectGroup(QDDimension initialSize, QDColor bc, int scale, int period, Protocol p) throws QTException {
        super(initialSize, bc, scale, period, p);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the Object object is an appropriate member of the Space. To be an
     * appropriate member the object must pass the passProtocol method of the Protocol
     * object of the Space.
     *
     * In addition to the requirement that the object be some kind of QTDrawable, the object
     * may also NOT be some kind of GroupDrawable. Thus you cannot embed DirectGroup objects
     * in a parent DirectGroup.
     * @param object the Object to test
     * @return true if the object passes the current protocol of the Protocol object of the Space.
     * @see quicktime.app.spaces.DynamicCollection,
     * {@link quicktime.app.spaces.Protocol}
     */
    @Override
    public boolean isAppropriate(Object object) {
        return (getProtocol().passProtocol(object)) && (!(object instanceof GroupDrawable));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Add a QTDrawable object to the group based on the provided alignment and layer parametres.
     * This method is provided as a convenience method to position a member in the current
     * display space of the Group.
     * @param layer the layer where the drawer will be placed
     * @param xAlign the x position of the drawable within the group - 0 is left, 1 is bounds.width - member.width
     * @param yAlign the y position of the drawable within the group - 0 is top, 1 is bounds.height - member.height
     * @return true if the member is appropriate and was added.
     * @throws quicktime.QTException
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
     * @since QTJava 6.1
     * Add a QTDrawable object to the group based on the current location of the member and the specified
     * layer parametre.
     * @param layer the layer where the drawer will be placed
     * @param member the member to add to the Display Space
     * @return true if the member is appropriate and was added.
     * @throws quicktime.QTException
     */
    @Override
    public boolean addMember(Object layer, int member) throws QTException {
        if (!isAppropriate(layer))
            return false;
        QTDrawable localQTDrawable = (QTDrawable) layer;
        _addQTDrawable(new GroupMember(this, localQTDrawable, member));
        return true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Add a QTDrawable object to the group with the current location of the QTDrawable.
     * If the member implements the Layerable interface it will get the layer and assign the layer appropriately.
     * Otherwise, the member will be added with a layer of 1.
     * @param member the member to add
     * @return true if the member is appropriate and was added.
     * @throws quicktime.QTException
     */
    @Override
    public boolean addMember(Object member) throws QTException {
        int i = 1;
        if ((member instanceof Layerable))
            i = ((Layerable) member).getLayer();
        return addMember(member, i);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @throws quicktime.QTException
     */
    @Override
    public void setGWorld(QDGraphics cgp) throws QTException {
        super.setGWorld(cgp);
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
     * @since QTJava 6.1
     * This method is called automatically via the QTCanvas object associated with
     * this client to set its boundary.
     *
     * @param bounds a QDRect object describing the boundary
     * @throws quicktime.QTException
     */
    @Override
    public void setDisplayBounds(QDRect bounds) throws QTException {
        super.setDisplayBounds(bounds);
        setClip(null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This is an optimized drawing method that will only reset the clipping of those
     * objects that are behind the supplied member. It will call memberChanged to do this.
     * It will then only redraw those members who are behind the specified member if and only
     * if the members behind were effected by the changed location of the specified member.
     * The supplied invalidRgn should specify the area that the supplied QTDrawble was located
     * at as this enables this method to determine which members are effected by the changed
     * location of the member.
     * @param d the drawable from which to recalculate.
     * @param invalidRgn the invalidRgn that the DirectGroup should redraw
     * @throws quicktime.QTException
     */
    @Override
    public synchronized void redrawMember(QTDrawable d, Region invalidRgn) throws QTException {
        if (d != null) {
            GroupMember localGroupMember1 = null;
            try {
                localGroupMember1 = memberChanged(d);
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
                        Region localRegion2 = localGroupMember2.drawer.getClip().sect(invalidRgn);
                        if (!localRegion2.empty()) {
                            localGroupMember2.drawer.redraw(localRegion2);
                        }
                    }
                    if ((i == 0) && (localGroupMember2.drawer.equals(d))) {
                        localGroupMember2.drawer.redraw(null);
                        i = 1;
                        if (invalidRgn != null) {
                            invalidRgn = invalidRgn.union(localGroupMember1.clipRegion);
                        } else
                            invalidRgn = localGroupMember1.clipRegion;
                    }
                }
                Region localRegion1 = invalidRgn.sect(this.eraseRegion);
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
     * @since QTJava 6.1
     * This method allows you to set a Transformable's clipping region.
     * @param theClip a Region that defines the new clipping region.
     * @throws quicktime.QTException
     */
    @Override
    public void setClip(Region theClip) throws QTException {
        this.clip = (theClip == null ? new Region(getDisplayBounds()) : theClip.sect(getDisplayBounds()));
        Region localRegion = this.clip;
        GroupMember localObject = null;
        this.doMemCleanup = true;
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember localGroupMember = (GroupMember) this.vec.elementAt(i);
            if (localObject != null)
                localRegion = localRegion.diff(drawersRegion(localObject));
            localGroupMember.drawer.setClip(localRegion);
            localObject = localGroupMember;
        }
        if (localObject != null) {
            this.eraseRegion = localRegion.diff(drawersRegion(localObject));
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
        switch(i) {
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
            paramGroupMember.clipRegion = (j != 0 ? createRotatedRegion(localQTDrawable) : new Region(localQTDrawable.getDisplayBounds()));
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
     * @since QTJava 6.1
     * This tells the group that the position or display size of the
     * drawer has changed and it should recalculate its alignment and clipping
     * based on the new position.
     * If the drawer is not a member of the group then null is returned, if
     * it is a member then a GroupMember object is returned that contains the layout
     * characteristics of the drawer.
     * @param d the drawer that has changed
     * @return the GroupMember object that is associated with that drawer.
     * @throws quicktime.QTException
     */
    @Override
    public GroupMember memberChanged(QTDrawable d) throws QTException {
        GroupMember localGroupMember = getGroupMember(d);
        if (localGroupMember != null) {
            setClips(d);
        }
        return localGroupMember;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will reset an item's alignment and redraw the group.
     * @param d the drawer to reposition the alignment
     * @param xAlign the new x alignment value for the member
     * @param yAlign the new y alignemen value for the member
     * @return true if the drawer is a member of the group
     * @throws quicktime.QTException
     */
    @Override
    public boolean setMemberAlignment(QTDrawable d, float xAlign, float yAlign) throws QTException {
        boolean bool = super.setMemberAlignment(d, xAlign, yAlign);
        if (bool) {
            setClips(d);
            redraw(null);
        }
        return bool;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This sets the layer of the incoming drawable to the new layer.
     * It returns true if the drawable is a member of the group and
     * was reset, false otherwise.
     * @param layer the new layer
     * @return true if d is a member of the group
     * @throws quicktime.QTException
     */
    @Override
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
        GroupMember localObject = null;
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember localGroupMember = (GroupMember) this.vec.elementAt(i);
            if (localObject != null)
                localRegion = localRegion.diff(localObject.clipRegion);
            localObject = localGroupMember;
            if (localObject.drawer.equals(paramQTDrawable)) {
                bool = true;
                this.doMemCleanup = true;
                localObject.clipRegion = null;
                this.redoCachedMembersClip = true;
                for (i++; i < this.vec.size(); i++) {
                    localGroupMember = (GroupMember) this.vec.elementAt(i);
                    localRegion = localRegion.diff(drawersRegion(localObject));
                    localGroupMember.drawer.setClip(localRegion);
                    localObject = localGroupMember;
                }
            }
        }
        if (localObject != null) {
            this.eraseRegion = localRegion.diff(drawersRegion(localObject));
        }
        this.redoCachedMembersClip = false;
        if (!this.doMemCleanup) {
            QTUtils.reclaimMemory();
        }
        return bool;
    }
}

