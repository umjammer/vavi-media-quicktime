/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;


/**
 * Appends media data to files inside an 'mdat' atom.
 * <p>
 * An open 'mdat' starts with a 'wide' atom, so that the header can become a 64 bit 'mdat' header
 * when the data grows over 4GB, like QuickTime does.
 */
final class MdatWriter {

    private MdatWriter() {
    }

    private static final class Open {
        final RandomAccessFile file;
        final long header;
        long length;

        Open(RandomAccessFile file, long header) {
            this.file = file;
            this.header = header;
        }
    }

    private static final Map<String, Open> opens = new HashMap<>();

    /** @return the file offset of the appended data */
    static synchronized long append(String path, byte[] buf, int offset, int length) throws IOException {
        Open o = opens.get(path);
        if (o == null) {
            RandomAccessFile f = new RandomAccessFile(path, "rw");
            long header = f.length();
            f.seek(header);
            byte[] h = new byte[16];
            Bytes.putInt(h, 0, 8);
            Bytes.putInt(h, 4, Bytes.fourCC("wide"));
            Bytes.putInt(h, 8, 8);
            Bytes.putInt(h, 12, Bytes.fourCC("mdat"));
            f.write(h);
            o = new Open(f, header);
            opens.put(path, o);
        }
        long at = o.header + 16 + o.length;
        o.file.seek(at);
        o.file.write(buf, offset, length);
        o.length += length;
        return at;
    }

    /** patches the 'mdat' size, the atom stays open */
    static synchronized void flush(String path) throws IOException {
        Open o = opens.get(path);
        if (o != null) {
            patch(o);
        }
    }

    private static void patch(Open o) throws IOException {
        byte[] h = new byte[16];
        if (o.length + 8 <= 0xffff_ffffL) {
            Bytes.putInt(h, 0, 8);
            Bytes.putInt(h, 4, Bytes.fourCC("wide"));
            Bytes.putInt(h, 8, (int) (o.length + 8));
            Bytes.putInt(h, 12, Bytes.fourCC("mdat"));
        } else {
            Bytes.putInt(h, 0, 1);
            Bytes.putInt(h, 4, Bytes.fourCC("mdat"));
            Bytes.putLong(h, 8, o.length + 16);
        }
        o.file.seek(o.header);
        o.file.write(h);
    }

    /** closes the 'mdat', the next append starts a new one */
    static synchronized void close(String path) throws IOException {
        Open o = opens.remove(path);
        if (o != null) {
            patch(o);
            o.file.close();
        }
    }

    static synchronized boolean isOpen(String path) {
        return opens.containsKey(path);
    }
}
