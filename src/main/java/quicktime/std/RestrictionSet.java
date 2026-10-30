package quicktime.std;

import quicktime.QTObject;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.movies.Movie;


/**
 * The RestrictionSet class implements the corresponding data structure of the Movie Toolbox.
 * QuickTime 6 introduces a group of new APIs that provide a mechanism to
 * preflight operations on content that may be restricted. What this means is that,
 * by utilizing these calls, you can determine which QuickTime functions are
 * restricted for the current movie, that is, which operations will fail.
 * These calls also allow developers to see what restrictions are enforced for a
 * particular version of QuickTime. Previously, in earlier versions of QuickTime,
 * there was no mechanism in place to report restrictions on no save or kiosk
 * movies.
 */
public final class RestrictionSet extends QTObject implements QuickTimeLib {

    private RestrictionSet(int nativeRep) {
        super(nativeRep, null);
    }

    /**
     * Returns the restrictions, if any, specified on a given movie.
     *
     * @param theMovie the movie whose restrictions are returned
     * @return the RestrictionSet object for given movie
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::QTGetMovieRestrictions"
     */
    public static RestrictionSet fromMovie(Movie theMovie) throws StdQTException {
        int[] outRestrictionSet = {0};
        int[] outSeed = {0};
        int i = QTGetMovieRestrictions(QTObject.ID(theMovie), outRestrictionSet, outSeed);
        return new RestrictionSet(outRestrictionSet[0]);
    }

    /**
     * Use this routine to determine what restrictions are enforced by the currently
     * running version of QuickTime.
     *
     * @return restrictions in force for the class passed.
     * @see "QuickTime::QTGetSupportedRestrictions"
     */
    public static int getSupportedRestrictions(int inRestrictionClass) {
        int[] outRestrictionIDs = {0};
        QTGetSupportedRestrictions(inRestrictionClass, outRestrictionIDs);
        return outRestrictionIDs[0];
    }

    /**
     * Returns the information about the restrictions designated by the specified
     * restriction set. If you want to determine all of the restrictions, use this routine to determine the count.
     *
     * @see "QuickTime::QTRestrictionsGetInfo"
     */
    public int getClassCount() {
        int[] outRestrictionClassCount = {0};
        int[] outSeed = {0};
        QTRestrictionsGetInfo(_ID(), outRestrictionClassCount, outSeed);
        return outRestrictionClassCount[0];
    }

    /**
     * Returns the information about the restrictions designated by the specified
     * restriction set.
     *
     * @return an updated integer value that changes with change to the restrition set
     * @see "QuickTime::QTRestrictionsGetInfo"
     */
    public int getSeed() {
        int[] outRestrictionClassCount = {0};
        int[] outSeed = {0};
        QTRestrictionsGetInfo(_ID(), outRestrictionClassCount, outSeed);
        return outSeed[0];
    }

    /**
     * Returns the restricted items.
     *
     * @param restrictionClass class of restrictions to be reported: kQTRestrictionClassSave or kQTRestrictionClassEdit
     * @return integer holding constants that indicate individual restrictions
     * @see "QuickTime::QTRestrictionsGetItem"
     */
    public int getItem(int restrictionClass) {
        int[] outRestrictions = {0};
        QTRestrictionsGetItem(_ID(), restrictionClass, outRestrictions);
        return outRestrictions[0];
    }

    private static short QTGetMovieRestrictions(int movie, int[] outRestrictionSet, int[] outSeed) {
        return PlatformManager.QTGetMovieRestrictions(movie, outRestrictionSet, outSeed);
    }

    private static short QTGetSupportedRestrictions(int inRestrictionClass, int[] outRestrictionIDs) {
        return PlatformManager.QTGetSupportedRestrictions(inRestrictionClass, outRestrictionIDs);
    }

    private static short QTRestrictionsGetInfo(int inRestrictionSet, int[] outRestrictionClassCount, int[] outSeed) {
        return PlatformManager.QTRestrictionsGetInfo(inRestrictionSet, outRestrictionClassCount, outSeed);
    }

    private static short QTRestrictionsGetItem(int inRestrictionSet, int inRestrictionClass, int[] outRestrictions) {
        return PlatformManager.QTRestrictionsGetItem(inRestrictionSet, inRestrictionClass, outRestrictions);
    }
}
