package quicktime.jdirect;

public interface CarbonCoreLib extends SharedLibrary {

    Object libraryInstance = QTNative.loadPL();

    String JDirect_MacOSX = "/System/Library/Frameworks/CoreServices.framework/Versions/A/CoreServices";
}
