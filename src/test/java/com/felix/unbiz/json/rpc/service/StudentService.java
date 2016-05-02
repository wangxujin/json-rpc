package com.felix.unbiz.json.rpc.service;

import com.felix.unbiz.json.rpc.service.bo.DataPage;
import com.felix.unbiz.json.rpc.service.bo.QueryCriteria;
import com.felix.unbiz.json.rpc.service.bo.Student;

/**
 * ClassName: StudentService <br>
 * Function: StudentService
 *
 * @author wangxujin
 */
public interface StudentService {

    DataPage<Student> getStudentByPage(QueryCriteria criteria);
}
