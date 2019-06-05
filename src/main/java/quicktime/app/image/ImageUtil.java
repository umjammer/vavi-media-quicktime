
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
 */
public final class ImageUtil {
    private static final boolean debug = false;

    public static final int kHitTestRgnSrc = 1768776048;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public static ImageDataSequence makeTransparent(ImageSpec paramImageSpec, QDColor paramQDColor) throws QTException {
        return makeTransparent(paramImageSpec,
                               paramQDColor,
                               new QDGraphics(16, paramImageSpec.getDescription().getBounds()),
                               null,
                               null);
    }

    /**
     * @deprecated
     */
    public static ImageDataSequence makeTransparent(ImageSpec paramImageSpec,
                                                    QDColor paramQDColor,
                                                    QDGraphics paramQDGraphics) throws QTException {
        return makeTransparent(paramImageSpec, paramQDColor, paramQDGraphics, null, null);
    }

    /**
     * @deprecated
     */
    public static ImageDataSequence makeTransparent(ImageSpec paramImageSpec,
                                                    QDColor paramQDColor,
                                                    QDGraphics paramQDGraphics,
                                                    Region paramRegion) throws QTException {
        return makeTransparent(paramImageSpec, paramQDColor, paramQDGraphics, paramRegion, null);
    }

    /**
     * @deprecated
     */
    public static ImageDataSequence makeTransparent(ImageDataSequence paramImageDataSequence,
                                                    QDColor paramQDColor,
                                                    QDGraphics paramQDGraphics,
                                                    Region[] paramArrayOfRegion) throws QTException {
        return makeTransparent(paramImageDataSequence, paramQDColor, paramQDGraphics, null, paramArrayOfRegion);
    }

    private static ImageDataSequence makeTransparent(ImageSpec paramImageSpec,
                                                     QDColor paramQDColor,
                                                     QDGraphics paramQDGraphics,
                                                     Region paramRegion,
                                                     Region[] paramArrayOfRegion) throws QTException {
        quicktime.qd.QDRect localQDRect = paramQDGraphics.getPortRect();
        PixMap localPixMap1 = paramQDGraphics.getPixMap();
        int i = localPixMap1.getPixelSize();

        int j = (paramRegion != null) || (paramArrayOfRegion != null) ? 1 : 0;

        DSequenceFromMemory localDSequenceFromMemory = new DSequenceFromMemory(paramImageSpec);
        localDSequenceFromMemory.setGWorld(paramQDGraphics);
        ImageDataSequence localImageDataSequence1 = new ImageDataSequence();

        CSequenceToMemory localCSequenceToMemory = new CSequenceToMemory(2);

        QDGraphics localQDGraphics1 = null;
        QDGraphics localQDGraphics2 = null;
        PixMap localPixMap2 = null;
        ImageDescription localImageDescription = null;
        ImageSequenceDataSource localImageSequenceDataSource = null;
        if (j != 0) {
            localQDGraphics1 = new QDGraphics(localQDRect);
            localQDGraphics2 = new QDGraphics(8, localQDRect);
            localPixMap2 = localQDGraphics2.getPixMap();
            localImageDescription = new ImageDescription(localPixMap2);
            localImageDescription.lock();
        }

        if ((paramImageSpec instanceof ImageDataSequence)) {
            ImageDataSequence localImageDataSequence2 = (ImageDataSequence) paramImageSpec;
            for (int k = 1; k <= localImageDataSequence2.size(); k++) {
                if (j != 0) {
                    localCSequenceToMemory.begin(localQDGraphics1,
                                                 localQDRect,
                                                 i,
                                                 1919706400,
                                                 CodecComponent.anyCodec,
                                                 1024,
                                                 1024,
                                                 2,
                                                 paramImageSpec.getDescription().getCTable(),
                                                 0);

                    localImageSequenceDataSource = new ImageSequenceDataSource(localCSequenceToMemory.getSequence(),
                                                                               1768776048,
                                                                               1,
                                                                               localImageDescription);
                    localImageSequenceDataSource.setSourceData(RawEncodedImage.fromPixMap(localPixMap2));

                    localQDGraphics1.setBackColor(paramQDColor);
                    localQDGraphics1.eraseRect(null);
                    localQDGraphics2.setBackColor(QDColor.white);
                    localQDGraphics2.eraseRect(null);
                    localQDGraphics2.setForeColor(QDColor.black);
                    localQDGraphics2.paintRgn(paramRegion != null ? paramRegion : paramArrayOfRegion[(k - 1)]);

                    localCSequenceToMemory.compressFrame(localQDGraphics1, localQDRect, 0);

                    localCSequenceToMemory.getSequence().setPrev(localQDGraphics1, null);

                    paramQDGraphics.setBackColor(paramQDColor);
                    paramQDGraphics.eraseRect(null);
                } else {
                    localCSequenceToMemory.begin(paramQDGraphics,
                                                 localQDRect,
                                                 i,
                                                 1919706400,
                                                 CodecComponent.anyCodec,
                                                 1024,
                                                 1024,
                                                 2,
                                                 paramImageSpec.getDescription().getCTable(),
                                                 0);

                    paramQDGraphics.setBackColor(paramQDColor);
                    paramQDGraphics.eraseRect(null);

                    localCSequenceToMemory.compressFrame(paramQDGraphics, localQDRect, 4);
                }

                localDSequenceFromMemory.redraw(null);

                localCSequenceToMemory.compressFrame(paramQDGraphics, localQDRect, j != 0 ? 0 : 4);

                if (k == 1) {
                    localImageDataSequence1.setDescription(localCSequenceToMemory.getDescription());
                    localImageDataSequence1.getDescription().setFrameCount(localImageDataSequence2.size());
                }

                localImageDataSequence1.append((quicktime.util.EncodedImage) localCSequenceToMemory.getFrames().elementAt(1));

                localDSequenceFromMemory.setFrameNext();

                if (k % 3 == 0)
                    QTUtils.reclaimMemory();
            }
        }
        if (j != 0) {
            localCSequenceToMemory
                    .begin(localQDGraphics1,
                           localQDRect,
                           i,
                           1919706400,
                           CodecComponent.anyCodec,
                           1024,
                           1024,
                           2,
                           paramImageSpec.getDescription().getCTable(),
                           0);

            localImageSequenceDataSource = new ImageSequenceDataSource(localCSequenceToMemory.getSequence(),
                                                                       1768776048,
                                                                       1,
                                                                       localImageDescription);
            localImageSequenceDataSource.setSourceData(RawEncodedImage.fromPixMap(localPixMap2));

            localQDGraphics1.setBackColor(paramQDColor);
            localQDGraphics1.eraseRect(null);
            localQDGraphics2.setBackColor(QDColor.white);
            localQDGraphics2.eraseRect(null);
            localQDGraphics2.setForeColor(QDColor.black);
            localQDGraphics2.paintRgn(paramRegion != null ? paramRegion : paramArrayOfRegion[0]);

            localCSequenceToMemory.compressFrame(localQDGraphics1, localQDRect, 0);

            localCSequenceToMemory.getSequence().setPrev(localQDGraphics1, null);

            paramQDGraphics.setBackColor(paramQDColor);
            paramQDGraphics.eraseRect(null);
        } else {
            localCSequenceToMemory
                    .begin(paramQDGraphics,
                           localQDRect,
                           i,
                           1919706400,
                           CodecComponent.anyCodec,
                           1024,
                           1024,
                           2,
                           paramImageSpec.getDescription().getCTable(),
                           0);

            paramQDGraphics.setBackColor(paramQDColor);
            paramQDGraphics.eraseRect(null);

            localCSequenceToMemory.compressFrame(paramQDGraphics, localQDRect, 4);
        }

        localDSequenceFromMemory.redraw(null);

        localCSequenceToMemory.compressFrame(paramQDGraphics, localQDRect, j != 0 ? 0 : 4);

        localImageDataSequence1.setDescription(localCSequenceToMemory.getDescription());
        localImageDataSequence1.getDescription().setFrameCount(1);

        localImageDataSequence1.append((quicktime.util.EncodedImage) localCSequenceToMemory.getFrames().elementAt(1));

        localPixMap1 = null;
        paramQDGraphics = null;

        return localImageDataSequence1;
    }

