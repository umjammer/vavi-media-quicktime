package quicktime.std.movies;

import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.std.clocks.TimeRecord;
import quicktime.util.StringHandle;

/**
 * The ActionFilter class is used by action filters on the MovieController.
 * An application should subclass this class over-riding the method(s) that
 * it is interested in. Actions associated with methods not over-ridden will
 * return false, indicating to the Movie toolbox that it should handle the action.
 * @see quicktime.std.clocks.QTCallBack
 */
public abstract class ActionFilter {

    /**
     * This method should be overridden to handle the following actions:
     * mcActionIdle ID = 1
     * mcActionActivate ID = 3
     * mcActionDeactivate ID = 4
     * mcActionDraw ID = 2 (The Graphics object of the controller can be retrieved from the controller if required)
     * mcActionSetColorTable (The ColorTable object of the movie can be retrieved from the movie)
     * mcActionForceTimeTableUpdate ID = 61
     * mcActionMovieChanged ID = 77
     * mcActionControllerSizeChanged ID = 26 (The controller object can be retrieved from the movie)
     * mcActionSuspend ID = 46
     * mcActionResume ID = 47
     * mcActionMovieEdited ID = 50
     * mcActionMovieFinished ID = 86
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionGetVolume
     * mcActionGetPlayRate
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param value In the first element of this array the application should return
     *  the desired value for this action and return true. If the application wants the
     *  MovieToolbox to return the current value then return false.
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, float value) {
        return false;
    }

    public boolean execute(MovieController movieController, int action, float[] params) {
        return false;
    }

    public boolean execute(MovieController movieController, int action, boolean[] params) {
        return false;
    }

    public boolean execute(MovieController movieController, int action, int[] params) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionSetLooping
     * mcActionSetLoopIsPalindrome
     * mcActionSetKeysEnabled
     * mcActionSetPlaySelection
     * mcActionSetUseBadge
     * mcActionSetPlayEveryFrame
     * mcActionSetCursorSettingEnabled
     * mcActionGetDragEnabled
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param value the value that is associated with that action
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, boolean value) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionSetGrowBoxBounds
     * mcActionGetTimeSliderRect
     *
     * @param mc the movie controller the action is invoked upon
     * @param rect the QDRect that defines the bounds of the movie
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, QDRect rect) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionStep
     * mcActionSetFlags
     * mcActionAppMessageReceived
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param value the value that is associated with that action
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, int value) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionGoToTime
     * mcActionSetSelectionBegin
     * mcActionGetSelectionBegin
     * mcActionSetSelectionDuration
     * mcActionGetSelectionDuration
     *
     * @param mc the movie controller the action is invoked upon
     * @param timeRecord the id of the action that invoked this method
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, TimeRecord timeRecord) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionExecuteAllActionsForQTEvent
     * mcActionExecuteOneActionForQTEvent
     *
     * This class will pass on the wired sprites actions.
     *
     * mcActionExecuteAllActionsForQTEvent
     *
     * This allows your application to filter QuickTime events for
     * which the movie has actions. This is sent before any actions
     * are executed. Return true from your filter proc if you do
     * not want the actions to be executed; return false if you do
     * want the actions executed.
     * The parameter is a ResolvedQTEventSpecPtr. The type of
     * QuickTime event may be determined from the QTAtomSpec
     * portion. The atom is either a kQTEventFrameLoaded atom, or
     * a kQTEventType atom in which case its ID specifies the type
     * of QuickTime event.
     *
     * mcActionExecuteOneActionForQTEvent
     *
     * This allows your application to filter individual actions
     * which are about to be executed in response to a QuickTime
     * event. This is sent before the action is executed. Return true
     * from your filter proc if you do not want the action to be
     * executed, return false if you do want the action executed.
     * The parameter is a ResolvedQTEventSpecPtr. The type of
     * action may be determined from the QTAtomSpec portion. The
     * atom is a kAction atom. The type of action is specified by
     * the leaf data of its child atom of type kWhichAction.
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param es a ResolvedQTEventSpec associated with the action
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, ResolvedQTEventSpec es) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionPerformActionList
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param container the AtomContainer for the perform action list
     * @param atom the atom
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, AtomContainer container, Atom atom) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionLinkToURL
     * mcActionShowMessageString
     * mcActionGetNextURL
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param string The String returned by the movie controller
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, StringHandle string) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionShowStatusString
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param stringTypeFlags information about the string.
     * @param string The String returned by the movie controller
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, int stringTypeFlags, StringHandle string) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionForceTimeTableUpdate
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param point the Point in local coordinates
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, QDPoint point) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionGetExternalMovie
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, QTGetExternalMovieRecord qtGetExternalMovieRecord) {
        return false;
    }

    /**
     * This method should be overridden to handle the following actions:
     * mcActionGetChapterTime
     *
     * @param mc the movie controller the action is invoked upon
     * @param action the id of the action that invoked this method
     * @param tr the TimeRecord
     * @param str the String
     * @return true if you handled the action otherwise false
     */
    public boolean execute(MovieController mc, int action, TimeRecord tr, String str) {
        return false;
    }
}
