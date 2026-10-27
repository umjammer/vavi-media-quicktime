/*
 * QuickTime for Java SDK Sample Code
 *
 * Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.
 */

package CustomMedia.com.vr;

import quicktime.std.movies.media.VideoMediaHandler;


public final class VRMediaHandler extends VideoMediaHandler {

    //_________________________ CLASS METHODS
    VRMediaHandler(int comp, Object ref) {
        super(comp, ref);
    }

// if you need to make native calls you get the native ID from
// any object you pass in (or from this itself by using: QTObject.ID(this), QTObject.ID(paramArg);
}
