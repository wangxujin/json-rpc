package com.felix.unbiz.json.rpc.server.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.felix.unbiz.json.rpc.server.bo.RpcRequest;
import com.felix.unbiz.json.rpc.server.bo.RpcResponse;
import com.felix.unbiz.json.rpc.server.callback.Callback;
import com.felix.unbiz.json.rpc.server.context.RpcContext;
import com.felix.unbiz.json.rpc.server.processor.RpcProcessor;

/**
 * ClassName: LoggerProcessor <br>
 * Function: 记录日志的RpcProcessor处理器
 *
 * @author wangxujin
 */
public class LoggerProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerProcessor.class);

    /**
     * 异步记录日志辅助类
     */
    private AccessLogSupport accessLogSupport = new AccessLogSupport();

    public void logProcess(RpcProcessor processor, RpcRequest request, Callback<RpcResponse> callback) {

        AccessLog accessLogInfo = new AccessLog();
        accessLogInfo.setStartTime(RpcContext.getContext().getAccessStartTime());
        accessLogInfo.setFromIp(RpcContext.getContext().getFromIp());
        accessLogInfo.setServiceIntfName(RpcContext.getContext().getServiceName());
        accessLogInfo.setProtocol(RpcContext.getContext().getProtocol());
        accessLogInfo.setEncoding(RpcContext.getContext().getEncoding());

        processor.process(request, callback);

        try {
            accessLogInfo.setRequest(RpcContext.getContext().getRequest());
            accessLogInfo.setResponse(RpcContext.getContext().getResponse());
            accessLogInfo.setEndTime(System.currentTimeMillis());
            accessLogSupport.log(accessLogInfo);
        } catch (Exception e) {
            LOG.error("Error occurred when logging, " + e.getMessage(), e);
        }
    }

}
