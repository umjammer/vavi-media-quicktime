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

    DataHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
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

    private static int allocate(DataRef paramDataRef, int paramInt) throws StdQTException {
        int i = GetDataHandler(QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
        if (i == 0)
            throw new StdQTException(63524);
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(OpenAComponent(i, arrayOfInt));
        return arrayOfInt[0];
    }

    private static int GetDataHandler(int paramInt1, int paramInt2, int paramInt3) {
        return ComponentManager.GetDataHandler(paramInt1, paramInt2, paramInt3);
    }

    private static short OpenAComponent(int paramInt, int[] paramArrayOfInt) {
        return ComponentManager.OpenAComponent(paramInt, paramArrayOfInt);
    }
}
