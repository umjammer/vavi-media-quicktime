
package quicktime.jdirect;

public abstract interface QTStreamingLib extends SharedLibrary {
    public static final Object libraryInstance = QTNative.loadQTS();

    public static final String JDirect_MacOSX = "/System/Library/Frameworks/QuickTime.framework/Versions/A/QuickTime";
}
