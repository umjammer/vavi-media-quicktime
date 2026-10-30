package quicktime.std.movies.media;

import quicktime.QTObject;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;


/**
 * The DataHandler class implements the corresponding data structure of the Movie Toolbox.
 */
public final class DataHandler extends Component implements QuickTimeLib {

    DataHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * This constructor creates a DataHandler object for the specified data reference.
     * @see "QuickTime::GetDataHandler()"
     * @param dataRef Specifies the data reference.
     * @param flags Specifies the control flags for this method.
     */
    public DataHandler(DataRef dataRef, int flags) throws StdQTException {
        this(allocate(dataRef, flags), null);
    }

    private static int allocate(DataRef dataRef, int flags) throws StdQTException {
        int i = GetDataHandler(QTObject.ID(dataRef), dataRef.getType(), flags);
        if (i == 0)
            throw new StdQTException(63524);
        int[] ci = new int[1];
        StdQTException.checkError(OpenAComponent(i, ci));
        return ci[0];
    }

    private static int GetDataHandler(int dataRef, int dataHandlerSubType, int flags) {
        return ComponentManager.GetDataHandler(dataRef, dataHandlerSubType, flags);
    }

    private static short OpenAComponent(int component, int[] ci) {
        return ComponentManager.OpenAComponent(component, ci);
    }
}
