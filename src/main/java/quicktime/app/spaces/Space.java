
package quicktime.app.spaces;

import java.util.Enumeration;

import quicktime.QTException;
import quicktime.app.time.Ticklish;
import quicktime.app.time.Timer;


/**
 * @deprecated
 */
public abstract interface Space extends Listener, DynamicCollection, Ticklish {
    /**
     * @deprecated
     */
    public abstract void addController(Controller paramController) throws QTException;

    /**
     * @deprecated
     */
    public abstract void removeController(Controller paramController) throws QTException;

    /**
     * @deprecated
     */
    public abstract Enumeration controllers();

    /**
     * @deprecated
     */
    public abstract Enumeration tickleList();

    /**
     * @deprecated
     */
    public abstract Timer getTimer();

    /**
     * @deprecated
     */
    public abstract Object getSource();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * spaces/Space.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
