/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Registry of method closures, the Java replacement for native callback procedure pointers (UPPs).
 * <p>
 * A closure binds a Java object and a method given by name and JNI descriptor (e.g. {@code "(II)V"})
 * to an {@code int} procedure pointer, which the emulated toolbox calls back through {@link #call(int, Object...)}.
 */
public final class Closures {

    private Closures() {
    }

    private record Closure(Object target, Method method) {
    }

    private static final Map<Integer, Closure> closures = new ConcurrentHashMap<>();

    /** @return a procedure pointer for the method */
    public static int newClosure(Object target, String name, String descriptor) {
        Method method = find(target.getClass(), name, parameterTypes(descriptor));
        method.setAccessible(true);
        int proc = Heap.newObject(target, 4);
        closures.put(proc, new Closure(target, method));
        return proc;
    }

    public static void dispose(int proc) {
        if (closures.remove(proc) != null) {
            Heap.free(proc);
        }
    }

    public static boolean isClosure(int proc) {
        return closures.containsKey(proc);
    }

    /**
     * Calls back the closure. Arguments are converted to the parameter types of the method.
     * @return the result of the method, null for void, or 0 if proc is 0
     */
    public static Object call(int proc, Object... args) {
        if (proc == 0) {
            return 0;
        }
        Closure c = closures.get(proc);
        if (c == null) {
            throw new IllegalArgumentException("not a closure: 0x" + Integer.toHexString(proc));
        }
        Class<?>[] types = c.method.getParameterTypes();
        Object[] a = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            a[i] = convert(i < args.length ? args[i] : 0, types[i]);
        }
        try {
            return c.method.invoke(c.target, a);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException r) {
                throw r;
            }
            if (e.getCause() instanceof Error r) {
                throw r;
            }
            throw new IllegalStateException(e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /** @return the int result of a callback */
    public static int callInt(int proc, Object... args) {
        Object r = call(proc, args);
        return r instanceof Number n ? n.intValue() : r instanceof Boolean b ? (b ? 1 : 0) : 0;
    }

    private static Object convert(Object v, Class<?> type) {
        if (!type.isPrimitive()) {
            return v;
        }
        Number n = v instanceof Boolean b ? (b ? 1 : 0) : (Number) v;
        if (type == int.class) return n.intValue();
        if (type == short.class) return n.shortValue();
        if (type == byte.class) return n.byteValue();
        if (type == long.class) return n.longValue();
        if (type == float.class) return n.floatValue();
        if (type == double.class) return n.doubleValue();
        if (type == boolean.class) return n.intValue() != 0;
        if (type == char.class) return (char) n.intValue();
        throw new IllegalArgumentException(type.getName());
    }

    private static Method find(Class<?> clazz, String name, Class<?>[] types) {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                return c.getDeclaredMethod(name, types);
            } catch (NoSuchMethodException e) {
                // try super class
            }
        }
        throw new IllegalArgumentException("no method " + name + " in " + clazz.getName());
    }

    /** @return parameter types of a JNI method descriptor */
    static Class<?>[] parameterTypes(String descriptor) {
        List<Class<?>> types = new ArrayList<>();
        int i = descriptor.indexOf('(') + 1;
        while (descriptor.charAt(i) != ')') {
            int dims = 0;
            while (descriptor.charAt(i) == '[') {
                dims++;
                i++;
            }
            Class<?> t;
            char c = descriptor.charAt(i);
            if (c == 'L') {
                int e = descriptor.indexOf(';', i);
                try {
                    t = Class.forName(descriptor.substring(i + 1, e).replace('/', '.'));
                } catch (ClassNotFoundException x) {
                    throw new IllegalArgumentException(descriptor, x);
                }
                i = e + 1;
            } else {
                t = switch (c) {
                    case 'I' -> int.class;
                    case 'S' -> short.class;
                    case 'B' -> byte.class;
                    case 'J' -> long.class;
                    case 'F' -> float.class;
                    case 'D' -> double.class;
                    case 'Z' -> boolean.class;
                    case 'C' -> char.class;
                    default -> throw new IllegalArgumentException(descriptor);
                };
                i++;
            }
            for (int d = 0; d < dims; d++) {
                t = t.arrayType();
            }
            types.add(t);
        }
        return types.toArray(Class[]::new);
    }
}
