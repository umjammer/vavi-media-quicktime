package quicktime.std.movies;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Enumeration;
import java.util.Vector;

import com.apple.mrj.macos.carbon.CarbonAccess;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.EffectsManager;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.Pict;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTUtils;


/**
 * This class creates an AtomContainer that contains the necessary
 * information for displaying a list of effects
 */
public final class ParameterDialog implements QuickTimeLib {

    private static final Logger logger = System.getLogger(ParameterDialog.class.getName());

    private static final boolean accumulateUpdates = true;

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    private static int res = 0;

    private int dialog;

    private ParameterDialog(EffectsList effectList, AtomContainer parameters, int dialogOptions) throws QTException {
        int[] createdDialog = { 0 };
        EffectsList effectsList = effectList;
        AtomContainer atomContainer = parameters;
        int i = dialogOptions;
        if (QTSession.isCurrentOS(4)) {
            GetGWorld(savedPort, savedDevice);
            res = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                synchronized (QTNative.globalsLock) {
                    res = ParameterDialog.QTCreateStandardParameterDialog(QTObject.ID(effectsList), QTObject.ID(atomContainer), i, createdDialog);
                    ParameterDialog.SetGWorld(ParameterDialog.savedPort[0], ParameterDialog.savedDevice[0]);
                }
                return ParameterDialog.res;
            });
            StdQTException.checkError(res);
        } else {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(savedPort, savedDevice);
                res = QTCreateStandardParameterDialog(QTObject.ID(effectList), QTObject.ID(parameters), dialogOptions, createdDialog);
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    SetGWorld(savedPort[0], savedDevice[0]);
                if (res != 0) {
                    if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                        SetGWorld(savedPort[0], savedDevice[0]);
                    StdQTException.checkError(res);
                }
            }
        }
        this.dialog = createdDialog[0];
    }

    /**
     * This call shows the dialog with the specified parameters. It shows this dialog as
     * a modal dialog. This currently does not support the featureUnsuppported result, which
     * will throw an exception. When this function is complete the
     * @see "QuickTime::QTCreateStandardParameterDialog"
     * @see "QuickTime::QTIsStandardParameterDialogEvent"
     * @see "QuickTime::QTDismissStandardParameterDialog"
     * @param effectsList this passes to the create dialog routine the
     *  parameters that will govern the effects that are displayed
     * @param dialogOptions options of type StdQTConstants.pdOptions...
     * @return the completed atom container that contains all of the information to display the effect.
     * @throws quicktime.QTException
     */
    public static AtomContainer showParameterDialog(AtomContainer effectsList, int dialogOptions) throws QTException {
        int[] ar = { 0 };
        AtomContainer parameters = new AtomContainer();
        int i;
        if (QTSession.isCurrentOS(4)) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            i = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int i1 = 0;
                synchronized (QTNative.globalsLock) {
                    logger.log(Level.DEBUG, "QTCreateStandardParameterDialog " + ParameterDialog.res);
                    i1 = ParameterDialog.QTCreateStandardParameterDialog(QTObject.ID(effectsList), QTObject.ID(parameters), dialogOptions, ar);
                    if (i1 == 0) {
                        ParameterDialog.res = ParameterDialog.QTIsStandardParameterDialogEvent(null, ar[0]);
                        ParameterDialog.QTDismissStandardParameterDialog(ar[0]);
                    }
                }
                return i1;
            });
            StdQTException.checkError(i);
            res = i;
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
            StdQTException.checkError(res);
        } else {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    GetGWorld(savedPort, savedDevice);
                }
                i = QTCreateStandardParameterDialog(QTObject.ID(effectsList), QTObject.ID(parameters), dialogOptions, ar);
                if (i == 0) {
                    res = QTIsStandardParameterDialogEvent(null, ar[0]);
                    QTDismissStandardParameterDialog(ar[0]);
                }
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    SetGWorld(savedPort[0], savedDevice[0]);
                }
                if (i != 0) {
                    StdQTException.checkError(i);
                }
            }
        }
        res = res == 56550 ? 0 : res;
        StdQTException.checkError(res);
        return parameters;
    }

    /**
     * This call shows the effects parameter dialog with the specified parameters.
     * It shows the effects parameter dialog as a modal dialog.
     * @see "QuickTime::QTCreateStandardParameterDialog"
     * @see "QuickTime::QTStandardParameterDialogDoAction"
     * @see "QuickTime::QTIsStandardParameterDialogEvent"
     * @see "QuickTime::QTDismissStandardParameterDialog"
     * @param effectsList governs the list of effects that are displayed in the dialog
     * @param dialogOptions options of type StdQTConstants.pdOptions...
     * @param parameters any previous effects parameter AtomContainer, or null
     * @param title the title of the parameter dialog, or null
     * @param pictArray array of preview picture(s) used in the dialog, or null
     * @return the AtomContainer containing the information to display the effect
     * @throws quicktime.QTException throws on userCanceledErr or if an error occurred
     */
    public static AtomContainer showParameterDialog(EffectsList effectsList, int dialogOptions, AtomContainer parameters, String title, Pict[] pictArray) throws QTException {
        AtomContainer atomContainer;
        if (parameters == null) {
            atomContainer = new AtomContainer();
        } else {
            atomContainer = parameters.copyAtom(Atom.kParentIsContainer);
        }
        if (QTSession.isCurrentOS(4)) {
            AtomContainer x = atomContainer;
            int i = dialogOptions;
            ParameterDialog dialog = new ParameterDialog(effectsList, x, i);
            res = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                synchronized (QTNative.globalsLock) {
                    try {
                        dialog.setDialogTitle(title);
                        dialog.setPreviewPictures(pictArray);
                        ParameterDialog.res = dialog.runParameterDialog();
                    } catch (QTException _) {
                    }
                }
                return ParameterDialog.res;
            });
            if (res == 56550)
                return x.copyAtom(Atom.kParentIsContainer);
        } else {
            synchronized (QTNative.globalsLock) {
                ParameterDialog dialog = new ParameterDialog(effectsList, atomContainer, dialogOptions);
                dialog.setDialogTitle(title);
                dialog.setPreviewPictures(pictArray);
                res = dialog.runParameterDialog();
                if (res == 56550) {
                    return atomContainer.copyAtom(Atom.kParentIsContainer);
                }
            }
        }
        StdQTException.checkError(res);
        return null;
    }

    /**
     * This call returns the speed of the effect, expressed in frames-per-second
     * @see "QuickTime::QTGetEffectSpeed"
     * @param parameters the AtomContainer specifying the effect
     * @return the speed of the effect in frames per second
     *  (an estimate of execution speed on a reference hardware platform)
     * @throws quicktime.QTException
     */
    public static int getEffectsSpeed(AtomContainer parameters) throws QTException {
        int[] pFPS = { 0 };
        StdQTException.checkError(QTGetEffectSpeed(QTObject.ID(parameters), pFPS));
        return pFPS[0];
    }

    private void setDialogTitle(String dialogTitle) {
        if (dialogTitle != null) {
            QTStandardParameterDialogDoAction(this.dialog, 8, QTUtils.String2PString(dialogTitle, 255));
        }
    }

    private void setPreviewPictures(Pict[] previewPictures) {
        if (previewPictures != null) {
            int[] params = new int[2];
            for (int i = 0; i < previewPictures.length; i++) {
                if (previewPictures[i] != null) {
                    params[0] = (i + 1);
                    params[1] = QTObject.ID(previewPictures[i]);
                    QTStandardParameterDialogDoAction(this.dialog, 6, params);
                }
            }
        }
    }

    private int runParameterDialog() throws QTException {
        int i = -128;
        byte[] theEvent = new byte[16];
        int j = 1;
        int k = 0;
        Vector<Integer> vector = null;
        do {
            GetNextEvent((short) -1, theEvent);
            i = QTIsStandardParameterDialogEvent(theEvent, this.dialog);
            switch(i) {
                case -8986:
                    j = 0;
                    break;
                case -128:
                    j = 0;
                    break;
                case 0:
                    break;
                case -2053:
                    short s = (short) ((theEvent[0] & 0xFF) << 8 | theEvent[1] & 0xFF);
                    s = EndianOrder.flipNativeToBigEndian16(s);
                    if (s == 6) {
                        int m = (theEvent[2] & 0xFF) << 24 | (theEvent[3] & 0xFF) << 16 | (theEvent[4] & 0xFF) << 8 | theEvent[5] & 0xFF;
                        m = EndianOrder.flipNativeToBigEndian32(m);
                        if (m != 0) {
                            if (k == 0)
                                k = m;
                            if (k != m) {
                                BeginUpdate(m);
                                EndUpdate(m);
                                if (((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(2))) && (vector == null)) {
                                    vector = new Vector<>();
                                }
                                if (vector != null) {
                                }
                                Integer localInteger = m;
                                if (!vector.contains(localInteger)) {
                                    vector.addElement(localInteger);
                                }
                            }
                        }
                    }
                    break;
            }
        } while (j != 0);
        if (vector != null) {
            for (Enumeration<Integer> e = vector.elements(); e.hasMoreElements(); ) {
                invalWindow(e.nextElement());
            }
            vector.removeAllElements();
        }
        logger.log(Level.DEBUG, "QTDismissStandardParameterDialog " + i);
        QTDismissStandardParameterDialog(this.dialog);
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            SetGWorld(savedPort[0], savedDevice[0]);
        }
        return i;
    }

    private static void invalWindow(int port) {
        short[] rect = new short[4];
        rect[0] = (rect[1] = 0);
        rect[2] = (rect[3] = 4095);
        if (QTSession.isCurrentOS(1)) {
            SetPort(port);
            InvalRect(rect);
        } else if (QTSession.isCurrentOS(2)) {
            MacSetPort(port);
            InvalRect(rect);
        }
    }

    private ParameterDialog() {
    }

    private static short QTCreateStandardParameterDialog(int effectList, int parameters, int dialogOptions, int[] createdDialog) {
        return EffectsManager.QTCreateStandardParameterDialog(effectList, parameters, dialogOptions, createdDialog);
    }

    private static short QTStandardParameterDialogDoAction(int createdDialog, int action, int[] params) {
        return EffectsManager.QTStandardParameterDialogDoAction(createdDialog, action, params);
    }

    private static short QTStandardParameterDialogDoAction(int createdDialog, int action, byte[] params) {
        return EffectsManager.QTStandardParameterDialogDoAction(createdDialog, action, params);
    }

    private static short QTIsStandardParameterDialogEvent(byte[] pEvent, int createdDialog) {
        return EffectsManager.QTIsStandardParameterDialogEvent(pEvent, createdDialog);
    }

    private static short QTDismissStandardParameterDialog(int createdDialog) {
        return EffectsManager.QTDismissStandardParameterDialog(createdDialog);
    }

    private static void SetGWorld(int port, int gdh) {
        QuickDrawManager.SetGWorld(port, gdh);
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }

    private static short QTGetEffectSpeed(int parameters, int[] pFPS) {
        return EffectsManager.QTGetEffectSpeed(parameters, pFPS);
    }

    private static boolean GetNextEvent(short eventMask, byte[] theEvent) {
        return PlatformManager.GetNextEvent(eventMask, theEvent);
    }

    private static void BeginUpdate(int window) {
        QuickDrawManager.BeginUpdate(window);
    }

    private static void EndUpdate(int window) {
        QuickDrawManager.EndUpdate(window);
    }

    private static void SetPort(int port) {
        QuickDrawManager.SetPort(port);
    }

    private static void MacSetPort(int port) {
        QuickDrawManager.MacSetPort(port);
    }

    private static void InvalRect(short[] rect) {
        QuickDrawManager.InvalRect(rect);
    }
}
