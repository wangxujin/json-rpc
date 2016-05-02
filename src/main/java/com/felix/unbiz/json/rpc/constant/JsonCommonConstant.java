package com.felix.unbiz.json.rpc.constant;

import com.felix.unbiz.json.rpc.protocol.JsonProtocol;

/**
 * ClassName: JsonCommonConstant <br>
 * Function: 公共的常量定义
 *
 * @author wangxujin
 */
public class JsonCommonConstant {

    /**
     * json rpc 默认的版本号
     */
    public static final String DEFAULT_VERSION = "2.0";

    /**
     * http前缀
     */
    public static final String TRANSPORT_PROTOCOL = "http://";

    /**
     * 默认的编码方式
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 默认序列化方式
     */
    public static final String DEFAULT_PROTOCAL_CONTENT_TYPE = JsonProtocol.GSON.getName();

    /**
     * 默认的服务url前缀
     */
    public static final String TRANSPORT_URL_BASE_PATH = "api";
}
