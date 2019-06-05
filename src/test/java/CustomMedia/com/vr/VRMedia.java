/*
 * QuickTime for Java SDK Sample Code

   Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.

 */
package CustomMedia.com.vr;

import quicktime.QTException;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.GenericMedia;
import quicktime.std.movies.media.Media;
/** Represents VR Media - this is an example of how to subclass GenericMedia
 * to return a Media object of a subclass that corresponds to the Media Type.
 */
public class VRMedia extends GenericMedia {
	public static void registerMediaType () {
		Media.addMediaType (vrMediaOSType, "com.vr.VRMedia");
	}
	
		// 0x5354706e is the OSType for a panorama media type
	private static final int vrMediaOSType = 0x5354706e;
//_________________________ CLASS METHODS
	public VRMedia (Integer v) throws QTException {
		super (v);
	}	//must have a default constructor for the makeMedia call
	/**
	* This constructor creates a media struct for the specified Track object.
	* <BR><BR><b>QuickTime::NewTrackMedia()</b><BR><BR>
	* @param itsTrack Specifies the Track object this media belongs to.
	* @param timeScale Specifies the time scale of the new media.
	* @param dataRef a DataRef object specifying the default data reference for this media.
	*/
	public VRMedia (Track itsTrack, int timeScale, DataRef dataRef) throws QTException {
		super (itsTrack, timeScale, dataRef, vrMediaOSType);
	}

	/**
	* This constructor creates a media struct for the specified Track object.
	* <BR><BR><b>QuickTime::NewTrackMedia()</b><BR><BR>
	* @param itsTrack Specifies the Track object this media belongs to.
	* @param timeScale Specifies the time scale of the new media.
	*/
	public VRMedia(Track itsTrack, int timeScale) throws QTException {
		this (itsTrack, timeScale, null);
	}
			
//_________________________ INSTANCE METHODS
}

/*
 * $Log: VRMedia.java,v $
 * Revision 1.1  2003/07/25 16:37:03  grahame
 * First checked in.
 *
 * Revision 1.8  2000/01/27 19:42:38  duano
 * "life begins in CVS for the QTJava project."
 *
 * $SSLog: /Biscotti/QTJavaDemos/CustomMedia/src/com/vr/VRMedia.java $
 * 
 * 7     3/11/99 5:32 PM Roger Smith
 * Update Source License Agreement
 * 
 * 6     22/9/98 4:51 PM Bill Stewart
 * update
 * 
 * 3     6/30/98 11:57 PM Roger Smith
 * change com.qt to quicktime to reflect new package naming scheme
 * 
 * 2     16/6/98 5:20 PM Bill Stewart
 * changes to GenericMedia
 * 
 * 1     29/4/98 3:11 PM Bill Stewart
 * Adding subproject 'QTJavaDemos' to '$/Biscotti'
 * 
 * 1     22/4/98 5:46 PM Bill Stewart
 * Adding subproject 'QTJavaDemos' to '$/Biscotti'
 * 
 * 1     15/4/98 6:49 PM Bill Stewart
 * Adding subproject 'VR Media' to '$/Biscotti/Projects'
 */
