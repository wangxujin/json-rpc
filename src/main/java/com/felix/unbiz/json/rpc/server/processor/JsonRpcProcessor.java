package com.felix.unbiz.json.rpc.server.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import com.felix.unbiz.json.rpc.exception.rpc.CodecException;
import com.felix.unbiz.json.rpc.exception.rpc.ExceptionHandler;
import com.felix.unbiz.json.rpc.exception.rpc.InvalidParamException;
import com.felix.unbiz.json.rpc.exception.rpc.MethodNotFoundException;
import com.felix.unbiz.json.rpc.exception.rpc.RpcException;
import com.felix.unbiz.json.rpc.protocol.Serializer;
import com.felix.unbiz.json.rpc.protocol.SerializerFactory;
import com.felix.unbiz.json.rpc.server.bo.RpcRequest;
import com.felix.unbiz.json.rpc.server.bo.RpcResponse;
import com.felix.unbiz.json.rpc.server.callback.Callback;
import com.felix.unbiz.json.rpc.server.context.RpcContext;
import com.felix.unbiz.json.rpc.util.MethodUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * ClassName: JsonRpcProcessor <br>
 * Function: json-rpc 处理请求，返回响应类
 *
 * @author wangxujin
 */
public class JsonRpcProcessor implements RpcProcessor {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping()
            .serializeSpecialFloatingPointValues().create();

    @Override
    public void process(RpcRequest request, Callback<RpcResponse> callback) {
        Serializer serializer = SerializerFactory.getSerializerByProtocol(RpcContext.getContext().getProtocol());
        JsonElement responseJson = null;
        try {
            JsonElement requestJson = serializer.deserialize(RpcContext.getContext().getEncoding(),
                    request.getRequest());
            RpcContext.getContext().setRequest(requestJson);

            responseJson = execute(request, requestJson);
        } catch (CodecException ex) {
            logger.error(ex.getMessage(), ex);
            responseJson = makeResponse(null, null, ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            responseJson = makeResponse(null, null, new RpcException(ex));
        } finally {
            RpcContext.getContext().setResponse(responseJson);
            byte[] response = serializer.serialize(RpcContext.getContext().getEncoding(), responseJson);
            callback.handleResult(new RpcResponse(response));
        }
    }

    /**
     * 解析请求数据，执行调用方法，组装响应数据
     *
     * @return
     */
    protected JsonElement execute(RpcRequest request, JsonElement requestJson) {
        try {
            JsonObject json = requestJson.getAsJsonObject();
            if (!json.get("jsonrpc").getAsString().equals("2.0")) {
                return makeResponse(null, null, new InvalidParamException("jsonrpc is not 2.0"));
            }
            String methodName = json.get("method").getAsString();
            JsonArray params = json.get("params").getAsJsonArray();
            JsonElement id = json.get("id");

            JsonElement result = invoke(request.getExporter().getServiceInterface(), request.getExporter()
                    .getServiceBean(), methodName, params);
            return makeResponse(id, result, null);
        } catch (MethodNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return makeResponse(null, null, ex);
        } catch (InvalidParamException ex) {
            logger.error(ex.getMessage(), ex);
            return makeResponse(null, null, ex);
        } catch (RpcException ex) {
            logger.error(ex.getMessage(), ex);
            return makeResponse(null, null, ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return makeResponse(null, null, new RpcException(ex));
        }
    }

    /**
     * 执行调用方法
     *
     * @param serviceInterface
     * @param bean
     * @param methodName
     * @param args
     *
     * @return
     */
    protected JsonElement invoke(Class<?> serviceInterface, Object bean, String methodName, JsonArray args) {
        Method method = MethodUtils.findMethod(serviceInterface, methodName, args);
        if (method == null) {
            throw new MethodNotFoundException(
                    methodName + " not found in target interface " + serviceInterface.getSimpleName());
        }
        Type[] type = method.getGenericParameterTypes();
        if (type.length != args.size()) {
            throw new InvalidParamException("type.length is not equal args.length");
        }
        Object[] params = new Object[args.size()];
        Iterator<JsonElement> e = args.iterator();
        for (int i = 0; i < args.size(); i++) {
            params[i] = gson.fromJson(e.next(), type[i]);
        }
        FastMethod fastMethod = FastClass.create(bean.getClass()).getMethod(method);
        try {
            Object object = fastMethod.invoke(bean, params);
            return gson.toJsonTree(object, method.getGenericReturnType());
        } catch (InvocationTargetException ex) {
            throw new RpcException(ex);
        }
    }

    /**
     * 组装响应数据
     *
     * @param id
     * @param result
     * @param ex
     *
     * @return
     */
    protected JsonElement makeResponse(JsonElement id, JsonElement result, RpcException ex) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jsonrpc", "2.0");
        if (id != null && result != null && ex == null) {
            jsonObject.add("id", id);
            jsonObject.add("result", result);
        } else if (id == null && result == null && ex != null) {
            jsonObject.add("id", new JsonNull());
            jsonObject.add("error", ExceptionHandler.serialize(ex));
        }
        return jsonObject;
    }
}
