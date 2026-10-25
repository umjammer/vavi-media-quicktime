package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.TimeCodeManager;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;
import quicktime.std.qtcomponents.TimeCodeDescription;
import quicktime.std.qtcomponents.TimeCoder;
import quicktime.std.sg.SGChannel;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTPointer;
import quicktime.util.QTPointerRef;

/**
 * The UserData class implements the corresponding data structure of the Movie Toolbox.
 * This structure specifies the user data list for an operation. You obtain a user data list identifier
 * by calling the GetMovieUserData, GetTrackUserData, or GetMediaUserData functions
 * (described on page 2-231 of Inside Macintosh: QuickTime, page 2-232 of
 * Inside Macintosh: QuickTime, and page 2-233 of Inside Macintosh: QuickTime, respectively).
 */
public final class UserData extends QTObject implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(UserData.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * This method allows your application to obtain access to a GraphicsExporter user data list.
     * QuickTime::GraphicsExportGetMetaData()
     * @return a reference to the GraphicsExporter's user data list.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.GraphicsExporter#getMetaData()}
     */
    public static UserData fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsExportGetMetaData(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return new UserData(arrayOfInt[0], paramGraphicsExporter);
    }

    /**
     * This method allows your application to obtain access to a movie's user data list.
     * QuickTime::GetMovieUserData()
     * @return a reference to the movie's user data list.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.Movie#getUserData()}
     */
    public static UserData fromMovie(Movie paramMovie) throws StdQTException {
        int i = GetMovieUserData(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return new UserData(i, paramMovie);
    }

    /**
     * Retrieves the current settings of all channels used by the sequence grabber.
     * The sequence grabber places all of this configuration information into a
     * Movie Toolbox user data list.
     * QuickTime::SGGetSettings
     * @return A Movie Toolbox user data list that contains the configuration information.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.sg.SequenceGrabber#getSettings()}
     */
    public static UserData fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SGGetSettings(QTObject.ID(paramSequenceGrabber), arrayOfInt, 0);
        StdQTException.checkError(i);
        return new UserData(arrayOfInt[0], null);
    }

    /**
     * Retrieves the current settings of the SequenceGrabber channel.
     * The sequence grabber places this configuration information into a Movie Toolbox user data list.
     * QuickTime::SGGetChannelSettings
     * @see {@link quicktime.std.sg.SGChannel#getSettings()}
     */
    public static UserData fromSGChannel(SGChannel paramSGChannel) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SGGetChannelSettings(QTObject.ID(paramSGChannel.getSequenceGrabber()), QTObject.ID(paramSGChannel), arrayOfInt, 0);
        StdQTException.checkError(i);
        return new UserData(arrayOfInt[0], null);
    }

    /**
     * This method allows your application to obtain access to a track's user data list.
     * QuickTime::GetTrackUserData()
     * @return a UserData object referencing the track's user data list.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.Track#getUserData()}
     */
    public static UserData fromTrack(Track paramTrack) throws StdQTException {
        int i = GetTrackUserData(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new UserData(i, paramTrack);
    }

    /**
     * This method allows your application to obtain access to a media's user data list.
     * QuickTime::GetMediaUserData()
     * @return a UserData object for the media's user data list.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.media.Media#getUserData()}
     */
    public static UserData fromMedia(Media paramMedia) throws StdQTException {
        int i = GetMediaUserData(QTObject.ID(paramMedia));
        StdQTException.checkError(GetMoviesError());
        return new UserData(i, paramMedia);
    }

    /**
     * This method allows your application to obtain access to a media's user data list.
     * QuickTime::TCGetSourceRef()
     * @param timeCoder timecode media handler.
     * @param tcd the description that describes the time code reference to be changed
     * @return a UserData object for the media's user data list.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.qtcomponents.TimeCoder#getUserDataSourceRef(quicktime.std.qtcomponents.TimeCodeDescription)}
     */
    public static UserData fromTimeCoderSource(TimeCoder paramTimeCoder, TimeCodeDescription paramTimeCodeDescription) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(TCGetSourceRef(QTObject.ID(paramTimeCoder), QTObject.ID(paramTimeCodeDescription), arrayOfInt));
        return new UserData(arrayOfInt[0], null);
    }

    /**
     * This constructor creates a UserData object.
     * QuickTime::NewUserData()
     */
    public UserData() throws StdQTException {
        this(allocate(), null);
    }

    /**
     * This constructor creates a UserData object from a handle that is in the standard user data storage format.
     * QuickTime::NewUserDataFromHandle()
     * @param handle Specifies the handle for the user data to be constructed.
     */
    public UserData(QTHandle paramQTHandle) throws StdQTException {
        this(allocate(paramQTHandle), null);
    }

    private UserData(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    private static int allocate() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = NewUserData(arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(QTHandle paramQTHandle) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = NewUserDataFromHandle(QTObject.ID(paramQTHandle), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method returns a handle which contains a publicly parseable form of the user data.
     * QuickTime::PutUserDataIntoHandle()
     * @return A QTHandle object which contains a publicly parseable form of the user data.
     * @return {@link quicktime.QTException}
     */
    public QTHandle putIntoHandle() throws QTException {
        QTHandle localQTHandle = new QTHandle();
        int i = PutUserDataIntoHandle(_ID(), QTObject.ID(localQTHandle));
        StdQTException.checkError(i);
        return localQTHandle;
    }

    /**
     * This method returns a specified user data item.
     * QuickTime::GetUserData()
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @return The handle which contains the data from the specified item.
     * @return {@link quicktime.QTException}
     */
    public QTHandle getData(int paramInt1, int paramInt2) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        int i = GetUserData(_ID(), QTObject.ID(localQTHandle), paramInt1, paramInt2);
        StdQTException.checkError(i);
        return localQTHandle;
    }

    /**
     * This method allows your application to add an item to a user data list.
     * QuickTime::AddUserData()
     * @param data Specifies the handle to the data to be added to the user data list.
     * @param udType Specifies the item's type value.
     * @param {@link quicktime.std.StdQTException}
     */
    public void addData(QTHandleRef paramQTHandleRef, int paramInt) throws StdQTException {
        int i = AddUserData(_ID(), QTObject.ID(paramQTHandleRef), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * This method removes an item from a user data list.
     * QuickTime::RemoveUserData()
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @param {@link quicktime.std.StdQTException}
     */
    public void removeData(int paramInt1, int paramInt2) throws StdQTException {
        int i = RemoveUserData(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to determine the number of items of a given type in a user data list.
     * QuickTime::CountUserDataType()
     * @param udType Specifies the type.
     * @return The number of items of the specified type in the user data list.
     * @return {@link quicktime.std.StdQTException}
     */
    public int countType(int paramInt) throws StdQTException {
        int i = CountUserDataType(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to retrieve the next user data type in a specified user data list.
     * QuickTime::GetNextUserDataType()
     * @param udType Specifies a user data type.
     * @return The next user data type value in the specified user data list.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getNextType(int paramInt) throws StdQTException {
        int i = GetNextUserDataType(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a specified user data item.
     * QuickTime::GetUserDataItem()
     * @param dataSize Specifies the size of the data.
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @return a QTPointer object that received the data from the specified item.
     * @return {@link quicktime.QTException}
     */
    public QTPointer getDataItem(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        QTPointer localQTPointer = new QTPointer(paramInt1, true);
        int i = GetUserDataItem(_ID(), QTObject.ID(localQTPointer), localQTPointer.getSize(), paramInt2, paramInt3);
        StdQTException.checkError(i);
        return localQTPointer;
    }

    /**
     * This method allows your application to set an item in a user data list.
     * QuickTime::SetUserDataItem()
     * @param data Specifies the pointer to the data item to be set in a user data list.
     * @param udType Specifies the type value assigned to the new item.
     * @param index Specifies the item's index value.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDataItem(QTPointerRef paramQTPointerRef, int paramInt1, int paramInt2) throws StdQTException {
        int i = SetUserDataItem(_ID(), QTObject.ID(paramQTPointerRef), paramQTPointerRef.getSize(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to place language-tagged text into an item in a user data list.
     * QuickTime::AddUserDataText()
     * @param data Specifies the handle to the data to be added to the user data list.
     * @param udType Specifies the type that is to be assigned to the new item.
     * @param index Specifies the item to which the text is to be added.
     * @param itlRegionTag Specifies the region code of the text to be added.
     * @param {@link quicktime.std.StdQTException}
     */
    public void addText(QTHandleRef paramQTHandleRef, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = AddUserDataText(_ID(), QTObject.ID(paramQTHandleRef), paramInt1, paramInt2, (short) paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to place language-tagged text into an item in a user data list.
     * QuickTime::AddUserDataText()
     * @param udType Specifies the type that is to be assigned to the new item.
     * @param index Specifies the item to which the text is to be added.
     * @param itlRegionTag Specifies the region code of the text to be added.
     * @param {@link quicktime.std.StdQTException}
     */
    public void addText(String paramString, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = NewHandle(paramString.length());
        byte[] arrayOfByte = new byte[paramString.length()];
        paramString.getBytes(0, paramString.length(), arrayOfByte, 0);
        HLock(i);
        int j = getIntFromPointer(i, 0);
        copyArrayToPointer(arrayOfByte, 0, j, 0, paramString.length());
        int k = AddUserDataText(_ID(), i, paramInt1, paramInt2, (short) paramInt3);
        DisposeHandle(i);
        StdQTException.checkError(k);
    }

    /**
     * Deprecated.
     * <I>since QTJava 4, see getTextAsString</I>
     * This method allows your application to retrieve language-tagged text from an item in a user data list.
     * QuickTime::GetUserDataText()
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @param itlRegionTag Specifies the language code of the text to be retrieved.
     * @return a QTHandle object that received the data.
     * @return {@link quicktime.QTException}
     */
    public QTHandle getText(int paramInt1, int paramInt2, short paramShort) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        int i = GetUserDataText(_ID(), QTObject.ID(localQTHandle), paramInt1, paramInt2, paramShort);
        StdQTException.checkError(i);
        return localQTHandle;
    }

    /**
     * This method allows your application to retrieve language-tagged text from an item in a user data list.
     * QuickTime::GetUserDataText()
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @param itlRegionTag Specifies the language code of the text to be retrieved.
     * @return a QTHandle object that received the data.
     * @return {@link quicktime.QTException}
     */
    public String getTextAsString(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        int i = NewHandle(4);
        int j = GetUserDataText(_ID(), i, paramInt1, paramInt2, (short) paramInt3);
        if (j == 0) {
            byte[] arrayOfByte = new byte[GetHandleSize(i)];
            HLock(i);
            copyPointerToArray(getIntFromPointer(i, 0), 0, arrayOfByte, 0, arrayOfByte.length);
            DisposeHandle(i);
            return new String(arrayOfByte);
        }
        throw new StdQTException(j);
    }

    /**
     * This method allows your application to remove language-tagged text from an item in a user data list.
     * QuickTime::RemoveUserDataText()
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @param itlRegionTag Specifies the language code of the text to be removed.
     * @param {@link quicktime.std.StdQTException}
     */
    public void removeText(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = RemoveUserDataText(_ID(), paramInt1, paramInt2, (short) paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * Copies the user data to the destination user data object
     * QuickTime::CopyUserData()
     * @param destUserData destination user data object
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @param {@link quicktime.std.StdQTException}
     */
    public void copyFromUserData(UserData paramUserData, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(CopyUserData(_ID(), QTObject.ID(paramUserData), paramBoolean == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified user data object
     * QuickTime::CopyUserData()
     * @param srcUserData source user data object
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @param {@link quicktime.std.StdQTException}
     */
    public void copyToUserData(UserData paramUserData, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(CopyUserData(QTObject.ID(paramUserData), _ID(), paramBoolean == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified user data object into the destination user data object
     * QuickTime::CopyUserData()
     * @param srcUserData source user data object
     * @param destUserData destination user data object
     * @param copyRule constant for merge or replace
     *     use StdQTConstants5_1.kQTCopyUserDataReplace or StdQTConstants5_1.kQTCopyUserDataMerge
     * @param {@link quicktime.std.StdQTException}
     */
    public static void copyUserData(UserData paramUserData1, UserData paramUserData2, int paramInt) throws StdQTException {
        StdQTException.checkError(CopyUserData(QTObject.ID(paramUserData1), QTObject.ID(paramUserData2), paramInt));
    }

    private static void copyArrayToPointer(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }

    private static int NewHandle(int paramInt) {
        return MemoryManager.NewHandle(paramInt);
    }

    private static int GetHandleSize(int paramInt) {
        return MemoryManager.GetHandleSize(paramInt);
    }

    private static void DisposeHandle(int paramInt) {
        MemoryManager.DisposeHandle(paramInt);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static void HLock(int paramInt) {
        MemoryManager.HLock(paramInt);
    }

    private static int TCGetSourceRef(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return TimeCodeManager.TCGetSourceRef(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int GraphicsExportGetMetaData(int paramInt, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetMetaData(paramInt, paramArrayOfInt);
    }

    private static int GetMovieUserData(int paramInt) {
        return MovieToolbox.GetMovieUserData(paramInt);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int SGGetSettings(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        return SequenceGrabberManager.SGGetSettings(paramInt1, paramArrayOfInt, paramInt2);
    }

    private static int SGGetChannelSettings(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        return SequenceGrabberManager.SGGetChannelSettings(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    }

    private static int GetTrackUserData(int paramInt) {
        return MovieToolbox.GetTrackUserData(paramInt);
    }

    private static int GetMediaUserData(int paramInt) {
        return MovieToolbox.GetMediaUserData(paramInt);
    }

    private static short NewUserData(int[] paramArrayOfInt) {
        return MovieToolbox.NewUserData(paramArrayOfInt);
    }

    private static short NewUserDataFromHandle(int paramInt, int[] paramArrayOfInt) {
        return MovieToolbox.NewUserDataFromHandle(paramInt, paramArrayOfInt);
    }

    private static short PutUserDataIntoHandle(int paramInt1, int paramInt2) {
        return MovieToolbox.PutUserDataIntoHandle(paramInt1, paramInt2);
    }

    private static short GetUserData(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieToolbox.GetUserData(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short AddUserData(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.AddUserData(paramInt1, paramInt2, paramInt3);
    }

    private static short RemoveUserData(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.RemoveUserData(paramInt1, paramInt2, paramInt3);
    }

    private static short CountUserDataType(int paramInt1, int paramInt2) {
        return MovieToolbox.CountUserDataType(paramInt1, paramInt2);
    }

    private static int GetNextUserDataType(int paramInt1, int paramInt2) {
        return MovieToolbox.GetNextUserDataType(paramInt1, paramInt2);
    }

    private static short GetUserDataItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MovieToolbox.GetUserDataItem(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static short SetUserDataItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MovieToolbox.SetUserDataItem(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static short AddUserDataText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort) {
        return MovieToolbox.AddUserDataText(paramInt1, paramInt2, paramInt3, paramInt4, paramShort);
    }

    private static short GetUserDataText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort) {
        return MovieToolbox.GetUserDataText(paramInt1, paramInt2, paramInt3, paramInt4, paramShort);
    }

    private static short RemoveUserDataText(int paramInt1, int paramInt2, int paramInt3, short paramShort) {
        return MovieToolbox.RemoveUserDataText(paramInt1, paramInt2, paramInt3, paramShort);
    }

    private static int MediaSetGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MediaSetGraphicsMode(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int MediaGetGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MediaGetGraphicsMode(paramInt, paramArrayOfInt, paramArrayOfByte);
    }

    private static int MediaGetTrackOpaque(int paramInt, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MediaGetTrackOpaque(paramInt, paramArrayOfByte);
    }

    private static short CopyUserData(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.CopyUserData(paramInt1, paramInt2, paramInt3);
    }
}

