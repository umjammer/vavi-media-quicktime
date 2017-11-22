
package quicktime.jdirect;

public abstract interface PrimitivesLib extends SharedLibrary {
    public static final Object libraryInstance = QTNative.loadPL();

    public static final String JDirect_MacOSX = "/System/Library/QuickTimeJava/QuickTimeJava.bundle";
}
