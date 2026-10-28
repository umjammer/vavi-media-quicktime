package quicktime.qd3d;

/**
 * This class represents all of the constants in the QuickDraw3D interface.
 */
public interface QD3DConstants {

    int kQ3AxisX = 0;

    int kQ3AxisY = 1;

    int kQ3AxisZ = 2;

    /**
     * define the viewer flags
     */
    int kQ3ViewerShowBadge = 1;

    /**
     * define the viewer flags
     */
    int kQ3ViewerActive = 2;

    /**
     * define the viewer flags
     */
    int kQ3ViewerControllerVisible = 4;

    /**
     * define the viewer flags
     */
    int kQ3ViewerDrawFrame = 8;

    /**
     * define the viewer flags
     */
    int kQ3ViewerDraggingOff = 16;

    /**
     * define the viewer flags
     */
    int kQ3ViewerButtonCamera = 32;

    /**
     * define the viewer flags
     */
    int kQ3ViewerButtonTruck = 64;

    /**
     * define the viewer flags
     */
    int kQ3ViewerButtonOrbit = 128;

    /**
     * define the viewer flags
     */
    int kQ3ViewerButtonZoom = 256;

    /**
     * define the viewer flags
     */
    int kQ3ViewerButtonDolly = 512;

    /**
     * define the viewer flags
     */
    int kQ3ViewerButtonReset = 1024;

    /**
     * define the viewer flags
     */
    int kQ3ViewerOutputTextMode = 2048;

    /**
     * define the viewer flags
     */
    int kQ3ViewerDragMode = 4096;

    /**
     * define the viewer flags
     */
    int kQ3ViewerDrawGrowBox = 8192;

    /**
     * define the viewer flags
     */
    int kQ3ViewerDrawDragBorder = 16384;

    /**
     * define the viewer flags
     */
    int kQ3ViewerDraggingInOff = 32768;

    /**
     * define the viewer flags
     */
    int kQ3ViewerDraggingOutOff = 65536;

    /**
     * define the viewer flags
     */
    int kQ3ViewerDefault = Integer.MIN_VALUE;

    /**
     * define the viewer states
     */
    int kQ3ViewerEmpty = 0;

    /**
     * define the viewer states
     */
    int kQ3ViewerHasModel = 1;

    /**
     * define the viewer states
     */
    int kQ3ViewerHasUndo = 2;

    /**
     * define the settings for the camera popup
     */
    int kQ3ViewerCameraRestore = 0;

    /**
     * define the settings for the camera popup
     */
    int kQ3ViewerCameraFit = 1;

    /**
     * define the settings for the camera popup
     */
    int kQ3ViewerCameraFront = 2;

    /**
     * define the settings for the camera popup
     */
    int kQ3ViewerCameraBack = 3;

    /**
     * define the settings for the camera popup
     */
    int kQ3ViewerCameraLeft = 4;

    /**
     * define the settings for the camera popup
     */
    int kQ3ViewerCameraRight = 5;

    /**
     * define the settings for the camera popup
     */
    int kQ3ViewerCameraTop = 6;

    /**
     * define the settings for the camera popup
     */
    int kQ3ViewerCameraBottom = 7;

    /**
     * Gestalt flags to see if the QT3DViewer is present
     */
    int gestaltQD3DViewer = 1899198051;

    /**
     * Gestalt flags to see if the QT3DViewer is present
     */
    int gestaltQD3DViewerNotPresent = 0;

    /**
     * Gestalt flags to see if the QT3DViewer is present
     */
    int gestaltQD3DViewerAvailable = 1;
}
