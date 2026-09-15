package quicktime.std.movies.media;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.TimeInfo;
import quicktime.std.movies.Track;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;

/**
 * The Media class implements the corresponding data structure of the Movie Toolbox.
 * The data references of a track constitute the track's media. Each track has a single media data structure.
 * Specifies the media for an operation. Your application obtains a media identifier from such
 * Movie Toolbox functions as NewTrackMedia and GetTrackMedia (described on page 2-153 of
 * Inside Macintosh: QuickTime and page 2-206 of Inside Macintosh: QuickTime, respectively).
 */
public abstract class Media extends QTObject implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Media.class);
                    }
                });
            }
        }.establish();
    }

    private static Hashtable<Integer, String> mediaObjects = new Hashtable<>();

    Track track;

    private static Media makeGenericMedia(int paramInt1, int paramInt2, Track paramTrack) throws QTException {
        String str = mediaObjects.get(paramInt1);
        try {
            GenericMedia localGenericMedia = null;
            if (str == null) {
                localGenericMedia = new GenericMedia(paramInt2);
            } else {
                Class<?> localClass = ClassLoader.getSystemClassLoader().loadClass(str);
                Class<?>[] arrayOfClass = new Class[1];
                arrayOfClass[0] = Class.forName("java.lang.Integer");
                Constructor<?> localConstructor = localClass.getDeclaredConstructor(arrayOfClass);
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = new Integer(paramInt2);
                localGenericMedia = (GenericMedia) localConstructor.newInstance(arrayOfObject);
            }
            localGenericMedia.setIDs(paramInt1, paramTrack);
            return localGenericMedia;
        } catch (Exception localException) {
            throw new QTException(localException.toString());
        }
    }

    private static Media fromGenericType(int paramInt1, Track paramTrack, int paramInt2, DataRef paramDataRef) throws QTException {
        int i = NewTrackMedia(QTObject.ID(paramTrack), paramInt1, paramInt2, QTObject.ID(paramDataRef), paramDataRef != null ? paramDataRef.getType() : 0);
        StdQTException.checkError(GetMoviesError());
        return makeGenericMedia(paramInt1, i, paramTrack);
    }

    private static Media makeMedia(int paramInt, Track paramTrack) throws QTException {
        if (paramInt == 0) {
            return null;
        }
        int[] arrayOfInt = new int[1];
        GetMediaHandlerDescription(paramInt, arrayOfInt, null, null);
        StdQTException.checkError(GetMoviesError());
        switch(arrayOfInt[0]) {
            case 1735291491:
                return new BaseMedia(paramInt, paramTrack);
            case 1297106247:
                return new MPEGMedia(paramInt, paramTrack);
            case 1836413801:
                return new MusicMedia(paramInt, paramTrack);
            case 1936684398:
                return new SoundMedia(paramInt, paramTrack);
            case 1936749172:
                return new SpriteMedia(paramInt, paramTrack);
            case 1952807028:
                return new TextMedia(paramInt, paramTrack);
            case 1902392164:
                return new ThreeDMedia(paramInt, paramTrack);
            case 1953325924:
                return new TimeCodeMedia(paramInt, paramTrack);
            case 1953981806:
                return new TweenMedia(paramInt, paramTrack);
            case 1986618469:
                return new VideoMedia(paramInt, paramTrack);
            case 1718383464:
                return new FlashMedia(paramInt, paramTrack);
            case 1937011309:
                return new StreamMedia(paramInt, paramTrack);
            case 1836019574:
                return new MovieMedia(paramInt, paramTrack);
        }
        return makeGenericMedia(arrayOfInt[0], paramInt, paramTrack);
    }

    protected static void addMediaType(int paramInt, String paramString) {
        mediaObjects.put(new Integer(paramInt), paramString);
    }

    /**
     * Creates a media struct for the specified Track object. The type of media object
     * that is created is determined by the mediaType argument. If a media class is not
     * found that corresponds with the specified media type then an instance of GenericMedia
     * is returned.
     * QuickTime::NewTrackMedia()
     * @param mediaType the type of media the track will or does contain
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media. If there
     *  is no known dataRef then null can be passed in.
     * @param {@link quicktime.QTException}
     */
    public static Media newFromType(int paramInt1, Track paramTrack, int paramInt2, DataRef paramDataRef) throws QTException {
        if ((paramInt1 == 0) || (paramTrack == null)) {
            throw new QTException(-50);
        }
        switch(paramInt1) {
            case 1735291491:
                return new BaseMedia(paramTrack, paramInt2, paramDataRef);
            case 1297106247:
                return new MPEGMedia(paramTrack, paramInt2, paramDataRef);
            case 1836413801:
                return new MusicMedia(paramTrack, paramInt2, paramDataRef);
            case 1936684398:
                return new SoundMedia(paramTrack, paramInt2, paramDataRef);
            case 1936749172:
                return new SpriteMedia(paramTrack, paramInt2, paramDataRef);
            case 1952807028:
                return new TextMedia(paramTrack, paramInt2, paramDataRef);
            case 1902392164:
                return new ThreeDMedia(paramTrack, paramInt2, paramDataRef);
            case 1953325924:
                return new TimeCodeMedia(paramTrack, paramInt2, paramDataRef);
            case 1953981806:
                return new TweenMedia(paramTrack, paramInt2, paramDataRef);
            case 1986618469:
                return new VideoMedia(paramTrack, paramInt2, paramDataRef);
            case 1718383464:
                return new FlashMedia(paramTrack, paramInt2, paramDataRef);
            case 1937011309:
                return new StreamMedia(paramTrack, paramInt2, paramDataRef);
            case 1836019574:
                return new MovieMedia(paramTrack, paramInt2, paramDataRef);
        }
        return fromGenericType(paramInt1, paramTrack, paramInt2, paramDataRef);
    }

    /**
     * Returns a Media class that corresponds to the media that contains a track's data.
     * QuickTime::GetTrackMedia()
     * @param theTrack specifies the track to get the media from
     * @return a Media subclass
     * @return {@link quicktime.QTException}
     */
    public static Media fromTrack(Track paramTrack) throws QTException {
        return getTrackMedia(paramTrack);
    }

    /**
     * Returns a Media class that corresponds to the media that contains a track's data.
     * QuickTime::GetTrackMedia()
     * @param theTrack specifies the track to get the media from
     * @return a Media subclass
     * @return {@link quicktime.QTException}
     */
    public static Media getTrackMedia(Track paramTrack) throws QTException {
        int i = GetTrackMedia(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return makeMedia(i, paramTrack);
    }

    protected Media(Track paramTrack, int paramInt1, int paramInt2, DataRef paramDataRef) throws QTException {
        super(allocate(paramTrack, paramInt1, paramInt2, paramDataRef));
        this.track = paramTrack;
    }

    private static int allocate(Track paramTrack, int paramInt1, int paramInt2, DataRef paramDataRef) throws StdQTException {
        int i = NewTrackMedia(QTObject.ID(paramTrack), paramInt1, paramInt2, QTObject.ID(paramDataRef), paramDataRef != null ? paramDataRef.getType() : 0);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    Media(Track paramTrack, int paramInt) throws QTException {
        super(paramInt);
        this.track = paramTrack;
    }

    protected final int getNativeMediaHandler() throws StdQTException {
        int i = GetMediaHandler(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    protected final void getNativeSampleDescription(int paramInt, SampleDescription paramSampleDescription) throws StdQTException {
        GetMediaSampleDescription(_ID(), paramInt, QTObject.ID(paramSampleDescription));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to retrieve a sample description from a media.
     * QuickTime::GetMediaSampleDescription()
     * @param index Specifies the index of the sample description to retrieve.
     * @return a SampleDescription object for the specified index.
     * @return {@link quicktime.QTException}
     */
    public SampleDescription getSampleDescription(int paramInt) throws QTException {
        SampleDescription localSampleDescription = makeDescription();
        getNativeSampleDescription(paramInt, localSampleDescription);
        return localSampleDescription;
    }

    /**
     * This method lets you change the contents of a particular sample description of a specified media.
     * QuickTime::SetMediaSampleDescription()
     * @param index Specifies the index of the sample description to be changed.
     * @param descH Specifies the handle to the sample description.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setSampleDescription(int paramInt, SampleDescription paramSampleDescription) throws StdQTException {
        SetMediaSampleDescription(_ID(), paramInt, QTObject.ID(paramSampleDescription));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to determine a media's data handler.
     * QuickTime::GetMediaDataHandler()
     * @param index Identifies the data reference.
     * @param {@link quicktime.std.StdQTException}
     */
    public final DataHandler getDataHandler(int paramInt) throws StdQTException {
        int i = GetMediaDataHandler(_ID(), (short) paramInt);
        StdQTException.checkError(GetMoviesError());
        return new DataHandler(i, this);
    }

    /**
     * This method allows you to assign a data handler to a media.
     * QuickTime::SetMediaDataHandler()
     * @param index Identifies the data reference for this data handler.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setDataHandler(int paramInt, DataHandler paramDataHandler) throws StdQTException {
        StdQTException.checkError(SetMediaDataHandler(_ID(), (short) paramInt, QTObject.ID(paramDataHandler)));
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * QuickTime::GetMediaHandler()
     * @return a MediaHandler object for the media's media handler.
     * @return {@link quicktime.std.StdQTException}
     */
    public MediaHandler getHandler() throws StdQTException {
        return makeHandler(getNativeMediaHandler(), this);
    }

    /**
     * This method allows you to assign a specific media handler to a track.
     * QuickTime::SetMediaHandler()
     * @param mH Contains a reference to a media handler component.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setHandler(MediaHandler paramMediaHandler) throws StdQTException {
        StdQTException.checkError(SetMediaHandler(_ID(), QTObject.ID(paramMediaHandler)));
    }

    /**
     * This method allows you to determine the track that uses a specified media.
     * QuickTime::GetMediaTrack()
     * @return a Track object of the track that uses the media.
     * @return {@link quicktime.QTException}
     */
    public final Track getTrack() throws QTException {
        return Track.fromMedia(this);
    }

    /**
     * This method returns the creation date stored in the media.
     * QuickTime::GetMediaCreationTime()
     * @return the creation date stored in the media.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getCreationTime() throws StdQTException {
        int i = GetMediaCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a media's modification date.
     * QuickTime::GetMediaModificationTime()
     * @return a media's modification date.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getModificationTime() throws StdQTException {
        int i = GetMediaModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine a media's time scale.
     * QuickTime::GetMediaTimeScale()
     * @return the media's time scale.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getTimeScale() throws StdQTException {
        int i = GetMediaTimeScale(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to set a media's time scale.
     * QuickTime::SetMediaTimeScale()
     * @param timeScale Specifies the media's new time scale.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setTimeScale(int paramInt) throws StdQTException {
        SetMediaTimeScale(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the duration of a media.
     * QuickTime::GetMediaDuration()
     * @return the duration of a media.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getDuration() throws StdQTException {
        int i = GetMediaDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a media's language or region code.
     * QuickTime::GetMediaLanguage()
     * @return a media's language or region code.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getLanguage() throws StdQTException {
        int i = GetMediaLanguage(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method sets a media's language or region code.
     * QuickTime::SetMediaLanguage()
     * @param language Specifies the media's language or region code.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setLanguage(int paramInt) throws StdQTException {
        SetMediaLanguage(_ID(), (short) paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns a media's quality level value.
     * QuickTime::GetMediaQuality()
     * @return a media's quality level value.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getQuality() throws StdQTException {
        int i = GetMediaQuality(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method sets a media's quality level value.
     * QuickTime::SetMediaQuality()
     * @param quality Specifies the media's quality value.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setQuality(int paramInt) throws StdQTException {
        SetMediaQuality(_ID(), (short) paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to retrieve information about a media handler.
     * QuickTime::GetMediaHandlerDescription()
     * @return a HandlerInfo object.
     * @return {@link quicktime.std.StdQTException}
     */
    public final HandlerInfo getHandlerDescription() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        byte[] arrayOfByte = new byte[255];
        int[] arrayOfInt2 = new int[1];
        GetMediaHandlerDescription(_ID(), arrayOfInt1, arrayOfByte, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new HandlerInfo(arrayOfInt1[0], new String(arrayOfByte, 1, arrayOfByte[0]), arrayOfInt2[0]);
    }

    /**
     * This method allows your application to obtain access to a media's user data list.
     * QuickTime::GetMediaUserData()
     * @return a UserData object for the media's user data list.
     * @return {@link quicktime.std.StdQTException}
     */
    public final UserData getUserData() throws StdQTException {
        return UserData.fromMedia(this);
    }

    /**
     * This method returns you the input map of the media.
     * QuickTime::GetMediaInputMap()
     * @return an AtomContainer object for media's input map.
     * @return {@link quicktime.std.StdQTException}
     */
    public final AtomContainer getInputMap() throws StdQTException {
        return AtomContainer.fromMediaInput(this);
    }

    /**
     * This method allows you to set a new input map for a media.
     * QuickTime::SetMediaInputMap()
     * @param inputMap Specifies the new input map.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setInputMap(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SetMediaInputMap(_ID(), QTObject.ID(paramAtomContainer)));
    }

    /**
     * This method starts a media-editing session.
     * QuickTime::BeginMediaEdits()
     */
    public final void beginEdits() throws StdQTException {
        StdQTException.checkError(BeginMediaEdits(_ID()));
    }

    /**
     * This method ends a media-editing session.
     * QuickTime::EndMediaEdits()
     */
    public final void endEdits() throws StdQTException {
        StdQTException.checkError(EndMediaEdits(_ID()));
    }

    /**
     * This method allows your application to determine the default index for a media.
     * QuickTime::SetMediaDefaultDataRefIndex()
     * @param index Specifies the index of the data ref.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setDefaultDataRefIndex(int paramInt) throws StdQTException {
        StdQTException.checkError(SetMediaDefaultDataRefIndex(_ID(), (short) paramInt));
    }

    /**
     * This method allows your application to retrieve information about a media's data handler.
     * QuickTime::GetMediaDataHandlerDescription()
     * @param index Identifies the data reference.
     * @return a HandlerInfo object.
     * @return {@link quicktime.std.StdQTException}
     */
    public final HandlerInfo getDataHandlerDescription(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        byte[] arrayOfByte = new byte[255];
        int[] arrayOfInt2 = new int[1];
        GetMediaDataHandlerDescription(_ID(), (short) paramInt, arrayOfInt1, arrayOfByte, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new HandlerInfo(arrayOfInt1[0], new String(arrayOfByte, 1, arrayOfByte[0]), arrayOfInt2[0]);
    }

    /**
     * This method returns the number of sample descriptions in a media.
     * QuickTime::GetMediaSampleDescriptionCount()
     * @return the number of sample descriptions in a media.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getSampleDescriptionCount() throws StdQTException {
        int i = GetMediaSampleDescriptionCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to determine the number of samples in a media.
     * QuickTime::GetMediaSampleCount()
     * @return an integer that specifies the number of samples in the specified media.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getSampleCount() throws StdQTException {
        int i = GetMediaSampleCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to determine the number of samples in a media.
     * QuickTime::GetMediaSyncSampleCount()
     * @return an integer that specifies the number of samples in the specified media.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getSyncSampleCount() throws StdQTException {
        int i = GetMediaSyncSampleCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to find the time at which a specified sample plays.
     * QuickTime::SampleNumToMediaTime()
     * @param logicalSampleNum Specifies the sample number.
     * @return a TimeInfo object that contains the time and duration of the sample.
     * @return {@link quicktime.std.StdQTException}
     */
    public final TimeInfo sampleNumToMediaTime(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        SampleNumToMediaTime(_ID(), paramInt, arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method allows you to find the sample that contains the data for a specified time.
     * QuickTime::MediaTimeToSampleNum()
     * @param time Specifies the time for which you are retrieving sample information.
     * @return a SampleTimeInfo object that contains the sample time and sample duration for the specified sample.
     * @return {@link quicktime.std.StdQTException}
     */
    public final SampleTimeInfo timeToSampleNum(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        MediaTimeToSampleNum(_ID(), paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(GetMoviesError());
        return new SampleTimeInfo(arrayOfInt2[0], arrayOfInt3[0], arrayOfInt1[0]);
    }

    /**
     * This method adds sample data and a description to a media object.
     * QuickTime::AddMediaSample()
     * @param data Specifies media sample's data handle.
     * @param dataOffset an offset into the sample data.
     * @param dataSize Specifies media sample's data size.
     * @param durationPerSample Specifies media sample's duration.
     * @param sampleDesc Specifies media sample's sample description.
     * @param numberOfSamples Specifies the number of samples.
     * @param sampleFlags Specifies flags for samples.
     * @return the time where the sample was inserted.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int addSample(QTHandleRef paramQTHandleRef, int paramInt1, int paramInt2, int paramInt3, SampleDescription paramSampleDescription, int paramInt4, int paramInt5) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(AddMediaSample(_ID(), QTObject.ID(paramQTHandleRef), paramInt1, paramInt2, paramInt3, QTObject.ID(paramSampleDescription), paramInt4, (short) paramInt5, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method returns a sample from a movie data file.
     * QuickTime::GetMediaSample()
     * @param maxSizeToGrow Specifies the maximum number of bytes of sample data to be returned.
     * @param time Specifies the starting time of the sample to be retrieved.
     * @param maxNumberOfSamples Specifies the maximum number of samples to be returned.
     * @return a MediaSample object specifying the media sample.
     * @return {@link quicktime.QTException}
     */
    public final MediaSample getSample(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        SampleDescription localSampleDescription = makeDescription();
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        int[] arrayOfInt5 = new int[1];
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(GetMediaSample(_ID(), QTObject.ID(localQTHandle), paramInt1, arrayOfInt1, paramInt2, arrayOfInt2, arrayOfInt3, QTObject.ID(localSampleDescription), arrayOfInt4, paramInt3, arrayOfInt5, arrayOfShort));
        return new MediaSample(arrayOfInt2[0], arrayOfInt3[0], localQTHandle, 0, arrayOfInt1[0], arrayOfInt5[0], localSampleDescription, arrayOfInt4[0], arrayOfShort[0]);
    }

    /**
     * This method allows your application to work with samples that have already been added to a movie data file.
     * QuickTime::AddMediaSampleReference()
     * @param dataOffset an offset into the sample data.
     * @param dataSize Specifies media sample's data size.
     * @param durationPerSample Specifies media sample's duration.
     * @param sampleDesc Specifies media sample's sample description.
     * @param numberOfSamples Specifies the number of samples.
     * @param sampleFlags Specifies flags for samples.
     * @return the time where the reference was inserted
     * @return {@link quicktime.std.StdQTException}
     */
    public final int addSampleReference(int paramInt1, int paramInt2, int paramInt3, SampleDescription paramSampleDescription, int paramInt4, int paramInt5) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(AddMediaSampleReference(_ID(), paramInt1, paramInt2, paramInt3, QTObject.ID(paramSampleDescription), paramInt4, (short) paramInt5, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method allows your application to obtain reference information about samples that are stored in a movie data file.
     * QuickTime::GetMediaSampleReference()
     * @param time Specifies the starting time of the sample reference to be retrieved.
     * @param maxNumberOfSamples Specifies the maximum number of samples to be returned.
     * @return a SampleInfo object.
     * @return {@link quicktime.QTException}
     */
    public final SampleInfo getSampleReference(int paramInt1, int paramInt2) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        SampleDescription localSampleDescription = makeDescription();
        int[] arrayOfInt5 = new int[1];
        int[] arrayOfInt6 = new int[1];
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(GetMediaSampleReference(_ID(), arrayOfInt1, arrayOfInt2, paramInt1, arrayOfInt3, arrayOfInt4, QTObject.ID(localSampleDescription), arrayOfInt5, paramInt2, arrayOfInt6, arrayOfShort));
        return new SampleInfo(arrayOfInt3[0], arrayOfInt4[0], arrayOfInt1[0], arrayOfInt2[0], arrayOfInt6[0], localSampleDescription, arrayOfInt5[0], arrayOfShort[0]);
    }

    /**
     * Allows your application to add groups of sample references to a movie data file.
     * QuickTime::AddMediaSampleReferences()
     * @param sampleDescription describes characteristics about the sample references
     * @param numOfSamples the number of references added
     * @param sampleRefs the references add, there must be at least as many references in the vector
     *  as numOfSamples (there may be more).
     * @return the time where the reference was inserted
     * @return {@link quicktime.std.StdQTException}
     */
    public final int addSampleReference(SampleDescription paramSampleDescription, int paramInt, SampleReferenceVector paramSampleReferenceVector) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = AddMediaSampleReferences(_ID(), QTObject.ID(paramSampleDescription), paramInt, QTObject.ID(paramSampleReferenceVector), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method  allows your application to obtain reference information about samples that are stored in a movie data file.
     * QuickTime::GetMediaSampleReferences()
     * @param time secifies the starting time of the sample reference to be retrieved.
     * @param maxNumberOfSamples Specifies the maximum number of samples to be returned.
     * @return a SampleReferenceInfo object.
     * @return {@link quicktime.QTException}
     */
    public final SampleReferenceInfo getSampleReferences(int paramInt1, int paramInt2) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        SampleDescription localSampleDescription = makeDescription();
        SampleReferenceVector localSampleReferenceVector = new SampleReferenceVector(paramInt2);
        int i = QTObject.ID(localSampleReferenceVector);
        int j = GetMediaSampleReferences(_ID(), paramInt1, arrayOfInt1, QTObject.ID(localSampleDescription), arrayOfInt2, paramInt2, arrayOfInt3, i);
        StdQTException.checkError(j);
        localSampleReferenceVector._setNumSamples(arrayOfInt3[0]);
        return new SampleReferenceInfo(arrayOfInt1[0], localSampleDescription, arrayOfInt2[0], localSampleReferenceVector);
    }

    /**
     * This method allows your application to specify a preferred chunk size for the media.
     * QuickTime::SetMediaPreferredChunkSize()
     * @param maxChunkSize Specifies the prefered maximum chunk size.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setPreferredChunkSize(int paramInt) throws StdQTException {
        StdQTException.checkError(SetMediaPreferredChunkSize(_ID(), paramInt));
    }

    /**
     * This method returns the preferred chunck size for the media.
     * QuickTime::GetMediaPreferredChunkSize()
     * @return the preferred chunck size for the media.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getPreferredChunkSize() throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaPreferredChunkSize(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method creates an association between the indicated frame difference sample
     * and a specified self-contained sample in a given media.
     * QuickTime::SetMediaShadowSync()
     * @param frameDiffSampleNum Specifies a frame difference sample.
     * @param syncSampleNum Specifies a shadow sync sample.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setShadowSync(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(SetMediaShadowSync(_ID(), paramInt1, paramInt2));
    }

    /**
     * This method returns the sample number of the shadow sync associated with a given frame difference sample number.
     * QuickTime::GetMediaShadowSync()
     * @return the sample number of the shadow sync.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getShadowSync(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaShadowSync(_ID(), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method searches for times of interest in a media.
     * QuickTime::GetMediaNextInterestingTime()
     * @param interestingTimeFlags Specifies the search criteria.
     * @param time Specifies a time value that establishes the starting point for the search.
     * @param rate Contains the search direction.
     * @return a TimeInfo object that specifies the first found interesting time.
     * @return {@link quicktime.std.StdQTException}
     */
    public final TimeInfo getNextInterestingTime(int paramInt1, int paramInt2, float paramFloat) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetMediaNextInterestingTime(_ID(), (short) paramInt1, paramInt2, QTUtils.X2Fix(paramFloat), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method allows your application to determine the size, in bytes, of the sample data in a media segment.
     * QuickTime::GetMediaDataSize()
     * @param startTime Contains a time value specifying the starting point of the segment.
     * @param duration Contains a time value that specifies the duration of the segment.
     * @return the data size.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getDataSize(int paramInt1, int paramInt2) throws StdQTException {
        int i = GetMediaDataSize(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a copy of a specified data reference.
     * QuickTime::GetMediaDataRef()
     * @param index Identifies the data reference.
     * @return a copy of a specified data reference.
     * @return {@link quicktime.QTException}
     */
    public final DataRef getDataRef(int paramInt) throws QTException {
        return DataRef.fromMedia(this, paramInt);
    }

    /**
     * This method changes the file that the specified media identifies as the location for its data storage.
     * QuickTime::SetMediaDataRef()
     * @param index the index value at which to add the data.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setDataRef(int paramInt, DataRef paramDataRef) throws StdQTException {
        StdQTException.checkError(SetMediaDataRef(_ID(), (short) paramInt, QTObject.ID(paramDataRef), paramDataRef.getType()));
    }

    /**
     * No QTDocumentation.
     * QuickTime::SetMediaDataRefAttributes()
     */
    public final void setDataRefAttributes(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(SetMediaDataRefAttributes(_ID(), (short) paramInt1, paramInt2));
    }

    /**
     * This method adds a data reference to a media.
     * QuickTime::AddMediaDataRef()
     * @param dataReference Specifies the data reference.
     * @return the index value that is assigned to the new data reference.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int addDataRef(DataRef paramDataRef) throws StdQTException {
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(AddMediaDataRef(_ID(), arrayOfShort, QTObject.ID(paramDataRef), paramDataRef.getType()));
        return arrayOfShort[0];
    }

    /**
     * This method allows your application to determine the number of data references in a media.
     * QuickTime::GetMediaDataRefCount()
     * @return the number of data references in the media.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getDataRefCount() throws StdQTException {
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(GetMediaDataRefCount(_ID(), arrayOfShort));
        return arrayOfShort[0];
    }

    /**
     * This method allows your application to provide information to the Movie Toolbox
     * that can influence playback of a single media.
     * QuickTime::SetMediaPlayHints()
     * @param flags Specifies the optimizations that can be used with this media.
     * @param flagsMask Indicates which flags in the flags parameter are to be considered in this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setPlayHints(int paramInt1, int paramInt2) throws StdQTException {
        SetMediaPlayHints(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * Deprecated.
     * <I>these arguments are not used in this call see getPlayHints()</I>
     * This method allows your application to get information from the Movie Toolbox
     * that determines the current state of playback of a single media.
     * QuickTime::GetMediaPlayHints()
     * @param flags Specifies the optimizations that can be used with this media.
     * @param flagsMask Indicates which flags in the flags parameter are to be considered in this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public final int getPlayHints(int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = { 0 };
        GetMediaPlayHints(_ID(), arrayOfInt);
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    /**
     * This method allows your application to get information from the Movie Toolbox
     * that determines the current state of playback of a single media.
     * QuickTime::GetMediaPlayHints()
     */
    public final int getPlayHints() throws StdQTException {
        int[] arrayOfInt = { 0 };
        GetMediaPlayHints(_ID(), arrayOfInt);
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    /**
     * No QT Documentation.
     * QuickTime::SetMediaPropertyAtom()
     */
    public final void setPropertyAtom(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SetMediaPropertyAtom(_ID(), QTObject.ID(paramAtomContainer)));
    }

    /**
     * Retrieves the properties from a media handler.
     * QuickTime::GetMediaPropertyAtom()
     * @return Contains the property atoms for the track associated with the media handler.
     * @return {@link quicktime.std.StdQTException}
     */
    public final AtomContainer getPropertyAtom() throws StdQTException {
        return AtomContainer.fromMediaProperty(this);
    }

    /**
     * This method loads a media's data into memory.
     * QuickTime::LoadMediaIntoRam()
     * @param time starting time of the media segment to load.
     * @param duration duration of the media segment to load
     * @param flags control the loading operation
     * @param {@link quicktime.std.StdQTException}
     */
    public final void loadIntoRam(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        StdQTException.checkError(LoadMediaIntoRam(_ID(), paramInt1, paramInt2, paramInt3));
    }

    /**
     * Copies the user data in the media to the destination media
     * QuickTime::CopyMediaUserData()
     * @param destMedia destination media for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @param {@link quicktime.std.StdQTException}
     */
    public void copyUserDataToMedia(Media paramMedia, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(CopyMediaUserData(_ID(), QTObject.ID(paramMedia), paramBoolean == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified media into this media
     * QuickTime::CopyMediaUserData()
     * @param srcMedia source media for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @param {@link quicktime.std.StdQTException}
     */
    public void copyUserDataFromMedia(Media paramMedia, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(CopyMediaUserData(QTObject.ID(paramMedia), _ID(), paramBoolean == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified media into the destination media
     * QuickTime::CopyMediaUserData()
     * @param srcMedia source media for user data
     * @param destMedia destination media for user data
     * @param copyRule constant for merge or replace
     *     use StdQTConstants5_1.kQTCopyUserDataReplace or StdQTConstants5_1.kQTCopyUserDataMerge
     * @param {@link quicktime.std.StdQTException}
     */
    public static void copyUserData(Media paramMedia1, Media paramMedia2, int paramInt) throws StdQTException {
        StdQTException.checkError(CopyMediaUserData(QTObject.ID(paramMedia1), QTObject.ID(paramMedia2), paramInt));
    }

    abstract SampleDescription makeDescription() throws QTException;

    abstract MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException;

    private static native int NewTrackMedia(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short GetMoviesError();

    private static native void GetMediaHandlerDescription(int paramInt, int[] paramArrayOfInt1, byte[] paramArrayOfByte, int[] paramArrayOfInt2);

    private static native int GetTrackMedia(int paramInt);

    private static native int GetMediaHandler(int paramInt);

    private static native void GetMediaSampleDescription(int paramInt1, int paramInt2, int paramInt3);

    private static native short SetMediaSampleDescription(int paramInt1, int paramInt2, int paramInt3);

    private static native int GetMediaDataHandler(int paramInt, short paramShort);

    private static native short SetMediaDataHandler(int paramInt1, short paramShort, int paramInt2);

    private static native short SetMediaHandler(int paramInt1, int paramInt2);

    private static native int GetMediaCreationTime(int paramInt);

    private static native int GetMediaModificationTime(int paramInt);

    private static native int GetMediaTimeScale(int paramInt);

    private static native void SetMediaTimeScale(int paramInt1, int paramInt2);

    private static native int GetMediaDuration(int paramInt);

    private static native short GetMediaLanguage(int paramInt);

    private static native void SetMediaLanguage(int paramInt, short paramShort);

    private static native short GetMediaQuality(int paramInt);

    private static native void SetMediaQuality(int paramInt, short paramShort);

    private static native short SetMediaInputMap(int paramInt1, int paramInt2);

    private static native short BeginMediaEdits(int paramInt);

    private static native short EndMediaEdits(int paramInt);

    private static native short SetMediaDefaultDataRefIndex(int paramInt, short paramShort);

    private static native void GetMediaDataHandlerDescription(int paramInt, short paramShort, int[] paramArrayOfInt1, byte[] paramArrayOfByte, int[] paramArrayOfInt2);

    private static native int GetMediaSampleDescriptionCount(int paramInt);

    private static native int GetMediaSampleCount(int paramInt);

    private static native int GetMediaSyncSampleCount(int paramInt);

    private static native void SampleNumToMediaTime(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void MediaTimeToSampleNum(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3);

    private static native short AddMediaSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short paramShort, int[] paramArrayOfInt);

    private static native short GetMediaSample(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int paramInt4, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int paramInt5, int[] paramArrayOfInt4, int paramInt6, int[] paramArrayOfInt5, short[] paramArrayOfShort);

    private static native short AddMediaSampleReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, short paramShort, int[] paramArrayOfInt);

    private static native short GetMediaSampleReference(int paramInt1, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int paramInt3, int[] paramArrayOfInt5, int paramInt4, int[] paramArrayOfInt6, short[] paramArrayOfShort);

    private static native short AddMediaSampleReferences(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt);

    private static native short GetMediaSampleReferences(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, int[] paramArrayOfInt3, int paramInt5);

    private static native short SetMediaPreferredChunkSize(int paramInt1, int paramInt2);

    private static native short GetMediaPreferredChunkSize(int paramInt, int[] paramArrayOfInt);

    private static native short SetMediaShadowSync(int paramInt1, int paramInt2, int paramInt3);

    private static native short GetMediaShadowSync(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native void GetMediaNextInterestingTime(int paramInt1, short paramShort, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GetMediaDataSize(int paramInt1, int paramInt2, int paramInt3);

    private static native short SetMediaDataRef(int paramInt1, short paramShort, int paramInt2, int paramInt3);

    private static native short SetMediaDataRefAttributes(int paramInt1, short paramShort, int paramInt2);

    private static native short AddMediaDataRef(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3);

    private static native short GetMediaDataRefCount(int paramInt, short[] paramArrayOfShort);

    private static native void SetMediaPlayHints(int paramInt1, int paramInt2, int paramInt3);

    private static native void GetMediaPlayHints(int paramInt, int[] paramArrayOfInt);

    private static native short SetMediaPropertyAtom(int paramInt1, int paramInt2);

    private static native short LoadMediaIntoRam(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short CopyMediaUserData(int paramInt1, int paramInt2, int paramInt3);
}

