package com.felix.unbiz.json.rpc.exception;

/**
 * ClassName: ServiceNotFoundException <br>
 * Function: 服务找不到异常类
 *
 * @author wangxujin
 */
public class ServiceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5330684328867284022L;

    public ServiceNotFoundException() {
        super();
    }

    public ServiceNotFoundException(String s) {
        super(s);
    }

    public ServiceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServiceNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
