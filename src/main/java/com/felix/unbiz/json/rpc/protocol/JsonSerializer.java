package com.felix.unbiz.json.rpc.protocol;

import com.felix.unbiz.json.rpc.codec.GsonCodec;

/**
 * ClassName: JsonSerializer <br>
 * Function: json协议序列化
 *
 * @author wangxujin
 */
public class JsonSerializer extends AbstractSerializer {

    @Override
    public void setCodec() {
        this.codec = new GsonCodec();
    }

    @Override
    public JsonProtocol getProtocol() {
        return JsonProtocol.GSON;
    }
}
