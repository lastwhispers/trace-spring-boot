package com.example.demo.util;

import com.example.demo.model.Teacher;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lastwhisper
 * @date 2020/2/14
 */
@Component
public class ConvertUtil {

    //public static Map<String, Object> getMap(User user, Teacher teacher) {
    //    Map<String, Object> map = new HashMap<>();
    //    map.put("user", user);
    //    map.put("teacher", teacher);
    //    return map;
    //}

    public Map<String, Object> getMap(User user, Teacher teacher) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("teacher", teacher);
        return map;
    }
}
