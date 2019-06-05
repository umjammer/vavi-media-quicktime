
package quicktime.jdirect;

public abstract interface QuickTimeLib extends InterfaceLib, PrimitivesLib, CarbonCoreLib {
    public static final Object libraryInstance = QTNative.loadQT();

    public static final String name = "QTJava";

    public static final String JDirect_MacOSX = "/System/Library/Frameworks/QuickTime.framework/Versions/A/QuickTime";
}
