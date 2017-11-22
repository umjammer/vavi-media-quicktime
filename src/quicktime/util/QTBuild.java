
package quicktime.util;

import java.util.Calendar;

import quicktime.QTSession;


public final class QTBuild {
    public static final int version = 6;

    public static final int subVersion = 1;

    public static final int qualifyingSubVersion = 3;

    public static final int build = 1;

    public static final String buildStage = "g";

    public static final int getVersion() {
        return 6;
    }

    public static final int getSubVersion() {
        return 1;
    }

    public static final int getQualifyingSubVersion() {
        return 3;
    }

    /**
     * @deprecated
     */
    public static final void expired() {
    }

    public static final void isValid() {
        if ("g".equals("g")) {
            return;
        }
        Calendar localCalendar = Calendar.getInstance();

        localCalendar.set(2005, 9, 15);
        if ((!QTSession.isCurrentOS(4)) && (Calendar.getInstance().after(localCalendar))) {
            System.err.println("This version of QTJava has expired");
            throw new SecurityException("QTJava has expired.");
        }
    }

    public static String info() {
        String str = Integer.toString(1);
        return "[QTJava:6.1.3g" + str + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * QTBuild.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
