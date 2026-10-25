package quicktime.std;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(RestrictionSet.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    private RestrictionSet(int paramInt) {
        super(paramInt, null);
    }

    /**
     * Returns the restrictions, if any, specified on a given movie.
     * QuickTime::QTGetMovieRestrictions
     * @param theMovie the movie whose restrictions are returned
     * @return the RestrictionSet object for given movie
     * @return {@link quicktime.std.StdQTException}
     */
    public static RestrictionSet fromMovie(Movie paramMovie) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = QTGetMovieRestrictions(QTObject.ID(paramMovie), arrayOfInt1, arrayOfInt2);
        return new RestrictionSet(arrayOfInt1[0]);
    }

    /**
     * Use this routine to determine what restrictions are enforced by the currently
     * running version of QuickTime.
     * QuickTime::QTGetSupportedRestrictions
     * @return restrictions in force for the class passed.
     */
    public static int getSupportedRestrictions(int paramInt) {
        int[] arrayOfInt = { 0 };
        QTGetSupportedRestrictions(paramInt, arrayOfInt);
        return arrayOfInt[0];
    }

    /**
     * Returns the information about the restrictions designated by the specified
     * restriction set. If you want to determine all of the restrictions, use this routine to determine the count.
     * QuickTime::QTRestrictionsGetInfo
     */
    public int getClassCount() {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        QTRestrictionsGetInfo(_ID(), arrayOfInt1, arrayOfInt2);
        return arrayOfInt1[0];
    }

    /**
     * Returns the information about the restrictions designated by the specified
     * restriction set.
     * QuickTime::QTRestrictionsGetInfo
     * @return an updated integer value that changes with change to the restrition set
     */
    public int getSeed() {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        QTRestrictionsGetInfo(_ID(), arrayOfInt1, arrayOfInt2);
        return arrayOfInt2[0];
    }

    /**
     * Returns the restricted items.
     * QuickTime::QTRestrictionsGetItem
     * @param restrictionClass class of restrictions to be reported: kQTRestrictionClassSave or kQTRestrictionClassEdit
     * @return integer holding constants that indicate individual restrictions
     */
    public int getItem(int paramInt) {
        int[] arrayOfInt = { 0 };
        QTRestrictionsGetItem(_ID(), paramInt, arrayOfInt);
        return arrayOfInt[0];
    }

    private static short QTGetMovieRestrictions(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return PlatformManager.QTGetMovieRestrictions(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short QTGetSupportedRestrictions(int paramInt, int[] paramArrayOfInt) {
        return PlatformManager.QTGetSupportedRestrictions(paramInt, paramArrayOfInt);
    }

    private static short QTRestrictionsGetInfo(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return PlatformManager.QTRestrictionsGetInfo(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short QTRestrictionsGetItem(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return PlatformManager.QTRestrictionsGetItem(paramInt1, paramInt2, paramArrayOfInt);
    }
}
