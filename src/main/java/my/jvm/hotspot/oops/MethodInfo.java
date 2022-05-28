package my.jvm.hotspot.oops;

public class MethodInfo {
    private int accessFlag;
    private String name;
    private String descriptor;
    private int attributesCount;
    private AttributeInfo[] attributes;

    public MethodInfo(int accessFlag, String name, String descriptor, int attributesCount, AttributeInfo[] attributes) {
        this.accessFlag = accessFlag;
        this.name = name;
        this.descriptor = descriptor;
        this.attributesCount = attributesCount;
        this.attributes = attributes;
    }
}
