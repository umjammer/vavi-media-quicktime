/*
 * quicktime.app: Sample Code for Initial Seeding
 *
 * © 2003 Copyright, Apple Computer
 * All rights reserved
 */

package Applets.QTTestApplet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Label;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.movies.Movie;

public class QTTestApplet extends Applet {	
	private boolean initDone = false;
	//this fix is for applets on Win 95/98 using 1.3 plugin. 
	//We create a lock that we would synchronize with the QTSession.open call in init() and
	//QTSession.close in destroy()  calls. this would make sure that QTSession.open() of the applet gets initialized
	//properly everytime it enters init().
	static Object lock =  new Object();
	
	public void init () {
		try {
			synchronized(QTTestApplet.lock) {
				if (QTSession.isInitialized() == false)
					QTSession.open();
			}		
			initDone = true;
		} catch (NoClassDefFoundError er) {
			add (new Label ("QTSession : Can't Find QTJava classes"), "North");
			add (new Label ("Check install and try again"), "South");
		} catch (SecurityException se) {
				// this is thrown by MRJ trying to find QTSession class
			add (new Label ("SecurityException : Can't Find QTJava classes"), "North");
			add (new Label ("Check install and try again"), "South");
		} catch (Exception e) {
				// do a dynamic test for QTException 
				//so the QTException class is not loaded unless
				// an unknown exception is thrown by the runtime
			if (e instanceof ClassNotFoundException || e instanceof java.io.FileNotFoundException) {
				add (new Label ("Can't Find QTJava classes"), "North");
				add (new Label ("Check install and try again"), "South");
			} else if (e instanceof QTException) {
				add (new Label ("Problem with QuickTime install"), "North");
				if (((QTException)e).errorCode() == -2093)
					add (new Label ("QuickTime must be installed"), "South");			
				else
					add (new Label (e.getMessage()), "South");			
			}
		}
	}
	
	// we create an inner class here so that the class loader
	// does NOT try to load QT classes until we know that 
	// everything is OK	
	public void start () {
		if (initDone)
			new DoQT();
	}	
    
	QTComponent myMovieQTCanvas;
	class DoQT {
		DoQT () {
			try {				
				setLayout (new BorderLayout());

                QTFile qtf = new QTFile (getCodeBase().getFile() + getParameter("media"));
                OpenMovieFile fis = OpenMovieFile.asRead(qtf);
                Movie mov = Movie.fromFile(fis);

                myMovieQTCanvas = QTFactory.makeQTComponent(mov);
				add ((Component)myMovieQTCanvas, "Center");	

				add (new Label ("QuickTime for Java"), "North");
				add (new Label ("Installed successfully"), "South");
			} catch (Throwable e) {
				if (e instanceof ClassNotFoundException) {
					add (new Label ("Can't Find QTJava classes"), "North");
					add (new Label ("Check install and try again"), "South");
				} else {
					System.out.println (e);
					e.printStackTrace();
				}
			}		
		}
	}
	
	public void stop () {}
	
	public void destroy () {
		synchronized(QTTestApplet.lock) {
			if (initDone)
				QTSession.close();
		}		
		initDone = false;
	}
}
