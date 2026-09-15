package quicktime.app.display;

public abstract interface DrawingNotifier {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by some kind of container object when the Listener
     * is added to the object that is the source of the interest.
     */
    public abstract void addDrawingListener(DrawingListener paramDrawingListener);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the Listener object
     * is removed from the object that is the source of the interest.
     */
    public abstract void removeDrawingListener(DrawingListener paramDrawingListener);
}

