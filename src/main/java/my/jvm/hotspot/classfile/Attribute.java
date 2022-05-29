package my.jvm.hotspot.classfile;

import lombok.Getter;
import my.jvm.hotspot.oops.ConstantPool;

import java.io.DataInputStream;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Getter
public abstract class Attribute {

    public static final String Code = "Code";
    public static final String ConstantValue = "ConstantValue";
    public static final String Deprecated = "Deprecated";
    public static final String Exceptions = "Exceptions";
    public static final String EnclosingMethod = "EnclosingMethod";
    public static final String InnerClasses = "InnerClasses";
    public static final String LineNumberTable = "LineNumberTable";
    public static final String LocalVariableTable = "LocalVariableTable";
    public static final String StackMapTable = "StackMapTable";
    public static final String Signature = "Signature";
    public static final String SourceFile = "SourceFile";
    public static final String SourceDebugExtension = "SourceDebugExtension";
    public static final String Synthetic = "Synthetic";
    public static final String LocalVariableTypeTable = "LocalVariableTypeTable";
    public static final String RuntimeVisibleAnnotations = "RuntimeVisibleAnnotations";
    public static final String RuntimeInvisibleAnnotations = "RuntimeInvisibleAnnotations";
    public static final String RuntimeVisibleParameterAnnotations = "RuntimeVisibleParameterAnnotations";
    public static final String RuntimeInvisibleParameterAnnotations = "RuntimeInvisibleParameterAnnotations";
    public static final String AnnotationDefault = "AnnotationDefault";
    public static final String BootstrapMethods = "BootstrapMethods";
    public static final String RuntimeVisibleTypeAnnotations = "RuntimeVisibleTypeAnnotations";
    public static final String RuntimeInvisibleTypeAnnotations = "RuntimeInvisibleTypeAnnotations";
    public static final String MethodParameters = "MethodParameters";
    public static final String Module = "Module";
    public static final String ModulePackages = "ModulePackages";
    public static final String ModuleMainClass = "ModuleMainClass";
    public static final String NestHost = "NestHost";
    public static final String NestMembers = "NestMembers";

    protected final int attributeNameIndex;
    protected final int attributeLength;

    public Attribute(int attributeNameIndex, int attributeLength) {
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = attributeLength;
    }

    public static Attribute parse(DataInputStream is, ConstantPool constantPool) throws Exception {
        int attributeNameIndex = is.readUnsignedShort();
        String attributeName = constantPool.getSlotAt(attributeNameIndex).getValue();
        int attributeLength = is.readInt();
        switch (attributeName) {
            case Code:
                return new CodeAttribute(attributeNameIndex, attributeLength, is, constantPool);
            case ConstantValue:
                break;
            case Deprecated:
                break;
            case Exceptions:
                return new ExceptionsAttribute(attributeNameIndex, attributeLength, is);
            case EnclosingMethod:
                break;
            case InnerClasses:
                break;
            case LineNumberTable:
                return new LineNumberTableAttribute(attributeNameIndex, attributeLength, is);
            case LocalVariableTable:
                return new LocalVariableTableAttribute(attributeNameIndex, attributeLength, is);
            case StackMapTable:
                break;
            case Signature:
                break;
            case SourceFile:
                return new SourceFileAttribute(attributeNameIndex, attributeLength, is);
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
                is.read(new byte[attributeLength]);
                break;
        }
        return null;
    }
}
