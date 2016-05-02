package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: InvalidParamException <br>
 * Function: InvalidParamException
 *
 * @author wangxujin
 */
public class InvalidParamException extends RpcException {

    private static final long serialVersionUID = 7763535737106706262L;

    public InvalidParamException() {
        super();
    }

    public InvalidParamException(String s) {
        super(s);
    }

    public InvalidParamException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidParamException(Throwable throwable) {
        super(throwable);
    }
}
