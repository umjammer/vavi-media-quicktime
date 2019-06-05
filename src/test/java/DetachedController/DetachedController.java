/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

import java.awt.Component;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;


public class DetachedController extends Frame implements StdQTConstants{
	public static void main(String args[]) {
		try {
			QTSession.open();
			QTFile file = QTFile.standardGetFilePreview(new int[] { kQTFileTypeMovie });
			new DetachedController(Movie.fromFile(OpenMovieFile.asRead(file)));
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	
	
	DetachedController(Movie mMovie) throws QTException{
		super ("QT in Java");
		
			// Create the movie and its player - detach movie from controller		
		addController(mMovie);			

			// Create the movie controller and the detached canvas.							
		addMovie(mMovie);
		
		add (new Label("DETACHED CONTROLLER"), "Center");
		
		addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				QTSession.close();
				dispose();
			}
								
			public void windowClosed (WindowEvent e) { 
				System.exit(0);
			}
		});
		
		pack();
		setVisible(true);		
		toFront();
	}
		
	void addController(Movie mMovie) throws QTException {
		
		MovieController mController = new MovieController(mMovie, mcScaleMovieToFit);
		
		//detach the movie from its controller
		mController.setAttached(false);
		
                QTComponent mComponent = QTFactory.makeQTComponent(mController);
		
                add((Component)mComponent, "North");
	}
        
	void addMovie(Movie mMovie) throws QTException{	
					
                QTComponent mComponent = QTFactory.makeQTComponent(mMovie);
                                        
		add((Component)mComponent, "South");					
	}
    
    //workaround for java 1.3 bug - throws IllegalStateException on quit
    public java.awt.im.InputContext getInputContext() {
        return null;
    }
}
