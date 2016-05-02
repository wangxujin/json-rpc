package com.felix.unbiz.json.rpc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.felix.unbiz.json.rpc.server.annotation.JsonRpcService;
import com.felix.unbiz.json.rpc.service.StudentService;
import com.felix.unbiz.json.rpc.service.bo.DataPage;
import com.felix.unbiz.json.rpc.service.bo.QueryCriteria;
import com.felix.unbiz.json.rpc.service.bo.Student;

/**
 * ClassName: StudentServiceImpl <br>
 * Function: StudentServiceImpl
 *
 * @author wangxujin
 */

@Service("studentService")
@JsonRpcService(serviceInterface = StudentService.class)
public class StudentServiceImpl implements StudentService {

    @Override
    public DataPage<Student> getStudentByPage(QueryCriteria criteria) {
        Student student = new Student();
        student.setNo(123);
        student.setName("wangxujin");

        Student student1 = new Student();
        student1.setNo(234);
        student1.setName("xichuan");

        List<Student> students = new ArrayList<Student>();
        students.add(student);
        students.add(student1);
        return new DataPage<Student>(students, 2, 10, 1);
    }
}
