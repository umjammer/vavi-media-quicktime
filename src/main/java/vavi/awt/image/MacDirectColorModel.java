package vavi.awt.image;

import java.awt.Image;
import java.awt.image.ColorModel;


public class MacDirectColorModel extends ColorModel {

    /**
     * @param bits
     */
    public MacDirectColorModel(int bits) {
        super(bits);
    }

    @Override
    public int getRed(int pixel) {
        return 0;
    }

    @Override
    public int getGreen(int pixel) {
        return 0;
    }

    @Override
    public int getBlue(int pixel) {
        return 0;
    }

    @Override
    public int getAlpha(int pixel) {
        return 0;
    }

    /**
     * @param id
     * @param i
     * @param width
     * @param height
     * @param bool
     * @return
     */
    public static Image GetWrappedGWorldImage(int id, int i, int width, int height, boolean bool) {
        return null;
    }
}
