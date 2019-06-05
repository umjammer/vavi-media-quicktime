=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003

Read Me Notes to "GraphicsExport" Demo and Sample Code

=============================================================================
This demo program shows how to use the GraphicsExport component to export a input graphics, and using the user dialog to customise export settings.
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
Media requirements for this Sample Code - user supplied image file

=============================================================================
Notes & Comments

This code shows how to export a graphics , using the user dialog to customise export settings to a image file. 

You select a graphics export component based on the desired output format, such as GIF or PNG. 

A GraphicsImporter is used to import the supplied image file. On export, the GraphicsImporter is passed as an input source to the Graphics Exporter.  The GraphicsExporter requestSettings dialog is called on a seperate thread as the main Java AWT event thread should not be blocked.

The graphic image can be exported to a handle, a file, or a data reference. 
QuickTime supports standard image formats, third-party developers may also write their own graphics exporters for other image file formats


=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

QTSession.close is called after the program has finished exporting


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
