package my.jvm.hotspot.oops;

import my.jvm.hotspot.classfile.*;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static my.jvm.hotspot.classfile.Attribute.Deprecated;
import static my.jvm.hotspot.classfile.Attribute.*;

public class InstanceKlass {
    private final int magic;
    private final int minorVersion;
    private final int majorVersion;
    private final int constantPoolCount;
    private final ConstantPool constantPool;
    private final int accessFlags;
    private final int thisClass;
    private final int superClass;
    private final int interfaceCount;
    private final int[] interfaces;
    private final int fieldCount;
    private final FieldInfo[] fieldsInfo;
    private final int methodCount;
    private final MethodInfo[] methodsInfo;
    private final int attributesCount;
    private final Attribute[] attributes;

    public InstanceKlass(int magic, int minorVersion, int majorVersion, int constantPoolCount, ConstantPool constantPool, int accessFlags, int thisClass, int superClass, int interfaceCount, int[] interfaces, int fieldCount, FieldInfo[] fieldsInfo, int methodCount, MethodInfo[] methodsInfo, int attributesCount, Attribute[] attributes) {
        this.magic = magic;
        this.minorVersion = minorVersion;
        this.majorVersion = majorVersion;
        this.constantPoolCount = constantPoolCount;
        this.constantPool = constantPool;
        this.accessFlags = accessFlags;
        this.thisClass = thisClass;
        this.superClass = superClass;
        this.interfaceCount = interfaceCount;
        this.interfaces = interfaces;
        this.fieldCount = fieldCount;
        this.fieldsInfo = fieldsInfo;
        this.methodCount = methodCount;
        this.methodsInfo = methodsInfo;
        this.attributesCount = attributesCount;
        this.attributes = attributes;
    }

    public void printKlass() {
        System.out.println("魔数为：" + Integer.toHexString(magic));
        System.out.println("小版本号：" + minorVersion);
        System.out.println("大版本号：" + majorVersion);
        System.out.println("常量池大小为：" + constantPoolCount);
        for (int i = 1; i < constantPoolCount; i++) {
            ConstantPool.Slot slot = constantPool.getSlotAt(i);
            System.out.println("\t第" + i + "个常量类型为 " + slot.getTagString() + "，值为 " + slot.getValue());
        }
        System.out.println("当前类的访问权限为：" + Integer.toHexString(accessFlags));
        System.out.println("当前类名为：" + constantPool.getSlotAt(thisClass).getValue());
        System.out.println("当前父类名为：" + constantPool.getSlotAt(superClass).getValue());
        System.out.println("当前接口个数为：" + interfaceCount);
        for (int i = 0; i < interfaceCount; i++) {
            System.out.println("\t第" + (i + 1) + "个接口为：" + constantPool.getSlotAt(interfaces[i]).getValue());
        }
        System.out.println("成员的个数为：" + fieldCount);
        for (int i = 0; i < fieldCount; i++) {
            FieldInfo info = fieldsInfo[i];
            System.out.println(
                    String.format("\t第%d个成员信息为：访问权限为 %d，名称为 %s，描述符为 %s，属性个数为 %d",
                            i + 1, info.getAccessFlag(), constantPool.getSlotAt(info.getNameIndex()).getValue(),
                            constantPool.getSlotAt(info.getDescriptorIndex()).getValue(), info.getAttributesCount()));
            for (int j = 0; j < info.getAttributes().length; j++) {
                System.out.println(String.format("\t\t第%d个属性为：%s", j + 1, toString(info.getAttributes()[j])));
            }
        }
        System.out.println("方法的个数为：" + methodCount);
        for (int i = 0; i < methodCount; i++) {
            MethodInfo info = methodsInfo[i];
            System.out.println(
                    String.format("\t第%d个方法信息为：访问权限为 %d，名称为 %s，描述符为 %s，属性个数为 %d",
                            i + 1, info.getAccessFlag(), constantPool.getSlotAt(info.getNameIndex()).getValue(),
                            constantPool.getSlotAt(info.getDescriptorIndex()).getValue(), info.getAttributesCount()));
            for (int j = 0; j < info.getAttributes().length; j++) {
                System.out.println(String.format("\t\t第%d个属性为：%s", j + 1, toString(info.getAttributes()[j])));
            }
        }
        System.out.println("类属性个数为：" + attributesCount);
        for (int j = 0; j < attributes.length; j++) {
            System.out.println(String.format("\t\t第%d个属性为：%s", j + 1, toString(attributes[j])));
        }
    }

