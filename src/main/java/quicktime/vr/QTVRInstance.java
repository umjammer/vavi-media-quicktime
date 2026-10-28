package quicktime.vr;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.VRManager;
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

/**
 * The QTVRInstance is used for making QTVR calls
 *
 * If an application installs any of the QTVR callbacks, then it must remove these callbacks
 * when it has finished with them (or with the QTVRInstance itself)
 * to allow the native structures used to be properly disposed.
 */
public final class QTVRInstance extends QTObject implements QuickTimeLib, QuickTimeVRLib, PrimitivesLib, InterfaceLib {

    private static Object linkage;

    private VRDispatcher enUPP;

    private VRDispatcher leaveUPP;

    private VRDispatcher mohsUPP;

    private VRDispatcher icUPP;

    static {
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QTVRInstance.class));
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * You can use the QTVRGetQTVRInstance function to get an instance of a QuickTime VR movie.
     * @see "QuickTime::QTVRGetQTVRInstance"
     * @param qtvrTrack A QTVR track contained in a QuickTime movie. You can obtain a reference to this track by
     *        calling QTVRGetQTVRTrack
     * @param mc A movie controller that is associated with the new QuickTime VR movie
     */
    public QTVRInstance(Track qtvrTrack, MovieController mc) throws QTException {
        super(allocate(qtvrTrack, mc));
    }

    private static int allocate(Track paramTrack, MovieController paramMovieController) throws QTException {
        int[] arrayOfInt = { 0 };
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

    private final Hashtable<Integer, VRDispatcher> ipProcs = new Hashtable<>();

    /**
     * The QTVRGetVRWorld function returns an atom
     * container that contains general scene information about the QuickTime VR movie
     * specified, as well as a list of all the nodes in that movie.
     * @see "QuickTime::QTVRGetVRWorld"
     * @return the VRWorld AtomContainer
     * @throws quicktime.vr.QTVRException
     */
    public AtomContainer getVRWorld() throws QTVRException {
        return AtomContainer.fromQTVRInstanceWorld(this);
    }

    /**
     * You can use the QTVRGetNodeInfo function to get the node information atom container
     * that describes a node and all the hot spots in the node.
     * @see "QuickTime::QTVRGetNodeInfo"
     * @param nodeID Set this parameter to kQTVRCurrentNode to receive information
     *        about the current node
     * @return an atom container that contains information about the
     *        specified node.
     * @throws quicktime.vr.QTVRException
     */
    public AtomContainer getNodeInfo(int nodeID) throws QTVRException {
        return AtomContainer.fromQTVRInstanceNode(this, nodeID);
    }

    /**
     * You can use the QTVRTriggerHotSpot function to trigger a hot spot.
     * Usually, you need only specify
     * the qtvr instance and the hot spot ID. You can pass zero for the nodeInfo and
     * selectedAtom parameters.
     * @see "QuickTime::QTVRTriggerHotSpot"
     * @param hotSpotID A hot spot ID.
     * @param nodeInfo A node information atom container (obtained from a previous
     *                 call to QTVRGetNodeInfo). You can pass the value 0 in this
     *                 parameter to have the QuickTime VR Manager determine the
     *                 appropriate node information atom container.
     * @param selectedAtom The atom of the hot spot to trigger. You can pass the value 0 in
     *                 this parameter to have the QuickTime VR Manager determine
     *                 the appropriate hot spot atom.
     * @throws quicktime.vr.QTVRException
     */
    public void triggerHotSpot(int hotSpotID, AtomContainer nodeInfo, Atom selectedAtom) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRTriggerHotSpot(_ID(), hotSpotID, QTObject.ID(nodeInfo), selectedAtom == null ? 0 : selectedAtom.getAtom());
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVREnableHotSpot function to enable or disable one or more hot
     * spots.
     * @see "QuickTime::QTVREnableHotSpot"
     * @param enableFlag The kind of hot spot or hot spots to enable or disable.
     * @param hotSpotValue The desired hot spot or spots, relative to the specified enabled flag.
     * @param enable A Boolean value that indicates whether the specified hot spots are to be enabled (true) or disabled (false).
     * @throws quicktime.vr.QTVRException
     */
    public void enableHotSpot(int enableFlag, int hotSpotValue, boolean enable) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVREnableHotSpot(_ID(), enableFlag, hotSpotValue, (byte) (enable ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRUpdate function to force an immediate update of a
     * QuickTime VR movie image.
     * @see "QuickTime::QTVRUpdate"
     * @param imagingMode An imaging mode. You can specify
     *                 the kQTVRCurrentMode imaging mode to use the current
     *                 imaging mode.
     * @throws quicktime.vr.QTVRException
     */
    public void update(int imagingMode) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRUpdate(_ID(), imagingMode);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRGoToNodeID function to set the current node of a movie.
     * @see "QuickTime::QTVRGoToNodeID"
     * @param nodeID The ID of the node you want to be the current node.
     * @throws quicktime.vr.QTVRException
     */
    public void goToNodeID(int nodeID) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGoToNodeID(_ID(), nodeID);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRNudge function to turn one step in a particular direction and
     * display the new view.
     * @see "QuickTime::QTVRNudge"
     * @param direction The direction of the nudge.
     * @throws quicktime.vr.QTVRException
     */
    public void nudge(int direction) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRNudge(_ID(), direction);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetPanAngle function to set the pan angle of a
     * QuickTime VR movie.
     * @see "QuickTime::QTVRSetPanAngle"
     * @param panAngle The desired pan angle of the specified movie.
     * @throws quicktime.vr.QTVRException
     */
    public void setPanAngle(float panAngle) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetPanAngle(_ID(), panAngle);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetTiltAngle function to set the tilt angle of a
     * QuickTime VR movie.
     * @see "QuickTime::QTVRSetTiltAngle"
     * @param tiltAngle The desired tilt angle of the specified movie.
     * @throws quicktime.vr.QTVRException
     */
    public void setTiltAngle(float tiltAngle) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetTiltAngle(_ID(), tiltAngle);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetFieldOfView function to set the vertical field of view of a
     * QuickTime VR movie.
     * @see "QuickTime::QTVRSetFieldOfView"
     * @param fieldOfView The desired vertical field of view for the specified movie.
     * @throws quicktime.vr.QTVRException
     */
    public void setFieldOfView(float fieldOfView) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetFieldOfView(_ID(), fieldOfView);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRGetConstraints function to get the current constraints of
     * a movie.
     * @see "QuickTime::QTVRGetConstraints"
     * @param kind The type of constraints to be returned.
     * @throws quicktime.vr.QTVRException
     */
    public float getConstraints_min(int kind) throws QTVRException {
        float[] arrayOfFloat1 = { 0.0F };
        float[] arrayOfFloat2 = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetConstraints(_ID(), (short) kind, arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat1[0];
    }

    /**
     * You can use the QTVRGetConstraints function to get the current constraints of
     * a movie.
     * @see "QuickTime::QTVRGetConstraints"
     * @param kind The type of constraints to be returned.
     * @throws quicktime.vr.QTVRException
     */
    public float getConstraints_max(int kind) throws QTVRException {
        float[] arrayOfFloat1 = { 0.0F };
        float[] arrayOfFloat2 = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetConstraints(_ID(), (short) kind, arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat2[0];
    }

    /**
     * You can use the QTVRSetConstraints function to set the constraints of a movie.
     * @see "QuickTime::QTVRSetConstraints"
     * @param kind The type of constraint to set..
     * @param minValue A floating-point value that contains the desired minimum constraint of the specified type.
     * @param maxValue A floating-point value that contains the desired maximum constraint of the specified type.
     * @throws quicktime.vr.QTVRException
     */
    public void setConstraints(int kind, float minValue, float maxValue) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetConstraints(_ID(), (short) kind, minValue, maxValue);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetImagingProperty function to set the current value of an
     * imaging property of a movie.
     * @see "QuickTime::QTVRSetImagingProperty"
     * @param imagingMode an imaging mode.
     * @param imagingProperty an imaging property.
     * @param propertyValue sets the current value of the specified imaging property for the specified mode.
     * @throws quicktime.vr.QTVRException
     */
    public void setImagingProperty(int imagingMode, int imagingProperty, int propertyValue) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetImagingProperty(_ID(), imagingMode, imagingProperty, propertyValue);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRGetImagingProperty function to get the current value of an
     * imaging property of a movie.
     * @see "QuickTime::QTVRGetImagingProperty"
     * @param imagingMode an imaging mode.
     * @param imagingProperty an imaging property.
     * @return the current value of the specified imaging property for the specified mode.
     * @throws quicktime.vr.QTVRException
     */
    public int getImagingProperty(int imagingMode, int imagingProperty) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetImagingProperty(_ID(), imagingMode, imagingProperty, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * You can use QTVRSetAngularUnits to set the type of unit used when specifying angles.
     * @see "QuickTime::QTVRSetAngularUnits"
     * @param units A constant that indicates the type of angular units to use.
     * @throws quicktime.vr.QTVRException
     */
    public void setAngularUnits(int units) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetAngularUnits(_ID(), units);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use QTVRPtToAngles to get the pan angle of a point.
     * @see "QuickTime::QTVRPtToAngles"
     * @param pt A point, in the local coordinates of the graphics world of the specified movie.
     * @throws quicktime.vr.QTVRException
     */
    public float ptToPanAngle(QDPoint pt) throws QTVRException {
        float[] arrayOfFloat1 = { 0.0F };
        float[] arrayOfFloat2 = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToAngles(_ID(), pt.getPoint(), arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat1[0];
    }

    /**
     * You can use QTVRPtToAngles to get the tilt angle of a point.
     * @see "QuickTime::QTVRPtToAngles"
     * @param pt A point, in the local coordinates of the graphics world of the specified movie.
     * @throws quicktime.vr.QTVRException
     */
    public float ptToTiltAngle(QDPoint pt) throws QTVRException {
        float[] arrayOfFloat1 = { 0.0F };
        float[] arrayOfFloat2 = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToAngles(_ID(), pt.getPoint(), arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat2[0];
    }

    /**
     * You can use QTVRWrapAndConstrain to preflight a change in the viewing or control characteristics of an object or
     * panoramic node.
     * @see "QuickTime::QTVRWrapAndConstrain"
     * @param kind a constraint type. See "Constraint Types" for a description of the available constraint types.
     * @param value the desired value of the specified viewing characteristic.
     * @throws quicktime.vr.QTVRException
     */
    public float wrapAndConstrain(int kind, float value) throws QTVRException {
        float[] arrayOfFloat = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRWrapAndConstrain(_ID(), (short) kind, value, arrayOfFloat);
        }
        QTVRException.checkError(i);
        return arrayOfFloat[0];
    }

    /**
     * You can use the QTVRSetEnteringNodeProc function to install or remove a
     * node-entering procedure.
     * @see "QuickTime::QTVRSetEnteringNodeProc"
     * @param enteringNodeProc an instance of the QTVREnteringNode object that will be called or null to remove the callback.
     * @param flags the flags
     * @throws quicktime.vr.QTVRException
     * @see quicktime.std.clocks.QTCallBack
     */
    public void setEnteringNodeProc(QTVREnteringNode enteringNodeProc, int flags) throws QTVRException {
        if (enteringNodeProc == null) {
            removeEnteringNodeProc();
            return;
        }
        if (this.enUPP != null) {
            removeEnteringNodeProc();
        }
        this.enUPP = new VRDispatcher(this, enteringNodeProc);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetEnteringNodeProc(_ID(), this.enUPP.ID(), 0, flags);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetEnteringNodeProc function to remove a
     * node-entering procedure.
     * @see "QuickTime::QTVRSetEnteringNodeProc"
     */
    public void removeEnteringNodeProc() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetEnteringNodeProc(_ID(), 0, 0, 0);
        }
        if (this.enUPP != null)
            this.enUPP.cleanupMethodClosure();
        this.enUPP = null;
    }

    /**
     * You can use the QTVRSetLeavingNodeProc function to install or remove a
     * node-leaving procedure.
     * @see "QuickTime::QTVRSetLeavingNodeProc"
     * @param leavingNodeProc an instance of the QTVRLeavingNode object that will be called or null to remove the callback.
     * @param flags the flags
     * @throws quicktime.vr.QTVRException
     * @see quicktime.std.clocks.QTCallBack
     */
    public void setLeavingNodeProc(QTVRLeavingNode leavingNodeProc, int flags) throws QTVRException {
        if (leavingNodeProc == null) {
            removeLeavingNodeProc();
            return;
        }
        if (this.leaveUPP != null) {
            removeLeavingNodeProc();
        }
        this.leaveUPP = new VRDispatcher(this, leavingNodeProc);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetLeavingNodeProc(_ID(), this.leaveUPP.ID(), 0, flags);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetLeavingNodeProc function to remove a
     * node-leaving procedure.
     * @see "QuickTime::QTVRSetLeavingNodeProc"
     */
    public void removeLeavingNodeProc() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetLeavingNodeProc(_ID(), 0, 0, 0);
        }
        if (this.leaveUPP != null)
            this.leaveUPP.cleanupMethodClosure();
        this.leaveUPP = null;
    }

    /**
     * You can use the QTVRSetMouseOverHotSpotProc function to install or remove a
     * mouse over hot spot procedure
     * @see "QuickTime::QTVRSetMouseOverHotSpotProc"
     * @param mouseOverHotSpotProc an instance of the QTVRMouseOverHotSpot object that will be called or null to remove the callback.
     * @param flags the flags
     * @throws quicktime.vr.QTVRException
     * @see quicktime.std.clocks.QTCallBack
     */
    public void setMouseOverHotSpotProc(QTVRMouseOverHotSpot mouseOverHotSpotProc, int flags) throws QTVRException {
        if (mouseOverHotSpotProc == null) {
            removeMouseOverHotSpotProc();
            return;
        }
        if (this.mohsUPP != null) {
            removeMouseOverHotSpotProc();
        }
        this.mohsUPP = new VRDispatcher(this, mouseOverHotSpotProc);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseOverHotSpotProc(_ID(), this.mohsUPP.ID(), 0, flags);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetMouseOverHotSpotProc function to remove a
     * mouse over hot spot procedure.
     * @see "QuickTime::QTVRSetMouseOverHotSpotProc"
     */
    public void removeMouseOverHotSpotProc() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseOverHotSpotProc(_ID(), 0, 0, 0);
        }
        if (this.mohsUPP != null)
            this.mohsUPP.cleanupMethodClosure();
        this.mohsUPP = null;
    }

    /**
     * You can use the QTVRInstallInterceptProc function to install or remove an intercept procedure for a
     * QuickTime VR Manager function
     * @see "QuickTime::QTVRInstallInterceptProc"
     * @param selector only the non-mouse selectors are supported
     * @param interceptProc the interceptor or null to remove a previously specified callback for the specified selector.
     * @param flags the flags
     * @throws quicktime.vr.QTVRException
     * @see quicktime.std.clocks.QTCallBack
     */
    public void installInterceptProc(int selector, QTVRInterceptor interceptProc, int flags) throws QTVRException {
        removeInterceptProc(selector);
        if (interceptProc == null) {
            return;
        }
        VRDispatcher localVRDispatcher = new VRDispatcher(this, interceptProc);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRInstallInterceptProc(_ID(), selector, localVRDispatcher.ID(), 0, flags);
        }
        QTVRException.checkError(i);
        if (localVRDispatcher != null) {
            this.ipProcs.put(selector, localVRDispatcher);
        }
    }

    /**
     * You can use the QTVRInstallInterceptProc function to install or remove an intercept procedure for a
     * QuickTime VR Manager function
     * @see "QuickTime::QTVRInstallInterceptProc"
     * @param selector only the non-mouse selectors are supported
     * @throws quicktime.vr.QTVRException
     * @see quicktime.std.clocks.QTCallBack
     */
    public void removeInterceptProc(int selector) throws QTVRException {
        VRDispatcher localVRDispatcher = this.ipProcs.remove(selector);
        if (localVRDispatcher != null) {
            localVRDispatcher.cleanupMethodClosure();
            int i = 0;
            synchronized (QTNative.globalsLock) {
                i = QTVRInstallInterceptProc(_ID(), selector, 0, 0, 0);
            }
            QTVRException.checkError(i);
        }
    }

    /**
     * This method is used by the QTVRInterceptor execute method to call the QTVR default
     * proc for the selector that was intercepted.
     * @see "QuickTime::QTVRCallInterceptedProc"
     * @see quicktime.std.clocks.QTCallBack
     */
    public void callInterceptedProc(QTVRInterceptRecord paramQTVRInterceptRecord) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRCallInterceptedProc(_ID(), QTObject.ID(paramQTVRInterceptRecord));
        }
        QTVRException.checkError(i);
    }

    /**
     * The showDefaultView method sets the default values of the pan angle, tilt angle, field of view, view center (for object nodes), default state, mouse-down state, and all applicable animation and control settings for the node specified by the QTVRInstance you call this on.
     * @see "QuickTime::QTVRShowDefaultView"
     */
    public void showDefaultView() throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRShowDefaultView(_ID());
        }
        QTVRException.checkError(i);
    }

    /**
     * The setViewCenter method sets the view center of the QuickTimeÊVR movie specified by the qtvr parameter to the fixed point specified by the viewCenter parameter.
     * @see "QuickTime::QTVRSetViewCenter"
     * @param viewCenter A QDPoint structure that contains the desired view center of the specified movie.
     * @throws quicktime.vr.QTVRException
     */
    public void setViewCenter(QDPoint viewCenter) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewCenter(_ID(), viewCenter.getQTVRFloatPoint());
        }
        QTVRException.checkError(i);
    }

    /**
     * The getViewCenter method returns, in the QDPoint class, the x and y coordinates of the current view center of the QuickTimeÊVR movie specified by the QTVRInstance you call this on.
     * @see "QuickTime::QTVRGetViewCenter"
     * @return the veiw center
     * @throws quicktime.vr.QTVRException
     */
    public QDPoint getViewCenter() throws QTVRException {
        byte[] arrayOfByte = new byte[8];
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewCenter(_ID(), arrayOfByte);
        }
        QTVRException.checkError(i);
        return QDPoint.fromArray(arrayOfByte, -16);
    }

    /**
     * This method gets the type of a hot spot whose ID you specify. In combination with the kQTVRGetHotSpotTypeSelector intercept selector, this allows an application to change a hot spot's type dynamically.
     * @see "QuickTime::QTVRGetHotSpotType"
     * @param hotSpotID a hot spot ID
     * @return the OSType of the hot spot specified by the hot spot ID.
     * @throws quicktime.vr.QTVRException
     */
    public int getHotSpotType(int hotSpotID) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetHotSpotType(_ID(), hotSpotID, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method installs a callback that is invoked every time a QTVRInstance is drawn. The application can use this
     * callback to draw onto the QDGraphics that is supplied to the callback.
     * @see "QuickTime::QTVRSetPrescreenImagingCompleteProc"
     * @param flags effect the conditions under which QTVR will invoke the callback
     * @throws quicktime.vr.QTVRException
     * @see quicktime.std.clocks.QTCallBack
     */
    public void setPrescreenImagingCompleteProc(QTVRImagingComplete paramQTVRImagingComplete, int paramInt) throws QTVRException {
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

    /**
     * You can use the QTVRSetPrescreenImagingCompleteProc function to remove a
     * prescreen imaging complete proc.
     * @see "QuickTime::QTVRSetPrescreenImagingCompleteProc"
     * @see quicktime.std.clocks.QTCallBack
     */
    public void removePrescreenImagingCompleteProc() {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetPrescreenImagingCompleteProc(_ID(), 0, 0, 0);
        }
        if (this.icUPP != null)
            this.icUPP.cleanupMethodClosure();
        this.icUPP = null;
    }

    /**
     * The QTVRGetHotSpotRegion function returns, in the hotSpotRegion parameter, a handle
     * to the region occupied by the hot spot, in the QuickTimeÊVR movie specified
     * by the qtvr parameter, whose ID is specified by the hotSpotID parameter.
     * The region is clipped to the bounds of the movie's graphics world.
     * @see "QuickTime::QTVRGetHotSpotRegion"
     * @param hotSpotID A hot spot ID
     * @return the region occupied by the hot spot having the specified ID
     * @throws quicktime.QTException
     */
    public Region getHotSpotRegion(int hotSpotID) throws QTException {
        Region localRegion = new Region();
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetHotSpotRegion(_ID(), hotSpotID, QTObject.ID(localRegion));
        }
        QTVRException.checkError(i);
        return localRegion;
    }

    /**
     * Returns a list of IDs of all the hot spots in the QuickTimeÊVR instance
     * that are currently visible.
     * @see "QuickTime::QTVRGetVisibleHotSpots"
     * @return currently visible hot spot IDs
     */
    public int[] getVisibleHotSpots() {
        int i = NewHandle(4);
        int j = 0;
        synchronized (QTNative.globalsLock) {
            j = QTVRGetVisibleHotSpots(_ID(), i);
        }
        int[] ai = new int[j];
        HLock(i);
        int k = getIntFromPointer(i, 0);
        for (int l = 0; l < j; l++) ai[l] = getIntFromPointer(k, l * 4);
        DisposeHandle(i);
        return ai;
    }

    /**
     * Returns the ID of the hot spot in the QuickTimeÊVR instance that lies directly under
     * the point specified by the pt parameter.
     * If no hot spot lies under that point, the method returns 0.
     * @see "QuickTime::QTVRPtToHotSpotID"
     * @param pt A point, in the local coordinates of the graphics world of the specified movie
     * @return the hot spot ID or 0
     * @throws quicktime.vr.QTVRException
     */
    public int ptToHotSpotID(QDPoint pt) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToHotSpotID(_ID(), pt.getPoint(), arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Sets the mouse-over tracking state of the QuickTimeÊVR movie specified by the QTVRInstance
     * object the method is called from. By default, QuickTimeÊVR tracks mouse movements in a
     * QuickTimeÊVR movie and changes the shape of the cursor as appropriate.
     * @see "QuickTime::QTVRSetMouseOverTracking"
     * @param enable a boolean specified whether tracking should be disabled (false) or enabled (true)
     * @throws quicktime.vr.QTVRException
     */
    public void setMouseOverTracking(boolean enable) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseOverTracking(_ID(), (byte) (enable ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    /**
     * Returns a Boolean value that indicates whether QuickTimeÊVR is currently handling mouse-over
     * tracking for the QuickTimeÊVR movie specified by the QTVRInstance ( true ) or not ( false ).
     * By default, QuickTimeÊVR tracks mouse movements in a QuickTimeÊVR movie and changes the shape
     * of the cursor as appropriate.
     * @see "QuickTime::QTVRGetMouseOverTracking"
     * @return true if QTVR is handling mouse-over tracking, false otherwise
     * @throws quicktime.vr.QTVRException
     */
    public boolean getMouseOverTracking() throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetMouseOverTracking(_ID());
        }
        return i == 1;
    }

    /**
     * Sets the mouse-down tracking state of the QuickTimeÊVR movie specified by the QTVRInstance
     * to the state specified by the enable parameter. By default, QuickTimeÊVR tracks mouse clicks
     * in a QuickTimeÊVR movie and triggers hot spots as appropriate.
     * @see "QuickTime::QTVRSetMouseDownTracking"
     * @param enable a boolean specified whether tracking should be disabled (false) or enabled (true)
     * @throws quicktime.vr.QTVRException
     */
    public void setMouseDownTracking(boolean enable) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseDownTracking(_ID(), (byte) (enable ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    /**
     * Returns, as its function result, a Boolean value that indicates whether QuickTimeÊVR is
     * currently handling mouse-down tracking for the QuickTimeÊVR movie specified by the
     * parameter ( true ) or not ( false ). By default, QuickTimeÊVR tracks mouse clicks in
     * a QuickTimeÊVR movie and triggers hot spots as necessary.
     * @see "QuickTime::QTVRGetMouseDownTracking"
     * @return true if QTVR is handling mouse-down tracking, false otherwise
     * @throws quicktime.vr.QTVRException
     */
    public boolean getMouseDownTracking() throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetMouseDownTracking(_ID());
        }
        return i == 1;
    }

    /**
     * Sets the Mouse Click Hysteresis property of the Interaction property defined as the distance,
     * in pixels, from the location of a mouse-down event to the limit within which the cursor is
     * considered not to have moved.
     * In other words, the cursor can move that many pixels during a mouse click and still have
     * the event considered a click. The default mouse-click hysteresis value is 2. This property is valid
     * for panoramas only.
     * @see "QuickTime::QTVRSetInteractionProperty"
     * @param hysteresis the mouse-click hysteresis
     * @throws quicktime.vr.QTVRException
     */
    public void setInteractionMouseClickHysteresis(int hysteresis) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 1, (short) hysteresis);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the Mouse Click Hysteresis property of the Interaction property defined as the distance,
     * in pixels, from the location of a mouse-down event to the limit within which the cursor is
     * considered not to have moved.
     * @see "QuickTime::QTVRGetInteractionProperty"
     * @return the mouse-click hysteresis value
     * @throws quicktime.vr.QTVRException
     */
    public int getInteractionMouseClickHysteresis() throws QTVRException {
        short[] arrayOfShort = { 0 };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 1, arrayOfShort);
        }
        QTVRException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Sets the Mouse Click Timeout Interaction property. The value parameter represents the mouse-click
     * timeout, the number of ticks after which a mouse click times out and is automatically switched
     * from a hot spot selection into a pan. The default mouse-click timeout value is 30 ticks (one-half
     * second). This property is valid for panoramas only.
     * @see "QuickTime::QTVRSetInteractionProperty"
     * @param timeout the mouse-click timeout
     * @throws quicktime.vr.QTVRException
     */
    public void setInteractionMouseClickTimeout(int timeout) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 2, timeout);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the Mouse Click Timeout Interaction property defined as the number of ticks after which a
     * mouse click times out and is automatically switched from a hot spot selection into a pan.
     * @see "QuickTime::QTVRGetInteractionProperty"
     * @return the mouse-click timeout value
     * @throws quicktime.vr.QTVRException
     */
    public int getInteractionMouseClickTimeout() throws QTVRException {
        int i = 0;
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 2, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Sets the panning and tilting speed interaction property. The value parameter represents the relative
     * speed of panning and tilting. This integer should be from 1 (the slowest speed) through 10
     * (the fastest speed); the default panning and tilting speed is 5. This property is valid only
     * for panoramas.
     * @see "QuickTime::QTVRSetInteractionProperty"
     * @param speed the pan and tilt speed
     * @throws quicktime.vr.QTVRException
     */
    public void setInteractionPanTiltSpeed(int speed) throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 3, speed);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the panning and tilting speed interaction property
     * @see "QuickTime::QTVRGetInteractionProperty"
     * @return the pan and tilt speed
     * @throws quicktime.vr.QTVRException
     */
    public int getInteractionPanTiltSpeed() throws QTVRException {
        int i = 0;
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 3, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Sets the Zoom Speed Interaction Parameter. The value parameter represents the zooming speed, the
     * relative speed of zooming in and out. This integer should be from 1 (the slowest speed) through 10
     * (the fastest speed); the default zooming speed is 5. This property is valid for both objects and
     * panoramas.
     * @see "QuickTime::QTVRSetInteractionProperty"
     * @param speed the zoom speed
     * @throws quicktime.vr.QTVRException
     */
    public void setInteractionZoomSpeed(int speed) throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 4, speed);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the zoom speed interaction property
     * @see "QuickTime::QTVRGetInteractionProperty"
     * @return the zoom speed
     * @throws quicktime.vr.QTVRException
     */
    public int getInteractionZoomSpeed() throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 4, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Sets the mouseDown translation flag interaction property. The value parameter indicates whether
     * translate mode is enabled ( true ) or disabled ( false ). When translate mode is enabled, the
     * user can no longer pan or tilt using the mouse; instead, dragging the cursor causes the object
     * to translate. The default translate flag value is false . This property is valid for objects only.
     * @see "QuickTime::QTVRSetInteractionProperty"
     * @param translate sets the mouseDown translation property
     * @throws quicktime.vr.QTVRException
     */
    public void setInteractionTranslateOnMouseDown(boolean translate) throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 101, (byte) (translate ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    /**
     * Sets the translation interaction property
     * @see "QuickTime::QTVRGetInteractionProperty"
     * @return the zoom speed
     * @throws quicktime.vr.QTVRException
     */
    public boolean getInteractionTranslateOnMouseDown() throws QTVRException {
        byte[] arrayOfByte = { 0 };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 101, arrayOfByte);
        }
        QTVRException.checkError(i);
        return arrayOfByte[0] == 1;
    }

    /**
     * Sets the Mouse Motion Scale interaction property. The value parameter represents the mouse-motion
     * scale, the number of degrees or radians that an object or panorama is panned or tilted when the cursor
     * is dragged the entire width of the object bounding box. The default value is 180.0. This property is
     * valid for objects only.
     * @see "QuickTime::QTVRSetInteractionProperty"
     * @param scale sets the mouse motion scale property
     * @throws quicktime.vr.QTVRException
     */
    public void setInteractionMouseMotionScale(float scale) throws QTVRException {
        float[] arrayOfFloat = { scale };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 102, arrayOfFloat);
        }
        QTVRException.checkError(i);
    }

    /**
     * Sets the mouse motion scale interaction property
     * @see "QuickTime::QTVRGetInteractionProperty"
     * @return the mouse motion scale
     * @throws quicktime.vr.QTVRException
     */
    public float getInteractionMouseMotionScale() throws QTVRException {
        float[] arrayOfFloat = { 0.0F };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 102, arrayOfFloat);
        }
        QTVRException.checkError(i);
        return arrayOfFloat[0];
    }

    /**
     * Sets the nudge mode interaction property. The parameter lets you set the QuickTimeÊVR nudge
     * mode to either rotate, translate, or be the same as the current mouse.
     * @see "QuickTime::QTVRSetInteractionProperty"
     * @param mode sets the nudge mode property (use kQTVRNudgeRotate, kQTVRNudgeTranslate, or kQTVRNudgeSameAsMouse)
     * @throws quicktime.vr.QTVRException
     */
    public void setInteractionNudgeMode(int mode) throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 103, mode);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the nudge mode interaction property
     * @see "QuickTime::QTVRGetInteractionProperty"
     * @return the nudge mode
     * @throws quicktime.vr.QTVRException
     */
    public int getInteractionNudgeMode() throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 103, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * Only the above parameters can be passed into this call.
     * @see "QuickTime::QTVRSetViewParameter"
     * Requires QT 5.0.1 and above
     * @param viewParameter one of kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * @param flagsIn zero or combination of kQTVRValueIsRelative kQTVRValueIsRate kQTVRValueIsUserPrefRelative
     *         kQTVRPanAngle: zero (absolute) kQTVRValueIsRelative (relative to value)
     *         kQTVRTiltAngle: zero (absolute) kQTVRValueIsRelative (relative to value)
     *         kQTVRFieldOfViewAngle: zero (absolute) kQTVRValueIsRelative (relative to value)
     *                 kQTVRValueIsRate + kQTVRValueIsUserPrefRelative (zoom in/out)
     * @param value value of the view angle
     * @throws quicktime.vr.QTVRException
     */
    public void setViewAngle(int viewParameter, int flagsIn, float value) throws QTVRException {
        int i = 0;
        float[] arrayOfFloat = { value };
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), viewParameter, arrayOfFloat, flagsIn);
        }
        QTVRException.checkError(i);
    }

    /**
     * kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * Only the above parameters can be passed into this call.
     * @see "QuickTime::QTVRGetViewParameter"
     * Requires QT 5.0.1 and above
     * @param viewParameter one of kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * @return the viewParameter value
     * @throws quicktime.vr.QTVRException
     */
    public float getViewAngle(int viewParameter) throws QTVRException {
        int i = 0;
        int j = 0;
        int[] arrayOfInt = { 0 };
        float[] arrayOfFloat = { 0.0F };
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), viewParameter, arrayOfFloat, j, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfFloat[0];
    }

    /**
     * kQTVRViewCenter
     *
     * @see "QuickTime::QTVRSetViewParameter"
     * Requires QT 5.0.1 and above
     * @param flagsIn zero or combination of kQTVRValueIsRelative kQTVRValueIsRate kQTVRValueIsUserPrefRelative
     *         kQTVRViewCenter: zero (absolute) kQTVRValueIsRelative (relative to value)
     * @param center point specifying the center
     * @throws quicktime.vr.QTVRException
     */
    public void setViewCentre(int flagsIn, QDPoint center) throws QTVRException {
        int i = 0;
        float[] arrayOfFloat = { center.getXF(), center.getYF() };
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), 260, arrayOfFloat, flagsIn);
        }
        QTVRException.checkError(i);
    }

    /**
     * kQTVRViewCenter
     *
     * @see "QuickTime::QTVRGetViewParameter"
     * Requires QT 5.0.1 and above
     * @return the center
     * @throws quicktime.vr.QTVRException
     */
    public QDPoint getViewCentre() throws QTVRException {
        int i = 0;
        int j = 0;
        int[] arrayOfInt = { 0 };
        float[] arrayOfFloat = { 0.0F, 0.0F };
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), 260, arrayOfFloat, j, arrayOfInt);
        }
        QTVRException.checkError(i);
        return new QDPoint(arrayOfFloat[0], arrayOfFloat[1]);
    }

    /**
     * kQTVRHotSpotsVisible
     *
     * @see "QuickTime::QTVRSetViewParameter"
     * Requires QT 5.0.1 and above
     * @param value true if visible
     * @throws quicktime.vr.QTVRException
     */
    public void setHotSpotsVisible(boolean value) throws QTVRException {
        int i = 0;
        int j = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), 512, (byte) (value ? 1 : 0), j);
        }
        QTVRException.checkError(i);
    }

    /**
     * kQTVRHotSpotsVisible
     *
     * @see "QuickTime::QTVRGetViewParameter"
     * Requires QT 5.0.1 and above
     * @return true if hotspot is visible
     * @throws quicktime.vr.QTVRException
     */
    public boolean getHotSpotsVisible() throws QTVRException {
        int i = 0;
        int j = 0;
        int[] arrayOfInt = { 0 };
        byte[] arrayOfByte = { 0 };
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), 512, arrayOfByte, j, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfByte[0] == 1;
    }

    /**
     * This function replaces one or more of the standard QuickTime VR cursors associated with the instance specified by the qtvr parameter
     * with the cursors specified in the cursor record pointed to by the cursRecord parameter. If the type field of the specified cursor
     * record is kQTVRUseDefaultCursor, the default cursor for the given resource ID is reloaded; in this case, the handle field of that
     * record should be set to NIL.
     * @param cursRecord the object pointing to the CursorRecord
     * @throws quicktime.vr.QTVRException
     */
    public void replaceCursor(CursorRecord cursRecord) throws QTVRException {
        int i = QTVRReplaceCursor(_ID(), cursRecord.getBytes());
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRGetCurrentNodeID function to get the current node of
     * a movie.
     * @see "QuickTime::QTVRGetCurrentNodeID"
     * @return the ID of the current node of the specified movie.
     */
    public int getCurrentNodeID() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetCurrentNodeID(_ID());
        }
    }

    /**
     * You can use the QTVRGetNodeType function to get the OSType of a movie node.
     * @see "QuickTime::QTVRGetNodeType"
     * @param nodeID A node ID. Pass kQTVRCurrentNode for the current node.
     * @return the type of the specified node.
     */
    public int getNodeType(int nodeID) {
        synchronized (QTNative.globalsLock) {
            return QTVRGetNodeType(_ID(), nodeID);
        }
    }

    /**
     * You can use the QTVRGetPanAngle function to get the pan angle of a
     * QuickTime VR movie.
     * @see "QuickTime::QTVRGetPanAngle"
     * @return a floating-point value that represents the current pan angle of the specified movie.
     */
    public float getPanAngle() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetPanAngle(_ID());
        }
    }

    /**
     * You can use the QTVRGetTiltAngle function to get the tilt angle of a
     * QuickTime VR movie.
     * @see "QuickTime::QTVRGetTiltAngle"
     * @return a floating-point value that represents the current tilt angle of
     *  the specified movie.
     */
    public float getTiltAngle() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetTiltAngle(_ID());
        }
    }

    /**
     * You can use the QTVRGetFieldOfView function to get the vertical field of view of a
     * QuickTime VR movie.
     * @see "QuickTime::QTVRGetFieldOfView"
     * @return the current vertical field of view of the specified movie.
     */
    public float getFieldOfView() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetFieldOfView(_ID());
        }
    }

    /**
     * You can use QTVRGetAngularUnits to get the type of unit currently used when specifying angles.
     * @see "QuickTime::QTVRGetAngularUnits"
     * @return a constant that indicates the type of angular units currently in use
     */
    public int getAngularUnits() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetAngularUnits(_ID());
        }
    }

    /**
     * You can use QTVRPanToColumn to get the column number in the object image array that corresponds to a pan angle.
     * @see "QuickTime::QTVRPanToColumn"
     * @param panAngle a pan angle.
     * @return the column number in the object image array that corresponds to the specified pan angle.
     */
    public int panToColumn(float panAngle) {
        synchronized (QTNative.globalsLock) {
            return QTVRPanToColumn(_ID(), panAngle);
        }
    }

    /**
     * You can use QTVRColumnToPan to get the pan angle that corresponds to a column number in the object image array.
     * @see "QuickTime::QTVRColumnToPan"
     * @param column A column number.
     * @return the pan angle that corresponds to the specified column number in the object image array.
     */
    public float columnToPan(int column) {
        synchronized (QTNative.globalsLock) {
            return QTVRColumnToPan(_ID(), (short) column);
        }
    }

    /**
     * You can use QTVRTiltToRow to get the row number in the object image array that corresponds to a tilt angle.
     * @see "QuickTime::QTVRTiltToRow"
     * @param tiltAngle a tilt angle.
     * @return The row number in the object image array that corresponds to the specified tilt angle.
     */
    public int tiltToRow(float tiltAngle) {
        synchronized (QTNative.globalsLock) {
            return QTVRTiltToRow(_ID(), tiltAngle);
        }
    }

    /**
     * You can use QTVRRowToTilt to get the tilt angle that corresponds to a row number in the object image array.
     * @see "QuickTime::QTVRRowToTilt"
     * @param row a row number.
     * @return the tilt angle that corresponds to the specified row number in the object image array.
     */
    public float rowToTilt(int row) {
        synchronized (QTNative.globalsLock) {
            return QTVRRowToTilt(_ID(), (short) row);
        }
    }

    private static short QTVRSetViewParameter(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3) {
        return VRManager.QTVRSetViewParameter(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    }

    private static short QTVRGetViewParameter(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int[] paramArrayOfInt) {
        return VRManager.QTVRGetViewParameter(paramInt1, paramInt2, paramArrayOfFloat, paramInt3, paramArrayOfInt);
    }

    private static short QTVRSetViewParameter(int paramInt1, int paramInt2, byte paramByte, int paramInt3) {
        return VRManager.QTVRSetViewParameter(paramInt1, paramInt2, paramByte, paramInt3);
    }

    private static short QTVRGetViewParameter(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int[] paramArrayOfInt) {
        return VRManager.QTVRGetViewParameter(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramArrayOfInt);
    }

    private static short QTVRGetQTVRInstance(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        return VRManager.QTVRGetQTVRInstance(paramArrayOfInt, paramInt1, paramInt2);
    }

    private static int QTVRGetCurrentNodeID(int paramInt) {
        return VRManager.QTVRGetCurrentNodeID(paramInt);
    }

    private static int QTVRGetNodeType(int paramInt1, int paramInt2) {
        return VRManager.QTVRGetNodeType(paramInt1, paramInt2);
    }

    private static short QTVRTriggerHotSpot(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return VRManager.QTVRTriggerHotSpot(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTVREnableHotSpot(int paramInt1, int paramInt2, int paramInt3, byte paramByte) {
        return VRManager.QTVREnableHotSpot(paramInt1, paramInt2, paramInt3, paramByte);
    }

    private static short QTVRUpdate(int paramInt1, int paramInt2) {
        return VRManager.QTVRUpdate(paramInt1, paramInt2);
    }

    private static short QTVRGoToNodeID(int paramInt1, int paramInt2) {
        return VRManager.QTVRGoToNodeID(paramInt1, paramInt2);
    }

    private static short QTVRNudge(int paramInt1, int paramInt2) {
        return VRManager.QTVRNudge(paramInt1, paramInt2);
    }

    private static float QTVRGetPanAngle(int paramInt) {
        return VRManager.QTVRGetPanAngle(paramInt);
    }

    private static short QTVRSetPanAngle(int paramInt, float paramFloat) {
        return VRManager.QTVRSetPanAngle(paramInt, paramFloat);
    }

    private static float QTVRGetTiltAngle(int paramInt) {
        return VRManager.QTVRGetTiltAngle(paramInt);
    }

    private static short QTVRSetTiltAngle(int paramInt, float paramFloat) {
        return VRManager.QTVRSetTiltAngle(paramInt, paramFloat);
    }

    private static float QTVRGetFieldOfView(int paramInt) {
        return VRManager.QTVRGetFieldOfView(paramInt);
    }

    private static short QTVRSetFieldOfView(int paramInt, float paramFloat) {
        return VRManager.QTVRSetFieldOfView(paramInt, paramFloat);
    }

    private static short QTVRGetConstraints(int paramInt, short paramShort, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2) {
        return VRManager.QTVRGetConstraints(paramInt, paramShort, paramArrayOfFloat1, paramArrayOfFloat2);
    }

    private static short QTVRSetConstraints(int paramInt, short paramShort, float paramFloat1, float paramFloat2) {
        return VRManager.QTVRSetConstraints(paramInt, paramShort, paramFloat1, paramFloat2);
    }

    private static short QTVRSetImagingProperty(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return VRManager.QTVRSetImagingProperty(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTVRGetImagingProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return VRManager.QTVRGetImagingProperty(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static int QTVRGetAngularUnits(int paramInt) {
        return VRManager.QTVRGetAngularUnits(paramInt);
    }

    private static short QTVRSetAngularUnits(int paramInt1, int paramInt2) {
        return VRManager.QTVRSetAngularUnits(paramInt1, paramInt2);
    }

    private static short QTVRPtToAngles(int paramInt1, int paramInt2, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2) {
        return VRManager.QTVRPtToAngles(paramInt1, paramInt2, paramArrayOfFloat1, paramArrayOfFloat2);
    }

    private static short QTVRPanToColumn(int paramInt, float paramFloat) {
        return VRManager.QTVRPanToColumn(paramInt, paramFloat);
    }

    private static float QTVRColumnToPan(int paramInt, short paramShort) {
        return VRManager.QTVRColumnToPan(paramInt, paramShort);
    }

    private static short QTVRTiltToRow(int paramInt, float paramFloat) {
        return VRManager.QTVRTiltToRow(paramInt, paramFloat);
    }

    private static float QTVRRowToTilt(int paramInt, short paramShort) {
        return VRManager.QTVRRowToTilt(paramInt, paramShort);
    }

    private static short QTVRWrapAndConstrain(int paramInt, short paramShort, float paramFloat, float[] paramArrayOfFloat) {
        return VRManager.QTVRWrapAndConstrain(paramInt, paramShort, paramFloat, paramArrayOfFloat);
    }

    private static short QTVRShowDefaultView(int paramInt) {
        return VRManager.QTVRShowDefaultView(paramInt);
    }

    private static short QTVRSetViewCenter(int paramInt, byte[] paramArrayOfByte) {
        return VRManager.QTVRSetViewCenter(paramInt, paramArrayOfByte);
    }

    private static short QTVRGetViewCenter(int paramInt, byte[] paramArrayOfByte) {
        return VRManager.QTVRGetViewCenter(paramInt, paramArrayOfByte);
    }

    private static short QTVRGetHotSpotType(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return VRManager.QTVRGetHotSpotType(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short QTVRSetEnteringNodeProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return VRManager.QTVRSetEnteringNodeProc(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTVRSetLeavingNodeProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return VRManager.QTVRSetLeavingNodeProc(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTVRSetMouseOverHotSpotProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return VRManager.QTVRSetMouseOverHotSpotProc(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTVRInstallInterceptProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return VRManager.QTVRInstallInterceptProc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static short QTVRCallInterceptedProc(int paramInt1, int paramInt2) {
        return VRManager.QTVRCallInterceptedProc(paramInt1, paramInt2);
    }

    private static short QTVRSetPrescreenImagingCompleteProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return VRManager.QTVRSetPrescreenImagingCompleteProc(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTVRPtToHotSpotID(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return VRManager.QTVRPtToHotSpotID(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int QTVRGetVisibleHotSpots(int paramInt1, int paramInt2) {
        return VRManager.QTVRGetVisibleHotSpots(paramInt1, paramInt2);
    }

    private static short QTVRGetHotSpotRegion(int paramInt1, int paramInt2, int paramInt3) {
        return VRManager.QTVRGetHotSpotRegion(paramInt1, paramInt2, paramInt3);
    }

    private static short QTVRSetMouseOverTracking(int paramInt, byte paramByte) {
        return VRManager.QTVRSetMouseOverTracking(paramInt, paramByte);
    }

    private static byte QTVRGetMouseOverTracking(int paramInt) {
        return VRManager.QTVRGetMouseOverTracking(paramInt);
    }

    private static short QTVRSetMouseDownTracking(int paramInt, byte paramByte) {
        return VRManager.QTVRSetMouseDownTracking(paramInt, paramByte);
    }

    private static byte QTVRGetMouseDownTracking(int paramInt) {
        return VRManager.QTVRGetMouseDownTracking(paramInt);
    }

    private static short QTVRSetInteractionProperty(int paramInt1, int paramInt2, byte paramByte) {
        return VRManager.QTVRSetInteractionProperty(paramInt1, paramInt2, paramByte);
    }

    private static short QTVRSetInteractionProperty(int paramInt1, int paramInt2, short paramShort) {
        return VRManager.QTVRSetInteractionProperty(paramInt1, paramInt2, paramShort);
    }

    private static short QTVRSetInteractionProperty(int paramInt1, int paramInt2, int paramInt3) {
        return VRManager.QTVRSetInteractionProperty(paramInt1, paramInt2, paramInt3);
    }

    private static short QTVRSetInteractionProperty(int paramInt1, int paramInt2, float[] paramArrayOfFloat) {
        return VRManager.QTVRSetInteractionProperty(paramInt1, paramInt2, paramArrayOfFloat);
    }

    private static short QTVRGetInteractionProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return VRManager.QTVRGetInteractionProperty(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short QTVRGetInteractionProperty(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return VRManager.QTVRGetInteractionProperty(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static short QTVRGetInteractionProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return VRManager.QTVRGetInteractionProperty(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short QTVRGetInteractionProperty(int paramInt1, int paramInt2, float[] paramArrayOfFloat) {
        return VRManager.QTVRGetInteractionProperty(paramInt1, paramInt2, paramArrayOfFloat);
    }

    private static short QTVRReplaceCursor(int paramInt, byte[] paramArrayOfByte) {
        return VRManager.QTVRReplaceCursor(paramInt, paramArrayOfByte);
    }

    private static void HLock(int paramInt) {
        MemoryManager.HLock(paramInt);
    }

    private static int NewHandle(int paramInt) {
        return MemoryManager.NewHandle(paramInt);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static void DisposeHandle(int paramInt) {
        MemoryManager.DisposeHandle(paramInt);
    }
}
