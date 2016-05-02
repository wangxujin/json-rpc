package com.felix.unbiz.json.rpc.server.log;

import com.felix.unbiz.json.rpc.server.bo.RpcRequest;
import com.felix.unbiz.json.rpc.server.bo.RpcResponse;
import com.felix.unbiz.json.rpc.server.callback.Callback;
import com.felix.unbiz.json.rpc.server.processor.RpcProcessor;

/**
 * ClassName: LoggerBuilder <br>
 * Function: 构造包含记录日志的RpcProcessor处理器
 *
 * @author wangxujin
 */
public class LoggerBuilder {

    /**
     * 记录日志的RpcProcessor处理器
     */
    private static LoggerProcessor loggerProcessor = new LoggerProcessor();

    /**
     * 构造包含记录日志的RpcProcessor处理器
     *
     * @param processor 核心处理器
     *
     * @return
     */
    public static RpcProcessor buildLoggerProcessor(final RpcProcessor processor) {
        final RpcProcessor real = new RpcProcessor() {
            @Override
            public void process(RpcRequest request, Callback<RpcResponse> callback) {
                loggerProcessor.logProcess(processor, request, callback);
            }
        };
        return real;
    }
}
