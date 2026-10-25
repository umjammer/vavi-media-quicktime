/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * The image export file dialog in Swing.
 */
final class ExportDialog {

    private ExportDialog() {
    }

    /** @return the chosen file type and path, null if canceled */
    static Object[] ask(String defaultPath, String title) {
        Object[][] result = new Object[1][];
        Runnable r = () -> {
            JFileChooser c = new JFileChooser();
            c.setDialogTitle(title);
            List<ImageFormats.Format> formats = new ArrayList<>();
            for (ImageFormats.Format f : ImageFormats.formats.values()) {
                if (f.canExport()) {
                    formats.add(f);
                    c.addChoosableFileFilter(new FileNameExtensionFilter(f.name(), f.extension()));
                }
            }
            c.setAcceptAllFileFilterUsed(false);
            if (defaultPath != null) {
                c.setSelectedFile(new File(defaultPath));
            }
            if (c.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            ImageFormats.Format f = formats.get(0);
            if (c.getFileFilter() instanceof FileNameExtensionFilter e) {
                for (ImageFormats.Format g : formats) {
                    if (g.name().equals(e.getDescription())) {
                        f = g;
                    }
                }
            }
            String path = c.getSelectedFile().getPath();
            if (!path.toLowerCase().endsWith("." + f.extension())) {
                path += "." + f.extension();
            }
            result[0] = new Object[] {f.fileType(), path};
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(r);
            } catch (Exception e) {
                return null;
            }
        }
        return result[0];
    }
}
