package com.example.demo.core.dao;

import com.example.demo.core.model.Teacher;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 *
 * @author lastwhisper
 * @date 2020/1/29
 */
@Component
public class TeacherDao {

    public Teacher findTeacherByTeacherDao(String id, String name){
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Teacher(id, name);
    }

    public Teacher findTeacherByTeacherDaoException(String id, String name) {
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int a = 1/0;
        return new Teacher(id, name);
    }

}
