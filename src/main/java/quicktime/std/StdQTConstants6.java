package quicktime.std;

/**
 * This class represents all of the constants in the QuickTime interface version 6.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public interface StdQTConstants6 {

    /**
     * QT6 supported codec types
     */
    int kSorenson3CodecType = 1398165811;

    /**
     * QT6 supported codec types
     */
    int kMPEG4VisualCodecType = 1836070006;

    /**
     * QuickTime 5.1 supported media type
     */
    int skinMediaType = 1936419182;

    int kCharacteristicHasNoDuration = 1852798057;

    int kCharacteristicHasSkinData = 1936419182;

    /**
     * New action constants
     */
    int kActionMovieGotoNextChapter = 1039;

    /**
     * New action constants
     */
    int kActionMovieGotoPreviousChapter = 1040;

    /**
     * New action constants
     */
    int kActionMovieGotoFirstChapter = 1041;

    /**
     * New action constants
     */
    int kActionMovieGotoLastChapter = 1042;

    /**
     * New action constants
     */
    int kActionMovieGotoChapterByIndex = 1043;

    /**
     * New action constants
     */
    int kActionSpriteSetCanBeHitTested = 3094;

    /**
     * New action constants
     */
    int kActionQTVRSetViewState = 4109;

    /**
     * New action constants
     */
    int kActionSetRandomSeed = 6164;

    /**
     * New action constants
     */
    int kActionSpriteTrackSetAllSpritesHitTestingMode = 7181;

    /**
     * New action constants
     */
    int kActionSpriteTrackNewImage = 7182;

    /**
     * New action constants
     */
    int kActionSpriteTrackDisposeImage = 7183;

    int kOperandMovieChapterCount = 1038;

    int kOperandMovieChapterIndex = 1039;

    int kOperandMovieChapterName = 1040;

    int kOperandMovieChapterNameByIndex = 1041;

    int kOperandMovieChapterIndexByName = 1042;

    int kOperandSpriteCanBeHitTested = 3105;

    int kOperandSpriteTrackAllSpritesHitTestingMode = 3106;

    int kOperandQTVRViewStateCount = 4103;

    int kOperandQTVRViewState = 4104;

    /**
     * QuickTime 5 action range
     */
    int kLastMovieAction5_1 = 1043;

    /**
     * QuickTime 5 action range
     */
    int kLastSpriteAction5_1 = 3094;

    /**
     * QuickTime 5 action range
     */
    int kLastQTVRAction5_1 = 4109;

    /**
     * QuickTime 5 action range
     */
    int kLastSystemAction5_1 = 6164;

    /**
     * QuickTime 5 action range
     */
    int kLastSpriteTrackAction5_1 = 7183;

    /**
     * User Data Copying Constants
     */
    int kQTCopyUserDataReplace = 1919970403;

    /**
     * User Data Copying Constants
     */
    int kQTCopyUserDataMerge = 1835361639;

    /**
     * Flash track properties
     */
    int kFlashTrackPropertyAcceptAllClicks = 1668049259;

    /**
     * Content Restrictions
     */
    int kQTRestrictionClassSave = 1935767141;

    /**
     * Content Restrictions
     */
    int kQTRestrictionSaveDontAddMovieResource = 1;

    /**
     * Content Restrictions
     */
    int kQTRestrictionSaveDontFlatten = 2;

    /**
     * Content Restrictions
     */
    int kQTRestrictionSaveDontExport = 4;

    /**
     * Content Restrictions
     */
    int kQTRestrictionSaveDontExtract = 8;

    int kQTRestrictionClassEdit = 1701079412;

    int kQTRestrictionEditDontCopy = 1;

    int kQTRestrictionEditDontCut = 2;

    int kQTRestrictionEditDontPaste = 4;

    int kQTRestrictionEditDontClear = 8;

    int kQTRestrictionEditDontModify = 16;

    int kQTRestrictionEditDontExtract = 32;
}
