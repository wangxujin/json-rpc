package com.felix.unbiz.json.rpc.client.header;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: HeaderInfo <br>
 * Function: 头部认证信息
 *
 * @author wangxujin
 */
public class HeaderInfo {

    /**
     * 客户端调用的头部认证信息
     */
    private Map<String, String> headerMap = new HashMap<String, String>();

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public HeaderInfo setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
        return this;
    }
}
