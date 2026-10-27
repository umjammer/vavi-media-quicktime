/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-2001 Apple Computer, Inc.
 */

package PlaySound;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.time.TaskAllMovies;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTException;
import quicktime.std.movies.Movie;


/**
 * This sample demonstrates how to play a sound or a midi file without using a QTCanvas.
 * All user interface elements are using AWT.
 */
public class PlaySound extends Frame {

    Button playMidi, playSnd;
    Movie movie;

    static String midiFile = "jazz.mid";
    static String soundFile = "sin440.aif";

    static void main(String[] args) {
        midiFile = args[0].replace("\\", "/");
        soundFile = args[1].replace("\\", "/");
        System.err.println("midi: " + midiFile);
        System.err.println("sound: " + soundFile);
        try {
            QTSession.open();
            PlaySound theSoundPlayer = new PlaySound();
        } catch (Exception e) {
            e.printStackTrace();
            QTSession.close();
        }
    }

    /**
     * Create the user interface
     */
    PlaySound() {
        playMidi = new Button("Play Midi File");
        playSnd = new Button("Play Sound File");

        this.add(playMidi);
        this.add(playSnd);

        playMidi.addActionListener(new Action());
        playSnd.addActionListener(new Action());

        this.setLayout(new FlowLayout());
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                QTSession.close();
                dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        pack();
    }

    /**
     * The Action class responds to user interaction with the buttons
     */
    class Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            Object object = event.getSource();
            try {
                if (object == playMidi) {
                    if (movie != null) {
                        movie.setRate(0);
                        movie.setActive(false);
                        TaskAllMovies.removeMovie();
                    }
                    playFile(midiFile);
                } else if (object == playSnd) {
                    if (movie != null) {
                        movie.setRate(0);
                        movie.setActive(false);
                        TaskAllMovies.removeMovie();
                    }
                    playFile(soundFile);
                }
            } catch (StdQTException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * The playFile method loads and plays an audio file with the given name
     * or partial path. If a sound is already playing, it will be stopped before
     * the new sound is played.
     *
     * @param fileName the name of the sound file to be played
     */
    public void playFile(String fileName) {
        try {
//       String soundLocation = QTFactory.findAbsolutePath(fileName).getPath();
            File soundLocation = new File(fileName);

            OpenMovieFile fileIn = OpenMovieFile.asRead(new QTFile(soundLocation));
            movie = Movie.fromFile(fileIn);
            TaskAllMovies.addMovieAndStart();
            movie.setActive(true);
            movie.setRate(1);
//   } catch (IOException e) {
//    e.printStackTrace();
        } catch (QTException e) {
            e.printStackTrace();
        }
    }
}