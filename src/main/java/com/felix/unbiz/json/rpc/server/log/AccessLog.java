package com.felix.unbiz.json.rpc.server.log;

/**
 * ClassName: AccessLog <br>
 * Function: 日志对象
 *
 * @author wangxujin
 */
public class AccessLog {
    private String fromIp;
    private String protocol;
    private String encoding;
    private String serviceIntfName;
    private String request;
    private String response;
    private long startTime;
    private long endTime;

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getServiceIntfName() {
        return serviceIntfName;
    }

    public void setServiceIntfName(String serviceIntfName) {
        this.serviceIntfName = serviceIntfName;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
