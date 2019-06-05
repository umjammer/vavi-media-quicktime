/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package JavaDrawing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.QTFile;
import quicktime.std.image.GraphicsImporter;


public class JavaDrawing extends Frame {
    public static void main(String args[]) {
        try {
            QTSession.open();
            JavaDrawing jd = new JavaDrawing("QT in Java");
            jd.pack();
            jd.setVisible(true);
            jd.toFront();
        } catch (Exception e) {
            e.printStackTrace();
            QTSession.close();
        }
    }

    JavaDrawing(String title) throws Exception {
        super(title);

        setBackground(Color.lightGray);

        // add a WindowListener to close the program down
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                QTSession.close();
                dispose();
            }

            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        QTFile jp = new QTFile(new File("/Users/nsano/src/vavi/java/test/146/tmp/erika.jpg"));
        GraphicsImporter qid = new GraphicsImporter(jp);

        QTComponent qtComponent = QTFactory.makeQTComponent(qid);
        add("Center", (Component) qtComponent);
    }
}
