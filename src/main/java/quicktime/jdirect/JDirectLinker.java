package quicktime.jdirect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;
import quicktime.QTSession;

public class JDirectLinker {

    private static final boolean debug = false;

    private static final String kLibraryInstanceFieldName = "libraryInstance";

    static class MethodSpec {

        public String name;

        public String signature;

        public int frameSize;

        public MethodSpec() {
        }

        public MethodSpec(String paramString1, String paramString2, int paramInt) {
            this.name = paramString1;
            this.signature = paramString2;
            this.frameSize = paramInt;
        }

        public String toString() {
            return this.name + this.signature + "@" + this.frameSize;
        }
    }

    static class Library {

        String libraryName;

        int libraryHandle;

        Library(String paramString) {
            this.libraryName = (paramString + ".dll");
            this.libraryHandle = JDirectLinker.loadNativeLibrary(this.libraryName);
        }

        protected void finalize() {
            JDirectLinker.unloadNativeLibrary(this.libraryHandle);
        }
    }

    public static Object loadLibrary(String paramString) {
        return new Library(paramString);
    }

    public static void linkNativeMethods(Class<?> paramClass, String[] paramArrayOfString) {
        int[] arrayOfInt = loadNativeLibraries(paramArrayOfString);
        MethodSpec[] arrayOfMethodSpec = getNativeMethods(paramClass);
        int i = arrayOfMethodSpec.length;
        for (int j = 0; j < i; j++) {
            MethodSpec localMethodSpec = arrayOfMethodSpec[j];
            linkNativeMethod(paramClass, localMethodSpec.name, localMethodSpec.signature, localMethodSpec.frameSize, arrayOfInt);
        }
    }

    /**
     * Uses interfaces to supply the DLL handles.
     */
    public static void linkNativeMethods(Class<?> paramClass) {
        int[] arrayOfInt = gatherLibraries(paramClass);
        MethodSpec[] arrayOfMethodSpec = getNativeMethods(paramClass);
        int i = arrayOfMethodSpec.length;
        for (int j = 0; j < i; j++) {
            MethodSpec localMethodSpec = arrayOfMethodSpec[j];
            linkNativeMethod(paramClass, localMethodSpec.name, localMethodSpec.signature, localMethodSpec.frameSize, arrayOfInt);
        }
    }

    private static int[] gatherLibraries(Class<?> paramClass) {
        Vector<Library> localVector = new Vector<>();
        while (paramClass != null) {
            examineInterfaces(localVector, paramClass.getInterfaces());
            paramClass = paramClass.getSuperclass();
        }
        int i = localVector.size();
        int[] arrayOfInt = new int[i];
        for (int j = 0; j < i; j++) {
            Library localLibrary = localVector.elementAt(j);
            arrayOfInt[j] = localLibrary.libraryHandle;
        }
        return arrayOfInt;
    }

    private static void examineInterfaces(Vector<Library> paramVector, Class<?>[] paramArrayOfClass) {
        int i = paramArrayOfClass.length;
        for (int j = 0; j < i; j++) {
            Class<?> localClass = paramArrayOfClass[j];
            if ((SharedLibrary.class != localClass) && (SharedLibrary.class.isAssignableFrom(localClass))) {
                try {
                    Field localField = localClass.getDeclaredField("libraryInstance");
                    if (localField != null) {
                        Library localLibrary = (Library) localField.get(localClass);
                        if (localLibrary != null) {
                            paramVector.addElement(localLibrary);
                        }
                    }
                } catch (NoSuchFieldException localNoSuchFieldException) {
                } catch (IllegalAccessException localIllegalAccessException) {
                    System.err.println("JDirectLinker.examineInterfaces: can't access library field from interface: " + localClass.getName());
                } catch (Throwable localThrowable) {
                    System.err.println("JDirectLinker.examineInterfaces: exception: " + localThrowable);
                }
                Class<?>[] arrayOfClass = localClass.getInterfaces();
                if (arrayOfClass.length != 0) {
                    examineInterfaces(paramVector, arrayOfClass);
                }
            }
        }
    }

    private static native int[] loadNativeLibraries(String[] paramArrayOfString) throws UnsatisfiedLinkError;

    private static native void linkNativeMethod(Class<?> paramClass, String paramString1, String paramString2, int paramInt, int[] paramArrayOfInt) throws UnsatisfiedLinkError;

    private static native int loadNativeLibrary(String paramString);

    private static native void unloadNativeLibrary(int paramInt);

    private static MethodSpec[] getNativeMethods(Class<?> paramClass) {
        Vector<MethodSpec> localVector = new Vector<>();
        Method[] arrayOfMethod = paramClass.getDeclaredMethods();
        int i = arrayOfMethod.length;
        for (int j = 0; j < i; j++) {
            Method localMethod = arrayOfMethod[j];
            if (Modifier.isNative(localMethod.getModifiers())) {
                String str1 = localMethod.getName();
                String str2 = getSignature(localMethod);
                int k = getFrameSize(localMethod);
                localVector.addElement(new MethodSpec(str1, str2, k));
            }
        }
        MethodSpec[] arrayOfMethodSpec = new MethodSpec[localVector.size()];
        localVector.copyInto(arrayOfMethodSpec);
        return arrayOfMethodSpec;
    }

    private static int getFrameSize(Method paramMethod) {
        int i = 0;
        Class<?>[] arrayOfClass = paramMethod.getParameterTypes();
        int j = arrayOfClass.length;
        for (int k = 0; k < j; k++) i += getArgSize(arrayOfClass[k]);
        return i;
    }

    private static int getArgSize(Class<?> paramClass) {
        String str = paramClass.getName();
        if (paramClass.isPrimitive()) {
            int i = Character.toUpperCase(str.charAt(0));
            switch(i) {
                case 68:
                case 76:
                    return 8;
            }
            return 4;
        }
        return 4;
    }

    private static String getSignature(Method paramMethod) {
        StringBuffer sb = new StringBuffer();
        sb.append('(');
        Class<?>[] arrayOfClass = paramMethod.getParameterTypes();
        int i = arrayOfClass.length;
        for (int j = 0; j < i; j++) {
            sb.append(getSignature(arrayOfClass[j]));
        }
        sb.append(')');
        sb.append(getSignature(paramMethod.getReturnType()));
        return sb.toString();
    }

    private static String getSignature(Class<?> paramClass) {
        String str = paramClass.getName();
        if (paramClass.isPrimitive()) {
            char i = Character.toUpperCase(str.charAt(0));
            switch(i) {
                case 66:
                    if (paramClass == Byte.TYPE) {
                        return "B";
                    }
                    return "Z";
                case 76:
                    return "J";
            }
            char[] arrayOfChar = { i };
            return new String(arrayOfChar);
        }
        if (paramClass.isArray()) {
            return "[" + getSignature(paramClass.getComponentType());
        }
        return "L" + str.replace('.', '/') + ";";
    }

    static {
        if (QTSession.isCurrentOS(2)) {
            if (QTSession.getJavaVersion() == 65537) {
                System.loadLibrary("QTJava");
            } else {
                class PrivelegedAction {

                    void establish() {
                        AccessController.doPrivileged(new PrivilegedAction<Void>() {

                            public Void run() {
                                System.loadLibrary("QTJava");
                                return null;
                            }
                        });
                    }
                }
                new PrivelegedAction().establish();
            }
        }
    }
}
