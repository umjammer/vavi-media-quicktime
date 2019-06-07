package quicktime.app.display;

import quicktime.QTException;
import quicktime.app.image.Transformable;
import quicktime.app.spaces.Space;

/**
 * Deprecated.
 * This interface defines the minimum API that a QTDisplay space provides.
 * That is, a QTDisplaySpace is some kind of QTDrawable so can be set as a
 * client for a QTCanvas.
 *
 * As a display space is by definition a collection of objects that have some
 * kind of visual appearance, the display space must manage (or its contained objects)
 * the layering of those objects within the visual presentation of the space.
 * As such, this interface provides an API for retrieving the current front and back
 * most layers of its members, and a way of retrieving the Layerable object for
 * a member that allow the member to both get and set its Layer within the display
 * space. Also a member can be added to the display space specifying the layer at which
 * it should be added. The lower a layer (or z) value the more frontmost the object is
 * displayed at. The backmost layer of a QTDisplaySpace is Short.MAX_VALUE.
 *
 * Members of display spaces may or may not know about their position and two Dimensional
 * display matrix. As such the QTDisplaySpace also provides an API for retrieving a
 * member's Transformable interface that allows a member to get and set its display
 * matrix.
 * @see {@link quicktime.app.image.Transformable},
 * {@link quicktime.app.display.Layerable}
 */
public abstract interface QTDisplaySpace extends QTDrawable, Space {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the layer of the frontmost member in the display space.
     * @return an int
     * @return {@link quicktime.QTException}
     */
    public abstract int getFrontLayer() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the layer of the backmost member in the display space.
     * @return an int
     * @return {@link quicktime.QTException}
     */
    public abstract int getBackLayer() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the Layerable object for a member (or null if the Object is not a member)
     * that allows the application to get and set the layer of a member. This may or may
     * not be the member itself, depending both on the type of the object of the member
     * and the manner in which the QTDisplaySpace handles the Layer property of its members.
     * @param member the member
     * @return a Layerable object to interact with the display layer of a member.
     */
    public abstract Layerable getLayerable(Object paramObject);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
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
    public abstract Transformable getTransformable(Object paramObject);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds a member to the QTDisplaySpace at the specified layer. If the member is
     * not an appropriate type of object for the Space this method returns false and the
     * member is not added.
     * @param member the member to add to the Display Space
     * @param layer the layer of the member when added.
     * @return true if the member was appropriate and was added to the space, otherwise false
     * @return {@link quicktime.QTException}
     */
    public abstract boolean addMember(Object paramObject, int paramInt) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return the frontmost object that is located at the specified coordinates.
     * Hit test coordinates will take into account the location of the space
     * thus if the space is at 50, 50 and an object is located in the top,left
     * of the space then the hit test coords for the first pixel of this object
     * is 50, 50.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return an Object at that coordinate or null if no object at specified coordinate
     * @return {@link quicktime.QTException}
     */
    public abstract Object hitTest(int paramInt1, int paramInt2) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/QTDisplaySpace.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
