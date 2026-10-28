package quicktime.app.display;

public interface DrawingNotifier {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by some kind of container object when the Listener
     * is added to the object that is the source of the interest.
     */
    void addDrawingListener(DrawingListener paramDrawingListener);

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the specified object when the Listener object
     * is removed from the object that is the source of the interest.
     */
    void removeDrawingListener(DrawingListener paramDrawingListener);
}
