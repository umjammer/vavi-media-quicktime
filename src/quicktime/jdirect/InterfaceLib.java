
package quicktime.jdirect;

public abstract interface InterfaceLib extends SharedLibrary {
  public static final Object libraryInstance = QTNative.loadIL();

    public static final String JDirect_MacOSX = "/System/Library/Frameworks/Carbon.framework/Versions/A/Carbon";
}
