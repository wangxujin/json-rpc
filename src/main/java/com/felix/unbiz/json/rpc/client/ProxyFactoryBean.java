package com.felix.unbiz.json.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.felix.unbiz.json.rpc.client.ha.selector.HAFactory;
import com.felix.unbiz.json.rpc.client.ha.selector.JsonFailStrategy;
import com.felix.unbiz.json.rpc.client.ha.selector.JsonLoadBalanceStrategy;
import com.felix.unbiz.json.rpc.client.header.HeaderInfo;
import com.felix.unbiz.json.rpc.constant.JsonCommonConstant;
import com.felix.unbiz.json.rpc.constant.JsonRpcClientConstant;
import com.felix.unbiz.json.rpc.exception.rpc.InvalidParamException;
import com.felix.unbiz.json.rpc.protocol.JsonProtocol;
import com.felix.unbiz.json.rpc.protocol.Serializer;
import com.felix.unbiz.json.rpc.protocol.SerializerFactory;
import com.felix.unbiz.json.rpc.util.ProxyUtils;
import com.felix.unbiz.json.rpc.util.StringUtils;

/**
 * ClassName: ProxyFactoryBean <br>
 * Function: json rpc动态代理类，用于客户端以本地方法调用的方式，发起远程RPC调用，并且返回结果
 *
 * @author wangxujin
 */
