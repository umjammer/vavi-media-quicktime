package quicktime.jdirect;

import com.apple.mrj.jdirect.MethodClosureUPP;
import quicktime.QTSession;

public class MethodClosure {

    protected int closure;

    private Object macProc;

    protected MethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
        if (paramObject == this) {
            throw new RuntimeException("MethodClosure cannot have this as the callback object");
        }
        if (QTSession.isCurrentOS(2)) {
            this.closure = NewMethodClosure(paramObject, paramString1, paramString2);
        } else if (QTSession.isCurrentOS(1)) {
            this.macProc = new MCUPP(paramObject, paramString1, paramString2, paramInt);
            this.closure = ((MethodClosureUPP) this.macProc).getProc();
        } else if (QTSession.isCurrentOS(4)) {
            if (QTSession.getJavaVersion() >= 65540) {
                this.macProc = new MCX_14(paramObject, paramString1, paramString2);
                this.closure = ((quicktime.internal.jdirect.MethodClosure) this.macProc).getProc();
            } else {
                this.macProc = new MCX(paramObject, paramString1, paramString2);
                this.closure = ((com.apple.mrj.jdirect.MethodClosure) this.macProc).getProc();
            }
        }
    }

    protected final void finalize() throws Throwable {
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

    private static native int NewMethodClosure(Object paramObject, String paramString1, String paramString2);

    private static native void DisposeMethodClosure(int paramInt);
}
