/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.ArrayList;
import java.util.List;


/**
 * AtomManager.
 * <p>
 * QT atom containers, kept in their documented binary layout inside a handle so that pointers to atom data
 * ({@code QTGetAtomDataPtr}) and containers read from files work as on the real toolbox:
 * <pre>
 * container header: 10 bytes reserved, 2 bytes lock count
 * QT atom header:   size(4) type(4) id(4) reserved(2) child count(2) reserved(4)
 * </pre>
 * The container holds one root atom ('sean'), a QTAtom is the byte offset of an atom header within the container,
 * {@link #kParentAtomIsContainer} stands for the root.
 * Results that the API returns as values are reported through {@link MoviesError}.
 */
public final class AtomManager {

    private AtomManager() {
    }

    public static final int kParentAtomIsContainer = 0;

    static final short paramErr = -50;
    static final short cannotFindAtomErr = -2101;
    static final short notLeafAtomErr = -2102;
    static final short atomIndexInvalidErr = -2104;
    static final short duplicateAtomTypeAndIDErr = -2105;
    static final short invalidAtomErr = -2106;
    static final short invalidAtomContainerErr = -2107;

    private static final int CONTAINER_HEADER = 12;
    private static final int ATOM_HEADER = 20;
    private static final int ROOT_TYPE = Bytes.fourCC("sean");

    /** an atom of a parsed container */
    static final class Node {
        int type;
        int id;
        byte[] data = new byte[0];
        List<Node> children = new ArrayList<>();
        Node parent;
        /** offset of the header in the container, valid after parse or serialize */
        int offset;

        Node(int type, int id) {
            this.type = type;
            this.id = id;
        }

        boolean isLeaf() {
            return children.isEmpty();
        }

        int size() {
            if (isLeaf()) {
                return ATOM_HEADER + data.length;
            }
            int s = ATOM_HEADER;
            for (Node c : children) {
                s += c.size();
            }
            return s;
        }

        Node copy(Node newParent) {
            Node n = new Node(type, id);
            n.data = data.clone();
            n.parent = newParent;
            for (Node c : children) {
                n.children.add(c.copy(n));
            }
            return n;
        }

        /** index among the siblings of the same type, 1 based */
        int indexOfType() {
            int i = 0;
            for (Node c : parent.children) {
                if (c.type == type) {
                    i++;
                }
                if (c == this) {
                    return i;
                }
            }
            return 0;
        }
    }

    /** a parsed atom container */
    static final class Tree {
        short lockCount;
        Node root = new Node(ROOT_TYPE, 1);
        final java.util.Map<Integer, Node> byOffset = new java.util.HashMap<>();

        Tree() {
            byOffset.put(kParentAtomIsContainer, root);
        }

        /** @return the atom at the reference */
        Node node(int atom) {
            return byOffset.get(atom == CONTAINER_HEADER ? kParentAtomIsContainer : atom);
        }

        int ref(Node n) {
            return n == root ? kParentAtomIsContainer : n.offset;
        }
    }

    // ---- binary layout

    static Tree parse(byte[] b) {
        Tree t = new Tree();
        if (b.length < CONTAINER_HEADER + ATOM_HEADER) {
            return t;
        }
        t.lockCount = Bytes.getShort(b, 10);
        t.root = parseAtom(b, CONTAINER_HEADER, b.length, null, t);
        t.byOffset.put(kParentAtomIsContainer, t.root);
        return t;
    }

    private static Node parseAtom(byte[] b, int off, int end, Node parent, Tree t) {
        int size = Bytes.getInt(b, off);
        if (size < ATOM_HEADER || off + size > end) {
            throw new IllegalArgumentException("broken atom at " + off);
        }
        Node n = new Node(Bytes.getInt(b, off + 4), Bytes.getInt(b, off + 8));
        n.parent = parent;
        n.offset = off;
        t.byOffset.put(off, n);
        int childCount = Bytes.getShort(b, off + 14) & 0xffff;
        if (childCount == 0) {
            n.data = java.util.Arrays.copyOfRange(b, off + ATOM_HEADER, off + size);
        } else {
            int p = off + ATOM_HEADER;
            for (int i = 0; i < childCount && p < off + size; i++) {
                Node c = parseAtom(b, p, off + size, n, t);
                n.children.add(c);
                p += Bytes.getInt(b, p);
            }
        }
        return n;
    }

