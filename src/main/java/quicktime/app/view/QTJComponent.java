package quicktime.app.view;

import javax.swing.JComponent;
import quicktime.QTException;

/**
 * This interface is implemented by the underlying QTJava JComponents . These are convenience methods that are invoked by applications to
 * set and get the current movie or image displayed. To remove pass a null parameter to the set method .
 */
public abstract interface QTJComponent {

    /**
     * Sets the current image for this QTJComponent to the specified image represented by the GraphicsImporterDrawer object.
     * @param img the GraphicsImporterDrawer object representing the image that this component displays. If the parameter is null the
     *  current image is removed.
     * @param {@link quicktime.QTException}
     */
    public abstract void setGraphicsImporterDrawer(GraphicsImporterDrawer paramGraphicsImporterDrawer) throws QTException;

    /**
     * Sets the current movie for this QTJComponent to the specified movie represented by the MoviePlayer object.
     * @param movPlayer the MoviePlayer object representing the movie that this component displays. If the parameter is null the
     *  current movie is removed.
     * @param {@link quicktime.QTException}
     */
    public abstract void setMoviePlayer(MoviePlayer paramMoviePlayer) throws QTException;

    /**
     * Gets the currently displayed MoviePlayer object of this component.
     * @return the current MoviePlayer or null if component has no MoviePlayer.
     */
    public abstract MoviePlayer getMoviePlayer();

    /**
     * Gets the currently displayed GraphicsImporterDrawer object of this component.
     * @return the GraphicsImporter object or null if no image is displayed.
     */
    public abstract GraphicsImporterDrawer getGraphicsImporterDrawer();

    /**
     * Returns the Swing component representation of the underlying object.
     * @return the JComponent
     */
    public abstract JComponent asJComponent();
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/view/
 * QTJComponent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