    private static File checkDir(String paramString) throws IOException {
        int i = paramString.charAt(paramString.length() - 1);
        if (i != 47) {
            paramString = paramString + "/";
        }
        File localFile = new File(paramString);
        if (!localFile.isDirectory())
            throw new IOException(paramString);
        return localFile;
    }

    /**
     * @deprecated
     */
    public static ImageDataSequence createSequence(quicktime.std.movies.Track paramTrack) throws QTException {
        Media localMedia = Media.getTrackMedia(paramTrack);
        int i = localMedia.getSampleCount();
        int j = 0;
        ImageDataSequence localImageDataSequence = new ImageDataSequence((ImageDescription) localMedia.getSampleDescription(1));
        for (int k = 1; k <= i; k++) {
            MediaSample localMediaSample = localMedia.getSample(0, j, 1);
            if (localMediaSample.descriptionIndex != 1)
                break;
            quicktime.util.QTPointerRef localQTPointerRef = ((quicktime.util.QTHandleRef) localMediaSample.data.clone())
                    .toQTPointer();
            localImageDataSequence.addMember(RawEncodedImage.fromQTPointer(localQTPointerRef));
            j += localMediaSample.duration;
            if (k % 5 == 0)
                QTUtils.reclaimMemory();
        }
        QTUtils.reclaimMemory();
        return localImageDataSequence;
    }

    /**
     * @deprecated
     */
    public static ImageDataSequence createSequence(File paramFile) throws IOException, QTException {
        return createSequence(paramFile, new FileFilter());
    }

    /**
     * @deprecated
     */
    public static ImageDataSequence createSequence(File paramFile, FileFilter paramFileFilter) throws IOException, QTException {
        if (!paramFile.exists()) {
            throw new java.io.FileNotFoundException(paramFile.getAbsolutePath());
        }

        String str1 = paramFile.getParent();
        if (str1.charAt(str1.length() - 1) != File.separatorChar) {
            str1 = str1 + File.separatorChar;
        }
        String str2 = paramFile.getName();
        paramFileFilter.extractPrefixAndSuffix(str2);

        File localFile = new File(str1);
        if (!localFile.isDirectory()) {
            throw new IOException(str1);
        }

        String[] arrayOfString = localFile.list(paramFileFilter);

        if (paramFileFilter != null) {
            arrayOfString = paramFileFilter.postProcess(arrayOfString);
        }

        ImageDataSequence localImageDataSequence = new ImageDataSequence();
        int i = 0;

        for (int j = 0; j < arrayOfString.length; j++) {
            QTFile localQTFile = null;
            localQTFile = new QTFile(str1 + arrayOfString[j]);
            GraphicsImporterDrawer localGraphicsImporterDrawer = new GraphicsImporterDrawer(localQTFile);

            if ((j == 0) || (localImageDataSequence.isEmpty())) {
                localImageDataSequence.setDescription(localGraphicsImporterDrawer.getDescription());
            }
            localImageDataSequence.append(localGraphicsImporterDrawer.getImage());
            i++;
        }

        if (localImageDataSequence == null)
            throw new java.io.FileNotFoundException(paramFile.getAbsolutePath());
        localImageDataSequence.getDescription().setFrameCount(i);
        return localImageDataSequence;
    }
}
