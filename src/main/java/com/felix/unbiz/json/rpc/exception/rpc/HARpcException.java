package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: HARpcException <br>
 * Function: HARpcException
 *
 * @author wangxujin
 */
public class HARpcException extends RpcException {

    private static final long serialVersionUID = -6384079858457157127L;

    public HARpcException() {
        super();
    }

    public HARpcException(String s) {
        super(s);
    }

    public HARpcException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HARpcException(Throwable throwable) {
        super(throwable);
    }
}
