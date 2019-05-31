=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003

Read Me Notes to "Time Code" Demo and Sample Code

=============================================================================
This demo program shows how to add and remove TimeCode tracks to a movie.
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
Media requirements for this Sample Code - user supplied movie

=============================================================================
Notes & Comments

This code uses the TimeCode media services of QuickTime to add TimeCode readouts to a movie.

There are occasional redraw problems with the MovieController when the time code track is removed from the movie whilst the movie is actually playing (stopping the movie allows the MovieController to redraw properly). Ideally the movie should be stopped when removing the TimeCode track.

The code that would save the new time code track to the movie is commented out but present.

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
