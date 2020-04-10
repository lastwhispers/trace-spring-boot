package com.example.demo.core.service;

import com.example.demo.core.dao.TeacherDao;
import com.example.demo.core.dao.UserDao;
import com.example.demo.core.model.Teacher;
import com.example.demo.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lastwhisper
 * @date 2019/12/11
 */
@Service(value = "teacherService")
public class TeacherService {

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private UserDao userDao;

    public Teacher findTeacherByTeacherService(String id, String name) {
        userDao.findUserByUserDao(new User(id, name));
        Teacher teacher;
        teacher = teacherDao.findTeacherByTeacherDao(id, name);
        return teacher;
    }

    public Teacher findTeacherByTeacherServiceException(String id, String name) {
        userDao.findUserByUserDao(new User(id, name));
        Teacher teacher = new Teacher();
        try {
            teacher = teacherDao.findTeacherByTeacherDaoException(id, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacher;
    }

}
