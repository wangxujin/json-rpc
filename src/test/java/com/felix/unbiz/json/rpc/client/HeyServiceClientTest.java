package com.felix.unbiz.json.rpc.client;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.felix.unbiz.json.rpc.BaseTest;
import com.felix.unbiz.json.rpc.service.HeyService;

/**
 * ClassName: HeyServiceClientTest <br>
 * Function: HeyServiceClientTest
 *
 * @author wangxujin
 */
public class HeyServiceClientTest extends BaseTest {

    @Resource
    private HeyService heyServiceProxy;

    @Resource
    private HeyServiceProxy heyServiceProxy1;

    @Test
    public void sayTest() throws Exception {
        System.out.println("=================================");
        System.out.println("=================================" + heyServiceProxy.say("wangxujin"));
    }

    @Test
    public void sayTest1() throws Exception {
        System.out.println("=================================");
        Map<String, String> map = new HashMap<String, String>();
        map.put("syscode", "erqwre123");
        System.out.println("=================================" + heyServiceProxy1.say("wangxujin", map));
    }
}
