package com.felix.unbiz.json.rpc.server.context;

import com.felix.unbiz.json.rpc.constant.JsonCommonConstant;
import com.felix.unbiz.json.rpc.util.StringPool;
import com.felix.unbiz.json.rpc.util.StringUtils;
import com.felix.unbiz.json.rpc.util.ThreadHolder;
import com.google.gson.JsonElement;

/**
 * ClassName: RpcContext <br/>
 * Function: 服务端内部的上下文
 *
 * @author wangxujin
 */
public class RpcContext {

    private static final ThreadLocal<RpcContext> LOCAL = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    public static RpcContext getContext() {
        return LOCAL.get();
    }

    public static void removeContext() {
        LOCAL.remove();
        ThreadHolder.clean();
    }

    private static final String ACCESS_START_TIME = "startTime";
    private static final String PROTOCOL = "protocol";
    private static final String SERVICE_NAME = "serviceName";
    private static final String FROM_IP = "fromIp";
    private static final String ENCODING = "encoding";
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";

    public RpcContext setStartTime() {
        ThreadHolder.putContext(ACCESS_START_TIME, System.currentTimeMillis());
        return this;
    }

    public RpcContext setProtocol(String protocol) {
        ThreadHolder.putContext(PROTOCOL, protocol);
        return this;
    }

    public RpcContext setFromIp(String fromIp) {
        ThreadHolder.putContext(FROM_IP, fromIp);
        return this;
    }

    public RpcContext setServiceName(String serviceName) {
        ThreadHolder.putContext(SERVICE_NAME, serviceName);
        return this;
    }

    public RpcContext setEncoding(String encoding) {
        ThreadHolder.putContext(ENCODING, encoding.toUpperCase());
        return this;
    }

    public RpcContext setRequest(JsonElement reqJson) {
        ThreadHolder.putContext(REQUEST, reqJson);
        return this;
    }

    public RpcContext setResponse(JsonElement resJson) {
        ThreadHolder.putContext(RESPONSE, resJson);
        return this;
    }

    public String getProtocol() {
        return ThreadHolder.getContext(PROTOCOL);
    }

    public Long getAccessStartTime() {
        Long result = ThreadHolder.getContext(ACCESS_START_TIME);
        if (result == null) {
            return new Long(0L);
        }
        return result;
    }

    public String getServiceName() {
        return ThreadHolder.getContext(SERVICE_NAME);
    }

    public String getFromIp() {
        return ThreadHolder.getContext(FROM_IP);
    }

    public String getEncoding() {
        String encoding = ThreadHolder.getContext(ENCODING);
        if (StringUtils.isEmpty(encoding)) {
            return JsonCommonConstant.DEFAULT_ENCODING;
        }
        return encoding.toUpperCase();
    }

    public String getRequest() {
        JsonElement reqJson = ThreadHolder.getContext(REQUEST);
        if (reqJson == null) {
            return StringPool.Symbol.EMPTY;
        }
        return reqJson.toString();
    }

    public String getResponse() {
        JsonElement resJson = ThreadHolder.getContext(RESPONSE);
        if (resJson == null) {
            return StringPool.Symbol.EMPTY;
        }
        return resJson.toString();
    }

}