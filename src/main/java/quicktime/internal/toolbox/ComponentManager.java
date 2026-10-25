/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 * ComponentManager.
 * <p>
 * The Component Manager with the components of the emulated toolbox: the clock, media handlers, data handlers,
 * movie importers ('eat ') and movie exporters ('spit').
 * A ComponentDescription is 20 bytes: type, subType, manufacturer, flags, flagsMask; 0 matches anything.
 */
public final class ComponentManager {

    private ComponentManager() {
    }

    static final short noErr = 0;
    static final short paramErr = -50;
    static final short badComponentInstance = (short) 0x8001;
    static final short badComponentSelector = (short) 0x8002;
    static final short invalidComponentID = -3000;
    static final short cantFindHandler = -2003;
    static final short noExportProcAvailableErr = -2089;

    static final int clok = Bytes.fourCC("clok");
    static final int mhlr = Bytes.fourCC("mhlr");
    static final int dhlr = Bytes.fourCC("dhlr");
    static final int eat = Bytes.fourCC("eat ");
    static final int spit = Bytes.fourCC("spit");
    static final int appl = Bytes.fourCC("appl");

    // movie import / export component flags
    static final int canMovieImportHandles = 1;
    static final int canMovieImportFiles = 2;
    static final int hasMovieImportUserInterface = 4;
    static final int canMovieExportHandles = 8;
    static final int canMovieExportFiles = 16;
    static final int hasMovieExportUserInterface = 32;
    static final int movieImportSubTypeIsFileExtension = 64;
    static final int canMovieImportInPlace = 128;
    static final int dontAutoFileMovieImport = 256;
    static final int canMovieExportAuxDataHandle = 512;
    static final int canMovieImportValidateHandles = 1024;
    static final int canMovieImportValidateFile = 2048;
    static final int dontRegisterWithEasyOpen = 4096;
    static final int canMovieImportInPlaceWithIdle = 8192;
    static final int movieImportMustGetDestinationMediaType = 1 << 14;
    static final int movieExportNeedsResourceFork = 1 << 15;
    static final int canMovieImportDataReferences = 1 << 16;
    static final int movieExportMustGetSourceMediaType = 1 << 17;
    static final int canMovieImportWithIdle = 1 << 18;
    static final int canMovieImportValidateDataReferences = 1 << 19;
    static final int reservedForUseByGraphicsImporters = 1 << 23;

    /** a registered component */
    static final class ComponentRec {
        final int ref;
        final int type;
        final int subType;
        final int manufacturer;
        final int flags;
        final String name;
        final String info;
        final Function<InstanceRec, Object> factory;
        int instances;

        ComponentRec(int type, int subType, int flags, String name, String info, Function<InstanceRec, Object> factory) {
            this.ref = Heap.newObject(this, 4);
            this.type = type;
            this.subType = subType;
            this.manufacturer = appl;
            this.flags = flags;
            this.name = name;
            this.info = info;
            this.factory = factory;
        }
    }

    /** an open component instance */
    static final class InstanceRec {
        final int ref;
        final ComponentRec component;
        Object impl;

        InstanceRec(ComponentRec component) {
            this.ref = Heap.newObject(this, 4);
            this.component = component;
            this.impl = component.factory == null ? null : component.factory.apply(this);
        }
    }

    /** the settings of an importer or exporter instance */
    static final class MovieComponent {
        int progressProc;
        int progressRefCon;
        byte[] settings;
        int newMovieFlags;
        int chunkSize;
        int duration;
        int width;
        int height;
        int sampleDuration;
        int sampleDurationScale;
        byte[] sampleDescription;
        int sampleDescriptionMediaType;
        long offset;
        long limit = -1;
        int auxiliaryData;
        int auxiliaryDataType;
        byte[] mediaFile;

        MovieFiles.Progress progress(MovieRec m, int operation) {
            if (progressProc == 0 || progressProc == -1) {
                return MovieFiles.NO_PROGRESS;
            }
            return (message, op, percent) -> Closures.callInt(progressProc, m == null ? 0 : m.ref, message, operation, percent, progressRefCon);
        }
    }

    private static final List<ComponentRec> components = new ArrayList<>();

    static ComponentRec register(int type, int subType, int flags, String name, String info, Function<InstanceRec, Object> factory) {
        ComponentRec c = new ComponentRec(type, subType, flags, name, info, factory);
        components.add(c);
        return c;
    }

