package quicktime.jdirect;

public abstract interface CarbonCoreLib extends SharedLibrary {

    public static final Object libraryInstance = QTNative.loadPL();

    public static final String JDirect_MacOSX = "/System/Library/Frameworks/CoreServices.framework/Versions/A/CoreServices";
}
