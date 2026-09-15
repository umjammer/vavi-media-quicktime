package quicktime.std.sg;

import quicktime.QTException;
import quicktime.util.QTPointerRef;

/**
 * The sequence grabber calls your data function whenever any channel component
 * writes data to the destination movie. You may use your data function to store the digitized data in some format other than a QuickTime movie.
 */
public abstract interface SGDataProc {

    /**
     * The method that is called by QuickTime to write the provided data.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     * The method returns information to QuickTime
     * supplying the value indicated by the writeType parameter - typically where you wrote the data (or zero if you didn't write data).
     * @param chan Identifies the channel component that is writing the digitized data. This will be
     *  a subclass of the SGChannel abstract class.
     * @param dataToWrite contains the digitized data
     * @param offset may specify where you are to write the digitized data
     * @param chRefCon contains control information - sample flags for use by the Movie ToolboxÕs AddMediaSample function
     * @param time Identifies the starting time of the data, in the channelÕs time scale. You can use the SGChannel.getTimeScale method to retrieve the channel's time scale.
     * @param writeType Indicates the type of write operation being performed.
     *
     *  seqGrabWriteAppendAppend the new data to the end of the file. Set the field referred to by the offset parameter to reflect the location at which you added the data.
     *  seqGrabWriteReserveDo not write any data to the output file. Instead, reserve space in the output file for the amount of data indicated by the len parameter. Set the field referred to by the offset parameter to the location of the reserved space.
     *  seqGrabWriteFillWrite the data into the location specified by the field referred to by the offset parameter. Set that field to the location of the byte following the last byte you wrote.
     * @return where you wrote the data or zero if you didn't write data.
     * @return {@link quicktime.QTException}
     */
    public abstract int execute(SGChannel paramSGChannel, QTPointerRef paramQTPointerRef, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws QTException;
}

