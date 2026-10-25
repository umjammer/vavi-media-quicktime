/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * A data reference of a media, as in a 'dref' entry: where the sample data lives.
 * <ul>
 * <li>'alis': an alias record, or self reference (flag 1) to the file holding the movie</li>
 * <li>'url ': a C string URL</li>
 * <li>'hndl': a handle, the data is the 4 byte handle followed by optional extension atoms</li>
 * </ul>
 * Data appended to a movie file goes into an 'mdat' atom that is closed when the movie is written.
 */
final class DataRefRec {

    static final int alis = Bytes.fourCC("alis");
    static final int url = Bytes.fourCC("url ");
    static final int hndl = Bytes.fourCC("hndl");
    static final int rsrc = Bytes.fourCC("rsrc");

    /** dref flag, data is in the same file as the movie */
    static final int selfReference = 1;

    int type;
    int flags;
    byte[] data;
    /** dataRefAttributes */
    int attributes;
    /** the file of the movie, for self references */
    String selfPath;

    DataRefRec(int type, int flags, byte[] data) {
        this.type = type;
        this.flags = flags;
        this.data = data;
    }

    /** @return a data reference to a file */
    static DataRefRec forFile(String path) {
        return new DataRefRec(alis, 0, FileManager.newAliasRecord(path));
    }

    /** @return a self reference to the movie file */
    static DataRefRec self(String path) {
        DataRefRec d = new DataRefRec(alis, selfReference, new byte[0]);
        d.selfPath = path;
        return d;
    }

    /** @return a data reference to a handle */
    static DataRefRec forHandle(int handle) {
        byte[] b = new byte[4];
        Bytes.putInt(b, 0, handle);
        return new DataRefRec(hndl, 0, b);
    }

    DataRefRec copy() {
        DataRefRec d = new DataRefRec(type, flags, data.clone());
        d.attributes = attributes;
        d.selfPath = selfPath;
        return d;
    }

    boolean sameAs(DataRefRec o) {
        if (type != o.type) {
            return false;
        }
        String p = path();
        return p != null ? p.equals(o.path()) : Arrays.equals(data, o.data);
    }

    /** @return the file path of the data, or null for non file references */
    String path() {
        if (type == alis) {
            if ((flags & selfReference) != 0 || data.length == 0) {
                return selfPath;
            }
            return FileManager.resolveAliasRecord(data);
        }
        if (type == url) {
            String u = cString();
            if (u.startsWith("file:")) {
                try {
                    return new File(URI.create(u)).getPath();
                } catch (IllegalArgumentException e) {
                    return u.substring(5);
                }
            }
        }
        return null;
    }

    String cString() {
        int len = 0;
        while (len < data.length && data[len] != 0) {
            len++;
        }
        return new String(data, 0, len, StandardCharsets.UTF_8);
    }

    int handle() {
        return Bytes.getInt(data, 0);
    }

    /** @return the bytes for a 'dref' entry, self references are written as such */
    byte[] entryData() {
        return (flags & selfReference) != 0 ? new byte[0] : data;
    }

    // ---- I/O

    private static final Map<String, RandomAccessFile> readers = new HashMap<>();

    private static synchronized RandomAccessFile reader(String path) throws IOException {
        RandomAccessFile f = readers.get(path);
        if (f == null) {
            f = new RandomAccessFile(path, "r");
            readers.put(path, f);
        }
        return f;
    }

    /** closes cached readers of the file, before it is written or deleted */
    static synchronized void closeReader(String path) {
        RandomAccessFile f = readers.remove(path);
        if (f != null) {
            try {
                f.close();
            } catch (IOException ignored) {
            }
        }
    }

    /** reads sample data */
    void read(long offset, byte[] buf, int bufOffset, int length) throws IOException {
        if (type == hndl) {
            Heap.read(Heap.deref(handle()) + (int) offset, buf, bufOffset, length);
            return;
        }
        String path = path();
        if (path != null) {
            MdatWriter.flush(path);
            synchronized (DataRefRec.class) {
                RandomAccessFile f = reader(path);
                f.seek(offset);
                f.readFully(buf, bufOffset, length);
            }
            return;
        }
        if (type == url) {
            URL u = URI.create(cString()).toURL();
            java.net.URLConnection c = u.openConnection();
            c.setRequestProperty("Range", "bytes=" + offset + "-" + (offset + length - 1));
            try (InputStream is = c.getInputStream()) {
                if (!(c instanceof java.net.HttpURLConnection h) || h.getResponseCode() != 206) {
                    is.skipNBytes(offset);
                }
                int n = is.readNBytes(buf, bufOffset, length);
                if (n < length) {
                    throw new java.io.EOFException(u.toString());
                }
            }
            return;
        }
        throw new IOException("unsupported data reference: " + Bytes.fourCC(type));
    }

    byte[] read(long offset, int length) throws IOException {
        byte[] b = new byte[length];
        read(offset, b, 0, length);
        return b;
    }

    /** @return the offset the data was written at */
    long append(byte[] buf, int offset, int length) throws IOException {
        if (type == hndl) {
            int h = handle();
            int size = Heap.handleSize(h);
            Heap.setHandleSize(h, size + length);
            Heap.write(Heap.deref(h) + size, buf, offset, length);
            return size;
        }
        String path = path();
        if (path == null) {
            throw new IOException("data reference is not writable: " + Bytes.fourCC(type));
        }
        closeReader(path);
        return MdatWriter.append(path, buf, offset, length);
    }

    @Override
    public String toString() {
        return Bytes.fourCC(type) + "[" + (path() != null ? path() : type == url ? cString() : "") + "]";
    }
}
