/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


/**
 * VRManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class VRManager {

    private VRManager() {
    }

    /** used by QTSession */
    public static short InitializeQTVR() {
        throw new UnsupportedToolboxException("InitializeQTVR");
    }

    /** used by QTVRInstance */
    public static short QTVRCallInterceptedProc(int qtvr, int qtvrMessage) {
        throw new UnsupportedToolboxException("QTVRCallInterceptedProc");
    }

    /** used by QTVRInstance */
    public static float QTVRColumnToPan(int qtvr, short column) {
        throw new UnsupportedToolboxException("QTVRColumnToPan");
    }

    /** used by QTVRInstance */
    public static short QTVREnableHotSpot(int qtvr, int enableFlag, int hotSpotValue, byte enable) {
        throw new UnsupportedToolboxException("QTVREnableHotSpot");
    }

    /** used by QTVRInstance */
    public static int QTVRGetAngularUnits(int qtvr) {
        throw new UnsupportedToolboxException("QTVRGetAngularUnits");
    }

    /** used by QTVRInstance */
    public static short QTVRGetConstraints(int qtvr, short kind, float[] minValue, float[] maxValue) {
        throw new UnsupportedToolboxException("QTVRGetConstraints");
    }

    /** used by QTVRInstance */
    public static int QTVRGetCurrentNodeID(int qtvr) {
        throw new UnsupportedToolboxException("QTVRGetCurrentNodeID");
    }

    /** used by QTVRInstance */
    public static float QTVRGetFieldOfView(int qtvr) {
        throw new UnsupportedToolboxException("QTVRGetFieldOfView");
    }

    /** used by QTVRInstance */
    public static short QTVRGetHotSpotRegion(int qtvr, int hotSpotID, int hotSpotRegion) {
        throw new UnsupportedToolboxException("QTVRGetHotSpotRegion");
    }

    /** used by QTVRInstance */
    public static short QTVRGetHotSpotType(int qtvr, int hotSpotID, int[] hotSpotType) {
        throw new UnsupportedToolboxException("QTVRGetHotSpotType");
    }

    /** used by QTVRInstance */
    public static short QTVRGetImagingProperty(int qtvr, int imagingMode, int imagingProperty, int[] propertyValue) {
        throw new UnsupportedToolboxException("QTVRGetImagingProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRGetInteractionProperty(int qtvr, int property, byte[] value) {
        throw new UnsupportedToolboxException("QTVRGetInteractionProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRGetInteractionProperty(int qtvr, int property, float[] value) {
        throw new UnsupportedToolboxException("QTVRGetInteractionProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRGetInteractionProperty(int qtvr, int property, int[] value) {
        throw new UnsupportedToolboxException("QTVRGetInteractionProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRGetInteractionProperty(int qtvr, int property, short[] value) {
        throw new UnsupportedToolboxException("QTVRGetInteractionProperty");
    }

    /** used by QTVRInstance */
    public static byte QTVRGetMouseDownTracking(int qtvr) {
        throw new UnsupportedToolboxException("QTVRGetMouseDownTracking");
    }

    /** used by QTVRInstance */
    public static byte QTVRGetMouseOverTracking(int qtvr) {
        throw new UnsupportedToolboxException("QTVRGetMouseOverTracking");
    }

    /** used by AtomContainer */
    public static short QTVRGetNodeInfo(int qtvr, int nodeID, int[] nodeInfo) {
        throw new UnsupportedToolboxException("QTVRGetNodeInfo");
    }

    /** used by QTVRInstance */
    public static int QTVRGetNodeType(int qtvr, int nodeID) {
        throw new UnsupportedToolboxException("QTVRGetNodeType");
    }

    /** used by QTVRInstance */
    public static float QTVRGetPanAngle(int qtvr) {
        throw new UnsupportedToolboxException("QTVRGetPanAngle");
    }

    /** used by QTVRInstance */
    public static short QTVRGetQTVRInstance(int[] qtvr, int qtvrTrack, int mc) {
        throw new UnsupportedToolboxException("QTVRGetQTVRInstance");
    }

    /** used by Movie */
    public static int QTVRGetQTVRTrack(int movie, int index) {
        throw new UnsupportedToolboxException("QTVRGetQTVRTrack");
    }

    /** used by QTVRInstance */
    public static float QTVRGetTiltAngle(int qtvr) {
        throw new UnsupportedToolboxException("QTVRGetTiltAngle");
    }

    /** used by AtomContainer */
    public static short QTVRGetVRWorld(int qtvr, int[] vrWorld) {
        throw new UnsupportedToolboxException("QTVRGetVRWorld");
    }

    /** used by QTVRInstance */
    public static short QTVRGetViewCenter(int qtvr, byte[] viewCenter) {
        throw new UnsupportedToolboxException("QTVRGetViewCenter");
    }

    /** used by QTVRInstance */
    public static short QTVRGetViewParameter(int qtvr, int viewParameter, byte[] value, int flagsIn, int[] flagsOut) {
        throw new UnsupportedToolboxException("QTVRGetViewParameter");
    }

    /** used by QTVRInstance */
    public static short QTVRGetViewParameter(int qtvr, int viewParameter, float[] value, int flagsIn, int[] flagsOut) {
        throw new UnsupportedToolboxException("QTVRGetViewParameter");
    }

    /** used by QTVRInstance */
    public static int QTVRGetVisibleHotSpots(int qtvr, int hotSpots) {
        throw new UnsupportedToolboxException("QTVRGetVisibleHotSpots");
    }

    /** used by QTVRInstance */
    public static short QTVRGoToNodeID(int qtvr, int nodeID) {
        throw new UnsupportedToolboxException("QTVRGoToNodeID");
    }

    /** used by QTVRInstance */
    public static short QTVRInstallInterceptProc(int qtvr, int selector, int interceptProc, int refCon, int flags) {
        throw new UnsupportedToolboxException("QTVRInstallInterceptProc");
    }

    /** used by QTVRInstance */
    public static short QTVRNudge(int qtvr, int direction) {
        throw new UnsupportedToolboxException("QTVRNudge");
    }

    /** used by QTVRInstance */
    public static short QTVRPanToColumn(int qtvr, float panAngle) {
        throw new UnsupportedToolboxException("QTVRPanToColumn");
    }

    /** used by QTVRInstance */
    public static short QTVRPtToAngles(int qtvr, int pt, float[] panAngle, float[] tiltAngle) {
        throw new UnsupportedToolboxException("QTVRPtToAngles");
    }

    /** used by QTVRInstance */
    public static short QTVRPtToHotSpotID(int qtvr, int pt, int[] hotSpotID) {
        throw new UnsupportedToolboxException("QTVRPtToHotSpotID");
    }

    /** used by QTVRInstance */
    public static short QTVRReplaceCursor(int qtvr, byte[] cursRecord) {
        throw new UnsupportedToolboxException("QTVRReplaceCursor");
    }

    /** used by QTVRInstance */
    public static float QTVRRowToTilt(int qtvr, short row) {
        throw new UnsupportedToolboxException("QTVRRowToTilt");
    }

    /** used by QTVRInstance */
    public static short QTVRSetAngularUnits(int qtvr, int units) {
        throw new UnsupportedToolboxException("QTVRSetAngularUnits");
    }

    /** used by QTVRInstance */
    public static short QTVRSetConstraints(int qtvr, short kind, float minValue, float maxValue) {
        throw new UnsupportedToolboxException("QTVRSetConstraints");
    }

    /** used by QTVRInstance */
    public static short QTVRSetEnteringNodeProc(int qtvr, int enteringNodeProc, int refCon, int flags) {
        throw new UnsupportedToolboxException("QTVRSetEnteringNodeProc");
    }

    /** used by QTVRInstance */
    public static short QTVRSetFieldOfView(int qtvr, float fieldOfView) {
        throw new UnsupportedToolboxException("QTVRSetFieldOfView");
    }

    /** used by QTVRInstance */
    public static short QTVRSetImagingProperty(int qtvr, int imagingMode, int imagingProperty, int propertyValue) {
        throw new UnsupportedToolboxException("QTVRSetImagingProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRSetInteractionProperty(int qtvr, int property, byte value) {
        throw new UnsupportedToolboxException("QTVRSetInteractionProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRSetInteractionProperty(int qtvr, int property, float[] value) {
        throw new UnsupportedToolboxException("QTVRSetInteractionProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRSetInteractionProperty(int qtvr, int property, int value) {
        throw new UnsupportedToolboxException("QTVRSetInteractionProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRSetInteractionProperty(int qtvr, int property, short value) {
        throw new UnsupportedToolboxException("QTVRSetInteractionProperty");
    }

    /** used by QTVRInstance */
    public static short QTVRSetLeavingNodeProc(int qtvr, int leavingNodeProc, int refCon, int flags) {
        throw new UnsupportedToolboxException("QTVRSetLeavingNodeProc");
    }

    /** used by QTVRInstance */
    public static short QTVRSetMouseDownTracking(int qtvr, byte enable) {
        throw new UnsupportedToolboxException("QTVRSetMouseDownTracking");
    }

    /** used by QTVRInstance */
    public static short QTVRSetMouseOverHotSpotProc(int qtvr, int mouseOverHotSpotProc, int refCon, int flags) {
        throw new UnsupportedToolboxException("QTVRSetMouseOverHotSpotProc");
    }

    /** used by QTVRInstance */
    public static short QTVRSetMouseOverTracking(int qtvr, byte enable) {
        throw new UnsupportedToolboxException("QTVRSetMouseOverTracking");
    }

    /** used by QTVRInstance */
    public static short QTVRSetPanAngle(int qtvr, float panAngle) {
        throw new UnsupportedToolboxException("QTVRSetPanAngle");
    }

    /** used by QTVRInstance */
    public static short QTVRSetPrescreenImagingCompleteProc(int qtvr, int imagingCompleteProc, int refCon, int flags) {
        throw new UnsupportedToolboxException("QTVRSetPrescreenImagingCompleteProc");
    }

    /** used by QTVRInstance */
    public static short QTVRSetTiltAngle(int qtvr, float tiltAngle) {
        throw new UnsupportedToolboxException("QTVRSetTiltAngle");
    }

    /** used by QTVRInstance */
    public static short QTVRSetViewCenter(int qtvr, byte[] viewCenter) {
        throw new UnsupportedToolboxException("QTVRSetViewCenter");
    }

    /** used by QTVRInstance */
    public static short QTVRSetViewParameter(int qtvr, int viewParameter, byte value, int flagsIn) {
        throw new UnsupportedToolboxException("QTVRSetViewParameter");
    }

    /** used by QTVRInstance */
    public static short QTVRSetViewParameter(int qtvr, int viewParameter, float[] value, int flagsIn) {
        throw new UnsupportedToolboxException("QTVRSetViewParameter");
    }

    /** used by QTVRInstance */
    public static short QTVRShowDefaultView(int qtvr) {
        throw new UnsupportedToolboxException("QTVRShowDefaultView");
    }

    /** used by QTVRInstance */
    public static short QTVRTiltToRow(int qtvr, float tiltAngle) {
        throw new UnsupportedToolboxException("QTVRTiltToRow");
    }

    /** used by QTVRInstance */
    public static short QTVRTriggerHotSpot(int qtvr, int hotSpotID, int nodeInfo, int selectedAtom) {
        throw new UnsupportedToolboxException("QTVRTriggerHotSpot");
    }

    /** used by QTVRInstance */
    public static short QTVRUpdate(int qtvr, int forceBlitMode) {
        throw new UnsupportedToolboxException("QTVRUpdate");
    }

    /** used by QTVRInstance */
    public static short QTVRWrapAndConstrain(int qtvr, short kind, float value, float[] result) {
        throw new UnsupportedToolboxException("QTVRWrapAndConstrain");
    }

    /** used by QTSession */
    public static short TerminateQTVR() {
        throw new UnsupportedToolboxException("TerminateQTVR");
    }

}
