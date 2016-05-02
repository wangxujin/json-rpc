package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: InvalidRequestException <br>
 * Function: InvalidRequestException
 *
 * @author wangxujin
 */
public class InvalidRequestException extends RpcException {

    private static final long serialVersionUID = 3338851667656057431L;

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String s) {
        super(s);
    }

    public InvalidRequestException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidRequestException(Throwable throwable) {
        super(throwable);
    }
}
