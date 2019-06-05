/*
 * quicktime.app: Sample Code for Initial Seeding
 *
 * © 1996, 97 Copyright, Apple Computer
 * All rights reserved
 */
 
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This displays the standard PopupMenu component of java.awt when 
 * the user clicks on the PopupMenuButton to which this PopupMenu is added
 */
public class PopupMenuButton extends IconButton implements ActionListener {
	/** 	
	 * Creates a Button which displays display a PopupMenu upon clicking
     * @param         iconPressed     the Pressed Button Icon
     * @param         iconReleased     the Released Button Icon
     * @param         pm     the PopupMenu to be displayed
     */
	public PopupMenuButton (Image iconPressed, Image iconReleased, PopupMenu pm) {
		super (iconPressed, iconReleased);
		this.pm = pm;
		setFireActionOnRelease (false);
		addActionListener (this);
		add (pm);
	}
	
	private PopupMenu pm;
	
	/**
	 * This method is fired when the user clicks on the PopupMenuButton. It
	 * displays the PopupMenu for selecting a movie from the list.
	 */
	public void actionPerformed (ActionEvent ae) {
		pm.show (this, 0, getSize().height);
	}
}