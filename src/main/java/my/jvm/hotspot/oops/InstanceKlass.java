package my.jvm.hotspot.oops;

import tools.ByteUtils;

import java.io.InputStream;

import static my.jvm.hotspot.classfile.ClassFileParser.*;

public class InstanceKlass {
    private byte[] magic = new byte[4];
    private int minorVersion;
    private int majorVersion;
    private ConstantPool constantPool;
    private int klassAccessFlags;
    private String thisName;
    private String superName;
    private int interfaceCount;
    private String[] interfaces;
    private int fieldCount;
    private int methodCount;
    private MethodInfo[] methodInfos;
    private int attributesCount;
    private AttributeInfo[] attributes;

    public static InstanceKlass parse(InputStream is) throws Exception {
        InstanceKlass klass = new InstanceKlass();
        klass.parseMagic(is);
        klass.parseVersion(is);
        klass.parseConstantPool(is);
        klass.parseAccessFlags(is);
        klass.parseThisClass(is);
        klass.parseSuperClass(is);
        klass.parseInterface(is);
        klass.parseFields(is);
        klass.parseMethods(is);
        klass.parseAttributes(is);
        return klass;
    }



    private void parseMagic(InputStream is) throws Exception {
        is.read(magic);
        System.out.println("魔数: " + ByteUtils.dumpBytes(magic));
    }

    private void parseVersion(InputStream is) throws Exception {
        is.read(U2);
        minorVersion = ByteUtils.toShort(U2);
        System.out.println("小版本号: " + minorVersion);

        is.read(U2);
        majorVersion = ByteUtils.toShort(U2);
        System.out.println("大版本号: " + majorVersion);
    }

    private void parseConstantPool(InputStream is) throws Exception {
        constantPool = new ConstantPool(is);
    }

    private void parseAccessFlags(InputStream is) throws Exception {
        is.read(U2);
        klassAccessFlags = ByteUtils.toUnsignedShort(U2);
        System.out.println("类的访问权限为：" + Integer.toHexString(klassAccessFlags));
    }

    private void parseThisClass(InputStream is) throws Exception {
        is.read(U2);
        int idx = ByteUtils.toUnsignedShort(U2);
        thisName = constantPool.getSlotAt(idx).getValue();
        System.out.println("this类名为：" + thisName);
    }

    private void parseSuperClass(InputStream is) throws Exception {
        is.read(U2);
        int idx = ByteUtils.toUnsignedShort(U2);
        superName = constantPool.getSlotAt(idx).getValue();
        System.out.println("super类名为：" + superName);
    }

    private void parseInterface(InputStream is) throws Exception {
        is.read(U2);
        interfaceCount = ByteUtils.toUnsignedShort(U2);
        System.out.println("接口数量为: " + interfaceCount);

        interfaces = new String[interfaceCount];
        for (int i = 0; i < interfaceCount; i++) {
            is.read(U2);
            int idx = ByteUtils.toUnsignedShort(U2);
            interfaces[i] = constantPool.getSlotAt(idx).getValue();
            System.out.println("\t第" + (i + 1) + "个接口为：" + interfaces[i]);
        }
    }

    private void parseFields(InputStream is) throws Exception {
        is.read(U2);
        fieldCount = ByteUtils.toUnsignedShort(U2);
        System.out.println("成员属性的个数为：" + fieldCount);

        for (int i = 1; i <= fieldCount; i++) {
            is.read(U2);
            String accessFlag = ByteUtils.dumpBytes(U2);
            is.read(U2);
            int nameIndex = ByteUtils.toUnsignedShort(U2);
            is.read(U2);
            int descriptorIndex = ByteUtils.toUnsignedShort(U2);
            is.read(U2);
            int attributesCount = ByteUtils.toUnsignedShort(U2);
            System.out.println("\t第" + i + "个成员属性的访问标志为：" + accessFlag + "，名字为：" + nameIndex + "，描述为：" + descriptorIndex + "，属性个数为：" + attributesCount + "，属性分别是：");
            for (int j = 0; j < attributesCount; j++) {
                is.read(U2);
                int attributeNameIndex = ByteUtils.toUnsignedShort(U2);
                is.read(U4);
                long attributeLength = ByteUtils.toUnsignedInt(U4);
                is.read(new byte[(int) attributeLength]);
                System.out.println("\t\t属性名为：" + attributeNameIndex + "，属性长度为：" + attributeLength);
            }
        }
    }

    private void parseMethods(InputStream is) throws Exception {
        is.read(U2);
        methodCount = ByteUtils.toUnsignedShort(U2);
        System.out.println("成员方法的个数为：" + methodCount);

        methodInfos = new MethodInfo[methodCount];
        for (int i = 0; i < methodCount; i++) {
            is.read(U2);
            int accessFlag = ByteUtils.toUnsignedShort(U2);
            is.read(U2);
            int nameIndex = ByteUtils.toUnsignedShort(U2);
            String name = constantPool.getSlotAt(nameIndex).getValue();
            is.read(U2);
            int descriptorIndex = ByteUtils.toUnsignedShort(U2);
            String descriptor = constantPool.getSlotAt(descriptorIndex).getValue();
            is.read(U2);
            int attributesCount = ByteUtils.toUnsignedShort(U2);
            AttributeInfo[] attributes = new AttributeInfo[attributesCount];
            System.out.println("\t第" + (i+1) + "个成员方法的访问标志为：" + accessFlag + "，名字为：" + name + "，描述为：" + descriptor + "，属性个数为：" + attributesCount + "，属性分别是：");
            for (int j = 0; j < attributesCount; j++) {
                attributes[j] = AttributeInfo.parse(is, constantPool);
                System.out.println("\t\t属性为：" + attributes[j]);
            }
            methodInfos[i] = new MethodInfo(accessFlag, name, descriptor, attributesCount, attributes);
        }
    }

    private void parseAttributes(InputStream is) throws Exception {
        is.read(U2);
        this.attributesCount = ByteUtils.toUnsignedShort(U2);
        this.attributes = new AttributeInfo[attributesCount];
        System.out.println("类属性个数为：" + attributesCount + "，分别是");
        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = AttributeInfo.parse(is, constantPool);
            System.out.println("\t" + attributes[i]);
        }

    }
}
