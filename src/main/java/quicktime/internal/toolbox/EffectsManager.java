/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


/**
 * EffectsManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class EffectsManager {

    private EffectsManager() {
    }

    /** used by ParameterDialog */
    public static short QTCreateStandardParameterDialog(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTCreateStandardParameterDialog");
    }

    /** used by ParameterDialog */
    public static short QTDismissStandardParameterDialog(int paramInt) {
        throw new UnsupportedToolboxException("QTDismissStandardParameterDialog");
    }

    /** used by ParameterDialog */
    public static short QTGetEffectSpeed(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTGetEffectSpeed");
    }

    /** used by EffectsList */
    public static short QTGetEffectsList(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTGetEffectsList");
    }

    /** used by ParameterDialog */
    public static short QTIsStandardParameterDialogEvent(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("QTIsStandardParameterDialogEvent");
    }

    /** used by ParameterDialog */
    public static short QTStandardParameterDialogDoAction(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTStandardParameterDialogDoAction");
    }

    /** used by ParameterDialog */
    public static short QTStandardParameterDialogDoAction(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTStandardParameterDialogDoAction");
    }

}
