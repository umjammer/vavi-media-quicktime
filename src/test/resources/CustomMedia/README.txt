=============================================================================
QuickTime for Java SDK                                  Updated: 31 July 2003

Read Me Notes to "Custom Media" Demo and Sample Code

=============================================================================
This demo program shows how to subclass the quicktime.std.movies.media.Media class to support any Custom media types. 

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

A QuickTimeVR Panoramic movie of the user's choice

=============================================================================
Notes & Comments

The user is prompted to select a QTVR Panoramic movie file.

It will print out the media object and the SampleDescription of all of the tracks that are found in the opened movie.

The TestVRMedia.zip file also contains the VRMedia class which is the custom Media subclass. It supplies an Integer constructor - the application should not call this directly - it is called by the Media factory method based on the media type and the vrMediaOSType that is registered with the Media factory.

The factory methods create Media subclasses based on the media type. If a match is not found for QuickTime's default media types a search is done to see if the application has itself registered knowledge of custom or application specific media types. If a match is still not found the factory will return a GenericMedia object.

Standard Media calls can still be done on a GenericMedia class - it is only if the application requires specific functionality and support for specific media or media handlers that custom media classes have to be written. This mechanism can be used to integrate those custom classes with the existing framework.

The registerMediaType static call could easily be done in the static initializer of the VRMedia class - it is left as a specific call for the purpose of illustrating the mechanism that is being used. 

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
