/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package KeyboardController;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.QDConstants;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;


public class KeyBoardController extends Frame implements StdQTConstants, QDConstants {

    static void main(String[] args) {
        try {
            QTSession.open();

            KeyBoardController te = new KeyBoardController("KeyBoard Controller");
            te.pack();
            te.setVisible(true);
            te.toFront();
        } catch (Exception e) {
            e.printStackTrace();
            QTSession.close();
        }

    }

    private final Movie moov;

    private float savedRate = 1;

    KeyBoardController(String title) throws Exception {
        super(title);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                QTSession.close();
                dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        QTFile qtFile = new QTFile(QTFactory.findAbsolutePath("jumps.mov"));
        OpenMovieFile movieFile = OpenMovieFile.asRead(qtFile);
        moov = Movie.fromFile(movieFile);

        QTComponent qtComponent = QTFactory.makeQTComponent(moov);
        add("Center", (Component) qtComponent);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_SPACE:
                            if (moov.getRate() != 0)
                                moov.setRate(0);
                            else
                                moov.setRate(savedRate);
                            break;
                        case KeyEvent.VK_UP:
                            moov.setTimeValue(moov.getDuration());
                            break;
                        case KeyEvent.VK_DOWN:
                            moov.setTimeValue(0);
                            break;
                        case KeyEvent.VK_LEFT:
                            moov.setRate(-1);
                            savedRate = -1;
                            break;
                        case KeyEvent.VK_RIGHT:
                            moov.setRate(1);
                            savedRate = 1;
                            break;
                        default:
                            break;
                    }
                } catch (QTException ee) {
                    throw new QTRuntimeException(ee);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

        });

        moov.setRate(1);
    }
}
