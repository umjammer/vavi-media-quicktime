package quicktime.std.music;

import quicktime.QTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTHandle;
import quicktime.util.UtilException;

/**
 * MusicData objects contain general information for a Music Track that the TunePlayer
 * uses when it instantiates NoteChannels for the parts contained in the music media.
 * MusicData object's internal data are all aligned on int values (ie. 32bit data segments)
 * so the offsets for getting and setting values in this structure are expressed in
 * terms of these 32bit values.
 *
 * Data in MusicData is in big endian order and the stuff static methods return
 * values are in the correct big endian format and order to be added to a
 * MusicData object as is. If an application writes its own stuff methods they
 * can return the data in big endian order to ensure compatibility with the existing stuff methods.
 * Thus when music events are added to the MusicData they must be in big endian format
 * and the values retrieved from a constructed MusicData object will be in big endian order.
 *
 * Many of the macros that are provided in the QuickTimeMusic.h file are translated here
 * as static methods. The names of the macros are represented here with the qtma_ removed
 * and the first letter after the _ is NOT a capital. Thus qtma_StuffRestEvent becomes stuffRestEvent.
 *
 * An application does NOT need to set the end of sequence marker event if this marker should
 * be set as the last music data element in the MusicData object. The set... and get... calls will
 * check for proper bounds and include a check that allows for the required existence
 * of the end of sequence marker.
 */
public class MusicData extends QTHandle {

    /**
     * The size (numer of ints) of a kNoteRequestEvent (including its header and footer)
     */
    public static final int kNoteRequestHeaderEventLength = 23;

    /**
     * Returns an int that is the stuffed representation of the header portion of a general music event.
     * @param part the part that the event applies to
     * @param length the length (including the surrounding general event header and footer)
     *  of the size of the general event. This size is in bytes.
     * @return the stuffed representation of this type of music event in big endian order
     */
    public static int stuffGeneralEvent_Header(int paramInt1, int paramInt2) {
        int i = 0xF0000000 | paramInt1 << 16 | paramInt2 << 0;
        return EndianOrder.flipNativeToBigEndian32(i);
    }

    /**
     * Returns an int that is the stuffed representation of the footer portion of a general music event.
     * @param length the length (including the surrounding general event header and footer)
     *  of the size of the general event. This size is in bytes.
     * @return the stuffed representation of this type of music event in big endian order
     */
    public static int stuffGeneralEvent_Footer(int paramInt1, int paramInt2) {
        int i = 0xC0000000 | paramInt1 << 16 | paramInt2 << 0;
        return EndianOrder.flipNativeToBigEndian32(i);
    }

    /**
     * Returns an int that is the stuffed representation of the rest music event.
     * @param duration the duration in the time scale of the TunePlayer or the MusicMedia
     * @return the stuffed representation of this type of music event in big endian order
     */
    public static int stuffRestEvent(int paramInt) {
        int i = 0x0 | paramInt << 0;
        return EndianOrder.flipNativeToBigEndian32(i);
    }

