package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: RpcTimeoutException <br>
 * Function: RpcTimeoutException
 *
 * @author wangxujin
 */
public class RpcTimeoutException extends RpcException {

    private static final long serialVersionUID = 6554698973970359734L;

    public RpcTimeoutException() {
        super();
    }

    public RpcTimeoutException(String s) {
        super(s);
    }

    public RpcTimeoutException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RpcTimeoutException(Throwable throwable) {
        super(throwable);
    }
}
