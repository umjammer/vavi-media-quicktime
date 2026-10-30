
package quicktime.std.clocks;

import com.apple.mrj.jdirect.CompletionRoutineNotifier;


class InterruptClosure extends CompletionRoutineNotifier {
    InterruptClosure(int routine, Object target) {
        super(routine, target);
    }

    int fetchPointer() {
        return getPointer();
    }
}
