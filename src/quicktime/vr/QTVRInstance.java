
package quicktime.vr;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.jdirect.QuickTimeVRLib;
import quicktime.qd.QDPoint;
import quicktime.qd.Region;
import quicktime.std.movies.Atom;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.Track;


public final class QTVRInstance extends QTObject implements QuickTimeLib, QuickTimeVRLib, PrimitivesLib, InterfaceLib {
    private static Object linkage;

    private VRDispatcher enUPP;

    private VRDispatcher leaveUPP;

    private VRDispatcher mohsUPP;

    private VRDispatcher icUPP;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QTVRInstance.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public QTVRInstance(Track paramTrack, MovieController paramMovieController) throws QTException {
        super(allocate(paramTrack, paramMovieController));
    }

    private static int allocate(Track paramTrack, MovieController paramMovieController) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetQTVRInstance(arrayOfInt, QTObject.ID(paramTrack), QTObject.ID(paramMovieController));
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    QTVRInstance(int paramInt) {
        super(paramInt);
    }

    private Hashtable<Integer, VRDispatcher> ipProcs = new Hashtable<>();

    public AtomContainer getVRWorld() throws QTVRException {
        return AtomContainer.fromQTVRInstanceWorld(this);
    }

    public AtomContainer getNodeInfo(int paramInt) throws QTVRException {
        return AtomContainer.fromQTVRInstanceNode(this, paramInt);
    }

