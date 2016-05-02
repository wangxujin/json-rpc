package com.felix.unbiz.json.rpc;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;

import com.felix.unbiz.json.rpc.server.JsonRpcServlet;

/**
 * ClassName: JettyServer <br>
 * Function: jetty服务器类
 *
 * @author wangxujin
 */
public class JettyServer {

    protected Server server;

    public void start(int port) {
        try {
            server = new Server(port);
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            ContextLoaderListener listener = new ContextLoaderListener();
            context.setInitParameter("contextConfigLocation", "classpath*:/applicationContext.xml");
            context.addEventListener(listener);
            server.setHandler(context);
            context.addServlet(new ServletHolder(new JsonRpcServlet()), "/api/*");
            server.start();
        } catch (Throwable t) {
            System.err.println("Server failed to start. " + t.getMessage());
            throw new RuntimeException(t);
        }
    }

    public void stop() {
        if (server != null) {
            try {
                server.stop();
                server = null;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    public static void main(String[] args) {
        JettyServer server = new JettyServer();
        server.start(8088);
    }
}
