package quicktime.jdirect;

/**
 * Base interface of all interfaces that represent platform shared
 * libraries. Sub-interfaces of SharedLibrary should contain a
 * static field of type Object, that is initialized in its static
 * initializer by calling JDirectLinker.loadLibrary.
 */
public interface QuickTimeVRLib extends SharedLibrary {

    Object libraryInstance = QTNative.loadVR();

    String JDirect_MacOSX = "/System/Library/Frameworks/QuickTime.framework/Versions/A/QuickTime";
}