    static {
        register(clok, Bytes.fourCC("micr"), 0, "Microsecond Clock", "Microsecond clock", null);
        register(Bytes.fourCC("play"), 0, 0, "Movie Controller", "The standard movie controller", null);
        register(clok, Bytes.fourCC("tick"), 0, "Tick Clock", "Tick clock", null);
        String[][] media = {
                {"vide", "Apple Video Media Handler"}, {"soun", "Apple Sound Media Handler"}, {"text", "Apple Text Media Handler"},
                {"tmcd", "Apple Time Code Media Handler"}, {"sprt", "Apple Sprite Media Handler"}, {"musi", "Apple Music Media Handler"},
                {"MPEG", "Apple MPEG Media Handler"}, {"flsh", "Apple Flash Media Handler"}, {"strm", "Apple Streaming Media Handler"},
                {"qd3d", "Apple 3D Media Handler"}, {"twen", "Apple Tween Media Handler"}, {"moov", "Apple Movie Media Handler"},
                {"qtvr", "Apple QTVR Media Handler"}, {"tx3g", "Apple Timed Text Media Handler"}, {"sbtl", "Apple Subtitle Media Handler"},
                {"clcp", "Apple Closed Caption Media Handler"}, {"meta", "Apple Metadata Media Handler"}, {"hint", "Apple Hint Media Handler"},
        };
        for (String[] m : media) {
            register(mhlr, Bytes.fourCC(m[0]), 0, m[1], m[1], null);
        }
        String[][] data = {{"alis", "Apple Alias Data Handler"}, {"url ", "Apple URL Data Handler"}, {"hndl", "Apple Handle Data Handler"},
                {"rsrc", "Apple Resource Data Handler"}, {"null", "Apple Null Data Handler"}};
        for (String[] d : data) {
            register(dhlr, Bytes.fourCC(d[0]), 0, d[1], d[1], null);
        }
        int importFlags = canMovieImportFiles | canMovieImportHandles | canMovieImportDataReferences | canMovieImportInPlace | canMovieImportValidateFile;
        String[][] importers = {
                {"MooV", "QuickTime Movie"}, {"mpg4", "MPEG-4"}, {"3gpp", "3GPP"}, {"3gp2", "3GPP2"}, {"M4A ", "MPEG-4 Audio"},
                {"M4V ", "MPEG-4 Video"}, {"AIFF", "AIFF"}, {"AIFC", "AIFC"}, {"WAVE", "WAVE"}, {"JPEG", "JPEG Image"},
                {"PNGf", "PNG Image"}, {"GIFf", "GIF Image"}, {"BMPf", "BMP Image"},
                {"MP3 ", "MP3 Audio"}, {"Mp3 ", "MP3 Audio"}, {"MPEG", "MPEG"}, {"caff", "Core Audio Format"}, {"AAC ", "AAC Audio"},
        };
        for (String[] i : importers) {
            register(eat, Bytes.fourCC(i[0]), importFlags, i[1], i[1] + " importer", c -> new MovieComponent());
        }
        int exportFlags = canMovieExportFiles | canMovieExportHandles;
        String[][] exporters = {
                {"MooV", "QuickTime Movie"}, {"mpg4", "MPEG-4"}, {"M4V ", "MPEG-4 Video (iPod)"}, {"M4A ", "MPEG-4 Audio"},
                {"3gpp", "3GPP"}, {"3gp2", "3GPP2"}, {"AIFF", "AIFF"}, {"WAVE", "WAVE"}, {"TEXT", "Text"},
        };
        for (String[] e : exporters) {
            register(spit, Bytes.fourCC(e[0]), exportFlags, e[1], "Movie to " + e[1], c -> new MovieComponent());
        }
        ImageCompressionManager.register();
        GraphicsImportManager.register();
        GraphicsExportManager.register();
    }

    static ComponentRec component(int ref) {
        ComponentRec c = Heap.peerOrNull(ref, ComponentRec.class);
        if (c == null) {
            InstanceRec i = Heap.peerOrNull(ref, InstanceRec.class);
            if (i != null) {
                return i.component;
            }
        }
        return c;
    }

    static InstanceRec instance(int ref) {
        return Heap.peerOrNull(ref, InstanceRec.class);
    }

    private static boolean matches(ComponentRec c, byte[] desc) {
        int type = Bytes.getInt(desc, 0);
        int subType = Bytes.getInt(desc, 4);
        int manufacturer = Bytes.getInt(desc, 8);
        int flags = Bytes.getInt(desc, 12);
        int mask = Bytes.getInt(desc, 16);
        return (type == 0 || type == c.type) && (subType == 0 || subType == c.subType) && (manufacturer == 0 || manufacturer == c.manufacturer)
                && (c.flags & mask) == (flags & mask);
    }

    static ComponentRec find(int type, int subType) {
        for (ComponentRec c : components) {
            if (c.type == type && (subType == 0 || c.subType == subType)) {
                return c;
            }
        }
        return null;
    }