    /**
     * Returns an int that is the stuffed representation of the note music event.
     * @param part the part that the note is played by
     * @param pitch the MIDI note pitch of the note - valid range is 32 - 95 (with middle C as 60)
     * @param velocity the note-on velocity when the note is played
     * @param duration the duration in the time scale of the TunePlayer or the MusicMedia
     * @return the stuffed representation of this type of music event in big endian order
     */
    public static int stuffNoteEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = 0x20000000 | paramInt1 << 24 | paramInt2 - 32 << 18 | paramInt3 - 0 << 11 | paramInt4 << 0;
        return EndianOrder.flipNativeToBigEndian32(i);
    }

    /**
     * Returns an int that is the stuffed representation of the control music event.
     * @param part the part that the note is played by
     * @param control the number of the controller
     * @param value 8.8 bit fixed point signed controller specific value
     * @return the stuffed representation of this type of music event in big endian order
     */
    public static int stuffControlEvent(int paramInt1, int paramInt2, int paramInt3) {
        int i = 0x40000000 | paramInt1 << 24 | paramInt2 << 16 | (paramInt3 & qtma_MASK(16)) << 0;
        return EndianOrder.flipNativeToBigEndian32(i);
    }

    /**
     * Returns an int that is the stuffed representation of the marker music event.
     * @param markerType the type of marker that is required
     * @param markerValue the value applied to the specified marker
     * @return the stuffed representation of this type of music event in big endian order
     */
    public static int stuffMarkerEvent(int paramInt1, int paramInt2) {
        int i = 0x60000000 | paramInt1 << 16 | paramInt2 << 0;
        return EndianOrder.flipNativeToBigEndian32(i);
    }

    /**
     * Returns a long that is the stuffed representation of the extended note music event.
     * Extended music events are 2 big endian ints stuffed into a long.
     * @param part the part that the note is played by
     * @param pitch a 8.8 pitch value where the high 8 bits are the MIDI note number and the low 8 bits represent some fractional value of a semitone
     * @param duration the duration in the time scale of the TunePlayer or the MusicMedia
     * @return the stuffed representation of this type of extended music event.
     */
    public static long stuffXNoteEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        long l1 = 0x90000000 | paramInt1 << 16 | paramInt2 << 0;
        l1 = EndianOrder.flipNativeToBigEndian64(l1);
        long l2 = 0x80000000 | paramInt4 << 0 | paramInt3 << 22;
        l2 = EndianOrder.flipNativeToBigEndian64(l2);
        l1 &= 0xFFFFFFFF;
        l2 &= 0xFFFFFFFF;
        return l1 << 32 | l2;
    }

    /**
     * Returns a long that is the stuffed representation of the extended note music event.
     * Extended music events are 2 big endian ints stuffed into a long.
     * @param part the part that the note is played by
     * @param control the number of the controller
     * @param value 8.8 bit fixed point signed controller specific value
     * @return the stuffed representation of this type of extended music event.
     */
    public static long stuffXControlEvent(int paramInt1, int paramInt2, int paramInt3) {
        long l1 = 0xA0000000 | paramInt1 << 16 | (paramInt3 & qtma_MASK(16)) << 0;
        l1 = EndianOrder.flipNativeToBigEndian64(l1);
        long l2 = 0x80000000 | paramInt2 << 0;
        l2 = EndianOrder.flipNativeToBigEndian64(l2);
        l1 &= 0xFFFFFFFF;
        l2 &= 0xFFFFFFFF;
        return l1 << 32 | l2;
    }

    /**
     * Returns a long that is the stuffed representation of the extended note music event.
     * Extended music events are 2 big endian ints stuffed into a long.
     * @param part the part that the knob is applied too
     * @param knob the knob
     * @param value the new value for the specified knob
     * @return the stuffed representation of this type of extended music event.
     */
    public static long stuffXKnobEvent(int paramInt1, int paramInt2, int paramInt3) {
        long l1 = 0xB0000000 | paramInt1 << 16 | paramInt3 >> 16 << 0;
        l1 = EndianOrder.flipNativeToBigEndian64(l1);
        long l2 = 0x80000000 | paramInt2 << 16 | (paramInt3 & 0xFFFF) << 0;
        l2 = EndianOrder.flipNativeToBigEndian64(l2);
        l1 &= 0xFFFFFFFF;
        l2 &= 0xFFFFFFFF;
        return l1 << 32 | l2;
    }

    /**
     * Given a stuffed big-endian order QuickTime music event this method
     * will extract the event type.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the music event
     */
    public static int eventType(int paramInt) {
        return qtma_EXT(paramInt, 29, 3) > 3 ? qtma_EXT(paramInt, 28, 4) : qtma_EXT(paramInt, 29, 3);
    }

    /**
     * Given a stuffed big-endian order QuickTime music event this method
     * will extract the part number.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the music event
     */
    public static int part(int paramInt) {
        return qtma_EXT(paramInt, 24, 5);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the part number.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int xPart(long paramLong) {
        long l = paramLong >>> 32;
        return qtma_EXT((int) l, 16, 12);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the rest duration.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int restDuration(int paramInt) {
        return qtma_EXT(paramInt, 0, 24);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the note pitch.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int notePitch(int paramInt) {
        return qtma_EXT(paramInt, 18, 6) + 32;
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the note velocity.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int noteVelocity(int paramInt) {
        return qtma_EXT(paramInt, 11, 7) + 0;
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the note duration.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int noteDuration(int paramInt) {
        return qtma_EXT(paramInt, 0, 11);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the note pitch.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int xNotePitch(long paramLong) {
        long l = paramLong >>> 32;
        return qtma_EXT((int) l, 0, 16);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the note velocity.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int xNoteVelocity(long paramLong) {
        return qtma_EXT((int) paramLong, 22, 7);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the note duration.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int xNoteDuration(long paramLong) {
        return qtma_EXT((int) paramLong, 0, 22);
    }

    /**
     * Given a stuffed big-endian order QuickTime music event this method
     * will extract the controller number.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the music event
     */
    public static int controlController(int paramInt) {
        return qtma_EXT(paramInt, 16, 8);
    }

    /**
     * Given a stuffed big-endian order QuickTime music event this method
     * will extract the controller's specified value.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the music event
     */
    public static int controlValue(int paramInt) {
        return qtma_EXT(paramInt, 0, 16);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the controller number.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int xControlController(long paramLong) {
        return qtma_EXT((int) paramLong, 0, 16);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the controller number.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int xControlValue(long paramLong) {
        long l = paramLong >>> 32;
        return qtma_EXT((int) l, 0, 16);
    }

    /**
     * Given a stuffed big-endian order QuickTime music event this method
     * will extract the marker's sub type.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the music event
     */
    public static int markerSubtype(int paramInt) {
        return qtma_EXT(paramInt, 16, 8);
    }

    /**
     * Given a stuffed big-endian order QuickTime music event this method
     * will extract the marker's value.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the music event
     */
    public static int markerValue(int paramInt) {
        return qtma_EXT(paramInt, 0, 16);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the knob's value.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int xKnobValue(long paramLong) {
        int i = (int) (paramLong >>> 32);
        int j = (int) paramLong;
        return qtma_EXT(i, 0, 16) << 16 | qtma_EXT(j, 0, 16);
    }

    /**
     * Given a stuffed big-endian order QuickTime extended music event this method
     * will extract the knob's value.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the extended music event
     */
    public static int xKnobID(long paramLong) {
        return qtma_EXT((int) paramLong, 16, 14);
    }

    /**
     * Given a stuffed big-endian order QuickTime music event this method
     * will extract the general event's sub type from the footer of the general event.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the music event
     */
    public static int generalSubtype_Footer(int paramInt) {
        return qtma_EXT(paramInt, 16, 14);
    }

    /**
     * Given a stuffed big-endian order QuickTime music event this method
     * will extract the general event's length from either header or the footer of the general event.
     * @param x the stuffed big-endian music event
     * @return the desired sub field of the music event
     */
    public static int generalLength(int paramInt) {
        return qtma_EXT(paramInt, 0, 16);
    }

    private static int qtma_MASK(int paramInt) {
        return (1 << paramInt) - 1;
    }

    private static int qtma_EXT(int paramInt1, int paramInt2, int paramInt3) {
        return EndianOrder.flipBigEndianToNative32(paramInt1) >>> paramInt2 & qtma_MASK(paramInt3);
    }

    /**
     * Create a MusicData object of the specified number of bytes.
     * The end of sequence marker is inserted in the last four bytes of the MusicData object.
     * @param size the size in bytes of the required MusicData object
     */
    public MusicData(int paramInt) throws QTException {
        super(paramInt, true);
        setIntAt(paramInt - 4, EndianOrder.flipNativeToBigEndian32(1610612736));
    }

    /**
     * Resets the size of the MusicData object to the new size (in bytes).
     * The end of sequence marker is inserted in the last four bytes of the MusicData object.
     * @param newSize - the new size of the MusicData object.
     * @param {@link quicktime.util.UtilException}
     */
    public void setSize(int paramInt) throws UtilException {
        super.setSize(paramInt);
        setIntAt(paramInt - 4, EndianOrder.flipNativeToBigEndian32(1610612736));
    }

    /**
     * Returns the big endian ordered stuffed QuickTime music event at the specified number of
     * ints into the MusicData.
     * @param offset - the number of ints in the header
     * @return the big endian ordered music event
     * @throws quicktime.QTException if the offset would get data past the end of the MusicData
     */
    public int getMusicEvent(int paramInt) throws QTException {
        if (paramInt * 4 >= getSize() - 4)
            throw new QTException(-50);
        return getIntAt(paramInt * 4);
    }

    /**
     * Sets the big endian ordered stuffed QuickTime music event at the specified number of
     * ints into the MusicData.
     *
     * An application can only set the last four bytes if the value
     * of the musicData in this case is the end of sequence marker - this is generally not required
     * as it will be set by the constructor and setSize calls.
     * @param offset - the number of ints in the header
     * @param musicData the big endian ordered music event
     * @throws quicktime.QTException if the offset would set data past the end of the MusicData
     */
    public void setMusicEvent(int paramInt1, int paramInt2) throws QTException {
        if (paramInt1 * 4 >= getSize())
            throw new QTException(-50);
        if ((paramInt1 * 4 == getSize() - 4) && (paramInt2 != EndianOrder.flipNativeToBigEndian32(1610612736))) {
            throw new QTException(-50);
        }
        setIntAt(paramInt1 * 4, paramInt2);
    }

    /**
     * Returns the stuffed QuickTime extended music event at the specified number of
     * ints into the MusicData. Extended music events are two big endian ints stuffed into a long.
     * @param offset - the number of ints in the header
     * @return the music event - two big endian ints stuffed into a long
     * @throws quicktime.QTException if the offset would get data past the end of the MusicData
     *  minus 4 bytes which is the end of sequence marker - which is not an extended music event.
     */
    public long getXMusicEvent(int paramInt) throws QTException {
        if (paramInt * 4 >= getSize() - 8)
            throw new QTException(-50);
        return getLongAt(paramInt * 4);
    }

    /**
     * Sets the stuffed QuickTime extended music event at the specified number of
     * ints into the MusicData. Extended music events are two big endian ints stuffed into a long.
     * @param offset - the number of ints in the header
     * @param musicData the music event - two big endian ints stuffed into a long
     * @throws quicktime.QTException if the offset would set data past the end of the MusicData
     *  minus 4 bytes which is the end of sequence marker - which is not an extended music event.
     */
    public void setXMusicEvent(int paramInt, long paramLong) throws QTException {
        if (paramInt * 4 >= getSize() - 8)
            throw new QTException(-50);
        setLongAt(paramInt * 4, paramLong);
    }

    /**
     * Copies the NoteRequest data to the specified offset (numer of
     * ints) into the MusicData. This will also setup the note request
     * header and tail values - the part number is used to associate
     * the given note request with a part reference in the concommitant
     * music data.
     *
     * When a note request event is stuffed into music data it is surrounded
     * by two ints:
     * 0xFnnn0017 where nnn is the part number - signifies start of note request event (0x0017 is the size of a note request event)
     * the note request data
     * 0xC0010017	-> signifies end of note request event
     *
     * Thus when calculating offsets for the position in the MusicData for a second NoteRequest event
     * it is (given that the first note request is event is at the start of the music data and its offset is 1 as the header event takes up the first entry):
     * nextNoteEventOffset = StdQTConstants.kNoteRequestHeaderEventLength
     * The size (the number of ints) of a NoteRequest event (inc. header and footer) is supplied as the StdQTConstants.kNoteRequestHeaderEventLength constant in this class.
     *
     * @param offset - the number of music events (ints) into the header
     * @param partNumber - the partNumber that is associated with this note request. The partNumber
     *  must be in the range of 0 to 0xFFF - the method will itself do the bit shifting required for the noteRequestEvent
     * @param nr the NoteRequest to copy
     * @throws quicktime.QTException if the offset would set data past the end of the MusicData
     *  minus 4 bytes which is the end of sequence marker - which is not a general music event.
     */
    public void setNoteRequest(int paramInt1, int paramInt2, NoteRequest paramNoteRequest) throws QTException {
        if (paramInt1 * 4 >= getSize() - 92) {
            throw new QTException(-50);
        }
        setIntAt(paramInt1 * 4, stuffGeneralEvent_Header(paramInt2, 23));
        copyFromArray(paramInt1 * 4 + 4, paramNoteRequest.getBytes(), 0, paramNoteRequest.getSize());
        setIntAt(paramInt1 * 4 + 4 + paramNoteRequest.getSize(), stuffGeneralEvent_Footer(1, 23));
    }

    /**
     * Returns a NoteRequest object from the specified offset (numer of
     * ints) into the MusicData. This checks to see if the data
     * at that offset is a valid NoteRequest structure - if not a paramErr is thrown.
     * Thus if retrieving a previously set NoteRequest the offset should take into account the addition
     * of the head and tail fields of the setNoteRequest.
     * @param offset - the number of ints in the header
     * @return the NoteRequest
     * @throws quicktime.QTException if the offset would get data past the end of the MusicData
     *  minus 4 bytes which is the end of sequence marker - which is not a general music event.
     */
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

    /**
     * Copies the AtomicInstrument data to the specified offset (numer of
     * ints) into the MusicData. This will also setup the atomic instrument
     * header and tail values - the part number is used to associate
     * the given instrument with a part reference in the concommitant
     * music data.
     *
     * When an AtomicInstrument event is stuffed into music data it is surrounded
     * by two music events (ints):
     * 0xFnnnxxxx where nnn is the part number - signifies start of note request event (xxxx is the size of an atomic instrument + head and tail event)
     * the AtomicInstrument data
     * 0xC006xxxx	-> signifies end of atomic instrument event (xxxx is the identical size as the header)
     *
     * Thus when calculating offsets for the position in the MusicData for a second AtomicInstrument event
     * it is:
     * nextAtomicInstOffset = sizeOfPreviousGeneralEvent + 1 (where 1 is the header event for the ai you are setting)
     *
     * @param offset - the number of ints into the header
     * @param partNumber - the partNumber that is associated with this atomic instrument. The partNumber
     *  must be in the range of 0 to 0xFFF - the method will itself do the bit shifting required for the noteRequestEvent
     * @param ai the AtomicInstrument to copy
     * @throws quicktime.QTException if the offset would set data past the end of the MusicData
     *  minus 4 bytes which is the end of sequence marker - which is not a general music event.
     */
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

    /**
     * Returns an AtomicInstrument from the specified offset (numer of
     * ints) into the MusicData. This checks to see if the data
     * at that offset is a valid AtomicInstrument structure - if not a paramErr exception is thrown
     * Thus if retrieving a previously set AtomicInstrument the offset should take into account the addition
     * of the head and tail fields of the AtomicInstrument event.
     * @param offset - the number of ints in the header
     * @return an AtomicInstrument
     * @throws quicktime.QTException if the offset would get data past the end of the MusicData
     *  minus 4 bytes which is the end of sequence marker - which is not a general music event.
     */
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
