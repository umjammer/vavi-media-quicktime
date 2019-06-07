package quicktime.std;

/**
 * This class represents all of the constants in the QuickTime interface version 6.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public abstract interface StdQTConstants6 {

    /**
     * QT6 supported codec types
     */
    public static final int kSorenson3CodecType = 1398165811;

    /**
     * QT6 supported codec types
     */
    public static final int kMPEG4VisualCodecType = 1836085622;

    /**
     * QuickTime 5.1 supported media type
     */
    public static final int skinMediaType = 1936419182;

    public static final int kCharacteristicHasNoDuration = 1852798057;

    public static final int kCharacteristicHasSkinData = 1936419182;

    /**
     * New action constants
     */
    public static final int kActionMovieGotoNextChapter = 1039;

    /**
     * New action constants
     */
    public static final int kActionMovieGotoPreviousChapter = 1040;

    /**
     * New action constants
     */
    public static final int kActionMovieGotoFirstChapter = 1041;

    /**
     * New action constants
     */
    public static final int kActionMovieGotoLastChapter = 1042;

    /**
     * New action constants
     */
    public static final int kActionMovieGotoChapterByIndex = 1043;

    /**
     * New action constants
     */
    public static final int kActionSpriteSetCanBeHitTested = 3094;

    /**
     * New action constants
     */
    public static final int kActionQTVRSetViewState = 4109;

    /**
     * New action constants
     */
    public static final int kActionSetRandomSeed = 6164;

    /**
     * New action constants
     */
    public static final int kActionSpriteTrackSetAllSpritesHitTestingMode = 7181;

    /**
     * New action constants
     */
    public static final int kActionSpriteTrackNewImage = 7182;

    /**
     * New action constants
     */
    public static final int kActionSpriteTrackDisposeImage = 7183;

    public static final int kOperandMovieChapterCount = 1038;

    public static final int kOperandMovieChapterIndex = 1039;

    public static final int kOperandMovieChapterName = 1040;

    public static final int kOperandMovieChapterNameByIndex = 1041;

    public static final int kOperandMovieChapterIndexByName = 1042;

    public static final int kOperandSpriteCanBeHitTested = 3105;

    public static final int kOperandSpriteTrackAllSpritesHitTestingMode = 3106;

    public static final int kOperandQTVRViewStateCount = 4103;

    public static final int kOperandQTVRViewState = 4104;

    /**
     * QuickTime 5 action range
     */
    public static final int kLastMovieAction5_1 = 1043;

    /**
     * QuickTime 5 action range
     */
    public static final int kLastSpriteAction5_1 = 3094;

    /**
     * QuickTime 5 action range
     */
    public static final int kLastQTVRAction5_1 = 4109;

    /**
     * QuickTime 5 action range
     */
    public static final int kLastSystemAction5_1 = 6164;

    /**
     * QuickTime 5 action range
     */
    public static final int kLastSpriteTrackAction5_1 = 7183;

    /**
     * User Data Copying Constants
     */
    public static final int kQTCopyUserDataReplace = 1919970403;

    /**
     * User Data Copying Constants
     */
    public static final int kQTCopyUserDataMerge = 1835361639;

    /**
     * Flash track properties
     */
    public static final int kFlashTrackPropertyAcceptAllClicks = 1668049259;

    /**
     * Content Restrictions
     */
    public static final int kQTRestrictionClassSave = 1935767141;

    /**
     * Content Restrictions
     */
    public static final int kQTRestrictionSaveDontAddMovieResource = 1;

    /**
     * Content Restrictions
     */
    public static final int kQTRestrictionSaveDontFlatten = 2;

    /**
     * Content Restrictions
     */
    public static final int kQTRestrictionSaveDontExport = 4;

    /**
     * Content Restrictions
     */
    public static final int kQTRestrictionSaveDontExtract = 8;

    public static final int kQTRestrictionClassEdit = 1701079412;

    public static final int kQTRestrictionEditDontCopy = 1;

    public static final int kQTRestrictionEditDontCut = 2;

    public static final int kQTRestrictionEditDontPaste = 4;

    public static final int kQTRestrictionEditDontClear = 8;

    public static final int kQTRestrictionEditDontModify = 16;

    public static final int kQTRestrictionEditDontExtract = 32;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * StdQTConstants6.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
