package com.felix.unbiz.json.rpc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.felix.unbiz.json.rpc.client.header.HeaderInfo;
import com.felix.unbiz.json.rpc.constant.JsonCommonConstant;
import com.felix.unbiz.json.rpc.constant.JsonRpcClientConstant;
import com.felix.unbiz.json.rpc.exception.rpc.CodecException;
import com.felix.unbiz.json.rpc.exception.rpc.ExceptionHandler;
import com.felix.unbiz.json.rpc.exception.rpc.RpcException;
import com.felix.unbiz.json.rpc.protocol.Serializer;
import com.felix.unbiz.json.rpc.util.ByteUtils;
import com.felix.unbiz.json.rpc.util.StringPool;
import com.felix.unbiz.json.rpc.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * ClassName: SimpleRpcClient <br>
 * Function: 简单的json rpc client
 *
 * @author wangxujin
 */
public class SimpleRpcClient implements RpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleRpcClient.class);

    private static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping()
            .serializeSpecialFloatingPointValues().create();

    private static final AtomicInteger COUNTER = new AtomicInteger();

    /**
     * 初始化HTTP Persistent Connections参数
     */
    static {
        System.setProperty("http.keepAlive", JsonRpcClientConstant.DEFAULT_HTTP_KEEPALIVE);
        System.setProperty("http.maxConnections", JsonRpcClientConstant.DEFAULT_MAX_CONNECTIONS);
    }

    /**
     * 远程服务端地址
     */
    private String url;

    /**
     * 编码方式
     */
    private String encoding;

    /**
     * 连接超时，单位毫秒
     */
    private int connectTimeout = JsonRpcClientConstant.DEFAULT_CLIENT_CONN_TIMEOUT;

    /**
     * 读超时，单位毫秒
     */
    private int readTimeout = JsonRpcClientConstant.DEFAULT_CLIENT_READ_TIMEOUT;

    /**
     * 序列化反序列化编码处理serializer
     */
    private Serializer serializer;

    /**
     * Creates a new instance of SimpleRpcClient.
     */
    public SimpleRpcClient() {
    }

    /**
     * Creates a new instance of SimpleRpcClient.
     */
    public SimpleRpcClient(String url, String encoding, int connectTimeout, int readTimeout,
                           Serializer serializer) {
        this.url = url;
        this.encoding = encoding;
        if (StringUtils.isEmpty(encoding)) {
            this.encoding = JsonCommonConstant.DEFAULT_ENCODING;
        }
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.serializer = serializer;
    }

    /**
     * Creates a new instance of SimpleRpcClient.
     */
    public SimpleRpcClient(String url, Serializer serializer) {
        this(url, JsonCommonConstant.DEFAULT_ENCODING, JsonRpcClientConstant.DEFAULT_CLIENT_CONN_TIMEOUT,
                JsonRpcClientConstant.DEFAULT_CLIENT_READ_TIMEOUT, serializer);
    }

    @Override
    public Object transport(Method method, Object[] args) throws RpcException {
        return transport(method.getName(), args, method.getGenericReturnType(), null);
    }

    @Override
    public Object transport(Method method, Object[] args, HeaderInfo headerInfo) throws RpcException {
        return transport(method.getName(), args, method.getGenericReturnType(), headerInfo);
    }

    /**
     * 调用发起远程通信
     *
     * @param methodName 方法名
     * @param args       方法参数
     * @param returnType 方法返回类型
     * @param headerInfo 头部认证信息
     *
     * @return
     *
     * @throws RpcException
     */
    private Object transport(String methodName, Object[] args, Type returnType, HeaderInfo headerInfo)
            throws RpcException {
        long start = System.currentTimeMillis();
        try {
            int id = COUNTER.getAndIncrement();
            JsonElement request = makeRequest(id, methodName, args);
            byte[] reqBytes = serializer.serialize(encoding, request);
            preLog(id, methodName, request, headerInfo.getHeaderMap());

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            sendRequest(reqBytes, headerInfo, connection);
            byte[] resBytes = readResponse(connection);

            JsonElement resJson = serializer.deserialize(encoding, resBytes);
            postLog(id, methodName, resJson, start);
            return parseResult(id, resJson, returnType);
        } catch (IOException e) {
            throw new RpcException("Rpc read response from server has IO problems - "
                    + e.getMessage(), e);
        } catch (CodecException e) {
            throw new RpcException("Rpc transport has serialization problems - " + e.getMessage(),
                    e);
        } catch (RpcException e) {
            throw e;
        }

    }

    /**
     * 组装rpc请求
     *
     * @param id
     * @param methodName
     * @param args
     *
     * @return 生成的请求数据
     */
    protected JsonElement makeRequest(int id, String methodName, Object[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jsonrpc", "2.0");
        map.put("method", methodName);
        if (args != null) {
            map.put("params", args);
        } else {
            map.put("params", new Object[0]);
        }
        map.put("id", id);
        return GSON.toJsonTree(map);
    }

    /**
     * 解析响应
     */
    protected Object parseResult(int id, JsonElement ele, Type genericReturnType) throws RpcException {
        JsonObject res = (JsonObject) ele;
        if (!res.get("jsonrpc").getAsString().equals("2.0")) {
            throw new RpcException("jsonrpc is not equals 2.0");
        }
        JsonElement result = res.get("result");
        if (result != null) {
            if (res.get("id").getAsInt() != id) {
                throw new RpcException(String.format("response id is not equal the request id, request id: %s ", id));
            } else {
                return GSON.fromJson(result, genericReturnType);
            }
        } else {
            JsonElement json = res.get("error");
            if (json != null) {
                throw ExceptionHandler.deserialize(json);
            } else {
                throw new RpcException("no error or result returned");
            }
        }
    }

    /**
     * 发送请求字节码
     *
     * @param reqBytes
     * @param connection
     *
     * @throws IOException
     */
    private void sendRequest(byte[] reqBytes, HeaderInfo headerInfo, URLConnection connection) {
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        OutputStream out = null;
        try {
            if (connectTimeout > 0) {
                httpConnection.setConnectTimeout(connectTimeout);
            }
            if (readTimeout > 0) {
                httpConnection.setReadTimeout(readTimeout);
            }
            if (headerInfo != null && headerInfo.getHeaderMap() != null) {
                for (Map.Entry<String, String> entry : headerInfo.getHeaderMap().entrySet()) {
                    httpConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-Type", serializer.getProtocol()
                    .getName() + ";charset=" + encoding);
            httpConnection.setRequestProperty("Content-Length", StringPool.Symbol.EMPTY + reqBytes.length);
            httpConnection.connect();
            out = httpConnection.getOutputStream();
            out.write(reqBytes);
            LOG.debug("Send " + reqBytes.length + " bytes done");
        } catch (Exception e) {
            throw new RpcException("Rpc send request failed - " + e.getMessage() + " on "
                    + connection.getURL(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.error("Close send request connection error");
                }
            }
        }
    }

    /**
     * 读服务端返回数据
     *
     * @param connection
     *
     * @return 字节码
     *
     * @throws RpcException
     */
    private byte[] readResponse(URLConnection connection) throws RpcException {
        byte[] resBytes;
        InputStream in = null;
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        int statusCode = 0;
        try {
            statusCode = httpConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                in = httpConnection.getInputStream();
            } else {
                if (httpConnection.getContentType().equals(serializer.getProtocol().getName())
                        && httpConnection.getErrorStream() != null) {
                    in = httpConnection.getErrorStream();
                } else {
                    in = httpConnection.getInputStream();
                }
            }
            int len = httpConnection.getContentLength();
            if (len <= 0) {
                throw new RpcException("Response data length should not be zero");
            }
            resBytes = ByteUtils.readStream(in, len);
            LOG.debug("Read " + resBytes.length + " bytes response done");
        } catch (IOException e) {
            throw new RpcException("Rpc read response from server has IO problems - "
                    + e.getMessage(), e);
        } catch (Exception e) {
            throw new RpcException("Rpc read response from server has error. statusCode : "
                    + statusCode, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error("Close read response connection error");
                }
            }
        }
        return resBytes;
    }

    /**
     * 执行前打印日志
     *
     * @param id
     * @param methodName
     * @param request
     * @param headerProperties
     */
    private void preLog(int id, String methodName, JsonElement request, Map<String, String> headerProperties) {
        LOG.info("json rpc call start - {}\t{}\t{}\t{}", id, methodName, request,
                headerProperties);
    }

    /**
     * 执行后打印日志
     *
     * @param id
     * @param methodName
     * @param response
     * @param start
     */
    private void postLog(int id, String methodName, JsonElement response, long start) {
        LOG.info("json rpc call end - {}\t{}\t{}\t{}", id, methodName, response,
                (System.currentTimeMillis() - start));
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("[url:");
        sb.append(url);
        sb.append(", encoding:");
        sb.append(encoding);
        sb.append(", connTimeout:");
        sb.append(connectTimeout);
        sb.append(", readTimeout:");
        sb.append(readTimeout);
        sb.append(", protocol:");
        sb.append(serializer.getProtocol().getName());
        sb.append("]");
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
