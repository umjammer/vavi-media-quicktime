/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * MovieToolbox.
 * <p>
 * The Movie Toolbox on the Java movie model ({@link MovieRec}, {@link TrackRec}, {@link MediaRec}).
 * Movie files are read and written by {@link MovieAtoms}, decoding, rendering and playback of the media
 * are done by the {@link MediaEngine}.
 * Functions that return a value report their result through {@link MoviesError}.
 */
public final class MovieToolbox {

    private static final Logger logger = Logger.getLogger(MovieToolbox.class.getName());

    private MovieToolbox() {
    }

    // errors
    static final short noErr = 0;
    static final short paramErr = -50;
    static final short fnfErr = -43;
    static final short ioErr = -36;
    static final short memFullErr = -108;
    static final short couldNotResolveDataRef = -2000;
    static final short badImageDescription = -2001;
    static final short badPublicMovieAtom = -2002;
    static final short cantFindHandler = -2003;
    static final short noMediaHandler = -2006;
    static final short invalidMedia = -2008;
    static final short invalidTrack = -2009;
    static final short invalidMovie = -2010;
    static final short invalidDataRef = -2012;
    static final short invalidDuration = -2014;
    static final short invalidTime = -2015;
    static final short badEditList = -2017;
    static final short mediaTypesDontMatch = -2018;
    static final short progressProcAborted = -2019;
    static final short invalidEditState = -2023;
    static final short userDataItemNotFound = -2026;
    static final short maxSizeToGrowTooSmall = -2027;
    static final short badTrackIndex = -2028;
    static final short trackIDNotFound = -2029;
    static final short trackNotInMovie = -2030;
    static final short timeNotInTrack = -2031;
    static final short timeNotInMedia = -2032;
    static final short badEditIndex = -2033;
    static final short invalidSampleNum = -2037;
    static final short invalidSampleDescIndex = -2039;
    static final short noMovieFound = -2048;
    static final short badDataRefIndex = -2050;
    static final short noDefaultDataRef = -2051;
    static final short featureUnsupported = -2053;
    static final short movieTextNotFoundErr = -2062;

    // flags
    static final int newMovieActive = 1;
    static final int createMovieFileDeleteCurFile = 0x8000_0000;
    static final int createMovieFileDontCreateMovie = 0x4000_0000;
    static final int createMovieFileDontOpenFile = 0x2000_0000;
    static final int movieInDataForkResID = -1;
    static final int nextTimeMediaSample = 1;
    static final int nextTimeMediaEdit = 2;
    static final int nextTimeTrackEdit = 4;
    static final int nextTimeSyncSample = 8;
    static final int nextTimeStep = 16;
    static final int nextTimeEdgeOK = 16384;
    static final int movieTrackMediaType = 1;
    static final int movieTrackCharacteristic = 2;
    static final int movieTrackEnabledOnly = 4;
    static final int findTextCaseSensitive = 2;
    static final int findTextReverseSearch = 4;
    static final int findTextWrapAround = 8;
    static final int findTextUseOffset = 16;

    static final int visualMediaCharacteristic = Bytes.fourCC("eyes");
    static final int audioMediaCharacteristic = Bytes.fourCC("ears");
    static final int VideoMediaType = Bytes.fourCC("vide");
    static final int SoundMediaType = Bytes.fourCC("soun");
    static final int TextMediaType = Bytes.fourCC("text");

    // ---- peers

    static MovieRec movie(int ref) {
        return Heap.peer(ref, MovieRec.class);
    }

    static TrackRec track(int ref) {
        return Heap.peer(ref, TrackRec.class);
    }

    static MediaRec media(int ref) {
        return Heap.peer(ref, MediaRec.class);
    }

    static UserDataRec userData(int ref) {
        return Heap.peer(ref, UserDataRec.class);
    }

    public static short GetMoviesError() {
        return MoviesError.get();
    }

    private static short err(int e) {
        return MoviesError.set(e);
    }

    private static short ok() {
        return MoviesError.ok();
    }

    /** @return the error of an exception */
    private static short err(Exception e) {
        logger.log(Level.FINE, e.getMessage(), e);
        if (e instanceof java.io.FileNotFoundException) {
            return err(fnfErr);
        }
        if (e instanceof IOException) {
            return err(e.getMessage() != null && e.getMessage().equals("aborted") ? progressProcAborted : ioErr);
        }
        return err(paramErr);
    }

    // ---- movie files

    public static short OpenMovieFile(byte[] fsspec, short[] resRefNum, byte permission) {
        String path = FileManager.specToPath(fsspec);
        if (path == null) {
            return err(paramErr);
        }
        return err(FileManager.open(path, permission == 0 ? 3 : permission, true, resRefNum));
    }

    public static short CloseMovieFile(short resRefNum) {
        FileManager.OpenedFile f = FileManager.opened(resRefNum);
        if (f != null) {
            try {
                MdatWriter.close(f.path);
            } catch (IOException e) {
                return err(e);
            }
        }
        return err(FileManager.close(resRefNum));
    }

    /** @return a new movie for the file, imported if the file is not a movie file */
    static MovieRec load(String path, int flags) throws IOException {
        if (!new File(path).exists()) {
            throw new java.io.FileNotFoundException(path);
        }
        MovieRec m = MovieAtoms.readFile(path);
        if (m == null) {
            m = ComponentManager.importFile(path, flags);
        }
        if (m == null) {
            throw new IOException("no movie: " + path);
        }
        m.active = (flags & newMovieActive) != 0;
        m.defaultDataRef = DataRefRec.self(path);
        return m;
    }

    private static void setMovieInfo(short[] resId, byte[] resName, byte[] dataRefWasChanged) {
        if (resId != null && resId.length > 0) {
            resId[0] = (short) movieInDataForkResID;
        }
        if (resName != null && resName.length > 0) {
            resName[0] = 0;
        }
        if (dataRefWasChanged != null && dataRefWasChanged.length > 0) {
            dataRefWasChanged[0] = 0;
        }
    }

