package Applets.QTSimpleApplet;
/*
 * QuickTime for Java SDK Sample Code

   Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.

 */
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.movies.Movie;

public class QTSimpleApplet extends Applet {
	private QTComponent qtc;
	//this fix is for applets on Win 98 using 1.3 plugin. 
	//We create a lock that we would synchronize with the QTSession.open call in init() and
	//QTSession.close in destroy()  calls. this would make sure that QTSession.open() of the applet gets initialized
	//properly everytime it enters init().
	static Object lock =  new Object();
	public void init () {
		try {
			// this is a workaround required by a bug in the loading
			// mechanism of applets using the JavaPlugin on Netscape on Win
			synchronized(QTSimpleApplet.lock) {
				if (QTSession.isInitialized() == false)
					QTSession.open();
			}
				// set up a QTComponent which will disply its content 
				// at its original size of smaller and centered in the space given
				// to the QTComponent when the applet is layed out
			setLayout (new BorderLayout());
			QTFile qtf = new QTFile (getCodeBase().getFile() + getParameter("file"));
                        OpenMovieFile fis = OpenMovieFile.asRead(qtf);
                        mov = Movie.fromFile(fis);
                        qtc = QTFactory.makeQTComponent(mov);
                        			
		} catch (QTException qtE) {
qtE.printStackTrace(System.err);
				throw new RuntimeException (qtE.getMessage());		
		} 
        }
        Movie mov = null;
	public void start () { 
		try { // if QT was not successfully initialized the QTComponent will be null
                    if (qtc != null) {
                        add((Component)qtc, "Center");
                        mov.setRate(1);
                    }
		} catch (QTException e) {
			e.printStackTrace();
		}
	}
	
	public void stop () { 
            try {
		if (qtc != null)
			qtc.setMovie(null);
		} catch (QTException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy () {
		synchronized(QTSimpleApplet.lock) {
			QTSession.close();
		}	
	}

    //workaround for java 1.3 bug - throws IllegalStateException on quit
    public java.awt.im.InputContext getInputContext() {
        return null;
    }
}
