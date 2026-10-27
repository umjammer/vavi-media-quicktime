/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package SoundRecord;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;


public class PlayMovie2 extends Frame implements WindowListener {

    private QTComponent qtc;

    public PlayMovie2(QTFile src) {
        super(src.getName());
        try {
            OpenMovieFile movieFile = OpenMovieFile.asRead(src);
            MovieController mc = new MovieController(Movie.fromFile(movieFile));
            movieFile.close();

            addNotify();
            Insets insets = getInsets();
            setBounds(0, 0, (insets.left + insets.right + mc.getBounds().getWidth()), (insets.top + insets.bottom + mc.getBounds().getHeight()));

            qtc = QTFactory.makeQTComponent(mc);
            add((Component) qtc);

            addWindowListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