    static byte[] serialize(Tree t) {
        byte[] b = new byte[CONTAINER_HEADER + t.root.size()];
        Bytes.putShort(b, 10, t.lockCount);
        t.byOffset.clear();
        writeAtom(b, CONTAINER_HEADER, t.root, t);
        t.byOffset.put(kParentAtomIsContainer, t.root);
        return b;
    }

    private static int writeAtom(byte[] b, int off, Node n, Tree t) {
        n.offset = off;
        t.byOffset.put(off, n);
        int size = n.size();
        Bytes.putInt(b, off, size);
        Bytes.putInt(b, off + 4, n.type);
        Bytes.putInt(b, off + 8, n.id);
        Bytes.putShort(b, off + 14, n.children.size());
        if (n.isLeaf()) {
            System.arraycopy(n.data, 0, b, off + ATOM_HEADER, n.data.length);
        } else {
            int p = off + ATOM_HEADER;
            for (Node c : n.children) {
                p = writeAtom(b, p, c, t);
            }
        }
        return off + size;
    }

    /** @return a new container handle holding just the root atom */
    public static int newContainer() {
        return Heap.newHandle(serialize(new Tree()));
    }

    /** @return the parsed container */
    static Tree read(int container) {
        return parse(Heap.handleBytes(container));
    }

    /** writes the container back, references of the tree are updated */
    static void write(int container, Tree t) {
        Heap.setHandleBytes(container, serialize(t));
    }

    private static Node children(Tree t, int parent) {
        Node p = t.node(parent);
        if (p == null) {
            MoviesError.set(invalidAtomErr);
        }
        return p;
    }

    // ---- QuickTime functions

    public static short QTNewAtomContainer(int[] container) {
        container[0] = newContainer();
        return 0;
    }

    public static short QTDisposeAtomContainer(int container) {
        if (container == 0) {
            return invalidAtomContainerErr;
        }
        Heap.disposeHandle(container);
        return 0;
    }

    public static short QTLockContainer(int container) {
        if (container != 0 && Heap.handleSize(container) >= CONTAINER_HEADER) {
            int p = Heap.deref(container);
            Heap.putShort(p + 10, (short) (Heap.getShort(p + 10) + 1));
        }
        return 0;
    }

    public static short QTUnlockContainer(int container) {
        if (container != 0 && Heap.handleSize(container) >= CONTAINER_HEADER) {
            int p = Heap.deref(container);
            short c = Heap.getShort(p + 10);
            if (c > 0) {
                Heap.putShort(p + 10, (short) (c - 1));
            }
        }
        return 0;
    }

    private static short insert(int container, int parent, int type, int id, short index, byte[] data, int[] newAtom) {
        Tree t = read(container);
        Node p = t.node(parent);
        if (p == null) {
            return MoviesError.set(invalidAtomErr);
        }
        if (p != t.root && p.isLeaf() && p.data.length > 0) {
            return MoviesError.set(notLeafAtomErr);
        }
        if (id == 0) {
            for (Node c : p.children) {
                if (c.type == type) {
                    id = Math.max(id, c.id);
                }
            }
            id++;
        } else {
            for (Node c : p.children) {
                if (c.type == type && c.id == id) {
                    return MoviesError.set(duplicateAtomTypeAndIDErr);
                }
            }
        }
        Node n = new Node(type, id);
        n.data = data;
        n.parent = p;
        // index is the position among the atoms of the same type, 0 appends
        int sameType = 0;
        int pos = p.children.size();
        if (index > 0) {
            int count = 0;
            for (Node c : p.children) {
                if (c.type == type) {
                    count++;
                }
            }
            if (index > count + 1) {
                return MoviesError.set(atomIndexInvalidErr);
            }
            for (int i = 0; i < p.children.size(); i++) {
                if (p.children.get(i).type == type && ++sameType == index) {
                    pos = i;
                    break;
                }
            }
        }
        p.data = new byte[0];
        p.children.add(pos, n);
        write(container, t);
        if (newAtom != null) {
            newAtom[0] = t.ref(n);
        }
        return MoviesError.ok();
    }

