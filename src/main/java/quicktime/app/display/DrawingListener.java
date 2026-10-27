package quicktime.app.display;

/**
 * Object that implement this interface can receive notification that an object has completed rendering a frame
 *
 * @deprecated
 */
public interface DrawingListener {

    /**
     * The execute method is called by the Drawable when drawing is completed.
     *
     * @param drawable the drawable which drawing has just been completed upon
     * @return an error code or zero if no error
     * @see <A href="../../../quicktime/app/display/package-summary.html">quicktime.app.display</A>
     * @since QTJava 6.1
     * @deprecated
     */
    void drawingComplete(QTDrawable drawable);
}

