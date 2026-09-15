package quicktime.qd;

/**
 * This exception is thrown if the NativeGraphics environment is not
 * initialized when you try to get the QDGraphics from the java.awt.Canvas
 */
public class NativeGraphicsException extends QDException {

    public NativeGraphicsException(String paramString) {
        super(paramString);
    }
}

