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

package com.chrisplus.nfcage.protocol;

import com.chrisplus.nfcage.Utils;

import android.nfc.tech.IsoDep;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by chrisplus on 30/6/16.
 */
public class Iso7816 {
    public static final byte[] EMPTY = {0};
    public static final short SW_NO_ERROR = (short) 0x9000;
    public static final short SW_DESFIRE_NO_ERROR = (short) 0x9100;
    public static final short SW_BYTES_REMAINING_00 = 0x6100;
    public static final short SW_WRONG_LENGTH = 0x6700;
    public static final short SW_SECURITY_STATUS_NOT_SATISFIED = 0x6982;
    public static final short SW_FILE_INVALID = 0x6983;
    public static final short SW_DATA_INVALID = 0x6984;
    public static final short SW_CONDITIONS_NOT_SATISFIED = 0x6985;
    public static final short SW_COMMAND_NOT_ALLOWED = 0x6986;
    public static final short SW_APPLET_SELECT_FAILED = 0x6999;
    public static final short SW_WRONG_DATA = 0x6A80;
    public static final short SW_FUNC_NOT_SUPPORTED = 0x6A81;
    public static final short SW_FILE_NOT_FOUND = 0x6A82;
    public static final short SW_RECORD_NOT_FOUND = 0x6A83;
    public static final short SW_INCORRECT_P1P2 = 0x6A86;
    public static final short SW_WRONG_P1P2 = 0x6B00;
    public static final short SW_CORRECT_LENGTH_00 = 0x6C00;
    public static final short SW_INS_NOT_SUPPORTED = 0x6D00;
    public static final short SW_CLA_NOT_SUPPORTED = 0x6E00;
    public static final short SW_UNKNOWN = 0x6F00;
    public static final short SW_FILE_FULL = 0x6A84;
    protected byte[] data;

    protected Iso7816() {
        data = Iso7816.EMPTY;
    }

    protected Iso7816(byte[] bytes) {
        data = (bytes == null) ? Iso7816.EMPTY : bytes;
    }

    public boolean match(byte[] bytes) {
        return match(bytes, 0);
    }

