package quicktime;

public class QTUnknownOSException extends QTException {

    public QTUnknownOSException() {
        super(System.getProperty("os.name"));
    }
}
