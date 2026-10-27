
package com.apple.mrj.macos.carbon;

import quicktime.jdirect.MethodClosure;


class EventLoopTimerClosureUPP extends MethodClosure {
    EventLoopTimerClosureUPP(Object paramObject) {
        super(paramObject, "EventLoopTimer", "(II)V", 0);
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

