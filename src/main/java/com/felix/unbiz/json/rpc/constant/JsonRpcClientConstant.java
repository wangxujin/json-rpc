package com.felix.unbiz.json.rpc.constant;

/**
 * ClassName: JsonRpcClientConstant <br>
 * Function: Json rpc 客户端常量
 *
 * @author wangxujin
 */
public class JsonRpcClientConstant {

    /**
     * 默认客户端连接超时时间，单位毫秒
     */
    public static final int DEFAULT_CLIENT_CONN_TIMEOUT = 5000;

    /**
     * 默认客户端调用读超时时间，单位毫秒
     */
    public static final int DEFAULT_CLIENT_READ_TIMEOUT = 10000;

    /**
     * 是否启用HTTP Persistent Connections复用tcp连接
     */
    public static final String DEFAULT_HTTP_KEEPALIVE = "true";

    /**
     * 用HTTP Persistent Connections复用tcp连接最大数量
     */
    public static final String DEFAULT_MAX_CONNECTIONS = "8";

}
