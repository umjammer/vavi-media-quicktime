/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


/**
 * Thrown when a QuickTime toolbox function has no implementation on this platform,
 * mostly because the subsystem (QuickTime VR, QuickDraw 3D, streaming, ...) has no modern equivalent.
 */
public class UnsupportedToolboxException extends UnsupportedOperationException {

    /** the QuickTime toolbox function name */
    private final String function;

    public UnsupportedToolboxException(String function) {
        super("QuickTime function '" + function + "' is not supported");
        this.function = function;
    }

    public UnsupportedToolboxException(String function, String reason) {
        super("QuickTime function '" + function + "' is not supported: " + reason);
        this.function = function;
    }

    /** @return the QuickTime toolbox function name */
    public String getFunction() {
        return function;
    }
}
