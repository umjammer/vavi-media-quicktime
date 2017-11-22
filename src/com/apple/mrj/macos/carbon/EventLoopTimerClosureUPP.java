
package com.apple.mrj.macos.carbon;

import quicktime.jdirect.MethodClosure;


class EventLoopTimerClosureUPP extends MethodClosure {
    EventLoopTimerClosureUPP(Object paramObject) {
        super(paramObject, "run", "()V", 0);
    }

    void dispose() {
        try {
            super.finalize();
        } catch (Throwable localThrowable) {
        }
    }

    int getClosure() {
        return this.closure;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/com/apple/mrj/macos
 * /carbon/EventLoopTimerClosureUPP.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
