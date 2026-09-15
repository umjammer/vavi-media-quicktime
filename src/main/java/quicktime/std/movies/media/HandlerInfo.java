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
    public int subType;

    /**
     * The name of the handler's manufacturer.
     */
    public String name;

    /**
     * ManufacturerID the ID of the handler's manufacturer.
     */
    public int manufacturerID;

    /**
     * This constructor creates a HandlerInfo object.
     * @param subType specifies the type of media (subType of Media), or type of data handled (subType of DataHandler).
     * @param name the name of the handler's manufacturer.
     * @param manufacturerID the ID of the handler's manufacturer.
     */
    public HandlerInfo(int paramInt1, String paramString, int paramInt2) {
        this.subType = paramInt1;
        this.name = paramString;
        this.manufacturerID = paramInt2;
    }

    /**
     * @return a String representation of this object
     */
    public String toString() {
        return getClass().getName() + "[mediaType=" + QTUtils.fromOSType(this.subType) + ",manufacturerName=" + this.name + "]";
    }
}