public class ProxyFactoryBean implements FactoryBean, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyFactoryBean.class);

    /**
     * 调用接口
     */
    private Class<?> serviceInterface;

    /**
     * 配置的服务器列表，含端口，以逗号分隔 例如"127.0.0.1:8080,2.2.2.2:9999"
     */
    private String servers;

    /**
     * 调用的url
     */
    private String serviceUrl;

    /**
     * 编码
     */
    private String encoding = JsonCommonConstant.DEFAULT_ENCODING;

    /**
     * 连接超时，毫秒数
     */
    private int connectionTimeout = JsonRpcClientConstant.DEFAULT_CLIENT_CONN_TIMEOUT;

    /**
     * 读超时，毫秒数
     */
    private int readTimeout = JsonRpcClientConstant.DEFAULT_CLIENT_READ_TIMEOUT;

    /**
     * 是否有头部认证信息，兼容老版本的json rpc,与headerMap互斥
     */
    private boolean hasHeaders = false;

    /**
     * 如果消息头，消息头对象
     */
    private Map<String, String> headerMap = null;

    /**
     * json rpc 协议
     */
    private JsonProtocol protocol = JsonProtocol.GSON;

    /**
     * 负载均衡策略
     */
    private JsonLoadBalanceStrategy loadBalanceStrategy = JsonLoadBalanceStrategy.RANDOM;

    /**
     * 容错策略
     */
    private JsonFailStrategy failStrategy = JsonFailStrategy.FAILOVER;

    /**
     * Creates a new instance of ProxyFactoryBean.
     */
    public ProxyFactoryBean() {

    }

    /**
     * Creates a new instance of ProxyFactoryBean.
     */
    public ProxyFactoryBean(Class<?> serviceInterface, String servers, String serviceUrl, String encoding,
                            int connectionTimeout, int readTimeout, boolean hasHeaders,
                            Map<String, String> headerMap, JsonProtocol protocol,
                            JsonLoadBalanceStrategy loadBalanceStrategy, JsonFailStrategy failStrategy) {
        this.serviceInterface = serviceInterface;
        this.servers = servers;
        this.serviceUrl = serviceUrl;
        if (StringUtils.isNotEmpty(encoding)) {
            this.encoding = encoding;
        }
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.hasHeaders = hasHeaders;
        this.headerMap = headerMap;
        if (protocol != null) {
            this.protocol = protocol;
        }
        if (loadBalanceStrategy != null) {
            this.loadBalanceStrategy = loadBalanceStrategy;
        }
        if (failStrategy != null) {
            this.failStrategy = failStrategy;
        }
    }

    /**
     * Creates a new instance of ProxyFactoryBean.
     */
    public ProxyFactoryBean(Class<?> serviceInterface, String servers, String serviceUrl,
                            Map<String, String> headerMap) {
        this(serviceInterface, servers, serviceUrl, JsonCommonConstant.DEFAULT_ENCODING,
                JsonRpcClientConstant.DEFAULT_CLIENT_CONN_TIMEOUT, JsonRpcClientConstant.DEFAULT_CLIENT_READ_TIMEOUT,
                false, headerMap, JsonProtocol.GSON, JsonLoadBalanceStrategy.RANDOM, JsonFailStrategy.FAILOVER);
    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] {serviceInterface}, new JsonInvocationProxy());
    }

    /**
     * ClassName: JsonInvocationProxy <br/>
     * Function: 客户端动态代理
     */
    class JsonInvocationProxy implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (ProxyUtils.isHashCode(method)) {
                return Integer.valueOf(System.identityHashCode(proxy));
            }
            if (ProxyUtils.isEqualsMethod(method)) {
                return Boolean.valueOf(proxy == args[0]);
            }
            if (ProxyUtils.isToStringMethod(method)) {
                return toString();
            }

            final Class<?> targetClass;
            Method targetMethod;
            Object[] targetArgs;

            if (hasHeaders) {
                // 如果需要发送消息头则需要定义Proxy，走该分支,兼容老版本json rpc调用方式
                // args中包括至少一个参数，且最后一个参数为Map<String, String>：表示参数传递（如消息头等)
                if (args.length < 1) {
                    LOG.error("if spring configuration hasHeaders is true , {}.{} 's params must not be empty",
                            method.getClass().getName(), method.getName());
                    throw new InvalidParamException("if spring configuration hasHeaders is true, args must not "
                            + "be empty");
                } else {
                    Object lastParameter = args[args.length - 1];
                    if (lastParameter != null && !(lastParameter instanceof Map)) {
                        throw new InvalidParamException("if spring configuration hasHeaders is true,last arg must be "
                                + "type of java.util.Map");
                    } else {
                        headerMap = (Map<String, String>) lastParameter;
                    }
                    args = Arrays.copyOf(args, args.length - 1);
                }
            }

            targetClass = method.getDeclaringClass();
            targetMethod = method;
            targetArgs = args;
            LOG.debug("Proxy bean start to invoke rpc class={}, method={}, args={}",
                    targetClass.getName(), targetMethod, Arrays.toString(targetArgs));

            List<String> serverList = getServerList();
            Serializer serializer = SerializerFactory.getSerializerByProtocol(protocol.getName());
            List<RpcClient> clientList = new ArrayList<RpcClient>(serverList.size());
            for (String server : serverList) {
                final String url = JsonCommonConstant.TRANSPORT_PROTOCOL + server + serviceUrl;
                RpcClient client = new SimpleRpcClient(url, encoding, connectionTimeout, readTimeout, serializer);
                clientList.add(client);
            }

            RpcClient client = new HARpcClient(clientList, HAFactory.build(loadBalanceStrategy, failStrategy));

            return client.transport(targetMethod, targetArgs, new HeaderInfo().setHeaderMap(headerMap));
        }
    }

    /**
     * 服务地址列表
     */
    private List<String> getServerList() {
        String[] split = StringUtils.split(servers, StringUtils.COMMA);
        return Arrays.asList(split);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("create proxy rpc bean " + getClass().getSimpleName() + " for interface: "
                + serviceInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public JsonProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(JsonProtocol protocol) {
        this.protocol = protocol;
    }

    public JsonLoadBalanceStrategy getLoadBalanceStrategy() {
        return loadBalanceStrategy;
    }

    public void setLoadBalanceStrategy(JsonLoadBalanceStrategy loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    public JsonFailStrategy getFailStrategy() {
        return failStrategy;
    }

    public void setFailStrategy(JsonFailStrategy failStrategy) {
        this.failStrategy = failStrategy;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public boolean isHasHeaders() {
        return hasHeaders;
    }

    public void setHasHeaders(boolean hasHeaders) {
        this.hasHeaders = hasHeaders;
    }
}
