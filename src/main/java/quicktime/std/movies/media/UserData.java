
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
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

    public static UserData fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsExportGetMetaData(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return new UserData(arrayOfInt[0], paramGraphicsExporter);
    }

    public static UserData fromMovie(Movie paramMovie) throws StdQTException {
        int i = GetMovieUserData(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return new UserData(i, paramMovie);
    }

    public static UserData fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SGGetSettings(QTObject.ID(paramSequenceGrabber), arrayOfInt, 0);

        StdQTException.checkError(i);
        return new UserData(arrayOfInt[0], null);
    }

    public static UserData fromSGChannel(SGChannel paramSGChannel) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SGGetChannelSettings(QTObject.ID(paramSGChannel.getSequenceGrabber()),
                                     QTObject.ID(paramSGChannel),
                                     arrayOfInt,
                                     0);

        StdQTException.checkError(i);
        return new UserData(arrayOfInt[0], null);
    }

    public static UserData fromTrack(Track paramTrack) throws StdQTException {
        int i = GetTrackUserData(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new UserData(i, paramTrack);
    }

    public static UserData fromMedia(Media paramMedia) throws StdQTException {
        int i = GetMediaUserData(QTObject.ID(paramMedia));
        StdQTException.checkError(GetMoviesError());
        return new UserData(i, paramMedia);
    }

    public static UserData fromTimeCoderSource(TimeCoder paramTimeCoder,
                                               TimeCodeDescription paramTimeCodeDescription) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException
                .checkError(TCGetSourceRef(QTObject.ID(paramTimeCoder), QTObject.ID(paramTimeCodeDescription), arrayOfInt));

        return new UserData(arrayOfInt[0], null);
    }

    public UserData() throws StdQTException {
        this(allocate(), null);
    }

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

    public QTHandle putIntoHandle() throws QTException {
        QTHandle localQTHandle = new QTHandle();
        int i = PutUserDataIntoHandle(_ID(), QTObject.ID(localQTHandle));
        StdQTException.checkError(i);
        return localQTHandle;
    }

    public QTHandle getData(int paramInt1, int paramInt2) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        int i = GetUserData(_ID(), QTObject.ID(localQTHandle), paramInt1, paramInt2);
        StdQTException.checkError(i);
        return localQTHandle;
    }

    public void addData(QTHandleRef paramQTHandleRef, int paramInt) throws StdQTException {
        int i = AddUserData(_ID(), QTObject.ID(paramQTHandleRef), paramInt);
        StdQTException.checkError(i);
    }

    public void removeData(int paramInt1, int paramInt2) throws StdQTException {
        int i = RemoveUserData(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public int countType(int paramInt) throws StdQTException {
        int i = CountUserDataType(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public int getNextType(int paramInt) throws StdQTException {
        int i = GetNextUserDataType(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public QTPointer getDataItem(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        QTPointer localQTPointer = new QTPointer(paramInt1, true);
        int i = GetUserDataItem(_ID(), QTObject.ID(localQTPointer), localQTPointer.getSize(), paramInt2, paramInt3);

        StdQTException.checkError(i);
        return localQTPointer;
    }

    public void setDataItem(QTPointerRef paramQTPointerRef, int paramInt1, int paramInt2) throws StdQTException {
        int i = SetUserDataItem(_ID(), QTObject.ID(paramQTPointerRef), paramQTPointerRef.getSize(), paramInt1, paramInt2);

        StdQTException.checkError(i);
    }

    public void addText(QTHandleRef paramQTHandleRef, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = AddUserDataText(_ID(), QTObject.ID(paramQTHandleRef), paramInt1, paramInt2, (short) paramInt3);

        StdQTException.checkError(i);
    }

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
     * @deprecated
     */
    public QTHandle getText(int paramInt1, int paramInt2, short paramShort) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        int i = GetUserDataText(_ID(), QTObject.ID(localQTHandle), paramInt1, paramInt2, paramShort);

        StdQTException.checkError(i);
        return localQTHandle;
    }

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

    public void removeText(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = RemoveUserDataText(_ID(), paramInt1, paramInt2, (short) paramInt3);

        StdQTException.checkError(i);
    }

    public void copyFromUserData(UserData paramUserData, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(CopyUserData(_ID(), QTObject.ID(paramUserData), paramBoolean == true ? 1919970403 : 1835361639));
    }

    public void copyToUserData(UserData paramUserData, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(CopyUserData(QTObject.ID(paramUserData), _ID(), paramBoolean == true ? 1919970403 : 1835361639));
    }

    public static void copyUserData(UserData paramUserData1, UserData paramUserData2, int paramInt) throws StdQTException {
        StdQTException.checkError(CopyUserData(QTObject.ID(paramUserData1), QTObject.ID(paramUserData2), paramInt));
    }

    private static native void copyArrayToPointer(byte[] paramArrayOfByte,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native int NewHandle(int paramInt);

    private static native int GetHandleSize(int paramInt);

    private static native void DisposeHandle(int paramInt);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void HLock(int paramInt);

    private static native int TCGetSourceRef(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int GraphicsExportGetMetaData(int paramInt, int[] paramArrayOfInt);

    private static native int GetMovieUserData(int paramInt);

    private static native short GetMoviesError();

    private static native int SGGetSettings(int paramInt1, int[] paramArrayOfInt, int paramInt2);

    private static native int SGGetChannelSettings(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);

    private static native int GetTrackUserData(int paramInt);

    private static native int GetMediaUserData(int paramInt);

    private static native short NewUserData(int[] paramArrayOfInt);

    private static native short NewUserDataFromHandle(int paramInt, int[] paramArrayOfInt);

    private static native short PutUserDataIntoHandle(int paramInt1, int paramInt2);

    private static native short GetUserData(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short AddUserData(int paramInt1, int paramInt2, int paramInt3);

    private static native short RemoveUserData(int paramInt1, int paramInt2, int paramInt3);

    private static native short CountUserDataType(int paramInt1, int paramInt2);

    private static native int GetNextUserDataType(int paramInt1, int paramInt2);

    private static native short GetUserDataItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short SetUserDataItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short AddUserDataText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort);

    private static native short GetUserDataText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort);

    private static native short RemoveUserDataText(int paramInt1, int paramInt2, int paramInt3, short paramShort);

    private static native int MediaSetGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MediaGetGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte);

    private static native int MediaGetTrackOpaque(int paramInt, byte[] paramArrayOfByte);

    private static native short CopyUserData(int paramInt1, int paramInt2, int paramInt3);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/UserData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
