package com.example.demo.controller;

import com.example.demo.model.Teacher;
import com.example.demo.model.User;
import com.example.demo.service.TeacherService;
import com.example.demo.service.UserService;
import com.example.demo.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private ConvertUtil convertUtil;

    @GetMapping("/user")
    @ResponseBody
    public Map<String, Object> getUserByController(String id, String name) {
        User user = userService.findUserByUserService(new User(id, name));
        Teacher teacher = null;
        teacher = teacherService.findTeacherByTeacherService(id, name);
        return convertUtil.getMap(user, teacher);
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
        return convertUtil.getMap(user, teacher);
    }

}
