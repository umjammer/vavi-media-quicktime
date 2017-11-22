
package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.Track;
import quicktime.std.sg.VisualChannel;
import quicktime.streaming.Presentation;
import quicktime.streaming.Stream;
import quicktime.util.QTHandleRef;


public final class Region extends QTHandleRef implements QuickTimeLib, QTStreamingLib, Cloneable {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Region.class);
                    }
                });
            }
        }.establish();
    }

    private static final boolean isMacOS = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4));

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    private QDGraphics recordingGraphics;

    public static Region fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsImportGetClip(QTObject.ID(paramGraphicsImporter), arrayOfInt);
        QTException.checkError(i);
        if (arrayOfInt[0] == 0)
            return null;
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    public static Region fromGraphicsImporterDefault(GraphicsImporter paramGraphicsImporter) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsImportGetDefaultClip(QTObject.ID(paramGraphicsImporter), arrayOfInt);
        QTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    public static Region fromMovieDisplayClip(Movie paramMovie) throws QTException {
        int i = GetMovieDisplayClipRgn(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromMovieDisplayBounds(Movie paramMovie) throws QTException {
        int i = GetMovieDisplayBoundsRgn(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromMovieClip(Movie paramMovie) throws QTException {
        int i = GetMovieClipRgn(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromPresentationClip(Presentation paramPresentation, Stream paramStream) throws QTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(QTSPresGetClip(QTObject.ID(paramPresentation), QTObject.ID(paramStream), arrayOfInt));

        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    public static Region fromMovieBounds(Movie paramMovie) throws QTException {
        int i = GetMovieBoundsRgn(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromMovieSegment(Movie paramMovie, int paramInt1, int paramInt2) throws QTException {
        int i = GetMovieSegmentDisplayBoundsRgn(QTObject.ID(paramMovie), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromMovieControllerBounds(MovieController paramMovieController) throws QTException {
        int i = MCGetControllerBoundsRgn(QTObject.ID(paramMovieController));
        StdQTException.checkError(GetMoviesError());
        return new Region(i);
    }

    public static Region fromMovieControllerWindow(MovieController paramMovieController,
                                                   QDGraphics paramQDGraphics) throws QTException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = MCGetWindowRgn(QTObject.ID(paramMovieController), QTObject.ID(paramQDGraphics));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());
        return new Region(i);
    }

    public static Region fromMovieControllerClip(MovieController paramMovieController) throws QTException {
        synchronized (QTNative.globalsLock) {
            int[] arrayOfInt = new int[1];
            StdQTException.checkError(MCGetClip(QTObject.ID(paramMovieController), arrayOfInt, null));

            return new Region(arrayOfInt[0]);
        }
    }

    public static Region fromMovieControllerBadge(MovieController paramMovieController,
                                                  Region paramRegion,
                                                  boolean paramBoolean) throws QTException {
        int[] arrayOfInt = null;
        if (paramBoolean) {
            arrayOfInt = new int[1];
        }
        StdQTException.checkError(MCDrawBadge(QTObject.ID(paramMovieController), QTObject.ID(paramRegion), arrayOfInt));

        return paramBoolean ? new Region(arrayOfInt[0]) : null;
    }

    public static Region fromTrackClip(Track paramTrack) throws QTException {
        int i = GetTrackClipRgn(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromTrackDisplay(Track paramTrack) throws QTException {
        int i = GetTrackDisplayBoundsRgn(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromTrackMovieBounds(Track paramTrack) throws QTException {
        int i = GetTrackMovieBoundsRgn(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromTrackBounds(Track paramTrack) throws QTException {
        int i = GetTrackBoundsRgn(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromTrackSegment(Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        int i = GetTrackSegmentDisplayBoundsRgn(QTObject.ID(paramTrack), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    public static Region fromVideoChannel(VisualChannel paramVisualChannel) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = SGGetChannelClip(QTObject.ID(paramVisualChannel), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    public Region() throws QTException {
        super(allocate(), null, false);
    }

    private static int allocate() throws QTException {
        int i = NewRgn();
        StdQTException.checkError(MemError());
        return i;
    }

    public Region(QDRect paramQDRect) throws QTException {
        this();
        rect(paramQDRect);
    }

    public Region(PixMap paramPixMap) throws QTException {
        this();
        int i = 0;
        if (!paramPixMap.isLocked()) {
            paramPixMap.lock();
            i = 1;
        }
        int j = getIntFromPointer(QTObject.ID(paramPixMap), 0);

        int k = BitMapToRegion(_ID(), j);

        if (i != 0)
            paramPixMap.unlock();
        QDException.checkError(k);
    }

    private Region(int paramInt) throws QTException {
        super(paramInt, null, false);
    }

    public int getSize() {
        return super.getSize();
    }

    public QDRect getRgnBBox() {
        if (QTSession.isCurrentOS(4)) {
            Object localObject = new QDRect();
            GetRegionBounds(_ID(), ((QDRect) localObject).getBytes());
            return (QDRect) localObject;
        }

        Object localObject = new byte[8];
        getBytesAt(2, 8, (byte[]) localObject, 0);
        return QDRect.fromArray((byte[]) localObject, 8);
    }

    public QDRect getBounds() {
        return getRgnBBox();
    }

    public void open(QDGraphics paramQDGraphics) throws QDException {
        if (paramQDGraphics == null)
            throw new QDException("Graphics must be specified for Open Region");
        if (this.recordingGraphics != null)
            close();
        this.recordingGraphics = paramQDGraphics;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(this.recordingGraphics), 0);
            OpenRgn();
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    public void close() throws QDException {
        if (this.recordingGraphics != null) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(savedPort, savedDevice);
                SetGWorld(QTObject.ID(this.recordingGraphics), 0);
                CloseRgn(_ID());
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        this.recordingGraphics = null;
    }

    public void rect(QDRect paramQDRect) {
        RectRgn(_ID(), paramQDRect.getRect());
    }

    public Region copy() throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            CopyRgn(_ID(), i);
        } else
            MacCopyRgn(_ID(), i);
        return new Region(i);
    }

    public Object clone() {
        try {
            return copy();
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    public void setEmpty() {
        SetEmptyRgn(_ID());
    }

    public void setRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = paramInt2 + paramInt4;
        int j = paramInt1 + paramInt3;
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            SetRectRgn(_ID(), (short) paramInt1, (short) paramInt2, (short) j, (short) i);
        } else {
            MacSetRectRgn(_ID(), (short) paramInt1, (short) paramInt2, (short) j, (short) i);
        }
    }

    public void offset(int paramInt1, int paramInt2) {
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            OffsetRgn(_ID(), (short) paramInt1, (short) paramInt2);
        } else {
            MacOffsetRgn(_ID(), (short) paramInt1, (short) paramInt2);
        }
    }

    public void inset(int paramInt1, int paramInt2) {
        InsetRgn(_ID(), (short) paramInt1, (short) paramInt2);
    }

    public Region sect(Region paramRegion) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        SectRgn(_ID(), QTObject.ID(paramRegion), i);
        return new Region(i);
    }

    public Region sect(QDRect paramQDRect) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        RectRgn(i, paramQDRect.getRect());
        int j = NewRgn();
        if (j == 0) {
            DisposeRgn(i);
            QDException.checkError(-108);
        }
        SectRgn(_ID(), i, j);
        DisposeRgn(i);
        return new Region(j);
    }

    public Region union(Region paramRegion) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            UnionRgn(_ID(), QTObject.ID(paramRegion), i);
        } else
            MacUnionRgn(_ID(), QTObject.ID(paramRegion), i);
        return new Region(i);
    }

    public Region union(QDRect paramQDRect) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        RectRgn(i, paramQDRect.getRect());
        int j = NewRgn();
        if (j == 0)
            QDException.checkError(-108);
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            UnionRgn(_ID(), i, j);
        } else
            MacUnionRgn(_ID(), i, j);
        return new Region(j);
    }

    public Region diff(Region paramRegion) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        DiffRgn(_ID(), QTObject.ID(paramRegion), i);
        return new Region(i);
    }

    public Region xor(Region paramRegion) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            XorRgn(_ID(), QTObject.ID(paramRegion), i);
        } else
            MacXorRgn(_ID(), QTObject.ID(paramRegion), i);
        return new Region(i);
    }

    public boolean rectIn(QDRect paramQDRect) {
        return RectInRgn(paramQDRect.getRect(), _ID()) != 0;
    }

    public boolean equalRgn(Region paramRegion) {
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            return EqualRgn(_ID(), QTObject.ID(paramRegion)) != 0;
        }
        return MacEqualRgn(_ID(), QTObject.ID(paramRegion)) != 0;
    }

    public boolean empty() {
        return EmptyRgn(_ID()) != 0;
    }

    public void map(QDRect paramQDRect1, QDRect paramQDRect2) {
        MapRgn(_ID(), paramQDRect1.getRect(), paramQDRect2.getRect());
    }

    public boolean pointIn(QDPoint paramQDPoint) {
        return PtInRgn(paramQDPoint.getPoint(), _ID()) != 0;
    }

    public String toString() {
        return super.toString() + "[rgnBBox=" + getRgnBBox() + "]";
    }

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void OpenRgn();

    private static native void CloseRgn(int paramInt);

    private static native int NewRgn();

    private static native short MemError();

    private static native void SectRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsImportGetClip(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportGetDefaultClip(int paramInt, int[] paramArrayOfInt);

    private static native int GetMovieDisplayClipRgn(int paramInt);

    private static native short GetMoviesError();

    private static native int GetMovieDisplayBoundsRgn(int paramInt);

    private static native int GetMovieClipRgn(int paramInt);

    private static native int GetMovieBoundsRgn(int paramInt);

    private static native int GetMovieSegmentDisplayBoundsRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native int MCGetControllerBoundsRgn(int paramInt);

    private static native int MCGetWindowRgn(int paramInt1, int paramInt2);

    private static native int MCGetClip(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int MCDrawBadge(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int GetTrackClipRgn(int paramInt);

    private static native int GetTrackDisplayBoundsRgn(int paramInt);

    private static native int GetTrackMovieBoundsRgn(int paramInt);

    private static native int GetTrackBoundsRgn(int paramInt);

    private static native int GetTrackSegmentDisplayBoundsRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native int SGGetChannelClip(int paramInt, int[] paramArrayOfInt);

    private static native short BitMapToRegion(int paramInt1, int paramInt2);

    private static native void RectRgn(int paramInt, byte[] paramArrayOfByte);

    private static native void MacCopyRgn(int paramInt1, int paramInt2);

    private static native void SetEmptyRgn(int paramInt);

    private static native void MacSetRectRgn(int paramInt,
                                             short paramShort1,
                                             short paramShort2,
                                             short paramShort3,
                                             short paramShort4);

    private static native void MacOffsetRgn(int paramInt, short paramShort1, short paramShort2);

    private static native void InsetRgn(int paramInt, short paramShort1, short paramShort2);

    private static native void DisposeRgn(int paramInt);

    private static native void MacUnionRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native void DiffRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native void MacXorRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native byte RectInRgn(byte[] paramArrayOfByte, int paramInt);

    private static native byte MacEqualRgn(int paramInt1, int paramInt2);

    private static native byte EmptyRgn(int paramInt);

    private static native void MapRgn(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native byte PtInRgn(int paramInt1, int paramInt2);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void PaintRgn(int paramInt);

    private static native void CopyRgn(int paramInt1, int paramInt2);

    private static native void SetRectRgn(int paramInt,
                                          short paramShort1,
                                          short paramShort2,
                                          short paramShort3,
                                          short paramShort4);

    private static native void OffsetRgn(int paramInt, short paramShort1, short paramShort2);

    private static native void UnionRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native void XorRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native byte EqualRgn(int paramInt1, int paramInt2);

    private static native byte GetRegionBounds(int paramInt, byte[] paramArrayOfByte);

    private static native byte IsRegionRectangular(int paramInt);

    private static native short QTSPresGetClip(int paramInt1, int paramInt2, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/Region
 * .class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
