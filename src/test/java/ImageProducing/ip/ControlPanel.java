/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package ImageProducing.ip;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import quicktime.QTException;
import quicktime.app.view.MoviePlayer;


public class ControlPanel extends JPanel {

    public ControlPanel(MoviePlayer mp) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 1, 2));
        setFont(new Font("Helvetica", Font.BOLD, 10));

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        startButton.addActionListener(event -> {
            try {
                moviePlayer.setRate(1);
            } catch (QTException e) {
                e.printStackTrace();
            }
        });
        stopButton.addActionListener(event -> {
            try {
                moviePlayer.setRate(0);
            } catch (QTException e) {
                e.printStackTrace();
            }
        });
        this.moviePlayer = mp;

        add(startButton);
        add(stopButton);
    }

    MoviePlayer moviePlayer;
}
