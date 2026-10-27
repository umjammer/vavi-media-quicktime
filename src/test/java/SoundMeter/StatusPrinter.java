package SoundMeter;

import quicktime.QTException;
import quicktime.app.time.Ticklish;
import quicktime.std.movies.media.AudioMediaHandler;


class StatusPrinter implements Ticklish {

    private final AudioMediaHandler mh;
    private final int numBands;
    private final int[] freqs;

    StatusPrinter(AudioMediaHandler mh, int num, int[] freqs) {
        this.mh = mh;
        numBands = num;
        this.freqs = freqs;
    }

    @Override
    public boolean tickle(float er, int time) throws QTException {
        getInfo();
        // System.out.println ("tickle:er=" + er + ",time=" + time);
        return true;
    }

    @Override
    public void timeChanged(int time) throws QTException {
        // new Exception().printStackTrace();
        // System.out.println ("tc:" + time);
        for (int i = 0; i < numBands; i++) {
            int f = freqs[i];
            String str = null;
            if (f >= 1000) {
                if (f % 1000 != 0)
                    str = f / 1000 + ".5\t";
                else
                    str = f / 1000 + "\t";
            } else
                str = f + "\t";
            System.out.print(str);
        }
        System.out.println("\n___________________________________________________________________________________________");
    }

    public void getInfo() throws QTException {
        int[] levels = mh.getSoundEqualizerBandLevels(numBands);
        for (int i = 0; i < numBands; i++)
            System.out.print(levels[i] + "\t");
        System.out.println();
        // System.out.println (mh.getSoundEqualizerBands(8));
    }

}