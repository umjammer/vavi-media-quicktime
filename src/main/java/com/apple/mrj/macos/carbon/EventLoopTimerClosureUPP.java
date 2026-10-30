package com.apple.mrj.macos.carbon;

import quicktime.jdirect.MethodClosure;


class EventLoopTimerClosureUPP extends MethodClosure {

    EventLoopTimerClosureUPP(Object target) {
        super(target, "EventLoopTimer", "(II)V", 0);
    }

    @Override
    protected void dispose() {
    }

    int getClosure() {
        return this.closure;
    }
}
