package com.felix.unbiz.json.rpc.client;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Test;

import com.felix.unbiz.json.rpc.BaseTest;
import com.felix.unbiz.json.rpc.service.BookService;

/**
 * ClassName: BookServiceClientTest <br>
 * Function: BookServiceClientTest
 *
 * @author wangxujin
 */
public class BookServiceClientTest extends BaseTest {

    @Resource
    private BookService bookServiceProxy;

    @Test
    public void getBooksByIdTest() throws Exception {
        bookServiceProxy.getBooksById(Arrays.asList(1, 2));
    }

}
