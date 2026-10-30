package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.TimeCodeManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.TimeCodeMedia;
import quicktime.std.movies.media.UserData;
import quicktime.std.movies.media.VisualMediaHandler;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;


public final class TimeCoder extends VisualMediaHandler implements QuickTimeLib {

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     *
     * @return a MediaHandler object for the media's media handler.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetMediaHandler()"
     * @see "quicktime.std.movies.media.TimeCodeMedia#getTimeCodeMediaHandler"
     * @see "quicktime.std.movies.media.Media#getMediaHandler"
     */
    public static TimeCoder fromMedia(TimeCodeMedia media) throws StdQTException {
        int i = GetMediaHandler(QTObject.ID(media));
        StdQTException.checkError(GetMoviesError());
        return new TimeCoder(i, media);
    }

    private TimeCoder(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Determines the time code from the time code handler at the current time value.
     *
     * @return The information object containing the time code, its definition, and data.
     * @throws quicktime.QTException
     * @see "QuickTime::TCGetCurrentTimeCode()"
     */
    public TimeCodeInfo getCurrent() throws QTException {
        return QTHandle.fromTimeCoderCurrent(this);
    }

    /**
     * Determines the time code from the time code handler.
     *
     * @param mediaTime The time value of the media at which to retrieve the time code information.
     * @return The information object containing the time code, its definition, and data.
     * @throws quicktime.QTException
     * @see "QuickTime::TCGetTimeCodeAtTime()"
     */
    public TimeCodeInfo getAtTime(int mediaTime) throws QTException {
        return QTHandle.fromTimeCoderTime(this, mediaTime);
    }

    /**
     * Converts a time value into a string (HH:MM:SS:FF) - if the timecode uses a drop frame technique
     * the separators are semi-colons (HH;MM;SS;FF).
     *
     * @param tcdef the time code definition structure used for the conversion
     * @param tct   the time code time structure used for the conversion
     * @return the converted string
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCTimeCodeToString()"
     */
    public String timeCodeToString(TimeCodeDef tcdef, TimeCodeTime tct) throws StdQTException {
        byte[] str = new byte[256];
        StdQTException.checkError(TCTimeCodeToString(_ID(), tcdef.getBytes(), tct.getBytes(), str));
        return new String(str, 1, str[0]);
    }

    /**
     * Converts a time value into a string (HH:MM:SS:FF) - if the timecode uses a drop frame technique
     * the separators are semi-colons (HH;MM;SS;FF).
     *
     * @param tcdef   the time code definition structure used for the conversion
     * @param counter the time code counter value
     * @return the converted string
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCTimeCodeToString()"
     */
    public String timeCodeToString(TimeCodeDef tcdef, int counter) throws StdQTException {
        byte[] str = new byte[256];
        int[] arrayOfInt = {counter};
        StdQTException.checkError(TCTimeCodeToString(_ID(), tcdef.getBytes(), arrayOfInt, str));
        return new String(str, 1, str[0]);
    }

    /**
     * Converts a time value into a frame number using specified definitions.
     *
     * @param tcrec The time code value structure to convert.
     * @param tcdef The time code definition object used for the conversion.
     * @return The frame number.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCTimeCodeToFrameNumber()"
     */
    public int toFrameNumber(TimeCodeTime tcrec, TimeCodeDef tcdef) throws StdQTException {
        int[] frameNumber = new int[1];
        int i = TCTimeCodeToFrameNumber(_ID(), tcdef.getBytes(), tcrec.getBytes(), frameNumber);
        StdQTException.checkError(i);
        return frameNumber[0];
    }

    /**
     * Converts a frame number into a time value using specified definitions.
     *
     * @param frameNumber The frame number to convert.
     * @param tcdef       The time code definition object used for the conversion.
     * @return The time code value structure.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCFrameNumberToTimeCode()"
     */
    public TimeCodeTime toTimeCode(int frameNumber, TimeCodeDef tcdef) throws StdQTException {
        TimeCodeTime timeCodeTime = new TimeCodeTime();
        int i = TCFrameNumberToTimeCode(_ID(), frameNumber, tcdef.getBytes(), timeCodeTime.getBytes());
        StdQTException.checkError(i);
        return timeCodeTime;
    }

    /**
     * @param tcd the description that describes the time code reference to be changed
     * @return the source information that is found in the sample reference
     * @throws quicktime.QTException
     * @see "QuickTime::TCGetSourceRef()"
     * @deprecated <I>since QTJava 5.0</I>
     * Allows you to retrieve the source information from the time code media sample.
     */
    public QTHandle getSourceRef(TimeCodeDescription tcd) throws QTException {
        return QTHandle.fromTimeCoderSource(this, tcd);
    }

    /**
     * Allows you to retrieve the source information from the time code media sample.
     *
     * @param tcd the description that describes the time code reference to be changed
     * @return the UserData that is found in the sample reference
     * @throws quicktime.QTException
     * @see "QuickTime::TCGetSourceRef()"
     * @see quicktime.std.movies.media.UserData#fromTimeCoderSource(quicktime.std.qtcomponents.TimeCoder, quicktime.std.qtcomponents.TimeCodeDescription)
     */
    public UserData getUserDataSourceRef(TimeCodeDescription tcd) throws QTException {
        return UserData.fromTimeCoderSource(this, tcd);
    }

    /**
     * Allows you to change the source information in the media sample.
     *
     * @param tcd  the description that describes the time code reference to be changed
     * @param sref the source information that is placed in the sample reference
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCSetSourceRef()"
     */
    public void setSourceRef(TimeCodeDescription tcd, QTHandleRef sref) throws StdQTException {
        StdQTException.checkError(TCSetSourceRef(_ID(), QTObject.ID(tcd), QTObject.ID(sref)));
    }

    /**
     * Allows you to change the source information in the media sample.
     *
     * @param tcd   the description that describes the time code reference to be changed
     * @param udata the userdata source that is placed in the sample reference
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCSetSourceRef()"
     */
    public void setUserDataSourceRef(TimeCodeDescription tcd, UserData udata) throws StdQTException {
        StdQTException.checkError(TCSetSourceRef(_ID(), QTObject.ID(tcd), QTObject.ID(udata)));
    }

    /**
     * Allows you to change the flags that affect how QT handles the timecode information.
     *
     * @param flags     specifies the new flag value
     * @param flagsMask specifies which of the flag values are to change.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCSetTimeCodeFlags()"
     */
    public void setFlags(int flags, int flagsMask) throws StdQTException {
        StdQTException.checkError(TCSetTimeCodeFlags(_ID(), flags, flagsMask));
    }

    /**
     * Allows you to retrieve the timecode control flags.
     *
     * @return the timecode control flags
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCGetTimeCodeFlags()"
     */
    public int getFlags() throws StdQTException {
        int[] flags = new int[1];
        StdQTException.checkError(TCGetTimeCodeFlags(_ID(), flags));
        return flags[0];
    }

    /**
     * Sets the current display options for text by the TimeCoder.
     *
     * @param textOptions text display options
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCSetDisplayOptions()"
     */
    public void setDisplayOptions(TCTextOptions textOptions) throws StdQTException {
        StdQTException.checkError(TCSetDisplayOptions(_ID(), textOptions.getBytes()));
    }

    /**
     * Returns the current display options for text by the TimeCoder.
     *
     * @return text display options
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TCGetDisplayOptions()"
     */
    public TCTextOptions getDisplayOptions() throws StdQTException {
        TCTextOptions displayOptions = new TCTextOptions();
        StdQTException.checkError(TCGetDisplayOptions(_ID(), displayOptions.getBytes()));
        return displayOptions;
    }

    private static int GetMediaHandler(int media) {
        return MovieToolbox.GetMediaHandler(media);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int TCTimeCodeToString(int mh, byte[] tcdef, byte[] tcrec, byte[] str) {
        return TimeCodeManager.TCTimeCodeToString(mh, tcdef, tcrec, str);
    }

    private static int TCTimeCodeToString(int mh, byte[] tcdef, int[] counter, byte[] str) {
        return TimeCodeManager.TCTimeCodeToString(mh, tcdef, counter, str);
    }

    private static int TCTimeCodeToFrameNumber(int mh, byte[] tcdef, byte[] tcrec, int[] frameNumber) {
        return TimeCodeManager.TCTimeCodeToFrameNumber(mh, tcdef, tcrec, frameNumber);
    }

    private static int TCFrameNumberToTimeCode(int mh, int frameNumber, byte[] tcdef, byte[] tcrec) {
        return TimeCodeManager.TCFrameNumberToTimeCode(mh, frameNumber, tcdef, tcrec);
    }

    private static int TCSetSourceRef(int mh, int tcdH, int srefH) {
        return TimeCodeManager.TCSetSourceRef(mh, tcdH, srefH);
    }

    private static int TCSetTimeCodeFlags(int mh, int flags, int flagsMask) {
        return TimeCodeManager.TCSetTimeCodeFlags(mh, flags, flagsMask);
    }

    private static int TCGetTimeCodeFlags(int mh, int[] flags) {
        return TimeCodeManager.TCGetTimeCodeFlags(mh, flags);
    }

    private static int TCSetDisplayOptions(int mh, byte[] options) {
        return TimeCodeManager.TCSetDisplayOptions(mh, options);
    }

    private static int TCGetDisplayOptions(int mh, byte[] options) {
        return TimeCodeManager.TCGetDisplayOptions(mh, options);
    }
}
