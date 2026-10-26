package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
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
 *
 * Instance of this class can be used to associate an ImageDescription
 * with the encoded Image data that the description describes.
 */
public class QTImage implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QTImage.class);
                    }
                });
            }
        }.establish();
    }

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    private ImageDescription desc;

    private EncodedImage data;

    /**
     * Associate an image description with the image data that is described
     * by the description.
     * @param id the image description
     * @param im the image data
     */
    public QTImage(ImageDescription paramImageDescription, EncodedImage paramEncodedImage) {
        this.data = paramEncodedImage;
        this.desc = paramImageDescription;
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
     * QuickTime::CompressImage
     * @param src the QDGraphics that contains the PixMap for compressing. This may NOT be null.
     * @param srcRect a Rect defining the portion of the image to compress
     * @param quality the desired compressed image quality
     * @param cType a compressor type
     * @param data a encoded image object to receive the compressed image data.
     *                 It is your program's responsibility to make sure that this
     *                 object can receive at least as much data as indicated by
     *                 the getMaxCompressionSize method.
     * @return an ImageDescription object. Your application should store this
     *          image description with the compressed image data.
     * @return {@link quicktime.QTException}
     */
    public static final ImageDescription compress(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt1, int paramInt2, EncodedImage paramEncodedImage) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = CompressImage(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getRect(), paramInt1, paramInt2, QTObject.ID(localImageDescription), ((ByteEncodedImage) paramEncodedImage).getBytes());
            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = CompressImage(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getRect(), paramInt1, paramInt2, QTObject.ID(localImageDescription), ((IntEncodedImage) paramEncodedImage).getInts());
            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = CompressImage(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getRect(), paramInt1, paramInt2, QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage));
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
     * QuickTime::FCompressImage
     * @param src a QDGraphics that contains the PixMap for compressing. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param colorDepth the depth at which the image is likely to be viewed
     * @param quality the desired compressed image quality
     * @param cType a compressor type
     * @param c a compressor or codec identifier
     * @param clut a custom color lookup table
     * @param flags flags providing further control information
     * @param data a EncodedImage object to receive the compressed image data.
     *                    It is your program's responsibility to make sure that this
     *                    object can receive at least as much data as indicated by
     *                    the getMaxCompressionSize method.
     * @return an ImageDescription object. Your application should store this
     *          image description with the compressed image data.
     * @return {@link quicktime.QTException}
     */
    public static final ImageDescription fCompress(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt1, int paramInt2, int paramInt3, CodecComponent paramCodecComponent, ColorTable paramColorTable, int paramInt4, EncodedImage paramEncodedImage) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = FCompressImage(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getRect(), (short) paramInt1, paramInt2, paramInt3, QTObject.ID(paramCodecComponent), QTObject.ID(paramColorTable), (short) paramInt4, 0, 0, 0, QTObject.ID(localImageDescription), ((ByteEncodedImage) paramEncodedImage).getBytes());
            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = FCompressImage(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getRect(), (short) paramInt1, paramInt2, paramInt3, QTObject.ID(paramCodecComponent), QTObject.ID(paramColorTable), (short) paramInt4, 0, 0, 0, QTObject.ID(localImageDescription), ((IntEncodedImage) paramEncodedImage).getInts());
            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = FCompressImage(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getRect(), (short) paramInt1, paramInt2, paramInt3, QTObject.ID(paramCodecComponent), QTObject.ID(paramColorTable), (short) paramInt4, 0, 0, 0, QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage));
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return localImageDescription;
    }

    /**
     * Converts the format of a compressed image.
     * QuickTime::ConvertImage
     * @param srcDD the image description structure that describes the compressed image
     * @param srcData the encoded image data to convert
     * @param colorDepth the depth at which the recompressed image is likely to be viewed
     * @param clut a custom color look up table
     * @param accuracy the accuracy for the decompressed image
     * @param quality the desired compressed image quality
     * @param cType the compressor type
     * @param codec a CodecComponent
     * @return an ImageDescription object defining the converted image data. Your
     *          application should store this image description with the converted
     *          image data.
     * @return {@link quicktime.QTException}
     */
    public static final ImageDescription convert(ImageDescription paramImageDescription, EncodedImage paramEncodedImage1, int paramInt1, ColorTable paramColorTable, int paramInt2, int paramInt3, int paramInt4, CodecComponent paramCodecComponent, EncodedImage paramEncodedImage2) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        int i = -50;
        if ((paramEncodedImage1 instanceof ByteEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage1).getBytes(), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), ((ByteEncodedImage) paramEncodedImage2).getBytes());
            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage1).getBytes(), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), ((IntEncodedImage) paramEncodedImage2).getInts());
            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage1).getBytes(), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage2));
            }
        } else if ((paramEncodedImage1 instanceof IntEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage1).getInts(), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), ((ByteEncodedImage) paramEncodedImage2).getBytes());
            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage1).getInts(), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), ((IntEncodedImage) paramEncodedImage2).getInts());
            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage1).getInts(), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage2));
            }
        } else if ((paramEncodedImage1 instanceof RawEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage1), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), ((ByteEncodedImage) paramEncodedImage2).getBytes());
            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage1), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), ((IntEncodedImage) paramEncodedImage2).getInts());
            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage1), (short) paramInt1, QTObject.ID(paramColorTable), paramInt2, paramInt3, paramInt4, QTObject.ID(paramCodecComponent), QTObject.ID(localImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage2));
            }
        }
        StdQTException.checkError(i);
        return localImageDescription;
    }

    /**
     * Ajusts a compressed image to the boundaries defined by a QDRect specified
     * by your application.
     * QuickTime::TrimImage
     * @param desc an ImageDescription object that describes the compressed image.
     *                  On return from this method, the compressor updates this image
     *                  description to refer to the resized image.
     * @param inData the encoded image data
     * @param outData a encoded image object to receive the trimmed image. Your application
     *                  should create this object at least as large as the source image.
     * @param trimRect defines the desired dimensions of the trimmed image
     * @param {@link quicktime.std.StdQTException}
     */
    public static final void trim(ImageDescription paramImageDescription, EncodedImage paramEncodedImage1, EncodedImage paramEncodedImage2, QDRect paramQDRect) throws StdQTException {
        int i = -50;
        if ((paramEncodedImage1 instanceof ByteEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage1).getBytes(), 0, 0, ((ByteEncodedImage) paramEncodedImage2).getBytes(), 0, 0, paramQDRect.getRect(), 0);
            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage1).getBytes(), 0, 0, ((IntEncodedImage) paramEncodedImage2).getInts(), 0, 0, paramQDRect.getRect(), 0);
            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage1).getBytes(), 0, 0, QTObject.ID((RawEncodedImage) paramEncodedImage2), 0, 0, paramQDRect.getRect(), 0);
            }
        } else if ((paramEncodedImage1 instanceof IntEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage1).getInts(), 0, 0, ((ByteEncodedImage) paramEncodedImage2).getBytes(), 0, 0, paramQDRect.getBytes(), 0);
            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage1).getInts(), 0, 0, ((IntEncodedImage) paramEncodedImage2).getInts(), 0, 0, paramQDRect.getBytes(), 0);
            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage1).getInts(), 0, 0, QTObject.ID((RawEncodedImage) paramEncodedImage2), 0, 0, paramQDRect.getBytes(), 0);
            }
        } else if ((paramEncodedImage1 instanceof RawEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage1), 0, 0, ((ByteEncodedImage) paramEncodedImage2).getBytes(), 0, 0, paramQDRect.getBytes(), 0);
            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage1), 0, 0, ((IntEncodedImage) paramEncodedImage2).getInts(), 0, 0, paramQDRect.getBytes(), 0);
            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage1), 0, 0, QTObject.ID((RawEncodedImage) paramEncodedImage2), 0, 0, paramQDRect.getBytes(), 0);
            }
        }
        StdQTException.checkError(i);
    }

    /**
     * Decompresses a single frame image into a pixel map structure.
     * QuickTime::DecompressImage
     * @param data a EncodedImage object that contains the compressed image data
     * @param desc an ImageDescription object that defines the compressed image
     * @param dst a QDGraphics that contains the PixMap where the decompressed image is to be displayed. This may NOT be null.
     * @param dstRect a QDRect into which the decompressed image is to be loaded
     * @param mode the transfer mode for the operation
     * @param {@link quicktime.std.StdQTException}
     * @param {@link quicktime.qd.QDException}
     */
    public static final void decompress(EncodedImage paramEncodedImage, ImageDescription paramImageDescription, QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt) throws StdQTException, QDException {
        decompress(paramEncodedImage, paramImageDescription, paramQDGraphics, null, paramQDRect, paramInt, null);
    }

    /**
     * Decompresses a single frame image into a pixel map structure.
     * QuickTime::DecompressImage
     * @param data a EncodedImage object that contains the compressed image data
     * @param desc an ImageDescription object that defines the compressed image
     * @param dst a QDGraphics that contains the PixMap where the decompressed image is to be displayed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to decompress
     * @param dstRect a QDRect into which the decompressed image is to be loaded
     * @param mode the transfer mode for the operation
     * @param mask a clipping region in the destination coordinate system
     * @param {@link quicktime.std.StdQTException}
     * @param {@link quicktime.qd.QDException}
     */
    public static final void decompress(EncodedImage paramEncodedImage, ImageDescription paramImageDescription, QDGraphics paramQDGraphics, QDRect paramQDRect1, QDRect paramQDRect2, int paramInt, Region paramRegion) throws StdQTException, QDException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = DecompressImage(((ByteEncodedImage) paramEncodedImage).getBytes(), QTObject.ID(paramImageDescription), QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect1 == null ? null : paramQDRect1.getRect(), paramQDRect2.getRect(), (short) paramInt, QTObject.ID(paramRegion));
            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = DecompressImage(((IntEncodedImage) paramEncodedImage).getInts(), QTObject.ID(paramImageDescription), QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect1 == null ? null : paramQDRect1.getRect(), paramQDRect2.getRect(), (short) paramInt, QTObject.ID(paramRegion));
            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = DecompressImage(QTObject.ID((RawEncodedImage) paramEncodedImage), QTObject.ID(paramImageDescription), QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect1 == null ? null : paramQDRect1.getRect(), paramQDRect2.getRect(), (short) paramInt, QTObject.ID(paramRegion));
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    /**
     * Decompresses a single frame image into a pixel map structure.
     * QuickTime::FDecompressImage
     * @param data a EncodedImage object that contains the compressed image data
     * @param desc an ImageDescription object that defines the compressed image
     * @param dst a QDGraphics that contains the PixMap where the decompressed image is to be displayed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to decompress
     * @param matrix a Matrix specifies how to transform the image during decompression
     * @param mode the transfer mode for the operation
     * @param mask a Region in the destination coordinate system
     * @param matte a PixMap that contains a blend matte
     * @param matteRect a QDRect defining a portion of the blend matte to apply
     * @param accuracy the accuracy desired in the decompressed image
     * @param codec a codec component
     * @param {@link quicktime.std.StdQTException}
     * @param {@link quicktime.qd.QDException}
     */
    public static final void fDecompress(EncodedImage paramEncodedImage, ImageDescription paramImageDescription, QDGraphics paramQDGraphics, QDRect paramQDRect1, Matrix paramMatrix, int paramInt1, Region paramRegion, PixMap paramPixMap, QDRect paramQDRect2, int paramInt2, CodecComponent paramCodecComponent) throws StdQTException, QDException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = FDecompressImage(QTObject.ID((RawEncodedImage) paramEncodedImage), QTObject.ID(paramImageDescription), QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect1 == null ? null : paramQDRect1.getRect(), paramMatrix == null ? null : paramMatrix.getBytes(), (short) paramInt1, QTObject.ID(paramRegion), QTObject.ID(paramPixMap), paramQDRect2 == null ? null : paramQDRect2.getRect(), paramInt2, QTObject.ID(paramCodecComponent), 0, 0, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    /**
     * Determines the maximum size an image will be after compression.  The size returned is in the
     * number of bytes that the encoded image will require.
     * see QuickTime::GetMaxCompressionSize
     * @param src is a QDGraphics that contains the PixMap to be compressed. This may NOT be null.
     * @param srcRect points to the QDRect defining the portion of the
     *  source image that is to be compressed
     * @param colorDepth the depth at which the image is to be compressed
     * @param quality the desired compressed image quality
     * @param cType a compressor type
     * @param c a compressor component
     * @return size of the compressed pixmap
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.qd.QDException}
     */
    public static final int getMaxCompressionSize(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt1, int paramInt2, int paramInt3, CodecComponent paramCodecComponent) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = GetMaxCompressionSize(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect == null ? null : paramQDRect.getRect(), (short) paramInt1, paramInt2, paramInt3, QTObject.ID(paramCodecComponent), arrayOfInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines the size in bytes of a compressed image.
     * QuickTime::GetCompressedImageSize
     * @param desc an ImageDescription object that defines the compressed image
     * @param data a EncodedImage object that contains the compressed image data
     * @return the size of the compressed image in bytes
     * @return {@link quicktime.std.StdQTException}
     */
    public static final int getCompressedSize(ImageDescription paramImageDescription, EncodedImage paramEncodedImage) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = -50;
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage).getBytes(), 0, 0, arrayOfInt);
        } else if ((paramEncodedImage instanceof IntEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage).getInts(), 0, 0, arrayOfInt);
        } else if ((paramEncodedImage instanceof RawEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage), 0, 0, arrayOfInt);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines the estimated amount of time required to compress a given image.
     * see QuickTime::GetCompressionTime
     * @param src a QDGraphics that contains a PixMap object that will be compressed. This may be null.
     * @param srcRect a QDRect defining the portion of the source image that is
     *                        to be compressed
     * @param colorDepth the depth at which the image is to be compressed
     * @param cType a compressor type
     * @param c the compressor component
     * @param spatialQuality the desired compressed image spatial quality
     * @param temporalQuality the desired compressed image temporal quality
     * @return the compression time in milliseconds
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.qd.QDException}
     */
    public static final int getCompressionTime(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt1, int paramInt2, CodecComponent paramCodecComponent, int paramInt3, int paramInt4) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        int i = 0;
        int j = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            if (paramQDGraphics != null) {
                SetGWorld(QTObject.ID(paramQDGraphics), 0);
                i = QTObject.ID(paramQDGraphics.getPixMap());
            }
            j = GetCompressionTime(i, paramQDRect == null ? null : paramQDRect.getRect(), (short) paramInt1, paramInt2, QTObject.ID(paramCodecComponent), paramInt3, paramInt4, arrayOfInt);
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
     * QuickTime::GetSimilarity
     * @param src a QDGraphics that contains a PixMap object containing a noncompressed image.
     * @param srcRect a QDRect defining the portion of the source image to compare
     *                 to the compressed image
     * @param desc an ImageDescription object that defines the compressed image
     * @param data an EncodedImage object that contains the compressed image data
     * @return the relative similarity value
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.qd.QDException}
     */
    public static final float getSimilarity(QDGraphics paramQDGraphics, QDRect paramQDRect, ImageDescription paramImageDescription, EncodedImage paramEncodedImage) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = GetSimilarity(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getRect(), QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage).getBytes(), arrayOfInt);
            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = GetSimilarity(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getBytes(), QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage).getInts(), arrayOfInt);
            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = GetSimilarity(QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect.getBytes(), QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage), arrayOfInt);
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
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /QTImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
