package quicktime.std.movies;

/**
 * The IStringInfo class implements the International String structure of the Movie Toolbox.
 */
public final class IStringInfo {

    public final String string;

    public final int region;

    IStringInfo(String paramString, int paramInt) {
        this.string = paramString;
        this.region = paramInt;
    }
}
