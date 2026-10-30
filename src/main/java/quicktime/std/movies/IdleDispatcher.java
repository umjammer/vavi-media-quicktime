
package quicktime.std.movies;

import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;


final class IdleDispatcher implements PrimitivesLib, InterfaceLib {

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private IdleMethodClosure mMethodClosure;

    private final NextTaskNeededSooner tns;

    IdleDispatcher(NextTaskNeededSooner tns) {
        this.methodName = "NewQTNextTaskNeededSoonerCallbackProc";
        this.methodSig = "(III)S";
        this.procInfo = 240;
        this.tns = tns;
    }

    private short NewQTNextTaskNeededSoonerCallbackProc(int duration, int flags, int refcon) {
        return (short) this.tns.execute(duration, flags);
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new IdleMethodClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanupMethodClosure() {
        this.mMethodClosure = null;
    }

    static class IdleMethodClosure extends MethodClosure {
        IdleMethodClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }
}
