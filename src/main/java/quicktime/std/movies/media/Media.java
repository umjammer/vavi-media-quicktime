package quicktime.std.movies.media;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MovieToolbox;
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

    private static final Hashtable<Integer, String> mediaObjects = new Hashtable<>();

    Track track;

    private static Media makeGenericMedia(int mediaType, int nativeRep, Track track) throws QTException {
        String str = mediaObjects.get(mediaType);
        try {
            GenericMedia instance = null;
            if (str == null) {
                instance = new GenericMedia(nativeRep);
            } else {
                Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(str);
                Class<?>[] classes = new Class[1];
                classes[0] = Class.forName("java.lang.Integer");
                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(classes);
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = nativeRep;
                instance = (GenericMedia) declaredConstructor.newInstance(arrayOfObject);
            }
            instance.setIDs(mediaType, track);
            return instance;
        } catch (Exception e) {
            throw new QTException(e.toString());
        }
    }

    private static Media fromGenericType(int mediaType, Track track, int timeScale, DataRef dataRef) throws QTException {
        int i = NewTrackMedia(QTObject.ID(track), mediaType, timeScale, QTObject.ID(dataRef), dataRef != null ? dataRef.getType() : 0);
        StdQTException.checkError(GetMoviesError());
        return makeGenericMedia(mediaType, i, track);
    }

    private static Media makeMedia(int media, Track track) throws QTException {
        if (media == 0) {
            return null;
        }
        int[] mediaType = new int[1];
        GetMediaHandlerDescription(media, mediaType, null, null);
        StdQTException.checkError(GetMoviesError());
        return switch (mediaType[0]) {
            case 1735291491 -> new BaseMedia(media, track);
            case 1297106247 -> new MPEGMedia(media, track);
            case 1836413801 -> new MusicMedia(media, track);
            case 1936684398 -> new SoundMedia(media, track);
            case 1936749172 -> new SpriteMedia(media, track);
            case 1952807028 -> new TextMedia(media, track);
            case 1902392164 -> new ThreeDMedia(media, track);
            case 1953325924 -> new TimeCodeMedia(media, track);
            case 1953981806 -> new TweenMedia(media, track);
            case 1986618469 -> new VideoMedia(media, track);
            case 1718383464 -> new FlashMedia(media, track);
            case 1937011309 -> new StreamMedia(media, track);
            case 1836019574 -> new MovieMedia(media, track);
            default -> makeGenericMedia(mediaType[0], media, track);
        };
    }

    protected static void addMediaType(int type, String className) {
        mediaObjects.put(type, className);
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
    public static Media newFromType(int mediaType, Track itsTrack, int timeScale, DataRef dataRef) throws QTException {
        if ((mediaType == 0) || (itsTrack == null)) {
            throw new QTException(-50);
        }
        return switch (mediaType) {
            case 1735291491 -> new BaseMedia(itsTrack, timeScale, dataRef);
            case 1297106247 -> new MPEGMedia(itsTrack, timeScale, dataRef);
            case 1836413801 -> new MusicMedia(itsTrack, timeScale, dataRef);
            case 1936684398 -> new SoundMedia(itsTrack, timeScale, dataRef);
            case 1936749172 -> new SpriteMedia(itsTrack, timeScale, dataRef);
            case 1952807028 -> new TextMedia(itsTrack, timeScale, dataRef);
            case 1902392164 -> new ThreeDMedia(itsTrack, timeScale, dataRef);
            case 1953325924 -> new TimeCodeMedia(itsTrack, timeScale, dataRef);
            case 1953981806 -> new TweenMedia(itsTrack, timeScale, dataRef);
            case 1986618469 -> new VideoMedia(itsTrack, timeScale, dataRef);
            case 1718383464 -> new FlashMedia(itsTrack, timeScale, dataRef);
            case 1937011309 -> new StreamMedia(itsTrack, timeScale, dataRef);
            case 1836019574 -> new MovieMedia(itsTrack, timeScale, dataRef);
            default -> fromGenericType(mediaType, itsTrack, timeScale, dataRef);
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

    protected Media(Track track, int mediaType, int timeScale, DataRef dataRef) throws QTException {
        super(allocate(track, mediaType, timeScale, dataRef));
        this.track = track;
    }

    private static int allocate(Track track, int mediaType, int timeScale, DataRef dataRef) throws StdQTException {
        int i = NewTrackMedia(QTObject.ID(track), mediaType, timeScale, QTObject.ID(dataRef), dataRef != null ? dataRef.getType() : 0);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    Media(Track track, int nativeRep) throws QTException {
        super(nativeRep);
        this.track = track;
    }

    protected final int getNativeMediaHandler() throws StdQTException {
        int i = GetMediaHandler(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    protected final void getNativeSampleDescription(int index, SampleDescription descH) throws StdQTException {
        GetMediaSampleDescription(_ID(), index, QTObject.ID(descH));
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
        SampleDescription descH = makeDescription();
        getNativeSampleDescription(index, descH);
        return descH;
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
    public final void setDataHandler(int index, DataHandler dataHandler) throws StdQTException {
        StdQTException.checkError(SetMediaDataHandler(_ID(), (short) index, QTObject.ID(dataHandler)));
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
        int[] mediaType = new int[1];
        byte[] creatorName = new byte[255];
        int[] creatorManufacturer = new int[1];
        GetMediaHandlerDescription(_ID(), mediaType, creatorName, creatorManufacturer);
        StdQTException.checkError(GetMoviesError());
        return new HandlerInfo(mediaType[0], new String(creatorName, 1, creatorName[0]), creatorManufacturer[0]);
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
        int[] dhType = new int[1];
        byte[] creatorName = new byte[255];
        int[] creatorManufacturer = new int[1];
        GetMediaDataHandlerDescription(_ID(), (short) index, dhType, creatorName, creatorManufacturer);
        StdQTException.checkError(GetMoviesError());
        return new HandlerInfo(dhType[0], new String(creatorName, 1, creatorName[0]), creatorManufacturer[0]);
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
        int[] sampleTime = new int[1];
        int[] sampleDuration = new int[1];
        SampleNumToMediaTime(_ID(), logicalSampleNum, sampleTime, sampleDuration);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(sampleTime[0], sampleDuration[0]);
    }

    /**
     * This method allows you to find the sample that contains the data for a specified time.
     * @see "QuickTime::MediaTimeToSampleNum()"
     * @param time Specifies the time for which you are retrieving sample information.
     * @return a SampleTimeInfo object that contains the sample time and sample duration for the specified sample.
     * @throws quicktime.std.StdQTException
     */
    public final SampleTimeInfo timeToSampleNum(int time) throws StdQTException {
        int[] sampleNum = new int[1];
        int[] sampleTime = new int[1];
        int[] sampleDuration = new int[1];
        MediaTimeToSampleNum(_ID(), time, sampleNum, sampleTime, sampleDuration);
        StdQTException.checkError(GetMoviesError());
        return new SampleTimeInfo(sampleTime[0], sampleDuration[0], sampleNum[0]);
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
        int[] sampleTime = new int[1];
        StdQTException.checkError(AddMediaSample(_ID(), QTObject.ID(data), dataOffset, dataSize, durationPerSample, QTObject.ID(sampleDesc), numberOfSamples, (short) sampleFlags, sampleTime));
        return sampleTime[0];
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
        QTHandle dataOut = new QTHandle();
        SampleDescription descH = makeDescription();
        int[] size = new int[1];
        int[] sampleTime = new int[1];
        int[] durationPerSample = new int[1];
        int[] descIndex = new int[1];
        int[] numberOfSamples = new int[1];
        short[] sampleFlags = new short[1];
        StdQTException.checkError(GetMediaSample(_ID(), QTObject.ID(dataOut), maxSizeToGrow, size, time, sampleTime, durationPerSample, QTObject.ID(descH), descIndex, maxNumberOfSamples, numberOfSamples, sampleFlags));
        return new MediaSample(sampleTime[0], durationPerSample[0], dataOut, 0, size[0], numberOfSamples[0], descH, descIndex[0], sampleFlags[0]);
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
        int[] sampleTime = new int[1];
        StdQTException.checkError(AddMediaSampleReference(_ID(), dataOffset, dataSize, durationPerSample, QTObject.ID(sampleDesc), numberOfSamples, (short) sampleFlags, sampleTime));
        return sampleTime[0];
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
        int[] dataOffset = new int[1];
        int[] size = new int[1];
        int[] sampleTime = new int[1];
        int[] durationPerSample = new int[1];
        SampleDescription descH = makeDescription();
        int[] descIndex = new int[1];
        int[] numberOfSamples = new int[1];
        short[] sampleFlags = new short[1];
        StdQTException.checkError(GetMediaSampleReference(_ID(), dataOffset, size, time, sampleTime, durationPerSample, QTObject.ID(descH), descIndex, maxNumberOfSamples, numberOfSamples, sampleFlags));
        return new SampleInfo(sampleTime[0], durationPerSample[0], dataOffset[0], size[0], numberOfSamples[0], descH, descIndex[0], sampleFlags[0]);
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
        int[] sampleTime = new int[1];
        int i = AddMediaSampleReferences(_ID(), QTObject.ID(sampleDescription), numOfSamples, QTObject.ID(sampleRefs), sampleTime);
        StdQTException.checkError(i);
        return sampleTime[0];
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
        int[] sampleTime = new int[1];
        int[] descIndex = new int[1];
        int[] actualNumberOfEntries = new int[1];
        SampleDescription descH = makeDescription();
        SampleReferenceVector sampleRefs = new SampleReferenceVector(maxNumberOfSamples);
        int i = QTObject.ID(sampleRefs);
        int j = GetMediaSampleReferences(_ID(), time, sampleTime, QTObject.ID(descH), descIndex, maxNumberOfSamples, actualNumberOfEntries, i);
        StdQTException.checkError(j);
        sampleRefs._setNumSamples(actualNumberOfEntries[0]);
        return new SampleReferenceInfo(sampleTime[0], descH, descIndex[0], sampleRefs);
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
        int[] maxChunkSize = new int[1];
        StdQTException.checkError(GetMediaPreferredChunkSize(_ID(), maxChunkSize));
        return maxChunkSize[0];
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
    public final int getShadowSync(int frameDiffSampleNum) throws StdQTException {
        int[] syncSampleNum = new int[1];
        StdQTException.checkError(GetMediaShadowSync(_ID(), frameDiffSampleNum, syncSampleNum));
        return syncSampleNum[0];
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
        int[] interestingTime = new int[1];
        int[] interestingDuration = new int[1];
        GetMediaNextInterestingTime(_ID(), (short) interestingTimeFlags, time, QTUtils.X2Fix(rate), interestingTime, interestingDuration);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(interestingTime[0], interestingDuration[0]);
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
    public final void setDataRef(int index, DataRef dataRef) throws StdQTException {
        StdQTException.checkError(SetMediaDataRef(_ID(), (short) index, QTObject.ID(dataRef), dataRef.getType()));
    }

    /**
     * No QTDocumentation.
     * @see "QuickTime::SetMediaDataRefAttributes()"
     */
    public final void setDataRefAttributes(int index, int attributes) throws StdQTException {
        StdQTException.checkError(SetMediaDataRefAttributes(_ID(), (short) index, attributes));
    }

    /**
     * This method adds a data reference to a media.
     * @see "QuickTime::AddMediaDataRef()"
     * @param dataReference Specifies the data reference.
     * @return the index value that is assigned to the new data reference.
     * @throws quicktime.std.StdQTException
     */
    public final int addDataRef(DataRef dataReference) throws StdQTException {
        short[] index = new short[1];
        StdQTException.checkError(AddMediaDataRef(_ID(), index, QTObject.ID(dataReference), dataReference.getType()));
        return index[0];
    }

    /**
     * This method allows your application to determine the number of data references in a media.
     * @see "QuickTime::GetMediaDataRefCount()"
     * @return the number of data references in the media.
     * @throws quicktime.std.StdQTException
     */
    public final int getDataRefCount() throws StdQTException {
        short[] count = new short[1];
        StdQTException.checkError(GetMediaDataRefCount(_ID(), count));
        return count[0];
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
        int[] flags = { 0 };
        GetMediaPlayHints(_ID(), flags);
        StdQTException.checkError(GetMoviesError());
        return flags[0];
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::SetMediaPropertyAtom()"
     */
    public final void setPropertyAtom(AtomContainer propertyAtom) throws StdQTException {
        StdQTException.checkError(SetMediaPropertyAtom(_ID(), QTObject.ID(propertyAtom)));
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

    abstract MediaHandler makeHandler(int nativeRep, Object ref) throws StdQTException;

    private static int NewTrackMedia(int track, int mediaType, int timeScale, int dataRef, int dataRefType) {
        return MovieToolbox.NewTrackMedia(track, mediaType, timeScale, dataRef, dataRefType);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static void GetMediaHandlerDescription(int media, int[] mediaType, byte[] creatorName, int[] creatorManufacturer) {
        MovieToolbox.GetMediaHandlerDescription(media, mediaType, creatorName, creatorManufacturer);
    }

    private static int GetTrackMedia(int track) {
        return MovieToolbox.GetTrackMedia(track);
    }

    private static int GetMediaHandler(int media) {
        return MovieToolbox.GetMediaHandler(media);
    }

    private static void GetMediaSampleDescription(int media, int index, int descH) {
        MovieToolbox.GetMediaSampleDescription(media, index, descH);
    }

    private static short SetMediaSampleDescription(int media, int index, int descH) {
        return MovieToolbox.SetMediaSampleDescription(media, index, descH);
    }

    private static int GetMediaDataHandler(int media, short index) {
        return MovieToolbox.GetMediaDataHandler(media, index);
    }

    private static short SetMediaDataHandler(int media, short index, int dataHandler) {
        return MovieToolbox.SetMediaDataHandler(media, index, dataHandler);
    }

    private static short SetMediaHandler(int media, int handler) {
        return MovieToolbox.SetMediaHandler(media, handler);
    }

    private static int GetMediaCreationTime(int media) {
        return MovieToolbox.GetMediaCreationTime(media);
    }

    private static int GetMediaModificationTime(int media) {
        return MovieToolbox.GetMediaModificationTime(media);
    }

    private static int GetMediaTimeScale(int media) {
        return MovieToolbox.GetMediaTimeScale(media);
    }

    private static void SetMediaTimeScale(int media, int scale) {
        MovieToolbox.SetMediaTimeScale(media, scale);
    }

    private static int GetMediaDuration(int media) {
        return MovieToolbox.GetMediaDuration(media);
    }

    private static short GetMediaLanguage(int media) {
        return MovieToolbox.GetMediaLanguage(media);
    }

    private static void SetMediaLanguage(int media, short language) {
        MovieToolbox.SetMediaLanguage(media, language);
    }

    private static short GetMediaQuality(int media) {
        return MovieToolbox.GetMediaQuality(media);
    }

    private static void SetMediaQuality(int media, short quality) {
        MovieToolbox.SetMediaQuality(media, quality);
    }

    private static short SetMediaInputMap(int media, int inputMap) {
        return MovieToolbox.SetMediaInputMap(media, inputMap);
    }

    private static short BeginMediaEdits(int media) {
        return MovieToolbox.BeginMediaEdits(media);
    }

    private static short EndMediaEdits(int media) {
        return MovieToolbox.EndMediaEdits(media);
    }

    private static short SetMediaDefaultDataRefIndex(int media, short index) {
        return MovieToolbox.SetMediaDefaultDataRefIndex(media, index);
    }

    private static void GetMediaDataHandlerDescription(int media, short index, int[] dhType, byte[] creatorName, int[] creatorManufacturer) {
        MovieToolbox.GetMediaDataHandlerDescription(media, index, dhType, creatorName, creatorManufacturer);
    }

    private static int GetMediaSampleDescriptionCount(int media) {
        return MovieToolbox.GetMediaSampleDescriptionCount(media);
    }

    private static int GetMediaSampleCount(int media) {
        return MovieToolbox.GetMediaSampleCount(media);
    }

    private static int GetMediaSyncSampleCount(int media) {
        return MovieToolbox.GetMediaSyncSampleCount(media);
    }

    private static void SampleNumToMediaTime(int media, int sampleNum, int[] sampleTime, int[] sampleDuration) {
        MovieToolbox.SampleNumToMediaTime(media, sampleNum, sampleTime, sampleDuration);
    }

    private static void MediaTimeToSampleNum(int media, int time, int[] sampleNum, int[] sampleTime, int[] sampleDuration) {
        MovieToolbox.MediaTimeToSampleNum(media, time, sampleNum, sampleTime, sampleDuration);
    }

    private static short AddMediaSample(int media, int dataIn, int inOffset, int size, int durationPerSample, int descH, int numberOfSamples, short sampleFlags, int[] sampleTime) {
        return MovieToolbox.AddMediaSample(media, dataIn, inOffset, size, durationPerSample, descH, numberOfSamples, sampleFlags, sampleTime);
    }

    private static short GetMediaSample(int media, int dataOut, int maxSizeToGrow, int[] size, int time, int[] sampleTime, int[] durationPerSample, int descH, int[] descIndex, int maxNumberOfSamples, int[] numberOfSamples, short[] sampleFlags) {
        return MovieToolbox.GetMediaSample(media, dataOut, maxSizeToGrow, size, time, sampleTime, durationPerSample, descH, descIndex, maxNumberOfSamples, numberOfSamples, sampleFlags);
    }

    private static short AddMediaSampleReference(int media, int dataOffset, int size, int durationPerSample, int descH, int numberOfSamples, short sampleFlags, int[] sampleTime) {
        return MovieToolbox.AddMediaSampleReference(media, dataOffset, size, durationPerSample, descH, numberOfSamples, sampleFlags, sampleTime);
    }

    private static short GetMediaSampleReference(int media, int[] dataOffset, int[] size, int time, int[] sampleTime, int[] durationPerSample, int descH, int[] descIndex, int maxNumberOfSamples, int[] numberOfSamples, short[] sampleFlags) {
        return MovieToolbox.GetMediaSampleReference(media, dataOffset, size, time, sampleTime, durationPerSample, descH, descIndex, maxNumberOfSamples, numberOfSamples, sampleFlags);
    }

    private static short AddMediaSampleReferences(int media, int descH, int numberOfRefs, int refs, int[] sampleTime) {
        return MovieToolbox.AddMediaSampleReferences(media, descH, numberOfRefs, refs, sampleTime);
    }

    private static short GetMediaSampleReferences(int media, int time, int[] sampleTime, int descH, int[] descIndex, int maxNumberOfEntries, int[] actualNumberOfEntries, int refs) {
        return MovieToolbox.GetMediaSampleReferences(media, time, sampleTime, descH, descIndex, maxNumberOfEntries, actualNumberOfEntries, refs);
    }

    private static short SetMediaPreferredChunkSize(int media, int maxChunkSize) {
        return MovieToolbox.SetMediaPreferredChunkSize(media, maxChunkSize);
    }

    private static short GetMediaPreferredChunkSize(int media, int[] maxChunkSize) {
        return MovieToolbox.GetMediaPreferredChunkSize(media, maxChunkSize);
    }

    private static short SetMediaShadowSync(int media, int frameDiffSampleNum, int syncSampleNum) {
        return MovieToolbox.SetMediaShadowSync(media, frameDiffSampleNum, syncSampleNum);
    }

    private static short GetMediaShadowSync(int media, int frameDiffSampleNum, int[] syncSampleNum) {
        return MovieToolbox.GetMediaShadowSync(media, frameDiffSampleNum, syncSampleNum);
    }

    private static void GetMediaNextInterestingTime(int media, short flags, int time, int rate, int[] interestingTime, int[] interestingDuration) {
        MovieToolbox.GetMediaNextInterestingTime(media, flags, time, rate, interestingTime, interestingDuration);
    }

    private static int GetMediaDataSize(int media, int time, int duration) {
        return MovieToolbox.GetMediaDataSize(media, time, duration);
    }

    private static short SetMediaDataRef(int media, short index, int dataRef, int dataRefType) {
        return MovieToolbox.SetMediaDataRef(media, index, dataRef, dataRefType);
    }

    private static short SetMediaDataRefAttributes(int media, short index, int attributes) {
        return MovieToolbox.SetMediaDataRefAttributes(media, index, attributes);
    }

    private static short AddMediaDataRef(int media, short[] index, int dataRef, int dataRefType) {
        return MovieToolbox.AddMediaDataRef(media, index, dataRef, dataRefType);
    }

    private static short GetMediaDataRefCount(int media, short[] count) {
        return MovieToolbox.GetMediaDataRefCount(media, count);
    }

    private static void SetMediaPlayHints(int media, int flags, int flagsMask) {
        MovieToolbox.SetMediaPlayHints(media, flags, flagsMask);
    }

    private static void GetMediaPlayHints(int media, int[] flags) {
        MovieToolbox.GetMediaPlayHints(media, flags);
    }

    private static short SetMediaPropertyAtom(int media, int propertyAtom) {
        return MovieToolbox.SetMediaPropertyAtom(media, propertyAtom);
    }

    private static short LoadMediaIntoRam(int media, int time, int duration, int flags) {
        return MovieToolbox.LoadMediaIntoRam(media, time, duration, flags);
    }

    private static short CopyMediaUserData(int src, int dst, int copyRule) {
        return MovieToolbox.CopyMediaUserData(src, dst, copyRule);
    }
}
