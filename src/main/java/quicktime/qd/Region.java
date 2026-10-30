package quicktime.qd;

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

    private static final boolean isMacOS = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4));

    private static final int[] savedPort = {0};

    private static final int[] savedDevice = {0};

    private QDGraphics recordingGraphics;

    /**
     * Determines the current clipping region.
     *
     * @param gi the GraphicsImporter
     * @return The current clip region or null, if not possible
     * @throws quicktime.QTException
     * @see "QuickTime::GraphicsImportGetClip"
     * @see quicktime.std.image.GraphicsImporter#getClip()
     */
    public static Region fromGraphicsImporter(GraphicsImporter gi) throws QTException {
        int[] clip = new int[1];
        int i = GraphicsImportGetClip(QTObject.ID(gi), clip);
        QTException.checkError(i);
        if (clip[0] == 0)
            return null;
        return clip[0] == 0 ? null : new Region(clip[0]);
    }

    /**
     * Determines the default clipping region.
     *
     * @param gi the GraphicsImporter
     * @return The default clip region or null, if not possible
     * @throws quicktime.QTException
     * @see "QuickTime::GraphicsImportGetDefaultClip"
     * @see quicktime.std.image.GraphicsImporter#getClip()
     */
    public static Region fromGraphicsImporterDefault(GraphicsImporter gi) throws QTException {
        int[] clip = {0};
        int i = GraphicsImportGetDefaultClip(QTObject.ID(gi), clip);
        QTException.checkError(i);
        return clip[0] == 0 ? null : new Region(clip[0]);
    }

    /**
     * This method allows your application to determine a movie's current display clipping region.
     *
     * @param m the Movie
     * @return a Region object.
     * @throws quicktime.QTException
     * @see "QuickTime::GetMovieDisplayClipRgn()"
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
     *
     * @param m the Movie
     * @return a Region object.
     * @throws quicktime.QTException
     * @see "QuickTime::GetMovieDisplayBoundsRgn()"
     * @see quicktime.std.movies.Movie#getDisplayBoundsRgn()
     */
    public static Region fromMovieDisplayBounds(Movie m) throws QTException {
        int i = GetMovieDisplayBoundsRgn(QTObject.ID(m));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a movie's clipping region.
     *
     * @param m the Movie
     * @return a Region object.
     * @throws quicktime.QTException
     * @see "QuickTime::GetMovieClipRgn()"
     * @see "quicktime.std.movies.Movie#getMovieClipRgn"
     */
    public static Region fromMovieClip(Movie m) throws QTException {
        int i = GetMovieClipRgn(QTObject.ID(m));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a Presentation's clipping region.
     *
     * @param p the Presentation
     * @param s the Stream
     * @return a Region object.
     * @throws quicktime.QTException
     * @see "QuickTime::QTSPresGetClip()"
     * @see quicktime.streaming.Presentation#getClip(quicktime.streaming.Stream)
     */
    public static Region fromPresentationClip(Presentation p, Stream s) throws QTException {
        int[] clip = {0};
        StdQTException.checkError(QTSPresGetClip(QTObject.ID(p), QTObject.ID(s), clip));
        return clip[0] == 0 ? null : new Region(clip[0]);
    }

    /**
     * This method allows your application to determine a movie's boundary region.
     * This region is in the movie's display coordinate system.
     * The movie boundary region encloses all of a movie's tracks after the union of
     * the track clip and the track matrix has been applied to all the movie's
     * tracks (but not to the movie itself).
     *
     * @param m the Movie
     * @return a Region object specifying the movie boundary region.
     * @throws quicktime.QTException
     * @see "QuickTime::GetMovieBoundsRgn()"
     * @see quicktime.std.movies.Movie#getBoundsRgn()
     */
    public static Region fromMovieBounds(Movie m) throws QTException {
        int i = GetMovieBoundsRgn(QTObject.ID(m));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine a movie's display boundary region during a specified segment.
     *
     * @param m        the Movie
     * @param time     Specifies the starting time of the movie segment to consider.
     * @param duration Specifies the length of the segment to consider.
     * @return a Region object.
     * @throws quicktime.QTException
     * @see "QuickTime::GetMovieSegmentDisplayBoundsRgn()"
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
     *
     * @param mc the MovieController
     * @return a Region object specifying the boundary region for this controller.
     * @throws quicktime.QTException
     * @see "QuickTime::MCGetControllerBoundsRgn()"
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
     *
     * @param mc     the MovieController
     * @param window Specifies the window. This may NOT be null.
     * @return a Region object specifying the window region.
     * @throws quicktime.QTException
     * @see "QuickTime::MCGetWindowRgn()"
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
     *
     * @param mc the MovieController
     * @return the clipping region of the entire movie controller
     * @throws quicktime.QTException
     * @see "QuickTime::MCGetClip()"
     * @see quicktime.std.movies.MovieController#getClip()
     */
    public static Region fromMovieControllerClip(MovieController mc) throws QTException {
        synchronized (QTNative.globalsLock) {
            int[] theClip = new int[1];
            StdQTException.checkError(MCGetClip(QTObject.ID(mc), theClip, null));
            return new Region(theClip[0]);
        }
    }

    /**
     * This method allows you to display a controller's badge.
     *
     * @param mc       the MovieController
     * @param movieRgn Specifies the boundary region of the controller's movie.
     * @return A Region that gives information about the location of the badge.
     * @throws quicktime.QTException
     * @see "QuickTime::MCDrawBadge()"
     * @see quicktime.std.movies.MovieController#drawBadge(quicktime.qd.Region, boolean)
     */
    public static Region fromMovieControllerBadge(MovieController mc, Region movieRgn, boolean returnBadgeRgn) throws QTException {
        int[] badgeRgn = null;
        if (returnBadgeRgn) {
            badgeRgn = new int[1];
        }
        StdQTException.checkError(MCDrawBadge(QTObject.ID(mc), QTObject.ID(movieRgn), badgeRgn));
        return returnBadgeRgn ? new Region(badgeRgn[0]) : null;
    }

    /**
     * This method allows your application to determine the clipping region of a track.
     *
     * @param t the Track
     * @return a Region object specifying the clip region for this track.
     * @throws quicktime.QTException
     * @see "QuickTime::GetTrackClipRgn()"
     * @see quicktime.std.movies.Track#getClipRgn()
     */
    public static Region fromTrackClip(Track t) throws QTException {
        int i = GetTrackClipRgn(QTObject.ID(t));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics world.
     *
     * @param t the Track
     * @return a Region object specifying the track's bounds region.
     * @throws quicktime.QTException
     * @see "QuickTime::GetTrackDisplayBoundsRgn()"
     * @see quicktime.std.movies.Track#getDisplayBoundsRgn()
     */
    public static Region fromTrackDisplay(Track t) throws QTException {
        int i = GetTrackDisplayBoundsRgn(QTObject.ID(t));
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * This method allows your application to determine the region the track occupies in a movie's boundary region.
     *
     * @param t the Track
     * @return a Region object specifying the region the track occupies in a movie's boundary region.
     * @throws quicktime.QTException
     * @see "QuickTime::GetTrackMovieBoundsRgn()"
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
     *
     * @param t the Track
     * @return a Region object specifying the region track's media uses to do drawing.
     * @throws quicktime.QTException
     * @see "QuickTime::GetTrackBoundsRgn()"
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
     *
     * @param t        the Track
     * @param time     Specifies the starting time of the track segment to consider.
     * @param duration Specifies the length of the segment to consider. Set this parameter to 0 to
     *                 consider an instant in time.
     * @throws quicktime.QTException
     * @see "QuickTime::GetTrackSegmentDisplayBoundsRgn()"
     * @see quicktime.std.movies.Track#getSegmentDisplayBoundsRgn(int, int)
     */
    public static Region fromTrackSegment(Track t, int time, int duration) throws QTException {
        int i = GetTrackSegmentDisplayBoundsRgn(QTObject.ID(t), time, duration);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Region(i);
    }

    /**
     * Allows you to retrieve a channels clipping region.
     *
     * @param vc the VisualChannel
     * @return the clipping region or null if no clip.
     * @throws quicktime.QTException
     * @see "QuickTime::SGGetChannelClip"
     * @see quicktime.std.sg.VisualChannel#getClip()
     */
    public static Region fromVideoChannel(VisualChannel vc) throws QTException {
        int[] rgn = {0};
        int i = SGGetChannelClip(QTObject.ID(vc), rgn);
        StdQTException.checkError(i);
        return rgn[0] == 0 ? null : new Region(rgn[0]);
    }

    /**
     * Allocates a new empty region.
     *
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
     *
     * @param r the specified rectangle
     * @see "QuickDraw::RectRgn"
     */
    public Region(QDRect r) throws QTException {
        this();
        rect(r);
    }

    /**
     * Converts a specified BitMap or PixMap to a region.
     *
     * @param pMap the specified pixmap
     * @see "QuickDraw::BitMapToRegion"
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

    private Region(int nativeRep) throws QTException {
        super(nativeRep, null, false);
    }

    /**
     * Returns size of the Region.
     *
     * @return the size of the handle
     */
    @Override
    public int getSize() {
        return super.getSize();
    }

    /**
     * Returns a copy of the bounding rectangle of the Region.
     *
     * @return a rect
     */
    public QDRect getRgnBBox() {
        if (QTSession.isCurrentOS(4)) {
            QDRect rgnBBox = new QDRect();
            GetRegionBounds(_ID(), rgnBBox.getBytes());
            return rgnBBox;
        }
        Object destArray = new byte[8];
        getBytesAt(2, 8, (byte[]) destArray, 0);
        return QDRect.fromArray((byte[]) destArray, 8);
    }

    /**
     * Returns a copy of the bounding rectangle of the Region.
     *
     * @return a rect
     */
    public QDRect getBounds() {
        return getRgnBBox();
    }

    /**
     * Begins the recording of lines or frames in the specified QDGraphics that when closed
     * will define a Region.
     *
     * @param graphics the QDGraphics object to record.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::OpenRgn"
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
     *
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
     *
     * @param r the specified rectangle
     * @see "QuickDraw::RectRgn"
     */
    public void rect(QDRect r) {
        RectRgn(_ID(), r.getRect());
    }

    /**
     * Copies the mathematical structure of the implicit region into a newly created region.
     *
     * @return A new region copy.
     * @throws quicktime.QTException
     * @see "QuickDraw::CopyRgn"
     * @see "QuickDraw::MacCopyRgn"
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
     *
     * @return A new region copy.
     * @see "QuickDraw::CopyRgn"
     */
    @Override
    public Object clone() {
        try {
            return copy();
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    /**
     * Resets the mathematical structure to the rectangle (0,0,0,0).
     * Destroys the previous mathematical structure of the implicit region.
     *
     * @see "QuickDraw::SetEmptyRgn"
     */
    public void setEmpty() {
        SetEmptyRgn(_ID());
    }

    /**
     * Resets the mathematical structure to the specified coordinates and sizes.
     * Destroys the previous mathematical structure of the implicit region.
     * May reset the structure to the rectangle(0,0,0,0), if bad parameters.
     *
     * @param x      the specified leftmost position.
     * @param y      the specified topmost position.
     * @param width  the specified width.
     * @param height the specified height.
     * @see "QuickDraw::SetRectRgn"
     * @see "QuickDraw::MacSetRectRgn"
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
     *
     * @param dh the specified horizontal offset.
     * @param dv the specified vertical offset.
     * @see "QuickDraw::OffsetRgn"
     * @see "QuickDraw::MacOffsetRgn"
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
     *
     * @param dh the specified horizontal inset.
     * @param dv the specified vertical inset.
     * @see "QuickDraw::InsetRgn"
     */
    public void inset(int dh, int dv) {
        InsetRgn(_ID(), (short) dh, (short) dv);
    }

    /**
     * Calculates the intersection of two regions and creates a new region representing that structure.
     * May return an empty region if no intersection or one of the regions is empty.
     *
     * @param regionB the specified intersecting region.
     * @return The region of intersection.
     * @throws quicktime.QTException
     * @see "QuickDraw::SectRgn"
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
     *
     * @param r the specified intersecting rect.
     * @return The region of intersection.
     * @throws quicktime.QTException
     * @see "QuickDraw::SectRgn"
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
     *
     * @param regionB the specified region to union with.
     * @return The region of union.
     * @throws quicktime.QTException
     * @see "QuickDraw::UnionRgn"
     * @see "QuickDraw::MacUnionRgn"
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
     *
     * @param rect the specified rect to union with.
     * @return The region of union.
     * @throws quicktime.QTException
     * @see "QuickDraw::UnionRgn"
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
     *
     * @param regionB the specified region to find difference with.
     * @return The region of difference.
     * @throws quicktime.QTException
     * @see "QuickDraw::DiffRgn"
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
     *
     * @param regionB the specified region to xor with.
     * @return The region of exclusively or.
     * @throws quicktime.QTException
     * @see "QuickDraw::XorRgn"
     * @see "QuickDraw::MacXorRgn"
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
     *
     * @param srcRect the specified source rectangle
     * @return True if at least one bit of the rectangle is contained by the region, false otherwise.
     * @see "QuickDraw::RectInRgn"
     */
    public boolean rectIn(QDRect srcRect) {
        return RectInRgn(srcRect.getRect(), _ID()) != 0;
    }

    /**
     * Compares two regions for sizes, shapes, and locations.
     *
     * @return true if both regions' sizes, shapes, and locations are identical, false otherwise.
     * @see "QuickDraw::EqualRgn"
     * @see "QuickDraw::MacEqualRgn"
     */
    public boolean equalRgn(Region b) {
        if ((isMacOS) || (QTSession.isCurrentOS(4))) {
            return EqualRgn(_ID(), QTObject.ID(b)) != 0;
        }
        return MacEqualRgn(_ID(), QTObject.ID(b)) != 0;
    }

    /**
     * Determines whether a region is empty or not.
     *
     * @return True if region is empty, false otherwise.
     * @see "QuickDraw::EmptyRgn"
     */
    public boolean empty() {
        return EmptyRgn(_ID()) != 0;
    }

    /**
     * Takes the implicit region with one rectangle, maps and scales it to another rectangle.
     *
     * @param srcRect the specified source rectangle
     * @param dstRect the specified destination rectangle
     * @see "QuickDraw::MapRgn"
     */
    public void map(QDRect srcRect, QDRect dstRect) {
        MapRgn(_ID(), srcRect.getRect(), dstRect.getRect());
    }

    /**
     * Checks whether the pixel below and to the right of the point is within the region.
     *
     * @param pt the specified point.
     * @return True if pt is contained by the region, false otherwise.
     * @see "QuickDraw::PtInRgn"
     */
    public boolean pointIn(QDPoint pt) {
        return PtInRgn(pt.getPoint(), _ID()) != 0;
    }

    /**
     * Print information about this object.
     *
     * @return a string representing this Region object
     */
    public String toString() {
        return super.toString() + "[rgnBBox=" + getRgnBBox() + "]";
    }

    private static void SetGWorld(int port, int gdh) {
        QuickDrawManager.SetGWorld(port, gdh);
    }

    private static void GetGWorld(int[] destArray1, int[] destArray2) {
        QuickDrawManager.GetGWorld(destArray1, destArray2);
    }

    private static void OpenRgn() {
        QuickDrawManager.OpenRgn();
    }

    private static void CloseRgn(int rgn) {
        QuickDrawManager.CloseRgn(rgn);
    }

    private static int NewRgn() {
        return QuickDrawManager.NewRgn();
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static void SectRgn(int a, int b, int dst) {
        QuickDrawManager.SectRgn(a, b, dst);
    }

    private static int GraphicsImportGetClip(int gi, int[] clip) {
        return GraphicsImportManager.GraphicsImportGetClip(gi, clip);
    }

    private static int GraphicsImportGetDefaultClip(int gi, int[] clip) {
        return GraphicsImportManager.GraphicsImportGetDefaultClip(gi, clip);
    }

    private static int GetMovieDisplayClipRgn(int movie) {
        return MovieToolbox.GetMovieDisplayClipRgn(movie);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int GetMovieDisplayBoundsRgn(int movie) {
        return MovieToolbox.GetMovieDisplayBoundsRgn(movie);
    }

    private static int GetMovieClipRgn(int movie) {
        return MovieToolbox.GetMovieClipRgn(movie);
    }

    private static int GetMovieBoundsRgn(int movie) {
        return MovieToolbox.GetMovieBoundsRgn(movie);
    }

    private static int GetMovieSegmentDisplayBoundsRgn(int movie, int time, int duration) {
        return MovieToolbox.GetMovieSegmentDisplayBoundsRgn(movie, time, duration);
    }

    private static int MCGetControllerBoundsRgn(int mc) {
        return MovieControllerManager.MCGetControllerBoundsRgn(mc);
    }

    private static int MCGetWindowRgn(int mc, int window) {
        return MovieControllerManager.MCGetWindowRgn(mc, window);
    }

    private static int MCGetClip(int mc, int[] theClip, int[] movieClip) {
        return MovieControllerManager.MCGetClip(mc, theClip, movieClip);
    }

    private static int MCDrawBadge(int mc, int movieRgn, int[] badgeRgn) {
        return MovieControllerManager.MCDrawBadge(mc, movieRgn, badgeRgn);
    }

    private static int GetTrackClipRgn(int track) {
        return MovieToolbox.GetTrackClipRgn(track);
    }

    private static int GetTrackDisplayBoundsRgn(int track) {
        return MovieToolbox.GetTrackDisplayBoundsRgn(track);
    }

    private static int GetTrackMovieBoundsRgn(int track) {
        return MovieToolbox.GetTrackMovieBoundsRgn(track);
    }

    private static int GetTrackBoundsRgn(int track) {
        return MovieToolbox.GetTrackBoundsRgn(track);
    }

    private static int GetTrackSegmentDisplayBoundsRgn(int track, int time, int duration) {
        return MovieToolbox.GetTrackSegmentDisplayBoundsRgn(track, time, duration);
    }

    private static int SGGetChannelClip(int ref, int[] rgn) {
        return SequenceGrabberManager.SGGetChannelClip(ref, rgn);
    }

    private static short BitMapToRegion(int rgn, int bitmap) {
        return QuickDrawManager.BitMapToRegion(rgn, bitmap);
    }

    private static void RectRgn(int rgn, byte[] r) {
        QuickDrawManager.RectRgn(rgn, r);
    }

    private static void MacCopyRgn(int src, int dst) {
        QuickDrawManager.MacCopyRgn(src, dst);
    }

    private static void SetEmptyRgn(int rgn) {
        QuickDrawManager.SetEmptyRgn(rgn);
    }

    private static void MacSetRectRgn(int rgn, short left, short top, short right, short bottom) {
        QuickDrawManager.MacSetRectRgn(rgn, left, top, right, bottom);
    }

    private static void MacOffsetRgn(int rgn, short dh, short dv) {
        QuickDrawManager.MacOffsetRgn(rgn, dh, dv);
    }

    private static void InsetRgn(int rgn, short dh, short dv) {
        QuickDrawManager.InsetRgn(rgn, dh, dv);
    }

    private static void DisposeRgn(int rgn) {
        QuickDrawManager.DisposeRgn(rgn);
    }

    private static void MacUnionRgn(int a, int b, int dst) {
        QuickDrawManager.MacUnionRgn(a, b, dst);
    }

    private static void DiffRgn(int a, int b, int dst) {
        QuickDrawManager.DiffRgn(a, b, dst);
    }

    private static void MacXorRgn(int a, int b, int dst) {
        QuickDrawManager.MacXorRgn(a, b, dst);
    }

    private static byte RectInRgn(byte[] r, int rgn) {
        return QuickDrawManager.RectInRgn(r, rgn);
    }

    private static byte MacEqualRgn(int a, int b) {
        return QuickDrawManager.MacEqualRgn(a, b);
    }

    private static byte EmptyRgn(int rgn) {
        return QuickDrawManager.EmptyRgn(rgn);
    }

    private static void MapRgn(int rgn, byte[] src, byte[] dst) {
        QuickDrawManager.MapRgn(rgn, src, dst);
    }

    private static byte PtInRgn(int pt, int rgn) {
        return QuickDrawManager.PtInRgn(pt, rgn);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static void PaintRgn(int rgn) {
        QuickDrawManager.PaintRgn(rgn);
    }

    private static void CopyRgn(int src, int dst) {
        QuickDrawManager.CopyRgn(src, dst);
    }

    private static void SetRectRgn(int rgn, short left, short top, short right, short bottom) {
        QuickDrawManager.SetRectRgn(rgn, left, top, right, bottom);
    }

    private static void OffsetRgn(int rgn, short dh, short dv) {
        QuickDrawManager.OffsetRgn(rgn, dh, dv);
    }

    private static void UnionRgn(int a, int b, int dst) {
        QuickDrawManager.UnionRgn(a, b, dst);
    }

    private static void XorRgn(int a, int b, int dst) {
        QuickDrawManager.XorRgn(a, b, dst);
    }

    private static byte EqualRgn(int a, int b) {
        return QuickDrawManager.EqualRgn(a, b);
    }

    private static byte GetRegionBounds(int rgn, byte[] rect) {
        return QuickDrawManager.GetRegionBounds(rgn, rect);
    }

    private static byte IsRegionRectangular(int rgn) {
        return QuickDrawManager.IsRegionRectangular(rgn);
    }

    private static short QTSPresGetClip(int presentation, int stream, int[] clip) {
        return StreamingManager.QTSPresGetClip(presentation, stream, clip);
    }
}
