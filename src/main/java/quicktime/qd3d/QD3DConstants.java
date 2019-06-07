package quicktime.qd3d;

/**
 * This class represents all of the constants in the QuickDraw3D interface.
 */
public abstract interface QD3DConstants {

    public static final int kQ3AxisX = 0;

    public static final int kQ3AxisY = 1;

    public static final int kQ3AxisZ = 2;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerShowBadge = 1;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerActive = 2;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerControllerVisible = 4;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerDrawFrame = 8;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerDraggingOff = 16;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerButtonCamera = 32;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerButtonTruck = 64;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerButtonOrbit = 128;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerButtonZoom = 256;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerButtonDolly = 512;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerButtonReset = 1024;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerOutputTextMode = 2048;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerDragMode = 4096;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerDrawGrowBox = 8192;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerDrawDragBorder = 16384;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerDraggingInOff = 32768;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerDraggingOutOff = 65536;

    /**
     * define the viewer flags
     */
    public static final int kQ3ViewerDefault = Integer.MIN_VALUE;

    /**
     * define the viewer states
     */
    public static final int kQ3ViewerEmpty = 0;

    /**
     * define the viewer states
     */
    public static final int kQ3ViewerHasModel = 1;

    /**
     * define the viewer states
     */
    public static final int kQ3ViewerHasUndo = 2;

    /**
     * define the settings for the camera popup
     */
    public static final int kQ3ViewerCameraRestore = 0;

    /**
     * define the settings for the camera popup
     */
    public static final int kQ3ViewerCameraFit = 1;

    /**
     * define the settings for the camera popup
     */
    public static final int kQ3ViewerCameraFront = 2;

    /**
     * define the settings for the camera popup
     */
    public static final int kQ3ViewerCameraBack = 3;

    /**
     * define the settings for the camera popup
     */
    public static final int kQ3ViewerCameraLeft = 4;

    /**
     * define the settings for the camera popup
     */
    public static final int kQ3ViewerCameraRight = 5;

    /**
     * define the settings for the camera popup
     */
    public static final int kQ3ViewerCameraTop = 6;

    /**
     * define the settings for the camera popup
     */
    public static final int kQ3ViewerCameraBottom = 7;

    /**
     * Gestalt flags to see if the QT3DViewer is present
     */
    public static final int gestaltQD3DViewer = 1899198051;

    /**
     * Gestalt flags to see if the QT3DViewer is present
     */
    public static final int gestaltQD3DViewerNotPresent = 0;

    /**
     * Gestalt flags to see if the QT3DViewer is present
     */
    public static final int gestaltQD3DViewerAvailable = 1;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/
 * QD3DConstants.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
