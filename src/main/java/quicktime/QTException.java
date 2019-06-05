
package quicktime;

import java.lang.reflect.Field;

import quicktime.util.QTBuild;


public class QTException extends Exception {
    public static String errorCodeToString(int paramInt) {
        try {
            if (paramInt == -108) {
                return "memFullErr";
            }
            Class localClass = ClassLoader.getSystemClassLoader().loadClass("quicktime.Errors");
            Field[] arrayOfField = localClass.getFields();
            for (int i = 0; i < arrayOfField.length; i++) {
                if (arrayOfField[i].getInt(null) == paramInt)
                    return arrayOfField[i].getName();
            }
        } catch (Exception localException) {
            return "Exception generated: " + localException.toString();
        }
        return "Unknown Error Code";
    }

    public QTException(String paramString) {
        super(paramString);
    }

    public QTException(int paramInt) {
        this(new Integer(paramInt).toString());
    }

    public static void checkError(int paramInt) throws QTException {
        if ((paramInt < 0) && (isDrawingError(paramInt) < 0)) {
            throw new QTException(paramInt);
        }
    }

    public static int isDrawingError(int paramInt) {
        switch (paramInt) {

        case -8988:
        case -8985:
        case -8984:
        case -8983:
        case -8982:
        case -8981:
        case -8980:
        case -8976:
            return 0;
        }

        return paramInt;
    }

    private int eCode = 0;

    public int errorCode() {
        return this.eCode;
    }

    public String errorCodeToString() {
        return errorCodeToString(this.eCode);
    }

    public String toString() {
        String str = Integer.toString(this.eCode) + "=" + errorCodeToString();
        return getClass().getName() + QTBuild.info() + "," + str + ",QT.vers:" + Integer.toHexString(QTSession.getQTVersion());
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/
 * QTException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
