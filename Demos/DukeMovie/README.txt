=============================================================================
QuickTime for Java SDK                                  Updated: 31 July 2003

Read Me Notes to "Duke Movie" Demo and Sample Code

=============================================================================
This demo program shows how to display any QuickTime content within a java.awt display space using a QTComponent. 

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

(1) Tx.gif in the duke directory (where X is a number indicative of a frame order)
(2) QuickTime media files of the user's choice

=============================================================================
Notes & Comments

The duke animation at the top of the window is from the Duke animation sample applet.

The user selects any QuickTime media - the file is displayed using the QTFactory.makeQTComponent method.  

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
