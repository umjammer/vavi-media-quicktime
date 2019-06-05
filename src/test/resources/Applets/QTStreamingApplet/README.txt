=============================================================================
QuickTime for Java SDK                              Updated: 30 November 1998

Read Me Notes to "QTStreaming Applet" Demo and Sample Code

=============================================================================
This demo program shows how to display any QuickTime content within a java.awt.Applet.

=============================================================================
The minimum runtime requirements for this Sample Code are:

- Common
	- Sun Compliant Java Runtime Environment 1.1
	- QuickTime 3 
	- QTJava.zip
	- An Applet viewer
	
- MacOS:
	- System 8 or later
	- Macintosh Runtime for Java (MRJ) 2.1

- Windows 95, 98, or NT::
	- JRE/JDK from Sun Microsystems, Inc. recommended

=============================================================================
Media requirements for this Sample Code:

A user supplied media

=============================================================================
Notes & Comments

test.html is for use with this code - it will expect to find the AppletTag.js when run in a browser. The applet tag accepts a name name/value pair where name="url" + i , and i = 1,2... till total . You should supply a name = "total" and its value is the total number of url's to be appended to the popup menu.
		

Enter a url eg. file:///.... to play a movie (or read in an image file) in the text box at the bottom of the applet

See the Simple Applet for comments about the code.

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used - it is called first in the init() method

It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly - it is called in the destroy() method

=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998 Apple Computer Inc. All rights reserved.
