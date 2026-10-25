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
 * The settings dialog of a sequence grabber channel in Swing: the device, the compressor and the frame rate of video,
 * the device and the format of sound.
 */
final class SGDialog {

    private SGDialog() {
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

    /** @return true if the settings were accepted */
    static boolean show(SequenceGrabberManager.Chan c) {
        boolean[] ok = new boolean[1];
        Runnable r = () -> ok[0] = ask(c);
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

    private static boolean ask(SequenceGrabberManager.Chan c) {
        JPanel p = new JPanel(new GridBagLayout());
        List<String> devices = SequenceGrabberManager.deviceNames(c.type);
        JComboBox<String> device = new JComboBox<>(devices.toArray(String[]::new));
        if (c.device != null) {
            device.setSelectedItem(c.device);
        }
        row(p, 0, "Source:", device);
        if (c.type == SequenceGrabberManager.vide) {
            List<Codecs.Codec> codecs = new ArrayList<>();
            for (Codecs.Codec k : Codecs.all()) {
                if (k.canCompress()) {
                    codecs.add(k);
                }
            }
            JComboBox<String> codec = new JComboBox<>(codecs.stream().map(k -> k.name).toArray(String[]::new));
            for (int i = 0; i < codecs.size(); i++) {
                if (codecs.get(i).type == c.compressorType) {
                    codec.setSelectedIndex(i);
                }
            }
            JSlider quality = new JSlider(0, Codecs.codecLosslessQuality, Math.min(Codecs.codecLosslessQuality, c.spatialQuality));
            JSpinner fps = new JSpinner(new SpinnerNumberModel(c.frameRate == 0 ? 30 : Math.max(1, c.frameRate >> 16), 1, 120, 1));
            JSpinner key = new JSpinner(new SpinnerNumberModel(c.keyFrameRate, 0, 9999, 1));
            row(p, 1, "Compressor:", codec);
            row(p, 2, "Quality:", quality);
            row(p, 3, "Frames per second:", fps);
            row(p, 4, "Key frame every:", key);
            if (JOptionPane.showConfirmDialog(null, p, "Video Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
                return false;
            }
            if (device.getSelectedItem() != null) {
                SequenceGrabberManager.setDevice(c, (String) device.getSelectedItem());
            }
            c.compressorType = codecs.get(Math.max(0, codec.getSelectedIndex())).type;
            c.compressor = 0;
            c.spatialQuality = quality.getValue();
            c.frameRate = ((Integer) fps.getValue()) << 16;
            c.keyFrameRate = (Integer) key.getValue();
            c.temporalQuality = c.keyFrameRate > 1 ? c.spatialQuality : 0;
            return true;
        }
        JComboBox<Integer> rate = new JComboBox<>(new Integer[] {8000, 11025, 16000, 22050, 32000, 44100, 48000});
        rate.setSelectedItem((int) ((c.sampleRate & 0xffff_ffffL) >> 16));
        JComboBox<Integer> size = new JComboBox<>(new Integer[] {8, 16, 24});
        size.setSelectedItem(c.sampleSize);
        JComboBox<Integer> channels = new JComboBox<>(new Integer[] {1, 2});
        channels.setSelectedItem(c.numChannels);
        row(p, 1, "Rate:", rate);
        row(p, 2, "Size:", size);
        row(p, 3, "Channels:", channels);
        if (JOptionPane.showConfirmDialog(null, p, "Sound Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return false;
        }
        if (device.getSelectedItem() != null) {
            SequenceGrabberManager.setDevice(c, (String) device.getSelectedItem());
        }
        c.sampleRate = ((Integer) rate.getSelectedItem()) << 16;
        c.sampleSize = (Integer) size.getSelectedItem();
        c.numChannels = (Integer) channels.getSelectedItem();
        return true;
    }
}
