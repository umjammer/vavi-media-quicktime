
package quicktime;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

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
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QTObjectManagement.class);
                    }
                });
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
        Integer localInteger = new Integer(paramInt);
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
            if ((paramQTObject instanceof quicktime.qd.Region)) {
                str1 = "Region";
            } else if ((paramQTObject instanceof Polygon)) {
                str1 = "Polygon";
            } else if ((paramQTObject instanceof quicktime.qd.Pict)) {
                str1 = "Pict";
            } else if ((paramQTObject instanceof quicktime.std.movies.AtomContainer)) {
                str1 = "AtomContainer";
            } else if ((paramQTObject instanceof ColorTable)) {
                str1 = "ColorTable";
            } else if ((paramQTObject instanceof quicktime.qd.PixMap)) {
                str1 = "PixMap";
            } else if ((paramQTObject instanceof quicktime.std.sg.SGDeviceList)) {
                str1 = "SGDeviceList";
            } else
                str1 = "QTHandleRef";
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
            str1 = str1 + "_" + Integer.toString(paramInt2);
        }

        qtObjs.put(new Integer(paramInt1), str1);
        return true;
    }

    static boolean remove(int paramInt, QTObject paramQTObject) throws QTException {
        Integer localInteger = new Integer(paramInt);
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

        Enumeration localEnumeration = qtObjs.keys();
        while (localEnumeration.hasMoreElements()) {
            Integer localObject1 = (Integer) localEnumeration.nextElement();
            String localObject2 = (String) qtObjs.get(localObject1);

            if (((String) localObject2).startsWith("SGDeviceList")) {
                try {
                    disposeSessionObject(((Integer) localObject1).intValue(), "SGDeviceList");
                } catch (QTException localQTException1) {
                    localQTException1.printStackTrace();
                }
            }
            if (((String) localObject2).startsWith("NoteChannel")) {
                try {
                    disposeSessionObject(((Integer) localObject1).intValue(), "NoteChannel");
                } catch (QTException localQTException2) {
                    localQTException2.printStackTrace();
                }
            }
            if (((String) localObject2).startsWith("SpriteWorld")) {
                try {
                    disposeSessionObject(((Integer) localObject1).intValue(), "SpriteWorld");
                } catch (QTException localQTException3) {
                    localQTException3.printStackTrace();
                }
            }
            if (((String) localObject2).startsWith("CDSequence")) {
                try {
                    disposeSessionObject(((Integer) localObject1).intValue(), "CDSequence");
                } catch (QTException localQTException4) {
                    localQTException4.printStackTrace();
                }
            }
        }
        localEnumeration = qtObjs.keys();
        Object localObject1 = new Vector();
        Object localObject2 = new Vector();
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
                    disposeSessionObject(localInteger.intValue(), str);
                } catch (QTException localQTException7) {
                    localQTException7.printStackTrace();
                }
            }
        }

        localEnumeration = ((Vector) localObject1).elements();
        while (localEnumeration.hasMoreElements()) {
            localInteger = (Integer) localEnumeration.nextElement();
            try {
                disposeSessionObject(localInteger.intValue(), "Movie");
            } catch (QTException localQTException5) {
                localQTException5.printStackTrace();
            }
        }

        localEnumeration = ((Vector) localObject2).elements();
        while (localEnumeration.hasMoreElements()) {
            localInteger = (Integer) localEnumeration.nextElement();
            try {
                disposeSessionObject(localInteger.intValue(), "QDGraphics");
            } catch (QTException localQTException6) {
                localQTException6.printStackTrace();
            }
        }

        qtObjs.clear();
        lock = false;

        qtObjs = null;
        ((Vector) localObject1).removeAllElements();
        localObject1 = null;
        ((Vector) localObject2).removeAllElements();
        localObject2 = null;
    }

    private static int pullIDFromName(String paramString) {
        int i = paramString.lastIndexOf("_");
        if (i != -1) {
            String str = paramString.substring(i + 1);
            return new Integer(str).intValue();
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
        } catch (UnsatisfiedLinkError ignored) {
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
        } catch (UnsatisfiedLinkError ignored) {
        }
    }

    private static native short QTDisposeAtomContainer(int paramInt);

    private static native void DisposeCTable(int paramInt);

    private static native void DisposeHandle(int paramInt);

    private static native void DisposeRgn(int paramInt);

    private static native short DisposeCodecNameList(int paramInt);

    private static native void DisposeMatte(int paramInt);

    private static native void DisposePtr(int paramInt);

    private static native short DisposeMovieEditState(int paramInt);

    private static native void DisposeTimeBase(int paramInt);

    private static native short DisposeTrackEditState(int paramInt);

    private static native short DisposeUserData(int paramInt);

    private static native void DisposeMovieController(int paramInt);

    private static native void DisposeMovie(int paramInt);

    private static native short CloseComponent(int paramInt);

    private static native short CDSequenceEnd(int paramInt);

    private static native void DisposeSpriteWorld(int paramInt);

    private static native void KillPicture(int paramInt);

    private static native void KillPoly(int paramInt);

    private static native void DisposeGWorld(int paramInt);

    private static native short SPBCloseDevice(int paramInt);

    private static native short SndDisposeChannel(int paramInt, byte paramByte);

    private static native int NADisposeNoteChannel(int paramInt1, int paramInt2);

    private static native int SGDisposeDeviceList(int paramInt1, int paramInt2);

    private static native short QTSDisposePresentation(int paramInt1, int paramInt2);

    private static native short QTSDisposeStream(int paramInt1, int paramInt2);
}


