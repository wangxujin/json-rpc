package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: RpcException <br>
 * Function: RpcException
 *
 * @author wangxujin
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = -867472878042138524L;

    public RpcException() {
        super();
    }

    public RpcException(String s) {
        super(s);
    }

    public RpcException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RpcException(Throwable throwable) {
        super(throwable);
    }
}
