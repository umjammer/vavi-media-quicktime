package Applets.QTStreamingApplet;
/*
 * quicktime.app: Sample Code for Initial Seeding
 *
 * © 1996, 97 Copyright, Apple Computer
 * All rights reserved
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/* This class creates a animated button that listens to mouse events
 * and changes the image of the button accordingly.
 */
public class IconButton extends Component implements MouseListener {
	
	/**
	 * Prepares an image for rendering on this component. The image data is
     * downloaded asynchronously in another thread and the appropriate screen
     * representation of the image is generated. 
     * @param         iconPressed     the Pressed Button Icon
     * @param         iconReleased     the Released Button Icon
     */
	public IconButton (Image iconPressed, Image iconReleased) {
		this.iconPressed = iconPressed;
		this.iconReleased = iconReleased;
		prepareImage(iconPressed, this);
		prepareImage(iconReleased, this);
		
		addMouseListener(this);
	}
	
	private Image iconReleased, iconPressed;
	transient private boolean buttonPressed = false;
  	transient private boolean drawPressed = false;
  	private boolean fireOnRelease = true;
    transient ActionListener actionListener;
	private String actionCommand = "";

    public synchronized void addActionListener (ActionListener l) {
		actionListener = l;
    }

    /**
     * Removes the specified action listener so that it no longer 
     * receives action events from this button. Action events occur  
     * when a user presses or releases the mouse over this button.
     * @param         l     the action listener.
     * @see           java.awt.event.ActionListener
     * @see           java.awt.Button#addActionListener
     * @since         JDK1.1
     */ 
    public synchronized void removeActionListener(ActionListener l) {
		actionListener = null;
    }

	public void setFireActionOnRelease (boolean flag) {
		fireOnRelease = flag;
	}

	public boolean isFireActionOnRelease () {
		return fireOnRelease;
	}
	
	/**
	  * Depending upon the state of mouse button (pressed or released)
	  * the corresponding image is displayed. This gives the button a
	  * animated look and feel
	  */
	public void paint(Graphics g) {
		if (drawPressed)
			g.drawImage(iconPressed, 0, 0, this);
		else
			g.drawImage(iconReleased, 0, 0, this);
	}
	                      
  	public void mouseClicked(MouseEvent e) {}
  	  	
  	public void mouseEntered(MouseEvent e) {
   		if (buttonPressed) {
   			drawPressed = true;
   			repaint();
   		}
 	}
  	
  	public void mouseExited(MouseEvent e) {
  		if (buttonPressed) {
  			drawPressed = false;
  			repaint();
  		}
  	}
  	
  	/**
  	  * This fires the action event which invokes the actionPerformed method on the 
  	  * PopupMenu Button that displays the actual PopupMenu
  	  */
  	public void mousePressed(MouseEvent e) {
  		buttonPressed = true;
  		drawPressed = true;
  		repaint();
  		if (actionListener != null && fireOnRelease == false)
  			actionListener.actionPerformed (new ActionEvent (this, ActionEvent.ACTION_PERFORMED, actionCommand, e.getModifiers()));
  	}
  	
  	public void mouseReleased(MouseEvent e) {
  		buttonPressed = false;
  		drawPressed = false;
  		repaint();
  		if (actionListener != null && fireOnRelease)
  			actionListener.actionPerformed (new ActionEvent (this, ActionEvent.ACTION_PERFORMED, actionCommand, e.getModifiers()));
  	}

   /**
     * Sets the command name for the action event fired 
     * by this button. By default this action command is 
     * set to match the label of the button.
     * @param     command  A string used to set the button's 
     *                  action command.
     * @see       java.awt.event.ActionEvent
     * @since     JDK1.1
     */
    public void setActionCommand (String command) {
        actionCommand = command;
   		if (actionCommand == null) 
   			actionCommand = "";
    }

    /**
     * Returns the command name of the action event fired by this button.
     */
    public String getActionCommand() {
        return actionCommand;
    }	
	
	/**
	 * This sets the size of the IconButton to be displayed.
	 */
	public Dimension getPreferredSize () {
		return new Dimension (iconReleased.getWidth(this), iconReleased.getHeight(this));
	}
}
