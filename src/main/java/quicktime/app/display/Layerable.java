package quicktime.app.display;

import quicktime.QTException;

/**
 * Deprecated.
 * An interface for dealing with display objects which are members of
 * QTDisplaySpaces. The use of this interface shields both the group and
 * its members from a required knowledge of layer characteristics. Thus
 * the group may use a helper object to contain and manage layers of
 * members, or it may keep the layering information internally and provide
 * a helper object that is not the member itself as the object that will
 * be able to set and get the layer information for a member
 */
public abstract interface Layerable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This is the Back most layer value for a group member
     */
    public static final int kBackMostLayer = 32767;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return the current layer of the Layerable object.
     * @return the layer
     * @return {@link quicktime.QTException}
     */
    public abstract int getLayer() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set the new layer of the Layerable object.
     * @param layer the new layer
     * @param {@link quicktime.QTException}
     */
    public abstract void setLayer(int paramInt) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/Layerable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
