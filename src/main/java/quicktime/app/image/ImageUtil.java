package quicktime.app.image;

import java.io.File;
import java.io.IOException;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.qd.PixMap;
import quicktime.qd.QDColor;
import quicktime.qd.QDGraphics;
import quicktime.qd.Region;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.ImageSequenceDataSource;
import quicktime.std.movies.media.Media;
import quicktime.std.movies.media.MediaSample;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;

/**
 * @deprecated
 * Some utility routines for dealing with images
 */
public final class ImageUtil {

    private static final boolean debug = false;

    /**
     * @deprecated
     * This is the OSType that is used to create hit test data for kAnimationCodec compressed images* @deprecated since QTJava 6.1
     */
    public static final int kHitTestRgnSrc = 1768776048;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Takes an ImageSpec object and recompresses the images so that the images have
     * the keyColor set as a transparent color that will not draw. It uses the
     * animation codec to do this, and there is no loss in quality. This will also
     * have a default behavior of setting the hit test region of the image data to
     * the pixels that are drawn when the hitTestImageData flags are specified.
     * The images will be compressed at a default setting of 16bit resolution.
     * @param keyColor the color that will be removed from the images appearance
     * @return an ImageDataSequence object that contains the new images
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public static ImageDataSequence makeTransparent(ImageSpec spec, QDColor keyColor) throws QTException {
        return makeTransparent(spec, keyColor, new QDGraphics(16, spec.getDescription().getBounds()), null, null);
    }

