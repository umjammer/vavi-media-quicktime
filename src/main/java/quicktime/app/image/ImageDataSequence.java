package quicktime.app.image;

import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Collection;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.VideoMedia;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.QTHandle;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;

/**
 * Deprecated.
 * A Container class for a collection of objects that have a single
 * image description and a sequence of picture data. The actual members
 * of the ImageDataSequence are EncodedImage objects.
 * This collection is a 1 based collection and is ordered. Typically to
 * iterate over the members the ImageSequencer class is used.
 * @see {@link quicktime.app.image.ImageSequencer}
 */
public class ImageDataSequence implements ImageSpec, Collection {

    Vector imageDataVector;

    private ImageDescription imageDescription;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates an ImageDataSequence with no settings
     */
    public ImageDataSequence() {
        this(null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates an ImageDataSequence with an initial description
     * @param desc the description that will describe the subsequently added image data
     */
    public ImageDataSequence(ImageDescription paramImageDescription) {
        this(paramImageDescription, new Vector());
    }

    ImageDataSequence(ImageDescription paramImageDescription, Vector paramVector) {
        setDescription(paramImageDescription);
        this.imageDataVector = paramVector;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the ImageDescription object that describes the image data
     * in this sequence.
     * @return the description
     */
    public ImageDescription getDescription() {
        return this.imageDescription;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the ImageDescription object that will describe how the image
     * data objects are to be interpreted by QT.
     * @param desc the new description
     */
    public void setDescription(ImageDescription paramImageDescription) {
        this.imageDescription = paramImageDescription;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the actual image data of the first item in the ImageDataSequence.
     * @return the image data
     */
    public EncodedImage getImage() {
        return getNth(1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the actual image data at the specified index.
     * The index is a one based index.
     * An Exception is throw if there was some problem retrieving the data.
     * @param index is the index to the data frame
     * @return the image data
     */
    public EncodedImage getImage(int paramInt) {
        return getNth(paramInt);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns an object that will iterate over the elements in the collection.
     * @return The iterator.
     */
    public Enumeration members() {
        return this.imageDataVector.elements();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return a Sequencer to iterate over the members of this sequence.
     * @return a Sequencer
     */
    public ImageSequencer sequence() {
        return new ImageSequencer(this);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Appends the member to the ImageDataSequence.
     * Adds an object to the collection. Returns true if the object is
     * appropriate member of the collection (and thus added) and false if not.
     * @param member the object that should be added as a member to the collection
     * @return a boolean.
     */
    public boolean addMember(Object paramObject) {
        if (!isAppropriate(paramObject))
            return false;
        append((EncodedImage) paramObject);
        return true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the object is a member of the collection it is removed.
     * @param member the object to remove from the collection
     */
    public void removeMember(Object paramObject) {
        this.imageDataVector.removeElement(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the provided object is an appropriate candidate for
     * membership in a collection. The provided QTJava collections use this method
     * to ensure a minimal requirement of Class or Interface instanceof tests to
     * ensure the type of object that can be added to a collection. Some collections
     * can have a protocol that allows an application to further refine and tighten
     * the type of object that is allowed within an individual collection.
     *
     * The member of an ImageDataSequence is an EncodedImage object.
     * @param object the object to test
     * @return true if the supplied object is appropriate for this collection, or false if not
     * @see {@link quicktime.app.spaces.DynamicCollection},
     * Protocol
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof EncodedImage;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the provided object is a member of the collection.
     * @param object the object to test for membership
     * @return a boolean
     */
    public boolean hasMember(Object paramObject) {
        return this.imageDataVector.contains(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Add the image data object at the specified index.
     * If the index is beyond the end of the collection
     * the image data is appended to the collection.
     * @param image The image data.
     * @param index The position in the sequence to add.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public void insert(EncodedImage paramEncodedImage, int paramInt) {
        this.imageDataVector.insertElementAt(paramEncodedImage, paramInt - 1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Add the image description object as the first item in the collection
     * @param image The image data to add.
     */
    public void prepend(EncodedImage paramEncodedImage) {
        insert(paramEncodedImage, 1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Add the image description object to the end of the collection
     * @param image The image data to add.
     */
    public void append(EncodedImage paramEncodedImage) {
        insert(paramEncodedImage, size() + 1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Remove the object at the specified index.
     * @param i The position in the sequence to delete.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public void remove(int paramInt) {
        this.imageDataVector.removeElementAt(paramInt - 1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Remove the object at the specified index.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public void removeFirst() {
        remove(1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Remove the object at the specified index.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public void removeLast() {
        remove(size() - 1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Remove all objects.
     */
    public void removeAll() {
        this.imageDataVector.removeAllElements();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Determines the number of items.
     * @return The size.
     */
    public int size() {
        return this.imageDataVector.size();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Gets the image data object at given position.
     * @param index Specifies the position in sequence.
     * @return the image data.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public EncodedImage getNth(int paramInt) {
        return (EncodedImage) this.imageDataVector.elementAt(paramInt - 1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the collection contains no members, or false if
     * it has one or more members.
     * @return a boolean
     */
    public boolean isEmpty() {
        return this.imageDataVector.isEmpty();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will insert a video track into the supplied movie with all of the image data
     * that is contained in this data sequence at the specified frames per second.
     * @param theMovie the movie to add the video track to
     * @param framesPerSecond how many frames per second the image data should be inserted at
     * @param {@link quicktime.QTException}
     */
    public void addToMovie(Movie paramMovie, float paramFloat) throws QTException {
        int i = 0;
        int j = 600;
        Track localTrack = paramMovie.addTrack(this.imageDescription.getWidth(), this.imageDescription.getHeight(), i);
        VideoMedia localVideoMedia = new VideoMedia(localTrack, j);
        localVideoMedia.beginEdits();
        for (int k = 0; k < size(); k++) {
            EncodedImage localEncodedImage = getNth(k + 1);
            QTHandle localQTHandle = null;
            if ((localEncodedImage instanceof RawEncodedImage)) {
                localQTHandle = new QTHandle((RawEncodedImage) localEncodedImage, 0, localEncodedImage.getSize());
            } else {
                if ((localEncodedImage instanceof IntEncodedImage)) {
                    localQTHandle = new QTHandle(localEncodedImage.getSize(), false);
                    int[] localObject = ((IntEncodedImage) localEncodedImage).getInts();
                    localQTHandle.copyFromArray(0, localObject, 0, localObject.length);
                } else {
                    localQTHandle = new QTHandle(localEncodedImage.getSize(), false);
                    byte[] localObject = ((ByteEncodedImage) localEncodedImage).getBytes();
                    localQTHandle.copyFromArray(0, localObject, 0, localObject.length);
                }
            }
            localVideoMedia.addSample(localQTHandle, 0, localQTHandle.getSize(), (int) (600.0F / paramFloat), this.imageDescription, 1, 0);
            if (k % 5 == 0)
                QTUtils.reclaimMemory();
        }
        localVideoMedia.endEdits();
        int k = 0;
        int m = 0;
        int n = 1;
        localTrack.insertMedia(k, m, localVideoMedia.getDuration(), n);
        QTUtils.reclaimMemory();
    }

    /**
     * Deprecated.
     * Return a string representation of this class* @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[size=" + size() + ",description=" + getDescription() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageDataSequence.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
