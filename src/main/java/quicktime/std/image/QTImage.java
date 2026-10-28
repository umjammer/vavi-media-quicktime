package quicktime.std.image;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.ColorTable;
import quicktime.qd.PixMap;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;


/**
 * The QTImage class provides static methods and constants for compressing and
 * decompressing QuickTime images.
 * <p>
 * Instance of this class can be used to associate an ImageDescription
 * with the encoded Image data that the description describes.
 */
public class QTImage implements QuickTimeLib {

    private static final int[] savedPort = {0};

    private static final int[] savedDevice = {0};

    private final ImageDescription desc;

    private final EncodedImage data;

    /**
     * Associate an image description with the image data that is described
     * by the description.
     *
     * @param id the image description
     * @param im the image data
     */
    public QTImage(ImageDescription id, EncodedImage im) {
        this.data = im;
        this.desc = id;
    }

    /**
     * Return the image data
     */
    public EncodedImage getImage() {
        return this.data;
    }

    /**
     * Return the description that describes the associated data.
     */
    public ImageDescription getDescription() {
        return this.desc;
    }

    /**
     * Compresses a single frame image that is currently stored as a pixel map.
     * The size of the compressed image can be obtained from the ImageDescription
     * object which is returned.
     *
     * @param src     the QDGraphics that contains the PixMap for compressing. This may NOT be null.
     * @param srcRect a Rect defining the portion of the image to compress
     * @param quality the desired compressed image quality
     * @param cType   a compressor type
     * @param data    a encoded image object to receive the compressed image data.
     *                It is your program's responsibility to make sure that this
     *                object can receive at least as much data as indicated by
     *                the getMaxCompressionSize method.
     * @return an ImageDescription object. Your application should store this
     * image description with the compressed image data.
     * @throws quicktime.QTException
     * @see "QuickTime::CompressImage"
     */
    public static ImageDescription compress(QDGraphics src, QDRect srcRect, int quality, int cType, EncodedImage data) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        if (src == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            if ((data instanceof ByteEncodedImage)) {
                i = CompressImage(QTObject.ID(src.getPixMap()), srcRect.getRect(), quality, cType, QTObject.ID(localImageDescription), ((ByteEncodedImage) data).getBytes());
            } else if ((data instanceof IntEncodedImage)) {
                i = CompressImage(QTObject.ID(src.getPixMap()), srcRect.getRect(), quality, cType, QTObject.ID(localImageDescription), ((IntEncodedImage) data).getInts());
            } else if ((data instanceof RawEncodedImage)) {
                i = CompressImage(QTObject.ID(src.getPixMap()), srcRect.getRect(), quality, cType, QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) data));
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return localImageDescription;
    }

    /**
     * Compresses a single frame image that is currently stored as a pixel map.
     * The size of the compressed image can be obtained from the ImageDescription
     * object which is returned.
     *
     * @param src        a QDGraphics that contains the PixMap for compressing. This may NOT be null.
     * @param srcRect    a QDRect defining the portion of the image to compress
     * @param colorDepth the depth at which the image is likely to be viewed
     * @param quality    the desired compressed image quality
     * @param cType      a compressor type
     * @param c          a compressor or codec identifier
     * @param clut       a custom color lookup table
     * @param flags      flags providing further control information
     * @param data       a EncodedImage object to receive the compressed image data.
     *                   It is your program's responsibility to make sure that this
     *                   object can receive at least as much data as indicated by
     *                   the getMaxCompressionSize method.
     * @return an ImageDescription object. Your application should store this
     * image description with the compressed image data.
     * @throws quicktime.QTException
     * @see "QuickTime::FCompressImage"
     */
    public static ImageDescription fCompress(QDGraphics src, QDRect srcRect, int colorDepth, int quality, int cType, CodecComponent c, ColorTable clut, int flags, EncodedImage data) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        if (src == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            if ((data instanceof ByteEncodedImage)) {
                i = FCompressImage(QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) colorDepth, quality, cType, QTObject.ID(c), QTObject.ID(clut), (short) flags, 0, 0, 0, QTObject.ID(localImageDescription), ((ByteEncodedImage) data).getBytes());
            } else if ((data instanceof IntEncodedImage)) {
                i = FCompressImage(QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) colorDepth, quality, cType, QTObject.ID(c), QTObject.ID(clut), (short) flags, 0, 0, 0, QTObject.ID(localImageDescription), ((IntEncodedImage) data).getInts());
            } else if ((data instanceof RawEncodedImage)) {
                i = FCompressImage(QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) colorDepth, quality, cType, QTObject.ID(c), QTObject.ID(clut), (short) flags, 0, 0, 0, QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) data));
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return localImageDescription;
    }

    /**
     * Converts the format of a compressed image.
     *
     * @param srcDD      the image description structure that describes the compressed image
     * @param srcData    the encoded image data to convert
     * @param colorDepth the depth at which the recompressed image is likely to be viewed
     * @param clut       a custom color look up table
     * @param accuracy   the accuracy for the decompressed image
     * @param quality    the desired compressed image quality
     * @param cType      the compressor type
     * @param codec      a CodecComponent
     * @return an ImageDescription object defining the converted image data. Your
     * application should store this image description with the converted
     * image data.
     * @throws quicktime.QTException
     * @see "QuickTime::ConvertImage"
     */
    public static ImageDescription convert(ImageDescription srcDD, EncodedImage srcData, int colorDepth, ColorTable clut, int accuracy, int quality, int cType, CodecComponent codec, EncodedImage outData) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        int i = -50;
        if ((srcData instanceof ByteEncodedImage)) {
            if ((outData instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), ((ByteEncodedImage) srcData).getBytes(), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), ((ByteEncodedImage) outData).getBytes());
            } else if ((outData instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), ((ByteEncodedImage) srcData).getBytes(), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), ((IntEncodedImage) outData).getInts());
            } else if ((outData instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), ((ByteEncodedImage) srcData).getBytes(), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) outData));
            }
        } else if ((srcData instanceof IntEncodedImage)) {
            if ((outData instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), ((IntEncodedImage) srcData).getInts(), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), ((ByteEncodedImage) outData).getBytes());
            } else if ((outData instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), ((IntEncodedImage) srcData).getInts(), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), ((IntEncodedImage) outData).getInts());
            } else if ((outData instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), ((IntEncodedImage) srcData).getInts(), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) outData));
            }
        } else if ((srcData instanceof RawEncodedImage)) {
            if ((outData instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), QTObject.ID((RawEncodedImage) srcData), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), ((ByteEncodedImage) outData).getBytes());
            } else if ((outData instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), QTObject.ID((RawEncodedImage) srcData), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), ((IntEncodedImage) outData).getInts());
            } else if ((outData instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(srcDD), QTObject.ID((RawEncodedImage) srcData), (short) colorDepth, QTObject.ID(clut), accuracy, quality, cType, QTObject.ID(codec), QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) outData));
            }
        }
        StdQTException.checkError(i);
        return localImageDescription;
    }

    /**
     * Ajusts a compressed image to the boundaries defined by a QDRect specified
     * by your application.
     *
     * @param desc     an ImageDescription object that describes the compressed image.
     *                 On return from this method, the compressor updates this image
     *                 description to refer to the resized image.
     * @param inData   the encoded image data
     * @param outData  a encoded image object to receive the trimmed image. Your application
     *                 should create this object at least as large as the source image.
     * @param trimRect defines the desired dimensions of the trimmed image
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::TrimImage"
     */
    public static void trim(ImageDescription desc, EncodedImage inData, EncodedImage outData, QDRect trimRect) throws StdQTException {
        int i = -50;
        if ((inData instanceof ByteEncodedImage)) {
            if ((outData instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), ((ByteEncodedImage) inData).getBytes(), 0, 0, ((ByteEncodedImage) outData).getBytes(), 0, 0, trimRect.getRect(), 0);
            } else if ((outData instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), ((ByteEncodedImage) inData).getBytes(), 0, 0, ((IntEncodedImage) outData).getInts(), 0, 0, trimRect.getRect(), 0);
            } else if ((outData instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), ((ByteEncodedImage) inData).getBytes(), 0, 0, QTObject.ID((RawEncodedImage) outData), 0, 0, trimRect.getRect(), 0);
            }
        } else if ((inData instanceof IntEncodedImage)) {
            if ((outData instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), ((IntEncodedImage) inData).getInts(), 0, 0, ((ByteEncodedImage) outData).getBytes(), 0, 0, trimRect.getBytes(), 0);
            } else if ((outData instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), ((IntEncodedImage) inData).getInts(), 0, 0, ((IntEncodedImage) outData).getInts(), 0, 0, trimRect.getBytes(), 0);
            } else if ((outData instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), ((IntEncodedImage) inData).getInts(), 0, 0, QTObject.ID((RawEncodedImage) outData), 0, 0, trimRect.getBytes(), 0);
            }
        } else if ((inData instanceof RawEncodedImage)) {
            if ((outData instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), QTObject.ID((RawEncodedImage) inData), 0, 0, ((ByteEncodedImage) outData).getBytes(), 0, 0, trimRect.getBytes(), 0);
            } else if ((outData instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), QTObject.ID((RawEncodedImage) inData), 0, 0, ((IntEncodedImage) outData).getInts(), 0, 0, trimRect.getBytes(), 0);
            } else if ((outData instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(desc), QTObject.ID((RawEncodedImage) inData), 0, 0, QTObject.ID((RawEncodedImage) outData), 0, 0, trimRect.getBytes(), 0);
            }
        }
        StdQTException.checkError(i);
    }

    /**
     * Decompresses a single frame image into a pixel map structure.
     *
     * @param data    a EncodedImage object that contains the compressed image data
     * @param desc    an ImageDescription object that defines the compressed image
     * @param dst     a QDGraphics that contains the PixMap where the decompressed image is to be displayed. This may NOT be null.
     * @param dstRect a QDRect into which the decompressed image is to be loaded
     * @param mode    the transfer mode for the operation
     * @throws quicktime.std.StdQTException
     * @throws quicktime.qd.QDException
     * @see "QuickTime::DecompressImage"
     */
    public static void decompress(EncodedImage data, ImageDescription desc, QDGraphics dst, QDRect dstRect, int mode) throws StdQTException, QDException {
        decompress(data, desc, dst, null, dstRect, mode, null);
    }

    /**
     * Decompresses a single frame image into a pixel map structure.
     *
     * @param data    a EncodedImage object that contains the compressed image data
     * @param desc    an ImageDescription object that defines the compressed image
     * @param dst     a QDGraphics that contains the PixMap where the decompressed image is to be displayed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to decompress
     * @param dstRect a QDRect into which the decompressed image is to be loaded
     * @param mode    the transfer mode for the operation
     * @param mask    a clipping region in the destination coordinate system
     * @throws quicktime.std.StdQTException
     * @throws quicktime.qd.QDException
     * @see "QuickTime::DecompressImage"
     */
    public static void decompress(EncodedImage data, ImageDescription desc, QDGraphics dst, QDRect srcRect, QDRect dstRect, int mode, Region mask) throws StdQTException, QDException {
        if (dst == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(dst), 0);
            if ((data instanceof ByteEncodedImage)) {
                i = DecompressImage(((ByteEncodedImage) data).getBytes(), QTObject.ID(desc), QTObject.ID(dst.getPixMap()), srcRect == null ? null : srcRect.getRect(), dstRect.getRect(), (short) mode, QTObject.ID(mask));
            } else if ((data instanceof IntEncodedImage)) {
                i = DecompressImage(((IntEncodedImage) data).getInts(), QTObject.ID(desc), QTObject.ID(dst.getPixMap()), srcRect == null ? null : srcRect.getRect(), dstRect.getRect(), (short) mode, QTObject.ID(mask));
            } else if ((data instanceof RawEncodedImage)) {
                i = DecompressImage(QTObject.ID((RawEncodedImage) data), QTObject.ID(desc), QTObject.ID(dst.getPixMap()), srcRect == null ? null : srcRect.getRect(), dstRect.getRect(), (short) mode, QTObject.ID(mask));
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    /**
     * Decompresses a single frame image into a pixel map structure.
     *
     * @param data      a EncodedImage object that contains the compressed image data
     * @param desc      an ImageDescription object that defines the compressed image
     * @param dst       a QDGraphics that contains the PixMap where the decompressed image is to be displayed. This may NOT be null.
     * @param srcRect   a QDRect defining the portion of the image to decompress
     * @param matrix    a Matrix specifies how to transform the image during decompression
     * @param mode      the transfer mode for the operation
     * @param mask      a Region in the destination coordinate system
     * @param matte     a PixMap that contains a blend matte
     * @param matteRect a QDRect defining a portion of the blend matte to apply
     * @param accuracy  the accuracy desired in the decompressed image
     * @param codec     a codec component
     * @throws quicktime.std.StdQTException
     * @throws quicktime.qd.QDException
     * @see "QuickTime::FDecompressImage"
     */
    public static void fDecompress(EncodedImage data, ImageDescription desc, QDGraphics dst, QDRect srcRect, Matrix matrix, int mode, Region mask, PixMap matte, QDRect matteRect, int accuracy, CodecComponent codec) throws StdQTException, QDException {
        if (dst == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(dst), 0);
            i = FDecompressImage(QTObject.ID((RawEncodedImage) data), QTObject.ID(desc), QTObject.ID(dst.getPixMap()), srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) mode, QTObject.ID(mask), QTObject.ID(matte), matteRect == null ? null : matteRect.getRect(), accuracy, QTObject.ID(codec), 0, 0, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    /**
     * Determines the maximum size an image will be after compression.  The size returned is in the
     * number of bytes that the encoded image will require.
     *
     * @param src        is a QDGraphics that contains the PixMap to be compressed. This may NOT be null.
     * @param srcRect    points to the QDRect defining the portion of the
     *                   source image that is to be compressed
     * @param colorDepth the depth at which the image is to be compressed
     * @param quality    the desired compressed image quality
     * @param cType      a compressor type
     * @param c          a compressor component
     * @return size of the compressed pixmap
     * @throws quicktime.std.StdQTException
     * @throws quicktime.qd.QDException
     * @see "see QuickTime::GetMaxCompressionSize"
     */
    public static int getMaxCompressionSize(QDGraphics src, QDRect srcRect, int colorDepth, int quality, int cType, CodecComponent c) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        if (src == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            i = GetMaxCompressionSize(QTObject.ID(src.getPixMap()), srcRect == null ? null : srcRect.getRect(), (short) colorDepth, quality, cType, QTObject.ID(c), arrayOfInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines the size in bytes of a compressed image.
     *
     * @param desc an ImageDescription object that defines the compressed image
     * @param data a EncodedImage object that contains the compressed image data
     * @return the size of the compressed image in bytes
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetCompressedImageSize"
     */
    public static int getCompressedSize(ImageDescription desc, EncodedImage data) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = -50;
        if ((data instanceof ByteEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(desc), ((ByteEncodedImage) data).getBytes(), 0, 0, arrayOfInt);
        } else if ((data instanceof IntEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(desc), ((IntEncodedImage) data).getInts(), 0, 0, arrayOfInt);
        } else if ((data instanceof RawEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(desc), QTObject.ID((RawEncodedImage) data), 0, 0, arrayOfInt);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines the estimated amount of time required to compress a given image.
     *
     * @param src             a QDGraphics that contains a PixMap object that will be compressed. This may be null.
     * @param srcRect         a QDRect defining the portion of the source image that is
     *                        to be compressed
     * @param colorDepth      the depth at which the image is to be compressed
     * @param cType           a compressor type
     * @param c               the compressor component
     * @param spatialQuality  the desired compressed image spatial quality
     * @param temporalQuality the desired compressed image temporal quality
     * @return the compression time in milliseconds
     * @throws quicktime.std.StdQTException
     * @throws quicktime.qd.QDException
     * @see "see QuickTime::GetCompressionTime"
     */
    public static int getCompressionTime(QDGraphics src, QDRect srcRect, int colorDepth, int cType, CodecComponent c, int spatialQuality, int temporalQuality) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        int i = 0;
        int j = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            if (src != null) {
                SetGWorld(QTObject.ID(src), 0);
                i = QTObject.ID(src.getPixMap());
            }
            j = GetCompressionTime(i, srcRect == null ? null : srcRect.getRect(), (short) colorDepth, cType, QTObject.ID(c), spatialQuality, temporalQuality, arrayOfInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(j);
        return arrayOfInt[0];
    }

    /**
     * Compares a compressed image to a picture stored in a pixel map and
     * returns a value indicating the relative similarity of the two images.
     *
     * @param src     a QDGraphics that contains a PixMap object containing a noncompressed image.
     * @param srcRect a QDRect defining the portion of the source image to compare
     *                to the compressed image
     * @param desc    an ImageDescription object that defines the compressed image
     * @param data    an EncodedImage object that contains the compressed image data
     * @return the relative similarity value
     * @throws quicktime.std.StdQTException
     * @throws quicktime.qd.QDException
     * @see "QuickTime::GetSimilarity"
     */
    public static float getSimilarity(QDGraphics src, QDRect srcRect, ImageDescription desc, EncodedImage data) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        if (src == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            if ((data instanceof ByteEncodedImage)) {
                i = GetSimilarity(QTObject.ID(src.getPixMap()), srcRect.getRect(), QTObject.ID(desc), ((ByteEncodedImage) data).getBytes(), arrayOfInt);
            } else if ((data instanceof IntEncodedImage)) {
                i = GetSimilarity(QTObject.ID(src.getPixMap()), srcRect.getBytes(), QTObject.ID(desc), ((IntEncodedImage) data).getInts(), arrayOfInt);
            } else if ((data instanceof RawEncodedImage)) {
                i = GetSimilarity(QTObject.ID(src.getPixMap()), srcRect.getBytes(), QTObject.ID(desc), QTObject.ID((RawEncodedImage) data), arrayOfInt);
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static short CompressImage(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return ImageCompressionManager.CompressImage(paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static short CompressImage(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte2) {
        return ImageCompressionManager.CompressImage(paramInt1, paramArrayOfByte1, paramInt2, paramInt3, paramInt4, paramArrayOfByte2);
    }

    private static short CompressImage(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return ImageCompressionManager.CompressImage(paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static short FCompressImage(int paramInt1, byte[] paramArrayOfByte, short paramShort1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short paramShort2, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10) {
        return ImageCompressionManager.FCompressImage(paramInt1, paramArrayOfByte, paramShort1, paramInt2, paramInt3, paramInt4, paramInt5, paramShort2, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10);
    }

    private static short FCompressImage(int paramInt1, byte[] paramArrayOfByte1, short paramShort1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short paramShort2, int paramInt6, int paramInt7, int paramInt8, int paramInt9, byte[] paramArrayOfByte2) {
        return ImageCompressionManager.FCompressImage(paramInt1, paramArrayOfByte1, paramShort1, paramInt2, paramInt3, paramInt4, paramInt5, paramShort2, paramInt6, paramInt7, paramInt8, paramInt9, paramArrayOfByte2);
    }

    private static short FCompressImage(int paramInt1, byte[] paramArrayOfByte, short paramShort1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short paramShort2, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int[] paramArrayOfInt) {
        return ImageCompressionManager.FCompressImage(paramInt1, paramArrayOfByte, paramShort1, paramInt2, paramInt3, paramInt4, paramInt5, paramShort2, paramInt6, paramInt7, paramInt8, paramInt9, paramArrayOfInt);
    }

    private static short ConvertImage(int paramInt1, byte[] paramArrayOfByte1, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte2) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramArrayOfByte1, paramShort, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramArrayOfByte2);
    }

    private static short ConvertImage(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramArrayOfByte, paramShort, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramArrayOfInt);
    }

    private static short ConvertImage(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramArrayOfByte, paramShort, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    }

    private static short ConvertImage(int paramInt1, int[] paramArrayOfInt, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramArrayOfInt, paramShort, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramArrayOfByte);
    }

    private static short ConvertImage(int paramInt1, int[] paramArrayOfInt1, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt2) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramArrayOfInt1, paramShort, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramArrayOfInt2);
    }

    private static short ConvertImage(int paramInt1, int[] paramArrayOfInt, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramArrayOfInt, paramShort, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    }

    private static short ConvertImage(int paramInt1, int paramInt2, short paramShort, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, byte[] paramArrayOfByte) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramInt2, paramShort, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramArrayOfByte);
    }

    private static short ConvertImage(int paramInt1, int paramInt2, short paramShort, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int[] paramArrayOfInt) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramInt2, paramShort, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramArrayOfInt);
    }

    private static short ConvertImage(int paramInt1, int paramInt2, short paramShort, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9) {
        return ImageCompressionManager.ConvertImage(paramInt1, paramInt2, paramShort, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9);
    }

    private static short TrimImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, int paramInt5, int paramInt6, byte[] paramArrayOfByte2, int paramInt7) {
        return ImageCompressionManager.TrimImage(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte1, paramInt5, paramInt6, paramArrayOfByte2, paramInt7);
    }

    private static short TrimImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6, byte[] paramArrayOfByte, int paramInt7) {
        return ImageCompressionManager.TrimImage(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6, paramArrayOfByte, paramInt7);
    }

    private static short TrimImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int paramInt8) {
        return ImageCompressionManager.TrimImage(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramArrayOfByte, paramInt8);
    }

    private static short TrimImage(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, int paramInt4, int paramInt5, byte[] paramArrayOfByte2, int paramInt6) {
        return ImageCompressionManager.TrimImage(paramInt1, paramArrayOfInt, paramInt2, paramInt3, paramArrayOfByte1, paramInt4, paramInt5, paramArrayOfByte2, paramInt6);
    }

    private static short TrimImage(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6) {
        return ImageCompressionManager.TrimImage(paramInt1, paramArrayOfInt1, paramInt2, paramInt3, paramArrayOfInt2, paramInt4, paramInt5, paramArrayOfByte, paramInt6);
    }

    private static short TrimImage(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte, int paramInt7) {
        return ImageCompressionManager.TrimImage(paramInt1, paramArrayOfInt, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfByte, paramInt7);
    }

    private static short TrimImage(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, int paramInt4, int paramInt5, byte[] paramArrayOfByte3, int paramInt6) {
        return ImageCompressionManager.TrimImage(paramInt1, paramArrayOfByte1, paramInt2, paramInt3, paramArrayOfByte2, paramInt4, paramInt5, paramArrayOfByte3, paramInt6);
    }

    private static short TrimImage(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4, int paramInt5, byte[] paramArrayOfByte2, int paramInt6) {
        return ImageCompressionManager.TrimImage(paramInt1, paramArrayOfByte1, paramInt2, paramInt3, paramArrayOfInt, paramInt4, paramInt5, paramArrayOfByte2, paramInt6);
    }

    private static short TrimImage(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte2, int paramInt7) {
        return ImageCompressionManager.TrimImage(paramInt1, paramArrayOfByte1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfByte2, paramInt7);
    }

    private static short DecompressImage(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, short paramShort, int paramInt3) {
        return ImageCompressionManager.DecompressImage(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramArrayOfByte3, paramShort, paramInt3);
    }

    private static short DecompressImage(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort, int paramInt3) {
        return ImageCompressionManager.DecompressImage(paramArrayOfInt, paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramShort, paramInt3);
    }

    private static short DecompressImage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort, int paramInt4) {
        return ImageCompressionManager.DecompressImage(paramInt1, paramInt2, paramInt3, paramArrayOfByte1, paramArrayOfByte2, paramShort, paramInt4);
    }

    private static short FDecompressImage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort, int paramInt4, int paramInt5, byte[] paramArrayOfByte3, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10) {
        return ImageCompressionManager.FDecompressImage(paramInt1, paramInt2, paramInt3, paramArrayOfByte1, paramArrayOfByte2, paramShort, paramInt4, paramInt5, paramArrayOfByte3, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10);
    }

    private static short GetMaxCompressionSize(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetMaxCompressionSize(paramInt1, paramArrayOfByte, paramShort, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static short GetCompressedImageSize(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetCompressedImageSize(paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static short GetCompressedImageSize(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2) {
        return ImageCompressionManager.GetCompressedImageSize(paramInt1, paramArrayOfInt1, paramInt2, paramInt3, paramArrayOfInt2);
    }

    private static short GetCompressedImageSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetCompressedImageSize(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static short GetCompressionTime(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetCompressionTime(paramInt1, paramArrayOfByte, paramShort, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfInt);
    }

    private static short GetSimilarity(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetSimilarity(paramInt1, paramArrayOfByte1, paramInt2, paramArrayOfByte2, paramArrayOfInt);
    }

    private static short GetSimilarity(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return ImageCompressionManager.GetSimilarity(paramInt1, paramArrayOfByte, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short GetSimilarity(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetSimilarity(paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramArrayOfInt);
    }
}
