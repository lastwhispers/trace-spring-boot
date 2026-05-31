package com.example.demo.core.controller;

import com.example.demo.common.Convert;
import com.example.demo.core.model.Teacher;
import com.example.demo.core.model.User;
import com.example.demo.core.service.TeacherService;
import com.example.demo.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 综合型测试Controller，用于测试trace-spring-boot监控功能
 *
 * @author lastwhispers
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private Convert convert;

    /**
     * 最简单的端点，测试基础追踪
     */
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        log.info("hello endpoint called");
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Hello, Trace!");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 循环调用userService，测试循环调用链追踪
     */
    @GetMapping("/user/list")
    public Map<String, Object> userList() {
        log.info("user/list endpoint called");
        List<Map<String, Object>> users = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User user = userService.findUserByUserService(new User(String.valueOf(i), "user" + i));
            Map<String, Object> item = new HashMap<>();
            item.put("index", i);
            item.put("user", user);
            users.add(item);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", users.size());
        result.put("data", users);
        return result;
    }

    /**
     * 使用PathVariable获取用户详情
     */
    @GetMapping("/user/detail/{id}")
    public Map<String, Object> userDetail(@PathVariable String id) {
        log.info("user/detail endpoint called with id={}", id);
        User user = userService.findUserByUserService(new User(id, "detail-user-" + id));
        Teacher teacher = teacherService.findTeacherByTeacherService(id, "teacher-" + id);
        return convert.getIncludeMap(user, teacher);
    }

    /**
     * POST创建用户，测试POST方法追踪
     */
    @PostMapping("/user")
    public Map<String, Object> createUser(@RequestParam String id, @RequestParam String name) {
        log.info("POST user endpoint called: id={}, name={}", id, name);
        User user = userService.findUserByUserService(new User(id, name));
        Map<String, Object> result = new HashMap<>();
        result.put("action", "create");
        result.put("user", user);
        return result;
    }

    /**
     * PUT更新用户，测试PUT方法追踪
     */
    @PutMapping("/user/{id}")
    public Map<String, Object> updateUser(@PathVariable String id, @RequestParam String name) {
        log.info("PUT user endpoint called: id={}, name={}", id, name);
        User user = userService.findUserByUserService(new User(id, name + "-updated"));
        Map<String, Object> result = new HashMap<>();
        result.put("action", "update");
        result.put("user", user);
        return result;
    }

    /**
     * DELETE删除用户，测试DELETE方法追踪
     */
    @DeleteMapping("/user/{id}")
    public Map<String, Object> deleteUser(@PathVariable String id) {
        log.info("DELETE user endpoint called: id={}", id);
        Map<String, Object> result = new HashMap<>();
        result.put("action", "delete");
        result.put("deletedId", id);
        result.put("success", true);
        return result;
    }

    /**
     * 多层嵌套调用，测试深度调用链追踪
     * Controller -> UserService -> TeacherService -> UserDao + TeacherDao
     */
    @GetMapping("/teacher/nested")
    public Map<String, Object> teacherNested(@RequestParam(required = false, defaultValue = "1") String id,
                                              @RequestParam(required = false, defaultValue = "nested") String name) {
        log.info("teacher/nested endpoint called");
        User user = userService.findUserByUserService(new User(id, name));
        Teacher teacher = teacherService.findTeacherByTeacherService(id, name + "-nested");
        Map<String, Object> result = new HashMap<>();
        result.put("type", "nested");
        result.put("user", user);
        result.put("teacher", teacher);
        return result;
    }

    /**
     * 调用Convert构建混合数据，测试@Include/@Exclude追踪
     */
    @GetMapping("/convert/data")
    public Map<String, Object> convertData(@RequestParam(required = false, defaultValue = "1") String id) {
        log.info("convert/data endpoint called");
        User user = userService.findUserByUserService(new User(id, "convert-user"));
        Teacher teacher = teacherService.findTeacherByTeacherService(id, "convert-teacher");
        return convert.getIncludeMap(user, teacher);
    }

    /**
     * 连续触发多种异常，测试异常追踪
     */
    @GetMapping("/exception/multi")
    public Map<String, Object> exceptionMulti(@RequestParam(required = false, defaultValue = "1") String id) {
        log.info("exception/multi endpoint called");
        List<Map<String, Object>> exceptions = new ArrayList<>();

        // ArithmeticException
        try {
            teacherService.findTeacherByTeacherServiceException(id, "arith");
        } catch (ArithmeticException e) {
            Map<String, Object> ex = new HashMap<>();
            ex.put("type", "ArithmeticException");
            ex.put("message", e.getMessage());
            exceptions.add(ex);
        }

        // NullPointerException
        try {
            throw new NullPointerException("test null pointer");
        } catch (NullPointerException e) {
            Map<String, Object> ex = new HashMap<>();
            ex.put("type", "NullPointerException");
            ex.put("message", e.getMessage());
            exceptions.add(ex);
        }

        // IllegalArgumentException
        try {
            throw new IllegalArgumentException("test illegal argument");
        } catch (IllegalArgumentException e) {
            Map<String, Object> ex = new HashMap<>();
            ex.put("type", "IllegalArgumentException");
            ex.put("message", e.getMessage());
            exceptions.add(ex);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("action", "exception-multi");
        result.put("exceptions", exceptions);
        result.put("count", exceptions.size());
        return result;
    }

    /**
     * 模拟慢查询（sleep 2秒），测试慢请求追踪
     */
    @GetMapping("/slow-query")
    public Map<String, Object> slowQuery(@RequestParam(required = false, defaultValue = "1") String id) {
        log.info("slow-query endpoint called");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        User user = userService.findUserByUserService(new User(id, "slow-user"));
        Map<String, Object> result = new HashMap<>();
        result.put("type", "slow-query");
        result.put("user", user);
        return result;
    }

    /**
     * 批量混合调用：循环调用user/teacher/exception各2次
     */
    @GetMapping("/batch/mixed")
    public Map<String, Object> batchMixed(@RequestParam(required = false, defaultValue = "1") String id) {
        log.info("batch/mixed endpoint called");
        List<String> results = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            // user call
            User user = userService.findUserByUserService(new User(id, "mixed-user-" + i));
            results.add("user-" + i + "-" + (user != null ? "ok" : "null"));

            // teacher call
            Teacher teacher = teacherService.findTeacherByTeacherService(id, "mixed-teacher-" + i);
            results.add("teacher-" + i + "-" + (teacher != null ? "ok" : "null"));

            // exception call
            try {
                teacherService.findTeacherByTeacherServiceException(id, "mixed-exception-" + i);
            } catch (Exception e) {
                results.add("exception-" + i + "-" + e.getClass().getSimpleName());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("action", "batch-mixed");
        result.put("results", results);
        result.put("total", results.size());
        return result;
    }

    /**
     * 多个PathVariable，测试多路径变量追踪
     */
    @GetMapping("/param/{id}/user/{name}")
    public Map<String, Object> multiParam(@PathVariable String id, @PathVariable String name) {
        log.info("param/{id}/user/{name} endpoint called: id={}, name={}", id, name);
        User user = userService.findUserByUserService(new User(id, name));
        Teacher teacher = teacherService.findTeacherByTeacherService(id, name);
        return convert.getIncludeMap(user, teacher);
    }
}
