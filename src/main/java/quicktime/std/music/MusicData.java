
package quicktime.std.music;

import quicktime.QTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTHandle;
import quicktime.util.UtilException;


public class MusicData extends QTHandle {
    public static final int kNoteRequestHeaderEventLength = 23;

    public static int stuffGeneralEvent_Header(int paramInt1, int paramInt2) {
        int i = 0xF0000000 | paramInt1 << 16 | paramInt2 << 0;

        return EndianOrder.flipNativeToBigEndian32(i);
    }

    public static int stuffGeneralEvent_Footer(int paramInt1, int paramInt2) {
        int i = 0xC0000000 | paramInt1 << 16 | paramInt2 << 0;

        return EndianOrder.flipNativeToBigEndian32(i);
    }

    public static int stuffRestEvent(int paramInt) {
        int i = 0x0 | paramInt << 0;
        return EndianOrder.flipNativeToBigEndian32(i);
    }

    public static int stuffNoteEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = 0x20000000 | paramInt1 << 24 | paramInt2 - 32 << 18 | paramInt3 - 0 << 11 | paramInt4 << 0;

        return EndianOrder.flipNativeToBigEndian32(i);
    }

    public static int stuffControlEvent(int paramInt1, int paramInt2, int paramInt3) {
        int i = 0x40000000 | paramInt1 << 24 | paramInt2 << 16 | (paramInt3 & qtma_MASK(16)) << 0;

        return EndianOrder.flipNativeToBigEndian32(i);
    }

    public static int stuffMarkerEvent(int paramInt1, int paramInt2) {
        int i = 0x60000000 | paramInt1 << 16 | paramInt2 << 0;

        return EndianOrder.flipNativeToBigEndian32(i);
    }

    public static long stuffXNoteEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        long l1 = 0x90000000 | paramInt1 << 16 | paramInt2 << 0;

        l1 = EndianOrder.flipNativeToBigEndian64(l1);

        long l2 = 0x80000000 | paramInt4 << 0 | paramInt3 << 22;

        l2 = EndianOrder.flipNativeToBigEndian64(l2);

        l1 &= 0xFFFFFFFF;
        l2 &= 0xFFFFFFFF;

        return l1 << 32 | l2;
    }

    public static long stuffXControlEvent(int paramInt1, int paramInt2, int paramInt3) {
        long l1 = 0xA0000000 | paramInt1 << 16 | (paramInt3 & qtma_MASK(16)) << 0;

        l1 = EndianOrder.flipNativeToBigEndian64(l1);
        long l2 = 0x80000000 | paramInt2 << 0;

        l2 = EndianOrder.flipNativeToBigEndian64(l2);

        l1 &= 0xFFFFFFFF;
        l2 &= 0xFFFFFFFF;

        return l1 << 32 | l2;
    }

    public static long stuffXKnobEvent(int paramInt1, int paramInt2, int paramInt3) {
        long l1 = 0xB0000000 | paramInt1 << 16 | paramInt3 >> 16 << 0;

        l1 = EndianOrder.flipNativeToBigEndian64(l1);
        long l2 = 0x80000000 | paramInt2 << 16 | (paramInt3 & 0xFFFF) << 0;

        l2 = EndianOrder.flipNativeToBigEndian64(l2);

        l1 &= 0xFFFFFFFF;
        l2 &= 0xFFFFFFFF;

        return l1 << 32 | l2;
    }

    public static int eventType(int paramInt) {
        return qtma_EXT(paramInt, 29, 3) > 3 ? qtma_EXT(paramInt, 28, 4) : qtma_EXT(paramInt, 29, 3);
    }

    public static int part(int paramInt) {
        return qtma_EXT(paramInt, 24, 5);
    }

    public static int xPart(long paramLong) {
        long l = paramLong >>> 32;
        return qtma_EXT((int) l, 16, 12);
    }

    public static int restDuration(int paramInt) {
        return qtma_EXT(paramInt, 0, 24);
    }

    public static int notePitch(int paramInt) {
        return qtma_EXT(paramInt, 18, 6) + 32;
    }

    public static int noteVelocity(int paramInt) {
        return qtma_EXT(paramInt, 11, 7) + 0;
    }

    public static int noteDuration(int paramInt) {
        return qtma_EXT(paramInt, 0, 11);
    }

    public static int xNotePitch(long paramLong) {
        long l = paramLong >>> 32;
        return qtma_EXT((int) l, 0, 16);
    }

    public static int xNoteVelocity(long paramLong) {
        return qtma_EXT((int) paramLong, 22, 7);
    }

    public static int xNoteDuration(long paramLong) {
        return qtma_EXT((int) paramLong, 0, 22);
    }

    public static int controlController(int paramInt) {
        return qtma_EXT(paramInt, 16, 8);
    }

    public static int controlValue(int paramInt) {
        return qtma_EXT(paramInt, 0, 16);
    }

    public static int xControlController(long paramLong) {
        return qtma_EXT((int) paramLong, 0, 16);
    }

    public static int xControlValue(long paramLong) {
        long l = paramLong >>> 32;
        return qtma_EXT((int) l, 0, 16);
    }

    public static int markerSubtype(int paramInt) {
        return qtma_EXT(paramInt, 16, 8);
    }

    public static int markerValue(int paramInt) {
        return qtma_EXT(paramInt, 0, 16);
    }

    public static int xKnobValue(long paramLong) {
        int i = (int) (paramLong >>> 32);
        int j = (int) paramLong;
        return qtma_EXT(i, 0, 16) << 16 | qtma_EXT(j, 0, 16);
    }

    public static int xKnobID(long paramLong) {
        return qtma_EXT((int) paramLong, 16, 14);
    }

    public static int generalSubtype_Footer(int paramInt) {
        return qtma_EXT(paramInt, 16, 14);
    }

    public static int generalLength(int paramInt) {
        return qtma_EXT(paramInt, 0, 16);
    }

    private static int qtma_MASK(int paramInt) {
        return (1 << paramInt) - 1;
    }

    private static int qtma_EXT(int paramInt1, int paramInt2, int paramInt3) {
        return EndianOrder.flipBigEndianToNative32(paramInt1) >>> paramInt2 & qtma_MASK(paramInt3);
    }

    public MusicData(int paramInt) throws QTException {
        super(paramInt, true);

        setIntAt(paramInt - 4, EndianOrder.flipNativeToBigEndian32(1610612736));
    }

    public void setSize(int paramInt) throws UtilException {
        super.setSize(paramInt);
        setIntAt(paramInt - 4, EndianOrder.flipNativeToBigEndian32(1610612736));
    }

    public int getMusicEvent(int paramInt) throws QTException {
        if (paramInt * 4 >= getSize() - 4)
            throw new QTException(-50);
        return getIntAt(paramInt * 4);
    }

    public void setMusicEvent(int paramInt1, int paramInt2) throws QTException {
        if (paramInt1 * 4 >= getSize())
            throw new QTException(-50);
        if ((paramInt1 * 4 == getSize() - 4) && (paramInt2 != EndianOrder.flipNativeToBigEndian32(1610612736))) {
            throw new QTException(-50);
        }
        setIntAt(paramInt1 * 4, paramInt2);
    }

    public long getXMusicEvent(int paramInt) throws QTException {
        if (paramInt * 4 >= getSize() - 8)
            throw new QTException(-50);
        return getLongAt(paramInt * 4);
    }

    public void setXMusicEvent(int paramInt, long paramLong) throws QTException {
        if (paramInt * 4 >= getSize() - 8)
            throw new QTException(-50);
        setLongAt(paramInt * 4, paramLong);
    }

    public void setNoteRequest(int paramInt1, int paramInt2, NoteRequest paramNoteRequest) throws QTException {
        if (paramInt1 * 4 >= getSize() - 92) {
            throw new QTException(-50);
        }

        setIntAt(paramInt1 * 4, stuffGeneralEvent_Header(paramInt2, 23));
        copyFromArray(paramInt1 * 4 + 4, paramNoteRequest.getBytes(), 0, paramNoteRequest.getSize());

        setIntAt(paramInt1 * 4 + 4 + paramNoteRequest.getSize(), stuffGeneralEvent_Footer(1, 23));
    }

    public NoteRequest getNoteRequest(int paramInt) throws QTException {
        if (paramInt * 4 >= getSize() - 92) {
            throw new QTException(-50);
        }
        int i = getIntAt(paramInt * 4 + 84);

        if (generalSubtype_Footer(i) != 1) {
            throw new QTException(-50);
        }
        NoteRequest localNoteRequest = new NoteRequest();
        copyToArray(paramInt * 4, localNoteRequest.getBytes(), 0, localNoteRequest.getSize());
        return localNoteRequest;
    }

    public void setAtomicInstrument(int paramInt1, int paramInt2, AtomicInstrument paramAtomicInstrument) throws QTException {
        int i = paramAtomicInstrument.getSize() / 4;
        if (paramAtomicInstrument.getSize() % 4 != 0) {
            i++;
        }
        int j = i + 2;
        if (paramInt1 * 4 >= getSize() - j * 4) {
            throw new QTException(-50);
        }
        setIntAt(paramInt1 * 4, stuffGeneralEvent_Header(paramInt2, j));
        copyFromHandle(paramInt1 * 4 + 4, paramAtomicInstrument, 0, paramAtomicInstrument.getSize());
        setIntAt(paramInt1 * 4 + 4 + i * 4, stuffGeneralEvent_Footer(6, j));
    }

    public AtomicInstrument getAtomicInstrument(int paramInt) throws QTException {
        if (paramInt * 4 > getSize() - 4) {
            throw new QTException(-50);
        }

        int i = getIntAt(paramInt * 4 - 4);

        int j = generalLength(i) * 4;

        if (paramInt * 4 - 4 + j > getSize()) {
            throw new QTException(-50);
        }

        int k = getIntAt(paramInt * 4 - 4 + j - 4);

        if (generalSubtype_Footer(k) != 6) {
            throw new QTException(-50);
        }

        int m = makeAndCopyHandle(paramInt * 4, j - 8);
        return new AtomicInstrument(m, null);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /MusicData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
