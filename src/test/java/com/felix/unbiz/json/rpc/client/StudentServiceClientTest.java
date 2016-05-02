package com.felix.unbiz.json.rpc.client;

import javax.annotation.Resource;

import org.junit.Test;

import com.felix.unbiz.json.rpc.BaseTest;
import com.felix.unbiz.json.rpc.service.StudentService;
import com.felix.unbiz.json.rpc.service.bo.DataPage;
import com.felix.unbiz.json.rpc.service.bo.QueryCriteria;
import com.felix.unbiz.json.rpc.service.bo.Student;

/**
 * ClassName: StudentServiceClientTest <br>
 * Function: StudentServiceClientTest
 *
 * @author wangxujin
 */
public class StudentServiceClientTest extends BaseTest {

    @Resource
    private StudentService studentServiceProxy;

    @Test
    public void testPage() throws Exception {
        DataPage<Student> page = studentServiceProxy.getStudentByPage(new QueryCriteria());
        System.out.println(page);
    }
}
