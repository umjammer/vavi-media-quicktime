package quicktime.io;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.FileManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.movies.media.DataRef;
import quicktime.util.QTHandleRef;


/**
 * An alias handle represents a file - it maybe a minimal alias or an alias that
 * will attempt to be resolved when used.
 */
public final class AliasHandle extends QTHandleRef implements QuickTimeLib {

    private boolean minimal;

    static AliasHandle fromQTFile(QTFile qtFile, boolean minimal) throws QTException {
        int[] alias = { 0 };
        int i = QTNewAlias(qtFile.getFSSpec(true, 768), alias, (byte) (minimal ? 1 : 0));
        QTIOException.checkError(i, qtFile.getPath());
        return new AliasHandle(alias[0], null, false, minimal);
    }

    static AliasHandle fromTargetQTFile(QTFile qtFile1, QTFile qtFile2, boolean minimal) throws QTException {
        int[] alias = { 0 };
        int i = 0;
        if (qtFile1 != null) {
            i = NewAlias(qtFile1.getFSSpec(true, 768), qtFile2.getFSSpec(true, 768), alias);
        } else {
            i = NewAlias(null, qtFile2.getFSSpec(true, 768), alias);
        }
        QTIOException.checkError(i, qtFile2.getPath());
        return new AliasHandle(alias[0], null, false, minimal);
    }

    /**
     * Converts an aliasDataRef type DataRef into an AliasHandle. This does not
     * copy the alias DataRef so any changes that are made to the Alias will
     * affect the DataRef as well.
     * @return the AliasHandle
     * @throws quicktime.QTException
     */
    public static AliasHandle fromDataRef(DataRef nativeRep) throws QTException {
        if (nativeRep.getType() != 1634494835)
            throw new QTException(-50);
        return new AliasHandle(QTObject.ID(nativeRep), nativeRep, nativeRep.isLocked(), false);
    }

    /**
     * Creates an AliasHandle from the given byte array containing AliasHandle data
     * @param srcArray byte array containing AliasHandle data
     * @return AliasHandle object pointing to the data contained in srcArray
     * @throws quicktime.QTException
     */
    public static AliasHandle fromArray(byte[] srcArray) throws QTException {
        AliasHandle aliasHandle = new AliasHandle(srcArray.length, false);
        aliasHandle.copyFromArray(0, srcArray, 0, srcArray.length);
        return aliasHandle;
    }

    private AliasHandle(int size, boolean clear) throws QTException {
        super(size, clear);
    }

    private AliasHandle(int nativeRep, Object ref, boolean locked, boolean minimal) {
        super(nativeRep, ref, locked);
        this.minimal = minimal;
    }

    /**
     * Returns true if the alias is a minimal alias, otherwise false
     */
    public boolean isMinimal() {
        return this.minimal;
    }

    /**
     * This method will take an alias and will resolve it,
     * returning the full path name of the resolved file.
     * @see "QuickTime::ResolveAlias"
     * @return the QTFile object of the resolved alias
     * @throws quicktime.QTException
     */
    public QTFile resolve() throws QTException {
        return QTFile.resolveAlias(this);
    }

    private static short QTNewAlias(byte[] fsspec, int[] alias, byte minimal) {
        return FileManager.QTNewAlias(fsspec, alias, minimal);
    }

    private static short NewAlias(byte[] fromFile, byte[] target, int[] alias) {
        return FileManager.NewAlias(fromFile, target, alias);
    }
}
