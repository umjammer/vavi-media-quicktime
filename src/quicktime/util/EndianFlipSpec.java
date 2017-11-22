
package quicktime.util;

public class EndianFlipSpec {
    public static final int kFlip16BitValue = 2;

    public static final int kFlip32BitValue = 4;

    public static final int kFlip64BitValue = 8;

    int offset;

    int sizeFlag;

    int num;

    public EndianFlipSpec(int paramInt1, int paramInt2, int paramInt3) {
        this.offset = paramInt1;
        this.sizeFlag = paramInt2;
        this.num = paramInt3;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getSizeFlag() {
        return this.sizeFlag;
    }

    public int getNumberOfFlips() {
        return this.num;
    }

    public boolean equals(Object paramObject) {
        if (paramObject == this)
            return true;
        if ((paramObject instanceof EndianFlipSpec)) {
            EndianFlipSpec localEndianFlipSpec = (EndianFlipSpec) paramObject;
            return (localEndianFlipSpec.offset == this.offset) && (localEndianFlipSpec.sizeFlag == this.sizeFlag)
                   && (localEndianFlipSpec.num == this.num);
        }
        return false;
    }

    public String toString() {
        return "flipSpec[offset=" + this.offset + ",sizeFlag=" + this.sizeFlag + ",num=" + this.num + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * EndianFlipSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
