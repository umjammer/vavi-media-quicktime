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

    private ParameterDialog(EffectsList paramEffectsList, AtomContainer paramAtomContainer, int paramInt) throws QTException {
        int[] arrayOfInt = { 0 };
        EffectsList localEffectsList = paramEffectsList;
        AtomContainer localAtomContainer = paramAtomContainer;
        int i = paramInt;
        if (QTSession.isCurrentOS(4)) {
            GetGWorld(savedPort, savedDevice);
            res = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                synchronized (QTNative.globalsLock) {
                    res = ParameterDialog.QTCreateStandardParameterDialog(QTObject.ID(localEffectsList), QTObject.ID(localAtomContainer), i, arrayOfInt);
                    ParameterDialog.SetGWorld(ParameterDialog.savedPort[0], ParameterDialog.savedDevice[0]);
                }
                return ParameterDialog.res;
            });
            StdQTException.checkError(res);
        } else {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(savedPort, savedDevice);
                res = QTCreateStandardParameterDialog(QTObject.ID(paramEffectsList), QTObject.ID(paramAtomContainer), paramInt, arrayOfInt);
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    SetGWorld(savedPort[0], savedDevice[0]);
                if (res != 0) {
                    if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                        SetGWorld(savedPort[0], savedDevice[0]);
                    StdQTException.checkError(res);
                }
            }
        }
        this.dialog = arrayOfInt[0];
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
        AtomContainer localAtomContainer;
        if (parameters == null) {
            localAtomContainer = new AtomContainer();
        } else {
            localAtomContainer = parameters.copyAtom(Atom.kParentIsContainer);
        }
        if (QTSession.isCurrentOS(4)) {
            AtomContainer x = localAtomContainer;
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
                ParameterDialog dialog = new ParameterDialog(effectsList, localAtomContainer, dialogOptions);
                dialog.setDialogTitle(title);
                dialog.setPreviewPictures(pictArray);
                res = dialog.runParameterDialog();
                if (res == 56550) {
                    return localAtomContainer.copyAtom(Atom.kParentIsContainer);
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
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(QTGetEffectSpeed(QTObject.ID(parameters), arrayOfInt));
        return arrayOfInt[0];
    }

    private void setDialogTitle(String paramString) {
        if (paramString != null) {
            QTStandardParameterDialogDoAction(this.dialog, 8, QTUtils.String2PString(paramString, 255));
        }
    }

    private void setPreviewPictures(Pict[] paramArrayOfPict) {
        if (paramArrayOfPict != null) {
            int[] arrayOfInt = new int[2];
            for (int i = 0; i < paramArrayOfPict.length; i++) {
                if (paramArrayOfPict[i] != null) {
                    arrayOfInt[0] = (i + 1);
                    arrayOfInt[1] = QTObject.ID(paramArrayOfPict[i]);
                    QTStandardParameterDialogDoAction(this.dialog, 6, arrayOfInt);
                }
            }
        }
    }

    private int runParameterDialog() throws QTException {
        int i = -128;
        byte[] arrayOfByte = new byte[16];
        int j = 1;
        int k = 0;
        Vector<Integer> localVector = null;
        do {
            GetNextEvent((short) -1, arrayOfByte);
            i = QTIsStandardParameterDialogEvent(arrayOfByte, this.dialog);
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
                    short s = (short) ((arrayOfByte[0] & 0xFF) << 8 | arrayOfByte[1] & 0xFF);
                    s = EndianOrder.flipNativeToBigEndian16(s);
                    if (s == 6) {
                        int m = (arrayOfByte[2] & 0xFF) << 24 | (arrayOfByte[3] & 0xFF) << 16 | (arrayOfByte[4] & 0xFF) << 8 | arrayOfByte[5] & 0xFF;
                        m = EndianOrder.flipNativeToBigEndian32(m);
                        if (m != 0) {
                            if (k == 0)
                                k = m;
                            if (k != m) {
                                BeginUpdate(m);
                                EndUpdate(m);
                                if (((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(2))) && (localVector == null)) {
                                    localVector = new Vector<>();
                                }
                                if (localVector != null) {
                                }
                                Integer localInteger = m;
                                if (!localVector.contains(localInteger)) {
                                    localVector.addElement(localInteger);
                                }
                            }
                        }
                    }
                    break;
            }
        } while (j != 0);
        if (localVector != null) {
            for (Enumeration<Integer> e = localVector.elements(); e.hasMoreElements(); ) {
                invalWindow(e.nextElement());
            }
            localVector.removeAllElements();
        }
        logger.log(Level.DEBUG, "QTDismissStandardParameterDialog " + i);
        QTDismissStandardParameterDialog(this.dialog);
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            SetGWorld(savedPort[0], savedDevice[0]);
        }
        return i;
    }

    private static void invalWindow(int paramInt) {
        short[] arrayOfShort = new short[4];
        arrayOfShort[0] = (arrayOfShort[1] = 0);
        arrayOfShort[2] = (arrayOfShort[3] = 4095);
        if (QTSession.isCurrentOS(1)) {
            SetPort(paramInt);
            InvalRect(arrayOfShort);
        } else if (QTSession.isCurrentOS(2)) {
            MacSetPort(paramInt);
            InvalRect(arrayOfShort);
        }
    }

    private ParameterDialog() {
    }

    private static short QTCreateStandardParameterDialog(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return EffectsManager.QTCreateStandardParameterDialog(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static short QTStandardParameterDialogDoAction(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return EffectsManager.QTStandardParameterDialogDoAction(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short QTStandardParameterDialogDoAction(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return EffectsManager.QTStandardParameterDialogDoAction(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short QTIsStandardParameterDialogEvent(byte[] paramArrayOfByte, int paramInt) {
        return EffectsManager.QTIsStandardParameterDialogEvent(paramArrayOfByte, paramInt);
    }

    private static short QTDismissStandardParameterDialog(int paramInt) {
        return EffectsManager.QTDismissStandardParameterDialog(paramInt);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short QTGetEffectSpeed(int paramInt, int[] paramArrayOfInt) {
        return EffectsManager.QTGetEffectSpeed(paramInt, paramArrayOfInt);
    }

    private static boolean GetNextEvent(short paramShort, byte[] paramArrayOfByte) {
        return PlatformManager.GetNextEvent(paramShort, paramArrayOfByte);
    }

    private static void BeginUpdate(int paramInt) {
        QuickDrawManager.BeginUpdate(paramInt);
    }

    private static void EndUpdate(int paramInt) {
        QuickDrawManager.EndUpdate(paramInt);
    }

    private static void SetPort(int paramInt) {
        QuickDrawManager.SetPort(paramInt);
    }

    private static void MacSetPort(int paramInt) {
        QuickDrawManager.MacSetPort(paramInt);
    }

    private static void InvalRect(short[] paramArrayOfShort) {
        QuickDrawManager.InvalRect(paramArrayOfShort);
    }
}
