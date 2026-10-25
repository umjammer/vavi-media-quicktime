/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Gestalt selectors and QuickTime preferences of the emulated toolbox.
 */
final class Gestalts {

    private Gestalts() {
    }

    /** gestaltUndefSelectorErr */
    static final short gestaltUndefSelectorErr = -5551;

    /** emulated QuickTime version, 7.6.8 */
    static final int QUICKTIME_VERSION = 0x0768_0000;

    private static final Map<Integer, Integer> selectors = Map.of(
            Bytes.fourCC("qtim"), QUICKTIME_VERSION,
            Bytes.fourCC("qtvr"), 0,
            Bytes.fourCC("qd3d"), 0,
            Bytes.fourCC("sysv"), 0x1040);

    static short gestalt(int selector, int[] response) {
        Integer v = selectors.get(selector);
        if (v == null) {
            return gestaltUndefSelectorErr;
        }
        response[0] = v;
        return 0;
    }

    /** preference type to atom container contents */
    private static final Map<Integer, byte[]> preferences = new ConcurrentHashMap<>();

    static short getPreference(int type, int[] container) {
        byte[] bytes = preferences.get(type);
        if (bytes == null) {
            return AtomManager.cannotFindAtomErr;
        }
        container[0] = Heap.newHandle(bytes);
        return 0;
    }

    static short setPreference(int type, int container) {
        if (container == 0) {
            preferences.remove(type);
        } else {
            preferences.put(type, Heap.handleBytes(container));
        }
        return 0;
    }
}
