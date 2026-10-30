package quicktime.app.display;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Objects;
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
 * @deprecated
 */
public abstract class GroupDrawable extends SimpleSpace implements QTDisplaySpace {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Provided for subclasses to create a group drawable.
     * @param initialSize initial size of the Drawable object
     * @param bc the background color
     * @param scale the scale of the Space's Timer
     * @param period the period of the Space's Timer.
     * @param p the Protocol that is enforced on candidate member objects of this group
     * @deprecated
     * @since QTJava 4.0
     */
    protected GroupDrawable(Dimension initialSize, QDColor bc, int scale, int period, Protocol p) throws QTException {
        this(new QDDimension(initialSize.width, initialSize.height), bc, scale, period, p);
    }

    /**
     * Provided for subclasses to create a group drawable.
     * @param initialSize initial size of the Drawable object
     * @param bc the background color
     * @param scale the scale of the Space's Timer
     * @param period the period of the Space's Timer.
     * @param p the Protocol that is enforced on candidate member objects of this group
     * @deprecated
     * @since QTJava 6.1
     */
    protected GroupDrawable(QDDimension initialSize, QDColor bc, int scale, int period, Protocol p) throws QTException {
        super(p, scale, period);
        this.displayBounds = new QDRect(initialSize);
        this.initSize = initialSize;
        this.eraseRegion = new Region(this.displayBounds);
        this.clip = this.eraseRegion;
        setBackgroundColor(bc);
    }

    QDGraphics port = QDGraphics.scratch;

    QDRect displayBounds;

    boolean visible = false;

    QDDimension initSize;

    Region eraseRegion;

    QDColor bgColor;

    /**
     * The current clip region of the group * @deprecated since QTJava 6.1
     * @deprecated
     */
    protected Region clip;

    /**
     * This vector is used to store the members in. It is kept sorted by
     * layer with the frontmost member in the first slot of the vector.
     * @deprecated
     * @since QTJava 6.1
     */
    protected final Vector<Object> vec = new Vector<>(8);

    /**
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @throws quicktime.std.StdQTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public QDGraphics getGWorld() throws StdQTException {
        return this.port;
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
        this.port = cgp;
        boolean bool = this.visible;
        this.visible = (!QDGraphics.scratch.equals(cgp));
    }

    /**
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void setLocation(int x, int y) throws QTException {
        this.displayBounds.setX(x);
        this.displayBounds.setY(y);
        setDisplayBounds(this.displayBounds);
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
        this.displayBounds = bounds;
    }

    /**
     * This method is called automatically via the QTCanvas object associated with
     * this client to set the boundary of the movie.
     *
     * @return the display boundary of the client
     * @throws quicktime.std.StdQTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public QDRect getDisplayBounds() throws StdQTException {
        return this.displayBounds;
    }

    /**
     * Returns the current background color or null if no background colour.
     * @deprecated
     * @since QTJava 6.1
     */
    public QDColor getBackgroundColor() {
        return this.bgColor;
    }

    /**
     * Sets the background color. This will force a redraw of the Compositor.
     * @deprecated
     * @since QTJava 6.1
     */
    public void setBackgroundColor(QDColor backgroundColor) throws QTException {
        this.bgColor = Objects.requireNonNullElseGet(backgroundColor, () -> this.port.getBackColor());
        if (this.visible) {
            redraw(null);
        }
    }

    /**
     * Remove a QTDrawable object from the Group of drawn items.
     * @param member the object to remove from the collection
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public synchronized void removeMember(Object member) throws QTException {
        for (int i = 0; i < this.vec.size(); i++) {
            GroupMember groupMember = (GroupMember) this.vec.elementAt(i);
            QTDrawable qtDrawable = groupMember.drawer;
            if (qtDrawable.equals(member)) {
                qtDrawable.setGWorld(QDGraphics.scratch);
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
     * Returns an iterator of QTDrawable members that are the items that are
     * currently contained within the DirectGroup.
     * @return an Enumeration
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public synchronized Enumeration members() {
        Vector<Object> vector = new Vector<>();
        Enumeration<?> enumeration = this.vec.elements();
        while (enumeration.hasMoreElements()) {
            GroupMember groupMember = (GroupMember) enumeration.nextElement();
            vector.addElement(groupMember.drawer);
        }
        return vector.elements();
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
     * Returns a GroupMember that contains the specified QTDrawable object or null
     * if the requested item is not a member of this group.
     * @return a GroupMember that contains the layer and alignment information for the member in this group
     * @deprecated
     * @since QTJava 6.1
     */
    public GroupMember getGroupMember(Object drawable) {
        Enumeration<?> enumeration = this.vec.elements();
        while (enumeration.hasMoreElements()) {
            GroupMember groupMember = (GroupMember) enumeration.nextElement();
            if (groupMember.drawer.equals(drawable))
                return groupMember;
        }
        return null;
    }

