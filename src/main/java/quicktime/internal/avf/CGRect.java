/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.avf;

import java.util.List;

import com.sun.jna.Structure;


/**
 * CoreGraphics CGRect with 64 bit CGFloats, passed by value.
 */
public class CGRect extends Structure implements Structure.ByValue {

    public double x;
    public double y;
    public double width;
    public double height;

    public CGRect() {
    }

    public CGRect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    protected List<String> getFieldOrder() {
        return List.of("x", "y", "width", "height");
    }
}
