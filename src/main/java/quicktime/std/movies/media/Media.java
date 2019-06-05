
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

    private static Media fromGenericType(int paramInt1,
                                         Track paramTrack,
                                         int paramInt2,
                                         DataRef paramDataRef) throws QTException {
        int i = NewTrackMedia(QTObject.ID(paramTrack),
                              paramInt1,
                              paramInt2,
                              QTObject.ID(paramDataRef),
                              paramDataRef != null ? paramDataRef.getType() : 0);

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

        switch (arrayOfInt[0]) {
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

    public static Media newFromType(int paramInt1, Track paramTrack, int paramInt2, DataRef paramDataRef) throws QTException {
        if ((paramInt1 == 0) || (paramTrack == null)) {
            throw new QTException(-50);
        }
        switch (paramInt1) {
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

    public static Media fromTrack(Track paramTrack) throws QTException {
        return getTrackMedia(paramTrack);
    }

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
        int i = NewTrackMedia(QTObject.ID(paramTrack),
                              paramInt1,
                              paramInt2,
                              QTObject.ID(paramDataRef),
                              paramDataRef != null ? paramDataRef.getType() : 0);

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

    protected final void getNativeSampleDescription(int paramInt,
                                                    SampleDescription paramSampleDescription) throws StdQTException {
        GetMediaSampleDescription(_ID(), paramInt, QTObject.ID(paramSampleDescription));
        StdQTException.checkError(GetMoviesError());
    }

    public SampleDescription getSampleDescription(int paramInt) throws QTException {
        SampleDescription localSampleDescription = makeDescription();
        getNativeSampleDescription(paramInt, localSampleDescription);
        return localSampleDescription;
    }

    public final void setSampleDescription(int paramInt, SampleDescription paramSampleDescription) throws StdQTException {
        SetMediaSampleDescription(_ID(), paramInt, QTObject.ID(paramSampleDescription));
        StdQTException.checkError(GetMoviesError());
    }

    public final DataHandler getDataHandler(int paramInt) throws StdQTException {
        int i = GetMediaDataHandler(_ID(), (short) paramInt);
        StdQTException.checkError(GetMoviesError());
        return new DataHandler(i, this);
    }

    public final void setDataHandler(int paramInt, DataHandler paramDataHandler) throws StdQTException {
        StdQTException.checkError(SetMediaDataHandler(_ID(), (short) paramInt, QTObject.ID(paramDataHandler)));
    }

    public MediaHandler getHandler() throws StdQTException {
        return makeHandler(getNativeMediaHandler(), this);
    }

    public final void setHandler(MediaHandler paramMediaHandler) throws StdQTException {
        StdQTException.checkError(SetMediaHandler(_ID(), QTObject.ID(paramMediaHandler)));
    }

    public final Track getTrack() throws QTException {
        return Track.fromMedia(this);
    }

    public final int getCreationTime() throws StdQTException {
        int i = GetMediaCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final int getModificationTime() throws StdQTException {
        int i = GetMediaModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final int getTimeScale() throws StdQTException {
        int i = GetMediaTimeScale(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final void setTimeScale(int paramInt) throws StdQTException {
        SetMediaTimeScale(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public final int getDuration() throws StdQTException {
        int i = GetMediaDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final int getLanguage() throws StdQTException {
        int i = GetMediaLanguage(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final void setLanguage(int paramInt) throws StdQTException {
        SetMediaLanguage(_ID(), (short) paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public final int getQuality() throws StdQTException {
        int i = GetMediaQuality(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final void setQuality(int paramInt) throws StdQTException {
        SetMediaQuality(_ID(), (short) paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public final HandlerInfo getHandlerDescription() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        byte[] arrayOfByte = new byte[255];
        int[] arrayOfInt2 = new int[1];
        GetMediaHandlerDescription(_ID(), arrayOfInt1, arrayOfByte, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new HandlerInfo(arrayOfInt1[0], new String(arrayOfByte, 1, arrayOfByte[0]), arrayOfInt2[0]);
    }

    public final UserData getUserData() throws StdQTException {
        return UserData.fromMedia(this);
    }

    public final AtomContainer getInputMap() throws StdQTException {
        return AtomContainer.fromMediaInput(this);
    }

    public final void setInputMap(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SetMediaInputMap(_ID(), QTObject.ID(paramAtomContainer)));
    }

    public final void beginEdits() throws StdQTException {
        StdQTException.checkError(BeginMediaEdits(_ID()));
    }

    public final void endEdits() throws StdQTException {
        StdQTException.checkError(EndMediaEdits(_ID()));
    }

    public final void setDefaultDataRefIndex(int paramInt) throws StdQTException {
        StdQTException.checkError(SetMediaDefaultDataRefIndex(_ID(), (short) paramInt));
    }

    public final HandlerInfo getDataHandlerDescription(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        byte[] arrayOfByte = new byte[255];
        int[] arrayOfInt2 = new int[1];
        GetMediaDataHandlerDescription(_ID(), (short) paramInt, arrayOfInt1, arrayOfByte, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new HandlerInfo(arrayOfInt1[0], new String(arrayOfByte, 1, arrayOfByte[0]), arrayOfInt2[0]);
    }

    public final int getSampleDescriptionCount() throws StdQTException {
        int i = GetMediaSampleDescriptionCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final int getSampleCount() throws StdQTException {
        int i = GetMediaSampleCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final int getSyncSampleCount() throws StdQTException {
        int i = GetMediaSyncSampleCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final TimeInfo sampleNumToMediaTime(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];

        SampleNumToMediaTime(_ID(), paramInt, arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());

        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    public final SampleTimeInfo timeToSampleNum(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];

        MediaTimeToSampleNum(_ID(), paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(GetMoviesError());

        return new SampleTimeInfo(arrayOfInt2[0], arrayOfInt3[0], arrayOfInt1[0]);
    }

    public final int addSample(QTHandleRef paramQTHandleRef,
                               int paramInt1,
                               int paramInt2,
                               int paramInt3,
                               SampleDescription paramSampleDescription,
                               int paramInt4,
                               int paramInt5) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException
                .checkError(AddMediaSample(_ID(),
                                           QTObject.ID(paramQTHandleRef),
                                           paramInt1,
                                           paramInt2,
                                           paramInt3,
                                           QTObject.ID(paramSampleDescription),
                                           paramInt4,
                                           (short) paramInt5,
                                           arrayOfInt));

        return arrayOfInt[0];
    }

    public final MediaSample getSample(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        SampleDescription localSampleDescription = makeDescription();
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        int[] arrayOfInt5 = new int[1];
        short[] arrayOfShort = new short[1];

        StdQTException.checkError(GetMediaSample(_ID(),
                                                 QTObject.ID(localQTHandle),
                                                 paramInt1,
                                                 arrayOfInt1,
                                                 paramInt2,
                                                 arrayOfInt2,
                                                 arrayOfInt3,
                                                 QTObject.ID(localSampleDescription),
                                                 arrayOfInt4,
                                                 paramInt3,
                                                 arrayOfInt5,
                                                 arrayOfShort));

        return new MediaSample(arrayOfInt2[0],
                               arrayOfInt3[0],
                               localQTHandle,
                               0,
                               arrayOfInt1[0],
                               arrayOfInt5[0],
                               localSampleDescription,
                               arrayOfInt4[0],
                               arrayOfShort[0]);
    }

    public final int addSampleReference(int paramInt1,
                                        int paramInt2,
                                        int paramInt3,
                                        SampleDescription paramSampleDescription,
                                        int paramInt4,
                                        int paramInt5) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(AddMediaSampleReference(_ID(),
                                                          paramInt1,
                                                          paramInt2,
                                                          paramInt3,
                                                          QTObject.ID(paramSampleDescription),
                                                          paramInt4,
                                                          (short) paramInt5,
                                                          arrayOfInt));

        return arrayOfInt[0];
    }

    public final SampleInfo getSampleReference(int paramInt1, int paramInt2) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        SampleDescription localSampleDescription = makeDescription();
        int[] arrayOfInt5 = new int[1];
        int[] arrayOfInt6 = new int[1];
        short[] arrayOfShort = new short[1];

        StdQTException.checkError(GetMediaSampleReference(_ID(),
                                                          arrayOfInt1,
                                                          arrayOfInt2,
                                                          paramInt1,
                                                          arrayOfInt3,
                                                          arrayOfInt4,
                                                          QTObject.ID(localSampleDescription),
                                                          arrayOfInt5,
                                                          paramInt2,
                                                          arrayOfInt6,
                                                          arrayOfShort));

        return new SampleInfo(arrayOfInt3[0],
                              arrayOfInt4[0],
                              arrayOfInt1[0],
                              arrayOfInt2[0],
                              arrayOfInt6[0],
                              localSampleDescription,
                              arrayOfInt5[0],
                              arrayOfShort[0]);
    }

    public final int addSampleReference(SampleDescription paramSampleDescription,
                                        int paramInt,
                                        SampleReferenceVector paramSampleReferenceVector) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = AddMediaSampleReferences(_ID(),
                                         QTObject.ID(paramSampleDescription),
                                         paramInt,
                                         QTObject.ID(paramSampleReferenceVector),
                                         arrayOfInt);

        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public final SampleReferenceInfo getSampleReferences(int paramInt1, int paramInt2) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        SampleDescription localSampleDescription = makeDescription();
        SampleReferenceVector localSampleReferenceVector = new SampleReferenceVector(paramInt2);
        int i = QTObject.ID(localSampleReferenceVector);
        int j = GetMediaSampleReferences(_ID(),
                                         paramInt1,
                                         arrayOfInt1,
                                         QTObject.ID(localSampleDescription),
                                         arrayOfInt2,
                                         paramInt2,
                                         arrayOfInt3,
                                         i);

        StdQTException.checkError(j);
        localSampleReferenceVector._setNumSamples(arrayOfInt3[0]);
        return new SampleReferenceInfo(arrayOfInt1[0], localSampleDescription, arrayOfInt2[0], localSampleReferenceVector);
    }

    public final void setPreferredChunkSize(int paramInt) throws StdQTException {
        StdQTException.checkError(SetMediaPreferredChunkSize(_ID(), paramInt));
    }

    public final int getPreferredChunkSize() throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaPreferredChunkSize(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    public final void setShadowSync(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(SetMediaShadowSync(_ID(), paramInt1, paramInt2));
    }

    public final int getShadowSync(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaShadowSync(_ID(), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    public final TimeInfo getNextInterestingTime(int paramInt1, int paramInt2, float paramFloat) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];

        GetMediaNextInterestingTime(_ID(), (short) paramInt1, paramInt2, QTUtils.X2Fix(paramFloat), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());

        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    public final int getDataSize(int paramInt1, int paramInt2) throws StdQTException {
        int i = GetMediaDataSize(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final DataRef getDataRef(int paramInt) throws QTException {
        return DataRef.fromMedia(this, paramInt);
    }

    public final void setDataRef(int paramInt, DataRef paramDataRef) throws StdQTException {
        StdQTException.checkError(SetMediaDataRef(_ID(), (short) paramInt, QTObject.ID(paramDataRef), paramDataRef.getType()));
    }

    public final void setDataRefAttributes(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(SetMediaDataRefAttributes(_ID(), (short) paramInt1, paramInt2));
    }

    public final int addDataRef(DataRef paramDataRef) throws StdQTException {
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(AddMediaDataRef(_ID(), arrayOfShort, QTObject.ID(paramDataRef), paramDataRef.getType()));
        return arrayOfShort[0];
    }

    public final int getDataRefCount() throws StdQTException {
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(GetMediaDataRefCount(_ID(), arrayOfShort));
        return arrayOfShort[0];
    }

    public final void setPlayHints(int paramInt1, int paramInt2) throws StdQTException {
        SetMediaPlayHints(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * @deprecated
     */
    public final int getPlayHints(int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        GetMediaPlayHints(_ID(), arrayOfInt);
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    public final int getPlayHints() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        GetMediaPlayHints(_ID(), arrayOfInt);
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    public final void setPropertyAtom(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SetMediaPropertyAtom(_ID(), QTObject.ID(paramAtomContainer)));
    }

    public final AtomContainer getPropertyAtom() throws StdQTException {
        return AtomContainer.fromMediaProperty(this);
    }

    public final void loadIntoRam(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        StdQTException.checkError(LoadMediaIntoRam(_ID(), paramInt1, paramInt2, paramInt3));
    }

    public void copyUserDataToMedia(Media paramMedia, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(CopyMediaUserData(_ID(), QTObject.ID(paramMedia), paramBoolean == true ? 1919970403 : 1835361639));
    }

    public void copyUserDataFromMedia(Media paramMedia, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(CopyMediaUserData(QTObject.ID(paramMedia), _ID(), paramBoolean == true ? 1919970403 : 1835361639));
    }

    public static void copyUserData(Media paramMedia1, Media paramMedia2, int paramInt) throws StdQTException {
        StdQTException.checkError(CopyMediaUserData(QTObject.ID(paramMedia1), QTObject.ID(paramMedia2), paramInt));
    }

    abstract SampleDescription makeDescription() throws QTException;

    abstract MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException;

    private static native int NewTrackMedia(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short GetMoviesError();

    private static native void GetMediaHandlerDescription(int paramInt,
                                                          int[] paramArrayOfInt1,
                                                          byte[] paramArrayOfByte,
                                                          int[] paramArrayOfInt2);

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

    private static native void GetMediaDataHandlerDescription(int paramInt,
                                                              short paramShort,
                                                              int[] paramArrayOfInt1,
                                                              byte[] paramArrayOfByte,
                                                              int[] paramArrayOfInt2);

    private static native int GetMediaSampleDescriptionCount(int paramInt);

    private static native int GetMediaSampleCount(int paramInt);

    private static native int GetMediaSyncSampleCount(int paramInt);

    private static native void SampleNumToMediaTime(int paramInt1,
                                                    int paramInt2,
                                                    int[] paramArrayOfInt1,
                                                    int[] paramArrayOfInt2);

    private static native void MediaTimeToSampleNum(int paramInt1,
                                                    int paramInt2,
                                                    int[] paramArrayOfInt1,
                                                    int[] paramArrayOfInt2,
                                                    int[] paramArrayOfInt3);

    private static native short AddMediaSample(int paramInt1,
                                               int paramInt2,
                                               int paramInt3,
                                               int paramInt4,
                                               int paramInt5,
                                               int paramInt6,
                                               int paramInt7,
                                               short paramShort,
                                               int[] paramArrayOfInt);

    private static native short GetMediaSample(int paramInt1,
                                               int paramInt2,
                                               int paramInt3,
                                               int[] paramArrayOfInt1,
                                               int paramInt4,
                                               int[] paramArrayOfInt2,
                                               int[] paramArrayOfInt3,
                                               int paramInt5,
                                               int[] paramArrayOfInt4,
                                               int paramInt6,
                                               int[] paramArrayOfInt5,
                                               short[] paramArrayOfShort);

    private static native short AddMediaSampleReference(int paramInt1,
                                                        int paramInt2,
                                                        int paramInt3,
                                                        int paramInt4,
                                                        int paramInt5,
                                                        int paramInt6,
                                                        short paramShort,
                                                        int[] paramArrayOfInt);

    private static native short GetMediaSampleReference(int paramInt1,
                                                        int[] paramArrayOfInt1,
                                                        int[] paramArrayOfInt2,
                                                        int paramInt2,
                                                        int[] paramArrayOfInt3,
                                                        int[] paramArrayOfInt4,
                                                        int paramInt3,
                                                        int[] paramArrayOfInt5,
                                                        int paramInt4,
                                                        int[] paramArrayOfInt6,
                                                        short[] paramArrayOfShort);

    private static native short AddMediaSampleReferences(int paramInt1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         int paramInt4,
                                                         int[] paramArrayOfInt);

    private static native short GetMediaSampleReferences(int paramInt1,
                                                         int paramInt2,
                                                         int[] paramArrayOfInt1,
                                                         int paramInt3,
                                                         int[] paramArrayOfInt2,
                                                         int paramInt4,
                                                         int[] paramArrayOfInt3,
                                                         int paramInt5);

    private static native short SetMediaPreferredChunkSize(int paramInt1, int paramInt2);

    private static native short GetMediaPreferredChunkSize(int paramInt, int[] paramArrayOfInt);

    private static native short SetMediaShadowSync(int paramInt1, int paramInt2, int paramInt3);

    private static native short GetMediaShadowSync(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native void GetMediaNextInterestingTime(int paramInt1,
                                                           short paramShort,
                                                           int paramInt2,
                                                           int paramInt3,
                                                           int[] paramArrayOfInt1,
                                                           int[] paramArrayOfInt2);

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

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/Media.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
