/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package GraphicsExport;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import quicktime.Errors;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.image.QTImageDrawer;
import quicktime.app.view.GraphicsImporterDrawer;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTImageProducer;
import quicktime.io.IOConstants;
import quicktime.io.QTFile;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsImporter;


public class GraphicsExport extends Frame implements StdQTConstants, IOConstants, Errors {

    final Frame frame;

    static void main(String[] args) {
        try {
            QTSession.open();
            GraphicsExport graphicsExport = new GraphicsExport("Graphics Export");
            graphicsExport.pack();
            graphicsExport.setVisible(true);
            graphicsExport.toFront();
        } catch (QTException err) {
            if (err.errorCode() == userCanceledErr)
                return;
            err.printStackTrace();
            QTSession.close();
        }
    }

    GraphicsExport(String title) throws QTException {
        super(title);
        frame = this;

        openImage();

        theButton = new Button("Export to File");
        theButton.addActionListener(ae -> {
            // Java doesn't like it when we block the main event thread,
            // so use another thread to dislpay the GraphicsExporter requestSettings dialog
            new Thread(new WriteToFile(frame, gi)).start();
        });

        add(theButton, "North");

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
    }

    private QTComponent qtComponent;
    private GraphicsImporter gi;
    private QTImageDrawer qtImageDrawer;
    private final Button theButton;

    // we use a QTImageDrawer in this case as the GraphicsExporter
    // requires a the java.awt.Image drawn into a QDGraphics (offscreen)
    // from which to do the export from
    void openImage() throws QTException {

        int[] fileTypes = {kQTFileTypeGIF, kQTFileTypeJPEG, kQTFileTypePicture};
        QTFile qtfile = null;
        try {
            qtfile = QTFile.standardGetFilePreview(fileTypes);
        } catch (quicktime.io.QTIOException ex) {
            System.out.println("UserCanceled : Application needs media file to run. Quitting....");
            QTSession.close();
            System.exit(1);
        }

        // import the image into QuickTime
        GraphicsImporter myGraphicsImporter = new GraphicsImporter(qtfile);

        //Create a GraphicsImporterDrawer which uses the GraphicsImporter to draw
        //this object produces pixels for the QTImageProducer
        GraphicsImporterDrawer myDrawer = new GraphicsImporterDrawer(myGraphicsImporter);

        //Create a java.awt.Image from the pixels supplied to it by the QTImageProducer
        QDRect r = myDrawer.getDisplayBounds();

        // this is the size of the image - this will become the size of the frame
        Dimension imageSize = new Dimension(r.getWidth(), r.getHeight());
        QTImageProducer qtProducer = new QTImageProducer(myDrawer, imageSize);
        Image image = Toolkit.getDefaultToolkit().createImage(qtProducer);

        gi = new GraphicsImporter(qtfile);
        QTComponent qtComponent = QTFactory.makeQTComponent(gi);
        add("Center", (Component) qtComponent);
    }

    static class WriteToFile implements Runnable {

        final Frame parent;
        final GraphicsImporter gi;

        WriteToFile(Frame parent, GraphicsImporter gi) {
            super();
            this.parent = parent;
            this.gi = gi;
        }

        @Override
        public void run() {
            try {
                //You can use functions that return information about the image format supported by a graphics exporter.
                FileDialog saveFileDlg = new FileDialog(parent, "Export To...", FileDialog.SAVE);
                saveFileDlg.setVisible(true);
                if (saveFileDlg.getFile() == null)
                    return;
                QTFile exportFile = new QTFile(saveFileDlg.getDirectory() + saveFileDlg.getFile());

                // you know which file format you want to write "PICT"
                GraphicsExporter graphicsExporter = new GraphicsExporter(kQTFileTypePicture);

                // Set the graphics importer as the input soure of the graphics exporter 
                graphicsExporter.setInputGraphicsImporter(gi);

                //By default, the graphics exporter's suggested file type and creator will be used for any newly
                //created file. To override the suggested file type or creator, call setOutputFileType() and setOutputFileCreator() .
                graphicsExporter.setOutputFile(exportFile);        // sets the destination for image file

                // Displays a dialog for the user to configure graphics exporter settings, if applicable.
                graphicsExporter.requestSettings();

                // Do the export
                int size = graphicsExporter.doExport();
                System.out.println("done:wrote " + size + " bytes to output file.");

            } catch (QTException err) {
                err.printStackTrace();
            }
        }
    }
}
