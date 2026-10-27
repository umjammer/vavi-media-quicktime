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

    static AliasHandle fromQTFile(QTFile paramQTFile, boolean paramBoolean) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = QTNewAlias(paramQTFile.getFSSpec(true, 768), arrayOfInt, (byte) (paramBoolean ? 1 : 0));
        QTIOException.checkError(i, paramQTFile.getPath());
        return new AliasHandle(arrayOfInt[0], null, false, paramBoolean);
    }

    static AliasHandle fromTargetQTFile(QTFile paramQTFile1, QTFile paramQTFile2, boolean paramBoolean) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        if (paramQTFile1 != null) {
            i = NewAlias(paramQTFile1.getFSSpec(true, 768), paramQTFile2.getFSSpec(true, 768), arrayOfInt);
        } else {
            i = NewAlias(null, paramQTFile2.getFSSpec(true, 768), arrayOfInt);
        }
        QTIOException.checkError(i, paramQTFile2.getPath());
        return new AliasHandle(arrayOfInt[0], null, false, paramBoolean);
    }

    /**
     * Converts an aliasDataRef type DataRef into an AliasHandle. This does not
     * copy the alias DataRef so any changes that are made to the Alias will
     * affect the DataRef as well.
     * @return the AliasHandle
     * @throws quicktime.QTException
     */
    public static AliasHandle fromDataRef(DataRef paramDataRef) throws QTException {
        if (paramDataRef.getType() != 1634494835)
            throw new QTException(-50);
        return new AliasHandle(QTObject.ID(paramDataRef), paramDataRef, paramDataRef.isLocked(), false);
    }

    /**
     * Creates an AliasHandle from the given byte array containing AliasHandle data
     * @param srcArray byte array containing AliasHandle data
     * @return AliasHandle object pointing to the data contained in srcArray
     * @throws quicktime.QTException
     */
    public static AliasHandle fromArray(byte[] srcArray) throws QTException {
        AliasHandle localAliasHandle = new AliasHandle(srcArray.length, false);
        localAliasHandle.copyFromArray(0, srcArray, 0, srcArray.length);
        return localAliasHandle;
    }

    private AliasHandle(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, paramBoolean);
    }

    private AliasHandle(int paramInt, Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
        super(paramInt, paramObject, paramBoolean1);
        this.minimal = paramBoolean2;
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

    private static short QTNewAlias(byte[] paramArrayOfByte, int[] paramArrayOfInt, byte paramByte) {
        return FileManager.QTNewAlias(paramArrayOfByte, paramArrayOfInt, paramByte);
    }

    private static short NewAlias(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        return FileManager.NewAlias(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfInt);
    }
}

