/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * AtomManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class AtomManager {

    private AtomManager() {
    }

    /** used by AtomContainer */
    public static short QTCopyAtom(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTCopyAtom");
    }

    /** used by AtomContainer */
    public static short QTCopyAtomDataToHandle(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTCopyAtomDataToHandle");
    }

    /** used by AtomContainer */
    public static short QTCopyAtomDataToPtr(int paramInt1, int paramInt2, byte paramByte, int paramInt3, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTCopyAtomDataToPtr");
    }

    /** used by AtomContainer */
    public static short QTCopyAtomDataToPtr(int paramInt1, int paramInt2, byte paramByte, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTCopyAtomDataToPtr");
    }

    /** used by AtomContainer */
    public static short QTCountChildrenOfType(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTCountChildrenOfType");
    }

    /** used by QTObjectManagement */
    public static short QTDisposeAtomContainer(int paramInt) {
        throw new UnsupportedToolboxException("QTDisposeAtomContainer");
    }

    /** used by AtomContainer */
    public static int QTFindChildByID(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("QTFindChildByID");
    }

    /** used by AtomContainer */
    public static int QTFindChildByIndex(int paramInt1, int paramInt2, int paramInt3, short paramShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTFindChildByIndex");
    }

    /** used by AtomContainer */
    public static short QTGetAtomDataPtr(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("QTGetAtomDataPtr");
    }

    /** used by AtomContainer */
    public static int QTGetAtomParent(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTGetAtomParent");
    }

    /** used by AtomContainer */
    public static short QTGetAtomTypeAndID(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("QTGetAtomTypeAndID");
    }

    /** used by AtomContainer */
    public static int QTGetNextChildType(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTGetNextChildType");
    }

    /** used by AtomContainer */
    public static short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTInsertChild");
    }

    /** used by AtomContainer */
    public static short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, int paramInt6, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTInsertChild");
    }

    /** used by AtomContainer */
    public static short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("QTInsertChild");
    }

    /** used by AtomContainer */
    public static short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, short[] paramArrayOfShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTInsertChild");
    }

    /** used by AtomContainer */
    public static short QTInsertChildren(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTInsertChildren");
    }

    /** used by AtomContainer */
    public static short QTLockContainer(int paramInt) {
        throw new UnsupportedToolboxException("QTLockContainer");
    }

    /** used by AtomContainer, AtomicInstrument */
    public static short QTNewAtomContainer(int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTNewAtomContainer");
    }

    /** used by AtomContainer */
    public static short QTNextChildAnyType(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTNextChildAnyType");
    }

    /** used by AtomContainer */
    public static short QTRemoveAtom(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTRemoveAtom");
    }

    /** used by AtomContainer */
    public static short QTRemoveChildren(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTRemoveChildren");
    }

    /** used by AtomContainer */
    public static short QTReplaceAtom(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTReplaceAtom");
    }

    /** used by AtomContainer */
    public static short QTUnlockContainer(int paramInt) {
        throw new UnsupportedToolboxException("QTUnlockContainer");
    }

}
