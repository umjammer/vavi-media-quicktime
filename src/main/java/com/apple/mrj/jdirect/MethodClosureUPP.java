package com.apple.mrj.jdirect;


public class MethodClosureUPP implements MethodClosure {

    /**
     * @param target
     * @param methodName
     * @param descriptor
     * @param procInfo
     */
    public MethodClosureUPP(Object target, String methodName, String descriptor, int procInfo) {
    }

    /**
     * @return
     */
    @Override
    public int getProc() {
        return 0;
    }

    /**
     * 
     */
    @Override
    public void dispose() {
    }
}
