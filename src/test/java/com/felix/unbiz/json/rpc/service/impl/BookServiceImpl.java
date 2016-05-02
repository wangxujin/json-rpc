package com.felix.unbiz.json.rpc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.felix.unbiz.json.rpc.service.BookService;

/**
 * ClassName: BookServiceImpl <br>
 * Function: BookService实现类
 *
 * @author wangxujin
 */
public class BookServiceImpl implements BookService, InitializingBean {

    private Map<Integer, String> map = new HashMap<Integer, String>();

    @Override
    public List<String> getBooksById(List<Integer> ids) {
        List<String> books = new ArrayList<String>(ids.size());
        for (int id : ids) {
            books.add(map.get(id));
        }
        return books;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        map.put(1, "javascript入门");
        map.put(2, "java编程思想");
        map.put(3, "spring in action");
    }
}
