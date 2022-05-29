package my.jvm.hotspot.classfile;

import lombok.Getter;

import java.io.DataInputStream;

@Getter
public class LocalVariableTableAttribute extends Attribute {
    private final int localVariableTableLength;
    private final LocalVariableInfo[] localVariableTable;

    public LocalVariableTableAttribute(int attributeName, int attributeLength, DataInputStream is) throws Exception {
        super(attributeName, attributeLength);
        this.localVariableTableLength = is.readUnsignedShort();
        this.localVariableTable = new LocalVariableInfo[localVariableTableLength];
        for (int i = 0; i < localVariableTableLength; i++) {
            int startPc = is.readUnsignedShort();
            int length = is.readUnsignedShort();
            int nameIndex = is.readUnsignedShort();
            int descriptorIndex = is.readUnsignedShort();
            int index = is.readUnsignedShort();
            localVariableTable[i] = new LocalVariableInfo(startPc, length, nameIndex, descriptorIndex, index);
        }
    }

    @Getter
    public static class LocalVariableInfo {
        private final int startPc;
        private final int length;
        private final int nameIndex;
        private final int descriptorIndex;
        private final int index;

        public LocalVariableInfo(int startPc, int length, int nameIndex, int descriptorIndex, int index) {
            this.startPc = startPc;
            this.length = length;
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
            this.index = index;
        }
    }
}
