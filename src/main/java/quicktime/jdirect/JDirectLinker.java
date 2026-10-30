package quicktime.jdirect;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import quicktime.QTSession;
import quicktime.internal.toolbox.PlatformManager;

public class JDirectLinker {

    private static final Logger logger = System.getLogger(JDirectLinker.class.getName());

    private static final String kLibraryInstanceFieldName = "libraryInstance";

    static class MethodSpec {

        public String name;

        public String signature;

        public int frameSize;

        public MethodSpec() {
        }

        public MethodSpec(String name, String signature, int frameSize) {
            this.name = name;
            this.signature = signature;
            this.frameSize = frameSize;
        }

        public String toString() {
            return this.name + this.signature + "@" + this.frameSize;
        }
    }

    static class Library {

        final String libraryName;

        final int libraryHandle;

        Library(String name) {
            this.libraryName = (name + ".dll");
            this.libraryHandle = JDirectLinker.loadNativeLibrary(this.libraryName);
        }

        protected void dispose() {
            JDirectLinker.unloadNativeLibrary(this.libraryHandle);
        }
    }

    public static Object loadLibrary(String name) {
        return new Library(name);
    }

    public static void linkNativeMethods(Class<?> clazz, String[] libraryNames) {
        int[] libraryHandles = loadNativeLibraries(libraryNames);
        MethodSpec[] nativeMethods = getNativeMethods(clazz);
        int i = nativeMethods.length;
        for (MethodSpec methodSpec : nativeMethods) {
            linkNativeMethod(clazz, methodSpec.name, methodSpec.signature, methodSpec.frameSize, libraryHandles);
        }
    }

    /**
     * Uses interfaces to supply the DLL handles.
     */
    public static void linkNativeMethods(Class<?> clazz) {
        int[] libraryHandles = gatherLibraries(clazz);
        MethodSpec[] nativeMethods = getNativeMethods(clazz);
        int i = nativeMethods.length;
        for (MethodSpec methodSpec : nativeMethods) {
            linkNativeMethod(clazz, methodSpec.name, methodSpec.signature, methodSpec.frameSize, libraryHandles);
        }
    }

    private static int[] gatherLibraries(Class<?> clazz) {
        Vector<Library> vector = new Vector<>();
        while (clazz != null) {
            examineInterfaces(vector, clazz.getInterfaces());
            clazz = clazz.getSuperclass();
        }
        int i = vector.size();
        int[] arrayOfInt = new int[i];
        for (int j = 0; j < i; j++) {
            Library library = vector.elementAt(j);
            arrayOfInt[j] = library.libraryHandle;
        }
        return arrayOfInt;
    }

    private static void examineInterfaces(Vector<Library> vector, Class<?>[] classes) {
        int i = classes.length;
        for (Class<?> clazz : classes) {
            if ((SharedLibrary.class != clazz) && (SharedLibrary.class.isAssignableFrom(clazz))) {
                try {
                    Field declaredField = clazz.getDeclaredField("libraryInstance");
                    if (declaredField != null) {
                        Library library = (Library) declaredField.get(clazz);
                        if (library != null) {
                            vector.addElement(library);
                        }
                    }
                } catch (NoSuchFieldException _) {
                } catch (IllegalAccessException e) {
                    logger.log(Level.DEBUG, "JDirectLinker.examineInterfaces: can't access library field from interface: " + clazz.getName());
                } catch (Throwable e) {
                    logger.log(Level.DEBUG, "JDirectLinker.examineInterfaces: exception: " + e);
                }
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces.length != 0) {
                    examineInterfaces(vector, interfaces);
                }
            }
        }
    }

    private static native int[] loadNativeLibraries(String[] libraryNames) throws UnsatisfiedLinkError;

    private static native void linkNativeMethod(Class<?> clazz, String name, String signature, int frameSize, int[] libraryHandles) throws UnsatisfiedLinkError;

    private static int loadNativeLibrary(String libName) {
        return PlatformManager.loadNativeLibrary(libName);
    }

    private static void unloadNativeLibrary(int library) {
        PlatformManager.unloadNativeLibrary(library);
    }

    private static MethodSpec[] getNativeMethods(Class<?> clazz) {
        Vector<MethodSpec> vector = new Vector<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        int i = declaredMethods.length;
        for (Method method : declaredMethods) {
            if (Modifier.isNative(method.getModifiers())) {
                String name = method.getName();
                String signature = getSignature(method);
                int k = getFrameSize(method);
                vector.addElement(new MethodSpec(name, signature, k));
            }
        }
        MethodSpec[] nativeMethods = new MethodSpec[vector.size()];
        vector.copyInto(nativeMethods);
        return nativeMethods;
    }

    private static int getFrameSize(Method method) {
        int i = 0;
        Class<?>[] parameterTypes = method.getParameterTypes();
        int j = parameterTypes.length;
        for (Class<?> ofClass : parameterTypes) i += getArgSize(ofClass);
        return i;
    }

    private static int getArgSize(Class<?> clazz) {
        String name = clazz.getName();
        if (clazz.isPrimitive()) {
            int i = Character.toUpperCase(name.charAt(0));
            return switch (i) {
                case 68, 76 -> 8;
                default -> 4;
            };
        }
        return 4;
    }

    private static String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        Class<?>[] parameterTypes = method.getParameterTypes();
        int i = parameterTypes.length;
        for (Class<?> ofClass : parameterTypes) {
            sb.append(getSignature(ofClass));
        }
        sb.append(')');
        sb.append(getSignature(method.getReturnType()));
        return sb.toString();
    }

    private static String getSignature(Class<?> clazz) {
        String name = clazz.getName();
        if (clazz.isPrimitive()) {
            char i = Character.toUpperCase(name.charAt(0));
            switch(i) {
                case 66:
                    if (clazz == Byte.TYPE) {
                        return "B";
                    }
                    return "Z";
                case 76:
                    return "J";
            }
            char[] arrayOfChar = { i };
            return new String(arrayOfChar);
        }
        if (clazz.isArray()) {
            return "[" + getSignature(clazz.getComponentType());
        }
        return "L" + name.replace('.', '/') + ";";
    }

    static {
        if (QTSession.isCurrentOS(2)) {
            System.loadLibrary("QTJava");
        }
    }
}
