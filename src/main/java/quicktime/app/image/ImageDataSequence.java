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
 * @deprecated
 * A Container class for a collection of objects that have a single
 * image description and a sequence of picture data. The actual members
 * of the ImageDataSequence are EncodedImage objects.
 * This collection is a 1 based collection and is ordered. Typically to
 * iterate over the members the ImageSequencer class is used.
 * @see quicktime.app.image.ImageSequencer
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
     * @deprecated
     * @since QTJava 6.1
     * Creates an ImageDataSequence with no settings
     */
    public ImageDataSequence() {
        this(null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
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
     * @deprecated
     * @since QTJava 6.1
     * Returns the ImageDescription object that describes the image data
     * in this sequence.
     * @return the description
     */
    @Override
    public ImageDescription getDescription() {
        return this.imageDescription;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the ImageDescription object that will describe how the image
     * data objects are to be interpreted by QT.
     * @param desc the new description
     */
    public void setDescription(ImageDescription paramImageDescription) {
        this.imageDescription = paramImageDescription;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the actual image data of the first item in the ImageDataSequence.
     * @return the image data
     */
    @Override
    public EncodedImage getImage() {
        return getNth(1);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
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
     * @deprecated
     * @since QTJava 6.1
     * Returns an object that will iterate over the elements in the collection.
     * @return The iterator.
     */
    @Override
    public Enumeration members() {
        return this.imageDataVector.elements();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Return a Sequencer to iterate over the members of this sequence.
     * @return a Sequencer
     */
    public ImageSequencer sequence() {
        return new ImageSequencer(this);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Appends the member to the ImageDataSequence.
     * Adds an object to the collection. Returns true if the object is
     * appropriate member of the collection (and thus added) and false if not.
     * @param member the object that should be added as a member to the collection
     * @return a boolean.
     */
    @Override
    public boolean addMember(Object member) {
        if (!isAppropriate(member))
            return false;
        append((EncodedImage) member);
        return true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If the object is a member of the collection it is removed.
     * @param member the object to remove from the collection
     */
    @Override
    public void removeMember(Object member) {
        this.imageDataVector.removeElement(member);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
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
     * @see quicktime.app.spaces.DynamicCollection,
     * Protocol
     */
    @Override
    public boolean isAppropriate(Object object) {
        return object instanceof EncodedImage;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the provided object is a member of the collection.
     * @param object the object to test for membership
     * @return a boolean
     */
    @Override
    public boolean hasMember(Object member) {
        return this.imageDataVector.contains(member);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
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
     * @deprecated
     * @since QTJava 6.1
     * Add the image description object as the first item in the collection
     * @param image The image data to add.
     */
    public void prepend(EncodedImage paramEncodedImage) {
        insert(paramEncodedImage, 1);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Add the image description object to the end of the collection
     * @param image The image data to add.
     */
    public void append(EncodedImage paramEncodedImage) {
        insert(paramEncodedImage, size() + 1);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Remove the object at the specified index.
     * @param i The position in the sequence to delete.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public void remove(int paramInt) {
        this.imageDataVector.removeElementAt(paramInt - 1);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Remove the object at the specified index.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public void removeFirst() {
        remove(1);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Remove the object at the specified index.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public void removeLast() {
        remove(size() - 1);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Remove all objects.
     */
    public void removeAll() {
        this.imageDataVector.removeAllElements();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Determines the number of items.
     * @return The size.
     */
    @Override
    public int size() {
        return this.imageDataVector.size();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Gets the image data object at given position.
     * @param index Specifies the position in sequence.
     * @return the image data.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     */
    public EncodedImage getNth(int paramInt) {
        return (EncodedImage) this.imageDataVector.elementAt(paramInt - 1);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the collection contains no members, or false if
     * it has one or more members.
     * @return a boolean
     */
    @Override
    public boolean isEmpty() {
        return this.imageDataVector.isEmpty();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will insert a video track into the supplied movie with all of the image data
     * that is contained in this data sequence at the specified frames per second.
     * @param theMovie the movie to add the video track to
     * @param framesPerSecond how many frames per second the image data should be inserted at
     * @throws quicktime.QTException
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
     * @deprecated
     * Return a string representation of this class* @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[size=" + size() + ",description=" + getDescription() + "]";
    }
}
