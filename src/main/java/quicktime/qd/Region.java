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
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Region.class);
                    }
                });
            }
        }.establish();
    }

    private static final boolean isMacOS = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4));

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    private QDGraphics recordingGraphics;

    /**
     * Determines the current clipping region.
     * QuickTime::GraphicsImportGetClip
     * @param gi the GraphicsImporter
     * @return The current clip region or null, if not possible
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getClip()}
     */
    public static Region fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsImportGetClip(QTObject.ID(paramGraphicsImporter), arrayOfInt);
        QTException.checkError(i);
        if (arrayOfInt[0] == 0)
            return null;
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    /**
     * Determines the default clipping region.
     * QuickTime::GraphicsImportGetDefaultClip
     * @param gi the GraphicsImporter
     * @return The default clip region or null, if not possible
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getClip()}
     */
    public static Region fromGraphicsImporterDefault(GraphicsImporter paramGraphicsImporter) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetDefaultClip(QTObject.ID(paramGraphicsImporter), arrayOfInt);
        QTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    /**
     * This method allows your application to determine a movie's current display clipping region.
     * QuickTime::GetMovieDisplayClipRgn()
     * @param m the Movie
     * @return a Region object.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Movie#getDisplayClipRgn()}
     */
    public static Region fromMovieDisplayClip(Movie paramMovie) throws QTException {
        int i = GetMovieDisplayClipRgn(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a movie's display boundary region.
     * The display boundary region encloses all of a movie's enabled tracks
     * after the track matrix, track clip, movie matrix, and movie clip have
     * been applied to all of the movie's tracks. This region is in the display
     * coordinate system of the movie's graphics world.
     * QuickTime::GetMovieDisplayBoundsRgn()
     * @param m the Movie
     * @return a Region object.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Movie#getDisplayBoundsRgn()}
     */
    public static Region fromMovieDisplayBounds(Movie paramMovie) throws QTException {
        int i = GetMovieDisplayBoundsRgn(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a movie's clipping region.
     * QuickTime::GetMovieClipRgn()
     * @param m the Movie
     * @return a Region object.
     * @return {@link quicktime.QTException}
     * @see quicktime.std.movies.Movie#getMovieClipRgn
     */
    public static Region fromMovieClip(Movie paramMovie) throws QTException {
        int i = GetMovieClipRgn(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a Presentation's clipping region.
     * QuickTime::QTSPresGetClip()
     * @param p the Presentation
     * @param s the Stream
     * @return a Region object.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.streaming.Presentation#getClip(quicktime.streaming.Stream)}
     */
    public static Region fromPresentationClip(Presentation paramPresentation, Stream paramStream) throws QTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(QTSPresGetClip(QTObject.ID(paramPresentation), QTObject.ID(paramStream), arrayOfInt));
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    /**
     * This method allows your application to determine a movie's boundary region.
     * This region is in the movie's display coordinate system.
     * The movie boundary region encloses all of a movie's tracks after the union of
     * the track clip and the track matrix has been applied to all the movie's
     * tracks (but not to the movie itself).
     * QuickTime::GetMovieBoundsRgn()
     * @param m the Movie
     * @return a Region object specifying the movie boundary region.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Movie#getBoundsRgn()}
     */
    public static Region fromMovieBounds(Movie paramMovie) throws QTException {
        int i = GetMovieBoundsRgn(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a movie's display boundary region during a specified segment.
     * QuickTime::GetMovieSegmentDisplayBoundsRgn()
     * @param m the Movie
     * @param time Specifies the starting time of the movie segment to consider.
     * @param duration Specifies the length of the segment to consider.
     * @return a Region object.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Movie#getSegmentDisplayBoundsRgn(int, int)}
     */
    public static Region fromMovieSegment(Movie paramMovie, int paramInt1, int paramInt2) throws QTException {
        int i = GetMovieSegmentDisplayBoundsRgn(QTObject.ID(paramMovie), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method returns the actual region occupied by the controller and its movie,
     * if the movie is attached to the controller.
     * QuickTime::MCGetControllerBoundsRgn()
     * @param mc the MovieController
     * @return a Region object specifying the boundary region for this controller.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.MovieController#getBoundsRgn()}
     */
    public static Region fromMovieControllerBounds(MovieController paramMovieController) throws QTException {
        int i = MCGetControllerBoundsRgn(QTObject.ID(paramMovieController));
        StdQTException.checkError(GetMoviesError());
        return new Region(i);
    }

    /**
     * This method  allows your application to determine the window region that is
     * actually in use by a controller and its movie.
     * QuickTime::MCGetWindowRgn()
     * @param mc the MovieController
     * @param window Specifies the window. This may NOT be null.
     * @return a Region object specifying the window region.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.MovieController#getWindowRgn(quicktime.qd.QDGraphics)}
     */
    public static Region fromMovieControllerWindow(MovieController paramMovieController, QDGraphics paramQDGraphics) throws QTException {
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

    /**
     * This method allows you to obtain information describing a movie controller's clipping regions.
     * QuickTime::MCGetClip()
     * @param mc the MovieController
     * @return the clipping region of the entire movie controller
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.MovieController#getClip()}
     */
    public static Region fromMovieControllerClip(MovieController paramMovieController) throws QTException {
        synchronized (QTNative.globalsLock) {
            int[] arrayOfInt = new int[1];
            StdQTException.checkError(MCGetClip(QTObject.ID(paramMovieController), arrayOfInt, null));
            return new Region(arrayOfInt[0]);
        }
    }

    /**
     * This method allows you to display a controller's badge.
     * QuickTime::MCDrawBadge()
     * @param mc the MovieController
     * @param movieRgn Specifies the boundary region of the controller's movie.
     * @return A Region that gives information about the location of the badge.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.MovieController#drawBadge(quicktime.qd.Region, boolean)}
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
     * QuickTime::GetTrackClipRgn()
     * @param t the Track
     * @return a Region object specifying the clip region for this track.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Track#getClipRgn()}
     */
    public static Region fromTrackClip(Track paramTrack) throws QTException {
        int i = GetTrackClipRgn(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics world.
     * QuickTime::GetTrackDisplayBoundsRgn()
     * @param t the Track
     * @return a Region object specifying the track's bounds region.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Track#getDisplayBoundsRgn()}
     */
    public static Region fromTrackDisplay(Track paramTrack) throws QTException {
        int i = GetTrackDisplayBoundsRgn(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine the region the track occupies in a movie's boundary region.
     * QuickTime::GetTrackMovieBoundsRgn()
     * @param t the Track
     * @return a Region object specifying the region the track occupies in a movie's boundary region.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Track#getMovieBoundsRgn()}
     */
    public static Region fromTrackMovieBounds(Track paramTrack) throws QTException {
        int i = GetTrackMovieBoundsRgn(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows the media to limit the size of the track boundary rectangle.  Therefore, the region
     * returned by GetTrackBoundsRgn may not be rectangular and may be smaller than the track boundary region.
     * QuickTime::GetTrackBoundsRgn()
     * @param t the Track
     * @return a Region object specifying the region track's media uses to do drawing.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Track#getBoundsRgn()}
     */
    public static Region fromTrackBounds(Track paramTrack) throws QTException {
        int i = GetTrackBoundsRgn(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics
     * world during a specified segment.
     * QuickTime::GetTrackSegmentDisplayBoundsRgn()
     * @param t the Track
     * @param time Specifies the starting time of the track segment to consider.
     * @param duration Specifies the length of the segment to consider. Set this parameter to 0 to
     *  consider an instant in time.
     * @param {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Track#getSegmentDisplayBoundsRgn(int, int)}
     */
    public static Region fromTrackSegment(Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        int i = GetTrackSegmentDisplayBoundsRgn(QTObject.ID(paramTrack), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * Allows you to retrieve a channels clipping region.
     * QuickTime::SGGetChannelClip
     * @param vc the VisualChannel
     * @return the clipping region or null if no clip.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.sg.VisualChannel#getClip()}
     */
    public static Region fromVideoChannel(VisualChannel paramVisualChannel) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = SGGetChannelClip(QTObject.ID(paramVisualChannel), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Region(arrayOfInt[0]);
    }

    /**
     * Allocates a new empty region.
     * QuickDraw::NewRgn
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
     * QuickDraw::RectRgn
     * @param r the specified rectangle
     */
    public Region(QDRect paramQDRect) throws QTException {
        this();
        rect(paramQDRect);
    }

    /**
     * Converts a specified BitMap or PixMap to a region.
     * QuickDraw::BitMapToRegion
     * @param pMap the specified pixmap
     */
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

    /**
     * Returns size of the Region.
     * @return the size of the handle
     */
    public int getSize() {
        return super.getSize();
    }

    /**
     * Returns a copy of the bounding rectangle of the Region.
     * @return a rect
     */
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
     * QuickDraw::OpenRgn
     * @param graphics the QDGraphics object to record.
     * @param {@link quicktime.qd.QDException}
     */
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

    /**
     * Completes the collection of lines or frames that define the Region.
     * QuickDraw::CloseRgn
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
     * QuickDraw::RectRgn
     * @param r the specified rectangle
     */
    public void rect(QDRect paramQDRect) {
        RectRgn(_ID(), paramQDRect.getRect());
    }

    /**
     * Copies the mathematical structure of the implicit region into a newly created region.
     * QuickDraw::CopyRgn
     * QuickDraw::MacCopyRgn
     * @return A new region copy.
     * @return {@link quicktime.QTException}
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
     * QuickDraw::CopyRgn
     * @return A new region copy.
     */
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
     * QuickDraw::SetEmptyRgn
     */
    public void setEmpty() {
        SetEmptyRgn(_ID());
    }

    /**
     * Resets the mathematical structure to the specified coordinates and sizes.
     * Destroys the previous mathematical structure of the implicit region.
     * May reset the structure to the rectangle(0,0,0,0), if bad parameters.
     * QuickDraw::SetRectRgn
     * QuickDraw::MacSetRectRgn
     * @param x the specified leftmost position.
     * @param y the specified topmost position.
     * @param width the specified width.
     * @param height the specified height.
     */
    public void setRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = paramInt2 + paramInt4;
        int j = paramInt1 + paramInt3;
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            SetRectRgn(_ID(), (short) paramInt1, (short) paramInt2, (short) j, (short) i);
        } else {
            MacSetRectRgn(_ID(), (short) paramInt1, (short) paramInt2, (short) j, (short) i);
        }
    }

    /**
     * Moves the region horizontally and vertically by adding the respective parameters to all of the region.
     * QuickDraw::OffsetRgn
     * QuickDraw::MacOffsetRgn
     * @param dh the specified horizontal offset.
     * @param dv the specified vertical offset.
     */
    public void offset(int paramInt1, int paramInt2) {
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            OffsetRgn(_ID(), (short) paramInt1, (short) paramInt2);
        } else {
            MacOffsetRgn(_ID(), (short) paramInt1, (short) paramInt2);
        }
    }

    /**
     * Moves all points on the region boundary horizontally and vertically by adding the respective parameters to those points.
     * QuickDraw::InsetRgn
     * @param dh the specified horizontal inset.
     * @param dv the specified vertical inset.
     */
    public void inset(int paramInt1, int paramInt2) {
        InsetRgn(_ID(), (short) paramInt1, (short) paramInt2);
    }

    /**
     * Calculates the intersection of two regions and creates a new region representing that structure.
     * May return an empty region if no intersection or one of the regions is empty.
     * QuickDraw::SectRgn
     * @param regionB the specified intersecting region.
     * @return The region of intersection.
     * @return {@link quicktime.QTException}
     */
    public Region sect(Region paramRegion) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        SectRgn(_ID(), QTObject.ID(paramRegion), i);
        return new Region(i);
    }

    /**
     * Calculates the intersection of this region and the incoming QDRect
     * and creates a new region representing that structure.
     * May return an empty region if no intersection or one of the regions is empty.
     * QuickDraw::SectRgn
     * @param r the specified intersecting rect.
     * @return The region of intersection.
     * @return {@link quicktime.QTException}
     */
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

    /**
     * Calculates the union of two regions and creates a new region representing that structure.
     * May return an empty region if both regions are empty.
     * QuickDraw::UnionRgn
     * QuickDraw::MacUnionRgn
     * @param regionB the specified region to union with.
     * @return The region of union.
     * @return {@link quicktime.QTException}
     */
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

    /**
     * Calculates the union of this region and the incoming rect,
     * and creates a new region representing that structure.
     * May return an empty region if both regions are empty.
     * QuickDraw::UnionRgn
     * @param rect the specified rect to union with.
     * @return The region of union.
     * @return {@link quicktime.QTException}
     */
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

    /**
     * Calculates the subtracts the specified explicit region from the implicit region and creates a new region representing that structure.
     * May return an empty region if the implicit region is empty.
     * QuickDraw::DiffRgn
     * @param regionB the specified region to find difference with.
     * @return The region of difference.
     * @return {@link quicktime.QTException}
     */
    public Region diff(Region paramRegion) throws QTException {
        int i = NewRgn();
        if (i == 0)
            QDException.checkError(-108);
        DiffRgn(_ID(), QTObject.ID(paramRegion), i);
        return new Region(i);
    }

    /**
     * Calculates the difference between the union and intersection of two regions and creates a new region representing that structure.
     * May return an empty region if both regions are coincident.
     * QuickDraw::XorRgn
     * QuickDraw::MacXorRgn
     * @param regionB the specified region to xor with.
     * @return The region of exclusively or.
     * @return {@link quicktime.QTException}
     */
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

    /**
     * Checks whether the specified rectangle intersects the region.
     * QuickDraw::RectInRgn
     * @param srcRect the specified source rectangle
     * @return True if at least one bit of the rectangle is contained by the region, false otherwise.
     */
    public boolean rectIn(QDRect paramQDRect) {
        return RectInRgn(paramQDRect.getRect(), _ID()) != 0;
    }

    /**
     * Compares two regions for sizes, shapes, and locations.
     * QuickDraw::EqualRgn
     * QuickDraw::MacEqualRgn
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
     * QuickDraw::EmptyRgn
     * @return True if region is empty, false otherwise.
     */
    public boolean empty() {
        return EmptyRgn(_ID()) != 0;
    }

    /**
     * Takes the implicit region with one rectangle, maps and scales it to another rectangle.
     * QuickDraw::MapRgn
     * @param srcRect the specified source rectangle
     * @param dstRect the specified destination rectangle
     */
    public void map(QDRect paramQDRect1, QDRect paramQDRect2) {
        MapRgn(_ID(), paramQDRect1.getRect(), paramQDRect2.getRect());
    }

    /**
     * Checks whether the pixel below and to the right of the point is within the region.
     * QuickDraw::PtInRgn
     * @param pt the specified point.
     * @return True if pt is contained by the region, false otherwise.
     */
    public boolean pointIn(QDPoint paramQDPoint) {
        return PtInRgn(paramQDPoint.getPoint(), _ID()) != 0;
    }

    /**
     * Print information about this object.
     * @return a string representing this Region object
     */
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

    private static native void MacSetRectRgn(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4);

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

    private static native void SetRectRgn(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4);

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
