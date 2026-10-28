package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.GraphicsImportManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieControllerManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.StreamingManager;
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

/**
 * The Region represents an arbitary area or set of areas on the drawing coordinate plane.
 * Methods are implemented for a corresponding QuickDraw structure used by QuickTime.
 * Refer to Region in QuickTime documentation.
 */
public final class Region extends QTHandleRef implements QuickTimeLib, QTStreamingLib, Cloneable {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(Region.class));
            }
        }.establish();
    }

    private static final boolean isMacOS = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4));

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    private QDGraphics recordingGraphics;

    /**
     * Determines the current clipping region.
     * @see "QuickTime::GraphicsImportGetClip"
     * @param gi the GraphicsImporter
     * @return The current clip region or null, if not possible
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsImporter#getClip()
     */
    public static Region fromGraphicsImporter(GraphicsImporter gi) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsImportGetClip(QTObject.ID(gi), arrayOfInt);
        QTException.checkError(i);
        if (arrayOfInt[0] == 0)
            return null;
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    /**
     * Determines the default clipping region.
     * @see "QuickTime::GraphicsImportGetDefaultClip"
     * @param gi the GraphicsImporter
     * @return The default clip region or null, if not possible
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsImporter#getClip()
     */
    public static Region fromGraphicsImporterDefault(GraphicsImporter gi) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetDefaultClip(QTObject.ID(gi), arrayOfInt);
        QTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    /**
     * This method allows your application to determine a movie's current display clipping region.
     * @see "QuickTime::GetMovieDisplayClipRgn()"
     * @param m the Movie
     * @return a Region object.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getDisplayClipRgn()
     */
    public static Region fromMovieDisplayClip(Movie m) throws QTException {
        int i = GetMovieDisplayClipRgn(QTObject.ID(m));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a movie's display boundary region.
     * The display boundary region encloses all of a movie's enabled tracks
     * after the track matrix, track clip, movie matrix, and movie clip have
     * been applied to all of the movie's tracks. This region is in the display
     * coordinate system of the movie's graphics world.
     * @see "QuickTime::GetMovieDisplayBoundsRgn()"
     * @param m the Movie
     * @return a Region object.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getDisplayBoundsRgn()
     */
    public static Region fromMovieDisplayBounds(Movie m) throws QTException {
        int i = GetMovieDisplayBoundsRgn(QTObject.ID(m));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a movie's clipping region.
     * @see "QuickTime::GetMovieClipRgn()"
     * @param m the Movie
     * @return a Region object.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getMovieClipRgn
     */
    public static Region fromMovieClip(Movie m) throws QTException {
        int i = GetMovieClipRgn(QTObject.ID(m));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a Presentation's clipping region.
     * @see "QuickTime::QTSPresGetClip()"
     * @param p the Presentation
     * @param s the Stream
     * @return a Region object.
     * @throws quicktime.QTException
     * @see quicktime.streaming.Presentation#getClip(quicktime.streaming.Stream)
     */
    public static Region fromPresentationClip(Presentation p, Stream s) throws QTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(QTSPresGetClip(QTObject.ID(p), QTObject.ID(s), arrayOfInt));
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    /**
     * This method allows your application to determine a movie's boundary region.
     * This region is in the movie's display coordinate system.
     * The movie boundary region encloses all of a movie's tracks after the union of
     * the track clip and the track matrix has been applied to all the movie's
     * tracks (but not to the movie itself).
     * @see "QuickTime::GetMovieBoundsRgn()"
     * @param m the Movie
     * @return a Region object specifying the movie boundary region.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getBoundsRgn()
     */
    public static Region fromMovieBounds(Movie m) throws QTException {
        int i = GetMovieBoundsRgn(QTObject.ID(m));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a movie's display boundary region during a specified segment.
     * @see "QuickTime::GetMovieSegmentDisplayBoundsRgn()"
     * @param m the Movie
     * @param time Specifies the starting time of the movie segment to consider.
     * @param duration Specifies the length of the segment to consider.
     * @return a Region object.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getSegmentDisplayBoundsRgn(int, int)
     */
    public static Region fromMovieSegment(Movie m, int time, int duration) throws QTException {
        int i = GetMovieSegmentDisplayBoundsRgn(QTObject.ID(m), time, duration);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method returns the actual region occupied by the controller and its movie,
     * if the movie is attached to the controller.
     * @see "QuickTime::MCGetControllerBoundsRgn()"
     * @param mc the MovieController
     * @return a Region object specifying the boundary region for this controller.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.MovieController#getBoundsRgn()
     */
    public static Region fromMovieControllerBounds(MovieController mc) throws QTException {
        int i = MCGetControllerBoundsRgn(QTObject.ID(mc));
        StdQTException.checkError(GetMoviesError());
        return new Region(i);
    }

    /**
     * This method  allows your application to determine the window region that is
     * actually in use by a controller and its movie.
     * @see "QuickTime::MCGetWindowRgn()"
     * @param mc the MovieController
     * @param window Specifies the window. This may NOT be null.
     * @return a Region object specifying the window region.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.MovieController#getWindowRgn(quicktime.qd.QDGraphics)
     */
    public static Region fromMovieControllerWindow(MovieController mc, QDGraphics window) throws QTException {
        if (window == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(window), 0);
            i = MCGetWindowRgn(QTObject.ID(mc), QTObject.ID(window));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());
        return new Region(i);
    }

    /**
     * This method allows you to obtain information describing a movie controller's clipping regions.
     * @see "QuickTime::MCGetClip()"
     * @param mc the MovieController
     * @return the clipping region of the entire movie controller
     * @throws quicktime.QTException
     * @see quicktime.std.movies.MovieController#getClip()
     */
    public static Region fromMovieControllerClip(MovieController mc) throws QTException {
        synchronized (QTNative.globalsLock) {
            int[] arrayOfInt = new int[1];
            StdQTException.checkError(MCGetClip(QTObject.ID(mc), arrayOfInt, null));
            return new Region(arrayOfInt[0]);
        }
    }

    /**
     * This method allows you to display a controller's badge.
     * @see "QuickTime::MCDrawBadge()"
     * @param mc the MovieController
     * @param movieRgn Specifies the boundary region of the controller's movie.
     * @return A Region that gives information about the location of the badge.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.MovieController#drawBadge(quicktime.qd.Region, boolean)
     */
    public static Region fromMovieControllerBadge(MovieController paramMovieController, Region paramRegion, boolean paramBoolean) throws QTException {
        int[] arrayOfInt = null;
        if (paramBoolean) {
            arrayOfInt = new int[1];
        }
        StdQTException.checkError(MCDrawBadge(QTObject.ID(paramMovieController), QTObject.ID(paramRegion), arrayOfInt));
        return paramBoolean ? new Region(arrayOfInt[0]) : null;
    }

    /**
     * This method allows your application to determine the clipping region of a track.
     * @see "QuickTime::GetTrackClipRgn()"
     * @param t the Track
     * @return a Region object specifying the clip region for this track.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Track#getClipRgn()
     */
    public static Region fromTrackClip(Track t) throws QTException {
        int i = GetTrackClipRgn(QTObject.ID(t));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics world.
     * @see "QuickTime::GetTrackDisplayBoundsRgn()"
     * @param t the Track
     * @return a Region object specifying the track's bounds region.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Track#getDisplayBoundsRgn()
     */
    public static Region fromTrackDisplay(Track t) throws QTException {
        int i = GetTrackDisplayBoundsRgn(QTObject.ID(t));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine the region the track occupies in a movie's boundary region.
     * @see "QuickTime::GetTrackMovieBoundsRgn()"
     * @param t the Track
     * @return a Region object specifying the region the track occupies in a movie's boundary region.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Track#getMovieBoundsRgn()
     */
    public static Region fromTrackMovieBounds(Track t) throws QTException {
        int i = GetTrackMovieBoundsRgn(QTObject.ID(t));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows the media to limit the size of the track boundary rectangle.  Therefore, the region
     * returned by GetTrackBoundsRgn may not be rectangular and may be smaller than the track boundary region.
     * @see "QuickTime::GetTrackBoundsRgn()"
     * @param t the Track
     * @return a Region object specifying the region track's media uses to do drawing.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Track#getBoundsRgn()
     */
    public static Region fromTrackBounds(Track t) throws QTException {
        int i = GetTrackBoundsRgn(QTObject.ID(t));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics
     * world during a specified segment.
     * @see "QuickTime::GetTrackSegmentDisplayBoundsRgn()"
     * @param t the Track
     * @param time Specifies the starting time of the track segment to consider.
     * @param duration Specifies the length of the segment to consider. Set this parameter to 0 to
     *  consider an instant in time.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Track#getSegmentDisplayBoundsRgn(int, int)
     */
    public static Region fromTrackSegment(Track t, int time, int duration) throws QTException {
        int i = GetTrackSegmentDisplayBoundsRgn(QTObject.ID(t), time, duration);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * Allows you to retrieve a channels clipping region.
     * @see "QuickTime::SGGetChannelClip"
     * @param vc the VisualChannel
     * @return the clipping region or null if no clip.
     * @throws quicktime.QTException
     * @see quicktime.std.sg.VisualChannel#getClip()
     */
    public static Region fromVideoChannel(VisualChannel vc) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = SGGetChannelClip(QTObject.ID(vc), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    /**
     * Allocates a new empty region.
     * @see "QuickDraw::NewRgn"
     */
    public Region() throws QTException {
        super(allocate(), null, false);
    }

    private static int allocate() throws QTException {
        int i = NewRgn();
        StdQTException.checkError(MemError());
        return i;
    }

    /**
     * Allocates a new region specified by a rectangle.
     * @see "QuickDraw::RectRgn"
     * @param r the specified rectangle
     */
    public Region(QDRect r) throws QTException {
        this();
        rect(r);
    }

    /**
     * Converts a specified BitMap or PixMap to a region.
     * @see "QuickDraw::BitMapToRegion"
     * @param pMap the specified pixmap
     */
    public Region(PixMap pMap) throws QTException {
        this();
        int i = 0;
        if (!pMap.isLocked()) {
            pMap.lock();
            i = 1;
        }
        int j = getIntFromPointer(QTObject.ID(pMap), 0);
        int k = BitMapToRegion(_ID(), j);
        if (i != 0)
            pMap.unlock();
        QDException.checkError(k);
    }

    private Region(int paramInt) throws QTException {
        super(paramInt, null, false);
    }

    /**
     * Returns size of the Region.
     * @return the size of the handle
     */
    @Override
    public int getSize() {
        return super.getSize();
    }

    /**
     * Returns a copy of the bounding rectangle of the Region.
     * @return a rect
     */
    public QDRect getRgnBBox() {
        if (QTSession.isCurrentOS(4)) {
            QDRect localObject = new QDRect();
            GetRegionBounds(_ID(), localObject.getBytes());
            return localObject;
        }
        Object localObject = new byte[8];
        getBytesAt(2, 8, (byte[]) localObject, 0);
        return QDRect.fromArray((byte[]) localObject, 8);
    }

    /**
     * Returns a copy of the bounding rectangle of the Region.
     * @return a rect
     */
    public QDRect getBounds() {
        return getRgnBBox();
    }

    /**
     * Begins the recording of lines or frames in the specified QDGraphics that when closed
     * will define a Region.
     * @see "QuickDraw::OpenRgn"
     * @param graphics the QDGraphics object to record.
     * @throws quicktime.qd.QDException
     */
    public void open(QDGraphics graphics) throws QDException {
        if (graphics == null)
            throw new QDException("Graphics must be specified for Open Region");
        if (this.recordingGraphics != null)
            close();
        this.recordingGraphics = graphics;
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

    /**
     * Completes the collection of lines or frames that define the Region.
     * @see "QuickDraw::CloseRgn"
     */
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

    /**
     * Resets the mathematical structure to the specified rectangle.
     * Destroys the previous mathematical structure of the implicit region.
     * @see "QuickDraw::RectRgn"
     * @param r the specified rectangle
     */
    public void rect(QDRect r) {
        RectRgn(_ID(), r.getRect());
    }

    /**
     * Copies the mathematical structure of the implicit region into a newly created region.
     * @see "QuickDraw::CopyRgn"
     * @see "QuickDraw::MacCopyRgn"
     * @return A new region copy.
     * @throws quicktime.QTException
     */
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

    /**
     * Copies the mathematical structure of the implicit region into a newly created region.
     * @see "QuickDraw::CopyRgn"
     * @return A new region copy.
     */
    @Override
    public Object clone() {
        try {
            return copy();
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    /**
     * Resets the mathematical structure to the rectangle (0,0,0,0).
     * Destroys the previous mathematical structure of the implicit region.
     * @see "QuickDraw::SetEmptyRgn"
     */
    public void setEmpty() {
        SetEmptyRgn(_ID());
    }

    /**
     * Resets the mathematical structure to the specified coordinates and sizes.
     * Destroys the previous mathematical structure of the implicit region.
     * May reset the structure to the rectangle(0,0,0,0), if bad parameters.
     * @see "QuickDraw::SetRectRgn"
     * @see "QuickDraw::MacSetRectRgn"
     * @param x the specified leftmost position.
     * @param y the specified topmost position.
     * @param width the specified width.
     * @param height the specified height.
     */
    public void setRect(int x, int y, int width, int height) {
        int i = height + width;
        int j = x + y;
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            SetRectRgn(_ID(), (short) x, (short) y, (short) j, (short) i);
        } else {
            MacSetRectRgn(_ID(), (short) x, (short) y, (short) j, (short) i);
        }
    }

    /**
     * Moves the region horizontally and vertically by adding the respective parameters to all of the region.
     * @see "QuickDraw::OffsetRgn"
     * @see "QuickDraw::MacOffsetRgn"
     * @param dh the specified horizontal offset.
     * @param dv the specified vertical offset.
     */
    public void offset(int dh, int dv) {
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            OffsetRgn(_ID(), (short) dh, (short) dv);
        } else {
            MacOffsetRgn(_ID(), (short) dh, (short) dv);
        }
    }

    /**
     * Moves all points on the region boundary horizontally and vertically by adding the respective parameters to those points.
     * @see "QuickDraw::InsetRgn"
     * @param dh the specified horizontal inset.
     * @param dv the specified vertical inset.
     */
    public void inset(int dh, int dv) {
        InsetRgn(_ID(), (short) dh, (short) dv);
    }

    /**
     * Calculates the intersection of two regions and creates a new region representing that structure.
     * May return an empty region if no intersection or one of the regions is empty.
     * @see "QuickDraw::SectRgn"
     * @param regionB the specified intersecting region.
     * @return The region of intersection.
     * @throws quicktime.QTException
     */
    public Region sect(Region regionB) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        SectRgn(_ID(), QTObject.ID(regionB), i);
        return new Region(i);
    }

    /**
     * Calculates the intersection of this region and the incoming QDRect
     * and creates a new region representing that structure.
     * May return an empty region if no intersection or one of the regions is empty.
     * @see "QuickDraw::SectRgn"
     * @param r the specified intersecting rect.
     * @return The region of intersection.
     * @throws quicktime.QTException
     */
    public Region sect(QDRect r) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        RectRgn(i, r.getRect());
        int j = NewRgn();
        if (j == 0) {
            DisposeRgn(i);
            QDException.checkError(-108);
        }
        SectRgn(_ID(), i, j);
        DisposeRgn(i);
        return new Region(j);
    }

    /**
     * Calculates the union of two regions and creates a new region representing that structure.
     * May return an empty region if both regions are empty.
     * @see "QuickDraw::UnionRgn"
     * @see "QuickDraw::MacUnionRgn"
     * @param regionB the specified region to union with.
     * @return The region of union.
     * @throws quicktime.QTException
     */
    public Region union(Region regionB) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            UnionRgn(_ID(), QTObject.ID(regionB), i);
        } else
            MacUnionRgn(_ID(), QTObject.ID(regionB), i);
        return new Region(i);
    }

    /**
     * Calculates the union of this region and the incoming rect,
     * and creates a new region representing that structure.
     * May return an empty region if both regions are empty.
     * @see "QuickDraw::UnionRgn"
     * @param rect the specified rect to union with.
     * @return The region of union.
     * @throws quicktime.QTException
     */
    public Region union(QDRect rect) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        RectRgn(i, rect.getRect());
        int j = NewRgn();
        if (j == 0)
            QDException.checkError(-108);
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            UnionRgn(_ID(), i, j);
        } else
            MacUnionRgn(_ID(), i, j);
        return new Region(j);
    }

    /**
     * Calculates the subtracts the specified explicit region from the implicit region and creates a new region representing that structure.
     * May return an empty region if the implicit region is empty.
     * @see "QuickDraw::DiffRgn"
     * @param regionB the specified region to find difference with.
     * @return The region of difference.
     * @throws quicktime.QTException
     */
    public Region diff(Region regionB) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        DiffRgn(_ID(), QTObject.ID(regionB), i);
        return new Region(i);
    }

    /**
     * Calculates the difference between the union and intersection of two regions and creates a new region representing that structure.
     * May return an empty region if both regions are coincident.
     * @see "QuickDraw::XorRgn"
     * @see "QuickDraw::MacXorRgn"
     * @param regionB the specified region to xor with.
     * @return The region of exclusively or.
     * @throws quicktime.QTException
     */
    public Region xor(Region regionB) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            XorRgn(_ID(), QTObject.ID(regionB), i);
        } else
            MacXorRgn(_ID(), QTObject.ID(regionB), i);
        return new Region(i);
    }

    /**
     * Checks whether the specified rectangle intersects the region.
     * @see "QuickDraw::RectInRgn"
     * @param srcRect the specified source rectangle
     * @return True if at least one bit of the rectangle is contained by the region, false otherwise.
     */
    public boolean rectIn(QDRect srcRect) {
        return RectInRgn(srcRect.getRect(), _ID()) != 0;
    }

    /**
     * Compares two regions for sizes, shapes, and locations.
     * @see "QuickDraw::EqualRgn"
     * @see "QuickDraw::MacEqualRgn"
     * @return true if both regions' sizes, shapes, and locations are identical, false otherwise.
     */
    public boolean equalRgn(Region paramRegion) {
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            return EqualRgn(_ID(), QTObject.ID(paramRegion)) != 0;
        }
        return MacEqualRgn(_ID(), QTObject.ID(paramRegion)) != 0;
    }

    /**
     * Determines whether a region is empty or not.
     * @see "QuickDraw::EmptyRgn"
     * @return True if region is empty, false otherwise.
     */
    public boolean empty() {
        return EmptyRgn(_ID()) != 0;
    }

    /**
     * Takes the implicit region with one rectangle, maps and scales it to another rectangle.
     * @see "QuickDraw::MapRgn"
     * @param srcRect the specified source rectangle
     * @param dstRect the specified destination rectangle
     */
    public void map(QDRect srcRect, QDRect dstRect) {
        MapRgn(_ID(), srcRect.getRect(), dstRect.getRect());
    }

    /**
     * Checks whether the pixel below and to the right of the point is within the region.
     * @see "QuickDraw::PtInRgn"
     * @param pt the specified point.
     * @return True if pt is contained by the region, false otherwise.
     */
    public boolean pointIn(QDPoint pt) {
        return PtInRgn(pt.getPoint(), _ID()) != 0;
    }

    /**
     * Print information about this object.
     * @return a string representing this Region object
     */
    public String toString() {
        return super.toString() + "[rgnBBox=" + getRgnBBox() + "]";
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void OpenRgn() {
        QuickDrawManager.OpenRgn();
    }

    private static void CloseRgn(int paramInt) {
        QuickDrawManager.CloseRgn(paramInt);
    }

    private static int NewRgn() {
        return QuickDrawManager.NewRgn();
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static void SectRgn(int paramInt1, int paramInt2, int paramInt3) {
        QuickDrawManager.SectRgn(paramInt1, paramInt2, paramInt3);
    }

    private static int GraphicsImportGetClip(int paramInt, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetClip(paramInt, paramArrayOfInt);
    }

    private static int GraphicsImportGetDefaultClip(int paramInt, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetDefaultClip(paramInt, paramArrayOfInt);
    }

    private static int GetMovieDisplayClipRgn(int paramInt) {
        return MovieToolbox.GetMovieDisplayClipRgn(paramInt);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int GetMovieDisplayBoundsRgn(int paramInt) {
        return MovieToolbox.GetMovieDisplayBoundsRgn(paramInt);
    }

    private static int GetMovieClipRgn(int paramInt) {
        return MovieToolbox.GetMovieClipRgn(paramInt);
    }

    private static int GetMovieBoundsRgn(int paramInt) {
        return MovieToolbox.GetMovieBoundsRgn(paramInt);
    }

    private static int GetMovieSegmentDisplayBoundsRgn(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.GetMovieSegmentDisplayBoundsRgn(paramInt1, paramInt2, paramInt3);
    }

    private static int MCGetControllerBoundsRgn(int paramInt) {
        return MovieControllerManager.MCGetControllerBoundsRgn(paramInt);
    }

    private static int MCGetWindowRgn(int paramInt1, int paramInt2) {
        return MovieControllerManager.MCGetWindowRgn(paramInt1, paramInt2);
    }

    private static int MCGetClip(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return MovieControllerManager.MCGetClip(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int MCDrawBadge(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MovieControllerManager.MCDrawBadge(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int GetTrackClipRgn(int paramInt) {
        return MovieToolbox.GetTrackClipRgn(paramInt);
    }

    private static int GetTrackDisplayBoundsRgn(int paramInt) {
        return MovieToolbox.GetTrackDisplayBoundsRgn(paramInt);
    }

    private static int GetTrackMovieBoundsRgn(int paramInt) {
        return MovieToolbox.GetTrackMovieBoundsRgn(paramInt);
    }

    private static int GetTrackBoundsRgn(int paramInt) {
        return MovieToolbox.GetTrackBoundsRgn(paramInt);
    }

    private static int GetTrackSegmentDisplayBoundsRgn(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.GetTrackSegmentDisplayBoundsRgn(paramInt1, paramInt2, paramInt3);
    }

    private static int SGGetChannelClip(int paramInt, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetChannelClip(paramInt, paramArrayOfInt);
    }

    private static short BitMapToRegion(int paramInt1, int paramInt2) {
        return QuickDrawManager.BitMapToRegion(paramInt1, paramInt2);
    }

    private static void RectRgn(int paramInt, byte[] paramArrayOfByte) {
        QuickDrawManager.RectRgn(paramInt, paramArrayOfByte);
    }

    private static void MacCopyRgn(int paramInt1, int paramInt2) {
        QuickDrawManager.MacCopyRgn(paramInt1, paramInt2);
    }

    private static void SetEmptyRgn(int paramInt) {
        QuickDrawManager.SetEmptyRgn(paramInt);
    }

    private static void MacSetRectRgn(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
        QuickDrawManager.MacSetRectRgn(paramInt, paramShort1, paramShort2, paramShort3, paramShort4);
    }

    private static void MacOffsetRgn(int paramInt, short paramShort1, short paramShort2) {
        QuickDrawManager.MacOffsetRgn(paramInt, paramShort1, paramShort2);
    }

    private static void InsetRgn(int paramInt, short paramShort1, short paramShort2) {
        QuickDrawManager.InsetRgn(paramInt, paramShort1, paramShort2);
    }

    private static void DisposeRgn(int paramInt) {
        QuickDrawManager.DisposeRgn(paramInt);
    }

    private static void MacUnionRgn(int paramInt1, int paramInt2, int paramInt3) {
        QuickDrawManager.MacUnionRgn(paramInt1, paramInt2, paramInt3);
    }

    private static void DiffRgn(int paramInt1, int paramInt2, int paramInt3) {
        QuickDrawManager.DiffRgn(paramInt1, paramInt2, paramInt3);
    }

    private static void MacXorRgn(int paramInt1, int paramInt2, int paramInt3) {
        QuickDrawManager.MacXorRgn(paramInt1, paramInt2, paramInt3);
    }

    private static byte RectInRgn(byte[] paramArrayOfByte, int paramInt) {
        return QuickDrawManager.RectInRgn(paramArrayOfByte, paramInt);
    }

    private static byte MacEqualRgn(int paramInt1, int paramInt2) {
        return QuickDrawManager.MacEqualRgn(paramInt1, paramInt2);
    }

    private static byte EmptyRgn(int paramInt) {
        return QuickDrawManager.EmptyRgn(paramInt);
    }

    private static void MapRgn(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        QuickDrawManager.MapRgn(paramInt, paramArrayOfByte1, paramArrayOfByte2);
    }

    private static byte PtInRgn(int paramInt1, int paramInt2) {
        return QuickDrawManager.PtInRgn(paramInt1, paramInt2);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static void PaintRgn(int paramInt) {
        QuickDrawManager.PaintRgn(paramInt);
    }

    private static void CopyRgn(int paramInt1, int paramInt2) {
        QuickDrawManager.CopyRgn(paramInt1, paramInt2);
    }

    private static void SetRectRgn(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
        QuickDrawManager.SetRectRgn(paramInt, paramShort1, paramShort2, paramShort3, paramShort4);
    }

    private static void OffsetRgn(int paramInt, short paramShort1, short paramShort2) {
        QuickDrawManager.OffsetRgn(paramInt, paramShort1, paramShort2);
    }

    private static void UnionRgn(int paramInt1, int paramInt2, int paramInt3) {
        QuickDrawManager.UnionRgn(paramInt1, paramInt2, paramInt3);
    }

    private static void XorRgn(int paramInt1, int paramInt2, int paramInt3) {
        QuickDrawManager.XorRgn(paramInt1, paramInt2, paramInt3);
    }

    private static byte EqualRgn(int paramInt1, int paramInt2) {
        return QuickDrawManager.EqualRgn(paramInt1, paramInt2);
    }

    private static byte GetRegionBounds(int paramInt, byte[] paramArrayOfByte) {
        return QuickDrawManager.GetRegionBounds(paramInt, paramArrayOfByte);
    }

    private static byte IsRegionRectangular(int paramInt) {
        return QuickDrawManager.IsRegionRectangular(paramInt);
    }

    private static short QTSPresGetClip(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return StreamingManager.QTSPresGetClip(paramInt1, paramInt2, paramArrayOfInt);
    }
}