    public boolean match(byte[] bytes, int start) {
        final byte[] data = this.data;
        if (data.length <= bytes.length - start) {
            for (final byte v : data) {
                if (v != bytes[start++])
                    return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean match(byte tag) {
        return (data.length == 1 && data[0] == tag);
    }

    public boolean match(short tag) {
        final byte[] data = this.data;
        if (data.length == 2) {
            final byte d0 = (byte) (0x000000FF & (tag >> 8));
            final byte d1 = (byte) (0x000000FF & tag);
            return (data[0] == d0 && data[1] == d1);
        }

        return (tag >= 0 && tag <= 255) ? match((byte) tag) : false;
    }

    public int size() {
        return data.length;
    }

    public byte[] getBytes() {
        return data;
    }

    public byte[] getBytes(int start, int count) {
        return Arrays.copyOfRange(data, start, start + count);
    }

    public int toInt() {
        return Utils.toInt(getBytes());
    }

    public int toIntR() {
        return Utils.toIntR(getBytes());
    }

    @Override
    public String toString() {
        return Utils.toHexString(data, 0, data.length);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || !(obj instanceof Iso7816))
            return false;

        return match(((Iso7816) obj).getBytes(), 0);
    }

    public final static class ID extends Iso7816 {
        public ID(byte... bytes) {
            super(bytes);
        }
    }

    public static class Response extends Iso7816 {
        public static final byte[] EMPTY = {};
        public static final byte[] ERROR = {0x6F, 0x00}; // SW_UNKNOWN

        public Response(byte[] bytes) {
            super((bytes == null || bytes.length < 2) ? Response.ERROR : bytes);
        }

        public byte getSw1() {
            return data[data.length - 2];
        }

        public byte getSw2() {
            return data[data.length - 1];
        }

        public String getSw12String() {
            int sw1 = getSw1() & 0x000000FF;
            int sw2 = getSw2() & 0x000000FF;
            return String.format("0x%02X%02X", sw1, sw2);
        }

        public short getSw12() {
            final byte[] d = this.data;
            int n = d.length;
            return (short) ((d[n - 2] << 8) | (0xFF & d[n - 1]));
        }

        public boolean isOkey() {
            return equalsSw12(SW_NO_ERROR);
        }

        public boolean equalsSw12(short val) {
            return getSw12() == val;
        }

        public int size() {
            return data.length - 2;
        }

        public byte[] getBytes() {
            return isOkey() ? Arrays.copyOfRange(data, 0, size())
                    : Response.EMPTY;
        }
    }

    public final static class MifareDResponse extends Response {
        public MifareDResponse(byte[] bytes) {
            super(bytes);
        }
    }


    public final static class StdTag {
        private static final byte CH_STA_OK = (byte) 0x90;
        private static final byte CH_STA_MORE = (byte) 0x61;
        private static final byte CH_STA_LE = (byte) 0x6C;
        private static final byte CMD_GETRESPONSE[] = {0, (byte) 0xC0, 0, 0,
                0,};
        private final IsoDep nfcTag;
        private ID id;

        public StdTag(IsoDep tag) {
            nfcTag = tag;
            id = new ID(tag.getTag().getId());
        }

        public ID getID() {
            return id;
        }

        public Response getBalance(int p1, boolean isEP) throws IOException {
            final byte[] cmd = {(byte) 0x80, // CLA Class
                    (byte) 0x5C, // INS Instruction
                    (byte) p1, // P1 Parameter 1
                    (byte) (isEP ? 2 : 1), // P2 Parameter 2
                    (byte) 0x04, // Le
            };

            return new Response(transceive(cmd));
        }

        public Response readRecord(int sfi, int index) throws IOException {
            final byte[] cmd = {(byte) 0x00, // CLA Class
                    (byte) 0xB2, // INS Instruction
                    (byte) index, // P1 Parameter 1
                    (byte) ((sfi << 3) | 0x04), // P2 Parameter 2
                    (byte) 0x00, // Le
            };

            return new Response(transceive(cmd));
        }

        public Response readRecord(int sfi) throws IOException {
            final byte[] cmd = {(byte) 0x00, // CLA Class
                    (byte) 0xB2, // INS Instruction
                    (byte) 0x01, // P1 Parameter 1
                    (byte) ((sfi << 3) | 0x05), // P2 Parameter 2
                    (byte) 0x00, // Le
            };

            return new Response(transceive(cmd));
        }

        public Response readBinary(int sfi) throws IOException {
            final byte[] cmd = {(byte) 0x00, // CLA Class
                    (byte) 0xB0, // INS Instruction
                    (byte) (0x00000080 | (sfi & 0x1F)), // P1 Parameter 1
                    (byte) 0x00, // P2 Parameter 2
                    (byte) 0x00, // Le
            };

            return new Response(transceive(cmd));
        }

        public Response readData(int sfi) throws IOException {
            final byte[] cmd = {(byte) 0x80, // CLA Class
                    (byte) 0xCA, // INS Instruction
                    (byte) 0x00, // P1 Parameter 1
                    (byte) (sfi & 0x1F), // P2 Parameter 2
                    (byte) 0x00, // Le
            };

            return new Response(transceive(cmd));
        }

        public Response getData(short tag) throws IOException {
            final byte[] cmd = {
                    (byte) 0x80, // CLA Class
                    (byte) 0xCA, // INS Instruction
                    (byte) ((tag >> 8) & 0xFF), (byte) (tag & 0xFF),
                    (byte) 0x00, // Le
            };

            return new Response(transceive(cmd));
        }

        public Response readData(short tag) throws IOException {
            final byte[] cmd = {(byte) 0x80, // CLA Class
                    (byte) 0xCA, // INS Instruction
                    (byte) ((tag >> 8) & 0xFF), // P1 Parameter 1
                    (byte) (tag & 0x1F), // P2 Parameter 2
                    (byte) 0x00, // Lc
                    (byte) 0x00, // Le
            };

            return new Response(transceive(cmd));
        }

        public Response selectByID(byte... id) throws IOException {
            ByteBuffer buff = ByteBuffer.allocate(id.length + 6);
            buff.put((byte) 0x00) // CLA Class
                    .put((byte) 0xA4) // INS Instruction
                    .put((byte) 0x00) // P1 Parameter 1
                    .put((byte) 0x00) // P2 Parameter 2
                    .put((byte) id.length) // Lc
                    .put(id).put((byte) 0x00); // Le

            return new Response(transceive(buff.array()));
        }

        public Response selectByName(byte... name) throws IOException {
            ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
            buff.put((byte) 0x00) // CLA Class
                    .put((byte) 0xA4) // INS Instruction
                    .put((byte) 0x04) // P1 Parameter 1
                    .put((byte) 0x00) // P2 Parameter 2
                    .put((byte) name.length) // Lc
                    .put(name).put((byte) 0x00); // Le

            return new Response(transceive(buff.array()));
        }

        public Response getProcessingOptions(byte... pdol) throws IOException {
            ByteBuffer buff = ByteBuffer.allocate(pdol.length + 6);
            buff.put((byte) 0x80) // CLA Class
                    .put((byte) 0xA8) // INS Instruction
                    .put((byte) 0x00) // P1 Parameter 1
                    .put((byte) 0x00) // P2 Parameter 2
                    .put((byte) pdol.length) // Lc
                    .put(pdol).put((byte) 0x00); // Le

            return new Response(transceive(buff.array()));
        }

        public void connect() throws IOException {
            nfcTag.connect();
        }

        public void close() throws IOException {
            nfcTag.close();
        }

        public byte[] transceive(final byte[] cmd) throws IOException {
            try {
                byte[] rsp = null;

                byte c[] = cmd;
                do {
                    byte[] r = nfcTag.transceive(c);
                    if (r == null)
                        break;

                    int N = r.length - 2;
                    if (N < 0) {
                        rsp = r;
                        break;
                    }

                    if (r[N] == CH_STA_LE) {
                        c[c.length - 1] = r[N + 1];
                        continue;
                    }

                    if (rsp == null) {
                        rsp = r;
                    } else {
                        int n = rsp.length;
                        N += n;

                        rsp = Arrays.copyOf(rsp, N);

                        n -= 2;
                        for (byte i : r)
                            rsp[n++] = i;
                    }

                    if (r[N] != CH_STA_MORE)
                        break;

                    byte s = r[N + 1];
                    if (s != 0) {
                        c = CMD_GETRESPONSE.clone();
                    } else {
                        rsp[rsp.length - 1] = CH_STA_OK;
                        break;
                    }

                } while (true);

                return rsp;

            } catch (Exception e) {
                return Response.ERROR;
            }
        }
    }
}