    public static short QTInsertChild(int container, int parent, int type, int id, short index, int size, byte[] data, int[] newAtom) {
        return insert(container, parent, type, id, index, java.util.Arrays.copyOf(data, size), newAtom);
    }

    public static short QTInsertChild(int container, int parent, int type, int id, short index, int size, int dataPtr, int[] newAtom) {
        return insert(container, parent, type, id, index, dataPtr == 0 || size == 0 ? new byte[size] : Heap.read(dataPtr, size), newAtom);
    }

    public static short QTInsertChild(int container, int parent, int type, int id, short index, int size, int[] data, int[] newAtom) {
        byte[] b = new byte[size];
        Bytes.fromArray(data, 0, b, 0, size);
        return insert(container, parent, type, id, index, b, newAtom);
    }

    public static short QTInsertChild(int container, int parent, int type, int id, short index, int size, short[] data, int[] newAtom) {
        byte[] b = new byte[size];
        Bytes.fromArray(data, 0, b, 0, size);
        return insert(container, parent, type, id, index, b, newAtom);
    }

    public static short QTInsertChildren(int container, int parent, int childrenContainer) {
        if (childrenContainer == 0) {
            return MoviesError.set(invalidAtomContainerErr);
        }
        Tree t = read(container);
        Node p = t.node(parent);
        if (p == null) {
            return MoviesError.set(invalidAtomErr);
        }
        Tree src = read(childrenContainer);
        for (Node c : src.root.children) {
            for (Node e : p.children) {
                if (e.type == c.type && e.id == c.id) {
                    return MoviesError.set(duplicateAtomTypeAndIDErr);
                }
            }
        }
        for (Node c : src.root.children) {
            p.children.add(c.copy(p));
        }
        p.data = new byte[0];
        write(container, t);
        return MoviesError.ok();
    }

