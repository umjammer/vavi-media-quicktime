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
 * A Container class for a collection of objects that have a single
 * image description and a sequence of picture data. The actual members
 * of the ImageDataSequence are EncodedImage objects.
 * This collection is a 1 based collection and is ordered. Typically to
 * iterate over the members the ImageSequencer class is used.
 *
 * @see quicktime.app.image.ImageSequencer
 * @deprecated
 */
public class ImageDataSequence implements ImageSpec, Collection {

    Vector<EncodedImage> imageDataVector;

    private ImageDescription imageDescription;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Creates an ImageDataSequence with no settings
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public ImageDataSequence() {
        this(null);
    }

    /**
     * Creates an ImageDataSequence with an initial description
     *
     * @param desc the description that will describe the subsequently added image data
     * @since QTJava 6.1
     * @deprecated
     */
    public ImageDataSequence(ImageDescription desc) {
        this(desc, new Vector<>());
    }

    ImageDataSequence(ImageDescription paramImageDescription, Vector<EncodedImage> paramVector) {
        setDescription(paramImageDescription);
        this.imageDataVector = paramVector;
    }

    /**
     * Returns the ImageDescription object that describes the image data
     * in this sequence.
     *
     * @return the description
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public ImageDescription getDescription() {
        return this.imageDescription;
    }

    /**
     * Sets the ImageDescription object that will describe how the image
     * data objects are to be interpreted by QT.
     *
     * @param desc the new description
     * @since QTJava 6.1
     * @deprecated
     */
    public void setDescription(ImageDescription desc) {
        this.imageDescription = desc;
    }

    /**
     * This method returns the actual image data of the first item in the ImageDataSequence.
     *
     * @return the image data
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public EncodedImage getImage() {
        return getNth(1);
    }

    /**
     * This method returns the actual image data at the specified index.
     * The index is a one based index.
     * An Exception is throw if there was some problem retrieving the data.
     *
     * @param index is the index to the data frame
     * @return the image data
     * @since QTJava 6.1
     * @deprecated
     */
    public EncodedImage getImage(int index) {
        return getNth(index);
    }

    /**
     * Returns an object that will iterate over the elements in the collection.
     *
     * @return The iterator.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public Enumeration members() {
        return this.imageDataVector.elements();
    }

    /**
     * Return a Sequencer to iterate over the members of this sequence.
     *
     * @return a Sequencer
     * @since QTJava 6.1
     * @deprecated
     */
    public ImageSequencer sequence() {
        return new ImageSequencer(this);
    }

    /**
     * Appends the member to the ImageDataSequence.
     * Adds an object to the collection. Returns true if the object is
     * appropriate member of the collection (and thus added) and false if not.
     *
     * @param member the object that should be added as a member to the collection
     * @return a boolean.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean addMember(Object member) {
        if (!isAppropriate(member))
            return false;
        append((EncodedImage) member);
        return true;
    }

    /**
     * If the object is a member of the collection it is removed.
     *
     * @param member the object to remove from the collection
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void removeMember(Object member) {
        this.imageDataVector.removeElement(member);
    }

    /**
     * Returns true if the provided object is an appropriate candidate for
     * membership in a collection. The provided QTJava collections use this method
     * to ensure a minimal requirement of Class or Interface instanceof tests to
     * ensure the type of object that can be added to a collection. Some collections
     * can have a protocol that allows an application to further refine and tighten
     * the type of object that is allowed within an individual collection.
     * <p>
     * The member of an ImageDataSequence is an EncodedImage object.
     *
     * @param object the object to test
     * @return true if the supplied object is appropriate for this collection, or false if not
     * @see quicktime.app.spaces.DynamicCollection,
     * Protocol
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isAppropriate(Object object) {
        return object instanceof EncodedImage;
    }

    /**
     * Returns true if the provided object is a member of the collection.
     *
     * @param object the object to test for membership
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean hasMember(Object object) {
        return this.imageDataVector.contains(object);
    }

    /**
     * Add the image data object at the specified index.
     * If the index is beyond the end of the collection
     * the image data is appended to the collection.
     *
     * @param image The image data.
     * @param index The position in the sequence to add.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     * @since QTJava 6.1
     * @deprecated
     */
    public void insert(EncodedImage image, int index) {
        this.imageDataVector.insertElementAt(image, index - 1);
    }

    /**
     * Add the image description object as the first item in the collection
     *
     * @param image The image data to add.
     * @since QTJava 6.1
     * @deprecated
     */
    public void prepend(EncodedImage image) {
        insert(image, 1);
    }

    /**
     * Add the image description object to the end of the collection
     *
     * @param image The image data to add.
     * @since QTJava 6.1
     * @deprecated
     */
    public void append(EncodedImage image) {
        insert(image, size() + 1);
    }

    /**
     * Remove the object at the specified index.
     *
     * @param i The position in the sequence to delete.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     * @since QTJava 6.1
     * @deprecated
     */
    public void remove(int i) {
        this.imageDataVector.removeElementAt(i - 1);
    }

    /**
     * Remove the object at the specified index.
     *
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     * @since QTJava 6.1
     * @deprecated
     */
    public void removeFirst() {
        remove(1);
    }

    /**
     * Remove the object at the specified index.
     *
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     * @since QTJava 6.1
     * @deprecated
     */
    public void removeLast() {
        remove(size() - 1);
    }

    /**
     * Remove all objects.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public void removeAll() {
        this.imageDataVector.removeAllElements();
    }

    /**
     * Determines the number of items.
     *
     * @return The size.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public int size() {
        return this.imageDataVector.size();
    }

    /**
     * Gets the image data object at given position.
     *
     * @param index Specifies the position in sequence.
     * @return the image data.
     * @throws java.lang.ArrayIndexOutOfBoundsException if index value is beyond the size of the collection.
     * @since QTJava 6.1
     * @deprecated
     */
    public EncodedImage getNth(int index) {
        return this.imageDataVector.elementAt(index - 1);
    }

    /**
     * Returns true if the collection contains no members, or false if
     * it has one or more members.
     *
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isEmpty() {
        return this.imageDataVector.isEmpty();
    }

    /**
     * This will insert a video track into the supplied movie with all of the image data
     * that is contained in this data sequence at the specified frames per second.
     *
     * @param theMovie        the movie to add the video track to
     * @param framesPerSecond how many frames per second the image data should be inserted at
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    public void addToMovie(Movie theMovie, float framesPerSecond) throws QTException {
        int i = 0;
        int j = 600;
        Track localTrack = theMovie.addTrack(this.imageDescription.getWidth(), this.imageDescription.getHeight(), i);
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
            localVideoMedia.addSample(localQTHandle, 0, localQTHandle.getSize(), (int) (600.0F / framesPerSecond), this.imageDescription, 1, 0);
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
     * Return a string representation of this class* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    public String toString() {
        return getClass().getName() + "[size=" + size() + ",description=" + getDescription() + "]";
    }
}
