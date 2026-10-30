package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.TimeCodeManager;
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

    /**
     * This method allows your application to obtain access to a GraphicsExporter user data list.
     * @see "QuickTime::GraphicsExportGetMetaData()"
     * @return a reference to the GraphicsExporter's user data list.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsExporter#getMetaData()
     */
    public static UserData fromGraphicsExporter(GraphicsExporter ge) throws StdQTException {
        int[] userData = new int[1];
        int i = GraphicsExportGetMetaData(QTObject.ID(ge), userData);
        StdQTException.checkError(i);
        return new UserData(userData[0], ge);
    }

    /**
     * This method allows your application to obtain access to a movie's user data list.
     * @see "QuickTime::GetMovieUserData()"
     * @return a reference to the movie's user data list.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.Movie#getUserData()
     */
    public static UserData fromMovie(Movie movie) throws StdQTException {
        int i = GetMovieUserData(QTObject.ID(movie));
        StdQTException.checkError(GetMoviesError());
        return new UserData(i, movie);
    }

    /**
     * Retrieves the current settings of all channels used by the sequence grabber.
     * The sequence grabber places all of this configuration information into a
     * Movie Toolbox user data list.
     * @see "QuickTime::SGGetSettings"
     * @return A Movie Toolbox user data list that contains the configuration information.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.sg.SequenceGrabber#getSettings()
     */
    public static UserData fromSequenceGrabber(SequenceGrabber sg) throws StdQTException {
        int[] userData = new int[1];
        int i = SGGetSettings(QTObject.ID(sg), userData, 0);
        StdQTException.checkError(i);
        return new UserData(userData[0], null);
    }

    /**
     * Retrieves the current settings of the SequenceGrabber channel.
     * The sequence grabber places this configuration information into a Movie Toolbox user data list.
     * @see "QuickTime::SGGetChannelSettings"
     * @see quicktime.std.sg.SGChannel#getSettings()
     */
    public static UserData fromSGChannel(SGChannel ref) throws StdQTException {
        int[] userData = new int[1];
        int i = SGGetChannelSettings(QTObject.ID(ref.getSequenceGrabber()), QTObject.ID(ref), userData, 0);
        StdQTException.checkError(i);
        return new UserData(userData[0], null);
    }

    /**
     * This method allows your application to obtain access to a track's user data list.
     * @see "QuickTime::GetTrackUserData()"
     * @return a UserData object referencing the track's user data list.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.Track#getUserData()
     */
    public static UserData fromTrack(Track track) throws StdQTException {
        int i = GetTrackUserData(QTObject.ID(track));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new UserData(i, track);
    }

    /**
     * This method allows your application to obtain access to a media's user data list.
     * @see "QuickTime::GetMediaUserData()"
     * @return a UserData object for the media's user data list.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.Media#getUserData()
     */
    public static UserData fromMedia(Media media) throws StdQTException {
        int i = GetMediaUserData(QTObject.ID(media));
        StdQTException.checkError(GetMoviesError());
        return new UserData(i, media);
    }

    /**
     * This method allows your application to obtain access to a media's user data list.
     * @see "QuickTime::TCGetSourceRef()"
     * @param timeCoder timecode media handler.
     * @param tcd the description that describes the time code reference to be changed
     * @return a UserData object for the media's user data list.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.TimeCoder#getUserDataSourceRef(quicktime.std.qtcomponents.TimeCodeDescription)
     */
    public static UserData fromTimeCoderSource(TimeCoder timeCoder, TimeCodeDescription tcd) throws StdQTException {
        int[] srefH = new int[1];
        StdQTException.checkError(TCGetSourceRef(QTObject.ID(timeCoder), QTObject.ID(tcd), srefH));
        return new UserData(srefH[0], null);
    }

    /**
     * This constructor creates a UserData object.
     * @see "QuickTime::NewUserData()"
     */
    public UserData() throws StdQTException {
        this(allocate(), null);
    }

    /**
     * This constructor creates a UserData object from a handle that is in the standard user data storage format.
     * @see "QuickTime::NewUserDataFromHandle()"
     * @param handle Specifies the handle for the user data to be constructed.
     */
    public UserData(QTHandle handle) throws StdQTException {
        this(allocate(handle), null);
    }

    private UserData(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    private static int allocate() throws StdQTException {
        int[] userData = new int[1];
        int i = NewUserData(userData);
        StdQTException.checkError(i);
        return userData[0];
    }

    private static int allocate(QTHandle h) throws StdQTException {
        int[] userData = new int[1];
        int i = NewUserDataFromHandle(QTObject.ID(h), userData);
        StdQTException.checkError(i);
        return userData[0];
    }

    /**
     * This method returns a handle which contains a publicly parseable form of the user data.
     * @see "QuickTime::PutUserDataIntoHandle()"
     * @return A QTHandle object which contains a publicly parseable form of the user data.
     * @throws quicktime.QTException
     */
    public QTHandle putIntoHandle() throws QTException {
        QTHandle h = new QTHandle();
        int i = PutUserDataIntoHandle(_ID(), QTObject.ID(h));
        StdQTException.checkError(i);
        return h;
    }

    /**
     * This method returns a specified user data item.
     * @see "QuickTime::GetUserData()"
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @return The handle which contains the data from the specified item.
     * @throws quicktime.QTException
     */
    public QTHandle getData(int udType, int index) throws QTException {
        QTHandle h = new QTHandle();
        int i = GetUserData(_ID(), QTObject.ID(h), udType, index);
        StdQTException.checkError(i);
        return h;
    }

    /**
     * This method allows your application to add an item to a user data list.
     * @see "QuickTime::AddUserData()"
     * @param data Specifies the handle to the data to be added to the user data list.
     * @param udType Specifies the item's type value.
     * @throws quicktime.std.StdQTException
     */
    public void addData(QTHandleRef data, int udType) throws StdQTException {
        int i = AddUserData(_ID(), QTObject.ID(data), udType);
        StdQTException.checkError(i);
    }

    /**
     * This method removes an item from a user data list.
     * @see "QuickTime::RemoveUserData()"
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @throws quicktime.std.StdQTException
     */
    public void removeData(int udType, int index) throws StdQTException {
        int i = RemoveUserData(_ID(), udType, index);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to determine the number of items of a given type in a user data list.
     * @see "QuickTime::CountUserDataType()"
     * @param udType Specifies the type.
     * @return The number of items of the specified type in the user data list.
     * @throws quicktime.std.StdQTException
     */
    public int countType(int udType) throws StdQTException {
        int i = CountUserDataType(_ID(), udType);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to retrieve the next user data type in a specified user data list.
     * @see "QuickTime::GetNextUserDataType()"
     * @param udType Specifies a user data type.
     * @return The next user data type value in the specified user data list.
     * @throws quicktime.std.StdQTException
     */
    public int getNextType(int udType) throws StdQTException {
        int i = GetNextUserDataType(_ID(), udType);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a specified user data item.
     * @see "QuickTime::GetUserDataItem()"
     * @param dataSize Specifies the size of the data.
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @return a QTPointer object that received the data from the specified item.
     * @throws quicktime.QTException
     */
    public QTPointer getDataItem(int dataSize, int udType, int index) throws QTException {
        QTPointer data = new QTPointer(dataSize, true);
        int i = GetUserDataItem(_ID(), QTObject.ID(data), data.getSize(), udType, index);
        StdQTException.checkError(i);
        return data;
    }

    /**
     * This method allows your application to set an item in a user data list.
     * @see "QuickTime::SetUserDataItem()"
     * @param data Specifies the pointer to the data item to be set in a user data list.
     * @param udType Specifies the type value assigned to the new item.
     * @param index Specifies the item's index value.
     * @throws quicktime.std.StdQTException
     */
    public void setDataItem(QTPointerRef data, int udType, int index) throws StdQTException {
        int i = SetUserDataItem(_ID(), QTObject.ID(data), data.getSize(), udType, index);
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to place language-tagged text into an item in a user data list.
     * @see "QuickTime::AddUserDataText()"
     * @param data Specifies the handle to the data to be added to the user data list.
     * @param udType Specifies the type that is to be assigned to the new item.
     * @param index Specifies the item to which the text is to be added.
     * @param itlRegionTag Specifies the region code of the text to be added.
     * @throws quicktime.std.StdQTException
     */
    public void addText(QTHandleRef data, int udType, int index, int itlRegionTag) throws StdQTException {
        int i = AddUserDataText(_ID(), QTObject.ID(data), udType, index, (short) itlRegionTag);
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to place language-tagged text into an item in a user data list.
     * @see "QuickTime::AddUserDataText()"
     * @param udType Specifies the type that is to be assigned to the new item.
     * @param index Specifies the item to which the text is to be added.
     * @param itlRegionTag Specifies the region code of the text to be added.
     * @throws quicktime.std.StdQTException
     */
    public void addText(String text, int udType, int index, int itlRegionTag) throws StdQTException {
        int i = NewHandle(text.length());
        byte[] arrayOfByte = new byte[text.length()];
        text.getBytes(0, text.length(), arrayOfByte, 0);
        HLock(i);
        int j = getIntFromPointer(i, 0);
        copyArrayToPointer(arrayOfByte, 0, j, 0, text.length());
        int k = AddUserDataText(_ID(), i, udType, index, (short) itlRegionTag);
        DisposeHandle(i);
        StdQTException.checkError(k);
    }

    /**
     * @deprecated
     * <I>since QTJava 4, see getTextAsString</I>
     * This method allows your application to retrieve language-tagged text from an item in a user data list.
     * @see "QuickTime::GetUserDataText()"
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @param itlRegionTag Specifies the language code of the text to be retrieved.
     * @return a QTHandle object that received the data.
     * @throws quicktime.QTException
     */
    public QTHandle getText(int udType, int index, short itlRegionTag) throws QTException {
        QTHandle h = new QTHandle();
        int i = GetUserDataText(_ID(), QTObject.ID(h), udType, index, itlRegionTag);
        StdQTException.checkError(i);
        return h;
    }

    /**
     * This method allows your application to retrieve language-tagged text from an item in a user data list.
     * @see "QuickTime::GetUserDataText()"
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @param itlRegionTag Specifies the language code of the text to be retrieved.
     * @return a QTHandle object that received the data.
     * @throws quicktime.QTException
     */
    public String getTextAsString(int udType, int index, int itlRegionTag) throws QTException {
        int i = NewHandle(4);
        int j = GetUserDataText(_ID(), i, udType, index, (short) itlRegionTag);
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
     * @see "QuickTime::RemoveUserDataText()"
     * @param udType Specifies the item's type value.
     * @param index Specifies the item's index value.
     * @param itlRegionTag Specifies the language code of the text to be removed.
     * @throws quicktime.std.StdQTException
     */
    public void removeText(int udType, int index, int itlRegionTag) throws StdQTException {
        int i = RemoveUserDataText(_ID(), udType, index, (short) itlRegionTag);
        StdQTException.checkError(i);
    }

    /**
     * Copies the user data to the destination user data object
     * @see "QuickTime::CopyUserData()"
     * @param destUserData destination user data object
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @throws quicktime.std.StdQTException
     */
    public void copyFromUserData(UserData destUserData, boolean replace) throws StdQTException {
        StdQTException.checkError(CopyUserData(_ID(), QTObject.ID(destUserData), replace ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified user data object
     * @see "QuickTime::CopyUserData()"
     * @param srcUserData source user data object
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @throws quicktime.std.StdQTException
     */
    public void copyToUserData(UserData srcUserData, boolean replace) throws StdQTException {
        StdQTException.checkError(CopyUserData(QTObject.ID(srcUserData), _ID(), replace ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified user data object into the destination user data object
     * @see "QuickTime::CopyUserData()"
     * @param srcUserData source user data object
     * @param destUserData destination user data object
     * @param copyRule constant for merge or replace
     *     use StdQTConstants5_1.kQTCopyUserDataReplace or StdQTConstants5_1.kQTCopyUserDataMerge
     * @throws quicktime.std.StdQTException
     */
    public static void copyUserData(UserData srcUserData, UserData destUserData, int copyRule) throws StdQTException {
        StdQTException.checkError(CopyUserData(QTObject.ID(srcUserData), QTObject.ID(destUserData), copyRule));
    }

    private static void copyArrayToPointer(byte[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static int NewHandle(int size) {
        return MemoryManager.NewHandle(size);
    }

    private static int GetHandleSize(int h) {
        return MemoryManager.GetHandleSize(h);
    }

    private static void DisposeHandle(int h) {
        MemoryManager.DisposeHandle(h);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static void HLock(int h) {
        MemoryManager.HLock(h);
    }

    private static int TCGetSourceRef(int mh, int tcdH, int[] srefH) {
        return TimeCodeManager.TCGetSourceRef(mh, tcdH, srefH);
    }

    private static int GraphicsExportGetMetaData(int ge, int[] userData) {
        return GraphicsExportManager.GraphicsExportGetMetaData(ge, userData);
    }

    private static int GetMovieUserData(int movie) {
        return MovieToolbox.GetMovieUserData(movie);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int SGGetSettings(int sg, int[] userData, int flags) {
        return SequenceGrabberManager.SGGetSettings(sg, userData, flags);
    }

    private static int SGGetChannelSettings(int sg, int ref, int[] userData, int flags) {
        return SequenceGrabberManager.SGGetChannelSettings(sg, ref, userData, flags);
    }

    private static int GetTrackUserData(int track) {
        return MovieToolbox.GetTrackUserData(track);
    }

    private static int GetMediaUserData(int media) {
        return MovieToolbox.GetMediaUserData(media);
    }

    private static short NewUserData(int[] userData) {
        return MovieToolbox.NewUserData(userData);
    }

    private static short NewUserDataFromHandle(int h, int[] userData) {
        return MovieToolbox.NewUserDataFromHandle(h, userData);
    }

    private static short PutUserDataIntoHandle(int userData, int h) {
        return MovieToolbox.PutUserDataIntoHandle(userData, h);
    }

    private static short GetUserData(int userData, int h, int type, int index) {
        return MovieToolbox.GetUserData(userData, h, type, index);
    }

    private static short AddUserData(int userData, int h, int type) {
        return MovieToolbox.AddUserData(userData, h, type);
    }

    private static short RemoveUserData(int userData, int type, int index) {
        return MovieToolbox.RemoveUserData(userData, type, index);
    }

    private static short CountUserDataType(int userData, int type) {
        return MovieToolbox.CountUserDataType(userData, type);
    }

    private static int GetNextUserDataType(int userData, int type) {
        return MovieToolbox.GetNextUserDataType(userData, type);
    }

    private static short GetUserDataItem(int userData, int data, int size, int type, int index) {
        return MovieToolbox.GetUserDataItem(userData, data, size, type, index);
    }

    private static short SetUserDataItem(int userData, int data, int size, int type, int index) {
        return MovieToolbox.SetUserDataItem(userData, data, size, type, index);
    }

    private static short AddUserDataText(int userData, int h, int type, int index, short language) {
        return MovieToolbox.AddUserDataText(userData, h, type, index, language);
    }

    private static short GetUserDataText(int userData, int h, int type, int index, short language) {
        return MovieToolbox.GetUserDataText(userData, h, type, index, language);
    }

    private static short RemoveUserDataText(int userData, int type, int index, short language) {
        return MovieToolbox.RemoveUserDataText(userData, type, index, language);
    }

    private static int MediaSetGraphicsMode(int mh, int mode, byte[] opColor) {
        return MediaHandlerManager.MediaSetGraphicsMode(mh, mode, opColor);
    }

    private static int MediaGetGraphicsMode(int mh, int[] mode, byte[] opColor) {
        return MediaHandlerManager.MediaGetGraphicsMode(mh, mode, opColor);
    }

    private static int MediaGetTrackOpaque(int mh, byte[] opaque) {
        return MediaHandlerManager.MediaGetTrackOpaque(mh, opaque);
    }

    private static short CopyUserData(int src, int dst, int copyRule) {
        return MovieToolbox.CopyUserData(src, dst, copyRule);
    }
}
