
package quicktime.jdirect;

final class ProcInfo {
    static final int kPascalStackBased = 0;
    static final int kCStackBased = 1;
    static final int kRegisterBased = 2;
    static final int kD0DispatchedPascalStackBased = 8;
    static final int kD1DispatchedPascalStackBased = 12;
    static final int kD0DispatchedCStackBased = 9;
    static final int kStackDispatchedPascalStackBased = 14;
    static final int kThinkCStackBased = 5;
    static final int kRegisterD0 = 0;
    static final int kRegisterD1 = 1;
    static final int kRegisterD2 = 2;
    static final int kRegisterD3 = 3;
    static final int kRegisterD4 = 8;
    static final int kRegisterD5 = 9;
    static final int kRegisterD6 = 10;
    static final int kRegisterD7 = 11;
    static final int kRegisterA0 = 4;
    static final int kRegisterA1 = 5;
    static final int kRegisterA2 = 6;
    static final int kRegisterA3 = 7;
    static final int kRegisterA4 = 12;
    static final int kRegisterA5 = 13;
    static final int kRegisterA6 = 14;
    static final int kCCRegisterCBit = 16;
    static final int kCCRegisterVBit = 17;
    static final int kCCRegisterZBit = 18;
    static final int kCCRegisterNBit = 19;
    static final int kCCRegisterXBit = 20;

    private static final int kNoByteCode = 0;
    private static final int kOneByteCode = 1;

    static final int RESULT_SIZE(int paramInt) {
        return SIZE_CODE(paramInt) << 4;
    }

    static final int STACK_ROUTINE_PARAMETER(int paramInt1, int paramInt2) {
        return SIZE_CODE(paramInt2) << 6 + (paramInt1 - 1) * 2;
    }

    static final int DISPATCHED_STACK_ROUTINE_PARAMETER(int paramInt1, int paramInt2) {
        return SIZE_CODE(paramInt2) << 8 + (paramInt1 - 1) * 2;
    }

    static final int DISPATCHED_STACK_ROUTINE_SELECTOR_SIZE(int paramInt) {
        return SIZE_CODE(paramInt) << 6;
    }

    static final int REGISTER_RESULT_LOCATION(int paramInt) {
        return paramInt << 6;
    }

    static final int REGISTER_ROUTINE_PARAMETER(int paramInt1, int paramInt2, int paramInt3) {
        return (SIZE_CODE(paramInt3) << 0 | paramInt2 << 2) << 11 + (paramInt1 - 1) * 5;
    }

    static final int COMPONENT_MGR_RESULT_SIZE(int paramInt) {
        return SIZE_CODE(paramInt) << 10;
    }

    static final int COMPONENT_MGR_PARAMETER(int paramInt1, int paramInt2) {
        return SIZE_CODE(paramInt2) << 12 + (paramInt1 - 1) * 2;
    }

    static final int SPECIAL_CASE_PROCINFO(int paramInt) {
        return 0xF | paramInt << 4;
    }

    private static final int kTwoByteCode = 2;
    private static final int kFourByteCode = 3;
    private static final int kCallingConventionWidth = 4;
    private static final int kCallingConventionPhase = 0;
    private static final int kCallingConventionMask = 15;
    private static final int kResultSizeWidth = 2;
    private static final int kResultSizePhase = 4;
    private static final int kResultSizeMask = 48;
    private static final int kStackParameterWidth = 2;
    private static final int kStackParameterPhase = 6;
    private static final int kStackParameterMask = -64;
    private static final int kRegisterResultLocationWidth = 5;
    private static final int kRegisterResultLocationPhase = 6;
    private static final int kRegisterParameterWidth = 5;
    private static final int kRegisterParameterPhase = 11;
    private static final int kRegisterParameterMask = 2147481600;
    private static final int kRegisterParameterSizePhase = 0;
    private static final int kRegisterParameterSizeWidth = 2;
    private static final int kRegisterParameterWhichPhase = 2;
    private static final int kRegisterParameterWhichWidth = 3;
    private static final int kDispatchedSelectorSizeWidth = 2;
    private static final int kDispatchedSelectorSizePhase = 6;
    private static final int kDispatchedParameterPhase = 8;
    private static final int kSpecialCaseSelectorWidth = 6;
    private static final int kSpecialCaseSelectorPhase = 4;
    private static final int kSpecialCaseSelectorMask = 1008;
    private static final int kComponentMgrResultSizeWidth = 2;
    private static final int kComponentMgrResultSizePhase = 10;
    private static final int kComponentMgrParameterWidth = 2;
    private static final int kComponentMgrParameterPhase = 12;
    private static final int kSpecialCase = 15;

    private static final int SIZE_CODE(int paramInt) {
        int i = 0;
        switch (paramInt) {
        case 4:
            i = 3;
            break;
        case 2:
            i = 2;
            break;
        case 1:
            i = 1;
        }
        return i;
    }
}