    static int open(ComponentRec c) {
        c.instances++;
        return new InstanceRec(c).ref;
    }

    // ---- Component Manager

    public static int FindNextComponent(int previous, byte[] desc) {
        boolean after = previous == 0;
        ComponentRec prev = component(previous);
        for (ComponentRec c : components) {
            if (!after) {
                after = c == prev;
                continue;
            }
            if (matches(c, desc)) {
                return c.ref;
            }
        }
        return 0;
    }

    public static int CountComponents(byte[] desc) {
        int n = 0;
        for (ComponentRec c : components) {
            if (matches(c, desc)) {
                n++;
            }
        }
        return n;
    }

    public static short GetComponentInfo(int component, byte[] desc, int nameHandle, int infoHandle, int iconHandle) {
        ComponentRec c = component(component);
        if (c == null) {
            return invalidComponentID;
        }
        if (desc != null) {
            Bytes.putInt(desc, 0, c.type);
            Bytes.putInt(desc, 4, c.subType);
            Bytes.putInt(desc, 8, c.manufacturer);
            Bytes.putInt(desc, 12, c.flags);
            Bytes.putInt(desc, 16, 0);
        }
        if (nameHandle != 0) {
            byte[] p = new byte[256];
            Bytes.putPString(p, 0, c.name, 256);
            Heap.setHandleBytes(nameHandle, java.util.Arrays.copyOf(p, (p[0] & 0xff) + 1));
        }
        if (infoHandle != 0) {
            byte[] p = new byte[256];
            Bytes.putPString(p, 0, c.info, 256);
            Heap.setHandleBytes(infoHandle, java.util.Arrays.copyOf(p, (p[0] & 0xff) + 1));
        }
        return noErr;
    }

    public static short OpenAComponent(int component, int[] ci) {
        ComponentRec c = component(component);
        if (c == null) {
            return invalidComponentID;
        }
        ci[0] = open(c);
        return noErr;
    }

    public static short OpenADefaultComponent(int componentType, int componentSubType, int[] ci) {
        ComponentRec c = find(componentType, componentSubType);
        if (c == null) {
            return cantFindHandler;
        }
        ci[0] = open(c);
        return noErr;
    }

    public static short CloseComponent(int ci) {
        InstanceRec i = instance(ci);
        if (i == null) {
            return badComponentInstance;
        }
        i.component.instances--;
        Heap.free(ci);
        return noErr;
    }

    public static int CountComponentInstances(int component) {
        ComponentRec c = component(component);
        return c == null ? 0 : c.instances;
    }

    // ---- handlers and clocks

    private static int defaultClock;

    static synchronized int defaultClock() {
        if (defaultClock == 0) {
            defaultClock = open(find(clok, Bytes.fourCC("micr")));
        }
        return defaultClock;
    }

    /** @return a media handler instance for the media */
    static int newMediaHandler(MediaRec m) {
        ComponentRec c = find(mhlr, m.type);
        if (c == null) {
            c = register(mhlr, m.type, 0, mediaHandlerName(m.type), mediaHandlerName(m.type), null);
        }
        int ci = open(c);
        instance(ci).impl = m;
        return ci;
    }

    /** @return the media of a media handler instance, null if none */
    static MediaRec mediaOfHandler(int handler) {
        InstanceRec i = instance(handler);
        return i != null && i.impl instanceof MediaRec m ? m : null;
    }

    static String mediaHandlerName(int type) {
        ComponentRec c = find(mhlr, type);
        return c != null ? c.name : "Apple " + Bytes.fourCC(type).trim() + " Media Handler";
    }

    static String dataHandlerName(int type) {
        ComponentRec c = find(dhlr, type);
        return c != null ? c.name : "Apple Data Handler";
    }

    /** @return a data handler instance */
    static int dataHandler(int type) {
        ComponentRec c = find(dhlr, type);
        return c == null ? 0 : open(c);
    }

    public static int GetDataHandler(int dataRef, int dataHandlerSubType, int flags) {
        ComponentRec c = find(dhlr, dataHandlerSubType);
        return c == null ? 0 : c.ref;
    }

    // ---- importing

    /** @return the importer subtype for the file, 0 if no Java importer reads it */
    private static int importType(String path) {
        int t = Importers.detect(path);
        return t != 0 ? t : Importers.byExtension(path);
    }

    /** @return the imported movie, null if it cannot be imported */
    static MovieRec importFile(String path, int flags) throws IOException {
        MovieRec m = Importers.importFile(path);
        if (m == null) {
            String converted = MediaEngine.get().importFile(path);
            if (converted != null) {
                m = MovieAtoms.readFile(converted);
            }
        }
        return m;
    }

