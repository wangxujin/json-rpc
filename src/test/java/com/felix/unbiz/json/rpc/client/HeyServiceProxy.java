package com.felix.unbiz.json.rpc.client;

import java.util.Map;

/**
 * ClassName: HeyServiceProxy <br>
 * Function:  HeyService兼容老的头部认证信息调用方式
 *
 * @author wangxujin
 */
public interface HeyServiceProxy {

    String say(String word, Map<String, String> headerMap);
}
