package quicktime.std.movies.media;

import quicktime.std.StdQTException;
import quicktime.std.movies.AtomContainer;

public class ThreeDMediaHandler extends VisualMediaHandler {

    ThreeDMediaHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::Media3DGetNamedObjectList()"
     */
    public AtomContainer getNamedObjectList() throws StdQTException {
        return AtomContainer.fromThreeDMediaHandlerObject(this);
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::Media3DGetRendererList()"
     */
    public AtomContainer getRendererList() throws StdQTException {
        return AtomContainer.fromThreeDMediaHandlerRenderer(this);
    }
}
