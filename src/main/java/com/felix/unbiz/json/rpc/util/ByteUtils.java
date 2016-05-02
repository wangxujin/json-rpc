package com.felix.unbiz.json.rpc.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * ClassName: ByteUtil <br/>
 * Function: 字节码工具类
 *
 * @author wangxujin
 */
public final class ByteUtils {

    private ByteUtils() {
    }

    /**
     * 读取流中的字节码
     *
     * @param input
     * @param length
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] readStream(InputStream input, int length) throws IOException {
        byte[] bytes = new byte[length];
        int offset = 0;
        while (offset < bytes.length) {
            int bytesRead = input.read(bytes, offset, bytes.length - offset);
            if (bytesRead == -1) {
                break; // end of stream
            }
            offset += bytesRead;
        }
        return bytes;
    }

}
