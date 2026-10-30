package quicktime.std.sg;

/**
 * The SGDataSourceInfo class contains information on an SGChannel Data Source.
 */
public class SGDataSourceInfo {

    public final String mName;

    public final int mScriptTag;

    SGDataSourceInfo(String name, int scriptTag) {
        this.mName = name;
        this.mScriptTag = scriptTag;
    }
}
