package my.jvm.hotspot.classfile;

import my.jvm.hotspot.oops.InstanceKlass;

import java.io.FileInputStream;
import java.io.InputStream;

public class ClassFileParser {

    public static final byte[] U2 = new byte[2];
    public static final byte[] U4 = new byte[4];
    public static final byte[] U8 = new byte[8];

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("/Users/zhangminke/Documents/IntelliJ_workspace/JVM/src/main/resources/HelloWrold.class");
        parseClassFile(fis);
    }

    public synchronized static InstanceKlass parseClassFile(InputStream is) throws Exception {
        return InstanceKlass.parse(is);
    }
}
