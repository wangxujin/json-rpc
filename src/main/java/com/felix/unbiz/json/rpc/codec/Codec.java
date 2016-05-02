package com.felix.unbiz.json.rpc.codec;

import com.felix.unbiz.json.rpc.exception.rpc.CodecException;
import com.google.gson.JsonElement;

/**
 * ClassName: Codec <br/>
 * Function: 编码接口，用作序列化以及反序列化
 *
 * @author wangxujin
 */
public interface Codec {

    /**
     * 编码序列化
     *
     * @param encoding
     * @param req
     *
     * @return
     *
     * @throws CodecException
     */
    JsonElement decode(String encoding, byte[] req) throws CodecException;

    /**
     * 解码反序列化
     *
     * @param encoding
     * @param res
     *
     * @return
     *
     * @throws CodecException
     */
    byte[] encode(String encoding, JsonElement res) throws CodecException;
}
