/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import duke.ControlPanel;
import duke.MovieScreen;
import duke.TumbleItem;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTFactory;


/**
 * Moving Movie Demo.
 */
public class DukeMovie extends Frame {
//_________________________ CLASS VARIABLES
	private static final Color bgColor = Color.lightGray; //tumbling duke gif's are light gray

//_________________________ CLASS METHODS
	public static void main(String args[]) {
		try {
			QTSession.open(); //start a QuickTime session
			Frame f = new DukeMovie("Biscotti = Java + QuickTime");
                        f.pack();
			f.setVisible(true);
			f.toFront();
		}
		catch (Exception e) { 
			QTSession.close(); 
			System.exit(0); 
		}
	}

	DukeMovie (String frameTitle) throws QTException, IOException {
		super (frameTitle);

		setBackground(bgColor);
		setLayout(new BorderLayout());
                
		ms = new MovieScreen();
		Panel panel = new Panel();
                panel.add((Component)ms.getMovieComponent());
                add("Center", panel);
		
		// create, add and start tumbling duke, if the images are available
		try {
			File file = QTFactory.findAbsolutePath ("duke");		
			duke = new TumbleItem(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (duke != null) {
			duke.setBackground(bgColor);
			add("North", duke);
		}
		
		// create our little control panel out of simple AWT objects
		cp = new ControlPanel(bgColor, ms, this);
		add("South", cp);

		//setBounds(0, 0, 625, 420);
		addWindowListener(new WindowAdapter () {
			public void windowOpened (WindowEvent ev) {
				if (duke != null) duke.start();
			}

			public void windowClosing (WindowEvent e) {
				if (duke != null) duke.stop();
				QTSession.close();
				dispose();
			}

			public void windowClosed (WindowEvent e) { 
				System.exit(0);
			}
		});
	}

//_________________________ INSTANCE VARIABLES
	private MovieScreen ms;
	private TumbleItem  duke;
	private ControlPanel cp;
}
