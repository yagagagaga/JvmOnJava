package tools;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class ByteUtils {

    public static char[] hexBytes =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static byte[][][] decimalBytes = new byte[256][256][];
    public static byte[] DigitTens = new byte[1000];
    public static byte[] DigitOnes = new byte[1000];
    public static byte[] DigitHundreds = new byte[1000];

    public static byte[][] decimalChars = new byte[10000][];
    public static byte[][] paddingChars = new byte[10000][];

    static {
        for (int i = 0; i < 256; ++i) {
            for (int j = 0; j < 256; ++j) {
                int value = i << 8 | j;
                decimalBytes[i][j] = Integer.toString(value).getBytes(UTF_8);
            }
        }

        for (int i = 0; i < 10000; ++i) {
            decimalChars[i] = Integer.toString(i).getBytes(UTF_8);
            paddingChars[i] = new byte[4];
            int padding = 4 - decimalChars[i].length;

            int j = 0;
            for (; j < padding; ++j) {
                paddingChars[i][j] = '0';
            }

            for (; j < 4; ++j) {
                paddingChars[i][j] = decimalChars[i][j - padding];
            }
        }

        for (int i = 0; i < 1000; ++i) {
            byte[] buffer = Integer.toString(i).getBytes(UTF_8);

            if (buffer.length == 3) {
                DigitHundreds[i] = buffer[0];
                DigitTens[i] = buffer[1];
                DigitOnes[i] = buffer[2];
            } else if (buffer.length == 2) {
                DigitHundreds[i] = '0';
                DigitTens[i] = buffer[0];
                DigitOnes[i] = buffer[1];
            } else {
                DigitHundreds[i] = '0';
                DigitTens[i] = '0';
                DigitOnes[i] = buffer[0];
            }
        }
    }

    private ByteUtils() {
        throw new IllegalStateException("Please do not instantiate the tool class!");
    }

    /**
     * @see #dumpBytes(byte[], int, int)
     */
    public static String dumpBytes(byte[] buffer) {
        if (buffer == null) {
            return null;
        }
        return dumpBytes(buffer, 0, buffer.length);
    }

    /**
     * 将字节数组转成十六进制的字符串
     */
    public static String dumpBytes(byte[] buffer, int offset, int length) {
        final StringBuilder sb = new StringBuilder("0x");

        for (int i = offset; i < offset + length; i++) {
            byte b = buffer[i];

            sb.append(hexBytes[(b >>> 4) & 0x0f]);
            sb.append(hexBytes[b & 0x0f]);
        }

        return sb.toString();
    }

    /**
     * 将十六进制的字符串转成字节数组
     */
    public static byte[] loadBytes(String dumpStr) {
        final char[] chars = dumpStr.toCharArray();
        byte[] res = new byte[(chars.length - 2) / 2];
        for (int i = 2; i < chars.length; i += 2) {
            final int a = Integer.parseInt(chars[i] + "", 16);
            final int b = Integer.parseInt(chars[i + 1] + "", 16);
            res[i / 2 - 1] = (byte) ((a << 4) + b);
        }
        return res;
    }

    public static byte[] copy(byte[] src, int pos, int len) {
        byte[] dst = new byte[len];
        System.arraycopy(src, pos, dst, 0, len);
        return dst;
    }

// ------------------ bytes --> number ------------------

    public static float toFloat(byte[] arr) {
        int accum = 0;
        accum = accum|(arr[0] & 0xff) << 0;
        accum = accum|(arr[1] & 0xff) << 8;
        accum = accum|(arr[2] & 0xff) << 16;
        accum = accum|(arr[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }

    public static double toDouble(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    /**
     * @return 无符号长整型
     * @see #toLong(byte[], int, int)
     * @deprecated 这个方法算出来的值不对，别用
     */
    @Deprecated
    public static long toUnsignLong(byte[] buffer, int offset, int length) {

        return toLong(buffer, offset, length) & 0xFFFFFFFFL;
    }

    public static long toLong(byte[] buffer) {
        return toLong(buffer, 0);
    }

    public static long toLong(byte[] buffer, int offset) {
        return toLong(buffer, offset, buffer.length);
    }

    /**
     * 将字节数组转成数字
     *
     * @param buffer 字节数组
     * @param offset 从哪个偏移量开始转换
     * @param length 转换多少个字节
     * @return 有符号长整型
     */
    public static long toLong(byte[] buffer, int offset, int length) {
        long result = 0;

        for (int i = 0; i < length; ++i) {
            result |= (buffer[offset + i] & 0xffL) << ((length - i - 1) << 3);
        }

        return result;
    }

    public static int toInt(byte[] buffer) {
        return toInt(buffer, 0);
    }

    public static int toInt(byte[] buffer, int offset) {
        return (((buffer[offset]) << 24) |
                ((buffer[offset + 1] & 0xff) << 16) |
                ((buffer[offset + 2] & 0xff) << 8) |
                ((buffer[offset + 3] & 0xff)));
    }

    public static long toUnsignedInt(byte[] buffer) {
        return toUnsignedInt(buffer, 0);
    }

    public static long toUnsignedInt(byte[] buffer, int offset) {
        return toInt(buffer, offset) & 0xFFFFFFFFL;
    }

    public static short toShort(byte[] buffer) {
        return toShort(buffer, 0);
    }

    public static short toShort(byte[] buffer, int offset) {
        return (short) (buffer[offset] << 8 | (buffer[offset + 1] & 0xFF));
    }

    public static int toUnsignedShort(byte[] buffer) {
        return toUnsignedShort(buffer, 0);
    }

    public static int toUnsignedShort(byte[] buffer, int offset) {
        return toShort(buffer, offset) & 0xFFFF;
    }

// ------------------ number --> bytes ------------------

    public static byte[] toBytes(short val) {
        byte[] b = new byte[2];
        b[1] = (byte) val;
        val >>= 8;
        b[0] = (byte) val;
        return b;
    }

    public static byte[] toBytes(int val) {
        byte[] b = new byte[4];
        for (int i = 3; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

    public static byte[] toBytes(long val) {
        byte[] b = new byte[8];
        for (int i = 7; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

    public static byte[] toBytes(long val, int length) {
        byte[] b = new byte[length];
        for (int i = b.length - 1; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

// ------------------ utils function ------------------

    public static byte[] copy(byte[]... byteArrays) {
        int len = 0;
        for (byte[] byteArray : byteArrays) {
            len += byteArray.length;
        }
        byte[] res = new byte[len];
        int i = 0;
        for (byte[] byteArray : byteArrays) {
            System.arraycopy(byteArray, 0, res, i, byteArray.length);
            i += byteArray.length;
        }
        return res;
    }

    public static boolean isAllFF(byte[] data, int offset, int length) {
        boolean allFF = true;
        for (int i = 0; i < length; i++) {
            allFF &= data[i + offset] == -1;
        }
        return allFF;
    }

    public static byte[] fillFF(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length could not less than 0");
        }

        byte[] res = new byte[length];
        Arrays.fill(res, (byte) -1);
        return res;
    }
}
