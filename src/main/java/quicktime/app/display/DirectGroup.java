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
 * @deprecated
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
        } catch (Exception e) {
            throw new QTRuntimeException(e.getMessage());
        }
    }

    /**
     * Create a new grouped drawable where the members draw directly to the screen.
     * @param initialSize the size of the display group
     * @param bc the background color of the group
     * {@link quicktime.app.display.DirectGroup#DirectGroup(quicktime.qd.QDDimension, quicktime.qd.QDColor)}
     * @deprecated
     * @since QTJava 4.0, replaced by DirectGroup
     */
    public DirectGroup(Dimension initialSize, QDColor bc) throws QTException {
        this(new QDDimension(initialSize.width, initialSize.height), bc, 10, 1, p);
    }

    /**
     * Create a new grouped drawable where the members draw directly to the screen.
     * @param initialSize the size of the display group
     * @param bc the background color of the group
     * @deprecated
     * @since QTJava 6.1
     */
    public DirectGroup(QDDimension initialSize, QDColor bc) throws QTException {
        this(initialSize, bc, 10, 1, p);
    }

    /**
     * Create a new grouped drawable where the members draw directly to the screen.
     * @param initialSize the size of the display group
     * @param bc the background color of the group
     * @param scale the scale of the space's Timer
     * @param period the period of the space's Timer
     * @param p the default Protocol of members
     * @deprecated
     * @since QTJava 6.1
     */
    protected DirectGroup(QDDimension initialSize, QDColor bc, int scale, int period, Protocol p) throws QTException {
        super(initialSize, bc, scale, period, p);
    }

    /**
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
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public boolean isAppropriate(Object object) {
        return (getProtocol().passProtocol(object)) && (!(object instanceof GroupDrawable));
    }

    /**
     * Add a QTDrawable object to the group based on the provided alignment and layer parametres.
     * This method is provided as a convenience method to position a member in the current
     * display space of the Group.
     * @param layer the layer where the drawer will be placed
     * @param xAlign the x position of the drawable within the group - 0 is left, 1 is bounds.width - member.width
     * @param yAlign the y position of the drawable within the group - 0 is top, 1 is bounds.height - member.height
     * @return true if the member is appropriate and was added.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public boolean addMember(Object object, int layer, float xAlign, float yAlign) throws QTException {
        if (!isAppropriate(object))
            return false;
        setAlignedMemberLocation((QTDrawable) object, xAlign, yAlign);
        _addQTDrawable(new GroupMember(this, (QTDrawable) object, layer));
        return true;
    }

    /**
     * Add a QTDrawable object to the group based on the current location of the member and the specified
     * layer parameter.
     * @param layer the layer where the drawer will be placed
     * @param member the member to add to the Display Space
     * @return true if the member is appropriate and was added.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public boolean addMember(Object layer, int member) throws QTException {
        if (!isAppropriate(layer))
            return false;
        QTDrawable d = (QTDrawable) layer;
        _addQTDrawable(new GroupMember(this, d, member));
        return true;
    }

    /**
     * Add a QTDrawable object to the group with the current location of the QTDrawable.
     * If the member implements the Layerable interface it will get the layer and assign the layer appropriately.
     * Otherwise, the member will be added with a layer of 1.
     * @param member the member to add
     * @return true if the member is appropriate and was added.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public boolean addMember(Object member) throws QTException {
        int i = 1;
        if ((member instanceof Layerable))
            i = ((Layerable) member).getLayer();
        return addMember(member, i);
    }

    /**
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void setGWorld(QDGraphics cgp) throws QTException {
        super.setGWorld(cgp);
        Enumeration<?> enumeration = this.vec.elements();
        while (enumeration.hasMoreElements()) {
            GroupMember groupMember = (GroupMember) enumeration.nextElement();
            groupMember.drawer.setGWorld(this.port);
        }
        if (this.visible) {
            setClip(null);
        }
    }

    /**
     * This method is called automatically via the QTCanvas object associated with
     * this client to set its boundary.
     *
     * @param bounds a QDRect object describing the boundary
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void setDisplayBounds(QDRect bounds) throws QTException {
        super.setDisplayBounds(bounds);
        setClip(null);
    }

    /**
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
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public synchronized void redrawMember(QTDrawable d, Region invalidRgn) throws QTException {
        if (d != null) {
            GroupMember groupMember1 = null;
            try {
                groupMember1 = memberChanged(d);
            } catch (QTException e) {
                if ((e.errorCode() == 65036) || (e.errorCode() == -108)) {
                    QTUtils.reclaimMemory();
                }
                throw e;
            }
            if (groupMember1 == null) {
                redraw(null);
                return;
            }
            synchronized (QTNative.globalsLock) {
                int i = 0;
                int j = 0;
                for (int k = 0; k < this.vec.size(); k++) {
                    GroupMember groupMember2 = (GroupMember) this.vec.elementAt(k);
                    if (i != 0) {
                        Region region2 = groupMember2.drawer.getClip().sect(invalidRgn);
                        if (!region2.empty()) {
                            groupMember2.drawer.redraw(region2);
                        }
                    }
                    if ((i == 0) && (groupMember2.drawer.equals(d))) {
                        groupMember2.drawer.redraw(null);
                        i = 1;
                        if (invalidRgn != null) {
                            invalidRgn = invalidRgn.union(groupMember1.clipRegion);
                        } else
                            invalidRgn = groupMember1.clipRegion;
                    }
                }
                Region area = invalidRgn.sect(this.eraseRegion);
                if (!area.empty()) {
                    this.port.setBackColor(this.bgColor);
                    this.port.eraseRgn(area);
                }
            }
        }
        redraw(null);
    }

    /**
     * This method allows you to set a Transformable's clipping region.
     * @param theClip a Region that defines the new clipping region.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void setClip(Region theClip) throws QTException {
        this.clip = (theClip == null ? new Region(getDisplayBounds()) : theClip.sect(getDisplayBounds()));
        Region eraseRegion = this.clip;
        GroupMember groupMember = null;
        this.doMemCleanup = true;
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember groupMember2 = (GroupMember) this.vec.elementAt(i);
            if (groupMember != null)
                eraseRegion = eraseRegion.diff(drawersRegion(groupMember));
            groupMember2.drawer.setClip(eraseRegion);
            groupMember = groupMember2;
        }
        if (groupMember != null) {
            this.eraseRegion = eraseRegion.diff(drawersRegion(groupMember));
        } else {
            this.eraseRegion = eraseRegion;
        }
        if (!this.doMemCleanup)
            QTUtils.reclaimMemory();
    }

    private Region drawersRegion(GroupMember groupMember) throws QTException {
        QTDrawable qtDrawable = groupMember.drawer;
        int i = qtDrawable.getMatrix().getType();
        int j = 0;
        switch(i) {
            case 4:
            case 5:
            case 6:
                j = 1;
                break;
            case 2:
            case 3:
                Matrix matrix = qtDrawable.getMatrix();
                if ((matrix.getSx() < 0.0F) || (matrix.getSy() < 0.0F)) {
                    j = 1;
                }
                break;
        }
        if ((groupMember.clipRegion == null) || (this.redoCachedMembersClip))
            groupMember.clipRegion = (j != 0 ? createRotatedRegion(qtDrawable) : new Region(qtDrawable.getDisplayBounds()));
        return groupMember.clipRegion;
    }

    private Region createRotatedRegion(QTDrawable qtDrawable) throws QTException {
        if (this.doMemCleanup) {
            QTUtils.reclaimMemory();
            QTUtils.checkFreeMemory();
            this.doMemCleanup = false;
        }
        Matrix matrix = qtDrawable.getMatrix().copy();
        QDRect bounds = new QDRect(qtDrawable.getOriginalSize());
        matrix.transformRect(bounds);
        QDGraphics qdGraphics = new QDGraphics(1, bounds);
        qdGraphics.setBackColor(QDColor.white);
        qdGraphics.eraseRect(bounds);
        qtDrawable.setGWorld(qdGraphics);
        qtDrawable.redraw(null);
        qtDrawable.setGWorld(getGWorld());
        try {
            return new Region(qdGraphics.getPixMap());
        } catch (QTException e) {
            if ((e.errorCode() == -108) || (e.errorCode() == 65036)) {
                QTUtils.reclaimMemory();
                try {
                    return new Region(qdGraphics.getPixMap());
                } catch (QTException ex) {
                    if (ex.errorCode() == 65036) {
                        new QTHandle(512000, false);
                        return new Region(qdGraphics.getPixMap());
                    }
                }
            }
            throw e;
        }
    }

    /**
     * This tells the group that the position or display size of the
     * drawer has changed and it should recalculate its alignment and clipping
     * based on the new position.
     * If the drawer is not a member of the group then null is returned, if
     * it is a member then a GroupMember object is returned that contains the layout
     * characteristics of the drawer.
     * @param d the drawer that has changed
     * @return the GroupMember object that is associated with that drawer.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public GroupMember memberChanged(QTDrawable d) throws QTException {
        GroupMember groupMember = getGroupMember(d);
        if (groupMember != null) {
            setClips(d);
        }
        return groupMember;
    }

    /**
     * This will reset an item's alignment and redraw the group.
     * @param d the drawer to reposition the alignment
     * @param xAlign the new x alignment value for the member
     * @param yAlign the new y alignemen value for the member
     * @return true if the drawer is a member of the group
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
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
     * This sets the layer of the incoming drawable to the new layer.
     * It returns true if the drawable is a member of the group and
     * was reset, false otherwise.
     * @param layer the new layer
     * @return true if d is a member of the group
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public boolean setMemberLayer(QTDrawable qtDrawable, int layer) throws QTException {
        boolean bool = super.setMemberLayer(qtDrawable, layer);
        if (bool) {
            this.redoCachedMembersClip = true;
            setClip(null);
            this.redoCachedMembersClip = false;
            redraw(null);
        }
        return bool;
    }

    private boolean redoCachedMembersClip = false;

    private boolean setClips(QTDrawable clips) throws QTException {
        boolean bool = false;
        Region region = this.clip;
        GroupMember groupMember = null;
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember groupMember2 = (GroupMember) this.vec.elementAt(i);
            if (groupMember != null)
                region = region.diff(groupMember.clipRegion);
            groupMember = groupMember2;
            if (groupMember.drawer.equals(clips)) {
                bool = true;
                this.doMemCleanup = true;
                groupMember.clipRegion = null;
                this.redoCachedMembersClip = true;
                for (i++; i < this.vec.size(); i++) {
                    groupMember2 = (GroupMember) this.vec.elementAt(i);
                    region = region.diff(drawersRegion(groupMember));
                    groupMember2.drawer.setClip(region);
                    groupMember = groupMember2;
                }
            }
        }
        if (groupMember != null) {
            this.eraseRegion = region.diff(drawersRegion(groupMember));
        }
        this.redoCachedMembersClip = false;
        if (!this.doMemCleanup) {
            QTUtils.reclaimMemory();
        }
        return bool;
    }
}
