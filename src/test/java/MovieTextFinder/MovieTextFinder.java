/*
 *	File:		MovieTextFinder.java
 *
 *	Contains:	QuickTime for Java code which demonstrates how to display
 *				and play a movie in a window.
 *
 *	Written by : QTJ Engineering
 *  Modified by: Scott Kuechle
 *
 *	Copyright:	© 1996-2000 by Apple Computer, Inc., all rights reserved.
 *
 *	Change History (most recent first):
 *
 *     <3>      2/19/01 msh		consolidated classes and cleaned up code
 *	   <2>	 	12/8/00	srk		modified for use with MovieTextFinder class
 *	   <1>      1999	???		first file
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import quicktime.Errors;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.qd.QDColor;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.SearchResult;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.FoundTextInfo;
import quicktime.std.movies.media.Media;
import quicktime.std.movies.media.MediaHandler;
import quicktime.std.movies.media.TextMediaHandler;
import quicktime.util.QTPointer;


/**
 * MovieTextFinder class
 *
 * This class contains code to display and play a quicktime movie in a JFrame.
 * First a QTCanvas is created. The QTCanvas will be use for displaying our
 * QuickTime movie. Next, a movie file is opened and a MovieController
 * object created for the movie. The MovieController object is used to control/play
 * the movie. Finally, a QTDrawable object is created for the
 * MovieController, and the QTDrawable object is set as the drawing client for the
 * QTCanvas.
 */
public class MovieTextFinder extends JFrame implements Errors {	

	JTextField searchTextField;
	JCheckBox wrapSearch;
	JCheckBox caseSensitiveSearch;
	JRadioButton searchForwardButton;
	JRadioButton searchBackwardButton;

	Movie theMovie;
        MovieController theMovieController;
        QTComponent qtComponent = null;
		
	static final private String theTextMovieName = "TextOnly.mov";
	
		/* this flag will determine which technique we
		 use to perform our search */
	static final private boolean kUseMovieSearchTextMethod = true;
	
		// current offset into search text 
	private int currentOffset = 0;
	
	private JDialog errorDialog;
	
	public static void main (String args[]) {
		try {
			QTSession.open();
			// make a window and show it - we only have one window/one movie at a time
			MovieTextFinder pm = new MovieTextFinder("QT in Java");
			pm.setVisible(true);
			pm.toFront();
		} catch (QTException e) {
			// at this point we close down QT if an exception is generated because it means
			// there was a problem with the initialization of QT>
			e.printStackTrace();
			QTSession.close ();
		}
	}

