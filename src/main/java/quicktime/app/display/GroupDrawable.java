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
 * This forms the base class for grouping QTDrawable objects within a single
 * display space that is controlled by this group.
 *
 * QTDrawable are
 * drawn in their layer order (Short.minimum value is frontmost, Short.maximum value is furthest back).
 * You can change a position or size of a member by interacting directly with the QTDrawable
 * member itself but you must notify the group of any changes in the display characteristics
 * of the QTDrawable by calling memberChanged().
 *
 * QTDrawable members can also be positioned within a GroupDrawable using alignment values.
 * An alignment of 0 will place the member at the top and/or left position. An alignment
 * value of 1 will place the member so that it's bottom and/or right is aligned with
 * the bottom of the GroupDrawable's display space.
 *
 * To reset the layer of an object you use the QTDrawable's Layerable object. QTDrawable objects do not
 * contain any sense of layering, so the GroupDrawable uses the GroupMember objects to contain the information
 * about a member's layer characteristics.
 *
 * @see quicktime.app.display.DirectGroup
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
     * <I>since QTJava 4.0</I>
     * Provided for subclasses to create a group drawable.
     * @param initialSize initial size of the Drawable object
     * @param bc the background color
     * @param scale the scale of the Space's Timer
     * @param period the period of the Space's Timer.
     * @param p the Protocol that is enforced on candidate member objects of this group
     */
    protected GroupDrawable(Dimension paramDimension, QDColor paramQDColor, int paramInt1, int paramInt2, Protocol paramProtocol) throws QTException {
        this(new QDDimension(paramDimension.width, paramDimension.height), paramQDColor, paramInt1, paramInt2, paramProtocol);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Provided for subclasses to create a group drawable.
     * @param initialSize initial size of the Drawable object
     * @param bc the background color
     * @param scale the scale of the Space's Timer
     * @param period the period of the Space's Timer.
     * @param p the Protocol that is enforced on candidate member objects of this group
     */
    protected GroupDrawable(QDDimension paramQDDimension, QDColor paramQDColor, int paramInt1, int paramInt2, Protocol paramProtocol) throws QTException {
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

    /**
     * @deprecated
     * The current clip region of the group * @deprecated since QTJava 6.1
     */
    protected Region clip;

    /**
     * @deprecated
     * @since QTJava 6.1
     * THis vector is used to store the members in. It is kept sorted by
     * layer with the frontmost member in the first slot of the vector.
     */
    protected final Vector<Object> vec = new Vector<>(8);

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @throws quicktime.std.StdQTException
     */
    @Override
    public QDGraphics getGWorld() throws StdQTException {
        return this.port;
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
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.port = paramQDGraphics;
        boolean bool = this.visible;
        this.visible = (!QDGraphics.scratch.equals(paramQDGraphics));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @throws quicktime.QTException
     */
    @Override
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        this.displayBounds.setX(paramInt1);
        this.displayBounds.setY(paramInt2);
        setDisplayBounds(this.displayBounds);
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
    public void setDisplayBounds(QDRect paramQDRect) throws QTException {
        this.displayBounds = paramQDRect;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called automatically via the QTCanvas object associated with
     * this client to set the boundary of the movie.
     *
     * @return the display boundary of the client
     * @throws quicktime.std.StdQTException
     */
    @Override
    public QDRect getDisplayBounds() throws StdQTException {
        return this.displayBounds;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current background colour or null if no background colour.
     */
    public QDColor getBackgroundColor() {
        return this.bgColor;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the background color. This will force a redraw of the Compositor.
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
     * @since QTJava 6.1
     * Remove a QTDrawable object from the Group of drawn items.
     * @param member the object to remove from the collection
     * @throws quicktime.QTException
     */
    @Override
    public synchronized void removeMember(Object member) throws QTException {
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember localGroupMember = (GroupMember) this.vec.elementAt(i);
            QTDrawable localQTDrawable = localGroupMember.drawer;
            if (localQTDrawable.equals(member)) {
                localQTDrawable.setGWorld(QDGraphics.scratch);
                this.vec.removeElementAt(i);
                memberRemoved(member);
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
     * @since QTJava 6.1
     * Returns an iterator of QTDrawable members that are the items that are
     * currently contained within the DirectGroup.
     * @return an Enumeration
     */
    @Override
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
     * @since QTJava 6.1
     * Returns true if the space does not have any members.
     * @return a boolean
     */
    @Override
    public boolean isEmpty() {
        return this.vec.isEmpty();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns a GroupMember that contains the specified QTDrawable object or null
     * if the requested item is not a member of this group.
     * @return a GroupMember that contains the layer and alignment information for the member in this group
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
     * @since QTJava 6.1
     * Returns the number of members in the collection, ie. the collection's size.
     * @return the size of the collection.
     */
    @Override
    public int size() {
        return this.vec.size();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will reset an item's alignment and redraw the group.
     * Internally the group uses alignment values to recall where an
     * item is in relation to the other members and the group's display space.
     * Upon resize of the group's display space items are layed out in accordance
     * of their alignment settings.
     * @param d the drawer to reposition the alignment
     * @param xAlign the new x alignment value for the member
     * @param yAlign the new y alignemen value for the member
     * @return true if the drawer is a member of the group
     * @throws quicktime.QTException
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
     * @since QTJava 6.1
     * This sets the layer of the incoming drawable to the new layer.
     * It returns true if the drawable is a member of the group and
     * was reset, false otherwise.
     * @param layer the new layer
     * @return true if d is a member of the group
     * @throws quicktime.QTException
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
     * @since QTJava 6.1
     * Returns the back most layer of any object in the group.
     * @return the current back most layer
     */
    @Override
    public int getBackLayer() {
        if (this.vec.isEmpty())
            return -1;
        GroupMember localGroupMember = (GroupMember) this.vec.elementAt(this.vec.size() - 1);
        return localGroupMember.getLayer();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current front most layer of any object in the group.
     * @return the current front most layer
     */
    @Override
    public int getFrontLayer() {
        if (this.vec.isEmpty())
            return -1;
        GroupMember localGroupMember = (GroupMember) this.vec.elementAt(0);
        return localGroupMember.getLayer();
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.initSize.getWidth(), this.initSize.getHeight());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size of the GroupDrawable
     * @throws quicktime.QTException
     */
    @Override
    public QDDimension getOriginalSize() throws QTException {
        return this.initSize.copy();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @throws quicktime.QTException
     */
    @Override
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
     * @since QTJava 6.1
     * Returns the Layerable object for a member (or null if the Object is not a member)
     * that allows the application to get and set the layer of a member. This may or may
     * not be the member itself, depending both on the type of the object of the member
     * and the manner in which the QTDisplaySpace handles the Layer property of its members.
     * @param member the member
     * @return a Layerable object to interact with the display layer of a member.
     */
    @Override
    public Layerable getLayerable(Object paramObject) {
        return getGroupMember(paramObject);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the Transformable object that represents the member in the DisplaySpace.
     * The Transformable object allows the application to get and set the TwoD display
     * characteristics of a member (position, size, rotation, skewing, perspective).
     * If the Object is not a member of the Space this returns null. This may or may not
     * return the member itself, based on the type of the member and how the Space deals
     * with the two dimensional placement and display of its members.
     * @param member the member of Space for which the Transformable object should be returned.
     * @return a Transformable object that allows the application to manipulate the two d
     *  display characteristics of the member or null if the object is not a member.
     */
    @Override
    public Transformable getTransformable(Object paramObject) {
        if (hasMember(paramObject) == true)
            return (Transformable) paramObject;
        return null;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the front most object located at the specified x and y
     * pixel location or null if there is no object at the location
     * @param x the x pixel to test
     * @param y the y pixel to test
     * @return front most object or null
     * @throws quicktime.QTException
     */
    @Override
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
     * @since QTJava 6.1
     * This will return the client's current display transformations.
     * A GroupDrawable object does not by default support any transformation
     * but location.
     *
     * @return the display transformations of the client
     * @throws quicktime.std.StdQTException
     */
    @Override
    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        localMatrix.setTx(this.displayBounds.getX());
        localMatrix.setTy(this.displayBounds.getY());
        return localMatrix;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     * A GroupDrawable object does not by default support any transformation
     * but location.
     * @param matrix the new location
     * @throws quicktime.QTException
     */
    @Override
    public void setMatrix(Matrix paramMatrix) throws QTException {
        int i = (int) (paramMatrix.getTx() + 0.5F);
        int j = (int) (paramMatrix.getTy() + 0.5F);
        setDisplayBounds(new QDRect(i, j, this.displayBounds.getWidth(), this.displayBounds.getHeight()));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method allows you to get the current clipped Region of the Transformable.
     * @return the clipping region
     * @throws quicktime.QTException
     */
    @Override
    public Region getClip() throws QTException {
        return this.clip;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the clip that contains any space within
     * the GroupDrawable that is not drawn upon by one of the members.
     * This region is contains the pixels that will be filled in
     * with the background colour.
     * @return the background region.
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
     * @since QTJava 6.1
     * A helper method that is used to set the
     * location of the member based on the x and y alignment.
     * The alignment values must be between 0 and 1.
     * @param drawer The drawer to locate based on the current size of the GroupDrawable and the alignment values
     * @param xAlign the x alignment
     * @param yAlign the y alignment
     * @throws quicktime.QTException
     */
    protected void setAlignedMemberLocation(QTDrawable paramQTDrawable, float paramFloat1, float paramFloat2) throws QTException {
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
     * @since QTJava 6.1
     * This tells the group that the position or display size of the
     * drawer has changed and it should do whatever actions are appropriate
     * to reestablish the display integrity of the group.
     * If the drawer is not a member of the group then null should be returned, if
     * it is a member then a GroupMember object is returned that contains the layout
     * characteristics of the drawer.
     * @param d the drawer that has changed
     * @return the GroupMember object that is associated with that drawer.
     * @throws quicktime.QTException
     */
    public abstract GroupMember memberChanged(QTDrawable paramQTDrawable) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * An optimized redraw that will redraw only those objects that it needs to
     * based on the fact that the supplied drawer has changed but those behind or in front
     * of it have not.
     * @param d the member of which some display characteristic has changed.
     * @param invalidRgn the Region that has changed as a result of the changes in the specified
     *  member. Generally this Region should encompass the area of the Group's display space that
     *  was occupied before the member was changed as this call will result in the member
     *  itself being redrawn.
     * @throws quicktime.QTException
     */
    public abstract void redrawMember(QTDrawable paramQTDrawable, Region paramRegion) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method allows you to set a Transformable's clipping region.
     * @param theClip a Region that defines the new clipping region.
     * @throws quicktime.QTException
     */
    @Override
    public abstract void setClip(Region paramRegion) throws QTException;
}
