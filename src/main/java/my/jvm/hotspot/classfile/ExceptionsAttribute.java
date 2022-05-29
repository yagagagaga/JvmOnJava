package my.jvm.hotspot.classfile;

import lombok.Getter;

import java.io.DataInputStream;

@Getter
public class ExceptionsAttribute extends Attribute {
    private final int numberOfExceptions;
    private final int[] exceptionTable;

    public ExceptionsAttribute(int attributeName, int attributeLength, DataInputStream is) throws Exception {
        super(attributeName, attributeLength);
        this.numberOfExceptions = is.readUnsignedShort();
        exceptionTable = new int[numberOfExceptions];
        for (int i = 0; i < numberOfExceptions; i++) {
            int exceptionIndex = is.readUnsignedShort();
            exceptionTable[i] = exceptionIndex;
        }
    }
}
