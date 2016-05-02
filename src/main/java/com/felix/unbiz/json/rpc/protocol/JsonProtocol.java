package com.felix.unbiz.json.rpc.protocol;

/**
 * ClassName: JsonProtocol <br>
 * Function: json-rpc支持的序列化协议
 *
 * @author wangxujin
 */
public enum JsonProtocol {

    /**
     * json
     */
    GSON("application/gson-rpc");

    private String name;

    JsonProtocol(String name) {
        this.name = name;
    }

    /**
     * 通过协议类型获取JsonProtocol
     *
     * @param name 协议类型
     *
     * @return
     */
    public static JsonProtocol getJsonProtocol(String name) {
        for (JsonProtocol protocol : values()) {
            if (protocol.getName().equalsIgnoreCase(name)) {
                return protocol;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
