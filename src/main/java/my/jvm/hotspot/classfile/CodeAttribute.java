package my.jvm.hotspot.classfile;

import lombok.Getter;
import my.jvm.hotspot.oops.ConstantPool;

import java.io.DataInputStream;

@Getter
public class CodeAttribute extends Attribute {
    private final int maxStack;
    private final int maxLocals;
    private final int codeLength;
    private final byte[] code;
    private final int exceptionTableLength;
    private final ExceptionsAttribute[] exceptionTable;
    private final int attributeCount;
    private final Attribute[] attributes;

    public CodeAttribute(int attributeName, int attributeLength, DataInputStream is, ConstantPool constantPool) throws Exception {
        super(attributeName, attributeLength);
        this.maxStack = is.readUnsignedShort();
        this.maxLocals = is.readUnsignedShort();
        this.codeLength = is.readInt();
        this.code = new byte[codeLength];
        is.read(code);
        this.exceptionTableLength = is.readUnsignedShort();
        exceptionTable = new ExceptionsAttribute[exceptionTableLength];
        for (int j = 0; j < exceptionTableLength; j++) {
            exceptionTable[j] = (ExceptionsAttribute) Attribute.parse(is, constantPool);
        }
        this.attributeCount = is.readUnsignedShort();
        attributes = new Attribute[attributeCount];
        for (int j = 0; j < attributeCount; j++) {
            attributes[j] = Attribute.parse(is, constantPool);
        }
    }
}
