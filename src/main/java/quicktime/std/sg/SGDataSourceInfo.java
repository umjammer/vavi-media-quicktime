package quicktime.std.sg;

/**
 * The SGDataSourceInfo class contains information on an SGChannel Data Source.
 */
public class SGDataSourceInfo {

    public String mName;

    public int mScriptTag;

    SGDataSourceInfo(String paramString, int paramInt) {
        this.mName = paramString;
        this.mScriptTag = paramInt;
    }
}

