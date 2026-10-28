/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */


package PlayMovie;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import quicktime.Errors;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.media.DataRef;


public class PlayMovie extends Frame implements Errors {

    static final private String theTextMovieName = "Demos/media/Sample.mov";

    private JDialog errorDialog;

    static void main(String[] args) {
        try {
            QTSession.open();
            // make a window and show it - we only have one window/one movie at a time
            PlayMovie pm = new PlayMovie("QT in Java");
            pm.setVisible(true);
            pm.toFront();
        } catch (QTException e) {
            // at this point we close down QT if an exception is generated because it means
            // there was a problem with the initialization of QT>
            e.printStackTrace();
            QTSession.close();
        }
    }

    public PlayMovie(String title) {
        super(title);

        new FileMenu(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goAway();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        try {
            //open and display a sample movie in the project folder
            System.err.println("file://" + QTFactory.findAbsolutePath(theTextMovieName));
            createNewMovieFromURL("file://" + QTFactory.findAbsolutePath(theTextMovieName));
        } catch (IOException ioe) {
            showErrorDialog(ioe.toString());
            System.exit(0);
        }
    }

    private Movie m;
    private MovieController mc;
    private QTComponent qtc = null;

    // This will resize the window to the size of the new movie
    public void createNewMovieFromURL(String theURL) {
        try {
            // create the DataRef that contains the information about where the movie is
            DataRef urlMovie = new DataRef(theURL);

            // create the movie
            m = Movie.fromDataRef(urlMovie, StdQTConstants.newMovieActive);

            // create the movie controller
            mc = new MovieController(m);

            // create and add a QTComponent if we haven't done so yet, otherwise set qtc's movie controller
            if (qtc == null) {
                qtc = QTFactory.makeQTComponent(mc);
                add((Component) qtc);
            } else {
                qtc.setMovieController(mc);
            }

            // this will set the size of the enclosing frame to the size of the incoming movie
            pack();

        } catch (QTException err) {
            err.printStackTrace();
        }
    }

    public MovieController getMovieController() {
        return mc;
    }

    public Movie getMovie() throws QTException {
        return m;
    }

    public void goAway() {
        try {
            if (qtc != null)
                qtc.setMovieController(null);
        } catch (QTException ex) {
        }
        QTSession.close();
        System.exit(0);
    }

    void stopPlayer() {
        try {
            if (m != null)
                m.setRate(0);
        } catch (QTException err) {
            err.printStackTrace();
        }
    }

    /**
     * Displays an error dialog reporting any problems encountered
     *
     * @param theError the error string
     */
    private void showErrorDialog(String theError) {
        errorDialog = new JDialog();
        errorDialog.setModal(true);
        errorDialog.setResizable(false);
        Container c = errorDialog.getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel jl = new JLabel(theError);
        jl.setMaximumSize(new Dimension(270, 20));

        jp.add(jl);

        JPanel jp2 = new JPanel();
        jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS));
        jp2.setBorder(BorderFactory.createEmptyBorder(5, 125, 15, 10));

        JButton jb = new JButton("OK");
        jb.setMaximumSize(new Dimension(70, 20));

        jp2.add(jb);

        errorDialog.getContentPane().add(jp, "South");
        errorDialog.getContentPane().add(jp2, "South");
        jb.addActionListener(e -> errorDialog.dispose());
        errorDialog.setBounds(0, 0, 300, 100);
        errorDialog.setVisible(true);
    }
}
