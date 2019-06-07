package quicktime.jdirect;

/**
 * Base interface of all interfaces that represent platform shared
 * libraries. Sub-interfaces of SharedLibrary should contain a
 * static field of type Object, that is initialized in its static
 * initializer by calling JDirectLinker.loadLibrary.
 */
public abstract interface QTStreamingLib extends SharedLibrary {

    public static final Object libraryInstance = QTNative.loadQTS();

    public static final String JDirect_MacOSX = "/System/Library/Frameworks/QuickTime.framework/Versions/A/QuickTime";
}
