package quicktime.vr;

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
 * <p>
 * If an application installs any of the QTVR callbacks, then it must remove these callbacks
 * when it has finished with them (or with the QTVRInstance itself)
 * to allow the native structures used to be properly disposed.
 */
public final class QTVRInstance extends QTObject implements QuickTimeLib, QuickTimeVRLib, PrimitivesLib, InterfaceLib {

    private VRDispatcher enUPP;

    private VRDispatcher leaveUPP;

    private VRDispatcher mohsUPP;

    private VRDispatcher icUPP;

    /**
     * You can use the QTVRGetQTVRInstance function to get an instance of a QuickTime VR movie.
     *
     * @param qtvrTrack A QTVR track contained in a QuickTime movie. You can obtain a reference to this track by
     *                  calling QTVRGetQTVRTrack
     * @param mc        A movie controller that is associated with the new QuickTime VR movie
     * @see "QuickTime::QTVRGetQTVRInstance"
     */
    public QTVRInstance(Track qtvrTrack, MovieController mc) throws QTException {
        super(allocate(qtvrTrack, mc));
    }

    private static int allocate(Track qtvrTrack, MovieController mc) throws QTException {
        int[] qtvr = {0};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetQTVRInstance(qtvr, QTObject.ID(qtvrTrack), QTObject.ID(mc));
        }
        QTVRException.checkError(i);
        return qtvr[0];
    }

    QTVRInstance(int nativeRep) {
        super(nativeRep);
    }

    private final Hashtable<Integer, VRDispatcher> ipProcs = new Hashtable<>();

    /**
     * The QTVRGetVRWorld function returns an atom
     * container that contains general scene information about the QuickTime VR movie
     * specified, as well as a list of all the nodes in that movie.
     *
     * @return the VRWorld AtomContainer
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetVRWorld"
     */
    public AtomContainer getVRWorld() throws QTVRException {
        return AtomContainer.fromQTVRInstanceWorld(this);
    }

    /**
     * You can use the QTVRGetNodeInfo function to get the node information atom container
     * that describes a node and all the hot spots in the node.
     *
     * @param nodeID Set this parameter to kQTVRCurrentNode to receive information
     *               about the current node
     * @return an atom container that contains information about the
     * specified node.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetNodeInfo"
     */
    public AtomContainer getNodeInfo(int nodeID) throws QTVRException {
        return AtomContainer.fromQTVRInstanceNode(this, nodeID);
    }

    /**
     * You can use the QTVRTriggerHotSpot function to trigger a hot spot.
     * Usually, you need only specify
     * the qtvr instance and the hot spot ID. You can pass zero for the nodeInfo and
     * selectedAtom parameters.
     *
     * @param hotSpotID    A hot spot ID.
     * @param nodeInfo     A node information atom container (obtained from a previous
     *                     call to QTVRGetNodeInfo). You can pass the value 0 in this
     *                     parameter to have the QuickTime VR Manager determine the
     *                     appropriate node information atom container.
     * @param selectedAtom The atom of the hot spot to trigger. You can pass the value 0 in
     *                     this parameter to have the QuickTime VR Manager determine
     *                     the appropriate hot spot atom.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRTriggerHotSpot"
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
     *
     * @param enableFlag   The kind of hot spot or hot spots to enable or disable.
     * @param hotSpotValue The desired hot spot or spots, relative to the specified enabled flag.
     * @param enable       A Boolean value that indicates whether the specified hot spots are to be enabled (true) or disabled (false).
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVREnableHotSpot"
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
     *
     * @param imagingMode An imaging mode. You can specify
     *                    the kQTVRCurrentMode imaging mode to use the current
     *                    imaging mode.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRUpdate"
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
     *
     * @param nodeID The ID of the node you want to be the current node.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGoToNodeID"
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
     *
     * @param direction The direction of the nudge.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRNudge"
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
     *
     * @param panAngle The desired pan angle of the specified movie.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetPanAngle"
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
     *
     * @param tiltAngle The desired tilt angle of the specified movie.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetTiltAngle"
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
     *
     * @param fieldOfView The desired vertical field of view for the specified movie.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetFieldOfView"
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
     *
     * @param kind The type of constraints to be returned.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetConstraints"
     */
    public float getConstraints_min(int kind) throws QTVRException {
        float[] minValue = {0.0F};
        float[] maxValue = {0.0F};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetConstraints(_ID(), (short) kind, minValue, maxValue);
        }
        QTVRException.checkError(i);
        return minValue[0];
    }

    /**
     * You can use the QTVRGetConstraints function to get the current constraints of
     * a movie.
     *
     * @param kind The type of constraints to be returned.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetConstraints"
     */
    public float getConstraints_max(int kind) throws QTVRException {
        float[] minValue = {0.0F};
        float[] maxValue = {0.0F};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetConstraints(_ID(), (short) kind, minValue, maxValue);
        }
        QTVRException.checkError(i);
        return maxValue[0];
    }

    /**
     * You can use the QTVRSetConstraints function to set the constraints of a movie.
     *
     * @param kind     The type of constraint to set..
     * @param minValue A floating-point value that contains the desired minimum constraint of the specified type.
     * @param maxValue A floating-point value that contains the desired maximum constraint of the specified type.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetConstraints"
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
     *
     * @param imagingMode     an imaging mode.
     * @param imagingProperty an imaging property.
     * @param propertyValue   sets the current value of the specified imaging property for the specified mode.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetImagingProperty"
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
     *
     * @param imagingMode     an imaging mode.
     * @param imagingProperty an imaging property.
     * @return the current value of the specified imaging property for the specified mode.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetImagingProperty"
     */
    public int getImagingProperty(int imagingMode, int imagingProperty) throws QTVRException {
        int[] propertyValue = {0};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetImagingProperty(_ID(), imagingMode, imagingProperty, propertyValue);
        }
        QTVRException.checkError(i);
        return propertyValue[0];
    }

    /**
     * You can use QTVRSetAngularUnits to set the type of unit used when specifying angles.
     *
     * @param units A constant that indicates the type of angular units to use.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetAngularUnits"
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
     *
     * @param pt A point, in the local coordinates of the graphics world of the specified movie.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRPtToAngles"
     */
    public float ptToPanAngle(QDPoint pt) throws QTVRException {
        float[] panAngle = {0.0F};
        float[] tiltAngle = {0.0F};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToAngles(_ID(), pt.getPoint(), panAngle, tiltAngle);
        }
        QTVRException.checkError(i);
        return panAngle[0];
    }

    /**
     * You can use QTVRPtToAngles to get the tilt angle of a point.
     *
     * @param pt A point, in the local coordinates of the graphics world of the specified movie.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRPtToAngles"
     */
    public float ptToTiltAngle(QDPoint pt) throws QTVRException {
        float[] panAngle = {0.0F};
        float[] tiltAngle = {0.0F};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToAngles(_ID(), pt.getPoint(), panAngle, tiltAngle);
        }
        QTVRException.checkError(i);
        return tiltAngle[0];
    }

    /**
     * You can use QTVRWrapAndConstrain to preflight a change in the viewing or control characteristics of an object or
     * panoramic node.
     *
     * @param kind  a constraint type. See "Constraint Types" for a description of the available constraint types.
     * @param value the desired value of the specified viewing characteristic.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRWrapAndConstrain"
     */
    public float wrapAndConstrain(int kind, float value) throws QTVRException {
        float[] result = {0.0F};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRWrapAndConstrain(_ID(), (short) kind, value, result);
        }
        QTVRException.checkError(i);
        return result[0];
    }

    /**
     * You can use the QTVRSetEnteringNodeProc function to install or remove a
     * node-entering procedure.
     *
     * @param enteringNodeProc an instance of the QTVREnteringNode object that will be called or null to remove the callback.
     * @param flags            the flags
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetEnteringNodeProc"
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
     *
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
     *
     * @param leavingNodeProc an instance of the QTVRLeavingNode object that will be called or null to remove the callback.
     * @param flags           the flags
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetLeavingNodeProc"
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
     *
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
     *
     * @param mouseOverHotSpotProc an instance of the QTVRMouseOverHotSpot object that will be called or null to remove the callback.
     * @param flags                the flags
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetMouseOverHotSpotProc"
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
     *
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
     *
     * @param selector      only the non-mouse selectors are supported
     * @param interceptProc the interceptor or null to remove a previously specified callback for the specified selector.
     * @param flags         the flags
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRInstallInterceptProc"
     * @see quicktime.std.clocks.QTCallBack
     */
    public void installInterceptProc(int selector, QTVRInterceptor interceptProc, int flags) throws QTVRException {
        removeInterceptProc(selector);
        if (interceptProc == null) {
            return;
        }
        VRDispatcher vrDispatcher = new VRDispatcher(this, interceptProc);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRInstallInterceptProc(_ID(), selector, vrDispatcher.ID(), 0, flags);
        }
        QTVRException.checkError(i);
        if (vrDispatcher != null) {
            this.ipProcs.put(selector, vrDispatcher);
        }
    }

    /**
     * You can use the QTVRInstallInterceptProc function to install or remove an intercept procedure for a
     * QuickTime VR Manager function
     *
     * @param selector only the non-mouse selectors are supported
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRInstallInterceptProc"
     * @see quicktime.std.clocks.QTCallBack
     */
    public void removeInterceptProc(int selector) throws QTVRException {
        VRDispatcher vrDispatcher = this.ipProcs.remove(selector);
        if (vrDispatcher != null) {
            vrDispatcher.cleanupMethodClosure();
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
     *
     * @see "QuickTime::QTVRCallInterceptedProc"
     * @see quicktime.std.clocks.QTCallBack
     */
    public void callInterceptedProc(QTVRInterceptRecord qtvrMessage) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRCallInterceptedProc(_ID(), QTObject.ID(qtvrMessage));
        }
        QTVRException.checkError(i);
    }

    /**
     * The showDefaultView method sets the default values of the pan angle, tilt angle, field of view, view center (for object nodes), default state, mouse-down state, and all applicable animation and control settings for the node specified by the QTVRInstance you call this on.
     *
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
     *
     * @param viewCenter A QDPoint structure that contains the desired view center of the specified movie.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetViewCenter"
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
     *
     * @return the veiw center
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetViewCenter"
     */
    public QDPoint getViewCenter() throws QTVRException {
        byte[] viewCenter = new byte[8];
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewCenter(_ID(), viewCenter);
        }
        QTVRException.checkError(i);
        return QDPoint.fromArray(viewCenter, -16);
    }

    /**
     * This method gets the type of a hot spot whose ID you specify. In combination with the kQTVRGetHotSpotTypeSelector intercept selector, this allows an application to change a hot spot's type dynamically.
     *
     * @param hotSpotID a hot spot ID
     * @return the OSType of the hot spot specified by the hot spot ID.
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetHotSpotType"
     */
    public int getHotSpotType(int hotSpotID) throws QTVRException {
        int[] hotSpotType = {0};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetHotSpotType(_ID(), hotSpotID, hotSpotType);
        }
        QTVRException.checkError(i);
        return hotSpotType[0];
    }

    /**
     * This method installs a callback that is invoked every time a QTVRInstance is drawn. The application can use this
     * callback to draw onto the QDGraphics that is supplied to the callback.
     *
     * @param flags effect the conditions under which QTVR will invoke the callback
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetPrescreenImagingCompleteProc"
     * @see quicktime.std.clocks.QTCallBack
     */
    public void setPrescreenImagingCompleteProc(QTVRImagingComplete ic, int flags) throws QTVRException {
        if (ic == null) {
            removePrescreenImagingCompleteProc();
            return;
        }
        if (this.icUPP != null) {
            removePrescreenImagingCompleteProc();
        }
        this.icUPP = new VRDispatcher(this, ic);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetPrescreenImagingCompleteProc(_ID(), this.icUPP.ID(), 0, flags);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetPrescreenImagingCompleteProc function to remove a
     * prescreen imaging complete proc.
     *
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
     *
     * @param hotSpotID A hot spot ID
     * @return the region occupied by the hot spot having the specified ID
     * @throws quicktime.QTException
     * @see "QuickTime::QTVRGetHotSpotRegion"
     */
    public Region getHotSpotRegion(int hotSpotID) throws QTException {
        Region hotSpotRegion = new Region();
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetHotSpotRegion(_ID(), hotSpotID, QTObject.ID(hotSpotRegion));
        }
        QTVRException.checkError(i);
        return hotSpotRegion;
    }

    /**
     * Returns a list of IDs of all the hot spots in the QuickTimeÊVR instance
     * that are currently visible.
     *
     * @return currently visible hot spot IDs
     * @see "QuickTime::QTVRGetVisibleHotSpots"
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
     *
     * @param pt A point, in the local coordinates of the graphics world of the specified movie
     * @return the hot spot ID or 0
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRPtToHotSpotID"
     */
    public int ptToHotSpotID(QDPoint pt) throws QTVRException {
        int[] hotSpotID = {0};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToHotSpotID(_ID(), pt.getPoint(), hotSpotID);
        }
        QTVRException.checkError(i);
        return hotSpotID[0];
    }

    /**
     * Sets the mouse-over tracking state of the QuickTimeÊVR movie specified by the QTVRInstance
     * object the method is called from. By default, QuickTimeÊVR tracks mouse movements in a
     * QuickTimeÊVR movie and changes the shape of the cursor as appropriate.
     *
     * @param enable a boolean specified whether tracking should be disabled (false) or enabled (true)
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetMouseOverTracking"
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
     *
     * @return true if QTVR is handling mouse-over tracking, false otherwise
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetMouseOverTracking"
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
     *
     * @param enable a boolean specified whether tracking should be disabled (false) or enabled (true)
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetMouseDownTracking"
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
     *
     * @return true if QTVR is handling mouse-down tracking, false otherwise
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetMouseDownTracking"
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
     *
     * @param hysteresis the mouse-click hysteresis
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetInteractionProperty"
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
     *
     * @return the mouse-click hysteresis value
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetInteractionProperty"
     */
    public int getInteractionMouseClickHysteresis() throws QTVRException {
        short[] value = {0};
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 1, value);
        }
        QTVRException.checkError(i);
        return value[0];
    }

    /**
     * Sets the Mouse Click Timeout Interaction property. The value parameter represents the mouse-click
     * timeout, the number of ticks after which a mouse click times out and is automatically switched
     * from a hot spot selection into a pan. The default mouse-click timeout value is 30 ticks (one-half
     * second). This property is valid for panoramas only.
     *
     * @param timeout the mouse-click timeout
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetInteractionProperty"
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
     *
     * @return the mouse-click timeout value
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetInteractionProperty"
     */
    public int getInteractionMouseClickTimeout() throws QTVRException {
        int i = 0;
        int[] value = {0};
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 2, value);
        }
        QTVRException.checkError(i);
        return value[0];
    }

    /**
     * Sets the panning and tilting speed interaction property. The value parameter represents the relative
     * speed of panning and tilting. This integer should be from 1 (the slowest speed) through 10
     * (the fastest speed); the default panning and tilting speed is 5. This property is valid only
     * for panoramas.
     *
     * @param speed the pan and tilt speed
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetInteractionProperty"
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
     *
     * @return the pan and tilt speed
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetInteractionProperty"
     */
    public int getInteractionPanTiltSpeed() throws QTVRException {
        int i = 0;
        int[] value = {0};
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 3, value);
        }
        QTVRException.checkError(i);
        return value[0];
    }

    /**
     * Sets the Zoom Speed Interaction Parameter. The value parameter represents the zooming speed, the
     * relative speed of zooming in and out. This integer should be from 1 (the slowest speed) through 10
     * (the fastest speed); the default zooming speed is 5. This property is valid for both objects and
     * panoramas.
     *
     * @param speed the zoom speed
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetInteractionProperty"
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
     *
     * @return the zoom speed
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetInteractionProperty"
     */
    public int getInteractionZoomSpeed() throws QTVRException {
        int[] value = {0};
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 4, value);
        }
        QTVRException.checkError(i);
        return value[0];
    }

    /**
     * Sets the mouseDown translation flag interaction property. The value parameter indicates whether
     * translate mode is enabled ( true ) or disabled ( false ). When translate mode is enabled, the
     * user can no longer pan or tilt using the mouse; instead, dragging the cursor causes the object
     * to translate. The default translate flag value is false . This property is valid for objects only.
     *
     * @param translate sets the mouseDown translation property
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetInteractionProperty"
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
     *
     * @return the zoom speed
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetInteractionProperty"
     */
    public boolean getInteractionTranslateOnMouseDown() throws QTVRException {
        byte[] value = {0};
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 101, value);
        }
        QTVRException.checkError(i);
        return value[0] == 1;
    }

    /**
     * Sets the Mouse Motion Scale interaction property. The value parameter represents the mouse-motion
     * scale, the number of degrees or radians that an object or panorama is panned or tilted when the cursor
     * is dragged the entire width of the object bounding box. The default value is 180.0. This property is
     * valid for objects only.
     *
     * @param scale sets the mouse motion scale property
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetInteractionProperty"
     */
    public void setInteractionMouseMotionScale(float scale) throws QTVRException {
        float[] value = {scale};
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 102, value);
        }
        QTVRException.checkError(i);
    }

    /**
     * Sets the mouse motion scale interaction property
     *
     * @return the mouse motion scale
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetInteractionProperty"
     */
    public float getInteractionMouseMotionScale() throws QTVRException {
        float[] value = {0.0F};
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 102, value);
        }
        QTVRException.checkError(i);
        return value[0];
    }

    /**
     * Sets the nudge mode interaction property. The parameter lets you set the QuickTimeÊVR nudge
     * mode to either rotate, translate, or be the same as the current mouse.
     *
     * @param mode sets the nudge mode property (use kQTVRNudgeRotate, kQTVRNudgeTranslate, or kQTVRNudgeSameAsMouse)
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetInteractionProperty"
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
     *
     * @return the nudge mode
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetInteractionProperty"
     */
    public int getInteractionNudgeMode() throws QTVRException {
        int[] value = {0};
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetInteractionProperty(_ID(), 103, value);
        }
        QTVRException.checkError(i);
        return value[0];
    }

    /**
     * kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * Only the above parameters can be passed into this call.
     *
     * @param viewParameter one of kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * @param flagsIn       zero or combination of kQTVRValueIsRelative kQTVRValueIsRate kQTVRValueIsUserPrefRelative
     *                      kQTVRPanAngle: zero (absolute) kQTVRValueIsRelative (relative to value)
     *                      kQTVRTiltAngle: zero (absolute) kQTVRValueIsRelative (relative to value)
     *                      kQTVRFieldOfViewAngle: zero (absolute) kQTVRValueIsRelative (relative to value)
     *                      kQTVRValueIsRate + kQTVRValueIsUserPrefRelative (zoom in/out)
     * @param value         value of the view angle
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetViewParameter"
     * Requires QT 5.0.1 and above
     */
    public void setViewAngle(int viewParameter, int flagsIn, float value) throws QTVRException {
        int i = 0;
        float[] arrayOfFloat = {value};
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), viewParameter, arrayOfFloat, flagsIn);
        }
        QTVRException.checkError(i);
    }

    /**
     * kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * Only the above parameters can be passed into this call.
     *
     * @param viewParameter one of kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * @return the viewParameter value
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetViewParameter"
     * Requires QT 5.0.1 and above
     */
    public float getViewAngle(int viewParameter) throws QTVRException {
        int i = 0;
        int j = 0;
        int[] flagsOut = {0};
        float[] value = {0.0F};
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), viewParameter, value, j, flagsOut);
        }
        QTVRException.checkError(i);
        return value[0];
    }

    /**
     * kQTVRViewCenter
     *
     * @param flagsIn zero or combination of kQTVRValueIsRelative kQTVRValueIsRate kQTVRValueIsUserPrefRelative
     *                kQTVRViewCenter: zero (absolute) kQTVRValueIsRelative (relative to value)
     * @param center  point specifying the center
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetViewParameter"
     * Requires QT 5.0.1 and above
     */
    public void setViewCentre(int flagsIn, QDPoint center) throws QTVRException {
        int i = 0;
        float[] value = {center.getXF(), center.getYF()};
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), 260, value, flagsIn);
        }
        QTVRException.checkError(i);
    }

    /**
     * kQTVRViewCenter
     *
     * @return the center
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetViewParameter"
     * Requires QT 5.0.1 and above
     */
    public QDPoint getViewCentre() throws QTVRException {
        int i = 0;
        int j = 0;
        int[] flagsOut = {0};
        float[] value = {0.0F, 0.0F};
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), 260, value, j, flagsOut);
        }
        QTVRException.checkError(i);
        return new QDPoint(value[0], value[1]);
    }

    /**
     * kQTVRHotSpotsVisible
     *
     * @param value true if visible
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRSetViewParameter"
     * Requires QT 5.0.1 and above
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
     * @return true if hotspot is visible
     * @throws quicktime.vr.QTVRException
     * @see "QuickTime::QTVRGetViewParameter"
     * Requires QT 5.0.1 and above
     */
    public boolean getHotSpotsVisible() throws QTVRException {
        int i = 0;
        int j = 0;
        int[] flagsOut = {0};
        byte[] value = {0};
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), 512, value, j, flagsOut);
        }
        QTVRException.checkError(i);
        return value[0] == 1;
    }

    /**
     * This function replaces one or more of the standard QuickTime VR cursors associated with the instance specified by the qtvr parameter
     * with the cursors specified in the cursor record pointed to by the cursRecord parameter. If the type field of the specified cursor
     * record is kQTVRUseDefaultCursor, the default cursor for the given resource ID is reloaded; in this case, the handle field of that
     * record should be set to NIL.
     *
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
     *
     * @return the ID of the current node of the specified movie.
     * @see "QuickTime::QTVRGetCurrentNodeID"
     */
    public int getCurrentNodeID() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetCurrentNodeID(_ID());
        }
    }

    /**
     * You can use the QTVRGetNodeType function to get the OSType of a movie node.
     *
     * @param nodeID A node ID. Pass kQTVRCurrentNode for the current node.
     * @return the type of the specified node.
     * @see "QuickTime::QTVRGetNodeType"
     */
    public int getNodeType(int nodeID) {
        synchronized (QTNative.globalsLock) {
            return QTVRGetNodeType(_ID(), nodeID);
        }
    }

    /**
     * You can use the QTVRGetPanAngle function to get the pan angle of a
     * QuickTime VR movie.
     *
     * @return a floating-point value that represents the current pan angle of the specified movie.
     * @see "QuickTime::QTVRGetPanAngle"
     */
    public float getPanAngle() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetPanAngle(_ID());
        }
    }

    /**
     * You can use the QTVRGetTiltAngle function to get the tilt angle of a
     * QuickTime VR movie.
     *
     * @return a floating-point value that represents the current tilt angle of
     * the specified movie.
     * @see "QuickTime::QTVRGetTiltAngle"
     */
    public float getTiltAngle() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetTiltAngle(_ID());
        }
    }

    /**
     * You can use the QTVRGetFieldOfView function to get the vertical field of view of a
     * QuickTime VR movie.
     *
     * @return the current vertical field of view of the specified movie.
     * @see "QuickTime::QTVRGetFieldOfView"
     */
    public float getFieldOfView() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetFieldOfView(_ID());
        }
    }

    /**
     * You can use QTVRGetAngularUnits to get the type of unit currently used when specifying angles.
     *
     * @return a constant that indicates the type of angular units currently in use
     * @see "QuickTime::QTVRGetAngularUnits"
     */
    public int getAngularUnits() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetAngularUnits(_ID());
        }
    }

    /**
     * You can use QTVRPanToColumn to get the column number in the object image array that corresponds to a pan angle.
     *
     * @param panAngle a pan angle.
     * @return the column number in the object image array that corresponds to the specified pan angle.
     * @see "QuickTime::QTVRPanToColumn"
     */
    public int panToColumn(float panAngle) {
        synchronized (QTNative.globalsLock) {
            return QTVRPanToColumn(_ID(), panAngle);
        }
    }

    /**
     * You can use QTVRColumnToPan to get the pan angle that corresponds to a column number in the object image array.
     *
     * @param column A column number.
     * @return the pan angle that corresponds to the specified column number in the object image array.
     * @see "QuickTime::QTVRColumnToPan"
     */
    public float columnToPan(int column) {
        synchronized (QTNative.globalsLock) {
            return QTVRColumnToPan(_ID(), (short) column);
        }
    }

    /**
     * You can use QTVRTiltToRow to get the row number in the object image array that corresponds to a tilt angle.
     *
     * @param tiltAngle a tilt angle.
     * @return The row number in the object image array that corresponds to the specified tilt angle.
     * @see "QuickTime::QTVRTiltToRow"
     */
    public int tiltToRow(float tiltAngle) {
        synchronized (QTNative.globalsLock) {
            return QTVRTiltToRow(_ID(), tiltAngle);
        }
    }

    /**
     * You can use QTVRRowToTilt to get the tilt angle that corresponds to a row number in the object image array.
     *
     * @param row a row number.
     * @return the tilt angle that corresponds to the specified row number in the object image array.
     * @see "QuickTime::QTVRRowToTilt"
     */
    public float rowToTilt(int row) {
        synchronized (QTNative.globalsLock) {
            return QTVRRowToTilt(_ID(), (short) row);
        }
    }

    private static short QTVRSetViewParameter(int qtvr, int viewParameter, float[] value, int flagsIn) {
        return VRManager.QTVRSetViewParameter(qtvr, viewParameter, value, flagsIn);
    }

    private static short QTVRGetViewParameter(int qtvr, int viewParameter, float[] value, int flagsIn, int[] flagsOut) {
        return VRManager.QTVRGetViewParameter(qtvr, viewParameter, value, flagsIn, flagsOut);
    }

    private static short QTVRSetViewParameter(int qtvr, int viewParameter, byte value, int flagsIn) {
        return VRManager.QTVRSetViewParameter(qtvr, viewParameter, value, flagsIn);
    }

    private static short QTVRGetViewParameter(int qtvr, int viewParameter, byte[] value, int flagsIn, int[] flagsOut) {
        return VRManager.QTVRGetViewParameter(qtvr, viewParameter, value, flagsIn, flagsOut);
    }

    private static short QTVRGetQTVRInstance(int[] qtvr, int qtvrTrack, int mc) {
        return VRManager.QTVRGetQTVRInstance(qtvr, qtvrTrack, mc);
    }

    private static int QTVRGetCurrentNodeID(int qtvr) {
        return VRManager.QTVRGetCurrentNodeID(qtvr);
    }

    private static int QTVRGetNodeType(int qtvr, int nodeID) {
        return VRManager.QTVRGetNodeType(qtvr, nodeID);
    }

    private static short QTVRTriggerHotSpot(int qtvr, int hotSpotID, int nodeInfo, int selectedAtom) {
        return VRManager.QTVRTriggerHotSpot(qtvr, hotSpotID, nodeInfo, selectedAtom);
    }

    private static short QTVREnableHotSpot(int qtvr, int enableFlag, int hotSpotValue, byte enable) {
        return VRManager.QTVREnableHotSpot(qtvr, enableFlag, hotSpotValue, enable);
    }

    private static short QTVRUpdate(int qtvr, int forceBlitMode) {
        return VRManager.QTVRUpdate(qtvr, forceBlitMode);
    }

    private static short QTVRGoToNodeID(int qtvr, int nodeID) {
        return VRManager.QTVRGoToNodeID(qtvr, nodeID);
    }

    private static short QTVRNudge(int qtvr, int direction) {
        return VRManager.QTVRNudge(qtvr, direction);
    }

    private static float QTVRGetPanAngle(int qtvr) {
        return VRManager.QTVRGetPanAngle(qtvr);
    }

    private static short QTVRSetPanAngle(int qtvr, float panAngle) {
        return VRManager.QTVRSetPanAngle(qtvr, panAngle);
    }

    private static float QTVRGetTiltAngle(int qtvr) {
        return VRManager.QTVRGetTiltAngle(qtvr);
    }

    private static short QTVRSetTiltAngle(int qtvr, float tiltAngle) {
        return VRManager.QTVRSetTiltAngle(qtvr, tiltAngle);
    }

    private static float QTVRGetFieldOfView(int qtvr) {
        return VRManager.QTVRGetFieldOfView(qtvr);
    }

    private static short QTVRSetFieldOfView(int qtvr, float fieldOfView) {
        return VRManager.QTVRSetFieldOfView(qtvr, fieldOfView);
    }

    private static short QTVRGetConstraints(int qtvr, short kind, float[] minValue, float[] maxValue) {
        return VRManager.QTVRGetConstraints(qtvr, kind, minValue, maxValue);
    }

    private static short QTVRSetConstraints(int qtvr, short kind, float minValue, float maxValue) {
        return VRManager.QTVRSetConstraints(qtvr, kind, minValue, maxValue);
    }

    private static short QTVRSetImagingProperty(int qtvr, int imagingMode, int imagingProperty, int propertyValue) {
        return VRManager.QTVRSetImagingProperty(qtvr, imagingMode, imagingProperty, propertyValue);
    }

    private static short QTVRGetImagingProperty(int qtvr, int imagingMode, int imagingProperty, int[] propertyValue) {
        return VRManager.QTVRGetImagingProperty(qtvr, imagingMode, imagingProperty, propertyValue);
    }

    private static int QTVRGetAngularUnits(int qtvr) {
        return VRManager.QTVRGetAngularUnits(qtvr);
    }

    private static short QTVRSetAngularUnits(int qtvr, int units) {
        return VRManager.QTVRSetAngularUnits(qtvr, units);
    }

    private static short QTVRPtToAngles(int qtvr, int pt, float[] panAngle, float[] tiltAngle) {
        return VRManager.QTVRPtToAngles(qtvr, pt, panAngle, tiltAngle);
    }

    private static short QTVRPanToColumn(int qtvr, float panAngle) {
        return VRManager.QTVRPanToColumn(qtvr, panAngle);
    }

    private static float QTVRColumnToPan(int qtvr, short column) {
        return VRManager.QTVRColumnToPan(qtvr, column);
    }

    private static short QTVRTiltToRow(int qtvr, float tiltAngle) {
        return VRManager.QTVRTiltToRow(qtvr, tiltAngle);
    }

    private static float QTVRRowToTilt(int qtvr, short row) {
        return VRManager.QTVRRowToTilt(qtvr, row);
    }

    private static short QTVRWrapAndConstrain(int qtvr, short kind, float value, float[] result) {
        return VRManager.QTVRWrapAndConstrain(qtvr, kind, value, result);
    }

    private static short QTVRShowDefaultView(int qtvr) {
        return VRManager.QTVRShowDefaultView(qtvr);
    }

    private static short QTVRSetViewCenter(int qtvr, byte[] viewCenter) {
        return VRManager.QTVRSetViewCenter(qtvr, viewCenter);
    }

    private static short QTVRGetViewCenter(int qtvr, byte[] viewCenter) {
        return VRManager.QTVRGetViewCenter(qtvr, viewCenter);
    }

    private static short QTVRGetHotSpotType(int qtvr, int hotSpotID, int[] hotSpotType) {
        return VRManager.QTVRGetHotSpotType(qtvr, hotSpotID, hotSpotType);
    }

    private static short QTVRSetEnteringNodeProc(int qtvr, int enteringNodeProc, int refCon, int flags) {
        return VRManager.QTVRSetEnteringNodeProc(qtvr, enteringNodeProc, refCon, flags);
    }

    private static short QTVRSetLeavingNodeProc(int qtvr, int leavingNodeProc, int refCon, int flags) {
        return VRManager.QTVRSetLeavingNodeProc(qtvr, leavingNodeProc, refCon, flags);
    }

    private static short QTVRSetMouseOverHotSpotProc(int qtvr, int mouseOverHotSpotProc, int refCon, int flags) {
        return VRManager.QTVRSetMouseOverHotSpotProc(qtvr, mouseOverHotSpotProc, refCon, flags);
    }

    private static short QTVRInstallInterceptProc(int qtvr, int selector, int interceptProc, int refCon, int flags) {
        return VRManager.QTVRInstallInterceptProc(qtvr, selector, interceptProc, refCon, flags);
    }

    private static short QTVRCallInterceptedProc(int qtvr, int qtvrMessage) {
        return VRManager.QTVRCallInterceptedProc(qtvr, qtvrMessage);
    }

    private static short QTVRSetPrescreenImagingCompleteProc(int qtvr, int imagingCompleteProc, int refCon, int flags) {
        return VRManager.QTVRSetPrescreenImagingCompleteProc(qtvr, imagingCompleteProc, refCon, flags);
    }

    private static short QTVRPtToHotSpotID(int qtvr, int pt, int[] hotSpotID) {
        return VRManager.QTVRPtToHotSpotID(qtvr, pt, hotSpotID);
    }

    private static int QTVRGetVisibleHotSpots(int qtvr, int hotSpots) {
        return VRManager.QTVRGetVisibleHotSpots(qtvr, hotSpots);
    }

    private static short QTVRGetHotSpotRegion(int qtvr, int hotSpotID, int hotSpotRegion) {
        return VRManager.QTVRGetHotSpotRegion(qtvr, hotSpotID, hotSpotRegion);
    }

    private static short QTVRSetMouseOverTracking(int qtvr, byte enable) {
        return VRManager.QTVRSetMouseOverTracking(qtvr, enable);
    }

    private static byte QTVRGetMouseOverTracking(int qtvr) {
        return VRManager.QTVRGetMouseOverTracking(qtvr);
    }

    private static short QTVRSetMouseDownTracking(int qtvr, byte enable) {
        return VRManager.QTVRSetMouseDownTracking(qtvr, enable);
    }

    private static byte QTVRGetMouseDownTracking(int qtvr) {
        return VRManager.QTVRGetMouseDownTracking(qtvr);
    }

    private static short QTVRSetInteractionProperty(int qtvr, int property, byte value) {
        return VRManager.QTVRSetInteractionProperty(qtvr, property, value);
    }

    private static short QTVRSetInteractionProperty(int qtvr, int property, short value) {
        return VRManager.QTVRSetInteractionProperty(qtvr, property, value);
    }

    private static short QTVRSetInteractionProperty(int qtvr, int property, int value) {
        return VRManager.QTVRSetInteractionProperty(qtvr, property, value);
    }

    private static short QTVRSetInteractionProperty(int qtvr, int property, float[] value) {
        return VRManager.QTVRSetInteractionProperty(qtvr, property, value);
    }

    private static short QTVRGetInteractionProperty(int qtvr, int property, byte[] value) {
        return VRManager.QTVRGetInteractionProperty(qtvr, property, value);
    }

    private static short QTVRGetInteractionProperty(int qtvr, int property, short[] value) {
        return VRManager.QTVRGetInteractionProperty(qtvr, property, value);
    }

    private static short QTVRGetInteractionProperty(int qtvr, int property, int[] value) {
        return VRManager.QTVRGetInteractionProperty(qtvr, property, value);
    }

    private static short QTVRGetInteractionProperty(int qtvr, int property, float[] value) {
        return VRManager.QTVRGetInteractionProperty(qtvr, property, value);
    }

    private static short QTVRReplaceCursor(int qtvr, byte[] cursRecord) {
        return VRManager.QTVRReplaceCursor(qtvr, cursRecord);
    }

    private static void HLock(int h) {
        MemoryManager.HLock(h);
    }

    private static int NewHandle(int size) {
        return MemoryManager.NewHandle(size);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static void DisposeHandle(int h) {
        MemoryManager.DisposeHandle(h);
    }
}
