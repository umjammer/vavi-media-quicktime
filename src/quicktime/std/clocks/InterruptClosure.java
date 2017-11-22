
package quicktime.std.clocks;

import com.apple.mrj.jdirect.CompletionRoutineNotifier;


class InterruptClosure extends CompletionRoutineNotifier {
    InterruptClosure(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    int fetchPointer() {
        return getPointer();
    }
}