    /**
     * Returns the number of members in the collection, ie. the collection's size.
     * @return the size of the collection.
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public int size() {
        return this.vec.size();
    }

    /**
     * This will reset an item's alignment and redraw the group.
     * Internally the group uses alignment values to recall where an
     * item is in relation to the other members and the group's display space.
     * Upon resize of the group's display space items are layed out in accordance
     * of their alignment settings.
     * @param d the drawer to reposition the alignment
     * @param xAlign the new x alignment value for the member
     * @param yAlign the new y alignment value for the member
     * @return true if the drawer is a member of the group
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public boolean setMemberAlignment(QTDrawable d, float xAlign, float yAlign) throws QTException {
        GroupMember groupMember = getGroupMember(d);
        if (groupMember != null) {
            setAlignedMemberLocation(groupMember.drawer, xAlign, yAlign);
        }
        return groupMember != null;
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
    public boolean setMemberLayer(QTDrawable qtDrawable, int layer) throws QTException {
        if (!isAppropriate(qtDrawable))
            return false;
        if (!hasMember(qtDrawable))
            return false;
        GroupMember groupMember1 = null;
        for (int i = 0; i < this.vec.size(); i++) {
            groupMember1 = (GroupMember) this.vec.elementAt(i);
            if (groupMember1.drawer.equals(qtDrawable)) {
                this.vec.removeElementAt(i);
                break;
            }
            groupMember1 = null;
        }
        if (groupMember1 != null) {
            int i = 0;
            groupMember1.layer = layer;
            for (int j = 0; j < this.vec.size(); j++) {
                GroupMember groupMember2 = (GroupMember) this.vec.elementAt(j);
                if (layer <= groupMember2.layer) {
                    i = 1;
                    this.vec.insertElementAt(groupMember1, j);
                    break;
                }
            }
            if (i == 0) {
                this.vec.addElement(groupMember1);
            }
        }
        return groupMember1 != null;
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
        GroupMember groupMember = (GroupMember) this.vec.elementAt(this.vec.size() - 1);
        return groupMember.getLayer();
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
        GroupMember groupMember = (GroupMember) this.vec.elementAt(0);
        return groupMember.getLayer();
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
    public synchronized void redraw(Region invalidRgn) throws QTException {
        if (!this.eraseRegion.empty()) {
            this.port.setBackColor(this.bgColor);
            this.port.eraseRgn(this.eraseRegion);
        }
        for (int i = this.vec.size() - 1; i >= 0; i--) {
            ((GroupMember) this.vec.elementAt(i)).drawer.redraw(invalidRgn);
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
    public Layerable getLayerable(Object member) {
        return getGroupMember(member);
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
    public Transformable getTransformable(Object member) {
        if (hasMember(member) == true)
            return (Transformable) member;
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
    public Object hitTest(int x, int y) throws QTException {
        if (this.vec != null) {
            if (!this.displayBounds.inside(x, y))
                return null;
            for (int i = 0; i < this.vec.size(); i++) {
                GroupMember groupMember = (GroupMember) this.vec.elementAt(i);
                if (groupMember.drawer.getDisplayBounds().inside(x, y))
                    return groupMember.drawer;
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
        Matrix matrix = new Matrix();
        matrix.setTx(this.displayBounds.getX());
        matrix.setTy(this.displayBounds.getY());
        return matrix;
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
    public void setMatrix(Matrix matrix) throws QTException {
        int i = (int) (matrix.getTx() + 0.5F);
        int j = (int) (matrix.getTy() + 0.5F);
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

    final synchronized void _addQTDrawable(GroupMember groupMember) throws QTException {
        QDRect displayBounds = getDisplayBounds();
        int i = 0;
        synchronized (this.vec) {
            for (int j = 0; j < this.vec.size(); j++) {
                GroupMember groupMember2 = (GroupMember) this.vec.elementAt(j);
                if (groupMember.drawer.equals(groupMember2.drawer))
                    return;
                if (groupMember.layer <= groupMember2.layer) {
                    this.vec.insertElementAt(groupMember, j);
                    i = 1;
                    break;
                }
            }
            if (i == 0) {
                this.vec.addElement(groupMember);
            }
        }
        memberAdded(groupMember.drawer);
        if (this.visible)
            setClip(null);
        groupMember.drawer.setGWorld(getGWorld());
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
    protected void setAlignedMemberLocation(QTDrawable drawer, float xAlign, float yAlign) throws QTException {
        if (xAlign < 0.0F) {
            xAlign = 1.0F;
        } else if (xAlign > 1.0F)
            xAlign = 1.0F;
        if (yAlign < 0.0F) {
            yAlign = 1.0F;
        } else if (yAlign > 1.0F) {
            yAlign = 1.0F;
        }
        QDRect displayBounds = getDisplayBounds();
        int i = displayBounds.getX();
        int j = displayBounds.getY();
        int k = displayBounds.getWidth();
        int m = displayBounds.getHeight();
        QDRect rect2 = drawer.getDisplayBounds();
        int n = k - rect2.getWidth();
        int i1 = m - rect2.getHeight();
        if (n < 0)
            n = 0;
        if (i1 < 0) {
            i1 = 0;
        }
        int i2 = (int) (n * xAlign);
        int i3 = (int) (i1 * yAlign);
        drawer.setLocation(i + i2, j + i3);
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
    public abstract GroupMember memberChanged(QTDrawable d) throws QTException;

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
    public abstract void redrawMember(QTDrawable d, Region invalidRgn) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method allows you to set a Transformable's clipping region.
     * @param theClip a Region that defines the new clipping region.
     * @throws quicktime.QTException
     */
    @Override
    public abstract void setClip(Region theClip) throws QTException;
}
