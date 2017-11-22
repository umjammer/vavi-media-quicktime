
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
     */
    public ImageDataSequence() {
        this(null);
    }

    /**
     * @deprecated
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
     */
    public ImageDescription getDescription() {
        return this.imageDescription;
    }

    /**
     * @deprecated
     */
    public void setDescription(ImageDescription paramImageDescription) {
        this.imageDescription = paramImageDescription;
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage() {
        return getNth(1);
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage(int paramInt) {
        return getNth(paramInt);
    }

    /**
     * @deprecated
     */
    public Enumeration members() {
        return this.imageDataVector.elements();
    }

    /**
     * @deprecated
     */
    public ImageSequencer sequence() {
        return new ImageSequencer(this);
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject) {
        if (!isAppropriate(paramObject))
            return false;
        append((EncodedImage) paramObject);
        return true;
    }

    /**
     * @deprecated
     */
    public void removeMember(Object paramObject) {
        this.imageDataVector.removeElement(paramObject);
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof EncodedImage;
    }

    /**
     * @deprecated
     */
    public boolean hasMember(Object paramObject) {
        return this.imageDataVector.contains(paramObject);
    }

    /**
     * @deprecated
     */
    public void insert(EncodedImage paramEncodedImage, int paramInt) {
        this.imageDataVector.insertElementAt(paramEncodedImage, paramInt - 1);
    }

    /**
     * @deprecated
     */
    public void prepend(EncodedImage paramEncodedImage) {
        insert(paramEncodedImage, 1);
    }

    /**
     * @deprecated
     */
    public void append(EncodedImage paramEncodedImage) {
        insert(paramEncodedImage, size() + 1);
    }

    /**
     * @deprecated
     */
    public void remove(int paramInt) {
        this.imageDataVector.removeElementAt(paramInt - 1);
    }

    /**
     * @deprecated
     */
    public void removeFirst() {
        remove(1);
    }

    /**
     * @deprecated
     */
    public void removeLast() {
        remove(size() - 1);
    }

    /**
     * @deprecated
     */
    public void removeAll() {
        this.imageDataVector.removeAllElements();
    }

    /**
     * @deprecated
     */
    public int size() {
        return this.imageDataVector.size();
    }

    /**
     * @deprecated
     */
    public EncodedImage getNth(int paramInt) {
        return (EncodedImage) this.imageDataVector.elementAt(paramInt - 1);
    }

    /**
     * @deprecated
     */
    public boolean isEmpty() {
        return this.imageDataVector.isEmpty();
    }

    /**
     * @deprecated
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
            localVideoMedia.addSample(localQTHandle,
                                      0,
                                      localQTHandle.getSize(),
                                      (int) (600.0F / paramFloat),
                                      this.imageDescription,
                                      1,
                                      0);

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

    public String toString() {
        return getClass().getName() + "[size=" + size() + ",description=" + getDescription() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageDataSequence.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
