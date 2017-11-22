
package quicktime.app.view;

import javax.swing.JComponent;

import quicktime.QTException;


public abstract interface QTJComponent {
    public abstract void setGraphicsImporterDrawer(GraphicsImporterDrawer paramGraphicsImporterDrawer) throws QTException;

    public abstract void setMoviePlayer(MoviePlayer paramMoviePlayer) throws QTException;

    public abstract MoviePlayer getMoviePlayer();

    public abstract GraphicsImporterDrawer getGraphicsImporterDrawer();

    public abstract JComponent asJComponent();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/view/
 * QTJComponent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
