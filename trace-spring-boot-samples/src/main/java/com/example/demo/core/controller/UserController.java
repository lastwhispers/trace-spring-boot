package com.example.demo.core.controller;

import com.example.demo.core.model.Teacher;
import com.example.demo.core.model.User;
import com.example.demo.core.service.TeacherService;
import com.example.demo.core.service.UserService;
import com.example.demo.common.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lastwhisper
 * @date 12/22/2019
 */
@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private Convert convert;

    @GetMapping("/user")
    @ResponseBody
    public Map<String, Object> getUserByController(String id, String name) {
        User user = userService.findUserByUserService(new User(id, name));
        Teacher teacher = null;
        teacher = teacherService.findTeacherByTeacherService(id, name);
        return convert.getIncludeMap(user, teacher);
    }

    @GetMapping("/userException")
    @ResponseBody
    public Map<String, Object> getUserByControllerException(String id, String name) {
        User user = userService.findUserByUserService(new User(id, name));
        Teacher teacher = null;
        try {
            teacher = teacherService.findTeacherByTeacherServiceException(id, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convert.getIncludeMap(user, teacher);
    }

    @GetMapping("/fork")
    @ResponseBody
    public Map<String, Object> fork(String id, String name,Integer num) {
        Map<String, Object> map = new HashMap<>();
        if(num%2==0){
            User user = userService.findUserByUserService(new User(id, name));
            map.put("user",user);
        }else {
            Teacher teacher = teacherService.findTeacherByTeacherService(id, name);
            map.put("teacher",teacher);
        }
        return map;
    }

}
