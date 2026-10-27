package quicktime.app.image;

import java.io.File;
import java.io.FilenameFilter;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
 * This class is used by the ImageUtil.createSequence call. It is used to
 * filter the contents of a directory and return true to those files that
 * match the pattern that this expects. After the files are selected the
 * array of fileNames accepted are submitted to the postProcess method which
 * returns an array of fileNames which will then be used to create a sequence
 * of images in the order of the files in this array.
 *
 * The filename format that this filter expects is:
 *
 * Both prefix and suffix can be empty, but  requires that
 * there be at least one numeric character in the name of the file.
 *
 * No sorting is done on the resulting file list, so generally the files will be
 * opened and added to the sequence in alpha/numeric order. So for files to appear
 * in proper numeric order you will need to specify leading zeroes in the frame numbers.
 *
 * You can also pass in a boolean flag to indicate that the filter should ignore the
 * suffix of the filename when matching.
 *
 * Subclasses can overide or extend the methods to provide custom filtering
 * operations.
 */
public class FileFilter implements FilenameFilter {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create the filter with the flag set (if true - the default) which will test
     * the suffix and prefix or if false it will test the prefix only
     */
    public FileFilter(boolean paramBoolean) {
        this.testSuffix = paramBoolean;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create the filter with the default setting of testing both prefix and suffix
     */
    public FileFilter() {
        this(true);
    }

    /**
     * @deprecated
     * The prefix of the file to be matched. The value is set by the extractPrefixAndSuffix
     * method. It should always have a value - even if it is just an empty string.* @deprecated since QTJava 6.1
     */
    protected String prefix = "";

    /**
     * @deprecated
     * The suffix of the file to be matched. The value is set by the extractPrefixAndSuffix
     * method.* @deprecated since QTJava 6.1
     */
    protected String suffix = "";

    /**
     * @deprecated
     * @since QTJava 6.1
     * If true (the default) then both the suffix and prefix are used to match the filename.
     * If false then only the prefix is matched.
     */
    protected boolean testSuffix;

    /**
     * @deprecated
     * @since QTJava 6.1
     * It dissects the incoming fName (the name of a file) into a
     * prefix which are all the characters of the fileName that precede
     * the last occurance of a number in the filename and a suffix
     * the extracts the characters that immediately follow the last occurance
     * of a number in the fName. It stores the result of these into the instance
     * variables prefix and suffix respectively.
     *
     * Subclasses can overwrite this to produce their own semantic for extracting
     * a prefix and suffix - they need to ensure that the instance variables are
     * set with the appropriate values.
     * @param fName the name of the file that is to be disected.
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
        this.suffix = paramString.substring(i);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method tests the frameNumber portion of the filename. If this
     * string does NOT have a number it will throw a NumberFormatException.
     * It returns the resulting number.
     * java.lang.NumberFormatException
     */
    protected int testFrameNumber(String paramString) throws NumberFormatException {
        int i = this.prefix.length();
        int j = paramString.length() - this.suffix.length();
        String str = paramString.substring(i, j);
        return Integer.parseInt(str);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is used by the createSequence call to filter the files found
     * in the supplied directory. It returns true if the filename matches the
     * conditions (prefix and suffix are found in the file and between these there
     * is a number). Both prefix and suffix can be empty but the frame number is required.
     * @param dir the directory being filtered
     * @param fName the name of a file found within this directory
     * @return true if the file matches the conditions otherwise false
     */
    @Override
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
     * @since QTJava 6.1
     * This method is called after the directory has been filtered. The incoming array
     * contains both the file list (files found within the directory that have been accepted
     * as a match) and the order of the files in this list is the order that the files will
     * be added to the sequence. The method can alter this array (or return a new one). The
     * returned array should contain no null values, and the order of the filenames in the
     * returned array is the order that the files will be added to the sequence.
     *
     * The default implementation returns the list as is.
     * @return the ordered list of files that the sequence will be created from.
     */
    protected String[] postProcess(String[] paramArrayOfString) {
        return paramArrayOfString;
    }
}
