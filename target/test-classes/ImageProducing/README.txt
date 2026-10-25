=============================================================================
QuickTime for Java SDK                                 Updated: 4 August 2003

Read Me Notes to "Image Producing" Demo and Sample Code

=============================================================================
This demo program shows how to display any QuickTime drawing object using Java's ImageProducing model.

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

(1) Jumps.mov 

=============================================================================
Notes & Comments

This sample works with the QTImageProducer and swing components.

The QTImageProducer is responsible for getting the QT movie and producing the pixels for a java.awt.Image. It draws the movie into its own QDGraphics world and then feeds the pixels to any java.image.ImageConsumers that are registered with it in a format that they are able to deal with.

The Swing buttons control the movie playback.

The IPCanvas takes the QTImageProducer as the source of pixels for an Image. It draws the image to the screen in the paint call, then redraws the QTImageProducer which will result in more pixels being produced. The redraw call will result in a repainting of the canvas. This process could be more efficient - it is a simple mechanism to illustrate the basic steps involved in using the QTImageProducer. For instance if the application ascertained the frame rate of the movie and the movies current rate it could only issue redraw calls to the QTImageProducer when the next frame was due to be drawn.

The IPDrawer is provided as example code to take this interaction and create a QTCanvas Drawable client.

The IPDrawer is used as a convenience class. The QTImageProducer can be used directly by any of Java's built in ImageConsumers (such as the java.awt.Component). Thus the QTImageProducer can be used directly as a pixel resource for a swing component.

The IPDrawer creates the Image and tells the QTImageProducer object to produce pixels. If the producer is a movie (which it is in this case) then the IPDrawer ensures that it gets the pixels as often as it can.

The IPDrawer is a client of the QTCanvas - which is added to a swing JPanel.

The IPDrawer paints its image onto the QTCanvas space using the java drawing commands. QuickTime is not drawing directly to the screen in this case.

=============================================================================
General Comments

- QTSession.open and close:

A QTSession.open will perform a gestalt check to ensure that QuickTime is present and is initialized. This is a required call before any QuickTime Java classes can be used.

When the user closes the window the program will quit, first calling QTSession.close to terminate QuickTime. It is necessary for programs to call QTSession.close if they have previously called QTSession.open in order to shut down QuickTime properly.


=============================================================================

QuickTime and QuickTime for Java are trademarks of Apple Computer, Inc.
(c) 1998 Apple Computer Inc. All rights reserved.