    public static short QTRemoveAtom(int container, int atom) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null || n == t.root) {
            return MoviesError.set(invalidAtomErr);
        }
        n.parent.children.remove(n);
        write(container, t);
        return MoviesError.ok();
    }

    public static short QTRemoveChildren(int container, int atom) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null) {
            return MoviesError.set(invalidAtomErr);
        }
        n.children.clear();
        write(container, t);
        return MoviesError.ok();
    }

    public static short QTReplaceAtom(int target, int targetAtom, int replacement, int replacementAtom) {
        Tree t = read(target);
        Node n = t.node(targetAtom);
        Tree r = read(replacement);
        Node s = r.node(replacementAtom);
        if (n == null || s == null) {
            return MoviesError.set(invalidAtomErr);
        }
        // the target atom keeps its type, id and position
        Node c = s.copy(n.parent);
        n.data = c.data;
        n.children = c.children;
        n.children.forEach(x -> x.parent = n);
        write(target, t);
        return MoviesError.ok();
    }

    public static short QTSwapAtoms(int container, int atom1, int atom2) {
        Tree t = read(container);
        Node a = t.node(atom1);
        Node b = t.node(atom2);
        if (a == null || b == null || a == t.root || b == t.root) {
            return MoviesError.set(invalidAtomErr);
        }
        // contents are swapped, ids and indexes stay
        byte[] d = a.data;
        a.data = b.data;
        b.data = d;
        List<Node> c = a.children;
        a.children = b.children;
        b.children = c;
        a.children.forEach(x -> x.parent = a);
        b.children.forEach(x -> x.parent = b);
        write(container, t);
        return MoviesError.ok();
    }

    public static short QTSetAtomID(int container, int atom, int newID) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null || n == t.root) {
            return MoviesError.set(invalidAtomErr);
        }
        for (Node c : n.parent.children) {
            if (c != n && c.type == n.type && c.id == newID) {
                return MoviesError.set(duplicateAtomTypeAndIDErr);
            }
        }
        n.id = newID;
        write(container, t);
        return MoviesError.ok();
    }

    private static short setData(int container, int atom, byte[] data) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null || n == t.root) {
            return MoviesError.set(invalidAtomErr);
        }
        if (!n.isLeaf()) {
            return MoviesError.set(notLeafAtomErr);
        }
        if (n.data.length == data.length) {
            // in place, pointers into the container stay valid
            Heap.write(Heap.deref(container) + n.offset + ATOM_HEADER, data, 0, data.length);
        } else {
            n.data = data;
            write(container, t);
        }
        return MoviesError.ok();
    }

    public static short QTSetAtomData(int container, int atom, int size, byte[] data) {
        return setData(container, atom, java.util.Arrays.copyOf(data, size));
    }

    public static short QTSetAtomData(int container, int atom, int size, int dataPtr) {
        return setData(container, atom, size == 0 ? new byte[0] : Heap.read(dataPtr, size));
    }

    public static short QTSetAtomData(int container, int atom, int size, int[] data) {
        byte[] b = new byte[size];
        Bytes.fromArray(data, 0, b, 0, size);
        return setData(container, atom, b);
    }

    public static short QTSetAtomData(int container, int atom, int size, short[] data) {
        byte[] b = new byte[size];
        Bytes.fromArray(data, 0, b, 0, size);
        return setData(container, atom, b);
    }

    public static short QTGetAtomDataPtr(int container, int atom, int[] size, int[] ptr) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null || n == t.root) {
            return MoviesError.set(invalidAtomErr);
        }
        if (!n.isLeaf()) {
            return MoviesError.set(notLeafAtomErr);
        }
        if (size != null) {
            size[0] = n.data.length;
        }
        if (ptr != null) {
            ptr[0] = Heap.deref(container) + n.offset + ATOM_HEADER;
        }
        return MoviesError.ok();
    }

    public static short QTCopyAtomDataToHandle(int container, int atom, int handle) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null || n == t.root) {
            return MoviesError.set(invalidAtomErr);
        }
        if (!n.isLeaf()) {
            return MoviesError.set(notLeafAtomErr);
        }
        Heap.setHandleBytes(handle, n.data);
        return MoviesError.ok();
    }

    private static byte[] dataToCopy(int container, int atom, boolean sizeOrLessOK, int size, int[] actualSize) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null || n == t.root) {
            MoviesError.set(invalidAtomErr);
            return null;
        }
        if (!n.isLeaf()) {
            MoviesError.set(notLeafAtomErr);
            return null;
        }
        if (n.data.length > size && !sizeOrLessOK) {
            MoviesError.set(paramErr);
            return null;
        }
        int length = Math.min(size, n.data.length);
        if (actualSize != null) {
            actualSize[0] = length;
        }
        MoviesError.ok();
        return java.util.Arrays.copyOf(n.data, length);
    }

    public static short QTCopyAtomDataToPtr(int container, int atom, byte sizeOrLessOK, int size, byte[] dst, int[] actualSize) {
        byte[] d = dataToCopy(container, atom, sizeOrLessOK != 0, size, actualSize);
        if (d == null) {
            return MoviesError.get();
        }
        System.arraycopy(d, 0, dst, 0, d.length);
        return 0;
    }

    public static short QTCopyAtomDataToPtr(int container, int atom, byte sizeOrLessOK, int size, int dstPtr, int[] actualSize) {
        byte[] d = dataToCopy(container, atom, sizeOrLessOK != 0, size, actualSize);
        if (d == null) {
            return MoviesError.get();
        }
        Heap.write(dstPtr, d, 0, d.length);
        return 0;
    }

    public static short QTCopyAtom(int container, int atom, int[] targetContainer) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null) {
            return MoviesError.set(invalidAtomErr);
        }
        Tree c = new Tree();
        if (n == t.root) {
            c.root = n.copy(null);
        } else {
            c.root.children.add(n.copy(c.root));
        }
        targetContainer[0] = Heap.newHandle(serialize(c));
        return MoviesError.ok();
    }

    public static short QTCountChildrenOfType(int container, int parent, int type) {
        Tree t = read(container);
        Node p = children(t, parent);
        if (p == null) {
            return 0;
        }
        short count = 0;
        for (Node c : p.children) {
            if (type == 0 || c.type == type) {
                count++;
            }
        }
        MoviesError.ok();
        return count;
    }

    public static int QTFindChildByIndex(int container, int parent, int type, short index, int[] id) {
        Tree t = read(container);
        Node p = children(t, parent);
        if (p == null) {
            return 0;
        }
        int i = 0;
        for (Node c : p.children) {
            if ((type == 0 || c.type == type) && ++i == index) {
                if (id != null) {
                    id[0] = c.id;
                }
                MoviesError.ok();
                return t.ref(c);
            }
        }
        MoviesError.set(cannotFindAtomErr);
        return 0;
    }

    public static int QTFindChildByID(int container, int parent, int type, int id, short[] index) {
        Tree t = read(container);
        Node p = children(t, parent);
        if (p == null) {
            return 0;
        }
        for (Node c : p.children) {
            if (c.type == type && c.id == id) {
                if (index != null) {
                    index[0] = (short) c.indexOfType();
                }
                MoviesError.ok();
                return t.ref(c);
            }
        }
        MoviesError.set(cannotFindAtomErr);
        return 0;
    }

    public static short QTNextChildAnyType(int container, int parent, int currentChild, int[] nextChild) {
        Tree t = read(container);
        Node p = t.node(parent);
        if (p == null) {
            return MoviesError.set(invalidAtomErr);
        }
        nextChild[0] = 0;
        if (currentChild == 0) {
            if (!p.children.isEmpty()) {
                nextChild[0] = t.ref(p.children.getFirst());
            }
        } else {
            Node c = t.node(currentChild);
            int i = p.children.indexOf(c);
            if (i < 0) {
                return MoviesError.set(invalidAtomErr);
            }
            if (i + 1 < p.children.size()) {
                nextChild[0] = t.ref(p.children.get(i + 1));
            }
        }
        return MoviesError.ok();
    }

    public static int QTGetNextChildType(int container, int parent, int currentType) {
        Tree t = read(container);
        Node p = children(t, parent);
        if (p == null) {
            return 0;
        }
        List<Integer> types = new ArrayList<>();
        for (Node c : p.children) {
            if (!types.contains(c.type)) {
                types.add(c.type);
            }
        }
        MoviesError.ok();
        if (currentType == 0) {
            return types.isEmpty() ? 0 : types.getFirst();
        }
        int i = types.indexOf(currentType);
        return i >= 0 && i + 1 < types.size() ? types.get(i + 1) : 0;
    }

    public static int QTGetAtomParent(int container, int child) {
        Tree t = read(container);
        Node n = t.node(child);
        if (n == null || n == t.root) {
            MoviesError.set(invalidAtomErr);
            return 0;
        }
        MoviesError.ok();
        return t.ref(n.parent);
    }

    public static short QTGetAtomTypeAndID(int container, int atom, int[] type, int[] id) {
        Tree t = read(container);
        Node n = t.node(atom);
        if (n == null) {
            return MoviesError.set(invalidAtomErr);
        }
        if (type != null) {
            type[0] = n.type;
        }
        if (id != null) {
            id[0] = n.id;
        }
        return MoviesError.ok();
    }

    // ---- helpers for the other toolbox managers

    /** @return the data of the leaf atom found by the type path from the root, each step the first atom of the type, or null */
    static byte[] leafData(int container, int... types) {
        Tree t = read(container);
        Node n = t.root;
        for (int type : types) {
            Node next = null;
            for (Node c : n.children) {
                if (c.type == type) {
                    next = c;
                    break;
                }
            }
            if (next == null) {
                return null;
            }
            n = next;
        }
        return n.data;
    }

}
