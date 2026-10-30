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
    public static short QTCreateStandardParameterDialog(int effectList, int parameters, int dialogOptions, int[] createdDialog) {
        throw new UnsupportedToolboxException("QTCreateStandardParameterDialog");
    }

    /** used by ParameterDialog */
    public static short QTDismissStandardParameterDialog(int createdDialog) {
        throw new UnsupportedToolboxException("QTDismissStandardParameterDialog");
    }

    /** used by ParameterDialog */
    public static short QTGetEffectSpeed(int parameters, int[] pFPS) {
        throw new UnsupportedToolboxException("QTGetEffectSpeed");
    }

    /** used by EffectsList */
    public static short QTGetEffectsList(int[] returnedList, int minSources, int maxSources, int getOptions) {
        throw new UnsupportedToolboxException("QTGetEffectsList");
    }

    /** used by ParameterDialog */
    public static short QTIsStandardParameterDialogEvent(byte[] pEvent, int createdDialog) {
        throw new UnsupportedToolboxException("QTIsStandardParameterDialogEvent");
    }

    /** used by ParameterDialog */
    public static short QTStandardParameterDialogDoAction(int createdDialog, int action, byte[] params) {
        throw new UnsupportedToolboxException("QTStandardParameterDialogDoAction");
    }

    /** used by ParameterDialog */
    public static short QTStandardParameterDialogDoAction(int createdDialog, int action, int[] params) {
        throw new UnsupportedToolboxException("QTStandardParameterDialogDoAction");
    }

}
