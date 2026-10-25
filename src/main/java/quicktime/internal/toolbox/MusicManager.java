/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * MusicManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class MusicManager {

    private MusicManager() {
    }

    /** used by MusicComponent */
    public static int MusicGetDescription(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MusicGetDescription");
    }

    /** used by MusicComponent */
    public static int MusicGetDrumKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MusicGetDrumKnobDescription");
    }

    /** used by MusicComponent */
    public static int MusicGetInstrumentKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MusicGetInstrumentKnobDescription");
    }

    /** used by MusicComponent */
    public static int MusicGetKnob(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MusicGetKnob");
    }

    /** used by MusicComponent */
    public static int MusicGetKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MusicGetKnobDescription");
    }

    /** used by MusicComponent */
    public static int MusicGetMasterTune(int paramInt) {
        throw new UnsupportedToolboxException("MusicGetMasterTune");
    }

    /** used by MusicComponent */
    public static int MusicGetPartAtomicInstrument(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        throw new UnsupportedToolboxException("MusicGetPartAtomicInstrument");
    }

    /** used by MusicComponent */
    public static int MusicGetPartName(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MusicGetPartName");
    }

    /** used by TunePlayer */
    public static int MusicMediaGetIndexedTunePlayer(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MusicMediaGetIndexedTunePlayer");
    }

    /** used by MusicComponent */
    public static int MusicSetKnob(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MusicSetKnob");
    }

    /** used by MusicComponent */
    public static int MusicSetMasterTune(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MusicSetMasterTune");
    }

    /** used by MusicComponent */
    public static int MusicSetPartName(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MusicSetPartName");
    }

    /** used by NoteAllocator */
    public static int NACopyrightDialog(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("NACopyrightDialog");
    }

    /** used by QTObjectManagement */
    public static int NADisposeNoteChannel(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("NADisposeNoteChannel");
    }

    /** used by NoteChannel */
    public static int NAFindNoteChannelTone(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NAFindNoteChannelTone");
    }

    /** used by NoteChannel */
    public static int NAGetController(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NAGetController");
    }

    /** used by NoteAllocator */
    public static int NAGetDefaultMIDIInput(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("NAGetDefaultMIDIInput");
    }

    /** used by NoteAllocator */
    public static int NAGetIndNoteChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("NAGetIndNoteChannel");
    }

    /** used by NoteChannel */
    public static int NAGetKnob(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NAGetKnob");
    }

    /** used by NoteAllocator */
    public static int NAGetMIDIPorts(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("NAGetMIDIPorts");
    }

    /** used by NoteChannel */
    public static int NAGetNoteChannelInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("NAGetNoteChannelInfo");
    }

    /** used by NoteChannel */
    public static int NAGetNoteRequest(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("NAGetNoteRequest");
    }

    /** used by NoteChannel */
    public static int NAGetRegisteredMusicDevice(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NAGetRegisteredMusicDevice");
    }

    /** used by NoteAllocator */
    public static int NAGetRegisteredMusicDevice(int paramInt1, int paramInt2, int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("NAGetRegisteredMusicDevice");
    }

    /** used by NoteAllocator */
    public static int NALoseDefaultMIDIInput(int paramInt) {
        throw new UnsupportedToolboxException("NALoseDefaultMIDIInput");
    }

    /** used by NoteChannel */
    public static int NANewNoteChannel(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NANewNoteChannel");
    }

    /** used by AtomicInstrument */
    public static int NANewNoteChannelFromAtomicInstrument(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NANewNoteChannelFromAtomicInstrument");
    }

    /** used by NoteAllocator */
    public static int NAPickArrangement(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("NAPickArrangement");
    }

    /** used by AtomicInstrument, NoteChannel */
    public static int NAPickEditInstrument(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("NAPickEditInstrument");
    }

    /** used by ToneDescription */
    public static int NAPickInstrument(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("NAPickInstrument");
    }

    /** used by NoteChannel */
    public static int NAPlayNote(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("NAPlayNote");
    }

    /** used by NoteChannel */
    public static int NAPrerollNoteChannel(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("NAPrerollNoteChannel");
    }

    /** used by NoteAllocator */
    public static int NARegisterMusicDevice(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("NARegisterMusicDevice");
    }

    /** used by NoteChannel */
    public static int NAResetNoteChannel(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("NAResetNoteChannel");
    }

    /** used by NoteAllocator */
    public static int NASaveMusicConfiguration(int paramInt) {
        throw new UnsupportedToolboxException("NASaveMusicConfiguration");
    }

    /** used by NoteChannel */
    public static int NASendMIDI(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("NASendMIDI");
    }

    /** used by NoteChannel */
    public static int NASetAtomicInstrument(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("NASetAtomicInstrument");
    }

    /** used by NoteChannel */
    public static int NASetController(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("NASetController");
    }

    /** used by NoteAllocator */
    public static int NASetDefaultMIDIInput(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("NASetDefaultMIDIInput");
    }

    /** used by NoteChannel */
    public static int NASetInstrumentNumber(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("NASetInstrumentNumber");
    }

    /** used by NoteChannel */
    public static int NASetKnob(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("NASetKnob");
    }

    /** used by NoteChannel */
    public static int NASetNoteChannelBalance(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("NASetNoteChannelBalance");
    }

    /** used by NoteChannel */
    public static int NASetNoteChannelSoundLocalization(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("NASetNoteChannelSoundLocalization");
    }

    /** used by NoteChannel */
    public static int NASetNoteChannelVolume(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("NASetNoteChannelVolume");
    }

    /** used by ToneDescription */
    public static int NAStuffToneDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("NAStuffToneDescription");
    }

    /** used by NoteAllocator */
    public static int NATask(int paramInt) {
        throw new UnsupportedToolboxException("NATask");
    }

    /** used by NoteAllocator */
    public static int NAUnregisterMusicDevice(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("NAUnregisterMusicDevice");
    }

    /** used by NoteChannel */
    public static int NAUnrollNoteChannel(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("NAUnrollNoteChannel");
    }

    /** used by TunePlayer */
    public static int TuneGetIndexedNoteChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TuneGetIndexedNoteChannel");
    }

    /** used by TunePlayer */
    public static int TuneGetNoteAllocator(int paramInt) {
        throw new UnsupportedToolboxException("TuneGetNoteAllocator");
    }

    /** used by TunePlayer */
    public static int TuneGetPartMix(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("TuneGetPartMix");
    }

    /** used by TunePlayer */
    public static int TuneGetStatus(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("TuneGetStatus");
    }

    /** used by TimeBase */
    public static int TuneGetTimeBase(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TuneGetTimeBase");
    }

    /** used by TunePlayer */
    public static int TuneGetTimeScale(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TuneGetTimeScale");
    }

    /** used by TunePlayer */
    public static int TuneGetVolume(int paramInt) {
        throw new UnsupportedToolboxException("TuneGetVolume");
    }

    /** used by TunePlayer */
    public static int TuneInstant(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("TuneInstant");
    }

    /** used by TunePlayer */
    public static int TunePreroll(int paramInt) {
        throw new UnsupportedToolboxException("TunePreroll");
    }

    /** used by TunePlayer */
    public static int TuneQueue(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        throw new UnsupportedToolboxException("TuneQueue");
    }

    /** used by TunePlayer */
    public static int TuneSetBalance(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TuneSetBalance");
    }

    /** used by TunePlayer */
    public static int TuneSetHeader(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TuneSetHeader");
    }

    /** used by TunePlayer */
    public static int TuneSetHeaderWithSize(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("TuneSetHeaderWithSize");
    }

    /** used by TunePlayer */
    public static int TuneSetNoteChannels(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("TuneSetNoteChannels");
    }

    /** used by TunePlayer */
    public static int TuneSetPartMix(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("TuneSetPartMix");
    }

    /** used by TunePlayer */
    public static int TuneSetPartTranspose(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("TuneSetPartTranspose");
    }

    /** used by TunePlayer */
    public static int TuneSetSofter(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TuneSetSofter");
    }

    /** used by TunePlayer */
    public static int TuneSetSoundLocalization(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TuneSetSoundLocalization");
    }

    /** used by TunePlayer */
    public static int TuneSetTimeScale(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TuneSetTimeScale");
    }

    /** used by TunePlayer */
    public static int TuneSetVolume(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TuneSetVolume");
    }

    /** used by TunePlayer */
    public static int TuneStop(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TuneStop");
    }

    /** used by TunePlayer */
    public static int TuneTask(int paramInt) {
        throw new UnsupportedToolboxException("TuneTask");
    }

    /** used by TunePlayer */
    public static int TuneUnroll(int paramInt) {
        throw new UnsupportedToolboxException("TuneUnroll");
    }

}
