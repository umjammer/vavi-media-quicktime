/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


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
    public static int MusicGetDescription(int mc, byte[] pd) {
        throw new UnsupportedToolboxException("MusicGetDescription");
    }

    /** used by MusicComponent */
    public static int MusicGetDrumKnobDescription(int mc, int knobIndex, byte[] mkd) {
        throw new UnsupportedToolboxException("MusicGetDrumKnobDescription");
    }

    /** used by MusicComponent */
    public static int MusicGetInstrumentKnobDescription(int mc, int knobIndex, byte[] mkd) {
        throw new UnsupportedToolboxException("MusicGetInstrumentKnobDescription");
    }

    /** used by MusicComponent */
    public static int MusicGetKnob(int mc, int knobID) {
        throw new UnsupportedToolboxException("MusicGetKnob");
    }

    /** used by MusicComponent */
    public static int MusicGetKnobDescription(int mc, int knobIndex, byte[] mkd) {
        throw new UnsupportedToolboxException("MusicGetKnobDescription");
    }

    /** used by MusicComponent */
    public static int MusicGetMasterTune(int mc) {
        throw new UnsupportedToolboxException("MusicGetMasterTune");
    }

    /** used by MusicComponent */
    public static int MusicGetPartAtomicInstrument(int mc, int part, int[] ai, int flags) {
        throw new UnsupportedToolboxException("MusicGetPartAtomicInstrument");
    }

    /** used by MusicComponent */
    public static int MusicGetPartName(int mc, int part, byte[] name) {
        throw new UnsupportedToolboxException("MusicGetPartName");
    }

    /** used by TunePlayer */
    public static int MusicMediaGetIndexedTunePlayer(int ti, int sampleDescIndex, int[] tp) {
        throw new UnsupportedToolboxException("MusicMediaGetIndexedTunePlayer");
    }

    /** used by MusicComponent */
    public static int MusicSetKnob(int mc, int knobID, int knobValue) {
        throw new UnsupportedToolboxException("MusicSetKnob");
    }

    /** used by MusicComponent */
    public static int MusicSetMasterTune(int mc, int masterTune) {
        throw new UnsupportedToolboxException("MusicSetMasterTune");
    }

    /** used by MusicComponent */
    public static int MusicSetPartName(int mc, int part, byte[] name) {
        throw new UnsupportedToolboxException("MusicSetPartName");
    }

    /** used by NoteAllocator */
    public static int NACopyrightDialog(int na, int p, byte[] author, byte[] copyright, byte[] other, byte[] title, int filterProc, int refCon) {
        throw new UnsupportedToolboxException("NACopyrightDialog");
    }

    /** used by QTObjectManagement */
    public static int NADisposeNoteChannel(int na, int noteChannel) {
        throw new UnsupportedToolboxException("NADisposeNoteChannel");
    }

    /** used by NoteChannel */
    public static int NAFindNoteChannelTone(int na, int noteChannel, byte[] td, int[] instrumentNumber) {
        throw new UnsupportedToolboxException("NAFindNoteChannelTone");
    }

    /** used by NoteChannel */
    public static int NAGetController(int na, int noteChannel, int controllerNumber, int[] controllerValue) {
        throw new UnsupportedToolboxException("NAGetController");
    }

    /** used by NoteAllocator */
    public static int NAGetDefaultMIDIInput(int na, byte[] midiIn) {
        throw new UnsupportedToolboxException("NAGetDefaultMIDIInput");
    }

    /** used by NoteAllocator */
    public static int NAGetIndNoteChannel(int na, int index, int[] noteChannel, int[] count) {
        throw new UnsupportedToolboxException("NAGetIndNoteChannel");
    }

    /** used by NoteChannel */
    public static int NAGetKnob(int na, int noteChannel, int knobNumber, int[] knobValue) {
        throw new UnsupportedToolboxException("NAGetKnob");
    }

    /** used by NoteAllocator */
    public static int NAGetMIDIPorts(int na, int[] inputPorts, int[] outputPorts) {
        throw new UnsupportedToolboxException("NAGetMIDIPorts");
    }

    /** used by NoteChannel */
    public static int NAGetNoteChannelInfo(int na, int noteChannel, int[] index, int[] part) {
        throw new UnsupportedToolboxException("NAGetNoteChannelInfo");
    }

    /** used by NoteChannel */
    public static int NAGetNoteRequest(int na, int noteChannel, byte[] nrOut) {
        throw new UnsupportedToolboxException("NAGetNoteRequest");
    }

    /** used by NoteChannel */
    public static int NAGetRegisteredMusicDevice(int na, int index, int synthType, int name, int connections, int[] mc) {
        throw new UnsupportedToolboxException("NAGetRegisteredMusicDevice");
    }

    /** used by NoteAllocator */
    public static int NAGetRegisteredMusicDevice(int na, int index, int[] synthType, byte[] name, byte[] connections, int[] mc) {
        throw new UnsupportedToolboxException("NAGetRegisteredMusicDevice");
    }

    /** used by NoteAllocator */
    public static int NALoseDefaultMIDIInput(int na) {
        throw new UnsupportedToolboxException("NALoseDefaultMIDIInput");
    }

    /** used by NoteChannel */
    public static int NANewNoteChannel(int na, byte[] noteRequest, int[] outChannel) {
        throw new UnsupportedToolboxException("NANewNoteChannel");
    }

    /** used by AtomicInstrument */
    public static int NANewNoteChannelFromAtomicInstrument(int na, int instrument, int flags, int[] outChannel) {
        throw new UnsupportedToolboxException("NANewNoteChannelFromAtomicInstrument");
    }

    /** used by NoteAllocator */
    public static int NAPickArrangement(int na, int filterProc, byte[] prompt, int zero1, int zero2, int t, byte[] songName) {
        throw new UnsupportedToolboxException("NAPickArrangement");
    }

    /** used by AtomicInstrument, NoteChannel */
    public static int NAPickEditInstrument(int na, int filterProc, byte[] prompt, int refCon, int nc, int instrument, int modifiers) {
        throw new UnsupportedToolboxException("NAPickEditInstrument");
    }

    /** used by ToneDescription */
    public static int NAPickInstrument(int na, int filterProc, byte[] prompt, byte[] sd, int flags, int refCon, int reserved1, int reserved2) {
        throw new UnsupportedToolboxException("NAPickInstrument");
    }

    /** used by NoteChannel */
    public static int NAPlayNote(int na, int noteChannel, int pitch, int velocity) {
        throw new UnsupportedToolboxException("NAPlayNote");
    }

    /** used by NoteChannel */
    public static int NAPrerollNoteChannel(int na, int noteChannel) {
        throw new UnsupportedToolboxException("NAPrerollNoteChannel");
    }

    /** used by NoteAllocator */
    public static int NARegisterMusicDevice(int na, int synthType, byte[] name, byte[] connections) {
        throw new UnsupportedToolboxException("NARegisterMusicDevice");
    }

    /** used by NoteChannel */
    public static int NAResetNoteChannel(int na, int noteChannel) {
        throw new UnsupportedToolboxException("NAResetNoteChannel");
    }

    /** used by NoteAllocator */
    public static int NASaveMusicConfiguration(int na) {
        throw new UnsupportedToolboxException("NASaveMusicConfiguration");
    }

    /** used by NoteChannel */
    public static int NASendMIDI(int na, int noteChannel, byte[] mp) {
        throw new UnsupportedToolboxException("NASendMIDI");
    }

    /** used by NoteChannel */
    public static int NASetAtomicInstrument(int na, int noteChannel, int instrument, int flags) {
        throw new UnsupportedToolboxException("NASetAtomicInstrument");
    }

    /** used by NoteChannel */
    public static int NASetController(int na, int noteChannel, int controllerNumber, int controllerValue) {
        throw new UnsupportedToolboxException("NASetController");
    }

    /** used by NoteAllocator */
    public static int NASetDefaultMIDIInput(int na, byte[] midiIn) {
        throw new UnsupportedToolboxException("NASetDefaultMIDIInput");
    }

    /** used by NoteChannel */
    public static int NASetInstrumentNumber(int na, int noteChannel, int instrumentNumber) {
        throw new UnsupportedToolboxException("NASetInstrumentNumber");
    }

    /** used by NoteChannel */
    public static int NASetKnob(int na, int noteChannel, int knobNumber, int knobValue) {
        throw new UnsupportedToolboxException("NASetKnob");
    }

    /** used by NoteChannel */
    public static int NASetNoteChannelBalance(int na, int noteChannel, int balance) {
        throw new UnsupportedToolboxException("NASetNoteChannelBalance");
    }

    /** used by NoteChannel */
    public static int NASetNoteChannelSoundLocalization(int na, int noteChannel, int data) {
        throw new UnsupportedToolboxException("NASetNoteChannelSoundLocalization");
    }

    /** used by NoteChannel */
    public static int NASetNoteChannelVolume(int na, int noteChannel, int volume) {
        throw new UnsupportedToolboxException("NASetNoteChannelVolume");
    }

    /** used by ToneDescription */
    public static int NAStuffToneDescription(int na, int gmNumber, byte[] td) {
        throw new UnsupportedToolboxException("NAStuffToneDescription");
    }

    /** used by NoteAllocator */
    public static int NATask(int na) {
        throw new UnsupportedToolboxException("NATask");
    }

    /** used by NoteAllocator */
    public static int NAUnregisterMusicDevice(int na, int index) {
        throw new UnsupportedToolboxException("NAUnregisterMusicDevice");
    }

    /** used by NoteChannel */
    public static int NAUnrollNoteChannel(int na, int noteChannel) {
        throw new UnsupportedToolboxException("NAUnrollNoteChannel");
    }

    /** used by TunePlayer */
    public static int TuneGetIndexedNoteChannel(int tp, int i, int[] nc) {
        throw new UnsupportedToolboxException("TuneGetIndexedNoteChannel");
    }

    /** used by TunePlayer */
    public static int TuneGetNoteAllocator(int tp) {
        throw new UnsupportedToolboxException("TuneGetNoteAllocator");
    }

    /** used by TunePlayer */
    public static int TuneGetPartMix(int tp, int partNumber, int[] volumeOut, int[] balanceOut, int[] mixFlagsOut) {
        throw new UnsupportedToolboxException("TuneGetPartMix");
    }

    /** used by TunePlayer */
    public static int TuneGetStatus(int tp, byte[] status) {
        throw new UnsupportedToolboxException("TuneGetStatus");
    }

    /** used by TimeBase */
    public static int TuneGetTimeBase(int tp, int[] tb) {
        throw new UnsupportedToolboxException("TuneGetTimeBase");
    }

    /** used by TunePlayer */
    public static int TuneGetTimeScale(int tp, int[] scale) {
        throw new UnsupportedToolboxException("TuneGetTimeScale");
    }

    /** used by TunePlayer */
    public static int TuneGetVolume(int tp) {
        throw new UnsupportedToolboxException("TuneGetVolume");
    }

    /** used by TunePlayer */
    public static int TuneInstant(int tp, int tune, int tunePosition) {
        throw new UnsupportedToolboxException("TuneInstant");
    }

    /** used by TunePlayer */
    public static int TunePreroll(int tp) {
        throw new UnsupportedToolboxException("TunePreroll");
    }

    /** used by TunePlayer */
    public static int TuneQueue(int tp, int tune, int tuneRate, int tuneStartPosition, int tuneStopPosition, int queueFlags, int callBackProc, int refCon) {
        throw new UnsupportedToolboxException("TuneQueue");
    }

    /** used by TunePlayer */
    public static int TuneSetBalance(int tp, int balance) {
        throw new UnsupportedToolboxException("TuneSetBalance");
    }

    /** used by TunePlayer */
    public static int TuneSetHeader(int tp, int header) {
        throw new UnsupportedToolboxException("TuneSetHeader");
    }

    /** used by TunePlayer */
    public static int TuneSetHeaderWithSize(int tp, int header, int size) {
        throw new UnsupportedToolboxException("TuneSetHeaderWithSize");
    }

    /** used by TunePlayer */
    public static int TuneSetNoteChannels(int tp, int count, int[] noteChannelList, int playCallBackProc, int refCon) {
        throw new UnsupportedToolboxException("TuneSetNoteChannels");
    }

    /** used by TunePlayer */
    public static int TuneSetPartMix(int tp, int partNumber, int volume, int balance, int mixFlags) {
        throw new UnsupportedToolboxException("TuneSetPartMix");
    }

    /** used by TunePlayer */
    public static int TuneSetPartTranspose(int tp, int part, int transpose, int velocityShift) {
        throw new UnsupportedToolboxException("TuneSetPartTranspose");
    }

    /** used by TunePlayer */
    public static int TuneSetSofter(int tp, int softer) {
        throw new UnsupportedToolboxException("TuneSetSofter");
    }

    /** used by TunePlayer */
    public static int TuneSetSoundLocalization(int tp, int data) {
        throw new UnsupportedToolboxException("TuneSetSoundLocalization");
    }

    /** used by TunePlayer */
    public static int TuneSetTimeScale(int tp, int scale) {
        throw new UnsupportedToolboxException("TuneSetTimeScale");
    }

    /** used by TunePlayer */
    public static int TuneSetVolume(int tp, int volume) {
        throw new UnsupportedToolboxException("TuneSetVolume");
    }

    /** used by TunePlayer */
    public static int TuneStop(int tp, int stopFlags) {
        throw new UnsupportedToolboxException("TuneStop");
    }

    /** used by TunePlayer */
    public static int TuneTask(int tp) {
        throw new UnsupportedToolboxException("TuneTask");
    }

    /** used by TunePlayer */
    public static int TuneUnroll(int tp) {
        throw new UnsupportedToolboxException("TuneUnroll");
    }

}
