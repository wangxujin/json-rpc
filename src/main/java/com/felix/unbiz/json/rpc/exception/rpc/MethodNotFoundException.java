package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: MethodNotFoundException <br>
 * Function: MethodNotFoundException
 *
 * @author wangxujin
 */
public class MethodNotFoundException extends RpcException {
    private static final long serialVersionUID = -498045494616319692L;

    public MethodNotFoundException() {
        super();
    }

    public MethodNotFoundException(String s) {
        super(s);
    }

    public MethodNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MethodNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
