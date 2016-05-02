package com.felix.unbiz.json.rpc;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.felix.unbiz.json.rpc.constant.JsonCommonConstant;
import com.felix.unbiz.json.rpc.util.ByteUtils;

/**
 * ClassName: BaseTest <br>
 * Function: 测试基础类
 *
 * @author wangxujin
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BaseTest extends AbstractJUnit4SpringContextTests {

    protected static JettyServer server;

    @Before
    public void setUp() {
        server = new JettyServer();
        server.start(8089);
    }

    @Test
    public void test() {

    }

    @After
    public void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    public String getContent(String path) throws Exception {
        HttpURLConnection httpconnection = (HttpURLConnection) new URL(path).openConnection();
        httpconnection.setRequestMethod("GET");
        httpconnection.setDoInput(true);
        httpconnection.setDoOutput(true);
        httpconnection.setRequestProperty("Content-Type", "text/html;charset="
                + JsonCommonConstant.DEFAULT_ENCODING);
        httpconnection.connect();
        InputStream in = httpconnection.getInputStream();
        int len = httpconnection.getContentLength();
        byte[] resBytes = ByteUtils.readStream(in, len);
        String content = new String(resBytes, JsonCommonConstant.DEFAULT_ENCODING);
        in.close();
        httpconnection.disconnect();
        return content;
    }

}
