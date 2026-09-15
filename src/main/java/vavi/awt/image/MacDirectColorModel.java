package vavi.awt.image;

import java.awt.Image;
import java.awt.image.ColorModel;


public class MacDirectColorModel extends ColorModel {

    /**
     * @param bits
     */
    public MacDirectColorModel(int bits) {
        super(bits);
        // TODO Auto-generated constructor stub
    }

    /* @see java.awt.image.ColorModel#getRed(int) */
    @Override
    public int getRed(int pixel) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* @see java.awt.image.ColorModel#getGreen(int) */
    @Override
    public int getGreen(int pixel) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* @see java.awt.image.ColorModel#getBlue(int) */
    @Override
    public int getBlue(int pixel) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* @see java.awt.image.ColorModel#getAlpha(int) */
    @Override
    public int getAlpha(int pixel) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

}

/* */
