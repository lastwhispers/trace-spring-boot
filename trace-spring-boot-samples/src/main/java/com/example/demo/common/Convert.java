package com.example.demo.common;

import cn.lastwhisper.trace.aspect.annotation.Exclude;
import cn.lastwhisper.trace.aspect.annotation.Include;
import com.example.demo.core.model.Teacher;
import com.example.demo.core.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lastwhisper
 * @date 2020/2/14
 */
@Include
@Component
public class Convert {

    /**
     * 被包含方法
     */
    public Map<String, Object> getIncludeMap(User user, Teacher teacher) {
        return getExcludeMap(user, teacher);
    }

    /**
     * 被排除方法
     */
    @Exclude
    public Map<String, Object> getExcludeMap(User user, Teacher teacher) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("teacher", teacher);
        return map;
    }
}
