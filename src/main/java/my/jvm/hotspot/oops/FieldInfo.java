package my.jvm.hotspot.oops;

import lombok.Getter;
import my.jvm.hotspot.classfile.Attribute;

@Getter
public class FieldInfo {
    private final int accessFlag;
    private final int nameIndex;
    private final int descriptorIndex;
    private final int attributesCount;
    private final Attribute[] attributes;

    public FieldInfo(int accessFlag, int nameIndex, int descriptorIndex, int attributesCount, Attribute[] attributes) {
        this.accessFlag = accessFlag;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributesCount = attributesCount;
        this.attributes = attributes;
    }
}
