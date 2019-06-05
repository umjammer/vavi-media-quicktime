=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003

Read Me Notes to "Play Movie" Demo and Sample Code

=============================================================================
This demo program shows how to display any QuickTime content within a java.awt.Frame using the QTFactory to create a QTComponent.

=============================================================================
The minimum runtime requirements for this Sample Code are:

- Common:
	- QuickTime 5.0.1 or later 
	- QTJava 6.1 (QTJava.zip)
	- Swing

- MacOS:
	- OS X 10.2 or later
	- Java 1.3.1 or later

- Windows:
	- Windows 98, ME, 2000, XP or later
	- Java 1.2.1 or later
	- JRE/JDK from Sun Microsystems, Inc. recommended

=============================================================================
Media requirements for this Sample Code:

(1) Sample.mov in the SDK folder
(2) QuickTime movie files of the user's choice

=============================================================================
Notes & Comments

When opened, the user is shown Sample.mov.  The user is then able to open a QuickTime Movie file with the Open menu item. If the selection is made the movie and its controller will be presented to the user. Closing the window or choosing the Quit menu item will quit the application.

The window is the size of the movie and resizing the window will resize the movie. The MovieController is displayed on screen using a QTComponent. The QTComponent is created using QTFactory.makeQTComponent(MovieController mc). The QTComponent is then added to the Java AWT Frame.

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
