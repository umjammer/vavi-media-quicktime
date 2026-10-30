
package quicktime.jdirect;

import com.apple.mrj.jdirect.MethodClosureUPP;


class MCUPP extends MethodClosureUPP {
    MCUPP(Object target, String methodName, String descriptor, int procInfo) {
        super(target, methodName, descriptor, procInfo);
    }
}
