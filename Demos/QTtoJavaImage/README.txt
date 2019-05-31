=============================================================================
QuickTime for Java SDK                                  Updated: 31 July 2003

Read Me Notes to "QT to Java Image" Demo and Sample Code

=============================================================================
This demo program shows the usage of the GraphicsImporterDrawer is used to produce pixels to create a java.awt.Image

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

This demo works with GraphicsImporter to produce pixels for the creation of a java.awt.Image. The sample code has some notes about other possible image sources that can be used in this manner.  

QTtoJavaImage class: Note that quicktime.app.image.GraphicsImporterDrawer has been deprecated in QTJava 6.1, and moved to
quicktime.app.view.GraphicsImporterDrawer.

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
