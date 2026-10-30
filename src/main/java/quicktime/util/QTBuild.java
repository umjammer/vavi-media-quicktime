package quicktime.util;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Calendar;
import quicktime.QTSession;


/**
 * Contains build information for the QTJava library.
 */
public final class QTBuild {

    private static final Logger logger = System.getLogger(QTBuild.class.getName());

    /**
     * The version of QTJava
     */
    public static final int version = 6;

    /**
     * The sub-version of QTJava
     */
    public static final int subVersion = 1;

    /**
     * The qualifier for the sub-version of QTJava
     */
    public static final int qualifyingSubVersion = 3;

    /**
     * The build of QTJava given same version/subversion. A value of 0 means final for that buildStage.
     */
    public static final int build = 1;

    /**
     * The buildType of QTJava given same version/subversion.
     * Possible values are 'g' for GoldenMaster, 'f' for final candidate, 'b' for beta, 'a' for alpha
     */
    public static final String buildStage = "g";

    /**
     * @return The version of QTJava
     */
    public static int getVersion() {
        return 6;
    }

    /**
     * @return The sub-version of QTJava
     */
    public static int getSubVersion() {
        return 1;
    }

    /**
     * @return The qualifier for the sub-version of QTJava
     */
    public static int getQualifyingSubVersion() {
        return 3;
    }

    /**
     * @deprecated
     *
     * @see #isValid
     */
    public static void expired() {
    }

    /**
     * This checks to see if the QTJava developer release versions (which time out) are still valid.
     * It this version has timed out then a security exception will be thrown,
     * otherwise this returns.
     *
     * The call can also be used as a means of establishing whether the QTJava classes can be found
     * by the system - the call can be made without the application having called QTSession.open.
     */
    public static void isValid() {
        if ("g".equals("g")) {
            return;
        }
        Calendar instance = Calendar.getInstance();
        instance.set(2005, Calendar.OCTOBER, 15);
        if ((!QTSession.isCurrentOS(4)) && (Calendar.getInstance().after(instance))) {
            logger.log(Level.DEBUG, "This version of QTJava has expired");
            throw new SecurityException("QTJava has expired.");
        }
    }

    /**
     * This returns an info string about the current version/build of QTJava
     */
    public static String info() {
        String str = Integer.toString(1);
        return "[QTJava:6.1.3g" + str + "]";
    }
}
