package quicktime.jdirect;

import com.apple.mrj.jdirect.MethodClosureUPP;
import quicktime.QTSession;
import quicktime.internal.toolbox.PlatformManager;

public class MethodClosure {

    protected int closure;

    private Object macProc;

    protected MethodClosure(Object target, String methodName, String descriptor, int procInfo) {
        if (target == this) {
            throw new RuntimeException("MethodClosure cannot have this as the callback object");
        }
        if (QTSession.isCurrentOS(2)) {
            this.closure = NewMethodClosure(target, methodName, descriptor);
        } else if (QTSession.isCurrentOS(1)) {
            this.macProc = new MCUPP(target, methodName, descriptor, procInfo);
            this.closure = ((MethodClosureUPP) this.macProc).getProc();
        } else if (QTSession.isCurrentOS(4)) {
            if (QTSession.getJavaVersion() >= 65540) {
                this.macProc = new MCX_14(target, methodName, descriptor);
                this.closure = ((quicktime.internal.jdirect.MethodClosure) this.macProc).getProc();
            } else {
                this.macProc = new MCX(target, methodName, descriptor);
                this.closure = ((com.apple.mrj.jdirect.MethodClosure) this.macProc).getProc();
            }
        }
    }

    protected void dispose() throws Throwable {
        if (this.closure != 0) {
            if (QTSession.isCurrentOS(1)) {
                ((MethodClosureUPP) this.macProc).dispose();
            } else if (QTSession.isCurrentOS(4)) {
                if (QTSession.getJavaVersion() >= 65540) {
                    ((quicktime.internal.jdirect.MethodClosure) this.macProc).dispose();
                } else
                    ((com.apple.mrj.jdirect.MethodClosure) this.macProc).dispose();
            } else
                DisposeMethodClosure(this.closure);
            this.closure = 0;
        }
    }

    private static int NewMethodClosure(Object target, String name, String descriptor) {
        return PlatformManager.NewMethodClosure(target, name, descriptor);
    }

    private static void DisposeMethodClosure(int proc) {
        PlatformManager.DisposeMethodClosure(proc);
    }
}
