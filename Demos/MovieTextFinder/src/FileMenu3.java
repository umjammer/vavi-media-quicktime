/*
 * QuickTime for Java SDK Sample Code
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import quicktime.Errors;
import quicktime.QTException;
import quicktime.io.QTFile;

/**
 * This class creates a menu bar and appends a file menu that allows the user to open movie
 * files to be searched as well as quit the application. 
 */
public class FileMenu3 {
	MovieTextFinder myPlayMovie;
		
	public FileMenu3 (MovieTextFinder src) {
		this.myPlayMovie = src;
		
		// create and initialize the menu bar
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu ("File");
		
		MenuItem openMenuItem = new MenuItem ("Open...");
		
		fileMenu.add(openMenuItem);
			
		menuBar.add (fileMenu);
		myPlayMovie.setMenuBar (menuBar);
					
		openMenuItem.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent event) {
				myPlayMovie.stopPlayer();
					
				// creat a movie through the file-open dialog of QT
				try {
					QTFile qtf = QTFile.standardGetFilePreview(QTFile.kStandardQTFileTypes);
					myPlayMovie.createNewMovieFromURL ("file://" + qtf.getPath());
				} catch (QTException e) {
					if (e.errorCode() != Errors.userCanceledErr)
		 				e.printStackTrace();
		 		}
		 	}
		});
	}
}			
