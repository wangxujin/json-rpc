package com.felix.unbiz.json.rpc.service;

import java.util.List;

/**
 * ClassName: BookService <br>
 * Function: BookService
 *
 * @author wangxujin
 */
public interface BookService {

    List<String> getBooksById(List<Integer> ids);
}
