package my.jvm.hotspot.oops;

import my.jvm.hotspot.runtime.ClassConstants;
import tools.ByteUtils;
import static my.jvm.hotspot.classfile.ClassFileParser.*;
import java.io.InputStream;

public class ConstantPool implements ClassConstants {
    private int constantPoolCount;
    private Slot[] slots;



    public ConstantPool(InputStream is) throws Exception {
        is.read(U2);
        constantPoolCount = ByteUtils.toUnsignedShort(U2);
        System.out.println("常量池大小: " + constantPoolCount);

        slots = new Slot[constantPoolCount];
        for (int i = 1; i < constantPoolCount; i++) {
            int tag = is.read();
            System.out.print('\t');
            switch (tag) {
                case JVM_CONSTANT_Utf8: {
                    is.read(U2);
                    int len = ByteUtils.toUnsignedShort(U2);
                    byte[] bytes = new byte[len];
                    is.read(bytes);
                    slots[i] = new Slot(tag, ByteUtils.copy(U2, bytes), this);
                    System.out.print("第" + i + "个常量，类型为utf8，值为：" + new String(bytes));
                }
                break;
                case JVM_CONSTANT_Integer: {
                    is.read(U4);
                    int val = ByteUtils.toInt(U4);
                    slots[i] = new Slot(tag, ByteUtils.copy(U4), this);
                    System.out.print("第" + i + "个常量，类型为int，值为：" + val);
                }
                break;
                case JVM_CONSTANT_Float: {
                    is.read(U4);
                    float val = ByteUtils.toFloat(U4);
                    slots[i] = new Slot(tag, ByteUtils.copy(U4), this);
                    System.out.print("第" + i + "个常量，类型为float，值为：" + val);
                }
                break;
                case JVM_CONSTANT_Long: {
                    is.read(U8);
                    long val = ByteUtils.toLong(U8);
                    slots[i] = new Slot(tag, ByteUtils.copy(U8), this);
                    System.out.print("第" + i + "个常量，类型为long，值为：" + val);
                }
                break;
                case JVM_CONSTANT_Double: {
                    is.read(U8);
                    double val = ByteUtils.toDouble(U8);
                    slots[i] = new Slot(tag, ByteUtils.copy(U8), this);
                    System.out.print("第" + i + "个常量，类型为double，值为：" + val);
                }
                break;
                case JVM_CONSTANT_Class: {
                    is.read(U2);
                    int index = ByteUtils.toUnsignedShort(U2);
                    slots[i] = new Slot(tag, ByteUtils.copy(U2), this);
                    System.out.print("第" + i + "个常量，类型为class，值为：" + index);
                }
                break;
                case JVM_CONSTANT_String: {
                    is.read(U2);
                    int index = ByteUtils.toUnsignedShort(U2);
                    slots[i] = new Slot(tag, ByteUtils.copy(U2), this);
                    System.out.print("第" + i + "个常量，类型为string，值为：" + index);
                }
                break;
                case JVM_CONSTANT_Fieldref: {
                    byte[] tmp = new byte[2];
                    is.read(tmp);
                    int index1 = ByteUtils.toUnsignedShort(tmp);
                    is.read(U2);
                    int index2 = ByteUtils.toUnsignedShort(U2);
                    slots[i] = new Slot(tag, ByteUtils.copy(tmp, U2), this);
                    System.out.print("第" + i + "个常量，类型为fieldref，字段类为：" + index1 + "，字段描述符为：" + index2);
                }
                break;
                case JVM_CONSTANT_Methodref: {
                    byte[] tmp = new byte[2];
                    is.read(tmp);
                    int index1 = ByteUtils.toUnsignedShort(tmp);
                    is.read(U2);
                    int index2 = ByteUtils.toUnsignedShort(U2);
                    slots[i] = new Slot(tag, ByteUtils.copy(tmp, U2), this);
                    System.out.print("第" + i + "个常量，类型为methodref，字段类为：" + index1 + "，字段描述符为：" + index2);
                }
                break;
                case JVM_CONSTANT_InterfaceMethodref: {
                    byte[] tmp = new byte[2];
                    is.read(tmp);
                    int index1 = ByteUtils.toUnsignedShort(tmp);
                    is.read(U2);
                    int index2 = ByteUtils.toUnsignedShort(U2);
                    slots[i] = new Slot(tag, ByteUtils.copy(tmp, U2), this);
                    System.out.print("第" + i + "个常量，类型为interfaceMethodref，字段类为：" + index1 + "，字段描述符为：" + index2);
                }
                break;
                case JVM_CONSTANT_NameAndType: {
                    byte[] tmp = new byte[2];
                    is.read(tmp);
                    int index1 = ByteUtils.toUnsignedShort(tmp);
                    is.read(U2);
                    int index2 = ByteUtils.toUnsignedShort(U2);
                    slots[i] = new Slot(tag, ByteUtils.copy(tmp, U2), this);
                    System.out.print("第" + i + "个常量，类型为nameAndType，字段/方法名称为：" + index1 + "，字段/方法描述符为：" + index2);
                }
                break;
                default:
                    System.out.print("第" + i + "个常量，tag值为：" + tag);
            }
            System.out.println();
        }
    }

    public Slot getSlotAt(int idx) {
        return slots[idx];
    }

    static class Slot {
        private byte tag;
        private byte[] data;
        private ConstantPool constantPool;

        public Slot(int tag, byte[] data, ConstantPool constantPool) {
            this.tag = (byte) tag;
            this.data = data;
            this.constantPool = constantPool;
        }

        public String getValue() {
            switch (tag) {
                case JVM_CONSTANT_Utf8: {
                    return new String(data, 2, data.length - 2);
                }
                case JVM_CONSTANT_Integer: {
                    return ByteUtils.toInt(data) + "";
                }
                case JVM_CONSTANT_Float: {
                    return ByteUtils.toFloat(data) + "";
                }
                case JVM_CONSTANT_Long: {
                    return ByteUtils.toLong(data) + "";
                }
                case JVM_CONSTANT_Double: {
                    return ByteUtils.toDouble(data) + "";
                }
                case JVM_CONSTANT_Class:
                case JVM_CONSTANT_String: {
                    int idx = ByteUtils.toUnsignedShort(data);
                    return constantPool.getSlotAt(idx).getValue();
                }
                case JVM_CONSTANT_Fieldref:
                case JVM_CONSTANT_Methodref:
                case JVM_CONSTANT_InterfaceMethodref:
                case JVM_CONSTANT_NameAndType: {
                    int idx1 = ByteUtils.toUnsignedShort(data);
                    String name = constantPool.getSlotAt(idx1).getValue();
                    int idx2 = ByteUtils.toUnsignedShort(data);
                    String desc = constantPool.getSlotAt(idx2).getValue();
                    return name + " " + desc;
                }
            }
            throw new Error("It shouldn't happen!");
        }
    }
}
