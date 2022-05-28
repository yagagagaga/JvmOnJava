package my.jvm.hotspot.oops;

import my.jvm.hotspot.runtime.ClassConstants;
import tools.ByteUtils;

import static my.jvm.hotspot.classfile.ClassFileParser.*;

import java.io.InputStream;
import java.util.Arrays;

public class AttributeInfo implements ClassConstants {
    protected String attributeName;
    protected int attributeLength;

    public AttributeInfo(String attributeName, int attributeLength) {
        this.attributeName = attributeName;
        this.attributeLength = attributeLength;
    }

    public static AttributeInfo parse(InputStream is, ConstantPool constantPool) throws Exception {
        is.read(U2);
        int attributeNameIndex = ByteUtils.toUnsignedShort(U2);
        String attributeName = constantPool.getSlotAt(attributeNameIndex).getValue();
        is.read(U4);
        int attributeLength = ByteUtils.toInt(U4);
        switch (attributeName) {
            case Code:
                return new CodeAttributeInfo(attributeName, attributeLength, is, constantPool);
            case ConstantValue:
                break;
            case Deprecated:
                break;
            case Exceptions:
                return new ExceptionsAttributeInfo(attributeName, attributeLength, is, constantPool);
            case EnclosingMethod:
                break;
            case InnerClasses:
                break;
            case LineNumberTable:
                return new LineNumberTableAttributeInfo(attributeName, attributeLength, is, constantPool);
            case LocalVariableTable:
                return new LocalVariableTableAttributeInfo(attributeName, attributeLength, is, constantPool);
            case StackMapTable:
                break;
            case Signature:
                break;
            case SourceFile:
                return new SourceFileAttributeInfo(attributeName, attributeLength, is, constantPool);
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

    static class CodeAttributeInfo extends AttributeInfo {
        int maxStack;
        int maxLocals;
        int codeLength;
        byte[] code;
        int exceptionTableLength;
        ExceptionsAttributeInfo[] exceptionTable;
        int attributeCount;
        AttributeInfo[] attributes;

        public CodeAttributeInfo(String attributeName, int attributeLength, InputStream is, ConstantPool constantPool) throws Exception {
            super(attributeName, attributeLength);
            is.read(U2);
            this.maxStack = ByteUtils.toUnsignedShort(U2);
            is.read(U2);
            this.maxLocals = ByteUtils.toUnsignedShort(U2);
            is.read(U4);
            this.codeLength = ByteUtils.toInt(U4);
            this.code = new byte[codeLength];
            is.read(code);
            is.read(U2);
            this.exceptionTableLength = ByteUtils.toUnsignedShort(U2);
            exceptionTable = new ExceptionsAttributeInfo[exceptionTableLength];
            for (int j = 0; j < exceptionTableLength; j++) {
                exceptionTable[j] = (ExceptionsAttributeInfo) AttributeInfo.parse(is, constantPool);
            }
            is.read(U2);
            this.attributeCount = ByteUtils.toUnsignedShort(U2);
            attributes = new AttributeInfo[attributeCount];
            for (int j = 0; j < attributeCount; j++) {
                attributes[j] = AttributeInfo.parse(is, constantPool);
            }
        }

        @Override
        public String toString() {
            return "CodeAttributeInfo{" +
                    "attributeName='" + attributeName + '\'' +
                    ", attributeLength=" + attributeLength +
                    ", maxStack=" + maxStack +
                    ", maxLocals=" + maxLocals +
                    ", codeLength=" + codeLength +
                    ", code=" + Arrays.toString(code) +
                    ", exceptionTableLength=" + exceptionTableLength +
                    ", exceptionTable=" + Arrays.toString(exceptionTable) +
                    ", attributeCount=" + attributeCount +
                    ", attributes=" + Arrays.toString(attributes) +
                    '}';
        }
    }

    static class ExceptionsAttributeInfo extends AttributeInfo {
        int numberOfExceptions;
        String[] exceptionTable;

        public ExceptionsAttributeInfo(String attributeName, int attributeLength, InputStream is, ConstantPool constantPool) throws Exception {
            super(attributeName, attributeLength);
            is.read(U2);
            this.numberOfExceptions = ByteUtils.toUnsignedShort(U2);
            exceptionTable = new String[numberOfExceptions];
            for (int i = 0; i < numberOfExceptions; i++) {
                is.read(U2);
                int exceptionIndex = ByteUtils.toUnsignedShort(U2);
                exceptionTable[i] = constantPool.getSlotAt(exceptionIndex).getValue();
            }
        }

        @Override
        public String toString() {
            return "ExceptionAttributeInfo{" +
                    "attributeName='" + attributeName + '\'' +
                    ", attributeLength=" + attributeLength +
                    ", numberOfExceptions=" + numberOfExceptions +
                    ", exceptionTable=" + Arrays.toString(exceptionTable) +
                    '}';
        }
    }

    static class LineNumberTableAttributeInfo extends AttributeInfo {
        int lineNumberTableLength;
        LineNumberInfo[] lineNumberTable;

        public LineNumberTableAttributeInfo(String attributeName, int attributeLength, InputStream is, ConstantPool constantPool) throws Exception {
            super(attributeName, attributeLength);
            is.read(U2);
            this.lineNumberTableLength = ByteUtils.toUnsignedShort(U2);
            this.lineNumberTable = new LineNumberInfo[lineNumberTableLength];

            for (int i = 0; i < lineNumberTableLength; i++) {
                is.read(U2);
                int startPc = ByteUtils.toUnsignedShort(U2);
                is.read(U2);
                int lineNumber = ByteUtils.toUnsignedShort(U2);
                lineNumberTable[i] = new LineNumberInfo(startPc, lineNumber);
            }
        }

        @Override
        public String toString() {
            return "LineNumberTableAttributeInfo{" +
                    "attributeName='" + attributeName + '\'' +
                    ", attributeLength=" + attributeLength +
                    ", lineNumberTableLength=" + lineNumberTableLength +
                    ", lineNumberTable=" + Arrays.toString(lineNumberTable) +
                    '}';
        }
    }

    static class LineNumberInfo {
        int startPc;
        int lineNumber;

        public LineNumberInfo(int startPc, int lineNumber) {
            this.startPc = startPc;
            this.lineNumber = lineNumber;
        }

        @Override
        public String toString() {
            return "LineNumberInfo{" +
                    "startPc=" + startPc +
                    ", lineNumber=" + lineNumber +
                    '}';
        }
    }

    static class LocalVariableTableAttributeInfo extends AttributeInfo {
        int localVariableTableLength;
        LocalVariableInfo[] localVariableTable;

        public LocalVariableTableAttributeInfo(String attributeName, int attributeLength, InputStream is, ConstantPool constantPool) throws Exception {
            super(attributeName, attributeLength);
            is.read(U2);
            this.localVariableTableLength = ByteUtils.toUnsignedShort(U2);
            this.localVariableTable = new LocalVariableInfo[localVariableTableLength];
            for (int i = 0; i < localVariableTableLength; i++) {
                is.read(U2);
                int startPc = ByteUtils.toUnsignedShort(U2);
                is.read(U2);
                int length = ByteUtils.toUnsignedShort(U2);
                is.read(U2);
                int nameIndex = ByteUtils.toUnsignedShort(U2);
                String name = constantPool.getSlotAt(nameIndex).getValue();
                is.read(U2);
                int descriptorIndex = ByteUtils.toUnsignedShort(U2);
                String descriptor = constantPool.getSlotAt(descriptorIndex).getValue();
                is.read(U2);
                int index = ByteUtils.toUnsignedShort(U2);
                localVariableTable[i] = new LocalVariableInfo(startPc, length, name, descriptor, index);
            }
        }

        @Override
        public String toString() {
            return "LocalVariableTable{" +
                    "attributeName='" + attributeName + '\'' +
                    ", attributeLength=" + attributeLength +
                    ", localVariableTableLength=" + localVariableTableLength +
                    ", localVariableTable=" + Arrays.toString(localVariableTable) +
                    '}';
        }
    }

    static class LocalVariableInfo {
        int startPc;
        int length;
        String name;
        String descriptor;
        int index;

        public LocalVariableInfo(int startPc, int length, String name, String descriptor, int index) {
            this.startPc = startPc;
            this.length = length;
            this.name = name;
            this.descriptor = descriptor;
            this.index = index;
        }

        @Override
        public String toString() {
            return "LocalVariableInfo{" +
                    "startPc=" + startPc +
                    ", length=" + length +
                    ", name='" + name + '\'' +
                    ", descriptor='" + descriptor + '\'' +
                    ", index=" + index +
                    '}';
        }
    }

    static class SourceFileAttributeInfo extends AttributeInfo {
        String sourceFile;

        public SourceFileAttributeInfo(String attributeName, int attributeLength, InputStream is, ConstantPool constantPool) throws Exception {
            super(attributeName, attributeLength);
            is.read(U2);
            int index = ByteUtils.toUnsignedShort(U2);
            this.sourceFile = constantPool.getSlotAt(index).getValue();;
        }

        @Override
        public String toString() {
            return "SourceFileAttributeInfo{" +
                    "attributeName='" + attributeName + '\'' +
                    ", attributeLength=" + attributeLength +
                    ", sourceFile='" + sourceFile + '\'' +
                    '}';
        }
    }
}
