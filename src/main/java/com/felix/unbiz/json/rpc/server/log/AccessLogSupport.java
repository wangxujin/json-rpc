package com.felix.unbiz.json.rpc.server.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: AccessLogSupport <br/>
 * Function: 记录日志辅助类
 *
 * @author wangxujin
 */
public class AccessLogSupport {

    private static final Logger LOG = LoggerFactory.getLogger(AccessLogSupport.class);

    /**
     * 异步线程
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
            .availableProcessors() * 2);

    /**
     * 异步提交访问日志
     *
     * @param log
     *
     * @throws Exception
     */
    public void log(AccessLog log) throws Exception {
        if (log == null) {
            return;
        }
        executorService.submit(new LogTask(log));
    }

    /**
     * ClassName: LogTask <br/>
     * Function: 日志task
     */
    private class LogTask implements Runnable {

        private AccessLog log;

        public LogTask(AccessLog log) {
            this.log = log;
        }

        @Override
        public void run() {
            try {
                StringBuilder accessLog = new StringBuilder();
                accessLog.append(log.getFromIp()).append('\t').append(log.getServiceIntfName())
                        .append('\t');

                accessLog.append(log.getProtocol()).append('\t');

                accessLog.append(log.getEncoding()).append('\t');

                if (log.getRequest() != null) {
                    accessLog.append(log.getRequest()).append('\t');
                } else {
                    accessLog.append('\t');
                }

                if (log.getResponse() != null) {
                    accessLog.append(log.getResponse()).append('\t');
                } else {
                    accessLog.append('\t');
                }

                accessLog.append(log.getEndTime() - log.getStartTime());
                LOG.info(accessLog.toString());
            } catch (Exception e) {
                LOG.error("Error occurred when log access", e);
            } finally {
                log = null;
            }
        }
    }

}
