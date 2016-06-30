/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chrisplus.nfcage;

/**
 * Created by chrisplus on 30/6/16.
 */
public class Utils {
    private final static char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private Utils() {

    }

    public static byte[] toBytes(int a) {
        return new byte[]{(byte) (0x000000ff & (a >>> 24)),
                (byte) (0x000000ff & (a >>> 16)),
                (byte) (0x000000ff & (a >>> 8)), (byte) (0x000000ff & (a))};
    }

    public static boolean testBit(byte data, int bit) {
        final byte mask = (byte) ((1 << bit) & 0x000000FF);

        return (data & mask) == mask;
    }

    public static int toInt(byte[] b, int s, int n) {
        int ret = 0;

        final int e = s + n;
        for (int i = s; i < e; ++i) {
            ret <<= 8;
            ret |= b[i] & 0xFF;
        }
        return ret;
    }

    public static int toIntR(byte[] b, int s, int n) {
        int ret = 0;

        for (int i = s; (i >= 0 && n > 0); --i, --n) {
            ret <<= 8;
            ret |= b[i] & 0xFF;
        }
        return ret;
    }

    public static int toInt(byte... b) {
        int ret = 0;
        for (final byte a : b) {
            ret <<= 8;
            ret |= a & 0xFF;
        }
        return ret;
    }

    public static int toIntR(byte... b) {
        return toIntR(b, b.length - 1, b.length);
    }

    public static String toHexString(byte... d) {
        return (d == null || d.length == 0) ? "" : toHexString(d, 0, d.length);
    }

    public static String toHexString(byte[] d, int s, int n) {
        final char[] ret = new char[n * 2];
        final int e = s + n;

        int x = 0;
        for (int i = s; i < e; ++i) {
            final byte v = d[i];
            ret[x++] = HEX[0x0F & (v >> 4)];
            ret[x++] = HEX[0x0F & v];
        }
        return new String(ret);
    }

    public static String toHexStringR(byte[] d, int s, int n) {
        final char[] ret = new char[n * 2];

        int x = 0;
        for (int i = s + n - 1; i >= s; --i) {
            final byte v = d[i];
            ret[x++] = HEX[0x0F & (v >> 4)];
            ret[x++] = HEX[0x0F & v];
        }
        return new String(ret);
    }

    public static String ensureString(String str) {
        return str == null ? "" : str;
    }

    public static String toStringR(int n) {
        final StringBuilder ret = new StringBuilder(16).append('0');

        long N = 0xFFFFFFFFL & n;
        while (N != 0) {
            ret.append((int) (N % 100));
            N /= 100;
        }

        return ret.toString();
    }

    public static int parseInt(String txt, int radix, int def) {
        int ret;
        try {
            ret = Integer.valueOf(txt, radix);
        } catch (Exception e) {
            ret = def;
        }

        return ret;
    }

    public static int BCDtoInt(byte[] b, int s, int n) {
        int ret = 0;

        final int e = s + n;
        for (int i = s; i < e; ++i) {
            int h = (b[i] >> 4) & 0x0F;
            int l = b[i] & 0x0F;

            if (h > 9 || l > 9)
                return -1;

            ret = ret * 100 + h * 10 + l;
        }

        return ret;
    }

    public static int BCDtoInt(byte... b) {
        return BCDtoInt(b, 0, b.length);
    }

}
