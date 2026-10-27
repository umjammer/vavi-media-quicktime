/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package CustomMedia.com.vr;

import quicktime.QTException;
import quicktime.std.movies.media.SampleDescription;


/**
 * This class represents a description of QTVR media.
 */
public final class VRSampleDescription extends SampleDescription {

    //_________________________ CLASS VARIABLES
    private final static int kNativeSize = 20;
    public static final int qtvrMediaType = 0x71747672;  //'qtvr'
 

/*       offset size
 UInt32  descSize;  0  4    /* total size of the QTVRSampleDescription
 UInt32 descType;  4  4   /* must be 'qtvr'

 UInt32 reserved1;  8  4   /* must be zero
 UInt16 reserved2;  12  2   /* must be zero
 UInt16 dataRefIndex; 14  2   /* must be zero

 UInt32 data;   16  4 => 20   /* Will be extended to hold vrWorld QTAtomContainer
*/

//_________________________ CLASS METHODS

    /** This constructor makes a new VRDescription object. */
    public VRSampleDescription() throws QTException {
        super(kNativeSize, true, qtvrMediaType);
    }

    private VRSampleDescription(int hand) {
        super(hand, null, false);
    }

//_________________________ INSTANCE METHODS

    /** Implementation of Cloneable Interface. */
    @Override
    public Object clone() {
        return new VRSampleDescription(makeAndCopyHandle());
    }

    /**
     * Returns the AtomContainer that is at the end of the Media structure.
     * It allows you to edit the contents of this atom container that is attached
     * to the description - it edits in place, rather than returning you a copy.
     * @return the VRWorld atom container.
     *
    public AtomContainer getVRWorld () {
    return MediaMoviesAccess.makeAtomContainer (getIntAt(16), this);
    }*/
}

/*
 * $Log: VRSampleDescription.java,v $
 * Revision 1.1  2003/07/25 16:37:03  grahame
 * First checked in.
 *
 * Revision 1.6  2000/01/27 19:42:38  duano
 * "life begins in CVS for the QTJava project."
 *
 * $SSLog: /Biscotti/QTJavaDemos/CustomMedia/src/com/vr/VRSampleDescription.java $
 *
 * 5     3/11/99 5:32 PM Roger Smith
 * Update Source License Agreement
 *
 * 2     6/30/98 11:57 PM Roger Smith
 * change com.qt to quicktime to reflect new package naming scheme
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
