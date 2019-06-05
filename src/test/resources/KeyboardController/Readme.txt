=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003

Read Me Notes to "Keyboard Controller" Demo and Sample Code

=============================================================================
This demo program shows how to customise user control of the playback of a movie using the Keyboard.

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
Media requirements for this Sample Code are found in the media directory of the QTJava SDK:

(1) jumps.mov

=============================================================================
Notes & Comments

A keyboard listener is added to the main Frame which listens for keyboard events. Keyboard events are received by the KeyAdapter which in turn controls the playback of the movie. The movie is displayed on screen by adding a QTComponent to the Java AWT Frame.  The QTComponent is created using QTFactory.makeQTComponent(Movie moov).

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
