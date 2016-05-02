package com.felix.unbiz.json.rpc.protocol;

import com.felix.unbiz.json.rpc.codec.Codec;
import com.felix.unbiz.json.rpc.exception.rpc.CodecException;
import com.felix.unbiz.json.rpc.util.Preconditions;
import com.google.gson.JsonElement;

/**
 * ClassName: AbstractSerializer <br>
 * Function: 序列化抽象层
 *
 * @author wangxujin
 */
public abstract class AbstractSerializer implements Serializer {

    /**
     * 编解码codec
     */
    protected Codec codec;

    /**
     * 设置编解码codec
     *
     * @return
     */
    public abstract void setCodec();

    public AbstractSerializer() {
        setCodec();
    }

    @Override
    public JsonElement deserialize(String encoding, byte[] req) throws CodecException {
        Preconditions.checkNotNull(codec, "codec not init");
        return codec.decode(encoding, req);
    }

    @Override
    public byte[] serialize(String encoding, JsonElement res) throws CodecException {
        Preconditions.checkNotNull(codec, "codec not init");
        return codec.encode(encoding, res);
    }
}
