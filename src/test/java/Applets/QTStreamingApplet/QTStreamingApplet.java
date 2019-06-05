/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.media.DataRef;


public class QTStreamingApplet extends Applet {

	//this fix is for applets on Win 95/98 using 1.3 plugin. 
	//We create a lock that we would synchronize with the QTSession.open call in init() and
	//QTSession.close in destroy()  calls. this would make sure that QTSession.open() of the applet gets initialized
	//properly everytime it enters init().
	static Object lock =  new Object();
	
	//private Drawable myQTContent;
	//private QTCanvas myQTCanvas;
    private List<String> urlTable;
    private TextField urlTextField;
    private PopupMenu pm;
    private QTComponent myQTCanvas;
    
	public void init () {
		try {
			synchronized(QTStreamingApplet.lock) {
				if (QTSession.isInitialized() == false)
					QTSession.open();
			}		
				// set up a QTCanvas which will disply its content 
				// at its original size of smaller and centered in the space given
				// to the QTCanvas when the applet is layed out
			setLayout (new BorderLayout());
			urlTable = new ArrayList<String>();
			//myQTCanvas = new QTCanvas (QTCanvas.kInitialSize, 0.5F, 0.5F);
			//add (myQTCanvas, "Center");		
			
			//myQTContent = ImageDrawer.getQTLogo();
		
			add (bottomPanel(), "South");
			readURL();
						
		} catch (QTException qtE) {
				//in this case the only QTException is in QTSession.open()
			throw new RuntimeException (qtE.getMessage());		
		}
	}	
	
	public void start () { 
	}
	
	public void stop () { 
            try{
		if (myQTCanvas != null)
			myQTCanvas.setMovie(null);
		} catch (QTException qtE) {
				//in this case the only QTException is in QTSession.open()
			throw new RuntimeException (qtE.getMessage());		
		}
	}
	
	public void destroy () {
		synchronized(QTStreamingApplet.lock) {
			QTSession.close();
		}	
	}
		
		
	/**
	 * creates drawable object from the URL the user has entered
	 * or from the menu item chosen
	 */
	private void createNewMovieFromURL(String selURL) throws QTException {
	    System.err.println ("createNewMovieFromURL " + selURL);
//	    String url = urlTextField.getText();	
	    DataRef dRef = new DataRef(selURL);
	    Movie mov = Movie.fromDataRef (dRef,StdQTConstants.newMovieActive);
	    MovieController mc = new MovieController(mov);
	    if (myQTCanvas == null) {
	        myQTCanvas = QTFactory.makeQTComponent(mc);
	        add((Component)myQTCanvas, "Center");	
	    }else {
	        myQTCanvas.setMovieController (mc);
	    }
//	    myQTContent = QTFactory.makeDrawable(url);
//	    myQTCanvas.setClient(myQTContent, true);
	}

	/**
	 * the parameter list in the html file should have a name = total having the value of the total
	 * number of url's to be appended to the popup menu. Each url should start with "url" + i , where i
	 * is 1, 2.... in increasing order till the i = total.
	 */
	private void readURL() throws QTException{
		for ( int i = 0; i < Integer.parseInt(getParameter("total")); i++) {
			String url = getParameter("url" + (i + 1));		
			if ( url != null) 
				appendItem(url);
		}
	}
	
	public Component bottomPanel () {
		Panel row2 =  new Panel();
		row2.setLayout(new FlowLayout(FlowLayout.CENTER));
		row2.setBackground(Color.white);	
			
		urlTextField = new TextField ("file:///... Enter an URL to a movie", 40);//Enter URL to movie here", 30);
		urlTextField.setFont (new Font ("Dialog", Font.PLAIN, 10));
		urlTextField.setEditable (true);
		urlTextField.addActionListener (new ActionListener () {
			//TextField tf = urlTextField;
			
			public void actionPerformed (ActionEvent ae) {
				//if (myQTCanvas != null) {
					try {
                                                System.err.println ("***createNewMovieFromURL " + urlTextField.getText());
						createNewMovieFromURL(urlTextField.getText());
						appendItem(urlTextField.getText());
					} catch (QTException e) {
							//probably a non-fatal error that the Applet 
							//should deal with more informatively
						e.printStackTrace();
					}
				//}
			}
		});

		row2.add(urlTextField);
		
		pm = new PopupMenu();
 		
		Image img1 = Toolkit.getDefaultToolkit().getImage(getCodeBase().getFile() + "images/p1.gif");
		Image img2 = Toolkit.getDefaultToolkit().getImage(getCodeBase().getFile() + "images/r1.gif");
		IconButton menuButton = new PopupMenuButton (img1, img2, pm);
					
		row2.add (menuButton);
		
		return row2;
	}
	
	/** 
	 * This appends the input movie url entered in the textfield or
	 * selected from the Choose movie menu to the PopupMenu. It checks
	 * for its duplicates and then appends it to the list in the popupmenu
     * @param     urlItem  Item to be appended to the PopupMenu
	 */
    private void appendItem(String urlItem) throws QTException {
    	if (urlTable.contains(urlItem) == false) {
    		MenuItem item = new MenuItem(urlItem);
        	pm.add (item);
        	item.addActionListener (new ActionListener () {
        		public void actionPerformed(ActionEvent event) {
        			try {
					MenuItem jm = (MenuItem)event.getSource();
					String selURL = jm.getLabel();

					urlTextField.setText(selURL);
					createNewMovieFromURL(selURL);
					}catch (QTException ex) {
						ex.printStackTrace();
					}
				}	
			});
        	urlTable.add(urlItem);
        }	
    }
    //workaround for java 1.3 bug - throws IllegalStateException on quit
    public java.awt.im.InputContext getInputContext() {
        return null;
    }

}
