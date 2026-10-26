/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;


/**
 * The standard compression settings dialog in Swing.
 */
final class SCDialog {

    private SCDialog() {
    }

    private static final String[] DEPTHS = {"Best", "Millions of Colors", "Millions of Colors+", "Thousands of Colors", "256 Colors", "256 Grays"};
    private static final int[] DEPTH_VALUES = {0, 24, 32, 16, 8, 40};

    private static final String[] SOUND_FORMATS = {"twos", "sowt", "raw ", "ulaw", "alaw", "fl32", "in24", "in32"};

    /** @return true if the settings were accepted */
    static boolean show(ImageCompressionManager.SC s, boolean sequence) {
        boolean[] ok = new boolean[1];
        Runnable r = () -> ok[0] = s.sound ? showSound(s) : showImage(s, sequence);
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(r);
            } catch (Exception e) {
                return false;
            }
        }
        return ok[0];
    }

    private static void row(JPanel p, int y, String label, java.awt.Component c) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.gridy = y;
        g.gridx = 0;
        g.anchor = GridBagConstraints.EAST;
        p.add(new JLabel(label), g);
        g.gridx = 1;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        p.add(c, g);
    }

    private static boolean showImage(ImageCompressionManager.SC s, boolean sequence) {
        List<Codecs.Codec> codecs = new ArrayList<>();
        for (Codecs.Codec c : Codecs.all()) {
            if (c.canCompress()) {
                codecs.add(c);
            }
        }
        JComboBox<String> codec = new JComboBox<>(codecs.stream().map(c -> c.name).toArray(String[]::new));
        int current = s.spatial(0);
        for (int i = 0; i < codecs.size(); i++) {
            if (codecs.get(i).type == current) {
                codec.setSelectedIndex(i);
            }
        }
        JComboBox<String> depth = new JComboBox<>(DEPTHS);
        for (int i = 0; i < DEPTH_VALUES.length; i++) {
            if (DEPTH_VALUES[i] == s.spatial(8)) {
                depth.setSelectedIndex(i);
            }
        }
        JSlider quality = new JSlider(0, Codecs.codecLosslessQuality, Math.min(Codecs.codecLosslessQuality, Math.max(0, s.spatial(10))));
        JPanel p = new JPanel(new GridBagLayout());
        row(p, 0, "Compressor:", codec);
        row(p, 1, "Depth:", depth);
        row(p, 2, "Quality:", quality);
        JSpinner fps = new JSpinner(new SpinnerNumberModel(Math.max(1, s.temporal(4) >> 16), 1, 120, 1));
        JSpinner key = new JSpinner(new SpinnerNumberModel(Math.max(0, s.temporal(8)), 0, 9999, 1));
        if (sequence) {
            row(p, 3, "Frames per second:", fps);
            row(p, 4, "Key frame every:", key);
        }
        int r = JOptionPane.showConfirmDialog(null, p, "Compression Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return false;
        }
        byte[] sptl = new byte[14];
        Bytes.putInt(sptl, 0, codecs.get(Math.max(0, codec.getSelectedIndex())).type);
        Bytes.putShort(sptl, 8, DEPTH_VALUES[Math.max(0, depth.getSelectedIndex())]);
        Bytes.putInt(sptl, 10, quality.getValue());
        s.info.put(ImageCompressionManager.scSpatialSettingsType, sptl);
        if (sequence) {
            byte[] tprl = s.info.get(ImageCompressionManager.scTemporalSettingsType).clone();
            Bytes.putInt(tprl, 0, quality.getValue());
            Bytes.putInt(tprl, 4, ((Integer) fps.getValue()) << 16);
            Bytes.putInt(tprl, 8, (Integer) key.getValue());
            s.info.put(ImageCompressionManager.scTemporalSettingsType, tprl);
        }
        return true;
    }

    private static boolean showSound(ImageCompressionManager.SC s) {
        JComboBox<String> format = new JComboBox<>(SOUND_FORMATS);
        format.setSelectedItem(Bytes.fourCC(Bytes.getInt(s.info.get(ImageCompressionManager.scSoundCompressionType), 0)));
        JComboBox<Integer> rate = new JComboBox<>(new Integer[] {8000, 11025, 16000, 22050, 32000, 44100, 48000, 96000});
        rate.setSelectedItem((int) ((Bytes.getInt(s.info.get(ImageCompressionManager.scSoundSampleRateType), 0) & 0xffff_ffffL) >> 16));
        JComboBox<Integer> size = new JComboBox<>(new Integer[] {8, 16, 24, 32});
        size.setSelectedItem((int) Bytes.getShort(s.info.get(ImageCompressionManager.scSoundSampleSizeType), 0));
        JComboBox<Integer> channels = new JComboBox<>(new Integer[] {1, 2});
        channels.setSelectedItem((int) Bytes.getShort(s.info.get(ImageCompressionManager.scSoundChannelCountType), 0));
        JPanel p = new JPanel(new GridBagLayout());
        row(p, 0, "Format:", format);
        row(p, 1, "Rate:", rate);
        row(p, 2, "Size:", size);
        row(p, 3, "Channels:", channels);
        int r = JOptionPane.showConfirmDialog(null, p, "Sound Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return false;
        }
        s.info.put(ImageCompressionManager.scSoundCompressionType, ImageCompressionManager.int4(Bytes.fourCC((String) format.getSelectedItem())));
        s.info.put(ImageCompressionManager.scSoundSampleRateType, ImageCompressionManager.int4(((Integer) rate.getSelectedItem()) << 16));
        byte[] b = new byte[2];
        Bytes.putShort(b, 0, (Integer) size.getSelectedItem());
        s.info.put(ImageCompressionManager.scSoundSampleSizeType, b);
        b = new byte[2];
        Bytes.putShort(b, 0, (Integer) channels.getSelectedItem());
        s.info.put(ImageCompressionManager.scSoundChannelCountType, b);
        return true;
    }
}