    public static short NewMovieFromFile(int[] movie, short resRefNum, short[] resId, byte[] resName, short flags, byte[] dataRefWasChanged) {
        FileManager.OpenedFile f = FileManager.opened(resRefNum);
        if (f == null) {
            return err(-51);
        }
        try {
            MovieRec m = load(f.path, flags);
            movie[0] = m.ref;
            setMovieInfo(resId, resName, dataRefWasChanged);
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    public static short NewMovieFromDataFork(int[] movie, short fileRefNum, int fileOffset, short flags, byte[] dataRefWasChanged) {
        FileManager.OpenedFile f = FileManager.opened(fileRefNum);
        if (f == null) {
            return err(-51);
        }
        try (RandomAccessFile raf = new RandomAccessFile(f.path, "r")) {
            raf.seek(fileOffset);
            byte[] h = new byte[8];
            raf.readFully(h);
            int size = Bytes.getInt(h, 0);
            if (Bytes.getInt(h, 4) != MovieAtoms.moov || size < 8) {
                return err(noMovieFound);
            }
            byte[] b = new byte[size];
            raf.seek(fileOffset);
            raf.readFully(b);
            MovieRec m = MovieAtoms.read(b, f.path);
            m.path = f.path;
            m.moovOffset = fileOffset;
            m.moovSize = size;
            m.active = (flags & newMovieActive) != 0;
            m.defaultDataRef = DataRefRec.self(f.path);
            movie[0] = m.ref;
            setMovieInfo(null, null, dataRefWasChanged);
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    /** @return the movie in the bytes, a 'moov' atom or a movie file image */
    private static MovieRec fromBytes(byte[] b, String selfPath) throws IOException {
        if (b.length >= 8 && Bytes.getInt(b, 4) == MovieAtoms.moov) {
            return MovieAtoms.read(b, selfPath);
        }
        // a movie file in memory
        for (MovieAtoms.Atom a : MovieAtoms.atoms(b, 0, b.length)) {
            if (a.type() == MovieAtoms.moov) {
                return MovieAtoms.read(Arrays.copyOfRange(b, a.offset(), a.end()), selfPath);
            }
        }
        throw new IOException("no movie atom");
    }

    public static short NewMovieFromHandle(int[] movie, int h, short flags, byte[] dataRefWasChanged) {
        try {
            MovieRec m = fromBytes(Heap.handleBytes(h), null);
            m.active = (flags & newMovieActive) != 0;
            movie[0] = m.ref;
            setMovieInfo(null, null, dataRefWasChanged);
            return ok();
        } catch (IOException e) {
            return err(badPublicMovieAtom);
        }
    }

    public static short NewMovieForDataRefFromHandle(int[] movie, int h, short flags, byte[] dataRefWasChanged, int dataRef, int dataRefType) {
        try {
            DataRefRec d = dataRef(dataRef, dataRefType);
            MovieRec m = fromBytes(Heap.handleBytes(h), d != null ? d.path() : null);
            m.active = (flags & newMovieActive) != 0;
            m.defaultDataRef = d;
            movie[0] = m.ref;
            setMovieInfo(null, null, dataRefWasChanged);
            return ok();
        } catch (IOException e) {
            return err(badPublicMovieAtom);
        }
    }

    /** @return the data reference of a data reference handle */
    static DataRefRec dataRef(int dataRef, int type) {
        if (dataRef == 0) {
            return null;
        }
        byte[] b = Heap.handleBytes(dataRef);
        if (type == 0) {
            type = DataRefRec.alis;
        }
        return new DataRefRec(type, 0, b);
    }

    /** @return a new handle with the data reference */
    static int newDataRefHandle(DataRefRec d) {
        if (d.type == DataRefRec.alis && (d.flags & DataRefRec.selfReference) != 0) {
            return d.selfPath != null ? Heap.newHandle(FileManager.newAliasRecord(d.selfPath)) : Heap.newHandle(0);
        }
        return Heap.newHandle(d.data);
    }

    public static short NewMovieFromDataRef(int[] movie, short flags, short[] id, int dataRef, int dataRefType) {
        DataRefRec d = dataRef(dataRef, dataRefType);
        if (d == null) {
            return err(invalidDataRef);
        }
        try {
            MovieRec m;
            String path = d.path();
            if (path != null) {
                m = load(path, flags);
            } else if (d.type == DataRefRec.hndl) {
                m = fromBytes(Heap.handleBytes(d.handle()), null);
                m.defaultDataRef = d;
            } else if (d.type == DataRefRec.url) {
                m = ComponentManager.importURL(d.cString(), flags);
            } else {
                return err(couldNotResolveDataRef);
            }
            m.active = (flags & newMovieActive) != 0;
            movie[0] = m.ref;
            if (id != null && id.length > 0) {
                id[0] = (short) movieInDataForkResID;
            }
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    public static int NewMovie(int flags) {
        MovieRec m = new MovieRec();
        m.active = (flags & newMovieActive) != 0;
        ok();
        return m.ref;
    }

    public static void DisposeMovie(int movie) {
        MovieRec m = Heap.peerOrNull(movie, MovieRec.class);
        if (m != null) {
            MediaEngine.get().dispose(m);
            m.dispose();
        }
    }

    public static short CreateMovieFile(byte[] fsspec, int creator, short scriptTag, int flags, short[] resRefNum, int[] newMovie) {
        String path = FileManager.specToPath(fsspec);
        if (path == null) {
            return err(paramErr);
        }
        File f = new File(path);
        try {
            DataRefRec.closeReader(path);
            MdatWriter.close(path);
            if (f.exists() && (flags & createMovieFileDeleteCurFile) != 0 && !f.delete()) {
                return err(ioErr);
            }
            if (!f.exists()) {
                new RandomAccessFile(f, "rw").close();
            } else if ((flags & createMovieFileDeleteCurFile) == 0) {
                return err(-48);
            }
        } catch (IOException e) {
            return err(e);
        }
        if ((flags & createMovieFileDontOpenFile) == 0 && resRefNum != null) {
            short r = FileManager.open(path, 3, true, resRefNum);
            if (r != 0) {
                return err(r);
            }
        }
        if ((flags & createMovieFileDontCreateMovie) == 0 && newMovie != null) {
            MovieRec m = new MovieRec();
            m.active = (flags & newMovieActive) != 0;
            m.defaultDataRef = DataRefRec.self(path);
            m.path = path;
            newMovie[0] = m.ref;
        }
        return ok();
    }

    public static short CreateShortcutMovieFile(byte[] fsspec, int creator, short scriptTag, int flags, int targetDataRef, int targetDataRefType) {
        String path = FileManager.specToPath(fsspec);
        DataRefRec d = dataRef(targetDataRef, targetDataRefType);
        if (path == null || d == null) {
            return err(paramErr);
        }
        try {
            String target = d.path();
            if (target == null) {
                return err(invalidDataRef);
            }
            MovieRec m = load(target, 0);
            for (TrackRec t : m.tracks) {
                if (t.media != null) {
                    for (DataRefRec r : t.media.dataRefs) {
                        if ((r.flags & DataRefRec.selfReference) != 0) {
                            r.flags = 0;
                            r.data = FileManager.newAliasRecord(target);
                        }
                    }
                }
            }
            new File(path).delete();
            m.path = null;
            m.moovOffset = -1;
            MovieFiles.writeMovieResource(m, path);
            m.dispose();
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    public static short DeleteMovieFile(byte[] fsspec) {
        String path = FileManager.specToPath(fsspec);
        if (path == null) {
            return err(paramErr);
        }
        DataRefRec.closeReader(path);
        try {
            MdatWriter.close(path);
        } catch (IOException ignored) {
        }
        File f = new File(path);
        if (!f.exists()) {
            return err(fnfErr);
        }
        return err(f.delete() ? noErr : ioErr);
    }

    public static short AddMovieResource(int movie, short resRefNum, short[] resId, byte[] resName) {
        FileManager.OpenedFile f = FileManager.opened(resRefNum);
        if (f == null) {
            return err(-51);
        }
        try {
            MovieRec m = movie(movie);
            m.moovOffset = m.path != null && m.path.equals(f.path) ? m.moovOffset : -1;
            MovieFiles.writeMovieResource(m, f.path);
            if (resId != null && resId.length > 0) {
                resId[0] = (short) movieInDataForkResID;
            }
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    public static short UpdateMovieResource(int movie, short resRefNum, short resId, byte[] resName) {
        FileManager.OpenedFile f = FileManager.opened(resRefNum);
        if (f == null) {
            return err(-51);
        }
        try {
            MovieFiles.writeMovieResource(movie(movie), f.path);
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    public static short RemoveMovieResource(short resRefNum, short resId) {
        FileManager.OpenedFile f = FileManager.opened(resRefNum);
        if (f == null) {
            return err(-51);
        }
        try (RandomAccessFile raf = new RandomAccessFile(f.path, "rw")) {
            long[] moov = MovieAtoms.findMoov(raf);
            if (moov == null) {
                return err(noMovieFound);
            }
            raf.seek(moov[0] + 4);
            raf.writeInt(MovieAtoms.free);
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    public static short PutMovieIntoHandle(int movie, int h) {
        Heap.setHandleBytes(h, MovieAtoms.write(movie(movie)));
        return ok();
    }

    public static short PutMovieForDataRefIntoHandle(int movie, int dataRef, int dataRefType, int h) {
        return PutMovieIntoHandle(movie, h);
    }

    public static short PutMovieIntoDataFork(int movie, short fileRefNum, int offset, int maxSize) {
        FileManager.OpenedFile f = FileManager.opened(fileRefNum);
        if (f == null) {
            return err(-51);
        }
        byte[] moov = MovieAtoms.write(movie(movie));
        if (maxSize > 0 && moov.length > maxSize) {
            return err(paramErr);
        }
        try (RandomAccessFile raf = new RandomAccessFile(f.path, "rw")) {
            raf.seek(offset);
            raf.write(moov);
            if (maxSize > moov.length + 8) {
                raf.writeInt(maxSize - moov.length);
                raf.writeInt(MovieAtoms.free);
            }
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    public static short PutMovieIntoTypedHandle(int movie, int targetTrack, int handleType, int publicMovie, int start, int duration, int flags, int userComp) {
        MovieRec m = movie(movie);
        MovieRec part = MovieEdits.copyRange(m, start, duration > 0 ? duration : m.duration());
        try {
            if (targetTrack != 0) {
                TrackRec keep = track(targetTrack);
                for (TrackRec t : new ArrayList<>(part.tracks)) {
                    if (t.id != keep.id) {
                        part.removeTrack(t);
                    }
                }
            }
            if (handleType == Bytes.fourCC("moov") || handleType == Bytes.fourCC("MooV") || handleType == 0) {
                Heap.setHandleBytes(publicMovie, MovieAtoms.write(part));
                return ok();
            }
            return err(ComponentManager.exportToHandle(part, handleType, publicMovie, userComp));
        } finally {
            part.dispose();
        }
    }

    public static int FlattenMovieData(int movie, int flags, byte[] fsspec, int creator, short scriptTag, int createMovieFileFlags) {
        String path = FileManager.specToPath(fsspec);
        if (path == null) {
            err(paramErr);
            return 0;
        }
        try {
            MovieRec m = movie(movie);
            MovieRec out = MovieFiles.flatten(m, path, flags, null, MovieFiles.progressOf(m));
            out.defaultDataRef = DataRefRec.self(path);
            ok();
            return out.ref;
        } catch (IOException e) {
            err(e);
            return 0;
        }
    }

    public static void FlattenMovie(int movie, int flags, byte[] fsspec, int creator, short scriptTag, int createMovieFileFlags, short[] resId, byte[] resName) {
        int m = FlattenMovieData(movie, flags, fsspec, creator, scriptTag, createMovieFileFlags);
        if (m != 0) {
            DisposeMovie(m);
            setMovieInfo(resId, resName, null);
        }
    }

    public static int FlattenMovieDataToDataRef(int movie, int flags, int dataRef, int dataRefType, int creator, short scriptTag, int createMovieFileFlags) {
        DataRefRec d = dataRef(dataRef, dataRefType);
        String path = d != null ? d.path() : null;
        if (path == null) {
            err(invalidDataRef);
            return 0;
        }
        return FlattenMovieData(movie, flags, FileManager.pathToSpec(path), creator, scriptTag, createMovieFileFlags);
    }

    public static short ConvertMovieToFile(int movie, int onlyTrack, byte[] fsspec, int fileType, int creator, short scriptTag, short[] resId, int flags, int userComp) {
        String path = FileManager.specToPath(fsspec);
        if (path == null) {
            return err(paramErr);
        }
        return err(ComponentManager.exportToFile(movie(movie), onlyTrack == 0 ? null : track(onlyTrack), path, fileType, userComp));
    }

    public static short ConvertFileToMovieFile(byte[] inputFile, byte[] outputFile, int creator, short scriptTag, short[] resId, int flags, int userComp, int proc, int refCon) {
        String in = FileManager.specToPath(inputFile);
        String out = FileManager.specToPath(outputFile);
        if (in == null || out == null) {
            return err(paramErr);
        }
        try {
            MovieRec m = load(in, 0);
            m.progressProc = proc;
            m.progressRefCon = refCon;
            MovieRec f = MovieFiles.flatten(m, out, 0, null, MovieFiles.progressOf(m));
            f.dispose();
            m.dispose();
            if (resId != null && resId.length > 0) {
                resId[0] = (short) movieInDataForkResID;
            }
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    // ---- scrap

    public static short PutMovieOnScrap(int movie, int flags) {
        MovieEdits.putOnScrap(movie(movie));
        return ok();
    }

    public static int NewMovieFromScrap(int flags) {
        MovieRec m = MovieEdits.fromScrap();
        if (m == null) {
            err(noMovieFound);
            return 0;
        }
        ok();
        return m.ref;
    }

    public static int IsScrapMovie(int targetTrack) {
        return MovieEdits.hasScrap() ? Bytes.fourCC("moov") : 0;
    }

    public static short PasteHandleIntoMovie(int h, int handleType, int movie, int flags, int userComp) {
        try {
            MovieRec src = fromBytes(Heap.handleBytes(h), null);
            MovieEdits.pasteSelection(movie(movie), src);
            src.dispose();
            return ok();
        } catch (IOException e) {
            return err(badPublicMovieAtom);
        }
    }

    // ---- playback and time

    public static void MoviesTask(int movie, int maxMilliSecToUse) {
        ClockManager.serviceAll();
        if (movie != 0) {
            MediaEngine.get().task(movie(movie));
        } else {
            for (ClockManager.TimeBaseRec tb : ClockManager.timeBases()) {
                if (tb.owner instanceof MovieRec m && m.active) {
                    MediaEngine.get().task(m);
                }
            }
        }
    }

    public static short QTGetTimeUntilNextTask(int[] duration, int scale) {
        duration[0] = Math.max(1, scale / 100);
        return 0;
    }

    public static short QTInstallNextTaskNeededSoonerCallback(int proc, int scale, int flags, int refCon) {
        return 0;
    }

    public static short QTUninstallNextTaskNeededSoonerCallback(int proc, int refCon) {
        return 0;
    }

    public static void StartMovie(int movie) {
        MovieRec m = movie(movie);
        m.updateTimeBase();
        if (m.time() >= m.duration() && m.preferredRate > 0) {
            m.timeBase.setTime(0, m.timeScale);
        }
        m.active = true;
        m.timeBase.setRate(m.preferredRate);
        MediaEngine.get().sync(m);
        ok();
    }

    public static void StopMovie(int movie) {
        MovieRec m = movie(movie);
        m.timeBase.setRate(0);
        MediaEngine.get().sync(m);
        ok();
    }

    public static void GoToBeginningOfMovie(int movie) {
        MovieRec m = movie(movie);
        m.timeBase.setTime(m.activeSegmentStart >= 0 ? m.activeSegmentStart : 0, m.timeScale);
        m.timeBase.fireTimeJump();
        MediaEngine.get().sync(m);
        ok();
    }

    public static void GoToEndOfMovie(int movie) {
        MovieRec m = movie(movie);
        m.timeBase.setTime(m.activeSegmentStart >= 0 ? m.activeSegmentStart + m.activeSegmentDuration : m.duration(), m.timeScale);
        m.timeBase.fireTimeJump();
        MediaEngine.get().sync(m);
        ok();
    }

    public static byte IsMovieDone(int movie) {
        MovieRec m = movie(movie);
        long t = m.time();
        int rate = m.timeBase.effectiveRate();
        long end = m.activeSegmentStart >= 0 ? m.activeSegmentStart + m.activeSegmentDuration : m.duration();
        long start = m.activeSegmentStart >= 0 ? m.activeSegmentStart : 0;
        ok();
        return (byte) ((rate >= 0 && t >= end) || (rate < 0 && t <= start) ? 1 : 0);
    }

    public static int GetMovieTime(int movie, int tr) {
        MovieRec m = movie(movie);
        long t = m.time();
        ClockManager.setRecord(tr, t, m.timeScale, m.timeBase.ref);
        ok();
        return (int) t;
    }

    public static void SetMovieTime(int movie, int tr) {
        MovieRec m = movie(movie);
        m.updateTimeBase();
        m.timeBase.setTime(ClockManager.recordValueIn(tr, m.timeScale), m.timeScale);
        m.timeBase.fireTimeJump();
        MediaEngine.get().sync(m);
        ok();
    }

    public static void SetMovieTimeValue(int movie, int value) {
        MovieRec m = movie(movie);
        m.updateTimeBase();
        m.timeBase.setTime(value, m.timeScale);
        m.timeBase.fireTimeJump();
        MediaEngine.get().sync(m);
        ok();
    }

    public static int GetMovieRate(int movie) {
        ok();
        return movie(movie).timeBase.rate;
    }

    public static void SetMovieRate(int movie, int rate) {
        MovieRec m = movie(movie);
        m.updateTimeBase();
        m.timeBase.setRate(rate);
        MediaEngine.get().sync(m);
        ok();
    }

    public static int GetMovieTimeBase(int movie) {
        ok();
        return movie(movie).timeBase.ref;
    }

    public static void SetMovieMasterTimeBase(int movie, int master, int slaveZero) {
        ClockManager.SetTimeBaseMasterTimeBase(movie(movie).timeBase.ref, master, slaveZero);
        ok();
    }

    public static void SetMovieMasterClock(int movie, int clock, int slaveZero) {
        ClockManager.SetTimeBaseMasterClock(movie(movie).timeBase.ref, clock, slaveZero);
        ok();
    }

    public static void ChooseMovieClock(int movie, int flags) {
        ok();
    }

    public static int GetMovieTimeScale(int movie) {
        ok();
        return movie(movie).timeScale;
    }

    public static void SetMovieTimeScale(int movie, int scale) {
        if (scale <= 0) {
            err(paramErr);
            return;
        }
        movie(movie).setTimeScale(scale);
        ok();
    }

    public static int GetMovieDuration(int movie) {
        ok();
        return (int) movie(movie).duration();
    }

    public static void GetMovieActiveSegment(int movie, int[] start, int[] duration) {
        MovieRec m = movie(movie);
        start[0] = (int) m.activeSegmentStart;
        duration[0] = (int) m.activeSegmentDuration;
        ok();
    }

    public static void SetMovieActiveSegment(int movie, int start, int duration) {
        MovieRec m = movie(movie);
        m.activeSegmentStart = start;
        m.activeSegmentDuration = start < 0 ? -1 : duration;
        m.updateTimeBase();
        ok();
    }

    public static byte GetMovieActive(int movie) {
        ok();
        return (byte) (movie(movie).active ? 1 : 0);
    }

    public static void SetMovieActive(int movie, byte active) {
        MovieRec m = movie(movie);
        m.active = active != 0;
        if (!m.active) {
            m.timeBase.setRate(0);
            MediaEngine.get().sync(m);
        }
        ok();
    }

    public static int GetMoviePreferredRate(int movie) {
        ok();
        return movie(movie).preferredRate;
    }

    public static void SetMoviePreferredRate(int movie, int rate) {
        MovieRec m = movie(movie);
        m.preferredRate = rate;
        m.changed = true;
        ok();
    }

    public static short GetMoviePreferredVolume(int movie) {
        ok();
        return movie(movie).preferredVolume;
    }

    public static void SetMoviePreferredVolume(int movie, short volume) {
        MovieRec m = movie(movie);
        m.preferredVolume = volume;
        m.changed = true;
        ok();
    }

    public static short GetMovieVolume(int movie) {
        ok();
        return movie(movie).volume;
    }

    public static void SetMovieVolume(int movie, short volume) {
        MovieRec m = movie(movie);
        m.volume = volume;
        MediaEngine.get().sync(m);
        ok();
    }

    public static short PrerollMovie(int movie, int time, int rate) {
        movie(movie).updateTimeBase();
        return ok();
    }

    public static short PrePrerollMovie(int movie, int time, int rate, int proc, int refCon) {
        movie(movie).updateTimeBase();
        if (proc != 0) {
            Closures.call(proc, movie, noErr, refCon);
        }
        return ok();
    }

    public static void AbortPrePrerollMovie(int movie, short err) {
        ok();
    }

    public static short LoadMovieIntoRam(int movie, int time, int duration, int flags) {
        return ok();
    }

    public static short LoadTrackIntoRam(int track, int time, int duration, int flags) {
        return ok();
    }

    public static short LoadMediaIntoRam(int media, int time, int duration, int flags) {
        return ok();
    }

    public static short GetMaxLoadedTimeInMovie(int movie, int[] time) {
        time[0] = (int) movie(movie).duration();
        return ok();
    }

    public static short QTMovieNeedsTimeTable(int movie, byte[] needsTimeTable) {
        needsTimeTable[0] = 0;
        return ok();
    }

    public static int GetMovieStatus(int movie, int[] firstProblemTrack) {
        if (firstProblemTrack != null && firstProblemTrack.length > 0) {
            firstProblemTrack[0] = 0;
        }
        ok();
        return 0;
    }

    public static short UpdateMovie(int movie) {
        MediaEngine.get().invalidate(movie(movie));
        return ok();
    }

    public static short InvalidateMovieRegion(int movie, int rgn) {
        MediaEngine.get().invalidate(movie(movie));
        return ok();
    }

    public static void GetMovieNextInterestingTime(int movie, short flags, short numMediaTypes, int[] whichMediaTypes, int time, int rate,
                                                   int[] interestingTime, int[] interestingDuration) {
        MovieRec m = movie(movie);
        long best = -1;
        long bestDuration = 0;
        for (TrackRec t : m.tracks) {
            if (!t.enabled || t.media == null) {
                continue;
            }
            if (numMediaTypes > 0 && whichMediaTypes != null) {
                boolean match = false;
                for (int i = 0; i < numMediaTypes && i < whichMediaTypes.length; i++) {
                    match |= mediaMatches(t.media, whichMediaTypes[i]);
                }
                if (!match) {
                    continue;
                }
            }
            long[] r = trackNextInterestingTime(t, flags, time, rate);
            if (r[0] >= 0 && (best < 0 || (rate >= 0 ? r[0] < best : r[0] > best))) {
                best = r[0];
                bestDuration = r[1];
            }
        }
        if (interestingTime != null) {
            interestingTime[0] = (int) best;
        }
        if (interestingDuration != null) {
            interestingDuration[0] = (int) bestDuration;
        }
        ok();
    }

    static boolean mediaMatches(MediaRec m, int typeOrCharacteristic) {
        if (m.type == typeOrCharacteristic) {
            return true;
        }
        if (typeOrCharacteristic == visualMediaCharacteristic) {
            return m.isVisual();
        }
        if (typeOrCharacteristic == audioMediaCharacteristic) {
            return m.isSound();
        }
        return false;
    }

    /** @return the next interesting track time and its duration, -1 if none */
    static long[] trackNextInterestingTime(TrackRec t, int flags, long time, int rate) {
        boolean forward = rate >= 0;
        boolean edgeOK = (flags & nextTimeEdgeOK) != 0;
        long best = -1;
        long bestDuration = 0;
        long s = 0;
        for (TrackRec.Edit e : t.edits) {
            long es = s;
            long ee = s + e.duration;
            s = ee;
            if ((flags & nextTimeTrackEdit) != 0 || (flags & nextTimeMediaEdit) != 0) {
                long c = forward ? es : es;
                if (forward ? (c > time || (edgeOK && c == time)) : (c < time || (edgeOK && c == time))) {
                    if (best < 0 || (forward ? c < best : c > best)) {
                        best = c;
                        bestDuration = e.duration;
                    }
                }
            }
            if (e.isEmpty() || t.media == null || (flags & (nextTimeMediaSample | nextTimeSyncSample | nextTimeStep)) == 0) {
                continue;
            }
            if (forward ? ee <= time : es > time) {
                continue;
            }
            MediaRec m = t.media;
            // the media time of the search start inside this edit
            long from = forward ? Math.max(time, es) : Math.min(time, ee - 1);
            long mt = e.mediaTime + t.mediaDuration(from - es, e.rate);
            long sample = m.sampleAt(Math.min(Math.max(mt, 0), Math.max(0, m.duration() - 1)));
            if (sample == 0) {
                continue;
            }
            for (int i = 0; i < 3 && sample > 0 && sample <= m.sampleCount(); i++) {
                if ((flags & nextTimeSyncSample) != 0) {
                    sample = forward ? (i == 0 && edgeOK ? (m.runs.get(m.runOfSample(sample)).sync ? sample : m.syncSampleAfter(sample)) : m.syncSampleAfter(sample))
                            : m.syncSampleAtOrBefore(i == 0 && edgeOK ? sample : sample - 1);
                    if (sample <= 0) {
                        break;
                    }
                }
                long smt = m.timeOfSample(sample);
                long tt = es + t.trackDuration(smt - e.mediaTime, e.rate);
                if (smt >= e.mediaTime && tt >= es && tt < ee
                        && (forward ? (tt > time || (edgeOK && tt == time)) : (tt < time || (edgeOK && tt == time)))) {
                    if (best < 0 || (forward ? tt < best : tt > best)) {
                        best = tt;
                        int r = m.runOfSample(sample);
                        bestDuration = r >= 0 ? t.trackDuration(m.runs.get(r).duration, e.rate) : 0;
                    }
                    break;
                }
                if ((flags & nextTimeSyncSample) == 0) {
                    sample += forward ? 1 : -1;
                }
            }
        }
        return new long[] {best, bestDuration};
    }

    // ---- tracks of a movie

    public static int GetMovieTrackCount(int movie) {
        ok();
        return movie(movie).tracks.size();
    }

    public static int GetMovieIndTrack(int movie, int index) {
        MovieRec m = movie(movie);
        if (index < 1 || index > m.tracks.size()) {
            err(badTrackIndex);
            return 0;
        }
        ok();
        return m.tracks.get(index - 1).ref;
    }

    public static int GetMovieIndTrackType(int movie, int index, int typeOrCharacteristic, int flags) {
        MovieRec m = movie(movie);
        int n = 0;
        for (TrackRec t : m.tracks) {
            if ((flags & movieTrackEnabledOnly) != 0 && !t.enabled) {
                continue;
            }
            if (t.media == null) {
                continue;
            }
            boolean match = (flags & movieTrackCharacteristic) != 0 ? mediaMatches(t.media, typeOrCharacteristic) : t.media.type == typeOrCharacteristic;
            if (match && ++n == index) {
                ok();
                return t.ref;
            }
        }
        err(badTrackIndex);
        return 0;
    }

    public static int GetMovieTrack(int movie, int trackID) {
        TrackRec t = movie(movie).track(trackID);
        if (t == null) {
            err(trackIDNotFound);
            return 0;
        }
        ok();
        return t.ref;
    }

    public static int NewMovieTrack(int movie, int width, int height, short volume) {
        MovieRec m = movie(movie);
        TrackRec t = m.newTrack();
        t.width = width;
        t.height = height;
        t.volume = volume;
        ok();
        return t.ref;
    }

    public static void DisposeMovieTrack(int track) {
        TrackRec t = track(track);
        MovieRec m = t.movie;
        m.removeTrack(t);
        m.updateTimeBase();
        ok();
    }

    public static short AddEmptyTrackToMovie(int srcTrack, int dstMovie, int dataRef, int dataRefType, int[] dstTrack) {
        TrackRec s = track(srcTrack);
        MovieRec m = movie(dstMovie);
        TrackRec t = m.newTrack();
        s.copySettings(t);
        if (s.media != null) {
            MediaRec md = new MediaRec(t, s.media.type, s.media.timeScale);
            md.language = s.media.language;
            md.quality = s.media.quality;
            md.handlerName = s.media.handlerName;
            md.handlerManufacturer = s.media.handlerManufacturer;
            DataRefRec d = dataRef(dataRef, dataRefType);
            if (d != null) {
                md.dataRefs.add(d);
            }
            t.media = md;
        }
        dstTrack[0] = t.ref;
        return ok();
    }

    public static int GetNextTrackForCompositing(int movie, int track) {
        return compositing(movie, track, 1);
    }

    public static int GetPrevTrackForCompositing(int movie, int track) {
        return compositing(movie, track, -1);
    }

    /** tracks are drawn from the highest layer to the lowest, the order of the movie breaks ties */
    private static int compositing(int movie, int track, int dir) {
        MovieRec m = movie(movie);
        List<TrackRec> order = new ArrayList<>(m.tracks);
        order.sort(Comparator.comparingInt((TrackRec t) -> -t.layer));
        int i = track == 0 ? (dir > 0 ? -1 : order.size()) : order.indexOf(track(track));
        int j = i + dir;
        ok();
        return j >= 0 && j < order.size() ? order.get(j).ref : 0;
    }

    public static void SelectMovieAlternates(int movie) {
        MovieRec m = movie(movie);
        java.util.Map<Integer, List<TrackRec>> groups = new java.util.HashMap<>();
        for (TrackRec t : m.tracks) {
            if (t.alternateGroup != 0) {
                groups.computeIfAbsent(t.alternateGroup, k -> new ArrayList<>()).add(t);
            }
        }
        int language = m.language;
        for (List<TrackRec> g : groups.values()) {
            TrackRec best = null;
            for (TrackRec t : g) {
                if (best == null) {
                    best = t;
                    continue;
                }
                boolean lang = t.media != null && t.media.language == language;
                boolean bestLang = best.media != null && best.media.language == language;
                int q = t.media != null ? t.media.quality : 0;
                int bq = best.media != null ? best.media.quality : 0;
                if ((lang && !bestLang) || (lang == bestLang && q > bq)) {
                    best = t;
                }
            }
            for (TrackRec t : g) {
                t.enabled = t == best;
            }
        }
        ok();
    }

    public static void SetAutoTrackAlternatesEnabled(int movie, byte enabled) {
        movie(movie).autoTrackAlternates = enabled != 0;
        ok();
    }

    public static void SetMovieLanguage(int movie, int language) {
        MovieRec m = movie(movie);
        m.language = language;
        if (m.autoTrackAlternates) {
            SelectMovieAlternates(movie);
        }
        ok();
    }

    // ---- movie properties

    public static int GetMovieCreationTime(int movie) {
        ok();
        return movie(movie).creationTime;
    }

    public static int GetMovieModificationTime(int movie) {
        ok();
        return movie(movie).modificationTime;
    }

    public static byte HasMovieChanged(int movie) {
        ok();
        return (byte) (movie(movie).changed ? 1 : 0);
    }

    public static void ClearMovieChanged(int movie) {
        movie(movie).changed = false;
        ok();
    }

    public static int GetMovieUserData(int movie) {
        ok();
        return movie(movie).userData.ref;
    }

    public static short CopyMovieUserData(int src, int dst, int copyRule) {
        return CopyUserData(movie(src).userData.ref, movie(dst).userData.ref, copyRule);
    }

    public static short CopyMovieSettings(int src, int dst) {
        MovieRec s = movie(src);
        MovieRec d = movie(dst);
        d.preferredRate = s.preferredRate;
        d.preferredVolume = s.preferredVolume;
        d.matrix = s.matrix.clone();
        d.posterTime = (int) ClockManager.convert(s.posterTime, s.timeScale, d.timeScale);
        d.previewTime = (int) ClockManager.convert(s.previewTime, s.timeScale, d.timeScale);
        d.previewDuration = (int) ClockManager.convert(s.previewDuration, s.timeScale, d.timeScale);
        d.userData.dispose();
        d.userData = s.userData.copy();
        d.userData.owned = true;
        d.changed = true;
        return ok();
    }

    public static void GetMovieMatrix(int movie, byte[] matrix) {
        MatrixManager.write(movie(movie).matrix, matrix);
        ok();
    }

    public static void SetMovieMatrix(int movie, byte[] matrix) {
        MovieRec m = movie(movie);
        m.matrix = matrix == null ? MatrixManager.identity() : MatrixManager.read(matrix);
        m.box = null;
        m.changed = true;
        MediaEngine.get().invalidate(m);
        ok();
    }

    /** writes a rect top left bottom right */
    private static void putRect(byte[] rect, double l, double t, double r, double b) {
        Bytes.putShort(rect, 0, (int) Math.round(t));
        Bytes.putShort(rect, 2, (int) Math.round(l));
        Bytes.putShort(rect, 4, (int) Math.round(b));
        Bytes.putShort(rect, 6, (int) Math.round(r));
    }

    /** @return the bounds of the visual tracks without the movie matrix */
    static double[] naturalBounds(MovieRec m) {
        int[] saved = m.matrix;
        m.matrix = MatrixManager.identity();
        try {
            return m.naturalBounds();
        } finally {
            m.matrix = saved;
        }
    }

    public static void GetMovieNaturalBoundsRect(int movie, byte[] rect) {
        double[] b = naturalBounds(movie(movie));
        putRect(rect, b[0], b[1], b[2], b[3]);
        ok();
    }

    public static void GetMovieBox(int movie, byte[] rect) {
        double[] b = movie(movie).naturalBounds();
        putRect(rect, b[0], b[1], b[2], b[3]);
        ok();
    }

    /** sets the movie matrix so that the natural bounds map to the box */
    public static void SetMovieBox(int movie, byte[] rect) {
        MovieRec m = movie(movie);
        double[] n = naturalBounds(m);
        double t = Bytes.getShort(rect, 0), l = Bytes.getShort(rect, 2), b = Bytes.getShort(rect, 4), r = Bytes.getShort(rect, 6);
        double w = n[2] - n[0];
        double h = n[3] - n[1];
        double sx = w == 0 ? 1 : (r - l) / w;
        double sy = h == 0 ? 1 : (b - t) / h;
        m.matrix = MatrixManager.fromDoubles(new double[][] {{sx, 0, 0}, {0, sy, 0}, {l - n[0] * sx, t - n[1] * sy, 1}});
        m.changed = true;
        MediaEngine.get().invalidate(m);
        ok();
    }

    private static short[] posterBox(MovieRec m) {
        if (m.box != null) {
            return m.box;
        }
        double[] b = m.naturalBounds();
        return new short[] {(short) Math.round(b[1]), (short) Math.round(b[0]), (short) Math.round(b[3]), (short) Math.round(b[2])};
    }

    public static void GetPosterBox(int movie, byte[] rect) {
        short[] b = posterBox(movie(movie));
        for (int i = 0; i < 4; i++) {
            Bytes.putShort(rect, i * 2, b[i]);
        }
        ok();
    }

    public static void SetPosterBox(int movie, byte[] rect) {
        MovieRec m = movie(movie);
        m.box = new short[] {Bytes.getShort(rect, 0), Bytes.getShort(rect, 2), Bytes.getShort(rect, 4), Bytes.getShort(rect, 6)};
        ok();
    }

    public static int GetMoviePosterTime(int movie) {
        ok();
        return movie(movie).posterTime;
    }

    public static void SetMoviePosterTime(int movie, int time) {
        MovieRec m = movie(movie);
        m.posterTime = time;
        m.changed = true;
        ok();
    }

    public static void GetMoviePreviewTime(int movie, int[] time, int[] duration) {
        MovieRec m = movie(movie);
        time[0] = m.previewTime;
        duration[0] = m.previewDuration;
        ok();
    }

    public static void SetMoviePreviewTime(int movie, int time, int duration) {
        MovieRec m = movie(movie);
        m.previewTime = time;
        m.previewDuration = duration;
        m.changed = true;
        ok();
    }

    public static byte GetMoviePreviewMode(int movie) {
        ok();
        return (byte) (movie(movie).previewMode ? 1 : 0);
    }

    public static void SetMoviePreviewMode(int movie, byte usePreview) {
        MovieRec m = movie(movie);
        m.previewMode = usePreview != 0;
        if (m.previewMode) {
            m.activeSegmentStart = m.previewTime;
            m.activeSegmentDuration = m.previewDuration;
        } else {
            m.activeSegmentStart = -1;
            m.activeSegmentDuration = -1;
        }
        m.updateTimeBase();
        ok();
    }

    public static void PlayMoviePreview(int movie, int callOutProc, int refCon) {
        MovieRec m = movie(movie);
        SetMoviePreviewMode(movie, (byte) 1);
        m.timeBase.setTime(m.previewTime, m.timeScale);
        StartMovie(movie);
        ok();
    }

    public static void ShowMoviePoster(int movie) {
        MovieRec m = movie(movie);
        m.timeBase.setTime(m.posterTime, m.timeScale);
        MediaEngine.get().invalidate(m);
        ok();
    }

    public static void GetMovieSelection(int movie, int[] time, int[] duration) {
        MovieRec m = movie(movie);
        time[0] = m.selectionTime;
        duration[0] = m.selectionDuration;
        ok();
    }

    public static void SetMovieSelection(int movie, int time, int duration) {
        MovieRec m = movie(movie);
        m.selectionTime = time;
        m.selectionDuration = duration;
        ok();
    }

    public static void SetMoviePlayHints(int movie, int flags, int flagsMask) {
        MovieRec m = movie(movie);
        m.playHints = (m.playHints & ~flagsMask) | (flags & flagsMask);
        ok();
    }

    public static void GetMovieProgressProc(int movie, int[] proc, int[] refCon) {
        MovieRec m = movie(movie);
        proc[0] = m.progressProc;
        refCon[0] = m.progressRefCon;
        ok();
    }

    public static void SetMovieProgressProc(int movie, int proc, int refCon) {
        MovieRec m = movie(movie);
        m.progressProc = proc;
        m.progressRefCon = refCon;
        ok();
    }

    public static void SetMovieDrawingCompleteProc(int movie, int flags, int proc, int refCon) {
        MovieRec m = movie(movie);
        m.drawingCompleteFlags = flags;
        m.drawingCompleteProc = proc;
        m.drawingCompleteRefCon = refCon;
        ok();
    }

    public static int GetMovieDataSize(int movie, int time, int duration) {
        MovieRec m = movie(movie);
        long size = 0;
        for (TrackRec t : m.tracks) {
            size += trackDataSize(t, time, duration);
        }
        ok();
        return (int) Math.min(Integer.MAX_VALUE, size);
    }

    static long trackDataSize(TrackRec t, long time, long duration) {
        if (t.media == null) {
            return 0;
        }
        long size = 0;
        for (TrackRec.Edit e : t.copyEdits(time, duration)) {
            if (!e.isEmpty()) {
                size += t.media.dataSize(e.mediaTime, t.mediaDuration(e.duration, e.rate));
            }
        }
        return size;
    }

    public static short GetMovieDefaultDataRef(int movie, int[] dataRef, int[] dataRefType) {
        MovieRec m = movie(movie);
        if (m.defaultDataRef == null) {
            dataRef[0] = 0;
            dataRefType[0] = 0;
            return err(noDefaultDataRef);
        }
        dataRef[0] = newDataRefHandle(m.defaultDataRef);
        dataRefType[0] = m.defaultDataRef.type;
        return ok();
    }

    public static short SetMovieDefaultDataRef(int movie, int dataRef, int dataRefType) {
        movie(movie).defaultDataRef = dataRef(dataRef, dataRefType);
        return ok();
    }

    public static short GetMovieAnchorDataRef(int movie, int[] dataRef, int[] dataRefType, int[] outFlags) {
        MovieRec m = movie(movie);
        DataRefRec d = m.anchorDataRef != null ? m.anchorDataRef : m.defaultDataRef;
        if (d == null) {
            dataRef[0] = 0;
            dataRefType[0] = 0;
        } else {
            dataRef[0] = newDataRefHandle(d);
            dataRefType[0] = d.type;
        }
        if (outFlags != null && outFlags.length > 0) {
            outFlags[0] = 0;
        }
        return ok();
    }

    public static short SetMovieAnchorDataRef(int movie, int dataRef, int dataRefType) {
        movie(movie).anchorDataRef = dataRef(dataRef, dataRefType);
        return ok();
    }

    public static short GetMovieColorTable(int movie, int[] ctab) {
        ctab[0] = movie(movie).colorTable;
        return ok();
    }

    public static short SetMovieColorTable(int movie, int ctab) {
        movie(movie).colorTable = ctab;
        return ok();
    }

    public static void ShowMovieInformation(int movie, int filterProc, int refCon) {
        throw new UnsupportedToolboxException("ShowMovieInformation", "no movie information dialog");
    }

    // ---- graphics

    public static void GetMovieGWorld(int movie, int[] port, int[] gdh) {
        MovieRec m = movie(movie);
        int[] cur = QuickDrawManager.currentGWorld();
        port[0] = m.gworld != 0 ? m.gworld : cur[0];
        gdh[0] = m.gworld != 0 ? m.gdevice : cur[1];
        ok();
    }

    public static void SetMovieGWorld(int movie, int port, int gdh) {
        MovieRec m = movie(movie);
        int[] cur = QuickDrawManager.currentGWorld();
        m.gworld = port != 0 ? port : cur[0];
        m.gdevice = gdh != 0 ? gdh : cur[1];
        MediaEngine.get().invalidate(m);
        ok();
    }

    public static void SetMovieClipRgn(int movie, int rgn) {
        movie(movie).clipRgn = rgn == 0 ? 0 : QuickDrawManager.copyRegion(rgn);
        ok();
    }

    public static int GetMovieClipRgn(int movie) {
        MovieRec m = movie(movie);
        ok();
        return m.clipRgn == 0 ? 0 : QuickDrawManager.copyRegion(m.clipRgn);
    }

    public static void SetMovieDisplayClipRgn(int movie, int rgn) {
        movie(movie).displayClipRgn = rgn == 0 ? 0 : QuickDrawManager.copyRegion(rgn);
        ok();
    }

    public static int GetMovieDisplayClipRgn(int movie) {
        MovieRec m = movie(movie);
        ok();
        return m.displayClipRgn == 0 ? 0 : QuickDrawManager.copyRegion(m.displayClipRgn);
    }

    private static int boundsRegion(double[] b) {
        return QuickDrawManager.newRectRegion((int) Math.round(b[0]), (int) Math.round(b[1]), (int) Math.round(b[2]), (int) Math.round(b[3]));
    }

    public static int GetMovieBoundsRgn(int movie) {
        ok();
        return boundsRegion(movie(movie).naturalBounds());
    }

    public static int GetMovieDisplayBoundsRgn(int movie) {
        MovieRec m = movie(movie);
        int rgn = boundsRegion(m.naturalBounds());
        if (m.displayClipRgn != 0) {
            QuickDrawManager.SectRgn(rgn, m.displayClipRgn, rgn);
        }
        ok();
        return rgn;
    }

    public static int GetMovieSegmentDisplayBoundsRgn(int movie, int time, int duration) {
        return GetMovieDisplayBoundsRgn(movie);
    }

    /** @return the bounds of the track in the movie, left top right bottom */
    static double[] trackBounds(TrackRec t, boolean withMovieMatrix) {
        double[][] m = MatrixManager.toDoubles(t.matrix);
        if (withMovieMatrix) {
            m = MatrixManager.multiply(m, MatrixManager.toDoubles(t.movie.matrix));
        }
        double w = t.width / 65536.0;
        double h = t.height / 65536.0;
        double l = Double.MAX_VALUE, tp = Double.MAX_VALUE, r = -Double.MAX_VALUE, b = -Double.MAX_VALUE;
        for (double[] p : new double[][] {{0, 0}, {w, 0}, {w, h}, {0, h}}) {
            double[] q = MatrixManager.transform(m, p[0], p[1]);
            l = Math.min(l, q[0]);
            r = Math.max(r, q[0]);
            tp = Math.min(tp, q[1]);
            b = Math.max(b, q[1]);
        }
        return new double[] {l, tp, r, b};
    }

    public static int GetTrackBoundsRgn(int track) {
        TrackRec t = track(track);
        ok();
        return QuickDrawManager.newRectRegion(0, 0, (int) Math.round(t.width / 65536.0), (int) Math.round(t.height / 65536.0));
    }

    public static int GetTrackMovieBoundsRgn(int track) {
        ok();
        return boundsRegion(trackBounds(track(track), false));
    }

    public static int GetTrackDisplayBoundsRgn(int track) {
        ok();
        return boundsRegion(trackBounds(track(track), true));
    }

    public static int GetTrackSegmentDisplayBoundsRgn(int track, int time, int duration) {
        return GetTrackDisplayBoundsRgn(track);
    }

    public static int GetTrackClipRgn(int track) {
        TrackRec t = track(track);
        ok();
        return t.clipRgn == 0 ? 0 : QuickDrawManager.copyRegion(t.clipRgn);
    }

    public static void SetTrackClipRgn(int track, int rgn) {
        track(track).clipRgn = rgn == 0 ? 0 : QuickDrawManager.copyRegion(rgn);
        ok();
    }

    public static int GetTrackMatte(int track) {
        ok();
        return track(track).matte;
    }

    public static void SetTrackMatte(int track, int matte) {
        track(track).matte = matte;
        ok();
    }

    public static void SetTrackGWorld(int track, int port, int gdh, int proc, int refCon) {
        track(track).gworld = port;
        ok();
    }

    public static byte PtInMovie(int movie, int pt) {
        double[] b = movie(movie).naturalBounds();
        int v = (short) (pt >> 16);
        int h = (short) pt;
        ok();
        return (byte) (h >= b[0] && h < b[2] && v >= b[1] && v < b[3] ? 1 : 0);
    }

    public static byte PtInTrack(int track, int pt) {
        double[] b = trackBounds(track(track), true);
        int v = (short) (pt >> 16);
        int h = (short) pt;
        ok();
        return (byte) (h >= b[0] && h < b[2] && v >= b[1] && v < b[3] ? 1 : 0);
    }

    public static int GetMoviePict(int movie, int time) {
        MovieRec m = movie(movie);
        int pict = MediaEngine.get().pict(m, time);
        if (pict == 0) {
            err(featureUnsupported);
        } else {
            ok();
        }
        return pict;
    }

    public static int GetMoviePosterPict(int movie) {
        return GetMoviePict(movie, movie(movie).posterTime);
    }

    public static int GetTrackPict(int track, int time) {
        TrackRec t = track(track);
        int pict = MediaEngine.get().pict(t, time);
        if (pict == 0) {
            err(featureUnsupported);
        } else {
            ok();
        }
        return pict;
    }

    // ---- wired actions

    public static short AddMovieExecuteWiredActionsProc(int movie, int proc, int refCon) {
        return ok();
    }

    public static short RemoveMovieExecuteWiredActionsProc(int movie, int proc, int refCon) {
        return ok();
    }

    public static short MovieExecuteWiredActions(int movie, int flags, int actions) {
        return err(featureUnsupported);
    }

    // ---- editing

    public static short InsertMovieSegment(int srcMovie, int dstMovie, int srcIn, int srcDuration, int dstIn) {
        if (srcDuration < 0 || srcIn < 0 || dstIn < 0) {
            return err(invalidDuration);
        }
        MovieEdits.insertMovieSegment(movie(srcMovie), movie(dstMovie), srcIn, srcDuration, dstIn);
        return ok();
    }

    public static short InsertEmptyMovieSegment(int movie, int dstIn, int duration) {
        if (duration < 0 || dstIn < 0) {
            return err(invalidDuration);
        }
        MovieEdits.insertEmptySegment(movie(movie), dstIn, duration);
        return ok();
    }

    public static short DeleteMovieSegment(int movie, int start, int duration) {
        if (duration < 0 || start < 0) {
            return err(invalidDuration);
        }
        MovieEdits.deleteSegment(movie(movie), start, duration);
        return ok();
    }

    public static short ScaleMovieSegment(int movie, int start, int oldDuration, int newDuration) {
        if (oldDuration <= 0 || newDuration < 0) {
            return err(invalidDuration);
        }
        MovieEdits.scaleSegment(movie(movie), start, oldDuration, newDuration);
        return ok();
    }

    public static int CutMovieSelection(int movie) {
        MovieRec c = MovieEdits.cutSelection(movie(movie));
        ok();
        return c.ref;
    }

    public static int CopyMovieSelection(int movie) {
        MovieRec c = MovieEdits.copySelection(movie(movie));
        ok();
        return c.ref;
    }

    public static void PasteMovieSelection(int movie, int src) {
        MovieEdits.pasteSelection(movie(movie), movie(src));
        ok();
    }

    public static void AddMovieSelection(int movie, int src) {
        MovieEdits.addSelection(movie(movie), movie(src));
        ok();
    }

    public static void ClearMovieSelection(int movie) {
        MovieEdits.clearSelection(movie(movie));
        ok();
    }

    public static int NewMovieEditState(int movie) {
        ok();
        return new MovieEdits.MovieEditState(movie(movie)).ref;
    }

    public static short UseMovieEditState(int movie, int state) {
        MovieEdits.MovieEditState s = Heap.peerOrNull(state, MovieEdits.MovieEditState.class);
        if (s == null) {
            return err(invalidEditState);
        }
        MovieEdits.useMovieEditState(movie(movie), s);
        return ok();
    }

    public static short DisposeMovieEditState(int state) {
        MovieEdits.MovieEditState s = Heap.peerOrNull(state, MovieEdits.MovieEditState.class);
        if (s == null) {
            return err(invalidEditState);
        }
        s.dispose();
        return ok();
    }

    public static int NewTrackEditState(int track) {
        ok();
        return new MovieEdits.TrackEditState(track(track)).ref;
    }

    public static short UseTrackEditState(int track, int state) {
        MovieEdits.TrackEditState s = Heap.peerOrNull(state, MovieEdits.TrackEditState.class);
        if (s == null) {
            return err(invalidEditState);
        }
        MovieEdits.useTrackEditState(track(track), s);
        return ok();
    }

    public static short DisposeTrackEditState(int state) {
        MovieEdits.TrackEditState s = Heap.peerOrNull(state, MovieEdits.TrackEditState.class);
        if (s == null) {
            return err(invalidEditState);
        }
        s.dispose();
        return ok();
    }

    public static short MovieSearchText(int movie, int text, int size, int flags, int[] searchTrack, int[] searchTime, int[] searchOffset) {
        MovieRec m = movie(movie);
        byte[] pattern = Heap.read(text, size);
        boolean caseSensitive = (flags & findTextCaseSensitive) != 0;
        boolean reverse = (flags & findTextReverseSearch) != 0;
        String needle = new String(pattern, Bytes.MAC_ROMAN);
        if (!caseSensitive) {
            needle = needle.toLowerCase();
        }
        List<TrackRec> texts = new ArrayList<>();
        for (TrackRec t : m.tracks) {
            if (t.media != null && (t.media.type == TextMediaType || t.media.type == Bytes.fourCC("tx3g"))) {
                texts.add(t);
            }
        }
        if (texts.isEmpty()) {
            return err(movieTextNotFoundErr);
        }
        int startTrack = searchTrack[0] == 0 ? 0 : Math.max(0, texts.indexOf(track(searchTrack[0])));
        long startTime = searchTime[0];
        int startOffset = (flags & findTextUseOffset) != 0 ? searchOffset[0] + (reverse ? -1 : 1) : (reverse ? Integer.MAX_VALUE : 0);
        boolean wrapped = false;
        for (int pass = 0; pass < 2; pass++) {
            for (int ti = startTrack; ti >= 0 && ti < texts.size(); ti += reverse ? -1 : 1) {
                TrackRec t = texts.get(ti);
                MediaRec md = t.media;
                long sample = Math.max(1, md.sampleAt(Math.max(0, t.trackTimeToMediaTime(startTime))));
                for (long s = sample; s >= 1 && s <= md.sampleCount(); s += reverse ? -1 : 1) {
                    String str = textSample(md, s);
                    if (str == null) {
                        continue;
                    }
                    String hay = caseSensitive ? str : str.toLowerCase();
                    int from = s == sample ? startOffset : (reverse ? Integer.MAX_VALUE : 0);
                    int i = reverse ? hay.lastIndexOf(needle, Math.min(from, hay.length())) : hay.indexOf(needle, Math.max(0, from));
                    if (i >= 0) {
                        long mt = md.timeOfSample(s);
                        long tt = mediaToTrackTime(t, mt);
                        searchTrack[0] = t.ref;
                        searchTime[0] = (int) Math.max(0, tt);
                        searchOffset[0] = i;
                        return ok();
                    }
                }
                startTime = reverse ? Long.MAX_VALUE / 4 : 0;
                startOffset = reverse ? Integer.MAX_VALUE : 0;
            }
            if ((flags & findTextWrapAround) == 0 || wrapped) {
                break;
            }
            wrapped = true;
            startTrack = reverse ? texts.size() - 1 : 0;
            startTime = reverse ? m.duration() : 0;
        }
        return err(movieTextNotFoundErr);
    }

    /** @return the text of a text sample: length(2) text, null if not readable */
    static String textSample(MediaRec m, long sample) {
        int r = m.runOfSample(sample);
        if (r < 0) {
            return null;
        }
        MediaRec.SampleRun run = m.runs.get(r);
        long k = sample - m.firstSampleOf(r);
        int size = (int) run.bytesOf(k, 1);
        if (size < 2) {
            return null;
        }
        try {
            byte[] b = m.dataRef(run.dataRefIndex).read(run.offsetOf(k), size);
            int len = Math.min(Bytes.getShort(b, 0) & 0xffff, size - 2);
            boolean utf16 = len >= 2 && (b[2] & 0xff) == 0xfe && (b[3] & 0xff) == 0xff;
            return utf16 ? new String(b, 4, len - 2, StandardCharsets.UTF_16BE) : new String(b, 2, len, m.type == Bytes.fourCC("tx3g") ? StandardCharsets.UTF_8 : Bytes.MAC_ROMAN);
        } catch (IOException e) {
            return null;
        }
    }

    /** @return the first track time playing the media time, -1 if none */
    static long mediaToTrackTime(TrackRec t, long mediaTime) {
        long s = 0;
        for (TrackRec.Edit e : t.edits) {
            if (!e.isEmpty()) {
                long md = t.mediaDuration(e.duration, e.rate);
                if (mediaTime >= e.mediaTime && mediaTime < e.mediaTime + md) {
                    return s + t.trackDuration(mediaTime - e.mediaTime, e.rate);
                }
            }
            s += e.duration;
        }
        return -1;
    }

    // ---- tracks

    public static int GetTrackMovie(int track) {
        ok();
        return track(track).movie.ref;
    }

    public static int GetTrackMedia(int track) {
        TrackRec t = track(track);
        ok();
        return t.media == null ? 0 : t.media.ref;
    }

    public static int GetTrackID(int track) {
        ok();
        return track(track).id;
    }

    public static int GetTrackCreationTime(int track) {
        ok();
        return track(track).creationTime;
    }

    public static int GetTrackModificationTime(int track) {
        ok();
        return track(track).modificationTime;
    }

    public static byte GetTrackEnabled(int track) {
        ok();
        return (byte) (track(track).enabled ? 1 : 0);
    }

    public static void SetTrackEnabled(int track, byte enabled) {
        TrackRec t = track(track);
        t.enabled = enabled != 0;
        t.changed();
        MediaEngine.get().invalidate(t.movie);
        ok();
    }

    public static int GetTrackUsage(int track) {
        ok();
        return track(track).usage;
    }

    public static void SetTrackUsage(int track, int usage) {
        TrackRec t = track(track);
        t.usage = usage & (TrackRec.trackUsageInMovie | TrackRec.trackUsageInPreview | TrackRec.trackUsageInPoster);
        t.changed();
        ok();
    }

    public static int GetTrackDuration(int track) {
        ok();
        return (int) track(track).duration();
    }

    public static int GetTrackOffset(int track) {
        ok();
        return (int) track(track).offset();
    }

    public static void SetTrackOffset(int track, int offset) {
        TrackRec t = track(track);
        t.setOffset(offset);
        t.movie.updateTimeBase();
        ok();
    }

    public static short GetTrackLayer(int track) {
        ok();
        return track(track).layer;
    }

    public static void SetTrackLayer(int track, short layer) {
        TrackRec t = track(track);
        t.layer = layer;
        t.changed();
        ok();
    }

    public static int GetTrackAlternate(int track) {
        TrackRec t = track(track);
        // the next track of the alternate group, the track itself if alone
        if (t.alternateGroup != 0) {
            List<TrackRec> tracks = t.movie.tracks;
            int i = tracks.indexOf(t);
            for (int k = 1; k < tracks.size(); k++) {
                TrackRec o = tracks.get((i + k) % tracks.size());
                if (o.alternateGroup == t.alternateGroup) {
                    ok();
                    return o.ref;
                }
            }
        }
        ok();
        return t.ref;
    }

    public static void SetTrackAlternate(int track, int alternate) {
        TrackRec t = track(track);
        if (alternate == 0 || alternate == track) {
            t.alternateGroup = 0;
        } else {
            TrackRec a = track(alternate);
            if (a.alternateGroup == 0) {
                int g = 0;
                for (TrackRec o : t.movie.tracks) {
                    g = Math.max(g, o.alternateGroup);
                }
                a.alternateGroup = g + 1;
            }
            t.alternateGroup = a.alternateGroup;
        }
        t.changed();
        ok();
    }

    public static short GetTrackVolume(int track) {
        ok();
        return track(track).volume;
    }

    public static void SetTrackVolume(int track, short volume) {
        TrackRec t = track(track);
        t.volume = volume;
        t.changed();
        MediaEngine.get().sync(t.movie);
        ok();
    }

    public static void GetTrackMatrix(int track, byte[] matrix) {
        MatrixManager.write(track(track).matrix, matrix);
        ok();
    }

    public static void SetTrackMatrix(int track, byte[] matrix) {
        TrackRec t = track(track);
        t.matrix = matrix == null ? MatrixManager.identity() : MatrixManager.read(matrix);
        t.changed();
        MediaEngine.get().invalidate(t.movie);
        ok();
    }

    public static short GetTrackDisplayMatrix(int track, byte[] matrix) {
        TrackRec t = track(track);
        MatrixManager.set(matrix, MatrixManager.multiply(MatrixManager.toDoubles(t.matrix), MatrixManager.toDoubles(t.movie.matrix)));
        return ok();
    }

    public static void GetTrackDimensions(int track, int[] width, int[] height) {
        TrackRec t = track(track);
        width[0] = t.width;
        height[0] = t.height;
        ok();
    }

    public static void SetTrackDimensions(int track, int width, int height) {
        TrackRec t = track(track);
        t.width = width;
        t.height = height;
        t.changed();
        MediaEngine.get().invalidate(t.movie);
        ok();
    }

    public static int GetTrackUserData(int track) {
        ok();
        return track(track).userData.ref;
    }

    public static short CopyTrackUserData(int src, int dst, int copyRule) {
        return CopyUserData(track(src).userData.ref, track(dst).userData.ref, copyRule);
    }

    public static short CopyTrackSettings(int src, int dst) {
        TrackRec s = track(src);
        TrackRec d = track(dst);
        s.copySettings(d);
        d.userData.dispose();
        d.userData = s.userData.copy();
        d.userData.owned = true;
        d.changed();
        return ok();
    }

    public static int GetTrackStatus(int track) {
        ok();
        return 0;
    }

    public static void GetTrackLoadSettings(int track, int[] preloadTime, int[] preloadDuration, int[] preloadFlags, int[] defaultHints) {
        TrackRec t = track(track);
        preloadTime[0] = t.preloadTime;
        preloadDuration[0] = t.preloadDuration;
        preloadFlags[0] = t.preloadFlags;
        defaultHints[0] = t.defaultHints;
        ok();
    }

    public static void SetTrackLoadSettings(int track, int preloadTime, int preloadDuration, int preloadFlags, int defaultHints) {
        TrackRec t = track(track);
        t.preloadTime = preloadTime;
        t.preloadDuration = preloadDuration;
        t.preloadFlags = preloadFlags;
        t.defaultHints = defaultHints;
        t.changed();
        ok();
    }

    public static short GetTrackSoundLocalizationSettings(int track, int[] settings) {
        TrackRec t = track(track);
        settings[0] = t.soundLocalization == null ? 0 : Heap.newHandle(t.soundLocalization);
        return ok();
    }

    public static short SetTrackSoundLocalizationSettings(int track, int settings) {
        track(track).soundLocalization = settings == 0 ? null : Heap.handleBytes(settings);
        return ok();
    }

    public static int GetTrackDataSize(int track, int time, int duration) {
        ok();
        return (int) Math.min(Integer.MAX_VALUE, trackDataSize(track(track), time, duration));
    }

    public static int GetTrackEditRate(int track, int time) {
        TrackRec.Edit e = track(track).editAt(time, null);
        if (e == null) {
            err(timeNotInTrack);
            return 0;
        }
        ok();
        return e.rate;
    }

    public static int TrackTimeToMediaTime(int value, int track) {
        ok();
        return (int) track(track).trackTimeToMediaTime(value);
    }

    public static void GetTrackNextInterestingTime(int track, short flags, int time, int rate, int[] interestingTime, int[] interestingDuration) {
        long[] r = trackNextInterestingTime(track(track), flags, time, rate);
        if (interestingTime != null) {
            interestingTime[0] = (int) r[0];
        }
        if (interestingDuration != null) {
            interestingDuration[0] = (int) r[1];
        }
        ok();
    }

    public static short InsertTrackSegment(int srcTrack, int dstTrack, int srcIn, int srcDuration, int dstIn) {
        if (srcDuration < 0 || srcIn < 0 || dstIn < 0) {
            return err(invalidDuration);
        }
        TrackRec s = track(srcTrack);
        TrackRec d = track(dstTrack);
        if (s.media != null && d.media != null && s.media.type != d.media.type) {
            return err(mediaTypesDontMatch);
        }
        MovieEdits.insertTrackSegment(s, d, srcIn, srcDuration, dstIn);
        d.movie.updateTimeBase();
        return ok();
    }

    public static short InsertEmptyTrackSegment(int track, int dstIn, int duration) {
        if (duration < 0 || dstIn < 0) {
            return err(invalidDuration);
        }
        TrackRec t = track(track);
        t.insertEmpty(dstIn, duration);
        t.movie.updateTimeBase();
        return ok();
    }

    public static short DeleteTrackSegment(int track, int start, int duration) {
        if (duration < 0 || start < 0) {
            return err(invalidDuration);
        }
        TrackRec t = track(track);
        t.delete(start, duration);
        t.movie.updateTimeBase();
        return ok();
    }

    public static short ScaleTrackSegment(int track, int start, int oldDuration, int newDuration) {
        if (oldDuration <= 0 || newDuration < 0) {
            return err(invalidDuration);
        }
        TrackRec t = track(track);
        t.scale(start, oldDuration, newDuration);
        t.movie.updateTimeBase();
        return ok();
    }

    public static short InsertMediaIntoTrack(int track, int trackStart, int mediaTime, int mediaDuration, int mediaRate) {
        TrackRec t = track(track);
        if (t.media == null) {
            return err(invalidMedia);
        }
        if (mediaDuration < 0 || mediaTime < -1 || (mediaTime >= 0 && mediaTime + mediaDuration > t.media.duration())) {
            return err(invalidDuration);
        }
        t.insertMedia(trackStart, mediaTime, mediaDuration, mediaRate == 0 ? 0x10000 : mediaRate);
        t.movie.updateTimeBase();
        return ok();
    }

    // track references

    public static short AddTrackReference(int track, int refTrack, int refType, int[] addedIndex) {
        TrackRec t = track(track);
        TrackRec r = track(refTrack);
        List<Integer> ids = t.references.computeIfAbsent(refType, k -> new ArrayList<>());
        int i = ids.indexOf(r.id);
        if (i < 0) {
            ids.add(r.id);
            i = ids.size() - 1;
        }
        if (addedIndex != null && addedIndex.length > 0) {
            addedIndex[0] = i + 1;
        }
        t.changed();
        return ok();
    }

    public static short DeleteTrackReference(int track, int refType, int index) {
        TrackRec t = track(track);
        List<Integer> ids = t.references.get(refType);
        if (ids == null || index < 1 || index > ids.size()) {
            return err(badTrackIndex);
        }
        ids.remove(index - 1);
        if (ids.isEmpty()) {
            t.references.remove(refType);
        }
        t.changed();
        return ok();
    }

    public static short SetTrackReference(int track, int refTrack, int refType, int index) {
        TrackRec t = track(track);
        List<Integer> ids = t.references.computeIfAbsent(refType, k -> new ArrayList<>());
        if (index < 1 || index > ids.size() + 1) {
            return err(badTrackIndex);
        }
        int id = refTrack == 0 ? 0 : track(refTrack).id;
        if (index == ids.size() + 1) {
            ids.add(id);
        } else {
            ids.set(index - 1, id);
        }
        t.changed();
        return ok();
    }

    public static int GetTrackReference(int track, int refType, int index) {
        TrackRec t = track(track);
        List<Integer> ids = t.references.get(refType);
        if (ids == null || index < 1 || index > ids.size()) {
            err(badTrackIndex);
            return 0;
        }
        TrackRec r = t.movie.track(ids.get(index - 1));
        ok();
        return r == null ? 0 : r.ref;
    }

    public static int GetTrackReferenceCount(int track, int refType) {
        List<Integer> ids = track(track).references.get(refType);
        ok();
        return ids == null ? 0 : ids.size();
    }

    public static int GetNextTrackReferenceType(int track, int refType) {
        List<Integer> types = new ArrayList<>(track(track).references.keySet());
        ok();
        if (refType == 0) {
            return types.isEmpty() ? 0 : types.getFirst();
        }
        int i = types.indexOf(refType);
        return i >= 0 && i + 1 < types.size() ? types.get(i + 1) : 0;
    }

    // ---- media

    public static int NewTrackMedia(int track, int mediaType, int timeScale, int dataRef, int dataRefType) {
        TrackRec t = track(track);
        if (timeScale <= 0) {
            err(paramErr);
            return 0;
        }
        if (t.media != null) {
            t.media.dispose();
        }
        MediaRec m = new MediaRec(t, mediaType, timeScale);
        DataRefRec d = dataRef(dataRef, dataRefType);
        if (d == null && t.movie.defaultDataRef != null) {
            d = t.movie.defaultDataRef.copy();
        }
        if (d != null) {
            m.dataRefs.add(d);
        }
        t.media = m;
        t.changed();
        ok();
        return m.ref;
    }

    public static void DisposeTrackMedia(int media) {
        MediaRec m = Heap.peerOrNull(media, MediaRec.class);
        if (m != null) {
            if (m.track != null && m.track.media == m) {
                m.track.media = null;
            }
            m.dispose();
        }
        ok();
    }

    public static int GetMediaTrack(int media) {
        MediaRec m = media(media);
        ok();
        return m.track == null ? 0 : m.track.ref;
    }

    public static int GetMediaCreationTime(int media) {
        ok();
        return media(media).creationTime;
    }

    public static int GetMediaModificationTime(int media) {
        ok();
        return media(media).modificationTime;
    }

    public static int GetMediaTimeScale(int media) {
        ok();
        return media(media).timeScale;
    }

    /** the sample durations keep their values, like on the real toolbox */
    public static void SetMediaTimeScale(int media, int scale) {
        if (scale <= 0) {
            err(paramErr);
            return;
        }
        MediaRec m = media(media);
        m.timeScale = scale;
        m.changed();
        ok();
    }

    public static int GetMediaDuration(int media) {
        ok();
        return (int) media(media).duration();
    }

    public static short GetMediaLanguage(int media) {
        ok();
        return media(media).language;
    }

    public static void SetMediaLanguage(int media, short language) {
        MediaRec m = media(media);
        m.language = language;
        m.changed();
        ok();
    }

    public static short GetMediaQuality(int media) {
        ok();
        return media(media).quality;
    }

    public static void SetMediaQuality(int media, short quality) {
        MediaRec m = media(media);
        m.quality = quality;
        m.changed();
        ok();
    }

    public static int GetMediaUserData(int media) {
        ok();
        return media(media).userData.ref;
    }

    public static short CopyMediaUserData(int src, int dst, int copyRule) {
        return CopyUserData(media(src).userData.ref, media(dst).userData.ref, copyRule);
    }

    public static void GetMediaHandlerDescription(int media, int[] mediaType, byte[] creatorName, int[] creatorManufacturer) {
        MediaRec m = media(media);
        if (mediaType != null) {
            mediaType[0] = m.type;
        }
        if (creatorName != null) {
            Bytes.putPString(creatorName, 0, m.handlerName.isEmpty() ? ComponentManager.mediaHandlerName(m.type) : m.handlerName, creatorName.length);
        }
        if (creatorManufacturer != null) {
            creatorManufacturer[0] = m.handlerManufacturer != 0 ? m.handlerManufacturer : Bytes.fourCC("appl");
        }
        ok();
    }

    public static int GetMediaHandler(int media) {
        MediaRec m = media(media);
        if (m.handler == 0) {
            m.handler = ComponentManager.newMediaHandler(m);
        }
        ok();
        return m.handler;
    }

    public static short SetMediaHandler(int media, int handler) {
        MediaRec m = media(media);
        m.handler = handler;
        return ok();
    }

    public static void GetMediaDataHandlerDescription(int media, short index, int[] dhType, byte[] creatorName, int[] creatorManufacturer) {
        MediaRec m = media(media);
        DataRefRec d = m.dataRef(index);
        if (d == null) {
            err(badDataRefIndex);
            return;
        }
        if (dhType != null) {
            dhType[0] = d.type;
        }
        if (creatorName != null) {
            Bytes.putPString(creatorName, 0, ComponentManager.dataHandlerName(d.type), creatorName.length);
        }
        if (creatorManufacturer != null) {
            creatorManufacturer[0] = Bytes.fourCC("appl");
        }
        ok();
    }

    public static int GetMediaDataHandler(int media, short index) {
        MediaRec m = media(media);
        DataRefRec d = m.dataRef(index);
        if (d == null) {
            err(badDataRefIndex);
            return 0;
        }
        ok();
        return ComponentManager.dataHandler(d.type);
    }

    public static short SetMediaDataHandler(int media, short index, int dataHandler) {
        return media(media).dataRef(index) == null ? err(badDataRefIndex) : ok();
    }

    public static short GetMediaDataRef(int media, short index, int[] dataRef, int[] dataRefType, int[] dataRefAttributes) {
        MediaRec m = media(media);
        DataRefRec d = m.dataRef(index);
        if (d == null) {
            return err(badDataRefIndex);
        }
        if (dataRef != null) {
            dataRef[0] = newDataRefHandle(d);
        }
        if (dataRefType != null) {
            dataRefType[0] = d.type;
        }
        if (dataRefAttributes != null) {
            dataRefAttributes[0] = d.attributes | ((d.flags & DataRefRec.selfReference) != 0 ? 1 : 0);
        }
        return ok();
    }

    public static short SetMediaDataRef(int media, short index, int dataRef, int dataRefType) {
        MediaRec m = media(media);
        if (index < 1 || index > m.dataRefs.size()) {
            return err(badDataRefIndex);
        }
        DataRefRec d = dataRef(dataRef, dataRefType);
        if (d == null) {
            return err(invalidDataRef);
        }
        m.dataRefs.set(index - 1, d);
        m.changed();
        return ok();
    }

    public static short SetMediaDataRefAttributes(int media, short index, int attributes) {
        DataRefRec d = media(media).dataRef(index);
        if (d == null) {
            return err(badDataRefIndex);
        }
        d.attributes = attributes;
        return ok();
    }

    public static short AddMediaDataRef(int media, short[] index, int dataRef, int dataRefType) {
        MediaRec m = media(media);
        DataRefRec d = dataRef(dataRef, dataRefType);
        if (d == null) {
            return err(invalidDataRef);
        }
        m.dataRefs.add(d);
        m.changed();
        index[0] = (short) m.dataRefs.size();
        return ok();
    }

    public static short GetMediaDataRefCount(int media, short[] count) {
        count[0] = (short) media(media).dataRefs.size();
        return ok();
    }

    public static short SetMediaDefaultDataRefIndex(int media, short index) {
        MediaRec m = media(media);
        if (index < 1 || index > m.dataRefs.size()) {
            return err(badDataRefIndex);
        }
        m.defaultDataRefIndex = index;
        return ok();
    }

    public static short GetMediaInputMap(int media, int[] inputMap) {
        MediaRec m = media(media);
        inputMap[0] = m.inputMap == null ? AtomManager.newContainer() : Heap.newHandle(m.inputMap);
        return ok();
    }

    public static short SetMediaInputMap(int media, int inputMap) {
        media(media).inputMap = inputMap == 0 ? null : Heap.handleBytes(inputMap);
        return ok();
    }

    public static short GetMediaPropertyAtom(int media, int[] propertyAtom) {
        MediaRec m = media(media);
        propertyAtom[0] = m.propertyAtom == null ? AtomManager.newContainer() : Heap.newHandle(m.propertyAtom);
        return ok();
    }

    public static short SetMediaPropertyAtom(int media, int propertyAtom) {
        media(media).propertyAtom = propertyAtom == 0 ? null : Heap.handleBytes(propertyAtom);
        return ok();
    }

    public static void GetMediaPlayHints(int media, int[] flags) {
        flags[0] = media(media).playHints;
        ok();
    }

    public static void SetMediaPlayHints(int media, int flags, int flagsMask) {
        MediaRec m = media(media);
        m.playHints = (m.playHints & ~flagsMask) | (flags & flagsMask);
        ok();
    }

    public static short GetMediaPreferredChunkSize(int media, int[] maxChunkSize) {
        maxChunkSize[0] = media(media).preferredChunkSize;
        return ok();
    }

    public static short SetMediaPreferredChunkSize(int media, int maxChunkSize) {
        media(media).preferredChunkSize = maxChunkSize;
        return ok();
    }

    public static short GetMediaShadowSync(int media, int frameDiffSampleNum, int[] syncSampleNum) {
        MediaRec m = media(media);
        syncSampleNum[0] = (int) m.syncSampleAtOrBefore(frameDiffSampleNum);
        return ok();
    }

    public static short SetMediaShadowSync(int media, int frameDiffSampleNum, int syncSampleNum) {
        return ok();
    }

    public static short BeginMediaEdits(int media) {
        media(media).editing = true;
        return ok();
    }

    public static short EndMediaEdits(int media) {
        MediaRec m = media(media);
        m.editing = false;
        for (DataRefRec d : m.dataRefs) {
            String p = d.path();
            if (p != null) {
                try {
                    MdatWriter.flush(p);
                } catch (IOException e) {
                    return err(e);
                }
            }
        }
        return ok();
    }

    public static int GetMediaSampleCount(int media) {
        ok();
        return (int) media(media).sampleCount();
    }

    public static int GetMediaSyncSampleCount(int media) {
        ok();
        return (int) media(media).syncSampleCount();
    }

    public static int GetMediaSampleDescriptionCount(int media) {
        ok();
        return media(media).descriptions.size();
    }

    public static void GetMediaSampleDescription(int media, int index, int descH) {
        MediaRec m = media(media);
        if (index < 1 || index > m.descriptions.size()) {
            err(invalidSampleDescIndex);
            return;
        }
        if (descH != 0) {
            Heap.setHandleBytes(descH, m.descriptions.get(index - 1).clone());
        }
        ok();
    }

    public static short SetMediaSampleDescription(int media, int index, int descH) {
        MediaRec m = media(media);
        if (index < 1 || index > m.descriptions.size()) {
            return err(invalidSampleDescIndex);
        }
        m.descriptions.set(index - 1, Heap.handleBytes(descH));
        m.changed();
        return ok();
    }

    public static int GetMediaDataSize(int media, int time, int duration) {
        ok();
        return (int) Math.min(Integer.MAX_VALUE, media(media).dataSize(time, duration));
    }

    public static void MediaTimeToSampleNum(int media, int time, int[] sampleNum, int[] sampleTime, int[] sampleDuration) {
        MediaRec m = media(media);
        long s = m.sampleAt(time);
        if (s == 0) {
            sampleNum[0] = 0;
            err(timeNotInMedia);
            return;
        }
        sampleNum[0] = (int) s;
        int r = m.runOfSample(s);
        if (sampleTime != null) {
            sampleTime[0] = (int) m.timeOfSample(s);
        }
        if (sampleDuration != null) {
            sampleDuration[0] = m.runs.get(r).duration;
        }
        ok();
    }

    public static void SampleNumToMediaTime(int media, int sampleNum, int[] sampleTime, int[] sampleDuration) {
        MediaRec m = media(media);
        int r = m.runOfSample(sampleNum);
        if (r < 0) {
            err(invalidSampleNum);
            return;
        }
        if (sampleTime != null) {
            sampleTime[0] = (int) m.timeOfSample(sampleNum);
        }
        if (sampleDuration != null) {
            sampleDuration[0] = m.runs.get(r).duration;
        }
        ok();
    }

    /** @return the index of the data reference new samples go to, a memory data reference is created if there is none */
    private static int writableDataRef(MediaRec m) {
        if (m.dataRefs.isEmpty()) {
            if (m.track != null && m.track.movie.defaultDataRef != null) {
                m.dataRefs.add(m.track.movie.defaultDataRef.copy());
            } else {
                m.dataRefs.add(DataRefRec.forHandle(Heap.newHandle(0)));
            }
        }
        return Math.min(Math.max(1, m.defaultDataRefIndex), m.dataRefs.size());
    }

    /** @return the description index for the sample description handle */
    private static int description(MediaRec m, int descH, int dataRefIndex) {
        if (descH == 0) {
            return m.descriptions.isEmpty() ? 0 : m.descriptions.size();
        }
        byte[] d = Heap.handleBytes(descH);
        if (d.length >= 16) {
            Bytes.putShort(d, 14, dataRefIndex);
        }
        return m.descriptionIndex(d);
    }

    /** @return the nominal 'stsz' size of each sample */
    private static int nominalSize(MediaRec m, int descIndex, long size, int count, int durationPerSample) {
        if (m.isSound() && descIndex > 0 && durationPerSample == 1 && MovieAtoms.soundBytesPerSample(m.descriptions.get(descIndex - 1)) != null) {
            return 1;
        }
        return (int) (size / Math.max(1, count));
    }

    public static short AddMediaSample(int media, int dataIn, int inOffset, int size, int durationPerSample, int descH, int numberOfSamples, short sampleFlags, int[] sampleTime) {
        MediaRec m = media(media);
        if (numberOfSamples <= 0 || size < 0) {
            return err(paramErr);
        }
        try {
            int dref = writableDataRef(m);
            int desc = description(m, descH, dref);
            if (desc == 0) {
                return err(badImageDescription);
            }
            byte[] data = Heap.read(Heap.deref(dataIn) + inOffset, size);
            long at = m.dataRef(dref).append(data, 0, size);
            long time = m.duration();
            m.addSamples(dref, at, nominalSize(m, desc, size, numberOfSamples, durationPerSample), size, durationPerSample, numberOfSamples, desc,
                    (sampleFlags & MediaRec.mediaSampleNotSync) == 0, 0);
            if (sampleTime != null && sampleTime.length > 0) {
                sampleTime[0] = (int) time;
            }
            return ok();
        } catch (IOException e) {
            return err(e);
        }
    }

    public static short AddMediaSampleReference(int media, int dataOffset, int size, int durationPerSample, int descH, int numberOfSamples, short sampleFlags, int[] sampleTime) {
        MediaRec m = media(media);
        if (numberOfSamples <= 0) {
            return err(paramErr);
        }
        int dref = writableDataRef(m);
        int desc = description(m, descH, dref);
        if (desc == 0) {
            return err(badImageDescription);
        }
        long time = m.duration();
        m.addSamples(dref, dataOffset & 0xffff_ffffL, nominalSize(m, desc, size, numberOfSamples, durationPerSample), size & 0xffff_ffffL, durationPerSample, numberOfSamples, desc,
                (sampleFlags & MediaRec.mediaSampleNotSync) == 0, 0);
        if (sampleTime != null && sampleTime.length > 0) {
            sampleTime[0] = (int) time;
        }
        return ok();
    }

    /** sample reference records are 18 bytes: dataOffset(4) dataSize(4) durationPerSample(4) numberOfSamples(4) sampleFlags(2) */
    public static short AddMediaSampleReferences(int media, int descH, int numberOfRefs, int refs, int[] sampleTime) {
        MediaRec m = media(media);
        int dref = writableDataRef(m);
        int desc = description(m, descH, dref);
        if (desc == 0) {
            return err(badImageDescription);
        }
        long time = m.duration();
        for (int i = 0; i < numberOfRefs; i++) {
            int p = refs + i * 18;
            long offset = Heap.getInt(p) & 0xffff_ffffL;
            long size = Heap.getInt(p + 4) & 0xffff_ffffL;
            int duration = Heap.getInt(p + 8);
            int count = Heap.getInt(p + 12);
            short flags = Heap.getShort(p + 16);
            m.addSamples(dref, offset, nominalSize(m, desc, size, count, duration), size, duration, count, desc, (flags & MediaRec.mediaSampleNotSync) == 0, 0);
        }
        if (sampleTime != null && sampleTime.length > 0) {
            sampleTime[0] = (int) time;
        }
        return ok();
    }

    /** @return {first sample, count} of the samples returned for a request at the media time */
    private static long[] samplesAt(MediaRec m, int time, int maxNumberOfSamples) {
        long s = m.sampleAt(time);
        if (s == 0) {
            return null;
        }
        int r = m.runOfSample(s);
        MediaRec.SampleRun run = m.runs.get(r);
        long left = run.count - (s - m.firstSampleOf(r));
        long n;
        if (maxNumberOfSamples > 0) {
            n = Math.min(left, maxNumberOfSamples);
        } else if (m.isSound()) {
            // a chunk of up to a second
            n = Math.min(left, Math.max(1, m.timeScale / Math.max(1, run.duration)));
        } else {
            n = 1;
        }
        return new long[] {s, n, r};
    }

    public static short GetMediaSample(int media, int dataOut, int maxSizeToGrow, int[] size, int time, int[] sampleTime, int[] durationPerSample,
                                       int descH, int[] descIndex, int maxNumberOfSamples, int[] numberOfSamples, short[] sampleFlags) {
        MediaRec m = media(media);
        long[] q = samplesAt(m, time, maxNumberOfSamples);
        if (q == null) {
            return err(timeNotInMedia);
        }
        MediaRec.SampleRun run = m.runs.get((int) q[2]);
        long k = q[0] - m.firstSampleOf((int) q[2]);
        int bytes = (int) run.bytesOf(k, q[1]);
        if (dataOut != 0) {
            if (maxSizeToGrow > 0 && bytes > maxSizeToGrow) {
                return err(maxSizeToGrowTooSmall);
            }
            try {
                DataRefRec d = m.dataRef(run.dataRefIndex);
                if (d == null) {
                    return err(badDataRefIndex);
                }
                Heap.setHandleBytes(dataOut, d.read(run.offsetOf(k), bytes));
            } catch (IOException e) {
                return err(e);
            }
        }
        fillSampleInfo(m, q, run, size, bytes, sampleTime, durationPerSample, descH, descIndex, numberOfSamples, sampleFlags);
        return ok();
    }

    private static void fillSampleInfo(MediaRec m, long[] q, MediaRec.SampleRun run, int[] size, int bytes, int[] sampleTime, int[] durationPerSample,
                                       int descH, int[] descIndex, int[] numberOfSamples, short[] sampleFlags) {
        if (size != null && size.length > 0) {
            size[0] = bytes;
        }
        if (sampleTime != null && sampleTime.length > 0) {
            sampleTime[0] = (int) m.timeOfSample(q[0]);
        }
        if (durationPerSample != null && durationPerSample.length > 0) {
            durationPerSample[0] = run.duration;
        }
        if (descH != 0 && run.descIndex >= 1 && run.descIndex <= m.descriptions.size()) {
            Heap.setHandleBytes(descH, m.descriptions.get(run.descIndex - 1).clone());
        }
        if (descIndex != null && descIndex.length > 0) {
            descIndex[0] = run.descIndex;
        }
        if (numberOfSamples != null && numberOfSamples.length > 0) {
            numberOfSamples[0] = (int) q[1];
        }
        if (sampleFlags != null && sampleFlags.length > 0) {
            sampleFlags[0] = (short) (run.sync ? 0 : MediaRec.mediaSampleNotSync);
        }
    }

    public static short GetMediaSampleReference(int media, int[] dataOffset, int[] size, int time, int[] sampleTime, int[] durationPerSample,
                                                int descH, int[] descIndex, int maxNumberOfSamples, int[] numberOfSamples, short[] sampleFlags) {
        MediaRec m = media(media);
        long[] q = samplesAt(m, time, maxNumberOfSamples);
        if (q == null) {
            return err(timeNotInMedia);
        }
        MediaRec.SampleRun run = m.runs.get((int) q[2]);
        long k = q[0] - m.firstSampleOf((int) q[2]);
        if (dataOffset != null && dataOffset.length > 0) {
            dataOffset[0] = (int) run.offsetOf(k);
        }
        fillSampleInfo(m, q, run, size, (int) run.bytesOf(k, q[1]), sampleTime, durationPerSample, descH, descIndex, numberOfSamples, sampleFlags);
        return ok();
    }

    public static short GetMediaSampleReferences(int media, int time, int[] sampleTime, int descH, int[] descIndex, int maxNumberOfEntries, int[] actualNumberOfEntries, int refs) {
        MediaRec m = media(media);
        long s = m.sampleAt(time);
        if (s == 0) {
            return err(timeNotInMedia);
        }
        int r = m.runOfSample(s);
        MediaRec.SampleRun first = m.runs.get(r);
        if (sampleTime != null && sampleTime.length > 0) {
            sampleTime[0] = (int) m.timeOfSample(s);
        }
        if (descH != 0) {
            Heap.setHandleBytes(descH, m.descriptions.get(first.descIndex - 1).clone());
        }
        if (descIndex != null && descIndex.length > 0) {
            descIndex[0] = first.descIndex;
        }
        int n = 0;
        long k = s - m.firstSampleOf(r);
        while (r < m.runs.size() && n < maxNumberOfEntries && m.runs.get(r).descIndex == first.descIndex) {
            MediaRec.SampleRun run = m.runs.get(r);
            if (refs != 0) {
                int p = refs + n * 18;
                Heap.putInt(p, (int) run.offsetOf(k));
                Heap.putInt(p + 4, (int) run.bytesOf(k, run.count - k));
                Heap.putInt(p + 8, run.duration);
                Heap.putInt(p + 12, (int) (run.count - k));
                Heap.putShort(p + 16, (short) (run.sync ? 0 : MediaRec.mediaSampleNotSync));
            }
            n++;
            r++;
            k = 0;
        }
        if (actualNumberOfEntries != null && actualNumberOfEntries.length > 0) {
            actualNumberOfEntries[0] = n;
        }
        return ok();
    }

    public static void GetMediaNextInterestingTime(int media, short flags, int time, int rate, int[] interestingTime, int[] interestingDuration) {
        MediaRec m = media(media);
        boolean forward = rate >= 0;
        boolean edgeOK = (flags & nextTimeEdgeOK) != 0;
        long t = -1;
        long d = 0;
        long s = m.sampleAt(Math.max(0, Math.min(time, (int) Math.max(0, m.duration() - 1))));
        if (s > 0) {
            if ((flags & nextTimeSyncSample) != 0) {
                long cur = m.timeOfSample(s);
                boolean curSync = m.runs.get(m.runOfSample(s)).sync;
                if (edgeOK && curSync && cur == time) {
                    t = cur;
                } else {
                    long n = forward ? m.syncSampleAfter(s) : m.syncSampleAtOrBefore(cur == time ? s - 1 : s);
                    t = n > 0 ? m.timeOfSample(n) : -1;
                    s = n;
                }
            } else {
                long cur = m.timeOfSample(s);
                if (edgeOK && cur == time) {
                    t = cur;
                } else if (forward) {
                    t = s < m.sampleCount() ? m.timeOfSample(s + 1) : -1;
                    s++;
                } else {
                    t = cur < time ? cur : (s > 1 ? m.timeOfSample(s - 1) : -1);
                    s = cur < time ? s : s - 1;
                }
            }
            if (t >= 0 && s > 0) {
                int r = m.runOfSample(s);
                d = r >= 0 ? m.runs.get(r).duration : 0;
            }
        }
        if (interestingTime != null) {
            interestingTime[0] = (int) t;
        }
        if (interestingDuration != null) {
            interestingDuration[0] = (int) d;
        }
        ok();
    }

    // ---- user data

    public static short NewUserData(int[] userData) {
        userData[0] = new UserDataRec().ref;
        return ok();
    }

    public static short DisposeUserData(int userData) {
        UserDataRec u = Heap.peerOrNull(userData, UserDataRec.class);
        if (u != null && !u.owned) {
            u.dispose();
        }
        return ok();
    }

    public static short NewUserDataFromHandle(int h, int[] userData) {
        byte[] b = Heap.handleBytes(h);
        userData[0] = UserDataRec.fromBytes(b, 0, b.length).ref;
        return ok();
    }

    public static short PutUserDataIntoHandle(int userData, int h) {
        Heap.setHandleBytes(h, userData(userData).toBytes());
        return ok();
    }

    public static short GetUserData(int userData, int h, int type, int index) {
        UserDataRec.Item i = userData(userData).item(type, index);
        if (i == null) {
            return err(userDataItemNotFound);
        }
        if (h != 0) {
            Heap.setHandleBytes(h, i.data.clone());
        }
        return ok();
    }

    public static short AddUserData(int userData, int h, int type) {
        userData(userData).add(type, Heap.handleBytes(h));
        return ok();
    }

    public static short RemoveUserData(int userData, int type, int index) {
        return userData(userData).remove(type, index) ? ok() : err(userDataItemNotFound);
    }

    public static short CountUserDataType(int userData, int type) {
        ok();
        return (short) userData(userData).count(type);
    }

    public static int GetNextUserDataType(int userData, int type) {
        ok();
        return userData(userData).nextType(type);
    }

    public static short GetUserDataItem(int userData, int data, int size, int type, int index) {
        UserDataRec.Item i = userData(userData).item(type, index);
        if (i == null) {
            return err(userDataItemNotFound);
        }
        if (i.data.length != size) {
            return err(paramErr);
        }
        Heap.write(data, i.data, 0, size);
        return ok();
    }

    public static short SetUserDataItem(int userData, int data, int size, int type, int index) {
        UserDataRec u = userData(userData);
        byte[] b = Heap.read(data, size);
        UserDataRec.Item i = u.item(type, index == 0 ? 1 : index);
        if (i == null) {
            u.add(type, b);
        } else {
            i.data = b;
        }
        return ok();
    }

    public static short AddUserDataText(int userData, int h, int type, int index, short language) {
        UserDataRec u = userData(userData);
        UserDataRec.Item i = u.item(type, index);
        if (i == null) {
            u.add(type, new byte[0]);
            i = u.item(type, u.count(type));
        }
        UserDataRec.setText(i, language, Heap.handleBytes(h));
        return ok();
    }

    public static short GetUserDataText(int userData, int h, int type, int index, short language) {
        UserDataRec.Item i = userData(userData).item(type, index);
        if (i == null) {
            return err(userDataItemNotFound);
        }
        byte[] text = UserDataRec.text(i, language);
        if (text == null) {
            return err(userDataItemNotFound);
        }
        Heap.setHandleBytes(h, text);
        return ok();
    }

    public static short RemoveUserDataText(int userData, int type, int index, short language) {
        UserDataRec u = userData(userData);
        UserDataRec.Item i = u.item(type, index);
        if (i == null || !UserDataRec.removeText(i, language)) {
            return err(userDataItemNotFound);
        }
        if (i.data.length == 0) {
            u.items.remove(i);
        }
        return ok();
    }

    /** copy rule 0: add items, 1: replace items of the same type */
    public static short CopyUserData(int src, int dst, int copyRule) {
        UserDataRec s = userData(src);
        UserDataRec d = userData(dst);
        if (copyRule == 1) {
            for (UserDataRec.Item i : s.items) {
                d.items.removeIf(x -> x.type == i.type);
            }
        }
        for (UserDataRec.Item i : s.items) {
            d.add(i.type, i.data.clone());
        }
        return ok();
    }

    // ---- importers

    public static short GetMovieImporterForDataRef(int dataRefType, int dataRef, int flags, int[] importer) {
        DataRefRec d = dataRef(dataRef, dataRefType);
        importer[0] = d == null ? 0 : ComponentManager.findImporter(d);
        return importer[0] == 0 ? err(cantFindHandler) : ok();
    }
}
