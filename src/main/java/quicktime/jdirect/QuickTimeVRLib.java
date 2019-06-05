
package quicktime.jdirect;

public abstract interface QuickTimeVRLib extends SharedLibrary {
    public static final Object libraryInstance = QTNative.loadVR();

    public static final String JDirect_MacOSX = "/System/Library/Frameworks/QuickTime.framework/Versions/A/QuickTime";
}
