/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package TimeCode;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FileMenu2 {

    TimeCode myTimeCode;

    public FileMenu2(TimeCode src) {
        this.myTimeCode = src;

        // make the menu bar up
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem addMenuItem = new MenuItem("Add TimeCode Track");
        MenuItem removeMenuItem = new MenuItem("Remove TimeCode Track");
        MenuItem quitMenuItem = new MenuItem("Quit");

        fileMenu.add(addMenuItem);
        fileMenu.add(removeMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);
        myTimeCode.setMenuBar(menuBar);

        addMenuItem.addActionListener(event -> myTimeCode.addTimecodeToMovie());
        removeMenuItem.addActionListener(event -> myTimeCode.deleteTimeCodeTracks());
        quitMenuItem.addActionListener(event -> {
            // closes down QT and quits
            myTimeCode.goAway();
        });
    }
}
