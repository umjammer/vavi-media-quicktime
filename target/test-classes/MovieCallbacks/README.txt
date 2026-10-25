=============================================================================
QuickTime for Java SDK                                  Updated: 31 July 2003

Read Me Notes to "Movie Callbacks" Demo and Sample Code

=============================================================================
This demo program shows how to display a QuickTime movie within a window and add CallBacks. The CallBacks are QuickTime calling back into Java through the MovieController, Movie and QuickTimeVR API. 

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

(1) QuickTime movie files of the user's choice - preferably a movie with QuickTime VR panorama or object presentation

=============================================================================
Notes & Comments

The program installs a number of QuickTime callbacks into the movie and its controller when the movie is opened.
(1) Movie - DrawingComplete proc is used to notify the Java program whenever QuickTime draws to the screen
(2) MovieController - ActionFilter procs are used - this subclass overides those actions that pass on no parameters and a float parameter
(3) If the Movie contains QuickTime VR content then a number of QTVR callbacks are installed for panning, tilting, hot spot, node enter/leaving.
See the notes on call backs in general in the JavaDoc for the quicktime.std.clocks.QTCallBack class

MovieCallbacks class: Note that the packages quicktime.app.display.QTCanvas and quicktime.app.players.QTPlayer have been deprecated in QTJava 6.1, 
and this class now uses quicktime.app.view.QTFactory to create a quicktime.app.view.QTCompoment to display the VR movie.

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
