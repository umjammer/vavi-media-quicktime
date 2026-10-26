package quicktime.internal.jdirect;

import quicktime.internal.toolbox.Closures;


/**
 * A Java method bound to a procedure pointer the emulated QuickTime toolbox can call back.
 *
 * @see Closures
 */
public class MethodClosure {

    private int proc;

    /**
     * @param target the object receiving the callback
     * @param methodName the name of the callback method
     * @param descriptor the JNI descriptor of the callback method
     */
    public MethodClosure(Object target, String methodName, String descriptor) {
        this.proc = Closures.newClosure(target, methodName, descriptor);
    }

    public void dispose() {
        Closures.dispose(proc);
        proc = 0;
    }

    /** @return the procedure pointer */
    public int getProc() {
        return proc;
    }
}