    /**
     * Takes an ImageSpec object and recompresses the images so that the images have
     * the keyColor set as a transparent color that will not draw. It uses the
     * animation codec to do this, and there is no loss in quality. This will use the depth
     * of the supplied QDGraphics to compress into - pixel sizes other than 8, 16, or 32 will throw
     * an exception. This will also
     * have a default behavior of setting the hit test region of the image data to
     * the pixels that are drawn when the hitTestImageData flags are specified.
     * @param keyColor the color that will be removed from the images appearance
     * @param gw the QDGraphics that is used to contain the depth and pixel data of the recompressed image(s).
     *  This graphics should be the size of the image (or a portion) that will be used to determine which
     *  parts of the image are compressed.
     * @return an ImageDataSequence object that contains the new images
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public static ImageDataSequence makeTransparent(ImageSpec spec, QDColor keyColor, QDGraphics gw) throws QTException {
        return makeTransparent(spec, keyColor, gw, null, null);
    }

    /**
     * Takes an ImageSpec object and recompresses the images so that the images have
     * the keyColor set as a transparent color that will not draw. It uses the
     * animation codec to do this, and there is no loss in quality. This will use the depth
     * of the supplied QDGraphics to compress into - pixel sizes other than 8, 16, or 32 will throw
     * an exception. The r parameter allows the application to specify a Region that is used as a hit-test
     * region - whether pixels are visible from this image at those locations or not. The hit-test Region will
     * be applied to all of the images.
     * @param keyColor the color that will be removed from the images appearance
     * @param gw the QDGraphics that is used to contain the depth and pixel data of the recompressed image(s).
     *  This graphics should be the size of the image (or a portion) that will be used to determine which
     *  parts of the image are compressed.
     * @param r a hit test Region
     * @return an ImageDataSequence object that contains the new images
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public static ImageDataSequence makeTransparent(ImageSpec spec, QDColor keyColor, QDGraphics gw, Region r) throws QTException {
        return makeTransparent(spec, keyColor, gw, r, null);
    }

    /**
     * Takes an ImageSpec object and recompresses the images so that the images have
     * the keyColor set as a transparent colour that will not draw. It uses the
     * animation codec to do this, and there is no loss in quality. This will use the depth
     * of the source image to map the colour into. This will use the depth
     * of the supplied QDGraphics to compress into - pixel sizes other than 8, 16, or 32 will throw
     * an exception. The r parameter allows the application to specify an array of Regions that are used as hit-test
     * regions - whether pixels are visible from this image at those locations or not. This array must at least be the same length
     * as the number of images in the sequence. As each image is recompressed the corresponding element of the Region array
     * will be used as the hit-test Region for that image.
     * @param keyColor the color that will be removed from the images appearance
     * @param gw the QDGraphics that is used to contain the depth and pixel data of the recompressed image(s).
     *  This graphics should be the size of the image (or a portion) that will be used to determine which
     *  parts of the image are compressed.
     * @param r an array of Regions that supply hit test data for each image in the sequence.
     * @return an ImageDataSequence object that contains the new images
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public static ImageDataSequence makeTransparent(ImageDataSequence imageDataSequence, QDColor keyColor, QDGraphics gw, Region[] r) throws QTException {
        return makeTransparent(imageDataSequence, keyColor, gw, null, r);
    }

    private static ImageDataSequence makeTransparent(ImageSpec spec, QDColor color, QDGraphics src, Region region, Region[] regions) throws QTException {
        quicktime.qd.QDRect portRect = src.getPortRect();
        PixMap pixMap = src.getPixMap();
        int i = pixMap.getPixelSize();
        int j = (region != null) || (regions != null) ? 1 : 0;
        DSequenceFromMemory dSequenceFromMemory = new DSequenceFromMemory(spec);
        dSequenceFromMemory.setGWorld(src);
        ImageDataSequence transparent = new ImageDataSequence();
        CSequenceToMemory cSequenceToMemory = new CSequenceToMemory(2);
        QDGraphics port = null;
        QDGraphics qdGraphics2 = null;
        PixMap pm = null;
        ImageDescription imageDescription = null;
        ImageSequenceDataSource imageSequenceDataSource = null;
        if (j != 0) {
            port = new QDGraphics(portRect);
            qdGraphics2 = new QDGraphics(8, portRect);
            pm = qdGraphics2.getPixMap();
            imageDescription = new ImageDescription(pm);
            imageDescription.lock();
        }
        if ((spec instanceof ImageDataSequence imageDataSequence2)) {
            for (int k = 1; k <= imageDataSequence2.size(); k++) {
                if (j != 0) {
                    cSequenceToMemory.begin(port, portRect, i, 1919706400, CodecComponent.anyCodec, 1024, 1024, 2, spec.getDescription().getCTable(), 0);
                    imageSequenceDataSource = new ImageSequenceDataSource(cSequenceToMemory.getSequence(), 1768776048, 1, imageDescription);
                    imageSequenceDataSource.setSourceData(RawEncodedImage.fromPixMap(pm));
                    port.setBackColor(color);
                    port.eraseRect(null);
                    qdGraphics2.setBackColor(QDColor.white);
                    qdGraphics2.eraseRect(null);
                    qdGraphics2.setForeColor(QDColor.black);
                    qdGraphics2.paintRgn(region != null ? region : regions[(k - 1)]);
                    cSequenceToMemory.compressFrame(port, portRect, 0);
                    cSequenceToMemory.getSequence().setPrev(port, null);
                    src.setBackColor(color);
                    src.eraseRect(null);
                } else {
                    cSequenceToMemory.begin(src, portRect, i, 1919706400, CodecComponent.anyCodec, 1024, 1024, 2, spec.getDescription().getCTable(), 0);
                    src.setBackColor(color);
                    src.eraseRect(null);
                    cSequenceToMemory.compressFrame(src, portRect, 4);
                }
                dSequenceFromMemory.redraw(null);
                cSequenceToMemory.compressFrame(src, portRect, j != 0 ? 0 : 4);
                if (k == 1) {
                    transparent.setDescription(cSequenceToMemory.getDescription());
                    transparent.getDescription().setFrameCount(imageDataSequence2.size());
                }
                transparent.append(cSequenceToMemory.getFrames().elementAt(1));
                dSequenceFromMemory.setFrameNext();
                if (k % 3 == 0)
                    QTUtils.reclaimMemory();
            }
        }
        if (j != 0) {
            cSequenceToMemory.begin(port, portRect, i, 1919706400, CodecComponent.anyCodec, 1024, 1024, 2, spec.getDescription().getCTable(), 0);
            imageSequenceDataSource = new ImageSequenceDataSource(cSequenceToMemory.getSequence(), 1768776048, 1, imageDescription);
            imageSequenceDataSource.setSourceData(RawEncodedImage.fromPixMap(pm));
            port.setBackColor(color);
            port.eraseRect(null);
            qdGraphics2.setBackColor(QDColor.white);
            qdGraphics2.eraseRect(null);
            qdGraphics2.setForeColor(QDColor.black);
            qdGraphics2.paintRgn(region != null ? region : regions[0]);
            cSequenceToMemory.compressFrame(port, portRect, 0);
            cSequenceToMemory.getSequence().setPrev(port, null);
            src.setBackColor(color);
            src.eraseRect(null);
        } else {
            cSequenceToMemory.begin(src, portRect, i, 1919706400, CodecComponent.anyCodec, 1024, 1024, 2, spec.getDescription().getCTable(), 0);
            src.setBackColor(color);
            src.eraseRect(null);
            cSequenceToMemory.compressFrame(src, portRect, 4);
        }
        dSequenceFromMemory.redraw(null);
        cSequenceToMemory.compressFrame(src, portRect, j != 0 ? 0 : 4);
        transparent.setDescription(cSequenceToMemory.getDescription());
        transparent.getDescription().setFrameCount(1);
        transparent.append(cSequenceToMemory.getFrames().elementAt(1));
        pixMap = null;
        src = null;
        return transparent;
    }

    private static File checkDir(String dir) throws IOException {
        int i = dir.charAt(dir.length() - 1);
        if (i != 47) {
            dir = dir + "/";
        }
        File file = new File(dir);
        if (!file.isDirectory())
            throw new IOException(dir);
        return file;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This routine will create an ImageDataSeqeuence object from specified Track of a Movie.
     * This method gets image data that matches the first sample description that is
     * contained in the track, loads the individual frames into memory and returns an ImageDataSequence
     * that contains the image data as described by this ImageDescription.
     * @param imageTrack this track contains image data - it must contain a VideoMedia object.
     * @return a ImageDataSequence object that contains a sequence of image data and a single description
     *  that defines the compression, bounds, etc. that is common to all of the image data objects
     * @throws quicktime.QTException
     */
    public static ImageDataSequence createSequence(quicktime.std.movies.Track imageTrack) throws QTException {
        Media trackMedia = Media.getTrackMedia(imageTrack);
        int i = trackMedia.getSampleCount();
        int j = 0;
        ImageDataSequence sequence = new ImageDataSequence((ImageDescription) trackMedia.getSampleDescription(1));
        for (int k = 1; k <= i; k++) {
            MediaSample sample = trackMedia.getSample(0, j, 1);
            if (sample.descriptionIndex != 1)
                break;
            quicktime.util.QTPointerRef qtPointerRef = ((quicktime.util.QTHandleRef) sample.data.clone()).toQTPointer();
            sequence.addMember(RawEncodedImage.fromQTPointer(qtPointerRef));
            j += sample.duration;
            if (k % 5 == 0)
                QTUtils.reclaimMemory();
        }
        QTUtils.reclaimMemory();
        return sequence;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This routine will create an ImageDataSeqeuence object from the collection of
     * files that are found in the specified directory that begin with the specified
     * fileName. The default FileFilter object is passed in as the filter. The default
     * ImageUtil object is passed in as the image processor object.
     * @param matchFile this should be the full path specification of a file that will be used as
     *  a pattern to match against the other files that are contained in the directory that the
     *  match file is found in.
     * @return a ImageDataSequence object that contains a sequence of image data and a single description
     *  that defines the compression, bounds, etc. that is common to all of the image data objects
     * @return java.io.IOException
     * @throws quicktime.QTException
     * @see <A href="../../../quicktime/app/image/FileFilter.html">for a description of the format of the file name
     *  that this filter will deal with.</A>
     */
    public static ImageDataSequence createSequence(File matchFile) throws IOException, QTException {
        return createSequence(matchFile, new FileFilter());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This routine will create an ImageDataSeqeuence object from the collection of
     * files that are found in the specified directory that begin with the specified
     * fileName. You supply a FileFilter object to do custom filter operations. The default
     * ImageUtil object is passed in as the image processor object.
     * @param matchFile this should be the full path specification of a file that will be used as
     *  a pattern to match against the other files that are contained in the directory that the
     *  match file is found in.
     * @param ff the file filter object
     * @return a ImageDataSequence object that contains a sequence of image data and a single description
     *  that defines the compression, bounds, etc. that is common to all of the image data objects
     * @return java.io.IOException
     * @throws quicktime.QTException
     * @see <A href="../../../quicktime/app/image/FileFilter.html">for a description of the format of the file name
     *  that this filter will deal with and the methods that can be overridden or extended.</A>
     */
    public static ImageDataSequence createSequence(File matchFile, FileFilter ff) throws IOException, QTException {
        if (!matchFile.exists()) {
            throw new java.io.FileNotFoundException(matchFile.getAbsolutePath());
        }
        String parent = matchFile.getParent();
        if (parent.charAt(parent.length() - 1) != File.separatorChar) {
            parent = parent + File.separatorChar;
        }
        String fName = matchFile.getName();
        ff.extractPrefixAndSuffix(fName);
        File file = new File(parent);
        if (!file.isDirectory()) {
            throw new IOException(parent);
        }
        String[] fileNames = file.list(ff);
        if (ff != null) {
            fileNames = ff.postProcess(fileNames);
        }
        ImageDataSequence sequence = new ImageDataSequence();
        int i = 0;
        for (int j = 0; j < fileNames.length; j++) {
            QTFile qtFile = null;
            qtFile = new QTFile(parent + fileNames[j]);
            GraphicsImporterDrawer graphicsImporterDrawer = new GraphicsImporterDrawer(qtFile);
            if ((j == 0) || (sequence.isEmpty())) {
                sequence.setDescription(graphicsImporterDrawer.getDescription());
            }
            sequence.append(graphicsImporterDrawer.getImage());
            i++;
        }
        if (sequence == null)
            throw new java.io.FileNotFoundException(matchFile.getAbsolutePath());
        sequence.getDescription().setFrameCount(i);
        return sequence;
    }
}
