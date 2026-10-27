package quicktime.std.image;

import quicktime.io.QTFile;

/**
 * The GraphicsImporterInfo class contains information about the reference information of a media sample.
 */
public class GraphicsImporterInfo {

    /**
     * file type that was chosen for export.
     */
    public int mOutExportedType;

    /**
     * the file that was written..
     */
    public QTFile mOutExportedSpec;

    /**
     * the script system in which the exported file name is to be displayed.
     */
    public short mOutScriptTag;

    public GraphicsImporterInfo(int paramInt, QTFile paramQTFile, short paramShort) {
        this.mOutExportedType = paramInt;
        this.mOutExportedSpec = paramQTFile;
        this.mOutScriptTag = paramShort;
    }
}
