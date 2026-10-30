package quicktime.std.image;

import quicktime.io.QTFile;

/**
 * The GraphicsImporterInfo class contains information about the reference information of a media sample.
 */
public class GraphicsImporterInfo {

    /**
     * file type that was chosen for export.
     */
    public final int mOutExportedType;

    /**
     * the file that was written..
     */
    public final QTFile mOutExportedSpec;

    /**
     * the script system in which the exported file name is to be displayed.
     */
    public final short mOutScriptTag;

    public GraphicsImporterInfo(int outExportedType, QTFile outExportedSpec, short outScriptTag) {
        this.mOutExportedType = outExportedType;
        this.mOutExportedSpec = outExportedSpec;
        this.mOutScriptTag = outScriptTag;
    }
}
