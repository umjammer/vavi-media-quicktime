
package quicktime.app.image;

import java.io.File;
import java.io.FilenameFilter;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public class FileFilter implements FilenameFilter {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public FileFilter(boolean paramBoolean) {
        this.testSuffix = paramBoolean;
    }

    /**
     * @deprecated
     */
    public FileFilter() {
        this(true);
    }

    protected String prefix = "";

    protected String suffix = "";

    /**
     * @deprecated
     */
    protected boolean testSuffix;

    /**
     * @deprecated
     */
    protected void extractPrefixAndSuffix(String paramString) {
        int i = paramString.length() - 1;
        int j = 0;
        for (int k = i; k >= 0; k--) {
            if (Character.isDigit(paramString.charAt(k))) {
                i = k + 1;
                for (int m = k - 1; m >= 0; m--) {
                    if (!Character.isDigit(paramString.charAt(m))) {
                        j = m + 1;
                        break;
                    }
                }
            }
        }

        this.prefix = paramString.substring(0, j);
        this.suffix = paramString.substring(i, paramString.length());
    }

    /**
     * @deprecated
     */
    protected int testFrameNumber(String paramString) throws NumberFormatException {
        int i = this.prefix.length();
        int j = paramString.length() - this.suffix.length();
        String str = paramString.substring(i, j);
        return Integer.parseInt(str);
    }

    /**
     * @deprecated
     */
    public boolean accept(File paramFile, String paramString) {
        boolean bool = false;

        if ((paramString.startsWith(this.prefix)) && ((!this.testSuffix) || (paramString.endsWith(this.suffix)))) {
            try {
                testFrameNumber(paramString);
                bool = true;
            } catch (NumberFormatException localNumberFormatException) {
                bool = false;
            }
        }
        return bool;
    }

    /**
     * @deprecated
     */
    protected String[] postProcess(String[] paramArrayOfString) {
        return paramArrayOfString;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /FileFilter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
