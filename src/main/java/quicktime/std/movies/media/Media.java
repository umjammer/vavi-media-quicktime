package quicktime.std.movies.media;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MovieToolbox;
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
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(Media.class));
            }
        }.establish();
    }

    private static final Hashtable<Integer, String> mediaObjects = new Hashtable<>();

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
                arrayOfObject[0] = paramInt2;
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
        return switch (arrayOfInt[0]) {
            case 1735291491 -> new BaseMedia(paramInt, paramTrack);
            case 1297106247 -> new MPEGMedia(paramInt, paramTrack);
            case 1836413801 -> new MusicMedia(paramInt, paramTrack);
            case 1936684398 -> new SoundMedia(paramInt, paramTrack);
            case 1936749172 -> new SpriteMedia(paramInt, paramTrack);
            case 1952807028 -> new TextMedia(paramInt, paramTrack);
            case 1902392164 -> new ThreeDMedia(paramInt, paramTrack);
            case 1953325924 -> new TimeCodeMedia(paramInt, paramTrack);
            case 1953981806 -> new TweenMedia(paramInt, paramTrack);
            case 1986618469 -> new VideoMedia(paramInt, paramTrack);
            case 1718383464 -> new FlashMedia(paramInt, paramTrack);
            case 1937011309 -> new StreamMedia(paramInt, paramTrack);
            case 1836019574 -> new MovieMedia(paramInt, paramTrack);
            default -> makeGenericMedia(arrayOfInt[0], paramInt, paramTrack);
        };
    }

    protected static void addMediaType(int paramInt, String paramString) {
        mediaObjects.put(paramInt, paramString);
    }

    /**
     * Creates a media struct for the specified Track object. The type of media object
     * that is created is determined by the mediaType argument. If a media class is not
     * found that corresponds with the specified media type then an instance of GenericMedia
     * is returned.
     * @see "QuickTime::NewTrackMedia()"
     * @param mediaType the type of media the track will or does contain
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media. If there
     *  is no known dataRef then null can be passed in.
     * @throws quicktime.QTException
     */
    public static Media newFromType(int paramInt1, Track paramTrack, int paramInt2, DataRef paramDataRef) throws QTException {
        if ((paramInt1 == 0) || (paramTrack == null)) {
            throw new QTException(-50);
        }
        return switch (paramInt1) {
            case 1735291491 -> new BaseMedia(paramTrack, paramInt2, paramDataRef);
            case 1297106247 -> new MPEGMedia(paramTrack, paramInt2, paramDataRef);
            case 1836413801 -> new MusicMedia(paramTrack, paramInt2, paramDataRef);
            case 1936684398 -> new SoundMedia(paramTrack, paramInt2, paramDataRef);
            case 1936749172 -> new SpriteMedia(paramTrack, paramInt2, paramDataRef);
            case 1952807028 -> new TextMedia(paramTrack, paramInt2, paramDataRef);
            case 1902392164 -> new ThreeDMedia(paramTrack, paramInt2, paramDataRef);
            case 1953325924 -> new TimeCodeMedia(paramTrack, paramInt2, paramDataRef);
            case 1953981806 -> new TweenMedia(paramTrack, paramInt2, paramDataRef);
            case 1986618469 -> new VideoMedia(paramTrack, paramInt2, paramDataRef);
            case 1718383464 -> new FlashMedia(paramTrack, paramInt2, paramDataRef);
            case 1937011309 -> new StreamMedia(paramTrack, paramInt2, paramDataRef);
            case 1836019574 -> new MovieMedia(paramTrack, paramInt2, paramDataRef);
            default -> fromGenericType(paramInt1, paramTrack, paramInt2, paramDataRef);
        };
    }

    /**
     * Returns a Media class that corresponds to the media that contains a track's data.
     * @see "QuickTime::GetTrackMedia()"
     * @param theTrack specifies the track to get the media from
     * @return a Media subclass
     * @throws quicktime.QTException
     */
    public static Media fromTrack(Track theTrack) throws QTException {
        return getTrackMedia(theTrack);
    }

    /**
     * Returns a Media class that corresponds to the media that contains a track's data.
     * @see "QuickTime::GetTrackMedia()"
     * @param theTrack specifies the track to get the media from
     * @return a Media subclass
     * @throws quicktime.QTException
     */
    public static Media getTrackMedia(Track theTrack) throws QTException {
        int i = GetTrackMedia(QTObject.ID(theTrack));
        StdQTException.checkError(GetMoviesError());
        return makeMedia(i, theTrack);
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
     * @see "QuickTime::GetMediaSampleDescription()"
     * @param index Specifies the index of the sample description to retrieve.
     * @return a SampleDescription object for the specified index.
     * @throws quicktime.QTException
     */
    public SampleDescription getSampleDescription(int index) throws QTException {
        SampleDescription localSampleDescription = makeDescription();
        getNativeSampleDescription(index, localSampleDescription);
        return localSampleDescription;
    }

    /**
     * This method lets you change the contents of a particular sample description of a specified media.
     * @see "QuickTime::SetMediaSampleDescription()"
     * @param index Specifies the index of the sample description to be changed.
     * @param descH Specifies the handle to the sample description.
     * @throws quicktime.std.StdQTException
     */
    public final void setSampleDescription(int index, SampleDescription descH) throws StdQTException {
        SetMediaSampleDescription(_ID(), index, QTObject.ID(descH));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to determine a media's data handler.
     * @see "QuickTime::GetMediaDataHandler()"
     * @param index Identifies the data reference.
     * @throws quicktime.std.StdQTException
     */
    public final DataHandler getDataHandler(int index) throws StdQTException {
        int i = GetMediaDataHandler(_ID(), (short) index);
        StdQTException.checkError(GetMoviesError());
        return new DataHandler(i, this);
    }

    /**
     * This method allows you to assign a data handler to a media.
     * @see "QuickTime::SetMediaDataHandler()"
     * @param index Identifies the data reference for this data handler.
     * @throws quicktime.std.StdQTException
     */
    public final void setDataHandler(int paramInt, DataHandler paramDataHandler) throws StdQTException {
        StdQTException.checkError(SetMediaDataHandler(_ID(), (short) paramInt, QTObject.ID(paramDataHandler)));
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * @see "QuickTime::GetMediaHandler()"
     * @return a MediaHandler object for the media's media handler.
     * @throws quicktime.std.StdQTException
     */
    public MediaHandler getHandler() throws StdQTException {
        return makeHandler(getNativeMediaHandler(), this);
    }

    /**
     * This method allows you to assign a specific media handler to a track.
     * @see "QuickTime::SetMediaHandler()"
     * @param mH Contains a reference to a media handler component.
     * @throws quicktime.std.StdQTException
     */
    public final void setHandler(MediaHandler mH) throws StdQTException {
        StdQTException.checkError(SetMediaHandler(_ID(), QTObject.ID(mH)));
    }

    /**
     * This method allows you to determine the track that uses a specified media.
     * @see "QuickTime::GetMediaTrack()"
     * @return a Track object of the track that uses the media.
     * @throws quicktime.QTException
     */
    public final Track getTrack() throws QTException {
        return Track.fromMedia(this);
    }

    /**
     * This method returns the creation date stored in the media.
     * @see "QuickTime::GetMediaCreationTime()"
     * @return the creation date stored in the media.
     * @throws quicktime.std.StdQTException
     */
    public final int getCreationTime() throws StdQTException {
        int i = GetMediaCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a media's modification date.
     * @see "QuickTime::GetMediaModificationTime()"
     * @return a media's modification date.
     * @throws quicktime.std.StdQTException
     */
    public final int getModificationTime() throws StdQTException {
        int i = GetMediaModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine a media's time scale.
     * @see "QuickTime::GetMediaTimeScale()"
     * @return the media's time scale.
     * @throws quicktime.std.StdQTException
     */
    public final int getTimeScale() throws StdQTException {
        int i = GetMediaTimeScale(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to set a media's time scale.
     * @see "QuickTime::SetMediaTimeScale()"
     * @param timeScale Specifies the media's new time scale.
     * @throws quicktime.std.StdQTException
     */
    public final void setTimeScale(int timeScale) throws StdQTException {
        SetMediaTimeScale(_ID(), timeScale);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the duration of a media.
     * @see "QuickTime::GetMediaDuration()"
     * @return the duration of a media.
     * @throws quicktime.std.StdQTException
     */
    public final int getDuration() throws StdQTException {
        int i = GetMediaDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a media's language or region code.
     * @see "QuickTime::GetMediaLanguage()"
     * @return a media's language or region code.
     * @throws quicktime.std.StdQTException
     */
    public final int getLanguage() throws StdQTException {
        int i = GetMediaLanguage(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method sets a media's language or region code.
     * @see "QuickTime::SetMediaLanguage()"
     * @param language Specifies the media's language or region code.
     * @throws quicktime.std.StdQTException
     */
    public final void setLanguage(int language) throws StdQTException {
        SetMediaLanguage(_ID(), (short) language);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns a media's quality level value.
     * @see "QuickTime::GetMediaQuality()"
     * @return a media's quality level value.
     * @throws quicktime.std.StdQTException
     */
    public final int getQuality() throws StdQTException {
        int i = GetMediaQuality(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method sets a media's quality level value.
     * @see "QuickTime::SetMediaQuality()"
     * @param quality Specifies the media's quality value.
     * @throws quicktime.std.StdQTException
     */
    public final void setQuality(int quality) throws StdQTException {
        SetMediaQuality(_ID(), (short) quality);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to retrieve information about a media handler.
     * @see "QuickTime::GetMediaHandlerDescription()"
     * @return a HandlerInfo object.
     * @throws quicktime.std.StdQTException
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
     * @see "QuickTime::GetMediaUserData()"
     * @return a UserData object for the media's user data list.
     * @throws quicktime.std.StdQTException
     */
    public final UserData getUserData() throws StdQTException {
        return UserData.fromMedia(this);
    }

    /**
     * This method returns you the input map of the media.
     * @see "QuickTime::GetMediaInputMap()"
     * @return an AtomContainer object for media's input map.
     * @throws quicktime.std.StdQTException
     */
    public final AtomContainer getInputMap() throws StdQTException {
        return AtomContainer.fromMediaInput(this);
    }

    /**
     * This method allows you to set a new input map for a media.
     * @see "QuickTime::SetMediaInputMap()"
     * @param inputMap Specifies the new input map.
     * @throws quicktime.std.StdQTException
     */
    public final void setInputMap(AtomContainer inputMap) throws StdQTException {
        StdQTException.checkError(SetMediaInputMap(_ID(), QTObject.ID(inputMap)));
    }

    /**
     * This method starts a media-editing session.
     * @see "QuickTime::BeginMediaEdits()"
     */
    public final void beginEdits() throws StdQTException {
        StdQTException.checkError(BeginMediaEdits(_ID()));
    }

    /**
     * This method ends a media-editing session.
     * @see "QuickTime::EndMediaEdits()"
     */
    public final void endEdits() throws StdQTException {
        StdQTException.checkError(EndMediaEdits(_ID()));
    }

    /**
     * This method allows your application to determine the default index for a media.
     * @see "QuickTime::SetMediaDefaultDataRefIndex()"
     * @param index Specifies the index of the data ref.
     * @throws quicktime.std.StdQTException
     */
    public final void setDefaultDataRefIndex(int index) throws StdQTException {
        StdQTException.checkError(SetMediaDefaultDataRefIndex(_ID(), (short) index));
    }

    /**
     * This method allows your application to retrieve information about a media's data handler.
     * @see "QuickTime::GetMediaDataHandlerDescription()"
     * @param index Identifies the data reference.
     * @return a HandlerInfo object.
     * @throws quicktime.std.StdQTException
     */
    public final HandlerInfo getDataHandlerDescription(int index) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        byte[] arrayOfByte = new byte[255];
        int[] arrayOfInt2 = new int[1];
        GetMediaDataHandlerDescription(_ID(), (short) index, arrayOfInt1, arrayOfByte, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new HandlerInfo(arrayOfInt1[0], new String(arrayOfByte, 1, arrayOfByte[0]), arrayOfInt2[0]);
    }

    /**
     * This method returns the number of sample descriptions in a media.
     * @see "QuickTime::GetMediaSampleDescriptionCount()"
     * @return the number of sample descriptions in a media.
     * @throws quicktime.std.StdQTException
     */
    public final int getSampleDescriptionCount() throws StdQTException {
        int i = GetMediaSampleDescriptionCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to determine the number of samples in a media.
     * @see "QuickTime::GetMediaSampleCount()"
     * @return an integer that specifies the number of samples in the specified media.
     * @throws quicktime.std.StdQTException
     */
    public final int getSampleCount() throws StdQTException {
        int i = GetMediaSampleCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to determine the number of samples in a media.
     * @see "QuickTime::GetMediaSyncSampleCount()"
     * @return an integer that specifies the number of samples in the specified media.
     * @throws quicktime.std.StdQTException
     */
    public final int getSyncSampleCount() throws StdQTException {
        int i = GetMediaSyncSampleCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to find the time at which a specified sample plays.
     * @see "QuickTime::SampleNumToMediaTime()"
     * @param logicalSampleNum Specifies the sample number.
     * @return a TimeInfo object that contains the time and duration of the sample.
     * @throws quicktime.std.StdQTException
     */
    public final TimeInfo sampleNumToMediaTime(int logicalSampleNum) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        SampleNumToMediaTime(_ID(), logicalSampleNum, arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method allows you to find the sample that contains the data for a specified time.
     * @see "QuickTime::MediaTimeToSampleNum()"
     * @param time Specifies the time for which you are retrieving sample information.
     * @return a SampleTimeInfo object that contains the sample time and sample duration for the specified sample.
     * @throws quicktime.std.StdQTException
     */
    public final SampleTimeInfo timeToSampleNum(int time) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        MediaTimeToSampleNum(_ID(), time, arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(GetMoviesError());
        return new SampleTimeInfo(arrayOfInt2[0], arrayOfInt3[0], arrayOfInt1[0]);
    }

    /**
     * This method adds sample data and a description to a media object.
     * @see "QuickTime::AddMediaSample()"
     * @param data Specifies media sample's data handle.
     * @param dataOffset an offset into the sample data.
     * @param dataSize Specifies media sample's data size.
     * @param durationPerSample Specifies media sample's duration.
     * @param sampleDesc Specifies media sample's sample description.
     * @param numberOfSamples Specifies the number of samples.
     * @param sampleFlags Specifies flags for samples.
     * @return the time where the sample was inserted.
     * @throws quicktime.std.StdQTException
     */
    public final int addSample(QTHandleRef data, int dataOffset, int dataSize, int durationPerSample, SampleDescription sampleDesc, int numberOfSamples, int sampleFlags) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(AddMediaSample(_ID(), QTObject.ID(data), dataOffset, dataSize, durationPerSample, QTObject.ID(sampleDesc), numberOfSamples, (short) sampleFlags, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method returns a sample from a movie data file.
     * @see "QuickTime::GetMediaSample()"
     * @param maxSizeToGrow Specifies the maximum number of bytes of sample data to be returned.
     * @param time Specifies the starting time of the sample to be retrieved.
     * @param maxNumberOfSamples Specifies the maximum number of samples to be returned.
     * @return a MediaSample object specifying the media sample.
     * @throws quicktime.QTException
     */
    public final MediaSample getSample(int maxSizeToGrow, int time, int maxNumberOfSamples) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        SampleDescription localSampleDescription = makeDescription();
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        int[] arrayOfInt5 = new int[1];
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(GetMediaSample(_ID(), QTObject.ID(localQTHandle), maxSizeToGrow, arrayOfInt1, time, arrayOfInt2, arrayOfInt3, QTObject.ID(localSampleDescription), arrayOfInt4, maxNumberOfSamples, arrayOfInt5, arrayOfShort));
        return new MediaSample(arrayOfInt2[0], arrayOfInt3[0], localQTHandle, 0, arrayOfInt1[0], arrayOfInt5[0], localSampleDescription, arrayOfInt4[0], arrayOfShort[0]);
    }

    /**
     * This method allows your application to work with samples that have already been added to a movie data file.
     * @see "QuickTime::AddMediaSampleReference()"
     * @param dataOffset an offset into the sample data.
     * @param dataSize Specifies media sample's data size.
     * @param durationPerSample Specifies media sample's duration.
     * @param sampleDesc Specifies media sample's sample description.
     * @param numberOfSamples Specifies the number of samples.
     * @param sampleFlags Specifies flags for samples.
     * @return the time where the reference was inserted
     * @throws quicktime.std.StdQTException
     */
    public final int addSampleReference(int dataOffset, int dataSize, int durationPerSample, SampleDescription sampleDesc, int numberOfSamples, int sampleFlags) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(AddMediaSampleReference(_ID(), dataOffset, dataSize, durationPerSample, QTObject.ID(sampleDesc), numberOfSamples, (short) sampleFlags, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method allows your application to obtain reference information about samples that are stored in a movie data file.
     * @see "QuickTime::GetMediaSampleReference()"
     * @param time Specifies the starting time of the sample reference to be retrieved.
     * @param maxNumberOfSamples Specifies the maximum number of samples to be returned.
     * @return a SampleInfo object.
     * @throws quicktime.QTException
     */
    public final SampleInfo getSampleReference(int time, int maxNumberOfSamples) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        SampleDescription localSampleDescription = makeDescription();
        int[] arrayOfInt5 = new int[1];
        int[] arrayOfInt6 = new int[1];
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(GetMediaSampleReference(_ID(), arrayOfInt1, arrayOfInt2, time, arrayOfInt3, arrayOfInt4, QTObject.ID(localSampleDescription), arrayOfInt5, maxNumberOfSamples, arrayOfInt6, arrayOfShort));
        return new SampleInfo(arrayOfInt3[0], arrayOfInt4[0], arrayOfInt1[0], arrayOfInt2[0], arrayOfInt6[0], localSampleDescription, arrayOfInt5[0], arrayOfShort[0]);
    }

    /**
     * Allows your application to add groups of sample references to a movie data file.
     * @see "QuickTime::AddMediaSampleReferences()"
     * @param sampleDescription describes characteristics about the sample references
     * @param numOfSamples the number of references added
     * @param sampleRefs the references add, there must be at least as many references in the vector
     *  as numOfSamples (there may be more).
     * @return the time where the reference was inserted
     * @throws quicktime.std.StdQTException
     */
    public final int addSampleReference(SampleDescription sampleDescription, int numOfSamples, SampleReferenceVector sampleRefs) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = AddMediaSampleReferences(_ID(), QTObject.ID(sampleDescription), numOfSamples, QTObject.ID(sampleRefs), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method  allows your application to obtain reference information about samples that are stored in a movie data file.
     * @see "QuickTime::GetMediaSampleReferences()"
     * @param time secifies the starting time of the sample reference to be retrieved.
     * @param maxNumberOfSamples Specifies the maximum number of samples to be returned.
     * @return a SampleReferenceInfo object.
     * @throws quicktime.QTException
     */
    public final SampleReferenceInfo getSampleReferences(int time, int maxNumberOfSamples) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        SampleDescription localSampleDescription = makeDescription();
        SampleReferenceVector localSampleReferenceVector = new SampleReferenceVector(maxNumberOfSamples);
        int i = QTObject.ID(localSampleReferenceVector);
        int j = GetMediaSampleReferences(_ID(), time, arrayOfInt1, QTObject.ID(localSampleDescription), arrayOfInt2, maxNumberOfSamples, arrayOfInt3, i);
        StdQTException.checkError(j);
        localSampleReferenceVector._setNumSamples(arrayOfInt3[0]);
        return new SampleReferenceInfo(arrayOfInt1[0], localSampleDescription, arrayOfInt2[0], localSampleReferenceVector);
    }

    /**
     * This method allows your application to specify a preferred chunk size for the media.
     * @see "QuickTime::SetMediaPreferredChunkSize()"
     * @param maxChunkSize Specifies the prefered maximum chunk size.
     * @throws quicktime.std.StdQTException
     */
    public final void setPreferredChunkSize(int maxChunkSize) throws StdQTException {
        StdQTException.checkError(SetMediaPreferredChunkSize(_ID(), maxChunkSize));
    }

    /**
     * This method returns the preferred chunck size for the media.
     * @see "QuickTime::GetMediaPreferredChunkSize()"
     * @return the preferred chunck size for the media.
     * @throws quicktime.std.StdQTException
     */
    public final int getPreferredChunkSize() throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaPreferredChunkSize(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method creates an association between the indicated frame difference sample
     * and a specified self-contained sample in a given media.
     * @see "QuickTime::SetMediaShadowSync()"
     * @param frameDiffSampleNum Specifies a frame difference sample.
     * @param syncSampleNum Specifies a shadow sync sample.
     * @throws quicktime.std.StdQTException
     */
    public final void setShadowSync(int frameDiffSampleNum, int syncSampleNum) throws StdQTException {
        StdQTException.checkError(SetMediaShadowSync(_ID(), frameDiffSampleNum, syncSampleNum));
    }

    /**
     * This method returns the sample number of the shadow sync associated with a given frame difference sample number.
     * @see "QuickTime::GetMediaShadowSync()"
     * @return the sample number of the shadow sync.
     * @throws quicktime.std.StdQTException
     */
    public final int getShadowSync(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaShadowSync(_ID(), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * This method searches for times of interest in a media.
     * @see "QuickTime::GetMediaNextInterestingTime()"
     * @param interestingTimeFlags Specifies the search criteria.
     * @param time Specifies a time value that establishes the starting point for the search.
     * @param rate Contains the search direction.
     * @return a TimeInfo object that specifies the first found interesting time.
     * @throws quicktime.std.StdQTException
     */
    public final TimeInfo getNextInterestingTime(int interestingTimeFlags, int time, float rate) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetMediaNextInterestingTime(_ID(), (short) interestingTimeFlags, time, QTUtils.X2Fix(rate), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method allows your application to determine the size, in bytes, of the sample data in a media segment.
     * @see "QuickTime::GetMediaDataSize()"
     * @param startTime Contains a time value specifying the starting point of the segment.
     * @param duration Contains a time value that specifies the duration of the segment.
     * @return the data size.
     * @throws quicktime.std.StdQTException
     */
    public final int getDataSize(int startTime, int duration) throws StdQTException {
        int i = GetMediaDataSize(_ID(), startTime, duration);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a copy of a specified data reference.
     * @see "QuickTime::GetMediaDataRef()"
     * @param index Identifies the data reference.
     * @return a copy of a specified data reference.
     * @throws quicktime.QTException
     */
    public final DataRef getDataRef(int index) throws QTException {
        return DataRef.fromMedia(this, index);
    }

    /**
     * This method changes the file that the specified media identifies as the location for its data storage.
     * @see "QuickTime::SetMediaDataRef()"
     * @param index the index value at which to add the data.
     * @throws quicktime.std.StdQTException
     */
    public final void setDataRef(int paramInt, DataRef paramDataRef) throws StdQTException {
        StdQTException.checkError(SetMediaDataRef(_ID(), (short) paramInt, QTObject.ID(paramDataRef), paramDataRef.getType()));
    }

    /**
     * No QTDocumentation.
     * @see "QuickTime::SetMediaDataRefAttributes()"
     */
    public final void setDataRefAttributes(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(SetMediaDataRefAttributes(_ID(), (short) paramInt1, paramInt2));
    }

    /**
     * This method adds a data reference to a media.
     * @see "QuickTime::AddMediaDataRef()"
     * @param dataReference Specifies the data reference.
     * @return the index value that is assigned to the new data reference.
     * @throws quicktime.std.StdQTException
     */
    public final int addDataRef(DataRef dataReference) throws StdQTException {
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(AddMediaDataRef(_ID(), arrayOfShort, QTObject.ID(dataReference), dataReference.getType()));
        return arrayOfShort[0];
    }

    /**
     * This method allows your application to determine the number of data references in a media.
     * @see "QuickTime::GetMediaDataRefCount()"
     * @return the number of data references in the media.
     * @throws quicktime.std.StdQTException
     */
    public final int getDataRefCount() throws StdQTException {
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(GetMediaDataRefCount(_ID(), arrayOfShort));
        return arrayOfShort[0];
    }

    /**
     * This method allows your application to provide information to the Movie Toolbox
     * that can influence playback of a single media.
     * @see "QuickTime::SetMediaPlayHints()"
     * @param flags Specifies the optimizations that can be used with this media.
     * @param flagsMask Indicates which flags in the flags parameter are to be considered in this operation.
     * @throws quicktime.std.StdQTException
     */
    public final void setPlayHints(int flags, int flagsMask) throws StdQTException {
        SetMediaPlayHints(_ID(), flags, flagsMask);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * @deprecated
     * <I>these arguments are not used in this call see getPlayHints()</I>
     * This method allows your application to get information from the Movie Toolbox
     * that determines the current state of playback of a single media.
     * @see "QuickTime::GetMediaPlayHints()"
     * @param flags Specifies the optimizations that can be used with this media.
     * @param flagsMask Indicates which flags in the flags parameter are to be considered in this operation.
     * @throws quicktime.std.StdQTException
     */
    public final int getPlayHints(int flags, int flagsMask) throws StdQTException {
        int[] arrayOfInt = { 0 };
        GetMediaPlayHints(_ID(), arrayOfInt);
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    /**
     * This method allows your application to get information from the Movie Toolbox
     * that determines the current state of playback of a single media.
     * @see "QuickTime::GetMediaPlayHints()"
     */
    public final int getPlayHints() throws StdQTException {
        int[] arrayOfInt = { 0 };
        GetMediaPlayHints(_ID(), arrayOfInt);
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::SetMediaPropertyAtom()"
     */
    public final void setPropertyAtom(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SetMediaPropertyAtom(_ID(), QTObject.ID(paramAtomContainer)));
    }

    /**
     * Retrieves the properties from a media handler.
     * @see "QuickTime::GetMediaPropertyAtom()"
     * @return Contains the property atoms for the track associated with the media handler.
     * @throws quicktime.std.StdQTException
     */
    public final AtomContainer getPropertyAtom() throws StdQTException {
        return AtomContainer.fromMediaProperty(this);
    }

    /**
     * This method loads a media's data into memory.
     * @see "QuickTime::LoadMediaIntoRam()"
     * @param time starting time of the media segment to load.
     * @param duration duration of the media segment to load
     * @param flags control the loading operation
     * @throws quicktime.std.StdQTException
     */
    public final void loadIntoRam(int time, int duration, int flags) throws StdQTException {
        StdQTException.checkError(LoadMediaIntoRam(_ID(), time, duration, flags));
    }

    /**
     * Copies the user data in the media to the destination media
     * @see "QuickTime::CopyMediaUserData()"
     * @param destMedia destination media for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @throws quicktime.std.StdQTException
     */
    public void copyUserDataToMedia(Media destMedia, boolean replace) throws StdQTException {
        StdQTException.checkError(CopyMediaUserData(_ID(), QTObject.ID(destMedia), replace == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified media into this media
     * @see "QuickTime::CopyMediaUserData()"
     * @param srcMedia source media for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @throws quicktime.std.StdQTException
     */
    public void copyUserDataFromMedia(Media srcMedia, boolean replace) throws StdQTException {
        StdQTException.checkError(CopyMediaUserData(QTObject.ID(srcMedia), _ID(), replace == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified media into the destination media
     * @see "QuickTime::CopyMediaUserData()"
     * @param srcMedia source media for user data
     * @param destMedia destination media for user data
     * @param copyRule constant for merge or replace
     *     use StdQTConstants5_1.kQTCopyUserDataReplace or StdQTConstants5_1.kQTCopyUserDataMerge
     * @throws quicktime.std.StdQTException
     */
    public static void copyUserData(Media srcMedia, Media destMedia, int copyRule) throws StdQTException {
        StdQTException.checkError(CopyMediaUserData(QTObject.ID(srcMedia), QTObject.ID(destMedia), copyRule));
    }

    abstract SampleDescription makeDescription() throws QTException;

    abstract MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException;

    private static int NewTrackMedia(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MovieToolbox.NewTrackMedia(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static void GetMediaHandlerDescription(int paramInt, int[] paramArrayOfInt1, byte[] paramArrayOfByte, int[] paramArrayOfInt2) {
        MovieToolbox.GetMediaHandlerDescription(paramInt, paramArrayOfInt1, paramArrayOfByte, paramArrayOfInt2);
    }

    private static int GetTrackMedia(int paramInt) {
        return MovieToolbox.GetTrackMedia(paramInt);
    }

    private static int GetMediaHandler(int paramInt) {
        return MovieToolbox.GetMediaHandler(paramInt);
    }

    private static void GetMediaSampleDescription(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.GetMediaSampleDescription(paramInt1, paramInt2, paramInt3);
    }

    private static short SetMediaSampleDescription(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.SetMediaSampleDescription(paramInt1, paramInt2, paramInt3);
    }

    private static int GetMediaDataHandler(int paramInt, short paramShort) {
        return MovieToolbox.GetMediaDataHandler(paramInt, paramShort);
    }

    private static short SetMediaDataHandler(int paramInt1, short paramShort, int paramInt2) {
        return MovieToolbox.SetMediaDataHandler(paramInt1, paramShort, paramInt2);
    }

    private static short SetMediaHandler(int paramInt1, int paramInt2) {
        return MovieToolbox.SetMediaHandler(paramInt1, paramInt2);
    }

    private static int GetMediaCreationTime(int paramInt) {
        return MovieToolbox.GetMediaCreationTime(paramInt);
    }

    private static int GetMediaModificationTime(int paramInt) {
        return MovieToolbox.GetMediaModificationTime(paramInt);
    }

    private static int GetMediaTimeScale(int paramInt) {
        return MovieToolbox.GetMediaTimeScale(paramInt);
    }

    private static void SetMediaTimeScale(int paramInt1, int paramInt2) {
        MovieToolbox.SetMediaTimeScale(paramInt1, paramInt2);
    }

    private static int GetMediaDuration(int paramInt) {
        return MovieToolbox.GetMediaDuration(paramInt);
    }

    private static short GetMediaLanguage(int paramInt) {
        return MovieToolbox.GetMediaLanguage(paramInt);
    }

    private static void SetMediaLanguage(int paramInt, short paramShort) {
        MovieToolbox.SetMediaLanguage(paramInt, paramShort);
    }

    private static short GetMediaQuality(int paramInt) {
        return MovieToolbox.GetMediaQuality(paramInt);
    }

    private static void SetMediaQuality(int paramInt, short paramShort) {
        MovieToolbox.SetMediaQuality(paramInt, paramShort);
    }

    private static short SetMediaInputMap(int paramInt1, int paramInt2) {
        return MovieToolbox.SetMediaInputMap(paramInt1, paramInt2);
    }

    private static short BeginMediaEdits(int paramInt) {
        return MovieToolbox.BeginMediaEdits(paramInt);
    }

    private static short EndMediaEdits(int paramInt) {
        return MovieToolbox.EndMediaEdits(paramInt);
    }

    private static short SetMediaDefaultDataRefIndex(int paramInt, short paramShort) {
        return MovieToolbox.SetMediaDefaultDataRefIndex(paramInt, paramShort);
    }

    private static void GetMediaDataHandlerDescription(int paramInt, short paramShort, int[] paramArrayOfInt1, byte[] paramArrayOfByte, int[] paramArrayOfInt2) {
        MovieToolbox.GetMediaDataHandlerDescription(paramInt, paramShort, paramArrayOfInt1, paramArrayOfByte, paramArrayOfInt2);
    }

    private static int GetMediaSampleDescriptionCount(int paramInt) {
        return MovieToolbox.GetMediaSampleDescriptionCount(paramInt);
    }

    private static int GetMediaSampleCount(int paramInt) {
        return MovieToolbox.GetMediaSampleCount(paramInt);
    }

    private static int GetMediaSyncSampleCount(int paramInt) {
        return MovieToolbox.GetMediaSyncSampleCount(paramInt);
    }

    private static void SampleNumToMediaTime(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        MovieToolbox.SampleNumToMediaTime(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void MediaTimeToSampleNum(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        MovieToolbox.MediaTimeToSampleNum(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }

    private static short AddMediaSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short paramShort, int[] paramArrayOfInt) {
        return MovieToolbox.AddMediaSample(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramShort, paramArrayOfInt);
    }

    private static short GetMediaSample(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int paramInt4, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int paramInt5, int[] paramArrayOfInt4, int paramInt6, int[] paramArrayOfInt5, short[] paramArrayOfShort) {
        return MovieToolbox.GetMediaSample(paramInt1, paramInt2, paramInt3, paramArrayOfInt1, paramInt4, paramArrayOfInt2, paramArrayOfInt3, paramInt5, paramArrayOfInt4, paramInt6, paramArrayOfInt5, paramArrayOfShort);
    }

    private static short AddMediaSampleReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, short paramShort, int[] paramArrayOfInt) {
        return MovieToolbox.AddMediaSampleReference(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramShort, paramArrayOfInt);
    }

    private static short GetMediaSampleReference(int paramInt1, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int paramInt3, int[] paramArrayOfInt5, int paramInt4, int[] paramArrayOfInt6, short[] paramArrayOfShort) {
        return MovieToolbox.GetMediaSampleReference(paramInt1, paramArrayOfInt1, paramArrayOfInt2, paramInt2, paramArrayOfInt3, paramArrayOfInt4, paramInt3, paramArrayOfInt5, paramInt4, paramArrayOfInt6, paramArrayOfShort);
    }

    private static short AddMediaSampleReferences(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return MovieToolbox.AddMediaSampleReferences(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static short GetMediaSampleReferences(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, int[] paramArrayOfInt3, int paramInt5) {
        return MovieToolbox.GetMediaSampleReferences(paramInt1, paramInt2, paramArrayOfInt1, paramInt3, paramArrayOfInt2, paramInt4, paramArrayOfInt3, paramInt5);
    }

    private static short SetMediaPreferredChunkSize(int paramInt1, int paramInt2) {
        return MovieToolbox.SetMediaPreferredChunkSize(paramInt1, paramInt2);
    }

    private static short GetMediaPreferredChunkSize(int paramInt, int[] paramArrayOfInt) {
        return MovieToolbox.GetMediaPreferredChunkSize(paramInt, paramArrayOfInt);
    }

    private static short SetMediaShadowSync(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.SetMediaShadowSync(paramInt1, paramInt2, paramInt3);
    }

    private static short GetMediaShadowSync(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MovieToolbox.GetMediaShadowSync(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static void GetMediaNextInterestingTime(int paramInt1, short paramShort, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        MovieToolbox.GetMediaNextInterestingTime(paramInt1, paramShort, paramInt2, paramInt3, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GetMediaDataSize(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.GetMediaDataSize(paramInt1, paramInt2, paramInt3);
    }

    private static short SetMediaDataRef(int paramInt1, short paramShort, int paramInt2, int paramInt3) {
        return MovieToolbox.SetMediaDataRef(paramInt1, paramShort, paramInt2, paramInt3);
    }

    private static short SetMediaDataRefAttributes(int paramInt1, short paramShort, int paramInt2) {
        return MovieToolbox.SetMediaDataRefAttributes(paramInt1, paramShort, paramInt2);
    }

    private static short AddMediaDataRef(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3) {
        return MovieToolbox.AddMediaDataRef(paramInt1, paramArrayOfShort, paramInt2, paramInt3);
    }

    private static short GetMediaDataRefCount(int paramInt, short[] paramArrayOfShort) {
        return MovieToolbox.GetMediaDataRefCount(paramInt, paramArrayOfShort);
    }

    private static void SetMediaPlayHints(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMediaPlayHints(paramInt1, paramInt2, paramInt3);
    }

    private static void GetMediaPlayHints(int paramInt, int[] paramArrayOfInt) {
        MovieToolbox.GetMediaPlayHints(paramInt, paramArrayOfInt);
    }

    private static short SetMediaPropertyAtom(int paramInt1, int paramInt2) {
        return MovieToolbox.SetMediaPropertyAtom(paramInt1, paramInt2);
    }

    private static short LoadMediaIntoRam(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieToolbox.LoadMediaIntoRam(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short CopyMediaUserData(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.CopyMediaUserData(paramInt1, paramInt2, paramInt3);
    }
}

