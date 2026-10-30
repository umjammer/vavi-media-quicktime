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

    protected Component(ComponentIdentifier componentIdentifier, int type) throws QTException {
        this(allocate(componentIdentifier, type), null);
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

    protected Component(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    protected Component(int component) throws StdQTException {
        super(allocate(component), null);
    }

    private static int allocate(ComponentIdentifier componentIdentifier, int type) throws StdQTException {
        if (componentIdentifier.getInfo().getType() != type)
            throw new StdQTException(62536);
        return allocate(QTObject.ID(componentIdentifier));
    }

    private static int allocate(int componentType, int componentSubType) throws StdQTException {
        int[] ci = new int[1];
        StdQTException.checkError(OpenADefaultComponent(componentType, componentSubType, ci));
        return ci[0];
    }

    private static int allocate(int component) throws StdQTException {
        int[] ci = new int[1];
        StdQTException.checkError(OpenAComponent(component, ci));
        return ci[0];
    }

    /**
     * Determines the current number of open connections managed by the Component.
     * @see "@see "QuickTime::CountComponentInstances""
     * @return The number of component instances of the specified Component.
     */
    public final int count() {
        return CountComponentInstances(_ID());
    }

    private static short OpenADefaultComponent(int componentType, int componentSubType, int[] ci) {
        return ComponentManager.OpenADefaultComponent(componentType, componentSubType, ci);
    }

    private static short OpenAComponent(int component, int[] ci) {
        return ComponentManager.OpenAComponent(component, ci);
    }

    private static int CountComponentInstances(int component) {
        return ComponentManager.CountComponentInstances(component);
    }
}
