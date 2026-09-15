package quicktime.sound;

/**
 * The SoundCallBack class is used by the SndChannel object and can be specified
 * when the channel is allocated. Calling doCommand( ) with a callBackCmd will allow
 * your SoundCallBack to be called when the sound playing in the SndChannel is done
 * playing
 */
public abstract interface SoundCallBack {

    /**
     * This method should be implemented to handle commands that are generated
     * from the doCommand calls on the SndChannel.
     * @param sc the SndChannel the callback is called from
     */
    public abstract void execute(SndChannel paramSndChannel);
}

