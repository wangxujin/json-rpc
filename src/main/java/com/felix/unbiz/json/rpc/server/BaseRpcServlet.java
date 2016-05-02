package com.felix.unbiz.json.rpc.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.felix.unbiz.json.rpc.constant.JsonCommonConstant;
import com.felix.unbiz.json.rpc.exception.ServiceNotFoundException;
import com.felix.unbiz.json.rpc.exception.rpc.InvalidRequestException;
import com.felix.unbiz.json.rpc.server.annotation.JsonRpcService;
import com.felix.unbiz.json.rpc.server.bo.RpcResponse;
import com.felix.unbiz.json.rpc.server.processor.RpcProcessor;
import com.felix.unbiz.json.rpc.util.ClassHtmlUtils;
import com.felix.unbiz.json.rpc.util.StringUtils;

/**
 * ClassName: BaseRpcServlet <br>
 * Function: 基础的 rpc servlet
 *
 * @author wangxujin
 */
public class BaseRpcServlet extends HttpServlet implements ApplicationContextAware {

    private static final long serialVersionUID = -6097810569535547884L;

    private static final Logger LOG = LoggerFactory.getLogger(BaseRpcServlet.class);

    protected Map<String, JsonRpcExporter> exporterMap = new ConcurrentHashMap<String, JsonRpcExporter>();

    protected RpcProcessor processor;

    protected void initJsonRpcExporter(ServletContext servletContext) {

        Enumeration<String> enumeration = servletContext.getAttributeNames();
        while (enumeration.hasMoreElements()) {

            String attribute = enumeration.nextElement();

            Object object = servletContext.getAttribute(attribute);
            if (object instanceof WebApplicationContext) {

                ApplicationContext applicationContext =
                        WebApplicationContextUtils.getWebApplicationContext(servletContext, attribute);

                Map<String, JsonRpcExporter> xmlBeans = applicationContext.getBeansOfType(JsonRpcExporter.class);
                if (xmlBeans == null || xmlBeans.isEmpty()) {
                    System.out.print("");
                } else {
                    for (JsonRpcExporter exporter : xmlBeans.values()) {
                        exporterMap.put(exporter.getName(), exporter);
                    }
                }

                Map<String, Object> annotationBeans = applicationContext.getBeansWithAnnotation(JsonRpcService.class);
                if (annotationBeans == null || annotationBeans.isEmpty()) {
                    System.out.print("");
                } else {
                    for (Object bean : annotationBeans.values()) {
                        JsonRpcService anno = bean.getClass().getAnnotation(JsonRpcService.class);
                        if (anno == null) {
                            Class<?> targetClass = AopUtils.getTargetClass(bean);
                            anno = targetClass.getAnnotation(JsonRpcService.class);
                            if (anno == null) {
                                continue;
                            }
                            if (anno.serviceInterface() == null) {
                                LOG.error("Rpc service interface not configured for " + targetClass.getName());
                                continue;
                            }
                        }
                        JsonRpcExporter exporter = new JsonRpcExporter(anno.serviceInterface().getName(), bean);
                        exporterMap.put(exporter.getName(), exporter);
                    }
                }
            }
        }

        if (exporterMap.size() == 0) {
            LOG.warn("No json rpc service found with XML or annotation configured ");
        }
    }

    /**
     * 从http请求request path中获取JsonRpcExporter，起到路由作用
     *
     * @param httpServletRequest
     *
     * @return
     */
    protected JsonRpcExporter getRpcExporter(HttpServletRequest httpServletRequest) {
        String context = httpServletRequest.getPathInfo();
        if (context == null) {
            throw new InvalidRequestException("Rpc path invalid");
        }
        if (context.length() > 0) {
            context = context.substring(1);
        }
        JsonRpcExporter serviceExporter = exporterMap.get(context);
        if (serviceExporter == null) {
            throw new ServiceNotFoundException("No rpc service found for " + context);
        }
        return serviceExporter;
    }

    /**
     * 获取http请求的request中的content-type，如没有设置则设置为默认的
     *
     * @param httpServletRequest
     *
     * @return
     */
    protected String getProtocolByHttpContentType(HttpServletRequest httpServletRequest) {
        if (StringUtils.isEmpty(httpServletRequest.getContentType())) {
            throw new InvalidRequestException("Rpc protocol invalid");
        }
        String protocol = httpServletRequest.getContentType().split(";")[0];
        if (protocol == null) {
            protocol = JsonCommonConstant.DEFAULT_PROTOCAL_CONTENT_TYPE;
            LOG.debug("Content-type set to default value "
                    + JsonCommonConstant.DEFAULT_PROTOCAL_CONTENT_TYPE);
        } else {
            protocol = protocol.toLowerCase();
        }
        return protocol;
    }

    /**
     * 设置请求结果
     *
     * @param httpServletResponse
     * @param response
     * @param contentType
     * @param encoding
     *
     * @throws IOException
     */
    protected void buildHttpResponse(HttpServletResponse httpServletResponse,
                                     RpcResponse response, String contentType, String encoding) throws IOException {
        httpServletResponse.setContentType(contentType);
        httpServletResponse.setCharacterEncoding(encoding);
        if (response == null || response.getResponse() == null) {
            httpServletResponse.setContentLength(0);
        } else {
            httpServletResponse.setContentLength(response.getResponse().length);
            httpServletResponse.getOutputStream().write(response.getResponse());
        }
        httpServletResponse.flushBuffer();
    }

    /**
     * 显示管理console的html页面
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    protected void showHtmlPage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String context = request.getPathInfo();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (context == null || context.equals("/*") || context.equals("/")) {
            out.println("<h1>Server Summary</h1>");
            out.println("This list all supported services");
            out.println("<table border=\"1\"><tr>" + "<th>Service</th>"
                    + "<th>Interface</th>" + "</tr>");
            for (Map.Entry<String, JsonRpcExporter> entry : exporterMap.entrySet()) {
                out.println("<tr><td>" + entry.getKey() + "</td><td><a href=\""
                        + request.getContextPath() + request.getServletPath() + "/"
                        + entry.getKey() + "\">"
                        + entry.getValue().getServiceInterfaceName()
                        + "</a></td></tr>");
            }
            out.println("</table>");
        } else {
            context = context.substring(1);
            JsonRpcExporter serviceExporter = exporterMap.get(context);
            if (serviceExporter != null) {
                out.println("<h1>Service: " + serviceExporter.getServiceInterfaceName()
                        + "</h1>");
                try {
                    out.println(ClassHtmlUtils.getServiceHtml(serviceExporter
                            .getServiceInterface()));
                    out.println("<br><input type=\"button\" name=\"Submit\" onclick=\"javascript:history.back(-1);\" "
                            + "value=\"Back\">");
                } catch (Exception e) {
                    out.println("e.toString()");
                }
            } else {
                response.setStatus(404);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
