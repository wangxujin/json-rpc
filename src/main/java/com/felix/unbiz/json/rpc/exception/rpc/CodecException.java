package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: CodecException <br>
 * Function: 编解码异常
 *
 * @author wangxujin
 */
public class CodecException extends RpcException {

    private static final long serialVersionUID = 2964995700650720059L;

    public CodecException() {
        super();
    }

    public CodecException(String s) {
        super(s);
    }

    public CodecException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CodecException(Throwable throwable) {
        super(throwable);
    }
}