    private String toString(Attribute attribute) {
        if (attribute == null) return null;
        String attributeName = constantPool.getSlotAt(attribute.getAttributeNameIndex()).getValue();
        switch (attributeName) {
            case Code: {
                CodeAttribute attr = (CodeAttribute) attribute;
                return String.format("属性名称为%s，操作数栈最大深度为%d，局部变量表所需空间为%d，字节码指令长度为%d，字节码指令为%s，异常表长度为%d，异常为%s，属性个数为%d，属性为%s",
                        attributeName,
                        attr.getMaxStack(),
                        attr.getMaxLocals(),
                        attr.getCodeLength(),
                        Arrays.toString(attr.getCode()),
                        attr.getExceptionTableLength(),
                        Arrays.toString(attr.getExceptionTable()),
                        attr.getAttributeCount(),
                        Arrays.stream(attr.getAttributes())
                                .map(this::toString)
                                .collect(Collectors.joining()));
            }
            case ConstantValue:
                break;
            case Deprecated:
                break;
            case Exceptions: {
                ExceptionsAttribute attr = (ExceptionsAttribute) attribute;
                return String.format("属性名称为%s，异常个数%d，异常为%s",
                        attributeName,
                        attr.getNumberOfExceptions(),
                        Stream.of(attr.getExceptionTable())
                                .flatMapToInt(Arrays::stream)
                                .mapToObj(x -> constantPool.getSlotAt(x).getValue())
                                .collect(Collectors.joining(",", "[", "]")));
            }
            case EnclosingMethod:
                break;
            case InnerClasses:
                break;
            case LineNumberTable: {
                LineNumberTableAttribute attr = (LineNumberTableAttribute) attribute;
                return String.format("属性名称为%s，行号表长度为%d，行号表为%s",
                        attributeName,
                        attr.getLineNumberTableLength(),
                        Arrays.stream(attr.getLineNumberTable())
                                .map(x -> String.format("<字节码行号为%d，源码行号为%d>", x.getStartPc(), x.getLineNumber()))
                                .collect(Collectors.joining(",", "[", "]")));
            }
            case LocalVariableTable: {
                LocalVariableTableAttribute attr = (LocalVariableTableAttribute) attribute;
                return String.format("属性名称为%s，局部变量表长度为%d，局部变量表为%s",
                        attributeName,
                        attr.getLocalVariableTableLength(),
                        Arrays.stream(attr.getLocalVariableTable())
                                .map(x -> String.format("<局部变量所在字节码偏移量为%d，局部变量长度为%d，局部变量名为%s，局部变量描述符为%s，局部变量位于栈帧的局部变量表的%d个位置>",
                                        x.getDescriptorIndex(),
                                        x.getLength(),
                                        constantPool.getSlotAt(x.getNameIndex()).getValue(),
                                        constantPool.getSlotAt(x.getDescriptorIndex()).getValue(),
                                        x.getIndex()))
                                .collect(Collectors.joining(",", "[", "]")));
            }
            case StackMapTable:
                break;
            case Signature:
                break;
            case SourceFile: {
                SourceFileAttribute attr = (SourceFileAttribute) attribute;
                return String.format("属性名称为%s，源码文件名称为%s",
                        attributeName,
                        constantPool.getSlotAt(attr.getSourceFileIndex()).getValue());
            }
            case SourceDebugExtension:
                break;
            case Synthetic:
                break;
            case LocalVariableTypeTable:
                break;
            case RuntimeVisibleAnnotations:
                break;
            case RuntimeInvisibleAnnotations:
                break;
            case RuntimeVisibleParameterAnnotations:
                break;
            case RuntimeInvisibleParameterAnnotations:
                break;
            case AnnotationDefault:
                break;
            case BootstrapMethods:
                break;
            case RuntimeVisibleTypeAnnotations:
                break;
            case RuntimeInvisibleTypeAnnotations:
                break;
            case MethodParameters:
                break;
            case Module:
                break;
            case ModulePackages:
                break;
            case ModuleMainClass:
                break;
            case NestHost:
                break;
            case NestMembers:
                break;
            default:
                break;
        }
        return "无法识别的属性";
    }
}
