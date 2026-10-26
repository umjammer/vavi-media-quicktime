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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * FileManager.
 * <p>
 * File specifications, file reference numbers and alias records of the classic File Manager and Alias Manager.
 * <ul>
 * <li>An FSSpec is a 262 byte record: vRefNum(2), parID(4), name(Str255). The emulated volume {@link #kVRefNum}
 * keeps full paths in a registry indexed by parID, the name holds the file name for display.</li>
 * <li>An alias record is written as a version 2 alias with the POSIX path extra tags (18, 19),
 * which real QuickTime and AVFoundation resolve.</li>
 * </ul>
 */
public final class FileManager {

    private FileManager() {
    }

    static final short noErr = 0;
    static final short paramErr = -50;
    static final short fnfErr = -43;
    static final short ioErr = -36;
    static final short rfNumErr = -51;
    static final short wrPermErr = -61;
    static final short dupFNErr = -48;

    /** FSSpec record size */
    public static final int FSSPEC_SIZE = 262;

    /** the emulated volume reference number */
    static final short kVRefNum = (short) 0x8001;

    private static final Map<Integer, String> paths = new HashMap<>();

    private static final Map<String, Integer> pathIds = new HashMap<>();

    /** @return an FSSpec for the absolute path */
    public static synchronized byte[] pathToSpec(String path) {
        String p = new File(path).getAbsolutePath();
        Integer id = pathIds.get(p);
        if (id == null) {
            id = paths.size() + 1;
            paths.put(id, p);
            pathIds.put(p, id);
        }
        byte[] spec = new byte[FSSPEC_SIZE];
        Bytes.putShort(spec, 0, kVRefNum);
        Bytes.putInt(spec, 2, id);
        Bytes.putPString(spec, 6, new File(p).getName(), 256);
        return spec;
    }

    /** @return the path of an FSSpec, null if unknown */
    public static synchronized String specToPath(byte[] spec) {
        if (spec == null || spec.length < 7) {
            return null;
        }
        if (Bytes.getShort(spec, 0) == kVRefNum) {
            return paths.get(Bytes.getInt(spec, 2));
        }
        // a hand made spec, the name is a (colon or slash separated) path
        String name = Bytes.getPString(spec, 6);
        return name.isEmpty() ? null : name.indexOf('/') >= 0 ? name : name.replace(':', File.separatorChar);
    }

    /** @return the path of an FSSpec, throws if unknown */
    static String path(byte[] spec) throws IOException {
        String path = specToPath(spec);
        if (path == null) {
            throw new IOException("invalid FSSpec");
        }
        return path;
    }

    // ---- file reference numbers

    /** an open file */
    static final class OpenedFile {
        final String path;
        final boolean writable;
        final boolean movieFile;
        RandomAccessFile file;

        OpenedFile(String path, boolean writable, boolean movieFile) {
            this.path = path;
            this.writable = writable;
            this.movieFile = movieFile;
        }

        synchronized RandomAccessFile file() throws IOException {
            if (file == null) {
                file = new RandomAccessFile(path, writable ? "rw" : "r");
            }
            return file;
        }

        synchronized void close() throws IOException {
            if (file != null) {
                file.close();
                file = null;
            }
        }
    }

    private static final Map<Short, OpenedFile> openedFiles = new ConcurrentHashMap<>();

    private static short nextRefNum = 1000;

    /** opens a file, permission 1: read, 2: write, 3: read write */
    static synchronized short open(String path, int permission, boolean movieFile, short[] refNum) {
        File f = new File(path);
        boolean writable = (permission & 2) != 0;
        if (!f.exists() && !writable) {
            return fnfErr;
        }
        if (writable && f.exists() && !f.canWrite()) {
            return wrPermErr;
        }
        short ref = nextRefNum++;
        if (nextRefNum > 32000) {
            nextRefNum = 1000;
        }
        openedFiles.put(ref, new OpenedFile(f.getAbsolutePath(), writable, movieFile));
        refNum[0] = ref;
        return noErr;
    }

    static short close(short refNum) {
        OpenedFile f = openedFiles.remove(refNum);
        if (f == null) {
            return rfNumErr;
        }
        try {
            f.close();
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    /** @return the open file for the reference number, or null */
    static OpenedFile opened(short refNum) {
        return openedFiles.get(refNum);
    }

    /** @return the whole data fork of an open file */
    static byte[] readFork(short refNum) throws IOException {
        OpenedFile f = opened(refNum);
        if (f == null) {
            throw new IOException("bad reference number: " + refNum);
        }
        synchronized (f) {
            RandomAccessFile r = f.file();
            byte[] b = new byte[(int) r.length()];
            r.seek(0);
            r.readFully(b);
            return b;
        }
    }

    /** replaces the data fork of an open file */
    static void writeFork(short refNum, byte[] b) throws IOException {
        OpenedFile f = opened(refNum);
        if (f == null || !f.writable) {
            throw new IOException("bad reference number: " + refNum);
        }
        synchronized (f) {
            RandomAccessFile r = f.file();
            r.setLength(0);
            r.seek(0);
            r.write(b);
        }
    }

    public static short FSpOpenDF(byte[] fsspec, byte permission, short[] refNum) {
        String path = specToPath(fsspec);
        if (path == null) {
            return paramErr;
        }
        return open(path, permission == 0 ? 3 : permission, false, refNum);
    }

    public static short FSClose(short refNum) {
        return close(refNum);
    }

    // ---- FSSpec / FSRef

    public static short FSMakeFSSpec(short vRefNum, int dirID, byte[] fileName, byte[] spec) {
        String name = Bytes.getPString(fileName, 0);
        String path;
        if (vRefNum == kVRefNum) {
            String dir = paths.get(dirID);
            path = dir == null ? name : new File(dir, name).getPath();
        } else {
            path = name.indexOf('/') >= 0 ? name : File.separator + name.replace(':', File.separatorChar);
        }
        byte[] s = pathToSpec(path);
        System.arraycopy(s, 0, spec, 0, Math.min(s.length, spec.length));
        return new File(path).exists() ? noErr : fnfErr;
    }

    /** FSRef emulated as 80 bytes: kVRefNum(2) path id(4) */
    public static short FSPathMakeRef(byte[] cPath, byte[] fsRef, byte[] isDirectory) {
        int len = 0;
        while (len < cPath.length && cPath[len] != 0) {
            len++;
        }
        String path = new String(cPath, 0, len, StandardCharsets.UTF_8);
        File f = new File(path);
        if (!f.exists()) {
            return fnfErr;
        }
        byte[] spec = pathToSpec(path);
        System.arraycopy(spec, 0, fsRef, 0, Math.min(6, fsRef.length));
        if (isDirectory != null && isDirectory.length > 0) {
            isDirectory[0] = (byte) (f.isDirectory() ? 1 : 0);
        }
        return noErr;
    }

    public static short FSMakeFSRefUnicode(byte[] parentRef, int nameLength, byte[] name, int textEncodingHint, byte[] newRef) {
        String dir = paths.get(Bytes.getInt(parentRef, 2));
        if (dir == null) {
            return paramErr;
        }
        String n = new String(name, 0, nameLength * 2, StandardCharsets.UTF_16BE);
        byte[] spec = pathToSpec(new File(dir, n).getPath());
        System.arraycopy(spec, 0, newRef, 0, Math.min(6, newRef.length));
        return new File(dir, n).exists() ? noErr : fnfErr;
    }

    /** writes kVRefNum and the parent directory id of the FSRef into the catalog info (vRefNum at 0, parID at 2) */
    public static int FSGetCatalogInfo(byte[] fsRef, int whichInfo, int catalogInfo, int outName, byte[] fsSpec, int parentRef) {
        String path = paths.get(Bytes.getInt(fsRef, 2));
        if (path == null) {
            return paramErr;
        }
        if (fsSpec != null) {
            byte[] spec = pathToSpec(path);
            System.arraycopy(spec, 0, fsSpec, 0, Math.min(spec.length, fsSpec.length));
        }
        return noErr;
    }

    public static short PBGetCatInfoSync(byte[] paramBlock) {
        throw new UnsupportedToolboxException("PBGetCatInfoSync", "classic Mac OS only");
    }

    public static void StandardGetFilePreview(int fileFilter, short numTypes, int[] typeList, byte[] reply) {
        throw new UnsupportedToolboxException("StandardGetFilePreview", "use java.awt.FileDialog");
    }

    // ---- aliases

    private static final int ALIAS_HEADER = 150;

    /** @return a version 2 alias record for the path */
    static byte[] newAliasRecord(String path) {
        File f = new File(path).getAbsoluteFile();
        byte[] posix = f.getPath().getBytes(StandardCharsets.UTF_8);
        byte[] volume = "/".getBytes(StandardCharsets.UTF_8);
        int size = ALIAS_HEADER + extraSize(posix) + extraSize(volume) + 4;
        byte[] b = new byte[size];
        // user type 0, record size, version 2, kind 0: file 1: folder
        Bytes.putShort(b, 4, size);
        Bytes.putShort(b, 6, 2);
        Bytes.putShort(b, 8, f.isDirectory() ? 1 : 0);
        Bytes.putPString(b, 10, "Macintosh HD", 28);
        Bytes.putPString(b, 50, f.getName(), 64);
        Bytes.putShort(b, 130, -1); // nlvl from
        Bytes.putShort(b, 132, -1); // nlvl to
        int p = ALIAS_HEADER;
        p = putExtra(b, p, 18, posix);
        p = putExtra(b, p, 19, volume);
        Bytes.putShort(b, p, -1);
        return b;
    }

    private static int extraSize(byte[] data) {
        return 4 + data.length + (data.length & 1);
    }

    private static int putExtra(byte[] b, int p, int tag, byte[] data) {
        Bytes.putShort(b, p, tag);
        Bytes.putShort(b, p + 2, data.length);
        System.arraycopy(data, 0, b, p + 4, data.length);
        return p + extraSize(data);
    }

    /** @return the absolute path an alias record points to, null if it cannot be resolved */
    static String resolveAliasRecord(byte[] b) {
        if (b.length < ALIAS_HEADER) {
            return null;
        }
        String posix = null;
        String volume = null;
        int p = ALIAS_HEADER;
        while (p + 4 <= b.length) {
            int tag = Bytes.getShort(b, p);
            int len = Bytes.getShort(b, p + 2) & 0xffff;
            if (tag == -1 || p + 4 + len > b.length) {
                break;
            }
            if (tag == 18) {
                posix = new String(b, p + 4, len, StandardCharsets.UTF_8);
            } else if (tag == 19) {
                volume = new String(b, p + 4, len, StandardCharsets.UTF_8);
            }
            p += 4 + len + (len & 1);
        }
        if (posix != null) {
            // tag 18 is relative to the volume mount point
            if (volume != null && !volume.equals("/") && !posix.startsWith(volume)) {
                return new File(volume, posix).getPath();
            }
            return posix.startsWith("/") ? posix : "/" + posix;
        }
        // no extra info, look for the file name next to nothing else we know
        return null;
    }

    /** @return a handle holding a new alias record */
    static int newAlias(String path) {
        return Heap.newHandle(newAliasRecord(path));
    }

    public static short QTNewAlias(byte[] fsspec, int[] alias, byte minimal) {
        String path = specToPath(fsspec);
        if (path == null) {
            return paramErr;
        }
        alias[0] = newAlias(path);
        return noErr;
    }

    public static short NewAlias(byte[] fromFile, byte[] target, int[] alias) {
        String path = specToPath(target);
        if (path == null) {
            return paramErr;
        }
        alias[0] = newAlias(path);
        return noErr;
    }

    public static short ResolveAlias(byte[] fromFile, int alias, byte[] target, byte[] wasChanged) {
        if (alias == 0) {
            return paramErr;
        }
        String path = resolveAliasRecord(Heap.handleBytes(alias));
        if (path == null) {
            return fnfErr;
        }
        byte[] spec = pathToSpec(path);
        System.arraycopy(spec, 0, target, 0, Math.min(spec.length, target.length));
        if (wasChanged != null && wasChanged.length > 0) {
            wasChanged[0] = 0;
        }
        return new File(path).exists() ? noErr : fnfErr;
    }

    /** Finder alias files are not followed, the spec is returned as is */
    public static short ResolveAliasFile(byte[] spec, byte resolveAliasChains, byte[] targetIsFolder, byte[] wasAliased) {
        String path = specToPath(spec);
        if (path == null) {
            return paramErr;
        }
        File f = new File(path);
        if (targetIsFolder != null && targetIsFolder.length > 0) {
            targetIsFolder[0] = (byte) (f.isDirectory() ? 1 : 0);
        }
        if (wasAliased != null && wasAliased.length > 0) {
            wasAliased[0] = 0;
        }
        return f.exists() ? noErr : fnfErr;
    }

    /** data reference type 'alis' */
    static final int AliasDataHandlerSubType = Bytes.fourCC("alis");

    public static short QTNewDataReferenceFromFullPathUnicode(byte[] utf16Path, int length, int pathStyle, int flags, int[] dataRef, int[] dataRefType) {
        String path = new String(utf16Path, 0, Math.min(utf16Path.length, length * 2), StandardCharsets.UTF_16BE);
        dataRef[0] = newAlias(path);
        dataRefType[0] = AliasDataHandlerSubType;
        return noErr;
    }
}
