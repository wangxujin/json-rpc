package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: ServerErrorException <br>
 * Function: ServerErrorException
 *
 * @author wangxujin
 */
public class ServerErrorException extends RpcException {

    private static final long serialVersionUID = 9014899261308045705L;

    public ServerErrorException() {
        super();
    }

    public ServerErrorException(String s) {
        super(s);
    }

    public ServerErrorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServerErrorException(Throwable throwable) {
        super(throwable);
    }
}
