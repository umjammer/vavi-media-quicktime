=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003
Read Me Notes to "PlaySound" Demo and Sample Code

=============================================================================
This sample demonstrates how to play a sound or a midi file without using a QTComponent. All user interface elements are using AWT
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

(1) QuickTime movies "jazz.mid" and "sin440.aif" from the SDK media folder

=============================================================================
Notes & Comments

The code displays a frame with two AWT buttons in a JAVA Frame. The Play Midi button would play midi media file upon clicking and the Play Sound button would stop the current midi file if its playing and play a sound file.
=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
