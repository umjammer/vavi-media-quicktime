package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.TimeCodeManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.TimeCodeMedia;
import quicktime.std.movies.media.UserData;
import quicktime.std.movies.media.VisualMediaHandler;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;

public final class TimeCoder extends VisualMediaHandler implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(TimeCoder.class));
            }
        }.establish();
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaHandler()"
     * @return a MediaHandler object for the media's media handler.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.TimeCodeMedia#getTimeCodeMediaHandler,
     * quicktime.std.movies.media.Media#getMediaHandler
     */
    public static TimeCoder fromMedia(TimeCodeMedia paramTimeCodeMedia) throws StdQTException {
        int i = GetMediaHandler(QTObject.ID(paramTimeCodeMedia));
        StdQTException.checkError(GetMoviesError());
        return new TimeCoder(i, paramTimeCodeMedia);
    }

    private TimeCoder(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Determines the time code from the time code handler at the current time value.
     * @see "QuickTime::TCGetCurrentTimeCode()"
     * @return The information object containing the time code, its definition, and data.
     * @throws quicktime.QTException
     */
    public TimeCodeInfo getCurrent() throws QTException {
        return QTHandle.fromTimeCoderCurrent(this);
    }

    /**
     * Determines the time code from the time code handler.
     * @see "QuickTime::TCGetTimeCodeAtTime()"
     * @param mediaTime The time value of the media at which to retrieve the time code information.
     * @return The information object containing the time code, its definition, and data.
     * @throws quicktime.QTException
     */
    public TimeCodeInfo getAtTime(int mediaTime) throws QTException {
        return QTHandle.fromTimeCoderTime(this, mediaTime);
    }

    /**
     * Converts a time value into a string (HH:MM:SS:FF) - if the timecode uses a drop frame technique
     * the separators are semi-colons (HH;MM;SS;FF).
     * @see "QuickTime::TCTimeCodeToString()"
     * @param tcdef the time code definition structure used for the conversion
     * @param tct the time code time structure used for the conversion
     * @return the converted string
     * @throws quicktime.std.StdQTException
     */
    public String timeCodeToString(TimeCodeDef tcdef, TimeCodeTime tct) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        StdQTException.checkError(TCTimeCodeToString(_ID(), tcdef.getBytes(), tct.getBytes(), arrayOfByte));
        return new String(arrayOfByte, 1, arrayOfByte[0]);
    }

    /**
     * Converts a time value into a string (HH:MM:SS:FF) - if the timecode uses a drop frame technique
     * the separators are semi-colons (HH;MM;SS;FF).
     * @see "QuickTime::TCTimeCodeToString()"
     * @param tcdef the time code definition structure used for the conversion
     * @param counter the time code counter value
     * @return the converted string
     * @throws quicktime.std.StdQTException
     */
    public String timeCodeToString(TimeCodeDef tcdef, int counter) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int[] arrayOfInt = { counter };
        StdQTException.checkError(TCTimeCodeToString(_ID(), tcdef.getBytes(), arrayOfInt, arrayOfByte));
        return new String(arrayOfByte, 1, arrayOfByte[0]);
    }

    /**
     * Converts a time value into a frame number using specified definitions.
     * @see "QuickTime::TCTimeCodeToFrameNumber()"
     * @param tcrec The time code value structure to convert.
     * @param tcdef The time code definition object used for the conversion.
     * @return The frame number.
     * @throws quicktime.std.StdQTException
     */
    public int toFrameNumber(TimeCodeTime tcrec, TimeCodeDef tcdef) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = TCTimeCodeToFrameNumber(_ID(), tcdef.getBytes(), tcrec.getBytes(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Converts a frame number into a time value using specified definitions.
     * @see "QuickTime::TCFrameNumberToTimeCode()"
     * @param frameNumber The frame number to convert.
     * @param tcdef The time code definition object used for the conversion.
     * @return The time code value structure.
     * @throws quicktime.std.StdQTException
     */
    public TimeCodeTime toTimeCode(int frameNumber, TimeCodeDef tcdef) throws StdQTException {
        TimeCodeTime localTimeCodeTime = new TimeCodeTime();
        int i = TCFrameNumberToTimeCode(_ID(), frameNumber, tcdef.getBytes(), localTimeCodeTime.getBytes());
        StdQTException.checkError(i);
        return localTimeCodeTime;
    }

    /**
     * @deprecated
     * <I>since QTJava 5.0</I>
     * Allows you to retrieve the source information from the time code media sample.
     * @see "QuickTime::TCGetSourceRef()"
     * @param tcd the description that describes the time code reference to be changed
     * @return the source information that is found in the sample reference
     * @throws quicktime.QTException
     */
    public QTHandle getSourceRef(TimeCodeDescription tcd) throws QTException {
        return QTHandle.fromTimeCoderSource(this, tcd);
    }

    /**
     * Allows you to retrieve the source information from the time code media sample.
     * @see "QuickTime::TCGetSourceRef()"
     * @param tcd the description that describes the time code reference to be changed
     * @return the UserData that is found in the sample reference
     * @throws quicktime.QTException
     * @see quicktime.std.movies.media.UserData#fromTimeCoderSource(quicktime.std.qtcomponents.TimeCoder, quicktime.std.qtcomponents.TimeCodeDescription)
     */
    public UserData getUserDataSourceRef(TimeCodeDescription tcd) throws QTException {
        return UserData.fromTimeCoderSource(this, tcd);
    }

    /**
     * Allows you to change the source information in the media sample.
     * @see "QuickTime::TCSetSourceRef()"
     * @param tcd the description that describes the time code reference to be changed
     * @param sref the source information that is placed in the sample reference
     * @throws quicktime.std.StdQTException
     */
    public void setSourceRef(TimeCodeDescription tcd, QTHandleRef sref) throws StdQTException {
        StdQTException.checkError(TCSetSourceRef(_ID(), QTObject.ID(tcd), QTObject.ID(sref)));
    }

    /**
     * Allows you to change the source information in the media sample.
     * @see "QuickTime::TCSetSourceRef()"
     * @param tcd the description that describes the time code reference to be changed
     * @param udata the userdata source that is placed in the sample reference
     * @throws quicktime.std.StdQTException
     */
    public void setUserDataSourceRef(TimeCodeDescription tcd, UserData udata) throws StdQTException {
        StdQTException.checkError(TCSetSourceRef(_ID(), QTObject.ID(tcd), QTObject.ID(udata)));
    }

    /**
     * Allows you to change the flags that affect how QT handles the timecode information.
     * @see "QuickTime::TCSetTimeCodeFlags()"
     * @param flags specifies the new flag value
     * @param flagsMask specifies which of the flag values are to change.
     * @throws quicktime.std.StdQTException
     */
    public void setFlags(int flags, int flagsMask) throws StdQTException {
        StdQTException.checkError(TCSetTimeCodeFlags(_ID(), flags, flagsMask));
    }

    /**
     * Allows you to retrieve the timecode control flags.
     * @see "QuickTime::TCGetTimeCodeFlags()"
     * @return the timecode control flags
     * @throws quicktime.std.StdQTException
     */
    public int getFlags() throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(TCGetTimeCodeFlags(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Sets the current display options for text by the TimeCoder.
     * @see "QuickTime::TCSetDisplayOptions()"
     * @param textOptions text display options
     * @throws quicktime.std.StdQTException
     */
    public void setDisplayOptions(TCTextOptions textOptions) throws StdQTException {
        StdQTException.checkError(TCSetDisplayOptions(_ID(), textOptions.getBytes()));
    }

    /**
     * Returns the current display options for text by the TimeCoder.
     * @see "QuickTime::TCGetDisplayOptions()"
     * @return text display options
     * @throws quicktime.std.StdQTException
     */
    public TCTextOptions getDisplayOptions() throws StdQTException {
        TCTextOptions localTCTextOptions = new TCTextOptions();
        StdQTException.checkError(TCGetDisplayOptions(_ID(), localTCTextOptions.getBytes()));
        return localTCTextOptions;
    }

    private static int GetMediaHandler(int paramInt) {
        return MovieToolbox.GetMediaHandler(paramInt);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int TCTimeCodeToString(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return TimeCodeManager.TCTimeCodeToString(paramInt, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static int TCTimeCodeToString(int paramInt, byte[] paramArrayOfByte1, int[] paramArrayOfInt, byte[] paramArrayOfByte2) {
        return TimeCodeManager.TCTimeCodeToString(paramInt, paramArrayOfByte1, paramArrayOfInt, paramArrayOfByte2);
    }

    private static int TCTimeCodeToFrameNumber(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        return TimeCodeManager.TCTimeCodeToFrameNumber(paramInt, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfInt);
    }

    private static int TCFrameNumberToTimeCode(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return TimeCodeManager.TCFrameNumberToTimeCode(paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int TCSetSourceRef(int paramInt1, int paramInt2, int paramInt3) {
        return TimeCodeManager.TCSetSourceRef(paramInt1, paramInt2, paramInt3);
    }

    private static int TCSetTimeCodeFlags(int paramInt1, int paramInt2, int paramInt3) {
        return TimeCodeManager.TCSetTimeCodeFlags(paramInt1, paramInt2, paramInt3);
    }

    private static int TCGetTimeCodeFlags(int paramInt, int[] paramArrayOfInt) {
        return TimeCodeManager.TCGetTimeCodeFlags(paramInt, paramArrayOfInt);
    }

    private static int TCSetDisplayOptions(int paramInt, byte[] paramArrayOfByte) {
        return TimeCodeManager.TCSetDisplayOptions(paramInt, paramArrayOfByte);
    }

    private static int TCGetDisplayOptions(int paramInt, byte[] paramArrayOfByte) {
        return TimeCodeManager.TCGetDisplayOptions(paramInt, paramArrayOfByte);
    }
}

