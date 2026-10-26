/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


/**
 * The per thread result of the last Movie Toolbox call, returned by {@code GetMoviesError}.
 */
public final class MoviesError {

    private MoviesError() {
    }

    private static final ThreadLocal<Short> error = ThreadLocal.withInitial(() -> (short) 0);

    /** @return the error of the last call */
    public static short get() {
        return error.get();
    }

    /** records the error and returns it */
    public static short set(int err) {
        error.set((short) err);
        return (short) err;
    }

    /** clears the error */
    public static short ok() {
        error.set((short) 0);
        return 0;
    }
}