    /** imports a URL, remote files are downloaded first */
    static MovieRec importURL(String url, int flags) throws IOException {
        URI uri = URI.create(url);
        if ("file".equals(uri.getScheme())) {
            return MovieToolbox.load(new File(uri).getPath(), flags);
        }
        String name = new File(uri.getPath()).getName();
        File temp = File.createTempFile("qt", name.isEmpty() ? ".mov" : "-" + name);
        temp.deleteOnExit();
        try (InputStream is = uri.toURL().openStream()) {
            Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return MovieToolbox.load(temp.getPath(), flags);
    }

    /** @return an importer instance for the data reference, 0 if none */
    static int findImporter(DataRefRec d) {
        String path = d.path();
        int type = path != null ? importType(path) : Importers.MooV;
        ComponentRec c = type != 0 ? find(eat, type) : null;
        if (c == null && path != null && MediaEngine.get().isAvailable()) {
            c = find(eat, Bytes.fourCC("MPEG"));
        }
        return c == null ? 0 : open(c);
    }

    private static MovieComponent settings(int ci) {
        InstanceRec i = instance(ci);
        return i != null && i.impl instanceof MovieComponent m ? m : null;
    }

    /** imports into an existing movie, as MovieImportFile and friends */
    private static int importInto(MovieRec src, int ci, int theMovie, int targetTrack, int[] usedTrack, int atTime, int[] addedDuration, int inFlags, int[] outFlags) {
        final int movieImportCreateTrack = 1;
        final int movieImportInParallel = 2;
        final int movieImportMustUseTrack = 4;
        final int movieImportResultUsedMultipleTracks = 8;
        MovieRec dst = MovieToolbox.movie(theMovie);
        MovieComponent s = settings(ci);
        if (s != null && s.duration > 0 && src.tracks.size() == 1 && src.tracks.getFirst().media != null && !src.tracks.getFirst().media.isSound()) {
            // still images get the requested duration
            TrackRec t = src.tracks.getFirst();
            if (t.edits.size() == 1) {
                t.edits.getFirst().duration = ClockManager.convert(s.duration, dst.timeScale, src.timeScale);
            }
        }
        long duration = src.duration();
        long at = atTime < 0 ? dst.duration() : atTime;
        int used = 0;
        if ((inFlags & movieImportMustUseTrack) != 0 && targetTrack != 0) {
            TrackRec target = MovieToolbox.track(targetTrack);
            for (TrackRec t : src.tracks) {
                if (t.media != null && target.media != null && t.media.type == target.media.type) {
                    MovieEdits.insertTrackSegment(t, target, 0, t.duration(), at);
                    used = target.ref;
                    break;
                }
            }
        } else if ((inFlags & movieImportInParallel) != 0 || (inFlags & movieImportCreateTrack) != 0) {
            for (TrackRec t : src.tracks) {
                TrackRec n = dst.newTrack();
                t.copySettings(n);
                if (at > 0) {
                    n.insertEmpty(0, at);
                }
                MovieEdits.insertTrackSegment(t, n, 0, t.duration(), at);
                used = used == 0 ? n.ref : used;
            }
        } else {
            MovieEdits.insertMovieSegment(src, dst, 0, duration, at);
            for (TrackRec t : dst.tracks) {
                used = used == 0 ? t.ref : used;
            }
        }
        dst.updateTimeBase();
        if (usedTrack != null && usedTrack.length > 0) {
            usedTrack[0] = used;
        }
        if (addedDuration != null && addedDuration.length > 0) {
            addedDuration[0] = (int) ClockManager.convert(duration, src.timeScale, dst.timeScale);
        }
        if (outFlags != null && outFlags.length > 0) {
            outFlags[0] = src.tracks.size() > 1 ? movieImportResultUsedMultipleTracks : 0;
        }
        src.dispose();
        return noErr;
    }

    public static int MovieImportFile(int ci, byte[] fsspec, int theMovie, int targetTrack, int[] usedTrack, int atTime, int[] addedDuration, int inFlags, int[] outFlags) {
        String path = FileManager.specToPath(fsspec);
        if (path == null) {
            return paramErr;
        }
        try {
            MovieRec src = importFile(path, 0);
            if (src == null) {
                return cantFindHandler;
            }
            return importInto(src, ci, theMovie, targetTrack, usedTrack, atTime, addedDuration, inFlags, outFlags);
        } catch (IOException e) {
            return MovieToolbox.ioErr;
        }
    }

    public static int MovieImportHandle(int ci, int h, int theMovie, int targetTrack, int[] usedTrack, int atTime, int[] addedDuration, int inFlags, int[] outFlags) {
        try {
            // the handle data is written to a temporary file the movie refers to
            File temp = File.createTempFile("qtimport", ".dat");
            temp.deleteOnExit();
            Files.write(temp.toPath(), Heap.handleBytes(h));
            MovieRec src = importFile(temp.getPath(), 0);
            if (src == null) {
                return cantFindHandler;
            }
            return importInto(src, ci, theMovie, targetTrack, usedTrack, atTime, addedDuration, inFlags, outFlags);
        } catch (IOException e) {
            return MovieToolbox.ioErr;
        }
    }

    public static int MovieImportDataRef(int ci, int dataRef, int dataRefType, int theMovie, int targetTrack, int[] usedTrack, int atTime, int[] addedDuration, int inFlags, int[] outFlags) {
        DataRefRec d = MovieToolbox.dataRef(dataRef, dataRefType);
        if (d == null) {
            return MovieToolbox.invalidDataRef;
        }
        try {
            MovieRec src;
            if (d.path() != null) {
                src = importFile(d.path(), 0);
            } else if (d.type == DataRefRec.url) {
                src = importURL(d.cString(), 0);
            } else if (d.type == DataRefRec.hndl) {
                return MovieImportHandle(ci, d.handle(), theMovie, targetTrack, usedTrack, atTime, addedDuration, inFlags, outFlags);
            } else {
                return MovieToolbox.couldNotResolveDataRef;
            }
            if (src == null) {
                return cantFindHandler;
            }
            return importInto(src, ci, theMovie, targetTrack, usedTrack, atTime, addedDuration, inFlags, outFlags);
        } catch (IOException e) {
            return MovieToolbox.ioErr;
        }
    }

    public static int MovieImportValidate(int ci, byte[] fsspec, int h, byte[] valid) {
        InstanceRec i = instance(ci);
        boolean ok;
        if (fsspec != null) {
            String path = FileManager.specToPath(fsspec);
            int type = path == null ? 0 : importType(path);
            ok = type != 0 && (i == null || i.component.subType == type || (i.component.subType == Importers.MooV && type == Importers.MooV));
            if (!ok && path != null && MediaEngine.get().isAvailable()) {
                ok = true;
            }
        } else {
            byte[] b = Heap.handleBytes(h);
            ok = b.length >= 8 && (Bytes.getInt(b, 4) == MovieAtoms.moov || Bytes.getInt(b, 0) == Bytes.fourCC("FORM") || Bytes.getInt(b, 0) == Bytes.fourCC("RIFF"));
        }
        if (valid != null && valid.length > 0) {
            valid[0] = (byte) (ok ? 1 : 0);
        }
        return noErr;
    }

    /** there is no user interface, the defaults are accepted */
    public static int MovieImportDoUserDialog(int ci, byte[] fsspec, int h, byte[] canceled) {
        if (canceled != null && canceled.length > 0) {
            canceled[0] = 0;
        }
        return noErr;
    }

    public static int MovieImportGetFileType(int ci, int[] fileType) {
        InstanceRec i = instance(ci);
        fileType[0] = i == null ? 0 : i.component.subType;
        return noErr;
    }

    public static int MovieImportGetAuxiliaryDataType(int ci, int[] auxType) {
        MovieComponent s = settings(ci);
        auxType[0] = s == null ? 0 : s.auxiliaryDataType;
        return noErr;
    }

    public static int MovieImportSetAuxiliaryData(int ci, int data, int handleType) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.auxiliaryData = data;
            s.auxiliaryDataType = handleType;
        }
        return noErr;
    }

    public static int MovieImportSetChunkSize(int ci, int chunkSize) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.chunkSize = chunkSize;
        }
        return noErr;
    }

    public static int MovieImportSetDimensions(int ci, int width, int height) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.width = width;
            s.height = height;
        }
        return noErr;
    }

    public static int MovieImportSetDuration(int ci, int duration) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.duration = duration;
        }
        return noErr;
    }

    public static int MovieImportSetSampleDuration(int ci, int duration, int scale) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.sampleDuration = duration;
            s.sampleDurationScale = scale;
        }
        return noErr;
    }

    public static int MovieImportSetSampleDescription(int ci, int desc, int mediaType) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.sampleDescription = desc == 0 ? null : Heap.handleBytes(desc);
            s.sampleDescriptionMediaType = mediaType;
        }
        return noErr;
    }

    public static int MovieImportGetSampleDescription(int ci, int[] desc, int[] mediaType) {
        MovieComponent s = settings(ci);
        desc[0] = s == null || s.sampleDescription == null ? 0 : Heap.newHandle(s.sampleDescription);
        mediaType[0] = s == null ? 0 : s.sampleDescriptionMediaType;
        return noErr;
    }

    public static int MovieImportSetMediaFile(int ci, int alias) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.mediaFile = alias == 0 ? null : Heap.handleBytes(alias);
        }
        return noErr;
    }

    public static int MovieImportSetOffsetAndLimit(int ci, int offset, int limit) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.offset = offset & 0xffff_ffffL;
            s.limit = limit & 0xffff_ffffL;
        }
        return noErr;
    }

    public static int MovieImportSetFromScrap(int ci, byte fromScrap) {
        return noErr;
    }

    public static int MovieImportSetNewMovieFlags(int ci, int flags) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.newMovieFlags = flags;
        }
        return noErr;
    }

    public static int MovieImportSetProgressProc(int ci, int proc, int refCon) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.progressProc = proc;
            s.progressRefCon = refCon;
        }
        return noErr;
    }

    public static int MovieImportGetSettingsAsAtomContainer(int ci, int[] settings) {
        MovieComponent s = settings(ci);
        settings[0] = s == null || s.settings == null ? AtomManager.newContainer() : Heap.newHandle(s.settings);
        return noErr;
    }

    public static int MovieImportSetSettingsFromAtomContainer(int ci, int settings) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.settings = settings == 0 ? null : Heap.handleBytes(settings);
        }
        return noErr;
    }

    /** a 'mime' atom per type: 'mime' > 'mcpy' (MIME type), 'desc', 'ext ' */
    private static int mimeList(int subType) {
        int container = AtomManager.newContainer();
        String[] mime = switch (Bytes.fourCC(subType)) {
            case "MooV" -> new String[] {"video/quicktime", "mov"};
            case "mpg4", "M4V " -> new String[] {"video/mp4", "mp4"};
            case "M4A " -> new String[] {"audio/mp4", "m4a"};
            case "3gpp" -> new String[] {"video/3gpp", "3gp"};
            case "3gp2" -> new String[] {"video/3gpp2", "3g2"};
            case "AIFF", "AIFC" -> new String[] {"audio/aiff", "aif"};
            case "WAVE" -> new String[] {"audio/wav", "wav"};
            case "JPEG" -> new String[] {"image/jpeg", "jpg"};
            case "PNGf" -> new String[] {"image/png", "png"};
            case "GIFf" -> new String[] {"image/gif", "gif"};
            case "MP3 ", "Mp3 " -> new String[] {"audio/mpeg", "mp3"};
            default -> null;
        };
        if (mime != null) {
            int[] a = new int[1];
            AtomManager.QTInsertChild(container, 0, Bytes.fourCC("mime"), 1, (short) 0, 0, 0, a);
            int mimeAtom = a[0];
            byte[] t = mime[0].getBytes(java.nio.charset.StandardCharsets.US_ASCII);
            AtomManager.QTInsertChild(container, mimeAtom, Bytes.fourCC("mcpy"), 1, (short) 0, t.length, t, null);
            mimeAtom = AtomManager.QTFindChildByIndex(container, 0, Bytes.fourCC("mime"), (short) 1, null);
            byte[] e = mime[1].getBytes(java.nio.charset.StandardCharsets.US_ASCII);
            AtomManager.QTInsertChild(container, mimeAtom, Bytes.fourCC("ext "), 1, (short) 0, e.length, e, null);
        }
        return container;
    }

    public static int MovieImportGetMIMETypeList(int ci, int[] list) {
        InstanceRec i = instance(ci);
        list[0] = mimeList(i == null ? 0 : i.component.subType);
        return noErr;
    }

    // ---- exporting

    /** exports with the exporter instance or the exporter of the file type */
    static short exportToFile(MovieRec m, TrackRec only, String path, int fileType, int userComp) {
        InstanceRec i = instance(userComp);
        int type = i != null ? i.component.subType : fileType == 0 ? Exporters.MooV : fileType;
        MovieComponent s = settings(userComp);
        try {
            MovieRec part = Exporters.part(m, only, 0, m.duration());
            int err = Exporters.export(part, path, type, s != null ? s.progress(m, 12) : MovieFiles.progressOf(m));
            return (short) err;
        } catch (IOException e) {
            return MovieToolbox.ioErr;
        }
    }

    static short exportToHandle(MovieRec part, int handleType, int h, int userComp) {
        try {
            File temp = File.createTempFile("qtexport", "." + Exporters.extension(handleType));
            try {
                int err = Exporters.export(MovieEdits.copy(part), temp.getPath(), handleType, MovieFiles.NO_PROGRESS);
                if (err == 0) {
                    Heap.setHandleBytes(h, Files.readAllBytes(temp.toPath()));
                }
                return (short) err;
            } finally {
                temp.delete();
            }
        } catch (IOException e) {
            return MovieToolbox.ioErr;
        }
    }

    public static int MovieExportToFile(int ci, byte[] fsspec, int theMovie, int onlyThisTrack, int startTime, int duration) {
        String path = FileManager.specToPath(fsspec);
        if (path == null) {
            return paramErr;
        }
        InstanceRec i = instance(ci);
        if (i == null) {
            return badComponentInstance;
        }
        MovieRec m = MovieToolbox.movie(theMovie);
        MovieComponent s = settings(ci);
        try {
            MovieRec part = Exporters.part(m, onlyThisTrack == 0 ? null : MovieToolbox.track(onlyThisTrack), startTime, duration);
            return Exporters.export(part, path, i.component.subType, s != null ? s.progress(m, 12) : MovieFiles.NO_PROGRESS);
        } catch (IOException e) {
            return MovieToolbox.ioErr;
        }
    }

    public static int MovieExportToHandle(int ci, int h, int theMovie, int onlyThisTrack, int startTime, int duration) {
        InstanceRec i = instance(ci);
        if (i == null) {
            return badComponentInstance;
        }
        MovieRec m = MovieToolbox.movie(theMovie);
        MovieRec part = Exporters.part(m, onlyThisTrack == 0 ? null : MovieToolbox.track(onlyThisTrack), startTime, duration);
        try {
            if (i.component.subType == Exporters.MooV) {
                Heap.setHandleBytes(h, MovieAtoms.write(part));
                return noErr;
            }
            return exportToHandle(part, i.component.subType, h, ci);
        } finally {
            part.dispose();
        }
    }

    public static int MovieExportToDataRef(int ci, int dataRef, int dataRefType, int theMovie, int onlyThisTrack, int startTime, int duration) {
        DataRefRec d = MovieToolbox.dataRef(dataRef, dataRefType);
        if (d == null) {
            return MovieToolbox.invalidDataRef;
        }
        if (d.type == DataRefRec.hndl) {
            return MovieExportToHandle(ci, d.handle(), theMovie, onlyThisTrack, startTime, duration);
        }
        String path = d.path();
        if (path == null) {
            return MovieToolbox.couldNotResolveDataRef;
        }
        return MovieExportToFile(ci, FileManager.pathToSpec(path), theMovie, onlyThisTrack, startTime, duration);
    }

    public static int MovieExportValidate(int ci, int theMovie, int onlyThisTrack, byte[] valid) {
        InstanceRec i = instance(ci);
        MovieRec m = MovieToolbox.movie(theMovie);
        boolean ok = true;
        if (i != null) {
            int type = i.component.subType;
            boolean sound = false, visual = false, text = false;
            for (TrackRec t : m.tracks) {
                if (onlyThisTrack != 0 && t.ref != onlyThisTrack) {
                    continue;
                }
                if (t.media != null) {
                    sound |= t.media.isSound();
                    visual |= t.media.isVisual();
                    text |= t.media.type == Bytes.fourCC("text");
                }
            }
            if (type == Exporters.AIFF || type == Exporters.WAVE || type == Exporters.M4A) {
                ok = sound;
            } else if (type == Exporters.TEXT) {
                ok = text;
            } else if (type != Exporters.MooV) {
                ok = sound || visual;
            }
        }
        if (valid != null && valid.length > 0) {
            valid[0] = (byte) (ok ? 1 : 0);
        }
        return noErr;
    }

    public static int MovieExportDoUserDialog(int ci, int theMovie, int onlyThisTrack, int startTime, int duration, byte[] canceled) {
        if (canceled != null && canceled.length > 0) {
            canceled[0] = 0;
        }
        return noErr;
    }

    public static int MovieExportGetSettingsAsAtomContainer(int ci, int[] settings) {
        MovieComponent s = settings(ci);
        settings[0] = s == null || s.settings == null ? AtomManager.newContainer() : Heap.newHandle(s.settings);
        return noErr;
    }

    public static int MovieExportSetSettingsFromAtomContainer(int ci, int settings) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.settings = settings == 0 ? null : Heap.handleBytes(settings);
        }
        return noErr;
    }

    public static int MovieExportSetProgressProc(int ci, int proc, int refCon) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.progressProc = proc;
            s.progressRefCon = refCon;
        }
        return noErr;
    }

    public static int MovieExportSetSampleDescription(int ci, int desc, int mediaType) {
        MovieComponent s = settings(ci);
        if (s != null) {
            s.sampleDescription = desc == 0 ? null : Heap.handleBytes(desc);
            s.sampleDescriptionMediaType = mediaType;
        }
        return noErr;
    }

    public static int MovieExportGetAuxiliaryData(int ci, int dataH, int[] handleType) {
        return MovieToolbox.featureUnsupported;
    }

    public static int MovieExportGetFileNameExtension(int ci, int[] extension) {
        InstanceRec i = instance(ci);
        String e = Exporters.extension(i == null ? 0 : i.component.subType);
        extension[0] = Bytes.fourCC((e + "    ").substring(0, 4));
        return noErr;
    }

    public static int MovieExportGetShortFileTypeString(int ci, byte[] typeString) {
        InstanceRec i = instance(ci);
        String e = Exporters.extension(i == null ? 0 : i.component.subType).toUpperCase(java.util.Locale.ROOT);
        Bytes.putPString(typeString, 0, e, typeString.length);
        return noErr;
    }

    public static int MovieExportGetCreatorType(int ci, int[] creator) {
        creator[0] = Bytes.fourCC("TVOD");
        return noErr;
    }

    public static int MovieExportGetSourceMediaType(int ci, int[] mediaType) {
        InstanceRec i = instance(ci);
        int type = i == null ? 0 : i.component.subType;
        mediaType[0] = type == Exporters.AIFF || type == Exporters.WAVE || type == Exporters.M4A ? Bytes.fourCC("soun") : type == Exporters.TEXT ? Bytes.fourCC("text") : 0;
        return noErr;
    }

    /** used by TimeCoder */
    public static int TCFrameNumberToTimeCode(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("TCFrameNumberToTimeCode");
    }

    /** used by QTHandle */
    public static int TCGetCurrentTimeCode(int paramInt, int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("TCGetCurrentTimeCode");
    }

    /** used by TimeCoder */
    public static int TCGetDisplayOptions(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("TCGetDisplayOptions");
    }

    /** used by QTHandle, UserData */
    public static int TCGetSourceRef(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TCGetSourceRef");
    }

    /** used by QTHandle */
    public static int TCGetTimeCodeAtTime(int paramInt1, int paramInt2, int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("TCGetTimeCodeAtTime");
    }

    /** used by TimeCoder */
    public static int TCGetTimeCodeFlags(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TCGetTimeCodeFlags");
    }

    /** used by TimeCoder */
    public static int TCSetDisplayOptions(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("TCSetDisplayOptions");
    }

    /** used by TimeCoder */
    public static int TCSetSourceRef(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("TCSetSourceRef");
    }

    /** used by TimeCoder */
    public static int TCSetTimeCodeFlags(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("TCSetTimeCodeFlags");
    }

    /** used by TimeCoder */
    public static int TCTimeCodeToFrameNumber(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TCTimeCodeToFrameNumber");
    }

    /** used by TimeCoder */
    public static int TCTimeCodeToString(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        throw new UnsupportedToolboxException("TCTimeCodeToString");
    }

    /** used by TimeCoder */
    public static int TCTimeCodeToString(int paramInt, byte[] paramArrayOfByte1, int[] paramArrayOfInt, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("TCTimeCodeToString");
    }

    /** used by XMLParser */
    public static short XMLParseAddAttribute(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("XMLParseAddAttribute");
    }

    /** used by XMLParser */
    public static short XMLParseAddAttributeAndValue(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, int[] paramArrayOfInt, int paramInt4, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("XMLParseAddAttributeAndValue");
    }

    /** used by XMLParser */
    public static short XMLParseAddAttributeValueKind(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("XMLParseAddAttributeValueKind");
    }

    /** used by XMLParser */
    public static short XMLParseAddElement(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        throw new UnsupportedToolboxException("XMLParseAddElement");
    }

    /** used by XMLParser */
    public static short XMLParseAddNameSpace(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("XMLParseAddNameSpace");
    }

    /** used by XMLParser */
    public static short XMLParseDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("XMLParseDataRef");
    }

    /** used by XMLDoc */
    public static short XMLParseDisposeXMLDoc(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseDisposeXMLDoc");
    }

    /** used by XMLParser */
    public static short XMLParseFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("XMLParseFile");
    }

    /** used by XMLParser */
    public static short XMLParseGetDetailedParseError(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("XMLParseGetDetailedParseError");
    }

    /** used by XMLParser */
    public static short XMLParseSetCharDataHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetCharDataHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetCommentHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetCommentHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetEndDocumentHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetEndDocumentHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetEndElementHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetEndElementHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("XMLParseSetOffsetAndLimit");
    }

    /** used by XMLParser */
    public static short XMLParseSetPreprocessInstructionHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetPreprocessInstructionHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetStartDocumentHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetStartDocumentHandler");
    }

    /** used by XMLParser */
    public static short XMLParseSetStartElementHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("XMLParseSetStartElementHandler");
    }

}
