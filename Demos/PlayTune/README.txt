=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003

Read Me Notes to "Play Tune" Demo and Sample Code

=============================================================================
This demo program shows how to use the TunePlayer to play a Tune using the QuickTime Music Architecture

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

Sin440.aif

=============================================================================
Notes & Comments

The user is shown a small window with a button on it - click the button to play the tune.

NoteChannels are constructed and given to the TunePlayer to play the tune with. The tune is then constructed using the static methods of the MusicData class to stuff the desired notes/rests/etc. into the data format that the TunePlayer expects.

You might wonder where the NoteAllocator component is and why it isn't visible in this sample code. The NoteAllocator.getDefault() method is used to construct those music structures that require the NoteAllocator component. The NoteChannel.getNoteAllocator() can be used to retrieve the NA for a particular channel.

An AtomicInstrument is used as the instrument for the second part of the tune. It uses a Sin Wave at 440KHz stored in an AIFF file as the sample data.

The sample also shows you how to make a Movie from the Tune that has been constructed.

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998-2003 Apple Computer Inc. All rights reserved.
