package my.jvm.hotspot.oops;

import my.jvm.hotspot.runtime.ClassConstants;

@SuppressWarnings("InnerClassMayBeStatic")
public class ConstantPool implements ClassConstants {
    private final Slot[] slots;

    public ConstantPool(Slot[] slots) {
        this.slots = slots;
    }

    public Slot getSlotAt(int idx) {
        return slots[idx];
    }

    public abstract class Slot {
        protected byte tag;

        public Slot(byte tag) {
            this.tag = tag;
        }

        public abstract String getValue();

        public abstract String getTagString();
    }

    public class Utf8Slot extends Slot {
        private final int len;
        private final String val;

        public Utf8Slot(byte tag, int len, byte[] bytes) {
            super(tag);
            this.len = len;
            this.val = new String(bytes);
        }

        @Override
        public String getTagString() {
            return "Utf8";
        }

        @Override
        public String getValue() {
            return val;
        }
    }

    public class IntegerSlot extends Slot {
        private final int val;

        public IntegerSlot(byte tag, int val) {
            super(tag);
            this.val = val;
        }

        @Override
        public String getTagString() {
            return "Integer";
        }

        @Override
        public String getValue() {
            return val + "";
        }
    }

    public class FloatSlot extends Slot {
        private final float val;

        public FloatSlot(byte tag, float val) {
            super(tag);
            this.val = val;
        }

        @Override
        public String getTagString() {
            return "Float";
        }

        @Override
        public String getValue() {
            return val + "";
        }
    }

    public class LongSlot extends Slot {
        private final long val;

        public LongSlot(byte tag, long val) {
            super(tag);
            this.val = val;
        }

        @Override
        public String getTagString() {
            return "Long";
        }

        @Override
        public String getValue() {
            return val + "";
        }
    }

    public class DoubleSlot extends Slot {
        private final double val;

        public DoubleSlot(byte tag, double val) {
            super(tag);
            this.val = val;
        }

        @Override
        public String getTagString() {
            return "Double";
        }

        @Override
        public String getValue() {
            return val + "";
        }
    }

    public class ClassSlot extends Slot {
        private final int index;

        public ClassSlot(byte tag, int index) {
            super(tag);
            this.index = index;
        }

        @Override
        public String getTagString() {
            return "Class";
        }

        @Override
        public String getValue() {
            return ConstantPool.this.getSlotAt(index).getValue();
        }
    }

    public class StringSlot extends Slot {
        private final int index;

        public StringSlot(byte tag, int index) {
            super(tag);
            this.index = index;
        }

        @Override
        public String getTagString() {
            return "String";
        }

        @Override
        public String getValue() {
            return ConstantPool.this.getSlotAt(index).getValue();
        }
    }

    public class FieldrefSlot extends Slot {
        private final int index1;
        private final int index2;

        public FieldrefSlot(byte tag, int index1, int index2) {
            super(tag);
            this.index1 = index1;
            this.index2 = index2;
        }

        @Override
        public String getTagString() {
            return "Fieldref";
        }

        @Override
        public String getValue() {
            return ConstantPool.this.getSlotAt(index1).getValue() +
                    "." +
                    ConstantPool.this.getSlotAt(index2).getValue();
        }
    }

    public class MethodrefSlot extends Slot {
        private final int index1;
        private final int index2;

        public MethodrefSlot(byte tag, int index1, int index2) {
            super(tag);
            this.index1 = index1;
            this.index2 = index2;
        }

        @Override
        public String getTagString() {
            return "Methodref";
        }

        @Override
        public String getValue() {
            return ConstantPool.this.getSlotAt(index1).getValue() +
                    "#" +
                    ConstantPool.this.getSlotAt(index2).getValue();
        }
    }

    public class InterfaceMethodrefSlot extends Slot {
        private final int index1;
        private final int index2;

        public InterfaceMethodrefSlot(byte tag, int index1, int index2) {
            super(tag);
            this.index1 = index1;
            this.index2 = index2;
        }

        @Override
        public String getTagString() {
            return "InterfaceMethodref";
        }

        @Override
        public String getValue() {
            return ConstantPool.this.getSlotAt(index1).getValue() +
                    ConstantPool.this.getSlotAt(index2).getValue();
        }
    }

    public class NameAndTypeSlot extends Slot {
        private final int index1;
        private final int index2;

        public NameAndTypeSlot(byte tag, int index1, int index2) {
            super(tag);
            this.index1 = index1;
            this.index2 = index2;
        }

        @Override
        public String getTagString() {
            return "NameAndType";
        }

        @Override
        public String getValue() {
            return ConstantPool.this.getSlotAt(index1).getValue() +
                    ":" +
                    ConstantPool.this.getSlotAt(index2).getValue();
        }
    }
}
