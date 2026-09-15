package quicktime.app.display;

/**
 * Deprecated.
 * Object that implement this interface can receive notification that an object has completed rendering a frame
 */
public abstract interface DrawingListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The execute method is called by the Drawable when drawing is completed.
     * @param drawable the drawable which drawing has just been completed upon
     * @return an error code or zero if no error
     * @see <A href="../../../quicktime/app/display/package-summary.html">quicktime.app.display</A>
     */
    public abstract void drawingComplete(QTDrawable paramQTDrawable);
}

