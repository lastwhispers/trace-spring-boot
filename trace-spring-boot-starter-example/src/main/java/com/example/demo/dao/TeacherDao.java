package com.example.demo.dao;

import com.example.demo.model.Teacher;
import org.springframework.stereotype.Component;

/**
 *
 * @author lastwhisper
 * @date 2020/1/29
 */
@Component
public class TeacherDao {

    public Teacher findTeacherByTeacherDao(String id, String name) {
        return new Teacher(id, name);
    }

    public Teacher findTeacherByTeacherDaoException(String id, String name) {
        int a = 1/0;
        return new Teacher(id, name);
    }
}
