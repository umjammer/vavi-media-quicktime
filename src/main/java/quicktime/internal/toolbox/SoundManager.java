/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * SoundManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class SoundManager {

    private SoundManager() {
    }

    /** used by SoundDescription */
    public static short AddSoundDescriptionExtension(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("AddSoundDescriptionExtension");
    }

    /** used by QTHandle */
    public static short GetSoundDescriptionExtension(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        throw new UnsupportedToolboxException("GetSoundDescriptionExtension");
    }

    /** used by SndHandle */
    public static short GetSoundHeaderOffset(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetSoundHeaderOffset");
    }

    /** used by SndInfo */
    public static short ParseAIFFHeader(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("ParseAIFFHeader");
    }

    /** used by SndHandle */
    public static short ParseSndHeader(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("ParseSndHeader");
    }

    /** used by SoundDescription */
    public static short RemoveSoundDescriptionExtension(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("RemoveSoundDescriptionExtension");
    }

    /** used by SPBDevice */
    public static short SPBBytesToMilliseconds(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SPBBytesToMilliseconds");
    }

    /** used by QTObjectManagement */
    public static short SPBCloseDevice(int paramInt) {
        throw new UnsupportedToolboxException("SPBCloseDevice");
    }

    /** used by SPBDevice */
    public static short SPBGetDeviceInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SPBGetDeviceInfo");
    }

    /** used by SPBDevice */
    public static short SPBGetDeviceInfo(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SPBGetDeviceInfo");
    }

    /** used by SPBDevice */
    public static short SPBGetDeviceInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SPBGetDeviceInfo");
    }

    /** used by SPBDevice */
    public static short SPBGetDeviceInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SPBGetDeviceInfo");
    }

    /** used by SPBDevice */
    public static short SPBGetIndexedDevice(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SPBGetIndexedDevice");
    }

    /** used by SPB */
    public static short SPBGetRecordingStatus(int paramInt, short[] paramArrayOfShort1, short[] paramArrayOfShort2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4) {
        throw new UnsupportedToolboxException("SPBGetRecordingStatus");
    }

    /** used by SPBDevice */
    public static short SPBMillisecondsToBytes(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SPBMillisecondsToBytes");
    }

    /** used by SPBDevice */
    public static short SPBOpenDevice(byte[] paramArrayOfByte, short paramShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SPBOpenDevice");
    }

    /** used by SPB */
    public static short SPBPauseRecording(int paramInt) {
        throw new UnsupportedToolboxException("SPBPauseRecording");
    }

    /** used by SPB */
    public static short SPBRecord(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("SPBRecord");
    }

    /** used by SPB */
    public static short SPBResumeRecording(int paramInt) {
        throw new UnsupportedToolboxException("SPBResumeRecording");
    }

    /** used by SPBDevice */
    public static short SPBSetDeviceInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SPBSetDeviceInfo");
    }

    /** used by SPBDevice */
    public static short SPBSetDeviceInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SPBSetDeviceInfo");
    }

    /** used by SPB */
    public static short SPBStopRecording(int paramInt) {
        throw new UnsupportedToolboxException("SPBStopRecording");
    }

    /** used by SndInfo */
    public static short SetupAIFFHeader(short paramShort1, short paramShort2, int paramInt1, short paramShort3, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("SetupAIFFHeader");
    }

    /** used by SndHandle */
    public static short SetupSndHeader(int paramInt1, short paramShort1, int paramInt2, short paramShort2, int paramInt3, short paramShort3, int paramInt4, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SetupSndHeader");
    }

    /** used by SndChannel */
    public static short SndChannelStatus(int paramInt, short paramShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SndChannelStatus");
    }

    /** used by QTObjectManagement */
    public static short SndDisposeChannel(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("SndDisposeChannel");
    }

    /** used by SndChannel */
    public static short SndDoCommand(int paramInt1, int paramInt2, byte paramByte) {
        throw new UnsupportedToolboxException("SndDoCommand");
    }

    /** used by SndChannel */
    public static short SndDoImmediate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SndDoImmediate");
    }

    /** used by SndChannel */
    public static short SndGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SndGetInfo");
    }

    /** used by Clock, SndChannel */
    public static short SndGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SndGetInfo");
    }

    /** used by SndChannel */
    public static short SndNewChannel(int[] paramArrayOfInt, short paramShort, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SndNewChannel");
    }

    /** used by SndChannel, Sound */
    public static short SndPlay(int paramInt1, int paramInt2, byte paramByte) {
        throw new UnsupportedToolboxException("SndPlay");
    }

    /** used by SndChannel */
    public static short SndSetInfo(int paramInt1, int paramInt2, byte paramByte) {
        throw new UnsupportedToolboxException("SndSetInfo");
    }

    /** used by SndChannel */
    public static short SndSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SndSetInfo");
    }

    /** used by SndChannel */
    public static short SndSetInfo(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SndSetInfo");
    }

}
