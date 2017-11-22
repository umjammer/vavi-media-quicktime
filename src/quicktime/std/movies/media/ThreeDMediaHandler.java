
package quicktime.std.movies.media;

import quicktime.std.StdQTException;
import quicktime.std.movies.AtomContainer;


public class ThreeDMediaHandler extends VisualMediaHandler {
    ThreeDMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public AtomContainer getNamedObjectList() throws StdQTException {
        return AtomContainer.fromThreeDMediaHandlerObject(this);
    }

    public AtomContainer getRendererList() throws StdQTException {
        return AtomContainer.fromThreeDMediaHandlerRenderer(this);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/ThreeDMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
