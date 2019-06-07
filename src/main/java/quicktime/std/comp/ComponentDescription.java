package quicktime.std.comp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * Describes component features. A value of zero indicates a non-specific value and can also
 * be used as a wildcard in searches.
 */
public class ComponentDescription extends QTByteObject implements QuickTimeLib {

    private static Object linkage;

    private static final int kNativeSize = 20;

    static final long serialVersionUID = 1680706231499437269L;

    String name;

    String info;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(ComponentDescription.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * Create a default ComponentDescription with all zero values.
     */
    public ComponentDescription() {
        super(20);
    }

    /**
     * Specify the type of the ComponentDescription.
     */
    public ComponentDescription(int paramInt) {
        this();
        setType(paramInt);
    }

    /**
     * Specify a full ComponentDescription class.
     * @param type Specifies the component type to search for. Set to 0 if not used in search.
     * @param subType Specifies the component subType. Set to 0 if not used in search.
     * @param manu Specifies the component manufacturer. Set to 0 if not used in search.
     * @param flag Specifies the component control flag used. Set to 0 if not used in search.
     * @param mask Specifies the component flag mask used. Set to 0 if not used in search.
     */
    public ComponentDescription(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        this();
        setType(paramInt1);
        setSubType(paramInt2);
        setManufacturer(paramInt3);
        setFlags(paramInt4);
        setMask(paramInt5);
    }

    private ComponentDescription(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[20];
        byte[] arrayOfByte = new byte[20];
        paramObjectInputStream.read(arrayOfByte);
        for (int i = 0; i < 20; i += 4) {
            int j = getIntFromArray(arrayOfByte, i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(getBytes(), i, k);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[20];
        for (int i = 0; i < 20; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(arrayOfByte, i, k);
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Returns the component type.
     */
    public int getType() {
        return getIntAt(0);
    }

    /**
     * Sets the type.
     */
    public void setType(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * Returns the component subType.
     */
    public int getSubType() {
        return getIntAt(4);
    }

    /**
     * Sets the type.
     */
    public void setSubType(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * Returns the component manufacturer.
     */
    public int getManufacturer() {
        return getIntAt(8);
    }

    /**
     * Sets the manufacturer.
     */
    public void setManufacturer(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * Returns the component flag.
     */
    public int getFlags() {
        return getIntAt(12);
    }

    /**
     * Sets the flag.
     */
    public void setFlags(int paramInt) {
        setIntAt(12, paramInt);
    }

    /**
     * Returns the component mask.
     */
    public int getMask() {
        return getIntAt(16);
    }

    /**
     * Sets the mask.
     */
    public void setMask(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * Count the Components that much the description parameters.
     * QuickTime::CountComponents
     * @return The number of components fitting the specified parameters.
     */
    public int count() {
        return CountComponents(getBytes());
    }

    /**
     * Returns the name of the component retrieved from the ComponentIdentifier.getInfo() call.
     * @return the component's name or null if no name retrieved
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns information about the component retrieved from the ComponentIdentifier.getInfo() call.
     * @return the component's information string or null if none retrieved
     */
    public String getInformationString() {
        return this.info;
    }

    /**
     * Prints the Component Info.
     */
    public String toString() {
        String str = "";
        if (this.name != null)
            str = str + ",name=" + this.name;
        if (this.info != null) {
            str = str + ",info=" + this.info;
        }
        return super.toString() + "[type=" + QTUtils.fromOSType(getType()) + ",subType=" + QTUtils.fromOSType(getSubType()) + ",manufacturer=" + QTUtils.fromOSType(getManufacturer()) + ",flags=" + getFlags() + str + "]";
    }

    /**
     * Return a copy of this object.
     */
    public Object clone() {
        return new ComponentDescription(getBytes());
    }

    private static native int CountComponents(byte[] paramArrayOfByte);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/comp/
 * ComponentDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
