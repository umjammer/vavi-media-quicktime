/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * MovieToolbox.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class MovieToolbox {

    private MovieToolbox() {
    }

    /** used by Movie */
    public static void AbortPrePrerollMovie(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("AbortPrePrerollMovie");
    }

    /** used by Movie */
    public static short AddEmptyTrackToMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("AddEmptyTrackToMovie");
    }

    /** used by Media */
    public static short AddMediaDataRef(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("AddMediaDataRef");
    }

    /** used by Media */
    public static short AddMediaSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short paramShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("AddMediaSample");
    }

    /** used by Media */
    public static short AddMediaSampleReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, short paramShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("AddMediaSampleReference");
    }

    /** used by Media */
    public static short AddMediaSampleReferences(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("AddMediaSampleReferences");
    }

    /** used by Movie */
    public static short AddMovieExecuteWiredActionsProc(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("AddMovieExecuteWiredActionsProc");
    }

    /** used by Movie */
    public static short AddMovieResource(int paramInt, short paramShort, short[] paramArrayOfShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("AddMovieResource");
    }

    /** used by Movie */
    public static void AddMovieSelection(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("AddMovieSelection");
    }

    /** used by Track */
    public static short AddTrackReference(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("AddTrackReference");
    }

    /** used by UserData */
    public static short AddUserData(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("AddUserData");
    }

    /** used by UserData */
    public static short AddUserDataText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort) {
        throw new UnsupportedToolboxException("AddUserDataText");
    }

    /** used by Media */
    public static short BeginMediaEdits(int paramInt) {
        throw new UnsupportedToolboxException("BeginMediaEdits");
    }

    /** used by Movie */
    public static void ChooseMovieClock(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("ChooseMovieClock");
    }

    /** used by Movie */
    public static void ClearMovieChanged(int paramInt) {
        throw new UnsupportedToolboxException("ClearMovieChanged");
    }

    /** used by Movie */
    public static void ClearMovieSelection(int paramInt) {
        throw new UnsupportedToolboxException("ClearMovieSelection");
    }

    /** used by QTFile */
    public static short ConvertFileToMovieFile(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, short paramShort, short[] paramArrayOfShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("ConvertFileToMovieFile");
    }

    /** used by Movie */
    public static short ConvertMovieToFile(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, short paramShort, short[] paramArrayOfShort, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("ConvertMovieToFile");
    }

    /** used by Media */
    public static short CopyMediaUserData(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("CopyMediaUserData");
    }

    /** used by Movie */
    public static int CopyMovieSelection(int paramInt) {
        throw new UnsupportedToolboxException("CopyMovieSelection");
    }

    /** used by Movie */
    public static short CopyMovieSettings(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("CopyMovieSettings");
    }

    /** used by Movie */
    public static short CopyMovieUserData(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("CopyMovieUserData");
    }

    /** used by Track */
    public static short CopyTrackSettings(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("CopyTrackSettings");
    }

    /** used by Track */
    public static short CopyTrackUserData(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("CopyTrackUserData");
    }

    /** used by UserData */
    public static short CopyUserData(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("CopyUserData");
    }

    /** used by UserData */
    public static short CountUserDataType(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("CountUserDataType");
    }

    /** used by Movie, QTFile */
    public static short CreateMovieFile(byte[] paramArrayOfByte, int paramInt1, short paramShort, int paramInt2, short[] paramArrayOfShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("CreateMovieFile");
    }

    /** used by QTFile */
    public static short CreateShortcutMovieFile(byte[] paramArrayOfByte, int paramInt1, short paramShort, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("CreateShortcutMovieFile");
    }

    /** used by Movie */
    public static int CutMovieSelection(int paramInt) {
        throw new UnsupportedToolboxException("CutMovieSelection");
    }

    /** used by QTFile */
    public static short DeleteMovieFile(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("DeleteMovieFile");
    }

    /** used by Movie */
    public static short DeleteMovieSegment(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("DeleteMovieSegment");
    }

    /** used by Track */
    public static short DeleteTrackReference(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("DeleteTrackReference");
    }

    /** used by Track */
    public static short DeleteTrackSegment(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("DeleteTrackSegment");
    }

    /** used by QTObjectManagement */
    public static void DisposeMovie(int paramInt) {
        throw new UnsupportedToolboxException("DisposeMovie");
    }

    /** used by QTObjectManagement */
    public static short DisposeMovieEditState(int paramInt) {
        throw new UnsupportedToolboxException("DisposeMovieEditState");
    }

    /** used by Movie */
    public static void DisposeMovieTrack(int paramInt) {
        throw new UnsupportedToolboxException("DisposeMovieTrack");
    }

    /** used by QTObjectManagement */
    public static short DisposeTrackEditState(int paramInt) {
        throw new UnsupportedToolboxException("DisposeTrackEditState");
    }

    /** used by Track */
    public static void DisposeTrackMedia(int paramInt) {
        throw new UnsupportedToolboxException("DisposeTrackMedia");
    }

    /** used by QTObjectManagement */
    public static short DisposeUserData(int paramInt) {
        throw new UnsupportedToolboxException("DisposeUserData");
    }

    /** used by Media */
    public static short EndMediaEdits(int paramInt) {
        throw new UnsupportedToolboxException("EndMediaEdits");
    }

    /** used by Movie */
    public static void FlattenMovie(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, short paramShort, int paramInt4, short[] paramArrayOfShort, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("FlattenMovie");
    }

    /** used by Movie */
    public static int FlattenMovieData(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, short paramShort, int paramInt4) {
        throw new UnsupportedToolboxException("FlattenMovieData");
    }

    /** used by Movie */
    public static int FlattenMovieDataToDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short paramShort, int paramInt6) {
        throw new UnsupportedToolboxException("FlattenMovieDataToDataRef");
    }

    /** used by Movie */
    public static short GetMaxLoadedTimeInMovie(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMaxLoadedTimeInMovie");
    }

    /** used by Media */
    public static int GetMediaCreationTime(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaCreationTime");
    }

    /** used by Media */
    public static int GetMediaDataHandler(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("GetMediaDataHandler");
    }

    /** used by Media */
    public static void GetMediaDataHandlerDescription(int paramInt, short paramShort, int[] paramArrayOfInt1, byte[] paramArrayOfByte, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMediaDataHandlerDescription");
    }

    /** used by DataRef */
    public static short GetMediaDataRef(int paramInt, short paramShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("GetMediaDataRef");
    }

    /** used by Media */
    public static short GetMediaDataRefCount(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GetMediaDataRefCount");
    }

    /** used by Media */
    public static int GetMediaDataSize(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetMediaDataSize");
    }

    /** used by Media */
    public static int GetMediaDuration(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaDuration");
    }

    /** used by Media, TimeCoder */
    public static int GetMediaHandler(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaHandler");
    }

    /** used by Media */
    public static void GetMediaHandlerDescription(int paramInt, int[] paramArrayOfInt1, byte[] paramArrayOfByte, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMediaHandlerDescription");
    }

    /** used by AtomContainer */
    public static short GetMediaInputMap(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMediaInputMap");
    }

    /** used by Media */
    public static short GetMediaLanguage(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaLanguage");
    }

    /** used by Media */
    public static int GetMediaModificationTime(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaModificationTime");
    }

    /** used by Media */
    public static void GetMediaNextInterestingTime(int paramInt1, short paramShort, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMediaNextInterestingTime");
    }

    /** used by Media */
    public static void GetMediaPlayHints(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMediaPlayHints");
    }

    /** used by Media */
    public static short GetMediaPreferredChunkSize(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMediaPreferredChunkSize");
    }

    /** used by AtomContainer */
    public static short GetMediaPropertyAtom(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMediaPropertyAtom");
    }

    /** used by Media */
    public static short GetMediaQuality(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaQuality");
    }

    /** used by Media */
    public static short GetMediaSample(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int paramInt4, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int paramInt5, int[] paramArrayOfInt4, int paramInt6, int[] paramArrayOfInt5, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GetMediaSample");
    }

    /** used by Media */
    public static int GetMediaSampleCount(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaSampleCount");
    }

    /** used by Media */
    public static void GetMediaSampleDescription(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetMediaSampleDescription");
    }

    /** used by Media */
    public static int GetMediaSampleDescriptionCount(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaSampleDescriptionCount");
    }

    /** used by Media */
    public static short GetMediaSampleReference(int paramInt1, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int paramInt3, int[] paramArrayOfInt5, int paramInt4, int[] paramArrayOfInt6, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GetMediaSampleReference");
    }

    /** used by Media */
    public static short GetMediaSampleReferences(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, int[] paramArrayOfInt3, int paramInt5) {
        throw new UnsupportedToolboxException("GetMediaSampleReferences");
    }

    /** used by Media */
    public static short GetMediaShadowSync(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMediaShadowSync");
    }

    /** used by Media */
    public static int GetMediaSyncSampleCount(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaSyncSampleCount");
    }

    /** used by Media */
    public static int GetMediaTimeScale(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaTimeScale");
    }

    /** used by Track */
    public static int GetMediaTrack(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaTrack");
    }

    /** used by UserData */
    public static int GetMediaUserData(int paramInt) {
        throw new UnsupportedToolboxException("GetMediaUserData");
    }

    /** used by Movie */
    public static byte GetMovieActive(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieActive");
    }

    /** used by Movie */
    public static void GetMovieActiveSegment(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMovieActiveSegment");
    }

    /** used by DataRef, Movie */
    public static short GetMovieAnchorDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("GetMovieAnchorDataRef");
    }

    /** used by Region */
    public static int GetMovieBoundsRgn(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieBoundsRgn");
    }

    /** used by Movie */
    public static void GetMovieBox(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetMovieBox");
    }

    /** used by Region */
    public static int GetMovieClipRgn(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieClipRgn");
    }

    /** used by ColorTable */
    public static short GetMovieColorTable(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMovieColorTable");
    }

    /** used by Movie */
    public static int GetMovieCreationTime(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieCreationTime");
    }

    /** used by Movie */
    public static int GetMovieDataSize(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetMovieDataSize");
    }

    /** used by DataRef */
    public static short GetMovieDefaultDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMovieDefaultDataRef");
    }

    /** used by Region */
    public static int GetMovieDisplayBoundsRgn(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieDisplayBoundsRgn");
    }

    /** used by Region */
    public static int GetMovieDisplayClipRgn(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieDisplayClipRgn");
    }

    /** used by Movie */
    public static int GetMovieDuration(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieDuration");
    }

    /** used by Movie, QDGraphics */
    public static void GetMovieGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMovieGWorld");
    }

    /** used by MovieImporter */
    public static short GetMovieImporterForDataRef(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMovieImporterForDataRef");
    }

    /** used by Movie */
    public static int GetMovieIndTrack(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetMovieIndTrack");
    }

    /** used by Movie */
    public static int GetMovieIndTrackType(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("GetMovieIndTrackType");
    }

    /** used by Movie */
    public static void GetMovieMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetMovieMatrix");
    }

    /** used by Movie */
    public static int GetMovieModificationTime(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieModificationTime");
    }

    /** used by Movie */
    public static void GetMovieNaturalBoundsRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetMovieNaturalBoundsRect");
    }

    /** used by Movie */
    public static void GetMovieNextInterestingTime(int paramInt1, short paramShort1, short paramShort2, int[] paramArrayOfInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("GetMovieNextInterestingTime");
    }

    /** used by Pict */
    public static int GetMoviePict(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetMoviePict");
    }

    /** used by Pict */
    public static int GetMoviePosterPict(int paramInt) {
        throw new UnsupportedToolboxException("GetMoviePosterPict");
    }

    /** used by Movie */
    public static int GetMoviePosterTime(int paramInt) {
        throw new UnsupportedToolboxException("GetMoviePosterTime");
    }

    /** used by Movie */
    public static int GetMoviePreferredRate(int paramInt) {
        throw new UnsupportedToolboxException("GetMoviePreferredRate");
    }

    /** used by Movie */
    public static short GetMoviePreferredVolume(int paramInt) {
        throw new UnsupportedToolboxException("GetMoviePreferredVolume");
    }

    /** used by Movie */
    public static byte GetMoviePreviewMode(int paramInt) {
        throw new UnsupportedToolboxException("GetMoviePreviewMode");
    }

    /** used by Movie */
    public static void GetMoviePreviewTime(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMoviePreviewTime");
    }

    /** used by Movie */
    public static void GetMovieProgressProc(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMovieProgressProc");
    }

    /** used by Movie */
    public static int GetMovieRate(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieRate");
    }

    /** used by Region */
    public static int GetMovieSegmentDisplayBoundsRgn(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetMovieSegmentDisplayBoundsRgn");
    }

    /** used by Movie */
    public static void GetMovieSelection(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetMovieSelection");
    }

    /** used by Movie */
    public static int GetMovieStatus(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetMovieStatus");
    }

    /** used by Movie */
    public static int GetMovieTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetMovieTime");
    }

    /** used by TimeBase */
    public static int GetMovieTimeBase(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieTimeBase");
    }

    /** used by Movie */
    public static int GetMovieTimeScale(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieTimeScale");
    }

    /** used by Movie */
    public static int GetMovieTrack(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetMovieTrack");
    }

    /** used by Movie */
    public static int GetMovieTrackCount(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieTrackCount");
    }

    /** used by UserData */
    public static int GetMovieUserData(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieUserData");
    }

    /** used by Movie */
    public static short GetMovieVolume(int paramInt) {
        throw new UnsupportedToolboxException("GetMovieVolume");
    }

    /** used by AtomContainer, Media, Movie, MovieController, MovieImporter, MultiMovieController, Pict, PixMap, QDGraphics, Region, SpriteWorld, TimeBase, TimeCoder, Track, UserData */
    public static short GetMoviesError() {
        return MoviesError.get();
    }

    /** used by Movie */
    public static int GetNextTrackForCompositing(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetNextTrackForCompositing");
    }

    /** used by Track */
    public static int GetNextTrackReferenceType(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetNextTrackReferenceType");
    }

    /** used by UserData */
    public static int GetNextUserDataType(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetNextUserDataType");
    }

    /** used by Movie */
    public static void GetPosterBox(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetPosterBox");
    }

    /** used by Movie */
    public static int GetPrevTrackForCompositing(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetPrevTrackForCompositing");
    }

    /** used by Track */
    public static int GetTrackAlternate(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackAlternate");
    }

    /** used by Region */
    public static int GetTrackBoundsRgn(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackBoundsRgn");
    }

    /** used by Region */
    public static int GetTrackClipRgn(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackClipRgn");
    }

    /** used by Track */
    public static int GetTrackCreationTime(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackCreationTime");
    }

    /** used by Track */
    public static int GetTrackDataSize(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetTrackDataSize");
    }

    /** used by Track */
    public static void GetTrackDimensions(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetTrackDimensions");
    }

    /** used by Region */
    public static int GetTrackDisplayBoundsRgn(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackDisplayBoundsRgn");
    }

    /** used by Track */
    public static short GetTrackDisplayMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetTrackDisplayMatrix");
    }

    /** used by Track */
    public static int GetTrackDuration(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackDuration");
    }

    /** used by Track */
    public static int GetTrackEditRate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetTrackEditRate");
    }

    /** used by Track */
    public static byte GetTrackEnabled(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackEnabled");
    }

    /** used by Track */
    public static int GetTrackID(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackID");
    }

    /** used by Track */
    public static short GetTrackLayer(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackLayer");
    }

    /** used by Track */
    public static void GetTrackLoadSettings(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4) {
        throw new UnsupportedToolboxException("GetTrackLoadSettings");
    }

    /** used by Track */
    public static void GetTrackMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetTrackMatrix");
    }

    /** used by PixMap */
    public static int GetTrackMatte(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackMatte");
    }

    /** used by Media, Track */
    public static int GetTrackMedia(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackMedia");
    }

    /** used by Track */
    public static int GetTrackModificationTime(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackModificationTime");
    }

    /** used by Movie, Track */
    public static int GetTrackMovie(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackMovie");
    }

    /** used by Region */
    public static int GetTrackMovieBoundsRgn(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackMovieBoundsRgn");
    }

    /** used by Track */
    public static void GetTrackNextInterestingTime(int paramInt1, short paramShort, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetTrackNextInterestingTime");
    }

    /** used by Track */
    public static int GetTrackOffset(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackOffset");
    }

    /** used by Pict */
    public static int GetTrackPict(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetTrackPict");
    }

    /** used by Track */
    public static int GetTrackReference(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetTrackReference");
    }

    /** used by Track */
    public static int GetTrackReferenceCount(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetTrackReferenceCount");
    }

    /** used by Region */
    public static int GetTrackSegmentDisplayBoundsRgn(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("GetTrackSegmentDisplayBoundsRgn");
    }

    /** used by QTHandle */
    public static short GetTrackSoundLocalizationSettings(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetTrackSoundLocalizationSettings");
    }

    /** used by Track */
    public static int GetTrackStatus(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackStatus");
    }

    /** used by Track */
    public static int GetTrackUsage(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackUsage");
    }

    /** used by UserData */
    public static int GetTrackUserData(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackUserData");
    }

    /** used by Track */
    public static short GetTrackVolume(int paramInt) {
        throw new UnsupportedToolboxException("GetTrackVolume");
    }

    /** used by UserData */
    public static short GetUserData(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("GetUserData");
    }

    /** used by UserData */
    public static short GetUserDataItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("GetUserDataItem");
    }

    /** used by UserData */
    public static short GetUserDataText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort) {
        throw new UnsupportedToolboxException("GetUserDataText");
    }

    /** used by Movie */
    public static void GoToBeginningOfMovie(int paramInt) {
        throw new UnsupportedToolboxException("GoToBeginningOfMovie");
    }

    /** used by Movie */
    public static void GoToEndOfMovie(int paramInt) {
        throw new UnsupportedToolboxException("GoToEndOfMovie");
    }

    /** used by Movie */
    public static byte HasMovieChanged(int paramInt) {
        throw new UnsupportedToolboxException("HasMovieChanged");
    }

    /** used by Movie */
    public static short InsertEmptyMovieSegment(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("InsertEmptyMovieSegment");
    }

    /** used by Track */
    public static short InsertEmptyTrackSegment(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("InsertEmptyTrackSegment");
    }

    /** used by Track */
    public static short InsertMediaIntoTrack(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("InsertMediaIntoTrack");
    }

    /** used by Movie */
    public static short InsertMovieSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("InsertMovieSegment");
    }

    /** used by Track */
    public static short InsertTrackSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("InsertTrackSegment");
    }

    /** used by Movie */
    public static short InvalidateMovieRegion(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("InvalidateMovieRegion");
    }

    /** used by Movie */
    public static byte IsMovieDone(int paramInt) {
        throw new UnsupportedToolboxException("IsMovieDone");
    }

    /** used by MovieImporter */
    public static int IsScrapMovie(int paramInt) {
        throw new UnsupportedToolboxException("IsScrapMovie");
    }

    /** used by Media */
    public static short LoadMediaIntoRam(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("LoadMediaIntoRam");
    }

    /** used by Movie */
    public static short LoadMovieIntoRam(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("LoadMovieIntoRam");
    }

    /** used by Track */
    public static short LoadTrackIntoRam(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("LoadTrackIntoRam");
    }

    /** used by Media */
    public static void MediaTimeToSampleNum(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("MediaTimeToSampleNum");
    }

    /** used by Movie */
    public static short MovieExecuteWiredActions(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieExecuteWiredActions");
    }

    /** used by Movie */
    public static short MovieSearchText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("MovieSearchText");
    }

    /** used by Movie */
    public static void MoviesTask(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MoviesTask");
    }

    /** used by Movie */
    public static int NewMovie(int paramInt) {
        throw new UnsupportedToolboxException("NewMovie");
    }

    /** used by Movie */
    public static int NewMovieEditState(int paramInt) {
        throw new UnsupportedToolboxException("NewMovieEditState");
    }

    /** used by Movie */
    public static short NewMovieForDataRefFromHandle(int[] paramArrayOfInt, int paramInt1, short paramShort, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("NewMovieForDataRefFromHandle");
    }

    /** used by Movie */
    public static short NewMovieFromDataFork(int[] paramArrayOfInt, short paramShort1, int paramInt, short paramShort2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("NewMovieFromDataFork");
    }

    /** used by Movie */
    public static short NewMovieFromDataRef(int[] paramArrayOfInt, short paramShort, short[] paramArrayOfShort, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("NewMovieFromDataRef");
    }

    /** used by Movie */
    public static short NewMovieFromFile(int[] paramArrayOfInt, short paramShort1, short[] paramArrayOfShort, byte[] paramArrayOfByte1, short paramShort2, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("NewMovieFromFile");
    }

    /** used by Movie */
    public static short NewMovieFromHandle(int[] paramArrayOfInt, int paramInt, short paramShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("NewMovieFromHandle");
    }

    /** used by Movie */
    public static int NewMovieFromScrap(int paramInt) {
        throw new UnsupportedToolboxException("NewMovieFromScrap");
    }

    /** used by Track */
    public static int NewMovieTrack(int paramInt1, int paramInt2, int paramInt3, short paramShort) {
        throw new UnsupportedToolboxException("NewMovieTrack");
    }

    /** used by Track */
    public static int NewTrackEditState(int paramInt) {
        throw new UnsupportedToolboxException("NewTrackEditState");
    }

    /** used by Media */
    public static int NewTrackMedia(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("NewTrackMedia");
    }

    /** used by UserData */
    public static short NewUserData(int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NewUserData");
    }

    /** used by UserData */
    public static short NewUserDataFromHandle(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("NewUserDataFromHandle");
    }

    /** used by Movie */
    public static short PasteHandleIntoMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("PasteHandleIntoMovie");
    }

    /** used by Movie */
    public static void PasteMovieSelection(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("PasteMovieSelection");
    }

    /** used by Movie */
    public static void PlayMoviePreview(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("PlayMoviePreview");
    }

    /** used by Movie */
    public static short PrePrerollMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("PrePrerollMovie");
    }

    /** used by Movie */
    public static short PrerollMovie(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("PrerollMovie");
    }

    /** used by Movie */
    public static byte PtInMovie(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("PtInMovie");
    }

    /** used by Track */
    public static byte PtInTrack(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("PtInTrack");
    }

    /** used by Movie */
    public static short PutMovieForDataRefIntoHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("PutMovieForDataRefIntoHandle");
    }

    /** used by Movie */
    public static short PutMovieIntoDataFork(int paramInt1, short paramShort, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("PutMovieIntoDataFork");
    }

    /** used by Movie */
    public static short PutMovieIntoHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("PutMovieIntoHandle");
    }

    /** used by Movie */
    public static short PutMovieIntoTypedHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        throw new UnsupportedToolboxException("PutMovieIntoTypedHandle");
    }

    /** used by Movie */
    public static short PutMovieOnScrap(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("PutMovieOnScrap");
    }

    /** used by UserData */
    public static short PutUserDataIntoHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("PutUserDataIntoHandle");
    }

    /** used by Movie */
    public static short QTGetTimeUntilNextTask(int[] paramArrayOfInt, int paramInt) {
        throw new UnsupportedToolboxException("QTGetTimeUntilNextTask");
    }

    /** used by Movie */
    public static short QTInstallNextTaskNeededSoonerCallback(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTInstallNextTaskNeededSoonerCallback");
    }

    /** used by Movie */
    public static short QTMovieNeedsTimeTable(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTMovieNeedsTimeTable");
    }

    /** used by Movie */
    public static short QTUninstallNextTaskNeededSoonerCallback(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTUninstallNextTaskNeededSoonerCallback");
    }

    /** used by Movie */
    public static short RemoveMovieExecuteWiredActionsProc(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("RemoveMovieExecuteWiredActionsProc");
    }

    /** used by Movie */
    public static short RemoveMovieResource(short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("RemoveMovieResource");
    }

    /** used by UserData */
    public static short RemoveUserData(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("RemoveUserData");
    }

    /** used by UserData */
    public static short RemoveUserDataText(int paramInt1, int paramInt2, int paramInt3, short paramShort) {
        throw new UnsupportedToolboxException("RemoveUserDataText");
    }

    /** used by Media */
    public static void SampleNumToMediaTime(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("SampleNumToMediaTime");
    }

    /** used by Movie */
    public static short ScaleMovieSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("ScaleMovieSegment");
    }

    /** used by Track */
    public static short ScaleTrackSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("ScaleTrackSegment");
    }

    /** used by Movie */
    public static void SelectMovieAlternates(int paramInt) {
        throw new UnsupportedToolboxException("SelectMovieAlternates");
    }

    /** used by Movie */
    public static void SetAutoTrackAlternatesEnabled(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("SetAutoTrackAlternatesEnabled");
    }

    /** used by Media */
    public static short SetMediaDataHandler(int paramInt1, short paramShort, int paramInt2) {
        throw new UnsupportedToolboxException("SetMediaDataHandler");
    }

    /** used by Media */
    public static short SetMediaDataRef(int paramInt1, short paramShort, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMediaDataRef");
    }

    /** used by Media */
    public static short SetMediaDataRefAttributes(int paramInt1, short paramShort, int paramInt2) {
        throw new UnsupportedToolboxException("SetMediaDataRefAttributes");
    }

    /** used by Media */
    public static short SetMediaDefaultDataRefIndex(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SetMediaDefaultDataRefIndex");
    }

    /** used by Media */
    public static short SetMediaHandler(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMediaHandler");
    }

    /** used by Media */
    public static short SetMediaInputMap(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMediaInputMap");
    }

    /** used by Media */
    public static void SetMediaLanguage(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SetMediaLanguage");
    }

    /** used by Media */
    public static void SetMediaPlayHints(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMediaPlayHints");
    }

    /** used by Media */
    public static short SetMediaPreferredChunkSize(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMediaPreferredChunkSize");
    }

    /** used by Media */
    public static short SetMediaPropertyAtom(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMediaPropertyAtom");
    }

    /** used by Media */
    public static void SetMediaQuality(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SetMediaQuality");
    }

    /** used by Media */
    public static short SetMediaSampleDescription(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMediaSampleDescription");
    }

    /** used by Media */
    public static short SetMediaShadowSync(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMediaShadowSync");
    }

    /** used by Media */
    public static void SetMediaTimeScale(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMediaTimeScale");
    }

    /** used by Movie */
    public static void SetMovieActive(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("SetMovieActive");
    }

    /** used by Movie */
    public static void SetMovieActiveSegment(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMovieActiveSegment");
    }

    /** used by Movie */
    public static short SetMovieAnchorDataRef(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMovieAnchorDataRef");
    }

    /** used by Movie */
    public static void SetMovieBox(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetMovieBox");
    }

    /** used by Movie */
    public static void SetMovieClipRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMovieClipRgn");
    }

    /** used by Movie */
    public static short SetMovieColorTable(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMovieColorTable");
    }

    /** used by Movie */
    public static short SetMovieDefaultDataRef(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMovieDefaultDataRef");
    }

    /** used by Movie */
    public static void SetMovieDisplayClipRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMovieDisplayClipRgn");
    }

    /** used by Movie */
    public static void SetMovieDrawingCompleteProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("SetMovieDrawingCompleteProc");
    }

    /** used by Movie */
    public static void SetMovieGWorld(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMovieGWorld");
    }

    /** used by Movie */
    public static void SetMovieLanguage(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMovieLanguage");
    }

    /** used by Movie */
    public static void SetMovieMasterClock(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMovieMasterClock");
    }

    /** used by Movie */
    public static void SetMovieMasterTimeBase(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMovieMasterTimeBase");
    }

    /** used by Movie */
    public static void SetMovieMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetMovieMatrix");
    }

    /** used by Movie */
    public static void SetMoviePlayHints(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMoviePlayHints");
    }

    /** used by Movie */
    public static void SetMoviePosterTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMoviePosterTime");
    }

    /** used by Movie */
    public static void SetMoviePreferredRate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMoviePreferredRate");
    }

    /** used by Movie */
    public static void SetMoviePreferredVolume(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SetMoviePreferredVolume");
    }

    /** used by Movie */
    public static void SetMoviePreviewMode(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("SetMoviePreviewMode");
    }

    /** used by Movie */
    public static void SetMoviePreviewTime(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMoviePreviewTime");
    }

    /** used by Movie */
    public static void SetMovieProgressProc(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMovieProgressProc");
    }

    /** used by Movie */
    public static void SetMovieRate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMovieRate");
    }

    /** used by Movie */
    public static void SetMovieSelection(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetMovieSelection");
    }

    /** used by Movie */
    public static void SetMovieTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMovieTime");
    }

    /** used by Movie */
    public static void SetMovieTimeScale(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMovieTimeScale");
    }

    /** used by Movie */
    public static void SetMovieTimeValue(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetMovieTimeValue");
    }

    /** used by Movie */
    public static void SetMovieVolume(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SetMovieVolume");
    }

    /** used by Movie */
    public static void SetPosterBox(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetPosterBox");
    }

    /** used by Track */
    public static void SetTrackAlternate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTrackAlternate");
    }

    /** used by Track */
    public static void SetTrackClipRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTrackClipRgn");
    }

    /** used by Track */
    public static void SetTrackDimensions(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SetTrackDimensions");
    }

    /** used by Track */
    public static void SetTrackEnabled(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("SetTrackEnabled");
    }

    /** used by Track */
    public static void SetTrackGWorld(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("SetTrackGWorld");
    }

    /** used by Track */
    public static void SetTrackLayer(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SetTrackLayer");
    }

    /** used by Track */
    public static void SetTrackLoadSettings(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("SetTrackLoadSettings");
    }

    /** used by Track */
    public static void SetTrackMatrix(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetTrackMatrix");
    }

    /** used by Track */
    public static void SetTrackMatte(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTrackMatte");
    }

    /** used by Track */
    public static void SetTrackOffset(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTrackOffset");
    }

    /** used by Track */
    public static short SetTrackReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("SetTrackReference");
    }

    /** used by Track */
    public static short SetTrackSoundLocalizationSettings(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTrackSoundLocalizationSettings");
    }

    /** used by Track */
    public static void SetTrackUsage(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetTrackUsage");
    }

    /** used by Track */
    public static void SetTrackVolume(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SetTrackVolume");
    }

    /** used by UserData */
    public static short SetUserDataItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("SetUserDataItem");
    }

    /** used by Movie */
    public static void ShowMovieInformation(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("ShowMovieInformation");
    }

    /** used by Movie */
    public static void ShowMoviePoster(int paramInt) {
        throw new UnsupportedToolboxException("ShowMoviePoster");
    }

    /** used by Movie */
    public static void StartMovie(int paramInt) {
        throw new UnsupportedToolboxException("StartMovie");
    }

    /** used by Movie */
    public static void StopMovie(int paramInt) {
        throw new UnsupportedToolboxException("StopMovie");
    }

    /** used by Track */
    public static int TrackTimeToMediaTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("TrackTimeToMediaTime");
    }

    /** used by Movie */
    public static short UpdateMovie(int paramInt) {
        throw new UnsupportedToolboxException("UpdateMovie");
    }

    /** used by Movie */
    public static short UpdateMovieResource(int paramInt, short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("UpdateMovieResource");
    }

    /** used by Movie */
    public static short UseMovieEditState(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("UseMovieEditState");
    }

    /** used by Track */
    public static short UseTrackEditState(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("UseTrackEditState");
    }

    public static short OpenMovieFile(byte[] fsspec, short[] resRefNum, byte permission) {
        throw new UnsupportedToolboxException("OpenMovieFile");
    }

    public static short CloseMovieFile(short resRefNum) {
        throw new UnsupportedToolboxException("CloseMovieFile");
    }
}
