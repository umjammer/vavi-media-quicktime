
package quicktime.util;

import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTRuntimeException;


public class EndianDescriptor {
    public static final int kFlipSpecifiedFields = 0;

    public static final int kFlipAllFields16 = 1;

    public static final int kFlipAllFields32 = 16;

    public static final int kFlipAllFields64 = 256;

    public static final int kFlipNoFields = 4096;

    public static final EndianDescriptor flipAll16 = new EndianDescriptor(1);

    public static final EndianDescriptor flipAll32 = new EndianDescriptor(16);

    public static final EndianDescriptor flipAll64 = new EndianDescriptor(256);

    private int flag;

    private Vector<EndianFlipSpec> vec;

    public EndianDescriptor(int paramInt) {
        this.flag = paramInt;
    }

    public int getFormatFlag() {
        return this.flag;
    }

    public void addFlipSpec(EndianFlipSpec paramEndianFlipSpec) {
        if (this.flag != 0) {
            throw new QTRuntimeException("Can't specify endian flip with " + this.flag + " format flag");
        }
        if (this.vec == null) {
            this.vec = new Vector<>();
        }
        for (int i = 0; i < this.vec.size(); i++) {
            EndianFlipSpec localEndianFlipSpec = this.vec.elementAt(i);
            if (paramEndianFlipSpec.offset == localEndianFlipSpec.offset)
                throw new QTRuntimeException("Each offset must be unique");
            if (paramEndianFlipSpec.offset < localEndianFlipSpec.offset) {
                this.vec.insertElementAt(paramEndianFlipSpec, i);
                return;
            }
        }
        this.vec.addElement(paramEndianFlipSpec);
    }

    public void removeFlipSpec(EndianFlipSpec paramEndianFlipSpec) {
        if (this.vec != null) {
            this.vec.removeElement(paramEndianFlipSpec);
        }
    }

    public EndianFlipSpec getFlipSpec(int paramInt) {
        if (this.vec == null)
            return null;
        for (int i = 0; i < this.vec.size(); i++) {
            EndianFlipSpec localEndianFlipSpec = this.vec.elementAt(i);
            if (paramInt == localEndianFlipSpec.offset)
                return localEndianFlipSpec;
            if (localEndianFlipSpec.offset > paramInt)
                return null;
        }
        return null;
    }

    public Enumeration<EndianFlipSpec> flipSpecs() {
        return this.vec == null ? null : this.vec.elements();
    }

    public String toString() {
        String str = getClass().getName() + "[flipFlag=";
        switch (this.flag) {
        case 0:
            str = str + "kFlipSpecifiedFields";
            break;
        case 1:
            str = str + "kFlipAllFields16";
            break;
        case 16:
            str = str + "kFlipAllFields32";
            break;
        case 256:
            str = str + "kFlipAllFields64";
            break;
        case 4096:
            str = str + "kFlipNoFields";
        }
        if (this.flag == 0) {
            str = str + "[";
            Enumeration<EndianFlipSpec> e = flipSpecs();
            while (e.hasMoreElements())
                str = str + e.nextElement() + ",";
            str = str + "]";
        }
        str = str + "]";
        return str;
    }
}
