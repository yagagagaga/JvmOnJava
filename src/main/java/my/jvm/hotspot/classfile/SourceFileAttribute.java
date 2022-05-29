package my.jvm.hotspot.classfile;

import lombok.Getter;

import java.io.DataInputStream;

@Getter
public class SourceFileAttribute extends Attribute {
    private final int sourceFileIndex;

    public SourceFileAttribute(int attributeName, int attributeLength, DataInputStream is) throws Exception {
        super(attributeName, attributeLength);
        sourceFileIndex = is.readUnsignedShort();
    }
}
