package com.felix.unbiz.json.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: HttpClientUtils <br>
 * Function: HttpClient 长连接实现类
 *
 * @author wangxujin
 */
public final class HttpClientUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 最大连接数
     */
    public static final int MAX_TOTAL_CONNECTIONS = 200;
    /**
     * 每个路由最大连接数
     */
    public static final int MAX_ROUTE_CONNECTIONS = 20;
    //
    //    private static HttpClient httpClient;
    //
    //    static {
    //        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    //        cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    //        cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
    //        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    //    }
    //
    //    private HttpClientUtils() {
    //    }
    //
    //    /**
    //     * 发送POST请求，返回响应
    //     *
    //     * @param url         url链接
    //     * @param reqByte     请求字节数组
    //     * @param timeout     连接超时时间
    //     * @param readTimeOut 请求超时时间
    //     * @param headerMap   头部认证信息
    //     * @param protocol    协议类型
    //     * @param encoding    编码信息
    //     *
    //     * @return
    //     */
    //    public static byte[] postRequest(String url, byte[] reqByte, int timeout, int readTimeOut, Map<String, String>
    //            headerMap, String protocol, String encoding) {
    //
    //        HttpPost httpPost = new HttpPost(url);
    //        httpPost.setHeader("Content-Type", protocol + ";charset=" + encoding);
    //        RequestConfig requestConfig = RequestConfig.custom()
    //                .setSocketTimeout(timeout)
    //                .setConnectTimeout(timeout)
    //                .setConnectionRequestTimeout(readTimeOut)
    //                .setExpectContinueEnabled(false).build();
    //        httpPost.setConfig(requestConfig);
    //
    //        if (headerMap != null && !headerMap.isEmpty()) {
    //            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
    //                httpPost.setHeader(entry.getKey(), entry.getValue());
    //            }
    //        }
    //        httpPost.setEntity(new ByteArrayEntity(reqByte));
    //        LOG.info(String.format("[HttpUtils Post] begin invoke url: %s , header : %s ,reqByte's length : %s", url,
    //                headerMap, reqByte.length));
    //        try {
    //            HttpResponse response = httpClient.execute(httpPost);
    //            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    //                HttpEntity entity = response.getEntity();
    //                return EntityUtils.toByteArray(entity);
    //            } else {
    //                throw new RpcException("Invocation failed " + url + ", HTTP status="
    //                        + response.getStatusLine().getStatusCode());
    //            }
    //        } catch (IOException e) {
    //            throw new RpcException("Rpc read response from server has IO problems - "
    //                    + e.getMessage(), e);
    //        }
    //    }

}
