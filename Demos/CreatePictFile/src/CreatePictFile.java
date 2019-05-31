/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

import java.awt.Button;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.GraphicsImporterDrawer;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.QTFile;
import quicktime.qd.OpenCPicParams;
import quicktime.qd.Pict;
import quicktime.qd.QDDrawer;
import quicktime.qd.QDGraphics;
import quicktime.std.StdQTConstants;
import quicktime.std.image.GraphicsImporter;


public class CreatePictFile extends Frame implements StdQTConstants, QDDrawer {

	Pict myPict;
	CreatePictFile pictFile;
	
	public static void main(String args[]) {		
		try {
			QTSession.open();
			CreatePictFile pictFile = new CreatePictFile();
			pictFile.setVisible(true);
			pictFile.toFront();
		} catch (Exception e) {
			e.printStackTrace();
			QTSession.close();
		}
	}
	
	CreatePictFile () throws Exception {
		super ("QT in Java");
		addWindowListener(new WindowAdapter () {
			public void windowClosed(WindowEvent inEvent) {
				System.exit(0);
			}
				
			public void windowClosing(WindowEvent inEvent) {
				QTSession.close();
				dispose();
			}
		});
					
		addNotify();
		Insets insets = getInsets();
		setBounds (0, 0, (insets.left + insets.right + 500), (insets.top + insets.bottom + 300));

		Button btn = new Button ("Click here to create a PICT File of window's contents");
		btn.addActionListener (new ActionListener () {
			public void actionPerformed (ActionEvent event) {
				File f = null;
                try {
                    f = new File("results.pict");
					myPict.writeToFile(f);							
				} catch (Exception ex) { 
					ex.printStackTrace();
				}
                System.out.println ("Done - " + f.getAbsolutePath());						
			}
		});
		
		add("North", btn);
		
		int[] fileTypes = { kQTFileTypeGIF, kQTFileTypeJPEG, kQTFileTypePicture };
            QTFile qtf = null;
            try {
                qtf = QTFile.standardGetFilePreview(fileTypes);
			}catch (quicktime.io.QTIOException ex) {
                System.out.println ("UserCanceled : Application needs media file to run. Quitting....");
                QTSession.close();
                System.exit(1);
            }

		myImageFile = new GraphicsImporterDrawer (qtf);

		QDGraphics recordingGraphics = new QDGraphics (myImageFile.getDisplayBounds());
		myImageFile.setGWorld (recordingGraphics);
		
			//Note that the last 2 Parameters to OpenCPicParams represent the resolution in DPI - the default is 72
		OpenCPicParams param = new OpenCPicParams(myImageFile.getDisplayBounds());
			
			// whenever you draw to a QDGraphics from which
			// you are creating a PICT you MUST use the beginDraw
			// method of that QDGraphics. This will set and retain
			// that graphics object as the current graphics for the duration
			// of the draw call - see below
		myPict = Pict.open (recordingGraphics, param);
		
		recordingGraphics.beginDraw (this);
		
		myPict.close();
                
                GraphicsImporter gi = new GraphicsImporter(qtf);
                QTComponent qtComponent = QTFactory.makeQTComponent(gi);
                add("Center", (Component) qtComponent);
        }
	
	GraphicsImporterDrawer myImageFile;

	public void draw (QDGraphics g) throws QTException {
		myImageFile.redraw(null);
	}
}
