package quicktime.std.comp;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;


/**
 * Representation of an open ComponentInstance.
 * Sub classes define specific ComponentInstance types.
 */
public class Component extends ComponentIdentifier implements QuickTimeLib {

    /**
     * Given a component identifier it opens the specified component.
     * @see "QuickTime::OpenAComponent"
     * @param comp the component identifier to open
     */
    public Component(ComponentIdentifier comp) throws StdQTException {
        this(allocate(QTObject.ID(comp)), null);
    }

    protected Component(ComponentIdentifier paramComponentIdentifier, int paramInt) throws QTException {
        this(allocate(paramComponentIdentifier, paramInt), null);
    }

    /**
     * Opens the specified component.
     * @see "@see "QuickTime::OpenADefaultComponent""
     * @param type the component type
     * @param subType the component sub type
     * @throws quicktime.std.StdQTException Possible component allocation error.
     */
    public Component(int type, int subType) throws QTException {
        this(allocate(type, subType), null);
    }

    protected Component(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    protected Component(int paramInt) throws StdQTException {
        super(allocate(paramInt), null);
    }

    private static int allocate(ComponentIdentifier paramComponentIdentifier, int paramInt) throws StdQTException {
        if (paramComponentIdentifier.getInfo().getType() != paramInt)
            throw new StdQTException(62536);
        return allocate(QTObject.ID(paramComponentIdentifier));
    }

    private static int allocate(int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(OpenADefaultComponent(paramInt1, paramInt2, arrayOfInt));
        return arrayOfInt[0];
    }

    private static int allocate(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(OpenAComponent(paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Determines the current number of open connections managed by the Component.
     * @see "@see "QuickTime::CountComponentInstances""
     * @return The number of component instances of the specified Component.
     */
    public final int count() {
        return CountComponentInstances(_ID());
    }

    private static short OpenADefaultComponent(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ComponentManager.OpenADefaultComponent(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short OpenAComponent(int paramInt, int[] paramArrayOfInt) {
        return ComponentManager.OpenAComponent(paramInt, paramArrayOfInt);
    }

    private static int CountComponentInstances(int paramInt) {
        return ComponentManager.CountComponentInstances(paramInt);
    }
}
