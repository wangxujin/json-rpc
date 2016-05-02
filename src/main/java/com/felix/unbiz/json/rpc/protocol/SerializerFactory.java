package com.felix.unbiz.json.rpc.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.felix.unbiz.json.rpc.exception.InvalidProtocolException;

/**
 * ClassName: SerializerFactory <br>
 * Function: 序列化工厂类
 *
 * @author wangxujin
 */
public class SerializerFactory {

    private static final Map<String, Serializer> SERIALIZER_MAP = new ConcurrentHashMap<String, Serializer>();

    private static volatile boolean isInitialized = false;

    static {
        init();
    }

    private SerializerFactory() {

    }

    /**
     * 初始化
     */
    private static void init() {
        if (!isInitialized) {
            SERIALIZER_MAP.put(JsonProtocol.GSON.getName(), new JsonSerializer());
            isInitialized = true;
        }
    }

    /**
     * 根据http请求中的content-type尝试寻找序列化Serializer
     *
     * @param protocol 协议类型
     *
     * @return
     */
    public static Serializer getSerializerByProtocol(String protocol) {
        Serializer serializer = SERIALIZER_MAP.get(protocol);
        if (serializer != null) {
            return serializer;
        }
        throw new InvalidProtocolException("json rpc protocol not supported for " + protocol);
    }

}
