
package quicktime;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import quicktime.internal.toolbox.AtomManager;
import quicktime.internal.toolbox.ClockManager;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieControllerManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.MusicManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.SoundManager;
import quicktime.internal.toolbox.SpriteManager;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.qd.ColorTable;
import quicktime.qd.Polygon;
import quicktime.sound.SPBDevice;
import quicktime.sound.SndChannel;
import quicktime.std.movies.media.UserData;
import quicktime.util.QTPointerRef;


final class QTObjectManagement implements
                               quicktime.jdirect.QuickTimeLib,
                               quicktime.jdirect.QuickTimeVRLib,
                               quicktime.jdirect.QuickDraw3DLib,
                               quicktime.jdirect.QTStreamingLib {

    private static final Logger logger = System.getLogger(QTObjectManagement.class.getName());

    static int addCount = 0;

    static int removeCount = 0;

    private static boolean lock = false;

    private static Hashtable<Integer, Object> qtObjs;

    private static final String atomContainerName = "AtomContainer";
    private static final String componentName = "Component";
    private static final String cdSequence = "CDSequence";
    private static final String codecNameListName = "CodecNameList";
    private static final String colorTableName = "ColorTable";
    private static final String qtHandleRefName = "QTHandleRef";
    private static final String movieName = "Movie";
    private static final String movieEditStateName = "MovieEditState";
    private static final String movieControllerName = "MovieController";
    private static final String noteChannelName = "NoteChannel";
    private static final String pictName = "Pict";
    private static final String pixMapName = "PixMap";
    private static final String presentationName = "Presentation";
    private static final String qtPointerRefName = "QTPointerRef";
    private static final String qdGraphics = "QDGraphics";
    private static final String regionName = "Region";
    private static final String polygonName = "Polygon";
    private static final String sndChannelName = "SndChannel";
    private static final String spbDeviceName = "SPBDevice";
    private static final String sgDeviceListName = "SGDeviceList";
    private static final String spriteWorldName = "SpriteWorld";
    private static final String streamName = "Stream";
    private static final String timeBaseName = "TimeBase";
    private static final String trackEditStateName = "TrackEditState";
    private static final String userDataName = "UserData";

    static boolean isMember(int id) {
        while (lock)
            Thread.yield();
        if (qtObjs == null)
            return false;
        Integer localInteger = id;
        return qtObjs.containsKey(localInteger);
    }

    static boolean add(QTObject qtObject, int id, int ownerID) {
        if ((qtObject == null) || (id == 0)) {

            return false;
        }

        if (qtObjs == null) {
            qtObjs = new Hashtable<>();
        }
        if (qtObjs.containsKey(id)) {
            return false;
        }

        String str1 = null;

        if ((qtObject instanceof quicktime.std.comp.Component)) {
            if ((qtObject instanceof quicktime.std.movies.MovieController)) {
                str1 = "MovieController";
            } else
                str1 = "Component";
        } else if ((qtObject instanceof quicktime.util.QTHandleRef)) {
            str1 = switch (qtObject) {
                case quicktime.qd.Region region -> "Region";
                case Polygon polygon -> "Polygon";
                case quicktime.qd.Pict pict -> "Pict";
                case quicktime.std.movies.AtomContainer atomContainer -> "AtomContainer";
                case ColorTable colorTable -> "ColorTable";
                case quicktime.qd.PixMap pixMap -> "PixMap";
                case quicktime.std.sg.SGDeviceList sgDeviceList -> "SGDeviceList";
                default -> "QTHandleRef";
            };
        } else if ((qtObject instanceof QTPointerRef)) {
            String name = qtObject.getClass().getName();
            if (name.endsWith("QDGraphics")) {
                str1 = "QDGraphics";
            } else if (name.endsWith("CodecNameList")) {
                str1 = "CodecNameList";
            } else {
                str1 = "QTPointerRef";
            }
        } else if (((qtObject instanceof quicktime.std.image.DSequence))
                   || ((qtObject instanceof quicktime.std.image.CSequence))) {
            str1 = "CDSequence";
        } else if ((qtObject instanceof quicktime.std.movies.Movie)) {
            str1 = "Movie";
        } else if ((qtObject instanceof quicktime.std.anim.SpriteWorld)) {
            str1 = "SpriteWorld";
        } else if ((qtObject instanceof quicktime.std.movies.MovieEditState)) {
            str1 = "MovieEditState";
        } else if ((qtObject instanceof SPBDevice)) {
            str1 = "SPBDevice";
        } else if ((qtObject instanceof SndChannel)) {
            str1 = "SndChannel";
        } else if ((qtObject instanceof quicktime.std.clocks.TimeBase)) {
            str1 = "TimeBase";
        } else if ((qtObject instanceof quicktime.std.movies.TrackEditState)) {
            str1 = "TrackEditState";
        } else if ((qtObject instanceof UserData)) {
            str1 = "UserData";
        } else if ((qtObject instanceof quicktime.std.music.NoteChannel)) {
            str1 = "NoteChannel";
        } else if ((!QTSession.hasSecurityRestrictions()) && (!QTSession.isCurrentOS(2))) {
            if ((qtObject instanceof quicktime.streaming.Presentation)) {
                str1 = "Presentation";
            } else if ((qtObject instanceof quicktime.streaming.Stream)) {
                str1 = "Stream";
            }
        }
        if (str1 == null) {
            return false;
        }

        if (ownerID != 0) {
            str1 = str1 + "_" + ownerID;
        }

        qtObjs.put(id, str1);
        return true;
    }

    static boolean remove(int id, QTObject qtObject) throws QTException {
        Integer localInteger = id;
        if (qtObjs.containsKey(localInteger)) {
            try {
                disposeSessionObject(id, (String) qtObjs.get(localInteger), qtObject);
            } catch (QTException e) {
                throw e;
            } finally {
                qtObjs.remove(localInteger);
            }
            return true;
        }

        return false;
    }

    static void removeAll() {
        if (qtObjs == null) {
            return;
        }

        while (lock)
            Thread.yield();
        lock = true;

        Enumeration<Integer> enumeration = qtObjs.keys();
        while (enumeration.hasMoreElements()) {
            Integer id = enumeration.nextElement();
            String localObject2 = (String) qtObjs.get(id);

            if (localObject2.startsWith("SGDeviceList")) {
                try {
                    disposeSessionObject(id, "SGDeviceList");
                } catch (QTException e) {
                    logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
            if (localObject2.startsWith("NoteChannel")) {
                try {
                    disposeSessionObject(id, "NoteChannel");
                } catch (QTException e) {
                    logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
            if (localObject2.startsWith("SpriteWorld")) {
                try {
                    disposeSessionObject(id, "SpriteWorld");
                } catch (QTException e) {
                    logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
            if (localObject2.startsWith("CDSequence")) {
                try {
                    disposeSessionObject(id, "CDSequence");
                } catch (QTException e) {
                    logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
        }
        enumeration = qtObjs.keys();
        Vector<Integer> vector1 = new Vector<>();
        Vector<Integer> vector2 = new Vector<>();
        Integer localInteger;
        while (enumeration.hasMoreElements()) {
            localInteger = enumeration.nextElement();
            String name = (String) qtObjs.get(localInteger);
            if ((name.startsWith("Movie")) && (!name.startsWith("MovieController")) && (!name.startsWith("MovieEditState"))) {
                vector1.add(localInteger);
            } else if (name.startsWith("QDGraphics")) {
                vector2.add(localInteger);
            } else if ((!name.startsWith("SGDeviceList")) &&
                       (!name.startsWith("NoteChannel")) &&
                       (!name.startsWith("SpriteWorld")) &&
                       (!name.startsWith("CDSequence"))) {
                try {
                    disposeSessionObject(localInteger, name);
                } catch (QTException e) {
                    logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
        }

        enumeration = vector1.elements();
        while (enumeration.hasMoreElements()) {
            localInteger = enumeration.nextElement();
            try {
                disposeSessionObject(localInteger, "Movie");
            } catch (QTException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }

        enumeration = vector2.elements();
        while (enumeration.hasMoreElements()) {
            localInteger = enumeration.nextElement();
            try {
                disposeSessionObject(localInteger, "QDGraphics");
            } catch (QTException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }

        qtObjs.clear();
        lock = false;

        qtObjs = null;
        vector1.removeAllElements();
        vector1 = null;
        vector2.removeAllElements();
        vector2 = null;
    }

    private static int pullIDFromName(String name) {
        int i = name.lastIndexOf("_");
        if (i != -1) {
            String str = name.substring(i + 1);
            return Integer.parseInt(str);
        }

        return 0;
    }

    private static void disposeSessionObject(int id, String name, Object object) throws QTException {
        try {
            if (object == null) {
                disposeSessionObject(id, name);
            } else if ((object instanceof SndChannel)) {
                boolean bool = ((SndChannel) object).isQuietNow();
                int i = SndDisposeChannel(id, (byte) (bool ? 1 : 0));

                QTException.checkError(i);
            } else {
                disposeSessionObject(id, name);
            }
        } catch (UnsatisfiedLinkError | UnsupportedOperationException ignored) {
        }
    }

    private static void disposeSessionObject(int id, String name) throws QTException {
        try {
            Object localObject = null;

            int i = 0;
            if (name.startsWith("AtomContainer")) {
                i = QTDisposeAtomContainer(id);
            } else if (name.startsWith("Component")) {
                i = CloseComponent(id);
            } else if (name.startsWith("CDSequence")) {
                i = CDSequenceEnd(id);
            } else if (name.startsWith("CodecNameList")) {
                i = DisposeCodecNameList(id);
            } else if (name.startsWith("ColorTable")) {
                DisposeCTable(id);
            } else if (name.startsWith("QDGraphics")) {
                DisposeGWorld(id);
            } else if (name.startsWith("QTHandleRef")) {
                DisposeHandle(id);

            } else if (name.startsWith("MovieEditState")) {
                i = DisposeMovieEditState(id);
            } else if (name.startsWith("MovieController")) {
                DisposeMovieController(id);
            } else if (name.startsWith("Movie")) {
                DisposeMovie(id);
            } else {
                int j;
                if (name.startsWith("NoteChannel")) {
                    j = pullIDFromName(name);
                    if (j != 0) {
                        i = NADisposeNoteChannel(j, id);
                    }
                } else if (name.startsWith("Pict")) {
                    KillPicture(id);
                } else if (name.startsWith("PixMap")) {
                    DisposeMatte(id);
                } else if (name.startsWith("QTPointerRef")) {
                    DisposePtr(id);
                } else if (name.startsWith("Region")) {
                    DisposeRgn(id);
                } else if (name.startsWith("Polygon")) {
                    KillPoly(id);
                } else if (name.startsWith("Presentation")) {
                    i = QTSDisposePresentation(id, 0);
                } else if (name.startsWith("SndChannel")) {
                    i = SndDisposeChannel(id, (byte) 1);
                } else if (name.startsWith("SPBDevice")) {
                    SPBCloseDevice(id);
                } else if (name.startsWith("SGDeviceList")) {

                    j = pullIDFromName(name);
                    if (j != 0) {
                        i = SGDisposeDeviceList(j, id);
                    }
                } else if (name.startsWith("SpriteWorld")) {
                    DisposeSpriteWorld(id);
                } else if (name.startsWith("Stream")) {
                    i = QTSDisposeStream(id, 0);
                } else if (name.startsWith("TimeBase")) {
                    DisposeTimeBase(id);
                } else if (name.startsWith("TrackEditState")) {
                    i = DisposeTrackEditState(id);
                } else if (name.startsWith("UserData")) {
                    i = DisposeUserData(id);
                }
            }

            QTException.checkError(i);
        } catch (UnsatisfiedLinkError | UnsupportedOperationException ignored) {
        }
    }

    private static short QTDisposeAtomContainer(int container) {
        return AtomManager.QTDisposeAtomContainer(container);
    }

    private static void DisposeCTable(int ctab) {
        QuickDrawManager.DisposeCTable(ctab);
    }

    private static void DisposeHandle(int h) {
        MemoryManager.DisposeHandle(h);
    }

    private static void DisposeRgn(int rgn) {
        QuickDrawManager.DisposeRgn(rgn);
    }

    private static short DisposeCodecNameList(int list) {
        return ImageCompressionManager.DisposeCodecNameList(list);
    }

    private static void DisposeMatte(int pixMap) {
        QuickDrawManager.DisposeMatte(pixMap);
    }

    private static void DisposePtr(int p) {
        MemoryManager.DisposePtr(p);
    }

    private static short DisposeMovieEditState(int state) {
        return MovieToolbox.DisposeMovieEditState(state);
    }

    private static void DisposeTimeBase(int tb) {
        ClockManager.DisposeTimeBase(tb);
    }

    private static short DisposeTrackEditState(int state) {
        return MovieToolbox.DisposeTrackEditState(state);
    }

    private static short DisposeUserData(int userData) {
        return MovieToolbox.DisposeUserData(userData);
    }

    private static void DisposeMovieController(int mc) {
        MovieControllerManager.DisposeMovieController(mc);
    }

    private static void DisposeMovie(int movie) {
        MovieToolbox.DisposeMovie(movie);
    }

    private static short CloseComponent(int ci) {
        return ComponentManager.CloseComponent(ci);
    }

    private static short CDSequenceEnd(int seqID) {
        return ImageCompressionManager.CDSequenceEnd(seqID);
    }

    private static void DisposeSpriteWorld(int spriteWorld) {
        SpriteManager.DisposeSpriteWorld(spriteWorld);
    }

    private static void KillPicture(int pict) {
        QuickDrawManager.KillPicture(pict);
    }

    private static void KillPoly(int poly) {
        QuickDrawManager.KillPoly(poly);
    }

    private static void DisposeGWorld(int gworld) {
        QuickDrawManager.DisposeGWorld(gworld);
    }

    private static short SPBCloseDevice(int inRefNum) {
        return SoundManager.SPBCloseDevice(inRefNum);
    }

    private static short SndDisposeChannel(int chan, byte quietNow) {
        return SoundManager.SndDisposeChannel(chan, quietNow);
    }

    private static int NADisposeNoteChannel(int na, int noteChannel) {
        return MusicManager.NADisposeNoteChannel(na, noteChannel);
    }

    private static int SGDisposeDeviceList(int sg, int list) {
        return SequenceGrabberManager.SGDisposeDeviceList(sg, list);
    }

    private static short QTSDisposePresentation(int presentation, int flags) {
        return StreamingManager.QTSDisposePresentation(presentation, flags);
    }

    private static short QTSDisposeStream(int stream, int flags) {
        return StreamingManager.QTSDisposeStream(stream, flags);
    }
}
