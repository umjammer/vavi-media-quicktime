
package quicktime.jdirect;

public abstract interface QuickDraw3DLib extends SharedLibrary {
    public static final Object libraryInstance = QTNative.load3D();
}
