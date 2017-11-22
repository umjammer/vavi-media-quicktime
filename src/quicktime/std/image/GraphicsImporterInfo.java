
package quicktime.std.image;

import quicktime.io.QTFile;


public class GraphicsImporterInfo {
    public int mOutExportedType;

    public QTFile mOutExportedSpec;

    public short mOutScriptTag;

    public GraphicsImporterInfo(int paramInt, QTFile paramQTFile, short paramShort) {
        this.mOutExportedType = paramInt;
        this.mOutExportedSpec = paramQTFile;
        this.mOutScriptTag = paramShort;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GraphicsImporterInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
