package quicktime.std.movies.media;

import quicktime.std.StdQTException;
import quicktime.std.movies.AtomContainer;

public class ThreeDMediaHandler extends VisualMediaHandler {

    ThreeDMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * No QT Documentation.
     * QuickTime::Media3DGetNamedObjectList()
     */
    public AtomContainer getNamedObjectList() throws StdQTException {
        return AtomContainer.fromThreeDMediaHandlerObject(this);
    }

    /**
     * No QT Documentation.
     * QuickTime::Media3DGetRendererList()
     */
    public AtomContainer getRendererList() throws StdQTException {
        return AtomContainer.fromThreeDMediaHandlerRenderer(this);
    }
}

