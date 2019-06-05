/*
 * QuickTime for Java SDK Sample Code

   Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.

 */
package PlayMovie;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Open_URL extends Dialog implements ActionListener {
    static String text = "file:///... Enter an URL to a movie";
	public Open_URL(PlayMovie parent) {
		super(parent, true);
		myPlayMovie = parent;
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setResizable(true);
		setSize(452,126);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setBackground (new Color(12632256));
                
		label2 = new Label("URL:");
		add(label2);
                
		urlTextField = new TextField(text);
		urlTextField.setFont(new Font("Dialog", Font.PLAIN, 10));
		add(urlTextField);
		setTitle("Open URL");
		
		okButton = new Button();
		okButton.setLabel("OK");
		add(okButton);
                
		cancelButton = new Button();
		cancelButton.setLabel("Cancel");
		add(cancelButton);
                			
		urlTextField.addActionListener(this);
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
			
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				myPlayMovie.createNewMovieFromURL(urlTextField.getText());
				text = urlTextField.getText();
				dispose();
			}
		});
                pack();
	}
	
	//{{DECLARE_CONTROLS
	Label label2;
	Label label1;
	Button okButton;
	Button cancelButton;
	TextField urlTextField;
	//}}
	
	private PlayMovie myPlayMovie;

	public void actionPerformed(ActionEvent evt) {		
		myPlayMovie.createNewMovieFromURL (urlTextField.getText());
		dispose();
	}
}
