/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quicktime.QTSession;
import quicktime.std.movies.Atom;
import quicktime.std.movies.AtomContainer;
import quicktime.util.QTHandle;
import quicktime.util.QTPointer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class HeapTest {

    @BeforeEach
    void setUp() throws Exception {
        QTSession.open();
    }

    @AfterEach
    void tearDown() {
        QTSession.close();
    }

    @Test
    void testHandle() throws Exception {
        int h = MemoryManager.NewHandle(8);
        assertEquals(8, MemoryManager.GetHandleSize(h));
        MemoryManager.setIntInHandle(h, 4, 0x01020304);
        assertEquals(0x0102, MemoryManager.getShortFromHandle(h, 4));
        int p = MemoryManager.getIntFromPointer(h, 0);
        assertEquals(0x03, MemoryManager.getByteFromPointer(p, 6));
        MemoryManager.SetHandleSize(h, 1024);
        assertEquals(1024, MemoryManager.GetHandleSize(h));
        assertEquals(0x01020304, MemoryManager.getIntFromHandle(h, 4));
        MemoryManager.DisposeHandle(h);
        assertThrows(IllegalArgumentException.class, () -> MemoryManager.getIntFromPointer(p, 0));
    }

    @Test
    void testArrays() {
        int[] a = {0x11223344, 0x55667788};
        assertEquals(0x3344, MemoryManager.getShortFromArray(a, 2));
        assertEquals(0x33445566, MemoryManager.getIntFromArray(a, 2));
        MemoryManager.setShortInArray(a, 2, (short) 0xabcd);
        assertEquals(0x1122abcd, a[0]);
        int ptr = MemoryManager.NewPtr(8);
        MemoryManager.copyArrayToPointer(a, 0, ptr, 0, 8);
        byte[] b = new byte[8];
        MemoryManager.copyPointerToArray(ptr, 0, b, 0, 8);
        assertArrayEquals(new byte[] {0x11, 0x22, (byte) 0xab, (byte) 0xcd, 0x55, 0x66, 0x77, (byte) 0x88}, b);
        MemoryManager.DisposePtr(ptr);
    }

    @Test
    void testQTHandle() throws Exception {
        QTHandle h = new QTHandle(new byte[] {1, 2, 3});
        assertEquals(3, h.getSize());
        h.setSize(5);
        assertArrayEquals(new byte[] {1, 2, 3, 0, 0}, h.getBytes());
        QTPointer p = new QTPointer(4, true);
        p.copyFromArray(0, new int[] {42}, 0, 1);
        int[] r = new int[1];
        p.copyToArray(0, r, 0, 1);
        assertEquals(42, r[0]);
        p.setSize(16);
        assertEquals(16, p.getSize());
    }

    @Test
    void testAtomContainer() throws Exception {
        AtomContainer ac = new AtomContainer();
        int foo = Bytes.fourCC("foo ");
        int bar = Bytes.fourCC("bar ");
        Atom parent = ac.insertChild(null, foo, 1, 0);
        Atom child1 = ac.insertChild(parent, bar, 0, 0, new byte[] {1, 2, 3, 4});
        Atom child2 = ac.insertChild(parent, bar, 0, 0, 0x12345678);
        assertNotEquals(child1.getAtom(), child2.getAtom());
        parent = ac.findChildByIndex_Atom(null, foo, 1);
        assertEquals(2, ac.countChildrenOfType(parent, bar));
        Atom c = ac.findChildByID_Atom(parent, bar, 2);
        assertEquals(2, ac.getAtomID(c));
        assertEquals(bar, ac.getAtomType(c));
        assertEquals(0x12345678, ac.getAtomData(c).getInt(0));
        assertArrayEquals(new byte[] {1, 2, 3, 4}, ac.copyAtomDataToArray(ac.findChildByIndex_Atom(parent, bar, 1)));
        ac.setAtomData(c, new byte[] {9});
        c = ac.findChildByID_Atom(parent, bar, 2);
        assertArrayEquals(new byte[] {9}, ac.copyAtomDataToArray(c));
        ac.removeAtom(c);
        assertEquals(1, ac.countChildrenOfType(parent, bar));
        assertNull(ac.nextChildAnyType(parent, ac.findChildByIndex_Atom(parent, bar, 1)));

        // binary layout
        byte[] bytes = ac.getBytes();
        assertEquals(12 + 20 + 20 + 24, bytes.length);
        assertEquals(Bytes.fourCC("sean"), Bytes.getInt(bytes, 16));
        AtomContainer copy = AtomContainer.fromQTHandle(new QTHandle(bytes));
        assertEquals(1, copy.countChildrenOfType(null, foo));
    }
}
