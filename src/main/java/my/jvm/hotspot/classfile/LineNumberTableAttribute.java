package my.jvm.hotspot.classfile;

import lombok.Getter;

import java.io.DataInputStream;

@Getter
public class LineNumberTableAttribute extends Attribute {
    private final int lineNumberTableLength;
    private final LineNumberInfo[] lineNumberTable;

    public LineNumberTableAttribute(int attributeName, int attributeLength, DataInputStream is) throws Exception {
        super(attributeName, attributeLength);
        this.lineNumberTableLength = is.readUnsignedShort();
        this.lineNumberTable = new LineNumberInfo[lineNumberTableLength];

        for (int i = 0; i < lineNumberTableLength; i++) {
            int startPc = is.readUnsignedShort();
            int lineNumber = is.readUnsignedShort();
            lineNumberTable[i] = new LineNumberInfo(startPc, lineNumber);
        }
    }

    @Getter
    public static class LineNumberInfo {
        private final int startPc;
        private final int lineNumber;

        public LineNumberInfo(int startPc, int lineNumber) {
            this.startPc = startPc;
            this.lineNumber = lineNumber;
        }
    }
}
