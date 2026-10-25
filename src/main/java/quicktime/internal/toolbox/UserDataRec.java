/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * User data of a movie, track or media: an ordered list of typed items, as in a 'udta' atom.
 * <p>
 * Text items (types starting with '©') hold one or more language entries: length(2), language(2), text.
 */
final class UserDataRec {

    /** a user data item */
    static final class Item {
        final int type;
        byte[] data;

        Item(int type, byte[] data) {
            this.type = type;
            this.data = data;
        }
    }

    final int ref;

    final List<Item> items = new ArrayList<>();

    UserDataRec() {
        this.ref = Heap.newObject(this, 4);
    }

    /** an owned user data is disposed with its owner */
    boolean owned;

    UserDataRec copy() {
        UserDataRec u = new UserDataRec();
        for (Item i : items) {
            u.items.add(new Item(i.type, i.data.clone()));
        }
        return u;
    }

    void dispose() {
        Heap.free(ref);
    }

    /** @return the index-th (1 based) item of the type, null if none */
    Item item(int type, int index) {
        int n = 0;
        for (Item i : items) {
            if (i.type == type && ++n == index) {
                return i;
            }
        }
        return null;
    }

    int count(int type) {
        int n = 0;
        for (Item i : items) {
            if (i.type == type) {
                n++;
            }
        }
        return n;
    }

    /** @return the next type after the type (0 for the first), 0 at the end */
    int nextType(int type) {
        List<Integer> types = new ArrayList<>();
        for (Item i : items) {
            if (!types.contains(i.type)) {
                types.add(i.type);
            }
        }
        if (type == 0) {
            return types.isEmpty() ? 0 : types.getFirst();
        }
        int i = types.indexOf(type);
        return i >= 0 && i + 1 < types.size() ? types.get(i + 1) : 0;
    }

    void add(int type, byte[] data) {
        items.add(new Item(type, data));
    }

    boolean remove(int type, int index) {
        Item i = item(type, index);
        return i != null && items.remove(i);
    }

    // ---- text items

    /** @return the text of the language in a text item, null if absent */
    static byte[] text(Item item, int language) {
        byte[] d = item.data;
        int p = 0;
        while (p + 4 <= d.length) {
            int len = Bytes.getShort(d, p) & 0xffff;
            int lang = Bytes.getShort(d, p + 2);
            if (p + 4 + len > d.length) {
                break;
            }
            if (lang == language) {
                return Arrays.copyOfRange(d, p + 4, p + 4 + len);
            }
            p += 4 + len;
        }
        return null;
    }

    /** sets the text of the language in a text item */
    static void setText(Item item, int language, byte[] text) {
        removeText(item, language);
        byte[] d = item.data;
        byte[] n = Arrays.copyOf(d, d.length + 4 + text.length);
        Bytes.putShort(n, d.length, text.length);
        Bytes.putShort(n, d.length + 2, language);
        System.arraycopy(text, 0, n, d.length + 4, text.length);
        item.data = n;
    }

    /** @return true if the language entry was removed */
    static boolean removeText(Item item, int language) {
        byte[] d = item.data;
        int p = 0;
        while (p + 4 <= d.length) {
            int len = Bytes.getShort(d, p) & 0xffff;
            int lang = Bytes.getShort(d, p + 2);
            if (p + 4 + len > d.length) {
                break;
            }
            if (lang == language) {
                byte[] n = new byte[d.length - 4 - len];
                System.arraycopy(d, 0, n, 0, p);
                System.arraycopy(d, p + 4 + len, n, p, d.length - p - 4 - len);
                item.data = n;
                return true;
            }
            p += 4 + len;
        }
        return false;
    }

    // ---- 'udta' atom contents

    /** @return the contents of a 'udta' atom */
    byte[] toBytes() {
        int size = 0;
        for (Item i : items) {
            size += 8 + i.data.length;
        }
        byte[] b = new byte[size + 4];
        int p = 0;
        for (Item i : items) {
            Bytes.putInt(b, p, 8 + i.data.length);
            Bytes.putInt(b, p + 4, i.type);
            System.arraycopy(i.data, 0, b, p + 8, i.data.length);
            p += 8 + i.data.length;
        }
        // terminating 32 bit 0
        return b;
    }

    /** reads the contents of a 'udta' atom */
    static UserDataRec fromBytes(byte[] b, int off, int end) {
        UserDataRec u = new UserDataRec();
        int p = off;
        while (p + 8 <= end) {
            int size = Bytes.getInt(b, p);
            if (size < 8 || p + size > end) {
                break;
            }
            u.add(Bytes.getInt(b, p + 4), Arrays.copyOfRange(b, p + 8, p + size));
            p += size;
        }
        return u;
    }
}