	/**
	 * MovieTextFinder constructor
	 *
	 * This class creates a QTCanvas which will be used to display QuickTime content. Next, a 
	 * movie file is opened. This movie contains a text track which we'll use to demonstrate
	 * how to search for individual text strings. Finally, we add the QTCanvas as a component
	 * to the JFrame container.
	 */
	public MovieTextFinder (String title) {
		super (title);

		new FileMenu3 (this);

		setResizable(false);

		JPanel p1 = new JPanel();
		
		p1.setLayout (new BoxLayout(p1, BoxLayout.Y_AXIS));
		p1.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

		searchForwardButton = new JRadioButton("Search Forward");
		searchForwardButton.setSelected(true);
		searchBackwardButton = new JRadioButton("Search Backward");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(searchForwardButton);
		buttonGroup.add(searchBackwardButton);
		
		JPanel radioPanel = new JPanel();
		
		radioPanel.setLayout(new BoxLayout(radioPanel,BoxLayout.Y_AXIS));
		radioPanel.add(searchForwardButton);
		radioPanel.add(searchBackwardButton);
		
		p1.add(radioPanel);

		wrapSearch = new JCheckBox("wrap search");
		caseSensitiveSearch = new JCheckBox("case sensitive");

		p1.add(wrapSearch);
		p1.add(caseSensitiveSearch);

		try {
				/* let's open and display our text movie which is
					located in our project folder */
			createNewMovieFromURL("file://" + QTFactory.findAbsolutePath (theTextMovieName));
		}
		catch (IOException ioe) {
			showErrorDialog(ioe.toString());
			System.exit(0);
		}

        Container C = getContentPane();
        C.setLayout (new FlowLayout(FlowLayout.LEFT));

		JPanel p3 = new JPanel();
        p3.setLayout (new GridLayout(5,1));
		p3.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		JLabel jl = new JLabel("  Enter Search Text");
		p3.add(jl);

		searchTextField = new JTextField(10);
		searchTextField.setMaximumSize(new Dimension(250,20));
		searchTextField.select(0,0);
		p3.add(searchTextField);

		JButton searchButton = new JButton("       Search      ");
		searchButton.setDefaultCapable(true);

		p3.add(Box.createRigidArea(new Dimension(0,20)));
		p3.add(searchButton);
		
		// trap <return>, <enter> key presses so we can perform our search
		addKeyListener( new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
					// <enter> key pressed? 
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					DoSearch();		// search for current text 
				}
			}
		} );

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DoSearch();
			}
		} );

		Container container = getContentPane();
        container.setLayout (new FlowLayout(FlowLayout.LEFT));
		container.add(p1, "West");

		try {	
			JPanel p0 = new JPanel();
			p0.setLayout (new FlowLayout(FlowLayout.LEFT));
			p0.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                        
			qtComponent = QTFactory.makeQTComponent(theMovieController);
			p0.add((Component)qtComponent);

			container.add(p0, "Center");		
		}
		catch (QTException e) {
			e.printStackTrace();
		}

		container.add(p3, "East");
		/* set focus on search text box, so user
			can type search text without having
			to first tab to it */
		searchTextField.requestFocus();

		pack();
		
		addWindowListener(new WindowAdapter () {

			public void windowClosing (WindowEvent e) {
				goAway();
			}
			
			public void windowClosed (WindowEvent e) { 
				System.exit(0);
			}
		});			
	}

	/*
	 * This internal method is called to perform a text search in the movie 
	 */
	private void DoSearch() {
		String theText = searchTextField.getText();
		try {
			
			/* set the search features */
			int searchFlags = quicktime.std.StdQTConstants.findTextUseOffset;

				/* backward search? */
			if (searchBackwardButton.isSelected())
				searchFlags |= quicktime.std.StdQTConstants.findTextReverseSearch;
				
				/* wrap search? */
			if (wrapSearch.isSelected())
				searchFlags |= quicktime.std.StdQTConstants.findTextWrapAround;
			
				/* case sensitive search? */
			if (caseSensitiveSearch.isSelected())
				searchFlags |= quicktime.std.StdQTConstants.findTextCaseSensitive;
	
			findTextInMovieTextTrack (theText, searchFlags);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	/**
	 *
	 * createNewMovieFromURL method
	 *
	 * This class creates a movie and it's associated movie controller from
	 * a url. It then creates a QTPlayer object which is used to display/play 
	 * and control the movie. 
	 *
	 * @param theURL string containing the URL of the movie to open
	 */
	public void createNewMovieFromURL (String theURL) {
		try {
			// create the DataRef that contains the information about where the movie is
			DataRef urlMovie = new DataRef(theURL);
			
			// create the movie 
			theMovie = Movie.fromDataRef (urlMovie,StdQTConstants.newMovieActive);
			currentOffset = 0;
			theMovieController = new MovieController(theMovie);
                        if (qtComponent != null)
                            qtComponent.setMovieController(theMovieController);

			// this will set the size of the enclosing frame to the size of the incoming movie
			pack();

		} catch (QTException err) {
			err.printStackTrace();
		}
	}
		
	/**
	 * Gets the movie to be searched
	 * 
	 * @return the Movie
	 */
	public Movie getMovie () throws QTException {
		return theMovie;
	}
	
	/** 
	 * This method tears down the QTSession object and quits the application
	 */
	public static void goAway () {
		QTSession.close();
		System.exit(0);	
	}	

	/** 
	 * This method stops the movie (if applicable) from playing
	 */
	void stopPlayer () {
		try {
			if (theMovie != null)
				theMovie.setRate(0);
		} catch (QTException err) {
			err.printStackTrace();
		}
	}
	
	/** 
	 * FindTextInMovieTextTrack method
	 *
	 * This method shows how to use both the quicktime.std.movies.searchText method and the 
	 * quicktime.std.movies.media.TextMediaHandler.findNextText method to search for text in 
	 * a movie text track.
	 * @param searchText the string to search for
	 * @param theFlags search flags- forward, backward, etc.
	 * @return true if the text was found
	 */
	boolean findTextInMovieTextTrack (String searchText, int theFlags) {
			// was the text found? 
		boolean textFound = false;
		
			// we will search in enabled text tracks only 
		theFlags |= StdQTConstants.searchTextEnabledTracksOnly;
		
		if ( kUseMovieSearchTextMethod) {
			//////////
			//
			// METHOD ONE: Use the quicktime.std.movies.searchText method, your one-stop, 
			//				find-the-text-and-do-the-right-thing function.
			//
			//////////
			
			try {
					// get the current movie time - we will start searching
					//	from this point on
				QTPointer searchTextPtr = new QTPointer(searchText.getBytes());	
				SearchResult sResult = theMovie.searchText(searchTextPtr,		// the text to search for
															theFlags,				// flags 					
															theMovie.getTime(),	// current movie time 		
															currentOffset 			// searchOffset 			 
															);
				if (sResult.getFoundTrack() != null) {
					textFound = true;	// found it!!!! 
					currentOffset = sResult.getOffset();	// save off current offset into text 
				}
			}
			catch (QTException e) {
				int theError = e.errorCode();
				if (theError == quicktime.Errors.movieTextNotFoundErr) {
					showErrorDialog("Text not found!");
				}
				else {
					e.printStackTrace();
				}
			}
		} else {
			//////////
			//
			// METHOD TWO: Use quicktime.std.movies.media.TextMediaHandler.findNextText method. Here, 
			//				you need to explicitly go to the found text and select it. 
			//
			//////////

					// the text media handler for our text track 
				MediaHandler trackMediaHandler = null;

				try {					
						// get the first enabled text track from the movie. If there is
						//	more than one, the first one found will be used 
					Track theTextTrack = theMovie.getIndTrackType (1,
																StdQTConstants.textMediaType, 
																StdQTConstants.movieTrackMediaType | 
																StdQTConstants.movieTrackEnabledOnly);
						
					if (theTextTrack != null) {
						// load the entire text track into RAM 
						theTextTrack.loadIntoRam (0, theTextTrack.getDuration(), 0);

						// get the text media handler for the track - we'll use
						//	this media handler to demonstrate how to perform a
						//	text search 
						Media trackMedia 	= Media.getTrackMedia (theTextTrack);
						trackMediaHandler 	= trackMedia.getHandler();
					}
					else {
						showErrorDialog ("Movie does not contain a text track!");
						return false;
					}
			
					// take the generic media handler gotten from our text
					//	track and cast it to a TextMediaHandler, since we know
					//	we are dealing with text media 
					TextMediaHandler textMediaHdlr = (TextMediaHandler)trackMediaHandler;
				
						// we'll need to make a QTPointer object from our search text 
						//	and pass this to the findNextText method below 
					QTPointer searchTextPtr = new QTPointer (searchText.getBytes());	
					
						// search for the specified text
					FoundTextInfo fTextInfo = textMediaHdlr.findNextText (searchTextPtr,			// the text to search for 
																		theFlags,				// search flags 
																		theMovie.getTime()	// time to begin searching from 
																		);	
					if (fTextInfo.time != -1) {
						textFound = true;	// found it!!!! 

							// save current offset into search text 
						currentOffset = fTextInfo.offset;
						
							// go to the time in the movie where the
							//	search text was found 
						theMovie.setTimeValue(fTextInfo.time);
	
							// highlight the text 
						textMediaHdlr.hiliteTextSample(theMovie.getTime(),
														fTextInfo.offset,
														fTextInfo.offset+searchText.length(),
														new QDColor(0x8000,0x8000,0x8000)	// use grey color hilight 
														);
					}
					else 
						showErrorDialog("Text not found!");
				}
				catch (QTException e) {
					int theError = e.errorCode();
					if (theError == quicktime.Errors.movieTextNotFoundErr) 
						showErrorDialog("Text not found!");
					else 
						e.printStackTrace();
				}
			}
		
			// tell the caller whether or not text was found 
		return textFound;
	}
	
	/**
	 * Displays an error dialog reporting any problems encountered
	 * 
	 * @param theError the error string
 	 */
	private void showErrorDialog(String theError) {
		errorDialog = new JDialog();
		errorDialog.setModal(true);
		errorDialog.setResizable(false);
		Container c = errorDialog.getContentPane();
		c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
		
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
		jp.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));				

		JLabel jl = new JLabel(theError);
		jl.setMaximumSize(new Dimension(270,20));
		
		jp.add(jl);
		
		JPanel jp2 = new JPanel();
		jp2.setLayout(new BoxLayout(jp2,BoxLayout.Y_AXIS));
		jp2.setBorder(BorderFactory.createEmptyBorder(5,125,15,10));

		JButton jb = new JButton("OK");
		jb.setMaximumSize(new Dimension(70,20));

		jp2.add(jb);
		
		errorDialog.getContentPane().add(jp, "South");
		errorDialog.getContentPane().add(jp2, "South");
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				errorDialog.dispose();
			}
		});
		errorDialog.setBounds(0,0,300,100);
		errorDialog.setVisible(true);
	}
}