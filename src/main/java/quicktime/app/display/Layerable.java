package quicktime.app.display;

import quicktime.QTException;

/**
 * @deprecated
 * An interface for dealing with display objects which are members of
 * QTDisplaySpaces. The use of this interface shields both the group and
 * its members from a required knowledge of layer characteristics. Thus
 * the group may use a helper object to contain and manage layers of
 * members, or it may keep the layering information internally and provide
 * a helper object that is not the member itself as the object that will
 * be able to set and get the layer information for a member
 */
public interface Layerable {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This is the Back most layer value for a group member
     */
    int kBackMostLayer = 32767;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Return the current layer of the Layerable object.
     * @return the layer
     * @throws quicktime.QTException
     */
    int getLayer() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the new layer of the Layerable object.
     * @param layer the new layer
     * @throws quicktime.QTException
     */
    void setLayer(int layer) throws QTException;
}
