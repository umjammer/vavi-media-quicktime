=============================================================================
QuickTime for Java SDK                                  Updated: 31 July 2003

Read Me Notes to "Detached Controller" Demo and Sample Code

=============================================================================
This demo program shows how to select and then play a QuickTime movie with its controller detached. 

=============================================================================
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
Media requirements for this Sample Code are:

A QuickTime movie of the user's choice

=============================================================================
Notes & Comments

The user is prompted to select a movie file. If the selection is made, a window is constructed and the movie and its controller are presented to the user. 

The movie and its controller are displayed separately in the same window, with the controller at the top and the actual movie at the bottom. They are separated by a button. This button is eventless and its sole purpose is to show that the movie and the controller are detached. 

You can control the movie with the detached controller at the top.

Resizing the window will resize the width of movie and the controller --  not the height. 

The setAttached() method call on the controller detaches or attaches the controller to its movie. Two QTComponent objects are used, one to play the movie, and one to play the controller of the movie. 
The controller of the movie is created using the new MovieController(movie, mcScaleMovieToFit)

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
