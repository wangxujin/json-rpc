package com.felix.unbiz.json.rpc.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.felix.unbiz.json.rpc.constant.HttpStatus;
import com.felix.unbiz.json.rpc.constant.JsonCommonConstant;
import com.felix.unbiz.json.rpc.exception.InvalidProtocolException;
import com.felix.unbiz.json.rpc.exception.ServiceNotFoundException;
import com.felix.unbiz.json.rpc.exception.rpc.CodecException;
import com.felix.unbiz.json.rpc.exception.rpc.InvalidRequestException;
import com.felix.unbiz.json.rpc.server.bo.RpcRequest;
import com.felix.unbiz.json.rpc.server.bo.RpcResponse;
import com.felix.unbiz.json.rpc.server.callback.CallFuture;
import com.felix.unbiz.json.rpc.server.context.RpcContext;
import com.felix.unbiz.json.rpc.server.log.LoggerBuilder;
import com.felix.unbiz.json.rpc.server.processor.JsonRpcProcessor;
import com.felix.unbiz.json.rpc.util.ByteUtils;
import com.felix.unbiz.json.rpc.util.IPUtils;

/**
 * ClassName: JsonRpcServlet <br>
 * Function: json rpc servlet
 *
 * @author wangxujin
 */
public class JsonRpcServlet extends BaseRpcServlet {

    private static final long serialVersionUID = 6950437995441162890L;

    private static final Logger LOG = LoggerFactory.getLogger(JsonRpcServlet.class);

    public JsonRpcServlet() {
        processor = LoggerBuilder.buildLoggerProcessor(new JsonRpcProcessor());
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            initJsonRpcExporter(config.getServletContext());
            LOG.info("Please visit http://" + IPUtils.getLocalHostAddress() + ":${port}/"
                    + JsonCommonConstant.TRANSPORT_URL_BASE_PATH + " for details");
        } catch (Exception ex) {
            LOG.error("Initialize rpc bean failed, " + ex.getMessage(), ex);
        }
    }

    /**
     * 显示管理页面
     *
     * @param request
     * @param response
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showHtmlPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CallFuture<RpcResponse> callFuture = new CallFuture<RpcResponse>();

        try {
            JsonRpcExporter exporter = getRpcExporter(request);
            String protocol = getProtocolByHttpContentType(request);
            RpcContext.getContext().setStartTime().setProtocol(protocol).setFromIp(IPUtils.getIpAddr(request))
                    .setServiceName(exporter.getName()).setEncoding(request.getCharacterEncoding());
            byte[] byteArray = ByteUtils.readStream(request.getInputStream(),
                    request.getContentLength());

            RpcRequest rpcRequest = new RpcRequest(exporter, byteArray);

            processor.process(rpcRequest, callFuture);
        } catch (InvalidRequestException e) {
            callFuture.cancel(true);
            LOG.error(e.getMessage());
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
        } catch (InvalidProtocolException e) {
            callFuture.cancel(true);
            LOG.error(e.getMessage());
            response.setStatus(HttpStatus.SC_NOT_ACCEPTABLE);
        } catch (ServiceNotFoundException e) {
            callFuture.cancel(true);
            LOG.error(e.getMessage());
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        } catch (CodecException e) {
            callFuture.cancel(true);
            LOG.error(e.getMessage());
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            callFuture.cancel(true);
        } catch (Exception e) {
            callFuture.cancel(true);
            LOG.error(e.getMessage(), e);
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                buildHttpResponse(response, callFuture.get(), request.getContentType(),
                        request.getCharacterEncoding());
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
            RpcContext.removeContext();
        }
    }
}
