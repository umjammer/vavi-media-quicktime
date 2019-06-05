
package quicktime.std.music;

import quicktime.util.QTUtils;


public class MusicComponentInfo {
    public int synthType;

    public String name;

    public SynthesizerConnections connections;

    public MusicComponent mc;

    public String toString() {
        return getClass().getName() + "[snythType=" + QTUtils.fromOSType(this.synthType) + ",name=" + this.name
               + ",connections=" + this.connections + ",mc=" + this.mc.toString() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /MusicComponentInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
