package com.felix.unbiz.json.rpc.service.impl;

import org.springframework.stereotype.Service;

import com.felix.unbiz.json.rpc.server.annotation.JsonRpcService;
import com.felix.unbiz.json.rpc.service.HeyService;

/**
 * ClassName: HeyServiceImpl <br>
 * Function: HeyService实现类
 *
 * @author wangxujin
 */

@Service("heyService")
@JsonRpcService(serviceInterface = HeyService.class)
public class HeyServiceImpl implements HeyService {

    @Override
    public String say(String word) {
        return "hello," + word;
    }
}
