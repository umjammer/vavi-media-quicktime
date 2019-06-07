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
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QTVRInstance.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * You can use the QTVRGetQTVRInstance function to get an instance of a QuickTime VR movie.
     * QuickTime::QTVRGetQTVRInstance
     * @param qtvrTrack A QTVR track contained in a QuickTime movie. You can obtain a reference to this track by
     *        calling QTVRGetQTVRTrack
     * @param mc A movie controller that is associated with the new QuickTime VR movie
     */
    public QTVRInstance(Track paramTrack, MovieController paramMovieController) throws QTException {
        super(allocate(paramTrack, paramMovieController));
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

    private Hashtable<Integer, VRDispatcher> ipProcs = new Hashtable<>();

    /**
     * The QTVRGetVRWorld function returns an atom
     * container that contains general scene information about the QuickTime VR movie
     * specified, as well as a list of all the nodes in that movie.
     * QuickTime::QTVRGetVRWorld
     * @return the VRWorld AtomContainer
     * @return {@link quicktime.vr.QTVRException}
     */
    public AtomContainer getVRWorld() throws QTVRException {
        return AtomContainer.fromQTVRInstanceWorld(this);
    }

    /**
     * You can use the QTVRGetNodeInfo function to get the node information atom container
     * that describes a node and all the hot spots in the node.
     * QuickTime::QTVRGetNodeInfo
     * @param nodeID Set this parameter to kQTVRCurrentNode to receive information
     *        about the current node
     * @return an atom container that contains information about the
     *        specified node.
     * @return {@link quicktime.vr.QTVRException}
     */
    public AtomContainer getNodeInfo(int paramInt) throws QTVRException {
        return AtomContainer.fromQTVRInstanceNode(this, paramInt);
    }

    /**
     * You can use the QTVRTriggerHotSpot function to trigger a hot spot.
     * Usually, you need only specify
     * the qtvr instance and the hot spot ID. You can pass zero for the nodeInfo and
     * selectedAtom parameters.
     * QuickTime::QTVRTriggerHotSpot
     * @param hotSpotID A hot spot ID.
     * @param nodeInfo A node information atom container (obtained from a previous
     *                 call to QTVRGetNodeInfo). You can pass the value 0 in this
     *                 parameter to have the QuickTime VR Manager determine the
     *                 appropriate node information atom container.
     * @param selectedAtom The atom of the hot spot to trigger. You can pass the value 0 in
     *                 this parameter to have the QuickTime VR Manager determine
     *                 the appropriate hot spot atom.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void triggerHotSpot(int paramInt, AtomContainer paramAtomContainer, Atom paramAtom) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRTriggerHotSpot(_ID(), paramInt, QTObject.ID(paramAtomContainer), paramAtom == null ? 0 : paramAtom.getAtom());
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVREnableHotSpot function to enable or disable one or more hot
     * spots.
     * QuickTime::QTVREnableHotSpot
     * @param enableFlag The kind of hot spot or hot spots to enable or disable.
     * @param hotSpotValue The desired hot spot or spots, relative to the specified enabled flag.
     * @param enable A Boolean value that indicates whether the specified hot spots are to be enabled (true) or disabled (false).
     * @param {@link quicktime.vr.QTVRException}
     */
    public void enableHotSpot(int paramInt1, int paramInt2, boolean paramBoolean) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVREnableHotSpot(_ID(), paramInt1, paramInt2, (byte) (paramBoolean ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRUpdate function to force an immediate update of a
     * QuickTime VR movie image.
     * QuickTime::QTVRUpdate
     * @param imagingMode An imaging mode. You can specify
     *                 the kQTVRCurrentMode imaging mode to use the current
     *                 imaging mode.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void update(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRUpdate(_ID(), paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRGoToNodeID function to set the current node of a movie.
     * QuickTime::QTVRGoToNodeID
     * @param nodeID The ID of the node you want to be the current node.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void goToNodeID(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGoToNodeID(_ID(), paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRNudge function to turn one step in a particular direction and
     * display the new view.
     * QuickTime::QTVRNudge
     * @param direction The direction of the nudge.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void nudge(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRNudge(_ID(), paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetPanAngle function to set the pan angle of a
     * QuickTime VR movie.
     * QuickTime::QTVRSetPanAngle
     * @param panAngle The desired pan angle of the specified movie.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setPanAngle(float paramFloat) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetPanAngle(_ID(), paramFloat);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetTiltAngle function to set the tilt angle of a
     * QuickTime VR movie.
     * QuickTime::QTVRSetTiltAngle
     * @param tiltAngle The desired tilt angle of the specified movie.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setTiltAngle(float paramFloat) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetTiltAngle(_ID(), paramFloat);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetFieldOfView function to set the vertical field of view of a
     * QuickTime VR movie.
     * QuickTime::QTVRSetFieldOfView
     * @param fieldOfView The desired vertical field of view for the specified movie.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setFieldOfView(float paramFloat) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetFieldOfView(_ID(), paramFloat);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRGetConstraints function to get the current constraints of
     * a movie.
     * QuickTime::QTVRGetConstraints
     * @param kind The type of constraints to be returned.
     * @param {@link quicktime.vr.QTVRException}
     */
    public float getConstraints_min(int paramInt) throws QTVRException {
        float[] arrayOfFloat1 = { 0.0F };
        float[] arrayOfFloat2 = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetConstraints(_ID(), (short) paramInt, arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat1[0];
    }

    /**
     * You can use the QTVRGetConstraints function to get the current constraints of
     * a movie.
     * QuickTime::QTVRGetConstraints
     * @param kind The type of constraints to be returned.
     * @param {@link quicktime.vr.QTVRException}
     */
    public float getConstraints_max(int paramInt) throws QTVRException {
        float[] arrayOfFloat1 = { 0.0F };
        float[] arrayOfFloat2 = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetConstraints(_ID(), (short) paramInt, arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat2[0];
    }

    /**
     * You can use the QTVRSetConstraints function to set the constraints of a movie.
     * QuickTime::QTVRSetConstraints
     * @param kind The type of constraint to set..
     * @param minValue A floating-point value that contains the desired minimum constraint of the specified type.
     * @param maxValue A floating-point value that contains the desired maximum constraint of the specified type.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setConstraints(int paramInt, float paramFloat1, float paramFloat2) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetConstraints(_ID(), (short) paramInt, paramFloat1, paramFloat2);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRSetImagingProperty function to set the current value of an
     * imaging property of a movie.
     * QuickTime::QTVRSetImagingProperty
     * @param imagingMode an imaging mode.
     * @param imagingProperty an imaging property.
     * @param propertyValue sets the current value of the specified imaging property for the specified mode.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setImagingProperty(int paramInt1, int paramInt2, int paramInt3) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetImagingProperty(_ID(), paramInt1, paramInt2, paramInt3);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRGetImagingProperty function to get the current value of an
     * imaging property of a movie.
     * QuickTime::QTVRGetImagingProperty
     * @param imagingMode an imaging mode.
     * @param imagingProperty an imaging property.
     * @return the current value of the specified imaging property for the specified mode.
     * @return {@link quicktime.vr.QTVRException}
     */
    public int getImagingProperty(int paramInt1, int paramInt2) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetImagingProperty(_ID(), paramInt1, paramInt2, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * You can use QTVRSetAngularUnits to set the type of unit used when specifying angles.
     * QuickTime::QTVRSetAngularUnits
     * @param units A constant that indicates the type of angular units to use.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setAngularUnits(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetAngularUnits(_ID(), paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * You can use QTVRPtToAngles to get the pan angle of a point.
     * QuickTime::QTVRPtToAngles
     * @param pt A point, in the local coordinates of the graphics world of the specified movie.
     * @param {@link quicktime.vr.QTVRException}
     */
    public float ptToPanAngle(QDPoint paramQDPoint) throws QTVRException {
        float[] arrayOfFloat1 = { 0.0F };
        float[] arrayOfFloat2 = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToAngles(_ID(), paramQDPoint.getPoint(), arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat1[0];
    }

    /**
     * You can use QTVRPtToAngles to get the tilt angle of a point.
     * QuickTime::QTVRPtToAngles
     * @param pt A point, in the local coordinates of the graphics world of the specified movie.
     * @param {@link quicktime.vr.QTVRException}
     */
    public float ptToTiltAngle(QDPoint paramQDPoint) throws QTVRException {
        float[] arrayOfFloat1 = { 0.0F };
        float[] arrayOfFloat2 = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToAngles(_ID(), paramQDPoint.getPoint(), arrayOfFloat1, arrayOfFloat2);
        }
        QTVRException.checkError(i);
        return arrayOfFloat2[0];
    }

    /**
     * You can use QTVRWrapAndConstrain to preflight a change in the viewing or control characteristics of an object or
     * panoramic node.
     * QuickTime::QTVRWrapAndConstrain
     * @param kind a constraint type. See "Constraint Types" for a description of the available constraint types.
     * @param value the desired value of the specified viewing characteristic.
     * @param {@link quicktime.vr.QTVRException}
     */
    public float wrapAndConstrain(int paramInt, float paramFloat) throws QTVRException {
        float[] arrayOfFloat = { 0.0F };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRWrapAndConstrain(_ID(), (short) paramInt, paramFloat, arrayOfFloat);
        }
        QTVRException.checkError(i);
        return arrayOfFloat[0];
    }

    /**
     * You can use the QTVRSetEnteringNodeProc function to install or remove a
     * node-entering procedure.
     * QuickTime::QTVRSetEnteringNodeProc
     * @param enteringNodeProc an instance of the QTVREnteringNode object that will be called or null to remove the callback.
     * @param flags the flags
     * @param {@link quicktime.vr.QTVRException}
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
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

    /**
     * You can use the QTVRSetEnteringNodeProc function to remove a
     * node-entering procedure.
     * QuickTime::QTVRSetEnteringNodeProc
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
     * QuickTime::QTVRSetLeavingNodeProc
     * @param leavingNodeProc an instance of the QTVRLeavingNode object that will be called or null to remove the callback.
     * @param flags the flags
     * @param {@link quicktime.vr.QTVRException}
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
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

    /**
     * You can use the QTVRSetLeavingNodeProc function to remove a
     * node-leaving procedure.
     * QuickTime::QTVRSetLeavingNodeProc
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
     * QuickTime::QTVRSetMouseOverHotSpotProc
     * @param mouseOverHotSpotProc an instance of the QTVRMouseOverHotSpot object that will be called or null to remove the callback.
     * @param flags the flags
     * @param {@link quicktime.vr.QTVRException}
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
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

    /**
     * You can use the QTVRSetMouseOverHotSpotProc function to remove a
     * mouse over hot spot procedure.
     * QuickTime::QTVRSetMouseOverHotSpotProc
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
     * QuickTime::QTVRInstallInterceptProc
     * @param selector only the non-mouse selectors are supported
     * @param interceptProc the interceptor or null to remove a previously specified callback for the specified selector.
     * @param flags the flags
     * @param {@link quicktime.vr.QTVRException}
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
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

    /**
     * You can use the QTVRInstallInterceptProc function to install or remove an intercept procedure for a
     * QuickTime VR Manager function
     * QuickTime::QTVRInstallInterceptProc
     * @param selector only the non-mouse selectors are supported
     * @param {@link quicktime.vr.QTVRException}
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
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

    /**
     * This method is used by the QTVRInterceptor execute method to call the QTVR default
     * proc for the selector that was intercepted.
     * QuickTime::QTVRCallInterceptedProc
     * @see {@link quicktime.std.clocks.QTCallBack}
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
     * QuickTime::QTVRShowDefaultView
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
     * QuickTime::QTVRSetViewCenter
     * @param viewCenter A QDPoint structure that contains the desired view center of the specified movie.
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setViewCenter(QDPoint paramQDPoint) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewCenter(_ID(), paramQDPoint.getQTVRFloatPoint());
        }
        QTVRException.checkError(i);
    }

    /**
     * The getViewCenter method returns, in the QDPoint class, the x and y coordinates of the current view center of the QuickTimeÊVR movie specified by the QTVRInstance you call this on.
     * QuickTime::QTVRGetViewCenter
     * @return the veiw center
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRGetHotSpotType
     * @param hotSpotID a hot spot ID
     * @return the OSType of the hot spot specified by the hot spot ID.
     * @return {@link quicktime.vr.QTVRException}
     */
    public int getHotSpotType(int paramInt) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetHotSpotType(_ID(), paramInt, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method installs a callback that is invoked every time a QTVRInstance is drawn. The application can use this
     * callback to draw onto the QDGraphics that is supplied to the callback.
     * QuickTime::QTVRSetPrescreenImagingCompleteProc
     * @param flags effect the conditions under which QTVR will invoke the callback
     * @param {@link quicktime.vr.QTVRException}
     * @see {@link quicktime.std.clocks.QTCallBack}
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
     * QuickTime::QTVRSetPrescreenImagingCompleteProc
     * @see {@link quicktime.std.clocks.QTCallBack}
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
     * QuickTime::QTVRGetHotSpotRegion
     * @param hotSpotID A hot spot ID
     * @return the region occupied by the hot spot having the specified ID
     * @return {@link quicktime.QTException}
     */
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
     * QuickTime::QTVRPtToHotSpotID
     * @param pt A point, in the local coordinates of the graphics world of the specified movie
     * @return the hot spot ID or 0
     * @return {@link quicktime.vr.QTVRException}
     */
    public int ptToHotSpotID(QDPoint paramQDPoint) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRPtToHotSpotID(_ID(), paramQDPoint.getPoint(), arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Sets the mouse-over tracking state of the QuickTimeÊVR movie specified by the QTVRInstance
     * object the method is called from. By default, QuickTimeÊVR tracks mouse movements in a
     * QuickTimeÊVR movie and changes the shape of the cursor as appropriate.
     * QuickTime::QTVRSetMouseOverTracking
     * @param enable a boolean specified whether tracking should be disabled (false) or enabled (true)
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setMouseOverTracking(boolean paramBoolean) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseOverTracking(_ID(), (byte) (paramBoolean ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    /**
     * Returns a Boolean value that indicates whether QuickTimeÊVR is currently handling mouse-over
     * tracking for the QuickTimeÊVR movie specified by the QTVRInstance ( true ) or not ( false ).
     * By default, QuickTimeÊVR tracks mouse movements in a QuickTimeÊVR movie and changes the shape
     * of the cursor as appropriate.
     * QuickTime::QTVRGetMouseOverTracking
     * @return true if QTVR is handling mouse-over tracking, false otherwise
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetMouseDownTracking
     * @param enable a boolean specified whether tracking should be disabled (false) or enabled (true)
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setMouseDownTracking(boolean paramBoolean) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetMouseDownTracking(_ID(), (byte) (paramBoolean ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    /**
     * Returns, as its function result, a Boolean value that indicates whether QuickTimeÊVR is
     * currently handling mouse-down tracking for the QuickTimeÊVR movie specified by the
     * parameter ( true ) or not ( false ). By default, QuickTimeÊVR tracks mouse clicks in
     * a QuickTimeÊVR movie and triggers hot spots as necessary.
     * QuickTime::QTVRGetMouseDownTracking
     * @return true if QTVR is handling mouse-down tracking, false otherwise
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetInteractionProperty
     * @param hysteresis the mouse-click hysteresis
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setInteractionMouseClickHysteresis(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 1, (short) paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the Mouse Click Hysteresis property of the Interaction property defined as the distance,
     * in pixels, from the location of a mouse-down event to the limit within which the cursor is
     * considered not to have moved.
     * QuickTime::QTVRGetInteractionProperty
     * @return the mouse-click hysteresis value
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetInteractionProperty
     * @param timeout the mouse-click timeout
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setInteractionMouseClickTimeout(int paramInt) throws QTVRException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 2, paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the Mouse Click Timeout Interaction property defined as the number of ticks after which a
     * mouse click times out and is automatically switched from a hot spot selection into a pan.
     * QuickTime::QTVRGetInteractionProperty
     * @return the mouse-click timeout value
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetInteractionProperty
     * @param speed the pan and tilt speed
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setInteractionPanTiltSpeed(int paramInt) throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 3, paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the panning and tilting speed interaction property
     * QuickTime::QTVRGetInteractionProperty
     * @return the pan and tilt speed
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetInteractionProperty
     * @param speed the zoom speed
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setInteractionZoomSpeed(int paramInt) throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 4, paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the zoom speed interaction property
     * QuickTime::QTVRGetInteractionProperty
     * @return the zoom speed
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetInteractionProperty
     * @param translate sets the mouseDown translation property
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setInteractionTranslateOnMouseDown(boolean paramBoolean) throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 101, (byte) (paramBoolean ? 1 : 0));
        }
        QTVRException.checkError(i);
    }

    /**
     * Sets the translation interaction property
     * QuickTime::QTVRGetInteractionProperty
     * @return the zoom speed
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetInteractionProperty
     * @param scale sets the mouse motion scale property
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setInteractionMouseMotionScale(float paramFloat) throws QTVRException {
        float[] arrayOfFloat = { paramFloat };
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 102, arrayOfFloat);
        }
        QTVRException.checkError(i);
    }

    /**
     * Sets the mouse motion scale interaction property
     * QuickTime::QTVRGetInteractionProperty
     * @return the mouse motion scale
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetInteractionProperty
     * @param mode sets the nudge mode property (use kQTVRNudgeRotate, kQTVRNudgeTranslate, or kQTVRNudgeSameAsMouse)
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setInteractionNudgeMode(int paramInt) throws QTVRException {
        int i;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetInteractionProperty(_ID(), 103, paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * Gets the nudge mode interaction property
     * QuickTime::QTVRGetInteractionProperty
     * @return the nudge mode
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetViewParameter
     * Requires QT 5.0.1 and above
     * @param viewParameter one of kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * @param flagsIn zero or combination of kQTVRValueIsRelative kQTVRValueIsRate kQTVRValueIsUserPrefRelative
     *         kQTVRPanAngle:	zero (absolute) kQTVRValueIsRelative (relative to value)
     *         kQTVRTiltAngle:	zero (absolute) kQTVRValueIsRelative (relative to value)
     *         kQTVRFieldOfViewAngle: zero (absolute) kQTVRValueIsRelative (relative to value)
     *                 kQTVRValueIsRate + kQTVRValueIsUserPrefRelative (zoom in/out)
     * @param value value of the view angle
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setViewAngle(int paramInt1, int paramInt2, float paramFloat) throws QTVRException {
        int i = 0;
        float[] arrayOfFloat = { paramFloat };
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), paramInt1, arrayOfFloat, paramInt2);
        }
        QTVRException.checkError(i);
    }

    /**
     * kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * Only the above parameters can be passed into this call.
     * QuickTime::QTVRGetViewParameter
     * Requires QT 5.0.1 and above
     * @param viewParameter one of kQTVRPanAngle, kQTVRTiltAngle, kQTVRFieldOfViewAngle
     * @return the viewParameter value
     * @return {@link quicktime.vr.QTVRException}
     */
    public float getViewAngle(int paramInt) throws QTVRException {
        int i = 0;
        int j = 0;
        int[] arrayOfInt = { 0 };
        float[] arrayOfFloat = { 0.0F };
        synchronized (QTNative.globalsLock) {
            i = QTVRGetViewParameter(_ID(), paramInt, arrayOfFloat, j, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfFloat[0];
    }

    /**
     * kQTVRViewCenter
     *
     * QuickTime::QTVRSetViewParameter
     * Requires QT 5.0.1 and above
     * @param flagsIn zero or combination of kQTVRValueIsRelative kQTVRValueIsRate kQTVRValueIsUserPrefRelative
     *         kQTVRViewCenter: zero (absolute) kQTVRValueIsRelative (relative to value)
     * @param center point specifying the center
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setViewCentre(int paramInt, QDPoint paramQDPoint) throws QTVRException {
        int i = 0;
        float[] arrayOfFloat = { paramQDPoint.getXF(), paramQDPoint.getYF() };
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), 260, arrayOfFloat, paramInt);
        }
        QTVRException.checkError(i);
    }

    /**
     * kQTVRViewCenter
     *
     * QuickTime::QTVRGetViewParameter
     * Requires QT 5.0.1 and above
     * @return the center
     * @return {@link quicktime.vr.QTVRException}
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
     * QuickTime::QTVRSetViewParameter
     * Requires QT 5.0.1 and above
     * @param value true if visible
     * @param {@link quicktime.vr.QTVRException}
     */
    public void setHotSpotsVisible(boolean paramBoolean) throws QTVRException {
        int i = 0;
        int j = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRSetViewParameter(_ID(), 512, (byte) (paramBoolean ? 1 : 0), j);
        }
        QTVRException.checkError(i);
    }

    /**
     * kQTVRHotSpotsVisible
     *
     * QuickTime::QTVRGetViewParameter
     * Requires QT 5.0.1 and above
     * @return true if hotspot is visible
     * @return {@link quicktime.vr.QTVRException}
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
     * @param {@link quicktime.vr.QTVRException}
     */
    public void replaceCursor(CursorRecord paramCursorRecord) throws QTVRException {
        int i = QTVRReplaceCursor(_ID(), paramCursorRecord.getBytes());
        QTVRException.checkError(i);
    }

    /**
     * You can use the QTVRGetCurrentNodeID function to get the current node of
     * a movie.
     * QuickTime::QTVRGetCurrentNodeID
     * @return the ID of the current node of the specified movie.
     */
    public int getCurrentNodeID() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetCurrentNodeID(_ID());
        }
    }

    /**
     * You can use the QTVRGetNodeType function to get the OSType of a movie node.
     * QuickTime::QTVRGetNodeType
     * @param nodeID A node ID. Pass kQTVRCurrentNode for the current node.
     * @return the type of the specified node.
     */
    public int getNodeType(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return QTVRGetNodeType(_ID(), paramInt);
        }
    }

    /**
     * You can use the QTVRGetPanAngle function to get the pan angle of a
     * QuickTime VR movie.
     * QuickTime::QTVRGetPanAngle
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
     * QuickTime::QTVRGetTiltAngle
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
     * QuickTime::QTVRGetFieldOfView
     * @return the current vertical field of view of the specified movie.
     */
    public float getFieldOfView() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetFieldOfView(_ID());
        }
    }

    /**
     * You can use QTVRGetAngularUnits to get the type of unit currently used when specifying angles.
     * QuickTime::QTVRGetAngularUnits
     * @return a constant that indicates the type of angular units currently in use
     */
    public int getAngularUnits() {
        synchronized (QTNative.globalsLock) {
            return QTVRGetAngularUnits(_ID());
        }
    }

    /**
     * You can use QTVRPanToColumn to get the column number in the object image array that corresponds to a pan angle.
     * QuickTime::QTVRPanToColumn
     * @param panAngle a pan angle.
     * @return the column number in the object image array that corresponds to the specified pan angle.
     */
    public int panToColumn(float paramFloat) {
        synchronized (QTNative.globalsLock) {
            return QTVRPanToColumn(_ID(), paramFloat);
        }
    }

    /**
     * You can use QTVRColumnToPan to get the pan angle that corresponds to a column number in the object image array.
     * QuickTime::QTVRColumnToPan
     * @param column A column number.
     * @return the pan angle that corresponds to the specified column number in the object image array.
     */
    public float columnToPan(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return QTVRColumnToPan(_ID(), (short) paramInt);
        }
    }

    /**
     * You can use QTVRTiltToRow to get the row number in the object image array that corresponds to a tilt angle.
     * QuickTime::QTVRTiltToRow
     * @param tiltAngle a tilt angle.
     * @return The row number in the object image array that corresponds to the specified tilt angle.
     */
    public int tiltToRow(float paramFloat) {
        synchronized (QTNative.globalsLock) {
            return QTVRTiltToRow(_ID(), paramFloat);
        }
    }

    /**
     * You can use QTVRRowToTilt to get the tilt angle that corresponds to a row number in the object image array.
     * QuickTime::QTVRRowToTilt
     * @param row a row number.
     * @return the tilt angle that corresponds to the specified row number in the object image array.
     */
    public float rowToTilt(int paramInt) {
        synchronized (QTNative.globalsLock) {
            return QTVRRowToTilt(_ID(), (short) paramInt);
        }
    }

    private static native short QTVRSetViewParameter(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);

    private static native short QTVRGetViewParameter(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int[] paramArrayOfInt);

    private static native short QTVRSetViewParameter(int paramInt1, int paramInt2, byte paramByte, int paramInt3);

    private static native short QTVRGetViewParameter(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int[] paramArrayOfInt);

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

    private static native short QTVRGetConstraints(int paramInt, short paramShort, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2);

    private static native short QTVRSetConstraints(int paramInt, short paramShort, float paramFloat1, float paramFloat2);

    private static native short QTVRSetImagingProperty(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRGetImagingProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int QTVRGetAngularUnits(int paramInt);

    private static native short QTVRSetAngularUnits(int paramInt1, int paramInt2);

    private static native short QTVRPtToAngles(int paramInt1, int paramInt2, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2);

    private static native short QTVRPanToColumn(int paramInt, float paramFloat);

    private static native float QTVRColumnToPan(int paramInt, short paramShort);

    private static native short QTVRTiltToRow(int paramInt, float paramFloat);

    private static native float QTVRRowToTilt(int paramInt, short paramShort);

    private static native short QTVRWrapAndConstrain(int paramInt, short paramShort, float paramFloat, float[] paramArrayOfFloat);

    private static native short QTVRShowDefaultView(int paramInt);

    private static native short QTVRSetViewCenter(int paramInt, byte[] paramArrayOfByte);

    private static native short QTVRGetViewCenter(int paramInt, byte[] paramArrayOfByte);

    private static native short QTVRGetHotSpotType(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTVRSetEnteringNodeProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRSetLeavingNodeProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRSetMouseOverHotSpotProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTVRInstallInterceptProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

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