    public void triggerHotSpot(int paramInt, AtomContainer paramAtomContainer, Atom paramAtom) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRTriggerHotSpot(_ID(),
                                   paramInt,
                                   QTObject.ID(paramAtomContainer),
                                   paramAtom == null ? 0 : paramAtom.getAtom());
        }
        QTVRException.checkError(i);
    }

    public void enableHotSpot(int paramInt1, int paramInt2, boolean paramBoolean) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVREnableHotSpot(_ID(), paramInt1, paramInt2, (byte) (paramBoolean ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    public void update(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRUpdate(_ID(), paramInt);
        }
        QTVRException.checkError(i);
    }

    public void goToNodeID(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGoToNodeID(_ID(), paramInt);
        }
        QTVRException.checkError(i);
    }

    public void nudge(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRNudge(_ID(), paramInt);
        }
        QTVRException.checkError(i);
    }

    public void setPanAngle(float paramFloat) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetPanAngle(_ID(), paramFloat);
        }
        QTVRException.checkError(i);
    }

    public void setTiltAngle(float paramFloat) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetTiltAngle(_ID(), paramFloat);
        }
        QTVRException.checkError(i);
    }

    public void setFieldOfView(float paramFloat) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetFieldOfView(_ID(), paramFloat);
        }
        QTVRException.checkError(i);
    }

    public float getConstraints_min(int paramInt) throws QTVRException {
        float[] arrayOfFloat1 = {
            0.0F
        };
        float[] arrayOfFloat2 = {
            0.0F
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetConstraints(_ID(), (short) paramInt, arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat1[0];
    }

    public float getConstraints_max(int paramInt) throws QTVRException {
        float[] arrayOfFloat1 = {
            0.0F
        };
        float[] arrayOfFloat2 = {
            0.0F
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetConstraints(_ID(), (short) paramInt, arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat2[0];
    }

    public void setConstraints(int paramInt, float paramFloat1, float paramFloat2) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetConstraints(_ID(), (short) paramInt, paramFloat1, paramFloat2);
        }
        QTVRException.checkError(i);
    }

    public void setImagingProperty(int paramInt1, int paramInt2, int paramInt3) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetImagingProperty(_ID(), paramInt1, paramInt2, paramInt3);
        }
        QTVRException.checkError(i);
    }

    public int getImagingProperty(int paramInt1, int paramInt2) throws QTVRException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetImagingProperty(_ID(), paramInt1, paramInt2, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    public void setAngularUnits(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetAngularUnits(_ID(), paramInt);
        }
        QTVRException.checkError(i);
    }

    public float ptToPanAngle(QDPoint paramQDPoint) throws QTVRException {
        float[] arrayOfFloat1 = {
            0.0F
        };
        float[] arrayOfFloat2 = {
            0.0F
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToAngles(_ID(), paramQDPoint.getPoint(), arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat1[0];
    }

    public float ptToTiltAngle(QDPoint paramQDPoint) throws QTVRException {
        float[] arrayOfFloat1 = {
            0.0F
        };
        float[] arrayOfFloat2 = {
            0.0F
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToAngles(_ID(), paramQDPoint.getPoint(), arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat2[0];
    }

    public float wrapAndConstrain(int paramInt, float paramFloat) throws QTVRException {
        float[] arrayOfFloat = {
            0.0F
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRWrapAndConstrain(_ID(), (short) paramInt, paramFloat, arrayOfFloat);
        }
        QTVRException.checkError(i);
        return arrayOfFloat[0];
    }

    public void setEnteringNodeProc(QTVREnteringNode paramQTVREnteringNode, int paramInt) throws QTVRException {
        if (paramQTVREnteringNode == null) {
            removeEnteringNodeProc();
            return;
        }

        if (this.enUPP != null) {
            removeEnteringNodeProc();
        }
        this.enUPP = new VRDispatcher(this, paramQTVREnteringNode);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetEnteringNodeProc(_ID(), this.enUPP.ID(), 0, paramInt);
        }
        QTVRException.checkError(i);
    }

    public void removeEnteringNodeProc() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetEnteringNodeProc(_ID(), 0, 0, 0);
        }
        if (this.enUPP != null)
            this.enUPP.cleanupMethodClosure();
        this.enUPP = null;
    }

    public void setLeavingNodeProc(QTVRLeavingNode paramQTVRLeavingNode, int paramInt) throws QTVRException {
        if (paramQTVRLeavingNode == null) {
            removeLeavingNodeProc();
            return;
        }

        if (this.leaveUPP != null) {
            removeLeavingNodeProc();
        }
        this.leaveUPP = new VRDispatcher(this, paramQTVRLeavingNode);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetLeavingNodeProc(_ID(), this.leaveUPP.ID(), 0, paramInt);
        }
        QTVRException.checkError(i);
    }

    public void removeLeavingNodeProc() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetLeavingNodeProc(_ID(), 0, 0, 0);
        }
        if (this.leaveUPP != null)
            this.leaveUPP.cleanupMethodClosure();
        this.leaveUPP = null;
    }

    public void setMouseOverHotSpotProc(QTVRMouseOverHotSpot paramQTVRMouseOverHotSpot, int paramInt) throws QTVRException {
        if (paramQTVRMouseOverHotSpot == null) {
            removeMouseOverHotSpotProc();
            return;
        }

        if (this.mohsUPP != null) {
            removeMouseOverHotSpotProc();
        }
        this.mohsUPP = new VRDispatcher(this, paramQTVRMouseOverHotSpot);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseOverHotSpotProc(_ID(), this.mohsUPP.ID(), 0, paramInt);
        }
        QTVRException.checkError(i);
    }

    public void removeMouseOverHotSpotProc() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseOverHotSpotProc(_ID(), 0, 0, 0);
        }
        if (this.mohsUPP != null)
            this.mohsUPP.cleanupMethodClosure();
        this.mohsUPP = null;
    }

    public void installInterceptProc(int paramInt1, QTVRInterceptor paramQTVRInterceptor, int paramInt2) throws QTVRException {
        removeInterceptProc(paramInt1);

        if (paramQTVRInterceptor == null) {
            return;
        }
        VRDispatcher localVRDispatcher = new VRDispatcher(this, paramQTVRInterceptor);

        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRInstallInterceptProc(_ID(), paramInt1, localVRDispatcher.ID(), 0, paramInt2);
        }
        QTVRException.checkError(i);

        if (localVRDispatcher != null) {
            this.ipProcs.put(paramInt1, localVRDispatcher);
        }
    }

    public void removeInterceptProc(int paramInt) throws QTVRException {
        VRDispatcher localVRDispatcher = this.ipProcs.remove(paramInt);
        if (localVRDispatcher != null) {
            localVRDispatcher.cleanupMethodClosure();

            int i = 0;
            synchronized (QTNative.globalsLock) {
                i = QTVRInstallInterceptProc(_ID(), paramInt, 0, 0, 0);
            }
            QTVRException.checkError(i);
        }
    }

    public void callInterceptedProc(QTVRInterceptRecord paramQTVRInterceptRecord) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRCallInterceptedProc(_ID(), QTObject.ID(paramQTVRInterceptRecord));
        }
        QTVRException.checkError(i);
    }

    public void showDefaultView() throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRShowDefaultView(_ID());
        }
        QTVRException.checkError(i);
    }

    public void setViewCenter(QDPoint paramQDPoint) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewCenter(_ID(), paramQDPoint.getQTVRFloatPoint());
        }
        QTVRException.checkError(i);
    }

    public QDPoint getViewCenter() throws QTVRException {
        byte[] arrayOfByte = new byte[8];
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewCenter(_ID(), arrayOfByte);
        }

        QTVRException.checkError(i);
        return QDPoint.fromArray(arrayOfByte, -16);
    }

    public int getHotSpotType(int paramInt) throws QTVRException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetHotSpotType(_ID(), paramInt, arrayOfInt);
        }

        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    public void setPrescreenImagingCompleteProc(QTVRImagingComplete paramQTVRImagingComplete,
                                                int paramInt) throws QTVRException {
        if (paramQTVRImagingComplete == null) {
            removePrescreenImagingCompleteProc();
            return;
        }

        if (this.icUPP != null) {
            removePrescreenImagingCompleteProc();
        }
        this.icUPP = new VRDispatcher(this, paramQTVRImagingComplete);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetPrescreenImagingCompleteProc(_ID(), this.icUPP.ID(), 0, paramInt);
        }
        QTVRException.checkError(i);
    }

    public void removePrescreenImagingCompleteProc() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetPrescreenImagingCompleteProc(_ID(), 0, 0, 0);
        }
        if (this.icUPP != null)
            this.icUPP.cleanupMethodClosure();
        this.icUPP = null;
    }

    public Region getHotSpotRegion(int paramInt) throws QTException {
        Region localRegion = new Region();
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetHotSpotRegion(_ID(), paramInt, QTObject.ID(localRegion));
        }
        QTVRException.checkError(i);
        return localRegion;
    }

    public int[] getVisibleHotSpots() {
        int i = NewHandle(4);
        int j = 0;
        synchronized (QTNative.globalsLock) {
            j = QTVRGetVisibleHotSpots(_ID(), i);
        }
        int ai[] = new int[j];
        HLock(i);
        int k = getIntFromPointer(i, 0);
        for (int l = 0; l < j; l++)
            ai[l] = getIntFromPointer(k, l * 4);

        DisposeHandle(i);
        return ai;
    }

    public int ptToHotSpotID(QDPoint paramQDPoint) throws QTVRException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToHotSpotID(_ID(), paramQDPoint.getPoint(), arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    public void setMouseOverTracking(boolean paramBoolean) throws QTVRException {
        int i = 0;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseOverTracking(_ID(), (byte) (paramBoolean ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    public boolean getMouseOverTracking() throws QTVRException {
        int i;

        synchronized (QTNative.globalsLock) {
            i = QTVRGetMouseOverTracking(_ID());
        }

        return i == 1;
    }

    public void setMouseDownTracking(boolean paramBoolean) throws QTVRException {
        int i = 0;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseDownTracking(_ID(), (byte) (paramBoolean ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    public boolean getMouseDownTracking() throws QTVRException {
        int i;

        synchronized (QTNative.globalsLock) {
            i = QTVRGetMouseDownTracking(_ID());
        }

        return i == 1;
    }

    public void setInteractionMouseClickHysteresis(int paramInt) throws QTVRException {
        int i = 0;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 1, (short) paramInt);
        }
        QTVRException.checkError(i);
    }

    public int getInteractionMouseClickHysteresis() throws QTVRException {
        short[] arrayOfShort = {
            0
        };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 1, arrayOfShort);
        }
        QTVRException.checkError(i);
        return arrayOfShort[0];
    }

    public void setInteractionMouseClickTimeout(int paramInt) throws QTVRException {
        int i = 0;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 2, paramInt);
        }
        QTVRException.checkError(i);
    }

    public int getInteractionMouseClickTimeout() throws QTVRException {
        int i = 0;
        int[] arrayOfInt = {
            0
        };

        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 2, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    public void setInteractionPanTiltSpeed(int paramInt) throws QTVRException {
        int i;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 3, paramInt);
        }
        QTVRException.checkError(i);
    }

    public int getInteractionPanTiltSpeed() throws QTVRException {
        int i = 0;
        int[] arrayOfInt = {
            0
        };

        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 3, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    public void setInteractionZoomSpeed(int paramInt) throws QTVRException {
        int i;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 4, paramInt);
        }
        QTVRException.checkError(i);
    }

    public int getInteractionZoomSpeed() throws QTVRException {
        int[] arrayOfInt = {
            0
        };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 4, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    public void setInteractionTranslateOnMouseDown(boolean paramBoolean) throws QTVRException {
        int i;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 101, (byte) (paramBoolean ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    public boolean getInteractionTranslateOnMouseDown() throws QTVRException {
        byte[] arrayOfByte = {
            0
        };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 101, arrayOfByte);
        }
        QTVRException.checkError(i);
        return arrayOfByte[0] == 1;
    }

    public void setInteractionMouseMotionScale(float paramFloat) throws QTVRException {
        float[] arrayOfFloat = {
            paramFloat
        };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 102, arrayOfFloat);
        }
        QTVRException.checkError(i);
    }

    public float getInteractionMouseMotionScale() throws QTVRException {
        float[] arrayOfFloat = {
            0.0F
        };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 102, arrayOfFloat);
        }
        QTVRException.checkError(i);
        return arrayOfFloat[0];
    }

    public void setInteractionNudgeMode(int paramInt) throws QTVRException {
        int i;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 103, paramInt);
        }
        QTVRException.checkError(i);
    }

    public int getInteractionNudgeMode() throws QTVRException {
        int[] arrayOfInt = {
            0
        };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 103, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    public void setViewAngle(int paramInt1, int paramInt2, float paramFloat) throws QTVRException {
        int i = 0;
        float[] arrayOfFloat = {
            paramFloat
        };

        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), paramInt1, arrayOfFloat, paramInt2);
        }
        QTVRException.checkError(i);
    }

    public float getViewAngle(int paramInt) throws QTVRException {
        int i = 0;
        int j = 0;
        int[] arrayOfInt = {
            0
        };
        float[] arrayOfFloat = {
            0.0F
        };

        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), paramInt, arrayOfFloat, j, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfFloat[0];
    }

    public void setViewCentre(int paramInt, QDPoint paramQDPoint) throws QTVRException {
        int i = 0;
        float[] arrayOfFloat = {
            paramQDPoint.getXF(), paramQDPoint.getYF()
        };
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), 260, arrayOfFloat, paramInt);
        }
        QTVRException.checkError(i);
    }

    public QDPoint getViewCentre() throws QTVRException {
        int i = 0;
        int j = 0;
        int[] arrayOfInt = {
            0
        };
        float[] arrayOfFloat = {
            0.0F, 0.0F
        };
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), 260, arrayOfFloat, j, arrayOfInt);
        }
        QTVRException.checkError(i);
        return new QDPoint(arrayOfFloat[0], arrayOfFloat[1]);
    }

    public void setHotSpotsVisible(boolean paramBoolean) throws QTVRException {
        int i = 0;
        int j = 0;

        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), 512, (byte) (paramBoolean ? 1 : 0), j);
        }
        QTVRException.checkError(i);
    }

    public boolean getHotSpotsVisible() throws QTVRException {
        int i = 0;
        int j = 0;

        int[] arrayOfInt = {
            0
        };
        byte[] arrayOfByte = {
            0
        };
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), 512, arrayOfByte, j, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfByte[0] == 1;
    }

    public void replaceCursor(CursorRecord paramCursorRecord) throws QTVRException {
        int i = QTVRReplaceCursor(_ID(), paramCursorRecord.getBytes());
        QTVRException.checkError(i);
    }

    public int getCurrentNodeID() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetCurrentNodeID(_ID());
        }
    }

    public int getNodeType(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return QTVRGetNodeType(_ID(), paramInt);
        }
    }

    public float getPanAngle() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetPanAngle(_ID());
        }
    }

    public float getTiltAngle() {
        synchronized (QTNative.globalsLock) {
        return QTVRGetTiltAngle(_ID());
        }
    }

    public float getFieldOfView() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetFieldOfView(_ID());
        }
    }

    public int getAngularUnits() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetAngularUnits(_ID());
        }
    }

    public int panToColumn(float paramFloat) {
        synchronized (QTNative.globalsLock) {
            return QTVRPanToColumn(_ID(), paramFloat);
        }
    }

    public float columnToPan(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return QTVRColumnToPan(_ID(), (short) paramInt);
        }
    }

    public int tiltToRow(float paramFloat) {
        synchronized (QTNative.globalsLock) {
            return QTVRTiltToRow(_ID(), paramFloat);
        }
    }

    public float rowToTilt(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return QTVRRowToTilt(_ID(), (short) paramInt);
        }
    }

    private static native short QTVRSetViewParameter(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);

    private static native short QTVRGetViewParameter(int paramInt1,
                                                     int paramInt2,
                                                     float[] paramArrayOfFloat,
                                                     int paramInt3,
                                                     int[] paramArrayOfInt);

    private static native short QTVRSetViewParameter(int paramInt1, int paramInt2, byte paramByte, int paramInt3);

    private static native short QTVRGetViewParameter(int paramInt1,
                                                     int paramInt2,
                                                     byte[] paramArrayOfByte,
                                                     int paramInt3,
                                                     int[] paramArrayOfInt);

    private static native short QTVRGetQTVRInstance(int[] paramArrayOfInt, int paramInt1, int paramInt2);

    private static native int QTVRGetCurrentNodeID(int paramInt);

    private static native int QTVRGetNodeType(int paramInt1, int paramInt2);

    private static native short QTVRTriggerHotSpot(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVREnableHotSpot(int paramInt1, int paramInt2, int paramInt3, byte paramByte);

    private static native short QTVRUpdate(int paramInt1, int paramInt2);

    private static native short QTVRGoToNodeID(int paramInt1, int paramInt2);

    private static native short QTVRNudge(int paramInt1, int paramInt2);

    private static native float QTVRGetPanAngle(int paramInt);

    private static native short QTVRSetPanAngle(int paramInt, float paramFloat);

    private static native float QTVRGetTiltAngle(int paramInt);

    private static native short QTVRSetTiltAngle(int paramInt, float paramFloat);

    private static native float QTVRGetFieldOfView(int paramInt);

    private static native short QTVRSetFieldOfView(int paramInt, float paramFloat);

    private static native short QTVRGetConstraints(int paramInt,
                                                   short paramShort,
                                                   float[] paramArrayOfFloat1,
                                                   float[] paramArrayOfFloat2);

    private static native short QTVRSetConstraints(int paramInt, short paramShort, float paramFloat1, float paramFloat2);

    private static native short QTVRSetImagingProperty(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRGetImagingProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int QTVRGetAngularUnits(int paramInt);

    private static native short QTVRSetAngularUnits(int paramInt1, int paramInt2);

    private static native short QTVRPtToAngles(int paramInt1,
                                               int paramInt2,
                                               float[] paramArrayOfFloat1,
                                               float[] paramArrayOfFloat2);

    private static native short QTVRPanToColumn(int paramInt, float paramFloat);

    private static native float QTVRColumnToPan(int paramInt, short paramShort);

    private static native short QTVRTiltToRow(int paramInt, float paramFloat);

    private static native float QTVRRowToTilt(int paramInt, short paramShort);

    private static native short QTVRWrapAndConstrain(int paramInt,
                                                     short paramShort,
                                                     float paramFloat,
                                                     float[] paramArrayOfFloat);

    private static native short QTVRShowDefaultView(int paramInt);

    private static native short QTVRSetViewCenter(int paramInt, byte[] paramArrayOfByte);

    private static native short QTVRGetViewCenter(int paramInt, byte[] paramArrayOfByte);

    private static native short QTVRGetHotSpotType(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTVRSetEnteringNodeProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRSetLeavingNodeProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRSetMouseOverHotSpotProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRInstallInterceptProc(int paramInt1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         int paramInt4,
                                                         int paramInt5);

    private static native short QTVRCallInterceptedProc(int paramInt1, int paramInt2);

    private static native short QTVRSetPrescreenImagingCompleteProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRPtToHotSpotID(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int QTVRGetVisibleHotSpots(int paramInt1, int paramInt2);

    private static native short QTVRGetHotSpotRegion(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTVRSetMouseOverTracking(int paramInt, byte paramByte);

    private static native byte QTVRGetMouseOverTracking(int paramInt);

    private static native short QTVRSetMouseDownTracking(int paramInt, byte paramByte);

    private static native byte QTVRGetMouseDownTracking(int paramInt);

    private static native short QTVRSetInteractionProperty(int paramInt1, int paramInt2, byte paramByte);

    private static native short QTVRSetInteractionProperty(int paramInt1, int paramInt2, short paramShort);

    private static native short QTVRSetInteractionProperty(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTVRSetInteractionProperty(int paramInt1, int paramInt2, float[] paramArrayOfFloat);

    private static native short QTVRGetInteractionProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short QTVRGetInteractionProperty(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native short QTVRGetInteractionProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTVRGetInteractionProperty(int paramInt1, int paramInt2, float[] paramArrayOfFloat);

    private static native short QTVRReplaceCursor(int paramInt, byte[] paramArrayOfByte);

    private static native void HLock(int paramInt);

    private static native int NewHandle(int paramInt);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void DisposeHandle(int paramInt);
}
