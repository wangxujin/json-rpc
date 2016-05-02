package com.felix.unbiz.json.rpc.protocol;

import com.felix.unbiz.json.rpc.exception.rpc.CodecException;
import com.google.gson.JsonElement;

/**
 * ClassName: Serializer <br>
 * Function: 序列化反序列化接口
 *
 * @author wangxujin
 */
public interface Serializer {

    /**
     * 获取序列化协议类型
     *
     * @return
     */
    JsonProtocol getProtocol();

    /**
     * 反序列化解码成JsonElement
     *
     * @param encoding
     * @param req
     *
     * @return
     *
     * @throws CodecException
     */
    JsonElement deserialize(String encoding, byte[] req) throws CodecException;

    /**
     * 编码JsonElement为字节码
     *
     * @param encoding
     * @param res
     *
     * @return
     *
     * @throws CodecException
     */
    byte[] serialize(String encoding, JsonElement res) throws CodecException;

}
