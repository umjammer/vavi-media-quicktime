package quicktime.util;

/**
 * Objects that implement the encoded image interface implement a particular format
 * for storing encoded image data. This allows images to be kept in a format that is
 * appropriate for a particular usage but simplifies the APIs that use encoded image data.
 * This interface can ony be implemented by classes that are a part of the QTJava library;
 * applications are not able to extend or apply usage of this interface, but can use the
 * image encoding options that are provided.
 *
 * To use objects that implement this interface you use the instanceof operator. The library
 * ships with three types of objects that implement this interface.
 *
 * RawEncodedImage - a select set of subclasses that can contain encoded images.
 * ByteEncodedImage - the image data is held in a java byte array.
 * IntEncodedImage - the image data is held in a java int array.
 *
 * The EncodedImage class contains no information about the encoding format of the image.
 * The particular encoding format of the image data is described by the encoded image's
 * accompanying ImageDescription object, which contains all of the metadata that is required
 * to completely describe the format and characteristics of the encoded image data.
 *
 * If you are looking at pixel values directly you should bear a couple of things in mind:
 * you may need to deal with the endian issue - they will be in a native endian format.
 * if the description describes the image data format to be something other than raw pixels
 * then the get methods are probably not very useful as you will not be looking at
 * pixel values.
 *
 * @see quicktime.std.image.ImageDescription,
 * {@link quicktime.util.ByteEncodedImage},
 * {@link quicktime.util.IntEncodedImage},
 * {@link quicktime.util.RawEncodedImage}
 */
public interface EncodedImage {

    /**
     * This value is returned by an EncodedImage class from the getRowBytes() method
     * if the encoded image does not know how many bytes per row or the
     * data does not contain any extra byte data beyond the image encoding
     */
    int kRowBytesUnknown = -1;

    /**
     * Returns the size in bytes of the EncodedImage data.
     * The offset is specified in bytes into the encoded image object.
     */
    int getSize();

    /**
     * Returns the byte at the specified offset.
     * The offset is specified in bytes into the encoded image object.
     */
    byte getByte(int offset);

    /**
     * Returns the short at the specified offset.
     * The offset is specified in bytes into the encoded image object.
     */
    short getShort(int offset);

    /**
     * Returns the int at the specified offset.
     * The offset is specified in bytes into the encoded image object.
     */
    int getInt(int offset);

    /**
     * Returns either kRowBytesUnknown or the number of bytes per row
     * that the encoded image data is comprised of. This number will
     * at least be as big as width*pixelSize, and maybe larger.
     */
    int getRowBytes();
}
