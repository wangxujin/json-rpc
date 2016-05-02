package com.felix.unbiz.json.rpc.server.bo;

/**
 * ClassName: RpcResponse <br/>
 * Function: JsonRpc结果逻辑对象
 *
 * @author wangxujin
 */
public class RpcResponse {

    /**
     * 响应字节码
     */
    private byte[] response;

    public RpcResponse(byte[] response) {
        this.response = response;
    }

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

}