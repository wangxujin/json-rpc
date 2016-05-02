package com.felix.unbiz.json.rpc.exception.rpc;

import com.felix.unbiz.json.rpc.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * ClassName: ExceptionHandler <br>
 * Function: 异常序列化、反序列化类
 *
 * @author wangxujin
 */
public class ExceptionHandler {

    private ExceptionHandler() {

    }

    /**
     * 反序列化异常信息
     *
     * @param json 异常对象
     *
     * @return
     */
    public static RpcException deserialize(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        //        int code = obj.get("code").getAsInt();
        String cause = "";
        if (obj.get("data").isJsonPrimitive()) {
            cause = obj.get("data").getAsString();
        }
        String message = "";
        if (obj.get("message").isJsonPrimitive()) {
            message = obj.get("message").getAsString();
        }
        return new RpcException(message, new Exception(cause));
    }

    /**
     * 序列化异常信息
     *
     * @param ex 异常类
     *
     * @return
     */
    public static JsonElement serialize(RpcException ex) {
        JsonObject obj = new JsonObject();
        int code;
        if (ex instanceof CodecException) {
            code = ErrorStatus.CODEC_ERROR.getCode();
        } else if (ex instanceof InvalidParamException) {
            code = ErrorStatus.INVALID_PARAM.getCode();
        } else if (ex instanceof InvalidRequestException) {
            code = ErrorStatus.INVALID_REQUEST.getCode();
        } else if (ex instanceof MethodNotFoundException) {
            code = ErrorStatus.METHOD_NOT_FOUND.getCode();
        } else if (ex instanceof ServerErrorException) {
            code = ErrorStatus.SERVER_ERROR.getCode();
        } else {
            code = ErrorStatus.INTERNAL_ERROR.getCode();
        }
        obj.add("code", new JsonPrimitive(code));
        obj.add("message", new JsonPrimitive(ex.getClass().getSimpleName()));
        obj.add("data", StringUtils.isEmpty(ex.getMessage()) ? new JsonNull() : new JsonPrimitive(ex.getMessage()));
        return obj;
    }

}
