/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package ImageProducing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ImageProducing.ip.ControlPanel;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.MoviePlayer;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTImageProducer;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.QDConstants;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieDrawingComplete;


/**
 * Draws images that are produced by the QTImageProducer that uses a MoviePlayer
 * as the source
 */
public class ImageProducing extends JFrame implements MovieDrawingComplete, QDConstants, StdQTConstants {

    static void main(String[] args) {
        try {
            QTSession.open();
            QTFile f1 = new QTFile(QTFactory.findAbsolutePath("jumps.mov").getPath());
            ImageProducing pm = new ImageProducing(f1);
            pm.pack();
            pm.setVisible(true);
            pm.toFront();
        } catch (Exception e) {
            e.printStackTrace();
            QTSession.close();
        }
    }

    ImageProducing(QTFile movFile) throws QTException {
        super("Consumer");

        OpenMovieFile openMovieFile = OpenMovieFile.asRead(movFile);
        myMovie = Movie.fromFile(openMovieFile);
        myMovie.getTimeBase().setFlags(loopTimeBase);
        MoviePlayer moviePlayer = new MoviePlayer(myMovie);
        QDRect r = moviePlayer.getDisplayBounds();
        Dimension d = new Dimension(r.getWidth(), r.getHeight());
        imgProducer = new QTImageProducer(moviePlayer, d);

        // this tells us that the movie has redrawn and
        // we use this to redraw the QTImageProducer - which will
        // supply more pixel data to its registered consumers
        myMovie.setDrawingCompleteProc(movieDrawingCallWhenChanged, this);

        JPanel panel = new JPanel(new BorderLayout());
        getContentPane().add("Center", panel);
        panel.add("South", new ControlPanel(moviePlayer));

        IPJComponent canvas = new IPJComponent(d, imgProducer);
        panel.add("Center", canvas);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    // have to remove a callback we install
                    myMovie.removeDrawingCompleteProc();
                } catch (QTException ex) {
                }
                QTSession.close();
                dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    QTImageProducer imgProducer;

    Movie myMovie;

    @Override
    public int execute(Movie myMovie) {
        try {
            imgProducer.updateConsumers(null);
        } catch (QTException e) {
            return e.errorCode();
        }
        return 0;
    }

    static class IPJComponent extends JComponent {

        IPJComponent(Dimension prefSize, QTImageProducer ip) {
            pSize = prefSize;
            img = createImage(ip);
            prepareImage(img, this);
        }

        private final Dimension pSize;

        private final Image img;

        @Override
        public Dimension getPreferredSize() {
            return pSize;
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, pSize.width, pSize.height, this);
        }

        // stops flicker as we have no background color to erase
        @Override
        public void update(Graphics g) {
            paint(g);
        }
    }
}
