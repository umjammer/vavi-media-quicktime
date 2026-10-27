
package quicktime;

import java.security.AccessController;
import java.security.PrivilegedAction;
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
import quicktime.jdirect.QTNative;
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
    private static Object linkage;

    static final boolean debug = false;

    static final boolean addDebug = false;

    static final boolean removeDebug = false;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QTObjectManagement.class));
            }
        }

        new PrivelegedAction().establish();
    }

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

    static boolean isMember(int paramInt) {
        while (lock)
            Thread.yield();
        if (qtObjs == null)
            return false;
        Integer localInteger = paramInt;
        return qtObjs.containsKey(localInteger);
    }

    static boolean add(QTObject paramQTObject, int paramInt1, int paramInt2) {
        if ((paramQTObject == null) || (paramInt1 == 0)) {

            return false;
        }

        if (qtObjs == null) {
            qtObjs = new Hashtable<>();
        }
        if (qtObjs.containsKey(paramInt1)) {
            return false;
        }

        String str1 = null;

        if ((paramQTObject instanceof quicktime.std.comp.Component)) {
            if ((paramQTObject instanceof quicktime.std.movies.MovieController)) {
                str1 = "MovieController";
            } else
                str1 = "Component";
        } else if ((paramQTObject instanceof quicktime.util.QTHandleRef)) {
            str1 = switch (paramQTObject) {
                case quicktime.qd.Region region -> "Region";
                case Polygon polygon -> "Polygon";
                case quicktime.qd.Pict pict -> "Pict";
                case quicktime.std.movies.AtomContainer atomContainer -> "AtomContainer";
                case ColorTable colorTable -> "ColorTable";
                case quicktime.qd.PixMap pixMap -> "PixMap";
                case quicktime.std.sg.SGDeviceList sgDeviceList -> "SGDeviceList";
                default -> "QTHandleRef";
            };
        } else if ((paramQTObject instanceof QTPointerRef)) {
            String str2 = paramQTObject.getClass().getName();
            if (str2.endsWith("QDGraphics")) {
                str1 = "QDGraphics";
            } else if (str2.endsWith("CodecNameList")) {
                str1 = "CodecNameList";
            } else {
                str1 = "QTPointerRef";
            }
        } else if (((paramQTObject instanceof quicktime.std.image.DSequence))
                   || ((paramQTObject instanceof quicktime.std.image.CSequence))) {
            str1 = "CDSequence";
        } else if ((paramQTObject instanceof quicktime.std.movies.Movie)) {
            str1 = "Movie";
        } else if ((paramQTObject instanceof quicktime.std.anim.SpriteWorld)) {
            str1 = "SpriteWorld";
        } else if ((paramQTObject instanceof quicktime.std.movies.MovieEditState)) {
            str1 = "MovieEditState";
        } else if ((paramQTObject instanceof SPBDevice)) {
            str1 = "SPBDevice";
        } else if ((paramQTObject instanceof SndChannel)) {
            str1 = "SndChannel";
        } else if ((paramQTObject instanceof quicktime.std.clocks.TimeBase)) {
            str1 = "TimeBase";
        } else if ((paramQTObject instanceof quicktime.std.movies.TrackEditState)) {
            str1 = "TrackEditState";
        } else if ((paramQTObject instanceof UserData)) {
            str1 = "UserData";
        } else if ((paramQTObject instanceof quicktime.std.music.NoteChannel)) {
            str1 = "NoteChannel";
        } else if ((!QTSession.hasSecurityRestrictions()) && (!QTSession.isCurrentOS(2))) {
            if ((paramQTObject instanceof quicktime.streaming.Presentation)) {
                str1 = "Presentation";
            } else if ((paramQTObject instanceof quicktime.streaming.Stream)) {
                str1 = "Stream";
            }
        }
        if (str1 == null) {
            return false;
        }

        if (paramInt2 != 0) {
            str1 = str1 + "_" + paramInt2;
        }

        qtObjs.put(paramInt1, str1);
        return true;
    }

    static boolean remove(int paramInt, QTObject paramQTObject) throws QTException {
        Integer localInteger = paramInt;
        if (qtObjs.containsKey(localInteger)) {
            try {
                disposeSessionObject(paramInt, (String) qtObjs.get(localInteger), paramQTObject);
            } catch (QTException localQTException) {
                throw localQTException;
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

        Enumeration<Integer> localEnumeration = qtObjs.keys();
        while (localEnumeration.hasMoreElements()) {
            Integer localObject1 = (Integer) localEnumeration.nextElement();
            String localObject2 = (String) qtObjs.get(localObject1);

            if (localObject2.startsWith("SGDeviceList")) {
                try {
                    disposeSessionObject(localObject1, "SGDeviceList");
                } catch (QTException localQTException1) {
                    localQTException1.printStackTrace();
                }
            }
            if (localObject2.startsWith("NoteChannel")) {
                try {
                    disposeSessionObject(localObject1, "NoteChannel");
                } catch (QTException localQTException2) {
                    localQTException2.printStackTrace();
                }
            }
            if (localObject2.startsWith("SpriteWorld")) {
                try {
                    disposeSessionObject(localObject1, "SpriteWorld");
                } catch (QTException localQTException3) {
                    localQTException3.printStackTrace();
                }
            }
            if (localObject2.startsWith("CDSequence")) {
                try {
                    disposeSessionObject(localObject1, "CDSequence");
                } catch (QTException localQTException4) {
                    localQTException4.printStackTrace();
                }
            }
        }
        localEnumeration = qtObjs.keys();
        Object localObject1 = new Vector<>();
        Object localObject2 = new Vector<>();
        Integer localInteger;
        while (localEnumeration.hasMoreElements()) {
            localInteger = (Integer) localEnumeration.nextElement();
            String str = (String) qtObjs.get(localInteger);
            if ((str.startsWith("Movie")) && (!str.startsWith("MovieController")) && (!str.startsWith("MovieEditState"))) {
                ((Vector) localObject1).addElement(localInteger);
            } else if (str.startsWith("QDGraphics")) {
                ((Vector) localObject2).addElement(localInteger);
            } else if ((!str.startsWith("SGDeviceList")) &&

                       (!str.startsWith("NoteChannel")) &&

                       (!str.startsWith("SpriteWorld")) &&

                       (!str.startsWith("CDSequence"))) {
                try {
                    disposeSessionObject(localInteger, str);
                } catch (QTException localQTException7) {
                    localQTException7.printStackTrace();
                }
            }
        }

        localEnumeration = ((Vector) localObject1).elements();
        while (localEnumeration.hasMoreElements()) {
            localInteger = (Integer) localEnumeration.nextElement();
            try {
                disposeSessionObject(localInteger, "Movie");
            } catch (QTException localQTException5) {
                localQTException5.printStackTrace();
            }
        }

        localEnumeration = ((Vector) localObject2).elements();
        while (localEnumeration.hasMoreElements()) {
            localInteger = (Integer) localEnumeration.nextElement();
            try {
                disposeSessionObject(localInteger, "QDGraphics");
            } catch (QTException localQTException6) {
                localQTException6.printStackTrace();
            }
        }

        qtObjs.clear();
        lock = false;

        qtObjs = null;
        ((Vector<?>) localObject1).removeAllElements();
        localObject1 = null;
        ((Vector<?>) localObject2).removeAllElements();
        localObject2 = null;
    }

    private static int pullIDFromName(String paramString) {
        int i = paramString.lastIndexOf("_");
        if (i != -1) {
            String str = paramString.substring(i + 1);
            return Integer.valueOf(str);
        }

        return 0;
    }

    private static void disposeSessionObject(int paramInt, String paramString, Object paramObject) throws QTException {
        try {
            if (paramObject == null) {
                disposeSessionObject(paramInt, paramString);
            } else if ((paramObject instanceof SndChannel)) {
                boolean bool = ((SndChannel) paramObject).isQuietNow();
                int i = SndDisposeChannel(paramInt, (byte) (bool ? 1 : 0));

                QTException.checkError(i);
            } else {
                disposeSessionObject(paramInt, paramString);
            }
        } catch (UnsatisfiedLinkError | UnsupportedOperationException ignored) {
        }
    }

    private static void disposeSessionObject(int paramInt, String paramString) throws QTException {
        try {
            Object localObject = null;

            int i = 0;
            if (paramString.startsWith("AtomContainer")) {
                i = QTDisposeAtomContainer(paramInt);
            } else if (paramString.startsWith("Component")) {
                i = CloseComponent(paramInt);
            } else if (paramString.startsWith("CDSequence")) {
                i = CDSequenceEnd(paramInt);
            } else if (paramString.startsWith("CodecNameList")) {
                i = DisposeCodecNameList(paramInt);
            } else if (paramString.startsWith("ColorTable")) {
                DisposeCTable(paramInt);
            } else if (paramString.startsWith("QDGraphics")) {
                DisposeGWorld(paramInt);
            } else if (paramString.startsWith("QTHandleRef")) {
                DisposeHandle(paramInt);

            } else if (paramString.startsWith("MovieEditState")) {
                i = DisposeMovieEditState(paramInt);
            } else if (paramString.startsWith("MovieController")) {
                DisposeMovieController(paramInt);
            } else if (paramString.startsWith("Movie")) {
                DisposeMovie(paramInt);
            } else {
                int j;
                if (paramString.startsWith("NoteChannel")) {
                    j = pullIDFromName(paramString);
                    if (j != 0) {
                        i = NADisposeNoteChannel(j, paramInt);
                    }
                } else if (paramString.startsWith("Pict")) {
                    KillPicture(paramInt);
                } else if (paramString.startsWith("PixMap")) {
                    DisposeMatte(paramInt);
                } else if (paramString.startsWith("QTPointerRef")) {
                    DisposePtr(paramInt);
                } else if (paramString.startsWith("Region")) {
                    DisposeRgn(paramInt);
                } else if (paramString.startsWith("Polygon")) {
                    KillPoly(paramInt);
                } else if (paramString.startsWith("Presentation")) {
                    i = QTSDisposePresentation(paramInt, 0);
                } else if (paramString.startsWith("SndChannel")) {
                    i = SndDisposeChannel(paramInt, (byte) 1);
                } else if (paramString.startsWith("SPBDevice")) {
                    SPBCloseDevice(paramInt);
                } else if (paramString.startsWith("SGDeviceList")) {

                    j = pullIDFromName(paramString);
                    if (j != 0) {
                        i = SGDisposeDeviceList(j, paramInt);
                    }
                } else if (paramString.startsWith("SpriteWorld")) {
                    DisposeSpriteWorld(paramInt);
                } else if (paramString.startsWith("Stream")) {
                    i = QTSDisposeStream(paramInt, 0);
                } else if (paramString.startsWith("TimeBase")) {
                    DisposeTimeBase(paramInt);
                } else if (paramString.startsWith("TrackEditState")) {
                    i = DisposeTrackEditState(paramInt);
                } else if (paramString.startsWith("UserData")) {
                    i = DisposeUserData(paramInt);
                }
            }

            QTException.checkError(i);
        } catch (UnsatisfiedLinkError | UnsupportedOperationException ignored) {
        }
    }

    private static short QTDisposeAtomContainer(int paramInt) {
        return AtomManager.QTDisposeAtomContainer(paramInt);
    }

    private static void DisposeCTable(int paramInt) {
        QuickDrawManager.DisposeCTable(paramInt);
    }

    private static void DisposeHandle(int paramInt) {
        MemoryManager.DisposeHandle(paramInt);
    }

    private static void DisposeRgn(int paramInt) {
        QuickDrawManager.DisposeRgn(paramInt);
    }

    private static short DisposeCodecNameList(int paramInt) {
        return ImageCompressionManager.DisposeCodecNameList(paramInt);
    }

    private static void DisposeMatte(int paramInt) {
        QuickDrawManager.DisposeMatte(paramInt);
    }

    private static void DisposePtr(int paramInt) {
        MemoryManager.DisposePtr(paramInt);
    }

    private static short DisposeMovieEditState(int paramInt) {
        return MovieToolbox.DisposeMovieEditState(paramInt);
    }

    private static void DisposeTimeBase(int paramInt) {
        ClockManager.DisposeTimeBase(paramInt);
    }

    private static short DisposeTrackEditState(int paramInt) {
        return MovieToolbox.DisposeTrackEditState(paramInt);
    }

    private static short DisposeUserData(int paramInt) {
        return MovieToolbox.DisposeUserData(paramInt);
    }

    private static void DisposeMovieController(int paramInt) {
        MovieControllerManager.DisposeMovieController(paramInt);
    }

    private static void DisposeMovie(int paramInt) {
        MovieToolbox.DisposeMovie(paramInt);
    }

    private static short CloseComponent(int paramInt) {
        return ComponentManager.CloseComponent(paramInt);
    }

    private static short CDSequenceEnd(int paramInt) {
        return ImageCompressionManager.CDSequenceEnd(paramInt);
    }

    private static void DisposeSpriteWorld(int paramInt) {
        SpriteManager.DisposeSpriteWorld(paramInt);
    }

    private static void KillPicture(int paramInt) {
        QuickDrawManager.KillPicture(paramInt);
    }

    private static void KillPoly(int paramInt) {
        QuickDrawManager.KillPoly(paramInt);
    }

    private static void DisposeGWorld(int paramInt) {
        QuickDrawManager.DisposeGWorld(paramInt);
    }

    private static short SPBCloseDevice(int paramInt) {
        return SoundManager.SPBCloseDevice(paramInt);
    }

    private static short SndDisposeChannel(int paramInt, byte paramByte) {
        return SoundManager.SndDisposeChannel(paramInt, paramByte);
    }

    private static int NADisposeNoteChannel(int paramInt1, int paramInt2) {
        return MusicManager.NADisposeNoteChannel(paramInt1, paramInt2);
    }

    private static int SGDisposeDeviceList(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGDisposeDeviceList(paramInt1, paramInt2);
    }

    private static short QTSDisposePresentation(int paramInt1, int paramInt2) {
        return StreamingManager.QTSDisposePresentation(paramInt1, paramInt2);
    }

    private static short QTSDisposeStream(int paramInt1, int paramInt2) {
        return StreamingManager.QTSDisposeStream(paramInt1, paramInt2);
    }
}


