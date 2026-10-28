package quicktime.std.movies.media;

import quicktime.util.QTUtils;

/**
 * HandlerInfo contains general information for Media and Data Handler components on the
 * type of information they handle and manufacturer's name and ID.
 */
public class HandlerInfo {

    /**
     * Specifies the type of media (subType of Media), or type of data handled (subType of DataHandler).
     */
    public final int subType;

    /**
     * The name of the handler's manufacturer.
     */
    public final String name;

    /**
     * ManufacturerID the ID of the handler's manufacturer.
     */
    public final int manufacturerID;

    /**
     * This constructor creates a HandlerInfo object.
     * @param subType specifies the type of media (subType of Media), or type of data handled (subType of DataHandler).
     * @param name the name of the handler's manufacturer.
     * @param manufacturerID the ID of the handler's manufacturer.
     */
    public HandlerInfo(int subType, String name, int manufacturerID) {
        this.subType = subType;
        this.name = name;
        this.manufacturerID = manufacturerID;
    }

    /**
     * @return a String representation of this object
     */
    public String toString() {
        return getClass().getName() + "[mediaType=" + QTUtils.fromOSType(this.subType) + ",manufacturerName=" + this.name + "]";
    }
}
