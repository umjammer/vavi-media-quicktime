/*
 * QuickTime for Java SDK Sample Code
 *
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


public class FileMenu {
    PlayMovie myPlayMovie;

    public FileMenu(PlayMovie src) {
        this.myPlayMovie = src;

        // make the menu bar up
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem openMenuItem = new MenuItem("Open...");
        MenuItem urlMenuItem = new MenuItem("Open URL...");

        fileMenu.add(openMenuItem);
        fileMenu.add(urlMenuItem);

        menuBar.add(fileMenu);
        myPlayMovie.setMenuBar(menuBar);

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                myPlayMovie.stopPlayer();

                // creat a movie through the file-open dialog of QT
                try {
                    QTFile qtf = QTFile.standardGetFilePreview(QTFile.kStandardQTFileTypes);
                    myPlayMovie.createNewMovieFromURL("file://" + qtf.getPath());
                } catch (QTException e) {
                    if (e.errorCode() != Errors.userCanceledErr)
                        e.printStackTrace();
                }
            }
        });
        urlMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                myPlayMovie.stopPlayer();

                // creat a movie through the file-open dialog of QT
                Open_URL d = new Open_URL(myPlayMovie);
                d.pack();
                d.setVisible(true);
            }
        });
    }
}
