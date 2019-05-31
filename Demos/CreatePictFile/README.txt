=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003

Read Me Notes to "Create Pict File" Demo and Sample Code

=============================================================================
This demo program shows how to create a PICT and PICT file from drawing operations into a QDGraphics 

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
Media requirements for this Sample Code are:

A QuickTime image file of the user's choice

=============================================================================
Notes & Comments

This code shows how to create a Pict file by  "recording" the results of a draw operation into a Pict object. Once the user selects an image file file it opens and displays the image in a new window. Clicking on the button at the top of the window saves the image as a pict file called results.pict in the application startup directory.

Every time the QDGraphics that is the recording source is drawn to this must be set to be the current GWorld or else the resultant PICT is corrupted. 

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
