=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003
 
Read Me Notes to "Image File" Demo and Sample Code

=============================================================================
This demo program shows the usage of the GraphicsImporter to import and display a plethora of image file formats.

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

Any image file that can be imported using the GraphicsImporter

=============================================================================
Notes & Comments

This demo works with GraphicsImporter.

In this demo, a GraphicsImporter is used to read and decompress an image file. The QTFactory class is used to create a QTComponent that is added to a Java AWT frame and displayed.


=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
