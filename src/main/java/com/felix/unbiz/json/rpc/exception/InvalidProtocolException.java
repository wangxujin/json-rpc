package com.felix.unbiz.json.rpc.exception;

/**
 * ClassName: InvalidProtocolException <br>
 * Function: 协议异常
 *
 * @author wangxujin
 */
public class InvalidProtocolException extends RuntimeException {

    private static final long serialVersionUID = -4362416327220499061L;

    public InvalidProtocolException() {
        super();
    }

    public InvalidProtocolException(String s) {
        super(s);
    }

    public InvalidProtocolException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidProtocolException(Throwable throwable) {
        super(throwable);
    }
}
