package quicktime.vr;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.QTByteObject;
import quicktime.util.QTHandle;

/**
 * The cursRecord parameter to replaceCursor() specifies a CursorRecord object,
 * which indicates the cursor to replace and its replacement cursor.
 */
public class CursorRecord extends QTByteObject implements PrimitivesLib {

    private static final int kNativeSize = 8;

    /**
     * Creates a CursorRecord object containing no data
     */
    public CursorRecord() {
        super(8);
    }

    /**
     * Creates a QTVRTrackRefEntry object from a byte array
     * Used in cloning
     */
    public CursorRecord(byte[] bytes) throws QTException {
        super(bytes);
        if (bytes.length != 8) {
            throw new QTException(-50);
        }
    }

    /**
     * Returns the cursor Type
     * CursorRecord types
     * kQTVRUseDefaultCursor
     * kQTVRStdCursorType
     * kQTVRColorCursorType
     * @return the cursor type.
     */
    public short getType() {
        return getShortAt(0);
    }

    /**
     * Sets the cursor Type
     * CursorRecord types
     * kQTVRUseDefaultCursor
     * kQTVRStdCursorType
     * kQTVRColorCursorType
     */
    public void setType(short type) {
        setShortAt(0, type);
    }

    /**
     * Returns the cursor resource ID
     * @return the resource ID.
     */
    public short getCursorID() {
        return getShortAt(2);
    }

    /**
     * Sets the cursor ID
     * @param cursorID -
     */
    public void setCursorID(short cursorID) {
        setShortAt(2, cursorID);
    }

    /**
     * Returns the cursor data
     * @return the handle object pointing to cursor data
     * @throws quicktime.QTException
     */
    public QTHandle getData() throws QTException {
        int i = getIntAt(4);
        int j = getIntFromHandle(i, 0);
        QTHandle data = new QTHandle();
        setIntInHandle(QTObject.ID(data), 0, j);
        return data;
    }

    /**
     * A handle to the cursor data that is to replace the specified cursor. If the Type is kQTVRUseDefaultCursor,
     * then this field should contain null.
     * @param data cursor data that is to replace the cursor
     */
    public void setData(QTHandle data) {
        int i = 0;
        if (data != null) {
            i = getIntFromHandle(QTObject.ID(data), 0);
        } else
            i = 0;
        setIntAt(4, i);
    }

    private static void setIntInHandle(int h, int offset, int value) {
        MemoryManager.setIntInHandle(h, offset, value);
    }

    private static int getIntFromHandle(int h, int offset) {
        return MemoryManager.getIntFromHandle(h, offset);
    }
}
