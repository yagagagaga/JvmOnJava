package my.jvm.hotspot.classfile;

import my.jvm.hotspot.oops.ConstantPool;
import my.jvm.hotspot.oops.FieldInfo;
import my.jvm.hotspot.oops.InstanceKlass;
import my.jvm.hotspot.oops.MethodInfo;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import static my.jvm.hotspot.runtime.ClassConstants.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ClassFileParser {

    public static void main(String[] args) {
        InstanceKlass instanceKlass = parseClassFile(
                new File("/Users/zhangminke/Documents/IntelliJ_workspace/JVM/src/main/resources/HelloWrold.class"));
        assert instanceKlass != null;
        instanceKlass.printKlass();
    }

    public synchronized static InstanceKlass parseClassFile(File classFile) {
        try (DataInputStream is = new DataInputStream(new BufferedInputStream(new FileInputStream(classFile)))) {
            return parseClassFile(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized static InstanceKlass parseClassFile(DataInputStream is) throws Exception {
        int magic = is.readInt();
        int minorVersion = is.readUnsignedShort();
        int majorVersion = is.readUnsignedShort();
        int constantPoolCount = is.readUnsignedShort();
        ConstantPool constantPool = parseConstantPool(is, constantPoolCount);
        int accessFlags = is.readUnsignedShort();
        int thisClass = is.readUnsignedShort();
        int superClass = is.readUnsignedShort();
        int interfaceCount = is.readUnsignedShort();
        int[] interfaces = parseInterfaces(is, interfaceCount);
        int fieldsCount = is.readUnsignedShort();
        FieldInfo[] fieldInfos = parseFields(is, fieldsCount, constantPool);
        int methodsCount = is.readUnsignedShort();
        MethodInfo[] methodInfos = parseMethods(is, methodsCount, constantPool);
        int attributesCount = is.readUnsignedShort();
        Attribute[] attributes = parseAttributes(is, attributesCount, constantPool);

        return new InstanceKlass(magic, minorVersion, majorVersion, constantPoolCount, constantPool, accessFlags, thisClass, superClass, interfaceCount, interfaces, fieldsCount, fieldInfos, methodsCount, methodInfos, attributesCount, attributes);
    }

    private static ConstantPool parseConstantPool(DataInputStream is, int constantPoolCount) throws Exception {
        ConstantPool.Slot[] slots = new ConstantPool.Slot[constantPoolCount];
        ConstantPool constantPool = new ConstantPool(slots);
        for (int i = 1; i < constantPoolCount; i++) {
            byte tag = (byte) is.read();
            switch (tag) {
                case JVM_CONSTANT_Utf8: {
                    int len = is.readUnsignedShort();
                    byte[] bytes = new byte[len];
                    is.read(bytes);
                    slots[i] = constantPool.new Utf8Slot(tag, len, bytes);
                }
                break;
                case JVM_CONSTANT_Integer: {
                    int val = is.readInt();
                    slots[i] = constantPool.new IntegerSlot(tag, val);
                }
                break;
                case JVM_CONSTANT_Float: {
                    float val = is.readFloat();
                    slots[i] = constantPool.new FloatSlot(tag, val);
                }
                break;
                case JVM_CONSTANT_Long: {
                    long val = is.readLong();
                    slots[i] = constantPool.new LongSlot(tag, val);
                }
                break;
                case JVM_CONSTANT_Double: {
                    double val = is.readDouble();
                    slots[i] = constantPool.new DoubleSlot(tag, val);
                }
                break;
                case JVM_CONSTANT_Class: {
                    int index = is.readUnsignedShort();
                    slots[i] = constantPool.new ClassSlot(tag, index);
                }
                break;
                case JVM_CONSTANT_String: {
                    int index = is.readUnsignedShort();
                    slots[i] = constantPool.new StringSlot(tag, index);

                }
                break;
                case JVM_CONSTANT_Fieldref: {
                    int index1 = is.readUnsignedShort();
                    int index2 = is.readUnsignedShort();
                    slots[i] = constantPool.new FieldrefSlot(tag, index1, index2);
                }
                break;
                case JVM_CONSTANT_Methodref: {
                    int index1 = is.readUnsignedShort();
                    int index2 = is.readUnsignedShort();
                    slots[i] = constantPool.new MethodrefSlot(tag, index1, index2);
                }
                break;
                case JVM_CONSTANT_InterfaceMethodref: {
                    int index1 = is.readUnsignedShort();
                    int index2 = is.readUnsignedShort();
                    slots[i] = constantPool.new InterfaceMethodrefSlot(tag, index1, index2);
                }
                break;
                case JVM_CONSTANT_NameAndType: {
                    int index1 = is.readUnsignedShort();
                    int index2 = is.readUnsignedShort();
                    slots[i] = constantPool.new NameAndTypeSlot(tag, index1, index2);
                }
                break;
                default:
                    System.out.print("第" + i + "个常量，tag值为：" + tag);
            }
        }
        return new ConstantPool(slots);
    }

    private static int[] parseInterfaces(DataInputStream is, int interfaceCount) throws Exception {
        int[] interfaces = new int[interfaceCount];
        for (int i = 0; i < interfaceCount; i++) {
            interfaces[i] = is.readUnsignedShort();
        }
        return interfaces;
    }

    private static FieldInfo[] parseFields(DataInputStream is, int fieldCount, ConstantPool constantPool) throws Exception {
        FieldInfo[] fieldsInfo = new FieldInfo[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
            int accessFlag = is.readUnsignedShort();
            int nameIndex = is.readUnsignedShort();
            int descriptorIndex = is.readUnsignedShort();
            int attributesCount = is.readUnsignedShort();
            Attribute[] attributes = parseAttributes(is, attributesCount, constantPool);
            fieldsInfo[i] = new FieldInfo(accessFlag, nameIndex, descriptorIndex, attributesCount, attributes);
        }
        return fieldsInfo;
    }


    private static MethodInfo[] parseMethods(DataInputStream is, int methodCount, ConstantPool constantPool) throws Exception {
        MethodInfo[] methodsInfo = new MethodInfo[methodCount];
        for (int i = 0; i < methodCount; i++) {
            int accessFlag = is.readUnsignedShort();
            int nameIndex = is.readUnsignedShort();
            int descriptorIndex = is.readUnsignedShort();
            int attributesCount = is.readUnsignedShort();
            Attribute[] attributes = parseAttributes(is, attributesCount, constantPool);
            methodsInfo[i] = new MethodInfo(accessFlag, nameIndex, descriptorIndex, attributesCount, attributes);
        }
        return methodsInfo;
    }

    private static Attribute[] parseAttributes(DataInputStream is, int attributesCount, ConstantPool constantPool) throws Exception {
        Attribute[] attributes = new Attribute[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = Attribute.parse(is, constantPool);
        }
        return attributes;
    }
}
